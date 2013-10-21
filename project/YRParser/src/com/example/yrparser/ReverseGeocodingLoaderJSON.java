package com.example.yrparser;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

@SuppressLint("NewApi")
public class ReverseGeocodingLoaderJSON extends AsyncTaskLoader<String> {
	private static final String TAG = "FilterReverseGeocodingLoaderJSON";
	InterestingConfigChanges lastConfig = new InterestingConfigChanges();
	private String city;
	private double lat;
	private double lon;

	public ReverseGeocodingLoaderJSON(Context context, double lat, double lon) {
		super(context);
		this.lat = lat;
		this.lon = lon;
	}

	@Override
	public String loadInBackground() {
		String city = getJSONAddress();

		return city;
	}

	@Override
	public void deliverResult(String data) {
		Log.d(TAG, "ReverseGeocodingLoaderJSON.deliverResult()");
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (data != null) {
				onReleaseResources(data);
			}
		}
		String oldAddressData = data;
		city = data;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(data);
		}

		// At this point we can release the resources associated with
		// 'oldAddresses' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldAddressData != null) {
			onReleaseResources(oldAddressData);
		}

	}

	@Override
	protected void onStartLoading() {
		if (city != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(city);
		}

		// // Start watching for changes in the app data.
		// if (mPackageObserver == null) {
		// mPackageObserver = new PackageIntentReceiver(this);
		// }

		// Has something interesting in the configuration changed since we
		// last built the app list?
		boolean configChange = lastConfig.applyNewConfig(getContext()
				.getResources());

		if (takeContentChanged() || city == null || configChange) {
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
	public void onCanceled(String data) {
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
		// 'addressData' if needed
		if (city != null) {
			onReleaseResources(city);
			city = null;
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
	private void onReleaseResources(String data) {
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

	private JSONObject getLocationInfo(double lat, double lng) {

		HttpGet httpGet = new HttpGet(
				"http://maps.google.com/maps/api/geocode/json?latlng=" + lat
						+ "," + lng + "&sensor=false");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
		}
		return jsonObject;
	}

	private String getJSONAddress() {
		String city = null;

		// get lat and lng value
		JSONObject locationInfo = getLocationInfo(lat, lon);
		JSONArray addressComponents;

		String location_string;
		try {
			// Get JSON Array called "results" and then get the 0th complete
			// object as JSON and then the JSON Array called
			// "address_components"
			addressComponents = locationInfo.getJSONArray("results")
					.getJSONObject(0).getJSONArray("address_components");
			JSONObject jsonObject;
			for (int i = 0; i < addressComponents.length(); i++) {
				jsonObject = addressComponents.getJSONObject(i);
				String type = jsonObject.getJSONArray("types").getString(0);
				if (type.equals("locality")) {
					city = jsonObject.getString("long_name");
					break;
				}
			}
			Log.d(TAG, "city: " + city);
		} catch (JSONException e1) {
			Log.e(TAG, "Error when fetching city");
		}
		return city;
	}
}
