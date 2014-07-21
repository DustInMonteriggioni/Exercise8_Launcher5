package com.example.exercise8_launcher5;

import android.os.Bundle;
import android.view.Menu;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity 
{
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	// variables used in the launcher
	PackageManager pm;
	
	AppInfoStorageCenter AISC;
	
	DeskTop deskTop;
	ListApps listApps;
	
	BroadcastReceiver appChangeReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// 去除顶部的标题栏, already setted in the manifest.xml
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		pm = getPackageManager();

		AISC = new AppInfoStorageCenter(this);	// reading files in the onStart()
		
		// prepare the two layouts of the launcher
		deskTop = new DeskTop(this);
		listApps = new ListApps(this);
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = 
				new SectionsPagerAdapter(getSupportFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.MainActivity);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	*/
	
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter 
	{
		public SectionsPagerAdapter(FragmentManager fm) 
		{	super(fm);	}

		@Override
		public Fragment getItem(int position) 
		{
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			DummySectionFragment DSF = new DummySectionFragment();
			DSF.setParameters
				(MainActivity.this, MainActivity.this.deskTop, MainActivity.this.listApps);
			Fragment fragment = (Fragment) DSF;
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() 
		{
			// Show 2 total pages.
			return 2;
		}
		
		/*
		@Override
		public CharSequence getPageTitle(int position) 
		{	
			Locale l = Locale.getDefault();
			switch (position) 
			{	case 0:
					return getString(R.string.title_section1).toUpperCase(l);
				case 1:
					return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
		*/
		
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment 
	{
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		
		DeskTop deskTop;
		ListApps listApps;
		MainActivity MA;

		public DummySectionFragment() {}
		
		public void setParameters(MainActivity ma,DeskTop dt, ListApps la)
		{	
			MA = ma;
			deskTop = dt;
			listApps = la;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{	
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);	// menu won't be created without this
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) 
		{
			int fragmentNo = getArguments().getInt(ARG_SECTION_NUMBER);
			View rootView = null;
			switch (fragmentNo)
			{	case 1:
					rootView = deskTop.mainLayout;
					break;
				case 2:
					rootView = listApps.mainLayout;
					break;
			}
			return rootView;
		}
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
		{
			super.onCreateOptionsMenu(menu, inflater);
			
			int fragmentNo = getArguments().getInt(ARG_SECTION_NUMBER);
			switch (fragmentNo)
			{	
				case 1:
					createMenu_deskTop(menu);
					break;
				case 2:
					createMenu_listApps(menu);
					break;
			}
		}
		
		private void createMenu_deskTop(Menu menu)
		{	
			menu.add("添加磁块")
				.setOnMenuItemClickListener(new OnMenuItemClickListener() 
				{
					@Override
					public boolean onMenuItemClick(MenuItem arg0) 
					{
						return false;
					}
				});
			menu.add("桌面设置")
				.setOnMenuItemClickListener(new OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem arg0) 
					{
						Intent intent = new Intent(MA, SettingsActivity.class);
						startActivity(intent);
						return true;
					}
				});
			menu.add("系统设置")
				.setOnMenuItemClickListener(new OnMenuItemClickListener()
				{	
					@Override
					public boolean onMenuItemClick(MenuItem arg0)
					{
						Intent intent
							= new Intent(android.provider.Settings.ACTION_SETTINGS);
						startActivity(intent);
						return true;
					}
				});
			menu.add("更换壁纸")
				.setOnMenuItemClickListener(new OnMenuItemClickListener()
				{	
					@Override
					public boolean onMenuItemClick(MenuItem arg0)
					{
						return false;
					}
				});
		}
		
		private void createMenu_listApps(Menu menu)
		{	
			menu.add("列表排序")
				.setOnMenuItemClickListener(new OnMenuItemClickListener() 
				{
					@Override
					public boolean onMenuItemClick(MenuItem arg0) 
					{
						return false;
					}
				});
			menu.add("添加/隐藏应用")
				.setOnMenuItemClickListener(new OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem arg0) 
					{
						AllAppDialog dialog = new AllAppDialog(MA);
						dialog.show();
						
						return true;
					}
				});
			menu.add("列表设置")
				.setOnMenuItemClickListener(new OnMenuItemClickListener()
				{	
					@Override
					public boolean onMenuItemClick(MenuItem arg0)
					{
						Intent intent = new Intent(MA, SettingsActivity.class);
						startActivity(intent);
						return true;
					}
				});
		}
		
	}
	

	@Override
	public void onStart()
	{	
		super.onStart();     

	    appChangeReceiver = new AppChangeReceiver(this);
	    IntentFilter filter = new IntentFilter();     
	    filter.addAction("android.intent.action.PACKAGE_ADDED");     
	    filter.addAction("android.intent.action.PACKAGE_REMOVED");     
	    filter.addDataScheme("package");     
	    this.registerReceiver(appChangeReceiver, filter);
	    
	    AISC.readFromFiles();
	}
	
	@Override 
	public void onDestroy()
	{   
		// not write files here because the lists will be empty
		//AISC.writeIntoFiles();
		
	    if(appChangeReceiver != null)     
	        this.unregisterReceiver(appChangeReceiver);     

	    super.onDestroy();
	}
	
	
}

