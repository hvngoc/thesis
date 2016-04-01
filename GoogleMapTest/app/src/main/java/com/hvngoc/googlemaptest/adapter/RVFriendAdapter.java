package com.hvngoc.googlemaptest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.model.Friend;

import java.util.List;

/**
 * Created by Hoang Van Ngoc on 24/03/2016.
 */
public class RVFriendAdapter extends RecyclerView.Adapter<RVFriendAdapter.ViewHolder>{
    List<Friend> mItems;
    int visibilityAdd;

    public RVFriendAdapter(List<Friend> listItems, int visibilityAdd) {
        super();
        mItems = listItems;
        this.visibilityAdd = visibilityAdd;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_friend, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Friend item = mItems.get(i);
        viewHolder.txt_friendUserName.setText(item.getName());
        viewHolder.txt_friendNum.setText(item.getNumFriend() + " friends. " + item.getMutualFriend() + " mutual friends.");
        viewHolder.btnFriendAdd.setVisibility(visibilityAdd);
        viewHolder.img_friendAvatar.setImageResource(R.drawable.icon_profile);//item.getAvatar();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img_friendAvatar;
        public TextView txt_friendUserName;
        public TextView txt_friendNum;
        public ImageView btnFriendAdd, btnFriendDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            img_friendAvatar = (ImageView)itemView.findViewById(R.id.img_friendAvatar);
            txt_friendUserName = (TextView)itemView.findViewById(R.id.txt_friendUserName);
            txt_friendNum = (TextView)itemView.findViewById(R.id.txt_friendNum);
            btnFriendAdd = (ImageView) itemView.findViewById(R.id.btnFriendAdd);
            btnFriendDelete = (ImageView) itemView.findViewById(R.id.btnFriendDelete);
        }
    }
}
