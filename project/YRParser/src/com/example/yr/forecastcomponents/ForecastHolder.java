package com.example.yr.forecastcomponents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ForecastHolder implements Iterable<Forecast> {
	private static final String TAG = "FilterWeatherData";
	private List<Forecast> forecastList = new ArrayList<Forecast>();
	private String sunrise;
	private String sunset;

	@Override
	public Iterator<Forecast> iterator() {
		return forecastList.iterator();
	}

	public void addForecastToForecastList(Forecast forecast) {
		forecastList.add(forecast);
	}

	public String getSunrise() {
		return sunrise;
	}

	public String getSunset() {
		return sunset;
	}

	public Forecast getOverviewForecast() {
		if (forecastList.isEmpty()) {
			return null;
		}
		return forecastList.get(0);

	}

	public void setSunrise(String sunrise) {
		this.sunrise = sunrise;
	}

	public void setSunset(String sunset) {
		this.sunset = sunset;
	}

}
