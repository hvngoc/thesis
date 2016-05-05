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
import com.hvngoc.googlemaptest.helper.DatePickerHelper;
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

        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerHelper(getActivity().getFragmentManager(), birthdate);
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
                .error(R.drawable.default_icon)         // optional
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