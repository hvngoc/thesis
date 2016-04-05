package com.hvngoc.googlemaptest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;


public class SettingsFragment extends Fragment {
    public SettingsFragment() {
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
        View rootView =  inflater.inflate(R.layout.fragment_settings, container, false);

        final MultiAutoCompleteTextView editSpecialTag = (MultiAutoCompleteTextView) rootView.findViewById(R.id.editSpecialTag);
        editSpecialTag.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, GLOBAL.listTag));
        editSpecialTag.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        editSpecialTag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editSpecialTag.showDropDown();
                return false;
            }
        });

        final MultiAutoCompleteTextView editSpecialFriend = (MultiAutoCompleteTextView) rootView.findViewById(R.id.editSpecialFriend);
        editSpecialFriend.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, GLOBAL.listTag));
        editSpecialFriend.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        editSpecialFriend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editSpecialFriend.showDropDown();
                return false;
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
