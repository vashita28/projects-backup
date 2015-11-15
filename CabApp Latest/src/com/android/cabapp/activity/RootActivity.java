package com.android.cabapp.activity;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.urbanairship.UAirship;

public class RootActivity extends CabAppParentActivity {

	private static final String TAG = RootActivity.class.getSimpleName();

	ImageView ivBack;
	MyApplication myApplication;

	RelativeLayout relTextBadgeFilter;
	TextView textBadgeFilter, textCashBack;

	static AlertDialog jobsAlertDialog;
	public static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		// Override how this activity is animated into view
		// The new activity is pulled in from the left and the current activity
		// is kept still
		// This has to be called before onCreate
		overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
		// setContentView(R.layout.common_top_bar_maps);
		if (Constants.isDebug)
			Log.d("RootActivity", "Root-onCreate()");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (Constants.isDebug)
			Log.d("RootActivity", "Root-onStop()");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (Constants.isDebug)
			Log.d("RootActivity", "Root-onDestroy()");
	}

	protected void initWidgets() {
		Util.bIsNowOrPreebookFragment = false;
		myApplication = (MyApplication) getApplication();
		ivBack = (ImageView) findViewById(R.id.ivBack);
		textBadgeFilter = (TextView) findViewById(R.id.textBadgeFilter);
		relTextBadgeFilter = (RelativeLayout) findViewById(R.id.relTextBadgeFilter);
		textCashBack = (TextView) findViewById(R.id.tvCashBack);
		setCashBackText();

		updateJobsCountBadge(AppValues.nJobsCount);

		if (ivBack != null)
			ivBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mContext != null)
						Util.hideSoftKeyBoard(mContext, ivBack);
					finish();
				}
			});
	}

	void setCashBackText() {

		float fCashBack = 0;
		if (AppValues.driverSettings != null) {
			if (AppValues.driverSettings.getCashback() != null) {

				fCashBack = Float.valueOf(AppValues.driverSettings.getCashback());
				textCashBack.setText(AppValues.driverSettings.getCurrencySymbol() + " "
						+ String.format(Locale.ENGLISH, "%.2f", fCashBack));
			}
		} else {
			textCashBack.setText("-");
		}
	}

	void updateJobsCountBadge(int jobsCount) {
		// if (relTextBadgeFilter != null)
		// if (jobsCount > 0) {
		// textBadgeFilter.setVisibility(View.VISIBLE);
		// textBadgeFilter.setText(String.valueOf(jobsCount));
		// relTextBadgeFilter.setVisibility(View.VISIBLE);
		// } else {
		// relTextBadgeFilter.setVisibility(View.GONE);
		// }
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

	public static void showJobsAlertDialog() {
		Log.e("RootActivity", "showJobsAlertDialog");

		if (jobsAlertDialog != null) {
			jobsAlertDialog.dismiss();
		}

		try {
			AlertDialog.Builder jobsAlertDialogBuilder = new AlertDialog.Builder(mContext);
			jobsAlertDialogBuilder.setTitle("Jobs").setMessage("New job available")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface argDialog, int argWhich) {
							((Activity) mContext).finish();
							AppValues.clearAllJobsList();
							Intent launch = new Intent(Intent.ACTION_MAIN);
							launch.setClass(mContext, MainActivity.class);
							launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							launch.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							UAirship.shared().getApplicationContext().startActivity(launch);
						}
					}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface argDialog, int argWhich) {
						}
					});
			jobsAlertDialog = jobsAlertDialogBuilder.create();
			jobsAlertDialog.setCancelable(false);
			jobsAlertDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Whenever this activity is paused (i.e. looses focus because another
		// activity is started etc)
		// Override how this activity is animated out of view
		// The new activity is kept still and this activity is pushed out to the
		// left
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
	}

}
