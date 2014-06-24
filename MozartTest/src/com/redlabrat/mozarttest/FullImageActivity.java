package com.redlabrat.mozarttest;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class FullImageActivity extends NavigationActivity {
    public ArrayList<String> names;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent i = getIntent();
		// Selected image id
		int position = i.getExtras().getInt("id");
		String name = i.getExtras().getString("name");
		names = i.getStringArrayListExtra("list");
		int num = GridActivity.collectionNumber;
		
		super.ACTIVITY_LAYOUT = R.layout.activity_full_image;
		super.col = names;
		super.onCreate(savedInstanceState);
		setTitle(name);
		
		//ImageAdapter imageAdapter = new ImageAdapter(this, CollectionActivity.collections.get(num));
		String URL = GridActivity.collections.get(num).getImages().get(position).getURL();
		ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        .bitmapConfig(Bitmap.Config.ARGB_8888)
	        .build();
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		//ImageLoader.getInstance().displayImage(URL, imageView, options);
		Bitmap image = ImageLoader.getInstance().loadImageSync(URL, options);
		PhotoViewAttacher mAttacher = null;
		
		imageView.setImageBitmap(image);
		mAttacher = new PhotoViewAttacher(imageView);
		mAttacher.setScaleType(ScaleType.CENTER_CROP);
		//mAttacher.onScale((float)scale, 0, 0);
		
		//Set the description frame
		double minWidth = Math.min(GridActivity.w, GridActivity.h)/2;
		FrameLayout descriptionFrame = (FrameLayout)findViewById(R.id.descriptionFrame);
		descriptionFrame.setMinimumWidth((int)minWidth);
		
		TextView textViewDescript = (TextView)findViewById(R.id.descriptionText);
		String description = "";
		textViewDescript.setWidth((int)minWidth);
		Image img = GridActivity.collections.get(num).getImages().get(position);
		for (Product p : img.getProducts())
		{
			description += p.getNumber() + " :\n";
			description += p.getDescription() + "\n";
		}
		textViewDescript.setText(description);
		
		super.mDrawerList.setItemChecked(num, true);
	}

	@Override
    public void selectItem(int position) {
    	super.selectItem(position);
    	GridActivity.collectionNumber = position;
    	Intent intent = new Intent(getApplicationContext(), GridActivity.class);
		startActivity(intent);
    }
	
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		return super.onMenuItemSelected(featureId, item);
	}
}
