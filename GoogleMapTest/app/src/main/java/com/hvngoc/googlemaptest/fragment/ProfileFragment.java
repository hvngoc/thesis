package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.PickPictureHelper;
import com.hvngoc.googlemaptest.model.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class ProfileFragment extends Fragment {

    Profile profile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    ImageView avatar;
    FloatingActionButton pick_image;
    EditText username;
    EditText email;
    RadioGroup radioSex;
    RadioButton radioMale, radioFemale;
    EditText birthdate;
    EditText address;
    FloatingActionButton save, cancel, edit_profile;
    TextView numFriend, numFollow, numPost;
    Bitmap bitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        avatar = (ImageView) view.findViewById(R.id.avatar);
        pick_image = (FloatingActionButton) view.findViewById(R.id.pick_image);
        username = (EditText) view.findViewById(R.id.username);
        email = (EditText) view.findViewById(R.id.email);
        radioSex = (RadioGroup) view.findViewById(R.id.radioSex);
        radioMale = (RadioButton) view.findViewById(R.id.radioMale);
        radioFemale = (RadioButton) view.findViewById(R.id.radioFemale);
        birthdate = (EditText) view.findViewById(R.id.birthdate);
        address = (EditText) view.findViewById(R.id.address);
        save = (FloatingActionButton) view.findViewById(R.id.save);
        cancel = (FloatingActionButton) view.findViewById(R.id.cancel);
        edit_profile = (FloatingActionButton) view.findViewById(R.id.edit_profile);
        numFriend = (TextView) view.findViewById(R.id.numFriend);
        numFollow = (TextView) view.findViewById(R.id.numFollow);
        numPost = (TextView) view.findViewById(R.id.numPost);

        SetEnableView(false, View.VISIBLE, View.INVISIBLE);

        radioSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        pick_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PickPictureHelper pickPictureHelper = new PickPictureHelper(getContext(), false);
                pickPictureHelper.setOnOKClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String imageURI = pickPictureHelper.getOnlyOnePicture();
                        //if uri = null then set default avatar for this instance
                        bitmap = BitmapFactory.decodeFile(imageURI);
                        avatar.setImageBitmap(bitmap);
                        pickPictureHelper.dismiss();
                    }
                });
                pickPictureHelper.show();
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetEnableView(true, View.INVISIBLE, View.VISIBLE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetEnableView(false, View.VISIBLE, View.INVISIBLE);
                new UpadteProfileAsyncTask().execute();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetEnableView(false, View.VISIBLE, View.INVISIBLE);
                SetContentProfileView();
            }
        });

        return view;
    }

    private void SetEnableView(boolean enable, int visibleEdit, int visibleSave){
        username.setEnabled(enable);
        radioSex.setEnabled(enable);
        birthdate.setEnabled(enable);
        address.setEnabled(enable);

        edit_profile.setVisibility(visibleEdit);

        pick_image.setVisibility(visibleSave);
        cancel.setVisibility(visibleSave);
        save.setVisibility(visibleSave);
    }

    private void SetContentProfileView(){
        //avatar.setImageFromURL(profile.avatar);
        Picasso.with(GLOBAL.CurentContext)
                .load(profile.getAvatar())
                .error(R.drawable.bigbang)         // optional
                .into(avatar);
        numFriend.setText(profile.numFriend + "");
        numFollow.setText(profile.numFollow + "");
        numPost.setText(profile.numPost + "");
        username.setText(profile.name);
        birthdate.setText(profile.birthday);
        address.setText(profile.address);
        email.setText(profile.email);
        GLOBAL.CurrentUser.setName(profile.name);
        GLOBAL.CurrentUser.setAvatar(profile.avatar);
        if (profile.gender.equals("male")){
            radioMale.setChecked(true);
            //radioSex.check(R.id.radioMale);
        }
        else {
            radioFemale.setChecked(true);
            //radioSex.check(R.id.radioFemale);
        }
    }

    ProgressDialog progressDialog = null;
    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        new LoadProfileAsyncTask().execute();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
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
                profile = gson.fromJson(res, Profile.class);
                SetContentProfileView();
            }
            else{
                //it always load successfully.
                //only fail when we have a trouble with internet or server
            }
            progressDialog.dismiss();
        }
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private class UpadteProfileAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            txtGender = checkFemale? "female": "male";
            if(bitmap != null)
                binary = getStringImage(bitmap);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Profile profile2 = gson.fromJson(res, Profile.class);
                updateProfile(profile2);
                SetContentProfileView();
            }
            else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }

        private void updateProfile(Profile p) {
            profile.name = p.name;
            profile.avatar = p.avatar;
            profile.address = p.address;
            profile.birthday = p.birthday;
            profile.gender = p.gender;
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


//package com.hvngoc.googlemaptest.activity;
//
//import android.app.ProgressDialog;
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.provider.MediaStore;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.content.CursorLoader;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.gson.Gson;
//import com.hvngoc.googlemaptest.R;
//import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
//import com.hvngoc.googlemaptest.model.Profile;
//import com.hvngoc.googlemaptest.model.User;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.Hashtable;
//import java.util.Map;
//import java.util.concurrent.Future;
//
//public class ProfileActivity extends AppCompatActivity {
//
//    private ImageView avatar;
//    private EditText username;
//    private EditText email;
//    private RadioGroup radioSex;
//    private EditText birthdate;
//    private EditText address;
//    private Button saveBtn;
//    private Button cancelBtn;
//    private FloatingActionButton pickImage;
//    private Bitmap bitmap = null;
//    private Boolean avatarChanged = false;
//    ProgressDialog progressDialog = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//        loadComponents();
//        loadData();
//        setOnlickButton();
//    }
//
//    private void setOnlickButton() {
//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                progressDialog = new ProgressDialog(ProfileActivity.this,
//                        R.style.AppTheme_Dark_Dialog);
//                progressDialog.setIndeterminate(true);
//                progressDialog.setMessage("Saving...");
//                progressDialog.show();
//                new UpdateProfileAsyncTask().execute();
//            }
//        });
//
//        pickImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent fintent = new Intent(Intent.ACTION_GET_CONTENT);
//                fintent.setType("image/jpg");
//                try {
//                    startActivityForResult(fintent, 100);
//                } catch (ActivityNotFoundException e) {
//
//                }
//            }
//        });
//
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//
//    private void loadData() {
//        Bundle extras = getIntent().getExtras();
//        username.setText(extras.getString("name"));
//        email.setText(extras.getString("email"));
//        birthdate.setText(extras.getString("birthdate"));
//        address.setText(extras.getString("address"));
//        RadioButton sexBtn = (RadioButton) findViewById(extras.getInt("sex"));
//        sexBtn.setChecked(true);
//    }
//
//    private void loadComponents() {
//        avatar = (ImageView) findViewById(R.id.avatar);
//        username = (EditText) findViewById(R.id.username);
//        email = (EditText) findViewById(R.id.email);
//        radioSex = (RadioGroup) findViewById(R.id.radioSex);
//        birthdate = (EditText) findViewById(R.id.birthdate);
//        address = (EditText) findViewById(R.id.address);
//        saveBtn = (Button) findViewById(R.id.save);
//        cancelBtn = (Button) findViewById(R.id.cancel);
//        pickImage = (FloatingActionButton) findViewById(R.id.pick_image);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//    }
//
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (data == null)
//            return;
//        switch (requestCode) {
//            case 100:
//                if (resultCode == RESULT_OK) {
//                    //imgPath = getPathFromURI(data.getData());
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    avatar.setImageBitmap(bitmap);
//                    avatarChanged = true;
//                }
//        }
//    }
//
//    private String getPathFromURI(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
//
//    public String getStringImage(Bitmap bmp){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//    }
//
//
//    private class UpdateProfileAsyncTask extends AsyncTask<Void, Void, Boolean> {
//        private String _username;
//        private String _email;
//        private String _address;
//        private String _birthday;
//        private int _gender;
//        HTTPPostHelper helper;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            this._email = email.getText().toString();
//            this._username = username.getText().toString();
//            this._address = address.getText().toString();
//            this._birthday = birthdate.getText().toString();
//            this._gender = radioSex.getCheckedRadioButtonId();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            return postData();
//        }
//
//
//        private Boolean postData() {
//
//            String serverUrl = GLOBAL.SERVER_URL + "updateProfile";
//            JSONObject jsonobj = new JSONObject();
//            try {
//                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
//                jsonobj.put("email", this._email);
//                jsonobj.put("username", this._username);
//                jsonobj.put("address", this._address);
//                jsonobj.put("birthday", this._birthday);
//                jsonobj.put("gender", this._gender);
//                if(avatarChanged && bitmap != null)
//                    jsonobj.put("avatar", getStringImage(bitmap));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            helper = new HTTPPostHelper(serverUrl, jsonobj);
//            return helper.sendHTTTPostRequest();
//        }
//
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//            progressDialog.dismiss();
//            if (result) {
//                finish();
//            } else {
//                Toast.makeText(ProfileActivity.this, "Error! Please try again", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//
//
//
//}
