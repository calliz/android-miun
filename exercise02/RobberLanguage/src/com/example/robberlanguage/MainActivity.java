package com.example.robberlanguage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

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
		startActivity(intent);
	}
	
	/** Called when the user clicks the Translate from button */
	public void translateFrom(View view) {
		Intent intent = new Intent(this, TranslateFromActivity.class);
		startActivity(intent);
	}
	
	/** Called when the user clicks the About button */
	public void about(View view) {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
	
	/** Called when the user clicks the Quit button */
	public void quit(View view) {
		finish();
	}

}
