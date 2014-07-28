package com.example.exercise8_launcher5;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;


@SuppressLint("NewApi")
public class AllAppDialog
{
	ListView listView;
	MainActivity MA;
	AlertDialog dialog;
	
	public AllAppDialog(Context context)
	{
		MA = (MainActivity) context;
		Builder builder = new Builder(MA);
		
		builder.setTitle("选择需要显示的应用：");
		
		listView = 
			(ListView) LayoutInflater.from(MA).inflate(R.layout.all_apps_dialog, null);
		listView.setAdapter(new AllAppDialog.iListAdapter(MA));
		
		builder.setView(listView);
		
		builder.setPositiveButton("确定", new OnClickListener()
		{	
			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				// keep the record setted in the list allApps and update relevant data
				MA.AISC.copyVisibleAppsIntoListApps();
				// changes exists in the lists, thus write the files
				MA.AISC.writeIntoFiles();
				MA.listApps.update();
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// user cancel to change, erase the change recorded in list allApps
				MA.AISC.readFromFiles();
			}
		});
		
		builder.setOnDismissListener(new OnDismissListener()
		{
			@Override
			public void onDismiss(DialogInterface arg0)
			{
				// dismissed, erase the change recorded in list allapps
				MA.AISC.readFromFiles();
			}
		});
		
		dialog = builder.create();
		
	}
	
	public void show()
	{	
		dialog.show();
	}
	
	
	
	
	class iListAdapter extends BaseAdapter
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
			int screenWidth=wm.getDefaultDisplay().getWidth();		//手机屏幕的宽度
			int screenHeight=wm.getDefaultDisplay().getHeight();	//手机屏幕的高度
			iconWidth = iconHeight = (int)screenHeight / 10;
			
			allAppList = MA.AISC.allApps;
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
			
			// always inflate one to avoid the check boxes disturb each other
			//if (convertView == null) // entered when the row is created
			LayoutInflater vi = (LayoutInflater)MA.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.app_row_dialog, null);
			convertView.setClickable(true);
			
			Drawable icon = appInfo.appIcon;
			// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
			icon.setBounds(0, 0, iconWidth, iconHeight);
			CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox_dialog); 
			checkBox.setText(appInfo.appName);
			checkBox.setCompoundDrawables(icon, null, null, null); //设置左图标
			
			if ( MA.AISC.allApps.findAppInfo(appInfo.packageName).visible == true )
				checkBox.setChecked(true);
			else
				checkBox.setChecked(false);
			
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean visible)
				{
					MA.AISC.allApps.findAppInfo(appInfo.packageName).visible = visible;
				}
			});
			
			return convertView;
		}
	}
	
}
