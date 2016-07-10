package com.hvngoc.googlemaptest.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;


public class FriendsFragment extends Fragment {

    private SampleViewPaperAdapter adapter;

    public FriendsFragment() {
        Log.i("FRIEND", "CONSTRUCTOR");
        FragmentActivity activity = (FragmentActivity)GLOBAL.CurrentContext;
        adapter = new SampleViewPaperAdapter(activity.getSupportFragmentManager());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("FRIEND", "CREATE");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpaper_friend);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout_friend);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_search_white_24dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_group_add_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_group_white);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person_add_white_24dp);

        Log.i("FRIEND", "VIEW");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("FRIEND", "RESUME");
        GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.BOTTOM_FRIEND;
    }

    private class SampleViewPaperAdapter extends FragmentPagerAdapter {
        private final int TAB_COUNT = 4;
        private String[] listTitle;
        private Fragment find, suggest, friend, request;

        public SampleViewPaperAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
            listTitle = new String[]{GLOBAL.CurrentContext.getString(R.string.friend_find),
                    GLOBAL.CurrentContext.getString(R.string.friend_suggested),
                    GLOBAL.CurrentContext.getString(R.string.friend_friends),
                    GLOBAL.CurrentContext.getString(R.string.friend_request)};
            Log.i("FRIEND", "CONSTRUCTOR ADAPTER");
        }

        @Override
        public Fragment getItem(int position) {
            Log.i("FRIEND POSITION", "" + position);
            switch (position){
                case 1:
                    if (suggest == null)
                        suggest = new FriendSuggestFragment();
                    return suggest;
                case 2:
                    if (friend == null)
                        friend = new FriendListFragment();
                    return friend;
                case 3:
                    if (request == null)
                        request = new FriendRequestFragment();
                    return request;
                default:
                    if (find == null)
                        find = new FriendFindFragment();
                    return find;
            }
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