package uk.co.pocketapp.whotel;

import uk.co.pocketapp.whotel.fragments.DealsFragment;
import uk.co.pocketapp.whotel.ui.RootActivity;
import uk.co.pocketapp.whotel.ui.SplashActivity;
import uk.co.pocketapp.whotel.util.Util;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mblox.engage.MbloxManager;
import com.mblox.engage.SyncFuture;
import com.mblox.engage.SyncFutureListener;
import com.mblox.engage.SyncResult;
import com.mblox.engage.UINotification;
import com.mblox.engage.util.UINotificationHelper;

public class UINotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		if (Util.getAgreeContinueAccepted(context)) {
			UINotification notification = UINotificationHelper
					.createUINotification(intent);

			String lines[] = notification.getNotification().split("\\r?\\n");
			String notificationTitle = "";
			StringBuffer notificationText = new StringBuffer();
			if (lines != null) {
				notificationTitle = lines[0];
				for (int i = 1; i < lines.length; i++) {
					notificationText.append(lines[i]);
				}
			}

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context);
			mBuilder.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle(notificationTitle)
					.setContentText(notificationText).setAutoCancel(true);

			if (notification.getSound() != null
					&& notification.getSound().length() > 0) {
				mBuilder.setSound(alarmSound);
			}

			Intent startIntent = new Intent(context, SplashActivity.class);
			startIntent.putExtra("isPush", "true");
			startIntent.putExtra(UINotificationHelper.MSG_ID_INTENT_KEY,
					notification.getMsgID());
			startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);

			PendingIntent resultPendingIntent = PendingIntent.getActivity(
					context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager
					.notify(Integer.parseInt(notification.getMsgID()),
							mBuilder.build());

			MbloxManager.getInstance(context).sync();

			if (DealsFragment.mDealsRefreshHandler != null)
				DealsFragment.mDealsRefreshHandler.sendEmptyMessage(0);

			if (RootActivity.mSyncHandler != null)
				RootActivity.mSyncHandler.sendEmptyMessage(0);
			else
				syncMessagesFromMBlox(context);
			// UINotificationHelper.showUINotification(context,
			// UINotificationHelper.createUINotification(intent),
			// R.drawable.ic_launcher, SplashActivity.class);
		}
	}

	void syncMessagesFromMBlox(Context context) {
		Util.mContext = context;
		Location location = Util.getLocation();
		SyncFuture future = MbloxManager.getInstance(context).sync(location);
		future.addListener(new SyncFutureListener() {

			@Override
			public void operationComplete(SyncFuture future) {
				future.removeListener(this);
				SyncResult result = future.getSyncResult();
				if (SyncResult.Result.SUCCESS.equals(result.getResult())) {
					Log.v("UINotificationReceiver", "SYNC SUCCESS");
				} else if (result.getResult() == SyncResult.Result.FAILURE) {
					Log.v("UINotificationReceiver", "Sync failed: "
							+ result.getCause().getMessage());
				}
			}
		});
	}
}
