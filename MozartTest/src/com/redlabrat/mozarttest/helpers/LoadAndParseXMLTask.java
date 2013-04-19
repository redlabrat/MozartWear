package com.redlabrat.mozarttest.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.redlabrat.mozarttest.data.Collection;

import android.os.AsyncTask;
import android.util.Log;

public class LoadAndParseXMLTask extends AsyncTask<String, Void, ArrayList<Collection>>{

	//TODO: create loading dialog to show progress
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
	}
	
	@Override
	protected ArrayList<Collection> doInBackground(String... urls) {
		
		try {
			return loadXMLFromNetwork(urls[0]);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			Log.e("ERROR", "Can not parse XML");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("ERROR", "Error in input stream");
			return null;
		}
	}

	private ArrayList<Collection> loadXMLFromNetwork(String address) throws XmlPullParserException, IOException {
		XMLParser parser = new XMLParser();
		InputStream is = null;
		try {
			is = downloadUrl(address);
			parser.parse(is);
		} 
		finally {
			if (is != null) {
				is.close();
			}
		}
		
		return parser.getListOfParsedCollections();
	}

	private InputStream downloadUrl(String address) throws IOException {
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.connect();
		return conn.getInputStream();
	}

}
