package com.hvngoc.googlemaptest.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.adapter.RVTourAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Tour;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TourActivity extends AppCompatActivity {

    private RecyclerView list_tours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list_tours = (RecyclerView) findViewById(R.id.list_tours);
        list_tours.setLayoutManager(new LinearLayoutManager(this));
        list_tours.setHasFixedSize(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createTour);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TourActivity.this, TourCreationActivity.class));
            }
        });

//        new LoadLiveTourAsyncTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GLOBAL.CurrentContext = this;
    }

    private class LoadLiveTourAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "getAllLiveTour";
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
            if (result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Tour>>() {}.getType();
                ArrayList<Tour> listTour = gson.fromJson(res, listType);
                list_tours.setAdapter(new RVTourAdapter(listTour));
            }
        }
    }
}
