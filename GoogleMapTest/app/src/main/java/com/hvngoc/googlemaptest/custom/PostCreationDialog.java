package com.hvngoc.googlemaptest.custom;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVPickImageAdapter;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.PickPictureHelper;
import com.hvngoc.googlemaptest.helper.LocationHelper;
import com.hvngoc.googlemaptest.model.Post;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Hoang Van Ngoc on 20/04/2016.
 */
public class PostCreationDialog extends Dialog implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private Context context;
    private FragmentManager fragmentManager;

    private GoogleMap googleMap;
    private SupportMapFragment supportMapFragment;

    private Post post;
    private RVPickImageAdapter adapter;
    private ArrayList<String> listImageUrls = new ArrayList<String>();

    private DelegationHelper delegationHelper;
    public void setDelegationHelper(DelegationHelper delegationHelper){
        this.delegationHelper = delegationHelper;
    }

    public PostCreationDialog(Context context, FragmentManager fragmentManager) {
        super(context);
        this.context = context;
        this.fragmentManager = fragmentManager;
        post = new Post();
        post.setPostID(UUID.randomUUID().toString());
        post.userName = GLOBAL.CurrentUser.getName();
        post.setUserAvatar(GLOBAL.CurrentUser.getAvatar());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(R.layout.layout_custom_post_creation);

        Button btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RadioGroup radioGroupCreatePost = (RadioGroup) findViewById(R.id.radioGroupCreatePost);
        radioGroupCreatePost.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioGetOnMap) {
                    findViewById(R.id.MapCreatePostMap).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.MapCreatePostMap).setVisibility(View.INVISIBLE);
                }
            }
        });

        final ImageView btnCreatePostGetImage = (ImageView) findViewById(R.id.btnCreatePostGetImage);
        btnCreatePostGetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PickPictureHelper pickPictureHelper = new PickPictureHelper(context, true);
                pickPictureHelper.setOnOKClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> listImages = pickPictureHelper.getmItemsChecked();

                        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerCreatePostImage);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        mRecyclerView.setHasFixedSize(true);
                        adapter = new RVPickImageAdapter(listImages);
                        mRecyclerView.setAdapter(adapter);
                        pickPictureHelper.dismiss();
                    }
                });
                pickPictureHelper.show();
            }
        });

        final ImageView btnCreatePostGetFeeling = (ImageView) findViewById(R.id.btnCreatePostGetFeeling);
        btnCreatePostGetFeeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final IconizedMenu menu = new IconizedMenu(context, btnCreatePostGetFeeling);
                menu.getMenuInflater().inflate(R.menu.menu_pick_feeling, menu.getMenu());
                menu.setOnMenuItemClickListener(new IconizedMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        post.feeling = item.getTitle().toString();
                        btnCreatePostGetFeeling.setImageResource(GLOBAL.EMOTION.get(post.feeling));
                        TextView text = (TextView) findViewById(R.id.txtCreatePostFeeling);
                        text.setText(post.feeling);
                        menu.dismiss();
                        return true;
                    }
                });
                menu.show();
            }
        });

        final ImageView btnCreatePostGetTag = (ImageView) findViewById(R.id.btnCreatePostGetTag);
        btnCreatePostGetTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, btnCreatePostGetTag);
                popupMenu.getMenuInflater().inflate(R.menu.menu_pick_tag, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        boolean checked = !item.isChecked();
                        item.setChecked(checked);
                        setTxtCreatePostTag(checked, item.getTitle().toString());

                        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                        item.setActionView(new View(context));
                        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                            @Override
                            public boolean onMenuItemActionExpand(MenuItem item) {
                                return false;
                            }

                            @Override
                            public boolean onMenuItemActionCollapse(MenuItem item) {
                                return false;
                            }
                        });

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        FloatingActionButton btnCreatePostOK = (FloatingActionButton) findViewById(R.id.btnCreatePostOK);
        btnCreatePostOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.setPostDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                TextView content = (TextView) findViewById(R.id.editTextCreatePost);
                post.setContent(content.getText().toString());
                showProgressDialog();
                List<String> images = getStringImages(adapter.getListBitmaps());
                for (int i = 0; i < images.size(); ++i) {
                    new UploadImagesAsyncTask(images.get(i), i, images.size()).execute();
                }
            }
        });
    }

    private ArrayList<String> listPostTag = new ArrayList<>();

    private  void setTxtCreatePostTag(boolean checked, String title){
        if (checked)
            listPostTag.add(title);
        else
            listPostTag.remove(title);
        String text = TextUtils.join(", ", listPostTag);
        TextView txtCreatePostTag = (TextView) findViewById(R.id.txtCreatePostTag);
        txtCreatePostTag.setText(text);
    }

