package com.hvngoc.googlemaptest.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.model.Post;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class NewsDetailActivity extends AppCompatActivity {
    CircleImageView userAvatar;
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

    FloatingActionButton fab_post_detail;

    ImageView imgShowMap;
    private Post currentPost;
    private SliderLayout mDemoSlider;

    private Boolean isLiking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        Log.i("DETAIL ACTIVITY", "CREATE");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GLOBAL.CurrentContext = this;

        Bundle extras = getIntent().getExtras();
        currentPost = (Post) extras.getSerializable("currentPost");

        initComponent();
        getNewsDetailData();
        getImageSlider();
    }

    private void initComponent(){
        userAvatar = (CircleImageView) findViewById(R.id.avatar);
        username = (TextView) findViewById(R.id.username);
        txtFeeling = (TextView) findViewById(R.id.txtFeeling);
        txtCommentDay = (TextView) findViewById(R.id.txtCommentDay);
        txtAddressLocation = (TextView) findViewById(R.id.txtAddressLocation);
        title = (TextView) findViewById(R.id.title);
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        btnLike = (Button) findViewById(R.id.btnLike);
        txtNumLike = (TextView) findViewById(R.id.txtNumLike);
        btnShare = (Button) findViewById(R.id.btnShare);
        txtNumShared = (TextView) findViewById(R.id.txtNumShared);
        btnComment = (Button) findViewById(R.id.btnComment);
        txtNumComment = (TextView) findViewById(R.id.txtNumComment);

        btnShare.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        btnComment.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

        imgShowMap = (ImageView) findViewById(R.id.imgViewShowMap);
        imgShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GLOBAL.CurrentContext, MapsActivity.class);
                intent.putExtra("currentPost", currentPost);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GLOBAL.CurrentContext.startActivity(intent);
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this, CommentActivity.class);
                intent.putExtra("postID", currentPost.getPostID());
                intent.putExtra("relation", currentPost.getRelationShip());
                startActivityForResult(intent, 299);
            }
        });

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiking)
                    return;
                isLiking = true;
                if (currentPost.isYouLike == 0) {
                    currentPost.numLike += 1;
                    currentPost.isYouLike = 1;
                    new LikeThisPostAsyncTask().execute();
                }
                else {
                    currentPost.numLike -= 1;
                    currentPost.isYouLike = 0;
                    new UnLikeThisPostAsyncTask().execute();
                }
                txtNumLike.setText("" + currentPost.numLike);
                setLikeButton();
            }
        });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPost.numShare += 1;
                Toast.makeText(getBaseContext(), getString(R.string.share_ok), Toast.LENGTH_LONG).show();
                txtNumShared.setText(currentPost.numShare + "");
                new ShareThisPostAsyncTask().execute();
            }
        });

        fab_post_detail = (FloatingActionButton) findViewById(R.id.fab_post_detail);
        if (!currentPost.getUserID().equals(GLOBAL.CurrentUser.getId())
                || (currentPost.getRelationShip().equals(CONSTANT.RELATIONSHIP_SHARE))){
            fab_post_detail.setVisibility(View.GONE);
        }
        else{
            fab_post_detail.setVisibility(View.VISIBLE);
        }
        fab_post_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this, EditPostActivity.class);
                intent.putExtra("currentPost", currentPost);
                startActivityForResult(intent, 333);
            }
        });
    }


    private void setImageSliderVisibility(int type) {
        mDemoSlider.setVisibility(type);
        txtAddressLocation.setVisibility(type);
    }

    private void getImageSlider() {
        ArrayList<String> imageUrls = currentPost.getListImages();
        if (imageUrls.size() == 0){
            setImageSliderVisibility(View.GONE);
        }else {
            setImageSliderVisibility(View.VISIBLE);
            for (String name : imageUrls) {
                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView.image(name)
                        .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);
                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setDuration(4000);
        }
    }
    private void getNewsDetailData(){
        username.setText(currentPost.userName);
        Picasso.with(GLOBAL.CurrentContext).load(currentPost.getUserAvatar()).error(R.drawable.icon_profile).into(userAvatar);
        title.setText(currentPost.getContent());
        txtFeeling.setText(getString(R.string.feeling) +" "+ currentPost.getFeeling());
        txtCommentDay.setText(currentPost.getPostDate());
        txtAddressLocation.setText(new GeolocatorAddressHelper(this, currentPost.Latitude, currentPost.Longitude).GetAddress());
        txtNumLike.setText("" + currentPost.numLike);
        txtNumShared.setText("" + currentPost.numShare);
        txtNumComment.setText("" + currentPost.numComment);
        setLikeButton();
    }

    private void setLikeButton(){
        Log.i("is you like", currentPost.isYouLike + "");
        isLiking = false;
        if (currentPost.isYouLike == 0)
            btnLike.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        else
            btnLike.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
    }

    private void changeDataAfterEdited(Post editedPost){
        currentPost.setContent(editedPost.getContent());
        currentPost.setFeeling(editedPost.getSaveFeeling());
        currentPost.Latitude = editedPost.Latitude;
        currentPost.Longitude = editedPost.Longitude;
        currentPost.setListImages(editedPost.getListImagesString());
        getNewsDetailData();
        getImageSlider();
    }

    //*************************************************************************************************************************//

    @Override
    protected void onResume() {
        super.onResume();
        GLOBAL.CurrentContext = this;
        Log.i("DETAIL ACTIVITY", "RESUME");
        setActionBarTitle(getString(R.string.title_activity_detail));
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("DETAIL ACTIVITY", "STOP");
        mDemoSlider.stopAutoCycle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 299 && resultCode == 300){
            int numComment = data.getExtras().getInt("numComment");
            if (currentPost.numComment < numComment)
                currentPost.numComment = numComment;
            txtNumComment.setText(currentPost.numComment + "");
        }
        else if (requestCode == 333 && resultCode == 222){
            Post editedPost = (Post) data.getExtras().getSerializable("editedPost");
            changeDataAfterEdited(editedPost);
            GLOBAL.needFragmentRefresh = true;
        }
    }

    //*******************************************************************************************************************************//

    private class LikeThisPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }

        private Boolean postData() {
            String serverUrl = GLOBAL.SERVER_URL;
            if (currentPost.getRelationShip().equals(CONSTANT.RELATIONSHIP_TOUR))
                serverUrl += "likeTourPost";
            else
                serverUrl += "likeThisPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("postID", currentPost.getPostID());
                jsonobj.put("day", ParseDateTimeHelper.getCurrent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
//            if(result) {
//                currentPost.numLike += 1;
//                currentPost.isYouLike = 1;
//                txtNumLike.setText("" + currentPost.numLike);
//            }
//            setLikeButton();
        }
    }

    private class UnLikeThisPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;

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
//            if(result) {
//                currentPost.numLike -= 1;
//                currentPost.isYouLike = 0;
//                txtNumLike.setText("" + currentPost.numLike);
//            }
//            setLikeButton();
        }
    }

    private class ShareThisPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }

        private Boolean postData() {
            String serverUrl = GLOBAL.SERVER_URL;
            if (currentPost.getRelationShip().equals(CONSTANT.RELATIONSHIP_TOUR))
                serverUrl+= "shareTourPost";
            else
                serverUrl += "shareThisPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("postID", currentPost.getPostID());
                jsonobj.put("day", ParseDateTimeHelper.getCurrent());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
//            if(result) {
//                currentPost.numShare += 1;
//                Toast.makeText(getBaseContext(), getString(R.string.share_ok),Toast.LENGTH_LONG).show();
//                txtNumShared.setText(currentPost.numShare + "");
//            }
        }
    }
}



