package com.android.cabapp.activity;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.BuyCreditsConfirmActivity.BuyCreditTask;
import com.android.cabapp.async.GetBuyCreditPaymentURLTask;
import com.android.cabapp.model.BuyCredit;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class AutoTopUpConfirmActivity extends RootActivity {

	private static final String TAG = AutoTopUpConfirmActivity.class
			.getSimpleName();

	TextView tvEditConfirm, tvOrderDetails, textCardType, textNoOfCredits,
			txtPricePerCredit, textAutoTopUpStatus, textTotal;
	RelativeLayout rlEditConfirm;

	static Context _context;
	Bundle mConfirmBundle;
	float totalValue = 0, fPricePerCredit = 0;
	int nNoOfCredits = 0, totalNooOfCreditsAfterSuccess = 0;

	String szTruncatedPan = "", szCardType = "", szCardPayworkToken = "";

	ProgressDialog progressDialog;
	public static Handler mAutoTopUpConfirmPopUpHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autotopup_confirmandedit);
		super.initWidgets();
		_context = this;
		mConfirmBundle = getIntent().getExtras();

		tvOrderDetails = (TextView) findViewById(R.id.textOrderDetails);
		tvEditConfirm = (TextView) findViewById(R.id.tvEditConfirm);
		rlEditConfirm = (RelativeLayout) findViewById(R.id.rlAutoTopUpEditConfirm);
		tvEditConfirm.setText("Confirm");
		tvOrderDetails.setText("Order Details");
		textCardType = (TextView) findViewById(R.id.textPaymentMethodData);
		textNoOfCredits = (TextView) findViewById(R.id.textCreditsData);
		txtPricePerCredit = (TextView) findViewById(R.id.textPricePerCreditData);
		textAutoTopUpStatus = (TextView) findViewById(R.id.textAutoTopUpData);
		textTotal = (TextView) findViewById(R.id.textTotalData);
		rlEditConfirm.setOnClickListener(new ViewOnClickListener());

		if (AppValues.driverSettings != null && mConfirmBundle != null) {

			textCardType
					.setText(mConfirmBundle
							.getString(Constants.AUTOTOPUP_SELECTED_CARDBRAND)
							+ "-"
							+ mConfirmBundle
									.getString(Constants.AUTOTOPUP_SELECTED_CARD_TRUNCATEDPAN));

			totalValue = mConfirmBundle
					.getFloat(Constants.AUTOTOPUP_TOTAL_AMOUNT);
			nNoOfCredits = (mConfirmBundle
					.getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT));
			textNoOfCredits.setText(String.valueOf(mConfirmBundle
					.getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT)));
			fPricePerCredit = Float.valueOf(AppValues.driverSettings
					.getCostPerCredit());
			txtPricePerCredit.setText(AppValues.driverSettings
					.getCurrencySymbol()
					+ ""
					+ String.format(Locale.ENGLISH, "%.2f", fPricePerCredit));
			textTotal.setText(AppValues.driverSettings.getCurrencySymbol()
					+ ""
					+ String.format(Locale.ENGLISH, "%.2f", mConfirmBundle
							.getFloat(Constants.AUTOTOPUP_TOTAL_AMOUNT)));

			if (AppValues.driverSettings.getIsAutoTopup() == 1)
				textAutoTopUpStatus.setText("Yes");
			else
				textAutoTopUpStatus.setText("No");

			szTruncatedPan = mConfirmBundle
					.getString(Constants.AUTOTOPUP_SELECTED_CARD_TRUNCATEDPAN);
			szCardType = mConfirmBundle
					.getString(Constants.AUTOTOPUP_SELECTED_CARDBRAND);
			szCardPayworkToken = mConfirmBundle
					.getString(Constants.AUTOTOPUP_CARD_PAYWORKTOKEN);

		}

		mAutoTopUpConfirmPopUpHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				if (message.what == 100) {
					ConfirmationPopUp();
				}
			}
		};

	}

	class ViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.rlAutoTopUpEditConfirm:
				ProceedWithAutoTopUpPayment();
				break;

			}
		}
	}

	void ProceedWithAutoTopUpPayment() {
		if (NetworkUtil.isNetworkOn(Util.mContext)) {
			progressDialog = new ProgressDialog(_context);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.show();

			AutoTopUpTask autoTopUpTask = new AutoTopUpTask();
			autoTopUpTask.numberOfCredits = nNoOfCredits;
			autoTopUpTask.szCostPerCredit = fPricePerCredit;
			autoTopUpTask.szTruncatedPan = szTruncatedPan;
			autoTopUpTask.szCardType = szCardType;
			autoTopUpTask.szPayWorkToken = szCardPayworkToken;
			if (AppValues.driverSettings.getIsAutoTopup() == 1)
				autoTopUpTask.isAutoTopUp = true;
			else
				autoTopUpTask.isAutoTopUp = false;
			autoTopUpTask.execute();
		} else {
			Util.showToastMessage(_context,
					getResources().getString(R.string.no_network_error),
					Toast.LENGTH_LONG);
		}
	}

	/* Auto top up: Old Proceed with payment */
	void ProceedWithPayment() {
		float cardFeesAmount = 0, nPricePerCredit = 0;
		float cardPaymentPercentageFee = 0;
		String truncatedPan = mConfirmBundle
				.getString(Constants.AUTOTOPUP_SELECTED_CARD_TRUNCATEDPAN);

		int isAutoTopUp = 0;
		if (AppValues.driverSettings.getIsAutoTopup() != null) {
			isAutoTopUp = AppValues.driverSettings.getIsAutoTopup();
		}

		if (AppValues.driverSettings != null) {
			cardPaymentPercentageFee = Float.valueOf(AppValues.driverSettings
					.getCardPaymentPercentageFee());
			float tipPercentage = (float) 0.0399;

			if (AppValues.driverSettings != null)
				tipPercentage = Float.parseFloat(AppValues.driverSettings
						.getCardPaymentPercentageFee()) / 100;
			// cardFeesAmount = Float.parseFloat(String.format(Locale.ENGLISH,
			// "%.2f", (float) (tipPercentage * totalValue)));

			nPricePerCredit = (float) (Float
					.parseFloat(AppValues.driverSettings.getCostPerCredit()));// *
																				// 100

		}

		final String szPaymentURL = Constants.driverURL
				+ "buycredit?accessToken=" + Constants.accessToken
				+ "&credits=" + textNoOfCredits.getText().toString()
				+ "&pricepercredit=" + nPricePerCredit + "&autotopup="
				+ isAutoTopUp + "&fee=" + (int) (cardFeesAmount)
				+ "&truncatedPan=" + truncatedPan;

		if (Constants.isDebug)
			Log.e(TAG, "szPaymentURL: " + szPaymentURL);

		if (NetworkUtil.isNetworkOn(Util.mContext)) {
			// TODO Auto-generated method stub
			((Activity) Util.mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					GetBuyCreditPaymentURLTask getBuyCreditPaymentTask = new GetBuyCreditPaymentURLTask();
					getBuyCreditPaymentTask.szURL = szPaymentURL;
					getBuyCreditPaymentTask.mContext = _context;
					getBuyCreditPaymentTask.execute();
				}
			});
		} else {
			Util.showToastMessage(_context,
					getResources().getString(R.string.no_network_error),
					Toast.LENGTH_LONG);
		}

	}

	public class AutoTopUpTask extends AsyncTask<String, Void, String> {
		int numberOfCredits;
		float szCostPerCredit;
		String szTruncatedPan = "", szCardType = "", szPayWorkToken = "";
		boolean isAutoTopUp;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			BuyCredit buyCredits = new BuyCredit(_context, numberOfCredits,
					isAutoTopUp, szTruncatedPan, szCardType, szCostPerCredit,
					szPayWorkToken);
			String response = buyCredits.BuyCreditCall();

			if (Constants.isDebug)
				Log.e(TAG, "Auto Top up response::> " + response);

			try {
				JSONObject jObject = new JSONObject(response);
				String errorMessage = "";
				if (jObject.has("response")
						&& jObject.getString("response").equals("success")) {
					/* Auto top up: Success response */
					// {"response":"success","totalNumberOfCredits":125}

					if (jObject.has("totalNumberOfCredits"))
						totalNooOfCreditsAfterSuccess = Integer.valueOf(jObject
								.getString("totalNumberOfCredits"));

					return "success";
				} else if (jObject.has("response")
						&& jObject.getString("response").equals("false")) {
					errorMessage = jObject.getString("error");
					return errorMessage;
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

			return response;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}

			if (result.equals("success")) {
				ConfirmationPopUp();
			} else {
				Util.showToastMessage(_context, result, Toast.LENGTH_LONG);
			}

		}

	}

	void ConfirmationPopUp() {

		final Dialog dialog = new Dialog(_context, R.style.mydialogstyle);
		dialog.setContentView(R.layout.dialog_payment_confirmation);
		dialog.setCanceledOnTouchOutside(false);
		TextView tvCreditAdded = (TextView) dialog
				.findViewById(R.id.tvCreditAdded);
		TextView tvNoOfCredits = (TextView) dialog
				.findViewById(R.id.tvNoOfCredits);

		tvCreditAdded.setText("You have added " + nNoOfCredits
				+ " credits to your account.");
		tvNoOfCredits.setText("" + totalNooOfCreditsAfterSuccess + " Credits");
		dialog.setCancelable(false);
		dialog.show();

		RelativeLayout rlclose = (RelativeLayout) dialog
				.findViewById(R.id.rlClose);
		rlclose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();

				AutoTopUpCardActivity.finishActivity();
				AutoTopUpEditActivity.finishActivity();
				AutoTopUpConfirmActivity.finishActivity();
				SetUpAutoTopUpActivity.finishActivity();

			}
		});
	}

	public static void finishActivity() {

		if (_context != null)
			((Activity) _context).finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mAutoTopUpConfirmPopUpHandler = null;
	}

}
