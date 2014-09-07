package com.example.exercise8_launcher5;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;


public class AppInfoProvider extends ContentProvider
{
	LauncherApplication LA;
	SQLiteDatabase database;
	iDBHelper dbHelper;

	@Override
	public boolean onCreate()
	{
		LA = (LauncherApplication) getContext();
		dbHelper = new iDBHelper(getContext());
		database = dbHelper.getWritableDatabase();
		return true;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values)
	{
		//SQLiteDatabase db = dbHelper.getWritableDatabase();
		// fix me
		//database.insert("", "", values);
		Log.i("", "testing: in AppInfoProvider.onCreate~!");
		
		return null;
	}
	
	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs)
	{
		//SQLiteDatabase db = dbHelper.getWritableDatabase();
		// fix me
		int count = 0;
		count = database.delete("", whereClause, whereArgs);
		
		return count;
	}

	@Override
	public Cursor query(Uri uri, String[] columns,
			String selection, String[] selectionArgs, String sortOrder)
	{
		// fix me
		return database.query
				("", columns, selection, selectionArgs, null, null, sortOrder);
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs)
	{
		// fix me
		int count = 0;
		count = database.update("", values, whereClause, whereArgs);
		
		return count;
	}
	
	@Override
	public String getType(Uri uri)
	{	return null;	}
	

	
	
	
	public static class iDBHelper extends SQLiteOpenHelper
	{	
		final static String DATABASE_NAME = "AppInfoLists.db";
		final static int DATABASE_VERSION = 1;
		
		final static String TABLE_ALL_APPS = "AllApps";
		final static String TABLE_LISTAPPS = "ListApps";
		final static String TABLE_DESKTOP = "Desktop";
		
		final static String TABLE_HEADER_PACKAGE_NAME = "PackageName";
		final static String TABLE_HEADER_IS_IN_LISTAPPS = "IsInListapps";
		
		
		public iDBHelper(Context context)
		{	
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{	
			String createAllAppsTable = "create table if not exists " + TABLE_ALL_APPS + 
					" (" + TABLE_HEADER_PACKAGE_NAME + " text primary key, "
						 + TABLE_HEADER_IS_IN_LISTAPPS + "text" + ");";
			String createListAppsTable = "create table if not exists " + TABLE_LISTAPPS + 
					" (" + TABLE_HEADER_PACKAGE_NAME + " text primary key" + ");";
			String createDesktopTable = "create table if not exists " + TABLE_DESKTOP + 
					" (" + TABLE_HEADER_PACKAGE_NAME + " text primary key" + ");";
			
			db.execSQL(createAllAppsTable);
			db.execSQL(createListAppsTable);
			db.execSQL(createDesktopTable);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("drop table if exists " + TABLE_ALL_APPS + ";");
			db.execSQL("drop table if exists " + TABLE_LISTAPPS + ";");
			db.execSQL("drop table if exists " + TABLE_DESKTOP + ";");
			onCreate(db);
		}
	}
	
	
}
