package com.hvngoc.googlemaptest.model;

public class Comment {
    private String id;
    private String content;
    private String userId;
    private String commentDate;

    public Comment(String id ,String content, String userId, String commentDate){
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.commentDate = commentDate;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostDate() {
        return this.commentDate;
    }

    public void setPostDate(String commentDate) {
        this.commentDate = commentDate;
    }
}
