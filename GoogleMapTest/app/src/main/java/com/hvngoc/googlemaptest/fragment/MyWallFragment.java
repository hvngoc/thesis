package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyWallFragment extends Fragment {

    private RecyclerView listPosts;
    private RVAdapter adapter;
    private String currentID;
    private LinearLayout listNothing;
    private boolean page = true;

    public MyWallFragment() {
        Log.i("WALL", "CONSTRUCTOR WAL");
    }
    public static MyWallFragment getInstance(String id) {
        MyWallFragment fragment = new MyWallFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("WALL", "RESUME WAL");
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        currentID = args.getString("id");
        adapter = new RVAdapter();
        startLoading();
        Log.i("WALL", "CREATE WAL");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wall, container, false);

        listPosts = (RecyclerView) rootView.findViewById(R.id.list_wall_post);
        listPosts.setLayoutManager(new LinearLayoutManager(GLOBAL.CurrentContext));
        listPosts.setHasFixedSize(true);

        listPosts.setAdapter(adapter);
        initLoadMoreItems();
        listNothing = (LinearLayout)rootView.findViewById(R.id.list_wall_nothing);
        return rootView;
    }

    private void SetContentView(int recycler, int nothing){
        listPosts.setVisibility(recycler);
        listNothing.setVisibility(nothing);
    }

    private boolean isLoading;
    private void initLoadMoreItems() {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) listPosts.getLayoutManager();
        listPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) {
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 3) && page) {
                        isLoading = true;
                        new LoadPostAsyncTask(totalItemCount).execute();
                    }
                }
            }
        });
    }

    private ProgressDialog progressDialog = null;
    private void startLoading() {
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new LoadPostAsyncTask(0).execute();

    }


    private class LoadPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private int skip;
        public LoadPostAsyncTask(int skip){
            this.skip = skip;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }

        private Boolean postData() {
            isLoading = false;
            String serverUrl = GLOBAL.SERVER_URL + "getAllPostOfUser";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", currentID);
                jsonobj.put("page", skip);
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
                ArrayList<Post> listPost = gson.fromJson(res, listType);
                if(listPost.size() < 10)
                    page = false;
                adapter.addListPost(listPost);
            }
            else if (skip == 0){
                SetContentView(View.INVISIBLE, View.VISIBLE);
            }
            isLoading = false;
            progressDialog.dismiss();
        }
    }
}
