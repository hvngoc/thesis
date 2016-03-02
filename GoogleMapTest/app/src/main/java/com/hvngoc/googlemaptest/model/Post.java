package com.hvngoc.googlemaptest.model;

/**
 * Created by 12125_000 on 3/2/2016.
 */
public class Post {
    private String postID;
    private String content;
    private String listImages;
    private String numLike;
    private String postDate;
    private String location;

    public Post (String postID, String content, String listImages, String numLike,
                 String postDate, String location) {
        this.setPostID(postID);
        this.setContent(content);
        this.setListImages(listImages);
        this.setNumLike(numLike);
        this.setPostDate(postDate);
        this.setLocation(location);
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

    public String getNumLike() {
        return numLike;
    }

    public void setNumLike(String numLike) {
        this.numLike = numLike;
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
