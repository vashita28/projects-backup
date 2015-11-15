package com.example.cabapppassenger.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.LoginTask;
import com.example.cabapppassenger.util.Constant;
import com.urbanairship.push.PushManager;

public class LoginActivity extends Activity {

	static Context mContext;
	EditText et_email;
	public static EditText et_password;
	ProgressDialog mDialog;
	String emailid;
	View activityRootView;
	TextView btn_login, btn_signup, tv_forgotpassword, tv_newcab_bottom,
			tvSignUp_bottom, tv_newcab;
	IntentFilter apidUpdateFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		mContext = this;
		Intent email = getIntent();
		if (email.hasExtra("Email"))
			emailid = email.getStringExtra("Email");
		et_email = (EditText) findViewById(R.id.et_email);
		et_password = (EditText) findViewById(R.id.et_password);

		if (emailid != null) {
			et_email.setText(emailid);
			et_email.setFocusable(false);
			// et_password.setFocusable(false);
		}
		tv_forgotpassword = (TextView) findViewById(R.id.tv_forgorpassword);
		tv_newcab_bottom = (TextView) findViewById(R.id.tv_newcab_bottom);
		tvSignUp_bottom = (TextView) findViewById(R.id.tvSignUp_bottom);
		tv_newcab = (TextView) findViewById(R.id.tv_newcab);

		btn_login = (TextView) findViewById(R.id.tvlogin);
		btn_signup = (TextView) findViewById(R.id.tvSignUp);
		tvSignUp_bottom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent signup = new Intent(mContext, SignupActivity.class);
				startActivity(signup);
			}
		});
		activityRootView = findViewById(R.id.rel_main);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						// r will be populated with the coordinates of your view
						// that area still visible.
						activityRootView.getWindowVisibleDisplayFrame(r);

						int heightDiff = activityRootView.getRootView()
								.getHeight() - (r.bottom - r.top);
						if (heightDiff > 100) { // if more than 100 pixels, its
							// Log.e("Visible", "Visible");
							tv_newcab_bottom.setVisibility(View.GONE);
							tvSignUp_bottom.setVisibility(View.GONE);
							btn_signup.setVisibility(View.VISIBLE);
							tv_newcab.setVisibility(View.VISIBLE);
						} else {
							// Log.e("Not visible", "not visible");
							tv_newcab_bottom.setVisibility(View.VISIBLE);
							tvSignUp_bottom.setVisibility(View.VISIBLE);
							btn_signup.setVisibility(View.GONE);
							tv_newcab.setVisibility(View.GONE);
						}
					}
				});
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// LocalBroadcastManager locationBroadcastManager =
				// LocalBroadcastManager
				// .getInstance(mContext);
				// locationBroadcastManager.registerReceiver(apidUpdateReceiver,
				// apidUpdateFilter);
				if (et_email.length() > 0 && et_password.length() > 0) {
					if (et_email.getText().toString() != null
							&& Constant.isEmailAddressValid(et_email.getText()
									.toString())) {
						if (et_password.getText().toString() != null
								&& et_password.length() >= 6) {
							if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
								// logintask
								// LoginTask task = new LoginTask(mContext);
								// task.email = et_email.getText().toString();
								// task.password = Constant.md5(et_password
								// .getText().toString());
								// task.execute(Constant.passengerURL
								hideKeybord(v);
								String apid = PushManager.shared().getAPID();
								Log.i("****APPID*****", "" + apid);

								// if (apid != null && !apid.equals("")) {
								login();
								hideKeybord(v);
								// }
								// else {
								// mDialog = showProgressDialog("Logging in");
								// if (mDialog != null)
								// mDialog.show();
								// Constant.togglepush();
								// }
							} else {
								if (mDialog != null)
									mDialog.dismiss();
								Toast.makeText(LoginActivity.this,
										"No network connection",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							// wrong pass
							Constant.alertdialog("passwordlessthan", mContext);
						}
					} else {
						// wrong email
						Constant.alertdialog("invalidemail", mContext);
					}
				} else {
					// emptyfields
					Constant.alertdialog("emptyfields", mContext);
				}

			}
		});
		tv_forgotpassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent forgotpass = new Intent(mContext,
						ForgotPasswordActivity.class);
				startActivity(forgotpass);
			}
		});
		// apidUpdateFilter = new IntentFilter();
		// apidUpdateFilter.addAction(PushManager.ACTION_REGISTRATION_FINISHED);
	}

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	void login() {
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			// LocalBroadcastManager locationBroadcastManager =
			// LocalBroadcastManager
			// .getInstance(mContext);
			// locationBroadcastManager.unregisterReceiver(apidUpdateReceiver);
			LoginTask task = new LoginTask(mContext, mDialog);
			task.email = et_email.getText().toString();
			task.password = Constant.md5(et_password.getText().toString());
			task.flag_keepmeloged = true;
			task.execute(Constant.passengerURL + "ws/v2/passenger/login");

		} else {
			if (mDialog != null)
				mDialog.dismiss();
			Toast.makeText(LoginActivity.this, "No network connection",
					Toast.LENGTH_SHORT).show();
			// PushManager.disablePush();
		}
	}

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}

	// private BroadcastReceiver apidUpdateReceiver = new BroadcastReceiver() {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// // check APID and proceed with login
	// Log.e("LoginActivity", "onReceive:: ");
	// String APID = PushManager.shared().getAPID();
	// if (APID != null && !APID.equals("")) {
	//
	// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
	//
	// LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager
	// .getInstance(LoginActivity.this);
	// locationBroadcastManager
	// .unregisterReceiver(apidUpdateReceiver);
	// LoginTask task = new LoginTask(mContext, mDialog);
	// task.email = et_email.getText().toString();
	// task.password = Constant.md5(et_password.getText()
	// .toString());
	// task.flag_keepmeloged = false;
	// task.execute(Constant.passengerURL
	// + "ws/v2/passenger/login");
	//
	// } else {
	// if (mDialog != null)
	// mDialog.dismiss();
	// Toast.makeText(LoginActivity.this,
	// "No network connection.", Toast.LENGTH_SHORT)
	// .show();
	// PushManager.disablePush();
	// }
	//
	// } else {
	// if (mDialog != null)
	// mDialog.dismiss();
	// Toast.makeText(LoginActivity.this, "No network connection.",
	// Toast.LENGTH_SHORT).show();
	// PushManager.disablePush();
	// }
	// }
	// };

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// LocalBroadcastManager locationBroadcastManager =
		// LocalBroadcastManager
		// .getInstance(this);
		// locationBroadcastManager.unregisterReceiver(apidUpdateReceiver);
	}

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}
}
