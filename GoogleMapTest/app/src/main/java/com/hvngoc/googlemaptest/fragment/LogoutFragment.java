package com.hvngoc.googlemaptest.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.activity.LoginActivity;
import com.hvngoc.googlemaptest.services.LocationNotifierService;
import com.squareup.picasso.Picasso;


public class LogoutFragment extends Fragment {

    ImageView img_logout_avatar;
    TextView txt_logout_name;
    Button btn_logout_logout;

    public LogoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_logout, container, false);
        img_logout_avatar = (ImageView) rootView.findViewById(R.id.img_logout_avatar);
        txt_logout_name = (TextView) rootView.findViewById(R.id.txt_logout_name);
        btn_logout_logout = (Button) rootView.findViewById(R.id.btn_logout_logout);

        Picasso.with(GLOBAL.CurentContext).load(GLOBAL.CurrentUser.getAvatar()).error(R.drawable.icon_profile).into(img_logout_avatar);
        txt_logout_name.setText(GLOBAL.CurrentUser.getName());
        btn_logout_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GLOBAL.startedUserHelper.clear();
                GLOBAL.CurrentUser = null;
                getActivity().finish();
                getActivity().stopService(new Intent(getContext().getApplicationContext(), LocationNotifierService.class));
                getActivity().startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
