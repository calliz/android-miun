package com.example.yr;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Intent;
import android.location.Location;
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
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVReader;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.yr.locationhandling.MyLocation;
import com.example.yr.locationhandling.ReverseGeocodingLoaderJSON;
import com.example.yr.locationhandling.MyLocation.LocationResult;

public class MainActivity extends SherlockFragmentActivity implements
		ActionBar.TabListener, LoaderManager.LoaderCallbacks<String> {
	private static final String TAG = "FilterMainActivity";

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

	private static final int LOCATION_REQ = 1;

	private static final String SET_LOCATION = null;

	static double CURRENT_LAT;
	static double CURRENT_LNG;
	static final String CURRENT_LOCATION_DEFAULT = "Stockholm";
	static String CURRENT_CITY;
	static String CURRENT_LOCATION_HOUR_URL;
	static String CURRENT_LOCATION_LONGTERM_URL;
	static String CURRENT_LOCATION_OVERVIEW_URL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		//
		final String savedLocation = (String) getLastCustomNonConfigurationInstance();
		if (savedLocation == null) {
			setDefaultLocation();
		}

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
			Log.d(TAG, "addTab: " + i);
		}
	}

	private void setDefaultLocation() {
		// set default location
		buildURLs(CURRENT_LOCATION_DEFAULT);

		// show default location message
		Toast.makeText(getApplicationContext(),
				"Default location is Stockholm", Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.current_location, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected id");
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.find_current_location:
			Log.d(TAG, "case R.id.find_current_location");
			fetchCurrentLocation();
			return true;
		case R.id.add_favorite_location:
			Log.d(TAG, "case R.id.add_favorite_location");
			Intent newActivity = new Intent(getApplicationContext(),
					SettingsOverviewActivity.class);
			startActivityForResult(newActivity, LOCATION_REQ);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == LOCATION_REQ) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				String city = data.getStringExtra(SET_LOCATION);
				Log.d(TAG, "Received location: " + city);
				Toast.makeText(getApplicationContext(),
						"Your Location is - " + city, Toast.LENGTH_LONG).show();
				buildURLs(city);
			}
		}
	}

	private void fetchCurrentLocation() {
		Log.d(TAG, "Fetching location");
		Toast.makeText(getApplicationContext(), "Fetching current location",
				Toast.LENGTH_LONG).show();
		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(final Location location) {
				// Got the location!
				runOnUiThread(new Runnable() {
					public void run() {
						setCurrLocation(location);
					}
				});
			}
		};

		MyLocation myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult);
	}

	protected void setCurrLocation(Location location) {
		if (location != null) {
			Log.d(TAG, "FOUND!");
			Toast.makeText(
					getApplicationContext(),
					"Latitude: " + location.getLatitude() + "\n Longitude: "
							+ location.getLongitude(), Toast.LENGTH_SHORT)
					.show();
			CURRENT_LAT = location.getLatitude();
			CURRENT_LNG = location.getLongitude();
			getSupportLoaderManager().initLoader(0, null, MainActivity.this);
		} else {
			Log.d(TAG, "NOT FOUND");
			Toast.makeText(getApplicationContext(),
					"Your Location could not be found", Toast.LENGTH_LONG)
					.show();
		}

	}

	private void buildURLs(String city) {
		CURRENT_CITY = city;
		CSVReader reader = null;
		String forecastxml = null;
		try {
			InputStream is = getResources().openRawResource(R.raw.sverige);
			reader = new CSVReader(new InputStreamReader(is), '\t');

			String[] nextLine;
			boolean done = false;
			while ((nextLine = reader.readNext()) != null && !done) {
				// nextLine[] is an array of values from the line
				if (nextLine[1].equals(city)) {
					Log.d(TAG, nextLine[1]);
					forecastxml = nextLine[17];
					done = true;
				}
			}

			if (forecastxml != null) {

				String baseUrl = forecastxml.substring(0,
						forecastxml.length() - 4);
				Log.d(TAG, baseUrl);

				CURRENT_LOCATION_HOUR_URL = baseUrl + "_hour_by_hour.xml";
				CURRENT_LOCATION_LONGTERM_URL = forecastxml;
				CURRENT_LOCATION_OVERVIEW_URL = forecastxml;

				Log.d(TAG, "CURRENT_LOCATION_HOUR_URL: "
						+ CURRENT_LOCATION_HOUR_URL);
				Log.d(TAG, "CURRENT_LOCATION_LONGTERM_URL: "
						+ CURRENT_LOCATION_LONGTERM_URL);
				Log.d(TAG, "CURRENT_LOCATION_OVERVIEW_URL: "
						+ CURRENT_LOCATION_OVERVIEW_URL);
			} else {
				Log.d(TAG, "CURRENT_LOCATION NOT FOUND!!!");
			}

		} catch (FileNotFoundException e) {
			Log.e(TAG, "FILENOTFOUND");
		} catch (IOException e) {
			Log.e(TAG, "IOEXCEPTION");
		}
		myFragmentAdapter.notifyDataSetChanged();
	}

	@Override
	public Loader<String> onCreateLoader(int id, Bundle loaderBundle) {
		Log.d(TAG, "AndroidGPSTRackingActivity.onCreateLoader()");
		return new ReverseGeocodingLoaderJSON(this, CURRENT_LAT, CURRENT_LNG);
	}

	@Override
	public void onLoadFinished(Loader<String> loader, String city) {
		if (city != null) {
			Log.d(TAG, "city fetched: " + city);

			Toast.makeText(getApplicationContext(),
					"Your Location is - " + city, Toast.LENGTH_LONG).show();
			buildURLs(city);
		} else {
			Log.e(TAG, "City is null");
			Toast.makeText(getApplicationContext(),
					"Your Location could not be found", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void onLoaderReset(Loader<String> arg0) {
		// TODO Auto-generated method stub

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
			case 0:
				Log.d(TAG, "MainActivity.getItem() -> case 0");
				return OverviewFragment.newInstance(position);
			case 1:
				Log.d(TAG, "MainActivity.getItem() -> case 1");
				return HourByHourFragment.newInstance(position);
			case 2:
				Log.d(TAG, "MainActivity.getItem() -> case 2");
				return LongTermFragment.newInstance(position);
			default:
				Log.d(TAG, "MainActivity.getItem() -> case default");
				return null;
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

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	@Override
	public String onRetainCustomNonConfigurationInstance() {
		final String savedLocation = CURRENT_CITY;
		return savedLocation;
	}
}
