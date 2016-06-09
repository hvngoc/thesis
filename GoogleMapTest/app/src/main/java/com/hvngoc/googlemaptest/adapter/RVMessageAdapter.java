package com.hvngoc.googlemaptest.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.ChatActivity;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.model.ChatMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 12125_000 on 5/17/2016.
 */
public class RVMessageAdapter extends RecyclerView.Adapter<RVMessageAdapter.ViewHolder> {
    List<ChatMessage> mItems;


    public RVMessageAdapter(){
        mItems = new ArrayList<>();
    }

    public RVMessageAdapter(List<ChatMessage> listMessage) {
        this.mItems = listMessage;
        Collections.reverse(this.mItems);
    }

    public int addMessage(ChatMessage message){
        mItems.add(message);
        notifyDataSetChanged();
        return mItems.size() - 1;
    }
    public int addListComment(ArrayList<ChatMessage> listMessage){
        mItems.addAll(listMessage);
        notifyDataSetChanged();
        return mItems.size() - 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_message, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RVMessageAdapter.ViewHolder holder, int position) {
        ChatMessage item = mItems.get(position);
        holder.txtMessage.setText(item.getMessage());
        holder.txtUserName.setText(item.getSenderName());
        Picasso.with(GLOBAL.CurrentContext).load(item.getSenderAvatar()).error(R.drawable.icon_profile).into(holder.imgAvatar);
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imgAvatar;
        public TextView txtUserName;
        public TextView txtMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (CircleImageView)itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView)itemView.findViewById(R.id.txtUserName);
            txtMessage = (TextView)itemView.findViewById(R.id.txtMessage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String fromUserID = mItems.get(position).getSenderID();
                    Intent intent = new Intent(GLOBAL.CurrentContext, ChatActivity.class);
                    intent.putExtra("fromUserID", fromUserID);
                    intent.putExtra("name", mItems.get(position).getSenderName());
                    GLOBAL.CurrentContext.startActivity(intent);
                }
            });
        }
    }


}
