package com.example.yrparser;

public class Forecast {
	private String timeFrom;
	private String timeTo;
	private String timePeriod;
	private String symbolNumber;
	private String symbolName;
	private String precipitationValue;
	private String windDirectionCode;
	private String windDirectionName;
	private String windSpeedMps;
	private String windSpeedName;
	private String temperatureValue;

	public String getTimeFrom() {
		return timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public String getSymbolNumber() {
		return symbolNumber;
	}

	public String getSymbolName() {
		return symbolName;
	}

	public String getPrecipitationValue() {
		return precipitationValue;
	}

	public String getWindDirectionCode() {
		return windDirectionCode;
	}

	public String getWindDirectionName() {
		return windDirectionName;
	}

	public String getWindSpeedMps() {
		return windSpeedMps;
	}

	public String getWindSpeedName() {
		return windSpeedName;
	}

	public String getTemperatureValue() {
		return temperatureValue;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public void setSymbolNumber(String symbolNumber) {
		this.symbolNumber = symbolNumber;
	}

	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}

	public void setPrecipitationValue(String precipitationValue) {
		this.precipitationValue = precipitationValue;
	}

	public void setWindDirectionCode(String windDirectionCode) {
		this.windDirectionCode = windDirectionCode;
	}

	public void setWindDirectionName(String windDirectionName) {
		this.windDirectionName = windDirectionName;
	}

	public void setWindSpeedMps(String windSpeedMps) {
		this.windSpeedMps = windSpeedMps;
	}

	public void setWindSpeedName(String windSpeedName) {
		this.windSpeedName = windSpeedName;
	}

	public void setTemperatureValue(String temperatureValue) {
		this.temperatureValue = temperatureValue;
	}

}
