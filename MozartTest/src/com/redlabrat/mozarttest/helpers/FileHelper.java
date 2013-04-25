package com.redlabrat.mozarttest.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.redlabrat.mozarttest.MozartApplication;
import com.redlabrat.mozarttest.R;
import com.redlabrat.mozarttest.data.Collection;

import static com.redlabrat.mozarttest.Constants.*;

import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Toast;

public class FileHelper {
	private Context mContext;
	private File folderForPics;
	
	public FileHelper(Context context) {
		mContext = context;
	}
	
	@SuppressWarnings("unchecked") // casting from Object in file to ArrayList 
	public ArrayList<Collection> loadListOfCollections() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		ArrayList<Collection> outData = null;
		try {
			fis = mContext.openFileInput(internalArrayFileName);
			ois = new ObjectInputStream(fis);
			outData = (ArrayList<Collection>) ois.readObject();
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
			Log.e("ERROR", "Can not cast object to ArrayList of collections");
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
	
	public void saveListOfCollections(ArrayList<Collection> outData) {
		FileOutputStream dataStream = null;
		ObjectOutputStream oos = null;
		
		// update of global variable
		MozartApplication app = (MozartApplication) mContext.getApplicationContext();
		app.setCollectionsArray(outData);
		
		try {
			dataStream = mContext.openFileOutput(internalArrayFileName, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(dataStream);
			oos.writeObject(outData);
		} catch (FileNotFoundException e) {
			Log.e("ERROR", "Error open out stream for saving collections array");
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
	
	public void saveImageToChache(Bitmap image, String fileName) {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File extChacheDir = mContext.getExternalCacheDir();
			folderForPics = new File(extChacheDir, imagesFolderName);
			// create folder for images
			if (!folderForPics.exists()) {
				folderForPics.mkdir();
			}
			File imageFile = new File(folderForPics, fileName);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(imageFile);
				if (!image.compress(CompressFormat.PNG, 0, fos)) {
					String errorText = mContext.getResources().getString(R.string.error_save_image);
					Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
					Log.e("ERROR", "Error while compressing bitmap!");
				}
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
		} else {
			String errorText = mContext.getResources().getString(R.string.error_media_mount);
			Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
			Log.e("ERROR", "Media not mounted!");
		}
	}
	
	public boolean loadImageFromCache(Bitmap image, String fileName) {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File extChacheDir = mContext.getExternalCacheDir();
			folderForPics = new File(extChacheDir, imagesFolderName);
			if (!folderForPics.exists()) {
				return false;
			}
			File imageFile = new File(folderForPics, fileName);
			try {
				image = BitmapFactory.decodeStream(new FileInputStream(imageFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else {
			String errorText = mContext.getResources().getString(R.string.error_media_mount);
			Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
			Log.e("ERROR", "Media not mounted!");
			return false;
		}
	}
	
	public String getImagePath(String fileName) {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File extChacheDir = mContext.getExternalCacheDir();
			folderForPics = new File(extChacheDir, imagesFolderName);
			if (!folderForPics.exists()) {
				Log.i("INFO", "Pictures folder not exist");
				folderForPics.mkdir();
			}
			File imageFile = new File(folderForPics, fileName);
			if (imageFile.exists()) {
				Log.i("INFO", "Picture " + imageFile.getAbsolutePath() + " returned");
				return imageFile.getAbsolutePath();
			} else {
				Log.i("INFO", "Picture " + fileName + " not exist");
				// loading necessary file
				//new ImageHelper(mContext).loadAndSaveImageToCache("http://mozartwear.com/assets/images/zima/" + fileName);
				imageFile = new File(folderForPics, fileName);
				Log.i("INFO", "Picture " + fileName + " loaded and returned");
				return imageFile.getAbsolutePath();
			}
		} else {
			String errorText = mContext.getResources().getString(R.string.error_media_mount);
			Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT).show();
			Log.e("ERROR", "Media not mounted!");
			return null;
		}
	}
	
	public ArrayList<Collection> parseInternalXml() {
		ArrayList<Collection> catalog = null;
		XMLParser parser = new XMLParser();
		InputStream is = mContext.getResources().openRawResource(R.raw.catalog);
		try {
			parser.parse(is);
		} catch (XmlPullParserException e) {
			Log.e("ERROR", "Can not parse XML");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("ERROR", "Can not read from stream while parsing XML");
			e.printStackTrace();
		}
		catalog = parser.getListOfParsedCollections();
		return catalog;
	}
	
	/**
	 * Method user after application installation. Copies XML catalog
	 * to internal app folder
	 */
	public boolean copyXMLCatalogToInternalMemory() {
		FileOutputStream fos = null;
		InputStream is = mContext.getResources().openRawResource(R.raw.catalog);
		byte [] buffer = new byte[100];
		int bytesReaded = 0;
		try {
			fos = mContext.openFileOutput(internalXMLCatalogFileName, Context.MODE_PRIVATE);
			while ((bytesReaded = is.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesReaded);
			}
		} catch (FileNotFoundException e) {
			Log.e("ERROR", "Error open out stream for saving images names array");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.e("ERROR", "Can not write XML catalog to internal file");
			e.printStackTrace();
			return false;
		}
		finally {
			if (fos != null) {
				try {
					fos.close();
					is.close();
				} catch (IOException e) {
					Log.e("ERROR", "Can not close XML file after copy");
					e.printStackTrace();
				}
			}
		}
		return true;
	}

}
