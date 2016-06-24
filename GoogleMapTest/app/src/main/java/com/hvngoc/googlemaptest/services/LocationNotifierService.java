package com.hvngoc.googlemaptest.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.app.MyApplication;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.LocationRoundHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.helper.SquareHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hoang Van Ngoc on 11/05/2016.
 */
public class LocationNotifierService extends Service implements LocationListener {

    private int TIME_UPDATER = 60000;
    private int DISTANCE_UPDATER = 500;

    private LocationManager Manager;

    private Vibrator vibrator;

    private ResultReceiver locationResultReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SERVICE", "START");
        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        Manager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null) {
            locationResultReceiver = intent.getParcelableExtra("LocationResultReceiver");
            TIME_UPDATER = intent.getIntExtra("time", 60000);
            DISTANCE_UPDATER = intent.getIntExtra("distance", 500);
        }
        if (Manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.i("GPS is on", "GPS on");
            Manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATER, DISTANCE_UPDATER, this);
        }
        else if (Manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.i("Network on", "Network is on");
            Manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_UPDATER, DISTANCE_UPDATER, this);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        Log.i("service stop", "service stop");
        Manager.removeUpdates(this);
    }

    private void NotifyDevice(){
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + MyApplication.getInstance().getApplicationContext().getPackageName() + "/raw/vibrate_audio");
            Ringtone r = RingtoneManager.getRingtone(MyApplication.getInstance().getApplicationContext(), alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        vibrator.vibrate(500);
    }

//    *************************************************************************************************

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Location change", "Location change");
        if (GLOBAL.CurrentUser != null) {
            new MakeNotificationPost(location.getLatitude(), location.getLongitude()).execute();
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

    private class MakeNotificationPost extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private Double latitude, longitude;

        private SquareHelper squareHelper;
        private double LatitudeUp, LatitudeDown, LongitudeRight, LongitudeLeft;

        public MakeNotificationPost(Double latitude, Double longitude){
            this.latitude = LocationRoundHelper.Round(latitude);
            this.longitude = LocationRoundHelper.Round(longitude);

            squareHelper = new SquareHelper(new LatLng(this.latitude, this.longitude), 500);
            LatitudeUp = LocationRoundHelper.Round(squareHelper.getLatUp());
            LatitudeDown = LocationRoundHelper.Round(squareHelper.getLatDown());
            LongitudeRight = LocationRoundHelper.Round(squareHelper.getLonRight());
            LongitudeLeft = LocationRoundHelper.Round(squareHelper.getLonLeft());
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "makeNotificationPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("Latitude", latitude);
                jsonobj.put("Longitude", longitude);
                jsonobj.put("LatitudeUp", LatitudeUp);
                jsonobj.put("LatitudeDown", LatitudeDown);
                jsonobj.put("LongitudeRight", LongitudeRight);
                jsonobj.put("LongitudeLeft", LongitudeLeft);
                jsonobj.put("day", ParseDateTimeHelper.getCurrent());
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
                NotifyDevice();
                locationResultReceiver.send(200, new Bundle());
            }
        }
    }
}
