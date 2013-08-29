package com.example.yrparser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
		new ForecastLoaderTask().execute(FORECAST_URL);

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
			return ArrayListFragment.newInstance(position);
		}

		@Override
		public int getCount() {
			return NUM_TABS;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Section " + (position + 1);
		}

	}

	public static class ArrayListFragment extends SherlockListFragment {
		private int position;

		/**
		 * Create a new instance of CountingFragment, providing "position" as an
		 * argument.
		 */
		static ArrayListFragment newInstance(int pos) {
			ArrayListFragment fragment = new ArrayListFragment();

			Bundle args = new Bundle();
			args.putInt("pos", pos);
			fragment.setArguments(args);

			return fragment;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			position = getArguments() != null ? getArguments().getInt("pos")
					: 1;
		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_pager_list, container,
					false);
			View tv = v.findViewById(R.id.text);
			((TextView) tv).setText("Fragment #" + (position + 1));
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setListAdapter(forecastAdapter);
		}

	}

	public class ForecastLoaderTask extends
			AsyncTask<String, Void, List<Forecast>> {

		ProgressDialog prog;

		@Override
		protected void onPreExecute() {
			prog = new ProgressDialog(MainActivity.this);
			prog.setMessage("Loading....");
			prog.show();
		}

		@Override
		protected List<Forecast> doInBackground(String... forecastURLs) {
			// Only one param for testing. Maybe more params later
			try {
				forecastList = new ForecastXMLParser().parse(forecastURLs[0]);
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

			return forecastList;

		}

		@Override
		protected void onPostExecute(List<Forecast> result) {
			forecast_data = new ForecastData[result.size()];

			for (int i = 0; i < result.size(); i++) {
				StringBuilder sb = new StringBuilder();
				Forecast fc = result.get(i);
				sb.append(fc.getDateFrom() + " - " + fc.getDateTo() + "\n"
						+ fc.getTimeFrom() + " - " + fc.getTimeTo()
						+ "\nTemp: " + fc.getTemperatureValue() + "\u00B0C\n"
						+ fc.getWindSpeedName() + " " + fc.getWindSpeedMps()
						+ " m/s\nfra " + fc.getWindDirectionName()
						+ "\nNedbør: " + fc.getPrecipitationValue() + "mm\n");

				int symbol = getSymbol(fc.getSymbolNumber(), fc.getTimePeriod());
				forecast_data[i] = new ForecastData(symbol, sb.toString());
			}

			prog.dismiss();

			forecastAdapter = new ForecastAdapter(MainActivity.this,
					R.layout.forecast_row, forecast_data);
		}

		private int getSymbol(String symbolNumber, String timePeriod) {
			int symbol = Integer.parseInt(symbolNumber);
			int period = Integer.parseInt(timePeriod);

			switch (symbol) {
			case 1:
				if (period > 0 && period < 3) {
					return R.drawable.sym_01d;
				} else {
					return R.drawable.sym_01n;
				}
			case 2:
				if (period > 0 && period < 3) {
					return R.drawable.sym_02d;
				} else {
					return R.drawable.sym_02n;
				}
			case 3:
				if (period > 0 && period < 3) {
					return R.drawable.sym_03d;
				} else {
					return R.drawable.sym_03n;
				}
			case 4:
				return R.drawable.sym_04;
			case 5:
				if (period > 0 && period < 3) {
					return R.drawable.sym_05d;
				} else {
					return R.drawable.sym_05n;
				}
			case 6:
				if (period > 0 && period < 3) {
					return R.drawable.sym_06d;
				} else {
					return R.drawable.sym_06n;
				}
			case 7:
				if (period > 0 && period < 3) {
					return R.drawable.sym_07d;
				} else {
					return R.drawable.sym_07n;
				}
			case 8:
				if (period > 0 && period < 3) {
					return R.drawable.sym_08d;
				} else {
					return R.drawable.sym_08n;
				}
			case 9:
				return R.drawable.sym_09;
			case 10:
				return R.drawable.sym_10;
			case 11:
				return R.drawable.sym_11;
			case 12:
				return R.drawable.sym_12;
			case 13:
				return R.drawable.sym_13;
			case 14:
				return R.drawable.sym_14;
			case 15:
				return R.drawable.sym_15;
			case 16:
				return R.drawable.sym_16;
			case 17:
				return R.drawable.sym_17;
			case 18:
				return R.drawable.sym_18;
			case 19:
				return R.drawable.sym_19;
			default:
				return R.drawable.sym_01d;
			}
		}
	}

}
