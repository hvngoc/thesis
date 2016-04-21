package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVPickImageAdapter;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.model.MyLocation;
import com.hvngoc.googlemaptest.model.Post;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Hoang Van Ngoc on 20/04/2016.
 */
public class PostCreationDialog extends Dialog implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private Context context;
    private FragmentManager fragmentManager;
    private GoogleMap googleMap;

    private Post post;

    public PostCreationDialog(Context context, FragmentManager fragmentManager) {
        super(context);
        this.context = context;
        this.fragmentManager = fragmentManager;
        post = new Post();
        post.relationShip = "posted";
        post.userName = GLOBAL.CurrentUser.getName();
        post.userAvatar = GLOBAL.CurrentUser.getAvatar();
        post.setPostDate(new SimpleDateFormat("mm:HH dd/MM/yyyy").format(new Date()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(R.layout.layout_custom_post_creation);

        Button btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RadioGroup radioGroupCreatePost = (RadioGroup) findViewById(R.id.radioGroupCreatePost);
        radioGroupCreatePost.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioGetOnMap) {
                    findViewById(R.id.MapCreatePostMap).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.MapCreatePostMap).setVisibility(View.INVISIBLE);
                }
            }
        });

        final ImageView btnCreatePostGetImage = (ImageView) findViewById(R.id.btnCreatePostGetImage);
        btnCreatePostGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load LIST image on sd card *************************************************************************************
                //to post.listImages and set view to recyclerVIEW
                RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.recyclerCreatePostImage);
                RecyclerView.Adapter mAdapter = new RVPickImageAdapter();

                mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        final ImageView btnCreatePostGetFeeling = (ImageView) findViewById(R.id.btnCreatePostGetFeeling);
        btnCreatePostGetFeeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final IconizedMenu menu = new IconizedMenu(context, btnCreatePostGetFeeling);
                menu.getMenuInflater().inflate(R.menu.menu_pick_feeling, menu.getMenu());
                menu.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        post.feeling = item.getTitle().toString();
                        btnCreatePostGetFeeling.setImageResource(GLOBAL.EMOTION.get(post.feeling));
                        TextView text = (TextView) findViewById(R.id.txtCreatePostFeeling);
                        text.setText(post.feeling);
                        menu.dismiss();
                        return true;
                    }
                });
                menu.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        InitContentView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapLongClickListener(this);
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
        InitilizeMap();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        post.Latitude = latLng.latitude;
        post.Latitude = latLng.longitude;
        Log.i("lat" + post.Latitude, "long" + post.Longitude);
        AddCurrentMarker();

        String address = new GeolocatorAddressHelper(context, post.Latitude, post.Longitude ).GetAddress();
        TextView txtCreatePostLocation = (TextView) findViewById(R.id.txtCreatePostLocation);
        txtCreatePostLocation.setText(address);

        Toast.makeText(context, "You mean: " + address, Toast.LENGTH_SHORT).show();
    }

    private void AddCurrentMarker(){
        this.googleMap.clear();
        MarkerOptions markerOption = new MarkerOptions()
                .position(new LatLng(post.Latitude, post.Longitude))
                .icon(BitmapDescriptorFactory.fromResource(GLOBAL.EMOTION.get(post.feeling)))
                .title(post.feeling);
        this.googleMap.addMarker(markerOption);
    }

    private void InitilizeMap() {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(post.Latitude, post.Longitude), 17));
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
        AddCurrentMarker();
    }

    private void InitContentView(){
        findViewById(R.id.MapCreatePostMap).setVisibility(View.INVISIBLE);

        MyLocation myLocation = new MyLocation(context);
        post.Latitude = myLocation.GetLatitude();
        post.Longitude = myLocation.GetLongitude();
        post.feeling = CONSTANT.EMOTION_STRING_NORMAL;

        String address = new GeolocatorAddressHelper(context, post.Latitude, post.Longitude ).GetAddress();
        TextView txtCreatePostLocation = (TextView) findViewById(R.id.txtCreatePostLocation);
        txtCreatePostLocation.setText(address);

        SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.MapCreatePostMap);
        supportMapFragment.getMapAsync(this);
    }
}
