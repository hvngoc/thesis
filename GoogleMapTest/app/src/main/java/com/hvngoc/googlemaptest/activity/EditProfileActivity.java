package com.hvngoc.googlemaptest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.helper.DatePickerHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.imagechooser.CustomGalleryActivity;
import com.hvngoc.googlemaptest.model.Profile;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {
    ImageView avatar;
    FloatingActionButton pick_image;
    EditText username;
    EditText email;
    RadioGroup radioSex;
    RadioButton radioMale, radioFemale;
    EditText birthdate;
    EditText address;
    ImageButton save, cancel;
    Bitmap bitmap;
    ImageLoader imageLoader;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        GLOBAL.CurrentContext = this;
        profile = WallActivity.currentPofile;
        initComponents();
        initImageLoader();
        loadUserProfile();
    }

    private void loadUserProfile() {
        Picasso.with(GLOBAL.CurrentContext)
                .load(profile.getAvatar())
                .error(R.drawable.icon_no_image)         // optional
                .into(avatar);
        username.setText(profile.name);
        email.setText(profile.email);
        if(profile.gender != null && profile.gender.equals("male"))
            radioMale.setChecked(true);
        else
            radioFemale.setChecked(true);
        birthdate.setText(profile.birthday);
        address.setText(profile.address);
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
    }

    ProgressDialog progressDialog = null;

    private void initComponents() {
        avatar = (ImageView) findViewById(R.id.avatar);
        pick_image = (FloatingActionButton) findViewById(R.id.pick_image);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        radioSex = (RadioGroup) findViewById(R.id.radioSex);
        radioMale = (RadioButton) findViewById(R.id.radioMale);
        radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        birthdate = (EditText) findViewById(R.id.birthdate);
        address = (EditText) findViewById(R.id.address);
        save = (ImageButton) findViewById(R.id.save);
        cancel = (ImageButton) findViewById(R.id.cancel);

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerHelper(getFragmentManager(), birthdate);
            }
        });

        pick_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomGalleryActivity.ACTION_PICK);
                startActivityForResult(i, 100);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(GLOBAL.CurrentContext, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Uploading...");
                progressDialog.show();
                new UpdateProfileAsyncTask().execute();
            }
        });
    }


    String singlePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            singlePath = data.getStringExtra("single_path");
            bitmap = BitmapFactory.decodeFile(singlePath);
            if(bitmap.getWidth() > 240 || bitmap.getHeight() > 240)
                bitmap = Bitmap.createScaledBitmap(bitmap, 240, 240, true);
            avatar.setImageBitmap(bitmap);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private class UpdateProfileAsyncTask extends AsyncTask<Void, Void, Boolean> {
        String txtName;
        String txtAddress;
        String txtBirthday;
        String txtGender;
        String binary = null;
        private HTTPPostHelper helper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtName = username.getText().toString();
            txtAddress = address.getText().toString();
            txtBirthday = birthdate.getText().toString();
            Boolean checkFemale = radioFemale.isChecked();
            txtGender = checkFemale ? "female": "male";
            if(bitmap != null)
                binary = getStringImage(bitmap);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                profile = gson.fromJson(res, Profile.class);
                updateProfile();
            }
            else {
                Toast.makeText(GLOBAL.CurrentContext, "Error", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
            finish();
            Intent intent = new Intent(EditProfileActivity.this, WallActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("id", GLOBAL.CurrentUser.getId());
            startActivity(intent);
        }

        private void updateProfile() {
            GLOBAL.CurrentUser.setName(profile.name);
            GLOBAL.CurrentUser.setAvatar(profile.avatar);
            GLOBAL.startedUserHelper.saveUser(GLOBAL.CurrentUser);
        }

        private Boolean postData() {
            String serverUrl = GLOBAL.SERVER_URL + "updateProfile";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("username", txtName);
                jsonobj.put("address", txtAddress);
                jsonobj.put("birthday", txtBirthday);
                jsonobj.put("gender", txtGender);
                jsonobj.put("binaryImage", binary);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, jsonobj);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return postData();
        }
    }
}
