package com.example.exercise8_launcher5;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


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
		search.setOnClickListener(new OnClickListener()
		{	
			@Override
			public void onClick(View arg0)
			{
				for (AppInfo appInfo : MA.getLauncherApplication().AISC.allApps)
					if (appInfo.appName.equals("搜索")
						|| appInfo.appName.equalsIgnoreCase("search"))
					{	launchSearchbox(appInfo.packageName);
						return;
					}
				// not found by name
				for (AppInfo appInfo : MA.getLauncherApplication().AISC.allApps)
					if (appInfo.packageName.contains("searchbox"))
					{	launchSearchbox(appInfo.packageName);
						return;
					}
			}
			
			private void launchSearchbox(String thePackageName)
			{	
				Intent intent = 
						MA.getPackageManager().getLaunchIntentForPackage(thePackageName);
				MA.startActivity(intent); 
			}
		});
		
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
		listView.setAdapter(new ListApps.iListAdapter(MA));
	}
	
	public void update()
	{	
		listView.setAdapter(new ListApps.iListAdapter(MA));
	}
	
	
	
	
	class iListAdapter extends BaseAdapter
	{
		// width and height in the list, different from that in the grid
		int iconWidth;
		int iconHeight;
		AppInfoList listApps_shown;
		MainActivity MA;
		
		@SuppressWarnings("deprecation")
		public iListAdapter(MainActivity ma)
		{	
			MA = ma;
			
			WindowManager wm = (WindowManager)MA.getSystemService(Context.WINDOW_SERVICE);
			@SuppressWarnings("unused")
			int screenWidth=wm.getDefaultDisplay().getWidth();	//手机屏幕的宽度
			int screenHeight=wm.getDefaultDisplay().getHeight();	//手机屏幕的高度
			iconWidth = iconHeight = (int)screenHeight / 10;
			
			listApps_shown = MA.getLauncherApplication().AISC.listApps;
		}
		
		public int getCount() 
		{	return listApps_shown.size();	}

		public Object getItem(int position) 
		{	return listApps_shown.get(position);	}

		public long getItemId(int position) 
		{	return position;	}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			final AppInfo appInfo = listApps_shown.get(position);
			// at start, the app_row in the position is empty, thus convertView == null
			if (convertView == null) // entered when the row is created
			{	LayoutInflater vi = (LayoutInflater)MA.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.app_row, null);
				convertView.setClickable(true);
			}
			
			// set the icon and the appName of the appRow
			Drawable icon = appInfo.appIcon;
			// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
			//icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
			icon.setBounds(0, 0, iconWidth, iconHeight);
			TextView appRow = (TextView)convertView.findViewById(R.id.appInfo); 
			appRow.setText(appInfo.appName);
			appRow.setCompoundDrawables(icon, null, null, null); //设置左图标
			appRow.setOnClickListener(new OnClickListener() 
			{	
				public void onClick(View v) 
				{	
					String packageName = appInfo.packageName;
					PackageManager pm = MA.getPackageManager(); 
					Intent intent = pm.getLaunchIntentForPackage(packageName);
					MA.startActivity(intent); 
				}
			});
			
			// set the long click menu
			appRow.setOnCreateContextMenuListener(new ListAppsLongClickMenu(MA, appInfo));
			
			return convertView;
		}
	}

}

