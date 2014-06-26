package com.redlabrat.mozarttest;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Main Activity
 * @author Alexandr Brich
 * @version 1.1
 */
public class MainActivity extends Activity implements OnClickListener{

	private Button loadButton = null;
	private Button showButton = null;
	static LoadFromNet mAnotherOpinion;	//Побочный поток
	
	/**
	 * Create an instance of main activity and initialize it by creating buttons
	 * @param savedInstanceState previously saved state of the activity
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		loadButton = (Button) findViewById(R.id.loadDataButton);
		loadButton.setOnClickListener(this);
		showButton = (Button) findViewById(R.id.showCollectionButton);
		showButton.setOnClickListener(this);
	}

	/** Inflate the main menu
	 * @param menu items to add to action bar
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 * @return true if menu was created without a problems, false if it wasn't created or an error 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Process the button click event 
	 * and if it was a ShowCollectionButton then it open ProductView Activity
	 * @param v view of the button which was clicked
	 */
	public synchronized  void onClick(View v) {
		/*switch (v.getId()) {
		case R.id.loadDataButton: {
			// Start update service
	        
			Intent intent = new Intent(getApplicationContext(), CollectionActivity.class);
			startActivity(intent);
			break;
		}
		
		case R.id.showCollectionButton: {
			Intent intent = new Intent(getApplicationContext(), ProductViewActivity.class);
			startActivity(intent);
			break;
		}
		}*/
	}
}
