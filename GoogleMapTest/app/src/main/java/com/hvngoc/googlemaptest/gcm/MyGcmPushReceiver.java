package com.hvngoc.googlemaptest.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.hvngoc.googlemaptest.activity.MainPageActivity;
import com.hvngoc.googlemaptest.app.Config;

/**
 * Created by 12125_000 on 5/10/2016.
 */
public class MyGcmPushReceiver extends GcmListenerService {

    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *               For Set of keys use data.keySet().
     */

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String title = bundle.getString("title");
        String message = bundle.getString("message");
        String param = bundle.getString("param");
        String targetID = bundle.getString("targetID");

        String timestamp = bundle.getString("created_at");
        Log.e(TAG, "From: " + from);
        Log.e(TAG, "Title: " + title);
        Log.e(TAG, "message: " + message);
        Log.e(TAG, "timestamp: " + timestamp);

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            pushNotification.putExtra("param", param);
            pushNotification.putExtra("targetID", targetID);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils();
            notificationUtils.playNotificationSound();
        } else {
            // App is in background!
            Intent resultIntent = new Intent(getApplicationContext(), MainPageActivity.class);
            resultIntent.putExtra("message", message);
            resultIntent.putExtra("param", param);
            resultIntent.putExtra("targetID", targetID);
            showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MYGCM", "Created");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MYGCM", "Destroyed");
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

}
