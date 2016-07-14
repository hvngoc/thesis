package com.hvngoc.googlemaptest.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.app.Config;
import com.hvngoc.googlemaptest.app.MyApplication;
import com.hvngoc.googlemaptest.custom.ChangeLanguageDialog;
import com.hvngoc.googlemaptest.custom.ChangePasswordDialog;
import com.hvngoc.googlemaptest.custom.DefaultLocationDialog;
import com.hvngoc.googlemaptest.custom.ReportDialog;
import com.hvngoc.googlemaptest.custom.SettingDialog;
import com.hvngoc.googlemaptest.fragment.FragmentDrawer;
import com.hvngoc.googlemaptest.fragment.FriendsFragment;
import com.hvngoc.googlemaptest.fragment.HomeFragment;
import com.hvngoc.googlemaptest.fragment.LogoutFragment;
import com.hvngoc.googlemaptest.fragment.MessagesFragment;
import com.hvngoc.googlemaptest.fragment.NotificationsFragment;
import com.hvngoc.googlemaptest.gcm.GcmIntentService;
import com.hvngoc.googlemaptest.helper.BarBadgeHelper;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.MessageDelegationHelper;
import com.hvngoc.googlemaptest.helper.StartedSettingHelper;
import com.hvngoc.googlemaptest.model.AppSetting;
import com.hvngoc.googlemaptest.model.BottomMessage;
import com.hvngoc.googlemaptest.model.User;
import com.hvngoc.googlemaptest.services.LocationNotifierService;
import com.hvngoc.googlemaptest.services.LocationResultReceiver;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainPageActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private FragmentDrawer drawerFragment;
    private String TAG = MainPageActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    protected BroadcastReceiver mRegistrationBroadcastReceiver;

    private Fragment fragmentHome, fragmentNotification, fragmentMessage, fragmentFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity:", " MainPageActivity");
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerFragment = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);

        initBottomBar(savedInstanceState);
        StartLocationServiceHelper(false);
        initBroadcastReceiver();
        registernewGCM();

        startLoadingBottomMessage();
    }

    BottomBar bottomBar;
    private void initBottomBar(Bundle savedInstanceState) {
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.noTopOffset();
        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen)
                            bottomBar.hide();
                        else
                            bottomBar.show();
                    }
                });
        bottomBar.setItemsFromMenu(R.menu.menu_bottom_bar, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                Fragment fragment = null;
                String title = getString(R.string.title_home);
                switch (itemId) {
                    case R.id.home_item:
                        if (fragmentHome == null)
                            fragmentHome = new HomeFragment();
                        fragment = fragmentHome;
                        title = getString(R.string.title_home);
                        break;
                    case R.id.chat_item:
                        if (fragmentMessage == null)
                            fragmentMessage = new MessagesFragment();
                        fragment = fragmentMessage;
                        title = getString(R.string.title_messages);
                        break;
                    case R.id.notification_item:
                        if (fragmentNotification == null)
                            fragmentNotification = new NotificationsFragment();
                        fragment = fragmentNotification;
                        title = getString(R.string.title_notifications);
                        break;
                    case R.id.friend_item:
                        if (fragmentFriend == null)
                            fragmentFriend = new FriendsFragment();
                        fragment = fragmentFriend;
                        title = getString(R.string.title_friends);
                        break;
                }
                replaceCurrentFragment(fragment, title);
            }
        });
//        initBarBadgeHelper();
    }

    private void initBarBadgeHelper() {
        BarBadgeHelper.Notification = bottomBar.makeBadgeForTabAt(CONSTANT.BOTTOM_NOTIFICATION, "#E91E63", BarBadgeHelper.notificationCount);
        BarBadgeHelper.ChatMessage = bottomBar.makeBadgeForTabAt(CONSTANT.BOTTOM_MESSAGE, "#E91E63", BarBadgeHelper.chatMessageCount);
        BarBadgeHelper.Friend = bottomBar.makeBadgeForTabAt(CONSTANT.BOTTOM_FRIEND, "#E91E63", BarBadgeHelper.friendCount);
    }

    private void startLoadingBottomMessage(){
        new LoadStartNotificationBottomMessage().execute();
    }

