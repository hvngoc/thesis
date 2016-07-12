package com.hvngoc.googlemaptest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
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
import com.hvngoc.googlemaptest.app.Config;
import com.hvngoc.googlemaptest.app.MyApplication;
import com.hvngoc.googlemaptest.gcm.GcmIntentService;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.model.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private String toUserID;
    protected BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initBroadcastReceiver();
        initToolbar();
        toUserID = getIntent().getExtras().getString("fromUserID");
        Log.i("toUSERID", toUserID);
        Log.i("CURRENTID", GLOBAL.CurrentUser.getId());
        Button buttonSend = (Button) findViewById(R.id.buttonSend);
        listView = (ListView) findViewById(R.id.listView1);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.card_item_chat_singlemessage);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        registernewGCM();
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
                    if(message.equals(CONSTANT.NOTIFICATION_MESSAGE) && GLOBAL.CurrentUser.getId().equals(targetID)) {
                        if (param.equals(toUserID)) {
                            new LoadOneMessageAsyncTask().execute();
                        }
                    }
                }
            }
        };
        registerGCM();
    }

    public void registerGCM() {
        /*make sure only one server start. it's destroy after finishing work*/
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String name = getIntent().getExtras().getString("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private boolean sendChatMessage(){
        if(chatText.getText().length() > 0) {
            new SendMessageAsyncTask().execute();
            sendChatMessageSuccess();
        }
        return true;
    }

    private void sendChatMessageSuccess() {
        chatArrayAdapter.add(new ChatMessage(chatText.getText().toString()));
        chatText.setText("");
    }
//    *************************************************************************************************************

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                if (message != null)
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
                if (messageList == null || messageList.size() == 0){
                    new LoadOneMessageAsyncTask().execute();
                }
                else
                    chatArrayAdapter.addListMessage(messageList);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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
//            if(result) {
//                sendChatMessageSuccess();
//            }
        }
    }
}
