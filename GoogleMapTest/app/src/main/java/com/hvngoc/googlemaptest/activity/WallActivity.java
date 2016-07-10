package com.hvngoc.googlemaptest.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.fragment.MyImagesFragment;
import com.hvngoc.googlemaptest.fragment.MyProfileFragment;
import com.hvngoc.googlemaptest.fragment.MyWallFragment;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class WallActivity extends AppCompatActivity {
    public static Profile currentPofile = null;
    TabLayout tlUserProfileTabs;
    CircleImageView userAvatar;
    View vUserDetails;
    View vUserProfileRoot;
    TextView username;
    TextView userEmail;
    TextView numPost, numFollower, numFriend;
    String currentID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        currentID = getIntent().getExtras().getString("id");
        if(currentID == null)
            currentID = GLOBAL.CurrentUser.getId();
        GLOBAL.CurrentContext = this;

        initComponent();
        setupToolbar();
        setupViewPager();
        loadProfile();
    }

    private ProgressDialog progressDialog = null;
    private void loadProfile() {
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        new LoadProfileAsyncTask().execute();
    }

    private void initComponent() {
        tlUserProfileTabs = (TabLayout) findViewById(R.id.tlUserProfileTabs);
        userAvatar = (CircleImageView) findViewById(R.id.avatar);
        vUserDetails = findViewById(R.id.vUserDetails);
        vUserProfileRoot = findViewById(R.id.vUserProfileRoot);
        username = (TextView) findViewById(R.id.username);
        userEmail = (TextView) findViewById(R.id.userEmail);
        numFollower = (TextView) findViewById(R.id.numFollower);
        numPost = (TextView) findViewById(R.id.numPost);
        numFriend = (TextView) findViewById(R.id.numFriend);
    }

    private void loadUserData() {
        Picasso.with(this)
                .load(currentPofile.getAvatar())
                .error(R.drawable.icon_no_image)
                .resize(120, 120)
                .centerCrop()
                .into(userAvatar);
        username.setText(currentPofile.name);
        userEmail.setText(currentPofile.email);
        numFollower.setText("" + currentPofile.numFollow);
        numFriend.setText("" + currentPofile.numFriend);
        numPost.setText("" + currentPofile.numPost);
    }


    private void setupViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final int[] imageResId = {
                R.drawable.ic_border_all_white_24dp,
                R.drawable.ic_view_stream_white_24dp,
                R.drawable.ic_account_box_white_24dp
        };
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(MyWallFragment.getInstance(currentID), getString(R.string.title_post));
        adapter.addFrag(MyImagesFragment.getInstance(currentID), getString(R.string.title_images));
        adapter.addFrag(MyProfileFragment.getInstance(currentID), getString(R.string.title_profile));
        viewPager.setAdapter(adapter);
        tlUserProfileTabs.setupWithViewPager(viewPager);
        for (int i = 0; i < tlUserProfileTabs.getTabCount(); i++) {
            tlUserProfileTabs.getTabAt(i).setIcon(imageResId[i]);
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getString(R.string.title_wall));
    }

    @Override
    protected void onResume() {
        super.onResume();
        GLOBAL.CurrentContext = this;
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
              return mFragmentTitleList.get(position);
        }
    }


    private class LoadProfileAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }
        private Boolean postData() {
            String serverUrl = GLOBAL.SERVER_URL + "getProfileOfUser";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", currentID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Log.i("PROFILE", "" + res);
                WallActivity.currentPofile = gson.fromJson(res, Profile.class);
                loadUserData();
            }
            else{
                //it always load successfully.
                //only fail when we have a trouble with internet or server
            }
            progressDialog.dismiss();
        }
    }

}
