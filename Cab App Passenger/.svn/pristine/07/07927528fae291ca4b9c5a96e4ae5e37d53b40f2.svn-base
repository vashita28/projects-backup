package com.example.cabapppassenger.receivers;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.fragments.Bookings_Driver_DetailsFragment;
import com.example.cabapppassenger.fragments.CabApp_Location_ParentFragment;
import com.example.cabapppassenger.fragments.ChatFragment;
import com.example.cabapppassenger.fragments.FavoritesParentFragment;
import com.example.cabapppassenger.fragments.HailNowParentFragment;
import com.example.cabapppassenger.fragments.MoreParentFragment;
import com.example.cabapppassenger.fragments.MyAccountParentFragment;
import com.example.cabapppassenger.fragments.MyBookingsParentFragment;
import com.example.cabapppassenger.fragments.PreBookParentFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class IntentReceiver extends BroadcastReceiver {

	private static final String logTag = "PushSample";
	/*
	 * public static String alertMessage = "", bookingId=""; public static
	 * boolean fromChat = false, frombooking = false, fromArrived = false;
	 */
	Editor editor;
	Context mContext;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	NotificationManager nMgr;
	String action, booking_type, bookingId, alertMessage, sound;
	AlertDialog.Builder bookingBuilder, chatBuilder;
	static AlertDialog bookingAlert, chatAlert;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(logTag, "Received intent: " + intent.toString());
		action = intent.getAction();
		mContext = context;
		shared_pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		bookingId = "";
		bookingId = intent.getStringExtra("addInfo");
		booking_type = intent.getStringExtra("type");
		sound = intent.getStringExtra("sound");
		alertMessage = intent.getStringExtra(PushManager.EXTRA_ALERT);

		int id = intent.getIntExtra(PushManager.EXTRA_NOTIFICATION_ID, 0);
		String ns = Context.NOTIFICATION_SERVICE;
		Log.i(logTag,
				"Received push notification. Alert: "
						+ intent.getStringExtra(PushManager.EXTRA_ALERT)
						+ " [NotificationID=" + id + "]");
		editor.putString("Booking Id", bookingId);

		editor.commit();
		if (shared_pref.getString("AccessToken", "") != null
				&& shared_pref.getString("AccessToken", "").length() > 0) {
			if (action.equals(PushManager.ACTION_PUSH_RECEIVED)) {

				Log.i(getClass().getSimpleName(), "ACTION_PUSH_RECEIVED"
						+ bookingId + "");

				if (MainFragmentActivity.getInstance() != null
						&& MainFragmentActivity.isVisible) {
					NotificationManager nMgr = (NotificationManager) UAirship
							.shared().getApplicationContext()
							.getSystemService(ns);

					if (booking_type.matches("bookingaccepted")
							&& MainFragmentActivity.getInstance() != null) {
						nMgr.cancelAll();
						Log.e("Booking details", bookingId);

						MainFragmentActivity.getInstance().runOnUiThread(
								new Runnable() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										if (MainFragmentActivity.getInstance() != null)
											alertdialog("Booking Accepted",
													"Alert");

									}
								});
					}

					else if (booking_type.matches("messagetopass")) {

						if (ChatFragment.chatFragmentInstance != null
								&& ChatFragment.chatFragmentInstance.bookingId
										.trim().equalsIgnoreCase(
												bookingId.trim())) {
							nMgr.cancelAll();

							MainFragmentActivity.getInstance().runOnUiThread(
									new Runnable() {

										@Override
										public void run() {
											Log.i("IntentReceiver",
													alertMessage);

											if (alertMessage
													.trim()
													.toLowerCase()
													.matches(
															"arrived at pick up")) {
												Log.i("IntentReceiver",
														alertMessage);
												ChatFragment myChatFragment = (ChatFragment) ChatFragment.chatFragmentInstance;
												myChatFragment.fromOutside = true;
												myChatFragment
														.refreshList(alertMessage);
												myChatFragment.playsound();
											} else {
												ChatFragment myChatFragment = (ChatFragment) ChatFragment.chatFragmentInstance;
												myChatFragment.fromOutside = true;
												myChatFragment
														.refreshList(alertMessage);
												if (sound.length() > 0)
													myChatFragment.playsound();
											}

										}
									});

						} else {
							if (MainFragmentActivity.getInstance() != null
									&& MainFragmentActivity.isVisible) {
								MainFragmentActivity.getInstance()
										.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												alertdialog(alertMessage,
														"New message");
											}
										});

							}
						}

					}
					logPushExtras(intent);
				}
			} else if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {

				Log.i(getClass().getSimpleName(), "ACTION_NOTIFICATION_OPENED"
						+ bookingId + "");

				nMgr = (NotificationManager) UAirship.shared()
						.getApplicationContext()
						.getSystemService(Context.NOTIFICATION_SERVICE);

				Log.i(logTag,
						"User clicked notification. Message: "
								+ intent.getStringExtra(PushManager.EXTRA_ALERT));

				if (booking_type.matches("messagetopass")) {
					Log.i("IntentReceiver", alertMessage);
					nMgr.cancelAll();

					if (MainFragmentActivity.getInstance() != null
							&& MainFragmentActivity.isVisible) {
						ChatFragment edit;
						if (alertMessage.trim().toLowerCase()
								.matches("arrived at pick up"))
							edit = new ChatFragment(bookingId, true, false);
						else if (sound.length() > 0)
							edit = new ChatFragment(bookingId, false, true);
						else
							edit = new ChatFragment(bookingId, false, false);
						FragmentTransaction ft = null;
						if (HailNowParentFragment.fragment != null) {
							ft = HailNowParentFragment.fragment
									.getChildFragmentManager()
									.beginTransaction();
						}

						else if (FavoritesParentFragment.fragment != null) {

							ft = FavoritesParentFragment.fragment
									.getChildFragmentManager()
									.beginTransaction();
						} else if (MyBookingsParentFragment.fragment != null) {

							ft = MyBookingsParentFragment.fragment
									.getChildFragmentManager()
									.beginTransaction();
						} else if (PreBookParentFragment.fragment != null) {

							ft = PreBookParentFragment.fragment
									.getChildFragmentManager()
									.beginTransaction();
						} else if (MoreParentFragment.fragment != null) {

							ft = MoreParentFragment.fragment
									.getChildFragmentManager()
									.beginTransaction();
						} else if (MyAccountParentFragment.fragment != null) {

							ft = MyAccountParentFragment.fragment
									.getChildFragmentManager()
									.beginTransaction();
						}

						if (ft != null) {
							ft.addToBackStack(null);
							ft.replace(R.id.frame_container, edit,
									"chatFragment");

							ft.commit();
						}

					} else {
						Intent launchIntent = new Intent(
								context.getApplicationContext(),
								MainFragmentActivity.class);

						launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_NEW_TASK);

						if (action != null && launchIntent != null) {
							Bundle toFragment = new Bundle();
							toFragment.putString("toFragment", "chatFragment");
							toFragment.putString("bookingId", bookingId);
							toFragment.putString("alertMessage", alertMessage);
							toFragment.putString("sound", sound);
							launchIntent.putExtra("toFragment", toFragment);
							context.startActivity(launchIntent);

						}
					}
				}

				if (booking_type.matches("bookingaccepted")) {
					if (MainFragmentActivity.getInstance() != null
							&& MainFragmentActivity.isVisible) {
						alertdialog("Booking Accepted", "Alert");
						nMgr.cancelAll();
					} else {

						Intent launchIntent = new Intent(
								context.getApplicationContext(),
								MainFragmentActivity.class);
						nMgr.cancelAll();
						launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_NEW_TASK);

						context.startActivity(launchIntent);
						Bundle toFragment = new Bundle();
						toFragment.putString("toFragment", "driverFragment");
						toFragment.putString("bookingId", bookingId);
						toFragment.putString("alertMessage", alertMessage);
						launchIntent.putExtra("toFragment", toFragment);
						context.startActivity(launchIntent);
					}
				}

			}
		} else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED)) {
			Log.i(logTag,
					"Registration complete. APID:"
							+ intent.getStringExtra(PushManager.EXTRA_APID)
							+ ". Valid: "
							+ intent.getBooleanExtra(
									PushManager.EXTRA_REGISTRATION_VALID, false));
		}

		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	private void logPushExtras(Intent intent) {
		Set<String> keys = intent.getExtras().keySet();
		for (String key : keys) {

			// ignore standard extra keys (GCM + UA)
			List<String> ignoredKeys = (List<String>) Arrays.asList(
					"collapse_key",// GCM collapse key
					"from",// GCM sender
					PushManager.EXTRA_NOTIFICATION_ID,// int id of generated
														// notification
														// (ACTION_PUSH_RECEIVED
														// only)
					PushManager.EXTRA_PUSH_ID,// internal UA push id
					PushManager.EXTRA_ALERT);// ignore alert
			if (ignoredKeys.contains(key)) {
				continue;
			}
			Log.i(logTag,
					"Push Notification Extra: [" + key + " : "
							+ intent.getStringExtra(key) + "]");
		}
	}

	public boolean isForeground(String myPackage) {
		ActivityManager manager = (ActivityManager) UAirship
				.shared()
				.getApplicationContext()
				.getSystemService(
						UAirship.shared().getApplicationContext().ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager
				.getRunningTasks(1);

		ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
		if (componentInfo.getPackageName().equals(myPackage))
			return true;
		return false;
	}

	public void alertdialog(final String message, final String title_alert) {
		TextView title = new TextView(MainFragmentActivity.getInstance());
		title.setTextColor(Color.parseColor("#d16413"));
		title.setText(title_alert);
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		if (MainFragmentActivity.slidingMenu != null
				&& MainFragmentActivity.slidingMenu.isMenuShowing())

			MainFragmentActivity.slidingMenu.toggle();

		if (title_alert.equals("New message")) {

			chatBuilder = new AlertDialog.Builder(
					MainFragmentActivity.getInstance());
			chatBuilder.setCustomTitle(title);
			chatBuilder.setMessage(message);

			chatBuilder.setCancelable(false);

			chatBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							chatBuilder = null;
							dialog.dismiss();
							if (title_alert.equals("New message")) {
								if (ChatFragment.chatFragmentInstance == null)
									messgetopass();
								else if (!ChatFragment.chatFragmentInstance.bookingId
										.trim().equalsIgnoreCase(
												bookingId.trim())) {
									ChatFragment newChatFragment = null;
									if (sound.length() > 0)
										newChatFragment = new ChatFragment(
												bookingId, false, true);
									else
										newChatFragment = new ChatFragment(
												bookingId, false, false);

									FragmentTransaction ft = ChatFragment.chatFragmentInstance
											.getParentFragment()
											.getChildFragmentManager()
											.beginTransaction();

									ft.replace(R.id.frame_container,
											newChatFragment, "chatFragment");

									ft.commit();
								}

							}
							if (MainFragmentActivity.slidingMenu != null)
								MainFragmentActivity.slidingMenu
										.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

						}
					});
			if (chatAlert == null && chatBuilder != null)
				chatAlert = chatBuilder.show();
			else if (chatAlert != null && chatBuilder != null) {
				chatAlert.dismiss();
				chatAlert = chatBuilder.show();
			}

		} else {
			bookingBuilder = new AlertDialog.Builder(
					MainFragmentActivity.getInstance());
			bookingBuilder.setCustomTitle(title);
			bookingBuilder.setMessage(message);

			bookingBuilder.setCancelable(false);

			bookingBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							dialog.dismiss();
							bookingAlert = null;
							if (MainFragmentActivity.slidingMenu != null)
								MainFragmentActivity.slidingMenu
										.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

							if (title_alert.equals("Alert")) {
								Bookings_Driver_DetailsFragment edit = new Bookings_Driver_DetailsFragment(
										false, bookingId);
								FragmentTransaction ft = null;
								if (HailNowParentFragment.fragment != null) {

									ft = HailNowParentFragment.fragment
											.getChildFragmentManager()
											.beginTransaction();
								}

								else if (FavoritesParentFragment.fragment != null) {

									ft = FavoritesParentFragment.fragment
											.getChildFragmentManager()
											.beginTransaction();
								} else if (MyBookingsParentFragment.fragment != null) {

									ft = MyBookingsParentFragment.fragment
											.getChildFragmentManager()
											.beginTransaction();
								} else if (PreBookParentFragment.fragment != null) {

									ft = PreBookParentFragment.fragment
											.getChildFragmentManager()
											.beginTransaction();
								} else if (MoreParentFragment.fragment != null) {

									ft = MoreParentFragment.fragment
											.getChildFragmentManager()
											.beginTransaction();
								} else if (MyAccountParentFragment.fragment != null) {

									ft = MyAccountParentFragment.fragment
											.getChildFragmentManager()
											.beginTransaction();
								} else if (CabApp_Location_ParentFragment.fragment != null) {

									ft = CabApp_Location_ParentFragment.fragment
											.getChildFragmentManager()
											.beginTransaction();
								} else {
									Log.e("Fragment Transaction is Null ",
											"FT is null");
								}
								if (ft != null) {

									ft.replace(R.id.frame_container, edit,
											"bookingsDriverDetailsFragment");
									// if(FindTaxiFragment.isActive==null)
									ft.addToBackStack(null);

									ft.commit();
								} else {
									Log.e("Fragment Transaction is Null",
											"Cannot Replace FT");
								}
							}

						}
					});
			if (bookingAlert == null && bookingBuilder != null)
				bookingAlert = bookingBuilder.show();
			else if (bookingBuilder != null) {
				bookingAlert.dismiss();
				bookingAlert = bookingBuilder.show();
			}

		}

	}

	public void messgetopass() {
		// nMgr.cancelAll();
		nMgr = (NotificationManager) UAirship.shared().getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (nMgr != null)
			nMgr.cancelAll();
		if (MainFragmentActivity.getInstance() != null
				&& MainFragmentActivity.isVisible) {

			MainFragmentActivity.getInstance().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Log.i("IntentReceiver", alertMessage);
					ChatFragment edit;
					if (alertMessage.trim().toLowerCase()
							.matches("arrived at pick up"))
						edit = new ChatFragment(bookingId, true, false);
					else if (sound.length() > 0)
						edit = new ChatFragment(bookingId, false, true);
					else
						edit = new ChatFragment(bookingId, false, false);

					FragmentTransaction ft = null;
					if (HailNowParentFragment.fragment != null) {
						ft = HailNowParentFragment.fragment
								.getChildFragmentManager().beginTransaction();
					}

					else if (FavoritesParentFragment.fragment != null) {

						ft = FavoritesParentFragment.fragment
								.getChildFragmentManager().beginTransaction();
					} else if (MyBookingsParentFragment.fragment != null) {

						ft = MyBookingsParentFragment.fragment
								.getChildFragmentManager().beginTransaction();
					} else if (PreBookParentFragment.fragment != null) {

						ft = PreBookParentFragment.fragment
								.getChildFragmentManager().beginTransaction();
					} else if (MoreParentFragment.fragment != null) {

						ft = MoreParentFragment.fragment
								.getChildFragmentManager().beginTransaction();
					} else if (MyAccountParentFragment.fragment != null) {

						ft = MyAccountParentFragment.fragment
								.getChildFragmentManager().beginTransaction();
					} else if (CabApp_Location_ParentFragment.fragment != null) {

						ft = CabApp_Location_ParentFragment.fragment
								.getChildFragmentManager().beginTransaction();
					}

					if (ft != null) {
						ft.addToBackStack(null);
						ft.replace(R.id.frame_container, edit, "chatFragment");

						ft.commit();
					}

				}
			});

		}

	}
}