//    *******************************************************************************************************************

    private class LoadStartNotificationBottomMessage extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }

        private Boolean postData() {
            String serverUrl = GLOBAL.SERVER_URL + "loadStartNotification";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                BottomMessage b = gson.fromJson(res, BottomMessage.class);
                BarBadgeHelper.chatMessageCount += b.getNumMessage();
                BarBadgeHelper.friendCount += b.getNumFriend();
                BarBadgeHelper.notificationCount += b.getNumNotification();
                initBarBadgeHelper();
            }
        }
    }

//    *********************************************************************************************************************

    @Override
    protected void onResume() {
        super.onResume();
        GLOBAL.CurrentContext = this;
            // display the first navigation drawer view on app launch
        setBottomBar(GLOBAL.MAIN_PAGE_POSITION_VIEW);
        displayView(GLOBAL.MAIN_PAGE_POSITION_VIEW);
        GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.BOTTOM_HOME;
        drawerFragment.setLanguageAgain();
        drawerFragment.setPictureProfile();
    }

    private void setBottomBar(int position) {
        bottomBar.selectTabAtPosition(position, true);
    }

    @Override
    public void onDrawerItemSelected(int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = null;
        switch (position) {
            case CONSTANT.BOTTOM_HOME:
                if (fragmentHome == null)
                    fragmentHome = new HomeFragment();
                fragment = fragmentHome;
                title = getString(R.string.title_home);
                break;
            case CONSTANT.BOTTOM_MESSAGE:
                if (fragmentMessage == null)
                    fragmentMessage = new MessagesFragment();
                fragment = fragmentMessage;
                title = getString(R.string.title_messages);
                break;
            case CONSTANT.BOTTOM_FRIEND:
                if (fragmentFriend == null)
                    fragmentFriend = new FriendsFragment();
                fragment = fragmentFriend;
                title = getString(R.string.title_friends);
                break;
            case CONSTANT.BOTTOM_NOTIFICATION:
                if (fragmentNotification == null)
                    fragmentNotification = new NotificationsFragment();
                fragment = fragmentNotification;
                title = getString(R.string.title_notifications);
                break;
            case CONSTANT.NAVIGATION_WALL:
                GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.BOTTOM_HOME;
                Intent intent = new Intent(this, WallActivity.class);
                intent.putExtra("id", GLOBAL.CurrentUser.getId());
                startActivity(intent);
                return;
            case CONSTANT.NAVIGATION_MAP:
                GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.BOTTOM_HOME;
                startActivity(new Intent(this, MapsActivity.class));
                return;
            case CONSTANT.NAVIGATION_TOUR:
                GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.BOTTOM_HOME;
                startActivity(new Intent(this, TourActivity.class));
                return;
            case CONSTANT.NAVIGATION_LANGUAGE:
                ChangeLanguageDialog changeLanguageDialog = new ChangeLanguageDialog();
                changeLanguageDialog.show(getSupportFragmentManager(), "ChangeLanguageDialog");
                changeLanguageDialog.setHelper(new DelegationHelper() {
                    @Override
                    public void doSomeThing() {
                        drawerFragment.setLanguageAgain();
                        GLOBAL.initNOTIFICATION();
                    }
                });
                return;
            case CONSTANT.NAVIGATION_SETTING:
                SettingDialog settingDialog = new SettingDialog();
                settingDialog.setDelegationHelper(new DelegationHelper() {
                    @Override
                    public void doSomeThing() {
                        StartLocationServiceHelper(true);
                    }
                });
                settingDialog.show(getSupportFragmentManager(), "SettingDialog");
                return;
            case CONSTANT.NAVIGATION_CHANGE_PASSWORD:
                ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
                changePasswordDialog.show(getSupportFragmentManager(), "ChangePasswordDialog");
                return;
            case CONSTANT.NAVIGATION_CHANGE_LOCATION:
                DefaultLocationDialog defaultLocationDialog = new DefaultLocationDialog();
                defaultLocationDialog.show(getSupportFragmentManager(), "DefaultLocationDialog");
                return;
            case CONSTANT.NAVIGATION_REPORT:
                ReportDialog reportDialog = new ReportDialog();
                reportDialog.show(getSupportFragmentManager(), "ReportDialog");
                return;
            case CONSTANT.NAVIGATION_LOGOUT:
                fragment = new LogoutFragment();
                title = getString(R.string.title_logout);
                break;
            default:
                break;
        }
        replaceCurrentFragment(fragment, title);
    }

    @Override
    protected void onPause() {
        super.onPause();
        messageDelegationHelper = null;
    }

    public void unregisterPlayService(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    private MessageDelegationHelper messageDelegationHelper = null;
    public void setMessageDelegationHelper(MessageDelegationHelper messageHelper) {
        this.messageDelegationHelper = messageHelper;
    }

    protected void initBroadcastReceiver() {
        if (mRegistrationBroadcastReceiver != null)
            unregisterPlayService();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("BROADCASTRECEIVER", "Createeeeeeeeeeee");
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    Bundle bundle = intent.getExtras();
                    String message = bundle.getString("message");
                    String param = bundle.getString("param");
                    String targetID = bundle.getString("targetID");
                    if(targetID.contains(GLOBAL.CurrentUser.getId())) {
                        BottomBarBadge unreadMessages;
                        if (message.equals(CONSTANT.NOTIFICATION_MESSAGE)) {
                            if (messageDelegationHelper != null)
                                messageDelegationHelper.doSomething(message, param);
                            unreadMessages = BarBadgeHelper.ChatMessage;
                            unreadMessages.setCount(++BarBadgeHelper.chatMessageCount);
                            Log.i("MESSAGEEEEEEEEEEEEEEE", "" + BarBadgeHelper.chatMessageCount);
                        } else if (message.equals(CONSTANT.NOTIFICATION_ADD_FRIEND)) {
                            if (messageDelegationHelper != null)
                                messageDelegationHelper.doSomething(message, param);
                            unreadMessages = BarBadgeHelper.Friend;
                            unreadMessages.setCount(++BarBadgeHelper.friendCount);
                        } else {
                            if (messageDelegationHelper != null)
                                messageDelegationHelper.doSomething(message, param);
                            unreadMessages = BarBadgeHelper.Notification;
                            unreadMessages.setCount(++BarBadgeHelper.notificationCount);
                        }
                        unreadMessages.show();
                    }
                }
            }
        };

        if (checkPlayServices()) {
            registerGCM();
        }
    }

    public void registerGCM() {
        /*make sure only one server start. it's destroy after finishing work*/
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    public boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void registernewGCM() {
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        MyApplication.getInstance().getPrefManager().clear();
    }


    private DelegationHelper delegationHelper;
    public void setDelegationHelper(DelegationHelper helper) {
        this.delegationHelper = helper;
    }


    private void StartLocationServiceHelper(boolean again) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationNotifierService.class.getName().equals(service.service.getClassName())) {
                if (again){
                    stopService(new Intent(getApplicationContext(), LocationNotifierService.class));
                    break;
                }
                else {
                    return;
                }
            }
        }
        StartedSettingHelper startedSettingHelper = new StartedSettingHelper(this);
        AppSetting appSetting = startedSettingHelper.getSetting();

        if (!appSetting.isBackground())
            return;

        LocationResultReceiver locationResultReceiver = new LocationResultReceiver(null);
        locationResultReceiver.setDelegationReceiver(new LocationResultReceiver.DelegationReceiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == 200) {
                    BottomBarBadge unreadMessages = BarBadgeHelper.Notification;
                    unreadMessages.setCount(++BarBadgeHelper.notificationCount);
                    unreadMessages.show();
                    if(delegationHelper != null)
                        delegationHelper.doSomeThing();
                }
            }
        });
        Intent intentService = new Intent(getApplicationContext(), LocationNotifierService.class);
        intentService.putExtra("LocationResultReceiver", locationResultReceiver);
        intentService.putExtra("time", appSetting.getTime());
        intentService.putExtra("distance", appSetting.getDistance());
        startService(intentService);
    }

    protected void replaceCurrentFragment(Fragment fragment, String title) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            if(title != null)
                setActionBarTitle(title);
        }
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

}
