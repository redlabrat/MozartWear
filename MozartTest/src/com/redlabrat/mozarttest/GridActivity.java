package com.redlabrat.mozarttest;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import android.support.v4.app.*;

public class GridActivity extends NavigationActivity {
	public static Context mContext;
	public static boolean first = true;
	public static boolean fromFull = false;
	public static int ORIENTATION = Configuration.ORIENTATION_PORTRAIT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mContext = getApplicationContext();
		super.onCreate(savedInstanceState);
		Log.i("Grid", "Create : " + w + "x" + h);
		//////////////////////////////////
		//by default it load the latest collection in list
		Log.i("Grid", "before if  "+ collectionNumber);
		if (ORIENTATION == getResources().getConfiguration().orientation) {
			Log.i("Grid", "without cnahges");
			if (!fromFull) {
	            collectionNumber = collections.size()-1;
	            Log.i("Grid", "in if -> "+ collectionNumber);
	        }
			else {
				fromFull = false;
			}
			selectItem(collectionNumber);
		} else {
			ORIENTATION = getResources().getConfiguration().orientation;
		}
	}
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		w = getWindowManager().getDefaultDisplay().getWidth();
		h = getWindowManager().getDefaultDisplay().getHeight();
		//columnCount = w/160;
		ORIENTATION = getResources().getConfiguration().orientation;
		Log.i("Grid override config ", "Size : " + w + "x" + h);
	}
	@Override
	public void selectItem(int position) {
		//choose the collection
		super.selectItem(position);
        collectionNumber = position;
        setTitle("Коллекция " + collections.get(collectionNumber).getName());
		// update the main content by replacing fragments
        //Fragment fragment = new GridFragment();
        Fragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putInt(GridFragment.ARG_COLLECTION_NUMBER, collectionNumber);
        fragment.setArguments(args);
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
	
	/**
     * Fragment that appears in the "content_frame", shows a grid with images
     */
    public static class GridFragment extends Fragment {
        public static final String ARG_COLLECTION_NUMBER = "collection_number";
        public static GridView gridview;
        public GridFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	Log.i("Grid", "Fragment create");
        	View rootView = inflater.inflate(R.layout.fragment_grid, container, false);
            int i = getArguments().getInt(ARG_COLLECTION_NUMBER);

    		//setting the number of columns showing on the screen
            w = getActivity().getWindowManager().getDefaultDisplay().getWidth();
    		h = getActivity().getWindowManager().getDefaultDisplay().getHeight();
    		columnCount = w/160;
    		gridview = (GridView) rootView.findViewById(R.id.gridView1);
    		gridview.setNumColumns((int)columnCount);
    		gridview.setAdapter(new ImageAdapter(GridActivity.mContext, GridActivity.collections.get(i)));
    		gridview.setOnItemClickListener(gridviewOnItemClickListener);

            return rootView;
        }
        
        private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
    		public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
    			//choose the image in current collection
    			Log.i("Grid in frag", "Col nomer = "+collectionNumber);
    			String name = "Коллекция " + collections.get(collectionNumber).getName() + " / " + position;
    			Intent intent = new Intent(getActivity().getApplicationContext(), FullImageActivity.class);
    	    	intent.putExtra("collection_number", collectionNumber);
    	    	intent.putExtra("image_number", position);
    	    	intent.putExtra("name", name);
    			if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
    	            startActivity(intent);
    	        } else {
    	            Log.i("App", "Not available");
    	        }
    			/*String name = "Коллекция " + collections.get(collectionNumber).getName() + " / " + position;
    	        Fragment fragment = new FullImageFragment();
    	        Bundle args = new Bundle();
    	        args.putInt(FullImageFragment.ARG_COLLECTION_NUMBER, collectionNumber);
    	        args.putInt(FullImageFragment.ARG_IMAGE_NUMBER, position);
    	        args.putString(FullImageFragment.ARG_NAME, name);
    	        fragment.setArguments(args);
    	        
    	        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    	        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/
    		}
    	};
    }
    
    /**
     * Fragment that appears in the "content_frame", shows a full image view
     */
    /*public static class FullImageFragment extends Fragment {
    	public static final String ARG_COLLECTION_NUMBER = "collection_number";
    	public static final String ARG_IMAGE_NUMBER = "image_number";
    	public static final String ARG_NAME = "name";
        
    	public FullImageFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_full_image, container, false);
            int colNum = getArguments().getInt(ARG_COLLECTION_NUMBER);
            int i = getArguments().getInt(ARG_IMAGE_NUMBER);
            String name = getArguments().getString(ARG_NAME);
    		
    		Collection collection = NavigationActivity.collections.get(colNum);
    		Image img = collection.getImages().get(i);
    		//ImageAdapter imageAdapter = new ImageAdapter(this, CollectionActivity.collections.get(num));
    		String URL = img.getURL();
    		
    		ImageView imageView = (ImageView) rootView.findViewById(R.id.full_image_view);
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
    		
    		//Set the description frame
    		double minWidth = Math.min(GridActivity.w, GridActivity.h)/2;
    		FrameLayout descriptionFrame = (FrameLayout)rootView.findViewById(R.id.descriptionFrame);
    		descriptionFrame.setMinimumWidth((int)minWidth);
    		
    		TextView textViewDescript = (TextView)rootView.findViewById(R.id.descriptionText);
    		String description = "";
    		textViewDescript.setWidth((int)minWidth);
    		for (Product p : img.getProducts())
    		{
    			description += p.getNumber() + " :\n";
    			description += p.getDescription() + "\n";
    		}
    		textViewDescript.setText(description);
    		
    		getActivity().setTitle(name);
            return rootView;
        }
    }*/
}
