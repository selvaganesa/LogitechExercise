package com.logitech.exercise;

import java.util.List;

import com.logitech.exercise.database.Device;

import android.os.Handler;

public interface IJsonResponseCallback {
	/**
	 * Result from Json parser
	 * @param device
	 * @param handler
	 */
	void parsedDeviceList(List<Device> device,Handler handler); // If handler is null app will broadcast thro receiver
	
	/**
	 *  Error display
	 * @param errorType
	 * @param errorMsg
	 */
	void errorDisplay(int errorType, String errorMsg,Handler handler);
}
