package com.redlabrat.mozarttest;

import java.util.ArrayList;

import com.redlabrat.mozarttest.View.ProductsScreenSliderFragment;

import static com.redlabrat.mozarttest.Constants.*;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Class of the product view screen
 * @author Alexandr Brich
 * @version 1.1
 */
public class ProductViewActivity extends NavigationActivity {//FragmentActivity
	/*** @serial make available handle the set of image pages*/
	private static ViewPager pager = null;
	/*** @serial handle the data which will be stored in the pages*/
	private static PagerAdapter adapter = null;
	//private static ScreenSlidePagerAdapter adapter = null;
	
	public static String collectionName = null;
	/*** @serial list of the image's paths*/
	private ArrayList<String> imagesPaths = null;
	
	/*** @serial number of the collection the image belong*/
	public static int colNum = 0;
	/*** @serial position of the image in collection*/
	private int imageNum = 0;
	
	/**
	 * Create an instance of main activity and initialize it
	 * @param savedInstanceState previously saved state of the activity
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i("Product", "ProductViewActivtiy");
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		imageNum = i.getExtras().getInt(image_number);
		colNum = i.getExtras().getInt(collection_number);
		getImagesPaths();
		
		adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

		Fragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(image_number, imageNum);
        fragment.setArguments(args);
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	}

	@Override
    public void selectItem(int position) {
    	super.selectItem(position);
    	GridActivity.collectionNumber = position;
    	GridActivity.fromFull = true;
    	Intent intent = new Intent(getApplicationContext(), GridActivity.class);
    	if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.i("App", "Not available");
        }
    }
    
	/**
	 * Get list of the images paths
	 */
	private void getImagesPaths() {
		imagesPaths = new ArrayList<String>();
		for (Image im : NavigationActivity.collections.get(colNum).getImages())
			imagesPaths.add(im.getURL());
	}
	
	/**
     * Fragment that appears in the "content_frame", consist of ViewPager
     */
    public static class PageFragment extends Fragment {
    	public PageFragment() {
            // Empty constructor required for fragment subclasses
        }
    	
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	View rootView = inflater.inflate(R.layout.activity_product_view, container, false);
            int img = getArguments().getInt(image_number);
    		
            pager = (ViewPager) rootView.findViewById(R.id.productsPager);
            pager.setAdapter(adapter);
    		OnPageChangeListener listener = new OnPageChangeListener() {
    			public void onPageSelected(int arg0) {
    				// TODO Auto-generated method stub
    				Log.i("Pager", "page selected " + arg0);
                    Collection collection =  NavigationActivity.collections.get(ProductViewActivity.colNum);
        			String name = collection.getName() + " " + (arg0+1) + " / " + collection.getCountOfImages();
        			getActivity().setTitle(name);
    			}
    			public void onPageScrolled(int arg0, float arg1, int arg2) {
    				// TODO Auto-generated method stub
    			}
    			public void onPageScrollStateChanged(int arg0) {
    				// TODO Auto-generated method stub	
    			}
    		};
    		pager.setOnPageChangeListener(listener);
    		pager.setCurrentItem(img, true);
            return rootView;
        }
    }

	/**
	 * Help to organize the possibility to switch between the product's views (each slides)
	 * @author Alexandr Brich
	 * @version 1.1
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * Get slide with product by its number
		 * @param position position in list of images path which mean the particular product
		 * @return slide with particular product
		 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int position) {
			Log.i("getItem", "Position " + position);
			Fragment fragment = new ProductsScreenSliderFragment();
			
			Bundle params = new Bundle();
			params.putInt(collection_number, colNum);
			params.putInt(image_number, position);
			fragment.setArguments(params);
			return fragment;
		}

		/**
		 * Get the total count of the image's paths
		 * @return count of the image's paths
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return imagesPaths.size();
		}
	}
}
