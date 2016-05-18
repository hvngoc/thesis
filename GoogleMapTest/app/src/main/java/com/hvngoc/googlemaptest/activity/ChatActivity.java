package com.hvngoc.googlemaptest.activity;

import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.adapter.ChatArrayAdapter;
import com.hvngoc.googlemaptest.adapter.RVMessageAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
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
        buttonSend = (Button) findViewById(R.id.buttonSend);
        listView = (ListView) findViewById(R.id.listView1);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_singlemessage);
        listView.setAdapter(chatArrayAdapter);
        chatText = (EditText) findViewById(R.id.chatText);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
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
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionBarTitle("Message");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GLOBAL.MAIN_PAGE_STRING_VIEW = "Messages";
                finish();
            }
        });
    }

    private boolean sendChatMessage(){
        new SendMessageAsyncTask().execute();
        return true;
    }

    private void sendChatMessageSuccess() {
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GLOBAL.MAIN_PAGE_STRING_VIEW = "Messages";
        finish();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chat;
    }

    @Override
    protected void InitRunCustomMenu() {

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
