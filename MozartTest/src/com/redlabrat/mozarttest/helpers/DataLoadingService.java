package com.redlabrat.mozarttest.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParserException;

import static com.redlabrat.mozarttest.Constants.*;

import com.redlabrat.mozarttest.MozartApplication;
import com.redlabrat.mozarttest.R;
import com.redlabrat.mozarttest.data.Collection;
import com.redlabrat.mozarttest.data.ImageWithProducts;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DataLoadingService extends Service {

	private ArrayList<Collection> collections = null;
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
		
		// download XML
		// if there is no Internet connection return error
		LoadAndParseXMLTask loadXml = new LoadAndParseXMLTask();
		loadXml.execute(xmlUrl_debug);
		ArrayList<Collection> newColl = null;
		ArrayList<Collection> currColl = ((MozartApplication) getApplicationContext()).getCollectionsArray();
		try {
			newColl = loadXml.get();
		} catch (InterruptedException e) {
			Log.e("ERROR", "Not loaded xml");
			e.printStackTrace();
		} catch (ExecutionException e) {
			Log.e("ERROR", "Error while loading xml");
			e.printStackTrace();
		}
		
		
		// check for changes
		if (newColl.equals(currColl)) {
			Log.i("INFO", "Xml not changed");
		} else {
			Log.i("INFO", "Xml changed!");
			fh.saveListOfCollections(newColl);
			downloadCollections(newColl);
		}
		// 4. Load new data
		// get images from XML
		getImagesFromXML();
		
		// save products description as array in preferences
		
		return binder;
	}
	
	
	
	private void getImagesFromXML() {
		XMLParser parser = new XMLParser();
		
		InputStream is = getResources().openRawResource(R.raw.catalog);
		try {
			parser.parse(is);
			collections = parser.getListOfParsedCollections();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		collections = parser.getListOfParsedCollections(); 
		
		downloadCollections(collections);
		
	}
	
	private void downloadCollections(ArrayList<Collection> coll) {
		String str = null;
		ArrayList<ImageWithProducts> imagesArray = null;
		int startSubString = 0;
		String fileName = null;

		for (int counter = 0; counter < coll.size(); counter++) {
			imagesArray = coll.get(counter).getListOfImages();
			for (int imgCounter = 0; imgCounter < imagesArray.size(); imgCounter++) {
				str = imagesArray.get(imgCounter).getUrl();
				try {
					if (new URL(str).getContent() != null) {
						ih.loadAndSaveImageToCache(str);
						
						// cutting image name
						startSubString = str.lastIndexOf("/");
						fileName = str.substring(startSubString);
					} else {
						Log.i("INFO", "Incorrect URL or file type " + str);
						// if this url is not available set default image
						fileName = no_imageFileName;
						
					}
				} catch (MalformedURLException e) {
					Log.e("ERROR", "Incorrect URL " + str);
					e.printStackTrace();
				} catch (IOException e) {
					Log.e("ERROR", "Can not load image " + str);
					e.printStackTrace();
				}
				imagesArray.get(imgCounter).setName(fileName);
			}
		}
		// save saving collection of images
		// without filePath
		fh.saveListOfCollections(coll);
	}
}
