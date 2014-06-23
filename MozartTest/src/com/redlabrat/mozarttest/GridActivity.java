package com.redlabrat.mozarttest;

import static com.redlabrat.mozarttest.Constants.catalog;

import java.io.File;
import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.widget.DrawerLayout;

public class GridActivity extends Activity {
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private GridView gridview;
    private Context mContext;
    private LoadFromNet load = null;
/////////////////////////////
    public static ArrayList<Collection> collections = new ArrayList<Collection>();
	public static int collectionNumber = 0;
	public static int w;
	public static int h;
/////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = getApplicationContext();
		File extChacheDir = mContext.getExternalCacheDir();
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			Toast.makeText(mContext, "Media not mounted!", Toast.LENGTH_SHORT).show();
		}
		load = new LoadFromNet(getApplicationContext(), catalog, false);
		
		//get from URL the name of the image to save
		int startSubString = catalog.lastIndexOf("/");
		String fileName = catalog.substring(startSubString);
		File catalogFile = new File(extChacheDir, fileName);
		collections = new ArrayList<Collection>();
		if (!catalogFile.exists()){
			load.start();//download the catalog from Net
			if(load.isAlive()) {
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
		setContentView(R.layout.activity_grid);
		
		setNavigationList();
		
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set up the drawer's list view with items and click listener
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, NavigationList.list);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
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
		w = getWindowManager().getDefaultDisplay().getWidth();
		h = getWindowManager().getDefaultDisplay().getHeight();
		Log.i("Display", "Size : " + w + "x" + h);
		//////////////////////////////////
		mDrawerList.setMinimumWidth((int)Math.min(w, h)/2);
		
		//setting the number of columns showing on the screen
		double col = w/160;
		//by default it load the latest collection in list
		if (savedInstanceState == null) {
            collectionNumber = collections.size() - 1;
        }
		gridview = (GridView) findViewById(R.id.gridView1);
		gridview.setNumColumns((int)col);
		gridview.setAdapter(new ImageAdapter(this, collections.get(collectionNumber)));
		gridview.setOnItemClickListener(gridviewOnItemClickListener);

		setTitle("Коллекция " + collections.get(collectionNumber).getName());
	}
	 
	// The click listener for ListView in the navigation drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectItem(arg2);
		}
    }

    private void selectItem(int position) {
    	mDrawerList.setItemChecked(position, true);
    	mDrawerLayout.closeDrawer(mDrawerList);
    	collectionNumber = position;
    	gridview.setAdapter(new ImageAdapter(this, collections.get(collectionNumber)));
    	setTitle("Коллекция " + collections.get(position).getName());
    }
	
	private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
			Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
			i.putExtra("id", position);
			startActivity(i);
		}
	};
	
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
			collections.clear();
			if (collections.size() != 0) {
				Log.i("onClick", "Cannot clear the collection set!");
				return false;
			}
			load = new LoadFromNet(getApplicationContext(), catalog, true);
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
			//to update NagivationList !
			setNavigationList();
			Toast.makeText(this, "Обновлено!", Toast.LENGTH_SHORT).show();
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	public void setNavigationList() {
		NavigationList.list.clear();
		if (NavigationList.list.size() == 0) {
			int newCount = collections.size();
		    for(int i = 0; i < newCount; i++){
		    	NavigationList.list.add(collections.get(i).getName());
		    	/*if (collections.get(i).getCountOfImages() != 0)
		    	{
		    		NavigationList.list.add(collections.get(i).getName());
		    	}*/
		    }
		}
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
