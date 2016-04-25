package com.hvngoc.googlemaptest.activity;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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
import com.hvngoc.googlemaptest.custom.IconizedMenu;
import com.hvngoc.googlemaptest.custom.MapInfoWindowsLayout;
import com.hvngoc.googlemaptest.custom.MapSearchingDialog;
import com.hvngoc.googlemaptest.model.MyLocation;
import com.hvngoc.googlemaptest.model.Post;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap googleMap;
    private IconizedMenu iconizedMenu;

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private String SEARCH_ENGINE = "Search by Name.";
    private int SEARCH_DISTANCE = 100;

    private HashMap<Marker, Post> markerManager = new HashMap<>();
    private ArrayList<Post> currentListPost = new ArrayList<>();

    AutoCompleteTextView search_text_auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        search_text_auto = (AutoCompleteTextView) findViewById(R.id.search_text_auto);
        search_text_auto.setDropDownBackgroundResource(R.color.white);
        search_text_auto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                search_text_auto.showDropDown();
                return false;
            }
        });

        RunCustomMenu();
        RunSearchingEngine();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PLACE_AUTOCOMPLETE_REQUEST_CODE:{
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    onMapLongClick(place.getLatLng());
                }
                break;
            }
        }
    }

    private  void RunSearchingEngine(){
        search_text_auto.setVisibility(View.VISIBLE);
        search_text_auto.bringToFront();
        switch (SEARCH_ENGINE){
            case "Search by Name.":{
                search_text_auto.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getListName()));
                search_text_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String text = search_text_auto.getAdapter().getItem(position).toString();
                        search_text_auto.setText(text);
                        currentListPost.clear();
                        for (int i = 0; i < GLOBAL.CurrentListPost.size(); ++i) {
                            if (text.compareTo(GLOBAL.CurrentListPost.get(i).userName) == 0) {
                                currentListPost.add(GLOBAL.CurrentListPost.get(i));
                            }
                        }
                        AddMarker();
                        ZoomAnimateLevelToFitMarkers();
                    }
                });
                break;
            }
            case "Search by Tag.":{
                search_text_auto.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, GLOBAL.listTag));
                search_text_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String text = search_text_auto.getAdapter().getItem(position).toString();
                        search_text_auto.setText(text);
                        currentListPost.clear();
                        for (int i = 0; i < GLOBAL.CurrentListPost.size(); ++i) {
                            if (text.compareTo(GLOBAL.CurrentListPost.get(i).tag) == 0) {
                                currentListPost.add(GLOBAL.CurrentListPost.get(i));
                            }
                        }
                        AddMarker();
                        ZoomAnimateLevelToFitMarkers();
                    }
                });
                break;
            }
            case "Search by Place.":{
                search_text_auto.setVisibility(View.INVISIBLE);
                try {
                    Intent intent = new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(MapsActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
            }
        }
    }

    private void RunCustomMenu(){
        iconizedMenu = new IconizedMenu(this, findViewById(R.id.imgViewOption));
        iconizedMenu.getMenuInflater().inflate(R.menu.menu_map_content, iconizedMenu.getMenu());
        iconizedMenu.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                iconizedMenu.dismiss();
                switch (item.getItemId()) {
                    case R.id.menu_search: {
                        RunSearchingEngine();
                        break;
                    }
                    case R.id.menu_around: {
                        MyLocation location = new MyLocation(MapsActivity.this);
                        LatLng latLng = new LatLng(location.GetLatitude(), location.GetLongitude());
                        onMapLongClick(latLng);
                        break;
                    }
                    case R.id.menu_bound: {
                        ZoomAnimateLevelToFitMarkers();
                        break;
                    }
                    case R.id.menu_setting: {
                        final MapSearchingDialog dialog = new MapSearchingDialog(MapsActivity.this);
                        dialog.setOnButtonOKClick(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SEARCH_ENGINE = dialog.searchEngine;
                                SEARCH_DISTANCE = dialog.searchDistance;
                                dialog.dismiss();
                                RunSearchingEngine();
                            }
                        });
                        dialog.show();
                        break;
                    }
                }
                return true;
            }
        });
    }

    private ArrayList<String> getListName(){
        ArrayList<String> listName = new ArrayList<>();
        for (Post item : GLOBAL.CurrentListPost){
            if (listName.contains(item.userName))
                continue;
            listName.add(item.userName);
        }
        return listName;
    }
//    ************************************************************************************************************    //
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_maps;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_on_map, menu);
        MenuItem action_notification = menu.findItem(R.id.action_notification);
        action_notification.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getBaseContext(), "goto notification fragment", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        MenuItem action_options = menu.findItem(R.id.action_options);
        action_options.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                iconizedMenu.show();
                return true;
            }
        });
        return true;
    }
//******************************************************************************************************************//
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, SEARCH_DISTANCE * 10));
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
            if (distance < SEARCH_DISTANCE)
                currentListPost.add(item);
        }
        AddMarker();
        googleMap.addCircle(new CircleOptions().center(latLng).radius(SEARCH_DISTANCE).
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, SEARCH_DISTANCE / 2));
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
    private void ZoomAnimateLevelToFitMarkers() {
        if(currentListPost.size() == 0)
            return;
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        for(Post item : currentListPost){
            LatLng ll = new LatLng(item.Latitude, item.Longitude);
            b.include(ll);
        }
        LatLngBounds bounds = b.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, SEARCH_DISTANCE * 10);
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

