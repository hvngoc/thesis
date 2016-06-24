package com.hvngoc.googlemaptest.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.activity.MainPageActivity;
import com.hvngoc.googlemaptest.adapter.RVFriendAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.MessageDelegationHelper;
import com.hvngoc.googlemaptest.model.Friend;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FriendListFragment extends Fragment {

    private RecyclerView recyclerListFriend;

    public FriendListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FRIEND LIST", "CREATE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("FRIEND LIST", "CREATE VIEW");
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_friend_list, container, false);
        recyclerListFriend = (RecyclerView) rootView.findViewById(R.id.recycler_list_friend);
        LinearLayoutManager llm = new LinearLayoutManager(GLOBAL.CurrentContext);
        recyclerListFriend.setLayoutManager(llm);
        recyclerListFriend.setHasFixedSize(true);
        return rootView;
    }

    ProgressDialog progressDialog = null;
    @Override
    public void onStart() {
        Log.i("FRIEND LIST", "ONSTART");
        super.onStart();
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        new LoadFriendAsyncTask().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FRIEND LIST", "ON RESUME");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("FRIEND LIST", "ATTATCH");
        ((MainPageActivity)context).setMessageDelegationHelper(new MessageDelegationHelper() {
            @Override
            public void doSomething(String message, String param) {
                if (message.equals(CONSTANT.NOTIFICATION_CONFIRM_FRIEND)) {
                    new LoadFriendAsyncTask().execute();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private class LoadFriendAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "getAllFriends";
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
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Friend>>(){}.getType();
                ArrayList<Friend> listFriend = gson.fromJson(res, listType);
                RVFriendAdapter adapter = new RVFriendAdapter(listFriend, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                recyclerListFriend.setAdapter(adapter);
            }
            progressDialog.dismiss();
        }
    }
}
