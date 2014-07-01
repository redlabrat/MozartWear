package com.redlabrat.mozarttest;

import java.util.ArrayList;

/**
 * Class stores the information about dress Collection
 * @author Kate Zenevich
 * @version 1.0
 */
public class Collection {
	/*** @serial name of the collection*/
	private String name;
	/*** @serial list of images belong to the collection*/
	private ArrayList<Image> images;
	
	public Collection() {
		images = new ArrayList<Image>();
	}
	
	/** 
	 * Set the name of the dress collection
	 * @param collectionName name of the collection
	 */
	public void setName(String collectionName) {
		name = collectionName;
	}
	
	/** 
	 * Set the list of images associated with this collection
	 * @param setOfImages list of images
	 */
	public void setImages(ArrayList<Image> setOfImages) {
		images = setOfImages;
	}
	
	/** 
	 * Get the name of the collection
	 * @return name of the collection
	 */
	public String getName() {
		return name;
	}
	
	/** 
	 * Get collection images
	 * @return list of images associated with collection
	 */
	public ArrayList<Image> getImages() {
		return images;
	}
	
	/** 
	 * Get the count of images in images list
	 * @return count of images in collection
	 */
	public int getCountOfImages() {
		return images.size();
	}
	
	/** 
	 * Add the image into collection's images list
	 * @param im added image
	 */
	public void addImage(Image im) {
		images.add(im);
	}
}
