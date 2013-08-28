package com.example.yrparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class ForecastXMLParser {
	private XmlPullParserFactory parserFactory;
	private XmlPullParser parser;

	private InputStream urlStream;
	private List<Forecast> forecastList;
	private Forecast forecast;

	/**
	 * XML tag constants
	 */
	private static final String LOCATION = "location";
	private static final String LAST_UPDATE = "lastupdate";
	private static final String NEXT_UPDATE = "nextupdate";
	private static final String SUN = "sun";
	private static final String TIME = "time";
	private static final String SYMBOL = "symbol";
	private static final String PRECIPITATION = "precipitation";
	private static final String WIND_DIRECTION = "windDirection";
	private static final String WIND_SPEED = "windSpeed";
	private static final String TEMPERATURE = "temperature";

	public List<Forecast> parse(String forecastURL)
			throws XmlPullParserException, MalformedURLException, IOException {

		parserFactory = XmlPullParserFactory.newInstance();
		parser = parserFactory.newPullParser();
		urlStream = downloadUrl(forecastURL);
		parser.setInput(urlStream, null);
		int eventType = parser.getEventType();
		forecastList = new ArrayList<Forecast>();
		String tagName;
		int nbrAttributes = 0;

		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = parser.getName();
			nbrAttributes = parser.getAttributeCount();

			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if (tagName.equals(LOCATION)) {
					// set location in ForecastList
				}
				if (tagName.equals(SUN)) {
					for (int i = 0; i < nbrAttributes; i++) {
						if (parser.getAttributeName(i).equals("rise")) {
							// set sunrise in ForecastList
						} else if (parser.getAttributeName(i).equals("set")) {
							// set sunset in ForecastList
						}
					}
				}
				if (tagName.equals(TIME)) {
					// create new Forecast()
					forecast = new Forecast();

					Log.d("ForecastXMLParser:TIME TAG", "Forecast created\n");

					for (int i = 0; i < nbrAttributes; i++) {
						Log.d("ForecastXMLParser:parser.getAttributeName()", parser.getAttributeName(i));
						if (parser.getAttributeName(i).equals("from")) {
							// set from time in Forecast
							Log.d("ForecastXMLParser:TIME TAG",
									"from attribute: "
											+ parser.getAttributeValue(i));
							forecast.setTimeFrom(parser.getAttributeValue(i));
						} else if (parser.getAttributeName(i).equals("to")) {
							// set to time in Forecast
							Log.d("ForecastXMLParser:TIME TAG",
									"to attribute: "
											+ parser.getAttributeValue(i));
							forecast.setTimeTo(parser.getAttributeValue(i));
						} else if (parser.getAttributeName(i).equals("period")) {
							// set period in Forecast
							Log.d("ForecastXMLParser:TIME TAG",
									"period attribute: "
											+ parser.getAttributeValue(i));
							forecast.setTimePeriod(parser.getAttributeValue(i));
						}
					}
				}
				if (tagName.equals(SYMBOL)) {
					for (int i = 0; i < nbrAttributes; i++) {
						if (parser.getAttributeName(i).equals("number")) {
							// set symbol number in Forecast
							forecast.setSymbolNumber(parser
									.getAttributeValue(i));
						} else if (parser.getAttributeName(i).equals("name")) {
							// set symbol name in Forecast
							forecast.setSymbolName(parser.getAttributeValue(i));
						}
					}
				}
				if (tagName.equals(PRECIPITATION)) {
					for (int i = 0; i < nbrAttributes; i++) {
						if (parser.getAttributeName(i).equals("value")) {
							// set prec value in Forecast
							forecast.setPrecipitationValue(parser
									.getAttributeValue(i));
						}
					}
				}
				if (tagName.equals(WIND_DIRECTION)) {
					for (int i = 0; i < nbrAttributes; i++) {
						if (parser.getAttributeName(i).equals("code")) {
							// set wind code in Forecast
							forecast.setWindDirectionCode(parser
									.getAttributeValue(i));
						} else if (parser.getAttributeName(i).equals("name")) {
							// set wind name in Forecast
							forecast.setWindDirectionName(parser
									.getAttributeValue(i));
						}
					}
				}
				if (tagName.equals(WIND_SPEED)) {
					for (int i = 0; i < nbrAttributes; i++) {
						if (parser.getAttributeName(i).equals("mps")) {
							// set velocity in in Forecast
							forecast.setWindSpeedMps(parser
									.getAttributeValue(i));
						} else if (parser.getAttributeName(i).equals("name")) {
							// set wind name in Forecast
							forecast.setWindSpeedName(parser
									.getAttributeValue(i));
						}
					}
				}
				if (tagName.equals(TEMPERATURE)) {
					for (int i = 0; i < nbrAttributes; i++) {
						if (parser.getAttributeName(i).equals("value")) {
							// settemp in in Forecast
							forecast.setTemperatureValue(parser
									.getAttributeValue(i));
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (tagName.equals(LAST_UPDATE)) {
					// set last update time in ForecastList
					// done = true;
				} else if (tagName.equals(NEXT_UPDATE)) {
					// set next update time in ForecastList
				} else if (tagName.equals(TIME)) {
					// add Forecast to ForecastList!!!
					forecastList.add(forecast);
				}
				break;
			}
			eventType = parser.next();
		}
		Log.d("ForecastXMLParser", forecastList.size() + " forecasts created\n");
		return forecastList;
	}

	private InputStream downloadUrl(String forecastURL)
			throws MalformedURLException, IOException {
		URL url = new URL(forecastURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.connect();
		InputStream stream = conn.getInputStream();
		return stream;
	}

}
