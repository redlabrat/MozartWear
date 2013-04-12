package com.redlabrat.mozarttest.data;

/**
 * class representing one product that could be mentioned on image
 * @author redlabrat
 *
 */
public class Product {

	private String name;
	private String description;
	
	public Product(){
		
	}
	
	public Product(String mName, String mDesc) {
		name = mName;
		description = mDesc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String mDesc) {
		this.description = mDesc;
	}
	
	
}
