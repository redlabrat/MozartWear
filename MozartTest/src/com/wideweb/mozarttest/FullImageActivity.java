package com.wideweb.mozarttest;

import uk.co.senab.photoview.PhotoViewAttacher;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import static com.wideweb.mozarttest.Constants.*;

public class FullImageActivity extends NavigationActivity {
	public int num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("Full", "Create");
		Intent i = getIntent();
		// Selected image id
		int position = i.getExtras().getInt(image_number);
		String name = i.getExtras().getString(title);
		num = i.getExtras().getInt(collection_number);

		super.onCreate(savedInstanceState);
		setTitle(name);

		Fragment fragment = new FullImageFragment();
		Bundle args = new Bundle();
		args.putInt(collection_number, num);
		args.putInt(image_number, position);
		args.putString(title, name);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		GridActivity.collectionNumber = num;
		super.onDestroy();
	}

	/**
	 * Fragment that appears in the "content_frame", shows a full image view
	 */
	public static class FullImageFragment extends Fragment {
		public FullImageFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.i("Full", "Fragment create");
			View rootView = inflater.inflate(R.layout.fragment_full_image,
					container, false);
			int colNum = getArguments().getInt(collection_number);
			int i = getArguments().getInt(image_number);
			String name = getArguments().getString(title);

			Collection collection = NavigationActivity.collections.get(colNum);
			Image img = collection.getImages().get(i);
			// ImageAdapter imageAdapter = new ImageAdapter(this,
			// CollectionActivity.collections.get(num));
			String URL = img.getURL();

			ImageView imageView = (ImageView) rootView
					.findViewById(R.id.full_image_view);
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			// ImageLoader.getInstance().displayImage(URL, imageView, options);
			Bitmap image = ImageLoader.getInstance()
					.loadImageSync(URL, options);
			PhotoViewAttacher mAttacher = null;

			imageView.setImageBitmap(image);
			mAttacher = new PhotoViewAttacher(imageView);
			mAttacher.setScaleType(ScaleType.CENTER_CROP);

			// Set the description frame
			double minWidth = Math.min(GridActivity.w, GridActivity.h) / 2;
			FrameLayout descriptionFrame = (FrameLayout) rootView
					.findViewById(R.id.descriptionFrame);
			descriptionFrame.setMinimumWidth((int) minWidth);

			TextView textViewDescript = (TextView) rootView
					.findViewById(R.id.descriptionText);
			String description = "";
			textViewDescript.setWidth((int) minWidth);
			for (Product p : img.getProducts()) {
				description += p.getNumber() + " :\n";
				description += p.getDescription() + "\n";
			}
			textViewDescript.setText(description);

			getActivity().setTitle(name);
			return rootView;
		}
	}
}
