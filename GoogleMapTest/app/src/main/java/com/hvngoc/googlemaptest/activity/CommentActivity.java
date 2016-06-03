package com.hvngoc.googlemaptest.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.adapter.RVCommentAdapter;
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

    private String postID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initToolbar();

        Bundle bundle = getIntent().getExtras();
        postID = bundle.getString("postID");

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RVCommentAdapter();
        mRecyclerView.setAdapter(mAdapter);

        Button btnCommentSend = (Button) findViewById(R.id.btnCommentSend);
        btnCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateNewCommentAsyncTask().execute();
            }
        });

        new LoadCommentAsyncTask().execute();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.hint_comment));
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
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                mRecyclerView.scrollToPosition(position);
            }
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
            String serverUrl = GLOBAL.SERVER_URL + "createNewCommentOfPost";
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
                String res = helper.getResponse();
                Gson gson = new Gson();
                Comment comment = gson.fromJson(res, Comment.class);
                int position = mAdapter.addComment(comment);
                mRecyclerView.scrollToPosition(position);
                etxtWriteComment.setText("");
            }
        }
    }
}
