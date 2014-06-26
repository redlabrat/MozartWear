package com.redlabrat.mozarttest;

import static com.redlabrat.mozarttest.Constants.catalog;

import java.io.File;
import java.util.ArrayList;
import com.redlabrat.mozarttest.R;
//import android.R;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

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
	public int Count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = getApplicationContext();
		File extChacheDir = mContext.getExternalCacheDir();
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			Toast.makeText(mContext, "Media not mounted!", Toast.LENGTH_SHORT).show();
		}
		load = new LoadFromNet(getApplicationContext(), false);
		
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
			.showImageOnLoading(R.drawable.search_icon)
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        .bitmapConfig(Bitmap.Config.RGB_565)
	        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)//EXACTLY)
	        .build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.defaultDisplayImageOptions(defaultOptions)   
			.threadPoolSize(5)
			.build();
        ImageLoader.getInstance().init(config);
		//////////////////////////
		//Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		//w = display.getWidth();
		//h = display.getHeight();
		//w = getWindowManager().getDefaultDisplay().getWidth();
		//h = getWindowManager().getDefaultDisplay().getHeight();
		//Log.i("Display", "Size : "+w+"x"+h);
		/////////////////////////
		getScreenOrientation();
		getRotateOrientation();
		addViews();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (!isNetworkAvailable()) {
			Toast.makeText(this, "Нет интернет соединения!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (item.getItemId() == R.id.action_settings) {
			Count = collections.size();
			collections.clear();
			if (collections.size() != 0) {
				Log.i("onClick", "Cannot clear the collection set!");
				return false;
			}
			load = new LoadFromNet(getApplicationContext(), true);
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
			Toast.makeText(this, "Обновлено!", Toast.LENGTH_SHORT).show();
			addViews();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	/**
	 * Process the button click event 
	 * @param v view of the button which was clicked
	 */
	public void onClick(View v) {
		int id = v.getId();
		collectionNumber = id;
		Intent intent = new Intent(getApplicationContext(), GridActivity.class);
		startActivity(intent);
	}
	
	public void addViews() {
		int i = 0;
		Button button;
		lin = (LinearLayout)findViewById(R.id.layoutCollection);
		lin.removeAllViews();
		ColorStateList colors = null;
		try {
            XmlResourceParser parser = getResources().getXml(R.color.your_color);
            colors = ColorStateList.createFromXml(getResources(), parser);
        } catch (Exception e) {
        	Log.i("ERROR", "Error while parsing file");
			e.printStackTrace();
        }
		
		TextView text = (TextView) findViewById(R.id.textView1);
		text.setTextSize((float)0.03*h);
		text.setTextColor(colors);
		
		int wrap = LayoutParams.WRAP_CONTENT;
		LinearLayout.LayoutParams layoutParams = 
				new LinearLayout.LayoutParams(wrap, wrap);
		
		int newCount = collections.size();
	    for(i = 0; i < newCount; i++){
	    	if (collections.get(i).getCountOfImages() != 0)
	    	{
	    		button = new Button(this);
	            button.setText(collections.get(i).getName());
	            button.setLayoutParams(layoutParams);
	            button.setTextColor(colors);
	            button.setTextSize((float)0.028*h);
	            button.setId(i);
	            button.setOnClickListener(this);
	            lin.addView(button);
	    	}
	    }
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	private void getScreenOrientation(){    
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			Log.i("THREAD", "Портретная ориентация");
		else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			Log.i("THREAD", "Альбомная ориентация");
	}
	
	private void getRotateOrientation() {
		int rotate = getWindowManager().getDefaultDisplay().getRotation();
		switch (rotate) {
		case Surface.ROTATION_0:
			Log.i("THREAD", "Не поворачивали");
			break;
		case Surface.ROTATION_90:
			Log.i("THREAD", "Повернули на 90 градусов по часовой стрелке");
			break;
		case Surface.ROTATION_180:
			Log.i("THREAD", "Повернули на 180 градусов");
			break;
		case Surface.ROTATION_270:
			Log.i("THREAD", "Повернули на 90 градусов по часовой стрелке");
			break;
		default:
			Log.i("THREAD", "Не понятно");
			break;
		}
	}
}
