package uk.co.pocketapp.whotel.ui;

import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.util.Util;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.crashlytics.android.Crashlytics;

public class SplashActivity extends Activity {
	private long splashDelay = 3000;
	private Thread mSplashThread;
	public Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.activity_splash);

		final SplashActivity sPlashScreen = this;

		if (getIntent().getExtras() != null) {
			// Log.d("",
			// "SPLASH PUSH" + getIntent().getExtras().getString("isPush"));
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("isPush", "true");
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			finish();
			startActivity(intent);
		} else {
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
					if (!Util.getAgreeContinueAccepted(SplashActivity.this)) {
						intent.setClass(sPlashScreen,
								AgreeAndContinueActivity.class);
					} else {
						intent.setClass(sPlashScreen,
								uk.co.pocketapp.whotel.ui.MainActivity.class);
					}
					startActivity(intent);
					finish();
					// stop();
				}
			};
			mSplashThread.start();
		}
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
