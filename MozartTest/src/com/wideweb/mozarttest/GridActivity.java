package com.wideweb.mozarttest;

import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.support.v4.app.*;
import static com.wideweb.mozarttest.Constants.*;

/**
 * Class for the base activity with added grid view with images
 * 
 * @author Kate Zenevich
 * @version 1.0
 */
public class GridActivity extends NavigationActivity {
	public static boolean fromFull = false;
	public static int ORIENTATION = Configuration.ORIENTATION_PORTRAIT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// by default it load the latest collection in list
		int newOrientation = getResources().getConfiguration().orientation;
		if (collections.size() != 0) {
			if (ORIENTATION == newOrientation) {
				Log.i("Grid", "without cnahges");
				if (!fromFull) {
					collectionNumber = collections.size() - 1;
				} else {
					fromFull = false;
				}
				selectItem(collectionNumber);
			} else {
				Log.i("Grid", "Changes was!");
				ORIENTATION = newOrientation;

				setTitle(collections.get(collectionNumber).getName());

				if (fromFull) {
					fromFull = false;
					selectItem(collectionNumber);
				}
			}
		}
	}

	@Override
	public void selectItem(int position) {
		// choose the collection
		super.selectItem(position);
		collectionNumber = position;
		setTitle(collections.get(collectionNumber).getName());
		// update the main content by replacing fragments
		Fragment fragment = new GridFragment();
		Bundle args = new Bundle();
		args.putInt(collection_number, collectionNumber);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
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
			View rootView = inflater.inflate(R.layout.fragment_grid, container,
					false);
			int i = getArguments().getInt(collection_number);
			// setting the number of columns showing on the screen
			DisplayMetrics metrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
			w = metrics.widthPixels;
			h = metrics.heightPixels;
			columnCount = w / 160;
			gridview = (GridView) rootView.findViewById(R.id.gridView1);
			gridview.setNumColumns((int) columnCount);
			gridview.setAdapter(new ImageAdapter(getActivity()
					.getApplicationContext(), GridActivity.collections.get(i)));
			gridview.setOnItemClickListener(gridviewOnItemClickListener);
			return rootView;
		}

		/**
		 * @serial onClickListener for the grid view - click on the particular
		 *         picture in the grid
		 */
		private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// choose the image in current collection
				Intent intent = new Intent(getActivity()
						.getApplicationContext(), // FullImageActivity.class);
						ProductViewActivity.class);
				intent.putExtra(collection_number, collectionNumber);
				intent.putExtra(image_number, position);
				if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
					startActivity(intent);
				} else {
					Log.i("App", "Not available");
				}
			}
		};
	}
}
