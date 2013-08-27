package com.example.yrparser;

import java.io.InputStream;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class ForecastXMLParser {
	private XmlPullParserFactory parserFactory;
	private XmlPullParser parser;

	private InputStream urlStream;

	private List<Forecast> forecastList;
	private Forecast forecast;

	/**
	 * XML tags
	 */
	private static final String LOCATION = "location";
	private static final String LAST_UPDATE = "lastupdate";
	private static final String NEXT_UPDATE = "nextupdate";
	private static final String SUN = "sun";
	private static final String TEXT = "text";
	private static final String TIME = "time";
	private static final String NAME = "name";
	
	private public List<Forecast> parse(String[] forecast) {
		// TODO Auto-generated method stub
		return null;
	}

}
