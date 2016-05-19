package com.hvngoc.googlemaptest.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NewsItemViewHolder> {

    List<Post> posts;

    public RVAdapter(){
        posts = new ArrayList<>();
    }

    public void addListPost(ArrayList<Post> list){
        posts.addAll(list);
        notifyDataSetChanged();
    }

    public  void addToFirst(Post item){
        posts.add(0, item);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public NewsItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_post, viewGroup, false);
        NewsItemViewHolder pvh = new NewsItemViewHolder(v);
        Log.i("Position post : ", "" + i);
        return pvh;
    }

    @Override
    public void onBindViewHolder(NewsItemViewHolder newsViewHolder, int i) {
        Post post = posts.get(i);
        newsViewHolder.username.setText(post.userName);
        Picasso.with(GLOBAL.CurrentContext).load(post.getUserAvatar()).error(R.drawable.icon_profile).into(newsViewHolder.userAvatar);
        newsViewHolder.news_title.setText(post.getContent());
        newsViewHolder.txtFeeling.setText("feeling " + post.feeling + " on");
        newsViewHolder.txtCommentDay.setText(post.getPostDate());
        Picasso.with(GLOBAL.CurrentContext)
                .load(post.getFirstImageUrl())
                .error(R.drawable.icon_no_image)         // optional
                .into(newsViewHolder.placephoto);
        newsViewHolder.txtAddressLocation.setText(new GeolocatorAddressHelper(GLOBAL.CurrentContext, post.Latitude, post.Longitude).GetAddress());
        newsViewHolder.txtNumLike.setText(""+post.numLike);
        newsViewHolder.txtNumShared.setText(""+post.numShare);
        newsViewHolder.txtNumComment.setText(""+post.numComment);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView cv;
        public ImageView userAvatar;
        public TextView username;
        public TextView txtFeeling;
        public TextView txtCommentDay;
        public ImageView placephoto;
        public TextView txtAddressLocation;
        public TextView news_title;

        public Button btnLike;
        public TextView txtNumLike;
        public Button btnShare;
        public TextView txtNumShared;
        public Button btnComment;
        public TextView txtNumComment;

        public NewsItemViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            cv = (CardView)itemView.findViewById(R.id.card_item);
            userAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            username = (TextView) itemView.findViewById(R.id.username);
            txtFeeling = (TextView) itemView.findViewById(R.id.txtFeeling);
            txtCommentDay = (TextView) itemView.findViewById(R.id.txtCommentDay);
            placephoto = (ImageView) itemView.findViewById(R.id.place_photo);
            txtAddressLocation = (TextView) itemView.findViewById(R.id.txtAddressLocation);
            news_title = (TextView) itemView.findViewById(R.id.news_title);

            btnLike = (Button) itemView.findViewById(R.id.btnLike);
            txtNumLike = (TextView) itemView.findViewById(R.id.txtNumLike);
            btnShare = (Button) itemView.findViewById(R.id.btnShare);
            txtNumShared = (TextView) itemView.findViewById(R.id.txtNumShared);
            btnComment = (Button) itemView.findViewById(R.id.btnComment);
            txtNumComment = (TextView) itemView.findViewById(R.id.txtNumComment);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Post currentPost = posts.get(position);
            Intent intent = new Intent("android.intent.action.NEWS_DETAIL");
            intent.putExtra("currentPost", currentPost);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            GLOBAL.CurrentContext.startActivity(intent);
        }
    }
}

