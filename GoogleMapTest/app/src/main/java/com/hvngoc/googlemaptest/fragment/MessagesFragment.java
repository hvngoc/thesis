package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.FriendMessageActivity;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.activity.MainPageActivity;
import com.hvngoc.googlemaptest.adapter.RVMessageAdapter;
import com.hvngoc.googlemaptest.helper.BarBadgeHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.MessageDelegationHelper;
import com.hvngoc.googlemaptest.model.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MessagesFragment extends Fragment {

    private RVMessageAdapter adapter;

    public MessagesFragment() {
        adapter = new RVMessageAdapter();
        startLoading();
        Log.i("MESSAGE", "CONSTRUCTOR");
    }

    private ProgressDialog progressDialog = null;
    private void startLoading() {
        progressDialog = new ProgressDialog(GLOBAL.CurrentContext,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(GLOBAL.CurrentContext.getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new LoadMessageAsyncTask().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        BarBadgeHelper.chatMessageCount = 0;
        GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.BOTTOM_MESSAGE;
        Log.i("MESSAGE FRAGMENT", "ON RESUME");
        ((MainPageActivity)mContext).setMessageDelegationHelper(new MessageDelegationHelper() {
            @Override
            public void doSomething(String message, String param) {
                if (message.equals(CONSTANT.NOTIFICATION_MESSAGE)) {
                    progressDialog.show();
                    new LoadMessageAsyncTask().execute();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        BarBadgeHelper.chatMessageCount = 0;
        ((MainPageActivity)mContext).setMessageDelegationHelper(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i("MESSAGE FRAGMENT", "ON CREATE");
    }

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("MESSAGE FRAGMENT", "ON ATTACH");
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        RecyclerView recyclerListMessage = (RecyclerView) rootView.findViewById(R.id.recycler_message);
        recyclerListMessage.setLayoutManager(new LinearLayoutManager(GLOBAL.CurrentContext));
        recyclerListMessage.setHasFixedSize(true);

        recyclerListMessage.setAdapter(adapter);

        FloatingActionButton newMessage = (FloatingActionButton) rootView.findViewById(R.id.newMessage);
        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GLOBAL.CurrentContext, FriendMessageActivity.class);
                startActivity(intent);
            }
        });
        Log.i("MESSAGE FRAGMENT", "ON VIEW");
        return rootView;
    }

    private class LoadMessageAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "getAllMessages";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                String res = helper.getResponse();
                Log.i("RES", res);
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<ChatMessage>>(){}.getType();
                ArrayList<ChatMessage> listMessage = gson.fromJson(res, listType);
                adapter.addListItem(listMessage);
            }
            progressDialog.dismiss();
        }
    }
}