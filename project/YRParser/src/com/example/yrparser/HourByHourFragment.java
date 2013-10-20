package com.example.yrparser;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.actionbarsherlock.app.SherlockListFragment;

public class HourByHourFragment extends SherlockListFragment
		implements LoaderManager.LoaderCallbacks<WeatherData> {

	private HourByHourAdapter hourByHourAdapter;

	/**
	 * Create a new instance of HourByHourFragment, providing "pos" as an
	 * argument.
	 */
	static HourByHourFragment newInstance(int pos) {
		HourByHourFragment fragment = new HourByHourFragment();

		Bundle args = new Bundle();
		args.putInt("pos", pos);
		fragment.setArguments(args);
		Log.e("DEBUGGING", "HourByHourFragment.newInstance()");
		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Initially there is no data
		setEmptyText("");

		// Create an empty adapter we will use to display the loaded data.
		hourByHourAdapter = new HourByHourAdapter(getActivity(),
				R.layout.forecast_row);

		// // Add list header
		// View hourByHourHeaderView = getActivity().getLayoutInflater()
		// .inflate(R.layout.hourbyhour_header, null);
		// getListView().addHeaderView(hourByHourHeaderView);

		setListAdapter(hourByHourAdapter);

		// Start out with a progress indicator.
		setListShown(false);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.

		Bundle loaderBundle = new Bundle();
		loaderBundle.putString(MainActivity.FORECAST_HOUR_URL, MainActivity.FORECAST_HOUR_URL);
		getLoaderManager().initLoader(getArguments().getInt("pos"),
				loaderBundle, this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		setListAdapter(null);
	}

	@Override
	public Loader<WeatherData> onCreateLoader(int id, Bundle loaderBundle) {
		Log.e("DEBUGGING", "HourByHourFragment.onCreateLoader()");
		return new ForecastListLoader(getActivity(),
				loaderBundle.getString(MainActivity.FORECAST_HOUR_URL));
	}

	@Override
	public void onLoadFinished(Loader<WeatherData> loader, WeatherData data) {
		hourByHourAdapter.setData(data);
		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}

	}

	@Override
	public void onLoaderReset(Loader<WeatherData> arg0) {
		hourByHourAdapter.setData(null);

	}

}