package com.example.cabapppassenger.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.adapter.CountryCodeAdapter;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.GetCountryCodeTask;
import com.example.cabapppassenger.task.ResendCodeTask;
import com.example.cabapppassenger.task.SignupTask;
import com.example.cabapppassenger.util.Constant;
import com.urbanairship.push.PushManager;

public class MobileNoActivity extends Activity {

	static Context mContext;
	EditText et_mobileno;
	TextView btn_sendcode, tv_tandctext;
	Spinner spinner_countrycode;
	Handler mHandler;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	Editor editor;
	ImageView ivBack;
	ProgressDialog mDialog;
	IntentFilter apidUpdateFilter;
	String firstname, lastname, email, city, password, country;
	boolean flag_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mobileno);
		mContext = this;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		spinner_countrycode = (Spinner) findViewById(R.id.spinner_countrycode);
		tv_tandctext = (TextView) findViewById(R.id.tv_tandctext);
		String str_bottomtxt = "<font color=#ffffff>By continuing you accept the Cab:App's</font> <font color=#f36a02><u>Terms and conditions</u></font>";
		tv_tandctext.setText(Html.fromHtml(String.format(str_bottomtxt, 1)));
		tv_tandctext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent terms = new Intent(MobileNoActivity.this,
						TermsActivity.class);
				startActivity(terms);
			}
		});
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 2 && Constant.arr_dialingcode.size() > 0)
					spinner_countrycode.setAdapter(new CountryCodeAdapter(
							MobileNoActivity.this, R.layout.custom_spinner,
							Constant.arr_dialingcode));
			}
		};
		if (Constant.arr_dialingcode != null
				& Constant.arr_dialingcode.size() > 0)
			spinner_countrycode.setAdapter(new CountryCodeAdapter(
					MobileNoActivity.this, R.layout.custom_spinner,
					Constant.arr_dialingcode));
		else {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				GetCountryCodeTask code = new GetCountryCodeTask(mContext);
				code.fromlanding_flag = false;
				code.mhandler = mHandler;
				code.execute("");
			} else {
				Toast.makeText(mContext, "No network connection",
						Toast.LENGTH_SHORT).show();
			}
		}
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				spinner_countrycode.setAdapter(null);
				Constant.hideKeybord(v, mContext);

			}
		});
		Intent mobile = getIntent();
		if (mobile != null) {
			firstname = mobile.getStringExtra("FirstName");
			lastname = mobile.getStringExtra("LastName");
			city = mobile.getStringExtra("City");
			country = mobile.getStringExtra("Country");
			email = mobile.getStringExtra("Email");
			password = mobile.getStringExtra("Password");
			flag_login = mobile.getBooleanExtra("From_Login", false);// flag
																		// when
			// user has
			// not
			// verified
			// his
			// mobile
			// number
			// and log's
			// in
			// directly.

		}
		// apidUpdateFilter = new IntentFilter();
		// apidUpdateFilter.addAction(PushManager.ACTION_REGISTRATION_FINISHED);

		et_mobileno = (EditText) findViewById(R.id.et_mobileno);

		btn_sendcode = (TextView) findViewById(R.id.tvsendcode);
		btn_sendcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// LocalBroadcastManager locationBroadcastManager =
				// LocalBroadcastManager
				// .getInstance(mContext);
				// locationBroadcastManager.registerReceiver(apidUpdateReceiver,
				// apidUpdateFilter);
				if (et_mobileno.getText().toString() != null
						&& et_mobileno.length() >= 10
						&& et_mobileno.length() <= 11) {
					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
						if (flag_login) {
							ResendCodeTask task = new ResendCodeTask(mContext);
							task.clientkey = shared_pref.getString("ClientKey",
									"");
							task.from_login = true;
							task.international_code = spinner_countrycode
									.getSelectedItem().toString();
							task.mobile_no = et_mobileno.getText().toString();
							task.execute(Constant.passengerURL
									+ "ws/v2/passenger/resendVerificationPin/");
							return;
						}
						// SignupTask task = new SignupTask(mContext);
						// task.firstname = firstname;
						// task.lastname = lastname;
						// task.city = city;
						// task.email = email;
						// task.password = password;
						// task.country = country;
						// task.mobile_no = et_mobileno.getText().toString();
						// task.international_code = spinner_countrycode
						// .getSelectedItem().toString();
						// task.password = password;
						// task.execute(Constant.passengerURL
						// + "ws/v2/passenger/account/register");
						// http://192.168.0.11:8081/ws/v2/passenger/account/register
						String apid = PushManager.shared().getAPID();
						Log.i("****APPID*****", "" + apid);

						// if (apid != null && !apid.equals("")) {
						signup();

						// }
						// else {
						// mDialog = showProgressDialog("Signing in");
						// mDialog.show();
						// Constant.togglepush();
						// }
					} else {
						if (mDialog != null)
							mDialog.dismiss();
						Toast.makeText(MobileNoActivity.this,
								"No network connection", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					Constant.alertdialog("invalidmobile", mContext);
				}
			}
		});

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		spinner_countrycode.setAdapter(null);
	}

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	void signup() {
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			// LocalBroadcastManager locationBroadcastManager =
			// LocalBroadcastManager
			// .getInstance(mContext);
			// locationBroadcastManager.unregisterReceiver(apidUpdateReceiver);
			SignupTask task = new SignupTask(mContext, mDialog, false);
			task.firstname = firstname;
			task.lastname = lastname;
			task.city = city;
			task.email = email;
			task.password = password;
			task.country = country;
			task.flag_keepmeloged = true;
			task.mobile_no = et_mobileno.getText().toString();
			task.international_code = spinner_countrycode.getSelectedItem()
					.toString();
			task.execute(Constant.passengerURL
					+ "ws/v2/passenger/account/register");
		} else {
			if (mDialog != null)
				mDialog.dismiss();
			Toast.makeText(MobileNoActivity.this, "No network connection",
					Toast.LENGTH_SHORT).show();
			// PushManager.disablePush();
		}
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
	// LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager
	// .getInstance(MobileNoActivity.this);
	// locationBroadcastManager
	// .unregisterReceiver(apidUpdateReceiver);
	// SignupTask task = new SignupTask(mContext, mDialog, false);
	// task.firstname = firstname;
	// task.lastname = lastname;
	// task.city = city;
	// task.email = email;
	// task.password = password;
	// task.country = country;
	// task.mobile_no = et_mobileno.getText().toString();
	// task.international_code = spinner_countrycode
	// .getSelectedItem().toString();
	// task.password = password;
	// task.flag_keepmeloged = false;
	// task.execute(Constant.passengerURL
	// + "ws/v2/passenger/account/register");
	//
	// } else {
	// if (mDialog != null)
	// mDialog.dismiss();
	// Toast.makeText(MobileNoActivity.this,
	// "No network connection.", Toast.LENGTH_SHORT)
	// .show();
	// PushManager.disablePush();
	// }
	//
	// } else {
	// if (mDialog != null)
	// mDialog.dismiss();
	// Toast.makeText(MobileNoActivity.this, "No network connection.",
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

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(MobileNoActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}
}
