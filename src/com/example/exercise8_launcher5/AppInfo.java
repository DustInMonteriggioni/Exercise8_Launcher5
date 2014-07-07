package com.example.exercise8_launcher5;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;


public class AppInfo
{	
	public String appName = "";
	public String packageName = "";
	public Drawable appIcon = null;
	
	public AppInfo(PackageInfo packageInfo, PackageManager pm)
	{	
		appName = packageInfo.applicationInfo.loadLabel(pm).toString();
		appIcon = packageInfo.applicationInfo.loadIcon(pm);
		packageName = packageInfo.packageName;
	}
}