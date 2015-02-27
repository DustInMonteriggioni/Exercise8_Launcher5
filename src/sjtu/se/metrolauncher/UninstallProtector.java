package sjtu.se.metrolauncher;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;



public class UninstallProtector extends PreferenceActivity
{
	// Interaction with the DevicePolicyManager
    DevicePolicyManager DPM;
    ComponentName componentName;
    static ActivityManager actMag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Prepare to work with the DPM
        DPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, UninstallReceiver.class);
        
        actMag = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    }
	
    
    public static class UninstallReceiver extends BroadcastReceiver//DeviceAdminReceiver
    {
        /*
    	@Override
        public void onEnabled(Context context, Intent intent)
        {
        	Log.i("", "testing: onEnabled~!");
        }

        @Override
        public void onDisabled(Context context, Intent intent)
        {
        	Log.i("", "testing: onDisabled~!");
        }

        @Override
        public void onPasswordChanged(Context context, Intent intent)
        {
        	Log.i("", "testing: onPasswordChanged~!");
        }

        @Override
        public void onPasswordFailed(Context context, Intent intent)
        {
        	Log.i("", "testing: onPasswordFailed~!");
        }

        @Override
        public void onPasswordSucceeded(Context context, Intent intent)
        {
        	Log.i("", "testing: onPasswordSucceeded~!");
        }

        @Override
        public void onPasswordExpiring(Context context, Intent intent)
        {
        	Log.i("", "testing: onPasswordExpiring~!");
        }
        */
        @Override
        public void onReceive(Context context, Intent intent)
        {	
        	ActivityManager actMag = (ActivityManager)context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        	String dataString = intent.getDataString();
    		if (dataString.equals("package:com.example.exercise1_texting") && intent.getAction().equals("android.intent.action.PACKAGE_REMOVED"))
    		{	Log.i("", "testing: aborting uninstalling~!");
    			actMag.killBackgroundProcesses("com.android.packageinstaller");
    			
    			//Intent i = new Intent(Intent.ACTION_MAIN);
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				//i.addCategory(Intent.CATEGORY_HOME);
				//startActivity(i);
    		
    		}
    		
        }
    }
}
