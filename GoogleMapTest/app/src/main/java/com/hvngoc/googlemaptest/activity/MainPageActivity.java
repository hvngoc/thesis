package com.hvngoc.googlemaptest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.fragment.FragmentDrawer;
import com.hvngoc.googlemaptest.fragment.FriendsFragment;
import com.hvngoc.googlemaptest.fragment.HomeFragment;
import com.hvngoc.googlemaptest.fragment.LogoutFragment;
import com.hvngoc.googlemaptest.fragment.NotificationsFragment;
import com.hvngoc.googlemaptest.fragment.ProfileFragment;
import com.hvngoc.googlemaptest.fragment.SettingsFragment;
import com.hvngoc.googlemaptest.fragment.WallFragment;
import com.hvngoc.googlemaptest.helper.NotificationManager;


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
        Log.i("USERID", GLOBAL.CurrentUser.getId());
        // display the first navigation drawer view on app launch
        displayView(0);
    }



    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void InitRunCustomMenu() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case 1:
                fragment = ProfileFragment.getInstance(GLOBAL.CurrentUser.getId(), CONSTANT.TYPE_ME);
                title = getString(R.string.title_profile);
                break;
            case 2:
                fragment = WallFragment.getInstance(GLOBAL.CurrentUser.getId());
                title = getString(R.string.title_wall);
                break;
            case 3:
                fragment = new FriendsFragment();
                title = getString(R.string.title_friends);
                break;
            case 4:
                fragment = new NotificationsFragment();
                title = getString(R.string.title_notifications);
                break;
            case 5:
                //fragment = new MessagesFragment();
                //title = getString(R.string.title_messages);
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                break;
            case 6:
                fragment = new SettingsFragment();
                title = getString(R.string.title_settings);
                break;
            case 7:
                fragment = new LogoutFragment();
                title = getString(R.string.title_logout);
                break;
            default:
                break;
        }

        replaceCurrentFragment(fragment, title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String fragmentName = NotificationManager.getCurrentFragment();
        Fragment fragment = null;
        switch (fragmentName){
            case CONSTANT.NAME_HOME_FRAGMENT:
                fragment = new HomeFragment();
                break;
            case CONSTANT.NAME_NOTIFICATION_FRAGMENT:
                fragment = new NotificationsFragment();
                break;
            case CONSTANT.NAME_FRIEND_FRAGMENT:
                fragment = new FriendsFragment();
                break;
            default:
                fragment = new HomeFragment();
        }
        replaceCurrentFragment(fragment, fragmentName);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }
}
