package com.redlabrat.mozarttest.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Collection implements Serializable{
	
	/**
	 * for Serialization
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<ImageWithProducts> listOfImages;
	
	public Collection() {
		super();
		listOfImages = new ArrayList<ImageWithProducts>();
	}
	public Collection(String name, ArrayList<ImageWithProducts> listOfImages) {
		super();
		this.name = name;
		this.listOfImages = listOfImages;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void addImageToCollection(ImageWithProducts newImage) {
		listOfImages.add(newImage);
	}
	public ArrayList<ImageWithProducts> getListOfImages() {
		return listOfImages;
	}
}
