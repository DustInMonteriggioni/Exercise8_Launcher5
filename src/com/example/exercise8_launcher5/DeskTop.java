package com.example.exercise8_launcher5;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.GridView;
import android.widget.LinearLayout;


public class DeskTop
{
	LinearLayout mainLayout;
	GridView gridView;	// the content of the layout
	MainActivity MA;
	
	public DeskTop(MainActivity ma) 
	{
		MA = ma;
		LayoutInflater li = (LayoutInflater)
				MA.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mainLayout = (LinearLayout) li.inflate(R.layout.fragment_grid, null);
		gridView = (GridView)mainLayout.findViewById(R.id.gridview);
		gridView.setAdapter(new iGridAdapter(MA));
	}
	
	public void update()
	{	
		gridView.setAdapter(new iGridAdapter(MA));
	}

}

