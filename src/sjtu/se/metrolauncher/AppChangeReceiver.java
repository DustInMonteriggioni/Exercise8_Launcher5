package sjtu.se.metrolauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;


public class AppChangeReceiver extends BroadcastReceiver
{   
	LauncherApplication LA;
	PackageManager pm;
	
    @Override 
    public void onReceive(Context context, Intent intent) 
    {   
    	LA = (LauncherApplication) context.getApplicationContext();
    	pm = LA.pm;
    	
    	// dataString == "package:" + packageName
		String dataString = intent.getDataString();
		String packageName = dataString.substring(8);
            
    	// install     
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) 	
        	// if the installed app can be launched, it should be in my launcher, thus need to update
        	if ( pm.getLaunchIntentForPackage(packageName) != null )
    			LA.AISC.updateOnChange(packageName, AppInfoStorageCenter.ADD_APP);
    	
        // uninstall     
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) 
        	// no launch intent check, because it is always null if uninstalled
        	LA.AISC.updateOnChange(packageName, AppInfoStorageCenter.REMOVE_APP);
        
        // written into the file in AISC.updateOnChange()
        
        // update the views
        if (LA.MA != null)
		{	LA.MA.deskTop.update();
			LA.MA.listApps.update();
		}
    }     
}