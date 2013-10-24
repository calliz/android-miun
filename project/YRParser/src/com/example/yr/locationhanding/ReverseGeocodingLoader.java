package com.example.yr.locationhanding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

@SuppressLint("NewApi")
public class ReverseGeocodingLoader extends AsyncTaskLoader<Address> {
	private static final String TAG = "FilterReverseGeocodingLoader";
	InterestingConfigChanges lastConfig = new InterestingConfigChanges();
	private Address address;
	private Geocoder geocoder;
	private double lat;
	private double lon;

	public ReverseGeocodingLoader(Context context, double lat, double lon) {
		super(context);
		this.lat = lat;
		this.lon = lon;
	}

	@Override
	public Address loadInBackground() {
		Address adr = null;

		if (Geocoder.isPresent()) {
			Log.d(TAG, "Geocoder is present");

			geocoder = new Geocoder(getContext(), Locale.getDefault());

			// a location might be associated with multiple addresses; so we
			// need a
			// list
			List<Address> addresses = null;

			try {
				// ask the Geocoder to give a list of address for the given
				// latitude
				// and longitude
				// 1 means max result - we need only 1

				addresses = geocoder.getFromLocation(lat, lon, 1);
				if (addresses == null) {
					Log.d(TAG, "addresses is null");
				}
				if (addresses.isEmpty()) {
					Log.d(TAG, "addresses is empty");
				}
			} catch (IOException e) {
				adr = null;
			}

			// get the first address
			if (addresses != null && addresses.size() > 0) {
				adr = addresses.get(0);
			}
		} else {
			Log.e(TAG, "Geocoder is not present");

		}
		return adr;
	}

	@Override
	public void deliverResult(Address data) {
		Log.d(TAG, "ReverseGeocodingLoader.deliverResult()");
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (data != null) {
				onReleaseResources(data);
			}
		}
		Address oldAddressData = data;
		address = data;

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
		if (address != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(address);
		}

		// // Start watching for changes in the app data.
		// if (mPackageObserver == null) {
		// mPackageObserver = new PackageIntentReceiver(this);
		// }

		// Has something interesting in the configuration changed since we
		// last built the app list?
		boolean configChange = lastConfig.applyNewConfig(getContext()
				.getResources());

		if (takeContentChanged() || address == null || configChange) {
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
	public void onCanceled(Address data) {
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
		if (address != null) {
			onReleaseResources(address);
			address = null;
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
	private void onReleaseResources(Address data) {
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
