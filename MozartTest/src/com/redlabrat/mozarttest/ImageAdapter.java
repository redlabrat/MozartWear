package com.redlabrat.mozarttest;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private Collection collection;
	public ImageAdapter(Context c, Collection coll) {
		mContext = c;
		collection = coll;
	}

	public int getCount() {
		return collection.getCountOfImages();
	}

	public Object getItem(int position) {
		return collection.getImages().get(position).getURL();
	}

	public long getItemId(int position) {
		return position;
	}

	// create a new ImageView for each item
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		String URL = collection.getImages().get(position).getURL();
		imageView = new ImageView(mContext);
		imageView.setLayoutParams(new GridView.LayoutParams(160,240));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//imageView.setPadding(2,2,2,2);//8
		imageView.setId(position);
		ImageLoader.getInstance().displayImage(URL, imageView);
		//imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//CENTER_CROP*/
		return imageView;
	}
}
