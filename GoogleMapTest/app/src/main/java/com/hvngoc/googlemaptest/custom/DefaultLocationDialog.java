package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.LocationHelper;
import com.hvngoc.googlemaptest.helper.LocationRoundHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hoang Van Ngoc on 25/05/2016.
 */
public class DefaultLocationDialog extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap googleMap;
    private SupportMapFragment supportMapFragment;

    private double defaultLatitude, defaultLongitude;

    private Dialog dialog;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.layout_custom_default_location);

        TextView btn_setting_cancel = (TextView) dialog.findViewById(R.id.btn_setting_cancel);
        btn_setting_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView btn_setting_change = (TextView) dialog.findViewById(R.id.btn_setting_change);
        btn_setting_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (defaultLatitude != GLOBAL.CurrentUser.getDefaultLatitude() && defaultLongitude != GLOBAL.CurrentUser.getDefaultLongitude()){
                    new UpdateDefaultLocationAsyncTask().execute();
                }
            }
        });
        dialog.show();
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        supportMapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.MapDefaultLocation);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getFragmentManager().beginTransaction().remove(supportMapFragment).commit();
    }

    private void SetLocationTextView(){
        String address = new GeolocatorAddressHelper(GLOBAL.CurrentContext, defaultLatitude, defaultLongitude).GetAddress();
        TextView txtCreatePostLocation = (TextView) dialog.findViewById(R.id.txtDefaultLocation);
        txtCreatePostLocation.setText(address);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        defaultLatitude = latLng.latitude;
        defaultLongitude = latLng.longitude;
        AddCurrentMarker();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapLongClickListener(this);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.getUiSettings().setRotateGesturesEnabled(false);
        this.googleMap.getUiSettings().setTiltGesturesEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setTrafficEnabled(true);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.setIndoorEnabled(true);
        this.googleMap.setBuildingsEnabled(false);

        this.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LocationHelper locationHelper = new LocationHelper(getContext());
                Double latitude = locationHelper.GetLatitude();
                Double longitude = locationHelper.GetLongitude();
                if (latitude == 0.0 && longitude == 0.0) {
                    latitude = GLOBAL.CurrentUser.getDefaultLatitude();
                    longitude = GLOBAL.CurrentUser.getDefaultLongitude();
                }
                onMapLongClick(new LatLng(latitude, longitude));
                return false;
            }
        });

        InitilizeMap();
    }
    private void AddCurrentMarker(){
        this.googleMap.clear();
        MarkerOptions markerOption = new MarkerOptions()
                .position(new LatLng(defaultLatitude, defaultLongitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markers_default));
        this.googleMap.addMarker(markerOption);
        SetLocationTextView();
    }
    private void InitilizeMap() {
        defaultLatitude = GLOBAL.CurrentUser.getDefaultLatitude();
        defaultLongitude = GLOBAL.CurrentUser.getDefaultLongitude();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(defaultLatitude, defaultLongitude), 15));
        (dialog.findViewById(R.id.MapDefaultLocation)).getViewTreeObserver().addOnGlobalLayoutListener(
                new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (android.os.Build.VERSION.SDK_INT >= 16) {
                            (dialog.findViewById(R.id.MapDefaultLocation)).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            (dialog.findViewById(R.id.MapDefaultLocation)).getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        AddCurrentMarker();
    }

    //    *******************************************************************************************************************
    private class UpdateDefaultLocationAsyncTask extends AsyncTask<Void, Void, Boolean> {
        HTTPPostHelper helper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            defaultLatitude = LocationRoundHelper.Round(defaultLatitude);
            defaultLongitude = LocationRoundHelper.Round(defaultLongitude);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("defaultLatitude", defaultLatitude);
                jsonobj.put("defaultLongitude", defaultLongitude);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String serverUrl = GLOBAL.SERVER_URL + "updateDefaultLocation";
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                dismiss();
                GLOBAL.CurrentUser.setDefaultLatitude(defaultLatitude);
                GLOBAL.CurrentUser.setDefaultLongitude(defaultLongitude);
                GLOBAL.startedUserHelper.saveUser(GLOBAL.CurrentUser);
            }
        }
    }

}
