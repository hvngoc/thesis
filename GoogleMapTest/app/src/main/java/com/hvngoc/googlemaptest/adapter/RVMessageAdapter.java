package com.hvngoc.googlemaptest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.ChatActivity;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.fragment.ProfileFragment;
import com.hvngoc.googlemaptest.model.ChatMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by 12125_000 on 5/17/2016.
 */
public class RVMessageAdapter extends RecyclerView.Adapter<RVMessageAdapter.ViewHolder> {
    List<ChatMessage> mItems;
    FragmentManager fragmentManager;
    Context context;

    public RVMessageAdapter(){
        mItems = new ArrayList<>();
    }

    public RVMessageAdapter(List<ChatMessage> listMessage, FragmentManager fragmentManager, Context context) {
        this.mItems = listMessage;
        this.fragmentManager = fragmentManager;
        this.context = context;
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
        //holder.txtUserName.setText(item.username);
        //Picasso.with(GLOBAL.CurentContext).load(item.avatar).error(R.drawable.icon_profile).into(holder.imgAvatar);
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgAvatar;
        public TextView txtUserName;
        public TextView txtMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView)itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView)itemView.findViewById(R.id.txtUserName);
            txtMessage = (TextView)itemView.findViewById(R.id.txtMessage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String fromUserID = mItems.get(position).getSenderID();
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("fromUserID", fromUserID);
                    context.startActivity(intent);
                }
            });
        }
    }


}
