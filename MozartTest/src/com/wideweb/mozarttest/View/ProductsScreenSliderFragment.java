package com.wideweb.mozarttest.View;

import uk.co.senab.photoview.PhotoViewAttacher;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wideweb.mozarttest.Collection;
import com.wideweb.mozarttest.Image;
import com.wideweb.mozarttest.NavigationActivity;
import com.wideweb.mozarttest.Product;
import com.wideweb.mozarttest.R;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import static com.wideweb.mozarttest.Constants.*;

/**
 * Class represents how the fragment of the Product will be shown at the Screen
 * @author Alexandr Brich
 * @version 1.1
 */
public class ProductsScreenSliderFragment extends Fragment {
	/*** @serial Rectangle of the screen in which will be represented the product*/
	//private FrameLayout descriptionFrame = null;
	/*** @serial Contains an image of the product*/
	private ImageView imageView = null;
	/*** @serial The View of description*/
	private TextView textViewDescript = null;
	/*** @serial string with description of the product*/
	private String description = null;
	/*** @serial URL of the selected product(image)*/
	private String URL = null;

	/*** @serial number of the collection the image belong*/
	private int colNum = 0;
	/*** @serial position of the image in collection*/
	private int imageNum = 0;
	/*** @serial attacher to imageView in which the image can be zoomed, scales ...*/
	private PhotoViewAttacher mAttacher = null;
	
	/**
	 * Constructor invoke the call of constructor of super class
	 */
	public ProductsScreenSliderFragment() {
		super();
	}
	
	/** 
	 * Creates a Fragment view of the product 
	 * by using the view of the product and the device settings
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 * @param inflater builder for the view
	 * @param container set of views in which the particular product will be shown
	 * @param savedInstanceState saved state if the fragment
	 * @return View of the product's fragment which will be shown on the screen
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.page_to_scroll, container, false);
		colNum = getArguments().getInt(collection_number);
		imageNum = getArguments().getInt(image_number);
		
		Collection collection =  NavigationActivity.collections.get(colNum);
		Image img = collection.getImages().get(imageNum);
		//name = collection.getName() + " " + (ProductViewActivity.currentPage+1) + " / " + collection.getCountOfImages();
		URL = img.getURL();
		//ImageAdapter imageAdapter = new ImageAdapter(this, CollectionActivity.collections.get(num));
		
		imageView = (ImageView) rootView.findViewById(R.id.imageToShow);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        .bitmapConfig(Bitmap.Config.ARGB_8888)
	        .build();
		//ImageLoader.getInstance().displayImage(URL, imageView, options);
		Bitmap image = ImageLoader.getInstance().loadImageSync(URL, options);
		
		imageView.setImageBitmap(image);
		if (mAttacher == null) 
			mAttacher = new PhotoViewAttacher(imageView);
		
		mAttacher.update();
		mAttacher.setScaleType(ScaleType.CENTER_CROP);
		
		//Set the description frame
		//double minWidth = Math.min(GridActivity.w, GridActivity.h)/2;
		//descriptionFrame = (FrameLayout)rootView.findViewById(R.id.descriptionFrame);
		//descriptionFrame.setMinimumWidth((int)minWidth);
		textViewDescript = (TextView)rootView.findViewById(R.id.descriptionText);
		textViewDescript.setTextSize(NavigationActivity.smallSize);
		description = "";
		//textViewDescript.setWidth((int)minWidth);
		for (Product p : img.getProducts())
		{
			description += p.getNumber() + " :\n";
			description += p.getDescription() + "\n";
		}
		textViewDescript.setText(description);
        return rootView;
		/*
		int minHeight = (int) getResources().getDimension(R.dimen.descript_frame_default_height);
		int minWidth = (int)getResources().getDimension(R.dimen.descript_frame_default_width);
		LayoutParams lp = contentFrame.getLayoutParams();
		lp.width = minWidth;
		lp.height = minHeight;
		contentFrame.setLayoutParams(lp);
		*/
	}
}
