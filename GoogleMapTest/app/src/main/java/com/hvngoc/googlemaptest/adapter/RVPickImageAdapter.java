package com.hvngoc.googlemaptest.adapter;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.hvngoc.googlemaptest.R;

import java.util.ArrayList;

/**
 * Created by Hoang Van Ngoc on 22/04/2016.
 */
public class RVPickImageAdapter extends RecyclerView.Adapter<RVPickImageAdapter.ViewHolder>{
    ArrayList<String> mItems;

    public RVPickImageAdapter(ArrayList<String> mItems) {
        super();
        this.mItems = mItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_image, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String item = mItems.get(i);
        viewHolder.imgPickImage.setImageBitmap(BitmapFactory.decodeFile(item));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgPickImage;
        public Button btnPickClose;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPickImage = (ImageView)itemView.findViewById(R.id.imgPickImage);
            btnPickClose = (Button) itemView.findViewById(R.id.btnPickClose);
            btnPickClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    mItems.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

    }
}
