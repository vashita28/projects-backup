package uk.co.pocketapp.whotel;

import uk.co.pocketapp.whotel.util.Util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mblox.engage.MbloxManager;

public class GCMBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Util.getAgreeContinueAccepted(context)) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(GoogleCloudMessaging.getInstance(context)
							.getMessageType(intent))) {
				MbloxManager.getInstance(context).receivedGCMMessage(intent);
				MbloxManager.getInstance(context).sync(intent);
			}
		}
	}
}
