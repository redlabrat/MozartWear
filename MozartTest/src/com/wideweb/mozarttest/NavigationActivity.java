package com.wideweb.mozarttest;

import static com.wideweb.mozarttest.Constants.mozart;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

/**
 * Class of the base activity with action bar and navigation drawer
 * @author Kate Zenevich
 * @version 1.0
 */
public class NavigationActivity extends ActionBarActivity {
	/*** @serial action bar of the application*/
	private ActionBarDrawerToggle mDrawerToggle;
	/*** @serial the title to the navigation drawer*/
	//private CharSequence mDrawerTitle;
	/*** @serial the title of the action bar*/
	private CharSequence mTitle;
	/*** @serial layout that handles the navigation drawer*/
	private DrawerLayout mDrawerLayout;
    /*** @serial stores the navigation drawer content list*/
    private ListView mDrawerList;
    /*** @serial action bar textView*/
    private TextView actionBarTitleView;
    /*** @serial action bar view with custom typeface*/
    private View mCustomActionView = null;
    
    /*** @serial navigation list*/
    private ArrayList<String> collectionNames = new ArrayList<String>();
    /*** @serial context of the application*/
    private Context mContext;
    /*** @serial helps to download and parse the xml file*/
    private LoadFromNet load = null;

    /*** @serial list of available collection from xml file*/
    public static ArrayList<Collection> collections;// = new ArrayList<Collection>();
    /*** @serial current collection position*/
    public static int collectionNumber = 0;
    /*** @serial width of the current device's display*/
	public static int w;
	/*** @serial height of the current device's display*/
	public static int h;
	/*** @serial GridView column count for current device's display*/
    public static double columnCount = 0;
    /*** @serial indicator of connection presence*/
	public boolean noConnect = false;
    
