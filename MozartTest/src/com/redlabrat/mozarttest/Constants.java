package com.redlabrat.mozarttest;

import android.content.Context;
import android.util.DisplayMetrics;
/**
 * Class contains all the constants strings used in the application
 * @author Alexandr Brich
 * @version 1.1
 * Created on July 10 2014
 */
public class Constants {
	public static final String fragmentImageID = "imageID";
	public static final String fragmentImageURL = "imageURL";
	public static final String fragmentImagePath = "imagePath";
	public static final String imagesFolderName = "images";
//	public static final String internalFolderName = "data";
	public static final String internalDataFileName = "names.dat";
	
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
