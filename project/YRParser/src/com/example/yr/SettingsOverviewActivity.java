package com.example.yr;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.yr.settings.LocationTable;
import com.example.yr.settings.MyLocationContentProvider;

public class SettingsOverviewActivity extends SherlockFragmentActivity
		implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final int DELETE_ID = Menu.FIRST + 1;
	private static final int USE_ID = Menu.FIRST + 2;
	private static final String TAG = "FilterSettingsOverviewActivity";
	private static final String SET_LOCATION = null;
	private SimpleCursorAdapter adapter;
	private ListView mListView;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_list);

		mListView = (ListView) findViewById(R.id.list);

		fillData();

		registerForContextMenu(mListView);
	}

	private void fillData() {
		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] { LocationTable.COLUMN_SUMMARY };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.label };

		getSupportLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.location_row, null,
				from, to, 0);

		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(SettingsOverviewActivity.this,
						SettingsDetailedActivity.class);
				Uri locationUri = Uri
						.parse(MyLocationContentProvider.CONTENT_URI + "/" + id);
				i.putExtra(MyLocationContentProvider.CONTENT_ITEM_TYPE,
						locationUri);

				startActivity(i);

			}

		});
	}

	// create the menu based on the XML defintion
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.listmenu, menu);
		return true;
	}

	// Reaction to the menu selection
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createLocation();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.delete_location);
		menu.add(0, USE_ID, 0, R.string.use_this_location);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case DELETE_ID:
			Uri uri = Uri.parse(MyLocationContentProvider.CONTENT_URI + "/"
					+ info.id);
			getContentResolver().delete(uri, null, null);
			fillData();
			return true;
		case USE_ID:
			Cursor cursor = (Cursor) adapter.getItem(info.position);
			String loc = cursor.getString(1);
			Log.d(TAG, loc);
			Intent returnIntent = new Intent();
			returnIntent.putExtra(SET_LOCATION, loc);
			setResult(RESULT_OK, returnIntent);
			finish();
		}

		return super.onContextItemSelected(item);
	}

	private void createLocation() {
		Intent i = new Intent(this, SettingsDetailedActivity.class);
		startActivity(i);
	}

	// creates a new loader after the initLoader () call
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { LocationTable.COLUMN_ID,
				LocationTable.COLUMN_SUMMARY };
		CursorLoader cursorLoader = new CursorLoader(this,
				MyLocationContentProvider.CONTENT_URI, projection, null, null,
				null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// data is not available anymore, delete reference
		adapter.swapCursor(null);
	}

}