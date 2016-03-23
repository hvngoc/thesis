package com.hvngoc.googlemaptest.model;

import java.io.Serializable;

public class Post implements Serializable {
    private String postID;
    private String content;
    private String listImages;
    private String postDate;
    private String location;
    public String feeling;

    public String userName, userAvatar;
    public String relationShip;

    public int numLike, numShare, numComment;

    public Post (String postID, String content, String listImages,
                 String postDate, String location, String feeling,
                 String userName, String userAvatar, String relationShip,
                 int numLike, int numShare, int numComment) {
        this.setPostID(postID);
        this.setContent(content);
        this.setListImages(listImages);
        this.setPostDate(postDate);
        this.setLocation(location);
        this.feeling = feeling;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.relationShip = relationShip;

        this.numLike = numLike;
        this.numShare = numShare;
        this.numComment = numComment;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}