	public static float smallSize = 0;
	public static boolean first = true;
	/** 
   	 * Create activity with navigation drawer and action bar
   	 * @see android.app.Activity#onCreate(android.os.Bundle)
   	 * @param savedInstanceState represents the saved state of the activity 
   	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//For catalog.xml
		mContext = getApplicationContext();
		Intent intent = getIntent();
		if (intent.getCategories() != null && 
				intent.getCategories().contains("android.intent.category.LAUNCHER") &&
				first ) {
			GridActivity.ORIENTATION = getResources().getConfiguration().orientation;
			first = false;
			boolean netAccess = isNetworkAvailable();
			//if file already exist then we only parsing it (second parameter - update options)
			load = new LoadFromNet(mContext, false, netAccess); 
			collections = new ArrayList<Collection>();
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
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		
		if (collections.size() == 0) { //first start without net access
			noConnect = true;
			setTitle(mozart);
			Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_LONG).show();
			//Toast.makeText(this, "��� �������� ����������!", Toast.LENGTH_LONG).show();
		}
		//else setTitle(collections.get(collectionNumber).getName());
		
		setNavigationList();
		
		//mTitle = mDrawerTitle = getTitle();
		mTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set up the drawer's list view with items and click listener
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, collectionNames);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        // enable ActionBar app icon to behave as action to toggle nav drawer
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setIcon(R.color.transparent);
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  // host Activity 
                mDrawerLayout,         // DrawerLayout object 
                R.drawable.ic_drawer,  // nav drawer image to replace 'Up' caret 
                R.string.drawer_open,  // "open drawer" description for accessibility 
                R.string.drawer_close  // "close drawer" description for accessibility 
                ) {
            public void onDrawerClosed(View view) {
            	setTitle(mTitle);
            	supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View drawerView) {
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
		if (ImageLoader.getInstance().isInited())
			ImageLoader.getInstance().destroy();
		
		ImageLoader.getInstance().init(config);
		//////////////////
		w = getWindowManager().getDefaultDisplay().getWidth();
		h = getWindowManager().getDefaultDisplay().getHeight();
		Log.i("Nav", "Device : "+w+"x"+h);
    	//setting the number of columns showing on the screen
		columnCount = w/160;
		float titleSize = Math.max(w, h)/32;
		smallSize = Math.max(w, h)/50;
		actionBarTitleView = null;
		if (Build.VERSION.SDK_INT < 11) {
			actionBarTitleView = (TextView)findViewById(R.id.action_bar_title);
			AssetManager manager = getAssets();
			Typeface type = null;
			if (manager != null && actionBarTitleView != null) {
				type = Typeface.createFromAsset(getAssets(), CustomTextView.fontPath);
			}
			actionBarTitleView.setTextSize(titleSize);
			actionBarTitleView.setTypeface(type);
		} else {
			// set action bar custom view
			LayoutInflater inflator = (LayoutInflater) this
		            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    getSupportActionBar().setDisplayShowCustomEnabled(true);
		    getSupportActionBar().setDisplayShowTitleEnabled(false);
		    mCustomActionView = inflator.inflate(R.layout.action_bar_text, null);
		    // assign the view to the actionbar
		    getSupportActionBar().setCustomView(mCustomActionView);
		    actionBarTitleView = ((TextView) mCustomActionView.findViewById(R.id.actionBarText));
		    actionBarTitleView.setTextSize(titleSize);
		}
	    //setTitle(mozart);
	}
	
	/**
	 * Class handles onClick event in navigation drawer list
	 * @author Kate Zenevich
	 * @version 1.0
	 */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
    	/** 
    	 * Calling when the item in the navigation list was selected
    	 * @param arg0 adapter of the data in the listView
    	 * @param arg1 the listView in which was risen this event
    	 * @param arg2 selected position in the list
    	 * @param arg3 number of item
    	 */
    	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectItem(arg2);
		}
    }

    /** 
	 * Select the navigation list item and hide the drawer
	 * @param position position of the item in list of the navigation drawer 
	 */
    public void selectItem(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /** 
	 * Set the title of the action bar
	 * @see android.app.Activity#setTitle(java.lang.CharSequence)
	 * @param title action bar title
	 */
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (Build.VERSION.SDK_INT <11) {
        	getSupportActionBar().setTitle(mTitle);
        }
        else {
        	actionBarTitleView.setText(mTitle);
        	getSupportActionBar().setTitle("");
        }
    }
    
    /** 
	 * Create the menu in this activity
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 * @param menu the menu to create
	 * @return true if menu was created without a problems, false if it wasn't created or an error 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.collection, menu);
        return super.onCreateOptionsMenu(menu);
	}
	
	/** 
	 * Inflate the menu in this activity
	 * hide the options menu if the navigation drawer was opened
	 * calling whenever the supportInvalidateOptionsMenu() is called
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 * @param menu the menu to prepare
	 * @return true if menu was updated without a problems, false if it was a problem
	 */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view    	
    	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_update).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /** 
	 * Calling when was selected the options item (in Action bar or options menu)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 * @param item the selected menu item
	 * @return true if it was successfully handled, false if not
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// ActionBarDrawerToggle will take care of this.
    	if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		if (item.getItemId() == R.id.action_update) {
			boolean netAccess = isNetworkAvailable();
			if (!netAccess) {
				Toast.makeText(this, R.string.error_no_internet, Toast.LENGTH_SHORT).show();
				//Toast.makeText(this, "��� �������� ����������!", Toast.LENGTH_SHORT).show();
				return false;
			}
			collections.clear();
			//forcing to update the xml file (second parameter - update options)
			load = new LoadFromNet(getApplicationContext(), true, netAccess);
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
			if (noConnect) {
				noConnect = false;
				Intent intent = getIntent();
			    finish();
			    startActivity(intent);
			}
			Toast.makeText(this, R.string.updated, Toast.LENGTH_SHORT).show();
			//Toast.makeText(this, "���������!", Toast.LENGTH_SHORT).show();
		}
        return super.onOptionsItemSelected(item);
    }
	
    /** 
	 * Calling after creating the activity before showing it
	 * synchronize action bar
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 * @param savedInstanceState represents the state of the activity 
	 */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    
    /** 
	 * Apply new configuration of activity
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 * @param newConfig the new configuration of the activity
	 */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
   
    /** 
	 * Sets the navigation list in Navigation drawer menu
	 */
	public void setNavigationList() {
		collectionNames = new ArrayList<String>();
		for (Collection c : collections) {
			collectionNames.add(c.getName());
		}
	}
	
	/** 
	 * Checking for active network connection
	 * @return true if there is active network connection, false if not
	 */
	public boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
