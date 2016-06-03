package com.hvngoc.googlemaptest.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;


public class FriendsFragment extends Fragment {

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpaper_friend);
        viewPager.setAdapter(new SampleViewPaperAdapter(getChildFragmentManager()));

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout_friend);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_group_white);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_add_white_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_group_add_white_24dp);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.NAVIGATION_FRIEND;
    }

    private class SampleViewPaperAdapter extends FragmentPagerAdapter {
        private final int TAB_COUNT = 3;
        private String[] listTitle;
        private Fragment[] listFragment;

        public SampleViewPaperAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
            listTitle = new String[]{getString(R.string.hint_friend), getString(R.string.friend_request), getString(R.string.friend_suggested)};
            listFragment = new Fragment[]{new FriendListFragment(), new FriendRequestFragment(), new FriendSuggestFragment()};
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("Friend", "" + position);
            return listFragment[position];
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return listTitle[position];
        }
    }
}