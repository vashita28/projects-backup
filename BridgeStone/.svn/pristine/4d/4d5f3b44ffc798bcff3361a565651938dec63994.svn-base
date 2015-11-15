package co.uk.pocketapp.bridgestone.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.Window;
import co.uk.pocketapp.bridgestone.R;
import co.uk.pocketapp.bridgestone.util.AppValues;

import com.crashlytics.android.Crashlytics;

public class SplashScreenActivity extends Activity {
	private long splashDelay = 3000;
	private Thread mSplashThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		final SplashScreenActivity sPlashScreen = this;
		AppValues.mapSensorValues.clear();
		AppValues.mapSensorValuesBelowMinimumPressure.clear();
		deleteLogFile();
		// SelectionFragment.bIsPhotosLoaded = false;

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

				finish();

				// Run next activity
				Intent intent = new Intent();
				intent.setClass(sPlashScreen, LoginActivity.class);
				startActivity(intent);
				// stop();
				finish();
			}
		};
		mSplashThread.start();
		Crashlytics.start(this);
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

	void deleteLogFile() {
		if (AppValues.bIsDebug) {
			File bridgeStone = new File(
					Environment.getExternalStorageDirectory() + File.separator
							+ "Bridgestone");
			if (!bridgeStone.isDirectory()) {
				bridgeStone.mkdir();
			}

			File logFile = new File(bridgeStone, "log.txt");
			if (logFile.exists()) {
				logFile.delete();
			}
		}
	}
}
