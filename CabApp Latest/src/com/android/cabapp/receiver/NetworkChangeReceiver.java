package com.android.cabapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (!isOnline(context)) {
			// Do something
			Log.d("Netowk Available ", "Flag No 1");
			Toast.makeText(context, "No internet connection available, please try later",Toast.LENGTH_LONG).show();
		}else{
			Log.d("Netowk Available ", "Flag No 2");
			//Toast.makeText(context, "Internet connection available",Toast.LENGTH_LONG).show();
		}
	}

	public boolean isOnline(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		// should check null because in air plan mode it will be null
		return (netInfo != null && netInfo.isConnected());

	}
}
