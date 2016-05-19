package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.hvngoc.googlemaptest.activity.BaseActivity;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.MessageDelegationHelper;
import com.hvngoc.googlemaptest.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RVAdapter adapter;

    public HomeFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("HOME", "RESUME HOME");
        GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.NAVIGATION_HOME;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView listnews = (RecyclerView) rootView.findViewById(R.id.list_news);
        LinearLayoutManager llm = new LinearLayoutManager(GLOBAL.CurrentContext);
        listnews.setLayoutManager(llm);
        listnews.setHasFixedSize(true);
        adapter = new RVAdapter();
        listnews.setAdapter(adapter);
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
        new LoadPostAsyncTask().execute();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((BaseActivity) context).setMessageDelegationHelper(new MessageDelegationHelper() {
            @Override
            public void doSomething(String message, String param) {
                if (message.equals(CONSTANT.NOTIFICATION_HOME)) {
                    new LoadPostAsyncTask().execute();
                }
            }
        });
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
            else {
                // Notify send request failed!
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, new NothingsFragment());
                fragmentTransaction.commit();
            }
            progressDialog.dismiss();
        }
    }
}