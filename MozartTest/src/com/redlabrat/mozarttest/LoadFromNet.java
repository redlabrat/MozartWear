package com.redlabrat.mozarttest;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
//import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.redlabrat.mozarttest.Collection;
import com.redlabrat.mozarttest.CollectionActivity;
import com.redlabrat.mozarttest.Image;
import com.redlabrat.mozarttest.Product;

//import static com.redlabrat.mozarttest.Constants.*;

public class LoadFromNet extends Thread {
	//public String Catalog = "http://mozart.com.ru/catalog.xml"; //Catalog URL
	public Collection col;
	public Image im;
	public Product pr;
	public String Catalog = null;
	
	public Context mContext = null;
	public String Catatog = null;
	public String fileName = null;
	public File extChacheDir = null;
	
	public LoadFromNet(Context context, String catalogURL)
	{
		mContext = context;
		Catalog = catalogURL;
		extChacheDir = mContext.getExternalCacheDir();
        /*File folderForPics = new File(extChacheDir, imagesFolderName);
		if (!folderForPics.exists()) {
			Log.i("INFO", "Pictures folder not exist");
			folderForPics.mkdir();
		}*/
		//get from URL the name of the image to save
		int startSubString = Catalog.lastIndexOf("/");
		fileName = Catalog.substring(startSubString);
	}
	
	@Override
	public void run()
	{
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			/*File folderForPics = new File(extChacheDir, imagesFolderName);
			// create folder for images
			if (!folderForPics.exists()) {
				folderForPics.mkdir();
			}*/
			File imageFile = new File(extChacheDir, fileName);
			if (!imageFile.exists())
			{
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(imageFile);
				//set the download URL, a url that points to a file on the internet
		        //this is the file to be downloaded
		        URL url = new URL(Catalog);
		        //create the new connection
		        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

		        //set up some things on the connection
		        urlConnection.setRequestMethod("GET");
		        urlConnection.setDoOutput(true);
		        urlConnection.connect();
		        InputStream inputStream = urlConnection.getInputStream();
		        //create a buffer...
		        byte[] buffer = new byte[1024];
		        int bufferLength = 0; //used to store a temporary size of the buffer
		        System.out.println("BEFORE WHILE");
		        //now, read through the input buffer and write the contents to the file
		        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
		        	//content+=buffer.toString();
		        	System.out.println("IN WHILE ");
		        	//add the data in the buffer to the file in the file output stream (the file on the sd card
		        	fos.write(buffer, 0, bufferLength);
		        	//add up the size so we know how much is downloaded
		        	//downloadedSize += bufferLength;
		        	//this is where you would do something to report the progress, like this maybe
		        }
		        Log.i("THREAD", "File length : " + String.valueOf(imageFile.length()));
			} catch (FileNotFoundException e) {
				Log.e("ERROR", "Error open out stream for saving file");
				e.printStackTrace();
			} catch (NullPointerException e) {
				Log.e("ERROR", "No image to save");
				imageFile.delete();
				e.printStackTrace();
			} catch (MalformedURLException e) {
				Log.i("ERROR", "MalformedURL");
				e.printStackTrace();
			} catch (IOException e) {
				Log.i("ERROR", "IOEXception");
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
			}
			
			ReadXml();
		} else {
			String errorText = mContext.getResources().getString(R.string.error_media_mount);
			Log.e("ERROR", "Media not mounted! "+errorText);
		}
	}
	/*
	public  String ReadFromFile()
	{
		File SDCardRoot = Environment.getExternalStorageDirectory();
        //create a new file, specifying the path, and the filename
        //which we want to save the file as.
        File file = new File(SDCardRoot, "catalog.xml");
		//File file = new File(fileName);
        int length = (int) file.length();
        byte[] buffer = new byte[length];
        String xml = null;
        try {
			FileInputStream fi = new FileInputStream(file);
			int readBytes = fi.read(buffer);
			if (length != readBytes)
				System.out.println("Read not all bytes!");
			else
			{
				xml = new String(buffer, "UTF8");
			}
			fi.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return xml;
	}
	*/
	public void ReadXml()
	{
		Log.i("THREAD", "Read Xml :");
		try {
			File fXmlFile = new File(extChacheDir, fileName);
			//File SDCardRoot = Environment.getExternalStorageDirectory();
	        //File fXmlFile = new File(SDCardRoot, "catalog.xml");
	        if (!fXmlFile.exists()) {
	        	Log.i("THREAD", "FILE NOT EXIST");
	        	return;
	        }
	        
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			Log.i("THREAD", "Root : " + doc.getDocumentElement().getNodeName());
			
			printNote(doc.getChildNodes());
			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
	}
	
	public void printNote(NodeList nodeList) {
		for (int count = 0; count < nodeList.getLength(); count++) 
	    {
			Node tempNode = nodeList.item(count);
			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.getNodeName().contains("col"))
				{
					System.out.println("Collection " + tempNode.getAttributes().item(0).getNodeValue());
					col = new Collection();
					col.setName(tempNode.getAttributes().item(0).getNodeValue());
					//col.name = tempNode.getAttributes().item(0).getNodeValue();
					//col.images = new ArrayList<Image>();
				}
				if (tempNode.getNodeName().contains("im"))
				{
					System.out.println("Image " + tempNode.getAttributes().item(0).getNodeValue());
					im = new Image();
					im.setURL("http://mozartwear.com/" + tempNode.getAttributes().item(0).getNodeValue());
					//im.URL = "http://mozartwear.com/" + tempNode.getAttributes().item(0).getNodeValue();
					int startSubString = im.getURL().lastIndexOf("/");
					im.setName(im.getURL().substring(startSubString));
					//im.name = im.URL.substring(startSubString);
					//im.products = new ArrayList<Product>();
				}
				if (tempNode.getNodeName().contains("pr"))
				{
					System.out.println("Product " + tempNode.getAttributes().item(0).getNodeValue() + " Description : " + tempNode.getTextContent());
					pr = new Product();
					pr.setNumber(tempNode.getAttributes().item(0).getNodeValue());
					pr.setDescription(tempNode.getTextContent());
					//pr.number = tempNode.getAttributes().item(0).getNodeValue();
					//pr.description = tempNode.getTextContent();
				}
				
				if (tempNode.hasChildNodes()) {
					// loop again if has child nodes
					printNote(tempNode.getChildNodes());
				}
				//end of node
				if (tempNode.getNodeName().contains("col"))
				{
					CollectionActivity.collections.add(col);
				}
				if (tempNode.getNodeName().contains("im"))
				{
					//col.images.add(im);
					col.addImage(im);
				}
				if (tempNode.getNodeName().contains("pr"))
				{
					//im.products.add(pr);
					im.addProduct(pr);
				}		
			}
	    }
	 }

}
