package com.example.exercise8_launcher5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.ListView;


public class iAppChangeReceiver extends BroadcastReceiver
{     
	MainActivity MA;
	PackageManager pm;
	
	public iAppChangeReceiver(MainActivity ma)
	{	
		MA = ma;
		pm = MA.pm;
	}
	
    @Override 
    public void onReceive(Context context, Intent intent) 
    {   
    	// install     
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) 
    	{	// dataString == "package:" + packageName
    		String dataString = intent.getDataString();
    		String packageName = dataString.substring(8);
            
    		if ( pm.getLaunchIntentForPackage(packageName) != null )
    		{	// the installed app can be launched, which should be 
    				// in the launcher, thus need to update
    			MA.allAppList.updateList(pm);
    			//GridView gridView = (GridView)deskTop.findViewById(R.id.gridview);
    			ListView listView = (ListView)MA.allApps.findViewById(R.id.listview);
    			//gridView.setAdapter(new iGridAdapter());
    			listView.setAdapter(new iListAdapter(MA));   			
    		}	
        }     
    	
        // uninstall     
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) 
        {   
        	// no launch intent check, because it is always null if uninstalled
    		MA.allAppList.updateList(pm);
    		//GridView gridView = (GridView)deskTop.findViewById(R.id.gridview);
    		ListView listView = (ListView)MA.allApps.findViewById(R.id.listview);
    		//gridView.setAdapter(new iGridAdapter());
    		listView.setAdapter(new iListAdapter(MA));	
        }     
    }     
}