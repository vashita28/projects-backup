package com.android.cabapp.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.adapter.CityBaseAdapter;
import com.android.cabapp.adapter.CountryBaseAdapter;
import com.android.cabapp.datastruct.json.City;
import com.android.cabapp.datastruct.json.CountryList;
import com.android.cabapp.model.AccountModification;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.model.GetAllCityList;
import com.android.cabapp.model.GetAllCountryList;
import com.android.cabapp.model.Registration;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class SignUp_AboutYou_Activity extends RootActivity implements
		OnKeyListener {

	private static final String TAG = SignUp_AboutYou_Activity.class
			.getSimpleName();
	static Context mContext;

	String szCurrentCity = null, szCurrentCountry = null;

	EditText etFirstName, etLastName, etMobileNo, etEmail;
	RelativeLayout rlTopPoint;
	Spinner spinnerCountry, spinnerCity;
	TextView textNext, tvAboutYouNote, txtEdit, tvFirstName, tvLastName,
			textYes, textNo, textHiddenNext;
	List<CountryList> countryList;
	City cityList;
	ImageView ivRegister1, ivDropDownCountry, ivDropDownCity;
	public static String cityCode = "", cityName = "";
	public static String countryCode = "";
	CountryBaseAdapter adapterCountry;
	CityBaseAdapter adapterCity;

	Bundle signupBundle;
	Bundle bundleAboutYou;
	public static boolean isComingFromEditMyAccount;
	private ProgressDialog dialogAboutYou;

	public static SharedPreferences sharedpreferences;
	Editor editor;

	View activityRootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_about_you);

		mContext = this;
		signupBundle = getIntent().getExtras();
		bundleAboutYou = new Bundle();

		activityRootView = findViewById(R.id.relMain);
		etFirstName = (EditText) findViewById(R.id.etFirstName);
		etLastName = (EditText) findViewById(R.id.etLastName);
		etMobileNo = (EditText) findViewById(R.id.etPhoneNumber);
		etEmail = (EditText) findViewById(R.id.etEmail);
		spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
		spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
		tvAboutYouNote = (TextView) findViewById(R.id.tvAboutYou);
		ivRegister1 = (ImageView) findViewById(R.id.ivRegister1);
		rlTopPoint = (RelativeLayout) findViewById(R.id.rlTopPoint);
		txtEdit = (TextView) findViewById(R.id.tvEdit);
		txtEdit.setVisibility(View.GONE);
		ivDropDownCountry = (ImageView) findViewById(R.id.ivDropDownCountry);
		ivDropDownCity = (ImageView) findViewById(R.id.ivDropDownCity);
		tvFirstName = (TextView) findViewById(R.id.tvFirstName);
		tvLastName = (TextView) findViewById(R.id.tvLastName);
		textNext = (TextView) findViewById(R.id.tvNext);
		textHiddenNext = (TextView) findViewById(R.id.tvHiddenNext);
		textNext.setOnClickListener(new TextViewOnClickListener());
		textHiddenNext.setOnClickListener(new TextViewOnClickListener());

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

		spinnerCountry.setOnTouchListener(new onTouchListener());
		spinnerCity.setOnTouchListener(new onTouchListener());

		// Setting current Country and City
		if (NetworkUtil.isNetworkOn(SignUp_AboutYou_Activity.this)) {
			CountryTask countryTask = new CountryTask();
			countryTask.execute();

			CityTask cityTask = new CityTask();
			cityTask.execute();
		} else {
			Util.showToastMessage(SignUp_AboutYou_Activity.this, getResources()
					.getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}

		if (signupBundle != null && AppValues.driverDetails != null
				&& AppValues.countryList != null && AppValues.cityList != null) {
			if (signupBundle
					.containsKey(Constants.FROM_EDIT_MY_ACC_PERSONAL_DETAILS)) {

				isComingFromEditMyAccount = signupBundle
						.getBoolean(Constants.FROM_EDIT_MY_ACC_PERSONAL_DETAILS);
				if (isComingFromEditMyAccount) {
					countryList = AppValues.countryList;
					setCountryAdapter();
					cityList = AppValues.cityList;
					setCityAdapter();

					/*
					 * // Grey out non-editable edittext
					 * etMobileNo.setTextColor(getResources().getColor(
					 * R.color.divider_color));
					 * etEmail.setTextColor(getResources().getColor(
					 * R.color.divider_color));
					 */
				}
			}
		}

		if (AppValues.mapRegistrationData.containsKey(Constants.FIRSTNAME)) {
			etFirstName.setText(AppValues.mapRegistrationData
					.get(Constants.FIRSTNAME));
			etLastName.setText(AppValues.mapRegistrationData
					.get(Constants.SURNAME));
			etMobileNo.setText(AppValues.mapRegistrationData
					.get(Constants.MOBILE_NUMBER));
			etEmail.setText(AppValues.mapRegistrationData
					.get(Constants.EMAIL_ADDRESS));

			etFirstName.setSelection(etFirstName.getText().length());
			etLastName.setSelection(etLastName.getText().length());
			etMobileNo.setSelection(etMobileNo.getText().length());
			etEmail.setSelection(etEmail.getText().length());

			String szCountryCode = AppValues.mapRegistrationData
					.get(Constants.COUNRTY_CODE);
			setCountryByDialingCode(szCountryCode);

			String szCityCode = AppValues.mapRegistrationData
					.get(Constants.WORKING_CITY);
			setCityById(szCityCode);

		}

		if (signupBundle != null) {
			if (signupBundle
					.containsKey(Constants.FROM_EDIT_MY_ACC_PERSONAL_DETAILS)) {

				isComingFromEditMyAccount = signupBundle
						.getBoolean(Constants.FROM_EDIT_MY_ACC_PERSONAL_DETAILS);
				if (isComingFromEditMyAccount) {

					// etMobileNo.setEnabled(false);
					// etEmail.setEnabled(false);
					spinnerCity.setEnabled(false);
					spinnerCountry.setEnabled(false);
					ivDropDownCountry.setVisibility(View.GONE);
					ivDropDownCity.setVisibility(View.GONE);

					tvAboutYouNote.setText(getResources().getString(
							R.string.about_you));
					rlTopPoint.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					rlTopPoint.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
					tvAboutYouNote.setGravity(Gravity.CENTER);
					ivRegister1.setVisibility(View.GONE);
					textNext.setText("Save");
					textHiddenNext.setText("Save");
					tvFirstName.setText("* FIRST NAME");
					tvLastName.setText("* LAST NAME");

					if (AppValues.driverDetails != null) {
						etFirstName.setText(AppValues.driverDetails
								.getFirstname());
						etLastName
								.setText(AppValues.driverDetails.getSurname());
						etMobileNo.setText(AppValues.driverDetails
								.getMobileNumber());
						etEmail.setText(AppValues.driverDetails.getEmail());

						etFirstName
								.setSelection(etFirstName.getText().length());
						etLastName.setSelection(etLastName.getText().length());

					}
				}
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

		// editTextChangeListener();

	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tvNext:
				nextButtonCall();

				break;

			case R.id.tvHiddenNext:
				nextButtonCall();

				break;

			}
		}
	}

	class onTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent arg1) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.spinnerCountry:
				Util.hideSoftKeyBoard(mContext, textNext);

				break;

			case R.id.spinnerCity:
				Util.hideSoftKeyBoard(mContext, textNext);

				break;
			}
			return false;
		}

	}

	void nextButtonCall() {
		if (mContext != null)
			Util.hideSoftKeyBoard(mContext, textNext);

		String szFirstName = etFirstName.getText().toString().trim();
		String szLastName = etLastName.getText().toString().trim();
		String szMobileNumber = etMobileNo.getText().toString().trim();
		String szEmail = etEmail.getText().toString().trim();

		Util.setEmailOrUserName(SignUp_AboutYou_Activity.this, szEmail);

		if (szFirstName.isEmpty() || szLastName.isEmpty()
				|| szMobileNumber.isEmpty() || szEmail.isEmpty()) {
			Util.showToastMessage(mContext, "Please complete all fields",
					Toast.LENGTH_LONG);
		} else if (!szEmail.matches(Constants.EMAIL_PATTERN)) {
			Util.showToastMessage(mContext,
					"Please enter a valid email address!", Toast.LENGTH_LONG);
		} else if (countryCode.isEmpty()) {
			Util.showToastMessage(mContext, "Please select a country",
					Toast.LENGTH_LONG);
		} else if (cityCode.isEmpty() || cityName.isEmpty()
				|| cityName.equalsIgnoreCase("Please select a city")) {
			Util.showToastMessage(mContext, "Please select a city",
					Toast.LENGTH_LONG);
		} else if (szMobileNumber.length() < 10) {
			Util.showToastMessage(mContext,
					"Mobile number cannot be less then 10 digits",
					Toast.LENGTH_LONG);
		} else if (szMobileNumber.length() > 11) {
			Util.showToastMessage(mContext,
					"Mobile number cannot be more then 11 digits",
					Toast.LENGTH_LONG);
		} else {
			if (isComingFromEditMyAccount) {
				bundleAboutYou.putBoolean(
						Constants.FROM_EDIT_MY_ACC_PERSONAL_DETAILS, true);
			}

			bundleAboutYou.putString(Constants.FIRSTNAME, szFirstName);
			bundleAboutYou.putString(Constants.SURNAME, szLastName);
			bundleAboutYou.putString(Constants.MOBILE_NUMBER, szMobileNumber);
			bundleAboutYou.putString(Constants.EMAIL_ADDRESS, szEmail);
			bundleAboutYou.putString(Constants.COUNRTY_CODE, countryCode);
			bundleAboutYou.putString(Constants.WORKING_CITY, cityCode);

			AppValues.mapRegistrationData.put(Constants.FIRSTNAME, szFirstName);
			AppValues.mapRegistrationData.put(Constants.SURNAME, szLastName);
			AppValues.mapRegistrationData.put(Constants.MOBILE_NUMBER,
					szMobileNumber);
			AppValues.mapRegistrationData.put(Constants.EMAIL_ADDRESS, szEmail);
			AppValues.mapRegistrationData.put(Constants.COUNRTY_CODE,
					countryCode);
			AppValues.mapRegistrationData.put(Constants.WORKING_CITY, cityCode);

			if (!isComingFromEditMyAccount) {
				if (NetworkUtil.isNetworkOn(mContext)) {

					dialogAboutYou = new ProgressDialog(
							SignUp_AboutYou_Activity.this);
					dialogAboutYou.setMessage("Loading...");
					dialogAboutYou.setCancelable(false);
					dialogAboutYou.show();

					RegistrationTaskAboutYou registrationTask = new RegistrationTaskAboutYou();
					registrationTask.bundle = bundleAboutYou;
					registrationTask.execute();
				} else {
					Util.showToastMessage(
							mContext,
							getResources().getString(R.string.no_network_error),
							Toast.LENGTH_LONG);
				}

			} else {

				final AlertDialog dialog = new AlertDialog.Builder(
						SignUp_AboutYou_Activity.this)
						.setMessage(
								"Changing of marked fields will put your account into a pending state")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface argDialog,
											int argWhich) {

										if (AppValues.driverDetails != null) {
											// your account details :
											bundleAboutYou.putString(
													Constants.USERNAME,
													AppValues.driverDetails
															.getUsername());
											bundleAboutYou
													.putString(
															Constants.REGISTRATION_PASSWORD,
															Util.getPassword(mContext));

											// Driver Details Data::>
											bundleAboutYou.putString(
													Constants.BADGE_NUMBER,
													AppValues.driverDetails
															.getBadgeNumber());
											bundleAboutYou
													.putString(
															Constants.DRIVER_BADGE_EXPIRY,
															AppValues.driverDetails
																	.getBadgeExpiration());

											// Badge color if city selected is
											// london
											if (Util.getCitySelectedLondon(mContext)) {
												if (AppValues.driverDetails
														.getBadgeColour() != null) {
													if (AppValues.driverDetails
															.getBadgeColour()
															.equals(Constants.BADGE_COLOUR_YELLOW)) {
														bundleAboutYou
																.putString(
																		Constants.BADGE_COLOUR,
																		Constants.BADGE_COLOUR_YELLOW);
														if (AppValues.driverDetails
																.getSector() != null)
															bundleAboutYou
																	.putString(
																			Constants.SECTOR,
																			AppValues.driverDetails
																					.getSector());

													} else {
														bundleAboutYou
																.putString(
																		Constants.BADGE_COLOUR,
																		Constants.BADGE_COLOUR_GREEN);
													}

												}
											}

											// Vehicle Details Data::>
											bundleAboutYou
													.putString(
															Constants.VECHILE_REGISTRATION_NO,
															AppValues.driverDetails
																	.getVehicle()
																	.getRegistration());
											bundleAboutYou.putString(
													Constants.VECHILE_MAKE,
													AppValues.driverDetails
															.getVehicle()
															.getMake());
											bundleAboutYou.putString(
													Constants.VECHILE_MODEL,
													AppValues.driverDetails
															.getVehicle()
															.getModel());
											bundleAboutYou.putString(
													Constants.VECHILE_COLOUR,
													AppValues.driverDetails
															.getVehicle()
															.getColour());
											bundleAboutYou
													.putString(
															Constants.VECHILE_MAXIMUM_PASSENGERS,
															String.valueOf(AppValues.driverDetails
																	.getPassengerCapacity()));
											bundleAboutYou
													.putString(
															Constants.VECHILE_WHEELCHAIR_ACCESS,
															AppValues.driverDetails
																	.getWheelchairAccess());

											// Payment Details Data::>
											if (AppValues.driverDetails
													.getPaymentType().equals(
															"both")) {
												bundleAboutYou
														.putString(
																Constants.PAYMENT_TYPE,
																AppValues.driverDetails
																		.getPaymentType());
												bundleAboutYou
														.putString(
																Constants.BANK_NAME,
																AppValues.driverDetails
																		.getBankDetails()
																		.getName());
												bundleAboutYou
														.putString(
																Constants.BANK_ACCOUNT_NAME,
																AppValues.driverDetails
																		.getBankDetails()
																		.getAccountName());
												bundleAboutYou
														.putString(
																Constants.BANK_ACCOUNT_NUMBER,
																AppValues.driverDetails
																		.getBankDetails()
																		.getAccountNumber());
												bundleAboutYou
														.putString(
																Constants.BANK_SORT_CODE,
																AppValues.driverDetails
																		.getBankDetails()
																		.getSortCode());
												bundleAboutYou.putString(
														Constants.BANK_IBAN,
														"true");

											} else {
												bundleAboutYou
														.putString(
																Constants.PAYMENT_TYPE,
																AppValues.driverDetails
																		.getPaymentType());
											}
											if (NetworkUtil
													.isNetworkOn(mContext)) {
												dialogAboutYou = new ProgressDialog(
														SignUp_AboutYou_Activity.this);
												dialogAboutYou
														.setMessage("Loading...");
												dialogAboutYou
														.setCancelable(false);
												dialogAboutYou.show();

												EditPersonalDetailsTask editMyPersonalDetails = new EditPersonalDetailsTask();
												editMyPersonalDetails.bundle = bundleAboutYou;
												editMyPersonalDetails.execute();
											} else {
												Util.showToastMessage(
														mContext,
														getResources()
																.getString(
																		R.string.no_network_error),
														Toast.LENGTH_LONG);
											}
										}

										// Intent intent = new Intent(
										// SignUp_AboutYou_Activity.this,
										// SignUp_YourAccount_Activity.class);
										// intent.putExtras(bundleAboutYou);
										// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										// startActivity(intent);
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								}).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();

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
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// }
				// });
			}

		}
	}

	public class CountryTask extends AsyncTask<String, Void, List<CountryList>> {

		@Override
		protected List<CountryList> doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (AppValues.countryList == null) {
				GetAllCountryList allCountry = new GetAllCountryList(mContext);
				AppValues.countryList = allCountry.getCountryList(mContext);
			}
			countryList = AppValues.countryList;
			return countryList;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Util.hideSoftKeyBoard(mContext, textNext);
		}

		@Override
		protected void onPostExecute(List<CountryList> countryList) {
			// TODO Auto-generated method stub
			super.onPostExecute(countryList);
			setCountryAdapter();

		}
	}

	void setCountryAdapter() {
		if (countryList != null) {
			// Setting COUNTRY spinner Data:
			adapterCountry = new CountryBaseAdapter(mContext, countryList);
			spinnerCountry.setAdapter(adapterCountry);
			// GetDefaultCountryCity();
			if (szCurrentCountry != null)
				setCountryByISOCode(szCurrentCountry);
		}

		if (AppValues.driverDetails != null && isComingFromEditMyAccount) {
			String countryCode = AppValues.driverDetails.getInternationalCode();
			if (countryCode != null) {
				if (adapterCountry != null) {
					for (int i = 0; i < adapterCountry.getCount(); i++) {
						if (countryCode.equals(countryList.get(i)
								.getDialingCode())) {
							spinnerCountry.setSelection(i + 1);
							break;
						}
					}
				}
			}
		}

	}

	void setCountryByISOCode(String countryISOCode) {

		if (countryISOCode != null) {
			if (adapterCountry != null) {
				for (int i = 0; i < adapterCountry.getCount(); i++) {
					if (countryISOCode.equals(countryList.get(i).getIso())) {
						spinnerCountry.setSelection(i);
						break;
					}
				}
			}
		}
	}

	void setCountryByDialingCode(String countryDialingCode) {

		if (countryDialingCode != null) {
			if (adapterCountry != null) {
				for (int i = 0; i < adapterCountry.getCount(); i++) {
					if (countryDialingCode.equals(countryList.get(i)
							.getDialingCode())) {
						spinnerCountry.setSelection(i);
						break;
					}
				}
			}
		}
	}

	public class CityTask extends AsyncTask<String, Void, City> {

		@Override
		protected City doInBackground(String... params) {
			// TODO Auto-generated method stub

			if (AppValues.cityList == null) {
				GetAllCityList allCity = new GetAllCityList(mContext);
				AppValues.cityList = allCity.getCityList(mContext);
			}
			cityList = AppValues.cityList;
			return cityList;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Util.hideSoftKeyBoard(mContext, textNext);
		}

		@Override
		protected void onPostExecute(City cityList) {
			// TODO Auto-generated method stub
			super.onPostExecute(cityList);

			setCityAdapter();

		}
	}

	void setCityAdapter() {
		if (cityList != null) {
			// Setting CITY spinner Data:
			adapterCity = new CityBaseAdapter(mContext, cityList);
			spinnerCity.setAdapter(adapterCity);

			// GetDefaultCountryCity();
			// if (szCurrentCity != null)
			// setCityByName(szCurrentCity);
		}

		if (AppValues.driverDetails != null && isComingFromEditMyAccount) {
			String cityCode = String.valueOf(AppValues.driverDetails
					.getCityId());
			if (cityCode != null) {
				if (adapterCity != null) {
					for (int i = 0; i < cityList.getCities().size(); i++) {
						if (cityCode
								.equals(cityList.getCities().get(i).getId())) {
							spinnerCity.setSelection(i + 1);
							break;
						}
					}
				}
			}
		}
	}

	void setCityByName(String cityName) {

		if (cityName != null) {
			if (adapterCity != null) {
				for (int i = 0; i < cityList.getCities().size(); i++) {

					if (cityName.equalsIgnoreCase(cityList.getCities().get(i)
							.getCityname())
							|| cityList.getCities().get(i).getCityname().trim()
									.toLowerCase()
									.contains(cityName.toLowerCase())) {
						spinnerCity.setSelection(i);
						break;
					}
				}
			}
		}
	}

	void setCityById(String cityCode) {
		if (cityCode != null) {
			if (adapterCity != null) {
				for (int i = 0; i < cityList.getCities().size(); i++) {

					if (cityCode.equals(cityList.getCities().get(i).getId())) {
						spinnerCity.setSelection(i);
						break;
					}
				}
			}
		}
	}

	public static void finishActivity() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == EditorInfo.IME_ACTION_DONE
				|| keyCode == EditorInfo.IME_ACTION_NEXT
				|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			return true;
		}

		return false;
	}

	public class RegistrationTaskAboutYou extends
			AsyncTask<String, Void, JSONArray> {
		public Bundle bundle;
		JSONArray jErrorsArray;

		@Override
		protected JSONArray doInBackground(String... params) {
			// TODO Auto-generated method stub
			Registration registration = new Registration(
					SignUp_AboutYou_Activity.this, bundle);
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

			if (mContext != null && dialogAboutYou != null)
				dialogAboutYou.dismiss();

			boolean isValid = false;
			try {
				int size = errorJsonArray.length();
				for (int i = 0; i < size; i++) {
					String errorMessage = errorJsonArray.getJSONObject(i)
							.getString("message");
					if (Constants.isDebug)
						Log.e(TAG, "errorMessage :" + errorMessage);
					if (errorMessage
							.equalsIgnoreCase("mobileNumber already in use")
							|| errorMessage
									.equalsIgnoreCase("invalid mobileNumber")
							|| errorMessage
									.equalsIgnoreCase("email already in use")
							|| errorMessage.equalsIgnoreCase("invalid email")) {
						Util.showToastMessage(mContext, errorMessage,
								Toast.LENGTH_LONG);
						isValid = false;
						break;
					} else {
						isValid = true;
					}
				}
				if (Constants.isDebug)
					Log.e(TAG, "isValid AboutYou: " + isValid);
				if (isValid) {
					Intent intent = new Intent(SignUp_AboutYou_Activity.this,
							SignUp_YourAccount_Activity.class);
					intent.putExtras(bundleAboutYou);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public class EditPersonalDetailsTask extends
			AsyncTask<String, Void, String> {
		public Bundle bundle;
		String errorMessage = "";

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			AccountModification accModification = new AccountModification(
					SignUp_AboutYou_Activity.this, bundle);
			String response = accModification.ModificationCredentials();
			Log.e(TAG, "EditPersonalDetailsTask response::> " + response);
			// response::>
			// {"success":"true","driverId":36,"numberOfCredits":59,"isauthorized":true}
			// true:pending state

			try {
				JSONObject jObject = new JSONObject(response);
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
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						errorMessage = jErrorsArray.getJSONObject(0).getString(
								"message");
						return errorMessage;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return errorMessage;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialogAboutYou != null)
				dialogAboutYou.dismiss();

			if (mContext != null && result.equals("success")) {
				DriverAccountDetails driverAccount = new DriverAccountDetails(
						mContext);
				driverAccount.retriveAccountDetails(mContext);

				DriverSettingDetails driverSettings = new DriverSettingDetails(
						mContext);
				driverSettings.retriveDriverSettings(mContext);
				finish();
			} else if (result.equals("pending")) {
				Util.showToastMessage(mContext,
						"Your account is awaiting approval", Toast.LENGTH_LONG);
				LogInActivity.finishActivity();
				MainActivity.finishActivity();
				finish();

				Intent intent = new Intent(SignUp_AboutYou_Activity.this,
						TutorialActivity.class);
				intent.putExtra("isFromMyAccount", true);
				// intent.putExtra(Constants.FROM_MY_ACCOUNT_EDIT, true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}

		}

	}

	// void GetDefaultCountryCity() {
	// Location currentLocation = null;
	// if (Util.getLocation(mContext) != null)
	// currentLocation = Util.getLocation(mContext);
	//
	// Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
	// List<Address> addresses;
	// try {
	// addresses = gcd.getFromLocation(currentLocation.getLatitude(),
	// currentLocation.getLongitude(), 1);
	// if (addresses.size() > 0) {
	// if (addresses.get(0).getLocality() != null) {
	// System.out.println(addresses.get(0).getLocality());
	// szCurrentCity = addresses.get(0).getLocality();
	// } else if (addresses.get(0).getAddressLine(1) != null) {
	// System.out.println(addresses.get(0).getAddressLine(1));
	// szCurrentCity = addresses.get(0).getAddressLine(1);
	// }
	// System.out.println(addresses.get(0).getCountryCode());
	// szCurrentCountry = addresses.get(0).getCountryCode();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
}
