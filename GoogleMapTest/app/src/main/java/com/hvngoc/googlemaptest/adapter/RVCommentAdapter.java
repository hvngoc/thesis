package com.hvngoc.googlemaptest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.model.Comment;


import java.util.ArrayList;
import java.util.List;

public class RVCommentAdapter extends RecyclerView.Adapter<RVCommentAdapter.ViewHolder>{
    List<Comment> mItems;

    public RVCommentAdapter() {
        super();
        mItems = new ArrayList<Comment>();
        /*
        mItems.add(new Comment("Ngoc V. Hoang", R.drawable.default_icon, "Ok!! I got it"));
        mItems.add(new Comment("Ngoc V. Hoang", R.drawable.default_icon, "It's not good as you think"));
        mItems.add(new Comment("Ngoc V. Hoang", R.drawable.default_icon, "Wow !! Beautiful"));
        mItems.add(new Comment("Ngoc V. Hoang", R.drawable.default_icon, "Let's go one day"));
        mItems.add(new Comment("Ngoc V. Hoang", R.drawable.default_icon, "I'm sorry about that"));
        mItems.add(new Comment("Ngoc V. Hoang", R.drawable.default_icon, "What are you doing there ??"));
        */
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.comment_card_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        /*
        Comment item = mItems.get(i);
        viewHolder.txtCommentSring.setText(item.getCommentString());
        viewHolder.txtUserName.setText(item.getUserName());
        viewHolder.imgAvatar.setImageResource(item.getIdAvatar());
        */
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgAvatar;
        public TextView txtUserName;
        public TextView txtCommentSring;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (ImageView)itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView)itemView.findViewById(R.id.txtUserName);
            txtCommentSring = (TextView)itemView.findViewById(R.id.txtCommentString);
        }
    }
}

