package com.logitech.exercise;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.logitech.exercise.common.Constants;
import com.logitech.exercise.database.Device;

import android.os.Handler;
import android.util.JsonReader;

public class DeviceJsonReader implements Runnable {
	private IJsonResponseCallback mCallback;
	private String urlString;
	private Handler handler;

	public static final String STATUS = "STATUS";
	public static final String DEVICE_LIST = "DEVICE_LIST";
	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
	public static final String ERROR_TYPE = "ERROR_TYPE";
	public static final int DEVICE_PASRSING_DONE = 0;
	public static final int DEVICE_PASRSING_ERROR = 1;

	public static int ERROR_HTTP_CONNECT = 0;
	public static int ERROR_JSON_PARSE = 1;
	public static int ERROR_TODO = 2;

	/**
	 * 
	 * @param url
	 * @param callback
	 * @param handler
	 */
	public DeviceJsonReader(String url, IJsonResponseCallback callback, Handler handler) {
		mCallback = callback;
		urlString = url;
		this.handler = handler;
	}

	@Override
	public void run() {
		boolean isSuccess = true;
		int errorType = ERROR_HTTP_CONNECT;
		String errorMsg = "";
		HttpURLConnection httpUrlConnection = null;
		InputStream inStream = null;
		List<Device> devices = null;
		try {
			// inStream = getDevicesFromServer(urlString);
			// InputStream inStream = null;
			URL url = new URL(urlString);
			httpUrlConnection = (HttpURLConnection) url.openConnection();
			inStream = new BufferedInputStream(httpUrlConnection.getInputStream());

		} catch (HttpRetryException e) {
			isSuccess = false;
			errorType = ERROR_HTTP_CONNECT;
			errorMsg = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			isSuccess = false;
			errorType = ERROR_HTTP_CONNECT;
			errorMsg = e.getMessage();
			e.printStackTrace();
		} catch (Exception e) {
			isSuccess = false;
			errorType = ERROR_HTTP_CONNECT;
			errorMsg = e.getMessage();
			e.printStackTrace();
		}
		if (inStream != null)
			try {
				devices = readAllDevices(inStream);
			} catch (UnsupportedEncodingException e) {
				isSuccess = false;
				errorType = ERROR_JSON_PARSE;
				errorMsg = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				errorType = ERROR_JSON_PARSE;
				errorMsg = e.getMessage();
				isSuccess = false;
				e.printStackTrace();
			} catch (Exception e) {
				isSuccess = false;
				errorType = ERROR_JSON_PARSE;
				errorMsg = e.getMessage();
				e.printStackTrace();
			}

		if (httpUrlConnection != null)
			httpUrlConnection.disconnect();

		try {
			if (inStream != null)
				inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (isSuccess)
			mCallback.parsedDeviceList(devices, handler);
		else
			mCallback.errorDisplay(errorType, errorMsg, handler);

	}

	/**
	 * Connect to that given URL and return Input stream
	 * 
	 * @return
	 */
	private InputStream getDevicesFromServer(String urlString) throws HttpRetryException, IOException, Exception {
		HttpURLConnection httpUrlConnection = null;
		InputStream inStream = null;
		try {
			URL url = new URL(urlString);
			httpUrlConnection = (HttpURLConnection) url.openConnection();
			inStream = new BufferedInputStream(httpUrlConnection.getInputStream());
		} finally {
			// httpUrlConnection.disconnect();
		}
		return inStream;
	}

	/**
	 * 
	 * @return
	 */
	private List<Device> readAllDevices(InputStream inStream)
			throws UnsupportedEncodingException, IOException, Exception {
		JsonReader jsonReader = null;
		try {
			jsonReader = new JsonReader(new InputStreamReader(inStream, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<Device> devices = null;
		try {
			jsonReader.beginObject();
			while (jsonReader.hasNext()) {
				String deviceName = jsonReader.nextName();
				if (deviceName.equals("devices")) {
					devices = readDevices(jsonReader);
				}
			}
			jsonReader.endObject();
			// inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return devices;
	}

	/**
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public List<Device> readDevices(JsonReader reader) throws IOException {
		List<Device> devices = new ArrayList<Device>();
		String name = null;
		String type = null;
		String model = null;
		reader.beginArray();
		while (reader.hasNext()) {
			reader.beginObject();
			while (reader.hasNext()) {
				String temp = reader.nextName();
				if (temp.equals(Constants.NAME)) {
					name = reader.nextString();
				} else if (temp.equals(Constants.DEVICETYPE)) {
					type = reader.nextString();
				} else if (temp.equals(Constants.MODEL)) {
					model = reader.nextString();
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
			Device device = new Device(name, model, type);
			devices.add(device);
		}
		reader.endArray();
		return devices;
	}

}
