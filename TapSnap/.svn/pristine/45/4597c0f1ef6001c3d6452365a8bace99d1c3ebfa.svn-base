package com.coudriet.snapnshare.android;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.snapshare.utility.AppStatus;
import com.snapshare.utility.Common;

public class ActivityLogIn extends Activity {

	protected EditText mUsername;
	protected EditText mPassword;
	// protected TextView mLoginTextView;
	// protected TextView mPasswordResetTextView;
	// protected TextView mSignUpTextView;
	// private Button btnfb;

	protected RelativeLayout registerNowRL;
	protected Button mLoginButton;
	protected Button mPasswordResetButton;
	protected TextView mRegisterNowTextView;

	private ProgressDialog pDialog;

	private String username, password;

	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_login);

		setProgressBarIndeterminateVisibility(false);
		actionBar = getActionBar();
		actionBar.hide();

		mPasswordResetButton = (Button) findViewById(R.id.resetBtn);
		mPasswordResetButton
				.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		mPasswordResetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityLogIn.this,
						PasswordResetActivity.class);
				startActivity(intent);
			}
		});

		registerNowRL = (RelativeLayout) findViewById(R.id.rlRegisterNow);
		mRegisterNowTextView = (TextView) findViewById(R.id.registerNowTextView);
		mRegisterNowTextView
				.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		// mSignUpTextView = (TextView)findViewById(R.id.signUpTextView);

		registerNowRL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ActivityLogIn.this,
						SignUpActivity.class);
				startActivity(intent);
			}
		});

		mUsername = (EditText) findViewById(R.id.usernameField);
		mPassword = (EditText) findViewById(R.id.passwordField);
		mUsername.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		mPassword.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		// mUsername.setHintTextColor(getResources().getColor(
		// R.color.edittext_hintcolor));
		// mPassword.setHintTextColor(getResources().getColor(
		// R.color.edittext_hintcolor));

		mLoginButton = (Button) findViewById(R.id.loginBtn);
		mLoginButton
				.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		mLoginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LogInTask logInTask = new LogInTask();
				logInTask.execute();

			}
		});

	}

	private class LogInTask extends AsyncTask<Void, Void, Void> {

		protected void onPreExecute() {

			pDialog = new ProgressDialog(ActivityLogIn.this);
			pDialog.setMessage("Logging in...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected void onPostExecute(Void Result) {
			// if (dialog != null)
			// dialog.dismiss();

			username = mUsername.getText().toString();
			password = mPassword.getText().toString();

			username = username.trim();
			password = password.trim();

			if (username.isEmpty() && password.isEmpty()) {

				if (pDialog != null)
					pDialog.dismiss();

				String msg = getString(R.string.login_error_message);
				String tit = getString(R.string.login_error_title);

				Common.callDialog(ActivityLogIn.this, msg, tit);

			} else if (username.isEmpty()) {

				if (pDialog != null)
					pDialog.dismiss();

				String msg = getString(R.string.login_error_message_for_user);
				String tit = getString(R.string.login_error_title);

				Common.callDialog(ActivityLogIn.this, msg, tit);

			} else if (password.isEmpty()) {

				if (pDialog != null)
					pDialog.dismiss();

				String msg = getString(R.string.login_error_message_for_password);
				String tit = getString(R.string.login_error_title);

				Common.callDialog(ActivityLogIn.this, msg, tit);

			} else {

				if (AppStatus.getInstance(ActivityLogIn.this).isOnline()) {

					// Login
					setProgressBarIndeterminateVisibility(true);

					ParseUser.logInInBackground(username, password,
							new LogInCallback() {
								@Override
								public void done(ParseUser user,
										ParseException e) {

									setProgressBarIndeterminateVisibility(false);

									if (e == null) {
										// Success!
										Intent intent = new Intent(
												ActivityLogIn.this,
												MainActivity.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
										startActivity(intent);
									} else {
										/*
										 * AlertDialog.Builder builder = new
										 * AlertDialog
										 * .Builder(LoginActivity.this); builder
										 * .setMessage(R.string.user_not_found )
										 * .setTitle(R.string.login_error_title
										 * ) .setPositiveButton(android.R.string
										 * .ok, null); AlertDialog dialog =
										 * builder.create(); dialog.show();
										 */
										if (pDialog != null)
											pDialog.dismiss();

										AlertDialog quitAlertDialog = new AlertDialog.Builder(
												ActivityLogIn.this)
												.setTitle(R.string.error_title)
												.setMessage(
														R.string.user_not_found)
												.setPositiveButton(
														"Ok",
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface argDialog,
																	int argWhich) {
																mPassword
																		.setText("");
															}
														}).create();
										quitAlertDialog
												.setCanceledOnTouchOutside(false);
										quitAlertDialog.show();

									}
								}
							});

				} else {

					if (pDialog != null)
						pDialog.dismiss();

					String msg = getString(R.string.connectivity_error);
					String tit = getString(R.string.login_error_title);

					Common.callDialog(ActivityLogIn.this, msg, tit);
				}
			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			return null;
		}
	}

}
