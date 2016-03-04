package com.hvngoc.googlemaptest.activity;

import android.content.Context;

import com.hvngoc.googlemaptest.model.User;

/**
 * Created by 12125_000 on 1/9/2016.
 */
public class GLOBAL {
    public static Context CurentContext = null;

    public static User CurrentUser = new User("user2", "Tuong V.Nguyen", "default.png");

    public static final String SERVER_URL = "http://192.168.1.18:8084/Neo4jWebAPI/";
}
