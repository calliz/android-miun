package com.example.robberlanguage;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TranslateToActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translate_to);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.translate_to, menu);
		return true;
	}

}