//**********************************************************************************************************************

    private class UploadImagesAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String encodedImage;
        private int index;
        private int size;

        public UploadImagesAsyncTask(String image, int index, int size) {
            encodedImage = image;
            this.index = index;
            this.size = size;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                String imageUrl = gson.fromJson(res, String.class);
                listImageUrls.add(imageUrl);
                if(listImageUrls.size() == size) {
                    new CreatePostAsyncTask().execute();
                }
            }
            else {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Upload Image Error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "uploadImage";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("binary", encodedImage);
                jsonobj.put("postID", post.getPostID());
                jsonobj.put("indexs", "" + index);
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
            for (String string : listPostTag) {
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
                delegationHelper.doSomeThing();
            }
            progressDialog.dismiss();
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
                jsonobj.put("feeling", post.feeling);
                jsonobj.put("listImages", getListImages());
                jsonobj.put("tag", tag);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }
    }

/*************************************************************************************************************************/
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

    public Post getPost(){
        return post;
    }

    private String getListImages(){
        String sss = "";
        for (String item : listImageUrls){
            sss += item + ";";
        }
        sss = sss.substring(0, sss.length() - 1);
        return sss;
    }

    ProgressDialog progressDialog = null;
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
    }

    /*********************************************************************************************************/
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        fragmentManager.beginTransaction().remove(supportMapFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        InitContentView();
    }

    private void InitContentView(){
        findViewById(R.id.MapCreatePostMap).setVisibility(View.INVISIBLE);

        LocationHelper locationHelper = new LocationHelper(context);
        post.Latitude = locationHelper.GetLatitude();
        post.Longitude = locationHelper.GetLongitude();
        post.feeling = CONSTANT.EMOTION_STRING_HAPPY;

        String address = new GeolocatorAddressHelper(context, post.Latitude, post.Longitude ).GetAddress();
        TextView txtCreatePostLocation = (TextView) findViewById(R.id.txtCreatePostLocation);
        txtCreatePostLocation.setText(address);

        supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.MapCreatePostMap);
        supportMapFragment.getMapAsync(this);
    }

    /*******************************************************************************************************************************/
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
        post.Latitude = latLng.latitude;
        post.Longitude = latLng.longitude;
        Log.i("lat" + post.Latitude, "long" + post.Longitude);
        AddCurrentMarker();

        String address = new GeolocatorAddressHelper(context, post.Latitude, post.Longitude ).GetAddress();
        TextView txtCreatePostLocation = (TextView) findViewById(R.id.txtCreatePostLocation);
        txtCreatePostLocation.setText(address);

        Toast.makeText(context, "You mean: " + address, Toast.LENGTH_SHORT).show();
    }

    private void AddCurrentMarker(){
        this.googleMap.clear();
        MarkerOptions markerOption = new MarkerOptions()
                .position(new LatLng(post.Latitude, post.Longitude))
                .icon(BitmapDescriptorFactory.fromResource(GLOBAL.EMOTION.get(post.feeling)))
                .title(post.feeling);
        this.googleMap.addMarker(markerOption);
    }

    private void InitilizeMap() {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(post.Latitude, post.Longitude), 17));
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
