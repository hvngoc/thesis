package com.hvngoc.googlemaptest.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.SparseIntArray;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.StartedUserHelper;
import com.hvngoc.googlemaptest.model.User;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.HashMap;

/**
 * Created by 12125_000 on 1/9/2016.
 */
public class GLOBAL {
    public static Context CurrentContext = null;

    public static User CurrentUser = null;

    public static int MAIN_PAGE_POSITION_VIEW = CONSTANT.NAVIGATION_HOME;

    public static StartedUserHelper startedUserHelper = null;

    public static final String SERVER_URL = "https://my-node-server.herokuapp.com/api/";

    public static final String SERVER_IMAGE_URL = "http://10.0.3.2:9000/images/";

    public static HashMap<String, Integer> EMOTION = new HashMap<String, Integer>(){
        {
            put(CONSTANT.EMOTION_STRING_FUNNY, R.drawable.markers_emotion_funny);
            put(CONSTANT.EMOTION_STRING_HAPPY, R.drawable.markers_emotion_happy);
            put(CONSTANT.EMOTION_STRING_MONEY, R.drawable.markers_emotion_money);
            put(CONSTANT.EMOTION_STRING_NORMAL, R.drawable.markers_emotion_normal);
            put(CONSTANT.EMOTION_STRING_REFUSE, R.drawable.markers_emotion_refuse);
            put(CONSTANT.EMOTION_STRING_SAD, R.drawable.markers_emotion_sad);
            put(CONSTANT.EMOTION_STRING_SCARED, R.drawable.markers_emotion_scared);
            put(CONSTANT.EMOTION_STRING_SLEEPY, R.drawable.markers_emotion_sleepy);
            put(CONSTANT.EMOTION_STRING_SURPRISE, R.drawable.markers_emotion_surprise);
            put(CONSTANT.EMOTION_STRING_TERRIBLE, R.drawable.markers_emotion_terrible);
        }
    };

    public static HashMap<String, String> NOTIFICATION = new HashMap<String, String>(){
        {
            put(CONSTANT.NOTIFICATION_ADD_FRIEND, CONSTANT.NOTIFICATION_STRING_ADD_FRIEND);
            put(CONSTANT.NOTIFICATION_COMMENT, CONSTANT.NOTIFICATION_STRING_COMMENT);
            put(CONSTANT.NOTIFICATION_CONFIRM_FRIEND, CONSTANT.NOTIFICATION_STRING_CONFIRM_FRIEND);
            put(CONSTANT.NOTIFICATION_FRIEND_POST, CONSTANT.NOTIFICATION_STRING_FRIEND_POST);
            put(CONSTANT.NOTIFICATION_MY_POST, CONSTANT.NOTIFICATION_STRING_MY_POST);
        }
    };




//    public static SparseIntArray EMOTION = new SparseIntArray(){
//        {
//            put(R.string.feeling_funny, R.drawable.markers_emotion_funny);
//            put(R.string.feeling_happy, R.drawable.markers_emotion_happy);
//            put(R.string.feeling_money, R.drawable.markers_emotion_money);
//            put(R.string.feeling_normal, R.drawable.markers_emotion_normal);
//            put(R.string.feeling_refuse, R.drawable.markers_emotion_refuse);
//            put(R.string.feeling_sad, R.drawable.markers_emotion_sad);
//            put(R.string.feeling_scare, R.drawable.markers_emotion_scared);
//            put(R.string.feeling_sleepy, R.drawable.markers_emotion_sleepy);
//            put(R.string.feeling_surprise, R.drawable.markers_emotion_surprise);
//            put(R.string.feeling_terrible, R.drawable.markers_emotion_terrible);
//        }
//    };

//    public static HashMap<String, Integer> NOTIFICATION = new HashMap<String, Integer>(){
//        {
//            put(CONSTANT.NOTIFICATION_ADD_FRIEND, R.string.notification_string_add_friend);
//            put(CONSTANT.NOTIFICATION_COMMENT, R.string.notification_string_comment);
//            put(CONSTANT.NOTIFICATION_CONFIRM_FRIEND, R.string.notification_string_confirm_friend);
//            put(CONSTANT.NOTIFICATION_FRIEND_POST, R.string.notification_string_friend_post);
//            put(CONSTANT.NOTIFICATION_MY_POST, R.string.notification_string_my_post);
//        }
//    };
}
