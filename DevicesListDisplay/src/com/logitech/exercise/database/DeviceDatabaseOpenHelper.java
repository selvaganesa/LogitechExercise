
package com.logitech.exercise.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DeviceDatabaseOpenHelper extends SQLiteOpenHelper {

	// Database name
	public static String DATABASE_NAME = "Devices";
	// Device table
	public static String TABLE_NAME = "device";
	public static String NAME = "Name";
	public static String TYPE = "Type";
	public static String MODEL = "Model";

	private static int VERSION = 1;
	private static String DATABASE_PATH = File.separator + DATABASE_NAME + ".sqlite";

	public DeviceDatabaseOpenHelper(Context context, String projectPath) {
		super(context, projectPath + DATABASE_PATH, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createDeviceTable(db);
	}

	/**
	 * 
	 * @param db
	 */
	private void createDeviceTable(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + NAME + " TEXT PRIMARY KEY NOT NULL, " + // 1://
																															// Name
				TYPE + " TEXT, " + // 2: Type
				MODEL + " TEXT); "; // 3: Model
		db.execSQL(CREATE_TABLE);
	}

	/** Drops the underlying database table. */
	public void dropDeviceTable(SQLiteDatabase db, boolean ifExists) {
		Log.i("DropTab", "DropTab - Drop Table ?");
		String sql = "DROP TABLE IF EXISTS " + "'" + TABLE_NAME + "'";
		db.execSQL(sql);
		createDeviceTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("onUpgrade", "DropTab -onUpgrade oldVersion newVersion  ?" + oldVersion + "," + newVersion);
		dropDeviceTable(db, true);
		createDeviceTable(db);
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i("onUpgrade", "DropTab - onDowngrade oldVersion newVersion  ?" + oldVersion + "," + newVersion);
		dropDeviceTable(db, true);
		createDeviceTable(db);
	}
}
