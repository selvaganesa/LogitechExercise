package com.logitech.exercise;

import com.logitech.exercise.database.IDeviceDatabaseInterface;

import android.os.Handler;

public interface IDeviceManagerInterface extends IDeviceDatabaseInterface {
	/**
	 * This is used if handler mode has been enabled in the UI
	 * 
	 * @param handler
	 */
	void syncDevices(Handler handler); // Type 1 - using handler

	/**
	 * This is used for local broadcast receiver no need to pass handler
	 */
	void syncDevices(); // Type 2 - using LocalBroadCastReceiver
}
