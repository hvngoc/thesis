package com.hvngoc.googlemaptest.adapter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.CONSTANT;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.fragment.ProfileFragment;
import com.hvngoc.googlemaptest.helper.HTTPPostHelper;
import com.hvngoc.googlemaptest.helper.ParseDateTimeHelper;
import com.hvngoc.googlemaptest.model.NotificationItem;
import com.hvngoc.googlemaptest.model.Post;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hoang Van Ngoc on 01/04/2016.
 */
public class RVNotificationAdapter extends RecyclerView.Adapter<RVNotificationAdapter.ViewHolder>{

    private ArrayList<NotificationItem> mItems;
    private FragmentTransaction fragmentTransaction;

    public RVNotificationAdapter(FragmentTransaction fragmentTransaction) {
        super();
        this.fragmentTransaction = fragmentTransaction;
        this.mItems = new ArrayList<>();
    }

    public void addListItem(ArrayList<NotificationItem> list){
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_notification, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        NotificationItem item = mItems.get(i);
        Picasso.with(GLOBAL.CurentContext).load(item.getUserAvatar()).error(R.drawable.icon_profile).into(viewHolder.imgAvatar);
        viewHolder.txtUserName.setText(item.getUserName());
        viewHolder.txtNotificationString.setText(item.getContent());
        viewHolder.txtNotificationDate.setText(ParseDateTimeHelper.parse(item.getDate()));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        public ImageView imgAvatar;
        public TextView txtUserName, txtNotificationString;
        public ImageView btnNotificationDelete;
        public TextView txtNotificationDate;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            imgAvatar = (ImageView)itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView)itemView.findViewById(R.id.txtUserName);
            txtNotificationString = (TextView)itemView.findViewById(R.id.txtNotificationString);
            btnNotificationDelete = (ImageView) itemView.findViewById(R.id.btnNotificationDelete);
            txtNotificationDate = (TextView) itemView.findViewById(R.id.txtNotificationDate);

            btnNotificationDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String dataId = mItems.get(position).getDataID();
                    new DeleteNotificationAsyncTask(dataId, position).execute();
                }
            });
        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            NotificationItem item = mItems.get(position);
            new DeleteNotificationAsyncTask(item.getDataID(), position).execute();
            switch (item.getSaveContent()){
                case CONSTANT.NOTIFICATION_ADD_FRIEND:
                    fragmentTransaction.replace(R.id.container_body, ProfileFragment.getInstance(item.getDataID(), CONSTANT.TYPE_REQUEST));
                    fragmentTransaction.commit();
                    break;
                case CONSTANT.NOTIFICATION_CONFIRM_FRIEND:
                    fragmentTransaction.replace(R.id.container_body, ProfileFragment.getInstance(item.getDataID(), CONSTANT.TYPE_FRIEND));
                    fragmentTransaction.commit();
                    break;
                default:
                    new GetPostDetailAsyncTask(item.getDataID()).execute();
                    break;
            }
        }
    }

//    ***********************************************************************************************************

    private class DeleteNotificationAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String dataID;
        private int position;
        public DeleteNotificationAsyncTask(String id, int position){
            this.dataID = id;
            this.position = position;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "deleteNotification";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("dataID", dataID);
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
                mItems.remove(position);
                notifyDataSetChanged();
            }
        }
    }

    private class GetPostDetailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private HTTPPostHelper helper;
        private String dataID;
        public GetPostDetailAsyncTask(String id){
            this.dataID = id;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String serverUrl = GLOBAL.SERVER_URL + "getPostDetail";
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("userID", GLOBAL.CurrentUser.getId());
                jsonobj.put("dataID", dataID);
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
                Post post = gson.fromJson(res, Post.class);
                Intent intent = new Intent("android.intent.action.NEWS_DETAIL");
                intent.putExtra("currentPost", post);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                GLOBAL.CurentContext.startActivity(intent);
            }
        }
    }

}
