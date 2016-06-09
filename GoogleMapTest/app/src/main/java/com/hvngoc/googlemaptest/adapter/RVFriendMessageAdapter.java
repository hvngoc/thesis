package com.hvngoc.googlemaptest.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.ChatActivity;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.model.Friend;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hoang Van Ngoc on 24/03/2016.
 */
public class RVFriendMessageAdapter extends RecyclerView.Adapter<RVFriendMessageAdapter.ViewHolder>{
    private List<Friend> mItems;

    public RVFriendMessageAdapter(List<Friend> listItems) {
        super();
        mItems = listItems;
        Log.i("Friend MEssage", "" + mItems.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_friend_message, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Friend item = mItems.get(i);
        viewHolder.txt_friendUserName.setText(item.getName());
        viewHolder.txt_friendNum.setText(item.getNumFriend() + "");
        viewHolder.txt_friendMutual.setText(item.getMutualFriend() + "");
        Picasso.with(GLOBAL.CurrentContext).load(item.getAvatar()).error(R.drawable.icon_profile).into(viewHolder.img_friendAvatar);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CircleImageView img_friendAvatar;
        public TextView txt_friendUserName;
        public TextView txt_friendNum;
        public TextView txt_friendMutual;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            img_friendAvatar = (CircleImageView)itemView.findViewById(R.id.img_friendAvatar);
            txt_friendUserName = (TextView)itemView.findViewById(R.id.txt_friendUserName);
            txt_friendNum = (TextView)itemView.findViewById(R.id.txt_friendNum);
            txt_friendMutual = (TextView) itemView.findViewById(R.id.txt_friendMutual);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String fromUserID = mItems.get(position).getId();
            Intent intent = new Intent(GLOBAL.CurrentContext, ChatActivity.class);
            intent.putExtra("fromUserID", fromUserID);
            intent.putExtra("name",mItems.get(position).getName());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            GLOBAL.CurrentContext.startActivity(intent);
        }
    }
}
