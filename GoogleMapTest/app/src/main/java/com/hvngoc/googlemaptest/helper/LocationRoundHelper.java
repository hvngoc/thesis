package com.hvngoc.googlemaptest.helper;

/**
 * Created by Hoang Van Ngoc on 04/05/2016.
 */
public class LocationRoundHelper {

    public static Double Round(double value){

        return Math.round(value * 1000000) / 1000000.0;
    }

}
