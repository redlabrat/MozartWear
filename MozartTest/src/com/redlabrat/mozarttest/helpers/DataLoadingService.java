package com.redlabrat.mozarttest.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.redlabrat.mozarttest.R;
import com.redlabrat.mozarttest.data.Collection;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DataLoadingService extends Service {

	private ArrayList<String> imageURLs = null;
	private ArrayList<String> imageNames = null;
	private static ImageHelper ih = null;
	private static FileHelper fh = null;
	private IBinder binder = null;
	/**
	 *  1. Load XML file
	 *  2. Check for changes
	 *  3. If not changed - finish work
	 *  4. If changed - load new data
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		binder = new Binder();
		ih = new ImageHelper(getApplicationContext());
		fh = new FileHelper(getApplicationContext());
		
		// if there is no Internet connection return error
		// 4. Load new data
		// get URL of images from XML
		getImagesURLs();
		// get description of products
		getImagesFromNetwork();
		
		// save products description as array in preferences
		
		return binder;
	}
	
	private void getImagesURLs() {
		XMLParser parser = new XMLParser();
		
		InputStream is = getResources().openRawResource(R.raw.catalog);
		try {
			parser.parse(is);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Collection> coll = parser.getListOfParsedCollections(); 
		
		imageURLs = new ArrayList<String>();
		imageNames = new ArrayList<String>();
		
		imageURLs.add(coll.get(0).getListOfImages().get(0).getUrl());
		imageURLs.add("http://mozartwear.com/assets/images/zima/img2.jpg");
		imageURLs.add("http://mozartwear.com/assets/images/zima/img3.jpg");
		imageURLs.add("http://mozartwear.com/assets/images/zima/img4.jpg");
		imageURLs.add("http://mozartwear.com/assets/images/zima/img5.jpg");
		imageURLs.add("http://mozartwear.com/assets/images/zima/img6.jpg");
		imageURLs.add("http://mozartwear.com/assets/images/zima/img7.jpg");
		imageURLs.add("http://mozartwear.com/assets/images/zima/img10.jpg");
		imageURLs.add("http://mozartwear.com/assets/images/zima/img11.jpg");
	}
	
	private void getImagesFromNetwork() {
		String str = null;
		for (int counter = 0; counter < imageURLs.size(); counter++) {
			str = imageURLs.get(counter);
			try {
				if (new URL(str).getContent() != null) {
					ih.loadAndSaveImageToCache(str);
				} else {
					Log.i("INFO", "Incorrect URL or file type " + str);
					// remove image name if it is not on server
					imageURLs.remove(counter);
				}
			} catch (MalformedURLException e) {
				Log.e("ERROR", "Incorrect URL " + str);
				e.printStackTrace();
			} catch (IOException e) {
				Log.e("ERROR", "Can not load image " + str);
				e.printStackTrace();
			}
		}
		// cutting images names to separate array
		int startSubString = 0;
		for (int counter = 0; counter < imageURLs.size(); counter++) {
			str = imageURLs.get(counter);
			startSubString = str.lastIndexOf("/");
			String fileName = str.substring(startSubString);
			imageNames.add(fileName);
		}
		// save images names list
		fh.saveImagesNames(imageNames);
	}
}
