package com.example.yrparser;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class OverviewFragment extends SherlockFragment implements
		LoaderManager.LoaderCallbacks<WeatherData> {

	private View rootview;
	private ImageView sunriseSymbol;
	private TextView sunriseInfo;
	private ImageView sunsetSymbol;
	private TextView sunsetInfo;
	private ImageView overviewSymbol;
	private TextView overviewInfo;

	/**
	 * Create a new instance of HourByHourFragment, providing "pos" as an
	 * argument.
	 */
	static OverviewFragment newInstance(int pos) {
		OverviewFragment fragment = new OverviewFragment();

		Bundle args = new Bundle();
		args.putInt("pos", pos);
		fragment.setArguments(args);
		Log.e("DEBUGGING", "OverviewFragment.newInstance()");
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_overview, container,
				false);

		overviewSymbol = (ImageView) rootview
				.findViewById(R.id.overview_symbol);
		overviewInfo = (TextView) rootview.findViewById(R.id.overview_info);

		sunriseSymbol = (ImageView) rootview
				.findViewById(R.id.sunrise_symbol);
		sunriseInfo = (TextView) rootview.findViewById(R.id.sunrise_info);

		sunsetSymbol = (ImageView) rootview
				.findViewById(R.id.sunset_symbol);
		sunsetInfo = (TextView) rootview.findViewById(R.id.sunset_info);

		return rootview;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// TODO implementera custom view i onCreateView enligt
		// http://stackoverflow.com/questions/15004897/custom-adapter-for-android-fragment-with-imageview-and-textview

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		Bundle loaderBundle = new Bundle();
		loaderBundle
				.putString(MainActivity.FORECAST_OVERVIEW_URL, MainActivity.FORECAST_OVERVIEW_URL);
		getLoaderManager().initLoader(getArguments().getInt("pos"),
				loaderBundle, this);
	}

	@Override
	public Loader<WeatherData> onCreateLoader(int id, Bundle loaderBundle) {
		return new ForecastListLoader(getActivity(),
				loaderBundle.getString(MainActivity.FORECAST_OVERVIEW_URL));
	}

	@Override
	public void onLoadFinished(Loader<WeatherData> loader, WeatherData data) {
		if (data != null) {
			int res;
			String text;
			Forecast overviewForecast = data.getOverviewForecast();
			if (overviewForecast != null) {
				res = Utils.getIcon(overviewForecast.getSymbolNumber(),
						overviewForecast.getTimePeriod());
				text = overviewForecast.getLabel();
			} else {
				res = R.drawable.sym_01d;
				text = "No forecast is available";
			}
			overviewSymbol.setImageResource(res);
			overviewInfo.setText(text);

			sunriseSymbol.setImageResource(R.drawable.sym_01n);
			sunriseInfo.setText(data.getSunrise());

			sunsetSymbol.setImageResource(R.drawable.sym_02n);
			sunsetInfo.setText(data.getSunset());
		}

	}

	@Override
	public void onLoaderReset(Loader<WeatherData> arg0) {

	}
}