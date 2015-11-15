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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.async.GetBuyCreditPaymentURLTask;
import com.android.cabapp.fragments.BuyAddCreditsFragment;
import com.android.cabapp.model.BuyCredit;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class BuyCreditsConfirmActivity extends RootActivity {

	private static final String TAG = BuyCreditsConfirmActivity.class.getSimpleName();

	ImageView ivCardType;
	int nNoOfCredits = 0;
	public static int totalNooOfCreditsAfterSuccess = 0;
	float fPricePerCredit = 0, fTotalPrice = 0;
	String szTruncatedPan = "", szCardType = "", szCardPayworkToken = "";

	TextView textPaymentMethod, textNoOfCredits, textPricePerCredits, textTotal;
	RelativeLayout rlbottombarConfirm;

	static Context _context;
	Bundle mBundle;

	ProgressDialog progressDialog;
	public static Handler mBuyCreditsConfirmPopUpHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buycreditsconfirm);
		super.initWidgets();
		_context = this;

		mBundle = getIntent().getExtras();

		ivCardType = (ImageView) findViewById(R.id.ivCard);
		textPaymentMethod = (TextView) findViewById(R.id.textPaymentMethodData);
		textNoOfCredits = (TextView) findViewById(R.id.textCreditsData);
		textPricePerCredits = (TextView) findViewById(R.id.textPricePerCreditData);
		textTotal = (TextView) findViewById(R.id.textTotalData);
		rlbottombarConfirm = (RelativeLayout) findViewById(R.id.rlbottombarConfirm);
		rlbottombarConfirm.setOnClickListener(new TextViewOnClickListener());

		if (mBundle != null) {

			szTruncatedPan = mBundle.getString(Constants.BUYCREDITS_CARD_TRUNCATEDPAN);
			szCardType = mBundle.getString(Constants.BUYCREDITS_CARD_BRAND);
			szCardPayworkToken = mBundle.getString(Constants.BUYCREDITS_CARD_PAYWORKTOKEN);

			if (Util.cardsCodeHashMap.get(mBundle.getString(Constants.BUYCREDITS_CARD_BRAND).toLowerCase()) != null)
				ivCardType.setBackgroundResource(Util.cardsCodeHashMap.get(mBundle.getString(Constants.BUYCREDITS_CARD_BRAND)
						.toLowerCase()));
			else
				ivCardType.setBackgroundResource(0);

			textPaymentMethod.setText("**** **** **** " + mBundle.getString(Constants.BUYCREDITS_CARD_TRUNCATEDPAN));

			nNoOfCredits = mBundle.getInt(Constants.BUYCREDITS_NOOFCREDITS);
			fPricePerCredit = mBundle.getFloat(Constants.BUYCREDITS_COSTPERCREDIT);
			fTotalPrice = mBundle.getFloat(Constants.BUYCREDITS_TOTLCOST);
			if (AppValues.driverSettings != null) {
				textNoOfCredits.setText(String.valueOf(nNoOfCredits));
				textPricePerCredits.setText(AppValues.driverSettings.getCurrencySymbol() + ""
						+ String.format(Locale.ENGLISH, "%.2f", fPricePerCredit));
				textTotal.setText(AppValues.driverSettings.getCurrencySymbol() + ""
						+ String.format(Locale.ENGLISH, "%.2f", fTotalPrice));
			}
		}

		mBuyCreditsConfirmPopUpHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				if (message.what == 100) {
					ConfirmButtonCall();
				}
			}
		};

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mBuyCreditsConfirmPopUpHandler = null;
	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.rlbottombarConfirm:

				ProceedWithBuyCreditsPayment();
				break;

			}
		}
	}

	void ProceedWithBuyCreditsPayment() {
		if (NetworkUtil.isNetworkOn(Util.mContext)) {
			progressDialog = new ProgressDialog(_context);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.show();

			BuyCreditTask buyCreditTask = new BuyCreditTask();
			buyCreditTask.numberOfCredits = nNoOfCredits;
			buyCreditTask.szCostPerCredit = fPricePerCredit;
			buyCreditTask.szTruncatedPan = szTruncatedPan;
			buyCreditTask.szCardType = szCardType;
			buyCreditTask.szPayWorkToken = szCardPayworkToken;
			if (AppValues.driverSettings.getIsAutoTopup() == 1)
				buyCreditTask.isAutoTopUp = true;
			else
				buyCreditTask.isAutoTopUp = false;
			buyCreditTask.execute();
		} else {
			Util.showToastMessage(_context, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}
	}

	void ConfirmButtonCall() {

		final Dialog dialog = new Dialog(_context, R.style.mydialogstyle);
		dialog.setContentView(R.layout.dialog_payment_confirmation);
		dialog.setCanceledOnTouchOutside(false);
		TextView tvCreditAdded = (TextView) dialog.findViewById(R.id.tvCreditAdded);
		TextView tvNoOfCredits = (TextView) dialog.findViewById(R.id.tvNoOfCredits);

		tvCreditAdded.setText("You have added " + textTotal.getText().toString() + " to your account.");
		String totalNoOfCredits = String.format(Locale.ENGLISH, "%.2f", fPricePerCredit * totalNooOfCreditsAfterSuccess);
		tvNoOfCredits.setText(AppValues.driverSettings.getCurrencySymbol() + "" + totalNoOfCredits);
		dialog.setCancelable(false);
		dialog.show();

		RelativeLayout rlclose = (RelativeLayout) dialog.findViewById(R.id.rlClose);
		rlclose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();

				BuyCreditsConfirmActivity.finishActivity();
				BuyCreditsCardActivity.finishActivity();

				if (BuyAddCreditsFragment.mHandler != null)
					BuyAddCreditsFragment.mHandler.sendEmptyMessage(100);

			}
		});
	}

	/* BuyCredit: Old payment method */
	void ProceedWithPayment() {
		float cardFeesAmount = 0, nPricePerCredit = 0;
		float cardPaymentPercentageFee = 0;
		String truncatedPan = mBundle.getString(Constants.BUYCREDITS_CARD_TRUNCATEDPAN);

		int isAutoTopUp = 0;
		if (AppValues.driverSettings.getIsAutoTopup() != null) {
			isAutoTopUp = AppValues.driverSettings.getIsAutoTopup();
		}

		if (AppValues.driverSettings != null) {
			// cardPaymentPercentageFee = Float.valueOf(AppValues.driverSettings
			// .getCardPaymentPercentageFee());
			float tipPercentage = (float) 0.0399;

			if (AppValues.driverSettings != null)
				tipPercentage = Float.parseFloat(AppValues.driverSettings.getCardPaymentPercentageFee()) / 100;
			cardFeesAmount = Float.parseFloat(String.format(Locale.ENGLISH, "%.2f", (float) (tipPercentage * fTotalPrice)));
			if (Constants.isDebug)
				Log.e(TAG, "totalValue: " + fTotalPrice + " getCardPaymentPercentageFee:   " + cardPaymentPercentageFee
						+ " cardFeesAmount " + cardFeesAmount);
			nPricePerCredit = (float) (Float.parseFloat(AppValues.driverSettings.getCostPerCredit()));// *100

		}

		final String szPaymentURL = Constants.driverURL + "buycredit?accessToken=" + Constants.accessToken + "&credits="
				+ textNoOfCredits.getText().toString() + "&pricepercredit=" + nPricePerCredit + "&autotopup=" + isAutoTopUp
				+ "&fee=" + (int) (cardFeesAmount)// * 100
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
			Util.showToastMessage(_context, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}

	}

	public static void finishActivity() {

		if (_context != null)
			((Activity) _context).finish();
	}

	public class BuyCreditTask extends AsyncTask<String, Void, String> {
		int numberOfCredits;
		float szCostPerCredit;
		String szTruncatedPan = "", szCardType = "", szPayWorkToken = "";
		boolean isAutoTopUp;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			BuyCredit buyCredits = new BuyCredit(_context, numberOfCredits, isAutoTopUp, szTruncatedPan, szCardType,
					szCostPerCredit, szPayWorkToken);
			String response = buyCredits.BuyCreditCall();

			if (Constants.isDebug)
				Log.e(TAG, "BuyCredits response::> " + response);

			try {
				JSONObject jObject = new JSONObject(response);
				String errorMessage = "";
				if (jObject.has("response") && jObject.getString("response").equals("success")) {
					/* Buy credit: Success response */
					// {"response":"success","totalNumberOfCredits":80}

					if (jObject.has("totalNumberOfCredits"))
						totalNooOfCreditsAfterSuccess = Integer.valueOf(jObject.getString("totalNumberOfCredits"));

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

			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}

			if (result.equals("success")) {
				if (mBuyCreditsConfirmPopUpHandler != null)
					mBuyCreditsConfirmPopUpHandler.sendEmptyMessage(100);
			} else {
				Util.showToastMessage(_context, result, Toast.LENGTH_LONG);
			}

		}

	}

}
