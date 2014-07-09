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
	
	public void copyFromPM(PackageManager pm)
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
	
	public void readFromFile(String file, PackageManager pm)
	{
		try 
		{	BufferedReader reader = new BufferedReader(new FileReader(file));
			String packageName;
			while ( (packageName = reader.readLine()) != null )
			{	AppInfo appInfo = new AppInfo(packageName, pm);
				this.add(appInfo);
			}
			reader.close();
		}
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	public void writeIntoFile(String file, PackageManager pm)
	{
		try 
		{	BufferedWriter writer  = new BufferedWriter(new FileWriter(file, false));
			for (AppInfo appInfo : this)
			{	String packageName = appInfo.packageName;
				writer.write(packageName + '\n');
			}
			writer.flush();
			writer.close();
		} 
		catch (IOException e) {e.printStackTrace();}
	}
}

