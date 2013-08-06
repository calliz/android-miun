package com.example.robberlanguage;

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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TranslateFromActivity extends Activity {

	private EditText reTranslatedText;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translate_from);

		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

		// Context menu. Could be changed to Contextual Action Mode for
		// Android3.0 +
		reTranslatedText = (EditText) findViewById(R.id.retranslated_text);
		registerForContextMenu(reTranslatedText);
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

	/** Called when the user clicks the Retranslate button */
	public void retranslateText(View view) {
		EditText reTranslateText = (EditText) findViewById(R.id.retranslate_text);
		if (reTranslateText.getText().length() == 0) {
			Toast.makeText(this, "There is nothing to retranslate!",
					Toast.LENGTH_SHORT).show();
		} else {
			Editable input = reTranslateText.getText();
			String result = stripRobberChars(input.toString());
			reTranslatedText.append("\n" + result);
			input.clear();
		}

	}

	private String stripRobberChars(String input) {
		StringBuffer sb = new StringBuffer();
		try {
			Pattern pattern = Pattern.compile("([a-z&&[^aeiou]])(o\\1)", 2);

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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
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

	public void clearTextarea() {
		reTranslatedText.getText().clear();
	}

}
