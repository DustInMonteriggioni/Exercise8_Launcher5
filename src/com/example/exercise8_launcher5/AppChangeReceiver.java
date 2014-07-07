package com.example.exercise8_launcher5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.GridView;
import android.widget.ListView;


public class AppChangeReceiver extends BroadcastReceiver
{   
	MainActivity MA;
	PackageManager pm;
	
	public AppChangeReceiver()
	{	
		super();
	}
	
	public AppChangeReceiver(MainActivity ma)
	{	
		super();
		MA = ma;
		pm = MA.pm;
	}
	
    @Override 
    public void onReceive(Context context, Intent intent) 
    {   
    	pm = MA.pm;
    	
    	// dataString == "package:" + packageName
		String dataString = intent.getDataString();
		String packageName = dataString.substring(8);
            
    	// install     
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) 
    	{	
    		if ( pm.getLaunchIntentForPackage(packageName) != null )
    		{	// the installed app can be launched, which should be 
    				// in the launcher, thus need to update
    			MA.AISC.updateOnChange(packageName, AppInfoStorageCenter.ADD_APP);
    			GridView gridView = (GridView)MA.deskTop.findViewById(R.id.gridview);
    			ListView listView = (ListView)MA.allApps.findViewById(R.id.listview);
    			gridView.setAdapter(new iGridAdapter(MA));
    			listView.setAdapter(new iListAdapter(MA));   			
    		}	
        }     
    	
        // uninstall     
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) 
        {   
        	// no launch intent check, because it is always null if uninstalled
        	MA.AISC.updateOnChange(packageName, AppInfoStorageCenter.REMOVE_APP);
			GridView gridView = (GridView)MA.deskTop.findViewById(R.id.gridview);
    		ListView listView = (ListView)MA.allApps.findViewById(R.id.listview);
    		gridView.setAdapter(new iGridAdapter(MA));
    		listView.setAdapter(new iListAdapter(MA));	
        }     
    }     
}