package com.redlabrat.mozarttest.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParserException;

import static com.redlabrat.mozarttest.Constants.*;

import com.redlabrat.mozarttest.MainActivity;
import com.redlabrat.mozarttest.MozartApplication;
import com.redlabrat.mozarttest.R;
import com.redlabrat.mozarttest.data.Collection;
import com.redlabrat.mozarttest.data.ImageWithProducts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class DataLoadingService extends Service {

	private ArrayList<Collection> collections = null;
	private static ImageHelper ih = null;
	private static FileHelper fh = null;
	private IBinder binder = null;
	private int amountOfImagesToLoad;
	private int amountOfLoadedImages;

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
		amountOfImagesToLoad = 0;
		amountOfLoadedImages = 0;
		
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
		//getImagesFromXML();
		
		// save products description as array in preferences
		
		return binder;
	}
	
//	private void getImagesFromXML() {
//		XMLParser parser = new XMLParser();
//		
//		InputStream is = getResources().openRawResource(R.raw.catalog);
//		try {
//			parser.parse(is);
//			collections = parser.getListOfParsedCollections();
//		} catch (XmlPullParserException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		collections = parser.getListOfParsedCollections(); 
//		
//		downloadCollections(collections);
//		
//	}
	
	private void downloadCollections(ArrayList<Collection> coll) {
		String str = null;
		ArrayList<ImageWithProducts> imagesArray = null;
		int startSubString = 0;
		String fileName = null;
		int notificationID = 10001;

		// calculating amount of Images to load
		for (int counter = 0; counter < coll.size(); counter++) {
			amountOfImagesToLoad += coll.get(counter).getAmountOfImages();
		}
		String notifyStr = "" + amountOfLoadedImages + " from " + amountOfImagesToLoad;
		final Intent intent = new Intent(this, MainActivity.class);
		final PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
		// Текст и иконка в Notification area
		// R.drawable.icon, "text in notification area", System.currentTimeMillis()
		final Notification notification = new Notification(R.drawable.ic_launcher, notifyStr, System.currentTimeMillis());
//			.Builder(getApplicationContext())
//			.setContentText(notifyStr)
//			.setSmallIcon(R.drawable.ic_launcher)
//			.build();
		notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
		// Связываем Notification View с нашем ресурсом
		notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
		notification.contentIntent = contentIntent;
		// Задаем начальное значение нашего Progress Bar
		notification.contentView.setProgressBar(R.id.status_progress, amountOfImagesToLoad, amountOfLoadedImages, false);

		final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		// Посылаем уведомление нашему notification'у
		notificationManager.notify(notificationID, notification);
		
		for (int counter = 0; counter < coll.size(); counter++) {
			imagesArray = coll.get(counter).getListOfImages();
			for (int imgCounter = 0; imgCounter < imagesArray.size();
					imgCounter++, amountOfLoadedImages++) {
				str = imagesArray.get(imgCounter).getUrl();
				try {
					Thread th = ih.loadAndSaveImageToCache(str);
					th.join();
					// cutting image name
					startSubString = str.lastIndexOf("/");
					fileName = str.substring(startSubString);
//					} else {
//						Log.i("INFO", "Incorrect URL or file type " + str);
//						// if this url is not available set default image
//						fileName = no_imageFileName;
//						
//					}
//				} catch (MalformedURLException e) {
//					Log.e("ERROR", "Incorrect URL " + str);
//					e.printStackTrace();
//				} catch (IOException e) {
//					Log.e("ERROR", "Can not load image " + str);
//					e.printStackTrace();
				} catch (InterruptedException e) {
					Log.e("ERROR", "Service was interrupted while waiting for load image");
					e.printStackTrace();
				}
				imagesArray.get(imgCounter).setName(fileName);
				imagesArray.get(imgCounter).setFilePath(fh.getImagePath(fileName));
				notification.contentView.setProgressBar(R.id.status_progress, amountOfImagesToLoad, amountOfLoadedImages, false);
				// Задаем текст
				notifyStr = "" + amountOfLoadedImages + " from " + amountOfImagesToLoad;
				notification.contentView.setTextViewText(R.id.text_progress, notifyStr);
				// Уведомляем об изменении
				notificationManager.notify(notificationID, notification); 
			}
		}
		notificationManager.cancel(notificationID);
		// save saving collection of images
		fh.saveListOfCollections(coll);
	}
}
