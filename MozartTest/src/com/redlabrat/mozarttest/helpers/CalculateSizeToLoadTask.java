package com.redlabrat.mozarttest.helpers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;

public class CalculateSizeToLoadTask extends AsyncTask<ArrayList<String>, Void, Integer> {

	private AsyncTaskListener resultListener;

	public CalculateSizeToLoadTask(AsyncTaskListener listener) {
		super();
		resultListener = listener;
	}
	
	@Override
	protected Integer doInBackground(ArrayList<String>... arg0) {
		int res = 0;// TODO Auto-generated method stub
		for (int counter = 0; counter < arg0[0].size(); counter++) {
			try {
				res = getImageSize(arg0[0].get(counter));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	public int getImageSize(String address) throws IOException {
		URL url = new URL(address);
//		URLConnection conn = url.openConnection();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
		conn.setRequestProperty("Accept","*/*");
		conn.setDoInput(true);
		return conn.getContentLength();
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
}
