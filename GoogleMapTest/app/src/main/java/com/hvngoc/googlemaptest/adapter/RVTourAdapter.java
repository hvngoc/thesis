package com.hvngoc.googlemaptest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.model.Tour;

import java.util.ArrayList;

/**
 * Created by Hoang Van Ngoc on 21/06/2016.
 */
public class RVTourAdapter extends RecyclerView.Adapter<RVTourAdapter.NewsItemViewHolder> {

    private ArrayList<Tour> listTour;

    public RVTourAdapter(ArrayList<Tour> listTour) {
        this.listTour = listTour;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public NewsItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_tour, viewGroup, false);
        return new NewsItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsItemViewHolder newsViewHolder, int i) {
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public NewsItemViewHolder(View itemView) {
            super(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
