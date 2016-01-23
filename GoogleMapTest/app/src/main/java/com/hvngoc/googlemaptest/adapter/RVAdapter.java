package com.hvngoc.googlemaptest.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.Global;
import com.hvngoc.googlemaptest.model.NewsItem;

import java.util.List;


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.NewsItemViewHolder> {

    public static class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView username;
        TextView title;
        ImageView placephoto;

        public NewsItemViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            cv = (CardView)itemView.findViewById(R.id.card_item);
            username = (TextView)itemView.findViewById(R.id.username);
            title = (TextView)itemView.findViewById(R.id.news_title);
            placephoto = (ImageView)itemView.findViewById(R.id.place_photo);
        }

        @Override
        public void onClick(View v) {
            //int position = getPosition();
            Intent intent = new Intent("android.intent.action.NEWS_DETAIL");
            intent.putExtra("title", title.getText());
            intent.putExtra("username", username.getText());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Global.CurentContext.startActivity(intent);
        }
    }

    List<NewsItem> news;

    public RVAdapter(List<NewsItem> persons){

        this.news = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public NewsItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_carditem, viewGroup, false);
        NewsItemViewHolder pvh = new NewsItemViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(NewsItemViewHolder newsViewHolder, int i) {
        newsViewHolder.username.setText(news.get(i).getUserName());
        newsViewHolder.title.setText(news.get(i).getTitle());
        newsViewHolder.placephoto.setImageResource(news.get(i).getPhoto());
    }


    @Override
    public int getItemCount() {
        return news.size();
    }
}

