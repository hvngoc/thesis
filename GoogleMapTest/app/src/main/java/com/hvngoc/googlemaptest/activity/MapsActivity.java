package com.hvngoc.googlemaptest.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

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
import com.hvngoc.googlemaptest.custom.MapsDialogLayout;
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

        final AutoCompleteTextView editTextSearch = (AutoCompleteTextView) findViewById(R.id.editTextSearch);
        editTextSearch.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, GLOBAL.listTag));
        editTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editTextSearch.showDropDown();
                return false;
            }
        });
    }


    private SearchView mSearchView;
    private MenuItem searchMenuItem;

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // newText is text entered by user to SearchView
            Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Get the SearchView and set the searchable configuration
        searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(listener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
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
        Post currentPost = (Post) extras.getSerializable("currentPost");

        currentListPost.add(currentPost);

        InitilizeMap(new LatLng(currentPost.Latitude, currentPost.Longitude));
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
    public void BtnSettingClick(View v) {
        MapSearchingDialog dialog = new MapSearchingDialog(this);
        dialog.show();
    }
    ///////////////////////////////////////////////////////
    // this is method to help us fit the Markers into specific bounds for camera position
    public void ZoomAnimateLevelToFitMarkers(int padding) {
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
    // this is method to help us add a Marker to the map
    public void AddMarker() {
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

