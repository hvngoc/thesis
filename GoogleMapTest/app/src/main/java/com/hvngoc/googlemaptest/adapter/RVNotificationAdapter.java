package com.hvngoc.googlemaptest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;

/**
 * Created by Hoang Van Ngoc on 01/04/2016.
 */
public class RVNotificationAdapter extends RecyclerView.Adapter<RVNotificationAdapter.ViewHolder>{

    public RVNotificationAdapter() {
        super();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_notification, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgAvatar;
        public TextView txtUserName, txtNotificationString;
        public ImageView btnNotificationDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView)itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView)itemView.findViewById(R.id.txtUserName);
            txtNotificationString = (TextView)itemView.findViewById(R.id.txtNotificationString);
            btnNotificationDelete = (ImageView) itemView.findViewById(R.id.btnNotificationDelete);
        }
    }
}
