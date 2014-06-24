package com.redlabrat.mozarttest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
//import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.util.Log;

import com.redlabrat.mozarttest.Collection;
import com.redlabrat.mozarttest.Image;
import com.redlabrat.mozarttest.Product;

public class LoadFromNet extends Thread {
	public Collection col;
	public Image im;
	public Product pr;
	public String Catalog = null;

	public Context mContext = null;
	public String Catatog = null;
	public String fileName = null;
	public File extChacheDir = null;

	public boolean update = false;

	public LoadFromNet(Context context, String catalogURL, boolean updateOptions) {
		mContext = context;
		Catalog = catalogURL;
		extChacheDir = mContext.getExternalCacheDir();
		update = updateOptions;
		// get from URL the name of the image to save
		int startSubString = Catalog.lastIndexOf("/");
		fileName = Catalog.substring(startSubString);
	}

	@Override
	public void run() {
		File imageFile = new File(extChacheDir, fileName);
		Log.i("GRID", "after new File(extChacheDir, fileName)"+imageFile.getAbsolutePath());
		if (!imageFile.exists() || update) // if file not exist or we choose to update everything
		{
			Log.i("GRID", "in IF");
			FileOutputStream fos = null;
			try {
				Log.i("GRID", "before new fileoutputstream");
				fos = new FileOutputStream(imageFile);
				// set the download URL, a url that points to a file on the
				// internet
				// this is the file to be downloaded
				Log.i("GRID", "after new fileoutputstream");
				URL url = new URL(Catalog);
				Log.i("GRID", "after new URL "+ Catalog);
				// create the new connection
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
				Log.i("GRID", "after url.openconnection");
				// set up some things on the connection
				urlConnection.setRequestMethod("GET");
				Log.i("GRID", "after setRequest get");
				urlConnection.setDoOutput(true);
				Log.i("GRID", "after setDoOutput true");
				urlConnection.connect();
				Log.i("GRID", "after .connectt");
				InputStream inputStream = urlConnection.getInputStream();
				// create a buffer...
				byte[] buffer = new byte[1024];
				int bufferLength = 0; // used to store a temporary size of the
										// buffer
				// now, read through the input buffer and write the contents to
				// the file
				while ((bufferLength = inputStream.read(buffer)) > 0) {
					// content+=buffer.toString();
					// add the data in the buffer to the file in the file output
					// stream (the file on the sd card
					fos.write(buffer, 0, bufferLength);
					// add up the size so we know how much is downloaded
					// downloadedSize += bufferLength;
					// this is where you would do something to report the
					// progress, like this maybe
				}
				Log.i("THREAD",
						"File length : " + String.valueOf(imageFile.length()));
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
			}  catch(UnknownHostException e) {
				Log.i("ERROR","Hostname cannot be resolved!");
				
			} catch (IOException e) {
				Log.i("ERROR", "IOEXception");
				e.printStackTrace();
			} finally {
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
	}
	
	public void ReadXml() {
		try {
			File fXmlFile = new File(extChacheDir, fileName);
			// File SDCardRoot = Environment.getExternalStorageDirectory();
			// File fXmlFile = new File(SDCardRoot, "catalog.xml");
			if (!fXmlFile.exists()) {
				Log.i("THREAD", "FILE NOT EXIST");
				return;
			}

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			printNote(doc.getChildNodes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void printNote(NodeList nodeList) {
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);
			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.getNodeName().contains("col")) {
					// System.out.println("Collection " +
					// tempNode.getAttributes().item(0).getNodeValue());
					col = new Collection();
					col.setName(tempNode.getAttributes().item(0).getNodeValue());
				}
				if (tempNode.getNodeName().contains("im")) {
					// System.out.println("Image " +
					// tempNode.getAttributes().item(0).getNodeValue());
					im = new Image();
					im.setURL("http://mozartwear.com/"
							+ tempNode.getAttributes().item(0).getNodeValue());
					int startSubString = im.getURL().lastIndexOf("/");
					im.setName(im.getURL().substring(startSubString));
				}
				if (tempNode.getNodeName().contains("pr")) {
					// System.out.println("Product " +
					// tempNode.getAttributes().item(0).getNodeValue() +
					// " Description : " + tempNode.getTextContent());
					pr = new Product();
					pr.setNumber(tempNode.getAttributes().item(0)
							.getNodeValue());
					pr.setDescription(tempNode.getTextContent());
				}

				if (tempNode.hasChildNodes()) {
					// loop again if has child nodes
					printNote(tempNode.getChildNodes());
				}
				// end of node
				if (tempNode.getNodeName().contains("col")) {
					//CollectionActivity.collections.add(col);
					if (col.getCountOfImages() != 0)
			    	{
						GridActivity.collections.add(col);
			    	}
				}
				if (tempNode.getNodeName().contains("im")) {
					col.addImage(im);
				}
				if (tempNode.getNodeName().contains("pr")) {
					im.addProduct(pr);
				}
			}
		}
	}

}
