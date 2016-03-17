
package com.logitech.exercise.database;

import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public final class DeviceDatabaseWrapper implements IDeviceDatabaseInterface {
	
    private static String TAG = "DeviceTableWrapper";
    private DeviceDatabaseOpenHelper mDeviceOpenHelper;
    private SQLiteDatabase mDeviceDatabase;

    private static DeviceDatabaseWrapper mDeviceContentProvider;

    private DeviceDatabaseWrapper(Context context, String ProjectPath) {
        mDeviceOpenHelper = new DeviceDatabaseOpenHelper(context, ProjectPath);
        mDeviceDatabase = mDeviceOpenHelper.getWritableDatabase();
    }
    /**
     * @param context
     * @param ProjectPath
     * @return
     */
    public static synchronized DeviceDatabaseWrapper getInstance(Context context,
            String ProjectPath) {
        if (mDeviceContentProvider == null) {
            Log.i(TAG, "TABLE Created SNS content provider");
            mDeviceContentProvider = new DeviceDatabaseWrapper(context, ProjectPath);
        }
        return mDeviceContentProvider;
    }

	@Override
	public List<Device> getAllDevices() {
		 Cursor cursor = mDeviceDatabase
	             .query(DeviceDatabaseOpenHelper.TABLE_NAME, null,null,null, null,
	                     null, null);
		return getDevicesFromCursor(cursor);
	}

	@Override
	public List<Device> getDevicesByType(String type) {
		Cursor cursor = mDeviceDatabase
	             .query(DeviceDatabaseOpenHelper.TABLE_NAME, null, DeviceDatabaseOpenHelper.TYPE + " = '" + type + "'",null, null,
	                     null, null);
		return getDevicesFromCursor(cursor);
	}

	@Override
	public List<Device> getDevicesByModel(String model) {
		Cursor cursor = mDeviceDatabase
	             .query(DeviceDatabaseOpenHelper.TABLE_NAME, null, DeviceDatabaseOpenHelper.MODEL + " = '" + model + "'",null, null,
	                     null, null);
		return getDevicesFromCursor(cursor);
	}
	@Override
	public Device getDevice(String name) {
		 Cursor cursor = mDeviceDatabase
	             .query(DeviceDatabaseOpenHelper.TABLE_NAME, null,null,null, null,
	                     null, null);
		 Device device = null;
		 List<Device> devices = getDevicesFromCursor(cursor);
		 if(devices!=null && devices.size()>0)
			 device = devices.get(1);
		return device ;
	}
	
	private List<Device> getDevicesFromCursor(Cursor cursor) {
		List<Device> deviceList = null;
		if (cursor != null) {
			deviceList = new ArrayList<Device>(cursor.getCount());
			int nameIndex = cursor.getColumnIndex(DeviceDatabaseOpenHelper.NAME);
			int typeIndex = cursor.getColumnIndex(DeviceDatabaseOpenHelper.TYPE);
			int modelIndex = cursor.getColumnIndex(DeviceDatabaseOpenHelper.MODEL);

			if (cursor.moveToFirst()) {
				do {
					Device device = new Device();
					device.setName(cursor.getString(nameIndex));
					device.setType(cursor.getString(typeIndex));
					device.setModel(cursor.getString(modelIndex));
					deviceList.add(device);

				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return deviceList;
	}
	
	@Override
	public long insertDevice(Device device) {
		return insertOrUpdate(device);
	}	
	/**
	 * 
	 */
	private long insertOrUpdate(Device device) {
		long id = -1;
		if (device != null) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(DeviceDatabaseOpenHelper.NAME, device.getName());
			contentValues.put(DeviceDatabaseOpenHelper.TYPE, device.getName());
			contentValues.put(DeviceDatabaseOpenHelper.TYPE, device.getModel());
			// this will insert if record is new, update otherwise
			id = mDeviceDatabase.insertWithOnConflict(DeviceDatabaseOpenHelper.TABLE_NAME, null, contentValues,
					SQLiteDatabase.CONFLICT_REPLACE);
		}
		return id;
	}

	@Override
	public boolean inserDevices(List<Device> devices) {
		boolean isSuccess = false;
		if (devices != null && devices.size() > 0) {
			String query = "INSERT or REPLACE INTO " + DeviceDatabaseOpenHelper.TABLE_NAME + " ("
					+ DeviceDatabaseOpenHelper.NAME + ", " + DeviceDatabaseOpenHelper.TYPE + ", "
					+ DeviceDatabaseOpenHelper.MODEL + ") values (?,?,?);";
			mDeviceDatabase.beginTransaction();
			SQLiteStatement statement = mDeviceDatabase.compileStatement(query);
			try {
				for (Device device : devices) {
					statement.bindString(1, device.getName());
					statement.bindString(2, device.getType());
					statement.bindString(3, device.getModel());
					statement.executeInsert();
				}
				if (statement != null)
					statement.close();
				mDeviceDatabase.setTransactionSuccessful();
				mDeviceDatabase.endTransaction();
				isSuccess = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isSuccess;
	}

	@Override
	public boolean updateDevice(Device device) {
		boolean isDone = insertOrUpdate(device)!=-1?true:false;
		return isDone;
	}

	@Override
	public boolean deleteDevice(String name) {
		boolean isDeleted = false;
		try {
			mDeviceDatabase.delete(DeviceDatabaseOpenHelper.TABLE_NAME,
					DeviceDatabaseOpenHelper.NAME + " = '" + name + "'", null);
			isDeleted = true;
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return isDeleted;
	}
	

	@Override
	public boolean clearAllDevies() {
		mDeviceOpenHelper.dropDeviceTable(mDeviceDatabase, true);
		return false;
	}

	
	private Object readResolve() throws ObjectStreamException {
		return mDeviceContentProvider;
	}

	private Object writeReplace() throws ObjectStreamException {
		return mDeviceContentProvider;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Singleton, cannot be clonned");
	}
}
