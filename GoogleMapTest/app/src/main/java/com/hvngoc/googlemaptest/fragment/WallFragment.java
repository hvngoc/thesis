package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.hvngoc.googlemaptest.activity.BaseActivity;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVAdapter;
import com.hvngoc.googlemaptest.custom.PostCreationDialog;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class WallFragment extends Fragment {

    private RecyclerView listPosts;
    private RVAdapter adapter;
    private String currentID;
    private LinearLayout listNothing;

    public WallFragment() {
        // Required empty public constructor
    }
    public static WallFragment getInstance(String currentID) {
        WallFragment fragment = new WallFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", currentID);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        currentID = args.getString("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((BaseActivity)getActivity()).setActionBarTitle("Wall");
        View rootView = inflater.inflate(R.layout.fragment_wall, container, false);
        listPosts = (RecyclerView) rootView.findViewById(R.id.list_wall_post);
        LinearLayoutManager llm = new LinearLayoutManager(GLOBAL.CurentContext);
        listPosts.setLayoutManager(llm);
        adapter = new RVAdapter();
        listPosts.setAdapter(adapter);
        listPosts.setHasFixedSize(true);

        listNothing = (LinearLayout)rootView.findViewById(R.id.list_wall_nothing);

        FloatingActionButton btnCreateNewPost = (FloatingActionButton) rootView.findViewById(R.id.btnCreateNewPost);
        btnCreateNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PostCreationDialog dialog = new PostCreationDialog();
                dialog.setDelegationHelper(new DelegationHelper() {
                    @Override
                    public void doSomeThing() {
                        SetContentView(View.VISIBLE, View.INVISIBLE);
                        Post post = dialog.getPost();
                        adapter.addToFirst(post);
                        listPosts.invalidate();
                        dialog.dismiss();
                    }
                });
                dialog.show(getFragmentManager(), "PostCreationDialog");
            }
        });

        if (!currentID.equals(GLOBAL.CurrentUser.getId()))
            btnCreateNewPost.setVisibility(View.INVISIBLE);

        return rootView;
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
        new LoadPostAsyncTask().execute();

    }

    private void SetContentView(int recycler, int nothing){
        listPosts.setVisibility(recycler);
        listNothing.setVisibility(nothing);
    }

    private class LoadPostAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "getAllPostOfUser";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", currentID);
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
                SetContentView(View.VISIBLE, View.INVISIBLE);
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Post>>(){}.getType();
                ArrayList<Post> listPost = gson.fromJson(res, listType);
                adapter.addListPost(listPost);
            }
            else {
                SetContentView(View.INVISIBLE, View.VISIBLE);
            }
            progressDialog.dismiss();
        }
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
