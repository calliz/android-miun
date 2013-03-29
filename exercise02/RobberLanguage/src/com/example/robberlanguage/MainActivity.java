package com.example.robberlanguage;

import com.example.myfirstapp.DisplayMessageActivity;
import com.example.myfirstapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** Called when the user clicks the Translate to button */
	public void translateTo(View view) {
		Intent intent = new Intent(this, TranslateToActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	/** Called when the user clicks the Translate from button */
	public void translateFrom(View view) {
		Intent intent = new Intent(this, TranslateFromActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	/** Called when the user clicks the About button */
	public void about(View view) {
		Intent intent = new Intent(this, AboutActivity.class);
		EditText editText = (EditText) findViewById(R.id.edit_message);
		String message = editText.getText().toString();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
	}
	
	/** Called when the user clicks the Quit button */
	public void quit(View view) {
		//quit activity
	}

}
