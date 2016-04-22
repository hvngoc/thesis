package com.hvngoc.googlemaptest.model;

/**
 * Created by 12125_000 on 4/12/2016.
 */
public class Profile {
    public Profile(){

    }
    public String avatar, name, gender, birthday, address, email;
    public int numFriend, numPost, numFollow;
    public Profile(String avatar, String name, String gender, String birthday, String address,
                int numFriend, int numPost, int numFollow){
        this.name = name;
        this.address = address;
        this.avatar = avatar;
        this.gender = gender;
        this.birthday = birthday;
        this.numFollow = numFollow;
        this.numFriend = numFriend;
        this.numPost = numPost;
    }
}
