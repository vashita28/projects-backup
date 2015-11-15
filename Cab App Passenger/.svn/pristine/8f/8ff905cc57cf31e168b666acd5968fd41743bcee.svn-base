package com.example.cabapppassenger.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.task.GetBookingsTask;
import com.example.cabapppassenger.util.Constant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class RootFragment extends Fragment implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	RelativeLayout relMenu;
	String mCurrentFragment;
	Editor editor;
	SharedPreferences shared_pref;
	Context mContext;
	String PREF_NAME = "CabApp_Passenger";
	private LocationRequest mLocationRequest;
	// Stores the current instantiation of the location client in this object
	private LocationClient mLocationClient;
	Handler mHandler;
	Bundle mMainBundle;

	public void initWidgets(View rootView, String currentFragment) {
		relMenu = (RelativeLayout) rootView.findViewById(R.id.relMenu);
		mCurrentFragment = currentFragment;
		mContext = getActivity();
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;
					if (bundleData.containsKey("bookingsData")) {
						parse_bookingsData(bundleData.getString("bookingsData",
								""));
					}
				}
			}
		};
		if (relMenu != null)
			relMenu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MainFragmentActivity.slidingMenu.toggle();
					GetBookingsTask getBookingsTask = new GetBookingsTask(
							getActivity(), Constant.passengerURL
									+ "ws/v2/passenger/bookings/?accessToken="
									+ shared_pref.getString("AccessToken", ""),
							mHandler, true);
					getBookingsTask.execute();
					hideKeybord(v);
				}
			});

	}

	public void parse_bookingsData(String bookingsdata) {
		// TODO Auto-generated method stub
		try {
			JSONObject jobj = new JSONObject(bookingsdata);
			String pendingBookingsCount = jobj
					.getString("pendingBookingsCount");
			Message msg = new Message();
			msg.obj = pendingBookingsCount;
			MainFragmentActivity.mTextCountUpdateHandler.sendMessage(msg);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// public void getLocation(View v) {
	//
	// // If Google Play Services is available
	// if (servicesConnected()) {
	// // Get the current location
	// Location currentLocation = mLocationClient.getLastLocation();
	//
	// }
	// }

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	// private boolean servicesConnected() {
	//
	// // Check that Google Play services is available
	// int resultCode = GooglePlayServicesUtil
	// .isGooglePlayServicesAvailable(getActivity());
	//
	// // If Google Play services is available
	// if (ConnectionResult.SUCCESS == resultCode) {
	//
	// return true;
	// // Google Play services was not available for some reason
	// } else {
	//
	// return false;
	// }
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Create a new global location parameters object
		mLocationRequest = LocationRequest.create();

		/*
		 * Set the update interval
		 */
		mLocationRequest.setInterval(Constant.UPDATE_INTERVAL_IN_MILLISECONDS);

		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		mLocationRequest
				.setFastestInterval(Constant.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		mLocationClient = new LocationClient(getActivity(), this, this);
	}

	@Override
	public void onStart() {

		super.onStart();
		mLocationClient.connect();

	}

	@Override
	public void onStop() {

		// If the client is connected
		if (mLocationClient.isConnected()) {
			stopPeriodicUpdates();
		}

		// After disconnect() is called, the client is considered "dead".
		mLocationClient.disconnect();

		super.onStop();
	}

	private void stopPeriodicUpdates() {
		mLocationClient.removeLocationUpdates(this);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LocationManager lm = null;
		boolean gps_enabled = false, network_enabled = false;
		if (lm == null)
			lm = (LocationManager) getActivity().getSystemService(
					Context.LOCATION_SERVICE);
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		if (!gps_enabled && !network_enabled) {
			showAlertDialog();
		}
	}

	private void showAlertDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(getString(R.string.gps_alert_message))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.settings),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(intent);
							}
						})
				.setNegativeButton(getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.setTitle(getString(R.string.gps_enable));
		AlertDialog alert = builder.create();
		alert.show();
	}
}
