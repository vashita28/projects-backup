package uk.co.pocketapp.whotel.ui;

import java.io.IOException;
import java.util.List;

import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.W_Hotel_Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.mblox.engage.MbloxManager;
import com.mblox.engage.Message;
import com.mblox.engage.MessageInboxListener;
import com.mblox.engage.SyncFuture;
import com.mblox.engage.SyncFutureListener;
import com.mblox.engage.SyncResult;
import com.mblox.engage.UpdatedMessage;
import com.mblox.engage.util.UINotificationHelper;

/**
 * Created by Pramod on 8/16/13.
 */
public class RootActivity extends SherlockFragmentActivity implements
		MessageInboxListener {

	String FLURRY_API_KEY = "3S65QVGZ9F8C6MGJ6G59";// "RGGV77RT7MRHBWSTRCBN";
	SlidingMenu menu;

	W_Hotel_Application whotelApplication;

	public static final String PREFERENCES_NAME = "prefs";
	public static final String MSG_ID_INTENT_KEY = "msgid";
	public static final String FLIPPED_KEY = "flipped";
	private static final String GCM_REGISTERED_KEY = "gcm_registration";

	private static final String TAG = "uk.co.pocketapp.whotel.ui.RootActivitiy";

	private UpdateViewHandler updateViewHandler;
	private GoogleCloudMessaging gcm;
	// TODO Set these parameters
	private static final String GCM_SENDER_ID = "666209944120";

	private static final String APPLICATION_CHANNEL_ID = "3fb915e0";

	// test channel id
	// 9f25f991

	// whotel live channel id
	// 3fb915e0

	public static Handler mSyncHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("RootActivity", "onCreate()");
		whotelApplication = (W_Hotel_Application) getApplication();

		mSyncHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				syncMessagesFromMBlox();
				super.handleMessage(msg);
			}
		};
	}

	void init() {
		mBloxInitialise();

		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.slidingmenu);

		menu.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				// TODO Auto-generated method stub
				menuOpenedEvent();
			}
		});

		menu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				// TODO Auto-generated method stub
				menuClosedEvent();
			}
		});
	}

	void menuOpenedEvent() {

	}

	void menuClosedEvent() {

	}

	@Override
	protected void onResume() {
		MbloxManager.getInstance(this).getInbox().addListener(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		MbloxManager.getInstance(this).getInbox().removeListener(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		MbloxManager.getInstance(this).dispose();
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		Log.d("RootActivity", "onStart()");
		super.onStart();
		FlurryAgent.onStartSession(this, FLURRY_API_KEY);
		// EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		Log.d("RootActivity", "onStop()");
		super.onStop();
		FlurryAgent.onEndSession(this);
		// EasyTracker.getInstance(this).activityStop(this);
	}

	void mBloxInitialise() {

		doBackgroundRegistrationIfNeeded();

		try {
			MbloxManager.getInstance(this).setAppChannelId(
					APPLICATION_CHANNEL_ID);
		} catch (IllegalStateException e) {
			// This won't happen unless the application ID is changed without
			// re-install
		}
		UINotificationHelper.createClickThroughEventIfPossible(
				getApplicationContext(), getIntent(), "notification clicked",
				getLocation());

		// setDeviceID();
		syncMessagesFromMBlox();

	}

	private void doBackgroundRegistrationIfNeeded() {
		final SharedPreferences prefs = getSharedPreferences(PREFERENCES_NAME,
				Context.MODE_PRIVATE);
		if (!prefs.getBoolean(GCM_REGISTERED_KEY, false)
				&& GCM_SENDER_ID != null) {
			new AsyncTask<String, String, String>() {
				@Override
				protected String doInBackground(String... params) {
					String msg = "";
					try {
						if (gcm == null) {
							gcm = GoogleCloudMessaging
									.getInstance(RootActivity.this);
						}
						String regId = gcm.register(GCM_SENDER_ID);
						msg = "Device registered, registration id=" + regId;
						prefs.edit().putBoolean(GCM_REGISTERED_KEY, true)
								.commit();
						MbloxManager.getInstance(RootActivity.this)
								.setGCMRegistrationID(regId);
						MbloxManager.getInstance(getApplicationContext())
								.sync();
					} catch (IOException ex) {
						msg = "Error :" + ex.getMessage();
					}
					return msg;
				}

				@Override
				protected void onPostExecute(String msg) {
					Log.v(TAG, msg + "\n");
				}

			}.execute(null, null, null);
		}
	}

	private void setDeviceID() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TextView deviceIdField = (TextView)
				// findViewById(R.id.device_id_field);
				String deviceId = MbloxManager.getInstance(
						getApplicationContext()).getDeviceId();
				String deviceIdString = getString(R.string.device_id);
				if (deviceId != null) {
					deviceIdString += " " + deviceId;
				} else {
					deviceIdString += " Unknown";
				}
				// deviceIdField.setText(deviceIdString);
				Log.d(TAG, " setDeviceID:: " + deviceIdString);
			}
		});
	}

	private Location getLocation() {
		Location location = null;
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (locationManager != null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
	}

	@Override
	public void inboxSynced(SyncResult result) {
		// TODO Auto-generated method stub
		if (result.getResult() == SyncResult.Result.SUCCESS
				|| result.getResult() == SyncResult.Result.INCOMPLETE) {
			List<Message> newMessages = result.getNewMessages();
			List<UpdatedMessage> updatedMessages = result.getUpdatedMessages();
			List<Message> removedMessages = result.getRemovedMessages();
			for (Message richMessage : newMessages) {
				Log.v(TAG, "New message: " + richMessage);
			}
			for (UpdatedMessage updatedMessage : updatedMessages) {
				Log.v(TAG,
						"Updated message from: "
								+ updatedMessage.getOldVersion() + "\n to: "
								+ updatedMessage.getNewVersion());
			}
			for (Message richMessage : removedMessages) {
				Log.v(TAG, "Removed message: " + richMessage);
			}
			List<Message> messages = MbloxManager.getInstance(this).getInbox()
					.getMessages();
			Log.v(TAG, "Current messages: ");
			for (Message richMessage : messages) {
				Log.v(TAG, richMessage.toString());
			}
			if (result.getResult() == SyncResult.Result.INCOMPLETE) {
				Log.w(TAG, "Sync incomplete, another sync should be performed");
				Log.w(TAG, "Reason: " + result.getCause().getMessage());
			} else {
				Log.v(TAG, "Sync success");
			}
		} else {
			// Sync failed and hence this code is unreachable since the inbox
			// listener will not be notified of this as the inbox has not
			// changed.
		}
		android.os.Message.obtain(updateViewHandler).sendToTarget();
	}

	private class UpdateViewHandler extends Handler {

		@Override
		public void handleMessage(android.os.Message msg) {
			updateView();
		}
	}

	void updateView() {
		// ((ListView) findViewById(R.id.lv_deals)).setAdapter(new DealsAdapter(
		// MbloxManager.getInstance(this).getInbox().getMessages(), this));
	}

	void syncMessagesFromMBlox() {
		updateViewHandler = new UpdateViewHandler();
		Location location = getLocation();
		SyncFuture future = MbloxManager.getInstance(getApplicationContext())
				.sync(location);
		future.addListener(new SyncFutureListener() {

			@Override
			public void operationComplete(SyncFuture future) {
				future.removeListener(this);
				setDeviceID();
				SyncResult result = future.getSyncResult();
				if (result.getResult() == SyncResult.Result.FAILURE) {
					Log.v(TAG, "Sync failed: " + result.getCause().getMessage());
					android.os.Message.obtain(updateViewHandler).sendToTarget();
				}
				// The other results will be noticed by the inbox
				// listener
			}
		});
		// updateView();
	}

}