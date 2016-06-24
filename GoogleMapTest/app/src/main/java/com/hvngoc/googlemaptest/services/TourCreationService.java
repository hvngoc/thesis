package com.hvngoc.googlemaptest.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.LocationRoundHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hoang Van Ngoc on 23/06/2016.
 */
public class TourCreationService extends Service implements LocationListener {

    private int TIME_UPDATER = 10000;
    private int DISTANCE_UPDATER = 500;
    private String defaultContent = null;
    private String tourId = null;

    private LocationManager Manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("service start", "TOUR TOUR service start" + GLOBAL.TOUR_ON_STARTING);
        Manager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            TIME_UPDATER = intent.getIntExtra("TIME_UPDATER", 10 * 60 * 1000);
            DISTANCE_UPDATER = intent.getIntExtra("DISTANCE_UPDATER", 5 * 1000);
            defaultContent = intent.getStringExtra("defaultContent");
            tourId = intent.getStringExtra("tourID");
            GLOBAL.TOUR_ON_STARTING = tourId;
        }
        if (Manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i("GPS is on", "TOUR GPS on");
            Manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATER, DISTANCE_UPDATER, this);
        }
        else if (Manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.i("Network on", "TOUR Network is on");
            Manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_UPDATER, DISTANCE_UPDATER, this);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        Log.i("service stop", "TOUR TOUR service stop" + GLOBAL.TOUR_ON_STARTING);
        GLOBAL.TOUR_ON_STARTING = null;
        Manager.removeUpdates(this);
    }

    //    *************************************************************************************************

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Location change", "TOUR   TOUR Location change");
        if (GLOBAL.CurrentUser != null) {
            new CreatePostOnTour(location.getLatitude(), location.getLongitude()).execute();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

//    **********************************************************************************************************
    private class CreatePostOnTour extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String date;
        private double latitude, longitude;

        public CreatePostOnTour(double latitude, double longitude){
            this.latitude = LocationRoundHelper.Round(latitude);
            this.longitude = LocationRoundHelper.Round(longitude);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            date = ParseDateTimeHelper.getCurrent();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "createPostOnTour";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("tourID", tourId);
                jsonobj.put("content", defaultContent);
                jsonobj.put("date", date);
                jsonobj.put("startLatitude", latitude);
                jsonobj.put("startLongitude", longitude);
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
}
