package com.hvngoc.googlemaptest.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

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
import com.hvngoc.googlemaptest.custom.MapSearchingDialog;
import com.hvngoc.googlemaptest.model.MyLocation;
import com.hvngoc.googlemaptest.model.Post;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap googleMap;

    private HashMap<Marker, Post> markerManager = new HashMap<>();
    private ArrayList<Post> currentListPost = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_on_map, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final AutoCompleteTextView editTextSearch = (AutoCompleteTextView) searchMenuItem.getActionView();
        editTextSearch.setHint("type here for searching on map");
        editTextSearch.setDropDownBackgroundResource(R.color.white);
        editTextSearch.setThreshold(1);
        editTextSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String getString = editTextSearch.getAdapter().getItem(position).toString();
                editTextSearch.setText(getString);
            }
        });
        editTextSearch.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, GLOBAL.listTag));
        editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editTextSearch.showDropDown();
                return false;
            }
        });

        MenuItem clickSearching = menu.findItem(R.id.action_searching);
        clickSearching.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String text = editTextSearch.getText().toString();
                Log.i("search action   ", text);
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
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_finding) {
            MyLocation location = new MyLocation(this);
            LatLng latLng = new LatLng(location.GetLatitude(), location.GetLongitude());
            onMapLongClick(latLng);
            return true;
        }
        if(id == R.id.action_bounding){
            ZoomAnimateLevelToFitMarkers(120);
            return true;
        }
        if(id == R.id.action_settings){
            MapSearchingDialog dialog = new MapSearchingDialog(this);
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            Location locationB = new Location("B");
            locationB.setLatitude(item.Latitude);
            locationB.setLongitude(item.Longitude);
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
        Intent intent = new Intent(this, NewsDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("currentPost", post);
        startActivity(intent);
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
        Post currentPost = (Post) extras.getSerializable("currentPost");

        currentListPost.add(currentPost);

        InitilizeMap(new LatLng(currentPost.Latitude, currentPost.Longitude));
        AddMarker();
    }
    private void ZoomAnimateLevelToFitMarkers(int padding) {
        if(currentListPost.size() == 0)
            return;
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for(Post item : currentListPost){
            LatLng ll = new LatLng(item.Latitude, item.Longitude);
            b.include(ll);
        }
        LatLngBounds bounds = b.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cu);
    }

    private void AddMarker() {
        googleMap.clear();
        markerManager.clear();
        for(Post item : currentListPost) {
            MarkerOptions markerOption = new MarkerOptions()
                    .position(new LatLng(item.Latitude, item.Longitude))
                    .icon(BitmapDescriptorFactory.fromResource(GLOBAL.EMOTION.get(item.feeling)))
                    .title(item.getContent());

            Marker marker = googleMap.addMarker(markerOption);
            markerManager.put(marker, item);
        }
    }
}

