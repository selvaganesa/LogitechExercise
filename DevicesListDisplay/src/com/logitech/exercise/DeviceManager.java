package com.logitech.exercise;

import java.io.File;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.List;

import com.logitech.exercise.common.Constants;
import com.logitech.exercise.database.Device;
import com.logitech.exercise.database.DeviceDatabaseWrapper;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public final class DeviceManager implements IDeviceManagerInterface, IJsonResponseCallback, Serializable {

	private static final String URL = "http://s3.amazonaws.com/harmony-recruit/devices.json";
	private static DeviceManager mDataManager;
	private Context mContext;
	private DeviceDatabaseWrapper mDeviceContentProvider;

	private DeviceManager(Context context) {
		// If we use lazy initialize this check is not required
		if (mDataManager != null) {
			throw new IllegalStateException("Already created.");
		}
		mContext = context.getApplicationContext();
		mDeviceContentProvider = DeviceDatabaseWrapper.getInstance(mContext, getStoreRoot());
	}

	/**
	 * 
	 * @return
	 */
	public synchronized static DeviceManager getInstance(Context context) {
		if (mDataManager == null)
			mDataManager = new DeviceManager(context);
		return mDataManager;
	}

	/**
	 * 
	 * @return
	 */
	public static String getStoreRoot() {
		String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
				+ Constants.DEVICE_FOLDER;
		return storagePath;
	}
	
	@Override
	public synchronized void syncDevices(Handler handler) {
		Thread workerThread = new Thread(new DeviceJsonReader(URL, this,handler));
		workerThread.setName("DeviceJsonParser");
		workerThread.start();
	}
	
	@Override
	public void syncDevices() {
		Thread workerThread = new Thread(new DeviceJsonReader(URL, this,null));
		workerThread.setName("DeviceJsonParser");
		workerThread.start();
	}
	
	

	@Override
	public List<Device> getAllDevices() {
		return mDeviceContentProvider.getAllDevices();
	}

	@Override
	public List<Device> getDevicesByType(String type) {
		return mDeviceContentProvider.getDevicesByType(type);
	}

	@Override
	public List<Device> getDevicesByModel(String model) {
		return mDeviceContentProvider.getDevicesByModel(model);
	}

	@Override
	public Device getDevice(String name) {
		return mDeviceContentProvider.getDevice(name);
	}

	@Override
	public long insertDevice(Device device) {
		return mDeviceContentProvider.insertDevice(device);
	}

	@Override
	public boolean inserDevices(List<Device> devices) {
		return mDeviceContentProvider.inserDevices(devices);
	}

	@Override
	public boolean updateDevice(Device device) {
		return mDeviceContentProvider.updateDevice(device);
	}

	@Override
	public boolean deleteDevice(String name) {
		return mDeviceContentProvider.deleteDevice(name);
	}

	@Override
	public boolean clearAllDevies() {
		return mDeviceContentProvider.clearAllDevies();
	}

	// To avoid singleton break
	private Object readResolve() throws ObjectStreamException {
		return mDataManager;
	}

	private Object writeReplace() throws ObjectStreamException {
		return mDataManager;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Singleton, cannot be clonned");
	}

	@Override
	public void parsedDeviceList(List<Device> devices, Handler handler) {
		mDeviceContentProvider.clearAllDevies();
		mDeviceContentProvider.inserDevices(devices);
		if(handler!=null){
			Message msg = new Message();
			msg.what = DeviceJsonReader.DEVICE_PASRSING_DONE;
			msg.obj = devices;
			handler.sendMessage(msg);
		}else{
			Intent intent = new Intent();
			intent.setAction(Constants.MESSAGE_BROADCAST_TAG);
			intent.putExtra(DeviceJsonReader.STATUS, DeviceJsonReader.DEVICE_PASRSING_DONE);
			// intent.putExtra(DeviceJsonReader.DEVICE_LIST, value)
			mContext.sendBroadcast(intent);
		}
	}
	@Override
	public void errorDisplay(int errorType, String errorMsg,Handler handler) {		
		if(handler!=null){
			Message msg = new Message();
			msg.what = DeviceJsonReader.DEVICE_PASRSING_ERROR;
			msg.obj = errorMsg;
			handler.sendMessage(msg);
		}else{
			Intent intent = new Intent();
			intent.setAction(Constants.MESSAGE_BROADCAST_TAG);
			intent.putExtra(DeviceJsonReader.STATUS, DeviceJsonReader.DEVICE_PASRSING_ERROR);
			intent.putExtra(DeviceJsonReader.ERROR_MESSAGE, errorMsg);
			intent.putExtra(DeviceJsonReader.ERROR_TYPE, errorType);
			mContext.sendBroadcast(intent);
		}
	}


}
