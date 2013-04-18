package com.redlabrat.mozarttest;

import java.util.ArrayList;

import com.redlabrat.mozarttest.data.Collection;

import android.app.Application;

public class MozartApplication extends Application {
	
	public ArrayList<Collection> collectionsArray = null;

	public ArrayList<Collection> getCollectionsArray() {
		return collectionsArray;
	}

	public void setCollectionsArray(ArrayList<Collection> collectionsArray) {
		this.collectionsArray = collectionsArray;
	}
	
	

}
