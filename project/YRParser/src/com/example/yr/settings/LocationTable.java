package com.example.yr.settings;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class LocationTable {

	// Database table
	public static final String TABLE_LOCATION = "location";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_SUMMARY = "summary";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_LOCATION + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_SUMMARY
			+ " text not null" + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(LocationTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
		onCreate(database);
	}
}