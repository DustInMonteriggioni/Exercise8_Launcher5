package com.example.exercise8_launcher5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

@SuppressWarnings("serial")
public class AppInfoList extends ArrayList<AppInfo>
{	
	PackageManager pm;
	
	public AppInfoList(PackageManager thePm)
	{	
		super();
		pm = thePm;
	}
	
	public boolean hasAppInfo(String thePackageName)
	{	
		for (AppInfo appInfo : this)
			if (appInfo.packageName.equals(thePackageName))
				return true;
		return false;
	}
	
	public AppInfo findAppInfo(String thePackageName)
	{	
		for (AppInfo appInfo : this)
			if (appInfo.packageName.equals(thePackageName))
				return appInfo;
		return null;
	}
	
	public void addAppInfo(String thePackageName)
	{	
		if (this.hasAppInfo(thePackageName))
			return;
		// else... 
		PackageInfo packageInfo = null;
		try 
			{packageInfo = pm.getPackageInfo(thePackageName, 0);}
		catch (NameNotFoundException e) {e.printStackTrace();}
		AppInfo appInfo = new AppInfo(packageInfo, pm);
		this.add(appInfo);
	}
	
	public void delAppInfo(String thePackageName)
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
	
	public void copyFromPM()
	{	
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		this.clear();
		for (PackageInfo packageInfo : packages) 
			// keep those who can be launched
			if ( pm.getLaunchIntentForPackage(packageInfo.packageName) != null )
			{	AppInfo tmpInfo = new AppInfo(packageInfo, pm);
				this.add(tmpInfo);
			}
	}
	
	public void readFromFile(String file)
	{
		try 
		{	BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			String packageName;
			boolean visible;
			while ( (line = reader.readLine()) != null )
			{	int index = line.indexOf('\t');
				packageName = line.substring(0, index);
				visible = Boolean.parseBoolean(line.substring(index + 1));	// index = '\t'
				AppInfo appInfo = new AppInfo(packageName, pm);
				appInfo.visible = visible;
				this.add(appInfo);
			}
			reader.close();
		}
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	public void writeIntoFile(String file)
	{
		try 
		{	BufferedWriter writer  = new BufferedWriter(new FileWriter(file, false));
			for (AppInfo appInfo : this)
			{	String packageName = appInfo.packageName;
				boolean visible = appInfo.visible;
				writer.write(packageName + '\t' + visible + '\n');
			}
			writer.flush();
			writer.close();
		} 
		catch (IOException e) {e.printStackTrace();}
	}
}

