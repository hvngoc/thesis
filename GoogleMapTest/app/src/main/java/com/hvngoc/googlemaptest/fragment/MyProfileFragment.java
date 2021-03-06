package com.hvngoc.googlemaptest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.EditProfileActivity;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.activity.WallActivity;
import com.hvngoc.googlemaptest.model.Profile;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {
    Profile profile;

    public MyProfileFragment() {
        // Required empty public constructor
    }


    public static MyProfileFragment getInstance(String id) {
        MyProfileFragment fragment = new MyProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    ImageView avatar;
    TextView username;
    TextView email;
    RadioGroup radioSex;
    RadioButton radioMale, radioFemale;
    TextView birthdate;
    TextView address;
    FloatingActionButton edit_profile;
    String currentID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        currentID = args.getString("id");
        profile = WallActivity.currentPofile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);

        avatar = (ImageView) view.findViewById(R.id.avatar);
        username = (TextView) view.findViewById(R.id.username);
        email = (TextView) view.findViewById(R.id.email);
        radioSex = (RadioGroup) view.findViewById(R.id.radioSex);
        radioMale = (RadioButton) view.findViewById(R.id.radioMale);
        radioFemale = (RadioButton) view.findViewById(R.id.radioFemale);
        birthdate = (TextView) view.findViewById(R.id.birthdate);
        address = (TextView) view.findViewById(R.id.address);
        edit_profile = (FloatingActionButton) view.findViewById(R.id.edit_profile);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GLOBAL.CurrentContext, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        setContentProfileView();
        return view;
    }


    private void setContentProfileView(){
        if(currentID.equals(GLOBAL.CurrentUser.getId())) {
            edit_profile.setVisibility(View.VISIBLE);
        }
        Picasso.with(GLOBAL.CurrentContext)
                .load(profile.getAvatar())
                .error(R.drawable.icon_no_image)         // optional
                .into(avatar);
        username.setText(profile.name);
        birthdate.setText(profile.birthday);
        address.setText(profile.address);
        email.setText(profile.email);
        if (currentID.equals(GLOBAL.CurrentUser.getId())) {
            GLOBAL.CurrentUser.setName(profile.name);
            GLOBAL.CurrentUser.setAvatar(profile.avatar);
        }
        if (profile.gender != null && profile.gender.equals("male"))
            radioMale.setChecked(true);
        else
            radioFemale.setChecked(true);
        radioFemale.setEnabled(false);
        radioMale.setEnabled(false);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("PROFILE", "RESUME");
    }

}
