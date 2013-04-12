package com.redlabrat.mozarttest;

import android.content.Context;
import android.util.DisplayMetrics;

public class Constants {
	public static final String fragmentImageID = "imageID";
	public static final String fragmentImageURL = "imageURL";
	public static final String fragmentImagePath = "imagePath";
	public static final String imagesFolderName = "images";
//	public static final String internalFolderName = "data";
	public static final String internalArrayFileName = "names.dat";
	public static final String internalXMLCatalogFileName = "catalog.xml";

	public static final String preferencesName = "preferences";
	public static final String pref_XMLCatalogCopied = "copyCatalog";
	
	public static final String dataTagName = "data";
	public static final String collectionTagName = "collection";
	public static final String imageTagName = "image";
	public static final String productTagName = "product";
	/**
	 *  Method converts dp points to corresponding device specific value of pixels 
	 * @param dp  Value of DIP need to convert
	 * @param context  Context of application
	 * @return  Corresponding value of pixels
	 */
	
	public static float convertDpToPixels(float dp, Context context) {
		DisplayMetrics metrics= context.getResources().getDisplayMetrics();
		float px = dp * metrics.density;
		return px;
	}

}
