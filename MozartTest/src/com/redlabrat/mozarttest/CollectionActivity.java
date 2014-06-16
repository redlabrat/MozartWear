package com.redlabrat.mozarttest;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import static com.redlabrat.mozarttest.Constants.*;

public class CollectionActivity extends Activity implements OnClickListener {
	public Context mContext;
	public LinearLayout lin = null;
	public LoadFromNet load = null;
	public static ArrayList<Collection> collections = new ArrayList<Collection>();
	public static int collectionNumber = 0;
/////////////////////////////
	public static int w;
	public static int h;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//ImageHelper ih = new ImageHelper(getApplicationContext());
		//Bitmap image = ih.downloadImage("http://mozart.com.ru/assets/images/leto/img11.jpg");
		load = new LoadFromNet(getApplicationContext(), catalog); 
		load.start();
		if(load.isAlive())
        {
			try {
				load.join(); //wait till thread is over
			} catch (InterruptedException e) {
				Log.i("THREAD", "Was interrupted!!!");
				e.printStackTrace();
			}
        }
		Log.i("OnCreate", "THREAD IS OVER");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		
		/*Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		w = display.getWidth();
		h = display.getHeight();
		*/
		mContext = getApplicationContext();
		int Count = collections.size();
		Log.i("Count = ", String.valueOf(Count));
		lin = (LinearLayout)findViewById(R.id.layoutCollection);
	    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    for(int i = 0; i < Count; i++){
	    	if (collections.get(i).getCountOfImages() != 0)
	    	{
	    		Button button = new Button(this);
	            button.setText(collections.get(i).getName());
	            button.setLayoutParams(layoutParams);
	            button.setId(i);
	            button.setOnClickListener(this);
	            lin.addView(button);
	    	}
	    }
	    //lin.setLayoutParams(layoutParams); 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.collection, menu);
		return true;
	}
	
	/**
	 * Process the button click event 
	 * @param v view of the button which was clicked
	 */
	public void onClick(View v) {
		int id = v.getId();
		collectionNumber = id;
		Intent intent = new Intent(getApplicationContext(), ProductViewActivity.class);
		startActivity(intent);
	}
}

