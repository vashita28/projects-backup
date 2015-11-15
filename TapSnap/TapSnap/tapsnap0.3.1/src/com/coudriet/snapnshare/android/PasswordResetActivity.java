package com.coudriet.snapnshare.android;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;
import com.snapshare.utility.AppStatus;
import com.snapshare.utility.Common;

public class PasswordResetActivity extends Activity {

	protected EditText mEmail;
	protected Button mSubmitTextView;
	protected TextView mEnterEmailTextView;
	private ActionBar actionBar;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_password_reset);
		// setProgressBarIndeterminateVisibility(false);

		// emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

		// Set up the action bar.
		actionBar = getActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.logo));

		mEnterEmailTextView = (TextView) findViewById(R.id.enterEmailTextView);
		mEnterEmailTextView
				.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);

		mEmail = (EditText) findViewById(R.id.emailField);
		mEmail.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		// mEmail.setHintTextColor(getResources().getColor(
		// R.color.edittext_hintcolor));
		mSubmitTextView = (Button) findViewById(R.id.resetButton);
		mSubmitTextView
				.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);

		mSubmitTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				progressDialog = new ProgressDialog(PasswordResetActivity.this);
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(false);
				progressDialog.show();

				PasswordResetTask passwordResetTask = new PasswordResetTask(
						progressDialog);
				passwordResetTask.execute();

			}
		});

	}

	private class PasswordResetTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;

		public PasswordResetTask(ProgressDialog dialog) {
			// TODO Auto-generated constructor stub
			this.dialog = dialog;
		}

		protected void onPreExecute() {
		}

		protected void onPostExecute(Void Result) {
			String email = mEmail.getText().toString();

			email = email.trim();

			if (email.isEmpty()) {
				if (progressDialog != null)
					progressDialog.dismiss();

				String msg = getString(R.string.password_reset_error_message);
				String tit = getString(R.string.signup_error_title);

				Common.callDialog(PasswordResetActivity.this, msg, tit);

				// AlertDialog.Builder builder = new AlertDialog.Builder(
				// PasswordResetActivity.this);
				// builder.setMessage(R.string.password_reset_error_message)
				// .setTitle(R.string.signup_error_title)
				// .setPositiveButton(android.R.string.ok, null);
				// AlertDialog dialog = builder.create();
				// dialog.show();
			} else {

				if (AppStatus.getInstance(PasswordResetActivity.this)
						.isOnline()) {

					if (!email.isEmpty()) {

						// create the new user!
						// setProgressBarIndeterminateVisibility(true);

						ParseUser.requestPasswordResetInBackground(email,
								new RequestPasswordResetCallback() {
									public void done(ParseException e) {

										if (e == null) {
											// Success!

											mEmail.setText("");

											hideSoftKeyboard(PasswordResetActivity.this);

											String requestMessage = "Please check your email for instructions on reseting your password.";

											AlertDialog.Builder builder = new AlertDialog.Builder(
													PasswordResetActivity.this);
											builder.setMessage(requestMessage)
													.setTitle("Email Sent!")
													.setPositiveButton(
															android.R.string.ok,
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int id) {

																	final Handler handler = new Handler();
																	handler.postDelayed(
																			new Runnable() {
																				@Override
																				public void run() {

																					Intent intent = new Intent(
																							PasswordResetActivity.this,
																							ActivityLogIn.class);
																					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
																					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
																					startActivity(intent);
																				}

																			},
																			1000);
																}
															});

											AlertDialog dialog1 = builder
													.create();
											dialog1.show();
										} else {

											if (progressDialog != null)
												progressDialog.dismiss();

											String msg = getString(R.string.valid_email);
											String tit = getString(R.string.signup_error_title);

											Common.callDialog(
													PasswordResetActivity.this,
													msg, tit);

											// AlertDialog.Builder builder =
											// new AlertDialog.Builder(
											// PasswordResetActivity.this);
											//
											// builder.setMessage(
											// R.string.valid_email)
											// .setTitle(
											// R.string.signup_error_title)
											// .setPositiveButton(
											// android.R.string.ok,
											// null);
											// AlertDialog dialog = builder
											// .create();
											// dialog.show();
										}
									}
								});

					} else {
						if (progressDialog != null)
							progressDialog.dismiss();

						String msg = getString(R.string.signup_error_message_valid_email);
						String tit = getString(R.string.signup_error_title);

						Common.callDialog(PasswordResetActivity.this, msg, tit);

					}

				} else {
					if (progressDialog != null)
						progressDialog.dismiss();

					String msg = getString(R.string.connectivity_error);
					String tit = getString(R.string.signup_error_title);

					Common.callDialog(PasswordResetActivity.this, msg, tit);

					/*
					 * Toast.makeText(PasswordResetActivity.this,
					 * R.string.connectivity_error, Toast.LENGTH_SHORT) .show();
					 */
				}
			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			return null;
		}
	}

	public static void hideSoftKeyboard(Activity activity) {

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}
}
