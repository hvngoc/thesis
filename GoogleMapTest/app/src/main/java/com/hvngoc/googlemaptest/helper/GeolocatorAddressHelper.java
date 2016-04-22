package com.hvngoc.googlemaptest.helper;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeolocatorAddressHelper {
    private Double latitude;
    private Double longitude;

    private Context context;

    public  GeolocatorAddressHelper(Context context, Double latitude, Double longitude) {
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
                return "UNKNOW";
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String result = realAddress(address, ", ") + realAddress(city, ", ") + realAddress(state, ", ") + realAddress(country, ".");
            if (result.length() == 0)
                return "UNKNOW";
            return result;
        } catch (IOException e) {
            return "UNKNOW";
        }
    }

    private String realAddress(String address, String padding) {
        if (address == null)
            return "";
        return address + padding;
    }
}
