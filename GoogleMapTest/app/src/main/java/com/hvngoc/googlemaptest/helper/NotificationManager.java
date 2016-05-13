package com.hvngoc.googlemaptest.helper;

import com.hvngoc.googlemaptest.model.MyNotification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 12125_000 on 5/13/2016.
 */
public class NotificationManager {
    private static String currentActivity = "MainPageActivity";
    private static String currentFragment = "Home";
    private static List<MyNotification> notifications = new ArrayList<MyNotification>();

    public static String getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(String currentActivity) {
        NotificationManager.currentActivity = currentActivity;
    }

    public static String getCurrentFragment() {
        return currentFragment;
    }

    public static void setCurrentFragment(String currentFragment) {
        NotificationManager.currentFragment = currentFragment;
    }

    public static List<MyNotification> getNotifications() {
        return notifications;
    }

    public static void addNotification(MyNotification notification) {
        NotificationManager.notifications.add(notification);
    }

    public static void clearNofifications() {
        NotificationManager.notifications.clear();
    }

    public static void setNotifications(List<MyNotification> notifications) {
        NotificationManager.notifications = notifications;
    }
}
