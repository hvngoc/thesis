package com.hvngoc.googlemaptest.model;

import com.hvngoc.googlemaptest.activity.GLOBAL;

/**
 * Created by 12125_000 on 3/2/2016.
 */
public class User {
    private String id;
    private String name;
    private String avatar;

    public User(){

    }

    private double defaultLatitude, defaultLongitude;

    public User(String id, String name, String avatar, double defaultLatitude, double defaultLongitude) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.defaultLatitude = defaultLatitude;
        this.defaultLongitude = defaultLongitude;
    }

    private String regID;
    public void setRegID(String regID){
        this.regID = regID;
    }
    public String getRegID(){
        return this.regID;
    }

    public double getDefaultLatitude(){
        return this.defaultLatitude;
    }

    public double getDefaultLongitude(){
        return this.defaultLongitude;
    }

    public void setDefaultLatitude(double defaultLatitude){
        this.defaultLatitude = defaultLatitude;
    }

    public void setDefaultLongitude(double defaultLongitude){
        this.defaultLongitude = defaultLongitude;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return GLOBAL.SERVER_IMAGE_URL + this.avatar;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
