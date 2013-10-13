package com.example.yrparser;

import java.io.IOException;
import java.net.MalformedURLException;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class ForecastListLoader extends AsyncTaskLoader<WeatherData> {
	// TODO fetch from somewhere else. e.g. preferences?
	InterestingConfigChanges lastConfig = new InterestingConfigChanges();
	private String forecastUrl;
	private WeatherData weatherData;

	public ForecastListLoader(Context context, String forecastUrl) {
		super(context);
		this.forecastUrl = forecastUrl;
	}

	@Override
	public WeatherData loadInBackground() {
		Log.e("DEBUGGING", "ForecastListLoader.loadInBackground()");
		WeatherData weatherData = null;
		try {
			weatherData = new ForecastXMLParser().parse(forecastUrl);
		} catch (MalformedURLException e) {
			Log.e("MalformedURLException", e.getMessage());
		} catch (XmlPullParserException e) {
			Log.e("XmlPullParserException", e.getMessage());
		} catch (IOException e) {
			Log.e("IOException", e.getMessage());
		}

		for (Forecast fc : weatherData) {
			fc.generateWeatherInfo();
		}
		return weatherData;
	}

	@Override
	public void deliverResult(WeatherData data) {
		Log.e("DEBUGGING", "ForecastListLoader.deliverResult()");
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (data != null) {
				onReleaseResources(data);
			}
		}
		WeatherData oldWeatherData = data;
		weatherData = data;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
		}

		// At this point we can release the resources associated with
		// 'oldForecasts' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldWeatherData != null) {
			onReleaseResources(oldWeatherData);
		}

	}

	@Override
	protected void onStartLoading() {
		if (weatherData != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(weatherData);
		}

		// // Start watching for changes in the app data.
		// if (mPackageObserver == null) {
		// mPackageObserver = new PackageIntentReceiver(this);
		// }

		// Has something interesting in the configuration changed since we
		// last built the app list?
		boolean configChange = lastConfig.applyNewConfig(getContext()
				.getResources());

		if (takeContentChanged() || weatherData == null || configChange) {
			// If the data has changed since the last time it was loaded
			// or is not currently available, start a load.
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	@Override
	public void onCanceled(WeatherData data) {
		super.onCanceled(data);

		// At this point we can release the resources associated with 'apps' if
		// needed
		onReleaseResources(data);
	}

	/**
	 * Handles request to completely reset the loader
	 */
	@Override
	protected void onReset() {
		super.onReset();

		// ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated with
		// 'forecastList' if needed
		if (weatherData != null) {
			onReleaseResources(weatherData);
			weatherData = null;
		}

		// // Stop monitoring for changes
		// if(packageObserver != null){
		// getContext().unregisterReceiver(packageObserver);
		// packageObserver = null;
		// }
	}

	/**
	 * Helper function to take care of releasing resources associated with an
	 * actively loaded data set.
	 */
	private void onReleaseResources(WeatherData data) {
		// For a simple List<> there is nothing to do. For something
		// like a Cursor, we would close it here.
	}

	public static class InterestingConfigChanges {
		final Configuration lastConfiguration = new Configuration();
		int lastDensity;

		protected boolean applyNewConfig(Resources res) {
			int configChanges = lastConfiguration.updateFrom(res
					.getConfiguration());
			boolean densityChanged = lastDensity != res.getDisplayMetrics().densityDpi;
			if (densityChanged
					|| (configChanges & (ActivityInfo.CONFIG_LOCALE
							| ActivityInfo.CONFIG_UI_MODE | ActivityInfo.CONFIG_SCREEN_LAYOUT)) != 0) {
				lastDensity = res.getDisplayMetrics().densityDpi;
				return true;
			}
			return false;
		}
	}

}
