package com.example.cabapppassenger.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.LoginActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.receivers.IntentReceiver;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.CancelBookingTask;
import com.google.android.gms.maps.model.LatLng;
import com.urbanairship.push.CustomPushNotificationBuilder;
import com.urbanairship.push.PushManager;

public class Constant {

	public static final String passengerURL = "http://cabapp.pocketapp.co.uk/";
	// public static final String passengerURL =
	// "http://cabapp.dev.pocketapp.co.uk/";
	// public static final String passengerURL =
	// "http://192.168.0.11:8081/ws/v2/passenger/";
	public static HashMap<String, Bitmap> hashmap_countrycode;
	public static boolean flag_fromslider_favourites = false;
	public static boolean flag_payment_method = false;
	public static boolean flag_from_onmap = false;
	public static boolean flag_pushnotification = false;
	public static ArrayList<String> arr_dialingcode = new ArrayList<String>();
	public static ArrayList<String> arr_passengercount = new ArrayList<String>();
	public static ArrayList<String> arr_favourites = new ArrayList<String>();
	public static final String accessToken = "877B1BC6DFE1D0003B56BEDF98FFF852F1A7FC58";
	public static final HashMap<String, LatLng> hash_latng_recent = new HashMap<String, LatLng>();
	public static final HashMap<String, String> hash_favid_favourites = new HashMap<String, String>();
	public static final HashMap<String, LatLng> hash_latng_favourites = new HashMap<String, LatLng>();
	public static HashMap<String, ArrayList<ArrayList<String>>> hashmap_passengercard = new HashMap<String, ArrayList<ArrayList<String>>>();
	public static final String USER_CARD_KEY = "userCardkey";
	public static final int HAILNOW_FRAGMENT = 0;
	public static final int PRE_BOOK_FRAGMENT = 1;
	public static final int MY_BOOKINGS_FRAGMENT = 2;
	public static final int FAVOURITES_FRAGMENT = 3;
	public static final int CabApp_Location_FRAGMENT = 4;
	public static final int MORE_FRAGMENT = 5;

	public static boolean flag_onhailnow_recent = false;
	public static boolean flag_added_newcard = false;
	public static boolean flag_bookingdetails_edit = false;
	public static boolean flag_onhailnow_fav = false;
	static AlertDialog cancelAlert;

	public static Location getLocation(Context context) {
		Location location = null;
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		return location;
	}

	public static final int MILLISECONDS_PER_SECOND = 1000;

	// The update interval
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

	// A fast interval ceiling
	public static final int FAST_CEILING_IN_SECONDS = 1;

	// Update interval in milliseconds
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;

	// A fast ceiling of update intervals, used when the app is visible
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* FAST_CEILING_IN_SECONDS;

