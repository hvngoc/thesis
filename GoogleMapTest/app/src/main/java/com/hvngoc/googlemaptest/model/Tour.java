package com.hvngoc.googlemaptest.model;

import android.location.Location;

import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.helper.LocationRoundHelper;

/**
 * Created by Hoang Van Ngoc on 22/06/2016.
 */
public class Tour {
    private String id;
    private String userAvatar;
    private String userName;

    private int status;

    private int startNumLike;
    private int startNumComment;
    private int startNumShare;
    private String startDate;
    private double startLatitude;
    private double startLongitude;

    private int numPlaces;

    private int stopNumLike;
    private int stopNumComment;
    private int stopNumShare;
    private String stopDate;
    private double stopLatitude;
    private double stopLongitude;

    public Tour(){

    }

    public Tour(String id, String userName, String userAvatar, int status,
                int startNumLike, int startNumComment, int startNumShare, String startDate, double startLatitude, double startLongitude,
                int numPlaces,
                int stopNumLike, int stopNumComment, int stopNumShare, String stopDate, double stopLatitude, double stopLongitude){
        this.id = id;
        this.userAvatar = userAvatar;
        this.userName = userName;
        this.status = status;
        this.startNumLike = startNumLike;
        this.startNumComment = startNumComment;
        this.startNumShare = startNumShare;
        this.startDate = startDate;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.numPlaces = numPlaces;
        this.stopNumLike = stopNumLike;
        this.stopNumShare = stopNumShare;
        this.stopNumComment = stopNumComment;
        this.stopDate = stopDate;
        this.stopLatitude = stopLatitude;
        this.stopLongitude = stopLongitude;
    }

    public String getAddressStart(){
        return new GeolocatorAddressHelper(GLOBAL.CurrentContext, startLatitude, startLongitude).GetAddress();
    }

    public String getAddressTop(){
        return new GeolocatorAddressHelper(GLOBAL.CurrentContext, stopLatitude, stopLongitude).GetAddress();
    }

    public double getDistanceNumber(){
        Location start = new Location("start");
        start.setLatitude(startLatitude);
        start.setLongitude(startLongitude);
        Location stop = new Location("stop");
        stop.setLatitude(stopLatitude);
        stop.setLongitude(stopLongitude);
        return LocationRoundHelper.MetersToKM(start.distanceTo(stop));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserAvatar() {
        return GLOBAL.SERVER_IMAGE_URL + userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStartNumLike() {
        return startNumLike;
    }

    public void setStartNumLike(int startNumLike) {
        this.startNumLike = startNumLike;
    }

    public int getStartNumComment() {
        return startNumComment;
    }

    public void setStartNumComment(int startNumComment) {
        this.startNumComment = startNumComment;
    }

    public int getStartNumShare() {
        return startNumShare;
    }

    public void setStartNumShare(int startNumShare) {
        this.startNumShare = startNumShare;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public int getNumPlaces() {
        return numPlaces;
    }

    public void setNumPlaces(int numPlaces) {
        this.numPlaces = numPlaces;
    }

    public int getStopNumLike() {
        return stopNumLike;
    }

    public void setStopNumLike(int stopNumLike) {
        this.stopNumLike = stopNumLike;
    }

    public int getStopNumComment() {
        return stopNumComment;
    }

    public void setStopNumComment(int stopNumComment) {
        this.stopNumComment = stopNumComment;
    }

    public int getStopNumShare() {
        return stopNumShare;
    }

    public void setStopNumShare(int stopNumShare) {
        this.stopNumShare = stopNumShare;
    }

    public String getStopDate() {
        return stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public double getStopLatitude() {
        return stopLatitude;
    }

    public void setStopLatitude(double stopLatitude) {
        this.stopLatitude = stopLatitude;
    }

    public double getStopLongitude() {
        return stopLongitude;
    }

    public void setStopLongitude(double stopLongitude) {
        this.stopLongitude = stopLongitude;
    }
}
