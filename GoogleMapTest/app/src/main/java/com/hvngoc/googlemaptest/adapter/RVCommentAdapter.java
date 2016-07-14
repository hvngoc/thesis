package com.hvngoc.googlemaptest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.model.Comment;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Comment> mItems;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public RVCommentAdapter(){
        mItems = new ArrayList<>();
    }

    public int addComment(Comment comment){
        mItems.add(comment);
        notifyDataSetChanged();
        return mItems.size() - 1;
    }
    public int addListComment(ArrayList<Comment> listComment){
        int pos = 0;
        if(mItems.size() == 0 && listComment.size() != 0)
            pos = listComment.size() - 1;
        else
            pos = mItems.size();
        mItems.addAll(0, listComment);
        notifyDataSetChanged();
        return pos;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_comment, viewGroup, false);
            return new ViewHolder(v);
        }
        else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_item, viewGroup, false);
            return new LoadingViewHolder(v);
        }
        return null;
    }


    private void setCommentViewHolder(ViewHolder viewHolder, int pos) {
        Comment item = mItems.get(pos);
        viewHolder.txtCommentSring.setText(item.getContent());
        viewHolder.txtCommentDay.setText(item.getCommentDate());
        viewHolder.txtUserName.setText(item.getUserName());
        Picasso.with(GLOBAL.CurrentContext).load(item.getUserAvatar()).error(R.drawable.icon_profile).into(viewHolder.imgAvatar);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if(holder instanceof ViewHolder)
            setCommentViewHolder((ViewHolder) holder, i);
        else if(holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imgAvatar;
        public TextView txtUserName;
        public TextView txtCommentSring;
        public TextView txtCommentDay;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (CircleImageView)itemView.findViewById(R.id.imgAvatar);
            txtUserName = (TextView)itemView.findViewById(R.id.txtUserName);
            txtCommentSring = (TextView)itemView.findViewById(R.id.txtCommentString);
            txtCommentDay = (TextView) itemView.findViewById(R.id.txtCommentDay);
        }
    }
}

