// (c) mBlox Inc 2013
package uk.co.pocketapp.whotel.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.mblox.engage.MbloxManager;
import com.mblox.engage.Message;
import com.mblox.engage.MessageInboxListener;
import com.mblox.engage.SyncFuture;
import com.mblox.engage.SyncFutureListener;
import com.mblox.engage.SyncResult;
import com.mblox.engage.UpdatedMessage;
import com.mblox.engage.exceptions.NoSuchMessageException;
import com.mblox.engage.util.UINotificationHelper;

import uk.co.pocketapp.whotel.R;

public class SampleActivity extends Activity implements MessageInboxListener {

	public static final String PREFERENCES_NAME = "prefs";
	public static final String MSG_ID_INTENT_KEY = "msgid";
	public static final String FLIPPED_KEY = "flipped";
	private static final String GCM_REGISTERED_KEY = "gcm_registration";

	private static final String TAG = "uk.co.pocketapp.whotel.ui.SampleActivity";

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	private UpdateViewHandler updateViewHandler;
	private GoogleCloudMessaging gcm;
	// TODO Set these parameters
	private static final String GCM_SENDER_ID = "666209944120";// "1063184119534";
	private static final String APPLICATION_CHANNEL_ID = "3fb915e0";

	// test channel id
	// 9f25f991

	// whotel live channel id
	// 3fb915e0

	// XXX

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

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

		setDeviceID();

		Button syncButton = (Button) findViewById(R.id.sync_button);
		syncButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Location location = getLocation();
				SyncFuture future = MbloxManager.getInstance(
						getApplicationContext()).sync(location);
				future.addListener(new SyncFutureListener() {

					@Override
					public void operationComplete(SyncFuture future) {
						future.removeListener(this);
						setDeviceID();
						SyncResult result = future.getSyncResult();
						if (result.getResult() == SyncResult.Result.FAILURE) {
							Log.v(TAG, "Sync failed: "
									+ result.getCause().getMessage());
						}
						// The other results will be noticed by the inbox
						// listener
					}
				});
			}
		});

		updateViewHandler = new UpdateViewHandler();

		updateView();
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
	public void inboxSynced(SyncResult result) {
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
									.getInstance(SampleActivity.this);
						}
						String regId = gcm.register(GCM_SENDER_ID);
						msg = "Device registered, registration id=" + regId;
						prefs.edit().putBoolean(GCM_REGISTERED_KEY, true)
								.commit();
						MbloxManager.getInstance(SampleActivity.this)
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

	private void updateView() {
		((ListView) findViewById(R.id.list)).setAdapter(new MessageAdapter(
				MbloxManager.getInstance(this).getInbox().getMessages()));
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

	private void setDeviceID() {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				TextView deviceIdField = (TextView) findViewById(R.id.device_id_field);
				String deviceId = MbloxManager.getInstance(
						getApplicationContext()).getDeviceId();
				String deviceIdString = getString(R.string.device_id);
				if (deviceId != null) {
					deviceIdString += " " + deviceId;
				} else {
					deviceIdString += " Unknown";
				}
				deviceIdField.setText(deviceIdString);
			}
		});
	}

	private class UpdateViewHandler extends Handler {

		@Override
		public void handleMessage(android.os.Message msg) {
			updateView();
		}

	}

	private class MessageAdapter extends BaseAdapter {

		private final List<Message> messages;

		private MessageAdapter(List<Message> messages) {
			this.messages = messages;
		}

		@Override
		public int getCount() {
			return messages.size();
		}

		@Override
		public Object getItem(int position) {
			return messages.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = getLayoutInflater().inflate(
					android.R.layout.two_line_list_item, parent, false);
			TextView row1 = (TextView) v.findViewById(android.R.id.text1);
			TextView row2 = (TextView) v.findViewById(android.R.id.text2);
			if (messages.isEmpty()) {
				row1.setText("No active messages");
			} else {
				final Message msg = messages.get(position);
				row1.setText(Html.fromHtml(msg.getMessageBody().toString()));
				row2.setText(getRow2Text(msg));
				row1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							MbloxManager.getInstance(getApplicationContext())
									.registerClick(msg.getId(), "body",
											getLocation());
						} catch (NoSuchMessageException e) {
							Log.w(TAG,
									"Clicked on a message that does not exists"
											+ "in the inbox");
						}
					}
				});
				row2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							MbloxManager.getInstance(getApplicationContext())
									.registerClick(msg.getId(), "metadata",
											getLocation());
						} catch (NoSuchMessageException e) {
							Log.w(TAG,
									"Clicked on a message that does not exists"
											+ "in the inbox");
						}
					}
				});
			}
			return v;
		}

		private String getRow2Text(Message msg) {
			StringBuilder sb = new StringBuilder();
			sb.append("ID: ").append(msg.getId()).append("\n");
			sb.append(dateFormat.format(msg.getReceivedDate()));
			return sb.toString();
		}
	}
}