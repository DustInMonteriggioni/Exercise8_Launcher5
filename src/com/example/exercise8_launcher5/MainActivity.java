package com.example.exercise8_launcher5;

import android.os.Bundle;
import android.view.Menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
	/*
	AppInfoList allAppList; // app info storage
	ArrayList<AppInfo> invisiableAppList;	// invisible in the allApps
	AppInfoList deskTopAppList;
	*/
	LinearLayout deskTop, allApps; // the layouts to be passed into the fragment
	
	BroadcastReceiver appChangeReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	// 去除顶部的标题栏
		setContentView(R.layout.activity_main);
		
		pm = getPackageManager();

		AISC = new AppInfoStorageCenter(this);	// reading files in the onStart()
		
		// prepare the two layouts of the launcher
		LayoutInflater Li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		deskTop = (LinearLayout) Li.inflate(R.layout.fragment_grid, null);
		allApps = (LinearLayout) Li.inflate(R.layout.fragment_linear, null);
		// the content of the two layouts
		GridView gridView = (GridView)deskTop.findViewById(R.id.gridview);
		ListView listView = (ListView)allApps.findViewById(R.id.listview);
		gridView.setAdapter(new iGridAdapter(this));
		listView.setAdapter(new iListAdapter(this));
		
		
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = 
				new SectionsPagerAdapter(getSupportFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

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
			DSF.setViews(MainActivity.this.deskTop, MainActivity.this.allApps);
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
		
		LinearLayout deskTop, allApps;

		public DummySectionFragment() {}
		
		public void setViews(LinearLayout dt, LinearLayout aa)
		{	
			deskTop = dt;
			allApps = aa;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) 
		{
			int fragmentNo = getArguments().getInt(ARG_SECTION_NUMBER);
			View rootView = null;
			switch (fragmentNo)
			{	case 1:
					rootView = inflater.inflate(R.layout.fragment_grid,
						container, false);
					rootView = deskTop;
					break;
				case 2:
					rootView = inflater.inflate(R.layout.fragment_linear,
							container, false);
					rootView = allApps;
					break;
			}
			return rootView;
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

