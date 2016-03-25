package com.hvngoc.googlemaptest.model;

/**
 * Created by Hoang Van Ngoc on 24/03/2016.
 */
public class Friend {

    private String id;
    private String name;
    private String avatar;

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private int numFriend;
    private int mutualFriend;

    public  Friend(String id, String name, String avatar, int numFriend, int mutualFriend){
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.numFriend = numFriend;
        this.mutualFriend = mutualFriend;
    }

    public  int getNumFriend(){
        return  this.numFriend;
    }

    public void setNumFriend(int numFriend){
        this.numFriend = numFriend;
    }

    public  int getMutualFriend(){
        return this.mutualFriend;
    }

    public  void setMutualFriend(int mutualFriend){
        this.mutualFriend = mutualFriend;
    }
}
