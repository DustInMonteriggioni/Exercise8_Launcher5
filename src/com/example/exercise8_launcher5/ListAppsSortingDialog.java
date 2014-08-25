package com.example.exercise8_launcher5;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ListAppsSortingDialog 
{
	MainActivity MA;
	AlertDialog dialog;

	public ListAppsSortingDialog(MainActivity ma)
	{
		MA = ma;
		Builder builder = new Builder(MA);
		
		builder.setTitle("排序规则：")
			.setItems(new String[] {"字母顺序排序", "安装时间排序", "使用频率排序"}, 
			new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface arg0, int itemNo)
			{
				AppInfoStorageCenter AISC = MA.getLauncherApplication().AISC;
				switch (itemNo)
				{	
					// in case that listApps might update, sort the allApps
					// 	 to remember the sorting rule
					case 0:
						AISC.allApps.sortByAlphabeticalOrder();
						break;
					case 1:
						AISC.allApps.sortByFirstInstallTime();
						break;
					case 2:
						AISC.allApps.sortByUseFrequency();
						break;
				}
				AISC.copyVisibleAppsIntoListApps();
				AISC.writeIntoFiles();
				MA.listApps.update();
			}
		});
		
		dialog = builder.create();
		
	}

	public void show() 
	{
		dialog.show();
	}
}
