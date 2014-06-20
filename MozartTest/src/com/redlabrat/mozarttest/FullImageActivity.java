package com.redlabrat.mozarttest;

import uk.co.senab.photoview.PhotoViewAttacher;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
		//ImageAdapter imageAdapter = new ImageAdapter(this, CollectionActivity.collections.get(num));
		String URL = CollectionActivity.collections.get(num).getImages().get(position).getURL();
		
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
		
		//Scaled the image
		double imWidth = image.getWidth();
		double imHeight = image.getHeight();
		double displayWidth = CollectionActivity.w;
		double displayHeight = CollectionActivity.h;
		
		double width = displayWidth/imWidth;
		double height = displayHeight/imHeight;
		
		double scale = Math.min(width, height);//max
		/*int pixW = (int)(imWidth*scale);
		int pixH = (int)(imHeight*scale);
		
		Log.i("BITMAP", imWidth+" x "+imHeight);//Height - высота
		Log.i("BITMAP display", displayWidth+" x "+displayHeight);//Height - высота
		Log.i("BITMAP", "Ширина = "+width+" Высота="+height);
		Log.i("BITMAP PIX", pixW+" x "+pixH);*/
		/*
		imageView.setImageBitmap(image);
		mAttacher = new PhotoViewAttacher(imageView);
		float scaleFactor = (float)scale;
		mAttacher.onScale(scaleFactor, 0, 0);
		*/
		//imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setImageBitmap(image);
		mAttacher = new PhotoViewAttacher(imageView);
		mAttacher.setScaleType(ScaleType.CENTER_CROP);
		//mAttacher.onScale((float)scale, 0, 0);
		
		//Set the description frame
		FrameLayout descriptionFrame = (FrameLayout)findViewById(R.id.descriptionFrame);
		double minWidth = CollectionActivity.w/2;
		descriptionFrame.setMinimumWidth((int)minWidth);
		
		TextView textViewDescript = (TextView)findViewById(R.id.descriptionText);
		String description = "";
		textViewDescript.setWidth((int)CollectionActivity.w/2);
		Image img = CollectionActivity.collections.get(num).getImages().get(position);
		for (Product p : img.getProducts())
		{
			description += p.getNumber() + " :\n";
			description += p.getDescription() + "\n";
		}
		textViewDescript.setText(description);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.full_image, menu);
		return true;
	}
}
