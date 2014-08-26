package com.example.exercise8_launcher5;

import java.io.File;
import java.io.IOException;

import android.content.pm.PackageManager;
import android.os.Environment;


public class AppInfoStorageCenter
{	
	AppInfoList allApps;
	AppInfoList deskTopApps;
	AppInfoList listApps;	// the shown ones
	
	MainActivity MA;
	PackageManager pm;
	
	final static int ADD_APP = 0;
	final static int REMOVE_APP = -1;
	static String DIR_NAME = null;
	final static String ALL_APPS_FILENAME = "All Apps.txt";
	final static String DESKTOP_APPS_FILENAME = "DeskTop Apps.txt";
	final static String LIST_APPS_FILENAME = "List Apps.txt";
	
	public AppInfoStorageCenter(LauncherApplication la)
	{	
		pm = la.pm;
		
		DIR_NAME = Environment.getExternalStorageDirectory().getPath()
				+ '/' + la.getString(R.string.app_name) + '/';
		
		allApps = new AppInfoList(pm);
		deskTopApps = new AppInfoList(pm);
		listApps = new AppInfoList(pm);
	}
	
	public void onFirstRun()
	{	
		// prepare the files
		File file = new File(DIR_NAME);
		file.mkdirs();
		
		try
		{	file = new File(DIR_NAME + DESKTOP_APPS_FILENAME);
			file.createNewFile();
			file = new File(DIR_NAME + LIST_APPS_FILENAME);
			file.createNewFile();
		}catch (IOException ioe){ioe.printStackTrace();}
		
		initAppInfoLists();
		
		// write into the files as soon as the list changes
		writeIntoFiles();
	}
	
	/**
	 * allApps copy from PackageManager, sort, copy into listApps
	 */
	public void initAppInfoLists()
	{	
		allApps.copyFromPM();
		allApps.sortByAlphabeticalOrder();
		copyVisibleAppsIntoListApps();
	}
	
	public void updateOnChange(String thePackageName, int typeOfUpdate) 
	{	
		// view updated in the AppChangeReceiver.onReceive()
		// merely update the lists and files here
		
		if (typeOfUpdate == ADD_APP)
		{	allApps.addAppInfo(thePackageName);
			listApps.addAppInfo(thePackageName);
		}
		else // typeOfUpdata == REMOVE_APP
		{	// check if the app is in the lists, if true, remove it
			allApps.delAppInfo(thePackageName);
			deskTopApps.delAppInfo(thePackageName);
			listApps.delAppInfo(thePackageName);
		}
		writeIntoFiles();
	}
	
	public void copyVisibleAppsIntoListApps()
	{	
		listApps.clear();
		for (AppInfo appInfo : allApps)
			if (appInfo.visible == true)
				listApps.addAppInfo(appInfo.packageName);
	}
	
	public void readFromFiles()
	{	
		File file = new File(DIR_NAME);
		if (!file.exists())	// first run
		{	onFirstRun();
			return;
		}
		allApps.readFromFile(DIR_NAME + ALL_APPS_FILENAME);
		deskTopApps.readFromFile(DIR_NAME + DESKTOP_APPS_FILENAME);
		listApps.readFromFile(DIR_NAME + LIST_APPS_FILENAME);
	}
	
	public void writeIntoFiles()
	{	
		allApps.writeIntoFile(DIR_NAME + ALL_APPS_FILENAME);
		deskTopApps.writeIntoFile(DIR_NAME + DESKTOP_APPS_FILENAME);
		listApps.writeIntoFile(DIR_NAME + LIST_APPS_FILENAME);
	}
	
}