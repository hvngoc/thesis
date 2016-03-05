package com.hvngoc.googlemaptest.model;

public class Comment {
    private String id;
    private String content;
    private String commentDate;
    private String userId;
    private String userName, userAvatar;

    public Comment(String id ,String content, String commentDate, String userId, String userName, String userAvatar){
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.commentDate = commentDate;
        this.userName = userName;
        this.userAvatar = userAvatar;
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

    public String getCommentDate() {
        return this.commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
