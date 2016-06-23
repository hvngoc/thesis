package com.hvngoc.googlemaptest.model;

import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Post implements Serializable {
    private String postID;
    private String content;
    private String listImages;
    private String postDate;
    private String feeling;
    public Double Latitude, Longitude;

    public String userName;
    private String userAvatar;
    private String relationShip;

    public int numLike, numShare, numComment;

    public int isYouLike;

    public Post(){

    }

    public Post (String postID, String content, String listImages,
                 String postDate, Double Latitude, Double Longitude, String feeling,
                 String userName, String userAvatar, String relationShip,
                 int numLike, int numShare, int numComment, int isYouLike) {
        this.setPostID(postID);
        this.setContent(content);
        this.setListImages(listImages);
        this.setPostDate(postDate);
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.setFeeling(feeling);
        this.userName = userName;
        this.setUserAvatar(userAvatar);
        this.setRelationShip(relationShip);

        this.numLike = numLike;
        this.numShare = numShare;
        this.numComment = numComment;

        this.isYouLike = isYouLike;
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

    public String getContentSmaller(){
        return  content.length() > 20 ? content.substring(0, 20) + "..." : content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getFirstImageUrl() {
        int index = listImages.indexOf(';');
        if (index != -1)
            return GLOBAL.SERVER_IMAGE_URL + listImages.substring(0, index);
        return GLOBAL.SERVER_IMAGE_URL + listImages;
    }

    public void setListImages(String listImages) {
        this.listImages = listImages;
    }


    public String getPostDate() {
        return ParseDateTimeHelper.parse(postDate);
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getUserAvatar() {
        return GLOBAL.SERVER_IMAGE_URL + userAvatar;
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

    public String getRelationShip() {
        return GLOBAL.RELATIONSHIP.get(relationShip);
    }

    public void setRelationShip(String relationShip) {
        this.relationShip = relationShip;
    }

    public String getFeeling() {
        return (String)GLOBAL.EMOTION.get(feeling).get(0);
    }

    public void setFeeling(String feeling) {
        for (String item : GLOBAL.EMOTION.keySet()) {
            if (GLOBAL.EMOTION.get(item).get(0).equals(feeling))
                this.feeling = item;
        }
    }

    public String getSaveFeeling(){
        return this.feeling;
    }
}