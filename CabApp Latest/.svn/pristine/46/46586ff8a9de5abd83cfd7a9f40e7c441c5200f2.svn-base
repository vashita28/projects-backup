package com.android.cabapp.fragments;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.cabapp.R;
import com.android.cabapp.activity.AutoTopUpCardActivity;
import com.android.cabapp.activity.AutoTopUpConfirmActivity;
import com.android.cabapp.activity.AutoTopUpEditActivity;
import com.android.cabapp.activity.BuyCreditsCardActivity;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.activity.SetUpAutoTopUpActivity;
import com.android.cabapp.activity.SignUp_Payment_Activity;
import com.android.cabapp.async.GetPaymentURLTask;
import com.android.cabapp.model.AutoTopUpStatus;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class BuyAddCreditsFragment extends RootFragment {
	private static final String TAG = BuyAddCreditsFragment.class.getSimpleName();

	TextView txtMinSpendAmt, txtNoOfCredits, txtTotal, textMinimumCreditReached, tvCredits, tvCostForNowJob,
			tvCostForFixedPriceJob;
	SeekBar seekCreditsbar;
	RelativeLayout rlContinue;
	int nNoOfCredits = 0;
	float minCreditPurchase = 0;
	float totalValue = 0, cardFeesAmount = 0, costPerCredit = 0;
	boolean bIsDialogShown = false;
	RelativeLayout rlAutoTopUp;
	ToggleButton toggleAutoTopUp;
	boolean isAutoTopUpOn;

	public static Handler mBuyCreditsHandler;
	public static Handler mHandler;

	private ProgressDialog dialogBuyCredit;

	Context mContext;

	int nPricePerCredit = 0, nMinProgress = 0;

	public BuyAddCreditsFragment() {
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mContext = Util.mContext;

		mBuyCreditsHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 0 && !bIsDialogShown) {
					bIsDialogShown = true;
					showDialog("Credits successfully purchased");

					if (MainActivity.creditsRefreshHandler != null)
						MainActivity.creditsRefreshHandler.sendEmptyMessage(0);
				}
			}

		};

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);
				if (message.what == 100) {
					Fragment fragmentMyAccount = new com.android.cabapp.fragments.MyAccountFragment();
					if (fragmentMyAccount != null) {
						((MainActivity) Util.mContext).replaceFragment(fragmentMyAccount, true);
					}
				}

			}
		};

		if (MainActivity.slidingMenu != null)
			MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// getActivity().getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
		// | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		View rootView = inflater.inflate(R.layout.fragment_buy_add_credits, container, false);
		super.initWidgets(rootView, this.getClass().getName());

		txtMinSpendAmt = (TextView) rootView.findViewById(R.id.tvMinimumSpendAmt);
		tvCredits = (TextView) rootView.findViewById(R.id.tvCredits);
		tvCostForNowJob = (TextView) rootView.findViewById(R.id.tvCostForNowJob);
		tvCostForFixedPriceJob = (TextView) rootView.findViewById(R.id.tvCostForFixedPriceJob);

		rlContinue = (RelativeLayout) rootView.findViewById(R.id.rlBuyCredits);
		txtNoOfCredits = (TextView) rootView.findViewById(R.id.tvCreditsValue);
		txtTotal = (TextView) rootView.findViewById(R.id.tvTotal);
		// txtTotalAmt = (TextView) rootView.findViewById(R.id.tvTotalAmt);
		// txtPerCredit = (TextView)
		// rootView.findViewById(R.id.tvCostPerCredit);
		seekCreditsbar = (SeekBar) rootView.findViewById(R.id.creditsbar);
		rlAutoTopUp = (RelativeLayout) rootView.findViewById(R.id.rlAutoTopUp);
		toggleAutoTopUp = (ToggleButton) rootView.findViewById(R.id.toggleAutoTopUp);
		textMinimumCreditReached = (TextView) rootView.findViewById(R.id.txtWhenYourCredits);
		if (AppValues.driverSettings != null) {
			// seekCreditsbar.setProgress(Integer
			// .parseInt(AppValues.driverSettings
			// .getMinimumCreditPurchase()));
		}
		// updateTotalCost(nMinProgress);// seekCreditsbar.getProgress()
		seekCreditsbar.setMax(100 - nMinProgress);

		String totalText = "<font color=#fd6f01>= </font> <font color=#f5f5f5> Total</font>";
		txtTotal.setText(Html.fromHtml(totalText));

		isAutoTopUpOn = Util.getAutoTopUp(getActivity());
		if (isAutoTopUpOn)
			toggleAutoTopUp.setChecked(true);
		else
			toggleAutoTopUp.setChecked(false);

		rlContinue.setOnClickListener(new ViewOnClickListener());
		rlAutoTopUp.setOnClickListener(new ViewOnClickListener());

		if (AppValues.driverSettings != null) {

			costPerCredit = Float.parseFloat(AppValues.driverSettings.getCostPerCredit());

			nMinProgress = Integer.parseInt(AppValues.driverSettings.getMinimumCreditPurchase());
			Float sliderCreditValue = Float.parseFloat(String.valueOf(seekCreditsbar.getProgress() + nMinProgress));
			txtNoOfCredits.setText(String.valueOf(String.format("%.2f", sliderCreditValue * costPerCredit)));

			if (AppValues.driverSettings.getAutoTopupMinimum() != null)
				textMinimumCreditReached.setText("when your credits reach " + AppValues.driverSettings.getAutoTopupMinimum());

			minCreditPurchase = Float.parseFloat(AppValues.driverSettings.getMinimumCreditPurchase()) * costPerCredit;
			String szMinSpendValue = AppValues.driverSettings.getCurrencySymbol() + " "
					+ (String.format("%.2f", minCreditPurchase));
			String szMinSpendText = " <font color=#f5f5f5>There is a minimum spend of </font><font color=#fd6f01>"
					+ szMinSpendValue + "</font>";
			txtMinSpendAmt.setText(Html.fromHtml(szMinSpendText));
			tvCredits.setText(AppValues.driverSettings.getCurrencySymbol());
			// String szPerCredit =
			// "<font color=#000000> @ </font><font color=#fd6f01>"
			// + AppValues.driverSettings.getCurrencySymbol() + " "
			// + String.format("%.2f",
			// Float.valueOf(AppValues.driverSettings.getCostPerCredit()))
			// + " </font> <font color=#000000> per credit</font>";
			// txtPerCredit.setText(Html.fromHtml(szPerCredit));

			String costForBooking = String.valueOf(String.format("%.2f",
					Float.parseFloat(AppValues.driverSettings.getCreditCostForBooking()) * costPerCredit));
			String costForFixedPriceBooking = String.valueOf(String.format("%.2f",
					Float.parseFloat(AppValues.driverSettings.getCreditCostForFixedPriceBooking()) * costPerCredit));
			String currencySymbol = AppValues.driverSettings.getCurrencySymbol();
			tvCostForNowJob
					.setText(getResources().getString(R.string.costfornowjob) + " " + currencySymbol + "" + costForBooking);
			tvCostForFixedPriceJob.setText(getResources().getString(R.string.costforfixedpricejob) + " " + currencySymbol + ""
					+ costForFixedPriceBooking);

		}

		seekCreditsbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

				Float sliderCreditValue = Float.parseFloat(String.valueOf(progress + nMinProgress));
				txtNoOfCredits.setText(String.valueOf(String.format("%.2f", sliderCreditValue * costPerCredit)));

				nNoOfCredits = progress + nMinProgress;
				// txtNoOfCredits.setText(Integer.toString(progress));
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// updateTotalCost(nNoOfCredits);
			}
		});

		super.initWidgets(rootView, this.getClass().getName());

		return rootView;
	}

	// void updateTotalCost(int noOfCredits) {
	// if (AppValues.driverSettings != null) {
	// costPerCredit =
	// Float.parseFloat(AppValues.driverSettings.getCostPerCredit());
	// totalValue = Float.parseFloat(String.format(Locale.ENGLISH, "%.2f",
	// costPerCredit * noOfCredits));
	// txtTotalAmt.setText(AppValues.driverSettings.getCurrencySymbol() + " " +
	// String.format("%.2f", totalValue));
	//
	// }
	//
	// }

	class ViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.rlBuyCredits:

				Bundle mBundle = new Bundle();

				Log.e(TAG, "BUYCREDITS_NOOFCREDITS:: " + txtNoOfCredits.getText().toString());
				Float noOfCredits = (Float.parseFloat(txtNoOfCredits.getText().toString())/ costPerCredit);
				int noOfCredit = Float.valueOf(noOfCredits).intValue();
				
				mBundle.putInt(Constants.BUYCREDITS_NOOFCREDITS, noOfCredit);
				mBundle.putFloat(Constants.BUYCREDITS_COSTPERCREDIT, costPerCredit);
				mBundle.putFloat(Constants.BUYCREDITS_TOTLCOST, Float.parseFloat(txtNoOfCredits.getText().toString()));

				Intent intentCardActiviy = new Intent(getActivity(), BuyCreditsCardActivity.class);
				intentCardActiviy.putExtras(mBundle);
				startActivity(intentCardActiviy);

				// if (minCreditPurchase > totalValue) {
				// if (AppValues.driverSettings != null)
				// showDialog("Minimum credit should be greater than or equal to "
				// + (minCreditPurchase));
				// } else

				// Payment::
				// AlertDialog NoCardFeesAlertDialog = new AlertDialog.Builder(
				// getActivity())
				// .setMessage(
				// "No card fees would be deducted for purchasing credits.")
				// .setPositiveButton("OK",
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(
				// DialogInterface argDialog,
				// int argWhich) {
				//
				// if (AppValues.driverSettings != null) {
				// float fLargeTransactionAmount = Float
				// .valueOf(AppValues.driverSettings
				// .getLargeTransactionAmount());
				// if (totalValue > fLargeTransactionAmount) {
				// AlertDialog transactionAlertDialog = new AlertDialog.Builder(
				// mContext)
				// .setTitle("Alert!")
				// .setMessage(
				// "This is a large transaction. Would you like to proceed?")
				// .setPositiveButton(
				// "OK",
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(
				// DialogInterface argDialog,
				// int argWhich) {
				//
				// ProceedWithPayment();
				// }
				// })
				// .setNegativeButton(
				// "Cancel",
				// new DialogInterface.OnClickListener() {
				// @Override
				// public void onClick(
				// DialogInterface argDialog,
				// int argWhich) {
				// }
				// }).create();
				// transactionAlertDialog
				// .setCanceledOnTouchOutside(false);
				// transactionAlertDialog.show();
				// } else {
				// // If total value is less than
				// // getLargeTransactionAmount
				// ProceedWithPayment();
				// }
				//
				// }
				// }
				// }).create();
				// NoCardFeesAlertDialog.setCanceledOnTouchOutside(false);
				// NoCardFeesAlertDialog.show();

				break;

			case R.id.rlAutoTopUp:

				if (NetworkUtil.isNetworkOn(Util.mContext)) {
					dialogBuyCredit = new ProgressDialog(mContext);
					dialogBuyCredit.setMessage("Loading...");
					dialogBuyCredit.setCancelable(false);
					dialogBuyCredit.show();

					AutoTopUpTask autoTopUpTask = new AutoTopUpTask();
					if (isAutoTopUpOn) {
						autoTopUpTask.iIsAutoTopUp = 0;
					} else {
						autoTopUpTask.iIsAutoTopUp = 1;
					}
					autoTopUpTask.execute();
				} else {
					Util.showToastMessage(mContext, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
				}

				break;
			}
		}
	}

	public class AutoTopUpTask extends AsyncTask<String, Void, String> {
		public int iIsAutoTopUp;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			AutoTopUpStatus autoTopUp = new AutoTopUpStatus(mContext, iIsAutoTopUp);
			String response = autoTopUp.UpdateAutoTopUp();

			if (Constants.isDebug)
				Log.e(TAG, "AutoTopUpTask response::> " + response);
			try {
				JSONObject jObject = new JSONObject(response);
				String errorMessage = "";
				if (jObject.has("success") && jObject.getString("success").equals("true")) {

					return "success";
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						errorMessage = jErrorsArray.getJSONObject(0).getString("message");
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

			if (dialogBuyCredit != null) {
				dialogBuyCredit.dismiss();
				dialogBuyCredit = null;
			}

			if (result.equals("success")) {

				if (isAutoTopUpOn) {
					toggleAutoTopUp.setChecked(false);
					Util.setAutoTopUp(getActivity(), false);
					isAutoTopUpOn = false;
				} else {
					toggleAutoTopUp.setChecked(true);
					Util.setAutoTopUp(getActivity(), true);
					isAutoTopUpOn = true;
				}

				DriverAccountDetails driverAccount = new DriverAccountDetails(mContext);
				driverAccount.retriveAccountDetails(mContext);

				DriverSettingDetails driverSettings = new DriverSettingDetails(mContext);
				driverSettings.retriveDriverSettings(mContext);

			} else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}

		}

	}

	void showDialog(String message) {
		AlertDialog quitAlertDialog = new AlertDialog.Builder(getActivity()).setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
						bIsDialogShown = false;
					}
				}).create();
		quitAlertDialog.setCanceledOnTouchOutside(false);
		quitAlertDialog.show();
		// return;
	}

	void ProceedWithPayment() {

		// Proceed with payment
		float cardPaymentPercentageFee = 0;
		int isAutoTopUp = 0;
		if (AppValues.driverSettings.getIsAutoTopup() != null) {
			isAutoTopUp = AppValues.driverSettings.getIsAutoTopup();
		}
		if (AppValues.driverSettings != null) {
			cardPaymentPercentageFee = Float.valueOf(AppValues.driverSettings.getCardPaymentPercentageFee());
			float tipPercentage = (float) 0.0399;

			if (AppValues.driverSettings != null)
				tipPercentage = Float.parseFloat(AppValues.driverSettings.getCardPaymentPercentageFee()) / 100;
			cardFeesAmount = Float.parseFloat(String.format(Locale.ENGLISH, "%.2f", (float) (tipPercentage * totalValue)));
			if (Constants.isDebug)
				Log.e(TAG, "totalValue: " + totalValue + " getCardPaymentPercentageFee:   " + cardPaymentPercentageFee
						+ " cardFeesAmount " + cardFeesAmount);
			nPricePerCredit = (int) (Float.parseFloat(AppValues.driverSettings.getCostPerCredit()) * 100);

			// noOfCredits = 0;
			// minCreditPurchase = 0;
			// totalValue = 0;
		}

		final String szPaymentURL = Constants.driverURL + "buycredit?accessToken=" + Constants.accessToken
				+ "&credits="
				// + (int) (totalValue *
				// 100)
				+ txtNoOfCredits.getText().toString() + "&pricepercredit=" + nPricePerCredit + "&autotopup=" + isAutoTopUp
				+ "&fee=" + (int) (cardFeesAmount * 100);

		if (Constants.isDebug)
			Log.e(TAG, "szPaymentURL: " + szPaymentURL);

		if (NetworkUtil.isNetworkOn(Util.mContext)) {
			// TODO Auto-generated method stub
			((Activity) Util.mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					GetPaymentURLTask getPaymentURLTask = new GetPaymentURLTask();
					getPaymentURLTask.szURL = szPaymentURL;
					getPaymentURLTask.mContext = Util.mContext;
					getPaymentURLTask.bIsAddCredit = true;
					getPaymentURLTask.execute();
				}
			});
		} else {
			Util.showToastMessage(getActivity(), getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}

	}
}
