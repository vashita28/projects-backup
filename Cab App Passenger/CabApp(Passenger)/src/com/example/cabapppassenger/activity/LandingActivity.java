package com.example.cabapppassenger.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.example.cabapppassenger.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LandingActivity extends Activity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		OnConnectionFailedListener {

	TextView btn_login, btn_signup;
	static Context mContext;
	LocationManager lm = null;
	Double latitude, longitude;
	LocationClient mLocationClient;
	Location location;
	LocationRequest mLocationRequest;

	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 10;
	public static final int FAST_CEILING_IN_SECONDS = 1;
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* FAST_CEILING_IN_SECONDS;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_landing);
		mContext = this;
		mLocationClient = new LocationClient(this, this, this);

		mLocationRequest = LocationRequest.create();
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest
				.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		// if (Constant.arr_dialingcode != null
		// & Constant.arr_dialingcode.size() <= 0) {
		//
		// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
		// GetCountryCodeTask code = new GetCountryCodeTask(mContext);
		// code.fromlanding_flag = true;
		// code.execute("");
		// } else {
		// Toast.makeText(mContext, "No network connection",
		// Toast.LENGTH_SHORT).show();
		// }
		// }
		btn_login = (TextView) findViewById(R.id.tvLogIn);
		btn_signup = (TextView) findViewById(R.id.tvSignUp);

		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent login = new Intent(mContext, LoginActivity.class);
				login.putExtra("Latitude", latitude);
				login.putExtra("Longitude", longitude);
				startActivity(login);
				finish();
			}
		});

		btn_signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent signup = new Intent(mContext, SignupActivity.class);
				signup.putExtra("Latitude", latitude);
				signup.putExtra("Longitude", longitude);
				startActivity(signup);
			}
		});
	}

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mLocationClient.connect();
		boolean gps_enabled = false, network_enabled = false;
		if (lm == null)
			lm = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		if (!gps_enabled) {// && !network_enabled
			showAlertDialog();
		}
	}

	private void showAlertDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setMessage(getString(R.string.gps_alert_message))
				.setCancelable(false)
				.setPositiveButton(getString(R.string.settings),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								startActivity(intent);
								dialog.cancel();
							}
						})
				.setNegativeButton(getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		builder.setTitle(getString(R.string.gps_enable));
		if (alert == null && builder != null)
			alert = builder.show();
		else if (alert != null) {
			alert.dismiss();
			alert = builder.show();
		}
		// AlertDialog alert = builder.create();
		// alert.show();
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
		}
		mLocationClient.disconnect();
	}
}
