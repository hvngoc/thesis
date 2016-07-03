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
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.adapter.RVTourAdapter;
import com.hvngoc.googlemaptest.custom.ConfirmDialog;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.model.Tour;
import com.hvngoc.googlemaptest.services.TourCreationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class TourActivity extends AppCompatActivity {

    private RecyclerView list_tours;
    private LinearLayout listNothing;

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

        listNothing = (LinearLayout)findViewById(R.id.list_wall_nothing);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.createTour);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GLOBAL.TOUR_ON_STARTING != null){
                    ConfirmDialog confirmDialog = new ConfirmDialog(TourActivity.this, getString(R.string.tour_confirm));
                    confirmDialog.setOnButtonOKClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new StopTourAsyncTask(GLOBAL.TOUR_ON_STARTING).execute();
                            stopService(new Intent(getApplicationContext(), TourCreationService.class));
                            startActivity(new Intent(TourActivity.this, TourCreationActivity.class));
                        }
                    });
                    confirmDialog.show();
                }
                else {
                    startActivity(new Intent(TourActivity.this, TourCreationActivity.class));
                }
            }
        });

        new LoadLiveTourAsyncTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GLOBAL.CurrentContext = this;
    }

    private void SetContentView(int recycler, int nothing){
        list_tours.setVisibility(recycler);
        listNothing.setVisibility(nothing);
    }

//    ----------------------------------------------------------------------------------------------------------//

    private class StopTourAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String data;
        public StopTourAsyncTask(String data){
            this.data = data;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "stopTourLive";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("tourID", data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }
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
                list_tours.setAdapter(new RVTourAdapter(listTour, "null"));
            }
            else {
                SetContentView(View.INVISIBLE, View.VISIBLE);
            }
        }
    }
}
