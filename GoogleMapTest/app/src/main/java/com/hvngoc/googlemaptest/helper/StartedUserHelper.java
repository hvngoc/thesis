package com.hvngoc.googlemaptest.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.model.User;

/**
 * Created by Hoang Van Ngoc on 23/03/2016.
 */
public class StartedUserHelper {
    private static final String SHARED_REFERENCES = "MySharedRef";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String AVATAR = "avatar";
    private static final String DEFAULT_LATITUDE = "defaultLatitude";
    private static final String DEFAULT_LONGITUDE = "defaultLongitude";

    private SharedPreferences sharedRef;
    private SharedPreferences.Editor editor;

    public StartedUserHelper(Context context){
        sharedRef = context.getSharedPreferences(SHARED_REFERENCES, Context.MODE_PRIVATE);
    }

    public  void saveUser(User user){
        editor = sharedRef.edit();
        editor.putString(ID, user.getId());
        editor.putString(NAME, user.getName());
        editor.putString(AVATAR, user.getAvatar());
        editor.putFloat(DEFAULT_LATITUDE, (float) user.getDefaultLatitude());
        editor.putFloat(DEFAULT_LONGITUDE, (float) user.getDefaultLongitude());
        editor.apply();
    }

    public boolean getUser(){
        String id = sharedRef.getString(ID, null);
        if (id == null)
            return false;
        String name = sharedRef.getString(NAME, null);
        String avatar = sharedRef.getString(AVATAR, null);
        double defaultLatitude = (double)sharedRef.getFloat(DEFAULT_LATITUDE, (float)0.0);
        double defaultLongitude = (double)sharedRef.getFloat(DEFAULT_LONGITUDE, (float)0.0);
        GLOBAL.CurrentUser = new User(id, name, avatar, defaultLatitude, defaultLongitude);
        return true;
    }

    public  void clear(){
        editor = sharedRef.edit();
        editor.clear();
        editor.apply();
    }
}
