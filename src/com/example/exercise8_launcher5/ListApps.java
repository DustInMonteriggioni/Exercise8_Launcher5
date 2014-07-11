package com.example.exercise8_launcher5;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;


public class ListApps
{
	LinearLayout mainLayout;
	// the contents of the layout
	ImageView search, settings;
	ListView listView;
	
	MainActivity MA;
	
	public ListApps(MainActivity ma) 
	{
		MA = ma;
		LayoutInflater li = (LayoutInflater)
				MA.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mainLayout = (LinearLayout) li.inflate(R.layout.fragment_linear, null);
		
		search = (ImageView)mainLayout.findViewById(R.id.search);
		settings = (ImageView)mainLayout.findViewById(R.id.settings);
		settings.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent(MA, SettingsActivity.class);
				MA.startActivity(intent);
			}
		});
		
		listView = (ListView)mainLayout.findViewById(R.id.listview);
		listView.setAdapter(new iListAdapter(MA));
	}
	
	public void update()
	{	
		listView.setAdapter(new iListAdapter(MA));
	}

}

