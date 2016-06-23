package com.hvngoc.googlemaptest.activity;

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
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.helper.LocationHelper;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class TourCreationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap googleMap;
    private EditText editTextCreatePost;
    private FloatingActionButton btnCreatePostOK;

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
                TextView textMinutes = (TextView)findViewById(R.id.textMinutes);
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

            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MapCreatePostMap);
        supportMapFragment.getMapAsync(this);
    }

    private void setLocationTextView(double Latitude, double Longitude){
        String address = new GeolocatorAddressHelper(this, Latitude, Longitude ).GetAddress();
        TextView txtCreatePostLocation = (TextView) findViewById(R.id.txtCreatePostLocation);
        txtCreatePostLocation.setText(address);
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
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        setLocationTextView(latLng.latitude, latLng.longitude);
        AddCurrentMarker(latLng);
    }

    private void AddCurrentMarker(LatLng latLng){
        this.googleMap.clear();
        MarkerOptions markerOption = new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markers_default));
        this.googleMap.addMarker(markerOption);
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
