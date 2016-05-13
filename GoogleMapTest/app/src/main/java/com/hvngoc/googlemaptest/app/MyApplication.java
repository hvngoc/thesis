package com.hvngoc.googlemaptest.app;

import android.app.Application;
import android.util.Log;

import com.hvngoc.googlemaptest.helper.MyPreferenceManager;

/**
 * Created by 12125_000 on 5/10/2016.
 */
public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;

    private MyPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Log.i("MYAPPLICATION", "Created an instance");
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }
}