package com.hvngoc.googlemaptest.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.LocationRoundHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hoang Van Ngoc on 11/05/2016.
 */
public class LocationNotifierService extends Service implements LocationListener {

    private static final int TIME_UPDATER = 10000;
    private static final int DISTANCE_UPDATER = 50;

    private LocationManager Manager;
    private MediaPlayer vibrateAudio;
    private Vibrator vibrator;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SERVICE", "START");
        vibrateAudio = MediaPlayer.create(getApplicationContext(), R.raw.vibrate_audio);
        vibrateAudio.setLooping(false);
        vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        Manager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        vibrateAudio.release();
        Manager.removeUpdates(this);
    }

    private void NotifyDevice(){
        if (!vibrateAudio.isPlaying())
            vibrateAudio.start();
        vibrator.vibrate(500);
    }

//    private void showNotifier(){
//        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//
//        ImageView chatHead = new ImageView(this);
//        chatHead.setImageResource(R.drawable.icon_profile);
//
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_PHONE,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//        params.gravity = Gravity.TOP | Gravity.START;
//        params.x = 0;
//        params.y = 100;
//        windowManager.addView(chatHead, params);
//    }

//    *************************************************************************************************

    @Override
    public void onLocationChanged(Location location) {
        Log.i("Location change", "Location change");
        if (GLOBAL.CurrentUser != null) {
            new MakeNotificationMyPost(location.getLatitude(), location.getLongitude()).execute();
            new MakeNotificationFriendPost(location.getLatitude(), location.getLongitude()).execute();
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

    private class MakeNotificationMyPost extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private Double latitude, longitude;

        public MakeNotificationMyPost(Double latitude, Double longitude){
            this.latitude = LocationRoundHelper.Round(latitude);
            this.longitude = LocationRoundHelper.Round(longitude);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "makeNotificationMyPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("Latitude", latitude);
                jsonobj.put("Longitude", longitude);
                jsonobj.put("distance", 1000);
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
                // chang star in menu bar
                Log.i("NOTIFICATION ...", "my post accepted");
            }
        }
    }

    private class MakeNotificationFriendPost extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private Double latitude, longitude;

        public MakeNotificationFriendPost(Double latitude, Double longitude){
            this.latitude = LocationRoundHelper.Round(latitude);
            this.longitude = LocationRoundHelper.Round(longitude);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "makeNotificationFriendPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("Latitude", latitude);
                jsonobj.put("Longitude", longitude);
                jsonobj.put("distance", 1000);
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
                // chang star in menu bar
                Log.i("NOTIFICATION ...", "friend post accepted");
            }
        }
    }
}
