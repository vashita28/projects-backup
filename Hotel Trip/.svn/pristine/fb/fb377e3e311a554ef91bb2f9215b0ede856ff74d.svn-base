package com.hoteltrip.android;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.hoteltrip.android.util.DataBaseHelper;

public class SplashActivity extends Activity {
	private long splashDelay = 3000;
	private Thread mSplashThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Crashlytics.start(this);
		setContentView(R.layout.activity_splash);

		final SplashActivity sPlashScreen = this;

		copyDBTask task = new copyDBTask();
		task.context = this;
		task.execute();

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
				intent.setClass(sPlashScreen, FindHotelActivity.class);
				startActivity(intent);
				// stop();
			}
		};

		mSplashThread.start();

		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// startActivity(new Intent(SplashActivity.this,
		// MainActivity.class));
		// finish();
		// }
		// }, splashDelay);
	}

	// class TimerClass extends TimerTask{
	// @Override
	// public void run() {
	// Intent mainIntent = new Intent(getApplicationContext(),
	// MainActivity.class);
	// startActivity(mainIntent);
	// }
	// }

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

	class copyDBTask extends AsyncTask<Void, Void, Void> {

		public Context context;
		DataBaseHelper dbHelper;

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				dbHelper = new DataBaseHelper(context);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dbHelper.myDataBase == null) {
				dbHelper.opendatabase();
			}
			printDatabaseCount();
		}

		void printDatabaseCount() {
			Cursor c = dbHelper.myDataBase.rawQuery("SELECT * FROM codes;",
					null);
			if (c != null)
				Log.d("SplashActivity", "db count:: " + c.getCount());
		}
	}

}
