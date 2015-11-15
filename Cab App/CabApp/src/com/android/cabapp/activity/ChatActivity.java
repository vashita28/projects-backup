package com.android.cabapp.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.adapter.MyChatAdpater;
import com.android.cabapp.datastruct.json.ChatMessages;
import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.fragments.InboxFragment;
import com.android.cabapp.model.GetChatMessages;
import com.android.cabapp.model.SendMessage;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.flurry.android.FlurryAgent;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class ChatActivity extends RootActivity {

	private static final String TAG = ChatActivity.class.getSimpleName();

	ImageView ivBack, ivLogo;
	ListView lvMessages, lvQuickMessages;
	RelativeLayout rlQuickMessages, rlCallPassengers, rlEmpty;
	MyChatAdpater myChatAdapter;
	List<ChatMessages> messagesList;
	TextView tvSend, tvLocation, tvPassengerName, tvCallPassenger, tvQuickMessages;// tvEmpty
	EditText editTextMessage;

	String response = "";
	Bundle bundle;
	String driverMessage, selectedFromList;
	String[] valuesQuickMsgList = new String[] { "On my way, stuck in traffic", "I'm running late by 5 min",
			"I'm running late by 10 mins", "I'm waiting, are you on the way? ", "Had to move, please call me",
			"Please call me asap" };

	ArrayAdapter<String> adapterQuickMsgList;

	boolean bGetChatMessages;

	ProgressDialog pDialog;

	IntentFilter newMessageIntentFilter, newMessageFromOtherPassengerIntentFilter;

	String szPassengerInternationalCode = "", szPassengerNumber = "", szPassengerName = "", szDOPinCode = "", szPUPinCode = "";

	public static String szJobID = "";

	// "{messages:[{source:\"passenger\",message:\"passenger message111111\"},{source:\"passenger\",message:\"passenger message2\"},{source:\"driver\",message:\"driver mesg1\"},{source:\"driver\",message:\"driver msg 2\"},{source:\"driver\",message:\"driver msg 3\"},{source:\"passenger\",message:\"passenger msg 3\"},{source:\"passenger\",message:\"passenger msg 4\"},{source:\"passenger\",message:\"passenger msg 5\"}]}";

	public static Context mChatActivityContext;

	ProgressBar mChatProgress;

	// public boolean fromOutside = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		mChatActivityContext = this;
		super.initWidgets();
		Log.e("ChatActivity", "ChatActivity-onCreate()");
		mContext = this;

		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivLogo = (ImageView) findViewById(R.id.ivLogo);
		lvMessages = (ListView) findViewById(R.id.lvMessages);
		// tvEmpty = (TextView) findViewById(android.R.id.empty);
		tvSend = (TextView) findViewById(R.id.tvSend);
		rlEmpty = (RelativeLayout) findViewById(R.id.rlEmpty);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvPassengerName = (TextView) findViewById(R.id.tvPassengerName);
		tvCallPassenger = (TextView) findViewById(R.id.tvCallPassenger);
		editTextMessage = (EditText) findViewById(R.id.etMessage);
		tvQuickMessages = (TextView) findViewById(R.id.tvQuickMessages);
		lvQuickMessages = (ListView) findViewById(R.id.lvQuickMessages);
		rlQuickMessages = (RelativeLayout) findViewById(R.id.rlQuickMessages);
		rlCallPassengers = (RelativeLayout) findViewById(R.id.rlCallPassengers);
		TextView textCancel = (TextView) findViewById(R.id.textCancel);
		mChatProgress = (ProgressBar) findViewById(R.id.chatProgress);

		cancelAllChatNotifications();

		tvQuickMessages.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rlQuickMessages.setVisibility(View.VISIBLE);
				// tvEmpty.setVisibility(View.GONE);
				rlEmpty.setVisibility(View.GONE);
			}
		});

		textCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rlQuickMessages.setVisibility(View.GONE);
			}
		});

		adapterQuickMsgList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,
				valuesQuickMsgList);
		lvQuickMessages.setAdapter(adapterQuickMsgList);

		lvQuickMessages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				selectedFromList = (String) (lvQuickMessages.getItemAtPosition(position));
				rlQuickMessages.setVisibility(View.GONE);
				if (!selectedFromList.isEmpty())
					editTextMessage.setText(selectedFromList);
			}
		});

		ivBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Util.hideSoftKeyBoard(ChatActivity.this, ivBack);
				finishMe();
			}
		});

		if (ivLogo != null)
			ivLogo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("ChatActivity", "onLogoClick");
					finish();
					if (InboxFragment.mHandler != null) {
						InboxFragment.mHandler.sendEmptyMessage(1);
					}
				}
			});

		tvSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// tvEmpty.setVisibility(View.GONE);
				rlEmpty.setVisibility(View.GONE);
				driverMessage = editTextMessage.getText().toString().trim();
				editTextMessage.setText("");
				editTextMessage.setCursorVisible(true);

				if (driverMessage.length() == 0 || driverMessage.isEmpty()) {
					Toast.makeText(ChatActivity.this, "Please add a message", Toast.LENGTH_LONG).show();

				} else if (driverMessage.toLowerCase().contains(
						getResources().getString(R.string.arrived_at_pick_up).toLowerCase())) {
					Util.showToastMessage(ChatActivity.this, "Message text not supported", Toast.LENGTH_LONG);
				} else {

					if (NetworkUtil.isNetworkOn(ChatActivity.this)) {
						if (messagesList != null && !driverMessage.toString().trim().isEmpty()) {
							messagesList.add(new ChatMessages("driver", driverMessage));

							refreshList("");
							InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

							SendMessageTask sendMessageTask = new SendMessageTask();
							sendMessageTask.jobID = szJobID;
							sendMessageTask.execute();

						}
					} else {
						Util.showToastMessage(ChatActivity.this, getResources().getString(R.string.no_network_error),
								Toast.LENGTH_LONG);
					}

					// if (NetworkUtil.isNetworkOn(ChatActivity.this)) {
					// SendMessageTask sendMessageTask = new SendMessageTask();
					// sendMessageTask.jobID = szJobID;
					// sendMessageTask.execute();
					// } else {
					// Util.showToastMessage(ChatActivity.this, getResources()
					// .getString(R.string.no_network_error),
					// Toast.LENGTH_LONG);
					// }

				}
			}
		});

		tvCallPassenger.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!szPassengerNumber.isEmpty()) {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + szPassengerInternationalCode + szPassengerNumber));
					startActivity(callIntent);
					FlurryAgent.logEvent("Call Passenger");
				} else
					Util.showToastMessage(ChatActivity.this, "No number", Toast.LENGTH_LONG);

				if (Constants.isDebug)
					Log.e(TAG, "Passenger number:: " + szPassengerInternationalCode + szPassengerNumber);
			}
		});

		newMessageIntentFilter = new IntentFilter();
		newMessageIntentFilter.addAction("NewMessage");

		newMessageFromOtherPassengerIntentFilter = new IntentFilter();
		newMessageFromOtherPassengerIntentFilter.addAction("NewMessageFromOtherDriver");

		LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager.getInstance(this);
		locationBroadcastManager.registerReceiver(newMessageReceivedReceiver, newMessageIntentFilter);
		locationBroadcastManager.registerReceiver(newMessageReceivedFromOtherPassengerReceiver,
				newMessageFromOtherPassengerIntentFilter);

		if (getIntent().getExtras() != null) {
			bundle = getIntent().getExtras();
			if (bundle != null) {
				if (bundle.containsKey(Constants.JOB_ID))
					szJobID = bundle.getString(Constants.JOB_ID);

				// Handling hearing imapairment::
				if (bundle.containsKey(Constants.HEARING_IMPAIRED))
					if (bundle.getString(Constants.HEARING_IMPAIRED).equals("true")) {
						rlCallPassengers.setVisibility(View.GONE);
					}

				if (bundle.containsKey(Constants.DROP_OFF_PINCODE)) {
					szDOPinCode = bundle.getString(Constants.DROP_OFF_PINCODE);
					szPUPinCode = bundle.getString(Constants.PICK_UP_PINCODE);

					szPassengerName = bundle.getString(Constants.PASSENGER_NAME);

					if (bundle.containsKey(Constants.PASSENGER_INTERNATIONAL_CODE))
						szPassengerInternationalCode = bundle.getString(Constants.PASSENGER_INTERNATIONAL_CODE);

					if (bundle.containsKey(Constants.PASSENGER_NUMBER)) {
						szPassengerNumber = bundle.getString(Constants.PASSENGER_NUMBER);
					}
					if (bundle.containsKey(Constants.JSON_GET_MESSAGE)) {
						response = bundle.getString(Constants.JSON_GET_MESSAGE);
					}

					if (bundle.containsKey("getChatMessages")) {
						bGetChatMessages = bundle.getBoolean("getChatMessages");

						if (bGetChatMessages) {
							GetMessagesTask getMessagesByJobID = new GetMessagesTask();
							getMessagesByJobID.jobID = bundle.getString(Constants.JOB_ID);
							getMessagesByJobID.execute();
						}
					}
					setDataInView();
				} else {
					GetMessagesTask getMessagesByJobID = new GetMessagesTask();
					getMessagesByJobID.bIsRefresh = true;
					getMessagesByJobID.jobID = szJobID;
					getMessagesByJobID.execute();
				}
			}
		}

		// if (!response.isEmpty())
		setMessageInAdapter();
	}

	void cancelAllChatNotifications() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) UAirship.shared().getApplicationContext().getSystemService(ns);
		nMgr.cancelAll();
	}

	void setDataInView() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					if (szPUPinCode == null || szPUPinCode.equals("null") || szPUPinCode.isEmpty() || szPUPinCode.equals(""))
						szPUPinCode = "AD";// As directed

					if (szDOPinCode == null || szDOPinCode.equals("null") || szDOPinCode.equals("") || szDOPinCode.isEmpty())
						szDOPinCode = "AD";// As directed

					String txtLocation = "<font color=#f5f5f5>" + szPUPinCode
							+ "</font><font color=#fd6f01> > </font> <font color=#f5f5f5>" + szDOPinCode + "</font>";
					tvLocation.setText(Html.fromHtml(txtLocation));

					if (szPassengerName.equals("fake passenger")) {
						tvPassengerName.setText("Walk up");
					} else {
						String szFirstName = szPassengerName.substring(0, szPassengerName.indexOf(" "));
						String szLastName = szPassengerName.substring(szPassengerName.indexOf(" "));
						tvPassengerName.setText(szFirstName.substring(0, 1) + "." + szLastName);
					}
				} catch (Exception e) {

				}
			}
		});
	}

	private BroadcastReceiver newMessageReceivedReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// check APID and proceed with login
			String message = intent.getStringExtra(PushManager.EXTRA_ALERT);
			String messageType = intent.getStringExtra("type");

			Log.e("ChatActivity", "onReceive::New Message::Type:: " + messageType + " Message is:: " + message);

			GetMessagesTask getMessagesByJobID = new GetMessagesTask();
			getMessagesByJobID.bIsRefresh = false;
			getMessagesByJobID.jobID = szJobID;
			getMessagesByJobID.execute();
		}
	};

	private BroadcastReceiver newMessageReceivedFromOtherPassengerReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// check APID and proceed with login
			String message = intent.getStringExtra(PushManager.EXTRA_ALERT);
			String messageType = intent.getStringExtra("type");

			Log.e("ChatActivity", "onReceive::New MessageFromOtherDriver::Type:: " + messageType + " Message is:: " + message);

			GetMessagesTask getMessagesByJobID = new GetMessagesTask();
			getMessagesByJobID.bIsRefresh = true;
			getMessagesByJobID.jobID = szJobID;
			getMessagesByJobID.execute();
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("ChatActivity", "ChatActivity-onDestroy()");
		LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager.getInstance(this);
		locationBroadcastManager.unregisterReceiver(newMessageReceivedReceiver);
		locationBroadcastManager.unregisterReceiver(newMessageReceivedFromOtherPassengerReceiver);
		mChatActivityContext = null;
		szJobID = "";
		Util.isOnChatActivityFragment = false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (rlQuickMessages.getVisibility() == View.VISIBLE) {
			rlQuickMessages.setVisibility(View.GONE);
			return;
		} else {
			finishMe();
		}
	}

	void setMessageInAdapter() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				messagesList = new ArrayList<ChatMessages>();
				JSONArray jsonResponse = null;
				try {
					jsonResponse = new JSONArray(response);
					int lengthJsonArr = jsonResponse.length();
					for (int i = 0; i < lengthJsonArr; i++) {
						JSONObject jsonChildNode = jsonResponse.getJSONObject(i);
						String source = jsonChildNode.optString(Constants.SOURCE).toString();
						String messageText = jsonChildNode.optString(Constants.MESSAGE).toString();
						Log.i(TAG, "source:: " + source + " messageText:: " + messageText);

						messagesList.add(new ChatMessages(source, messageText));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (myChatAdapter == null) {
					myChatAdapter = new MyChatAdpater(ChatActivity.this, messagesList);
					lvMessages.setAdapter(myChatAdapter);
				} else {
					myChatAdapter.updateList(messagesList);
					myChatAdapter.notifyDataSetChanged();
				}

				lvMessages.post(new Runnable() {
					@Override
					public void run() {
						lvMessages.setSelection(lvMessages.getAdapter().getCount() - 1);

						if (lvMessages.getAdapter() == null || lvMessages.getAdapter().getCount() == 0)
							// tvEmpty.setVisibility(View.VISIBLE);
							rlEmpty.setVisibility(View.VISIBLE);
						else
							// tvEmpty.setVisibility(View.GONE);
							rlEmpty.setVisibility(View.GONE);
					}
				});
			}
		});
	}

	public static void finishMe() {
		Log.e("ChatActivity", "ChatActivity-finishMe()");
		if (mChatActivityContext != null)
			((Activity) mChatActivityContext).finish();
	}

	public class SendMessageTask extends AsyncTask<String, Void, String> {
		public String jobID;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mChatProgress.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// try {
			// driverMessage = driverMessage.getBytes("UTF-8").toString();
			// } catch (UnsupportedEncodingException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }

			boolean isSound = false;
			if (driverMessage.contains("I'm waiting, are you on the way?"))
				isSound = true;

			SendMessage message = new SendMessage(jobID, ChatActivity.this, driverMessage, isSound);
			String response = message.sendMessages();
			try {
				JSONObject jObject = new JSONObject(response);
				if (jObject.has("success") && jObject.getString("success").equals("true"))
					return "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return response;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			mChatProgress.setVisibility(View.GONE);
			/*
			 * if (result != null && result.equals("success")) { if
			 * (messagesList == null) { messagesList = new
			 * ArrayList<ChatMessages>(); } messagesList.add(new
			 * ChatMessages("driver", driverMessage));
			 * myChatAdapter.updateList(messagesList);
			 * editTextMessage.setText("");
			 * editTextMessage.setCursorVisible(false);
			 * Util.hideSoftKeyBoard(mContext, tvSend); }
			 */
			lvMessages.post(new Runnable() {
				@Override
				public void run() {
					lvMessages.setSelection(myChatAdapter.getCount() - 1);
				}
			});
		}

	}

	public class GetMessagesTask extends AsyncTask<String, Void, Job> {
		public String jobID;
		public boolean bIsRefresh = true;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if (bIsRefresh) {
				pDialog = new ProgressDialog(ChatActivity.this);
				pDialog.setMessage("Loading...");
				pDialog.setCancelable(false);
				pDialog.show();
			}
		}

		@Override
		protected Job doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (Constants.isDebug)
				Log.d(TAG, "getMessagesTask jobID:: " + jobID);

			GetChatMessages message = new GetChatMessages(jobID, ChatActivity.this);
			Job job = message.getJobByJobID();
			// Log.e("ChatActivity", "Job is:: " + job);
			if (job != null) {

				return job;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Job job) {
			// TODO Auto-generated method stub
			super.onPostExecute(job);
			if (pDialog != null)
				pDialog.dismiss();

			if (job != null && mChatActivityContext != null) {
				szPassengerName = job.getName();
				szJobID = job.getId();

				if (job.getPickupLocation() != null && job.getPickupLocation().getPostCode() != null)
					szPUPinCode = job.getPickupLocation().getPostCode();
				if (job.getDropLocation() != null && job.getDropLocation().getPostCode() != null)
					szDOPinCode = job.getDropLocation().getPostCode();

				szPassengerInternationalCode = job.getInternationalCode();
				szPassengerNumber = job.getMobileNumber();

				if (job.getMessages() != null)
					response = job.getMessages().toString();
				Log.e("ChatActivity", "Job Response: " + response);

				setDataInView();
				setMessageInAdapter();
			}
		}
	}

	public void refreshList(String alertMessage) {
		Log.i(getClass().getSimpleName(), "refreshList" + "   alertMessageee:: " + alertMessage);
		if (messagesList != null && !alertMessage.toString().trim().isEmpty()) {
			if (!alertMessage.equals("")) {
				messagesList.add(new ChatMessages("driver", alertMessage));

			}
			Log.e("message list size", "" + messagesList.size());
			if (messagesList.size() <= 0)
				rlEmpty.setVisibility(View.VISIBLE);
			else
				rlEmpty.setVisibility(View.GONE);

			if (myChatAdapter == null) {
				myChatAdapter = new MyChatAdpater(mContext, messagesList);
				lvMessages.setAdapter(myChatAdapter);
				myChatAdapter.notifyDataSetChanged();

			} else {
				myChatAdapter.updateList(messagesList);
				myChatAdapter.notifyDataSetChanged();
			}

			lvMessages.post(new Runnable() {
				@Override
				public void run() {
					lvMessages.setSelection(myChatAdapter.getCount() - 1);
				}
			});
		} else {
			rlEmpty.setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(ChatActivity.this, AppValues.FLURRY_API_KEY);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.e("ChatActivity", "ChatActivity-onStop()");
		FlurryAgent.onEndSession(ChatActivity.this);
		szJobID = "";
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Util.isOnChatActivityFragment = true;
	}

}
