package com.example.exercise8_launcher5;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


public class AppInfoStorageCenter
{	
	AppInfoList allApps;
	AppInfoList deskTopApps;
	AppInfoList listApps_shown;
	AppInfoList listApps_hidden;
	
	PackageManager pm;
	
	final static int ADD_APP = 0;
	final static int REMOVE_APP = -1;
	
	public AppInfoStorageCenter(PackageManager thePm)
	{	
		allApps = new AppInfoList();
		deskTopApps = new AppInfoList();
		listApps_shown = new AppInfoList();
		listApps_hidden = new AppInfoList();
		
		pm = thePm;
		
		onFirstRun();
	}
	
	public void onFirstRun()
	{	
		allApps.updateList(pm);
		listApps_shown.updateList(pm);
	}
	
	public void updateOnChange(String thePackageName, int typeOfUpdate) 
	{	
		// view updated in the AppChangeReceiver.onReceive()
		// merely update the lists here
		
		allApps.updateList(pm);
		
		if (typeOfUpdate == ADD_APP)
		{	PackageInfo packageInfo = null;
			try 
				{packageInfo = pm.getPackageInfo(thePackageName, 0);}
			catch (NameNotFoundException e) {e.printStackTrace();}
			AppInfo appInfo = new AppInfo(packageInfo, pm);
			listApps_shown.add(appInfo);
		}
		else // typeOfUpdata == REMOVE_APP
		{	// check if the app is in the deskTop, if true, remove it
			deskTopApps.delAppInfoFromList(thePackageName);
			// check if the app is in the shown list app, if true, remove it
			listApps_shown.delAppInfoFromList(thePackageName);
			// check if the app is in the hidden app list, then remove it
			listApps_hidden.delAppInfoFromList(thePackageName);
		}
	}
	
	public void readFromFiles()
	{	
		return;
	}
	
	public void writeIntoFiles()
	{	
		
	}
	
}