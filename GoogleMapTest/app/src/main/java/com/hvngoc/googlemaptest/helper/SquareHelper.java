package com.hvngoc.googlemaptest.helper;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hoang Van Ngoc on 29/05/2016.
 */
public class SquareHelper {
    private static final double RE = 6371;
    private double latUp;
    private double latDown;
    private double lonRight;
    private double lonLeft;

    public SquareHelper(LatLng latLng, double distance) {

        distance *= (1 + Math.sqrt(2)) / 60;

        setLatUp(latLng.latitude + distance / RE);
        setLatDown(latLng.latitude - distance / RE);
        setLonRight(latLng.longitude + distance / (RE * Math.abs(Math.cos(2 * latLng.latitude))));
        setLonLeft(latLng.longitude - distance / (RE * Math.abs(Math.cos(2 * latLng.latitude))));
    }

    public double getLatUp() {
        return latUp;
    }

    public void setLatUp(double latUp) {
        this.latUp = latUp;
    }

    public double getLatDown() {
        return latDown;
    }

    public void setLatDown(double latDown) {
        this.latDown = latDown;
    }

    public double getLonRight() {
        return lonRight;
    }

    public void setLonRight(double lonRight) {
        this.lonRight = lonRight;
    }

    public double getLonLeft() {
        return lonLeft;
    }

    public void setLonLeft(double lonLeft) {
        this.lonLeft = lonLeft;
    }
}
