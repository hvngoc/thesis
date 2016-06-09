package com.hvngoc.googlemaptest.activity;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.custom.MapInfoWindowsLayout;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.LocationRoundHelper;
import com.hvngoc.googlemaptest.helper.SquareHelper;
import com.hvngoc.googlemaptest.model.Post;
import com.hvngoc.googlemaptest.view.MapFooterLayout;
import com.hvngoc.googlemaptest.view.MapHeaderLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap googleMap;

    private int SEARCH_DISTANCE = 100;

    private HashMap<Marker, Post> markerManager = new HashMap<>();
    private ArrayList<Post> currentListPost = new ArrayList<>();

    private MapHeaderLayout mapHeaderLayout;
    private MapFooterLayout mapFooterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GLOBAL.CurrentContext = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        InitRunMapHeader();
        InitRunMapFooter();
    }

    private  void InitRunMapFooter(){
        mapFooterLayout = new MapFooterLayout(this);
        mapFooterLayout.setDelegationHelper(new DelegationHelper() {
            @Override
            public void doSomeThing() {
                SEARCH_DISTANCE = mapFooterLayout.getSearchDistance();
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        RelativeLayout map_activity = (RelativeLayout) findViewById(R.id.layout_map_activity);
        map_activity.addView(mapFooterLayout, params);
        mapFooterLayout.setVisibility(View.INVISIBLE);
    }

    private void InitRunMapHeader(){
        mapHeaderLayout = new MapHeaderLayout(this);
        mapHeaderLayout.setDelegationHelper(new DelegationHelper() {
            @Override
            public void doSomeThing() {
                currentListPost = mapHeaderLayout.getCurrentListPost();
                AddMarker();
                ZoomAnimateLevelToFitMarkers();
            }
        });
        mapHeaderLayout.setOnPlaceSelectionListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mapHeaderLayout.setTextPlaceFragment(place);
                onMapLongClick(place.getLatLng());
            }

            @Override
            public void onError(Status status) {

            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.toolbar);
        RelativeLayout map_activity = (RelativeLayout) findViewById(R.id.layout_map_activity);
        map_activity.addView(mapHeaderLayout, params);
    }

//    private ArrayList<MenuObject> getMenuObjects() {
//        MenuObject close = new MenuObject();
//        close.setResource(android.R.drawable.ic_delete);
//        MenuObject find = new MenuObject(getString(R.string.finding_around));
//        find.setResource(android.R.drawable.ic_menu_myplaces);
//        MenuObject bound = new MenuObject(getString(R.string.bounding_all));
//        bound.setResource(android.R.drawable.ic_menu_mapmode);
//        ArrayList<MenuObject> list =  new ArrayList<>();
//        list.add(close);
//        list.add(find);
//        list.add(bound);
//        return list;
//    }
//    ***************************************************************************************************************   //

    private class SearchPostByDistanceAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private LatLng latLng;
        private SquareHelper squareHelper;

        private double LatitudeUp, LatitudeDown, LongitudeRight, LongitudeLeft;

        public SearchPostByDistanceAsyncTask(LatLng latLng){
            this.latLng = new LatLng(LocationRoundHelper.Round(latLng.latitude), LocationRoundHelper.Round(latLng.longitude));
            squareHelper = new SquareHelper(latLng, SEARCH_DISTANCE);
            LatitudeUp = LocationRoundHelper.Round(squareHelper.getLatUp());
            LatitudeDown = LocationRoundHelper.Round(squareHelper.getLatDown());
            LongitudeRight = LocationRoundHelper.Round(squareHelper.getLonRight());
            LongitudeLeft = LocationRoundHelper.Round(squareHelper.getLonLeft());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "SearchPostByDistance";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("Latitude", latLng.latitude);
                jsonobj.put("Longitude", latLng.longitude);
                jsonobj.put("LatitudeUp", LatitudeUp);
                jsonobj.put("LatitudeDown", LatitudeDown);
                jsonobj.put("LongitudeRight", LongitudeRight);
                jsonobj.put("LongitudeLeft", LongitudeLeft);
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
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Post>>() {
                }.getType();
                currentListPost = gson.fromJson(res, listType);
                AddMarker();
            }
            else {
                googleMap.clear();
            }
            googleMap.addCircle(new CircleOptions().center(latLng).radius(SEARCH_DISTANCE).
                    strokeColor(Color.BLUE).strokeWidth(2).fillColor(0x110000FF));
        }
    }

//    ************************************************************************************************************    //


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, GET_CAMERA_ZOOM()));
        currentListPost.clear();
        new SearchPostByDistanceAsyncTask(latLng).execute();
        mapFooterLayout.setVisibility(View.VISIBLE);
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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, GET_CAMERA_ZOOM()));
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
        try{
            Bundle extras = getIntent().getExtras();
            Post currentPost = (Post) extras.getSerializable("currentPost");
            if (currentPost != null) {
                currentListPost.add(currentPost);
                InitilizeMap(new LatLng(currentPost.Latitude, currentPost.Longitude));
                AddMarker();
            }
        }catch (Exception e){
            InitilizeMap(new LatLng(GLOBAL.CurrentUser.getDefaultLatitude(), GLOBAL.CurrentUser.getDefaultLongitude()));
        }
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
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, GET_CAMERA_ZOOM());
        googleMap.animateCamera(cu);
    }

    private void AddMarker() {
        googleMap.clear();
        markerManager.clear();
        for(Post item : currentListPost) {
            MarkerOptions markerOption = new MarkerOptions()
                    .position(new LatLng(item.Latitude, item.Longitude))
                    .icon(BitmapDescriptorFactory.fromResource((int)GLOBAL.EMOTION.get(item.getSaveFeeling()).get(1)))
                    .title(item.getContent());

            Marker marker = googleMap.addMarker(markerOption);
            markerManager.put(marker, item);
        }
    }

    private int GET_CAMERA_ZOOM(){
        if (SEARCH_DISTANCE == 100)
            return 18;
        if (SEARCH_DISTANCE == 200)
            return 17;
        if (SEARCH_DISTANCE < 600)
            return 16;
        return 15;
    }
}

