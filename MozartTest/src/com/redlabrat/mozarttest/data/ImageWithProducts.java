package com.redlabrat.mozarttest.data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that represents description of image with its products
 * extracted from XML file
 * @author redlabrat
 *
 */
public class ImageWithProducts implements Parcelable{

	private ArrayList<Product> listOfProducts;
	private String url = null;
	private String name = null;
	private String filePath = null;
	
	public ImageWithProducts() {
		listOfProducts = new ArrayList<Product>();
	}

	public ImageWithProducts(ArrayList<Product> listOfProducts, String url) {
		super();
		this.listOfProducts = listOfProducts;
		this.url = url;
	}

	@SuppressWarnings("unchecked")
	public ImageWithProducts(Parcel source) {
		listOfProducts = source.readArrayList(Product.class.getClassLoader());
		url = source.readString();
		name = source.readString();
		filePath = source.readString();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void addProduct(Product newProduct) {
		listOfProducts.add(newProduct);
	}

	public ArrayList<Product> getListOfProducts() {
		return listOfProducts;
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(listOfProducts);
		dest.writeString(url);
		dest.writeString(name);
		dest.writeString(filePath);
	}
	
	public static final Parcelable.Creator<ImageWithProducts> CREATOR = new Creator<ImageWithProducts>() {
		
		public ImageWithProducts[] newArray(int size) {
			return new ImageWithProducts[size];
		}
		
		public ImageWithProducts createFromParcel(Parcel source) {
			return new ImageWithProducts(source);
		}
	};
}
