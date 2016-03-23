package com.hvngoc.googlemaptest.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
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
import com.hvngoc.googlemaptest.helper.LocationPostHelper;
import com.hvngoc.googlemaptest.model.MyLocation;
import com.hvngoc.googlemaptest.model.Post;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap googleMap;
    private LocationPostHelper locationHelper = new LocationPostHelper();

    private HashMap<Marker, Post> markerManager = new HashMap<>();
    private ArrayList<Post> currentListPost = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_maps;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnMapLongClickListener(this);
        this.googleMap.setOnInfoWindowClickListener(this);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.getUiSettings().setRotateGesturesEnabled(false);
        this.googleMap.getUiSettings().setTiltGesturesEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setTrafficEnabled(true);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.setIndoorEnabled(true);
        this.googleMap.setBuildingsEnabled(false);

        SetMarkerStartScreen();
    }
    @Override
    public void onMapLongClick(LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        currentListPost.clear();
        Location locationA = new Location("A");
        locationA.setLatitude(latLng.latitude);
        locationA.setLongitude(latLng.longitude);
        for (Post item : GLOBAL.CurrentListPost) {
            double distance = 0;
            locationHelper.setLocation(item.getLocation());
            Double latitude = locationHelper.getLatitude();
            Double longitude = locationHelper.getLongitude();

            Location locationB = new Location("B");
            locationB.setLatitude(latitude);
            locationB.setLongitude(longitude);
            distance = locationA.distanceTo(locationB);
            if (distance < 100.0)
                currentListPost.add(item);
        }
        AddMarker();
        googleMap.addCircle(new CircleOptions().center(latLng).radius(100.0).
                strokeColor(Color.BLUE).strokeWidth(2).fillColor(0x110000FF));
    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        marker.hideInfoWindow();
        Post post = markerManager.get(marker);
        MapsDialogLayout dialog = new MapsDialogLayout(MapsActivity.this, post);
        dialog.show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Post post = markerManager.get(marker);
        this.googleMap.setInfoWindowAdapter(new MapInfoWindowsLayout(this, post));
        marker.showInfoWindow();
        return true;
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
    private void SetMarkerStartScreen() {
        Bundle extras = getIntent().getExtras();
        int position = extras.getInt("position");

        currentListPost.add(GLOBAL.CurrentListPost.get(position));
        locationHelper.setLocation(GLOBAL.CurrentListPost.get(position).getLocation());

        Double latitude = locationHelper.getLatitude();
        Double longitude = locationHelper.getLongitude();

        InitilizeMap(new LatLng(latitude, longitude));
        AddMarker();
    }
    //////////////////////////button click///////////////////
    public void BtnAllBoundingClick(View v) {
        ZoomAnimateLevelToFitMarkers(120);
    }
    public void BtnViewSearchClick(View v){
        EditText etxtSearch = (EditText) findViewById(R.id.editTextSearch);
        String text = etxtSearch.getText().toString();
        currentListPost.clear();
        for (int i = 0; i < GLOBAL.CurrentListPost.size(); ++i){
            if(text.compareTo(GLOBAL.CurrentListPost.get(i).userName) == 0){
                currentListPost.add(GLOBAL.CurrentListPost.get(i));
            }
        }
        if(currentListPost.size() == 0) {
            currentListPost.addAll(GLOBAL.CurrentListPost);
            AddMarker();
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
        if(currentListPost.size() == 0)
            return;
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for(Post item : currentListPost){
            locationHelper.setLocation(item.getLocation());
            Double latitude = locationHelper.getLatitude();
            Double longitude = locationHelper.getLongitude();

            LatLng ll = new LatLng(latitude, longitude);
            b.include(ll);
        }
        LatLngBounds bounds = b.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }
    // this is method to help us add a Marker to the map
    public void AddMarker() {
        googleMap.clear();
        markerManager.clear();
        for(Post item : currentListPost) {
            locationHelper.setLocation(item.getLocation());
            Double latitude = locationHelper.getLatitude();
            Double longitude = locationHelper.getLongitude();

            MarkerOptions markerOption = new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromResource(GLOBAL.EMOTION.get(item.feeling)))
                    .title(item.getContent());

            Marker marker = googleMap.addMarker(markerOption);
            markerManager.put(marker, item);
        }
    }
}

