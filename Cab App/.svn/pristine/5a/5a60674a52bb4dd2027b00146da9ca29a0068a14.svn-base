package com.android.cabapp.fragments;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.activity.ChatActivity;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.model.UpdateLocation;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.BackKeyPressedCallbackListener;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class RootFragment extends Fragment implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener,
		BackKeyPressedCallbackListener {
	RelativeLayout relMenu, relTextBadgeFilter;
	ImageView ivBack, ivLogo;

	String mCurrentFragment;
	TextView textBadge, textBadgeFilter, textCashBack;

	Bundle mMainBundle;

	// A request to connect to Location Services
	private LocationRequest mLocationRequest;

	// Stores the current instantiation of the location client in this object
	private LocationClient mLocationClient;

	boolean isFromMyJobs;
	public static BackKeyPressedCallbackListener backkeyPressListener;
	Timer myLocationTimer;

	public static String szJobID = "", szPaymentType = "";
	public static Location currentLocation;

	public void initWidgets(View rootView, String currentFragment) {
		relMenu = (RelativeLayout) rootView.findViewById(R.id.relMenu);
		ivBack = (ImageView) rootView.findViewById(R.id.ivBack);
		ivLogo = (ImageView) rootView.findViewById(R.id.ivLogo);
		textBadge = (TextView) rootView.findViewById(R.id.textBadge);
		textBadgeFilter = (TextView) rootView
				.findViewById(R.id.textBadgeFilter);
		relTextBadgeFilter = (RelativeLayout) rootView
				.findViewById(R.id.relTextBadgeFilter);
		updateJobsCountBadge(AppValues.nJobsCount);
		mCurrentFragment = currentFragment;

		if (relMenu != null)
			relMenu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MainActivity.slidingMenu.toggle();

					// if (getActivity() != null && textBadge != null)
					// Util.hideSoftKeyBoard(getActivity(), textBadge);
				}
			});

		// if (isOverlayVisible) {
		// relMenu.setEnabled(false);
		// } else {
		// relMenu.setEnabled(true);
		// }

		if (ivBack != null)
			ivBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("RootFragment", "onBack");
					handleBackKeyPress();
				}
			});

		if (ivLogo != null)
			ivLogo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("RootFragment", "onLogoClick");

					Fragment fragment = null;
					fragment = new com.android.cabapp.fragments.JobsFragment();
					((MainActivity) getActivity())
							.setSlidingMenuPosition(Constants.JOBS_FRAGMENT);

					if (fragment != null)
						((MainActivity) getActivity()).replaceFragment(
								fragment, false);
				}
			});

		ChatActivity.szJobID = "";

		textCashBack = (TextView) rootView.findViewById(R.id.tvCashBack);
		setCashBackText();
	}

	void setCashBackText() {

		float fCashBack = 0;
		if (AppValues.driverSettings != null) {
			if (AppValues.driverSettings.getCashback() != null) {

				fCashBack = Float.valueOf(AppValues.driverSettings
						.getCashback());
				textCashBack.setText(AppValues.driverSettings
						.getCurrencySymbol()
						+ " "
						+ String.format(Locale.ENGLISH, "%.2f", fCashBack));
			}
		} else {
			textCashBack.setText("-");
		}
	}

	void updateJobsCountBadge(int jobsCount) {
		// // if (relTextBadge != null)
		// // if (jobsCount > 0) {
		// // textBadge.setVisibility(View.VISIBLE);
		// // textBadge.setText(String.valueOf(jobsCount));
		// // relTextBadge.setVisibility(View.VISIBLE);
		// // } else {
		// // textBadge.setVisibility(View.GONE);
		// // relTextBadge.setVisibility(View.GONE);
		// // }
		//
		// if (relTextBadgeFilter != null)
		// if (jobsCount > 0) {
		// textBadgeFilter.setVisibility(View.VISIBLE);
		// textBadgeFilter.setText(String.valueOf(jobsCount));
		// relTextBadgeFilter.setVisibility(View.VISIBLE);
		// } else {
		// relTextBadgeFilter.setVisibility(View.GONE);
		// }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Create a new global location parameters object
		mLocationRequest = LocationRequest.create();

		/*
		 * Set the update interval
		 */
		mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);

		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		// Set the interval ceiling to one minute
		mLocationRequest
				.setFastestInterval(Constants.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		/*
		 * Create a new location client, using the enclosing class to handle
		 * callbacks.
		 */
		mLocationClient = new LocationClient(getActivity(), this, this);

		backkeyPressListener = this;
	}

	/*
	 * Called when the Activity is restarted, even before it becomes visible.
	 */
	@Override
	public void onStart() {

		super.onStart();

		/*
		 * Connect the client. Don't re-start any requests here; instead, wait
		 * for onResume()
		 */
		mLocationClient.connect();

		UpdateLocationTimerTask timerTask = new UpdateLocationTimerTask();
		myLocationTimer = new Timer();
		myLocationTimer.schedule(timerTask, 30000, 30000);
	}

	/*
	 * Called when the Activity is no longer visible at all. Stop updates and
	 * disconnect.
	 */
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

	/**
	 * In response to a request to stop updates, send a request to Location
	 * Services
	 */
	private void stopPeriodicUpdates() {
		mLocationClient.removeLocationUpdates(this);
		// mConnectionState.setText(R.string.location_updates_stopped);
	}

	/**
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected() {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			// Log.d(LocationUtils.APPTAG,
			// getString(R.string.play_services_available));

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			// Display an error dialog
			// Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
			// this, 0);
			// if (dialog != null) {
			// ErrorDialogFragment errorFragment = new ErrorDialogFragment();
			// errorFragment.setDialog(dialog);
			// errorFragment.show(getSupportFragmentManager(),
			// LocationUtils.APPTAG);
			// }
			return false;
		}
	}

	/**
	 * Invoked by the "Get Location" button.
	 * 
	 * Calls getLastLocation() to get the current location
	 * 
	 * @param v
	 *            The view object associated with this method, in this case a
	 *            Button.
	 */
	public void getLocation(View v) {

		// If Google Play Services is available
		if (servicesConnected()) {

			// Get the current location
			// Location currentLocation = mLocationClient.getLastLocation();
			// AppValues.currentLocation = currentLocation;

			// Display the current location in the UI
			// mLatLng.setText(LocationUtils.getLatLng(this, currentLocation));
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		startPeriodicUpdates();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (Constants.isDebug)
			Log.e("RootFragment", "onLocationChanged:: " + location);

		currentLocation = location;
	}

	/**
	 * In response to a request to start updates, send a request to Location
	 * Services
	 */
	private void startPeriodicUpdates() {

		mLocationClient.requestLocationUpdates(mLocationRequest, this);
		// mConnectionState.setText(R.string.location_requested);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (mCurrentFragment.equals(JobAcceptedFragment.class.getName()))
			Util.bIsJobAcceptedFragment = true;
		else
			Util.bIsJobAcceptedFragment = false;

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

	void handleBackKeyPress() {
		Util.hideSoftKeyBoard(Util.mContext, ivBack);
		if (Constants.isDebug)
			Log.d("RootFragment", "onBack-Main Bundle " + mMainBundle);
		Fragment fragment = null;
		if (mCurrentFragment.equals(JobsMapFragment.class.getName())) {
			fragment = new com.android.cabapp.fragments.JobAcceptedFragment();
		} else if (mCurrentFragment.equals(JobAcceptedFragment.class.getName())) {
			if (isFromMyJobs) {
				fragment = new com.android.cabapp.fragments.MyJobsFragment();
				((MainActivity) getActivity())
						.setSlidingMenuPosition(Constants.MY_JOBS_FRAGMENT);
			} else {
				AppValues.clearAllJobsList();
				fragment = new com.android.cabapp.fragments.JobsFragment();
				((MainActivity) getActivity())
						.setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
			}
		}
		// else if (mCurrentFragment
		// .equals(TakePaymentFragment.class.getName())) {
		// fragment = new
		// com.android.cabapp.fragments.CabPayFragment();
		// fragment.setArguments(mMainBundle);
		// }
		else if (mCurrentFragment.equals(CabPayFragment.class.getName())) {
			fragment = new com.android.cabapp.fragments.JobAcceptedFragment();
		} else if (mCurrentFragment.equals(PaymentDetailsFragment.class
				.getName())) {
			fragment = new com.android.cabapp.fragments.CabPayFragment();
		} else if (mCurrentFragment.equals(CabPayReceiptFragment.class
				.getName())
				&& mMainBundle != null
				&& mMainBundle.containsKey(Constants.IS_FROM_HISTORY)) {
			if (mMainBundle.getBoolean(Constants.IS_FROM_HISTORY)) {
				fragment = new com.android.cabapp.fragments.JobAcceptedFragment();
			}
		} else if (mCurrentFragment.equals(CabPayReceiptFragment.class
				.getName())) {
			fragment = new com.android.cabapp.fragments.CabPayFragment();
		} else if (mCurrentFragment.equals(BuyAddCreditsFragment.class
				.getName())) {
			fragment = new com.android.cabapp.fragments.MyAccountFragment();
		} else if (mCurrentFragment.equals(JobAcceptedFragment.class.getName())) {
			if (mMainBundle.containsKey(Constants.IS_FROM_HISTORY)
					&& mMainBundle.getBoolean(Constants.IS_FROM_HISTORY)) {
				fragment = new com.android.cabapp.fragments.MyJobsFragment();
			}
		}

		fragment.setArguments(mMainBundle);
		if (fragment != null)
			((MainActivity) getActivity()).replaceFragment(fragment, false);
	}

	@Override
	public void onBackKeyPressedInFragments() {
		// TODO Auto-generated method stub
		handleBackKeyPress();
	}

	class UpdateLocationTimerTask extends TimerTask {
		public void run() {
			if (Util.mContext != null)
				if (NetworkUtil.isNetworkOn(Util.mContext)) {
					UpdateLocationTask updateLocationTask = new UpdateLocationTask();
					updateLocationTask.execute();
				}
			Intent intent = new Intent();
			intent.setAction("NewJobs");
			LocalBroadcastManager.getInstance(Util.mContext).sendBroadcast(
					intent);
		}
	}

	class UpdateLocationTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (!szJobID.isEmpty() && !szPaymentType.isEmpty()
					&& !Util.getAccessToken(Util.mContext).isEmpty()) {
				UpdateLocation updateLocation = new UpdateLocation(
						Util.mContext);
				updateLocation.updateCurrentLocation(szPaymentType, szJobID,
						currentLocation);
			}
			return null;
		}
	}
}
