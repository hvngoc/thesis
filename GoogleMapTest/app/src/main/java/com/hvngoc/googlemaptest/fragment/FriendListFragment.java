package com.hvngoc.googlemaptest.fragment;

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
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVFriendAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Friend;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_friend_list, container, false);
        recyclerListFriend = (RecyclerView) rootView.findViewById(R.id.recycler_list_friend);
        LinearLayoutManager llm = new LinearLayoutManager(GLOBAL.CurentContext);
        recyclerListFriend.setLayoutManager(llm);
        recyclerListFriend.setHasFixedSize(true);
        Log.i("create", "create");
        return rootView;
    }

    ProgressDialog progressDialog = null;
    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.i("started", "started");
        new LoadFriendAsyncTask().execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
            String serverUrl = GLOBAL.SERVER_URL + "neo4j/getAllFriend";
            JSONObject jsonobj = new JSONObject();
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendStringHTTTPostRequest(GLOBAL.CurrentUser.getId());
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Friend>>(){}.getType();
                ArrayList<Friend> listFriend = gson.fromJson(res, listType);
                RVFriendAdapter adapter = new RVFriendAdapter(listFriend);
                recyclerListFriend.setAdapter(adapter);
            }
            else {
                // Notify send request failed!
            }
            progressDialog.dismiss();
        }
    }
}