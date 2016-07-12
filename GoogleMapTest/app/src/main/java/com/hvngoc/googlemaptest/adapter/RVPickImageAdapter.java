package com.hvngoc.googlemaptest.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.hvngoc.googlemaptest.helper.DelegationHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoang Van Ngoc on 22/04/2016.
 */
public class RVPickImageAdapter extends RecyclerView.Adapter<RVPickImageAdapter.ViewHolder>{
    public ArrayList<String> mItems;
    public List<Bitmap> bitmaps = null;
    ImageLoader imageLoader;

    public RVPickImageAdapter(ArrayList<String> mItems) {
        super();
        this.mItems = mItems;
        imageLoader = ImageLoader.getInstance();
    }

    public void add(String item){
        mItems.add(0, item);
        notifyDataSetChanged();
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
            imageLoader.displayImage("file://" + item, viewHolder.imgPickImage);
        }
        else {
            viewHolder.imgPickImage.setImageResource(R.drawable.icon_no_image);
        }
    }

    public List<Bitmap> getListBitmaps() {
        if(bitmaps == null) {
            bitmaps = new ArrayList<Bitmap>();
            for (int i = 0; i < mItems.size(); i++) {
                Bitmap bitmap = BitmapFactory.decodeFile(mItems.get(i));
                int width = bitmap.getWidth();
                if (width > 480)
                    bitmap = scaleImageWithRatio(bitmap, width / 480);
                int height = bitmap.getHeight();
                if (height > 300)
                    bitmap = scaleImageWithRatio(bitmap, height / 300);
                bitmaps.add(bitmap);
            }
        }
        return bitmaps;
    }

    private Bitmap scaleImageWithRatio(Bitmap bitmap, float ratio) {
        int realWidth = Math.round(bitmap.getWidth() / ratio);
        int realHeight = Math.round(bitmap.getHeight() / ratio);
        bitmap = Bitmap.createScaledBitmap(bitmap, realWidth, realHeight, true);
        return bitmap;
    }

    public interface OnClickImage{
        void doSomething(String uri);
    }
    private OnClickImage onClickImage;
    public void setOnClickImage(OnClickImage onClickImage){
        this.onClickImage = onClickImage;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgPickImage;
        public Button btnPickClose;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgPickImage = (ImageView)itemView.findViewById(R.id.imgPickImage);
            imgPickImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onClickImage.doSomething(mItems.get(position));
                }
            });
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
