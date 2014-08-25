package com.example.exercise8_launcher5;

import android.app.Application;
import android.content.pm.PackageManager;

public class LauncherApplication extends Application
{
	// variables used in the launcher
	PackageManager pm;
	AppInfoStorageCenter AISC;
	MainActivity MA;	// set in MainActivity.onCreate()
	
	@Override
	public void onCreate()
	{	
		super.onCreate();
		
		pm = this.getPackageManager();
		AISC = new AppInfoStorageCenter(this);
		AISC.readFromFiles();
	}
}