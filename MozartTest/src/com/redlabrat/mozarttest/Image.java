package com.redlabrat.mozarttest;

import java.util.ArrayList;

/**
 * Class stores the information about image with dress
 * @author Kate Zenevich
 * @version 1.0
 */
public class Image {
	/*** @serial name of image file*/
	private String name;
	/*** @serial image address in network*/
	private String URL;
	/*** @serial list of products shown in the image*/
	private ArrayList<Product> products;
	
	public Image() {
		products = new ArrayList<Product>();
	}
	
	/** 
	 * Set the name of the image file
	 * @param imageName name of the file
	 */
	public void setName(String imageName) {
		name = imageName;
	}
	
	/** 
	 * Set the image URL
	 * @param imageURL url of the image
	 */
	public void setURL(String imageURL) {
		URL = imageURL;
	}
	
	/** 
	 * Set the list of products shown in image
	 * @param setOfProducts list of products
	 */
	public void setProducts(ArrayList<Product> setOfProducts) {
		products = setOfProducts;
	}
	
	/** 
	 * Get the image file name
	 * @return name of the file
	 */
	public String getName() {
		return name;
	}
	
	/** 
	 * Get the image url
	 * @return url
	 */
	public String getURL() {
		return URL;
	}
	
	/** 
	 * Get the list of products
	 * @return list of products
	 */
	public ArrayList<Product> getProducts() {
		return products;
	}
	
	/** 
	 * Get the count of products in products list
	 * @return count of products
	 */
	public int getCountOfProducts() {
		return products.size();
	}
	
	/** 
	 * Add the product into image's products list
	 * @param pr added product
	 */
	public void addProduct(Product pr) {
		products.add(pr);
	}
}
