package com.hvngoc.googlemaptest.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.custom.CommentDialogLayout;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Post;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewsDetailActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{


    ImageView userAvatar;
    TextView username;
    TextView txtFeeling;
    TextView txtCommentDay;
    TextView txtAddressLocation;
    TextView title;

    Button btnLike;
    TextView txtNumLike;
    Button btnShare;
    TextView txtNumShared;
    Button btnComment;
    TextView txtNumComment;

    ImageView imgShowMap;
    private SliderLayout mDemoSlider;
    private Post currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GLOBAL.CurentContext = this;

        Bundle extras = getIntent().getExtras();
        currentPost = (Post) extras.getSerializable("currentPost");

        userAvatar = (ImageView) findViewById(R.id.avatar);
        username = (TextView) findViewById(R.id.username);
        txtFeeling = (TextView) findViewById(R.id.txtFeeling);
        txtCommentDay = (TextView) findViewById(R.id.txtCommentDay);
        txtAddressLocation = (TextView) findViewById(R.id.txtAddressLocation);
        title = (TextView) findViewById(R.id.title);

        btnLike = (Button) findViewById(R.id.btnLike);
        txtNumLike = (TextView) findViewById(R.id.txtNumLike);
        btnShare = (Button) findViewById(R.id.btnShare);
        txtNumShared = (TextView) findViewById(R.id.txtNumShared);
        btnComment = (Button) findViewById(R.id.btnComment);
        txtNumComment = (TextView) findViewById(R.id.txtNumComment);

        imgShowMap = (ImageView) findViewById(R.id.imgViewShowMap);
        imgShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GLOBAL.CurentContext, MapsActivity.class);
                intent.putExtra("currentPost", currentPost);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GLOBAL.CurentContext.startActivity(intent);
            }
        });

        getNewsDetailData();
        getImageSlider();

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialogLayout dialog = new CommentDialogLayout(GLOBAL.CurentContext, currentPost.getPostID());
                dialog.show();
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPost.isYouLike == 0)
                    new LikeThisPostAsyncTask().execute();
                else
                    new UnLikeThisPostAsyncTask().execute();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareThisPostAsyncTask().execute();
            }
        });

    }

    private void getImageSlider() {
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.hannibal);
        file_maps.put("Big Bang Theory",R.drawable.bigbang);
        file_maps.put("House of Cards",R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }
    private void getNewsDetailData(){
        username.setText(currentPost.userName);
        Picasso.with(GLOBAL.CurentContext).load("YOUR IMAGE URL HERE").error(R.drawable.icon_profile).into(userAvatar);
        title.setText(currentPost.getContent());
        txtFeeling.setText("feeling " + currentPost.feeling + " on");
        txtCommentDay.setText(currentPost.getPostDate());
        txtAddressLocation.setText(new GeolocatorAddressHelper(this, currentPost.Latitude, currentPost.Longitude).GetAddress());
        txtNumLike.setText("" + currentPost.numLike);
        txtNumShared.setText("" + currentPost.numShare);
        txtNumComment.setText("" + currentPost.numComment);
        setLikeButton();
    }

    private void setLikeButton(){
        Log.i("is you like", currentPost.isYouLike + "");
        if (currentPost.isYouLike == 0)
            btnLike.setBackgroundResource(R.drawable.ic_favorite_black);
        else
            btnLike.setBackgroundResource(R.drawable.ic_favorite_ok);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail_news;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }


    private class LikeThisPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "likeThisPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("postID", currentPost.getPostID());
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
                currentPost.numLike += 1;
                currentPost.isYouLike = 1;
                txtNumLike.setText("" + currentPost.numLike);
            }
            else {

            }
            setLikeButton();
        }
    }

    private class UnLikeThisPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "unLikeThisPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("postID", currentPost.getPostID());
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
                currentPost.numLike -= 1;
                currentPost.isYouLike = 0;
                txtNumLike.setText("" + currentPost.numLike);
            }
            else {

            }
            setLikeButton();
        }
    }

    private class ShareThisPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "shareThisPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("postID", currentPost.getPostID());
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
                currentPost.numShare += 1;
                Toast.makeText(getBaseContext(), "share ok!!!",Toast.LENGTH_LONG).show();
                txtNumShared.setText(currentPost.numShare + "");
            }
            else {

            }
        }
    }

}



