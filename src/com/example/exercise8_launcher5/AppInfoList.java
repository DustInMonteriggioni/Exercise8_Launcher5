package com.example.exercise8_launcher5;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

@SuppressWarnings("serial")
public class AppInfoList extends ArrayList<AppInfo>
{	
	public void delAppInfoFromList(String thePackageName)
	{	
		// to avoid java.util.concurrentmodificationexception
		AppInfo appInfoToDel = null;
		
		for (AppInfo appInfo : this)
			if (appInfo.packageName.equals(thePackageName))
			{	appInfoToDel = appInfo;
				break;
			}
		if (appInfoToDel != null)
			this.remove(appInfoToDel);
	}
	
	public void updateList(PackageManager pm)
	{	
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		this.clear();
		for (PackageInfo packageInfo : packages) 
		{	// keep those who can be launched
			if ( pm.getLaunchIntentForPackage(packageInfo.packageName) != null )
			{	AppInfo tmpInfo = new AppInfo(packageInfo, pm);
				this.add(tmpInfo);
			}
		}
	}
}