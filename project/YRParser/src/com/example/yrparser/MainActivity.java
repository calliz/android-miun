package com.example.yrparser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;

public class MainActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private MyFragmentAdapter myFragmentAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	private ViewPager viewPager;
	private static final int NUM_TABS = 3;
	private static final String FORECAST_HOUR_URL = "http://www.yr.no/place/Sweden/Scania/Malm%C3%B6/forecast_hour_by_hour.xml";
	private static final String FORECAST_LONGTERM_URL = "http://www.yr.no/place/Sweden/Scania/Malm%C3%B6/forecast.xml";
	private static final String FORECAST_OVERVIEW_URL = "http://www.yr.no/place/Sweden/Scania/Malm%C3%B6/forecast.xml";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(myFragmentAdapter);
		viewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < myFragmentAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(myFragmentAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		// forecastList = new ArrayList<Forecast>();

	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class MyFragmentAdapter extends FragmentStatePagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 1:
				Log.e("DEBUGGING", "MainActivity.getItem() -> case 1");
				return HourByHourFragment.newInstance(position);
			case 2:
				Log.e("DEBUGGING", "MainActivity.getItem() -> case 2");
				return LongTermFragment.newInstance(position);
			default:
				Log.e("DEBUGGING", "MainActivity.getItem() -> case 0");
				return OverviewFragment.newInstance(position);
			}
		}

		@Override
		public int getCount() {
			return NUM_TABS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String s = "";
			switch (position) {
			case 0:
				s = "Overview";
				break;
			case 1:
				s = "Hour by hour";
				break;
			case 2:
				s = "Long term";
				break;
			default:
				break;
			}
			return s;
		}
	}

	public static class HourByHourFragment extends SherlockListFragment
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
			loaderBundle.putString(FORECAST_HOUR_URL, FORECAST_HOUR_URL);
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
					loaderBundle.getString(FORECAST_HOUR_URL));
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

	public static class LongTermFragment extends SherlockListFragment implements
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
					.putString(FORECAST_LONGTERM_URL, FORECAST_LONGTERM_URL);
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
					loaderBundle.getString(FORECAST_LONGTERM_URL));
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

	// public static class TextFragment extends SherlockFragment {
	//
	// private static final String ARG_INDEX = "TextFragment.index";
	// private static final String ARG_TEXT = "TextFragment.text";
	//
	// public static TextFragment newInstance(String text, int index) {
	// TextFragment fragment = new TextFragment();
	// Bundle args = new Bundle();
	// args.putString(ARG_TEXT, text);
	// args.putInt(ARG_INDEX, index);
	// fragment.setArguments(args);
	// return fragment;
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater,
	// ViewGroup container, Bundle savedInstanceState) {
	//
	// TextView textView = new TextView(getActivity());
	// textView.setLayoutParams(new LayoutParams(
	// LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	// textView.setText(getArguments().getString(ARG_TEXT) + " "
	// + getArguments().getInt(ARG_INDEX));
	// return textView;
	// }
	//
	// }

	public static class OverviewFragment extends SherlockFragment implements
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
					.putString(FORECAST_OVERVIEW_URL, FORECAST_OVERVIEW_URL);
			getLoaderManager().initLoader(getArguments().getInt("pos"),
					loaderBundle, this);
		}

		@Override
		public Loader<WeatherData> onCreateLoader(int id, Bundle loaderBundle) {
			return new ForecastListLoader(getActivity(),
					loaderBundle.getString(FORECAST_OVERVIEW_URL));
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
}
