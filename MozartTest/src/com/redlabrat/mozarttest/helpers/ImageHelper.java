package com.redlabrat.mozarttest.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import com.redlabrat.mozarttest.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
/**
 * Help to organize the process of download and save the images from Network
 * @author Alexandr Brich
 * @version 1.1
 */
public class ImageHelper {
	private Context mContext;

	/**
	 * @serial object which stores the image of the product 
	 */
	private Bitmap image = null;
	
	/**
	 * Constructor
	 * @param context Context of the application
	 */
	public ImageHelper(Context context) {
		mContext = context;
	}
	
	/**
	 * Download the image from network by address of this image
	 * @param address address where the image is situated
	 * @return image object if it was successfully download otherwise it rise an error
	 * @exception URISyntaxException Wrong URL
	 */
	public Bitmap downloadImage(final String address) {
		new Thread(new Runnable() {
			public void run() {
				Log.i("THREAD", "Download thread started");
				try {
					loadImageFromNetwork(image, address);
				} catch (URISyntaxException e) {
					String errorText = address + mContext.getResources().getString(R.string.error_404);
					Toast.makeText(mContext, errorText, Toast.LENGTH_LONG).show();
					Log.e("ERROR", address + "URL not found!");
					e.printStackTrace();
				}
				Log.i("THREAD", "Download finished and image setted");
			}
		}).start();
//		this.imageAddress = address;
//		run();
		return image;
	}

	/**
	 * Load and Save the desired image on the fileURL in thread to the object image
	 * @param fileURL URL of the image
	 * @exception URISyntaxException Wrong URL
	 */
	public void loadAndSaveImageToCache(final String fileURL) {
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			public void run() {
				Log.i("THREAD", "Download thread started");
				try {
					image = loadImageFromNetwork(image, fileURL);
				}
				// catching not existed file
				catch (URISyntaxException e) {
					image = null;
					handler.post(new Runnable() {
						
						public void run() {
							String errorText = fileURL + " " + mContext.getResources().getString(R.string.error_404);
							Toast.makeText(mContext, errorText, Toast.LENGTH_LONG).show();
							Log.e("ERROR", fileURL + " URL not found!");
						}
					});
					e.printStackTrace();
					return;
				}
				//get from URL the name of the image to save
				int startSubString = fileURL.lastIndexOf("/");
				String fileName = fileURL.substring(startSubString);
				new FileHelper(mContext).saveImageToChache(image, fileName);
				Log.i("THREAD", "Download finished and image saved");
				image = null;
			}
		}).start();
	}
	
	/**
	 * Load the image from network
	 * @param bitmap object which will contain an image
	 * @param imageUrl URL of the image
	 * @return Object which contains an downloading image
	 * @throws URISyntaxException Wrong URL
	 */
	private Bitmap loadImageFromNetwork(Bitmap bitmap, String imageUrl) throws URISyntaxException {
		try {
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
		 /*InputStream ip = (InputStream)new URL(imageUrl).getContent();
		 int av = ip.available();
		 Log.i("BITMAP", "Available - "+ av);
		 byte[] buffer = new byte[av];
		 int read = ip.read(buffer);
		 Log.i("BITMAP", "READ - "+ read);
		 
		 bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
		 
		 */
		} catch (IOException e) {
			// case when file not exist (HTTP 404 server answer)
			throw new URISyntaxException(imageUrl, "404. File not found");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
