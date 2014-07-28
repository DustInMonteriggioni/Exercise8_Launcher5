package com.example.exercise8_launcher5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

@SuppressWarnings("serial")
public class AppInfoList extends ArrayList<AppInfo>
{	
	PackageManager pm;
	
	public AppInfoList(PackageManager thePm)
	{	
		super();
		pm = thePm;
	}
	
	public boolean hasAppInfo(String thePackageName)
	{	
		for (AppInfo appInfo : this)
			if (appInfo.packageName.equals(thePackageName))
				return true;
		return false;
	}
	
	public AppInfo findAppInfo(String thePackageName)
	{	
		for (AppInfo appInfo : this)
			if (appInfo.packageName.equals(thePackageName))
				return appInfo;
		return null;
	}
	
	public void addAppInfo(String thePackageName)
	{	
		if (this.hasAppInfo(thePackageName))
			return;
		// else... 
		PackageInfo packageInfo = null;
		try 
			{packageInfo = pm.getPackageInfo(thePackageName, 0);}
		catch (NameNotFoundException e) {e.printStackTrace();}
		AppInfo appInfo = new AppInfo(packageInfo, pm);
		this.add(appInfo);
	}
	
	public void delAppInfo(String thePackageName)
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
	
	public void sortByAlphabeticalOrder()
	{	
		final Comparator<Object> chineseStrComparator
			= Collator.getInstance(java.util.Locale.CHINA);
		Comparator<AppInfo> comparator = new Comparator<AppInfo>()
		{	
			@Override
			public int compare(AppInfo appInfo1, AppInfo appInfo2)
			{
				return chineseStrComparator
						.compare(appInfo1.appName, appInfo2.appName);
			}
		};
		Collections.sort(this, comparator);
	}
	
	public void sortByFirstInstallTime()
	{	
		Comparator<AppInfo> comparator = new Comparator<AppInfo>()
		{
			@Override
			public int compare(AppInfo appInfo1, AppInfo appInfo2)
			{
				long time1 = 0, time2 = 0;
				try 
				{
					time1 = pm.
						getPackageInfo(appInfo1.packageName, 0).firstInstallTime;
					time2 = pm.
						getPackageInfo(appInfo2.packageName, 0).firstInstallTime;
				}
				catch (NameNotFoundException e) {e.printStackTrace();}
				
				// not "return (int)(time1 - time2);" cause (time1 - time2)
				// 	may be a long, (int)longNum may change its sign
				if (time1 < time2)
					return 1;	// not -1 to put the recent installed app upper, not lower
				else if (time1 > time2)
					return -1;
				else
					return 0;
			}
		};
		
		Collections.sort(this, comparator);
	}
	
	public void sortByUseFrequency()
	{	
		// not permitted to invoke system's stats, the comparator can't work
		Comparator<AppInfo> comparator = new Comparator<AppInfo>()
		{
			@Override
			public int compare(AppInfo appInfo1, AppInfo appInfo2)
			{
				ComponentName name1 = pm
					.getLaunchIntentForPackage(appInfo1.packageName)
					.getComponent();
				ComponentName name2 = pm
					.getLaunchIntentForPackage(appInfo2.packageName)
					.getComponent();
				int launchCount1 = 0, launchCount2 = 0;
				
				try 
				{
					// 获得ServiceManager类
					Class<?> ServiceManager 
						= Class.forName("android.os.ServiceManager");
					// 获得ServiceManager的getService方法
					Method getService 
						= ServiceManager.getMethod("getService", String.class);
					// 调用getService获取RemoteService
					Object oRemoteService = getService.invoke(null, "usagestats");
					// 获得IUsageStats.Stub类 
					Class<?> cStub = Class
							.forName("com.android.internal.app.IUsageStats$Stub");
					// 获得asInterface方法
					Method asInterface = cStub
							.getMethod("asInterface", android.os.IBinder.class);
					//调用asInterface方法获取IUsageStats对象
					Object oIUsageStats = asInterface.invoke(null, oRemoteService);
					//获得getPkgUsageStats(ComponentName)方法
					Method getPkgUsageStats = oIUsageStats.getClass()
							.getMethod("getPkgUsageStats", ComponentName.class);
					//调用getPkgUsageStats 获取PkgUsageStats对象
					Object stats1 = getPkgUsageStats.invoke(oIUsageStats, name1);
					Object stats2 = getPkgUsageStats.invoke(oIUsageStats, name2);
					//获得PkgUsageStats类 
					Class<?> PkgUsageStats 
						= Class.forName("com.android.internal.os.PkgUsageStats");
					launchCount1 = PkgUsageStats
							.getDeclaredField("launchCount").getInt(stats1);
					launchCount2 = PkgUsageStats
							.getDeclaredField("launchCount").getInt(stats2);
				} 
				catch (Exception e) {e.printStackTrace();}
				
				Log.i("", "testing: " + appInfo1.appName + launchCount1);
				
				if(launchCount1 < launchCount2)
					return 1;	// not -1 to put the frequency app upper, not lower
				else if(launchCount1 < launchCount2)
					return -1;
				else	// launchCount1 == launchCount2 
					return 0;
			}
		};
		
		for (AppInfo appInfo : this)
			comparator.compare(appInfo, appInfo);
		
		//Collections.sort(this, comparator);
	}
	
	public void copyFromPM()
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
	
	public void readFromFile(String file)
	{
		try 
		{	BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			String packageName;
			boolean visible;
			while ( (line = reader.readLine()) != null )
			{	int index = line.indexOf('\t');
				packageName = line.substring(0, index);
				visible = Boolean.parseBoolean(line.substring(index + 1));	// index = '\t'
				AppInfo appInfo = new AppInfo(packageName, pm);
				appInfo.visible = visible;
				this.add(appInfo);
			}
			reader.close();
		}
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();}
	}
	
	public void writeIntoFile(String file)
	{
		try 
		{	BufferedWriter writer  = new BufferedWriter(new FileWriter(file, false));
			for (AppInfo appInfo : this)
			{	String packageName = appInfo.packageName;
				boolean visible = appInfo.visible;
				writer.write(packageName + '\t' + visible + '\n');
			}
			writer.flush();
			writer.close();
		} 
		catch (IOException e) {e.printStackTrace();}
	}
}

