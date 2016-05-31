package com.hvngoc.googlemaptest.activity;

import android.content.Context;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.StartedUserHelper;
import com.hvngoc.googlemaptest.model.Profile;
import com.hvngoc.googlemaptest.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by 12125_000 on 1/9/2016.
 */
public class GLOBAL {
    public static Context CurrentContext = null;

    public static User CurrentUser = null;

    public static Profile CurrentUserProfile = null;

    public static int MAIN_PAGE_POSITION_VIEW = CONSTANT.NAVIGATION_HOME;

    public static StartedUserHelper startedUserHelper = null;

    public static final String SERVER_URL = "http://10.0.3.2:9000/api/";
    public static final String SERVER_IMAGE_URL = "http://10.0.3.2:9000/images/";
    //public static final String SERVER_URL = "https://my-node-server.herokuapp.com/api/";
    //public static final String SERVER_IMAGE_URL = "https://my-node-server.herokuapp.com/images/";

    public static HashMap<String, String> NOTIFICATION = null;

    public static HashMap<String, String> RELATIONSHIP = null;

    public static HashMap<String, ArrayList<Object>> EMOTION = null;

    public static void initNOTIFICATION(){
        NOTIFICATION = new HashMap<String, String>(){
            {
                put(CONSTANT.NOTIFICATION_ADD_FRIEND, CurrentContext.getString(R.string.notification_string_add_friend));
                put(CONSTANT.NOTIFICATION_COMMENT, CurrentContext.getString(R.string.notification_string_comment));
                put(CONSTANT.NOTIFICATION_CONFIRM_FRIEND, CurrentContext.getString(R.string.notification_string_confirm_friend));
                put(CONSTANT.NOTIFICATION_FRIEND_POST, CurrentContext.getString(R.string.notification_string_friend_post));
                put(CONSTANT.NOTIFICATION_MY_POST, CurrentContext.getString(R.string.notification_string_my_post));
            }
        };
        RELATIONSHIP = new HashMap<String, String>(){
            {
                put(CONSTANT.RELATIONSHIP_POST, CurrentContext.getString(R.string.relation_post));
                put(CONSTANT.RELATIONSHIP_SHARE, CurrentContext.getString(R.string.relation_share));
            }
        };
        EMOTION = new HashMap<String, ArrayList<Object>>(){
            {
                put(CONSTANT.EMOTION_STRING_FUNNY, new ArrayList<Object>(Arrays.asList(GLOBAL.CurrentContext.getString(R.string.feeling_funny) ,R.drawable.markers_emotion_funny)));
                put(CONSTANT.EMOTION_STRING_HAPPY, new ArrayList<Object>(Arrays.asList(GLOBAL.CurrentContext.getString(R.string.feeling_happy), R.drawable.markers_emotion_happy)));
                put(CONSTANT.EMOTION_STRING_MONEY, new ArrayList<Object>(Arrays.asList(GLOBAL.CurrentContext.getString(R.string.feeling_money), R.drawable.markers_emotion_money)));
                put(CONSTANT.EMOTION_STRING_NORMAL, new ArrayList<Object>(Arrays.asList(GLOBAL.CurrentContext.getString(R.string.feeling_normal), R.drawable.markers_emotion_normal)));
                put(CONSTANT.EMOTION_STRING_REFUSE, new ArrayList<Object>(Arrays.asList(GLOBAL.CurrentContext.getString(R.string.feeling_refuse), R.drawable.markers_emotion_refuse)));
                put(CONSTANT.EMOTION_STRING_SAD, new ArrayList<Object>(Arrays.asList(GLOBAL.CurrentContext.getString(R.string.feeling_sad), R.drawable.markers_emotion_sad)));
                put(CONSTANT.EMOTION_STRING_SCARED, new ArrayList<Object>(Arrays.asList(GLOBAL.CurrentContext.getString(R.string.feeling_scare), R.drawable.markers_emotion_scared)));
                put(CONSTANT.EMOTION_STRING_SLEEPY, new ArrayList<Object>(Arrays.asList(GLOBAL.CurrentContext.getString(R.string.feeling_sleepy), R.drawable.markers_emotion_sleepy)));
                put(CONSTANT.EMOTION_STRING_SURPRISE, new ArrayList<Object>(Arrays.asList(GLOBAL.CurrentContext.getString(R.string.feeling_surprise), R.drawable.markers_emotion_surprise)));
                put(CONSTANT.EMOTION_STRING_TERRIBLE, new ArrayList<Object>(Arrays.asList(GLOBAL.CurrentContext.getString(R.string.feeling_terrible), R.drawable.markers_emotion_terrible)));
            }
        };
    }
}
