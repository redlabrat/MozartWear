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

public class ProductsScreenSliderFragment extends Fragment {

	private FrameLayout contentFrame = null;
	private ImageView image = null;
	private TextView textViewDescript = null;
	private String description = null;
	private String imagePath = null;

	public ProductsScreenSliderFragment() {
		super();
	}
	
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
	
	@Override
	public void setArguments(Bundle args) {
		imagePath = args.getString(fragmentImagePath);
		Log.i("INFO", "Image path " + imagePath + " recived");
	};
	
	private void addImageToScrollView() {
		Drawable drawable = Drawable.createFromPath(imagePath);
		image.setImageDrawable(drawable);
		image.setScaleType(ImageView.ScaleType.CENTER_CROP);
	}
}
