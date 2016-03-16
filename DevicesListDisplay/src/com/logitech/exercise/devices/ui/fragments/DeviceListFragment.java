
package com.logitech.exercise.devices.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import com.logitech.exercise.database.Device;
import com.logitech.exercise.devices.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class DeviceListFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener {

	public static final String LEFT_DRAWER_POINTS_FRAGMENT = "LEFT_DRAWER_POINTS_FRAGMET";
	private static final String DEVICES = "DEVICES";

	private static DeviceListFragment mDeviceListFragment;
	private IDeviceListCallbacks mCallback;
	private List<Device> mDevices;
	private ListView mDeviceListView;
	private DeviceListAdapter mDeviceAdapter;

	public interface IDeviceListCallbacks {
		void onDeviceClicked(Device device);

		void onDeviceLongPressed(Device device);
	}

	/**
	 * 
	 * @param devices
	 * @return
	 */
	public static DeviceListFragment newInstance(ArrayList<Device> devices) {
		mDeviceListFragment = new DeviceListFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable(DEVICES, devices);
		mDeviceListFragment.setArguments(bundle);
		return mDeviceListFragment;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCallback = (IDeviceListCallbacks) getActivity();
		mDevices = (ArrayList<Device>) mDeviceListFragment.getArguments().getSerializable(DEVICES);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_devices, null);

		mDeviceListView = (ListView) view.findViewById(R.id.frag_devices_listview);
		mDeviceAdapter = new DeviceListAdapter(getActivity());
		mDeviceListView.setAdapter(mDeviceAdapter);
		mDeviceListView.setOnItemClickListener(this);
		mDeviceListView.setOnItemLongClickListener(this);
		 // retain this fragment
        setRetainInstance(true);
		return view;
	}

	/**
	 * 
	 * @param devices
	 */
	public void updateDeviceList(List<Device> devices) {
		this.mDevices = devices;
		mDeviceAdapter.notifyDataSetChanged();
	}

	private class DeviceListAdapter extends BaseAdapter {
		private LayoutInflater mLayoutInflater;

		private class HolderView {
			TextView tvName;
			TextView tvType;
			TextView tvModel;
		}

		public DeviceListAdapter(Context context) {
			mLayoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mDevices.size();
		}

		@Override
		public Object getItem(int position) {
			return mDevices.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holder;
			if (null == convertView) {
				convertView = mLayoutInflater.inflate(R.layout.list_item_device, null);
				holder = new HolderView();
				holder.tvName = (TextView) convertView.findViewById(R.id.list_item_tv_device_name);
				holder.tvType = (TextView) convertView.findViewById(R.id.list_item_tv_device_type);
				holder.tvModel = (TextView) convertView.findViewById(R.id.list_item_tv_device_model);

				convertView.setTag(holder);
			} else {
				holder = (HolderView) convertView.getTag();
			}

			Device device = mDevices.get(position);

			holder.tvName.setText(getString(R.string.device_name_with_colon) + "\t" + device.getName());
			holder.tvModel.setText(getString(R.string.device_model_with_colon) + "\t" + device.getModel());
			holder.tvType.setText(getString(R.string.device_type_with_colon) + "\t" + device.getType());

			return convertView;
		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mCallback.onDeviceClicked(mDevices.get(position));
	}
}
