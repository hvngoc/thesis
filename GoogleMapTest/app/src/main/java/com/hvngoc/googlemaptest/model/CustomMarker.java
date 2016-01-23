package com.hvngoc.googlemaptest.model;

public class CustomMarker {

    private String title;
    private Double latitude;
    private Double longitude;
    private  int idStatus;

    public CustomMarker(String title,int idStatus, Double latitude, Double longitude) {
        this.title = title;
        this.idStatus = idStatus;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public CustomMarker() {
        this.title = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public String getCustomMarkerTitle(){
        return title;
    }
    public Double getCustomMarkerLatitude() {
        return latitude;
    }
    public Double getCustomMarkerLongitude() {
        return longitude;
    }
    public int GetIdStatus(){
        return idStatus;
    }

    public  void setCustomMarkerTitle(String title){
        this.title = title;
    }
    public void setCustomMarkerLatitude(Double mLatitude) {
        this.latitude = mLatitude;
    }
    public void setCustomMarkerLongitude(Double mLongitude) {
        this.longitude = mLongitude;
    }
}

