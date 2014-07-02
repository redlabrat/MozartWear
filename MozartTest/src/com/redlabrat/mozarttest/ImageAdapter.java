package com.redlabrat.mozarttest;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Class handles the process of showing collection's images in grid view
 * @author Kate Zenevich
 * @version 1.0
 */
public class ImageAdapter extends BaseAdapter {
	/*** @serial context of the application*/
	private Context mContext;
	/*** @serial current collection*/
	private Collection collection;
	
	public ImageAdapter(Context c, Collection coll) {
		mContext = c;
		collection = coll;
	}

	/** 
	 * Get the count of images in images list
	 * @return count of images in collection
	 */
	public int getCount() {
		return collection.getCountOfImages();
	}

	/** 
	 * Get the object by position
	 * @return object
	 */
	public Object getItem(int position) {
		return collection.getImages().get(position).getURL();
	}

	/** 
	 * Get the item ID by position
	 * @return id
	 */
	public long getItemId(int position) {
		return position;
	}

	/** 
	 * Create a new ImageView for each item
	 * @param position position of the image
	 * @param convertView particular modified view 
	 * @param parent group of parents views
	 * @return imageView with image
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// set the dimension of the imageView based on the device dimensions
		// and desired column count
		float width = 160;//(float) (NavigationActivity.w/NavigationActivity.columnCount);
		float height = 240;//(float) (width * 1.5);
		
		ImageView imageView;
		String URL = collection.getImages().get(position).getURL();
		imageView = new ImageView(mContext);
		imageView.setLayoutParams(new GridView.LayoutParams((int)width, (int)height));//(160,240));
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//imageView.setPadding(2,2,2,2);//8
		imageView.setId(position);
		ImageLoader.getInstance().displayImage(URL, imageView);
		return imageView;
	}
}
