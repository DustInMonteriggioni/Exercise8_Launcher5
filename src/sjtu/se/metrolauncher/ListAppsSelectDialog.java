package sjtu.se.metrolauncher;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;


@SuppressLint("NewApi")
public class ListAppsSelectDialog
{
	MainActivity MA;
	AppInfoStorageCenter AISC;
	AlertDialog dialog;
	ListView listView;
	
	public ListAppsSelectDialog(LauncherApplication LA, Context context)
	{
		MA = LA.MA;
		AISC = MA.getLauncherApplication().AISC;
		Builder builder = new Builder(context);
		
		builder.setTitle("选择需要显示的应用：");
		
		listView = 
			(ListView) LayoutInflater.from(MA).inflate(R.layout.listapps_select_dialog, null);
		listView.setBackgroundColor(Color.WHITE);
		listView.setAdapter(new ListAppsSelectDialog.iListAdapter(MA));
		
		builder.setView(listView);
		
		builder.setPositiveButton("确定", new OnClickListener()
		{	
			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				// keep the record setted in the list allApps and update relevant data
				AISC.copyVisibleAppsIntoListApps();
				// changes exists in the lists, thus write the files
				AISC.writeIntoFiles();
				MA.listApps.update();
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				// user cancel to change, erase the change recorded in list allApps
				AISC.readFromFiles();
			}
		});
		
		builder.setOnDismissListener(new OnDismissListener()
		{
			@Override
			public void onDismiss(DialogInterface arg0)
			{
				// dismissed, erase the change recorded in list allapps
				AISC.readFromFiles();
			}
		});
		
		dialog = builder.create();
		
	}
	
	public void show()
	{	
		dialog.show();
	}
	
	
	
	
	class iListAdapter extends BaseAdapter
	{
		// width and height in the list, different from that in the grid
		int iconWidth;
		int iconHeight;
		AppInfoList allAppList;
		MainActivity MA;
		
		@SuppressWarnings("deprecation")
		public iListAdapter(MainActivity ma)
		{	
			MA = ma;
			
			WindowManager wm = (WindowManager)MA.getSystemService(Context.WINDOW_SERVICE);
			@SuppressWarnings("unused")
			int screenWidth=wm.getDefaultDisplay().getWidth();		//手机屏幕的宽度
			int screenHeight=wm.getDefaultDisplay().getHeight();	//手机屏幕的高度
			iconWidth = iconHeight = (int)screenHeight / 10;
			
			allAppList = AISC.allApps;
		}
		
		public int getCount() 
		{	return allAppList.size();	}

		public Object getItem(int position) 
		{	return allAppList.get(position);	}

		public long getItemId(int position) 
		{	return position;	}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			final AppInfo appInfo = allAppList.get(position);
			
			// always inflate one to avoid the check boxes disturb each other
			//if (convertView == null) // entered when the row is created
			LayoutInflater vi = (LayoutInflater)MA.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.app_row_listapps_select_dialog, null);
			convertView.setClickable(true);
			
			Drawable icon = appInfo.appIcon;
			// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
			icon.setBounds(0, 0, iconWidth, iconHeight);
			CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox_dialog); 
			checkBox.setText(appInfo.appName);
			checkBox.setTextColor(Color.BLACK);
			checkBox.setCompoundDrawables(icon, null, null, null); //设置左图标
			
			if ( AISC.allApps.findAppInfo(appInfo.packageName).visible == true )
				checkBox.setChecked(true);
			else
				checkBox.setChecked(false);
			
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			{
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean visible)
				{
					AISC.allApps.findAppInfo(appInfo.packageName).visible = visible;
				}
			});
			
			return convertView;
		}
	}
	
}
