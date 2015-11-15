package co.uk.peeki.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkBroadcastReceiver extends BroadcastReceiver {
	public static boolean ms_bIsNetworkAvailable = true;

	private Context m_context = null;

	@Override
	public void onReceive(Context argContext, Intent argIntent) {
		Log.d("NetworkBroadcastReceiver-onReceive()", "o n R e c e i v e");
		m_context = argContext;
		ConnectivityManager connectivityManager = (ConnectivityManager) argContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Log.d("NetworkBroadcastReceiver-onReceive()", "activeNetInfo "
				+ activeNetInfo + " mobNetInfo " + mobNetInfo);

		if ((mobNetInfo != null && mobNetInfo.isAvailable() && mobNetInfo
				.isConnected())
				|| (activeNetInfo != null && activeNetInfo.isAvailable() && activeNetInfo
						.isConnected())
				|| (wifiInfo != null && wifiInfo.isAvailable() && wifiInfo
						.isConnected())) {
			ms_bIsNetworkAvailable = true;
			Log.d("NetworkBroadcastReceiver-onReceive()", "NETWORK AVAILABLE");

		} else {
			ms_bIsNetworkAvailable = false;

			Log.d("NetworkBroadcastReceiver-onReceive()",
					"NETWORK NOT AVAILABLE");
		}
	}

	public boolean IsNetworkAvailable() {
		if (!ms_bIsNetworkAvailable) {
			Toast.makeText(m_context, "NETWORK NOT AVAILABLE",
					Toast.LENGTH_LONG).show();
		}
		return ms_bIsNetworkAvailable;
	}

}
