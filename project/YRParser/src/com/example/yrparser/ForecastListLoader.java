package com.example.yrparser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class ForecastListLoader extends AsyncTaskLoader<List<Forecast>> {
	//TODO fetch from somewhere else. e.g. preferences?
	private String forecastUrl;

	public ForecastListLoader(Context context, String forecastUrl) {
		super(context);
		this.forecastUrl = forecastUrl;
	}

	@Override
	public List<Forecast> loadInBackground() {
		List<Forecast> forecastList = null;
			try {
				forecastList = new ForecastXMLParser().parse(forecastUrl);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			for (int i = 0; i < forecastList.size(); i++) {
				StringBuilder sb = new StringBuilder();
				Forecast fc = forecastList.get(i);
				sb.append(fc.getDateFrom() + " - " + fc.getDateTo() + "\n"
						+ fc.getTimeFrom() + " - " + fc.getTimeTo() + "\nTemp: "
						+ fc.getTemperatureValue() + "\u00B0C\n"
						+ fc.getWindSpeedName() + " " + fc.getWindSpeedMps()
						+ " m/s\nfrom " + fc.getWindDirectionCode()
						+ "\nPrecipitation: " + fc.getPrecipitationValue() + "mm\n");

				int symbol = getSymbol(fc.getSymbolNumber(), fc.getTimePeriod());
				forecast_data[i] = new ForecastData(symbol, sb.toString());
			
			
			
		return forecastList;
	}

	@Override
	public void onCanceled(List<Forecast> data) {
		// TODO Auto-generated method stub
		super.onCanceled(data);
	}

	@Override
	public void deliverResult(List<Forecast> data) {
		// TODO Auto-generated method stub
		super.deliverResult(data);
	}

	@Override
	protected void onReset() {
		// TODO Auto-generated method stub
		super.onReset();
	}

	@Override
	protected void onStartLoading() {
		// TODO Auto-generated method stub
		super.onStartLoading();
	}

	@Override
	protected void onStopLoading() {
		// TODO Auto-generated method stub
		super.onStopLoading();
	}

}
