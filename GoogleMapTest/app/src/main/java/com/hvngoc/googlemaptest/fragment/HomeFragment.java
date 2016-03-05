package com.hvngoc.googlemaptest.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Post;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView listnews;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        loadComponents(rootView);
        //initData();
        //initListNewsAdapter();
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

    private void loadComponents(View rootView) {
        listnews = (RecyclerView) rootView.findViewById(R.id.list_news);
        LinearLayoutManager llm = new LinearLayoutManager(GLOBAL.CurentContext);
        listnews.setLayoutManager(llm);
        listnews.setHasFixedSize(true);
    }


    private void initListNewsAdapter(){
        // Remember set posts to apdater, not news.

        // Example code, remember set posts to adapter.
        //RVAdapter adapter = new RVAdapter(news);
        RVAdapter adapter = new RVAdapter(GLOBAL.CurrentListPost);
        listnews.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
            String serverUrl = GLOBAL.SERVER_URL + "neo4j/getAllPost";
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
                Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
                GLOBAL.CurrentListPost = gson.fromJson(res, listType);
                initListNewsAdapter();
            }
            else {
                // Notify send request failed!
            }
            progressDialog.dismiss();
        }


    }
}