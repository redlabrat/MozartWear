package com.redlabrat.mozarttest.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.redlabrat.mozarttest.data.Collection;
import com.redlabrat.mozarttest.data.ImageWithProducts;

import static com.redlabrat.mozarttest.Constants.*;

import android.util.Xml;

public class XMLParser {
	
	private static final String xmlNs = null;
	private ArrayList<Collection> listOfParsedCollections;
	
	public void parse(InputStream is) throws XmlPullParserException, IOException{
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
		xmlParser.require(XmlPullParser.START_TAG, xmlNs, dataTagName);
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
		parsedCollection.setName(xmlParser.getAttributeValue(xmlNs, "name"));
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
	
	private ImageWithProducts readImage(XmlPullParser xmlParser) {
		
		return null;
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
