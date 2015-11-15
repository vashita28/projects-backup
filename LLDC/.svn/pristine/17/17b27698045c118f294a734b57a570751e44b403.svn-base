package co.uk.android.lldc.tablet;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import co.uk.android.lldc.R;
import co.uk.android.lldc.async.DownloadFileAsync;
import co.uk.android.lldc.async.ExtractAsyntsk;
import co.uk.android.lldc.models.DashboardModel;
import co.uk.android.lldc.models.WiFiModel;
import co.uk.android.lldc.service.LLDC_Sync_Servcie;
import co.uk.android.lldc.utils.Constants;
import co.uk.android.lldc.utils.Utils;

import com.crashlytics.android.Crashlytics;

public class SplashScreen extends Activity {
	private static final String TAG = SplashScreen.class.getSimpleName();

	public static Handler mHandler = null;

	ProgressDialog dialog = null;

	boolean isServerStarted = false;

	boolean bIsTablet = false;

	static Context mContext;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		bIsTablet = Utils.isTablet(SplashScreen.this);
		if (bIsTablet) {
			// Utils.setIsTabletFlag(SplashScreen.this, true);
			// Utils.bIsTablet = true;
			Log.e("Splashscreen", "Device is Tablet...");
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			Log.e("Splashscreen", "Device is Phone...");
			// Utils.bIsTablet = false;
			// Constant.setIsTabletFlag(SplashScreen.this, false);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		setContentView(R.layout.activity_splash);
		mContext = this;
		LLDCApplication.init(getApplicationContext());

		// your coords of course
		LLDCApplication.parkCenter.setLatitude(18.93497658);
		LLDCApplication.parkCenter.setLongitude(72.83402252);

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		Log.e(TAG, "Screen size: " + size.x + "*" + size.y);

		LLDCApplication.screenWidth = size.x;
		LLDCApplication.screenHeight = size.y;

		LLDCApplication.mBaseDir = Environment.getExternalStorageDirectory()
				+ File.separator + "Android/data/"
				+ SplashScreen.this.getPackageName() + "/";

		// onInitiateApplication();
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				Log.e(TAG, "handler msg::> " + msg.what);

				if (msg.what == 1002) {
					// if (isServerStarted) {
					if (dialog != null) {
						dialog.setMessage("Loading...");
						dialog.show();
					}

				} else if (msg.what == 1005) {

					// Check if file is already downlaoded
					if (LLDCApplication.getMapTileFileName(
							getApplicationContext()).equals("")
							|| !LLDCApplication
									.getMapTileFileName(getApplicationContext())
									.toString()
									.trim()
									.equals(LLDCApplication
											.getMapTileNameFromCMS(
													getApplicationContext())
											.toString().trim())) {
						LLDCApplication.setMapTileFileName(
								getApplicationContext(), "");
						if (LLDCApplication
								.isConnectingToInternet(getApplicationContext())) {
							if (dialog != null && !dialog.isShowing()) {
								dialog.setMessage("Loading...");
								dialog.show();
							}
							DownloadZipFile();
						} else {
							LLDCApplication
									.onShowToastMesssage(
											getApplicationContext(),
											"Please check your internet connection and try again...");
						}

					} else {
						if (dialog != null && dialog.isShowing()) {
							dialog.dismiss();
						}
						LLDCApplication.mapTileFileName = LLDCApplication
								.getMapTileFileName(getApplicationContext());
						Intent msgIntent = null;
						if (bIsTablet) {
							msgIntent = new Intent(SplashScreen.this,
									HomeActivityTablet.class);
						} else {
							msgIntent = new Intent(SplashScreen.this,
									HomeActivity.class);
						}
						startActivity(msgIntent);
						finish();
					}
				} else if (msg.what == 1006) {
					onExtractIntitalData();
				} else if (msg.what == 1007) {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
					LLDCApplication
							.onShowToastMesssage(getApplicationContext(),
									"Download failed. Please check your internet connection and try again...");
				} else if (msg.what == 1008) {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
					LLDCApplication.onShowToastMesssage(
							getApplicationContext(),
							"Please connect to park Wi-Fi first...");

					Intent intent = new Intent(mContext, ParkWifiActivity.class);
					startActivityForResult(intent, 2);

				}

			};
		};

		dialog = new ProgressDialog(SplashScreen.this);
		dialog.setCanceledOnTouchOutside(false);

		// if (LLDCApplication.isConnectingToInternet(getApplicationContext())
		// && !Constants.bIsParkWifiConnected) {
		// onCheckWiFiConnection();
		// } else {
		callIfWifiConnected();
		// }

	}

	private void callIfWifiConnected() {
		if (LLDCApplication.isConnectingToInternet(getApplicationContext())) {
			onInitiateApplication();
		} else {
			LLDCApplication.onShowToastMesssage(getApplicationContext(),
					"Please check your internet connection...");
			onCheckDataPresent();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// check if the request code is same as what is passed here it is 2
		if (requestCode == 2) {

			Log.e(TAG, TAG + "check wifi again");
			onCheckWiFiConnection();
			// String message=data.getStringExtra("MESSAGE");
			// textView1.setText(message);
		}
	}

	private void onCheckWiFiConnection() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
			WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			if (wifiManager != null) {
				WifiInfo info = wifiManager.getConnectionInfo();
				if (info != null) {
					String szWifiName = info.getSSID();
					Log.e(TAG, "WiFiType::Name> " + szWifiName);
					dialog = new ProgressDialog(SplashScreen.this);
					dialog.setCanceledOnTouchOutside(false);
					dialog.setMessage("Loading...");
					dialog.show();

					WiFiTask wiFiTask = new WiFiTask();
					wiFiTask.currentWiFiName = szWifiName;
					// wiFiTask.dialog = dialog;
					wiFiTask.execute();
				}
			}
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

	private void DownloadZipFile() {
		// String[] url = {""};
		DownloadFileAsync tempDownlaod = (DownloadFileAsync) new DownloadFileAsync(
				SplashScreen.this,
				LLDCApplication.getMapTileNameFromCMS(getApplicationContext()),
				LLDCApplication.getMapTileURLFromCMS(getApplicationContext()));
		tempDownlaod.execute();
	}

	private void onExtractIntitalData() {
		new ExtractAsyntsk(SplashScreen.this,
				LLDCApplication.getMapTileNameFromCMS(getApplicationContext()))
				.execute();
	}

	class WiFiTask extends AsyncTask<String, Void, String> {
		List<String> wifiList = new ArrayList<String>();
		// ProgressDialog dialog;
		String currentWiFiName = "", szResponse = "";

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			// dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			wifiList.add("PA-Mobile2");

			WiFiModel wifiModel = new WiFiModel(mContext);
			szResponse = wifiModel.getWiFiList(mContext);

			/* Response */
			// [{"wiFissid":"PA-VOIP"}]
			try {
				JSONArray jsonArray = new JSONArray(szResponse);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonobject = (JSONObject) jsonArray.get(i);

					Iterator<String> keys = jsonobject.keys();
					while (keys.hasNext()) {
						String key = keys.next();
						String value = jsonobject.getString(key);
						wifiList.add(value);
					}
				}
				return "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return e.getMessage();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// if (dialog != null)
			// dialog.dismiss();

			if (result.equals("success")) {
				for (int i = 0; i < wifiList.size(); i++) {
					if (currentWiFiName.equals(wifiList.get(i))) {
						Constants.bIsParkWifiConnected = true;
						break;
					}
				}
				System.out.println("Constants.bIsParkWifiConnected: "
						+ Constants.bIsParkWifiConnected);
				if (!Constants.bIsParkWifiConnected) {
					mHandler.sendEmptyMessageDelayed(1008,
							LLDCApplication.SPLASH_TIME_OUT);
				} else {
					callIfWifiConnected();
				}

			} else {
				LLDCApplication.onShowToastMesssage(getApplicationContext(),
						result);
			}

		}
	}

	public static void finishActivity() {
		if (mContext != null)
			((Activity) mContext).finish();
	}
}
