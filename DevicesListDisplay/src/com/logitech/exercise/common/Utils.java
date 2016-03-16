package com.logitech.exercise.common;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isInternetAvailable(final Context context) {
		final ConnectivityManager connectivityManager = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE));
		return connectivityManager.getActiveNetworkInfo() != null
				&& connectivityManager.getActiveNetworkInfo().isConnected();
	}
}
