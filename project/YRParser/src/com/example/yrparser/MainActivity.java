package com.example.yrparser;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
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
	private List<Forecast> forecastList;
	private static ForecastAdapter forecastAdapter;
	private ForecastData[] forecast_data;

	private static final int NUM_TABS = 3;
	private static final String FORECAST_URL = "http://www.yr.no/sted/Sverige/Sk%C3%A5ne/Malm%C3%B6/forecast.xml";
	private int CURRENT_TAB = 0;
	private int CURRENT_FORECAST_URL;

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
			// case 1:
			// return HourByHourFragment.newInstance(position);
			// case 2:
			// return LongTermFragment.newInstance(position);
			default:
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

	public static class OverviewFragment extends SherlockListFragment implements
			LoaderManager.LoaderCallbacks<List<Forecast>> {

		private ForecastData[] forecast_data;
		private ForecastAdapter overviewAdapter;

		// private int position;

		/**
		 * Create a new instance of OverviewFragment, providing "pos" as an
		 * argument.
		 */
		static OverviewFragment newInstance(int pos) {
			OverviewFragment fragment = new OverviewFragment();

			Bundle args = new Bundle();
			args.putInt("pos", pos);
			fragment.setArguments(args);

			return fragment;
		}

		// /**
		// * The Fragment's UI is just a simple text view showing its instance
		// * number.
		// */
		// @Override
		// public View onCreateView(LayoutInflater inflater, ViewGroup
		// container,
		// Bundle savedInstanceState) {
		//
		// // data = (ForecastData[]) getArguments().getSerializable(
		// // FORECASTDATA_KEY);
		//
		// View v = inflater.inflate(R.layout.fragment_overview_list,
		// container, false);
		// View tv = v.findViewById(R.id.text);
		// ((TextView) tv).setText("Forecast Overview");
		//
		// return v;
		// }

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Initially there is no data
			setEmptyText("Testar tom lista");

			// Create an empty adapter we will use to display the loaded data.
			overviewAdapter = new ForecastAdapter(getActivity(),
					R.layout.forecast_row);
			setListAdapter(overviewAdapter);

			// Start out with a progress indicator.
			setListShown(false);

			// Prepare the loader. Either re-connect with an existing one,
			// or start a new one.
			getLoaderManager().initLoader(0, null, this);

//			new ForecastLoaderTask(this).execute(FORECAST_URL);
		}

		@Override
		public Loader<List<Forecast>> onCreateLoader(int arg0, Bundle arg1) {
			return new ForecastListLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<List<Forecast>> loader,
				List<Forecast> data) {
			overviewAdapter.setData(data);
			 // The list should now be shown.
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }

		}

		@Override
		public void onLoaderReset(Loader<List<Forecast>> arg0) {
			overviewAdapter.setData(null);
			
		}

		// @Override
		// public void onFetchSuccess(ForecastData[] data) {
		// forecast_data = data;
		// setListAdapter(new ForecastAdapter(getActivity(),
		// R.layout.forecast_row, data));
		// prog.dismiss();
		// }
		//
		// @Override
		// public void onFetchFailure() {
		// prog.dismiss();
		// }

	}

}
