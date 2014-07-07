package com.example.exercise8_launcher5;


import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.GridView;
import android.widget.ListView;


public class AppListLongClickMenu implements OnCreateContextMenuListener
{
	MainActivity MA;
	AppInfo appInfo;
	
	public AppListLongClickMenu(MainActivity ma, AppInfo theInfo)
	{	
		MA = ma;
		appInfo = theInfo;
	}
	
	@Override
	public void onCreateContextMenu
		(ContextMenu menu, View v,ContextMenuInfo menuInfo)
	{
		// TODO Auto-generated method stub
		menu.setHeaderTitle("Ӧ�ò�����");
		menu.add("��ӵ�����")
			.setOnMenuItemClickListener(new OnMenuItemClickListener() 
			{
				@Override
				public boolean onMenuItemClick(MenuItem item)
				{
					// TODO Auto-generated method stub
					GridView gridView = (GridView)MA.deskTop.findViewById(R.id.gridview);
					MA.AISC.deskTopApps.add(appInfo);
					gridView.setAdapter(new iGridAdapter(MA));
					return true;
				}
			});
		menu.add("���ظó���")
			.setOnMenuItemClickListener(new OnMenuItemClickListener() 
			{
				@Override
				public boolean onMenuItemClick(MenuItem item)
				{
					// TODO Auto-generated method stub
					ListView listView = (ListView)MA.allApps.findViewById(R.id.listview);
					MA.AISC.listApps_hidden.add(appInfo);
					Log.i("testing", "testing: OK here~");
					MA.AISC.listApps_shown.remove(appInfo);
					listView.setAdapter(new iListAdapter(MA));
					return true;
				}
			});
		menu.add("ж��")
			.setOnMenuItemClickListener(new OnMenuItemClickListener() 
			{
				@Override
				public boolean onMenuItemClick(MenuItem item)
				{
					// TODO Auto-generated method stub
					String packageName = appInfo.packageName;
					Uri uri = Uri.parse("package:" + packageName);//��ȡɾ��������URI
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_DELETE);//��������Ҫִ�е�ж�ض���
					intent.setData(uri);//���û�ȡ����URI
					MA.startActivity(intent);
					return true;
				}
			});
		menu.add("����")
			.setOnMenuItemClickListener(new OnMenuItemClickListener()
			{
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					// TODO Auto-generated method stub
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