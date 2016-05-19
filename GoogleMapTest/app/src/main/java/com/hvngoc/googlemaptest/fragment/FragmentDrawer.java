package com.hvngoc.googlemaptest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.squareup.picasso.Picasso;

public class FragmentDrawer extends Fragment {

    private ImageView pictureProfile;
    private TextView nameTxtView;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;

    public FragmentDrawer() {

    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(int position);
    }
    private FragmentDrawerListener drawerListener;
    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        pictureProfile = (ImageView) layout.findViewById(R.id.ic_profile_picture);
        nameTxtView = (TextView) layout.findViewById(R.id.nametxt);

        CardView card_nav_home = (CardView) layout.findViewById(R.id.card_nav_home);
        card_nav_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_HOME);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_profile = (CardView) layout.findViewById(R.id.card_nav_profile);
        card_nav_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_PROFILE);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_wall = (CardView) layout.findViewById(R.id.card_nav_wall);
        card_nav_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_WALL);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_map = (CardView) layout.findViewById(R.id.card_nav_map);
        card_nav_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_MAP);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_friends = (CardView) layout.findViewById(R.id.card_nav_friends);
        card_nav_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_FRIEND);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_notification = (CardView) layout.findViewById(R.id.card_nav_notification);
        card_nav_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_NOTIFICATION);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_message = (CardView) layout.findViewById(R.id.card_nav_message);
        card_nav_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_MESSAGE);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_language = (CardView) layout.findViewById(R.id.card_nav_language);
        card_nav_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_LANGUAGE);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_settings = (CardView) layout.findViewById(R.id.card_nav_settings);
        card_nav_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_SETTING);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_change_password = (CardView) layout.findViewById(R.id.card_nav_change_password);
        card_nav_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_CHANGE_PASSWORD);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_about = (CardView) layout.findViewById(R.id.card_nav_about);
        card_nav_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_ABOUT);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_help = (CardView) layout.findViewById(R.id.card_nav_help);
        card_nav_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_HELP);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_report = (CardView) layout.findViewById(R.id.card_nav_report);
        card_nav_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_REPORT);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_logout = (CardView) layout.findViewById(R.id.card_nav_logout);
        card_nav_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_LOGOUT);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        CardView card_nav_close = (CardView) layout.findViewById(R.id.card_nav_close);
        card_nav_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_CLOSE);
                mDrawerLayout.closeDrawer(containerView);
            }
        });
        return layout;
    }

    public void setPictureProfile() {
        Picasso.with(GLOBAL.CurrentContext)
                .load(GLOBAL.CurrentUser.getAvatar())
                .error(R.drawable.icon_profile)         // optional
                .into(pictureProfile);
        nameTxtView.setText(GLOBAL.CurrentUser.getName());
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }
}