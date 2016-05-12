package com.hvngoc.googlemaptest.adapter;

import android.graphics.Bitmap;
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
    ArrayList<Bitmap> bitmaps;

    public RVPickImageAdapter(ArrayList<String> mItems) {
        super();
        this.mItems = mItems;
        bitmaps = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_image, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String item = mItems.get(i);
        Bitmap bitmap = BitmapFactory.decodeFile(item);
        viewHolder.imgPickImage.setImageBitmap(bitmap);
        bitmaps.add(bitmap);
    }

    public ArrayList<Bitmap> getListBitmaps() {
        return bitmaps;
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
                    bitmaps.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

    }
}
