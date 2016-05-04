package com.hvngoc.googlemaptest.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

    private FragmentDrawer drawerFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity:", " MainPageActivity");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        GLOBAL.CurentContext = this;
        // display the first navigation drawer view on app launch
        displayView(0);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }


    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // newText is text entered by user to SearchView
            Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_LONG).show();
            return false;
        }
    };



    private SearchView mSearchView;
    private MenuItem searchMenuItem;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Get the SearchView and set the searchable configuration
        return true;
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
                fragment = new ProfileFragment();
                title = getString(R.string.title_profile);
                break;
            case 2:
                fragment = new WallFragment();
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
                fragment = new MessagesFragment();
                title = getString(R.string.title_messages);
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

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //moveTaskToBack(true);
    }
}
