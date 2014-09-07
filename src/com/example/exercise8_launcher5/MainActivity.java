package com.example.exercise8_launcher5;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity 
{
	LauncherApplication LA;
	
	MainActivityFragments fragments;
	DeskTop deskTop;
	ListApps listApps;
	
	WallPaperManager wpm;
	
	int screenWidth;
	int screenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		LA = (LauncherApplication)this.getApplication();
		LA.MA = this;
		
		setScreenWidthAndHeight();
		
		// 去除顶部的标题栏, already set in the manifest.xml
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_activity);
		
		deskTop = new DeskTop(this);
		listApps = new ListApps(this);
		fragments = new MainActivityFragments(this);
		
		wpm = new WallPaperManager(this);	// wall paper set in the constructor
	}

	@Override
	public void onStart()
	{	
		super.onStart();
	    //LA.AISC.readFromFiles();
	}
	
	@Override
	public void onStop()
	{	
		//LA.AISC.writeIntoFiles();
		super.onStop();
	}
	
	private void setScreenWidthAndHeight()
	{	
		screenWidth = getResources().getDisplayMetrics().widthPixels;	//手机屏幕的宽度
		screenHeight = getResources().getDisplayMetrics().heightPixels;	//手机屏幕的高度
	}
	
	public LauncherApplication getLauncherApplication()
	{	
		return LA;
	}
}

