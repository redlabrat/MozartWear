package com.redlabrat.mozarttest.data;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Collection implements Parcelable, Serializable{
	
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
	public Collection(Parcel source) {
		listOfImages = new ArrayList<ImageWithProducts>();
		name = source.readString();
		source.readTypedList(listOfImages, ImageWithProducts.CREATOR);
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
	
	public int getAmountOfImages() {
		return listOfImages.size();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() == Collection.class) {
			Collection col = (Collection) o;
			if (col.name == name) {
				if (col.listOfImages.equals(listOfImages)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return super.equals(o);
		}
	}
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeTypedList(listOfImages);
	}
	
	public static final Parcelable.Creator<Collection> CREATOR = new Creator<Collection>() {
		
		public Collection[] newArray(int size) {
			return new Collection[size];
		}
		
		public Collection createFromParcel(Parcel source) {
			return new Collection(source);
		}
	};
}
