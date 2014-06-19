package com.redlabrat.mozarttest;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class FullImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image);
		Intent i = getIntent();
		// Selected image id
		int position = i.getExtras().getInt("id");
		int num = CollectionActivity.collectionNumber;
		ImageAdapter imageAdapter = new ImageAdapter(this, CollectionActivity.collections.get(num));
		String URL = CollectionActivity.collections.get(num).getImages().get(position).getURL();
		
		FrameLayout descriptionFrame = (FrameLayout)findViewById(R.id.descriptionFrame);
		double minWidth = CollectionActivity.w/2;
		descriptionFrame.setMinimumWidth((int)minWidth);

		ImageView imageView = (ImageView) findViewById(R.id.full_image_view);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        .bitmapConfig(Bitmap.Config.ARGB_8888)
	        .build();
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		ImageLoader.getInstance().displayImage(URL, imageView, options);
		
		//Set the description frame
		TextView textViewDescript = (TextView)findViewById(R.id.descriptionText);
		String description = "";
		textViewDescript.setWidth((int)CollectionActivity.w/2);
		Image image = CollectionActivity.collections.get(num).getImages().get(position);
		for (Product p : image.getProducts())
		{
			description += p.getNumber() + " :\n";
			description += p.getDescription() + "\n";
		}
		textViewDescript.setText(description);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.full_image, menu);
		return true;
	}

}
