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
				switch (itemNo)
				{	
					// in case that listApps might update, sort the allApps
					// 	 to remember the sorting rule
					case 0:
						MA.AISC.allApps.sortByAlphabeticalOrder();
						break;
					case 1:
						MA.AISC.allApps.sortByFirstInstallTime();
						break;
					case 2:
						MA.AISC.allApps.sortByUseFrequency();
						break;
				}
				MA.AISC.copyVisibleAppsIntoListApps();
				MA.AISC.writeIntoFiles();
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
