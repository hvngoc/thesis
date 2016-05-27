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

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentDrawer extends Fragment {

    private CircleImageView pictureProfile;
    private TextView nameTxtView;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;

    private View layout;

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
        layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        pictureProfile = (CircleImageView) layout.findViewById(R.id.ic_profile_picture);
        nameTxtView = (TextView) layout.findViewById(R.id.nametxt);


        CardView card_nav_profile = (CardView) layout.findViewById(R.id.card_nav_profile);
        card_nav_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_PROFILE);
            }
        });
        CardView card_nav_wall = (CardView) layout.findViewById(R.id.card_nav_wall);
        card_nav_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_WALL);
            }
        });

        CardView card_nav_friends = (CardView) layout.findViewById(R.id.card_nav_friends);
        card_nav_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_FRIEND);
            }
        });

        CardView card_nav_language = (CardView) layout.findViewById(R.id.card_nav_language);
        card_nav_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_LANGUAGE);
            }
        });
        CardView card_nav_settings = (CardView) layout.findViewById(R.id.card_nav_settings);
        card_nav_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_SETTING);
            }
        });
        CardView card_nav_change_password = (CardView) layout.findViewById(R.id.card_nav_change_password);
        card_nav_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_CHANGE_PASSWORD);
            }
        });
        CardView card_nav_change_location = (CardView) layout.findViewById(R.id.card_nav_change_location);
        card_nav_change_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_CHANGE_LOCATION);
            }
        });

        CardView card_nav_report = (CardView) layout.findViewById(R.id.card_nav_report);
        card_nav_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                drawerListener.onDrawerItemSelected(CONSTANT.NAVIGATION_REPORT);
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

    public void setLanguageAgain(){
        TextView textView;
        textView = (TextView) layout.findViewById(R.id.navigation_profile);
        textView.setText(getString(R.string.title_profile));
        textView = (TextView) layout.findViewById(R.id.navigation_wall);
        textView.setText(getString(R.string.title_wall));
        textView = (TextView) layout.findViewById(R.id.navigation_friend);
        textView.setText(getString(R.string.title_friends));
        textView = (TextView) layout.findViewById(R.id.navigation_language);
        textView.setText(getString(R.string.title_language));
        textView = (TextView) layout.findViewById(R.id.navigation_setting);
        textView.setText(getString(R.string.title_settings));
        textView = (TextView) layout.findViewById(R.id.navigation_change_pass);
        textView.setText(getString(R.string.title_change_pass_work));
        textView = (TextView) layout.findViewById(R.id.navigation_change_location);
        textView.setText(getString(R.string.title_change_location));
        textView = (TextView) layout.findViewById(R.id.navigation_report);
        textView.setText(getString(R.string.title_report));
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