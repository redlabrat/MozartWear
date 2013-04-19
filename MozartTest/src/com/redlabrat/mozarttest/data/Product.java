package com.redlabrat.mozarttest.data;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * class representing one product that could be mentioned on image
 * @author redlabrat
 *
 */
public class Product implements Parcelable, Serializable{
	
	/**
	 * for Serialization
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	
	public Product(){
		
	}
	
	public Product(String mName, String mDesc) {
		name = mName;
		description = mDesc;
	}

	public Product(Parcel source) {
		name = source.readString();
		description = source.readString();
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

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(description);
	}
	
	public static final Parcelable.Creator<Product> CREATOR = new Creator<Product>() {
		
		public Product[] newArray(int size) {
			return new Product[size];
		}
		
		public Product createFromParcel(Parcel source) {
			return new Product(source);
		}
	};
}
