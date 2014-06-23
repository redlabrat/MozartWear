package com.redlabrat.mozarttest;

import uk.co.senab.photoview.PhotoViewAttacher;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class FullImageActivity extends Activity {
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image);
		Intent i = getIntent();
		// Selected image id
		int position = i.getExtras().getInt("id");
		int num = GridActivity.collectionNumber;
		//ImageAdapter imageAdapter = new ImageAdapter(this, CollectionActivity.collections.get(num));
		String URL = GridActivity.collections.get(num).getImages().get(position).getURL();
				//CollectionActivity.collections.get(num).getImages().get(position).getURL();
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
		/*double imWidth = image.getWidth();
		double imHeight = image.getHeight();
		double displayWidth = CollectionActivity.w;
		double displayHeight = CollectionActivity.h;
		
		double width = displayWidth/imWidth;
		double height = displayHeight/imHeight;
		
		double scale = Math.min(width, height);//max
		*/
		imageView.setImageBitmap(image);
		mAttacher = new PhotoViewAttacher(imageView);
		mAttacher.setScaleType(ScaleType.CENTER_CROP);
		//mAttacher.onScale((float)scale, 0, 0);
		
		//Set the description frame
		FrameLayout descriptionFrame = (FrameLayout)findViewById(R.id.descriptionFrame);
		double minWidth = Math.min(GridActivity.w, GridActivity.h)/2;
		
		descriptionFrame.setMinimumWidth((int)minWidth);
		
		TextView textViewDescript = (TextView)findViewById(R.id.descriptionText);
		String description = "";
		textViewDescript.setWidth((int)minWidth);
		Image img = GridActivity.collections.get(num).getImages().get(position);
				//CollectionActivity.collections.get(num).getImages().get(position);
		for (Product p : img.getProducts())
		{
			description += p.getNumber() + " :\n";
			description += p.getDescription() + "\n";
		}
		textViewDescript.setText(description);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set up the drawer's list view with items and click listener
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, NavigationList.list);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        setTitle(img.getName().substring(1));
	}

	 /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectItem(arg2);
		}
    }

    private void selectItem(int position) {
    	mDrawerList.setItemChecked(position, true);
    	mDrawerLayout.closeDrawer(mDrawerList);
    	GridActivity.collectionNumber = position;
    	
    	Intent intent = new Intent(getApplicationContext(), GridActivity.class);
		startActivity(intent);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.full_image, menu);
		return true;
	}
}
