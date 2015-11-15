package com.android.cabapp.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.model.LogIn;
import com.android.cabapp.model.MyJobsCount;
import com.android.cabapp.model.UserAuthorisation;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.urbanairship.UAirship;

public class LogInActivity extends Activity {

	private static final String TAG = LogInActivity.class.getSimpleName();

	EditText etUserName, etPassword;
	TextView textForgotPassword, textLogIn, textSignUp;
	static Context mContext;
	CheckBox cbRememberMe;

	private ProgressDialog progressDialogLogIn;
	String accessToken = "", userName = "", password = "";
	boolean bIsRememberMeChecked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		mContext = this;

		etUserName = (EditText) findViewById(R.id.etUserName);
		etPassword = (EditText) findViewById(R.id.etPassword);
		textForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
		textLogIn = (TextView) findViewById(R.id.tvLogIn);
		textSignUp = (TextView) findViewById(R.id.tvSignUp);
		cbRememberMe = (CheckBox) findViewById(R.id.checkboxRememberMe);

		textForgotPassword.setOnClickListener(new TextViewOnClickListener());
		textLogIn.setOnClickListener(new TextViewOnClickListener());
		textSignUp.setOnClickListener(new TextViewOnClickListener());

		if (Util.getIsRememberMeChecked(mContext)) {
			etUserName.setText(Util.getLogInEmailOrUserName(mContext));
			etPassword.setText(Util.getLogInPassword(mContext));

			etUserName.setSelection(etUserName.getText().length());
			etPassword.setSelection(etPassword.getText().length());
			cbRememberMe.setChecked(true);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		NotificationManager nMgr = (NotificationManager) UAirship.shared()
				.getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (nMgr != null)
			nMgr.cancelAll();
		super.onResume();

	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tvForgotPassword:
				Intent intent = new Intent(LogInActivity.this,
						ForgotPasswordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			case R.id.tvLogIn:
				Util.hideSoftKeyBoard(LogInActivity.this, etUserName);

				bIsRememberMeChecked = cbRememberMe.isChecked();

				userName = etUserName.getText().toString().trim();
				password = etPassword.getText().toString().trim();
				if (userName.length() == 0 || password.length() == 0) {
					Util.showToastMessage(mContext,
							"Please complete all fields", Toast.LENGTH_LONG);
				} else if (userName.contains("@")
						&& !userName.matches(Constants.EMAIL_PATTERN)) {
					Util.showToastMessage(mContext,
							"Please enter a valid email address",
							Toast.LENGTH_LONG);
				}
				// else if (password.length() < 7) {
				// Util.showToastMessage(mContext,
				// "Password should be minimum of 6 characters.",
				// Toast.LENGTH_LONG);
				// }
				else {
					if (NetworkUtil.isNetworkOn(LogInActivity.this)) {
						if (!userName.isEmpty() && !password.isEmpty()) {
							progressDialogLogIn = new ProgressDialog(
									LogInActivity.this);
							progressDialogLogIn.setMessage("Logging in...");
							progressDialogLogIn.setCancelable(false);
							progressDialogLogIn.show();

							LogInTask logInTask = new LogInTask();
							logInTask.szUsername = userName;
							logInTask.szPassword = password;
							logInTask.execute();
						}
					} else {
						Util.showToastMessage(
								LogInActivity.this,
								getResources().getString(
										R.string.no_network_error),
								Toast.LENGTH_LONG);
					}
				}

				break;

			case R.id.tvSignUp:

				Intent intentSignUp = new Intent(LogInActivity.this,
						SignUp_AboutYou_Activity.class);
				intentSignUp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentSignUp);
				break;

			}
		}

	}

	public class LogInTask extends AsyncTask<String, Void, String> {
		public String szUsername, szPassword;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			String response = "";

			if (Util.getAccessToken(mContext).isEmpty()) {
				UserAuthorisation userAuthorisation = new UserAuthorisation(
						mContext, szUsername);
				response = userAuthorisation.IsUserAuthorised();

				Log.d(TAG, "Login Response :: " + response);

				try {
					JSONObject jObject = new JSONObject(response);

					if (jObject.has("driverId")) {
						Util.setDriverID(mContext,
								jObject.getString("driverId"));
					}

					if (jObject.has("document")) {
						Util.setIsDocumentsUploaded(mContext,
								jObject.getBoolean("document"));
					}

					if (jObject.has("status")
							&& jObject.getString("status").equals("ap")) {
						Util.setIsAuthorised(mContext, true);
						response = "approved";
					} else {
						Util.setAccessToken(mContext, "");
						if (jObject.has("status")) {
							if (jObject.getString("status").equals("pn")) {
								response = "pending";
							} else if (jObject.getString("status").equals("dc")
									|| jObject.getString("status").equals("sp")
									|| jObject.getString("status").equals("dl")) {
								response = "failed";
							}
						}
					}

					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						String errorMessage = jErrorsArray.getJSONObject(0)
								.getString("message");
						return errorMessage;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Error in connection! Please try again";
				}
			} else {
				response = "approved";
			}

			if (response.equals("approved")) {
				LogIn login = new LogIn(LogInActivity.this, szUsername,
						szPassword);
				response = login.LoginCredentials();
				try {
					JSONObject jObject = new JSONObject(response);

					if (jObject.has("success")
							&& jObject.getString("success").equals("true")) {

						if (jObject.has("accessToken")) {
							accessToken = jObject.getString("accessToken");
							Log.e(TAG, "ACCESSTOKEN::>   " + accessToken);
							AppValues.mapRegistrationData.clear();
							Constants.accessToken = accessToken;
							Util.setAccessToken(mContext, Constants.accessToken);
							Util.setIsAuthorised(mContext, true);
							Util.setEmailOrUserName(mContext, szUsername);
							Util.setPassword(mContext, szPassword);
							DriverAccountDetails driverAccount = new DriverAccountDetails(
									mContext);
							driverAccount.retriveAccountDetails(mContext);

							DriverSettingDetails driverSettings = new DriverSettingDetails(
									mContext);
							driverSettings.retriveDriverSettings(mContext);

							MyJobsCount myJobsCount = new MyJobsCount(mContext);
							AppValues.nJobsCount = myJobsCount.getMyJobsCount();

						}
						if (jObject.has("numberOfCredits")) {
							int noOfCredits = jObject.getInt("numberOfCredits");
							Util.setNumberOfCredits(mContext, noOfCredits);
						}
						if (jObject.has("isForgot")) {
							String isForgot = jObject.getString("isForgot");
							if (isForgot.equals("Y"))
								Util.setForgot(mContext, "Y");
							else
								Util.setForgot(mContext, "N");
						}
					} else {
						if (jObject.has("errors")) {
							JSONArray jErrorsArray = jObject
									.getJSONArray("errors");

							String errorMessage = jErrorsArray.getJSONObject(0)
									.getString("message");
							return errorMessage;
						}
					}
					// {"errors":[{"type":"ValidationException","message":"Invalid email"}]}

					// {"errors":[{"type":"AuthenticationException","message":"Incorrect
					// username and\/or password"}]}

					return "success";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Error in connection! Please try again";
				}
			}
			return response;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progressDialogLogIn != null)
				progressDialogLogIn.dismiss();

			if (!accessToken.isEmpty()) {
				if (Util.getForgot(mContext).equals("Y")) {
					Constants.accessToken = "";
					Util.setAccessToken(mContext, "");
					Util.showToastMessage(
							mContext,
							"Please change your password first and then log in",
							Toast.LENGTH_LONG);
					Util.finishActivity(mContext);
					LandingActivity.finishActivity();
					Intent intent1 = new Intent(LogInActivity.this,
							ChangePasswordActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("email", userName);
					bundle.putBoolean("fromLogIn", true);
					intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent1.putExtras(bundle);
					startActivity(intent1);
				} else {
					// Success:
					// Util.showToastMessage(mContext, "Successful login",
					// Toast.LENGTH_LONG);

					if (bIsRememberMeChecked) {
						Util.setIsRememberMeChecked(mContext,
								bIsRememberMeChecked);
						Util.setLogInEmailOrUserName(mContext, userName);
						Util.setLogInPassword(mContext, password);
					} else {
						Util.setIsRememberMeChecked(mContext,
								bIsRememberMeChecked);
						Util.setLogInEmailOrUserName(mContext, "");
						Util.setLogInPassword(mContext, "");
					}

					Util.finishActivity(mContext);
					LandingActivity.finishActivity();
					Intent intent1 = new Intent(LogInActivity.this,
							MainActivity.class);
					intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent1);
				}
			} else if (result.equals("pending")) {
				Util.setIsAuthorised(mContext, false);
				Util.showToastMessage(mContext,
						"Your account is awaiting approval", Toast.LENGTH_LONG);
			} else if (result.equals("failed")) {
				Util.setIsAuthorised(mContext, false);
				Util.showToastMessage(mContext,
						"Your Account request has been declined",
						Toast.LENGTH_LONG);
				Util.finishActivity(mContext);
				Intent intent1 = new Intent(mContext, LandingActivity.class);
				intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent1);
			} else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
				etPassword.setText("");
			}

		}
	}

	public static void finishActivity() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

}
