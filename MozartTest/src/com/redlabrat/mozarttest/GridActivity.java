package com.redlabrat.mozarttest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class GridActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grid);
		
		int number = CollectionActivity.collectionNumber;
		GridView gridview = (GridView) findViewById(R.id.gridView1);
		//setting the number of columns showing on the screen
		double col = 0;
		if (isPortraitOrientation()) 
			col = CollectionActivity.w/160;
		else
			col = CollectionActivity.h/160;
		
		gridview.setNumColumns((int)col);
		gridview.setAdapter(new ImageAdapter(this, CollectionActivity.collections.get(number)));
		gridview.setOnItemClickListener(gridviewOnItemClickListener);
	}
	
	private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,long id) {
			Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
			i.putExtra("id", position);
			startActivity(i);
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grid, menu);
		return true;
	}
	
	private boolean isPortraitOrientation(){    
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			return true;
		else 
			return false;
	}
}
