package com.example.yr;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yr.settings.LocationTable;
import com.example.yr.settings.MyLocationContentProvider;

public class SettingsDetailedActivity extends Activity {
	private EditText mTitleText;

	private Uri locationUri;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.location_edit);

		mTitleText = (EditText) findViewById(R.id.location_edit_summary);
		Button confirmButton = (Button) findViewById(R.id.location_edit_button);

		Bundle extras = getIntent().getExtras();

		// check from the saved Instance
		locationUri = (bundle == null) ? null : (Uri) bundle
				.getParcelable(MyLocationContentProvider.CONTENT_ITEM_TYPE);

		// Or passed from the other activity
		if (extras != null) {
			locationUri = extras
					.getParcelable(MyLocationContentProvider.CONTENT_ITEM_TYPE);

			fillData(locationUri);
		}

		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (TextUtils.isEmpty(mTitleText.getText().toString())) {
					makeToast();
				} else {
					setResult(RESULT_OK);
					finish();
				}
			}

		});
	}

	private void fillData(Uri uri) {
		String[] projection = { LocationTable.COLUMN_SUMMARY };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {
			cursor.moveToFirst();

			mTitleText.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(LocationTable.COLUMN_SUMMARY)));

			// always close the cursor
			cursor.close();
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(MyLocationContentProvider.CONTENT_ITEM_TYPE,
				locationUri);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	private void saveState() {
		String summary = mTitleText.getText().toString();

		// only save if summary
		// is available

		if (summary.length() == 0) {
			return;
		}

		ContentValues values = new ContentValues();
		values.put(LocationTable.COLUMN_SUMMARY, summary);

		if (locationUri == null) {
			// New location
			locationUri = getContentResolver().insert(
					MyLocationContentProvider.CONTENT_URI, values);
		} else {
			// Update location
			getContentResolver().update(locationUri, values, null, null);
		}
	}

	private void makeToast() {
		Toast.makeText(SettingsDetailedActivity.this,
				"Please maintain a summary", Toast.LENGTH_LONG).show();
	}
}