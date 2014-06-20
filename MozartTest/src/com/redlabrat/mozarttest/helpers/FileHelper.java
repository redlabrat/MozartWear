package com.redlabrat.mozarttest.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import com.redlabrat.mozarttest.CollectionActivity;
import com.redlabrat.mozarttest.R;

import static com.redlabrat.mozarttest.Constants.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

/**
 * Help to organize the process of saving image to the file on local memory card
 * @author Alexandr Brich
 * @version 1.1
 */
public class FileHelper {
	private Context mContext;
	/*** @serial Folder in which we save images*/
	private File folderForPics;
	/////////////////////////
	private File folderForCollection;
	/**
	 * Constructor
	 * @param context Context of the application
	 */
	public FileHelper(Context context, String collectionName) {
		mContext = context;
		//Must exist an external storage and the directory to save files imagesFolderName
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File extChacheDir = mContext.getExternalCacheDir();
			folderForPics = new File(extChacheDir, imagesFolderName);
			// create folder for images
			if (!folderForPics.exists()) {
				folderForPics.mkdir();
			}
			folderForCollection = new File(folderForPics, collectionName);
			if (!folderForCollection.exists()) {
				folderForCollection.mkdir();
			}
		} else {
			String errorText = mContext.getResources().getString(R.string.error_media_mount);
			Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
			Log.e("ERROR", "Media not mounted!");
		}
	}
	
	/**
	 * Load from file internalDataFileName list of images name
	 * @return list of images names
	 * @exception FileNotFoundException The internalDataFileName wasn't found
	 * @exception StreamCorruptedException There isn't an object in stream
	 * @exception IOException Couldn't read from file or Couldn't close a stream
	 * @exception ClassNotFoundException Cannot convert the object we read from file to ArrayList 
	 */
	@SuppressWarnings("unchecked") // casting from Object in file to ArrayList 
	public ArrayList<String> loadImagesNames() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		ArrayList<String> outData = null;
		try {
			fis = mContext.openFileInput(internalDataFileName);
			ois = new ObjectInputStream(fis);
			outData = (ArrayList<String>) ois.readObject();
		} catch (FileNotFoundException e) {
			Log.e("ERROR", "Error open input stream for loading images names array");
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			Log.e("ERROR", "There is no serialized objects in input stream");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("ERROR", "Can not read input stream for ArrayList");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e("ERROR", "Can not cast object to ArrayList");
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					Log.e("ERROR", "Can not close input array file");
					e.printStackTrace();
				}
			}
		}
		return outData;
	}
	
	/**
	 * Save list of images name to file internalDataFileName
	 * @param outData list of images name
	 * @exception FileNotFoundException Couldn't open a stream to save the list to file internalDataFileName
	 * @exception IOException Couldn't write to file or Couldn't close a stream 
	 */
	public void saveImagesNames(ArrayList<String> outData) {
		FileOutputStream dataStream = null;
		ObjectOutputStream oos = null;
		try {
			dataStream = mContext.openFileOutput(internalDataFileName, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(dataStream);
			oos.writeObject(outData);
		} catch (FileNotFoundException e) {
			Log.e("ERROR", "Error open out stream for saving images names array");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("ERROR", "Can not write array to file");
			e.printStackTrace();
		}
		finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					Log.e("ERROR", "Can not close output array file");
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Save the image to specific file in directory folderForPics
	 * @param image an object which contain an image we want to save to file 
	 * @param fileName Name of file to save the image 
	 * @exception FileNotFoundException Couldn't open a stream to save the image to selected file
	 * @exception IOException Cannot close a stream 
	 * @exception NullPointerException There is no image to save
	 */
	public void saveImageToChache(Bitmap image, String fileName) {
		//Must exist an external storage and the directory to save files imagesFolderName
		/*if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File extChacheDir = mContext.getExternalCacheDir();
			folderForPics = new File(extChacheDir, imagesFolderName);
			// create folder for images
			if (!folderForPics.exists()) {
				folderForPics.mkdir();
			}
			folderForCollection = new File(folderForPics, ProductViewActivity.collectionName);
			if (!folderForCollection.exists()) {
				folderForCollection.mkdir();
			}*/
			//File imageFile = new File(folderForPics, fileName);
			File imageFile = new File(folderForCollection, fileName);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(imageFile);
				//////////////////////////////////////////////
				//Scaled the image
				double imWidth = image.getWidth();
				double imHeight = image.getHeight();
				double displayWidth = CollectionActivity.w;
				double displayHeight = CollectionActivity.h;
				Log.i("BITMAP", imWidth+" x "+imHeight);//Height - высота
				Log.i("BITMAP display", displayWidth+" x "+displayHeight);//Height - высота
				double picture = 0.667;
				double device = 0.6;
				try {
					picture = imWidth/imHeight;
					device = displayWidth/displayHeight;
					//Log.i("BITMAP", "Picture="+picture+" Device="+device);//Height - высота
				}
				catch (ArithmeticException e) {
					Log.i("ERROR", "Divided by zero ");
				}
				double scale = Math.min(picture, device);
				int pixW = (int)(imWidth*scale);
				int pixH = (int)(imHeight*scale);
				Log.i("BITMAP PIX", pixW+" x "+pixH);
				Bitmap bit = Bitmap.createScaledBitmap(image, pixW, pixH, false);
				////////////////////////////////
				if (!bit.compress(CompressFormat.JPEG, 70, fos)) { //PNG 70 saves the image to file
					String errorText = mContext.getResources().getString(R.string.error_save_image);
					Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
					Log.e("ERROR", "Error while compressing bitmap!");
				}
				Log.i("BITMAP FILE", "Size : "+imageFile.length());
				///////////////////////////////////////////////
			} catch (FileNotFoundException e) {
				Log.e("ERROR", "Error open out stream for saving file");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.e("ERROR", "No image to save");
				imageFile.delete();
				e.printStackTrace();
			}
			finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						Log.e("ERROR", "Can not close image file");
						e.printStackTrace();
					}
				}
			}
		/*} else {
			String errorText = mContext.getResources().getString(R.string.error_media_mount);
			Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
			Log.e("ERROR", "Media not mounted!");
		}*/
	}
	
	/**
	 * Load the image from file in directory folderForPics to object image
	 * @param image an object which will contain an image we want to load from file 
	 * @param fileName Name of file which contains the image 
	 * @exception FileNotFoundException There is no file with such name
	 * @return true if image was loaded successfully, false if the folder or file don't exist or in any other cases
	 */
	public boolean loadImageFromCache(Bitmap image, String fileName) {
		/*if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File extChacheDir = mContext.getExternalCacheDir();
			folderForPics = new File(extChacheDir, imagesFolderName);
			if (!folderForPics.exists()) {
				return false;
			}*/
			File imageFile = new File(folderForCollection, fileName);
			Log.i("LOAD FROM CACHE", fileName+" size : "+imageFile.length());
			try {
				image = BitmapFactory.decodeStream(new FileInputStream(imageFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		/*} else {
			String errorText = mContext.getResources().getString(R.string.error_media_mount);
			Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
			Log.e("ERROR", "Media not mounted!");
			return false;
		}*/
	}
	
	/**
	 * 
	 * @param fileName Name of file which contains the image 
	 * @exception FileNotFoundException There is no file with such name
	 * @return absolute path to image
 	 */
	public String getImagePath(String fileNameURL, int r) {
		/*if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File extChacheDir = mContext.getExternalCacheDir();
			folderForPics = new File(extChacheDir, imagesFolderName);
			if (!folderForPics.exists()) {
				Log.i("INFO", "Pictures folder not exist");
				folderForPics.mkdir();
			}*/
		
			//get from URL the name of the image to save
			int startSubString = fileNameURL.lastIndexOf("/");
			String fileName = fileNameURL.substring(startSubString);
			
			//File imageFile = new File(folderForPics, fileName);
			File imageFile = new File(folderForCollection, fileName);
			if (!imageFile.exists()) {
				//if file with an image not exist, than need to download and save the image to this file
				if (r != 0)
				{
					Log.i("INFO", "Picture " + fileName + " not exist");
					/////////////////////////////
					ImageHelper ih = new ImageHelper(mContext);
					ih.setURL(fileNameURL);
					ih.start();
					if(ih.isAlive())
			        {
						try {
							ih.join(); //wait till thread is over
						} catch (InterruptedException e) {
							Log.i("THREAD", "Was interrupted!!!");
							e.printStackTrace();
						}
			        }
					Log.i("BITMAP", "WAS LOADED and saved");
					/////////////////////////////
					//new ImageHelper(mContext).loadAndSaveImageToCache(fileNameURL);
					Log.i("INFO", "Picture " + fileName + " loaded and returned");
				}
				imageFile = new File(folderForPics, fileName);
			}
			return imageFile.getAbsolutePath();
		/*} else {
			String errorText = mContext.getResources().getString(R.string.error_media_mount);
			Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
			Log.e("ERROR", "Media not mounted!");
			return null;
		}*/
	}

}
