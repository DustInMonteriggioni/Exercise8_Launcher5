package sjtu.se.metrolauncher;

import android.app.Application;
import android.content.pm.PackageManager;

public class LauncherApplication extends Application
{
	// variables used in the launcher
	PackageManager pm;
	AppInfoStorageCenter AISC;
	MainActivity MA;	// set in MainActivity.onCreate()
	SettingsActivity SA;
	
	@Override
	public void onCreate()
	{	
		super.onCreate();
		
		pm = this.getPackageManager();
		AISC = new AppInfoStorageCenter(this);
		AISC.readFromFiles();
	
	}
}
