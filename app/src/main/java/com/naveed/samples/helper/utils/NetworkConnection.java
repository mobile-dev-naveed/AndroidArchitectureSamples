package com.naveed.samples.helper.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {

	private static NetworkConnection mCheckConnection;

	public static NetworkConnection getInstance() {
		if (mCheckConnection == null) {
			mCheckConnection=new NetworkConnection();
		}
		return mCheckConnection;
	}

	public static boolean isConnection(Context ctx) {
		return isNetworkConnected(ctx);
	}

	public static boolean isNetworkConnected(Context context){
		boolean isConnected =false;
		ConnectivityManager connMgr =
				(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			boolean wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
			boolean mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
			isConnected = wifiConnected||mobileConnected;
		}
		return isConnected;
	}

}
