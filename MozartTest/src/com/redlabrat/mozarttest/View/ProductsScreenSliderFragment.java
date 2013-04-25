package com.redlabrat.mozarttest.View;

import java.util.ArrayList;

import com.redlabrat.mozarttest.DetailedViewActivity;
import com.redlabrat.mozarttest.R;
import com.redlabrat.mozarttest.data.ImageWithProducts;
import com.redlabrat.mozarttest.data.Product;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import static com.redlabrat.mozarttest.Constants.*;

public class ProductsScreenSliderFragment extends Fragment implements View.OnClickListener {

	private FrameLayout contentFrame = null;
	private ImageView imageView = null;
	//private TextView textViewDescript = null;
	private LinearLayout descriptLayout = null;
	//private String description = null;
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
		imageView.setOnClickListener(this);
		descriptLayout = (LinearLayout) rootView.findViewById(R.id.descriptionLayout);
		//textViewDescript = (TextView) rootView.findViewById(R.id.descriptionText);

		// TODO:  need to set valid for device frame size
//		int minHeight = (int) getResources().getDimension(R.dimen.descript_frame_default_height);
//		int minWidth = (int)getResources().getDimension(R.dimen.descript_frame_default_width);
//		LayoutParams lp = contentFrame.getLayoutParams();
//		lp.width = minWidth;
//		lp.height = android.app.ActionBar.LayoutParams.WRAP_CONTENT;
//		contentFrame.setLayoutParams(lp);
//		contentFrame.setMinimumHeight(minHeight);
//		contentFrame.setMinimumWidth(minWidth);
		
		addImageToScrollView();
		setDescription();

		return rootView;
	}
	
	@Override
	public void setArguments(Bundle args) {
		imageData = args.getParcelable(fragmentImage);
		Log.i("INFO", "Image path " + imageData.getFilePath() + " recieved");
	};
	
	
	
	private void addImageToScrollView() {
		if (imageData.getFilePath() != null) {
			Drawable drawable = Drawable.createFromPath(imageData.getFilePath());
			imageView.setImageDrawable(drawable);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}
	}
	
	private void setDescription() {
		ArrayList<Product> products = imageData.getListOfProducts();
		for (int counter = 0; counter < products.size(); counter++) {
			TextView header = new TextView(getActivity());
			header.setTextAppearance(getActivity(), R.style.TextHeaderAppearence);
			header.setText(products.get(counter).getName());
			header.setGravity(Gravity.CENTER);
			descriptLayout.addView(header);
			TextView contains = new TextView(getActivity());
			contains.setTextAppearance(getActivity(), R.style.TextContainsAppearence);
			contains.setText(products.get(counter).getDescription());
			contains.setGravity(Gravity.CENTER);
			descriptLayout.addView(contains);
		}
	}

	public void onClick(View v) {
		Intent intent = new Intent(getActivity(), DetailedViewActivity.class);
		Bundle params = new Bundle();
		params.putParcelable(imageDataForDetailedActivity, imageData);
		intent.putExtras(params);
		startActivity(intent);
	}
}
