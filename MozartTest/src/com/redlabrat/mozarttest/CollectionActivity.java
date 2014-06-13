package com.redlabrat.mozarttest;

import static com.redlabrat.mozarttest.Constants.imagesFolderName;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderAdapter;
import org.xmlpull.v1.XmlPullParserFactory;

import com.redlabrat.mozarttest.helpers.FileHelper;
import com.redlabrat.mozarttest.helpers.ImageHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.R.xml;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Paint.Join;
import android.view.View;
import android.view.View.OnClickListener;

public class CollectionActivity extends Activity implements OnClickListener {

	public LinearLayout lin = null;
	public Context mContext;
	public String Catalog = "http://mozart.com.ru/catalog.xml";
	public LoadFromNet load = null;
	
	public static ArrayList<Collection> collections = new ArrayList<Collection>();
	public static int collectionNumber = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//ImageHelper ih = new ImageHelper(getApplicationContext());
		//Bitmap image = ih.downloadImage("http://mozart.com.ru/assets/images/leto/img11.jpg");
		load = new LoadFromNet(getApplicationContext(), Catalog); 
		load.start();
		if(load.isAlive())
        {
			try {
				load.join(); //wait till thread is over
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		Log.i("OnCreate", "THREAD IS OVER");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		
		mContext = getApplicationContext();
		int Count = collections.size();
		Log.i("Count = ", String.valueOf(Count));
		lin = (LinearLayout)findViewById(R.id.layoutCollection);
	    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	    for(int i = 0; i < Count; i++){
	    	if (collections.get(i).images.size() != 0)
	    	{
	    		Button button = new Button(this);
	            button.setText(collections.get(i).name);
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
		// TODO Auto-generated method stub
		int id = v.getId();
		collectionNumber = id;
		Intent intent = new Intent(getApplicationContext(), ProductViewActivity.class);
		startActivity(intent);
	}
}

