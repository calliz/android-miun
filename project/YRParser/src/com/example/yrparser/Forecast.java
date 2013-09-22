package com.example.yrparser;

import android.graphics.drawable.Drawable;

public class Forecast {
	private String label;
	private String sunrise;
	private String sunset;
	private String timeFrom;
	private String timeTo;
	private String dateFrom;
	private String dateTo;
	private String timePeriod;
	private String symbolNumber;
	private String symbolName;
	private String precipitationValue;
	private String windDirectionCode;
	private String windDirectionName;
	private String windSpeedMps;
	private String windSpeedName;
	private String temperatureValue;

	public String getSunrise() {
		return sunrise;
	}

	public String getSunset() {
		return sunset;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public String getDateTo() {
		return dateTo;
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

	public void setSunrise(String sunrise) {
		this.sunrise = sunrise;
	}

	public void setSunset(String sunset) {
		this.sunset = sunset;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
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

	public int getIcon() {
		int symbol = Integer.parseInt(symbolNumber);
		int period = Integer.parseInt(timePeriod);

		switch (symbol) {
		case 1:
			if (period > 0 && period < 3) {
				return R.drawable.sym_01d;
			} else {
				return R.drawable.sym_01n;
			}
		case 2:
			if (period > 0 && period < 3) {
				return R.drawable.sym_02d;
			} else {
				return R.drawable.sym_02n;
			}
		case 3:
			if (period > 0 && period < 3) {
				return R.drawable.sym_03d;
			} else {
				return R.drawable.sym_03n;
			}
		case 4:
			return R.drawable.sym_04;
		case 5:
			if (period > 0 && period < 3) {
				return R.drawable.sym_05d;
			} else {
				return R.drawable.sym_05n;
			}
		case 6:
			if (period > 0 && period < 3) {
				return R.drawable.sym_06d;
			} else {
				return R.drawable.sym_06n;
			}
		case 7:
			if (period > 0 && period < 3) {
				return R.drawable.sym_07d;
			} else {
				return R.drawable.sym_07n;
			}
		case 8:
			if (period > 0 && period < 3) {
				return R.drawable.sym_08d;
			} else {
				return R.drawable.sym_08n;
			}
		case 9:
			return R.drawable.sym_09;
		case 10:
			return R.drawable.sym_10;
		case 11:
			return R.drawable.sym_11;
		case 12:
			return R.drawable.sym_12;
		case 13:
			return R.drawable.sym_13;
		case 14:
			return R.drawable.sym_14;
		case 15:
			return R.drawable.sym_15;
		case 16:
			return R.drawable.sym_16;
		case 17:
			return R.drawable.sym_17;
		case 18:
			return R.drawable.sym_18;
		case 19:
			return R.drawable.sym_19;
		default:
			return R.drawable.sym_01d;
		}
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
