package com.hvngoc.googlemaptest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.adapter.RVPickImageAdapter;
import com.hvngoc.googlemaptest.custom.IconizedMenu;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.LocationHelper;
import com.hvngoc.googlemaptest.helper.LocationRoundHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.helper.PickPictureHelper;
import com.hvngoc.googlemaptest.imagechooser.CustomGallery;
import com.hvngoc.googlemaptest.imagechooser.CustomGalleryAdapter;
import com.hvngoc.googlemaptest.model.Post;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PostCreationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap googleMap;
    private SupportMapFragment supportMapFragment;
    private HashTagHelper hastagHelper;
    private EditText editTextCreatePost;
    private Post post;
    private RVPickImageAdapter adapter;
    private CustomGalleryAdapter galleryAdapter;
    private RecyclerView recyclerCreatePostImage;
    private ArrayList<String> listImageUrls = new ArrayList<String>();
    ImageLoader imageLoader;
    private List<String> tags;
    private ImageView btnCreatePostGetFeeling;
    private FloatingActionButton btnCreatePostOK;
    private TextView txtCreatePostFeeling;
    private ImageView btnCreatePostGetImage;

    List<String> images;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);
        GLOBAL.CurrentContext = this;
        initComponent();
        createSamplePost();
        initContentView();
        initImageLoader();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.hint_create_post));
    }

    private void initImageLoader() {
        // for universal image loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        galleryAdapter = new CustomGalleryAdapter(this, imageLoader);
    }

    private void initContentView() {
        findViewById(R.id.MapCreatePostMap).setVisibility(View.INVISIBLE);
        post.setFeeling(getString(R.string.feeling_happy));
        setLocationTextView(GLOBAL.CurrentUser.getDefaultLatitude(), GLOBAL.CurrentUser.getDefaultLongitude());
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.MapCreatePostMap);
        supportMapFragment.getMapAsync(this);
    }

    private void createSamplePost(){
        post = new Post();
        post.setPostID(UUID.randomUUID().toString());
        post.userName = GLOBAL.CurrentUser.getName();
        post.setUserAvatar(GLOBAL.CurrentUser.getAvatar());
        hastagHelper = HashTagHelper.Creator.create(ContextCompat.getColor(this, R.color.blue), null);
        hastagHelper.handle(editTextCreatePost);
    }

    private void setMapVisibility(int value){
        findViewById(R.id.MapCreatePostMap).setVisibility(value);
    }

    private void initComponent() {
        recyclerCreatePostImage = (RecyclerView) findViewById(R.id.recyclerCreatePostImage);
        txtCreatePostFeeling = (TextView) findViewById(R.id.txtCreatePostFeeling);
        btnCreatePostGetFeeling = (ImageView) findViewById(R.id.btnCreatePostGetFeeling);
        btnCreatePostOK = (FloatingActionButton) findViewById(R.id.btnCreatePostOK);
        editTextCreatePost = (EditText)findViewById(R.id.editTextCreatePost);
        RadioGroup radioGroupCreatePost = (RadioGroup) findViewById(R.id.radioGroupCreatePost);
        radioGroupCreatePost.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioGetOnMapLocation:
                        setMapVisibility(View.VISIBLE);
                        btnCreatePostOK.bringToFront();
                        break;
                    case R.id.radioYourLocation:
                        LocationHelper locationHelper = new LocationHelper(GLOBAL.CurrentContext);
                        Double latitude = locationHelper.GetLatitude();
                        Double longitude = locationHelper.GetLongitude();
                        if (latitude == 0.0 && longitude == 0.0) {
                            latitude = GLOBAL.CurrentUser.getDefaultLatitude();
                            longitude = GLOBAL.CurrentUser.getDefaultLongitude();
                        }
                        setLocationTextView(latitude, longitude);
                        setMapVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        btnCreatePostGetImage = (ImageView) findViewById(R.id.btnCreatePostGetImage);
        btnCreatePostGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CONSTANT.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }
        });


        btnCreatePostGetFeeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final IconizedMenu menu = new IconizedMenu(GLOBAL.CurrentContext, btnCreatePostGetFeeling);
                menu.getMenuInflater().inflate(R.menu.menu_pick_feeling, menu.getMenu());
                menu.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        post.setFeeling(item.getTitle().toString());
                        btnCreatePostGetFeeling.setImageResource((int)GLOBAL.EMOTION.get(post.getSaveFeeling()).get(1));
                        txtCreatePostFeeling.setText(post.getFeeling());
                        menu.dismiss();
                        return true;
                    }
                });
                menu.show();
            }
        });

        btnCreatePostOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.setPostDate(ParseDateTimeHelper.getCurrent());
                post.Latitude = LocationRoundHelper.Round(post.Latitude);
                post.Longitude = LocationRoundHelper.Round(post.Longitude);
                post.setContent(editTextCreatePost.getText().toString());
                showProgressDialog();
                images = getStringImages(adapter.getListBitmaps());
                int i = 0;
                for (i = 0; i < images.size(); i += 5) {
                    int size = (images.size() >= 5 + i) ? 5 : images.size();
                    List<String> subList = images.subList(i, size);
                    new UploadImagesAsyncTask(subList, i, images.size()).execute();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listImageUrls = new ArrayList<String>();
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            ArrayList<String> image_path = data.getStringArrayListExtra("image_path");
            recyclerCreatePostImage.setLayoutManager(new LinearLayoutManager(GLOBAL.CurrentContext, LinearLayoutManager.HORIZONTAL, false));
            recyclerCreatePostImage.setHasFixedSize(true);
            adapter = new RVPickImageAdapter(image_path);

            adapter.setOnClickImage(new RVPickImageAdapter.OnClickImage() {
                @Override
                public void doSomething(String uri) {
                    startCropImageActivity(Uri.fromFile(new File(uri)));
                }
            });
            recyclerCreatePostImage.setAdapter(adapter);
        }
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                ((ImageView) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
//              }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    public List<String> getStringImages(List<Bitmap> bitmaps){
        ArrayList<String> images = new ArrayList<String>();
        for (int i = 0; i < bitmaps.size(); i++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            images.add(encodedImage);
        }
        return images;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.uploading));
        progressDialog.show();
    }

    private void setLocationTextView(double Latitude, double Longitude){
        post.Latitude = Latitude;
        post.Longitude = Longitude;
        String address = new GeolocatorAddressHelper(GLOBAL.CurrentContext, post.Latitude, post.Longitude ).GetAddress();
        TextView txtCreatePostLocation = (TextView) findViewById(R.id.txtCreatePostLocation);
        txtCreatePostLocation.setText(address);
    }


