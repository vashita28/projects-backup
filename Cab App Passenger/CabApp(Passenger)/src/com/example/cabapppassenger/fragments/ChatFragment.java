package com.example.cabapppassenger.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.adapter.MyChatAdapater;
import com.example.cabapppassenger.datastruct.json.Bookings;
import com.example.cabapppassenger.datastruct.json.ChatMessages;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.BookingStatusTask;
import com.example.cabapppassenger.task.GetBookingsTask;
import com.example.cabapppassenger.task.SendMessageTask;
import com.example.cabapppassenger.util.Constant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ChatFragment extends RootFragment implements
		OnCustomBackPressedListener {

	private static final String TAG = ChatFragment.class.getSimpleName();

	public static ChatFragment chatFragmentInstance = null;

	Context mContext;
	ImageView ivBack, ivLogo;
	ListView lvMessages, lvQuickMessages;

	List<ChatMessages> messagesList;
	TextView tvEmpty, tvSend, tvLocation, tvDriverName, tvCallDriver,
			tvQuickMessages, tvCancel;
	RelativeLayout rlQuickMessages;
	String PREF_NAME = "CabApp_Passenger", accessToken, driverMobileNumber,
			driverFirstName, driverLastName;
	SharedPreferences sharedPreferences;
	EditText editTextMessage;
	MediaPlayer mediaPlayer, mediaPlayer2;
	MyChatAdapater myChatAdapter;

	String response = "";
	Bundle bundle, bundle_arrived;
	String passengerMessage, selectedFromList;
	String[] valuesQuickMsgList = new String[] {

	"OK,thanks", "I'm waiting,are you on the way?", "I'll be out in 5 mins",
			"I'll be out in 10 mins", "Running late,please wait",
			"Please call me asap" };

	Handler mHandler, mHandlerBookingStatus;
	ArrayAdapter<String> adapterQuickMsgList;
	boolean bGetChatMessages, isArrived = false, isSound = false;
	public boolean fromOutside = true;;
	public String bookingId;

	ProgressDialog pDialog;

	public ChatFragment(String bookingId, boolean isArrived, boolean isSound) {

		super();
		this.bookingId = bookingId;
		this.isArrived = isArrived;
		this.isSound = isSound;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(getClass().getSimpleName(), "on create view");
		mContext = getActivity();
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		sharedPreferences = getActivity().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		accessToken = sharedPreferences.getString("AccessToken", "");
		// bookingId = sharedPreferences.getString("Booking Id", "");
		final View rootView = inflater.inflate(R.layout.fragment_chat,
				container, false);

		ivBack = (ImageView) rootView.findViewById(R.id.ivBack);
		// ivLogo = (ImageView) rootView.findViewById(R.id.iv_l);
		lvMessages = (ListView) rootView.findViewById(R.id.lv_messages);
		tvEmpty = (TextView) rootView.findViewById(android.R.id.empty);
		tvSend = (TextView) rootView.findViewById(R.id.tv_send);
		tvDriverName = (TextView) rootView.findViewById(R.id.tv_passenger_name);
		tvCallDriver = (TextView) rootView.findViewById(R.id.tv_call_driver);
		editTextMessage = (EditText) rootView.findViewById(R.id.et_message);
		tvQuickMessages = (TextView) rootView
				.findViewById(R.id.tv_quick_messages);
		lvQuickMessages = (ListView) rootView
				.findViewById(R.id.lvQuickMessages);
		rlQuickMessages = (RelativeLayout) rootView
				.findViewById(R.id.rlQuickMessages);

		adapterQuickMsgList = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				valuesQuickMsgList);
		lvQuickMessages.setAdapter(adapterQuickMsgList);

		tvCancel = (TextView) rootView.findViewById(R.id.textCancel);
		tvQuickMessages.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (rlQuickMessages.getVisibility() == View.VISIBLE) {
					rlQuickMessages.setVisibility(View.GONE);
					tvCallDriver.setVisibility(View.VISIBLE);

				} else {
					rlQuickMessages.setVisibility(View.VISIBLE);
					tvCallDriver.setVisibility(View.GONE);

				}

			}
		});

		lvQuickMessages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				selectedFromList = (String) (lvQuickMessages
						.getItemAtPosition(position));
				rlQuickMessages.setVisibility(View.GONE);
				// messagesList
				// .add(new ChatMessages("passenger", selectedFromList));

				editTextMessage.setText(selectedFromList);
				editTextMessage.setCursorVisible(true);
				refreshList("");
				/*
				 * if (!selectedFromList.isEmpty())
				 * editTextMessage.setText(selectedFromList);
				 */
				/*
				 * SendMessageTask sendMessageTask = new
				 * SendMessageTask(mContext, Constant.passengerURL +
				 * "/ws/v2/passenger/bookings/" + bookingId +
				 * "/messages/?accessToken=" + accessToken, selectedFromList,
				 * mHandler);
				 * 
				 * sendMessageTask.execute();
				 */

			}
		});

		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				rlQuickMessages.setVisibility(View.GONE);
			}
		});
		ivBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				InputMethodManager imm = (InputMethodManager) mContext
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(),
						InputMethodManager.RESULT_UNCHANGED_SHOWN);
				getParentFragment().getChildFragmentManager().popBackStack();
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					mediaPlayer.stop();

					mediaPlayer.release();
				}

			}
		});

		tvSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tvEmpty.setVisibility(View.GONE);

				passengerMessage = editTextMessage.getText().toString().trim();
				if (passengerMessage.length() == 0) {
					Toast.makeText(mContext, "Field is empty!",
							Toast.LENGTH_LONG).show();
				} else {
					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
						if (messagesList != null) {
							messagesList.add(new ChatMessages("passenger",
									passengerMessage));

							editTextMessage.setText("");
							editTextMessage.setCursorVisible(true);
							refreshList("");
							InputMethodManager imm = (InputMethodManager) mContext
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(),
									InputMethodManager.RESULT_UNCHANGED_SHOWN);
							SendMessageTask sendMessageTask = new SendMessageTask(
									mContext, Constant.passengerURL
											+ "/ws/v2/passenger/bookings/"
											+ bookingId
											+ "/messages/?accessToken="
											+ accessToken, passengerMessage,
									mHandler);

							sendMessageTask.execute();
						}

					} else {
						Toast.makeText(mContext, "No network connection.",
								Toast.LENGTH_LONG).show();
					}

				}
			}
		});

		tvCallDriver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mediaPlayer != null) {
					try {
						if (mediaPlayer.isPlaying()) {
							mediaPlayer.stop();
							mediaPlayer.release();
							mediaPlayer = null;
						}
					} catch (Exception e) {
						Log.i(getClass().getSimpleName(),
								"Exception on mediaPlayer.isPlaying()");
					}

				}

				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + driverMobileNumber));
				startActivity(callIntent);

				Log.e(TAG, "Passenger number:: " + driverMobileNumber);
			}
		});

		mHandlerBookingStatus = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg != null && msg.peekData() != null) {
					Bundle bundledata = (Bundle) msg.obj;
					if (bundledata != null)
						if (bundledata.containsKey("Driver_mobileno"))
							driverMobileNumber = bundledata
									.getString("Driver_mobileno");
					driverFirstName = bundledata.getString("Driver_firstname");
					driverLastName = bundledata.getString("Driver_lastname");
					// String sub=driverLastName.substring(0);
					tvDriverName.setText(driverFirstName + " "
							+ driverLastName.charAt(0) + ".");
				}
			}
		};
		if (getArguments() != null) {
			if (getArguments().containsKey("driverMobileNumber")) {
				driverMobileNumber = getArguments().getString(
						"driverMobileNumber");
				driverFirstName = getArguments().getString("driverFirstName");
				driverLastName = getArguments().getString("driverLastName");
				tvDriverName.setText(driverFirstName + " "
						+ driverLastName.charAt(0) + ".");
			}
		} else {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				BookingStatusTask task = new BookingStatusTask(mContext,
						bookingId);
				task.mHandler = mHandlerBookingStatus;
				task.execute("");
			} else {
				Toast.makeText(MainFragmentActivity.getInstance(),
						"No network connection.", Toast.LENGTH_SHORT).show();
			}
		}

		updateList();
		super.onCreate(savedInstanceState);
		return rootView;

	}

	public void updateList() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);
				String bookingData;
				if (message != null && message.peekData() != null) {
					messagesList = new ArrayList<ChatMessages>();
					Log.i(getClass().getSimpleName(),
							"in handler with message from getBookingsTask");
					Bundle bundleData = (Bundle) message.obj;
					bookingData = bundleData.containsKey("bookingsData") ? bundleData
							.getString("bookingsData") : "";
					Bookings response = new Bookings();
					Gson gson = new Gson();
					response = gson.fromJson(bookingData, Bookings.class);

					JsonArray messages = response.getMessages();
					String source = "", messageText = "";
					for (JsonElement jsonElement : messages) {
						try {

							source = jsonElement.getAsJsonObject()
									.get("source").getAsString();
							messageText = jsonElement.getAsJsonObject()
									.get("message").getAsString();
						} catch (Exception e) {

						}
						Log.i(getClass().getSimpleName(), "Source " + source);
						Log.i(getClass().getSimpleName(), "message "
								+ messageText);
						messagesList.add(new ChatMessages(source, messageText));
						fromOutside = false;
					}

					refreshList("");

				}

			}

		};
		GetBookingsTask getBookingsTask = new GetBookingsTask(getActivity(),
				Constant.passengerURL + "ws/v2/passenger/bookings/" + bookingId
						+ "/?accessToken=" + accessToken, mHandler, false);

		getBookingsTask.execute();
	}

	public void playsound() {
		/* if (isArrived) { */
		Log.i("ChatFragment", "playSound");
		try {
			mediaPlayer = MediaPlayer.create(getActivity(), R.raw.cabapp_horn);
			mediaPlayer.start();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					mediaPlayer2 = MediaPlayer.create(getActivity(),
							R.raw.cabapp_horn);
					mediaPlayer2.start();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		/* } */
	}

	public void refreshList(String alertMessage) {
		Log.i(getClass().getSimpleName(), "refreshList");
		if (messagesList != null) {
			if (!alertMessage.equals("") && fromOutside) {
				messagesList.add(new ChatMessages("driver", alertMessage));

			}

			if (myChatAdapter == null) {
				myChatAdapter = new MyChatAdapater(mContext, messagesList);
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
		}

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	public void onPause() {
		super.onPause();
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		Log.i("ChatFragment", "onPause");
		chatFragmentInstance = null;
		if (mediaPlayer != null) {
			try {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					mediaPlayer.release();
					mediaPlayer = null;
				}
				if (mediaPlayer2.isPlaying()) {
					mediaPlayer2.stop();
					mediaPlayer2.release();
					mediaPlayer2 = null;
				}
			} catch (Exception e) {
				Log.i(getClass().getSimpleName(),
						"Exception on mediaPlayer.isPlaying()");
			}

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		chatFragmentInstance = this;
		if (isArrived) {
			isArrived = false;
			Log.i("ChatFragment", "isArrived");
			playsound();

		}
		if (isSound) {
			isSound = false;
			Log.i("ChatFragment", "isArrived");
			playsound();

		}
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);

	}

	@Override
	public void onCustomBackPressed() {
		Log.i(getClass().getSimpleName(), "onCustomBackPressed");
		getParentFragment().getChildFragmentManager().popBackStack();

	}

	@Override
	public void onDetach() {
		chatFragmentInstance = null;

		Log.i("ChatFragment", "onDetach");
		try {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null;
			}
			if (mediaPlayer2.isPlaying()) {
				mediaPlayer2.stop();
				mediaPlayer2.release();
				mediaPlayer2 = null;
			}
		} catch (Exception e) {
			Log.i(getClass().getSimpleName(),
					"Exception on mediaPlayer.isPlaying()");
		}
		super.onDetach();
	}
}
