package com.redlabrat.mozarttest;

import java.io.File;
import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import static com.redlabrat.mozarttest.Constants.*;

public class CollectionActivity extends Activity implements OnClickListener {
	public Context mContext;
	public LinearLayout lin = null;
	public LoadFromNet load = null;
	public static ArrayList<Collection> collections = new ArrayList<Collection>();
	public static int collectionNumber = 0;
/////////////////////////////
	public static int w;
	public static int h;
/////////////////////////////	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = getApplicationContext();
		File extChacheDir = null;
		File folderForPics = null;
		
		//Must exist an external storage and the directory to save files imagesFolderName
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			extChacheDir = mContext.getExternalCacheDir();
			folderForPics = new File(extChacheDir, imagesFolderName);
			// create folder for images if it not exist
			if (!folderForPics.exists()) {
				folderForPics.mkdir();
			}
		} else {
			String errorText = mContext.getResources().getString(R.string.error_media_mount);
			Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
			Log.e("ERROR", "Media not mounted!");
		}
		
		load = new LoadFromNet(getApplicationContext(), catalog, false);
		
		//get from URL the name of the image to save
		int startSubString = catalog.lastIndexOf("/");
		String fileName = catalog.substring(startSubString);
		File catalogFile = new File(extChacheDir, fileName);
		if (!catalogFile.exists())
		{
			load.start();//download the catalog from Net
			if(load.isAlive())
	        {
				try {
					load.join(); //wait till thread is over
				} catch (InterruptedException e) {
					Log.i("THREAD", "Was interrupted!!!");
					e.printStackTrace();
				}
	        }
		}
		else load.ReadXml();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		
		// Create global configuration and initialize ImageLoader with this configuration
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.search_icon) // resource or drawable
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        //////
	        .bitmapConfig(Bitmap.Config.RGB_565)
	        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)//.imageScaleType(ImageScaleType.EXACTLY)
	        ///////
	        .build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.defaultDisplayImageOptions(defaultOptions)   
			.threadPoolSize(5)
			.build();
        ImageLoader.getInstance().init(config);
		//////////////////////////
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		w = display.getWidth();
		h = display.getHeight();
		/////////////////////////
		
		int Count = collections.size();
		Log.i("OnCreate", "Done Parsing! Count of collection is " + String.valueOf(Count));
		lin = (LinearLayout)findViewById(R.id.layoutCollection);
	    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    int i = 0;
	    for(i = 0; i < Count; i++){
	    	if (collections.get(i).getCountOfImages() != 0)
	    	{
	    		Button button = new Button(this);
	            button.setText(collections.get(i).getName());
	            button.setLayoutParams(layoutParams);
	            button.setId(i);
	            button.setOnClickListener(this);
	            lin.addView(button);
	            //adding the folder for this collection
	            File folderForCollection = new File(folderForPics, collections.get(i).getName());
				if (!folderForCollection.exists()) {
					folderForCollection.mkdir();
				}
	    	}
	    }
	    Button button = new Button(this);
	    button.setText("Обновить");
        button.setLayoutParams(layoutParams);
        button.setId(i);
        button.setOnClickListener(this);
        lin.addView(button);
	    //lin.setLayoutParams(layoutParams); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection, menu);
		return true;
	}
	
	/**
	 * Process the button click event 
	 * @param v view of the button which was clicked
	 */
	public void onClick(View v) {
		int id = v.getId();
		if (id == collections.size()) { //button Update click
			load = new LoadFromNet(getApplicationContext(), catalog, true);
			load.start();
		}
		else {
			collectionNumber = id;
			Intent intent = new Intent(getApplicationContext(), GridActivity.class);
			startActivity(intent);
		}
	}
}

