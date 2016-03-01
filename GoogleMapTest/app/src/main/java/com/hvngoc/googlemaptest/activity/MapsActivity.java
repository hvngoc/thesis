package com.hvngoc.googlemaptest.activity;
import java.util.ArrayList;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.custom.MapInfoWindowsLayout;
import com.hvngoc.googlemaptest.custom.MapsDialogLayout;
import com.hvngoc.googlemaptest.model.CustomMarker;
import com.hvngoc.googlemaptest.model.MyLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener{

    private GoogleMap googleMap;

    private ArrayList<Marker> listMarkers = new ArrayList<Marker>();
    private ArrayList<CustomMarker> markersHashMap = new ArrayList<CustomMarker>();

    private ArrayList<CustomMarker> markersHvngoc = new ArrayList<CustomMarker>();
    private ArrayList<CustomMarker> markersFriend = new ArrayList<CustomMarker>();
    private ArrayList<CustomMarker> markersAll = new ArrayList<CustomMarker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        SetMarkerStartScreen();
        SetMarkerHvngoc();
        SetMarkersFriend();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapLongClickListener(this);
        this.googleMap.setOnInfoWindowClickListener(this);
        this.googleMap.setInfoWindowAdapter(new MapInfoWindowsLayout(this));
        InitilizeMap(new LatLng(10.7626943, 106.6801512));
        InitializeUiSettings();
        AddMarker(markersHashMap);
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        ArrayList<CustomMarker> temp = new ArrayList<CustomMarker>();
        Location locationA = new Location("A");
        locationA.setLatitude(latLng.latitude);
        locationA.setLongitude(latLng.longitude);
        for (CustomMarker item : markersAll) {
            double distance = 0;
            Location locationB = new Location("B");
            locationB.setLatitude(item.getCustomMarkerLatitude());
            locationB.setLongitude(item.getCustomMarkerLongitude());
            distance = locationA.distanceTo(locationB);
            if (distance < 100.0)
                temp.add(item);
        }
        markersHashMap = temp;
        AddMarker(temp);
        googleMap.addCircle(new CircleOptions().center(latLng).radius(100.0).
                strokeColor(Color.BLUE).strokeWidth(2).fillColor(0x110000FF));

//        String ss = new GeolocatorAddressHelper(this, latLng.latitude, latLng.longitude).GetAddress();
//        Log.d("click", ss);
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
        MapsDialogLayout dialog = new MapsDialogLayout(MapsActivity.this);
        dialog.show();
    }

    private void InitilizeMap(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        (findViewById(R.id.mapFragment)).getViewTreeObserver().addOnGlobalLayoutListener(
                new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (android.os.Build.VERSION.SDK_INT >= 16) {
                            (findViewById(R.id.mapFragment)).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            (findViewById(R.id.mapFragment)).getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }
    private void InitializeUiSettings() {
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.setTrafficEnabled(true);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(false);
    }
    private void SetMarkerStartScreen() {
        markersHashMap.add(new CustomMarker("demo screen", R.drawable.markers_emotion_funny, 10.7626943, 106.6801512));
    }
    private void SetMarkerHvngoc(){
        markersHvngoc.add(new CustomMarker("HCMUS :)))", R.drawable.markers_emotion_money,10.7626846, 106.6818203));
        markersHvngoc.add(new CustomMarker("Hello moto :))", R.drawable.markers_emotion_normal, 10.7605192, 106.6812714));
        markersHvngoc.add(new CustomMarker("Test app", R.drawable.markers_emotion_sleepy, 10.7626943, 106.6801512));
        markersAll.add(new CustomMarker("HCMUS :)))", R.drawable.markers_emotion_money, 10.7626846, 106.6818203));
        markersAll.add(new CustomMarker("Hello moto :))", R.drawable.markers_emotion_normal, 10.7605192, 106.6812714));
        markersAll.add(new CustomMarker("Test app", R.drawable.markers_emotion_sleepy, 10.7626943, 106.6801512));
    }
    private  void SetMarkersFriend(){
        markersFriend.add(new CustomMarker("DH y duoc", R.drawable.markers_emotion_refuse, 10.7553016, 106.661008));
        markersFriend.add(new CustomMarker("DH test", R.drawable.markers_emotion_happy, 10.7536678, 106.6601068));
        markersFriend.add(new CustomMarker("DH demo", R.drawable.markers_emotion_sad, 10.7536151, 106.6622311));
        markersAll.add(new CustomMarker("DH y duoc", R.drawable.markers_emotion_refuse, 10.7553016, 106.661008));
        markersAll.add(new CustomMarker("DH test", R.drawable.markers_emotion_happy, 10.7536678, 106.6601068));
        markersAll.add(new CustomMarker("DH demo", R.drawable.markers_emotion_sad, 10.7536151, 106.6622311));
    }
    //////////////////////////button click///////////////////
    public void BtnAllBoundingClick(View v) {
        ZoomAnimateLevelToFitMarkers(120);
    }
    public void BtnViewSearchClick(View v){
        EditText etxtSearch = (EditText) findViewById(R.id.editTextSearch);
        String text = etxtSearch.getText().toString();
        if (text.compareTo("hvngoc") == 0){
            AddMarker(markersHvngoc);
            markersHashMap = markersHvngoc;
        }
        else if (text.compareTo("friend") == 0){
            AddMarker(markersFriend);
            markersHashMap = markersFriend;
        }
        else{
            AddMarker(markersAll);
            markersHashMap = markersAll;
        }
        ZoomAnimateLevelToFitMarkers(120);
    }
    public void BtnViewMyLocationClick(View v){
        MyLocation location = new MyLocation(this);
        LatLng latLng = new LatLng(location.GetLatitude(), location.GetLongitude());
        onMapLongClick(latLng);
    }
    ///////////////////////////////////////////////////////
    // this is method to help us fit the Markers into specific bounds for camera position
    public void ZoomAnimateLevelToFitMarkers(int padding) {
        if(markersHashMap.size() == 0)
            return;
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for(CustomMarker item : markersHashMap){
            LatLng ll = new LatLng(item.getCustomMarkerLatitude(), item.getCustomMarkerLongitude());
            b.include(ll);
        }
        LatLngBounds bounds = b.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }
    // this is method to help us add a Marker to the map
    public void AddMarker(ArrayList<CustomMarker> markers) {
        googleMap.clear();
        listMarkers.clear();
        for(CustomMarker item : markers) {
            MarkerOptions markerOption = new MarkerOptions().position(
                    new LatLng(item.getCustomMarkerLatitude(), item.getCustomMarkerLongitude())).icon(
                    BitmapDescriptorFactory.fromResource(item.GetIdStatus())).title(item.getCustomMarkerTitle());

            Marker newMark = googleMap.addMarker(markerOption);
            listMarkers.add(newMark);
        }
    }
}

