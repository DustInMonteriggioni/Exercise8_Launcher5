package com.example.exercise8_launcher5;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
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
		
		builder.setTitle("选择启动器壁纸：");
		
		gridView = new GridView(MA);
		gridView.setNumColumns(3);
		gridView.setAdapter(new WallPaperSelectDialog.iGridAdapter(MA));
		gridView.setFocusable(false);
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
		
		@SuppressWarnings("deprecation")
		public iGridAdapter(MainActivity ma)
		{	
			MA = ma;
			
			WindowManager wm = (WindowManager)MA.getSystemService(Context.WINDOW_SERVICE);
			int screenWidth=wm.getDefaultDisplay().getWidth();		//手机屏幕的宽度
			int screenHeight=wm.getDefaultDisplay().getHeight();	//手机屏幕的高度
			iconWidth = (int)screenWidth / 4;
			iconHeight = (int)screenHeight / 4;
			
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
			
			//final int x = position;
			//Log.i("", "testing: " + position);
			
			if (convertView == null)
				convertView = new ImageView(MA);
			
			ImageView wallPaperCandidate = (ImageView)convertView;
			wallPaperCandidate.setImageResource(resourceID);
			wallPaperCandidate.setLayoutParams
				(new GridView.LayoutParams(iconWidth, iconHeight));
			
			wallPaperCandidate.setFocusable(true);
			wallPaperCandidate.setClickable(true);
			wallPaperCandidate.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View arg0)
				{
					//position == 0 can't be clicked... don't know why... T.T
					//Log.i("", "testing: " + x + "clicked~!");
					wpm.saveWallPaperPref(resourceID);
					wpm.setLauncherWallPaper(resourceID);
					dialog.dismiss();
				}
			});
			
			return wallPaperCandidate;
		}
	}
}
