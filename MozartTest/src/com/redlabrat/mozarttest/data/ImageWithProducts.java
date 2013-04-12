package com.redlabrat.mozarttest.data;

import java.util.ArrayList;

/**
 * Class that represents description of image with its products
 * extracted from XML file
 * @author redlabrat
 *
 */
public class ImageWithProducts {

	private ArrayList<Product> listOfProducts;
	private String url;
	
	public ImageWithProducts() {
		listOfProducts = new ArrayList<Product>();
	}

	public ImageWithProducts(ArrayList<Product> listOfProducts, String url) {
		super();
		this.listOfProducts = listOfProducts;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void addProduct(Product newProduct) {
		listOfProducts.add(newProduct);
	}

	public ArrayList<Product> getListOfProducts() {
		return listOfProducts;
	}
}
