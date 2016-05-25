package com.hvngoc.googlemaptest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.hvngoc.googlemaptest.R;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MAIN PAGE", "RESUME");
        // display the first navigation drawer view on app launch
        displayView(GLOBAL.MAIN_PAGE_POSITION_VIEW);
        GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.NAVIGATION_HOME;
        drawerFragment.setLanguageAgain();
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
    protected void onPause() {
        super.onPause();
    }
}
