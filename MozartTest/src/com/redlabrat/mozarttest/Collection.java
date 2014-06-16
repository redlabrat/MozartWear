package com.redlabrat.mozarttest;

import java.util.ArrayList;

public class Collection {
	private String name;
	private ArrayList<Image> images;
	
	public Collection() {
		images = new ArrayList<Image>();
	}
	
	public void setName(String collectionName) {
		name = collectionName;
	}
	
	public void setImages(ArrayList<Image> setOfImages) {
		images = setOfImages;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Image> getImages() {
		return images;
	}
	
	public int getCountOfImages() {
		return images.size();
	}
	
	public void addImage(Image im) {
		images.add(im);
	}
}
