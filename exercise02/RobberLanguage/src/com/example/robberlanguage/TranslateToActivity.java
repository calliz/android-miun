package com.example.robberlanguage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.Toast;

public class TranslateToActivity extends Activity {

	private EditText translatedText;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translate_to);

		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		// Context menu. Could be changed to Contextual Action Mode for
		// Android3.0 +
		translatedText = (EditText) findViewById(R.id.translated_text);
		registerForContextMenu(translatedText);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/** Called when the user clicks the Translate button */
	public void translateText(View view) {
		EditText translateText = (EditText) findViewById(R.id.translate_text);
		if (translateText.getText().length() == 0) {
			Toast.makeText(this, "There is nothing to translate!",
					Toast.LENGTH_SHORT).show();
		} else {
			Editable input = translateText.getText();
			translatedText.append("\n" + input);
			input.clear();
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
		Toast.makeText(this, "onCreateContextMenu", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		
		Toast.makeText(this, "onContextItemSelected", Toast.LENGTH_SHORT).show();
		
		
		switch (item.getItemId()) {
		case R.id.clear:
			clearTextarea();
			Toast.makeText(this, "clearish", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void clearTextarea() {
		translatedText.getText().clear();
	}

}