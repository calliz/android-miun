package com.example.yrparser;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.actionbarsherlock.app.SherlockListFragment;

public class LongTermFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<WeatherData> {

	private LongTermAdapter longTermAdapter;

	/**
	 * Create a new instance of LongTermFragment, providing "pos" as an
	 * argument.
	 */
	static LongTermFragment newInstance(int pos) {
		LongTermFragment fragment = new LongTermFragment();

		Bundle args = new Bundle();
		args.putInt("pos", pos);
		fragment.setArguments(args);
		Log.e("DEBUGGING", "LongTermFragment.newInstance()");
		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Initially there is no data
		setEmptyText("");

		// Create an empty adapter we will use to display the loaded data.
		longTermAdapter = new LongTermAdapter(getActivity(),
				R.layout.forecast_row);
		setListAdapter(longTermAdapter);

		// Start out with a progress indicator.
		setListShown(false);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.

		Bundle loaderBundle = new Bundle();
		loaderBundle
				.putString(MainActivity.FORECAST_LONGTERM_URL, MainActivity.FORECAST_LONGTERM_URL);
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
		return new ForecastListLoader(getActivity(),
				loaderBundle.getString(MainActivity.FORECAST_LONGTERM_URL));
	}

	@Override
	public void onLoadFinished(Loader<WeatherData> loader, WeatherData data) {
		longTermAdapter.setData(data);
		// The list should now be shown.
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}

	}

	@Override
	public void onLoaderReset(Loader<WeatherData> arg0) {
		longTermAdapter.setData(null);

	}

}