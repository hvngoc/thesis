package com.hvngoc.googlemaptest.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.hvngoc.googlemaptest.model.AppSetting;

/**
 * Created by Hoang Van Ngoc on 23/06/2016.
 */
public class StartedSettingHelper {
    private final String SHARED_REFERENCES = "HOMFY_SETTING";
    private final String BACKGROUND = "background";
    private final String TIME = "time";
    private final String DISTANCE = "distance";

    private SharedPreferences sharedRef;

    public StartedSettingHelper(Context context){
        sharedRef = context.getSharedPreferences(SHARED_REFERENCES, Context.MODE_PRIVATE);
    }

    public void saveSetting(AppSetting appSetting){
        SharedPreferences.Editor editor = sharedRef.edit();
        editor.putBoolean(BACKGROUND, appSetting.isBackground());
        editor.putInt(TIME, appSetting.getTime());
        editor.putInt(DISTANCE, appSetting.getDistance());
        editor.apply();
    }

    public AppSetting getSetting(){
        boolean background = sharedRef.getBoolean(BACKGROUND, true);
        int time = sharedRef.getInt(TIME, 60000);
        int distance = sharedRef.getInt(DISTANCE, 500);
        return new AppSetting(background, time, distance);
    }

}
