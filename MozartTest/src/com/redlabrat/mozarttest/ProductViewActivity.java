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

public class ProductViewActivity extends FragmentActivity {

	private ViewPager pager = null;
	private PagerAdapter adapter = null;

	private ArrayList<String> imagesPaths = null;

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

	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			ProductsScreenSliderFragment fragment = new ProductsScreenSliderFragment();
			Bundle params = new Bundle();
			params.putString(fragmentImagePath, imagesPaths.get(position));
			fragment.setArguments(params);
			return fragment;
		}

		@Override
		public int getCount() {
			return imagesPaths.size();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
