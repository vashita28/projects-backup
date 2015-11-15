package com.android.cabapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.android.cabapp.R;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.crashlytics.android.Crashlytics;

public class SplashScreenActivity extends Activity {
	private static final String TAG = SplashScreenActivity.class
			.getSimpleName();
	private long splashDelay = 3000;
	private Thread mSplashThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Crashlytics.start(this);
		setContentView(R.layout.activity_splashscreen);

		final String access_token = Util
				.getAccessToken(SplashScreenActivity.this);
		Util.mContext = SplashScreenActivity.this;

		// if (!access_token.isEmpty()) {
		// DriverDetailsTask driverDetailsTask = new DriverDetailsTask();
		// driverDetailsTask.mContext = SplashScreenActivity.this;
		// driverDetailsTask.execute();
		//
		// DriverSettingsTask driverSettingTask = new DriverSettingsTask();
		// driverSettingTask.mContext = SplashScreenActivity.this;
		// driverSettingTask.execute();
		// }

		// The thread to wait for splash screen events
		mSplashThread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						// Wait given period of time or exit on touch
						wait(splashDelay);
					}
				} catch (InterruptedException ex) {
				}

				// Run next activity
				Intent intent = new Intent();

				boolean isAuthorised = Util
						.getIsAuthorised(SplashScreenActivity.this);

				boolean isDocumentUploaded = Util
						.getIsDocumentsUploaded(SplashScreenActivity.this);

				if (Constants.isDebug) {
					if (!access_token.isEmpty())
						Log.d("SplashScreen", "Token:: " + access_token
								+ " Authorised:: " + isAuthorised
								+ " Document Uploaded:: " + isDocumentUploaded);

				}
				if (isAuthorised) {
					if (access_token.isEmpty()) {
						intent.setClass(
								SplashScreenActivity.this,
								com.android.cabapp.activity.LandingActivity.class);
					} else if (access_token != null && !access_token.equals("")) {
						Constants.accessToken = access_token;
						intent.setClass(SplashScreenActivity.this,
								com.android.cabapp.activity.MainActivity.class);
					}
				} else if (!Util.getDriverID(SplashScreenActivity.this)
						.isEmpty()) {
					if (isDocumentUploaded) {
						intent.setClass(
								SplashScreenActivity.this,
								com.android.cabapp.activity.TutorialActivity.class);
					} else {
						intent.setClass(
								SplashScreenActivity.this,
								com.android.cabapp.activity.DocumentUploadActivity.class);
					}
				} else {
					if (Util.getIsRememberMeChecked(SplashScreenActivity.this)) {
						intent.setClass(SplashScreenActivity.this,
								com.android.cabapp.activity.LogInActivity.class);
					} else {
						intent.setClass(
								SplashScreenActivity.this,
								com.android.cabapp.activity.LandingActivity.class);
					}
				}

				startActivity(intent);
				finish();
			}
		};
		mSplashThread.start();
	}

	/**
	 * Processes splash screen touch events
	 */
	@Override
	public boolean onTouchEvent(MotionEvent evt) {
		if (evt.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (mSplashThread) {
				mSplashThread.notifyAll();
			}
		}
		return true;
	}

}
