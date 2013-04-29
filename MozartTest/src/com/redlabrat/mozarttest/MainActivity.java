package com.redlabrat.mozarttest;

import static com.redlabrat.mozarttest.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;

import com.redlabrat.mozarttest.data.Collection;
import com.redlabrat.mozarttest.helpers.DataLoadingService;
import com.redlabrat.mozarttest.helpers.FileHelper;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements OnClickListener {

	// private ArrayList<Button> collectionButtons = null;
	private SparseIntArray mapIdForCollNum = null;
	private LinearLayout buttonsLayout = null;
	private SharedPreferences prefs = null;
	private FileHelper fh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prefs = getSharedPreferences(preferencesName, MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = prefs.edit();
		fh = new FileHelper(getApplicationContext());
		ArrayList<Collection> catalog = null;
		// if it is a first start we need to copy
		// default XML catalog to internal memory
		if (!prefs.getBoolean(pref_XMLCatalogCopied, false)) {
			catalog = fh.parseInternalXml();
			fh.saveListOfCollections(catalog);
			prefsEditor.putBoolean(pref_XMLCatalogCopied, true);
			prefsEditor.apply();
		} else {
			// loading collections from file
			catalog = fh.loadListOfCollections();
			if (catalog == null) {
				Log.e("ERROR", "Can not read catalog array from file");
				catalog = new ArrayList<Collection>();
			}
		}
		// setting global variable
		((MozartApplication) getApplication()).setCollectionsArray(catalog);

		// show collections
		buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
		// collectionButtons = new ArrayList<Button>();
		mapIdForCollNum = new SparseIntArray();
		for (int counter = 0; counter < catalog.size(); counter++) {
			Button butt = new Button(getApplicationContext());
			butt.setOnClickListener(this);
			butt.setText(catalog.get(counter).getName());
			butt.setBackgroundColor(getResources().getColor(
					R.color.backgoundColor));
			butt.setTextColor(getResources().getColor(
					R.color.collectionNameColor));
			mapIdForCollNum.put(butt.getId(), counter);
			buttonsLayout.addView(butt);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		default: {
			Intent intent = new Intent(getApplicationContext(),
					CollectionViewActivity.class);
			intent.putExtra(activityCollectionNumber,
					mapIdForCollNum.get(v.getId()));
			startActivity(intent);
			break;
		}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.startLoadService: {
				// Start update service
				Intent service = new Intent(getApplicationContext(), DataLoadingService.class);
//				ServiceConnection conn = new DataLoadingServiceConnection();
//				if (bindService(service, conn, BIND_AUTO_CREATE)) {
//					Log.i("INFO", "Service successfully started!");
//				} else {
//					Log.i("INFO", "Can not bind to service");
//				}
				startService(service);
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private class DataLoadingServiceConnection implements ServiceConnection {

		public void onServiceConnected(ComponentName name, IBinder service) {

		}

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

	}
}
