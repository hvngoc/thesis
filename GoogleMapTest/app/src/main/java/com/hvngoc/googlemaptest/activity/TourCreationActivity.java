package com.hvngoc.googlemaptest.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.LocationHelper;
import com.hvngoc.googlemaptest.helper.LocationRoundHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.services.TourCreationService;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class TourCreationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap googleMap;
    private EditText editTextCreatePost;
    private FloatingActionButton btnCreatePostOK;

    private double startLatitude, startLongitude;

    private int DISTANCES = 1, MINUTES = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_creation);
        GLOBAL.CurrentContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_tour_creation);

        initComponent();

        KeyboardVisibilityEvent.setEventListener(this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen)
                            btnCreatePostOK.setVisibility(View.INVISIBLE);
                        else
                            btnCreatePostOK.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void initComponent() {
        btnCreatePostOK = (FloatingActionButton) findViewById(R.id.btnCreatePostOK);
        editTextCreatePost = (EditText)findViewById(R.id.editTextCreatePost);

        SeekBar seekBarDistance = (SeekBar)findViewById(R.id.seekBarDistance);
        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DISTANCES = progress + 1;
                TextView textDistance = (TextView)findViewById(R.id.textDistance);
                textDistance.setText(DISTANCES + " km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar seekBarMinutes = (SeekBar)findViewById(R.id.seekBarMinutes);
        seekBarMinutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                MINUTES = 10 + progress * 10;
                TextView textMinutes = (TextView) findViewById(R.id.textMinutes);
                textMinutes.setText(MINUTES + " " + getString(R.string.minutes));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        TextView textMinutes = (TextView)findViewById(R.id.textMinutes);
        textMinutes.setText(MINUTES + " " + getString(R.string.minutes));

        HashTagHelper hashTagHelper = HashTagHelper.Creator.create(ContextCompat.getColor(this, R.color.blue), null);
        hashTagHelper.handle(editTextCreatePost);

        btnCreatePostOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(TourCreationActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.show();
                new CreateTourAsyncTask().execute();
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MapCreatePostMap);
        supportMapFragment.getMapAsync(this);
    }

    private ProgressDialog progressDialog = null;

    private void setLocationTextView(double Latitude, double Longitude){
        String address = new GeolocatorAddressHelper(this, Latitude, Longitude ).GetAddress();
        TextView txtCreatePostLocation = (TextView) findViewById(R.id.txtCreatePostLocation);
        txtCreatePostLocation.setText(address);

        startLatitude = LocationRoundHelper.Round(Latitude);
        startLongitude = LocationRoundHelper.Round(Longitude);
    }

    private void startTourCreationService(String tourID){
        Intent intent = new Intent(getApplicationContext(), TourCreationService.class);
        intent.putExtra("TIME_UPDATER", MINUTES * 60 * 1000);
        intent.putExtra("DISTANCE_UPDATER", DISTANCES * 1000);
        intent.putExtra("defaultContent", editTextCreatePost.getText().toString());
        intent.putExtra("tourID", tourID);
        startService(intent);
        onBackPressed();
    }

//    -------------------------------------------------------------------------------------------------------

    private class CreateTourAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String content;
        private String date;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            content = editTextCreatePost.getText().toString();
            date = ParseDateTimeHelper.getCurrent();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "createTour";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("content", content);
                jsonobj.put("date", date);
                jsonobj.put("startLatitude", startLatitude);
                jsonobj.put("startLongitude", startLongitude);
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
                String tourID = gson.fromJson(res, String.class);
                startTourCreationService(tourID);
            }
            progressDialog.dismiss();
        }
    }
    /*-----------------------------------MAP INITIALIZATION-----------------------------------*/
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
                LocationHelper locationHelper = new LocationHelper(GLOBAL.CurrentContext);
                Double latitude = locationHelper.GetLatitude();
                Double longitude = locationHelper.GetLongitude();
                if (latitude == 0.0 && longitude == 0.0) {
                    latitude = GLOBAL.CurrentUser.getDefaultLatitude();
                    longitude = GLOBAL.CurrentUser.getDefaultLongitude();
                }
                setLocationTextView(latitude, longitude);
                return false;
            }
        });
        InitilizeMap();
        btnCreatePostOK.bringToFront();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        AddCurrentMarker(latLng);
    }

    private void AddCurrentMarker(LatLng latLng){
        this.googleMap.clear();
        MarkerOptions markerOption = new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markers_default));
        this.googleMap.addMarker(markerOption);
        setLocationTextView(latLng.latitude, latLng.longitude);
    }

    private void InitilizeMap() {
        LatLng startLatLng = new LatLng(GLOBAL.CurrentUser.getDefaultLatitude(), GLOBAL.CurrentUser.getDefaultLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 15));
        (findViewById(R.id.MapCreatePostMap)).getViewTreeObserver().addOnGlobalLayoutListener(
                new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (android.os.Build.VERSION.SDK_INT >= 16) {
                            (findViewById(R.id.MapCreatePostMap)).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            (findViewById(R.id.MapCreatePostMap)).getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        AddCurrentMarker(startLatLng);
    }
}
