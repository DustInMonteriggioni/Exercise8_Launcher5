package com.example.exercise8_launcher5;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;


public class WallPaperManager
{
	final static String WALLPAPER = "WallPaper";
	
	MainActivity MA;
	SharedPreferences preferences;
	
	int[] wallPaperResourceList = 
		{R.drawable.black, R.drawable.cloud, R.drawable.gray,
		 R.drawable.home, R.drawable.silver, R.drawable.yellow};
	
	public WallPaperManager(MainActivity ma)
	{	
		MA = ma;
		preferences = MA.getPreferences(Activity.MODE_PRIVATE);
		int wallpaperResourceID = loadWallPaperPref();
		MA.getWindow().setBackgroundDrawableResource(wallpaperResourceID);
	}
	
	public void setLauncherWallPaper(int resourceID)
	{	
		Drawable wallPaper = MA.getResources().getDrawable(resourceID);
		//wallPaper.setBounds(0, 0, 100, 100);
		MA.getWindow().setBackgroundDrawable(wallPaper);
		//MA.getWindow().setBackgroundDrawableResource(resourceID);
	}
	
	public void saveWallPaperPref(int resourceID)
	{	
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(WALLPAPER, resourceID);
		editor.commit();
	}
	
	public int loadWallPaperPref()
	{	
		// if there's no key "WallPaper", use R.drawable.home as default
		return preferences.getInt(WALLPAPER, R.drawable.black);
	}
	
}
