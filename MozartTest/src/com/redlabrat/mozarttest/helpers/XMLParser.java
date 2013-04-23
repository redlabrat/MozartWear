package com.redlabrat.mozarttest.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.redlabrat.mozarttest.data.Collection;
import com.redlabrat.mozarttest.data.ImageWithProducts;
import com.redlabrat.mozarttest.data.Product;

import static com.redlabrat.mozarttest.Constants.*;

import android.util.Xml;

public class XMLParser {
	
	private static final String xmlNs = null;
	private ArrayList<Collection> listOfParsedCollections;
	
	public ArrayList<Collection> getListOfParsedCollections() {
		return listOfParsedCollections;
	}

	public void parse(InputStream is) throws XmlPullParserException, IOException{
		listOfParsedCollections = new ArrayList<Collection>();
		try {
			XmlPullParser xmlParser = Xml.newPullParser();
			xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			xmlParser.setInput(is, null);
			xmlParser.nextTag();
			readXML(xmlParser);
		} finally {
			is.close();
		}
	}
	
	private void readXML(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
		xmlParser.require(XmlPullParser.START_TAG, xmlNs, catalogTagName);
		while(xmlParser.next() != XmlPullParser.END_TAG) {
			if (xmlParser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = xmlParser.getName();
			if (name.equals( collectionTagName)) {
				listOfParsedCollections.add(readCollection(xmlParser));
			} else {
				skip(xmlParser);
			}
		}
	}
	
	private Collection readCollection(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
		Collection parsedCollection = new Collection();
		xmlParser.require(XmlPullParser.START_TAG, xmlNs, collectionTagName);
		parsedCollection.setName(xmlParser.getAttributeValue(xmlNs, xmlAttributeName));
		while(xmlParser.next() != XmlPullParser.END_TAG) {
			if (xmlParser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = xmlParser.getName();
			if (name.equals(imageTagName)) {
				parsedCollection.addImageToCollection(readImage(xmlParser));
			} else {
				skip(xmlParser);
			}
		}
		return parsedCollection;
	}
	
	private ImageWithProducts readImage(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
		ImageWithProducts parsedImage = new ImageWithProducts();
		xmlParser.require(XmlPullParser.START_TAG, xmlNs, imageTagName);
		parsedImage.setUrl(xmlParser.getAttributeValue(xmlNs, xmlAttributeUrl));
		while(xmlParser.next() != XmlPullParser.END_TAG) {
			if (xmlParser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = xmlParser.getName();
			if (name.equals(productTagName)) {
				parsedImage.addProduct(readProduct(xmlParser));
			} else {
				skip(xmlParser);
			}
		}
		
		return parsedImage;
	}

	private Product readProduct(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
		Product parsedProduct = new Product();
		xmlParser.require(XmlPullParser.START_TAG, xmlNs, productTagName);
		parsedProduct.setName(xmlParser.getAttributeValue(xmlNs, xmlAttributeName));
		parsedProduct.setDescription(readText(xmlParser));
		xmlParser.require(XmlPullParser.END_TAG, xmlNs, productTagName);
		return parsedProduct;
	}

	private String readText(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
		StringBuilder text = new StringBuilder("");
		if (xmlParser.next() == XmlPullParser.TEXT) {
			text.append(xmlParser.getText());
			xmlParser.nextTag();
		}
		// editing spaces for description
		int space = text.indexOf(" ");
		if (space != -1) {
			text.setCharAt(space, '\n');
		}
		while (space < text.length()) {
			space = text.indexOf(", ", space);
			if (space != -1) {
				text.setCharAt(++space, '\n');
			} else {
				space = text.length();
			}
		}
		
		return text.toString();
	}
	
	private void skip(XmlPullParser xmlParser) throws XmlPullParserException, IOException {
		if (xmlParser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		
		while (depth != 0) {
			switch (xmlParser.next()) {
				case XmlPullParser.END_TAG: {
					depth--;
					break;
				}
				case XmlPullParser.START_TAG: {
					depth++;
					break;
				}
			}
		}
	}
}
