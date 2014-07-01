package com.redlabrat.mozarttest;

/**
 * Class stores the information about product (clothes)
 * @author Kate Zenevich
 * @version 1.0
 */
public class Product {
	/*** @serial product unique number*/
	private String number;
	/*** @serial product description*/
	private String description;
	
	/** 
	 * Set the product number
	 * @param productNumber code of the product
	 */
	public void setNumber(String productNumber) {
		number = productNumber;
	}
	
	/** 
	 * Set the description of the product
	 * @param productDescription description
	 */
	public void setDescription(String productDescription) {
		description = productDescription;
	}
	
	/** 
	 * Get the product number
	 * @return code
	 */
	public String getNumber() {
		return number;
	}
	
	/** 
	 * Get the product's description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
}
