package com.hvngoc.googlemaptest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.adapter.RVCommentAdapter;
import com.hvngoc.googlemaptest.app.Config;
import com.hvngoc.googlemaptest.app.MyApplication;
import com.hvngoc.googlemaptest.gcm.GcmIntentService;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.model.Comment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Hoang Van Ngoc on 03/06/2016.
 */
public class CommentActivity extends AppCompatActivity{

    private RecyclerView mRecyclerView;
    private RVCommentAdapter mAdapter;
    private String TAG = MainPageActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    protected BroadcastReceiver mRegistrationBroadcastReceiver;
    private String postID;
    private String relation;
    private boolean page = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initToolbar();
        initBroadcastReceiver();

        Bundle bundle = getIntent().getExtras();
        postID = bundle.getString("postID");
        relation = bundle.getString("relation");

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RVCommentAdapter();
        mRecyclerView.setAdapter(mAdapter);
        initLoadMoreItems();

        Button btnCommentSend = (Button) findViewById(R.id.btnCommentSend);
        btnCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateNewCommentAsyncTask().execute();
            }
        });

        registernewGCM();
        new LoadCommentAsyncTask(0).execute();
    }


    private boolean isLoading;
    private void initLoadMoreItems() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy < 0) {
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                    if (!isLoading && lastVisibleItem == 1 && page) {
                        isLoading = true;
                        new LoadCommentAsyncTask(totalItemCount).execute();
                    }
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.hint_comment));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("numComment", mAdapter.getItemCount());
        setResult(300, resultIntent);
        finish();
        super.onBackPressed();
    }

    private class LoadLastCommentAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }

        private Boolean postData() {
            String serverUrl = GLOBAL.SERVER_URL + "getLastCommentOfPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("postID", postID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Comment comment = gson.fromJson(res, Comment.class);
                int position = mAdapter.addComment(comment);
                mRecyclerView.scrollToPosition(position);
            }
        }
    }

    private class LoadCommentAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private int skip;

        public LoadCommentAsyncTask(int skip){
            this.skip = skip;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }

        private Boolean postData() {
            String serverUrl = GLOBAL.SERVER_URL + "getAllCommentsOfPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("postID", postID);
                jsonobj.put("skip", skip);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Comment>>() {
                }.getType();
                ArrayList<Comment> listComment = gson.fromJson(res, listType);
                int position =  mAdapter.addListComment(listComment);
                if(listComment.size() < 10)
                    page = false;
                mRecyclerView.scrollToPosition(position);
            }
            isLoading = false;
        }
    }

    private class CreateNewCommentAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        String content = "";
        TextView etxtWriteComment;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            etxtWriteComment = (TextView) findViewById(R.id.etxtWriteComment);
            content = etxtWriteComment.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (content.length() == 0)
                return false;
            return postData();
        }

        private Boolean postData() {
            String serverUrl = GLOBAL.SERVER_URL;
            if (relation.equals(CONSTANT.RELATIONSHIP_TOUR))
                serverUrl += "createNewCommentTourPost";
            else
                serverUrl += "createNewCommentOfPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("postID", postID);
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("content", content);
                jsonobj.put("day", ParseDateTimeHelper.getCurrent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
//                String res = helper.getResponse();
//                Gson gson = new Gson();
//                Comment comment = gson.fromJson(res, Comment.class);
//                int position = mAdapter.addComment(comment);
//                mRecyclerView.scrollToPosition(position);
                etxtWriteComment.setText("");
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    protected void initBroadcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    Bundle bundle = intent.getExtras();
                    String message = bundle.getString("message");
                    String param = bundle.getString("param");
                    String targetID = bundle.getString("targetID");
                    if(targetID.contains(GLOBAL.CurrentUser.getId())) {
                        if (message.equals(CONSTANT.NOTIFICATION_COMMENT) && param.equals(postID)) {
                            new  LoadLastCommentAsyncTask().execute();
                        }
                    }
                }
            }
        };

        if (checkPlayServices()) {
            registerGCM();
        }
    }

    public void registerGCM() {
        /*make sure only one server start. it's destroy after finishing work*/
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    public boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void registernewGCM() {
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        MyApplication.getInstance().getPrefManager().clear();
    }
}
