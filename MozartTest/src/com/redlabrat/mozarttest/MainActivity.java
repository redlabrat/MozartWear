package com.redlabrat.mozarttest;

import static com.redlabrat.mozarttest.Constants.*;

import com.redlabrat.mozarttest.helpers.DataLoadingService;
import com.redlabrat.mozarttest.helpers.FileHelper;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	private Button loadButton = null;
	private Button showButton = null;
	private SharedPreferences prefs = null;
	private FileHelper fh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = getSharedPreferences(preferencesName, MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		fh = new FileHelper(getApplicationContext());
		// if it is a first start we need to copy 
		// default XML catalog to internal memory
		if (!prefs.getBoolean(pref_XMLCatalogCopied, false)) {
			fh.copyXMLCatalogToInternalMemory();
			prefsEditor.putBoolean(pref_XMLCatalogCopied, true);
			prefsEditor.apply();
		}
		
		loadButton = (Button) findViewById(R.id.loadDataButton);
		loadButton.setOnClickListener(this);
		showButton = (Button) findViewById(R.id.showCollectionButton);
		showButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loadDataButton: {
			// Start update service
			Intent service = new Intent(getApplicationContext(), DataLoadingService.class);
			ServiceConnection conn = new DataLoadingServiceConnection();
			if (bindService(service, conn, BIND_AUTO_CREATE)) {
				Log.i("INFO", "Service successfully started");
			} else {
				Log.i("INFO", "Can not bind to service");
			}
			break;
		}
		case R.id.showCollectionButton: {
			Intent intent = new Intent(getApplicationContext(), CollectionViewActivity.class);
			//TODO: change for appropriate collection number
			intent.putExtra(activityCollectionNumber, 0);
			startActivity(intent);
			break;
		}
		}
		
	}
	
	private class DataLoadingServiceConnection implements ServiceConnection {

		public void onServiceConnected(ComponentName name, IBinder service) {
			
		}

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
