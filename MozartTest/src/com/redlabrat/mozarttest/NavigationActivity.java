package com.redlabrat.mozarttest;

import static com.redlabrat.mozarttest.Constants.catalog;

import java.io.File;
import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class NavigationActivity extends ActionBarActivity {
	public ActionBarDrawerToggle mDrawerToggle;
	public CharSequence mDrawerTitle;
	public CharSequence mTitle;
    
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    
    public ArrayList<String> col = new ArrayList<String>();
    private Context mContext;
    private LoadFromNet load = null;
/////////////////////////////
    public static ArrayList<Collection> collections = new ArrayList<Collection>();
    public static int collectionNumber = 0;
	public static int w;
	public static int h;
    public static double columnCount = 0;
/////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//For catalog.xml
		Log.i("Nav", "Size : ");
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
		
		/*if (savedInstanceState == null ) {
            collectionNumber = collections.size() - 1;
        }*/
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);
		setTitle("Коллекция " + collections.get(collectionNumber).getName());
		setNavigationList();
		
		mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // set up the drawer's list view with items and click listener
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, col);//NavigationList.list);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  // host Activity 
                mDrawerLayout,         // DrawerLayout object 
                R.drawable.ic_launcher,  // nav drawer image to replace 'Up' caret 
                R.string.drawer_open,  // "open drawer" description for accessibility 
                R.string.drawer_close  // "close drawer" description for accessibility 
                ) {
            public void onDrawerClosed(View view) {
            	setTitle(mTitle);
            	supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
            	//setTitle(mDrawerTitle);
            	supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        mDrawerList.setItemChecked(collectionNumber, true);
        
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
		
		//////////////////
		w = getWindowManager().getDefaultDisplay().getWidth();
		h = getWindowManager().getDefaultDisplay().getHeight();
    	//setting the number of columns showing on the screen
		columnCount = w/160;
		Log.i("Nav create ", "Size : " + w + "x" + h + " col = " + columnCount);
	}
	
	// The click listener for ListView in the navigation drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectItem(arg2);
		}
    }

    public void selectItem(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.collection, menu);
        return super.onCreateOptionsMenu(menu);
	}
	
	// Called whenever we call invalidateOptionsMenu() 
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// ActionBarDrawerToggle will take care of this.
    	if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
    	if (!isNetworkAvailable()) {
			Toast.makeText(this, "Нет интернет соединения!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (item.getItemId() == R.id.action_settings) {
			collections.clear();
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
        return super.onOptionsItemSelected(item);
    }
	
	/**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
   
	public void setNavigationList() {
		col = new ArrayList<String>();
		for (Collection c : collections) {
			col.add(c.getName());
		}
	}
	
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
