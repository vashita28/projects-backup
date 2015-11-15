package com.hoteltrip.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoteltrip.android.receivers.NetworkBroadcastReceiver;
import com.hoteltrip.android.tasks.LoginTask;
import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.Const;
import com.hoteltrip.android.util.Utils;

public class MembersLoginActivity extends Activity {

	EditText email_et, password_et;
	ImageButton login_imgbtn;

	Handler mHandler;
	ProgressDialog mDialog;

	RelativeLayout rel_login;
	TextView loggedIn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_memberslogin);

		rel_login = (RelativeLayout) findViewById(R.id.rel_login);
		loggedIn = (TextView) findViewById(R.id.tv_loggedin_message);

		email_et = (EditText) findViewById(R.id.email_et);
		password_et = (EditText) findViewById(R.id.password_et);
		email_et.setTypeface(Utils.getHelveticaNeue(MembersLoginActivity.this));
		password_et.setTypeface(Utils
				.getHelveticaNeue(MembersLoginActivity.this));
		login_imgbtn = (ImageButton) findViewById(R.id.login_imgbtn);
		login_imgbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

				if (email_et.length() == 0 || password_et.length() == 0) {
					emptyfieldsalertdialog();
				} else {
					if (isEmailAddressValid(email_et.getText().toString())) {
						// Login
						if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
							mDialog = showProgressDialog();
							LoginTask task = new LoginTask(
									MembersLoginActivity.this, mHandler);
							task.szEmail = email_et.getText().toString();
							task.szPassword = password_et.getText().toString();
							task.execute();
						} else
							Toast.makeText(MembersLoginActivity.this,
									"No network connection", Toast.LENGTH_LONG)
									.show();
					} else {
						validemailalertdialog();
					}
				}
			}
		});

		mHandler = new Handler() {

			@Override
			public void handleMessage(android.os.Message msg) {

				if (mDialog != null) {
					mDialog.dismiss();
				}

				if (msg.what == Const.LOGIN_SUCCESS) { // login success
					rel_login.setVisibility(View.GONE);
					loggedIn.setVisibility(View.VISIBLE);
					Bundle bundle = (Bundle) msg.obj;
					AppValues.szLoggedInEmail = bundle.getString("email");
					// AppValues.szName = bundle.getString("name");
					loggedIn.setText("Welcome " + bundle.getString("name")
							+ "!");
				} else if (msg.what == Const.LOGIN_FAILED) { // login failed
					Bundle bundle = (Bundle) msg.obj;
					String szError = bundle.getString("error");
					showAlertDialog(szError);
					rel_login.setVisibility(View.VISIBLE);
					loggedIn.setVisibility(View.GONE);
				}
			};
		};

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!AppValues.szName.equals("")) {
			rel_login.setVisibility(View.GONE);
			loggedIn.setVisibility(View.VISIBLE);
			loggedIn.setText("Welcome " + AppValues.szName + "!");
		} else {
			rel_login.setVisibility(View.VISIBLE);
			loggedIn.setVisibility(View.GONE);
		}
	}

	private void emptyfieldsalertdialog() {
		// Cancellation Alert Dialog
		TextView title = new TextView(this);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setTypeface(Utils.getHelveticaNeue(MembersLoginActivity.this));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCustomTitle(title);

		builder.setMessage("Please Enter All the Details");

		builder.setCancelable(false);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}

		});

		AlertDialog alert = builder.show();
		TextView messageText = (TextView) alert
				.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER_VERTICAL);
		messageText.setTextColor(Color.BLACK);
		messageText.setTextSize(16);

	}

	private void validemailalertdialog() {
		// Cancellation Alert Dialog
		TextView title = new TextView(this);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setTypeface(Utils.getHelveticaNeue(MembersLoginActivity.this));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCustomTitle(title);

		builder.setMessage("Please Enter Valid Email");

		builder.setCancelable(false);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}

		});

		AlertDialog alert = builder.show();
		TextView messageText = (TextView) alert
				.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER_VERTICAL);
		messageText.setTextColor(Color.BLACK);
		messageText.setTextSize(16);

	}

	public boolean isEmailAddressValid(String email) {
		boolean isEmailValid = false;

		String strExpression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern objPattern = Pattern.compile(strExpression,
				Pattern.CASE_INSENSITIVE);
		Matcher objMatcher = objPattern.matcher(inputStr);
		if (objMatcher.matches()) {
			isEmailValid = true;
		}
		return isEmailValid;
	}

	ProgressDialog showProgressDialog() {
		ProgressDialog dialog = new ProgressDialog(MembersLoginActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage("Logging in...");
		dialog.show();
		return dialog;
	}

	void showAlertDialog(String alertMessage) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				MembersLoginActivity.this);
		alertDialog.setTitle("Login error!");
		alertDialog.setMessage(alertMessage);
		alertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}

				});

		alertDialog.show();
	}

}
