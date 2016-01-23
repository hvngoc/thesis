package com.hvngoc.googlemaptest.model;


public class NewsItem {
    private String userName;
    private String title;
    private String urlImage;
    private int photo;

    public NewsItem(String username, String title, int photo){
        this.userName = username;
        this.title = title;
        this.photo = photo;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}

