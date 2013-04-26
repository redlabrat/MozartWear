package com.redlabrat.mozarttest;

import com.redlabrat.mozarttest.data.ImageWithProducts;
import com.redlabrat.mozarttest.helpers.FileHelper;

import static com.redlabrat.mozarttest.Constants.*;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

public class DetailedViewActivity extends Activity {
	
	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// no title for maximizing image 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detailed_view);

		final WebView contentView = (WebView) findViewById(R.id.detailedWebVeiw);
		
		ImageWithProducts imageData = getIntent().getExtras().getParcelable(imageDataForDetailedActivity);
		imageData.setFilePath(new FileHelper(getApplicationContext()).getImagePath("img1.jpg"));
		String imagePath = "file://" + imageData.getFilePath();
		Drawable imageDraw = Drawable.createFromPath(imageData.getFilePath());
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);

		int scaleW = (int) ((double) (metrics.widthPixels / imageDraw.getMinimumWidth())) * 100;
		int scaleH = (int) ((double) (metrics.heightPixels / imageDraw.getMinimumHeight())) * 100;
		int scale = 0;
		if (scaleH > scaleW) {
			scale = scaleH;
		} else {
			scale = scaleW;
		}
		settings = contentView.getSettings();
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		//settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		//settings.setUseWideViewPort(true);
		//settings.setLoadWithOverviewMode(true);
//		int width = metrics.heightPixels;
		//<meta name='viewport' content='target-densitydpi=device-dpi,initial-scale=1,minimum-scale=1,user-scalable=yes'/>
//		String data="<html><head><meta name=\'viewport\' content=\'target-densitydpi=device-dpi,minimum-scale="+scale+",user-scalable=yes\'/></head>";
//		data=data+"<body><center><img height=\'100%\' src=\'"+imagePath+"\' /></center></body></html>";
//		contentView.loadData(data, "text/html", null);
//		settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

		contentView.setInitialScale(scale);
		contentView.loadUrl(imagePath);
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}
}
