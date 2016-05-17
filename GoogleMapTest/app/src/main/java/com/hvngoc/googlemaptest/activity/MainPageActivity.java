package com.hvngoc.googlemaptest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.fragment.FragmentDrawer;
import com.hvngoc.googlemaptest.fragment.FriendsFragment;
import com.hvngoc.googlemaptest.fragment.HomeFragment;
import com.hvngoc.googlemaptest.fragment.LogoutFragment;
import com.hvngoc.googlemaptest.fragment.MessagesFragment;
import com.hvngoc.googlemaptest.fragment.NotificationsFragment;
import com.hvngoc.googlemaptest.fragment.ProfileFragment;
import com.hvngoc.googlemaptest.fragment.SettingsFragment;
import com.hvngoc.googlemaptest.fragment.WallFragment;


public class MainPageActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity:", " MainPageActivity");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        GLOBAL.CurentContext = this;
        drawerFragment.setPictureProfile();
        // display the first navigation drawer view on app launch
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayView(GLOBAL.MAIN_PAGE_STRING_VIEW);
        GLOBAL.MAIN_PAGE_STRING_VIEW = getString(R.string.title_home);
        Log.i("GLOBAL TITLE", GLOBAL.MAIN_PAGE_STRING_VIEW);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void InitRunCustomMenu() {

    }

    @Override
    public void onDrawerItemSelected(String title) {
        displayView(title);
    }

    private void displayView(String title) {
        Log.i("MAIN PAGE:", title);
        Fragment fragment = null;
        switch (title) {
            case "Home":
                fragment = new HomeFragment();
                break;
            case "Profile":
                fragment = ProfileFragment.getInstance(GLOBAL.CurrentUser.getId(), CONSTANT.TYPE_ME);
                break;
            case "Wall":
                fragment = WallFragment.getInstance(GLOBAL.CurrentUser.getId());
                break;
            case "Map":
                startActivity(new Intent(MainPageActivity.this, MapsActivity.class));
                return;
            case "Friends":
                fragment = new FriendsFragment();
                break;
            case "Notifications":
                fragment = new NotificationsFragment();
                break;
            case "Messages":
                fragment = new MessagesFragment();
                //Intent intent = new Intent(this, ChatActivity.class);
                //startActivity(intent);
                break;
            case "Settings":
                fragment = new SettingsFragment();
                break;
            case "Language":
                break;
            case "About":
                break;
            case "Help":
                break;
            case "Report":
                break;
            case "Log Out":
                fragment = new LogoutFragment();
                break;
            case "Close":
                //System.exit(0);
                break;
            default:
                break;
        }
        replaceCurrentFragment(fragment, title);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
}
