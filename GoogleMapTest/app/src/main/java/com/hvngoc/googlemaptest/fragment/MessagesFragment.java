package com.hvngoc.googlemaptest.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.adapter.RVFriendAdapter;
import com.hvngoc.googlemaptest.adapter.RVMessageAdapter;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.model.ChatMessage;
import com.hvngoc.googlemaptest.model.Friend;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MessagesFragment extends Fragment {

    private RecyclerView recyclerListMessage;


    public MessagesFragment() {
        // Required empty public constructor
    }

    ProgressDialog progressDialog = null;

    @Override
    public void onStart() {
        super.onStart();
        /*
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        new LoadMessageAsyncTask().execute();
        */

        RVMessageAdapter adapter = getSampleAdapter();
        recyclerListMessage.setAdapter(adapter);
    }

    private RVMessageAdapter getSampleAdapter() {
        ArrayList<ChatMessage> adapter = new ArrayList<ChatMessage>();
        for (int i = 0; i < 5; i++) {
            ChatMessage mess = new ChatMessage(true, "How are you");
            adapter.add(mess);
        }
        return new RVMessageAdapter(adapter, getFragmentManager(), getActivity());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        recyclerListMessage = (RecyclerView) rootView.findViewById(R.id.recycler_message);
        LinearLayoutManager llm = new LinearLayoutManager(GLOBAL.CurentContext);
        recyclerListMessage.setLayoutManager(llm);
        recyclerListMessage.setHasFixedSize(true);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }



    private class LoadMessageAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "getAllMessages";
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
                Type listType = new TypeToken<ArrayList<ChatMessage>>(){}.getType();
                ArrayList<ChatMessage> listMessage = gson.fromJson(res, listType);
                RVMessageAdapter adapter = new RVMessageAdapter(listMessage, getActivity().getSupportFragmentManager(), getActivity());
                recyclerListMessage.setAdapter(adapter);
            }
            progressDialog.dismiss();
        }
    }
}