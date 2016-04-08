package com.hvngoc.googlemaptest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.hvngoc.googlemaptest.R;

public class FriendFindFragment extends Fragment {

    CardView cardFindFriend;
    ImageView btnFriendSearch;
    public FriendFindFragment() {
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
        View view = inflater.inflate(R.layout.fragment_friend_find, container, false);
        cardFindFriend = (CardView) view.findViewById(R.id.cardFindFriend);
        cardFindFriend.setVisibility(View.INVISIBLE);

        btnFriendSearch = (ImageView) view.findViewById(R.id.btnFriendSearch);
        btnFriendSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send query and then set to card view
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
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