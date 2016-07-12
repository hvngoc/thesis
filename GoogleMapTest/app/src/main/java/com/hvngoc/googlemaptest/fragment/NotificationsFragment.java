package com.hvngoc.googlemaptest.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.activity.MainPageActivity;
import com.hvngoc.googlemaptest.adapter.RVNotificationAdapter;
import com.hvngoc.googlemaptest.helper.BarBadgeHelper;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.MessageDelegationHelper;
import com.hvngoc.googlemaptest.model.NotificationItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class NotificationsFragment extends Fragment {

    private RVNotificationAdapter rvNotificationAdapter;

    public NotificationsFragment() {
        rvNotificationAdapter = new RVNotificationAdapter();
        startLoading();
        Log.i("NOTIFICATION", "CONSTRUCTOR");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i("Notification", "CREATE");
    }

    @Override
    public void onPause() {
        super.onPause();
        BarBadgeHelper.notificationCount = 0;
        ((MainPageActivity)mContext).setDelegationHelper(null);
        ((MainPageActivity)mContext).setMessageDelegationHelper(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainPageActivity)mContext).setDelegationHelper(null);
        ((MainPageActivity)mContext).setMessageDelegationHelper(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        BarBadgeHelper.notificationCount = 0;
        ((MainPageActivity)mContext).setDelegationHelper(new DelegationHelper() {
            @Override
            public void doSomeThing() {
                progressDialog.show();
                new LoadNotificationsAsyncTask().execute();
            }
        });

        ((MainPageActivity)mContext).setMessageDelegationHelper(new MessageDelegationHelper() {
            @Override
            public void doSomething(String message, String param) {
                if (!message.equals(CONSTANT.NOTIFICATION_MESSAGE) && !message.equals(CONSTANT.NOTIFICATION_ADD_FRIEND)) {
                    progressDialog.show();
                    new LoadNotificationsAsyncTask().execute();
                }
            }
        });
        GLOBAL.MAIN_PAGE_POSITION_VIEW = CONSTANT.BOTTOM_NOTIFICATION;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(GLOBAL.CurrentContext));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(rvNotificationAdapter);

        Log.i("notification", "CREATE VIEW");

        return rootView;
    }


    private ProgressDialog progressDialog = null;
    private void startLoading() {
        progressDialog = new ProgressDialog(GLOBAL.CurrentContext,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(GLOBAL.CurrentContext.getString(R.string.loading));
        progressDialog.show();
        new LoadNotificationsAsyncTask().execute();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }


    private Context mContext;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        Log.i("notification", "ATTACH");
    }


    private class LoadNotificationsAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "getUserNotification";
            JSONObject json = new JSONObject();
            try {
                json.put("userID", GLOBAL.CurrentUser.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            helper = new HTTPPostHelper(serverUrl, json);
            return helper.sendHTTTPostRequest();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                String res = helper.getResponse();
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<NotificationItem>>(){}.getType();
                ArrayList<NotificationItem> list = gson.fromJson(res, listType);
                rvNotificationAdapter.addListItem(list);
            }else {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, new NothingsFragment());
                fragmentTransaction.commit();
            }
            progressDialog.dismiss();
        }
    }
}
