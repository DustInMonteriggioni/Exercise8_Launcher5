package com.example.exercise8_launcher5;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class SettingsActivity extends Activity
{	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		// now only realize the naive version
		setContentView(R.layout.activity_settings_naive);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


}