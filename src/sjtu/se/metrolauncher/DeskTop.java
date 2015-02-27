package sjtu.se.metrolauncher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class DeskTop
{
	MainActivity MA;
	LinearLayout mainLayout;
	GridView gridView;	// the content of the layout
	int columnNum;
	String longClickEvent;
	
	public DeskTop(MainActivity ma) 
	{
		MA = ma;
		LayoutInflater li = (LayoutInflater)
				MA.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mainLayout = (LinearLayout) li.inflate(R.layout.desktop, null);
		
		columnNum = getColumnNumPreference();
		longClickEvent = getLongClickEventPreference();
		gridView = (GridView)mainLayout.findViewById(R.id.gridview);
		gridView.setNumColumns(columnNum);
		gridView.setAdapter(new DeskTop.iGridAdapter(MA));
	}
	
	public void update()
	{	
		gridView.setAdapter(new DeskTop.iGridAdapter(MA));
	}
	
	
	/**
	 * get the columnNum stored by the SettingsActivity
	 */
	private int getColumnNumPreference()
	{	
		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(MA.getLauncherApplication());
		String colNumStr = preference.getString("desktop_column_number", "4");
		return Integer.parseInt(colNumStr);
	}
	
	private String getLongClickEventPreference()
	{	
		SharedPreferences preference = PreferenceManager
				.getDefaultSharedPreferences(MA.getLauncherApplication());
		return preference.getString("desktop_longclick_action", "菜单");
	}
	
	
	
	
	
	class iGridAdapter extends BaseAdapter
	{	
		// width and height in the grid icon, different from that in the list
		int iconWidth;
		int iconHeight;
		AppInfoList deskTopAppList;
		MainActivity MA;
		
		int appDraggedPos;	// the index of the app icon dragged
		
		public iGridAdapter(MainActivity ma)
		{	
			MA = ma;
			iconWidth = iconHeight = MA.screenWidth / columnNum;
			deskTopAppList = MA.getLauncherApplication().AISC.deskTopApps;
		}
		
		public int getCount() 
		{	return deskTopAppList.size();	}

		public Object getItem(int position) 
		{	return deskTopAppList.get(position);	}

		public long getItemId(int position) 
		{	return position;	}

		/**
		 * convertView在iGridAdapter.getView()中没有被重复使用，不过在
		 * iListAdapter.getView()中重复使用。这是因为 gridView中, position 0的getView()
		 * 总是被多次调用，导致这里的长按菜单（dialog中的OnClickListener）失效。
		 * 由于view数量不多因此不重复使用convertView以逃避这一问题
		 */
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			final AppInfo appInfo = deskTopAppList.get(position);
			// not reuse the convertView. explained above
			ImageView appIcon = new ImageView(MA);
			appIcon.setImageDrawable(appInfo.appIcon);
			appIcon.setLayoutParams(new GridView.LayoutParams(iconWidth, iconHeight));
			// to realize the function of launching
			appIcon.setOnClickListener(new OnClickListener() 
			{	
				@Override
				public void onClick(View v) 
				{	
					String packageName = appInfo.packageName;
					PackageManager pm = MA.getPackageManager(); 
					Intent intent = pm.getLaunchIntentForPackage(packageName);
					MA.startActivity(intent); 
				}
			});
			
			if (longClickEvent.equals("菜单"))
				appIcon.setOnCreateContextMenuListener(new DeskTopLongClickMenu(MA, appInfo));
			
			if (longClickEvent.equals("拖拽"))
			{	appIcon.setOnDragListener(new OnDragListener()
				{	
					@Override
					public boolean onDrag(View view, DragEvent event)
					{
						// drop at list[position]
						if (event.getAction() == DragEvent.ACTION_DROP)
						{	AppInfo appDragged = deskTopAppList.get(appDraggedPos);
							deskTopAppList.set(appDraggedPos, null);
							deskTopAppList.add(position, appDragged);
							deskTopAppList.remove(null);
							MA.getLauncherApplication().AISC.writeIntoFiles();
							DeskTop.this.update();
						}
						return true;
					}
				});
				appIcon.setOnLongClickListener(new OnLongClickListener()
				{
					@Override
					public boolean onLongClick(View view)
					{
						appDraggedPos = position;	// list[position] dragged
						DragShadowBuilder builder = new DragShadowBuilder(view);
						view.startDrag(null, builder, null, 0);
						return false;
					}
				});
			}
			
			return appIcon;
		}
	}

}

