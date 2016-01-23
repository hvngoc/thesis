package com.hvngoc.googlemaptest.custom;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.hvngoc.googlemaptest.R;

public class MapInfoWindowsLayout implements GoogleMap.InfoWindowAdapter {

    private final View myContentsView;

    public MapInfoWindowsLayout(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE );
        myContentsView = inflater.inflate(R.layout.layout_custom_maps_infowindow, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return myContentsView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        //find view by id here

        return null;
    }
}
