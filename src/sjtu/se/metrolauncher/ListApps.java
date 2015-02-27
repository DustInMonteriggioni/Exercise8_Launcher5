package sjtu.se.metrolauncher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class ListApps
{
	MainActivity MA;
	LinearLayout mainLayout;
	// the contents of the layout
	ImageView search, settings;
	ListView listView;
	int textColor;
	
	public ListApps(MainActivity ma) 
	{
		MA = ma;
		LayoutInflater li = (LayoutInflater)
				MA.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mainLayout = (LinearLayout) li.inflate(R.layout.listapps, null);
		
		search = (ImageView)mainLayout.findViewById(R.id.search);
		settings = (ImageView)mainLayout.findViewById(R.id.settings);
		setOnClickListenersOfSearchAndSettings();
		
		textColor = getColorPreference();
		listView = (ListView)mainLayout.findViewById(R.id.listview);
		listView.setAdapter(new ListApps.iListAdapter(MA));
	}
	
	public void update()
	{	
		listView.setAdapter(new ListApps.iListAdapter(MA));
	}
	
	
	private int getColorPreference()
	{	
		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(MA.getLauncherApplication());
		String colorStr = preference.getString("listapps_textcolor", "black");
		return Color.parseColor(colorStr);
	}
	
	private String findPackageNameForSearch()
	{	
		// try to find by appName
		for (AppInfo appInfo : MA.getLauncherApplication().AISC.allApps)
			if (appInfo.appName.equals("搜索") || appInfo.appName.equalsIgnoreCase("search"))
				return appInfo.packageName;
		// not found by appName, judge by packageName
		for (AppInfo appInfo : MA.getLauncherApplication().AISC.allApps)
			if (appInfo.packageName.contains("searchbox"))
				return appInfo.packageName;
		// still not found
		return "";	// not null to distinguish from not proceed finding
	}
	
	private void setOnClickListenersOfSearchAndSettings()
	{	
		search.setOnClickListener(new OnClickListener()
		{	
			String packageName = null;
			
			@Override
			public void onClick(View view)
			{
				if (packageName == null)
					findPackageNameForSearch();
				if ( !packageName.equals("") )	// found search in the phone
				{	Intent intent = 
							MA.getPackageManager().getLaunchIntentForPackage(packageName);
					MA.startActivity(intent); 
				}
			}
		});
		
		settings.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				Intent intent = new Intent(MA, SettingsActivity.class);
				MA.startActivity(intent);
			}
		});
	}
	
	
	
	
	class iListAdapter extends BaseAdapter
	{
		// width and height in the list, different from that in the grid
		int iconWidth;
		int iconHeight;
		AppInfoList listApps_shown;
		MainActivity MA;
		
		public iListAdapter(MainActivity ma)
		{	
			MA = ma;
			iconWidth = iconHeight = MA.screenHeight / 10;
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
				convertView = vi.inflate(R.layout.app_row_listapps, null);
				convertView.setClickable(true);
			}
			
			// set the icon and the appName of the appRow
			Drawable icon = appInfo.appIcon;
			// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
			icon.setBounds(0, 0, iconWidth, iconHeight);
			TextView appRow = (TextView)convertView.findViewById(R.id.appInfo); 
			appRow.setText(appInfo.appName);
			appRow.setTextColor(textColor);
			appRow.setCompoundDrawables(icon, null, null, null); //设置左图标
			appRow.setOnClickListener(new OnClickListener() 
			{	
				public void onClick(View v) 
				{	
					Intent intent = MA.getLauncherApplication().pm
							.getLaunchIntentForPackage(appInfo.packageName);
					MA.startActivity(intent);
				}
			});
			
			// set the long click menu
			appRow.setOnCreateContextMenuListener(new ListAppsLongClickMenu(MA, appInfo));
			
			return convertView;
		}
	}

}

