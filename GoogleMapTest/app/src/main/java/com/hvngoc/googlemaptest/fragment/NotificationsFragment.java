package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVNotificationAdapter;


public class NotificationsFragment extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton  fabClear;

    public NotificationsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        fabClear = (FloatingActionButton) rootView.findViewById(R.id.fabNotificationClear);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(GLOBAL.CurentContext));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RVNotificationAdapter());
        return rootView;
    }

//    ProgressDialog progressDialog = null;
//    @Override
//    public void onStart() {
//        super.onStart();
//        progressDialog = new ProgressDialog(getActivity(),
//                R.style.AppTheme_Dark_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//        new LoadNotificationsAsyncTask().execute();
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
