package com.example.exercise8_launcher5;

import java.io.File;
import java.io.IOException;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;


public class AppInfoStorageCenter
{	
	AppInfoList allApps;
	AppInfoList deskTopApps;
	AppInfoList listApps_shown;
	AppInfoList listApps_hidden;
	
	MainActivity MA;
	PackageManager pm;
	
	final static int ADD_APP = 0;
	final static int REMOVE_APP = -1;
	static String DIR_NAME = null;
	final static String DESKTOP_APPS_FILENAME = "DeskTop Apps.txt";
	final static String LISTAPP_SHOWN_FILENAME = "List Apps Shown.txt";
	final static String LISTAPP_HIDDEN_FILENAME = "List Apps Hidden.txt";
	
	public AppInfoStorageCenter(MainActivity ma)
	{	
		MA = ma;
		pm = MA.pm;
		
		DIR_NAME = Environment.getExternalStorageDirectory().getPath()
				+ '/' + MA.getString(R.string.app_name) + '/';
		
		allApps = new AppInfoList();
		deskTopApps = new AppInfoList();
		listApps_shown = new AppInfoList();
		listApps_hidden = new AppInfoList();
	}
	
	public void onFirstRun()
	{	
		// prepare the files
		File file = new File(DIR_NAME);
		file.mkdirs();
		
		try
		{	file = new File(DIR_NAME + DESKTOP_APPS_FILENAME);
			file.createNewFile();
			file = new File(DIR_NAME + LISTAPP_SHOWN_FILENAME);
			file.createNewFile();
			file = new File(DIR_NAME + LISTAPP_HIDDEN_FILENAME);
			file.createNewFile();
		}catch (IOException ioe){ioe.printStackTrace();}
		
		allApps.copyFromPM(pm);
		listApps_shown.copyFromPM(pm);
		
		// write into the files as soon as the list changes
		writeIntoFiles();
	}
	
	public void updateOnChange(String thePackageName, int typeOfUpdate) 
	{	
		// view updated in the AppChangeReceiver.onReceive()
		// merely update the lists here
		
		allApps.copyFromPM(pm);
		
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
		File file = new File(DIR_NAME);
		if (!file.exists())	// first run
		{	onFirstRun();
			return;
		}
		deskTopApps.readFromFile(DIR_NAME + DESKTOP_APPS_FILENAME, pm);
		listApps_shown.readFromFile(DIR_NAME + LISTAPP_SHOWN_FILENAME, pm);
		listApps_hidden.readFromFile(DIR_NAME + LISTAPP_HIDDEN_FILENAME, pm);
	}
	
	public void writeIntoFiles()
	{	
		deskTopApps.writeIntoFile(DIR_NAME + DESKTOP_APPS_FILENAME, pm);
		listApps_shown.writeIntoFile(DIR_NAME + LISTAPP_SHOWN_FILENAME, pm);
		listApps_hidden.writeIntoFile(DIR_NAME + LISTAPP_HIDDEN_FILENAME, pm);
	}
	
}