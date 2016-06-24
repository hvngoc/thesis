package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVAdapter;
import com.hvngoc.googlemaptest.adapter.RVTourAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Tour;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyImagesFragment extends Fragment {

    private RecyclerView list_tours;
    private String currentID;
    private LinearLayout listNothing;

    public MyImagesFragment() {
        // Required empty public constructor
    }

    public static MyImagesFragment getInstance(String id){
        MyImagesFragment fragment = new MyImagesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        currentID = args.getString("id");
    }

    private void SetContentView(int recycler, int nothing){
        list_tours.setVisibility(recycler);
        listNothing.setVisibility(nothing);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_images, container, false);

        list_tours = (RecyclerView) rootView.findViewById(R.id.list_images);
        list_tours.setLayoutManager(new LinearLayoutManager(GLOBAL.CurrentContext));
        list_tours.setHasFixedSize(true);

        listNothing = (LinearLayout)rootView.findViewById(R.id.list_wall_nothing);

        return rootView;
    }

    ProgressDialog progressDialog = null;
    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        new LoadMyTourAsyncTask().execute();
    }

    private class LoadMyTourAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "getAllMyTour";
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
            if (result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Tour>>() {}.getType();
                ArrayList<Tour> listTour = gson.fromJson(res, listType);
                list_tours.setAdapter(new RVTourAdapter(listTour));
            }
            else {
                SetContentView(View.INVISIBLE, View.VISIBLE);
            }
            progressDialog.dismiss();
        }
    }

}
