package com.example.exercise8_launcher5;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity
{	
	LauncherApplication LA;
	
	ListPreference deskTopColumnNum;
	ListPreference deskTopLongClickAction;
	ListPreference listAppsTextColor;
	Preference listAppsShownAppSelect;
	Preference listAppsRefresh;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		
		LA = (LauncherApplication) getApplication();
		LA.SA = this;
		
		addPreferencesFromResource(R.xml.settings);
		
		deskTopColumnNum = (ListPreference) findPreference("desktop_column_number");
		deskTopColumnNum.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object obj)
			{
				int columnNum = Integer.parseInt(obj.toString());
				LA.MA.deskTop.gridView.setNumColumns(columnNum);
				LA.MA.deskTop.columnNum = columnNum;
				LA.MA.deskTop.update();
				return true;
			}
		});
		
		deskTopLongClickAction = (ListPreference) findPreference("desktop_longclick_action");
		deskTopLongClickAction.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{
			@Override
			public boolean onPreferenceChange(Preference preference, Object obj)
			{
				LA.MA.deskTop.longClickEvent = obj.toString();
				LA.MA.deskTop.update();
				return true;
			}
		});
		
		listAppsTextColor = (ListPreference) findPreference("listapps_textcolor");
		listAppsTextColor.setOnPreferenceChangeListener(new OnPreferenceChangeListener()
		{	
			@Override
			public boolean onPreferenceChange(Preference preference, Object obj)
			{
				int textColor = Color.parseColor(obj.toString());
				LA.MA.listApps.textColor = textColor;
				LA.MA.listApps.update();
				return true;
			}
		});
		
		listAppsShownAppSelect = (Preference) findPreference("listapps_shown_app_select");
		listAppsShownAppSelect.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{	
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				ListAppsSelectDialog dialog = new ListAppsSelectDialog(LA, LA.SA);
				dialog.show();
				return true;
			}
		});
		
		listAppsRefresh = (Preference) findPreference("listapps_refresh");
		listAppsRefresh.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				LA.AISC.initAppInfoLists();
				Toast.makeText(LA.SA, "应用列表已更新", Toast.LENGTH_LONG).show();
				return true;
			}
		});
	}

}