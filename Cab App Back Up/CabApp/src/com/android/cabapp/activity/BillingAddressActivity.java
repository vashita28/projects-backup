package com.android.cabapp.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.model.AccountModification;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class BillingAddressActivity extends RootActivity {

	private static final String TAG = BillingAddressActivity.class
			.getSimpleName();

	TextView txtEdit, tvSave, tvHiddenSave;
	EditText etBillingAdd1, etBillingAdd2, etCity, etPostCode, etCountry;

	private ProgressDialog dialogBillingAdd;

	View activityRootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_address);

		mContext = this;

		activityRootView = findViewById(R.id.relMain);
		txtEdit = (TextView) findViewById(R.id.tvEdit);
		txtEdit.setVisibility(View.GONE);
		etBillingAdd1 = (EditText) findViewById(R.id.etAddressLine1);
		etBillingAdd2 = (EditText) findViewById(R.id.etAddressLine2);
		etCity = (EditText) findViewById(R.id.etCity);
		etPostCode = (EditText) findViewById(R.id.etPostCode);
		etCountry = (EditText) findViewById(R.id.etCountry);
		tvSave = (TextView) findViewById(R.id.tvSaveBillingAddress);
		tvSave.setOnClickListener(new TextViewOnClickListener());
		tvHiddenSave = (TextView) findViewById(R.id.tvHiddenSave);
		tvHiddenSave.setOnClickListener(new TextViewOnClickListener());

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
							tvHiddenSave.setVisibility(View.VISIBLE);
							tvSave.setVisibility(View.GONE);
						} else {

							tvHiddenSave.setVisibility(View.GONE);
							tvSave.setVisibility(View.VISIBLE);
						}
					}
				});

		// if (AppValues.driverSettings != null) {
		// etCity.setText(AppValues.driverSettings.getName());
		// etCity.setEnabled(false);
		// }

		if (AppValues.driverDetails != null) {
			if (AppValues.driverDetails.getBillingAddress1() != null
					&& !AppValues.driverDetails.getBillingAddress1().isEmpty())
				etBillingAdd1.setText(AppValues.driverDetails
						.getBillingAddress1());

			if (AppValues.driverDetails.getBillingAddress2() != null
					&& !AppValues.driverDetails.getBillingAddress2().isEmpty())
				etBillingAdd2.setText(AppValues.driverDetails
						.getBillingAddress2());

			if (AppValues.driverDetails.getBillingCity() != null
					&& !AppValues.driverDetails.getBillingCity().isEmpty())
				etCity.setText(AppValues.driverDetails.getBillingCity());

			if (AppValues.driverDetails.getBillingPostcode() != null
					&& !AppValues.driverDetails.getBillingPostcode().isEmpty())
				etPostCode.setText(AppValues.driverDetails.getBillingPostcode()
						.substring(0, 1).toUpperCase()
						+ AppValues.driverDetails.getBillingPostcode()
								.substring(1));
			if (AppValues.driverDetails.getBillingCounty() != null
					&& !AppValues.driverDetails.getBillingCounty().isEmpty())
				etCountry.setText(AppValues.driverDetails.getBillingCounty());

			etBillingAdd1.setSelection(etBillingAdd1.getText().length());
			etBillingAdd2.setSelection(etBillingAdd2.getText().length());
			etCity.setSelection(etCity.getText().length());
			etPostCode.setSelection(etPostCode.getText().length());
			etCountry.setSelection(etCountry.getText().length());

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
			case R.id.tvSaveBillingAddress:
				SaveButtonCall();

				break;

			case R.id.tvHiddenSave:
				SaveButtonCall();

				break;

			}
		}
	}

	void SaveButtonCall() {

		Util.hideSoftKeyBoard(mContext, tvSave);
		String sBillingAdd1 = "", sBillingAdd2 = "", sBillingCity = "", sBillingPostCode = "", sBillingCountry = "";
		sBillingAdd1 = etBillingAdd1.getText().toString().trim();
		sBillingAdd2 = etBillingAdd2.getText().toString().trim();
		sBillingCity = etCity.getText().toString().trim();
		sBillingPostCode = etPostCode.getText().toString().trim();
		sBillingCountry = etCountry.getText().toString().trim();

		if (sBillingAdd1.isEmpty() // || sBillingAdd2.isEmpty()
				|| sBillingCity.isEmpty()
				|| sBillingPostCode.isEmpty()
				|| sBillingCountry.isEmpty()) {
			Util.showToastMessage(mContext, "Please complete all fields",
					Toast.LENGTH_LONG);
		} else {
			Bundle bundle = new Bundle();
			bundle.putString(Constants.BILLING_ADDRESS1, sBillingAdd1);
			bundle.putString(Constants.BILLING_ADDRESS2, sBillingAdd2);
			bundle.putString(Constants.BILLING_POSTCODE, sBillingPostCode);
			bundle.putString(Constants.BILLING_CITY, sBillingCity);
			bundle.putString(Constants.BILLING_COUNTRY, sBillingCountry);

			if (AppValues.driverDetails != null) {

				bundle.putString(Constants.WORKING_CITY,
						AppValues.driverDetails.getWorkingCity());

				bundle.putString(Constants.REGISTRATION_PASSWORD,
						Util.getPassword(mContext));

				bundle.putString(Constants.FIRSTNAME,
						AppValues.driverDetails.getFirstname());
				bundle.putString(Constants.SURNAME,
						AppValues.driverDetails.getSurname());
				bundle.putString(Constants.EMAIL_ADDRESS,
						AppValues.driverDetails.getEmail());
				bundle.putString(Constants.USERNAME,
						AppValues.driverDetails.getUsername());
				bundle.putString(Constants.BADGE_COLOUR,
						AppValues.driverDetails.getBadgeColour());
				bundle.putString(Constants.BADGE_NUMBER,
						AppValues.driverDetails.getBadgeNumber());
				bundle.putString(Constants.DRIVER_BADGE_EXPIRY,
						AppValues.driverDetails.getBadgeExpiration());
				bundle.putString(Constants.MOBILE_NUMBER,
						AppValues.driverDetails.getMobileNumber());
				bundle.putString(Constants.COUNRTY_CODE,
						AppValues.driverDetails.getInternationalCode());
				bundle.putString(Constants.PAYMENT_TYPE,
						AppValues.driverDetails.getPaymentType());
				bundle.putString(Constants.VECHILE_WHEELCHAIR_ACCESS,
						AppValues.driverDetails.getWheelchairAccess());
				bundle.putString(Constants.VECHILE_MAXIMUM_PASSENGERS,
						String.valueOf(AppValues.driverDetails
								.getPassengerCapacity()));
				bundle.putString(Constants.VECHILE_COLOUR,
						AppValues.driverDetails.getVehicle().getColour());
				bundle.putString(Constants.VECHILE_REGISTRATION_NO,
						AppValues.driverDetails.getVehicle().getRegistration());
				bundle.putString(Constants.VECHILE_MAKE,
						AppValues.driverDetails.getVehicle().getMake());
				bundle.putString(Constants.VECHILE_MODEL,
						AppValues.driverDetails.getVehicle().getModel());

				if (AppValues.driverDetails.getBankDetails() != null) {
					bundle.putString(Constants.BANK_NAME,
							AppValues.driverDetails.getBankDetails().getName());
					bundle.putString(Constants.BANK_ACCOUNT_NAME,
							AppValues.driverDetails.getBankDetails()
									.getAccountName());
					bundle.putString(Constants.BANK_ACCOUNT_NUMBER,
							AppValues.driverDetails.getBankDetails()
									.getAccountNumber());
					bundle.putString(Constants.BANK_SORT_CODE,
							AppValues.driverDetails.getBankDetails()
									.getSortCode());
					// bundle.putString(Constants.BANK_IBAN,
					// AppValues.driverDetails.getBankDetails().g);

					if (NetworkUtil.isNetworkOn(mContext)) {
						dialogBillingAdd = new ProgressDialog(
								BillingAddressActivity.this);
						dialogBillingAdd.setMessage("Loading...");
						dialogBillingAdd.setCancelable(false);
						dialogBillingAdd.show();

						EditBillingAddressTask editBillingAdd = new EditBillingAddressTask();
						editBillingAdd.bundle = bundle;
						editBillingAdd.execute();
					} else {
						Util.showToastMessage(mContext, getResources()
								.getString(R.string.no_network_error),
								Toast.LENGTH_LONG);
					}
				}

			}

		}
	}

	public class EditBillingAddressTask extends AsyncTask<String, Void, String> {
		public Bundle bundle;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			AccountModification accModification = new AccountModification(
					BillingAddressActivity.this, bundle);
			String response = accModification.ModificationCredentials();
			Log.e(TAG, "EditBillingAddressResponse response::> " + response);
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

			if (dialogBillingAdd != null)
				dialogBillingAdd.dismiss();

			if (result.equals("success")) {
				finish();
			} else if (result.equals("pending")) {
				Util.showToastMessage(mContext,
						"Your account is awaiting approval", Toast.LENGTH_LONG);
				LogInActivity.finishActivity();
				MainActivity.finishActivity();
				finish();

				Intent intent = new Intent(BillingAddressActivity.this,
						TutorialActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}

		}

	}

}
