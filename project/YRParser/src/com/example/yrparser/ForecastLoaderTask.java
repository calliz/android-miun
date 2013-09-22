package com.example.yrparser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class ForecastLoaderTask extends AsyncTask<String, Void, List<Forecast>> {

	private final OnFetchingForecastsListener listener;
	private ForecastData[] forecast_data;

	public interface OnFetchingForecastsListener {
		public void onFetchSuccess(ForecastData[] forecast_data);

		public void onFetchFailure();
	}

	public ForecastLoaderTask(OnFetchingForecastsListener listener) {
		this.listener = listener;
	}

	@Override
	protected List<Forecast> doInBackground(String... forecastURLs) {
		List<Forecast> forecastList = null;
		// TODO Only one param for testing. Maybe more params later
		// Should maybe be different parsing for different tabs!!!
		try {
			forecastList = new ForecastXMLParser().parse(forecastURLs[0]);
		} catch (MalformedURLException e) {
			listener.onFetchFailure();
		} catch (XmlPullParserException e) {
			listener.onFetchFailure();
		} catch (IOException e) {
			listener.onFetchFailure();
		}

		return forecastList;
	}

	@Override
	protected void onPostExecute(List<Forecast> result) {
		forecast_data = new ForecastData[result.size()];

		for (int i = 0; i < result.size(); i++) {
			StringBuilder sb = new StringBuilder();
			Forecast fc = result.get(i);
			sb.append(fc.getDateFrom() + " - " + fc.getDateTo() + "\n"
					+ fc.getTimeFrom() + " - " + fc.getTimeTo() + "\nTemp: "
					+ fc.getTemperatureValue() + "\u00B0C\n"
					+ fc.getWindSpeedName() + " " + fc.getWindSpeedMps()
					+ " m/s\nfrom " + fc.getWindDirectionCode()
					+ "\nPrecipitation: " + fc.getPrecipitationValue() + "mm\n");

			int symbol = getSymbol(fc.getSymbolNumber(), fc.getTimePeriod());
			forecast_data[i] = new ForecastData(symbol, sb.toString());
		}

		listener.onFetchSuccess(forecast_data);
		// forecastAdapter = new ForecastAdapter(MainActivity.this,
		// R.layout.forecast_row, forecast_data);
	}

	private int getSymbol(String symbolNumber, String timePeriod) {
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
}
