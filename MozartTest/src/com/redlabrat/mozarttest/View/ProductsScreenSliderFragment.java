package com.redlabrat.mozarttest.View;

import java.util.ArrayList;

import com.redlabrat.mozarttest.R;
import com.redlabrat.mozarttest.data.ImageWithProducts;
import com.redlabrat.mozarttest.data.Product;

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

public class ProductsScreenSliderFragment extends Fragment {

	private FrameLayout contentFrame = null;
	private ImageView imageView = null;
	private TextView textViewDescript = null;
	private String description = null;
	private ImageWithProducts imageData = null;

	public ProductsScreenSliderFragment() {
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.page_to_scroll, container, false);
		contentFrame = (FrameLayout) rootView.findViewById(R.id.descriptionFrame);
		imageView = (ImageView) rootView.findViewById(R.id.imageToShow);
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
		setDescriptionText();

		return rootView;
	}
	
	@Override
	public void setArguments(Bundle args) {
		imageData = args.getParcelable(fragmentImage);
		Log.i("INFO", "Image path " + imageData.getFilePath() + " recieved");
	};
	
	private void addImageToScrollView() {
		Drawable drawable = Drawable.createFromPath(imageData.getFilePath());
		imageView.setImageDrawable(drawable);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	}
	
	private void setDescriptionText() {
		description = "";
		ArrayList<Product> products = imageData.getListOfProducts();
		for (int counter = 0; counter < products.size(); counter++) {
			description += products.get(counter).getDescription() + "\n";
		}
		textViewDescript.setText(description);
	}
}