	public static final String md5(final String password) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(password.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void hideKeybord(View view, Context mContext) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	public static boolean isEmailAddressValid(String email) {
		boolean isEmailValid = false;

		String strExpression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern objPattern = Pattern.compile(strExpression,
				Pattern.CASE_INSENSITIVE);
		Matcher objMatcher = objPattern.matcher(inputStr);
		if (objMatcher.matches()) {
			isEmailValid = true;
		}
		return isEmailValid;
	}

	public static void alertdialog(final String message, Context mContext) {
		TextView title = new TextView(mContext);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCustomTitle(title);

		if (message == "emptyfields") {
			builder.setMessage("Please enter all the details.");
		}
		if (message == "select_dropoff") {
			builder.setMessage("Please add Drop Off.");
		}
		if (message == "invalidemail") {
			builder.setMessage("Please enter valid Email.");
		}
		if (message == "passwordlessthan") {
			builder.setMessage("Password must be at least 6 characters.");
		}
		if (message == "passwordnotmatches") {
			builder.setMessage("Password and confirm password does not match.");
		}
		if (message == "invalidmobile") {
			builder.setMessage("Please enter a valid mobile number.");
		}
		if (message == "passwordmatches") {
			builder.setMessage("Password and confirm Password does not match.");
		}
		if (message == "notpasswordmatches") {
			builder.setMessage("Old Password and new Password should not be same.");
		}
		if (message == "Pending") {
			builder.setMessage("This booking is not accepted by any driver.");
		}
		if (message == "Cancelled") {
			builder.setMessage("This booking is cancelled.");
		}
		if (message == "nocardadded") {
			builder.setMessage("No card added so please add new card or select cash option");
		}
		if (message == "nofeedback") {
			builder.setMessage("Please enter your feedback.");
		}

		builder.setCancelable(false);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}
		});

		AlertDialog alert = builder.show();
		if (alert != null) {
			TextView messageText = (TextView) alert
					.findViewById(android.R.id.message);
			if (messageText != null) {
				messageText.setGravity(Gravity.CENTER_VERTICAL);
				messageText.setTextColor(Color.BLACK);
				messageText.setTextSize(16);
			}
		}

	}

	public static void cancel_dialog(final String message,
			final Context mContext, final String bookingid,
			final Handler mHandler) {
		TextView title = new TextView(mContext);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCustomTitle(title);

		if (message == "cancel_sure") {
			builder.setMessage("Are you sure you want to cancel the booking?");
		}
		builder.setCancelable(false);
		builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
					CancelBookingTask task = new CancelBookingTask(mContext);
					task.booking_id = bookingid;
					task.handler = mHandler;
					task.execute();
				} else {
					Toast.makeText(mContext, "No network connection.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				// CancelBookingTask task = new CancelBookingTask(mContext);
				// task.booking_id = bookingid;
				// task.handler = mHandler;
				// task.execute();
				// } else {
				// Toast.makeText(mContext, "No network connection.",
				// Toast.LENGTH_SHORT).show();
				// }
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.show();
		if (alert != null) {
			TextView messageText = (TextView) alert
					.findViewById(android.R.id.message);
			if (messageText != null) {
				messageText.setGravity(Gravity.CENTER_VERTICAL);
				messageText.setTextColor(Color.BLACK);
				messageText.setTextSize(16);
			}
		}

	}

	public static void togglepush() {

		CustomPushNotificationBuilder nb = new CustomPushNotificationBuilder();
		nb.layout = R.layout.customnotification_layout;
		nb.layoutIconId = R.id.iv_appicon;
		nb.layoutSubjectId = R.id.tv_appname;
		nb.layoutMessageId = R.id.tv_message;
		PushManager.shared().setNotificationBuilder(nb);
		PushManager.enablePush();
		PushManager.shared().setIntentReceiver(IntentReceiver.class);

	}

	public static void logout(Context mContext, Editor editor,
			boolean flag_logout) {
		if (flag_logout) {
			logout("logout", mContext, editor);
			flag_logout = false;

		}
	}

	public static void logout(String message, final Context mContext,
			final Editor editor) {
		TextView title = new TextView(mContext);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCustomTitle(title);

		if (message == "logout") {
			builder.setMessage("Your session has expired. Please log in again.");
		}

		builder.setCancelable(false);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				try {
					dialog.cancel();
					editor.clear();
					editor.commit();

					Intent landing = new Intent(mContext, LoginActivity.class);
					mContext.startActivity(landing);
					PushManager.disablePush();
					PushManager.shared().setNotificationBuilder(null);
					Constant.hashmap_passengercard.clear();
					Constant.hash_favid_favourites.clear();
					Constant.hash_latng_favourites.clear();
					Constant.hash_latng_recent.clear();
					MainFragmentActivity.finishMe();
				} catch (Exception e) {

				}
			}
		});
		try {
			if (cancelAlert == null && builder != null)
				cancelAlert = builder.show();
			else if (cancelAlert != null && builder != null) {
				cancelAlert.dismiss();
				cancelAlert = builder.show();

			}
		} catch (Exception e) {

		}
	}
}
