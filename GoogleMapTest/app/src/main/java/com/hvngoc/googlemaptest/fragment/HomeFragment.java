package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.hvngoc.googlemaptest.activity.PostCreationActivity;
import com.hvngoc.googlemaptest.adapter.RVAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RVAdapter adapter;

    public HomeFragment() {
        adapter = new RVAdapter();
        Log.i("HOME", "CONSTRUCTOR");
        startLoading();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("HOME", "CREATE HOME");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("HOME", "RESUME HOME");
        GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.BOTTOM_HOME;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView listnews = (RecyclerView) rootView.findViewById(R.id.list_news);
        listnews.setLayoutManager(new LinearLayoutManager(GLOBAL.CurrentContext));
        listnews.setHasFixedSize(true);

        listnews.setAdapter(adapter);

        FloatingActionButton createPost = (FloatingActionButton) rootView.findViewById(R.id.createPost);
        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), PostCreationActivity.class));
            }
        });
        return rootView;
    }

    ProgressDialog progressDialog = null;
    private void startLoading() {
        progressDialog = new ProgressDialog(GLOBAL.CurrentContext,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(GLOBAL.CurrentContext.getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(getRetainInstance());
        progressDialog.show();
        new LoadPostAsyncTask().execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("HOME", "ATTACH HOME");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private class LoadPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "getAllPostOfFriends";
            JSONObject jsonobj = new JSONObject();
            Log.i("userID", GLOBAL.CurrentUser.getId());
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
                Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
                ArrayList<Post> CurrentListPost = gson.fromJson(res, listType);
                adapter.addListPost(CurrentListPost);
            }
            progressDialog.dismiss();
        }
    }
}