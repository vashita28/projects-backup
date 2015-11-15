package com.handpoint.util;

import java.util.Iterator;

import com.handpoint.headstart.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 
 * Class to notify the user of device status changed. 
 *
 */
public class NotificationMgr {

	public static final String EXTRA_BUNDLE = "com.handpoint.util.EXTRA_BUNDLE";
	
	private final static int STATUS_NOTIFICATION_ID = 1;
	
	private static NotificationMgr instance;
	private Context ctx;
	private NotificationManager notificationManager;

	private NotificationMgr(Context ctx) {
		super();
		this.ctx = ctx;
		this.notificationManager = (NotificationManager) this.ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * Initializes notification manager 
	 * @param ctx - android context
	 */
	public static void init(Context ctx) {
		instance = new NotificationMgr(ctx);
	}

	/**
	 * Returns instance of notification manager
	 * @return NotificationMgr notification manager
	 */
	public static NotificationMgr getInstance() {
		return instance;
	}
	/**
	 * Post a notification to be shown in the status bar.
	 * @param pattern - pattern for message
	 * @param action - action for intent which will be sent after click on item in status bar
	 * @param data - message
	 * @param icon - icon that will be showed in status bar
	 */
	public void notifyStatusChanged(String pattern, String action, String data, int icon, Bundle bundle) {
		Notification notif = new Notification();
		notif.flags |= Notification.FLAG_ONGOING_EVENT;
 		Intent notificationIntent = new Intent(action);
 		putBundleContext(bundle, notificationIntent);
 		notif.when=System.currentTimeMillis();
 		notif.icon = icon;
 		PendingIntent pi = PendingIntent.getActivity(ctx, 0, notificationIntent, 0);
 		String contentText;
 		if (TextUtils.isEmpty(pattern)) {
 			contentText = data;
 		} else {
 			contentText = String.format(pattern, data);
 		}
 		notif.setLatestEventInfo(
				ctx, 
				ctx.getResources().getText(R.string.app_name),  
				contentText, 
				pi);
		notificationManager.notify(STATUS_NOTIFICATION_ID, notif);
	}

	private void putBundleContext(Bundle bundle, Intent notificationIntent) {
		if (null != bundle) {
 			for(Iterator<String> iterator = bundle.keySet().iterator(); iterator.hasNext();) {
 				String key = iterator.next();
 				Object value = bundle.get(key);
 				if (value instanceof String) {
 					notificationIntent.putExtra(key, (String) value);					
				} else if (value instanceof Boolean) {
					notificationIntent.putExtra(key, (Boolean) value);
				} else if (value instanceof Integer) {
					notificationIntent.putExtra(key, (Integer) value);
				} else if (value instanceof Parcelable) {
					notificationIntent.putExtra(key, (Parcelable) value);
				}
 				//TODO: add other types
 			} 			
 		}
	}
	/**
	 * Cancel a previously shown notification.
	 */
	public void cancelStatusChanged() {
		notificationManager.cancel(STATUS_NOTIFICATION_ID);
	}
}
