package com.wideweb.mozarttest;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Class of the TextView with custom typeface of the text
 * @author Kate Zenevich
 * @version 1.0
 */
public class CustomTextView extends TextView {
	public static Typeface font = null;
	public static String fontPath = "fonts/Roboto-Thin.ttf";
	//"fonts/Roboto-Thin.ttf";
	//"fonts/Geometria-Light.otf";
	//"fonts/CaviarDreams_Italic.ttf";
	public CustomTextView(Context context) {
		super(context);
		setFont();
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}
	
	/** 
	 * Set up the typeface of the text in textView
	 */
	private void setFont() {
	    if (!isInEditMode()) {
	    	try {
				font = Typeface.createFromAsset(getContext().getAssets(), fontPath);
	        } catch (Exception e) {
	            Log.e("ERROR", "Could not get typeface '" + fontPath
	                    + "' because " + e.getMessage());
	        }
	    	setTypeface(font);
	    }
	}
}
