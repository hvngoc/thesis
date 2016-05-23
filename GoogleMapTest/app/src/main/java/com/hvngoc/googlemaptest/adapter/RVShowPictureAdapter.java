package com.hvngoc.googlemaptest.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.hvngoc.googlemaptest.R;
import com.hvngoc.googlemaptest.activity.GLOBAL;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Hoang Van Ngoc on 22/04/2016.
 */
public class RVShowPictureAdapter extends RecyclerView.Adapter<RVShowPictureAdapter.ViewHolder>{
    private ArrayList<String> mItems;
    private ArrayList<Boolean> mChecked;
    private boolean isMultipleClick;
    private int curPosition;
    private CheckBox preCheckbox;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public RVShowPictureAdapter(ArrayList<String> mItems, boolean isMultipleClick) {
        super();
        this.mItems = mItems;
        this.isMultipleClick = isMultipleClick;
        initImageLoader();
        if(this.isMultipleClick) {
            mChecked = new ArrayList<>();
            int size = mItems.size();
            for (int i = 0; i < size; ++i) {
                mChecked.add(false);
            }
        }
        else{
            curPosition = -1;
        }
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(GLOBAL.CurrentContext)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        imageLoader = ImageLoader.getInstance();
        if(imageLoader == null)
            Log.i("IMAGE", "NULLLLLLLLLLLLLLLLLLLL");
        ImageLoader.getInstance().init(config);
    }

    public void addItemString(String item){
        mItems.add(item);
        if (this.isMultipleClick)
            mChecked.add(false);
        notifyDataSetChanged();
    }

    public ArrayList<String> getmItemsChecked(){
        ArrayList<String> mDumy = new ArrayList<>();
        int size = mItems.size();
        for (int i = 0; i < size; ++i){
            if (mChecked.get(i))
                mDumy.add(mItems.get(i));
        }
        return mDumy;
    }

    public String getOnlyOnePicture(){
        if (curPosition == -1)
            return null;
        return mItems.get(curPosition);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_picture, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        String item = mItems.get(i);
        Log.i("URL", item);
        if(item != null && !item.equals(""))  {
            final File image = DiskCacheUtils.findInCache(item, imageLoader.getDiskCache());
            if (image!= null && image.exists()) {
                Picasso.with(GLOBAL.CurrentContext).load(image).fit().centerCrop().into(viewHolder.imgPickPicture);
            }
            else {
                imageLoader.displayImage("file://" + item, viewHolder.imgPickPicture);
            }
        }
        else {
            viewHolder.imgPickPicture.setImageResource(R.drawable.no_media);
        }


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgPickPicture;
        public CheckBox checkboxPickPicture;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPickPicture = (ImageView)itemView.findViewById(R.id.imgPickPicture);
            checkboxPickPicture = (CheckBox) itemView.findViewById(R.id.checkboxPickPicture);
            checkboxPickPicture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    if (isMultipleClick) {
                        mChecked.set(position, isChecked);
                    }
                    else{
                        if (isChecked){
                            if (curPosition != -1){
                                    preCheckbox.setChecked(false);
                            }
                            curPosition = position;
                            preCheckbox = checkboxPickPicture;
                        }
                        else {
                            curPosition = -1;
                        }
                    }
                }
            });
        }
    }
}
