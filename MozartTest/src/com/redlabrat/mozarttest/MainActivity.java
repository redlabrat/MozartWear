package com.redlabrat.mozarttest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	private Button loadButton = null;
	private Button showButton = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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

			break;
		}
		case R.id.showCollectionButton: {
			Intent intent = new Intent(getApplicationContext(), ProductViewActivity.class);
			startActivity(intent);
			break;
		}
		}
		
	}
}
