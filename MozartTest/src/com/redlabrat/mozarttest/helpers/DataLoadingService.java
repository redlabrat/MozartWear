package com.redlabrat.mozarttest.helpers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Service for loading data from Network
 * @author Alexandr Brich
 * @version 1.1
 */
public class DataLoadingService extends Service {

	private ArrayList<String> imageURLs = null;
	private ArrayList<String> imageNames = null;
	private static ImageHelper ih = null;
	private static FileHelper fh = null;
	/*
	 *  1. Load XML file
	 *  2. Check for changes // How to check for changes ?
	 *  3. If not changed - finish work // It's mean that images is up-to-date
	 *  4. If changed - load new data //Load only new data?
	 */
	
	/** 
	 * Get the list of images URL and download the images from Network
	 * @see android.app.Service#onBind(android.content.Intent)
	 * @param arg0 intent
	 * @return null
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		ih = new ImageHelper(getApplicationContext());
		fh = new FileHelper(getApplicationContext());
		// 4. Load new data
		// get URL of images from XML
		getImagesURLs();
		// get description of products
		getImagesFromNetwork();
		
		// save products description as array in preferences
		
		return null;
	}
	
	/**
	 * Get list of images URL
	 * get URL of images from XML
	 */
	private void getImagesURLs() {
		imageURLs = new ArrayList<String>();
		imageNames = new ArrayList<String>();
		//imageURLs.add("http://mozartwear.com/assets/images/leto/img1.jpg");
	}
	
	/**
	 * Get list of images and list of images name from Network
	 * get description of products
	 * @exception MalformedURLException Incorrect URL
	 * @exception IOException Cannot load an image from Network
	 */
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

		//get from each URL name of the image by cutting all after slash
		int startSubString = 0;
		for (int counter = 0; counter < imageURLs.size(); counter++) {
			str = imageURLs.get(counter);
			startSubString = str.lastIndexOf("/");
			String fileName = str.substring(startSubString);
			imageNames.add(fileName);//add to list of image names
		}
		// save images names list to the file internalDataFileName
		fh.saveImagesNames(imageNames);
	}
}
