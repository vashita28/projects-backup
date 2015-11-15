package com.android.cabapp.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.async.DriverSettingsTask;
import com.android.cabapp.model.ForgotPassword;
import com.android.cabapp.model.Registration;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class SignUp_YourAccount_Activity extends RootActivity {

	private static final String TAG = SignUp_YourAccount_Activity.class
			.getSimpleName();

	EditText etUsername, etPassword, etConfirmPassword;
	TextView textTermsConditions, textNext, textHiddenNext, tvYourAccountNote,
			textYes, textNo, txtEdit, tvUsername, tvPassword,
			tvConfirmPassword;
	Bundle yourAccountBundle;
	ImageView ivRegister2;
	RelativeLayout rlTermsConditions, rlTopPoint;
	boolean isComingFromEditMyAccount;

	private ProgressDialog dialogYourAccount;

	View activityRootView;

	static Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_youraccount);
		mContext = this;
		_context = this;

		yourAccountBundle = getIntent().getExtras();

		activityRootView = findViewById(R.id.relMain);
		tvUsername = (TextView) findViewById(R.id.tvUsername);
		tvPassword = (TextView) findViewById(R.id.tvPassword);
		tvConfirmPassword = (TextView) findViewById(R.id.tvConfirmPassword);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
		textTermsConditions = (TextView) findViewById(R.id.tvTermsConditions);
		tvYourAccountNote = (TextView) findViewById(R.id.tvYourAccount);
		ivRegister2 = (ImageView) findViewById(R.id.ivRegister2);
		rlTopPoint = (RelativeLayout) findViewById(R.id.rlTopPoint);
		txtEdit = (TextView) findViewById(R.id.tvEdit);
		txtEdit.setVisibility(View.GONE);
		rlTermsConditions = (RelativeLayout) findViewById(R.id.rlTermsConditions);
		textNext = (TextView) findViewById(R.id.tvNextYourAccount);
		textHiddenNext = (TextView) findViewById(R.id.tvHiddenNext);
		textNext.setOnClickListener(new TextViewOnClickListener());
		textHiddenNext.setOnClickListener(new TextViewOnClickListener());
		textTermsConditions.setOnClickListener(new TextViewOnClickListener());

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
						if (heightDiff > 100) {
							textHiddenNext.setVisibility(View.VISIBLE);
							textNext.setVisibility(View.GONE);
						} else {

							textHiddenNext.setVisibility(View.GONE);
							textNext.setVisibility(View.VISIBLE);
						}
					}
				});

		if (AppValues.mapRegistrationData.containsKey(Constants.USERNAME)) {
			etUsername.setText(AppValues.mapRegistrationData
					.get(Constants.USERNAME));
			etUsername.setSelection(etUsername.getText().length());
		}

		if (yourAccountBundle != null) {
			if (yourAccountBundle
					.containsKey(Constants.FROM_EDIT_MY_ACC_PERSONAL_DETAILS)) {
				isComingFromEditMyAccount = yourAccountBundle
						.getBoolean(Constants.FROM_EDIT_MY_ACC_PERSONAL_DETAILS);
				if (isComingFromEditMyAccount) {

					// etUsername.setEnabled(false);
					rlTopPoint.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					rlTopPoint.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
					tvYourAccountNote.setText(getResources().getString(
							R.string.your_account_change_password));
					tvYourAccountNote.setGravity(Gravity.CENTER);
					ivRegister2.setVisibility(View.GONE);
					rlTermsConditions.setVisibility(View.GONE);
					tvUsername.setText("CURRENT PASSWORD");
					tvPassword.setText("NEW PASSWORD");
					tvConfirmPassword.setText("CONFIRM NEW PASSWORD");
					etUsername.setText("");
					etUsername
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
					textNext.setText("Save");
					textHiddenNext.setText("Save");

					// if (AppValues.driverDetails != null) {
					// etUsername.setText(AppValues.driverDetails
					// .getUsername());
					// }
				}
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();
	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tvNextYourAccount:
				nextButtonCall();
				break;

			case R.id.tvHiddenNext:
				nextButtonCall();
				break;

			case R.id.tvTermsConditions:
				Util.hideSoftKeyBoard(mContext, textTermsConditions);
				Intent intentTermsConditions = new Intent(
						SignUp_YourAccount_Activity.this,
						TermsAndConditions_Activity.class);
				intentTermsConditions.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentTermsConditions);
				break;

			}
		}
	}

	void nextButtonCall() {
		if (mContext != null)
			Util.hideSoftKeyBoard(mContext, textNext);

		String sUserName = etUsername.getText().toString().trim();
		String sPassword = etPassword.getText().toString().trim();
		String sConfirmPassword = etConfirmPassword.getText().toString().trim();

		if (sUserName.isEmpty() || sPassword.isEmpty()
				|| sConfirmPassword.isEmpty()) {
			Util.showToastMessage(mContext, "Please complete all fields",
					Toast.LENGTH_LONG);
		} else if (sPassword.length() < 6 || sConfirmPassword.length() < 6) {
			Util.showToastMessage(mContext,
					"Password should be minimum of 6 characters",
					Toast.LENGTH_LONG);
		} else if (!sPassword.equals(sConfirmPassword)) {
			Util.showToastMessage(mContext, "Your passwords don't match",
					Toast.LENGTH_LONG);
		} else {

			yourAccountBundle.putString(Constants.USERNAME, sUserName);
			yourAccountBundle.putString(Constants.REGISTRATION_PASSWORD,
					sPassword);

			AppValues.mapRegistrationData.put(Constants.USERNAME, sUserName);
			AppValues.mapRegistrationData.put(Constants.REGISTRATION_PASSWORD,
					sPassword);

			if (isComingFromEditMyAccount) {
				// final Dialog dialog = new Dialog(mContext,
				// R.style.mydialogstyle);
				// dialog.setContentView(R.layout.confirmation_dialog);
				// dialog.setCanceledOnTouchOutside(false);
				// // dialog.setTitle("Alert!!");
				// dialog.setCancelable(false);
				// dialog.show();
				// textYes = (TextView) dialog.findViewById(R.id.tvYes);
				// textNo = (TextView) dialog.findViewById(R.id.tvNo);
				// textNo.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// dialog.dismiss();
				// }
				// });
				// textYes.setOnClickListener(new OnClickListener() {

				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// dialog.dismiss();
				// if (AppValues.driverDetails != null) {
				// Driver Details Data::>
				// yourAccountBundle.putString(Constants.BADGE_NUMBER,
				// AppValues.driverDetails.getBadgeNumber());
				// yourAccountBundle.putString(
				// Constants.DRIVER_BADGE_EXPIRY,
				// AppValues.driverDetails
				// .getBadgeExpiration());
				//
				// // Badge color if city selected is london
				// if (Util.getCitySelectedLondon(mContext)) {
				// if (AppValues.driverDetails.getBadgeColour() != null)
				// {
				// if (AppValues.driverDetails
				// .getBadgeColour()
				// .equals(Constants.BADGE_COLOUR_YELLOW)) {
				// yourAccountBundle.putString(
				// Constants.BADGE_COLOUR,
				// Constants.BADGE_COLOUR_YELLOW);
				// if (AppValues.driverDetails.getSector() != null)
				// yourAccountBundle.putString(
				// Constants.SECTOR,
				// AppValues.driverDetails
				// .getSector());
				//
				// } else {
				// yourAccountBundle.putString(
				// Constants.BADGE_COLOUR,
				// Constants.BADGE_COLOUR_GREEN);
				// }
				//
				// }
				// }

				// Vehicle Details Data::>
				// yourAccountBundle.putString(
				// Constants.VECHILE_REGISTRATION_NO,
				// AppValues.driverDetails.getVehicle()
				// .getRegistration());
				// yourAccountBundle.putString(Constants.VECHILE_MAKE,
				// AppValues.driverDetails.getVehicle()
				// .getMake());
				// yourAccountBundle.putString(
				// Constants.VECHILE_MODEL,
				// AppValues.driverDetails.getVehicle()
				// .getModel());
				// yourAccountBundle.putString(
				// Constants.VECHILE_COLOUR,
				// AppValues.driverDetails.getVehicle()
				// .getColour());
				// yourAccountBundle.putString(
				// Constants.VECHILE_MAXIMUM_PASSENGERS,
				// String.valueOf(AppValues.driverDetails
				// .getPassengerCapacity()));
				// yourAccountBundle.putString(
				// Constants.VECHILE_WHEELCHAIR_ACCESS,
				// AppValues.driverDetails
				// .getWheelchairAccess());

				// Payment Details Data::>
				// if (AppValues.driverDetails.getPaymentType()
				// .equals("both")) {
				// yourAccountBundle.putString(
				// Constants.PAYMENT_TYPE,
				// AppValues.driverDetails
				// .getPaymentType());
				// yourAccountBundle.putString(
				// Constants.BANK_NAME,
				// AppValues.driverDetails
				// .getBankDetails().getName());
				// yourAccountBundle.putString(
				// Constants.BANK_ACCOUNT_NAME,
				// AppValues.driverDetails
				// .getBankDetails()
				// .getAccountName());
				// yourAccountBundle.putString(
				// Constants.BANK_ACCOUNT_NUMBER,
				// AppValues.driverDetails
				// .getBankDetails()
				// .getAccountNumber());
				// yourAccountBundle
				// .putString(Constants.BANK_SORT_CODE,
				// AppValues.driverDetails
				// .getBankDetails()
				// .getSortCode());
				// yourAccountBundle.putString(
				// Constants.BANK_IBAN, "true");

				// } else {
				// yourAccountBundle.putString(
				// Constants.PAYMENT_TYPE,
				// AppValues.driverDetails
				// .getPaymentType());
				// }
				if (NetworkUtil.isNetworkOn(mContext)) {
					String sEmail = Util
							.getEmailOrUserName(SignUp_YourAccount_Activity.this);
					if (Constants.isDebug)
						Log.e(TAG, "sEmail::>" + sEmail);

					ChangePasswordTask changePasswordTask = new ChangePasswordTask();
					changePasswordTask.sEmail = sEmail;
					changePasswordTask.sOldPassword = sUserName;
					changePasswordTask.sNewPassword = sPassword;
					changePasswordTask.execute();

					// EditPersonalDetailsTask editMyPersonalDetails =
					// new EditPersonalDetailsTask();
					// editMyPersonalDetails.bundle = yourAccountBundle;
					// editMyPersonalDetails.execute();
				} else {
					Util.showToastMessage(
							mContext,
							getResources().getString(R.string.no_network_error),
							Toast.LENGTH_LONG);
				}
				// }
				// }
				// });

			} else {

				if (NetworkUtil.isNetworkOn(SignUp_YourAccount_Activity.this)) {

					dialogYourAccount = new ProgressDialog(
							SignUp_YourAccount_Activity.this);
					dialogYourAccount.setMessage("Loading...");
					dialogYourAccount.setCancelable(false);
					dialogYourAccount.show();

					RegistrationTaskYourAccount registrationTask = new RegistrationTaskYourAccount();
					registrationTask.bundle = yourAccountBundle;
					registrationTask.execute();
				} else {
					Util.showToastMessage(
							mContext,
							getResources().getString(R.string.no_network_error),
							Toast.LENGTH_LONG);
				}
				// Intent intent = new Intent(
				// SignUp_YourAccount_Activity.this,
				// SignUp_DriverDetails_Activity.class);
				// intent.putExtras(yourAccountBundle);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(intent);
			}

		}
	}

	// public class EditPersonalDetailsTask extends
	// AsyncTask<String, Void, String> {
	// public Bundle bundle;
	//
	// @Override
	// protected String doInBackground(String... params) {
	// // TODO Auto-generated method stub
	// AccountModification accModification = new AccountModification(
	// SignUp_YourAccount_Activity.this, bundle);
	// String response = accModification.ModificationCredentials();
	// Log.e(TAG, "EditPersonalDetailsTask response::> " + response);
	// try {
	// JSONObject jObject = new JSONObject(response);
	// String errorMessage = "";
	// if (jObject.has("success")
	// && jObject.getString("success").equals("true")) {
	// if (jObject.has("isauthorized")) {
	// if (jObject.getString("isauthorized").equals("false")) {
	// return "success";
	// } else if (jObject.getString("isauthorized").equals(
	// "true")) {
	// errorMessage = "pending";
	// return errorMessage;
	// }
	// }
	// // return "success";
	// } else {
	// if (jObject.has("errors")) {
	// JSONArray jErrorsArray = jObject.getJSONArray("errors");
	//
	// errorMessage = jErrorsArray.getJSONObject(0).getString(
	// "message");
	// return errorMessage;
	// }
	// }
	// return "success";
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return response;
	//
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	//
	// if (dialogYourAccount != null)
	// dialogYourAccount.dismiss();
	//
	// if (result.equals("success")) {
	// SignUp_AboutYou_Activity.finishActivity();
	// finish();
	//
	// } else if (result.equals("pending")) {
	// Util.showToastMessage(mContext,
	// "Your account is in pending state.", Toast.LENGTH_LONG);
	// SignUp_AboutYou_Activity.finishActivity();
	// LogInActivity.finishActivity();
	// MainActivity.finishActivity();
	// finish();
	//
	// Intent intent = new Intent(SignUp_YourAccount_Activity.this,
	// TutorialActivity.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// startActivity(intent);
	// } else {
	// Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
	// }
	//
	// }
	//
	// }

	public class ChangePasswordTask extends AsyncTask<String, Void, String> {
		public String sEmail, sOldPassword, sNewPassword;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogYourAccount = new ProgressDialog(
					SignUp_YourAccount_Activity.this);
			dialogYourAccount.setMessage("Updating password...");
			dialogYourAccount.setCancelable(false);
			dialogYourAccount.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ForgotPassword changePassword = new ForgotPassword(
					SignUp_YourAccount_Activity.this, sEmail, sOldPassword,
					sNewPassword);
			String response = changePassword.ChangePassword();
			try {
				JSONObject jObject = new JSONObject(response);

				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {
					return "success";
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						String errorMessage = jErrorsArray.getJSONObject(0)
								.getString("message");
						return errorMessage;
					}
				}

				return "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return response;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialogYourAccount != null)
				dialogYourAccount.dismiss();

			if (result.equals("success")) {

				DriverSettingsTask driverSettingTask = new DriverSettingsTask();
				driverSettingTask.mContext = SignUp_YourAccount_Activity.this;
				driverSettingTask.execute();

				Util.setPassword(SignUp_YourAccount_Activity.this, sNewPassword);
				Util.setLogInPassword(mContext, sNewPassword);
				Util.showToastMessage(mContext,
						"Password changed successfully", Toast.LENGTH_LONG);
				finish();
			} else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}

		}

	}

	public class RegistrationTaskYourAccount extends
			AsyncTask<String, Void, JSONArray> {
		public Bundle bundle;
		JSONArray jErrorsArray;

		@Override
		protected JSONArray doInBackground(String... params) {
			// TODO Auto-generated method stub
			Registration registration = new Registration(
					SignUp_YourAccount_Activity.this, bundle);
			String response = registration.RegistrationCredentials();

			try {
				JSONObject jObject = new JSONObject(response);

				if (jObject.has("errors")) {
					JSONArray jErrorsArray = jObject.getJSONArray("errors");
					return jErrorsArray;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jErrorsArray;

		}

		@Override
		protected void onPostExecute(JSONArray errorJsonArray) {
			// TODO Auto-generated method stub
			super.onPostExecute(errorJsonArray);

			if (mContext != null && dialogYourAccount != null)
				dialogYourAccount.dismiss();

			boolean isValid = false;
			try {
				int size = errorJsonArray.length();
				for (int i = 0; i < size; i++) {
					String errorMessage = errorJsonArray.getJSONObject(i)
							.getString("message");
					if (Constants.isDebug)
						Log.e(TAG, "errorMessage :" + errorMessage);
					if (errorMessage
							.equalsIgnoreCase("username already in use")) {
						Util.showToastMessage(mContext, errorMessage,
								Toast.LENGTH_LONG);
						isValid = false;
						break;
					} else {
						isValid = true;
					}
				}
				if (Constants.isDebug)
					Log.e(TAG, "isValid YourAccount: " + isValid);
				if (isValid) {
					Intent intent = new Intent(
							SignUp_YourAccount_Activity.this,
							SignUp_DriverDetails_Activity.class);
					intent.putExtras(yourAccountBundle);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	static void finishActivity() {
		if (_context != null)
			((Activity) _context).finish();
	}

}
