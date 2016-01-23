package com.hvngoc.googlemaptest.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeolocatorAddressHelper {
    private double latitude;
    private double longitude;

    private Context context;

    public  GeolocatorAddressHelper(Context context, double latitude, double longitude) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public  String GetAddress() {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses == null || addresses.size() == 0)
                return "";
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            return address + ", " + city + ", " + state + ", " + country + ".";
        } catch (IOException e) {
            return "";
        }
    }
}
