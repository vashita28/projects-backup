package co.uk.pocketapp.gmd.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

import com.crashlytics.android.Crashlytics;

public class SplashScreen extends Activity {

	protected int _splashTime = 3000; // 3 seconds wait
	private Thread splashTread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		setContentView(R.layout.splashscreen);

		if (!AppValues.getReportXMLFile().exists()) {
			Util.setIsReportXMLDownloaded(SplashScreen.this, false);
			Util.setReportID(SplashScreen.this, "");
			Util.setIsReportUploaded(SplashScreen.this, false);

			getContentResolver().delete(DataProvider.Material_Log.CONTENT_URI,
					null, null);
			getContentResolver().delete(DataProvider.Mill.CONTENT_URI, null,
					null);
			getContentResolver().delete(DataProvider.Services.CONTENT_URI,
					null, null);
			getContentResolver().delete(DataProvider.Tasks.CONTENT_URI, null,
					null);
			getContentResolver().delete(DataProvider.Child_Leaf.CONTENT_URI,
					null, null);
		}

		if (AppValues.getDirectory() != null) {

			// if (!Util.getIsServiceCheclistValuesInserted(SplashScreen.this))
			// {
			// Fill_Service_CheckList_Values_Task task = new
			// Fill_Service_CheckList_Values_Task();
			// task.mContext = SplashScreen.this;
			// task.execute();
			// }

			splashTread = new Thread() {

				@Override
				public void run() {

					try {
						synchronized (this) {
							wait(_splashTime);
						}

					} catch (InterruptedException e) {
					}

					finally {
						Intent intent = new Intent();
						if (!Util.getLoggedIn(SplashScreen.this)) {
							intent.setClass(SplashScreen.this,
									Activity_login.class);
						} else {
							intent.setClass(SplashScreen.this,
									MainActivity.class);
						}
						startActivity(intent);
						finish();
					}
				}
			};
			splashTread.start();

		} else {

			AlertDialog m_AlertDialog = new AlertDialog.Builder(
					SplashScreen.this)
					.setTitle("Warning")
					.setMessage("SD card is missing")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
									int pid = android.os.Process.myPid();
									android.os.Process.killProcess(pid);
								}
							}).create();
			m_AlertDialog.setCanceledOnTouchOutside(false);
			m_AlertDialog.show();
		}

		System.gc();
	}

	// Function that will handle the touch
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			synchronized (splashTread) {
				splashTread.notifyAll();
			}
		}
		return true;
	}

}
