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
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;


public class LocationHelper implements LocationListener {
    private static final int TIME_UPDATER = 10000;
    private static final int DISTANCE_UPDATER = 20;

    Context context;
    LocationManager Manager;
    Location currentLocation;

    public LocationHelper(Context context){
        this.context = context;
        Manager = (LocationManager)context.getSystemService(Service.LOCATION_SERVICE);
        currentLocation = this.GetCurtentLocation();
        if(currentLocation == null){
            showSetting();
        }
    }

    private Location GetCurtentLocation()
    {
        try{
            if (Manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TIME_UPDATER, DISTANCE_UPDATER, this);
                if (Manager != null) {
                    currentLocation = Manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
            if (currentLocation == null) {
                if (Manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIME_UPDATER, DISTANCE_UPDATER, this);
                    if (Manager != null) {
                        currentLocation = Manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return currentLocation;
    }
    private void showSetting(){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Warning");
        alert.setMessage("Setting GPS to determine your location??");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
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
