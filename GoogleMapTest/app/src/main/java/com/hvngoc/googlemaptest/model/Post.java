package com.hvngoc.googlemaptest.model;

import android.util.Log;

import com.hvngoc.googlemaptest.activity.GLOBAL;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Post implements Serializable {
    private String postID;
    private String content;
    private String listImages;
    private String postDate;
    public String feeling;
    public Double Latitude, Longitude;

    public String userName;
    private String userAvatar;
    public String relationShip;

    public int numLike, numShare, numComment;

    public int isYouLike;

    public String tag;

    public Post(){

    }

    public Post (String postID, String content, String listImages,
                 String postDate, Double Latitude, Double Longitude, String feeling,
                 String userName, String userAvatar, String relationShip,
                 int numLike, int numShare, int numComment, int isYouLike, String tag) {
        this.setPostID(postID);
        this.setContent(content);
        this.setListImages(listImages);
        this.setPostDate(postDate);
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.feeling = feeling;
        this.userName = userName;
        this.setUserAvatar(userAvatar);
        this.relationShip = relationShip;

        this.numLike = numLike;
        this.numShare = numShare;
        this.numComment = numComment;

        this.isYouLike = isYouLike;
        this.tag = tag;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getFirstImageUrl() {
        String prefix = GLOBAL.SERVER_IMAGE_URL;
        String url = prefix + listImages.substring(0, listImages.indexOf(';'));
        Log.d("Image URL", url);
        return url;
    }

    public void setListImages(String listImages) {
        this.listImages = listImages;
    }


    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getUserAvatar() {
        String url = GLOBAL.SERVER_IMAGE_URL + userAvatar;
        return url;
    }

    public ArrayList<String> getListImages() {
        ArrayList<String> images = new ArrayList<String>(Arrays.asList(listImages.split(";")));
        ArrayList<String> urls = new ArrayList<String>();
        for (String temp: images) {
            urls.add(GLOBAL.SERVER_IMAGE_URL + temp);
        }
        return urls;
    }


    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}