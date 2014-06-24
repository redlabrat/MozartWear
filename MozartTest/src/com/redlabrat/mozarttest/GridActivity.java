package com.redlabrat.mozarttest;

import static com.redlabrat.mozarttest.Constants.catalog;

import java.io.File;
import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import android.content.Context;
import android.graphics.Bitmap;

public class GridActivity extends NavigationActivity {
	private GridView gridview;
    private Context mContext;
    private LoadFromNet load = null;
/////////////////////////////
    public static ArrayList<Collection> collections = new ArrayList<Collection>();
	public ArrayList<String> names;
    public static int collectionNumber = 0;
	public static int w;
	public static int h;
/////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//For catalog.xml
		names = new ArrayList<String>();
		mContext = getApplicationContext();
		File extChacheDir = mContext.getExternalCacheDir();
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			Toast.makeText(mContext, "Media not mounted!", Toast.LENGTH_SHORT).show();
		}
		load = new LoadFromNet(getApplicationContext(), catalog, false);
		Log.i("GRID", "after new Load");
		//get from URL the name of the image to save
		int startSubString = catalog.lastIndexOf("/");
		String fileName = catalog.substring(startSubString);
		File catalogFile = new File(extChacheDir, fileName);
		collections = new ArrayList<Collection>();
		Log.i("GRID", "after new ArrayList");
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
		setList();
		
		super.ACTIVITY_LAYOUT = R.layout.activity_grid;
		super.col = names;
		
		//by default it load the latest collection in list	
		if (savedInstanceState == null) {
            collectionNumber = collections.size() - 1;
        }
		setTitle("Коллекция " + collections.get(collectionNumber).getName());
		super.onCreate(savedInstanceState);
		
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
		//setting the number of columns showing on the screen
		double col = w/160;
		gridview = (GridView) findViewById(R.id.gridView1);
		gridview.setNumColumns((int)col);
		gridview.setAdapter(new ImageAdapter(this, collections.get(collectionNumber)));
		gridview.setOnItemClickListener(gridviewOnItemClickListener);
		
		super.mDrawerList.setItemChecked(collectionNumber, true);
	}
    
	@Override
	public void selectItem(int position) {
        super.selectItem(position);
        setTitle("Коллекция " + collections.get(position).getName());
        collectionNumber = position;
    	gridview.setAdapter(new ImageAdapter(this, collections.get(collectionNumber)));
    }
	
	private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
			String name = "Коллекция " + collections.get(collectionNumber).getName() + " / " + position;
			Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
			i.putExtra("id", position);
			i.putExtra("name", name);
			i.putExtra("list", names);
			startActivity(i);
		}
	};
    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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
			setList();
			Toast.makeText(this, "Обновлено!", Toast.LENGTH_SHORT).show();
		}
		return super.onMenuItemSelected(featureId, item);
	}
    
	public void setList() {
		names.clear();
		for (Collection c : collections) 
			names.add(c.getName());
	}
}
