package com.hvngoc.googlemaptest.helper;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.hvngoc.googlemaptest.R;


public class LocationHelper implements LocationListener {
    private static final int TIME_UPDATER = 5000;
    private static final int DISTANCE_UPDATER = 20;

    private Context context;
    private Location currentLocation;

    public LocationHelper(Context context){
        this.context = context;
        LocationManager Manager = (LocationManager)context.getSystemService(Service.LOCATION_SERVICE);
        try{
            if (Manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATER, DISTANCE_UPDATER, this);
                currentLocation = Manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (currentLocation == null) {
                if (Manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_UPDATER, DISTANCE_UPDATER, this);
                    currentLocation = Manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        if(currentLocation == null){
            showSetting();
        }
        Manager.removeUpdates(this);
    }

    private void showSetting(){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle(context.getString(R.string.warning));
        alert.setMessage(context.getString(R.string.setting_gps));

        alert.setPositiveButton(context.getString(R.string.hint_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        alert.setNegativeButton(context.getString(R.string.hint_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alert.show();
    }
    public Double GetLatitude(){
        if(currentLocation != null){
            return currentLocation.getLatitude();
        }
        return 0.0;
    }

    public Double GetLongitude(){
        if(currentLocation != null){
            return currentLocation.getLongitude();
        }
        return 0.0;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
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
}
