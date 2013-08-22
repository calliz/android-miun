package com.example.robberlanguage;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TranslateActivity extends Activity {
	private EditText translate_text;
	private TextView translate_title;
	private Button translate_button;
	private TextView translated_title;
	private static int screenNbr;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translate);

		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		screenNbr = getScreenNbr();

		translate_title = (TextView) findViewById(R.id.translate_title);
		translate_text = (EditText) findViewById(R.id.translate_text);
		translate_button = (Button) findViewById(R.id.translate_button);
		translated_title = (TextView) findViewById(R.id.translated_title);

		if (screenNbr == 0) {
			translate_title.setText("Text to translate");
			translate_text.setHint("Text to be translated");
			translate_button.setText("Translate");
			translated_title.setText("Translated text");
		} else if (screenNbr == 1) {
			translate_title.setText("Text to retranslate");
			translate_text.setHint("Text to be retranslated");
			translate_button.setText("Retranslate");
			translated_title.setText("Retranslated text");
		} else {

		}

		// Context menu. Could be changed to Contextual Action Mode for
		// Android3.0 +
		translate_text = (EditText) findViewById(R.id.translated_text);
		registerForContextMenu(translate_text);
	}

	private int getScreenNbr() {
		Bundle extras = getIntent().getExtras();
		int i = -1;
		if (extras != null) {
			i = extras.getInt("EXTRA_SCREEN_NBR");
		}
		return i;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.translate, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.clear_text);
		menu.clear();
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.clear:
			clearTextarea();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/** Called when the user clicks the Translate button */
	public void translateText(View view) {
		EditText translateText = (EditText) findViewById(R.id.translate_text);
		if (translateText.getText().length() == 0) {
			Toast.makeText(this, R.string.toastMessage, Toast.LENGTH_SHORT)
					.show();
		} else {
			Editable input = translateText.getText();
			String result = null;

			if (screenNbr == 0) {
				result = appendRobberChars(input.toString());
			} else if (screenNbr == 1) {
				result = stripRobberChars(input.toString());
			} else {

			}
			translate_text.append("\n" + result);
			input.clear();
		}

	}

	private String appendRobberChars(String input) {
		Pattern pattern = Pattern.compile(getString(R.string.compileToPattern),
				2);
		Matcher matcher = pattern.matcher(input);
		StringBuffer sb = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group() + "o"
					+ matcher.group().toLowerCase(Locale.ENGLISH));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private String stripRobberChars(String input) {
		StringBuffer sb = new StringBuffer();
		try {
			Pattern pattern = Pattern.compile(
					getString(R.string.compileFromPattern), 2);

			Matcher matcher = pattern.matcher(input);

			while (matcher.find()) {
				matcher.appendReplacement(sb, matcher.group(1));
			}
			matcher.appendTail(sb);
		} catch (PatternSyntaxException e) {
			// Syntax error in regular expression
		}
		if (sb.length() == 0) {
			sb.append("No match");
		}
		return sb.toString();
	}

	public void clearTextarea() {
		translate_text.getText().clear();
	}

}
