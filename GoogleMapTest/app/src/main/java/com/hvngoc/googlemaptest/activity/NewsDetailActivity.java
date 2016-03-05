package com.hvngoc.googlemaptest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.custom.CommentDialogLayout;
import com.hvngoc.googlemaptest.model.Post;
import com.squareup.picasso.Picasso;


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


    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GLOBAL.CurentContext = this;

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

        ImageView imgShowMap = (ImageView) findViewById(R.id.imgViewShowMap);
        imgShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GLOBAL.CurentContext, MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GLOBAL.CurentContext.startActivity(intent);
            }
        });

        getNewsDetailData();

        getImageSlider();

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = getIntent().getExtras();
                int position = extras.getInt("position");
                CommentDialogLayout dialog = new CommentDialogLayout(GLOBAL.CurentContext, position);
                dialog.show();
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int i = extras.getInt("position");
            Log.i("Position", ""+i);
            Post post = GLOBAL.CurrentListPost.get(i);
            if(username == null)
                Log.i("Username: ", "nulllllllllllllllllllllllllllllllllllllllllllllll");

            if(txtNumComment == null)
                Log.i("Comment: ", "nulllllllllllllllllllllllllllllllllllllllllllllll");
            username.setText(post.userName);
            Picasso.with(GLOBAL.CurentContext).load("YOUR IMAGE URL HERE").error(R.drawable.icon_profile).into(userAvatar);
            title.setText(post.getContent());
            txtFeeling.setText("feeling " + post.feeling + " on");
            txtCommentDay.setText(post.getPostDate());
            //newsViewHolder.txtAddressLocation.setText(new GeolocatorAddressHelper() posts.get(i);
            txtNumLike.setText("" + post.numLike);
            txtNumShared.setText("" + post.numShare);
            txtNumComment.setText("" + post.numComment);

        }
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

}



