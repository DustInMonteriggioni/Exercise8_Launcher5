package com.example.exercise8_launcher5;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class WallPaperSelectDialog 
{
	MainActivity MA;
	WallPaperManager wpm;
	
	AlertDialog dialog;
	GridView gridView;
	
	public WallPaperSelectDialog(MainActivity ma)
	{
		MA = ma;
		wpm = MA.wpm;
		
		Builder builder = new Builder(MA);
		
		builder.setTitle("Ñ¡ÔñÆô¶¯Æ÷±ÚÖ½£º");
		
		gridView = new GridView(MA);
		gridView.setNumColumns(3);
		gridView.setAdapter(new WallPaperSelectDialog.iGridAdapter(MA));
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long ID)
			{
				int resourceID = wpm.wallPaperResourceList[position];
				wpm.saveWallPaperPref(resourceID);
				wpm.setLauncherWallPaper(resourceID);
				dialog.dismiss();
			}
		});
		
		builder.setView(gridView);
		
		dialog = builder.create();
	}
	
	public void show()
	{	
		dialog.show();
	}
	
	
	
	
	class iGridAdapter extends BaseAdapter
	{
		// width and height in the list, different from that in the grid
		int iconWidth;
		int iconHeight;
		int[] wallPaperResourceList;
		MainActivity MA;
		
		public iGridAdapter(MainActivity ma)
		{	
			MA = ma;
			iconWidth = MA.screenWidth / 4;
			iconHeight = MA.screenHeight / 4;
			wallPaperResourceList = wpm.wallPaperResourceList;
		}
		
		public int getCount() 
		{	return wallPaperResourceList.length;	}

		public Object getItem(int position) 
		{	return wallPaperResourceList[position];	}

		public long getItemId(int position) 
		{	return position;	}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			final int resourceID = wallPaperResourceList[position];
			if (convertView == null)
				convertView = new ImageView(MA);
			
			ImageView wallPaper = (ImageView)convertView;
			wallPaper.setImageResource(resourceID);
			wallPaper.setLayoutParams(new GridView.LayoutParams(iconWidth, iconHeight));
			// change wallPaper in gridView.onItemClickListener()
			//	not set onClickListener here to avoid the bug explained in
			//	DeskTop.iGridAdapter.getView()
			return wallPaper;
		}
		
	}
}
