package com.redlabrat.mozarttest;

import java.util.ArrayList;

import com.redlabrat.mozarttest.View.ProductsScreenSliderFragment;
import com.redlabrat.mozarttest.helpers.FileHelper;

import static com.redlabrat.mozarttest.Constants.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

/**
 * Class of the product view screen
 * @author Alexandr Brich
 * @version 1.1
 */
public class ProductViewActivity extends FragmentActivity {

	private ViewPager pager = null;
	private PagerAdapter adapter = null;
	
	/*** @serial list of the image's paths*/
	private ArrayList<String> imagesPaths = null;

	/**
	 * Create an instance of main activity and initialize it
	 * @param savedInstanceState previously saved state of the activity
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_view);

		getImagesPaths();
		pager = (ViewPager) findViewById(R.id.productsPager);
		adapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		// ImageView image2 = new ImageView(getApplicationContext());
		// scroll = (LinearLayout) findViewById(R.id.horizontallScroll);
		// descriptionText = (TextView) findViewById(R.id.descriptionText);
		// image.setImageBitmap(new
		//				ImageHelper().downloadImage("http://mozartwear.com/assets/images/zima/img1.jpg"));
		// loadImages();
	}

	/**
	 * Get list of the images paths
	 */
	private void getImagesPaths() {
		imagesPaths = new ArrayList<String>();
		
		FileHelper fh = new FileHelper(getApplicationContext());
		// after service finished
		//imagesPaths = fh.loadImagesNames();
		
		imagesPaths.add(fh.getImagePath("img1.jpg"));
		imagesPaths.add(fh.getImagePath("img2.jpg"));
		imagesPaths.add(fh.getImagePath("img3.jpg"));
		imagesPaths.add(fh.getImagePath("img4.jpg"));
		imagesPaths.add(fh.getImagePath("img5.jpg"));
		imagesPaths.add(fh.getImagePath("img6.jpg"));
		imagesPaths.add(fh.getImagePath("img7.jpg"));
		imagesPaths.add(fh.getImagePath("img8.jpg"));
		imagesPaths.add(fh.getImagePath("img10.jpg"));
		imagesPaths.add(fh.getImagePath("img11.jpg"));
		imagesPaths.add(fh.getImagePath("img12.jpg"));
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
			ProductsScreenSliderFragment fragment = new ProductsScreenSliderFragment();
			Bundle params = new Bundle();
			params.putString(fragmentImagePath, imagesPaths.get(position));
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

	/** 
	 * Inflate the menu in this activity
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 * @param menu items to add like optional menu
	 * @return true if menu was created without a problems, false if it wasn't created or an error 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
