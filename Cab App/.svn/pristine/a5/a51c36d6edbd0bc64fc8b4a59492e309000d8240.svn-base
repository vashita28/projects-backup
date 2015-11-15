package com.android.cabapp.service;

import java.util.Collection;
import java.util.HashMap;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class CabAppServiceConnection {
	private static CabAppService mBoundService;
	public static Activity currentActivity;

	/**
	 * Get the connection to the IMService.
	 */
	public static ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.d("CabAppServiceConnection-onServiceConnected()",
					"S e r v i c e C o n n e c t e d");
			mBoundService = ((CabAppService.LocalBinder) service).getService();
		}

		public void onServiceDisconnected(ComponentName className) {
			Log.d("CabAppServiceConnection-onServiceDisconnected()",
					"S e r v i c e D i s c o n n e c t e d");
			mBoundService = null;
		}
	};
	private static HashMap<String, Activity> hashMap = new HashMap<String, Activity>();

	/**
	 * Method to start the service.
	 */
	public static void startService(Activity startActivity) {
		Intent intent = new Intent(CabAppService.class.getName());
		currentActivity = startActivity;
		hashMap.put(startActivity.getClass().toString(), startActivity);
		Log.d("CabAppServiceConnection-startService()",
				"S e r v i c e   s t a r t e d - "
						+ startActivity.getClass().toString());
		startActivity.startService(intent);
		startActivity
				.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

	}

	public static void stopService(Activity startActivity) {
		unBindService(startActivity);
		startActivity.stopService(new Intent(CabAppService.class.getName()));
		Collection<Activity> values = hashMap.values();
		for (Activity activity : values) {
			try {
				activity.finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.d("CabAppServiceConnection-stopService()",
				"S e r v i c e   S t o p p e d");
	}

	public static void unBindService(Activity startActivity) {
		startActivity.unbindService(mConnection);
		Log.d("CabAppServiceConnection-unBindService()",
				"S e r v i c e   U n B i n d");
	}

}
