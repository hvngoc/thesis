package com.hvngoc.googlemaptest.activity;

import android.content.Context;

import com.hvngoc.googlemaptest.model.Post;
import com.hvngoc.googlemaptest.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12125_000 on 1/9/2016.
 */
public class GLOBAL {
    public static Context CurentContext = null;

    public static User CurrentUser = new User("user2", "Tuong V.Nguyen", "default.png");

    public static final String SERVER_URL = "http://10.0.3.2:8084/Neo4jWebAPI/";

    public static List<Post> CurrentListPost = new ArrayList<Post>();
}
