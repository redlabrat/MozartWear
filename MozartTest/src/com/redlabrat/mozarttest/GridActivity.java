package com.redlabrat.mozarttest;

import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import android.support.v4.app.*;

import static com.redlabrat.mozarttest.Constants.*;

/**
 * Class for the base activity with added grid view with images
 * @author Kate Zenevich
 * @version 1.0
 */
public class GridActivity extends NavigationActivity {
	//public static Context mContext;
	public static boolean fromFull = false;
	public static int ORIENTATION = Configuration.ORIENTATION_PORTRAIT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//mContext = getApplicationContext();
		super.onCreate(savedInstanceState);
		Log.i("Grid", "Create : " + w + "x" + h);
		//////////////////////////////////
		//by default it load the latest collection in list
		Log.i("Grid", "before if  "+ collectionNumber);
		int newOrientation = getResources().getConfiguration().orientation;
		if (ORIENTATION == newOrientation) {
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
			Log.i("Grid", "Changes was!");
			ORIENTATION = newOrientation;
		}
		setTitle("Коллекция " + collections.get(collectionNumber).getName());
	}
    
	/*@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		ORIENTATION = getResources().getConfiguration().orientation;
	}*/
	
	@Override
	public void selectItem(int position) {
		//choose the collection
		super.selectItem(position);
        collectionNumber = position;
        setTitle("Коллекция " + collections.get(collectionNumber).getName());
		// update the main content by replacing fragments
        Fragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putInt(collection_number, collectionNumber);
        fragment.setArguments(args);
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
	
	/**
     * Fragment that appears in the "content_frame", shows a grid with images
     */
    public static class GridFragment extends Fragment {
        public static GridView gridview;
        
        public GridFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	Log.i("Grid", "Fragment create");
        	View rootView = inflater.inflate(R.layout.fragment_grid, container, false);
            int i = getArguments().getInt(collection_number);

    		//setting the number of columns showing on the screen
            w = getActivity().getWindowManager().getDefaultDisplay().getWidth();
    		h = getActivity().getWindowManager().getDefaultDisplay().getHeight();
    		columnCount = w/160;
    		gridview = (GridView) rootView.findViewById(R.id.gridView1);
    		gridview.setNumColumns((int)columnCount);
    		gridview.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), GridActivity.collections.get(i)));
    		gridview.setOnItemClickListener(gridviewOnItemClickListener);

            return rootView;
        }
        
        /** @serial onClickListener for the grid view -
         *  click on the particular picture in the grid
         */
        private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
    		public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
    			//choose the image in current collection
    			Intent intent = new Intent(getActivity().getApplicationContext(), //FullImageActivity.class);
    					ProductViewActivity.class);
    	    	intent.putExtra(collection_number, collectionNumber);
    	    	intent.putExtra(image_number, position);
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
}
