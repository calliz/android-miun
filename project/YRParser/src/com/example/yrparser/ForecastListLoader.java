package com.example.yrparser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.AsyncTaskLoader;

public class ForecastListLoader extends AsyncTaskLoader<List<Forecast>> {
	// TODO fetch from somewhere else. e.g. preferences?
	InterestingConfigChanges lastConfig = new InterestingConfigChanges();
	private String forecastUrl;
	private List<Forecast> forecastList;

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

		for (Forecast fc : forecastList) {
			fc.generateWeatherInfo();
		}

		return forecastList;
	}

	@Override
	public void deliverResult(List<Forecast> data) {
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (data != null) {
				onReleaseResources(data);
			}
		}
		List<Forecast> oldForecasts = data;
		forecastList = data;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
		}

		// At this point we can release the resources associated with
		// 'oldForecasts' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldForecasts != null) {
			onReleaseResources(oldForecasts);
		}

	}

	@Override
	protected void onStartLoading() {
		if (forecastList != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(forecastList);
		}

		// // Start watching for changes in the app data.
		// if (mPackageObserver == null) {
		// mPackageObserver = new PackageIntentReceiver(this);
		// }

		// Has something interesting in the configuration changed since we
		// last built the app list?
		boolean configChange = lastConfig.applyNewConfig(getContext()
				.getResources());

		if (takeContentChanged() || forecastList == null || configChange) {
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
	public void onCanceled(List<Forecast> data) {
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
		if (forecastList != null) {
			onReleaseResources(forecastList);
			forecastList = null;
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
	private void onReleaseResources(List<Forecast> data) {
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
