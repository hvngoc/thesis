package com.hvngoc.googlemaptest.model;

/**
 * Created by Hoang Van Ngoc on 23/06/2016.
 */
public class AppSetting {
    private boolean background;
    private int time;
    private int distance;

    public AppSetting(){

    }

    public AppSetting(boolean background, int time, int distance){
        this.setBackground(background);
        this.setTime(time);
        this.setDistance(distance);
    }

    public boolean isBackground() {
        return background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
