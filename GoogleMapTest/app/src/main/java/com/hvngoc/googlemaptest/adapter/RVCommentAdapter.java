package com.hvngoc.googlemaptest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.model.Comment;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class RVCommentAdapter extends RecyclerView.Adapter<RVCommentAdapter.ViewHolder>{
    List<Comment> mItems;

    public RVCommentAdapter(){
        mItems = new ArrayList<>();
    }

    public int addComment(Comment comment){
        mItems.add(comment);
        notifyDataSetChanged();
        return mItems.size() - 1;
    }
    public int addListComment(ArrayList<Comment> listComment){
        mItems.addAll(listComment);
        notifyDataSetChanged();
        return mItems.size() - 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_comment, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Comment item = mItems.get(i);
        viewHolder.txtCommentSring.setText(item.getContent());
        viewHolder.txtCommentDay.setText(item.getCommentDate());
        viewHolder.txtUserName.setText(item.getUserName());
        Picasso.with(GLOBAL.CurentContext).load(item.getUserAvatar()).error(R.drawable.icon_profile).into(viewHolder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgAvatar;
        public TextView txtUserName;
        public TextView txtCommentSring;
        public TextView txtCommentDay;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView)itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView)itemView.findViewById(R.id.txtUserName);
            txtCommentSring = (TextView)itemView.findViewById(R.id.txtCommentString);
            txtCommentDay = (TextView) itemView.findViewById(R.id.txtCommentDay);
        }
    }
}

