package com.coudriet.tapsnap.android;

import com.coudriet.tapsnap.fragments.InboxFragment;
import com.coudriet.tapsnap.fragments.InboxFragment.InboxReceiver;
import com.parse.PushService;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PushReceiver extends BroadcastReceiver {
	private static final String TAG = "IntentReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e(TAG, "PushReceiver:BroadcastReceiver");
		if(MainActivity.isActivityVisible)
		{
		NotificationManager mNotificationManager = null ;
		try
		{
		mNotificationManager =
		        (NotificationManager) context.getSystemService(PushService.NOTIFICATION_SERVICE);
		}
		catch(Exception ex)
		{
			
		}
		if(mNotificationManager!=null)
		mNotificationManager.cancelAll();
		try {
			 if (InboxFragment.mUpdateListHandler != null)
			 InboxFragment.mUpdateListHandler.sendEmptyMessage(0);

			//Intent intent2 = new Intent();
			//intent2.setAction("com.coudriet.tapsnap.android.UPDATE_LIST");
			//context.sendBroadcast(intent2);
		} catch (Exception e) {
			Log.d(TAG, "Exception: " + e.getMessage());
		}
		}
	}


}