package com.hvngoc.googlemaptest.imagechooser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

	private Context mContext;
	private LayoutInflater infalter;
	private ArrayList<CustomGallery> data = new ArrayList<CustomGallery>();
	ImageLoader imageLoader;

	private boolean isActionMultiplePick;

	public CustomGalleryAdapter(Context c, ImageLoader imageLoader) {
		infalter = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = c;
		this.imageLoader = imageLoader;
	}

	public List<Bitmap> getListBitmaps() {
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		for (int i = 0; i < data.size(); i++) {
			Bitmap bitmap = BitmapFactory.decodeFile(data.get(i).sdcardPath);
			if(bitmap.getWidth() > 480 || bitmap.getHeight() > 300) {
				bitmap = Bitmap.createScaledBitmap(bitmap, 480, 300, true);
			}
			bitmaps.add(bitmap);
		}
		return bitmaps;
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

	public void selectAll(boolean selection) {
		for (int i = 0; i < data.size(); i++) {
			data.get(i).isSeleted = selection;
		}

		notifyDataSetChanged();
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

	public ArrayList<String> getImageStringSelected() {
		ArrayList<String> dataList = new ArrayList<String>();

		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).isSeleted) {
				dataList.add(data.get(i).sdcardPath);
			}
		}

		return dataList;
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

	public void changeSelection(View v, int position) {

		if (data.get(position).isSeleted) {
			data.get(position).isSeleted = false;
		} else {
			data.get(position).isSeleted = true;
		}

		((ViewHolder) v.getTag()).imgCheck.setSelected(data
				.get(position).isSeleted);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {

			convertView = infalter.inflate(R.layout.gallery_item, null);
			holder = new ViewHolder();
			holder.imgQueue = (ImageView) convertView
					.findViewById(R.id.imgQueue);

			holder.imgCheck = (CheckBox) convertView
					.findViewById(R.id.imgCheck);

			if (isActionMultiplePick) {
				holder.imgCheck.setVisibility(View.VISIBLE);
			} else {
				holder.imgCheck.setVisibility(View.GONE);
			}

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imgQueue.setTag(position);

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

		return convertView;
	}

	public class ViewHolder {
		ImageView imgQueue;
		CheckBox imgCheck;
	}

	public void clearCache() {
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
	}

	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}
}
