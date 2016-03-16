package com.logitech.exercise.database;

import java.util.List;

public interface IDeviceDatabaseInterface {
	/**
	 * Get all devices in the table
	 * @return List<Device>
	 */
	List<Device> getAllDevices();	
	/**
	 * Get all devices which are filtered by type
	 * @param type
	 * @return List<Device>
	 */
	List<Device> getDevicesByType(String type);
	/**
	 * Get all devices which are filtered by model
	 * @param model
	 * @return
	 */
	List<Device> getDevicesByModel(String model);
	/**
	 * Get a device by name
	 * @param name
	 * @return
	 */
	Device getDevice(String name);
	/**
	 * Insert single device
	 * @return
	 */
	long insertDevice(Device device);
	/**
	 * Insert list of devices
	 * @param devices
	 * @return true - success, false - insertion failed
	 */
	boolean inserDevices(List<Device> devices);
	/**
	 * Update a particular device 
	 * @return true - success, false - insertion failed
	 */
	boolean updateDevice(Device device);
	/**
	 * Delete given device
	 * @return true - success, false - update failed
	 */
	boolean deleteDevice(String name);
	/**
	 * Delete all devices in the device table
	 * @return true - success, false - delete failed
	 */
	boolean clearAllDevies();
}
