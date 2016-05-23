package com.hvngoc.googlemaptest.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoang Van Ngoc on 22/04/2016.
 */
public class RVPickImageAdapter extends RecyclerView.Adapter<RVPickImageAdapter.ViewHolder>{
    ArrayList<String> mItems;
    ImageLoader imageLoader;

    public RVPickImageAdapter(ArrayList<String> mItems) {
        super();
        this.mItems = mItems;
        imageLoader = ImageLoader.getInstance();
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
        if(item != null && !item.equals(""))  {
            final File image = DiskCacheUtils.findInCache(item, imageLoader.getDiskCache());
            if (image!= null && image.exists()) {
                Picasso.with(GLOBAL.CurrentContext).load(image).fit().centerCrop().into(viewHolder.imgPickImage);
            }
            else {
                imageLoader.displayImage("file://" + item, viewHolder.imgPickImage);
            }
        }
        else {
            viewHolder.imgPickImage.setImageResource(R.drawable.icon_no_image);
        }
    }

    public List<Bitmap> getListBitmaps() {
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();
        for (int i = 0; i < mItems.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(mItems.get(i));
            if(bitmap.getWidth() > 480 || bitmap.getHeight() > 300) {
                bitmap = Bitmap.createScaledBitmap(bitmap, 480, 300, true);
            }
            bitmaps.add(bitmap);
        }
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
                    notifyDataSetChanged();
                }
            });
        }

    }
}
