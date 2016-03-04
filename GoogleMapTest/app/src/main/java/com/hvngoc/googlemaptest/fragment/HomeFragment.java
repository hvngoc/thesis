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
import com.hvngoc.googlemaptest.activity.Global;
import com.hvngoc.googlemaptest.adapter.RVAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.NewsItem;
import com.hvngoc.googlemaptest.model.Post;
import com.hvngoc.googlemaptest.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private List<NewsItem> news;
    private List<Post> posts;
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
        LinearLayoutManager llm = new LinearLayoutManager(Global.CurentContext);
        listnews.setLayoutManager(llm);
        listnews.setHasFixedSize(true);
    }

    private void initData(){
        news = new ArrayList<NewsItem>();
        news.add(new NewsItem("Nguyễn Văn Tường", "The most beatiful place in the world, The most beatiful place in the world, The most beatiful place in the world", R.drawable.image1));
        news.add(new NewsItem("Ngô Thị Liên", "Flowers of Dalat. Go, go, go, ...", R.drawable.image1));
        news.add(new NewsItem("Nguyễn Minh Nhân", "Special feeling!", R.drawable.image2));
        news.add(new NewsItem("Trương Thanh Sang", "Yesterday!", R.drawable.image4));
        news.add(new NewsItem("Trần Ngọc Như", "Come here with me!", R.drawable.image5));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image6));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image1));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image2));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image1));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image4));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image5));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image6));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image1));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image2));
        news.add(new NewsItem("Nguyễn Như Huyền", "One year ago.", R.drawable.image4));

    }

    private void initListNewsAdapter(){
        // Remember set posts to apdater, not news.

        // Example code, remember set posts to adapter.
        //RVAdapter adapter = new RVAdapter(news);
        RVAdapter adapter = new RVAdapter(posts);
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
            String serverUrl = "http://192.168.1.85:8080/Neo4jWebAPI/neo4j/getAllPost";
            JSONObject jsonobj = new JSONObject();
            /*
            try {
                jsonobj.put("userID", Global.CurrentUser.getId());
                jsonobj.put("userID", "user1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendStringHTTTPostRequest("user1");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
                posts = gson.fromJson(res, listType);
                initListNewsAdapter();
                progressDialog.dismiss();
            }
            else {
                // Notify send request failed!
            }
        }


    }
}