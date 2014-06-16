package com.redlabrat.mozarttest.View;

import com.redlabrat.mozarttest.CollectionActivity;
import com.redlabrat.mozarttest.Image;
import com.redlabrat.mozarttest.Product;
import com.redlabrat.mozarttest.ProductViewActivity;
import com.redlabrat.mozarttest.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

	////////
	public static int w;
	public static int h;
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
		//image
		textViewDescript = (TextView) rootView.findViewById(R.id.descriptionText);

		// TODO:  need to set valid for device frame size
		int minHeight = (int) getResources().getDimension(R.dimen.descript_frame_default_height);
		int minWidth = (int)getResources().getDimension(R.dimen.descript_frame_default_width);
		LayoutParams lp = contentFrame.getLayoutParams();
		lp.width = minWidth;
		lp.height = minHeight;
		
		w = image.getWidth();
		h = image.getHeight();
		
		contentFrame.setLayoutParams(lp);
//		contentFrame.setMinimumHeight(minHeight);
//		contentFrame.setMinimumWidth(minWidth);
		
		addImageToScrollView();
		description = "";
		//Image i = CollectionActivity.collections.get(CollectionActivity.collectionNumber).images.get(ProductViewActivity.Pos);
		Image i = CollectionActivity.collections.get(CollectionActivity.collectionNumber).getImages().get(ProductViewActivity.Pos);
		for (Product p : i.getProducts())
		{
			description += p.getNumber() + " :\n";
			description += p.getDescription() + "\n";
		}
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
		image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);//CENTER_CROP
		//CENTER_INSIDE - the blank sides exist, can be by side or from all sides of the picture
		//CENTER_CROP - none of the white sides 
	}
	
	private void setPic(String imagePath, ImageView destination) {
	    int targetW = destination.getWidth();
	    int targetH = destination.getHeight();
	    // Get the dimensions of the bitmap
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(imagePath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;

	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;

	    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
	    destination.setImageBitmap(bitmap);
	}
	
	//Refresh the viewed fragment
	public void Rrefresh() {
		//image.refreshDrawableState();
	}
}
