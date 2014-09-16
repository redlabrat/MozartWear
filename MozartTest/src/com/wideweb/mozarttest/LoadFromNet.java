package com.wideweb.mozarttest;

import static com.wideweb.mozarttest.Constants.catalog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
//import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class LoadFromNet extends Thread {
	/*** @serial temporal collection to add in collection list*/
	public Collection col;
	/*** @serial temporal image to add in collection images list*/
	public Image im;
	/*** @serial temporal product to add in images products list*/
	public Product pr;

	/*** @serial context of the application*/
	public Context mContext = null;
	/*** @serial URL of the catalog.xml*/
	public String Catalog = null;
	/*** @serial name of the xml file to write in cache*/
	public String fileName = null;
	/*** @serial path to the external device cache folder*/
	public File extChacheDir = null;

	/*** @serial the working mode - with xml update or without*/
	public boolean update = false;
	/*** @serial indicator of the presence network connection*/
	public boolean networkAccess = false;

	/** 
	 * Set the needed parameters
	 * @param context context of the application
	 * @param updateOptions indicator of the update mode 
	 * @param netAccess indicator of the presence of network connection
	 */
	public LoadFromNet(Context context, boolean updateOptions, boolean netAccess) {
		mContext = context;
		Catalog = catalog;
		extChacheDir = getExternalCacheDir(mContext);
		if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			//Toast.makeText(mContext, "Media not mounted!", Toast.LENGTH_SHORT).show();
			Toast.makeText(mContext, R.string.error_media_mount, Toast.LENGTH_SHORT).show();
			return;
		}
		update = updateOptions;
		networkAccess = netAccess;
		// get from URL the name of the image to save
		int startSubString = Catalog.lastIndexOf("/");
		fileName = Catalog.substring(startSubString);
	}

	/** 
	 * Get the external cache directory for this application
	 * @param context context of the application
	 * @return path to the cache directory
	 */
	private static File getExternalCacheDir(final Context context) {
		//e.g. "<sdcard>/Android/data/<package_name>/cache/"
		//Build.VERSION.SDK_INT < 8
		final File extCacheDir = new File(Environment.getExternalStorageDirectory(),
		        "/Android/data/" + context.getApplicationInfo().packageName + "/cache/");
		extCacheDir.mkdirs();
	    return extCacheDir;
	}
	
	/** 
	 * If needed load the catalog.xml from network, then parsing it
	 */
	@Override
	public void run() {
		File imageFile = new File(extChacheDir, fileName);
		if (!networkAccess && !imageFile.exists()) {
			return ;
		} 
		if (!imageFile.exists() || update) // if file not exist or we choose to update everything
		{
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(imageFile);
				// set the download URL, a url that points to a file on the
				// internet - the file to be downloaded
				URL url = new URL(Catalog);
				// create the new connection
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
				// set up some things on the connection
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(true);
				urlConnection.connect();
				InputStream inputStream = urlConnection.getInputStream();
				// create a buffer...
				byte[] buffer = new byte[1024];
				int bufferLength = 0; 
				// used to store a temporary size of the buffer
				// now, read through the input buffer and write the contents to
				// the file
				while ((bufferLength = inputStream.read(buffer)) > 0) {
					// add the data in the buffer to the file in the file output
					// stream (the file on the sd card)
					fos.write(buffer, 0, bufferLength);
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
				Log.i("ERROR", "Malformed URL");
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
	
	/** 
	 * Handle the parsing of the catalog.xml
	 */
	public void ReadXml() {
		try {
			File fXmlFile = new File(extChacheDir, fileName);
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

	/** 
	 * Get the list of objects according to the catalog.xml
	 * @param nodeList the node list of the current branch of xml file
	 */
	//@SuppressLint("NewApi")
	public void printNote(NodeList nodeList) {
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);
			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				if (tempNode.getNodeName().contains("col")) {
					col = new Collection();
					col.setName(tempNode.getAttributes().item(0).getNodeValue());
				}
				if (tempNode.getNodeName().contains("im")) {
					im = new Image();
					im.setURL("http://mozartwear.com/"
							+ tempNode.getAttributes().item(0).getNodeValue());
					int startSubString = im.getURL().lastIndexOf("/");
					im.setName(im.getURL().substring(startSubString));
				}
				if (tempNode.getNodeName().contains("pr")) {
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
					if (col.getCountOfImages() != 0)
						GridActivity.collections.add(col);
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
