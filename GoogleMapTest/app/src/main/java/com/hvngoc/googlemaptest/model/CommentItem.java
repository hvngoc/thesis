package com.hvngoc.googlemaptest.model;

public class CommentItem {
    private String userName;
    private int idAvatar;
    private String commentString;

    public CommentItem(String userName, int idAvatar, String commentString){
        this.commentString = commentString;
        this.idAvatar = idAvatar;
        this.userName = userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }
    public  String getUserName(){
        return  this.userName;
    }

    public int getIdAvatar(){
        return this.idAvatar;
    }
    public void setIdAvatar(int id){
        idAvatar = id;
    }

    public String getCommentString(){
        return  this.commentString;
    }
    public void setCommentString(String commentString){
        this.commentString = commentString;
    }
}
