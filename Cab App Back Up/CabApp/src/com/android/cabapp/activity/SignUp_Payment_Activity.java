package com.android.cabapp.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.async.DriverDetailsTask;
import com.android.cabapp.fragments.MyFiltersFragment;
import com.android.cabapp.model.AccountModification;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.model.Registration;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class SignUp_Payment_Activity extends Activity {// not extends
														// RootActivity for
														// saving bank details
														// on back pressed.
	private static final String TAG = SignUp_Payment_Activity.class
			.getSimpleName();

	ImageView ivBack, ivRegister5;
	TextView textFinish, textHiddenFinish, txtEdit, tvPayment, textOK;
	EditText etBankName, etAccountName, etAccountNo, etSortCode, etPassword;
	CheckBox checkboxCash, checkboxCard, cashboxAccount;
	RelativeLayout rlTopPoint, rlBankDetails;

	Bundle paymentBundle;
	boolean isComingFromEditMyAccount = false, isComingFromMyFilter = false;
	private ProgressDialog dialogPayment;
	boolean isCashChecked = false, isCardChecked = false,
			isAccountChecked = false;
	Context mContext = null;

	String szBankName = "", szAccountName = "", szAccountNumber = "",
			szSortCode = "", szPaymentType = "";

	View activityRootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_payment);
		mContext = this;
		paymentBundle = getIntent().getExtras();

		activityRootView = findViewById(R.id.relMain);
		etBankName = (EditText) findViewById(R.id.etBankNameData);
		etAccountName = (EditText) findViewById(R.id.etAccountNameData);
		etAccountNo = (EditText) findViewById(R.id.etAccountNo);
		etSortCode = (EditText) findViewById(R.id.etSortCode);
		checkboxCash = (CheckBox) findViewById(R.id.checkboxCash);
		checkboxCard = (CheckBox) findViewById(R.id.checkboxCard);
		cashboxAccount = (CheckBox) findViewById(R.id.cashboxAccount);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		rlTopPoint = (RelativeLayout) findViewById(R.id.rlTopPoint);
		tvPayment = (TextView) findViewById(R.id.tvPayment);
		ivRegister5 = (ImageView) findViewById(R.id.ivRegister5);
		rlBankDetails = (RelativeLayout) findViewById(R.id.rlBankDetails);

		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(6);
		etSortCode.setFilters(FilterArray);

		FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(8);
		etAccountNo.setFilters(FilterArray);

		txtEdit = (TextView) findViewById(R.id.tvEdit);
		txtEdit.setVisibility(View.GONE);

		checkboxCard.setOnClickListener(new TextViewOnClickListener());
		textFinish = (TextView) findViewById(R.id.tvFinish);
		textFinish.setOnClickListener(new TextViewOnClickListener());
		ivBack.setOnClickListener(new TextViewOnClickListener());
		textHiddenFinish = (TextView) findViewById(R.id.tvHiddenFinish);
		textHiddenFinish.setOnClickListener(new TextViewOnClickListener());

		etAccountNo.setOnFocusChangeListener(new EditTextFocusListener());
		etSortCode.setOnFocusChangeListener(new EditTextFocusListener());

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
							textHiddenFinish.setVisibility(View.VISIBLE);
							textFinish.setVisibility(View.GONE);
						} else {

							textHiddenFinish.setVisibility(View.GONE);
							textFinish.setVisibility(View.VISIBLE);
						}
					}
				});

		setMapRegistrationData();
		setDataFromBundle();

		isCardChecked = checkboxCard.isChecked();
		if (isCardChecked)
			rlBankDetails.setVisibility(View.VISIBLE);
		else
			rlBankDetails.setVisibility(View.GONE);

		if (paymentBundle != null) {
			if (paymentBundle
					.containsKey(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS)) {
				isComingFromEditMyAccount = paymentBundle
						.getBoolean(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS);
				if (isComingFromEditMyAccount) {
					setHeader();
				}
			}
		}

		checkboxCard.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					setMapRegistrationData();
					setDataFromBundle();
					rlBankDetails.setVisibility(View.VISIBLE);
				} else {
					rlBankDetails.setVisibility(View.GONE);
					etBankName.setText("");
					etAccountName.setText("");
					etAccountNo.setText("");
					etSortCode.setText("");
				}
			}
		});

		// If Coming from MyFilter call modification uri
		Log.e(TAG, "paymentBundle " + paymentBundle);
		if (paymentBundle != null
				&& paymentBundle.containsKey("isFromMyFilter")) {
			if (paymentBundle.getBoolean("isFromMyFilter")) {

				isComingFromMyFilter = paymentBundle
						.getBoolean("isFromMyFilter");
				MyFiltersFragment.isCardFilterSelected = false;

				setHeader();

			}
		}

	}

	void setHeader() {
		rlTopPoint.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
		rlTopPoint.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
		tvPayment.setText(getResources().getString(R.string.payment));
		tvPayment.setGravity(Gravity.CENTER);
		ivRegister5.setVisibility(View.GONE);
		textFinish.setText("Save");
		textHiddenFinish.setText("Save");

		etAccountNo.setTransformationMethod(PasswordTransformationMethod
				.getInstance());
		etSortCode.setTransformationMethod(PasswordTransformationMethod
				.getInstance());

		isCardChecked = checkboxCard.isChecked();
		if (isCardChecked)
			rlBankDetails.setVisibility(View.VISIBLE);
		else
			rlBankDetails.setVisibility(View.GONE);
	}

	void setMapRegistrationData() {
		if (AppValues.mapRegistrationData.containsKey(Constants.BANK_NAME)) {
			etBankName.setText(AppValues.mapRegistrationData
					.get(Constants.BANK_NAME));
			etAccountName.setText(AppValues.mapRegistrationData
					.get(Constants.BANK_ACCOUNT_NAME));
			etAccountNo.setText(AppValues.mapRegistrationData
					.get(Constants.BANK_ACCOUNT_NUMBER));
			etSortCode.setText(AppValues.mapRegistrationData
					.get(Constants.BANK_SORT_CODE));

			etBankName.setSelection(etBankName.getText().length());
			etAccountName.setSelection(etAccountName.getText().length());
			etAccountNo.setSelection(etAccountNo.getText().length());
			etSortCode.setSelection(etSortCode.getText().length());

			if (AppValues.mapRegistrationData
					.containsKey(Constants.IS_CARD_CHECKED)) {
				if (AppValues.mapRegistrationData
						.get(Constants.IS_CARD_CHECKED).equals("true"))
					checkboxCard.setChecked(true);
			}
		}
	}

	void setDataFromBundle() {

		if (paymentBundle != null) {
			if (paymentBundle
					.containsKey(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS)) {
				isComingFromEditMyAccount = paymentBundle
						.getBoolean(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS);
				if (isComingFromEditMyAccount) {

					if (AppValues.driverDetails != null) {

						if (AppValues.driverDetails.getPaymentType()
								.equalsIgnoreCase("Cash")) {
							// checkboxCard.setChecked(false);
							rlBankDetails.setVisibility(View.GONE);
						} else {
							if (AppValues.driverDetails.getBankDetails() != null) {
								if (AppValues.driverDetails.getBankDetails()
										.getName() != null)
									etBankName.setText(AppValues.driverDetails
											.getBankDetails().getName());
								if (AppValues.driverDetails.getBankDetails()
										.getAccountName() != null)
									etAccountName
											.setText(AppValues.driverDetails
													.getBankDetails()
													.getAccountName());
								if (AppValues.driverDetails.getBankDetails()
										.getAccountNumber() != null)
									etAccountNo.setText(AppValues.driverDetails
											.getBankDetails()
											.getAccountNumber());
								if (AppValues.driverDetails.getBankDetails()
										.getSortCode() != null)
									etSortCode.setText(AppValues.driverDetails
											.getBankDetails().getSortCode());

								etBankName.setSelection(etBankName.getText()
										.length());
								etAccountName.setSelection(etAccountName
										.getText().length());
								etAccountNo.setSelection(etAccountNo.getText()
										.length());
								etSortCode.setSelection(etSortCode.getText()
										.length());

								checkboxCard.setChecked(true);
								rlBankDetails.setVisibility(View.VISIBLE);
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mContext = null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// super.initWidgets();

	}

	class EditTextFocusListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub

			switch (v.getId()) {

			case R.id.etAccountNo:
				if (hasFocus && isComingFromEditMyAccount)
					etAccountNo.setTransformationMethod(null);
				else
					etAccountNo
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
				break;

			case R.id.etSortCode:
				if (hasFocus && isComingFromEditMyAccount)
					etSortCode.setTransformationMethod(null);
				else
					etSortCode
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());

				break;

			}

		}

	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tvFinish:
				Log.e(TAG, "isComingFromEditMyAccount: "
						+ isComingFromEditMyAccount);
				if (isComingFromEditMyAccount) {
					PasswordVerificationPopUp();
				} else
					FinishButtonCall();

				break;

			case R.id.tvHiddenFinish:
				if (isComingFromEditMyAccount)
					PasswordVerificationPopUp();
				else
					FinishButtonCall();

				break;

			case R.id.ivBack:
				setDataInHashMap();
				Util.hideSoftKeyBoard(mContext, ivBack);
				finish();

				break;

			case R.id.checkboxCard:
				Log.e(TAG, "checkboxCard clicked");
				Util.hideSoftKeyBoard(mContext, checkboxCard);

				break;

			}
		}
	}

	void bankDetailsValidation() {
		if (szBankName.isEmpty() || szAccountName.isEmpty()
				|| szAccountNumber.isEmpty() || szSortCode.isEmpty()) {
			Util.showToastMessage(mContext, "Please complete all fields",
					Toast.LENGTH_LONG);
		} else if (szAccountNumber.length() != 8) {
			Util.showToastMessage(mContext,
					"Account number should be of exactly 8 digits",
					Toast.LENGTH_LONG);
		} else if (szSortCode.length() != 6) {
			Util.showToastMessage(mContext,
					"Sort code should be of exactly 6 digits",
					Toast.LENGTH_LONG);
		} else {
			callVerificationPopUp();
		}
	}

	@SuppressWarnings("deprecation")
	void callVerificationPopUp() {

		final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = SignUp_Payment_Activity.this
				.getLayoutInflater();
		View layout = inflater.inflate(R.layout.password_dialog, null);
		dialog.setView(layout);
		final EditText etPassword = (EditText) layout
				.findViewById(R.id.etPassword);
		dialog.setTitle("Verification");
		dialog.setMessage("Enter Your Password");
		dialog.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface argDialog, int argWhich) {
				// TODO Auto-generated method stub
				Util.hideSoftKeyBoard(mContext, etPassword);
				String szPassword = etPassword.getText().toString().trim();
				if (szPassword.isEmpty()) {
					Util.showToastMessage(mContext,
							"Please enter your valid password to proceed",
							Toast.LENGTH_LONG);
				} else if (szPassword.equals(Util.getPassword(mContext))) {
					FinishButtonCall();
				} else {
					Util.showToastMessage(mContext,
							"Incorrect password. Please try again",
							Toast.LENGTH_LONG);
				}
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

	}

	void PasswordVerificationPopUp() {

		intialisingData();
		if (isCardChecked && isCashChecked) {
			bankDetailsValidation();
		} else if (isCardChecked) {
			bankDetailsValidation();
		}
		// if (szBankName.isEmpty() || szAccountName.isEmpty()
		// || szAccountNumber.isEmpty() || szSortCode.isEmpty()) {
		// Util.showToastMessage(mContext, "Please complete all fields",
		// Toast.LENGTH_LONG);
		// } else if (szAccountNumber.length() != 8) {
		// Util.showToastMessage(mContext,
		// "Account number should be of exactly 8 digits",
		// Toast.LENGTH_LONG);
		// } else if (szSortCode.length() != 6) {
		// Util.showToastMessage(mContext,
		// "Sort code should be of exactly 6 digits",
		// Toast.LENGTH_LONG);
		// }
		else {
			callVerificationPopUp();
		}
	}

	void intialisingData() {
		szBankName = etBankName.getText().toString().trim();
		szAccountName = etAccountName.getText().toString().trim();
		szAccountNumber = etAccountNo.getText().toString().trim();
		szSortCode = etSortCode.getText().toString().trim();

		isCashChecked = checkboxCash.isChecked();
		isCardChecked = checkboxCard.isChecked();
		isAccountChecked = cashboxAccount.isChecked();
	}

	void FinishButtonCall() {
		if (mContext != null)
			Util.hideSoftKeyBoard(mContext, textFinish);

		intialisingData();

		if (!isCashChecked && !isCardChecked && !isAccountChecked) {
			Util.showToastMessage(mContext, "Please select a payment type",
					Toast.LENGTH_LONG);
		} else {

			if (isCardChecked && isCashChecked) {
				szPaymentType = "both";
				validate(szPaymentType);
			} else if (isCashChecked) {
				szPaymentType = "cash";
				callRegistration(szPaymentType);
			} else if (isCardChecked) {
				szPaymentType = "card";
				validate(szPaymentType);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		setDataInHashMap();
		finish();
	}

	void setDataInHashMap() {
		szBankName = etBankName.getText().toString().trim();
		szAccountName = etAccountName.getText().toString().trim();
		szAccountNumber = etAccountNo.getText().toString().trim();
		szSortCode = etSortCode.getText().toString().trim();
		if (!szBankName.isEmpty() || !szAccountName.isEmpty()
				|| !szAccountNumber.isEmpty() || !szSortCode.isEmpty()) {
			AppValues.mapRegistrationData.put(Constants.BANK_NAME, szBankName);
			AppValues.mapRegistrationData.put(Constants.BANK_ACCOUNT_NAME,
					szAccountName);
			AppValues.mapRegistrationData.put(Constants.BANK_ACCOUNT_NUMBER,
					szAccountNumber);
			AppValues.mapRegistrationData.put(Constants.BANK_SORT_CODE,
					szSortCode);
			isCardChecked = checkboxCard.isChecked();
			if (isCardChecked)
				AppValues.mapRegistrationData.put(Constants.IS_CARD_CHECKED,
						"true");
			else
				AppValues.mapRegistrationData.put(Constants.IS_CARD_CHECKED,
						"false");
		}
	}

	public class RegistrationTask extends AsyncTask<String, Void, String> {
		public Bundle bundle;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Registration registration = new Registration(
					SignUp_Payment_Activity.this, bundle);
			String response = registration.RegistrationCredentials();
			Log.e(TAG, "Registration response::> " + response);
			try {
				JSONObject jObject = new JSONObject(response);

				// {
				// "success": "true",
				// "driverId": 104,
				// "numberOfCredits": 5,
				// "isauthorized": false
				// }

				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {
					Util.setDriverID(SignUp_Payment_Activity.this,
							jObject.getString("driverId"));
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

			if (mContext != null && dialogPayment != null)
				dialogPayment.dismiss();

			if (result.equals("success")) {
				// Toast.makeText(mContext, "Successful submission of data!",
				// Toast.LENGTH_LONG).show();
				Util.setIsAuthorised(mContext, false);

				SignUp_AboutYou_Activity.finishActivity();
				SignUp_YourAccount_Activity.finishActivity();
				SignUp_VehicleDetails_Activity.finishActivity();
				SignUp_DriverDetails_Activity.finishActivity();
				finish();// SignUp_Payment_Activity
				LogInActivity.finishActivity();
				LandingActivity.finishActivity();

				Intent intent = new Intent(SignUp_Payment_Activity.this,
						DocumentUploadActivity.class);// TutorialActivity
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra(Constants.FROM_REGISTRATION_COMPLETE, true);
				intent.putExtras(paymentBundle);
				startActivity(intent);
			} else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}

		}

	}

	public void validate(String paymentType) {
		if (szBankName.isEmpty() || szAccountName.isEmpty()
				|| szAccountNumber.isEmpty() || szSortCode.isEmpty()) {
			Util.showToastMessage(mContext, "Please complete all fields",
					Toast.LENGTH_LONG);
		} else if (szAccountNumber.length() != 8) {
			Util.showToastMessage(mContext,
					"Account number should be of exactly 8 digits",
					Toast.LENGTH_LONG);
		} else if (szSortCode.length() != 6) {
			Util.showToastMessage(mContext,
					"Sort code should be of exactly 6 digits",
					Toast.LENGTH_LONG);
		} else {
			callRegistration(paymentType);
		}
	}

	public void callRegistration(String sPaymentType) {
		if (!paymentBundle.isEmpty()) {
			if (sPaymentType.equals("card") || sPaymentType.equals("both")) {
				paymentBundle.putBoolean(Constants.IS_CARD_CHECKED, true);
				paymentBundle.putString(Constants.BANK_NAME, szBankName);
				paymentBundle.putString(Constants.BANK_ACCOUNT_NAME,
						szAccountName);
				paymentBundle.putString(Constants.BANK_ACCOUNT_NUMBER,
						szAccountNumber);
				paymentBundle.putString(Constants.PAYMENT_TYPE, sPaymentType);
				paymentBundle.putString(Constants.BANK_SORT_CODE, szSortCode);
				paymentBundle.putString(Constants.BANK_IBAN, "true");

				AppValues.mapRegistrationData.put(Constants.BANK_NAME,
						szBankName);
				AppValues.mapRegistrationData.put(Constants.BANK_ACCOUNT_NAME,
						szAccountName);
				AppValues.mapRegistrationData.put(
						Constants.BANK_ACCOUNT_NUMBER, szAccountNumber);
				AppValues.mapRegistrationData.put(Constants.BANK_SORT_CODE,
						szSortCode);
				AppValues.mapRegistrationData.put(Constants.BANK_IBAN, "true");
			} else {
				paymentBundle.putString(Constants.PAYMENT_TYPE, sPaymentType);// "cash"

				AppValues.mapRegistrationData.put(Constants.PAYMENT_TYPE,
						sPaymentType);
			}

			if (isComingFromMyFilter) {
				if (NetworkUtil.isNetworkOn(mContext)) {
					dialogPayment = new ProgressDialog(mContext);
					dialogPayment.setMessage("Loading...");
					dialogPayment.setCancelable(false);
					dialogPayment.show();

					setBadgeColor();
					MyFilterTask myFilterTask = new MyFilterTask();
					myFilterTask.bundle = paymentBundle;
					myFilterTask.execute();
				} else {
					Util.showToastMessage(
							mContext,
							getResources().getString(R.string.no_network_error),
							Toast.LENGTH_LONG);
				}
			} else if (!isComingFromEditMyAccount) {
				if (NetworkUtil.isNetworkOn(mContext)) {

					dialogPayment = new ProgressDialog(
							SignUp_Payment_Activity.this);
					dialogPayment.setMessage("Loading...");
					dialogPayment.setCancelable(false);
					dialogPayment.show();

					RegistrationTask registrationTask = new RegistrationTask();
					registrationTask.bundle = paymentBundle;
					registrationTask.execute();
				} else {
					Util.showToastMessage(
							mContext,
							getResources().getString(R.string.no_network_error),
							Toast.LENGTH_LONG);
				}
			} else {
				if (NetworkUtil.isNetworkOn(mContext)) {

					if (isCardChecked)
						paymentBundle.putString(Constants.PAYMENT_TYPE, "both");
					else
						paymentBundle.putString(Constants.PAYMENT_TYPE, "cash");

					if (AppValues.driverDetails != null) {
						paymentBundle.putString(Constants.WORKING_CITY,
								AppValues.driverDetails.getWorkingCity());
						paymentBundle.putString(Constants.FIRSTNAME,
								AppValues.driverDetails.getFirstname());
						paymentBundle.putString(Constants.SURNAME,
								AppValues.driverDetails.getSurname());
						paymentBundle.putString(Constants.EMAIL_ADDRESS,
								AppValues.driverDetails.getEmail());
						paymentBundle.putString(Constants.USERNAME,
								AppValues.driverDetails.getUsername());
						paymentBundle.putString(
								Constants.REGISTRATION_PASSWORD,
								Util.getPassword(mContext));

						setBadgeColor();

						paymentBundle.putString(Constants.BADGE_NUMBER,
								AppValues.driverDetails.getBadgeNumber());
						paymentBundle.putString(Constants.DRIVER_BADGE_EXPIRY,
								AppValues.driverDetails.getBadgeExpiration());
						paymentBundle.putString(Constants.MOBILE_NUMBER,
								AppValues.driverDetails.getMobileNumber());
						paymentBundle.putString(Constants.COUNRTY_CODE,
								AppValues.driverDetails.getInternationalCode());
						paymentBundle.putString(
								Constants.VECHILE_WHEELCHAIR_ACCESS,
								AppValues.driverDetails.getWheelchairAccess());
						paymentBundle.putString(
								Constants.VECHILE_MAXIMUM_PASSENGERS, String
										.valueOf(AppValues.driverDetails
												.getPassengerCapacity()));

						paymentBundle.putString(
								Constants.VECHILE_REGISTRATION_NO,
								AppValues.driverDetails.getVehicle()
										.getRegistration());
						paymentBundle.putString(Constants.VECHILE_MAKE,
								AppValues.driverDetails.getVehicle().getMake());
						paymentBundle.putString(Constants.VECHILE_COLOUR,
								AppValues.driverDetails.getVehicle()
										.getColour());
						paymentBundle
								.putString(Constants.VECHILE_MODEL,
										AppValues.driverDetails.getVehicle()
												.getModel());
					}

					Log.e(TAG,
							"Payment type bundle: " + paymentBundle.toString());

					dialogPayment = new ProgressDialog(
							SignUp_Payment_Activity.this);
					dialogPayment.setMessage("Loading...");
					dialogPayment.setCancelable(false);
					dialogPayment.show();

					EditBankAccountTask editBankDetails = new EditBankAccountTask();
					editBankDetails.bundle = paymentBundle;
					editBankDetails.execute();
				} else {
					Util.showToastMessage(
							mContext,
							getResources().getString(R.string.no_network_error),
							Toast.LENGTH_LONG);
				}
			}

		}
	}

	public class EditBankAccountTask extends AsyncTask<String, Void, String> {
		public Bundle bundle;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			AccountModification accModification = new AccountModification(
					SignUp_Payment_Activity.this, bundle);
			String response = accModification.ModificationCredentials();
			Log.e(TAG, "EditBankAccountTask response::> " + response);
			try {
				JSONObject jObject = new JSONObject(response);
				String errorMessage = "";
				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {

					DriverAccountDetails driverAccount = new DriverAccountDetails(
							mContext);
					driverAccount.retriveAccountDetails(mContext);

					DriverSettingDetails driverSettings = new DriverSettingDetails(
							mContext);
					driverSettings.retriveDriverSettings(mContext);

					if (jObject.has("isauthorized")) {
						if (jObject.getString("isauthorized").equals("false")) {
							return "success";
						} else if (jObject.getString("isauthorized").equals(
								"true")) {
							errorMessage = "pending";
							return errorMessage;
						}
					}
					// return "success";
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						errorMessage = jErrorsArray.getJSONObject(0).getString(
								"message");
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

			if (dialogPayment != null)
				dialogPayment.dismiss();

			if (result.equals("success")) {
				MyFiltersFragment.isCardFilterSelected = true;
				if (isComingFromEditMyAccount)
					Util.showToastMessage(mContext,
							"You have successfully saved your changes",
							Toast.LENGTH_LONG);
				finish();
			} else if (result.equals("pending")) {

				Util.showToastMessage(mContext,
						"Your account is awaiting approval", Toast.LENGTH_LONG);
				LogInActivity.finishActivity();
				MainActivity.finishActivity();
				finish();

				Intent intent = new Intent(SignUp_Payment_Activity.this,
						TutorialActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			} else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}
		}
	}

	void setBadgeColor() {
		// Badge color if city selected is london
		if (Util.getCitySelectedLondon(mContext)) {
			if (AppValues.driverDetails.getBadgeColour() != null) {
				if (AppValues.driverDetails.getBadgeColour().equals(
						Constants.BADGE_COLOUR_YELLOW)) {
					paymentBundle.putString(Constants.BADGE_COLOUR,
							Constants.BADGE_COLOUR_YELLOW);
					if (AppValues.driverDetails.getSector() != null)
						paymentBundle.putString(Constants.SECTOR,
								AppValues.driverDetails.getSector());
				} else {
					paymentBundle.putString(Constants.BADGE_COLOUR,
							Constants.BADGE_COLOUR_GREEN);
				}

			}
		}
	}

	public class MyFilterTask extends AsyncTask<String, Void, String> {
		public Bundle bundle;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			AccountModification accModification = new AccountModification(
					mContext, bundle);
			String response = accModification.ModificationCredentials();
			Log.e(TAG, "MyfilterTask bank account response::> " + response);
			try {
				JSONObject jObject = new JSONObject(response);
				String errorMessage = "";
				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {
					if (jObject.has("isauthorized")) {
						if (jObject.getString("isauthorized").equals("false")) {
							return "success";
						} else if (jObject.getString("isauthorized").equals(
								"true")) {
							errorMessage = "pending";
							return errorMessage;
						}
					}
					// return "success";
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						errorMessage = jErrorsArray.getJSONObject(0).getString(
								"message");
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

			if (dialogPayment != null)
				dialogPayment.dismiss();

			if (result.equals("success")) {

				DriverSettingDetails driverSettings = new DriverSettingDetails(
						mContext);
				driverSettings.retriveDriverSettings(mContext);

				DriverDetailsTask driverDetailsTask = new DriverDetailsTask();
				driverDetailsTask.isFromMyFilters = true;
				driverDetailsTask.mContext = mContext;
				driverDetailsTask.execute();

				MyFiltersFragment.isCardFilterSelected = true;
				finish();

			} else if (result.equals("pending")) {

				Util.showToastMessage(mContext,
						"Your account is awaiting approval", Toast.LENGTH_LONG);
				// SignUp_AboutYou_Activity.finishActivity();
				LogInActivity.finishActivity();
				MainActivity.finishActivity();
				finish();

				Intent intent = new Intent(SignUp_Payment_Activity.this,
						TutorialActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			} else {
				// MyFiltersFragment.isCardFilterSelected = false;
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}

		}

	}
}
