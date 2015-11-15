package co.uk.android.lldc.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import co.uk.android.lldc.service.LLDC_Sync_Servcie;
import co.uk.android.lldc.utils.Utils;

public class NetworkChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, final Intent intent) {
//		android.os.Debug.waitForDebugger();s
		Boolean status = Utils.isConnectingToInternet(context);
		if (status) {
			try {
				Intent msgIntent = new Intent(context, LLDC_Sync_Servcie.class);
				context.startService(msgIntent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}