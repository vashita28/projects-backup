/**
 * Created: by vashita on 24/dec/2014
 */
package com.android.cabapp.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.model.GetPaymentStatus;
import com.android.cabapp.model.PaymentWithURL;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class PaymentWithUrlActivity extends RootActivity {
	String TAG = PaymentWithUrlActivity.class.getName();

	private static boolean isEmailSelected = true, isSMSselected = false;
	RelativeLayout rlSendUrl, rlResendLayout, rlWalkUpLayout, rlResend, rlConfirm, rlEmail, rlSMS;
	LinearLayout llReceiptTypeRadiobutton, llReceiptTypeForWalkUp;
	RadioButton radioEmail, radioSms;
	TextView tvSendUrl, textEmail, textSMS;
	EditText etEmailOrNumber;
	ImageView ivBack;
	// ProgressBar pProgressBar;
	Bundle mBundle;
	Context _context;
	ProgressDialog progressDialog;
	String sMeterValue = "", sTipValue = "", sCardFees = "", sTotalAmt = "", sJobId = "", sReceiptType = "", sMobileNo = "",
			sInternationalNo = "", sEmail = "", sPickUp = "", sDropOff = "", currency = "", currencyCode = "";
	String szEmailPhoneNumber = "";
	public static String szReturnedBookingId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_with_url);

		_context = this;
		mBundle = getIntent().getExtras();

		ivBack = (ImageView) findViewById(R.id.ivBack);
		// pProgressBar = (ProgressBar) findViewById(R.id.progressBarSendURL);
		rlSendUrl = (RelativeLayout) findViewById(R.id.rlSendUrl);
		rlResendLayout = (RelativeLayout) findViewById(R.id.rlResendLayout);
		rlWalkUpLayout = (RelativeLayout) findViewById(R.id.rlContent);
		rlResend = (RelativeLayout) findViewById(R.id.rlResend);
		rlConfirm = (RelativeLayout) findViewById(R.id.rlConfirm);
		tvSendUrl = (TextView) findViewById(R.id.tvSendUrl);
		rlEmail = (RelativeLayout) findViewById(R.id.rlEmail);
		rlSMS = (RelativeLayout) findViewById(R.id.rlSMS);
		textEmail = (TextView) findViewById(R.id.textEmail);
		textSMS = (TextView) findViewById(R.id.textSMS);
		etEmailOrNumber = (EditText) findViewById(R.id.etEmailOrCellNumber);
		llReceiptTypeRadiobutton = (LinearLayout) findViewById(R.id.llReceiptTyperadioBtns);
		radioEmail = (RadioButton) findViewById(R.id.radioBtnEmail);
		radioSms = (RadioButton) findViewById(R.id.radioBtnSms);
		llReceiptTypeForWalkUp = (LinearLayout) findViewById(R.id.llReceiptType);
		rlEmail.setOnClickListener(new TextOnClickListener());
		rlSMS.setOnClickListener(new TextOnClickListener());
		rlSendUrl.setOnClickListener(new TextOnClickListener());
		ivBack.setOnClickListener(new TextOnClickListener());
		rlResend.setOnClickListener(new TextOnClickListener());
		rlConfirm.setOnClickListener(new TextOnClickListener());
		radioEmail.setOnClickListener(new TextOnClickListener());
		radioSms.setOnClickListener(new TextOnClickListener());

		etEmailOrNumber.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mBundle != null && mBundle.containsKey(Constants.JOB_ID) && mBundle.getString(Constants.JOB_ID) != null
				&& !mBundle.getString(Constants.JOB_ID).isEmpty()) {
			sJobId = mBundle.getString(Constants.JOB_ID);
		}
		if (!sJobId.isEmpty()) {// for hail jobs
			// llReceiptTypeRadiobutton.setVisibility(View.VISIBLE);
			llReceiptTypeForWalkUp.setVisibility(View.VISIBLE);
			etEmailOrNumber.setVisibility(View.VISIBLE);

			if (mBundle != null) {
				sMobileNo = mBundle.getString(Constants.PASSENGER_NUMBER);
				sInternationalNo = mBundle.getString(Constants.PASSENGER_INTERNATIONAL_CODE);
				sEmail = mBundle.getString(Constants.PASSENGER_EMAIL);
				sPickUp = mBundle.getString(Constants.PICK_UP_ADDRESS);
				sDropOff = mBundle.getString(Constants.DROP_ADDRESS);
				etEmailOrNumber.setText(sEmail);
				etEmailOrNumber.setSelection(etEmailOrNumber.getText().length());
			}
		}
		/*
		 * else { llReceiptTypeRadiobutton.setVisibility(View.GONE);
		 * llReceiptTypeForWalkUp.setVisibility(View.VISIBLE); }
		 */
	}

	class TextOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.rlSendUrl:
				ivBack.setVisibility(View.GONE);
				Util.hideSoftKeyBoard(_context, tvSendUrl);
				if (sJobId.isEmpty()) {// walk up validation
					walkupPayment();
				} else {// hail jobs
					sReceiptType = "";
					szEmailPhoneNumber = etEmailOrNumber.getText().toString().trim();
					if (isEmailSelected) {
						sReceiptType = "email";
						sEmail = szEmailPhoneNumber;
					} else if (isSMSselected) {
						sReceiptType = "sms";
						sMobileNo = szEmailPhoneNumber;
					} else {
						sReceiptType = "none";
					}
					ProceedWithSendURL();
				}

				break;

			case R.id.ivBack:
				finish();
				break;

			case R.id.rlEmail:
				isEmailSelected = true;
				isSMSselected = false;
				etEmailOrNumber.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
				toogleReceiptMethod(textEmail);
				if (sEmail != null && !sEmail.isEmpty()) {
					etEmailOrNumber.setText(sEmail);
					etEmailOrNumber.setSelection(etEmailOrNumber.getText().length());
				}
				break;

			case R.id.rlSMS:
				isEmailSelected = false;
				isSMSselected = true;
				etEmailOrNumber.setInputType(InputType.TYPE_CLASS_PHONE);
				toogleReceiptMethod(textSMS);
				if (sMobileNo != null && !sMobileNo.isEmpty()) {
					etEmailOrNumber.setSelection(etEmailOrNumber.getText().length());
					etEmailOrNumber.setText(sMobileNo);
				}
				break;

			case R.id.rlResend:
				ivBack.setVisibility(View.VISIBLE);
				rlWalkUpLayout.setVisibility(View.VISIBLE);
				rlResendLayout.setVisibility(View.GONE);
				sJobId = szReturnedBookingId;
				break;

			case R.id.rlConfirm:
				GetPaymentStatusTask getPaymentStatusTask = new GetPaymentStatusTask();
				getPaymentStatusTask.execute();

				break;

			case R.id.radioBtnEmail:
				isEmailSelected = true;
				isSMSselected = false;
				break;

			case R.id.radioBtnSms:
				isEmailSelected = false;
				isSMSselected = true;
				break;

			}
		}
	}

	void toogleReceiptMethod(TextView textViewSelected) {
		textEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email_grey, 0, 0, 0);
		textSMS.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sms_grey, 0, 0, 0);

		textEmail.setTextColor(getResources().getColor(R.color.textcolor_grey));
		textSMS.setTextColor(getResources().getColor(R.color.textcolor_grey));

		if (textViewSelected == textEmail)
			textViewSelected.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
		else if (textViewSelected == textSMS)
			textViewSelected.setCompoundDrawablesWithIntrinsicBounds(R.drawable.sms, 0, 0, 0);

		textViewSelected.setTextColor(getResources().getColor(R.color.textview_selected));

	}

	void walkupPayment() {
		szEmailPhoneNumber = etEmailOrNumber.getText().toString().trim();
		if (szEmailPhoneNumber.isEmpty()) {
			if (isEmailSelected) {
				Util.showToastMessage(Util.mContext, "Please enter email address", Toast.LENGTH_LONG);
			} else if (isSMSselected) {
				Util.showToastMessage(Util.mContext, "Please enter mobile number for SMS", Toast.LENGTH_LONG);
			}
		} else {
			if (isEmailSelected && !szEmailPhoneNumber.matches(Constants.EMAIL_PATTERN)) {
				Util.showToastMessage(Util.mContext, "Please enter a valid email address", Toast.LENGTH_LONG);
			} else if (isSMSselected && szEmailPhoneNumber.length() < 10) {
				Util.showToastMessage(Util.mContext, "Please enter a valid mobile number", Toast.LENGTH_LONG);
			} else {
				sReceiptType = "";
				if (isEmailSelected) {
					sReceiptType = "email";
				} else if (isSMSselected) {
					sReceiptType = "sms";
				} else {
					sReceiptType = "none";
				}
				if (mBundle != null && mBundle.containsKey(Constants.JOB_ID) && mBundle.getString(Constants.JOB_ID) != null
						&& !mBundle.getString(Constants.JOB_ID).isEmpty()) {
					sJobId = mBundle.getString(Constants.JOB_ID);
				}

				if (sJobId.isEmpty()) {// for walk up jobs
					if (isEmailSelected) {
						sMobileNo = "";
						sInternationalNo = "";
						sEmail = szEmailPhoneNumber;
					} else if (isSMSselected) {
						sMobileNo = szEmailPhoneNumber;
						if (AppValues.driverDetails != null && AppValues.driverDetails.getInternationalCode() != null)
							sInternationalNo = AppValues.driverDetails.getInternationalCode();
						sEmail = "";
					}
					sPickUp = "";
					sDropOff = "";
				}
				ProceedWithSendURL();
			}
		}

	}

	void ProceedWithSendURL() {

		sMeterValue = mBundle.getString(Constants.METER_VALUE);
		sTipValue = mBundle.getString(Constants.TIP_VALUE);
		sCardFees = mBundle.getString(Constants.CARD_FEE_VALUE);
		sTotalAmt = mBundle.getString(Constants.TOTAL_VALUE);

		if (NetworkUtil.isNetworkOn(Util.mContext)) {

			rlWalkUpLayout.setVisibility(View.GONE);
			rlResendLayout.setVisibility(View.VISIBLE);

			SendURLforPaymentTask sendURLTask = new SendURLforPaymentTask();
			sendURLTask.szMeterValue = sMeterValue;
			sendURLTask.szTipValue = sTipValue;
			sendURLTask.szCardFees = sCardFees;
			sendURLTask.szTotalAmt = sTotalAmt;
			sendURLTask.szJobId = sJobId;
			sendURLTask.szReceiptType = sReceiptType;
			sendURLTask.szMobileNumber = sMobileNo;
			sendURLTask.szInternationalCode = sInternationalNo;
			sendURLTask.szEmail = sEmail;
			sendURLTask.szPickUpAddress = sPickUp;
			sendURLTask.szDropOffAddress = sDropOff;
			sendURLTask.currency = AppValues.driverSettings.getCurrencySymbol();
			sendURLTask.currencyCode = AppValues.driverSettings.getCurrencyCode();
			sendURLTask.execute();
		} else {
			Util.showToastMessage(_context, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}
	}

	public class SendURLforPaymentTask extends AsyncTask<String, Void, String> {
		int numberOfCredits;
		float szCostPerCredit;
		String szMeterValue = "", szTipValue = "", szCardFees = "", szTotalAmt = "", szJobId = "", szReceiptType = "",
				szMobileNumber = "", szInternationalCode = "", szEmail = "", szPickUpAddress = "", szDropOffAddress = "",
				currency = "", currencyCode = "";
		boolean isAutoTopUp;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (Constants.isDebug)
				Log.e(TAG, "szEmail: " + szEmail + "     szMobileNumber:   " + szMobileNumber);
			PaymentWithURL sendPaymentUrl = new PaymentWithURL(_context, szMeterValue, szTipValue, szCardFees, szTotalAmt,
					szJobId, szReceiptType, szMobileNumber, szInternationalCode, szEmail, szPickUpAddress, szDropOffAddress,
					currency, currencyCode);
			String response = sendPaymentUrl.SendPaymentUrlToCustomer();

			if (Constants.isDebug)
				Log.e(TAG, "SendURLforPaymentTask response::> " + response);

			try {
				JSONObject jObject = new JSONObject(response);
				String errorMessage = "";
				if (jObject.has("response") && jObject.getString("response").equals("success")) {

					/*
					 * update cash back value and booking id
					 */
					szReturnedBookingId = jObject.getString("bookingId");
					Util.setCashBack(_context, jObject.getString("cashbackValue"));
					return "success";

				} else if (jObject.has("response") && jObject.getString("response").equals("false")) {
					errorMessage = jObject.getString("error");
					return errorMessage;
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						errorMessage = jErrorsArray.getJSONObject(0).getString("message");
						return errorMessage;
					}
				}
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

			if (result.equals("success")) {
				Util.showToastMessage(_context, "Link sent successfully", Toast.LENGTH_LONG);

			} else {
				Util.showToastMessage(_context, "Error occured, Please try again", Toast.LENGTH_LONG);
			}

		}

	}

	public class GetPaymentStatusTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			GetPaymentStatus getPaymentStatus = null;
			Log.e(TAG, "job id to check payment for:  " + szReturnedBookingId);
			if (szReturnedBookingId != null && !szReturnedBookingId.isEmpty()) {
				getPaymentStatus = new GetPaymentStatus(_context, szReturnedBookingId);
			} else {
				getPaymentStatus = new GetPaymentStatus(_context, sJobId);
			}
			String response = getPaymentStatus.getPaymentStatus();

			if (Constants.isDebug)
				Log.e(TAG, "GetPaymentStatusTask response::> " + response);

			try {
				JSONObject jObject = new JSONObject(response);
				if (jObject.has("success") && jObject.getString("success").equals("true")) {
					return "true";
				} else if (jObject.has("success") && jObject.getString("success").equals("false")) {
					return "false";
				}
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

			// etEmailOrNumber.setText("");
			if (result.equals("true")) {
				Util.showToastMessage(_context, "Payment done successfully", Toast.LENGTH_LONG);
				/* on success redirect to jobs screen */
				finish();
				Fragment fragment = null;
				fragment = new com.android.cabapp.fragments.JobsFragment();
				((MainActivity) Util.mContext).setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
				if (fragment != null)
					((MainActivity) Util.mContext).replaceFragment(fragment, true);
			} else if (result.equals("false")) {
				Util.showToastMessage(_context, "Your payment has not received", Toast.LENGTH_LONG);
			} else {
				Util.showToastMessage(_context, "Error occured, Please try again", Toast.LENGTH_LONG);
			}

		}

	}

}
