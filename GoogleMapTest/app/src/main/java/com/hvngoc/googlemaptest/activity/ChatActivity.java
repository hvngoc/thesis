package com.hvngoc.googlemaptest.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.adapter.ChatArrayAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.MessageDelegationHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.model.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChatActivity extends BaseActivity {

    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private String toUserID;

    private boolean side = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbar();
        toUserID = getIntent().getExtras().getString("fromUserID");
        Log.i("toUSERID", toUserID);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        listView = (ListView) findViewById(R.id.listView1);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);
        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return (event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && sendChatMessage();
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        new LoadMessageAsyncTask().execute();
        setMessageDelegationHelper(new MessageDelegationHelper() {
            @Override
            public void doSomething(String message, String param) {
                if (param.equals(toUserID)) {
                    new LoadOneMessageAsyncTask().execute();
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionBarTitle(getString(R.string.title_messages));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean sendChatMessage(){
        if(chatText.getText().length() > 0)
            new SendMessageAsyncTask().execute();
        return true;
    }

    private void sendChatMessageSuccess() {
        chatArrayAdapter.add(new ChatMessage(chatText.getText().toString()));
        chatText.setText("");
        side = false;
    }
//    *************************************************************************************************************
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chat;
    }

    @Override
    protected void InitRunCustomMenu() {

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.action_notification:
                Log.i("CHAT ACTIVITY", "CLICK NOTIFICATION");
                GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.NAVIGATION_NOTIFICATION;
                intent = new Intent(ChatActivity.this, MainPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.message_notification:
                Log.i("MAPS ACTIVITY", "CLICK NOTIFICATION");
                GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.NAVIGATION_MESSAGE;
                intent = new Intent(ChatActivity.this, MainPageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//    ***********************************************************************************************************

    private class LoadOneMessageAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "loadOneMessageOfUser";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("targetUserID", toUserID);
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
                Gson gson = new Gson();
                ChatMessage message = gson.fromJson(res, ChatMessage.class);
                chatArrayAdapter.add(message);
            }
        }
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
            String serverUrl = GLOBAL.SERVER_URL + "loadMessageOfUser";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("targetUserID", toUserID);
                jsonobj.put("skip", 0);
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
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<ChatMessage>>(){}.getType();
                ArrayList<ChatMessage> messageList = gson.fromJson(res, listType);
                chatArrayAdapter.addListMessage(messageList);
            }
        }
    }


    private class SendMessageAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String message;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            message = chatText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }

        private Boolean postData() {
            String serverUrl = GLOBAL.SERVER_URL + "sendMessageToUser";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("fromUserID", GLOBAL.CurrentUser.getId());
                jsonobj.put("toUserID", toUserID);
                jsonobj.put("message", message);
                jsonobj.put("date", ParseDateTimeHelper.getCurrent());
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
                sendChatMessageSuccess();
            }
        }
    }
}
