package com.hvngoc.googlemaptest.imagechooser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.hvngoc.googlemaptest.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class CustomGalleryAdapter extends BaseAdapter {

	private LayoutInflater infalter;
	private ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();
	ImageLoader imageLoader;

	private boolean isActionMultiplePick;

	public CustomGalleryAdapter(Context c, ImageLoader imageLoader) {
		infalter = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageLoader = imageLoader;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public CustomGallery getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setMultiplePick(boolean isMultiplePick) {
		this.isActionMultiplePick = isMultiplePick;
	}


	public ArrayList<CustomGallery> getSelected() {
		ArrayList<CustomGallery> dataList = new ArrayList<CustomGallery>();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isSeleted) {
                dataList.add(data.get(i));
			}
		}
		return dataList;
	}

	public void add(CustomGallery item) {
		this.data.add(0, item);
		notifyDataSetChanged();
	}


	public void addAll(ArrayList<CustomGallery> files) {
		try {
			this.data.clear();
			this.data.addAll(files);

		} catch (Exception e) {
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}

	int oldPosition = -1;
	ViewHolder oldHolder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {

			convertView = infalter.inflate(R.layout.gallery_item, null);
			holder = new ViewHolder();
			holder.imgQueue = (ImageView) convertView
					.findViewById(R.id.imgQueue);
			holder.imgCheck = (CheckBox) convertView
					.findViewById(R.id.imgCheck);

//			if (isActionMultiplePick) {
//				holder.imgCheck.setVisibility(View.VISIBLE);
//			} else {
//				holder.imgCheck.setVisibility(View.GONE);
//			}

			holder.imgCheck.setVisibility(View.VISIBLE);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgQueue.setTag(position);
		holder.imgQueue.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.imgCheck.setChecked(!holder.imgCheck.isChecked());
				data.get(position).isSeleted = !data.get(position).isSeleted;
				if (!isActionMultiplePick){
					if (oldPosition != -1){
						data.get(oldPosition).isSeleted = false;
						oldHolder.imgCheck.setChecked(false);
					}
					oldPosition = position;
					oldHolder = holder;
				}
			}
		});
		try {

			imageLoader.displayImage("file://" + data.get(position).sdcardPath,
					holder.imgQueue, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.imgQueue
									.setImageResource(R.drawable.icon_no_image);
							super.onLoadingStarted(imageUri, view);
						}
					});

			if (isActionMultiplePick) {

				holder.imgCheck
						.setSelected(data.get(position).isSeleted);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		convertView.setClickable(true);
		return convertView;
	}

	public class ViewHolder{
		ImageView imgQueue;
		CheckBox imgCheck;
	}
}
