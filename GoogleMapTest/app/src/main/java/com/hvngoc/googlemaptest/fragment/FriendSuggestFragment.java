package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class FriendSuggestFragment extends Fragment {

    private RVFriendAdapter adapter;

    public FriendSuggestFragment() {
        adapter = new RVFriendAdapter(View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
        startLoading();
        Log.i("FRIEND SUGGEST", "CONSTRUCTOR");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FRIEND SUGGEST", "CREATE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_friend_list, container, false);

        RecyclerView recyclerListFriend = (RecyclerView) rootView.findViewById(R.id.recycler_list_friend);
        recyclerListFriend.setLayoutManager(new LinearLayoutManager(GLOBAL.CurrentContext));
        recyclerListFriend.setHasFixedSize(true);

        recyclerListFriend.setAdapter(adapter);

        Log.i("FRIEND SUGGEST", "CREATE VIEW");

        return rootView;
    }

    private ProgressDialog progressDialog = null;
    private void startLoading() {
        progressDialog = new ProgressDialog(GLOBAL.CurrentContext,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(GLOBAL.CurrentContext.getString(R.string.loading));
        progressDialog.show();
        new LoadFriendAsyncTask().execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainPageActivity)context).setMessageDelegationHelper(new MessageDelegationHelper() {
            @Override
            public void doSomething(String message, String param) {
                if (message.equals(CONSTANT.NOTIFICATION_CONFIRM_FRIEND)) {
                    progressDialog.show();
                    new LoadFriendAsyncTask().execute();
                }
            }
        });
        Log.i("FRIEND SUGGEST", "ATTACH");
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
            String serverUrl = GLOBAL.SERVER_URL + "getSuggestFriends";
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
                adapter.addListItem(listFriend);
            }
            else {
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_friend_list, new NothingsFragment());
                fragmentTransaction.commit();
            }
            progressDialog.dismiss();
        }
    }
}
