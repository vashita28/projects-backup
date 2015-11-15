package uk.co.pocketapp.whotel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mblox.engage.util.SyncOnLocationChangeService;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		SyncOnLocationChangeService.setInterval(context, (5 * 60 * 1000));
		SyncOnLocationChangeService.setMinDistance(context, 200);
		SyncOnLocationChangeService.startService(context);
	}
}
