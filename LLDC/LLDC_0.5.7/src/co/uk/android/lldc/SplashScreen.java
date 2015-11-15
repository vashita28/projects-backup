package co.uk.android.lldc;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import co.uk.android.lldc.models.DashboardModel;
import co.uk.android.lldc.service.LLDC_Sync_Servcie;

import com.crashlytics.android.Crashlytics;

public class SplashScreen extends Activity {

	public static Handler mHandler = null;

	ProgressDialog dialog = null;

	boolean isServerStarted = false;
	
	TextView tvVersion;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.activity_splash);

		LLDCApplication.init(getApplicationContext());

		// your coords of course
		LLDCApplication.parkCenter.setLatitude(18.93497658);
		LLDCApplication.parkCenter.setLongitude(72.83402252);
		
		if (LLDCApplication.isDebug) {
			LLDCApplication.nextServerCall = 60000; 
			LLDCApplication.refershInterval = 60000;
		}
		
		WindowManager wm = (WindowManager) getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		LLDCApplication.screenWidth = size.x;
		LLDCApplication.screenHeight = size.y;
		
		// onInitiateApplication();
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg.what == 1002) {
					// if (isServerStarted) {
					if (dialog != null) {
						dialog.setMessage("Loading...");
						dialog.show();
					}
					// } else {
					// Intent msgIntent = new Intent(SplashScreen.this,
					// HomeActivity.class);
					// startActivity(msgIntent);
					// finish();
					// }
				} else if (msg.what == 1005) {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
					Intent msgIntent = new Intent(SplashScreen.this,
							HomeActivity.class);
					startActivity(msgIntent);
					finish();
				}

			};
		};

		tvVersion = (TextView) findViewById(R.id.tvVersion);
		
		tvVersion.setText(LLDCApplication.VERSION);
		
		dialog = new ProgressDialog(SplashScreen.this);
		dialog.setCanceledOnTouchOutside(false);

		if (LLDCApplication.isConnectingToInternet(getApplicationContext())) {
			onInitiateApplication();
		} else {
			LLDCApplication.onShowToastMesssage(getApplicationContext(),
					"Please check your internet connection...");
			onCheckDataPresent();
		}

	}

	public void onCheckDataPresent() {
		DashboardModel dashboardmodel = LLDCApplication.DBHelper
				.onGetDashBoardData();
		if (!dashboardmodel.getWelcomeMsgIn().equals("")) {
			LLDCApplication.parkCenter
					.setLatitude(Double.parseDouble(dashboardmodel.getParkLat()
							.toString().trim()));
			LLDCApplication.parkCenter
					.setLongitude(Double.parseDouble(dashboardmodel
							.getParkLon().toString().trim()));
			mHandler.sendEmptyMessageDelayed(1005,
					LLDCApplication.SPLASH_TIME_OUT);
		}
	}

	public void onInitiateApplication() {
		try {
			long time = Long.parseLong(LLDCApplication
					.getServerTime(SplashScreen.this));
			Long currTime = Calendar.getInstance().getTimeInMillis();
			if (currTime - time > LLDCApplication.refershInterval) {
				Intent msgIntent = new Intent(this, LLDC_Sync_Servcie.class);
				startService(msgIntent);
				isServerStarted = true;
				LLDCApplication.nextServerCall = LLDCApplication.refershInterval;
				mHandler.sendEmptyMessageDelayed(1002,
						LLDCApplication.SPLASH_TIME_OUT);
			} else {
				LLDCApplication.nextServerCall = currTime - time;
				mHandler.sendEmptyMessageDelayed(1005,
						LLDCApplication.SPLASH_TIME_OUT);
			}
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("SplashActivity",
					"onInitiateApplication exception is " + e.toString());
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			mHandler = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
	}

}
