package com.example.exercise8_launcher5;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnCreateContextMenuListener;



public class DeskTopLongClickMenu implements OnCreateContextMenuListener
{
	MainActivity MA;
	AppInfo appInfo;
	
	public DeskTopLongClickMenu(MainActivity ma, AppInfo theInfo)
	{	
		MA = ma;
		appInfo = theInfo;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo info)
	{
		menu.setHeaderTitle("应用操作：");
		menu.add("从桌面中去除")
			.setOnMenuItemClickListener(new OnMenuItemClickListener() 
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				MA.getLauncherApplication().AISC
					.deskTopApps.delAppInfo(appInfo.packageName);
				// changing exists in the list, thus write file
				MA.getLauncherApplication().AISC.writeIntoFiles();
				
				MA.deskTop.update();
				return true;
			}
		});
	}

}
