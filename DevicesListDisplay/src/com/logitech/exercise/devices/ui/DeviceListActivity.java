package com.logitech.exercise.devices.ui;

import java.util.ArrayList;
import java.util.List;

import com.logitech.exercise.DeviceJsonReader;
import com.logitech.exercise.DeviceManager;
import com.logitech.exercise.LocalMessageReceiver;
import com.logitech.exercise.common.Constants;
import com.logitech.exercise.common.Utils;
import com.logitech.exercise.database.Device;
import com.logitech.exercise.devices.R;
import com.logitech.exercise.devices.ui.fragments.DeviceListFragment;
import com.logitech.exercise.devices.ui.fragments.DeviceListFragment.IDeviceListCallbacks;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DeviceListActivity extends Activity implements IDeviceListCallbacks {
	private static final String TAG = "DeviceListActivity";
	private Handler localHandler;
	private DeviceManager mDeviceManager;
	private DeviceListFragment mDeviceListFragment;
	private Toast mToast;
	private boolean isHandlerCallbackMethod = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_devicelist);
		localHandler = new LocalHandler();
		mDeviceManager = DeviceManager.getInstance(this);
		// find the retained fragment on activity create
		// If savedInstanceState null then it is fresh start
		if (savedInstanceState == null) {
			if (Utils.isInternetAvailable(this)) {
				if (isHandlerCallbackMethod)
					mDeviceManager.syncDevices(localHandler);
				else
					mDeviceManager.syncDevices();
			} else {
				showToast(getString(R.string.no_internet_connection));
				updateList();
			}
		} else {
			// FragmentManager fm = getFragmentManager();
			// mDeviceListFragment = (DeviceListFragment)
			// fm.findFragmentByTag(Constants.FRAG_DEVICE_LIST_TAG);
			showToast("Fragment retained.");
		}
	}

	@Override
	protected void onStart() {
		registerReceiver(localMessenger, new IntentFilter(Constants.MESSAGE_BROADCAST_TAG));
		super.onStart();
	}

	@Override
	protected void onStop() {
		unregisterReceiver(localMessenger);
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * @param msg
	 * @param delay
	 */
	private void showToast(String msg) {
		if (mToast != null) {
			mToast.cancel();
			mToast = null;
		}
		mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		mToast.show();
	}

	/**
	 * Sync Broadcast receiver
	 */
	protected BroadcastReceiver localMessenger = new LocalMessageReceiver() {
		public void onReceive(Context arg0, Intent intent) {
			Log.d(TAG, "localMessenger info of broadcast DLA");
			if (intent != null) {
				int status = intent.getIntExtra(DeviceJsonReader.STATUS, -1);
				if (status == DeviceJsonReader.DEVICE_PASRSING_DONE) {
					updateList();
				} else {
					showToast(intent.getStringExtra(DeviceJsonReader.ERROR_MESSAGE));
				}
			}
		}
	};

	/**
	 *
	 */
	private class LocalHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.d(TAG, "LocalHandler info of broadcast DLA");
			super.handleMessage(msg);
			if (msg == null)
				return;
			switch (msg.what) {
			case DeviceJsonReader.DEVICE_PASRSING_DONE:
				updateList();
				break;
			case DeviceJsonReader.DEVICE_PASRSING_ERROR:
				showToast((String) msg.obj);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 
	 */
	private void updateList() {
		List<Device> devices = mDeviceManager.getAllDevices();
		if (devices != null && devices.size() > 0) {
			if (mDeviceListFragment == null) {
				mDeviceListFragment = DeviceListFragment.newInstance((ArrayList<Device>) devices);
			}
			if (!mDeviceListFragment.isAdded())
				addFragmentToView(mDeviceListFragment, Constants.FRAG_DEVICE_LIST_TAG, R.id.frag_container_device_list);
			else
				mDeviceListFragment.updateDeviceList(devices);
		} else {
			showToast(getString(R.string.empty_devices));
		}
	}

	/**
	 * 
	 * @param fragment
	 * @param tag
	 * @param containerId
	 */
	protected void addFragmentToView(Fragment fragment, String tag, int containerId) {
		try {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(containerId, fragment);
			fragmentTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDeviceClicked(Device device) {
		showToast("Clicked Device : " + device.getName());
	}

	@Override
	public void onDeviceLongPressed(Device device) {
		// TODO Auto-generated method stub
	}
}
