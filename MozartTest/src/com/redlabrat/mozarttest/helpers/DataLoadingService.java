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

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class DataLoadingService extends Service implements AsyncTaskListener {

	private ArrayList<Collection> collections = null;
	ArrayList<String> imagesUrls = null;
	private static ImageHelper ih = null;
	private static FileHelper fh = null;
	private IBinder binder = null;
	private int amountOfImagesToLoad;
	private int amountOfLoadedImages;
	private int notificationID = 10001;

	
	/**
	 *  1. Load XML file
	 *  2. Check for changes
	 *  3. If not changed - finish work
	 *  4. If changed - load new data
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ih = new ImageHelper(getApplicationContext());
		fh = new FileHelper(getApplicationContext());
		amountOfImagesToLoad = 0;
		amountOfLoadedImages = 0;
		
		// download XML
		// TODO: if there is no Internet connection return error
		LoadAndParseXMLTask loadXml = new LoadAndParseXMLTask(this);
		loadXml.execute(xmlUrl);

		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
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
	
	private void calculateSizeToDownload(ArrayList<Collection> coll) {
		ArrayList<ImageWithProducts> imagesArray = null;
		imagesUrls = new ArrayList<String>();
		for (int counter = 0; counter < coll.size(); counter++) {
			imagesArray = coll.get(counter).getListOfImages();
			for (int imgCounter = 0; imgCounter < imagesArray.size();
					imgCounter++, amountOfLoadedImages++) {
				imagesUrls.add(imagesArray.get(imgCounter).getUrl());
			}
		}
		CalculateSizeToLoadTask task = new CalculateSizeToLoadTask(this);
		task.execute(imagesUrls);
	}
	
	private void downloadCollections(ArrayList<Collection> coll) {
		String str = null;
		int startSubString = 0;
		String fileName = null;

		// calculating amount of Images to load
		for (int counter = 0; counter < coll.size(); counter++) {
			amountOfImagesToLoad += coll.get(counter).getAmountOfImages();
		}
		String notifyStr = createNotifyStr();
		Log.i("INFO", notifyStr);
		final Notification notification = createNotification(notifyStr);
		// Задаем начальное значение нашего Progress Bar
		//notification.contentView.setProgressBar(R.id.status_progress, amountOfImagesToLoad, amountOfLoadedImages, false);

		final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		// Посылаем уведомление нашему notification'у
		notificationManager.notify(notificationID, notification);
/*		
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
				notifyStr = createNotifyStr();
				notification.contentView.setTextViewText(R.id.text_progress, notifyStr);
				// Уведомляем об изменении
				notificationManager.notify(notificationID, notification); 
			}
		}
		notificationManager.cancel(notificationID);
*/		// save saving collection of images
		fh.saveListOfCollections(coll);
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private Notification createNotification(String str) {
		Notification notification = null;
		final Intent intent = new Intent(this, MainActivity.class);
		final PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
		// Текст и иконка в Notification area
		// R.drawable.icon, "text in notification area", System.currentTimeMillis()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			notification = new Notification.Builder(getApplicationContext())
			.setContentTitle(str)
			.setContentText(str)
			.setSmallIcon(R.drawable.ic_launcher)
			.build();
		} else {
			notification = new Notification(R.drawable.ic_launcher, str, System.currentTimeMillis());
		}
		notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
		// Связываем Notification View с нашим ресурсом
		notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
		notification.contentIntent = contentIntent;
		
		return notification;
	}
	
	private String createNotifyStr() {
		StringBuilder str = new StringBuilder();
		Resources res = getResources();
		str.append(amountOfLoadedImages)
			.append(" " + res.getString(R.string.update_status_begining))
			.append(" " + amountOfImagesToLoad)
			.append(" " + res.getString(R.string.update_status_ending));
		return str.toString();
	}

	public void onPostExecuteTask(Bundle data) {
		int taskId = data.getInt(asyncTaskId);
		switch (taskId) {
			case loadXmlAsyncTaskID: {
				ArrayList<Collection> newColl = data.getParcelableArrayList(loadedXmlResult);
				ArrayList<Collection> currColl = ((MozartApplication) getApplicationContext()).getCollectionsArray();
				// check for changes
				if (newColl.equals(currColl)) {
					Log.i("INFO", "Xml not changed");
				} else {
					Log.i("INFO", "Xml changed!");
					fh.saveListOfCollections(newColl);
					calculateSizeToDownload(newColl);
				}
				break;
			}
			case calculateSizeAsyncTaskID: {
				
				break;
			}
			case loadImagesAsyncTaskID: {
				
				stopSelf();
				break;
			}
		}
		
	}
}
