package com.example.exercise8_launcher5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class DeskTop
{
	MainActivity MA;
	LinearLayout mainLayout;
	GridView gridView;	// the content of the layout
	int columnNum;
	
	public DeskTop(MainActivity ma) 
	{
		MA = ma;
		
		// get the columnNum stored by the SettingsActivity
		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(MA.getLauncherApplication());
		String colNumStr = preference.getString("desktop_column_number", "4");
		columnNum = Integer.parseInt(colNumStr);
		
		LayoutInflater li = (LayoutInflater)
				MA.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mainLayout = (LinearLayout) li.inflate(R.layout.desktop, null);
		gridView = (GridView)mainLayout.findViewById(R.id.gridview);
		gridView.setNumColumns(columnNum);
		gridView.setAdapter(new DeskTop.iGridAdapter(MA));
	}
	
	public void update()
	{	
		gridView.setAdapter(new DeskTop.iGridAdapter(MA));
	}
	
	
	
	
	class iGridAdapter extends BaseAdapter
	{	
		// width and height in the grid icon, different from that in the list
		int iconWidth;
		int iconHeight;
		AppInfoList deskTopAppList;
		MainActivity MA;
		
		@SuppressWarnings("deprecation")
		public iGridAdapter(MainActivity ma)
		{	
			MA = ma;
			
			WindowManager wm = (WindowManager)MA.getSystemService(Context.WINDOW_SERVICE);
			int screenWidth=wm.getDefaultDisplay().getWidth();	//手机屏幕的宽度
			@SuppressWarnings("unused")
			int screenHeight=wm.getDefaultDisplay().getHeight();	//手机屏幕的高度
			iconWidth = iconHeight = (int)screenWidth / columnNum;
			
			deskTopAppList = MA.getLauncherApplication().AISC.deskTopApps;
		}
		
		public int getCount() 
		{	return deskTopAppList.size();	}

		public Object getItem(int position) 
		{	return deskTopAppList.get(position);	}

		public long getItemId(int position) 
		{	return position;	}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			final AppInfo appInfo = deskTopAppList.get(position);
			ImageView appIcon;
			if (convertView == null)
				appIcon = new ImageView(MA);
			else
				appIcon = (ImageView)convertView;
			appIcon.setImageDrawable(appInfo.appIcon);
			
			// if the app is the 1st, 10th, 11th... use the big icon
			//if ((position + 1) % 10 == 0 || (position + 1) % 10 == 1)
			//{	appIcon.setLayoutParams
			//		(new GridView.LayoutParams(2 * width, 2 * height));
			//} 
			//else
			appIcon.setLayoutParams(new GridView.LayoutParams(iconWidth, iconHeight));
			// to realize the function of launching
			appIcon.setOnClickListener(new OnClickListener() 
			{	
				@Override
				public void onClick(View v) 
				{	
					String packageName = appInfo.packageName;
					PackageManager pm = MA.getPackageManager(); 
					Intent intent = pm.getLaunchIntentForPackage(packageName);
					MA.startActivity(intent); 
				}
			});
			
			// set the long click menu
			appIcon.setOnCreateContextMenuListener(new DeskTopLongClickMenu(MA, appInfo));
			
			// set OnDragListener
			appIcon.setOnDragListener(new OnDragListener()
			{
				@Override
				public boolean onDrag(View arg0, DragEvent arg1)
				{
					return true;
				}
			});
			
			return appIcon;
		}
	}

}

