package com.coudriet.tapsnap.android;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coudriet.tapsnap.utility.AppStatus;
import com.coudriet.tapsnap.utility.Common;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class ActivityLogIn extends Activity {



	protected EditText mUsername;
	protected EditText mPassword;
	// protected TextView mLoginTextView;
	// protected TextView mPasswordResetTextView;
	// protected TextView mSignUpTextView;
	// private Button btnfb;
	private ProgressDialog progressDialog;
	protected ParseUser mCurrentUser;
	public final static String MyPREFERENCES = "MyPrefs";
	protected RelativeLayout registerNowRL;
	protected Button mLoginButton, btnfb;
	protected Button mPasswordResetButton;
	protected TextView mRegisterNowTextView;
	private boolean isLoginViaFacebook = false;
	public final JSONObject userProfile = new JSONObject();
	public static SharedPreferences sharedpreferences;

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
		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
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
		btnfb = (Button) findViewById(R.id.btnFB);
		btnfb.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		btnfb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				isLoginViaFacebook = true;
				FacebookTask facebookTask = new FacebookTask();
				facebookTask.execute();
				// onLoginButtonClicked();

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
										if(e.getMessage()!=null)
										Log.d(getClass().getSimpleName(), e.getMessage());
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (isLoginViaFacebook) {

			ParseFacebookUtils.finishAuthentication(requestCode, resultCode,
					data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private String onLoginButtonClicked() {

		String accessToken="";
		List<String> permissions = Arrays.asList("public_profile",
				"user_about_me", "user_relationships", "user_birthday",
				"user_location", "email");

		Log.d(getClass().getSimpleName(), "current user" + ParseUser.getCurrentUser() + "");
		
		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {

			@Override
			public void done(ParseUser user, ParseException err) {
				Log.d(getClass().getSimpleName(), "current user" + ParseUser.getCurrentUser() + "");
			
				if (user == null) {
					Log.v(getClass().getSimpleName(), "User value: null");
					if (!AppStatus.getInstance(ActivityLogIn.this).isOnline()) {
						if (progressDialog != null)
							progressDialog.dismiss();

						String msg = getString(R.string.connectivity_error);
						String tit = getString(R.string.signup_error_title);

						Common.callDialog(ActivityLogIn.this, msg, tit);
					} else {
						if (progressDialog != null)
							progressDialog.dismiss();
						Toast.makeText(ActivityLogIn.this,
								"The user cancelled the Facebook login.",
								Toast.LENGTH_LONG).show();
					}
				} else if (user.isNew()) {
					// create the new user!
					Log.v(getClass().getSimpleName(), "User value: new user");
					Editor editor = sharedpreferences.edit();
					editor.putBoolean(ParseConstants.KEY_LOGIN_VIA_FACEBOOK,
							true);
					editor.commit();
					if (AppStatus.getInstance(ActivityLogIn.this).isOnline()) {

						setProgressBarIndeterminateVisibility(true);

						// Getting Facebook Data:
						Request request = Request.newMeRequest(
								ParseFacebookUtils.getSession(),
								new Request.GraphUserCallback() {
									@Override
									public void onCompleted(
											final GraphUser user,
											Response response) {
										if (user != null) {
											// Create a JSON object to hold the
											// profile info

											try {
												// Populate the JSON object
												userProfile.put("facebookId",
														user.getId());
												userProfile.put("name",
														user.getName());

												// Saving into Parse:
												mCurrentUser = ParseUser
														.getCurrentUser();

												if (mCurrentUser != null) {
													// Username
													mCurrentUser
															.setUsername(user
																	.getName());
													// Password
													mCurrentUser
															.setPassword("");
													// Email
													mCurrentUser.setEmail(user
															.asMap()
															.get("email")
															.toString());
													// Birthday
													mCurrentUser
															.put(ParseConstants.KEY_BIRTHDAY,
																	user.getBirthday());
													// Profile picture
													String profilePicture = "http://graph.facebook.com/"
															+ user.getId()
															+ "/picture?type=large";

													mCurrentUser
															.put(ParseConstants.KEY_FACEBOOK_PROFILE_PICTURE,
																	profilePicture);

													// Saving in background into
													// parse
													mCurrentUser
															.saveInBackground();

													Log.e("SignupActivity",
															"new user Login via fb: Success!");
												}

											} catch (JSONException e) {
												Log.d("ERROR ",
														"Error parsing returned user data.");
											}

										} else if (response.getError() != null) {
											// handle error
										}
									}

									
								  

								});
						request.executeAsync();
						

					} else {
						if (progressDialog != null)
							progressDialog.dismiss();

						String msg = getString(R.string.connectivity_error);
						String tit = getString(R.string.signup_error_title);

						Common.callDialog(ActivityLogIn.this, msg, tit);

					}

					Toast.makeText(ActivityLogIn.this,
							"User signed up and logged in through Facebook!",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(ActivityLogIn.this,
							MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
				} else {
					Log.e(getClass().getSimpleName(), "User value: old user");
					Editor editor = sharedpreferences.edit();
					editor.putBoolean(ParseConstants.KEY_LOGIN_VIA_FACEBOOK,
							true);
					editor.commit();
					if (AppStatus.getInstance(ActivityLogIn.this).isOnline()) {
						// Getting Facebook Data:
						Request request = Request.newMeRequest(
								ParseFacebookUtils.getSession(),
								new Request.GraphUserCallback() {
									@Override
									public void onCompleted(
											final GraphUser user,
											Response response) {
										if (user != null) {
											// Create a JSON object to hold the
											// profile info

											try {
												// Populate the JSON object
												userProfile.put("facebookId",
														user.getId());
												userProfile.put("name",
														user.getName());

												// Saving into Parse:
												mCurrentUser = ParseUser
														.getCurrentUser();

												if (mCurrentUser != null) {

													// Username
													mCurrentUser
															.setUsername(user
																	.getName());
													// Password
													mCurrentUser
															.setPassword("");
													// Email
													mCurrentUser.setEmail(user
															.asMap()
															.get("email")
															.toString());
													// Profile picture
													String profilePicture = "http://graph.facebook.com/"
															+ user.getId()
															+ "/picture?type=large";

													mCurrentUser
															.put(ParseConstants.KEY_FACEBOOK_PROFILE_PICTURE,
																	profilePicture);

													mCurrentUser
															.saveEventually();

												}

												Log.d(getClass().getSimpleName(),
														"Username "
																+ mCurrentUser
																		.getUsername()
																+ " email "
																+ mCurrentUser
																		.getEmail());

											} catch (JSONException e) {
												Log.d("ERROR ",
														"Error parsing returned user data.");
											}

										} else if (response.getError() != null) {
											Log.d(getClass().getSimpleName(),
													"error "
															+ response
																	.getError());
										}
										Intent intent = new Intent(
												ActivityLogIn.this,
												MainActivity.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
										startActivity(intent);
									}

								});
						request.executeAsync();

					} else {
						if (progressDialog != null)
							progressDialog.dismiss();

						String msg = getString(R.string.connectivity_error);
						String tit = getString(R.string.signup_error_title);

						Common.callDialog(ActivityLogIn.this, msg, tit);

					}
					Toast.makeText(ActivityLogIn.this,
							"User logged in through Facebook!",
							Toast.LENGTH_LONG).show();

				}
			}
		});
		 try {
			accessToken = Session
					.getActiveSession()
					.getAccessToken();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return accessToken;
	}

	private class FacebookTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			onLoginButtonClicked();
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog = ProgressDialog.show(ActivityLogIn.this, "",
					"Logging in...", true);
			progressDialog.setCancelable(true);
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// try {
			// if (progressDialog != null)
			// progressDialog.dismiss();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}

	}


}
