package com.hvngoc.googlemaptest.adapter;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.hvngoc.googlemaptest.R;

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

    public RVShowPictureAdapter(ArrayList<String> mItems, boolean isMultipleClick) {
        super();
        this.mItems = mItems;
        this.isMultipleClick = isMultipleClick;
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
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String item = mItems.get(i);
        viewHolder.imgPickPicture.setImageBitmap(BitmapFactory.decodeFile(item));
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
