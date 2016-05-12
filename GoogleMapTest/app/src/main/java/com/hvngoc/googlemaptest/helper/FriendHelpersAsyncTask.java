package com.hvngoc.googlemaptest.helper;

import android.os.AsyncTask;
import android.view.View;

import com.google.gson.Gson;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.custom.ConfirmDialog;
import com.hvngoc.googlemaptest.model.Profile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 12125_000 on 5/6/2016.
 */
public class FriendHelpersAsyncTask {
    private DelegationHelper delegation;
    private String friendID;
    private ConfirmDialog confirmDialog;

    public FriendHelpersAsyncTask(String friendID) {
        this.friendID = friendID;
    }

    public void setDelegation(DelegationHelper delegater) {
        this.delegation = delegater;
    }

    public  void runDeleteRequestAsyncTask() {
        confirmDialog = new ConfirmDialog(GLOBAL.CurentContext, "Are you sure for deleting this request?");
        confirmDialog.setOnButtonOKClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                new DeleteRequestAsyncTask().execute();
            }
        });
        confirmDialog.show();
    }

    public  void runConfirmRequestAsyncTask() {
        confirmDialog = new ConfirmDialog(GLOBAL.CurentContext, "Are you sure for confirming this request?");
        confirmDialog.setOnButtonOKClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                new ConfirmFriendRequestAsyncTask().execute();
            }
        });
        confirmDialog.show();
    }

    public void runAddFriendAsyncTask( ) {
        confirmDialog = new ConfirmDialog(GLOBAL.CurentContext, "Are you sure for making a friend request?");
        confirmDialog.setOnButtonOKClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                new AddFriendAsyncTask().execute();
            }
        });
        confirmDialog.show();
    }

    public void runDeleteFriendAsyncTask() {
        confirmDialog = new ConfirmDialog(GLOBAL.CurentContext, "Are you sure for deleting from friend list?");
        confirmDialog.setOnButtonOKClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                new DeleteFriendAsyncTask().execute();
            }
        });
        confirmDialog.show();
    }

//    *********************************************************************************************************//

    private class DeleteRequestAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "deleteAddFriendRequest";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("friendID", friendID);
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
                if (delegation != null)
                    delegation.doSomeThing();
            }

        }
    }

    private class DeleteFriendAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "deleteFriend";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("friendID", friendID);
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
                if (delegation != null)
                    delegation.doSomeThing();
            }

        }
    }

    private class AddFriendAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "addFriend";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("friendID", friendID);
                jsonobj.put("day", ParseDateTimeHelper.getCurrent());
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
                if (delegation != null)
                    delegation.doSomeThing();
            }

        }
    }

    private class ConfirmFriendRequestAsyncTask extends AsyncTask<Void, Void, Boolean> {
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
            String serverUrl = GLOBAL.SERVER_URL + "confirmFriendRequest";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("friendID", friendID);
                jsonobj.put("day", ParseDateTimeHelper.getCurrent());
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
                if (delegation != null)
                    delegation.doSomeThing();
            }

        }
    }
}
