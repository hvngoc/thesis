package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.BaseActivity;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVCommentAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.MessageDelegationHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.model.Comment;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class CommentDialogLayout extends Dialog {

    private RecyclerView mRecyclerView;
    private RVCommentAdapter mAdapter;

    private String postID;
    private Context context;
    private TextView txtNumComment;

    public CommentDialogLayout(Context context, String postID, TextView txtNumComment) {
        super(context);
        this.context = context;
        this.postID = postID;
        this.txtNumComment = txtNumComment;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(R.layout.layout_custom_comment_dialog);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RVCommentAdapter();
        mRecyclerView.setAdapter(mAdapter);

        Button btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button btnCommentSend = (Button) findViewById(R.id.btnCommentSend);
        btnCommentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateNewCommentAsyncTask().execute();
            }
        });

        new LoadCommentAsyncTask().execute();

        ((BaseActivity)this.context).setMessageDelegationHelper(new MessageDelegationHelper() {
            @Override
            public void doSomething(String message, String param) {
                if (message.equals(CONSTANT.NOTIFICATION_COMMENT) && param.equals(postID)){
                    new LoadLastCommentAsyncTask().execute();
                }
            }
        });

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
                int numComment = position + 1;
                txtNumComment.setText("" + numComment);
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
                int numComment = position + 1;
                txtNumComment.setText("" + numComment);
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
                int numComment = position + 1;
                txtNumComment.setText("" + numComment);
                etxtWriteComment.setText("");
            }
        }
    }
}
