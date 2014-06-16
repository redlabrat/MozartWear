package com.redlabrat.mozarttest;

import java.util.ArrayList;

public class Image {
	private String name;
	private String URL;
	private ArrayList<Product> products;
	
	public Image() {
		products = new ArrayList<Product>();
	}
	
	public void setName(String imageName) {
		name = imageName;
	}
	
	public void setURL(String imageURL) {
		URL = imageURL;
	}
	
	public void setProducts(ArrayList<Product> setOfProducts) {
		products = setOfProducts;
	}
	
	public String getName() {
		return name;
	}
	
	public String getURL() {
		return URL;
	}
	
	public ArrayList<Product> getProducts() {
		return products;
	}
	
	public int getCountOfProducts() {
		return products.size();
	}
	
	public void addProduct(Product pr) {
		products.add(pr);
	}
}
