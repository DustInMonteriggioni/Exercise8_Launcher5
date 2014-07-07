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
import android.widget.TextView;


public class iListAdapter extends BaseAdapter
{
	// width and height in the list, different from that in the grid
	int iconWidth;
	int iconHeight;
	AppInfoList allAppList;
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
		
		allAppList = MA.AISC.listApps_shown;
	}
	
	public int getCount() 
	{	return allAppList.size();	}

	public Object getItem(int position) 
	{	return allAppList.get(position);	}

	public long getItemId(int position) 
	{	return position;	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		final AppInfo appInfo = allAppList.get(position);
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
		appRow.setOnCreateContextMenuListener(new AppListLongClickMenu(MA, appInfo));
		
		return convertView;
	}
}