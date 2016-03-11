package com.hvngoc.googlemaptest.activity;

import android.content.Context;

import com.hvngoc.googlemaptest.model.Post;
import com.hvngoc.googlemaptest.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 12125_000 on 1/9/2016.
 */
public class GLOBAL {
    public static Context CurentContext = null;

    public static User CurrentUser = new User("user2", "Tuong V.Nguyen", "default.png");

    public static final String SERVER_URL = "http://10.0.3.2:8084/Neo4jWebAPI/";

    public static List<Post> CurrentListPost = new ArrayList<Post>();

    public static HashMap<String, Integer> EMOTION = new HashMap<String, Integer>(){
        {
            put(CONSTANT.EMOTION_STRING_FUNNY, CONSTANT.EMOTION_ID_FUNNY);
            put(CONSTANT.EMOTION_STRING_HAPPY, CONSTANT.EMOTION_ID_HAPPY);
            put(CONSTANT.EMOTION_STRING_MONEY, CONSTANT.EMOTION_ID_MONEY);
            put(CONSTANT.EMOTION_STRING_NORMAL, CONSTANT.EMOTION_ID_NORMAL);
            put(CONSTANT.EMOTION_STRING_REFUSE, CONSTANT.EMOTION_ID_REFUSE);
            put(CONSTANT.EMOTION_STRING_SAD, CONSTANT.EMOTION_ID_SAD);
            put(CONSTANT.EMOTION_STRING_SCARED, CONSTANT.EMOTION_ID_SCARED);
            put(CONSTANT.EMOTION_STRING_SLEEPY, CONSTANT.EMOTION_ID_SLEEPY);
            put(CONSTANT.EMOTION_STRING_SURPRISE, CONSTANT.EMOTION_ID_SURPRISE);
            put(CONSTANT.EMOTION_STRING_TERRIBLE, CONSTANT.EMOTION_ID_TERRIBLE);
        }
    };
}
