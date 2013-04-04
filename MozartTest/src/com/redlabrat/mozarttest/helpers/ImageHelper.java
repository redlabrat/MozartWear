package com.redlabrat.mozarttest.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import com.redlabrat.mozarttest.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class ImageHelper {
	private Context mContext;
	private Bitmap image = null;
	
	public ImageHelper(Context context) {
		mContext = context;
	}
	
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
				int startSubString = fileURL.lastIndexOf("/");
				String fileName = fileURL.substring(startSubString);
				new FileHelper(mContext).saveImageToChache(image, fileName);
				Log.i("THREAD", "Download finished and image saved");
				image = null;
			}
		}).start();
	}
	

	private Bitmap loadImageFromNetwork(Bitmap bitmap, String imageUrl) throws URISyntaxException {
		try {
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl)
					.getContent());
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
