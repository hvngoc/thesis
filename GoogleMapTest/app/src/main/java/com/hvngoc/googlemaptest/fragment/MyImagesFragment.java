package com.hvngoc.googlemaptest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.UserProfileAdapter;


public class MyImagesFragment extends Fragment {

    public MyImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_images, container, false);
        RecyclerView listImages = (RecyclerView) rootView.findViewById(R.id.list_images);
        StaggeredGridLayoutManager layoutManager;
        layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        listImages.setLayoutManager(layoutManager);
        listImages.setAdapter(new UserProfileAdapter(GLOBAL.CurrentContext));
        return rootView;
    }



}
