package com.hvngoc.googlemaptest.custom;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.helper.LocationPostHelper;
import com.hvngoc.googlemaptest.model.Post;

import org.w3c.dom.Text;

public class MapInfoWindowsLayout implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;
    private Post post;
    private Context context;
    public MapInfoWindowsLayout(Context context, Post post){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        myContentsView = inflater.inflate(R.layout.layout_custom_maps_infowindow, null);
        this.context = context;
        this.post = post;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView txtInfoUsername = (TextView) myContentsView.findViewById(R.id.txtInfoUsername);
        txtInfoUsername.setText(this.post.userName);

        TextView txtInfoFeeling = (TextView) myContentsView.findViewById(R.id.txtInfoFeeling);
        txtInfoFeeling.setText("feeling " + this.post.feeling);

        LocationPostHelper locationHelper = new LocationPostHelper(this.post.getLocation());
        Double latitude = locationHelper.getLatitude();
        Double longitude = locationHelper.getLongitude();

        String address = new GeolocatorAddressHelper(this.context, latitude, longitude).GetAddress();
        TextView txtInfoAddress = (TextView) myContentsView.findViewById(R.id.txtInfoAddress);
        txtInfoAddress.setText(address);
        return myContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {

        return myContentsView;
    }
}
