package com.hvngoc.googlemaptest.activity;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
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
import com.hvngoc.googlemaptest.adapter.SearchPostAdapter;
import com.hvngoc.googlemaptest.custom.IconizedMenu;
import com.hvngoc.googlemaptest.custom.MapInfoWindowsLayout;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.LocationRoundHelper;
import com.hvngoc.googlemaptest.helper.SquareHelper;
import com.hvngoc.googlemaptest.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener{

    private final int MIN_DISTANCE = 100;
    private IconizedMenu iconizedMenu;
    private AutoCompleteTextView search_text_header;

    private GoogleMap googleMap;
    private int SEARCH_DISTANCE = 100;

    private HashMap<Marker, Post> markerManager = new HashMap<>();
    private ArrayList<Post> currentListPost = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GLOBAL.CurrentContext = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        InitRunMapFooter();
    }

    private void InitRunMapFooter(){
        final TextView textDistance = (TextView) findViewById(R.id.textDistance);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SEARCH_DISTANCE = progress * MIN_DISTANCE + MIN_DISTANCE;
                textDistance.setText(SEARCH_DISTANCE + "m");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint(getString(R.string.hint_type_search_map));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                autocompleteFragment.setText(place.getAddress().toString());
                onMapLongClick(place.getLatLng());
            }

            @Override
            public void onError(Status status) {

            }
        });

        search_text_header = (AutoCompleteTextView) findViewById(R.id.search_text_header);
        search_text_header.setDropDownBackgroundResource(R.color.white);
        search_text_header.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = (Post) search_text_header.getAdapter().getItem(position);
                currentListPost.clear();
                currentListPost.add(post);
                AddMarker();
                ZoomAnimateLevelToFitMarkers();
                search_text_header.setText("");
            }
        });
        search_text_header.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                search_text_header.showDropDown();
                return false;
            }
        });
        search_text_header.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() > 30)
                    text = text.substring(0, 30);
                if (text.length() > 0)
                    new SearchPostAsyncTask(text).execute();
            }
        });

        ImageView img_header_search = (ImageView) findViewById(R.id.img_header_search);
        img_header_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMarker();
                ZoomAnimateLevelToFitMarkers();
            }
        });

        ImageView img_header_action = (ImageView) findViewById(R.id.img_header_action);
        img_header_action.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        img_header_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconizedMenu.show();
            }
        });

        iconizedMenu = new IconizedMenu(this, img_header_action);
        iconizedMenu.getMenuInflater().inflate(R.menu.menu_map_search_engine, iconizedMenu.getMenu());
        iconizedMenu.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                RunSearchOption(item.getItemId());
                iconizedMenu.dismiss();
                return false;
            }
        });
        RunSearchOption(R.id.map_search_post);
    }

    private void RunSearchOption(int id){
        switch (id){
            case R.id.map_search_post:
                setEnableView(View.INVISIBLE, View.VISIBLE);
                break;
            case R.id.map_search_place:
                setEnableView(View.VISIBLE, View.INVISIBLE);
                break;
        }
    }
    private void setEnableView(int viewPlace, int viewSearch){
        findViewById(R.id.place_autocomplete_fragment).setVisibility(viewPlace);
        search_text_header.setVisibility(viewSearch);
        findViewById(R.id.img_header_search).setVisibility(viewSearch);
    }

//    ***************************************************************************************************************   //

    private class SearchPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String text;
        public SearchPostAsyncTask(String text){
            this.text = text;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "SearchPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("params", text);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
                currentListPost = gson.fromJson(res, listType);
                search_text_header.setAdapter(new SearchPostAdapter(MapsActivity.this, android.R.layout.simple_list_item_1, currentListPost));
                search_text_header.showDropDown();
            }
        }
    }

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

