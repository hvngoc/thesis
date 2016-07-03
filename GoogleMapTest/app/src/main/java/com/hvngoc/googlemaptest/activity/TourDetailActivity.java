package com.hvngoc.googlemaptest.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.adapter.RVAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TourDetailActivity extends AppCompatActivity {

    private RVAdapter adapter = new RVAdapter();
    private String tourID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        Bundle bundle = getIntent().getExtras();
        tourID = bundle.getString("tourID");
        String currentID = bundle.getString("userID");
        int status = bundle.getInt("status");

        FloatingActionButton fab_tour_detail = (FloatingActionButton) findViewById(R.id.fab_tour_detail);
        if (currentID.equals(GLOBAL.CurrentUser.getId()) && status == 1){
            fab_tour_detail.setVisibility(View.VISIBLE);
        }
        else{
            fab_tour_detail.setVisibility(View.GONE);
        }
        fab_tour_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourDetailActivity.this, AdditionTourPostingActivity.class);
                intent.putExtra("tourID", tourID);
                startActivityForResult(intent, 555);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_tour_detail));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView list_posts = (RecyclerView) findViewById(R.id.list_posts);
        list_posts.setLayoutManager(new LinearLayoutManager(this));
        list_posts.setHasFixedSize(true);
        list_posts.setAdapter(adapter);

        new LoadTourPostAsyncTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tour_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_tour_maps:
                Intent intent = new Intent(TourDetailActivity.this, MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("currentTour", adapter.getListPost());
                GLOBAL.CurrentContext.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GLOBAL.CurrentContext = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 555){
            Post post = (Post) data.getSerializableExtra("post");
            adapter.addToFirst(post);
        }
    }

    //   ------------------------------------------------------------------------------------------//

    private class LoadTourPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "getAllPostOfTour";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("tourID", tourID);
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
                Type listType = new TypeToken<ArrayList<Post>>() {
                }.getType();
                ArrayList<Post> listPost = gson.fromJson(res, listType);
                adapter.addListPost(listPost);
            }
        }
    }
}
