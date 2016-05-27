package com.hvngoc.googlemaptest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.app.Config;
import com.hvngoc.googlemaptest.custom.AboutDialog;
import com.hvngoc.googlemaptest.custom.ChangeLanguageDialog;
import com.hvngoc.googlemaptest.custom.ChangePasswordDialog;
import com.hvngoc.googlemaptest.custom.ConfirmDialog;
import com.hvngoc.googlemaptest.custom.DefaultLocationDialog;
import com.hvngoc.googlemaptest.custom.HelpDialog;
import com.hvngoc.googlemaptest.custom.ReportDialog;
import com.hvngoc.googlemaptest.custom.SettingDialog;
import com.hvngoc.googlemaptest.fragment.FragmentDrawer;
import com.hvngoc.googlemaptest.fragment.FriendsFragment;
import com.hvngoc.googlemaptest.fragment.HomeFragment;
import com.hvngoc.googlemaptest.fragment.LogoutFragment;
import com.hvngoc.googlemaptest.fragment.MessagesFragment;
import com.hvngoc.googlemaptest.fragment.NotificationsFragment;
import com.hvngoc.googlemaptest.fragment.ProfileFragment;
import com.hvngoc.googlemaptest.fragment.WallFragment;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.LanguageHelper;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabSelectedListener;


public class MainPageActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener {

    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity:", " MainPageActivity");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerFragment = (FragmentDrawer)getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        GLOBAL.CurrentContext = this;
        drawerFragment.setPictureProfile();
        initBottomBar(savedInstanceState);
    }

    BottomBar bottomBar;
    private void initBottomBar(Bundle savedInstanceState) {
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.noTopOffset();
        bottomBar.setItemsFromMenu(R.menu.menu_bottom_bar, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {
                Fragment fragment = new HomeFragment();
                String title = "Home";
                switch (itemId) {
                    case R.id.home_item:
                        fragment = new HomeFragment();
                        title = "Home";
                        break;
                    case R.id.chat_item:
                        fragment = new MessagesFragment();
                        title = "Message";
                        break;
                    case R.id.notification_item:
                        fragment = new NotificationsFragment();
                        title = "Notification";
                        break;
                    case R.id.map_item:
                        GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.NAVIGATION_HOME;
                        startActivity(new Intent(MainPageActivity.this, MapsActivity.class));
                        break;
                }
                replaceCurrentFragment(fragment, title);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // display the first navigation drawer view on app launch
        displayView(GLOBAL.MAIN_PAGE_POSITION_VIEW);
        setBottomBar(GLOBAL.MAIN_PAGE_POSITION_VIEW);
        GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.NAVIGATION_HOME;
        drawerFragment.setLanguageAgain();
    }

    private void setBottomBar(int position) {
        bottomBar.selectTabAtPosition(0, true);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void InitRunCustomMenu() {

    }

    @Override
    public void onDrawerItemSelected(int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = null;
        switch (position) {
            case CONSTANT.NAVIGATION_HOME:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case CONSTANT.NAVIGATION_PROFILE:
                fragment = ProfileFragment.getInstance(GLOBAL.CurrentUser.getId(), CONSTANT.TYPE_ME);
                title = getString(R.string.title_profile);
                break;
            case CONSTANT.NAVIGATION_WALL:
                fragment = WallFragment.getInstance(GLOBAL.CurrentUser.getId());
                title = getString(R.string.title_wall);
                break;
            case CONSTANT.NAVIGATION_MAP:
                startActivity(new Intent(MainPageActivity.this, MapsActivity.class));
                return;
            case CONSTANT.NAVIGATION_FRIEND:
                fragment = new FriendsFragment();
                title = getString(R.string.title_friends);
                break;
            case CONSTANT.NAVIGATION_NOTIFICATION:
                fragment = new NotificationsFragment();
                title = getString(R.string.title_notifications);
                break;
            case CONSTANT.NAVIGATION_MESSAGE:
                fragment = new MessagesFragment();
                title = getString(R.string.title_messages);
                break;
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
            case CONSTANT.NAVIGATION_ABOUT:
                AboutDialog aboutDialog = new AboutDialog();
                aboutDialog.show(getSupportFragmentManager(), "AboutDialog");
                return;
            case CONSTANT.NAVIGATION_HELP:
                HelpDialog helpDialog = new HelpDialog();
                helpDialog.show(getSupportFragmentManager(), "HelpDialog");
                return;
            case CONSTANT.NAVIGATION_REPORT:
                ReportDialog reportDialog = new ReportDialog();
                reportDialog.show(getSupportFragmentManager(), "ReportDialog");
                return;
            case CONSTANT.NAVIGATION_LOGOUT:
                fragment = new LogoutFragment();
                title = getString(R.string.title_logout);
                break;
            case CONSTANT.NAVIGATION_CLOSE:
                ConfirmDialog confirmDialog = new ConfirmDialog(MainPageActivity.this, getString(R.string.sure_logout));
                confirmDialog.setOnButtonOKClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                confirmDialog.show();
                return;
            default:
                break;
        }
        replaceCurrentFragment(fragment, title);
    }

    @Override
    protected void initBroadcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    Bundle bundle = intent.getExtras();
                    String message = bundle.getString("message");
                    String param = bundle.getString("param");
                    if(message.equals(CONSTANT.NOTIFICATION_MESSAGE)) {
                        BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(2, "#E91E63", 1);
                        unreadMessages.show();
                        unreadMessages.setAnimationDuration(200);
                    }
                    else if (message.equals(CONSTANT.NOTIFICATION_HOME)){

                    }
                    else {
                        BottomBarBadge unreadMessages = bottomBar.makeBadgeForTabAt(1, "#E91E63", 1);
                        unreadMessages.show();
                        unreadMessages.setAnimationDuration(200);
                    }
                    if(messageDelegationHelper != null)
                        messageDelegationHelper.doSomething(message, param);
                }
            }
        };

        if (checkPlayServices()) {
            registerGCM();
        }
    }

}
