package com.hvngoc.googlemaptest.model;

import java.io.Serializable;

public class Post implements Serializable {
    private String postID;
    private String content;
    private String listImages;
    private String postDate;
    public String feeling;
    public Double Latitude, Longitude;

    public String userName, userAvatar;
    public String relationShip;

    public int numLike, numShare, numComment;

    public int isYouLike;

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
        this.feeling = feeling;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.relationShip = relationShip;

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

    public void setContent(String content) {
        this.content = content;
    }

    public String getListImages() {
        return listImages;
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
}