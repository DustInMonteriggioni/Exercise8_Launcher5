package com.example.exercise8_launcher5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;


public class MainActivityFragments
{
	MainActivity MA;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	iFragmentPagerAdapter adapter;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager viewPager;
	
	public MainActivityFragments(MainActivity ma)
	{	
		MA = ma;
		
		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) MA.findViewById(R.id.MainActivity);
		// Create the adapter that will return a fragment for each of the two
		// 	primary sections of the app.
		adapter = new iFragmentPagerAdapter(MA.getSupportFragmentManager());
		viewPager.setAdapter(adapter);
	}
	
	
	
	
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class iFragmentPagerAdapter extends FragmentPagerAdapter 
	{
		public iFragmentPagerAdapter(FragmentManager fm) 
		{	super(fm);	}

		@Override
		public Fragment getItem(int position) 
		{
			// getItem() is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			iFragment fragment = new iFragment();
			Bundle args = new Bundle();
			args.putInt(iFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() 
		{
			// Show 2 total pages.
			return 2;
		}
	}
	
	
	
	
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class iFragment extends Fragment
	{	
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
		
		MainActivity MA;
		DeskTop deskTop;
		ListApps listApps;
		
		@Override
		public void onCreate(Bundle savedInstanceState)
		{	
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);	// menu won't be created without this
			
			MA = (MainActivity)this.getActivity();
			deskTop = MA.deskTop;
			listApps = MA.listApps;
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
					WallPaperSelectDialog dialog = new WallPaperSelectDialog(MA);
					dialog.show();
					return true;
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
					ListAppsSortingDialog dialog = new ListAppsSortingDialog(MA);
					dialog.show();
					return true;
				}
			});
			menu.add("添加/隐藏应用")
				.setOnMenuItemClickListener(new OnMenuItemClickListener()
			{
				@Override
				public boolean onMenuItemClick(MenuItem arg0) 
				{
					ListAppsSelectDialog dialog 
						= new ListAppsSelectDialog(MA.getLauncherApplication(), MA);
					dialog.show();
					return true;
				}
			});
			menu.add("软件设置")
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
	
}