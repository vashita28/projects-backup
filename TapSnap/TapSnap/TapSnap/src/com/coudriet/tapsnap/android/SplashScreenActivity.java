package com.coudriet.tapsnap.android;

import com.crashlytics.android.Crashlytics;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreenActivity extends Activity {
	private long splashDelay = 3000;
	private Thread mSplashThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Crashlytics.start(this);
		setContentView(R.layout.activity_splash_screen);

		final SplashScreenActivity sPlashScreen = this;

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

				// finish();

				// Run next activity
				Intent intent = new Intent();
				intent.setClass(sPlashScreen,
						com.coudriet.tapsnap.android.MainActivity.class);
				startActivity(intent);
				// stop();
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
