package com.example.yrparser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeatherData implements Iterable<Forecast> {
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

	public void setSunrise(String sunrise) {
		this.sunrise = sunrise;
	}

	public void setSunset(String sunset) {
		this.sunset = sunset;
	}

}
