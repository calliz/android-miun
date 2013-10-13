package com.example.yrparser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class ForecastXMLParser {
	private XmlPullParserFactory parserFactory;
	private XmlPullParser parser;

	private InputStream urlStream;
	private WeatherData forecastContainer;
	private Forecast forecast;

	/**
	 * XML tag constants
	 */
	private static final String SUN = "sun";
	private static final String TIME = "time";
	private static final String SYMBOL = "symbol";
	private static final String PRECIPITATION = "precipitation";
	private static final String WIND_DIRECTION = "windDirection";
	private static final String WIND_SPEED = "windSpeed";
	private static final String TEMPERATURE = "temperature";

	public WeatherData parse(String forecastURL) throws XmlPullParserException,
			MalformedURLException, IOException {
		Log.e("DEBUGGING", "ForecastXMLParser.parse() -> started");
		parserFactory = XmlPullParserFactory.newInstance();
		parser = parserFactory.newPullParser();
		urlStream = downloadUrl(forecastURL);
		parser.setInput(urlStream, null);
		int eventType = parser.getEventType();
		forecastContainer = new WeatherData();
		String tagName;
		int nbrAttributes = 0;
		String sunrise = null;
		String sunset = null;

		while (eventType != XmlPullParser.END_DOCUMENT) {
			tagName = parser.getName();
			nbrAttributes = parser.getAttributeCount();

			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if (tagName.equals(SUN)) {
					for (int i = 0; i < nbrAttributes; i++) {
						if (parser.getAttributeName(i).equals("rise")) {
							// save sunrise
							sunrise = parser.getAttributeValue(i);
							forecastContainer.setSunrise(sunrise);
						} else if (parser.getAttributeName(i).equals("set")) {
							// save sunset
							sunset = parser.getAttributeValue(i);
							forecastContainer.setSunset(sunset);
						}
					}
				}
				if (tagName.equals(TIME)) {
					// create new Forecast()
					forecast = new Forecast();
					for (int i = 0; i < nbrAttributes; i++) {
						if (parser.getAttributeName(i).equals("from")) {
							// set from time in Forecast
							forecast.setTimeFrom(convertToTime(parser
									.getAttributeValue(i)));
							forecast.setDateFrom(convertToDate(parser
									.getAttributeValue(i)));
						} else if (parser.getAttributeName(i).equals("to")) {
							// set to time in Forecast
							forecast.setTimeTo(convertToTime(parser
									.getAttributeValue(i)));
							forecast.setDateTo(convertToDate(parser
									.getAttributeValue(i)));
						} else if (parser.getAttributeName(i).equals("period")) {
							// set period in Forecast
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
							// forecast.setWindSpeedName(Translater
							// .translateWindSpeedName(parser
							// .getAttributeValue(i)));
							forecast.setWindSpeedName(parser
									.getAttributeValue(i));
						}
					}
				}
				if (tagName.equals(TEMPERATURE)) {
					for (int i = 0; i < nbrAttributes; i++) {
						if (parser.getAttributeName(i).equals("value")) {
							// set temp in Forecast
							forecast.setTemperatureValue(parser
									.getAttributeValue(i));
						}
					}
				}
				break;
			case XmlPullParser.END_TAG:
				if (tagName.equals(TIME)) {
					// add sunset and sunrise to Forecast
					forecast.setSunrise(sunrise);
					forecast.setSunset(sunset);

					// add Forecast to ForecastList!!!
					forecastContainer.addForecastToForecastList(forecast);
				}
				break;
			}
			eventType = parser.next();
		}
		Log.e("DEBUGGING", "ForecastXMLParser.parse() -> ended");
		return forecastContainer;
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

	private String convertToDate(String dateTime) {
		Date input = null;
		try {
			input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
					.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String output = new SimpleDateFormat("yyyy-MM-dd").format(input);
		return output;
	}

	private String convertToTime(String dateTime) {
		Date input = null;
		try {
			input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
					.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String output = new SimpleDateFormat("HH:mm").format(input);
		return output;
	}

}
