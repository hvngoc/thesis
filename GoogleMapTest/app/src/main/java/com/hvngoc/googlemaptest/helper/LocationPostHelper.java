package com.hvngoc.googlemaptest.helper;

import com.hvngoc.googlemaptest.model.Post;

/**
 * Created by Hoang Van Ngoc on 11/03/2016.
 */
public class LocationPostHelper {
    private String location;
    private int index;

    public LocationPostHelper(String location){
        this.location = location;
        index = this.location.indexOf(',');
    }
    public LocationPostHelper(){

    }

    public void setLocation(String location){
        this.location = location;
        index = this.location.indexOf(',');
    }

    public String getLocation(){
        return this.location;
    }

    public Double getLatitude(){
        return Double.parseDouble(this.location.substring(0, index));
    }

    public Double getLongitude(){
        return Double.parseDouble(this.location.substring(index + 1, this.location.length()));
    }
}
