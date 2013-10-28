package com.example.yr;

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
import com.example.yr.forecastcomponents.Forecast;
import com.example.yr.forecastcomponents.ForecastHolder;
import com.example.yr.parsing.ForecastListLoader;
import com.example.yr.utils.Utils;

public class OverviewFragment extends SherlockFragment implements
		LoaderManager.LoaderCallbacks<ForecastHolder> {
	private static final String TAG = "FilterOverviewFragment";

	private View rootview;
	private TextView overviewHeader;
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
		Log.d(TAG, "OverviewFragment.newInstance()");
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// setRetainInstance(true);
		rootview = inflater.inflate(R.layout.fragment_overview, container,
				false);

		overviewHeader = (TextView) rootview.findViewById(R.id.overview_header);

		overviewSymbol = (ImageView) rootview
				.findViewById(R.id.overview_symbol);
		overviewInfo = (TextView) rootview.findViewById(R.id.overview_info);

		sunriseSymbol = (ImageView) rootview.findViewById(R.id.sunrise_symbol);
		sunriseInfo = (TextView) rootview.findViewById(R.id.sunrise_info);

		sunsetSymbol = (ImageView) rootview.findViewById(R.id.sunset_symbol);
		sunsetInfo = (TextView) rootview.findViewById(R.id.sunset_info);

		return rootview;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<ForecastHolder> onCreateLoader(int id, Bundle loaderBundle) {
		return new ForecastListLoader(getActivity(),
				MainActivity.CURRENT_LOCATION_OVERVIEW_URL);
	}

	@Override
	public void onLoadFinished(Loader<ForecastHolder> loader,
			ForecastHolder data) {
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

			overviewHeader.setText("Overview for " + MainActivity.CURRENT_CITY);
			overviewSymbol.setImageResource(res);
			overviewInfo.setText(text);

			sunriseSymbol.setImageResource(R.drawable.sym_01n);
			sunriseInfo.setText(data.getSunrise());

			sunsetSymbol.setImageResource(R.drawable.sym_02n);
			sunsetInfo.setText(data.getSunset());
		}

	}

	@Override
	public void onLoaderReset(Loader<ForecastHolder> arg0) {

	}
}