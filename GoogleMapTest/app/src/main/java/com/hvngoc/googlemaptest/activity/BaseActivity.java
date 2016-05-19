package com.hvngoc.googlemaptest.activity;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.app.Config;
import com.hvngoc.googlemaptest.app.MyApplication;
import com.hvngoc.googlemaptest.fragment.MessagesFragment;
import com.hvngoc.googlemaptest.fragment.NotificationsFragment;
import com.hvngoc.googlemaptest.gcm.GcmIntentService;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.MessageDelegationHelper;
import com.hvngoc.googlemaptest.services.LocationNotifierService;
import com.hvngoc.googlemaptest.services.LocationResultReceiver;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;

public abstract class BaseActivity extends AppCompatActivity {

    private String TAG = MainPageActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        InitRunCustomMenu();
        initBroadcastReceiver();
        StartLocationServiceHelper();
    }

    private void initBroadcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                // checking for type intent filter
//                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    String token = intent.getStringExtra("token");
//                    Toast.makeText(getApplicationContext(), "GCM registration token: " + token, Toast.LENGTH_LONG).show();
//                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
//                    // gcm registration id is stored in our server's MySQL
//                    Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();
//                } else
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    Bundle bundle = intent.getExtras();
                    String message = bundle.getString("message");
                    String param = bundle.getString("param");
                    if(message.equals(CONSTANT.NOTIFICATION_MESSAGE)) {
                        message_notification.getIcon().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    }
                    else if (message.equals(CONSTANT.NOTIFICATION_HOME)){

                    }
                    else
                        action_notification.setIcon(android.R.drawable.star_big_on);
                    if(messageDelegationHelper != null)
                        messageDelegationHelper.doSomething(message, param);
                }
            }
        };

        if (checkPlayServices()) {
            registerGCM();
        }
    }

    private MessageDelegationHelper messageDelegationHelper;
    public void setMessageDelegationHelper(MessageDelegationHelper helper) {
        this.messageDelegationHelper = helper;
    }

//    *******************************************************************************************************************************

    // starting the service to register with GCM
    public void registerGCM() {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (GcmIntentService.class.getName().equals(service.service.getClassName())) {
//                return;
//            }
//        }
        /*make sure only one server start. it's destroy after finishing work*/
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private boolean checkPlayServices() {
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

    @Override
    protected void onResume() {
        super.onResume();
        registernewGCM();
    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
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

//    ******************************************************************************************************

    // replace current fragment in body_container with new fragment and new titile on Toolbar
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

    protected abstract int getLayoutResource();

    private void StartLocationServiceHelper() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationNotifierService.class.getName().equals(service.service.getClassName())) {
                return;
            }
        }
        LocationResultReceiver locationResultReceiver = new LocationResultReceiver(null);
        locationResultReceiver.setDelegationReceiver(new LocationResultReceiver.DelegationReceiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == 200)
                {
                    action_notification.setIcon(android.R.drawable.star_big_on);
                    if (delegationHelper != null)
                        delegationHelper.doSomeThing();
                }
            }
        });
        Intent intentService = new Intent(getApplicationContext(), LocationNotifierService.class);
        intentService.putExtra("LocationResultReceiver", locationResultReceiver);
        startService(intentService);
    }

    private DelegationHelper delegationHelper;
    public void setDelegationHelper(DelegationHelper helper){
        this.delegationHelper = helper;
    }

//    ****************************************************************************************************

    protected ContextMenuDialogFragment mMenuDialogFragment;
    protected abstract void InitRunCustomMenu();

    private MenuItem message_notification;
    private MenuItem action_notification;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        action_notification = menu.findItem(R.id.action_notification);
        action_notification.setIcon(android.R.drawable.star_big_off);
        message_notification = menu.findItem(R.id.message_notification);
        message_notification.getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_notification:
                Log.i("BASE ACTIVITY", "CLICK NOTIFICATION");
                replaceCurrentFragment(new NotificationsFragment(), "Notification");
                return true;
            case R.id.message_notification:
                message_notification.getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                replaceCurrentFragment(new MessagesFragment(), "Message");
                return true;
            case R.id.action_options:
                Log.i("BASE ACTIVITY", "CLICK OPTIONS");
                if (getSupportFragmentManager().findFragmentByTag(ContextMenuDialogFragment.TAG) == null && mMenuDialogFragment != null) {
                    mMenuDialogFragment.show(getSupportFragmentManager(), ContextMenuDialogFragment.TAG);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded())
            mMenuDialogFragment.dismiss();
        else
            finish();
    }
}
