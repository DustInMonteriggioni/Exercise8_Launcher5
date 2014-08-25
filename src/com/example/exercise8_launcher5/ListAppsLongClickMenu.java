package com.example.exercise8_launcher5;


import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;


public class ListAppsLongClickMenu implements OnCreateContextMenuListener
{
	MainActivity MA;
	AppInfo appInfo;
	
	public ListAppsLongClickMenu(MainActivity ma, AppInfo theInfo)
	{	
		MA = ma;
		appInfo = theInfo;
	}
	
	@Override
	public void onCreateContextMenu
		(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	{
		final AppInfoStorageCenter AISC = MA.getLauncherApplication().AISC;
		
		menu.setHeaderTitle("Ӧ�ò�����");
		menu.add("��ӵ�����")
			.setOnMenuItemClickListener(new OnMenuItemClickListener() 
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				AISC.deskTopApps.addAppInfo(appInfo.packageName);
				// changing exists in the list, thus write file
				AISC.writeIntoFiles();
				
				MA.deskTop.update();
				return true;
			}
		});
		menu.add("���ظó���")
			.setOnMenuItemClickListener(new OnMenuItemClickListener() 
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				String thePackageName = appInfo.packageName;
				
				AISC.allApps.findAppInfo(thePackageName).visible = false;
				AISC.listApps.delAppInfo(thePackageName);
				// changing exists in the list, thus write file
				AISC.writeIntoFiles();
				
				MA.listApps.update();
				return true;
			}
		});
		menu.add("ж��")
			.setOnMenuItemClickListener(new OnMenuItemClickListener() 
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				String packageName = appInfo.packageName;
				Uri uri = Uri.parse("package:" + packageName);//��ȡɾ��������URI
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DELETE);//��������Ҫִ�е�ж�ض���
				intent.setData(uri);//���û�ȡ����URI
				MA.startActivity(intent);
				
				AISC.updateOnChange(packageName, AppInfoStorageCenter.REMOVE_APP);
				return true;
			}
		});
		menu.add("����")
			.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				Intent intent = 
						new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				Uri uri = Uri.fromParts("package", appInfo.packageName, null);  
                intent.setData(uri);
                MA.startActivity(intent);
				return true;
			}
		});
	}  
}