/*-------------------------------------UPLOAD SERVICE---------------------------------*/

    private class UploadImagesAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String encodedImage;
        private int index;
        private int size;
        private List<String> images;
        private int startIndex;


        public UploadImagesAsyncTask(List<String> images, int startIndex, int size) {
            this.size = size;
            this.images = images;
            this.startIndex = startIndex;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<String>>(){}.getType();
                List<String> imageUrls = gson.fromJson(res, listType);
                listImageUrls.addAll(imageUrls);
                if(listImageUrls.size() == size) {
                    new CreatePostAsyncTask().execute();
                }
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(GLOBAL.CurrentContext, getString(R.string.upload_image_error), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "uploadImages";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("postID", post.getPostID());
                jsonobj.put("startIndex", startIndex);
                jsonobj.put("binary", new JSONArray(images));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }
    }


    private class CreatePostAsyncTask extends AsyncTask<Void, Void, Boolean> {
        HTTPPostHelper helper;
        String tag = "[";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tags = hastagHelper.getAllHashTags();
            for (String string : tags) {
                tag += "'" + string + "',";
            }
            if (tag.length() > 1)
                tag = tag.substring(0, tag.length() - 1);
            tag += "]";
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                post = gson.fromJson(res, Post.class);
            }
            else {
                Toast.makeText(GLOBAL.CurrentContext, "Create Post Error!", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            finish();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "createPost";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("postID", post.getPostID());
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("content", post.getContent());
                jsonobj.put("date", post.getPostDate());
                jsonobj.put("Latitude", post.Latitude);
                jsonobj.put("Longitude", post.Longitude);
                jsonobj.put("feeling", post.getSaveFeeling());
                jsonobj.put("listImages", getListImages());
                jsonobj.put("tag", tag);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }
    }

    private String getListImages(){
        String sss = "";
        for (String item : listImageUrls){
            sss += item + ";";
        }
        sss = sss.substring(0, sss.length() - 1);
        return sss;
    }

    /*-----------------------------------MAP INITIALIZATION-----------------------------------*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapLongClickListener(this);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.getUiSettings().setRotateGesturesEnabled(false);
        this.googleMap.getUiSettings().setTiltGesturesEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.setTrafficEnabled(true);
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.setIndoorEnabled(true);
        this.googleMap.setBuildingsEnabled(false);
        InitilizeMap();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        setLocationTextView(latLng.latitude, latLng.longitude);
        AddCurrentMarker();
    }

    private void AddCurrentMarker(){
        this.googleMap.clear();
        MarkerOptions markerOption = new MarkerOptions()
                .position(new LatLng(post.Latitude, post.Longitude))
                .icon(BitmapDescriptorFactory.fromResource((int) GLOBAL.EMOTION.get(post.getSaveFeeling()).get(1)))
                .title(post.getFeeling());
        this.googleMap.addMarker(markerOption);
    }

    private void InitilizeMap() {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(GLOBAL.CurrentUser.getDefaultLatitude(), GLOBAL.CurrentUser.getDefaultLongitude()), 15));
        (findViewById(R.id.MapCreatePostMap)).getViewTreeObserver().addOnGlobalLayoutListener(
                new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        if (android.os.Build.VERSION.SDK_INT >= 16) {
                            (findViewById(R.id.MapCreatePostMap)).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            (findViewById(R.id.MapCreatePostMap)).getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        AddCurrentMarker();
    }
}