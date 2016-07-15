package com.hvngoc.googlemaptest.adapter;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.GeolocatorAddressHelper;
import com.hvngoc.googlemaptest.model.Post;
import com.squareup.picasso.Picasso;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Post> posts;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public RVAdapter(){
        posts = new ArrayList<>();
    }

    public void addToFirst(Post post){
        posts.add(0, post);
        notifyDataSetChanged();
    }

    public void clearListPost() {
        posts.clear();
    }

    public void addListPost(ArrayList<Post> list){
        //posts.clear();
        Log.i("added List", "" + list.size());
        posts.addAll(list);
        notifyDataSetChanged();
        Log.i("New List", "" + posts.size());
    }

    public ArrayList<Post> getListPost(){
        return posts;
    }


    @Override
    public int getItemViewType(int position) {
        return posts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item_post, viewGroup, false);
            return new NewsItemViewHolder(v);
        }
        else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_item, viewGroup, false);
            return new LoadingViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if(holder instanceof NewsItemViewHolder)
            setNewsItemViewHolder((NewsItemViewHolder)holder, i);
        else if(holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    private void setNewsItemViewHolder(NewsItemViewHolder newsViewHolder, int pos) {
        Post post = posts.get(pos);
        newsViewHolder.username.setText(post.userName);
        Picasso.with(GLOBAL.CurrentContext).load(post.getUserAvatar()).error(R.drawable.icon_profile).into(newsViewHolder.userAvatar);
        newsViewHolder.news_title.setText(post.getContentSmaller());
        newsViewHolder.txtFeeling.setText(GLOBAL.CurrentContext.getString(R.string.feeling) + " " + post.getFeeling());
        newsViewHolder.txtCommentDay.setText(post.getPostDate());
        String firstImage = post.getFirstImageUrl();
        if(firstImage != "") {
            newsViewHolder.firstImage.setVisibility(View.VISIBLE);
            Picasso.with(GLOBAL.CurrentContext)
                    .load(post.getFirstImageUrl())
                    .error(R.drawable.icon_no_image)         // optional
                    .into(newsViewHolder.placephoto);
        }
        else {
            newsViewHolder.firstImage.setVisibility(View.GONE);
        }
        newsViewHolder.txtAddressLocation.setText(new GeolocatorAddressHelper(GLOBAL.CurrentContext, post.Latitude, post.Longitude).GetAddress());
        newsViewHolder.txtNumLike.setText(""+post.numLike);
        newsViewHolder.txtNumShared.setText(""+post.numShare);
        newsViewHolder.txtNumComment.setText(""+post.numComment);
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView cv;
        public CircleImageView userAvatar;
        public TextView username;
        public TextView txtFeeling;
        public TextView txtCommentDay;
        public ImageView placephoto;
        public TextView txtAddressLocation;
        public TextView news_title;
        public HashTagHelper hashTagHelper;

        public Button btnLike;
        public TextView txtNumLike;
        public Button btnShare;
        public TextView txtNumShared;
        public Button btnComment;
        public TextView txtNumComment;
        public RelativeLayout firstImage;

        public NewsItemViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            cv = (CardView)itemView.findViewById(R.id.card_item);
            userAvatar = (CircleImageView) itemView.findViewById(R.id.avatar);
            username = (TextView) itemView.findViewById(R.id.username);
            txtFeeling = (TextView) itemView.findViewById(R.id.txtFeeling);
            txtCommentDay = (TextView) itemView.findViewById(R.id.txtCommentDay);
            placephoto = (ImageView) itemView.findViewById(R.id.place_photo);
            txtAddressLocation = (TextView) itemView.findViewById(R.id.txtAddressLocation);
            news_title = (TextView) itemView.findViewById(R.id.news_title);
            hashTagHelper = HashTagHelper.Creator.create(ContextCompat.getColor(GLOBAL.CurrentContext, R.color.blue), null);
            hashTagHelper.handle(news_title);
            btnLike = (Button) itemView.findViewById(R.id.btnLike);
            txtNumLike = (TextView) itemView.findViewById(R.id.txtNumLike);
            btnShare = (Button) itemView.findViewById(R.id.btnShare);
            txtNumShared = (TextView) itemView.findViewById(R.id.txtNumShared);
            btnComment = (Button) itemView.findViewById(R.id.btnComment);
            txtNumComment = (TextView) itemView.findViewById(R.id.txtNumComment);
            firstImage = (RelativeLayout) itemView.findViewById(R.id.firstImage);
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

