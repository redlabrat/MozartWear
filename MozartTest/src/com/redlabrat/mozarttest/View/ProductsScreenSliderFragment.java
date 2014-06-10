package com.redlabrat.mozarttest.View;

import com.redlabrat.mozarttest.R;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import static com.redlabrat.mozarttest.Constants.*;
/**
 * Class represents how the fragment of the Product will be shown at the Screen
 * @author Alexandr Brich
 * @version 1.1
 */
public class ProductsScreenSliderFragment extends Fragment {
	/*** @serial Rectangle of the screen in which will be represented the product*/
	private FrameLayout contentFrame = null;
	/*** @serial Contains an image of the product*/
	private ImageView image = null;
	/*** @serial The View of description*/
	private TextView textViewDescript = null;
	/*** @serial string with description of the product*/
	private String description = null;
	/*** @serial path to the selected product(image)*/
	private String imagePath = null;

	/**
	 * Constructor invoke the call of constructor of super class
	 */
	public ProductsScreenSliderFragment() {
		super();
	}
	
	/** 
	 * Creates a Fragment view of the product by using the view of the product and the device settings
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 * @param inflater 
	 * @param container set of views in which the particular product will be shown
	 * @param savedInstanceState State of the view
	 * @return View of the product's fragment which will be shown on the screen
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.page_to_scroll, container, false);
		contentFrame = (FrameLayout) rootView.findViewById(R.id.descriptionFrame);
		image = (ImageView) rootView.findViewById(R.id.imageToShow);
		textViewDescript = (TextView) rootView.findViewById(R.id.descriptionText);

		// TODO:  need to set valid for device frame size
		int minHeight = (int) getResources().getDimension(R.dimen.descript_frame_default_height);
		int minWidth = (int)getResources().getDimension(R.dimen.descript_frame_default_width);
		LayoutParams lp = contentFrame.getLayoutParams();
		lp.width = minWidth;
		lp.height = minHeight;
		contentFrame.setLayoutParams(lp);
//		contentFrame.setMinimumHeight(minHeight);
//		contentFrame.setMinimumWidth(minWidth);
		
		addImageToScrollView();
		description = "Default description\n" + "image path: " + imagePath;
		textViewDescript.setText(description);

		return rootView;
	}
	
	/**
	 * Set the argument of the image path
	 * @param args storages the image path
	 * @see android.support.v4.app.Fragment#setArguments(android.os.Bundle)
	 */
	@Override
	public void setArguments(Bundle args) {
		imagePath = args.getString(fragmentImagePath);
		Log.i("INFO", "Image path " + imagePath + " recived");
	};
	
	/**
	 * Add selected image to the Scroll View to enable scrolling
	 * and customize the way it will be shown on the screen
	 */
	private void addImageToScrollView() {
		Drawable drawable = Drawable.createFromPath(imagePath);
		image.setImageDrawable(drawable);
		image.setScaleType(ImageView.ScaleType.CENTER_CROP);
	}
}
