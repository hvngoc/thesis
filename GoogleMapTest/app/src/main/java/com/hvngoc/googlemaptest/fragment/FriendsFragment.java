package com.hvngoc.googlemaptest.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hvngoc.googlemaptest.R;



public class FriendsFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

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

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpaper_friend);
        viewPager.setAdapter(new SampleViewPaperAdapter(getActivity().getSupportFragmentManager()));

        tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout_friend);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_media_play);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_media_pause);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_plusone_small_off_client);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_cast_disabled_light);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class SampleViewPaperAdapter extends FragmentPagerAdapter {
        private final int TAB_COUNT = 4;
        private String listTitle[] = new String[]{"friends", "suggested", "request", "find"};
        private Fragment listFragment[] = new Fragment[]{new FriendListFragment(), new FriendSuggestFragment(),
                new NothingsFragment(), new NothingsFragment()};

        public SampleViewPaperAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
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