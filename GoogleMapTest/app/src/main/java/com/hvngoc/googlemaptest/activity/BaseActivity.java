package com.hvngoc.googlemaptest.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.services.LocationNotifierService;
import com.hvngoc.googlemaptest.services.LocationResultReceiver;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;

public abstract class BaseActivity extends AppCompatActivity {

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
        StartLocationServiceHelper();
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

    protected MenuItem action_notification;

    protected ContextMenuDialogFragment mMenuDialogFragment;
    protected abstract void InitRunCustomMenu();
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        action_notification = menu.findItem(R.id.action_notification);
        action_notification.setIcon(android.R.drawable.star_big_off);
        action_notification.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getBaseContext(), "goto notification fragment", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        MenuItem action_options = menu.findItem(R.id.action_options);
        action_options.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (getSupportFragmentManager().findFragmentByTag(ContextMenuDialogFragment.TAG) == null && mMenuDialogFragment != null) {
                    mMenuDialogFragment.show(getSupportFragmentManager(), ContextMenuDialogFragment.TAG);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded())
            mMenuDialogFragment.dismiss();
        else
            finish();
    }
}
