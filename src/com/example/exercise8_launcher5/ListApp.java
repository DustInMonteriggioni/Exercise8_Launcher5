package com.example.exercise8_launcher5;

import java.util.ArrayList;
import java.util.List;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

@SuppressWarnings("serial")
public class ListApp extends ArrayList<AppInfo>
{	
	public void copyFrom(PackageManager pm)
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