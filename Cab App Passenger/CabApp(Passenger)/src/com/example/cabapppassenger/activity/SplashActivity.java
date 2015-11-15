package com.example.cabapppassenger.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.crashlytics.android.Crashlytics;
import com.example.cabapppassenger.R;

public class SplashActivity extends Activity {

	private long splashDelay = 3000;
	private Thread mSplashThread;
	static Context mContext;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		mContext = this;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		Log.e("Access Token", "" + shared_pref.getString("AccessToken", ""));

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				sessionmaintain();
			}
		});

	}

	private void sessionmaintain() {
		// TODO Auto-generated method stub
		if (shared_pref.getString("AccessToken", "") != null
				&& !shared_pref.getString("AccessToken", "").matches(" ")
				&& shared_pref.getString("AccessToken", "").length() > 0) {

			mSplashThread = new Thread() {
				@Override
				public void run() {
					synchronized (this) {
						// Wait given period of time or exit on touch
						try {
							wait(splashDelay);
							Intent login = new Intent(SplashActivity.this,
									MainFragmentActivity.class);
							startActivity(login);
							finish();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			};
			mSplashThread.start();

			// }

		} else {
			mSplashThread = new Thread() {
				@Override
				public void run() {
					synchronized (this) {
						// Wait given period of time or exit on touch
						try {
							wait(splashDelay);
							Intent login = new Intent(SplashActivity.this,
									LandingActivity.class);
							startActivity(login);
							finish();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			};
			mSplashThread.start();
		}

	}

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

}