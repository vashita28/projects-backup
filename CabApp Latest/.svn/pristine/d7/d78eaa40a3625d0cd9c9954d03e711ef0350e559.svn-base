package com.android.cabapp.receiver;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.cabapp.R;
import com.android.cabapp.activity.ChatActivity;
import com.android.cabapp.activity.LogInActivity;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.activity.RootActivity;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.urbanairship.UAirship;
import com.urbanairship.actions.ActionUtils;
import com.urbanairship.actions.DeepLinkAction;
import com.urbanairship.actions.LandingPageAction;
import com.urbanairship.actions.OpenExternalUrlAction;
import com.urbanairship.push.GCMMessageHandler;
import com.urbanairship.push.PushManager;

public class IntentReceiver extends BroadcastReceiver {

	private static final String TAG = "IntentReceiver";

	// A set of actions that launch activities when a push is opened. Update
	// with any custom actions that also start activities when a push is opened.
	private static String[] ACTIVITY_ACTIONS = new String[] { DeepLinkAction.DEFAULT_REGISTRY_NAME,
			OpenExternalUrlAction.DEFAULT_REGISTRY_NAME, LandingPageAction.DEFAULT_REGISTRY_NAME };

	Thread soundThread;
	String sound = "";
	Context context;

	@Override
	public void onReceive(Context mContext, Intent intent) {
		Log.i(TAG, "Received intent: " + intent.getExtras());
		context = mContext;

		String action = intent.getAction();
		String message = intent.getStringExtra(PushManager.EXTRA_ALERT);
		String messageType = intent.getStringExtra("type");
		String jobID = intent.getStringExtra("addInfo");

		if (Constants.isDebug) {
			Log.v(TAG, "=================================== PUSH MESSAGE ===================================");
			Log.v(TAG, "action		:: " + action);
			Log.v(TAG, "message		:: " + message);
			Log.v(TAG, "messagetype	:: " + messageType);
			Log.v(TAG, "=================================== PUSH MESSAGE ===================================");
		}

		if (action == null) {
			return;
		}

		if (action.equals(PushManager.ACTION_PUSH_RECEIVED)) {
			/*
			 * Check if the action = com.urbanairship.push.PUSH_RECEIVED
			 */
			int id = intent.getIntExtra(PushManager.EXTRA_NOTIFICATION_ID, 0);

			// int id = R.id.icon;
			/* play sound only for new job */
			sound = intent.getStringExtra("sound");
			soundForPush();

			if (Constants.isDebug) {
				Log.v(TAG, "=================================== PUSH MESSAGE ===================================");
				Log.v(TAG, "Alert		:: " + intent.getStringExtra(PushManager.EXTRA_ALERT));
				Log.v(TAG, "NotificationID		:: " + id);
				logPushExtras(intent);
				Log.v(TAG, "Job id		:: " + jobID);
				Log.v(TAG, "ChatThread current job ID		:: " + ChatActivity.szJobID);
				Log.v(TAG, "=================================== PUSH MESSAGE ===================================");
			}

			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager nMgr = (NotificationManager) UAirship.shared().getApplicationContext().getSystemService(ns);

			Boolean isTypeMessageToDriver = false, isTypeMessage = false, isTypeBookingCancelled = false, isTypeNewBooking = false, isUtilContextNotNULL = false, isUtilChatActivityFragment = false, isPushJobEquivalentToCurrent = false;
			try {
				isTypeMessageToDriver = messageType.equals(Constants.TYPE_MESSAGE_TO_DRIVER);
				isTypeMessage = messageType.equals(Constants.TYPE_MESSAGE);
				isTypeBookingCancelled = messageType.equals(Constants.TYPE_BOOKING_CANCELLED);
				isTypeNewBooking = messageType.equals(Constants.TYPE_NEW_BOOKINGS);
				isUtilContextNotNULL = Util.mContext != null;
				isUtilChatActivityFragment = Util.isOnChatActivityFragment;
				isPushJobEquivalentToCurrent = jobID.equals(ChatActivity.szJobID);
			} catch (Exception e) {
				// TODO: handle exception
			}

			if (isTypeMessageToDriver && isUtilContextNotNULL) {

				if (!isUtilChatActivityFragment) {
					Util.showChatDialog(Util.mContext, message, jobID);
				}

				if (isPushJobEquivalentToCurrent) {
					intent.setAction(Constants.NEW_MESSAGE);
					LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
					nMgr.cancel(id);
				}
				return;
			}

			if (isUtilContextNotNULL && isTypeBookingCancelled) {
				if (nMgr != null)
					nMgr.cancel(id);
				// nMgr.cancelAll();

				/* Vashita */
				/*
				 * Util.showAlertDialog(Util.mContext, "Booking with ID " +
				 * jobID + " has been cancelled");
				 */

				intent.setAction("NewJobs");
				LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

				/* Vashita */
				// intent.setAction("JobCancelled");
				// LocalBroadcastManager.getInstance(context)
				// .sendBroadcast(intent);
				if (Util.bIsJobAcceptedFragment) {
					Util.showCancelJobAlertDialogOk(Util.mContext);
				} else {
					Util.showCancelJobAlertDialog(Util.mContext);
				}

				return;
			}

			if (isUtilContextNotNULL && isTypeNewBooking) {
				if (!MainActivity.ms_bIsInBackground)
					nMgr.cancelAll();
				if (Util.bIsNowOrPreebookFragment) {
					intent.setAction("NewJobs");
					LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
				} else if (RootActivity.mContext != null) {
					RootActivity.showJobsAlertDialog();
				} else {
					Util.showJobsAlertDialog(Util.mContext);
					intent.setAction("NewJobs");
					LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
				}
				return;
			}

			if (Util.mContext != null && !isTypeMessageToDriver && !isTypeMessageToDriver && !isTypeMessage) {
				if (nMgr != null)
					nMgr.cancel(id);

				Util.showAlertDialog(Util.mContext, message);
			}

		} else if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {
			/*
			 * Check if the action = com.urbanairship.push.NOTIFICATION_OPENED
			 */
			Log.i(TAG, "User clicked notification. Message: " + intent.getStringExtra(PushManager.EXTRA_ALERT));
			logPushExtras(intent);

			// Only launch the main activity if the payload does not contain any
			// actions that might have already opened an activity

			if (!ActionUtils.containsRegisteredActions(intent.getExtras(), ACTIVITY_ACTIONS)) {
				Intent launch = new Intent(Intent.ACTION_MAIN);
				if (messageType.equals("message")) {
					launch.setClass(context, LogInActivity.class);
					launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					launch.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				} else {
					if (ChatActivity.mChatActivityContext != null) {
						// ChatActivity.finishMe();
						// Intent chatIntent = new Intent(context,
						// ChatActivity.class);
						// chatIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						ChatActivity.szJobID = jobID;
						intent.setAction("NewMessageFromOtherDriver");
						LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
						// chatIntent.putExtra(Constants.JOB_ID, jobID);
						// context.startActivity(chatIntent);
						return;
					}
					Log.e("IntentReceiver", "PUH OPENED redirect to MAIN");
					launch.setClass(context, MainActivity.class);
					launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					launch.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					launch.putExtra(Constants.JOB_ID, jobID);
					if (Util.mContext == null)
						launch.putExtra("appislaunchingfresh", true);

					if (messageType.equals("messagetodriver")) {
						launch.putExtra("isChatMessage", true);
					} else {
						AppValues.clearAllJobsList();
						launch.putExtra("isChatMessage", false);
					}
				}
				context.startActivity(launch);
			}

		} else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED)) {
			/*
			 * Check if the action = com.urbanairship.push.REGISTRATION_FINISHED
			 */

			Log.i(TAG,
					"Registration complete. APID:" + intent.getStringExtra(PushManager.EXTRA_APID) + ". Valid: "
							+ intent.getBooleanExtra(PushManager.EXTRA_REGISTRATION_VALID, false));
		} else if (action.equals(GCMMessageHandler.ACTION_GCM_DELETED_MESSAGES)) {
			/*
			 * Check if the action = com.urbanairship.push.NOTIFICATION_OPENED
			 */
			Log.i(TAG, "The GCM service deleted " + intent.getStringExtra(GCMMessageHandler.EXTRA_GCM_TOTAL_DELETED)
					+ " messages.");
		}

		// Notify any app-specific listeners using the local broadcast receiver
		// to avoid
		// leaking any sensitive information. This sends out all push and
		// location intents
		// to the rest of the application.
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	/**
	 * Log the values sent in the payload's "extra" dictionary.
	 * 
	 * @param intent
	 *            A PushManager.ACTION_NOTIFICATION_OPENED or
	 *            ACTION_PUSH_RECEIVED intent.
	 */
	private void logPushExtras(Intent intent) {

		if (Constants.isDebug) {
			Log.i(TAG, "=================================== PUSH MESSAGE EXTRAS ===================================");
		}

		Set<String> keys = intent.getExtras().keySet();
		for (String key : keys) {

			// ignore standard C2DM extra keys
			List<String> ignoredKeys = (List<String>) Arrays.asList("collapse_key", "from", PushManager.EXTRA_NOTIFICATION_ID,
					PushManager.EXTRA_PUSH_ID, PushManager.EXTRA_ALERT);
			if (ignoredKeys.contains(key)) {
				continue;
			}
			if (Constants.isDebug) {
				Log.i(TAG, key + " :: " + intent.getStringExtra(key));
			}

		}

		if (Constants.isDebug) {
			Log.i(TAG, "=================================== PUSH MESSAGE EXTRAS ===================================");
		}
	}

	void soundForPush() {
		if (sound.trim().equals("push.mp3")) {

			final Thread secondSoundThread = new Thread() {
				public void run() {
					MediaPlayer player = null;

					player = MediaPlayer.create(context, R.raw.cabapp_horn);
					player.start();

					try {
						Thread.sleep(player.getDuration() + 100);

					} catch (InterruptedException e) {

					}
				}

			};

			soundThread = new Thread() {
				public void run() {
					MediaPlayer player = null;

					player = MediaPlayer.create(context, R.raw.cabapp_horn);
					player.start();

					try {
						Thread.sleep(player.getDuration() + 100);
						// secondSoundThread.start();

					} catch (InterruptedException e) {

					}
				}

			};
			soundThread.start();
		} else {

			soundThread = new Thread() {
				public void run() {
					MediaPlayer player = null;

					player = MediaPlayer.create(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
					player.start();

					try {
						Thread.sleep(player.getDuration() + 100);
					} catch (InterruptedException e) {

					}
				}

			};
			soundThread.start();
		}

	}
}