package com.android.cabapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.fragments.BuyAddCreditsFragment;
import com.android.cabapp.fragments.CabPayFragment;
import com.android.cabapp.fragments.InboxFragment;
import com.android.cabapp.fragments.PaymentDetailsFragment;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.flurry.android.FlurryAgent;

public class TakePaymentActivity extends RootActivity {

	WebView wvPayment;

	ImageView ivBack, ivLogo;
	TextView txtEdit;
	String TAG = TakePaymentActivity.class.getName();
	String szURL = "";
	ProgressBar progressPayment;
	boolean bIsAddCredit = false, bIsPaymentWithJobID = false;

	Bundle mBundle;

	boolean bIsPaymentSuccessfulCalbackGiven = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_payment);
		mContext = this;

		if (getIntent().getExtras() != null) {
			mBundle = getIntent().getExtras();

			szURL = mBundle.getString("paymentURL");
			bIsAddCredit = mBundle.getBoolean("isaddcredit");
			if (szURL.contains("bookingId"))
				bIsPaymentWithJobID = true;
			if (Constants.isDebug)
				Log.i(TAG, "URL: " + szURL);
		}

		wvPayment = (WebView) findViewById(R.id.wvPayment);
		progressPayment = (ProgressBar) findViewById(R.id.progressPayment);

		ivLogo = (ImageView) findViewById(R.id.ivLogo);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setVisibility(View.VISIBLE);
		txtEdit = (TextView) findViewById(R.id.tvEdit);
		txtEdit.setVisibility(View.GONE);
		ivBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ivLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				if (InboxFragment.mHandler != null) {
					InboxFragment.mHandler.sendEmptyMessage(1);
				}
			}
		});

		WebSettings settings = wvPayment.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		wvPayment.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		final AlertDialog alertDialog = new AlertDialog.Builder(
				TakePaymentActivity.this).create();

		wvPayment.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (Constants.isDebug)
					Log.i(TAG, "Processing webview url click...");
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);

				if (url.contains("success") || url.contains("tx_payment")) {
					// Call driver setting API to update Settings
					if (!bIsPaymentSuccessfulCalbackGiven)
						fetchDriverDetailsData();
				}// similarly check for error, timeout, hold, decline and
					// show message
				else if (url.contains("error")) {
					Util.showToastMessage(
							TakePaymentActivity.this,
							"Error processing payment request. Please try again!",
							Toast.LENGTH_LONG);
				} else if (url.contains("timeout")) {
					Util.showToastMessage(TakePaymentActivity.this,
							"Payment request has timed out. Please try again!",
							Toast.LENGTH_LONG);
				} else if (url.contains("hold")) {
					Util.showToastMessage(TakePaymentActivity.this,
							"Payment is on hold. Please try again!",
							Toast.LENGTH_LONG);
				} else if (url.contains("decline")) {
					Util.showToastMessage(TakePaymentActivity.this,
							"Payment declined. Please try again!",
							Toast.LENGTH_LONG);
				}
			}

			public void onPageFinished(WebView view, String url) {
				if (Constants.isDebug)
					Log.i(TAG, "Finished loading URL: " + url);
				if (progressPayment != null && progressPayment.isShown()) {
					progressPayment.setVisibility(View.GONE);
				}
				// https://pay.test.netbanx.com/cabapp-rest/tx_payment/en
			}

			private FragmentManager getSupportFragmentManager() {
				// TODO Auto-generated method stub
				return null;
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if (progressPayment.isShown()) {
					progressPayment.setVisibility(View.GONE);
				}
				if (Constants.isDebug)
					Log.e(TAG, "Error: " + description);
				alertDialog.setTitle("Error");
				alertDialog.setMessage(description);
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				alertDialog.show();
			}
		});

		wvPayment.loadUrl(szURL);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();
	}

	void fetchDriverDetailsData() {
		bIsPaymentSuccessfulCalbackGiven = true;
		Thread networkThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				DriverAccountDetails driverAccount = new DriverAccountDetails(
						mContext);
				driverAccount.retriveAccountDetails(mContext);

				DriverSettingDetails driverSettings = new DriverSettingDetails(
						mContext);
				driverSettings.retriveDriverSettings(mContext);

				// code to redirect to jobs fragment
				finish();

				if (!bIsAddCredit) {
					FlurryAgent.logEvent("Payment successful");
					AppValues.mNowJobsList.clear();

					if (!bIsPaymentWithJobID) {
						Message message = new Message();
						message.obj = mBundle;
						message.what = 100;
						if (CabPayFragment.mHandler != null) {
							CabPayFragment.mHandler.sendMessage(message);
						}
					} else {
						if (PaymentDetailsFragment.mHandler != null) {
							PaymentDetailsFragment.mHandler.sendEmptyMessage(0);
						}
					}
				} else {
					FlurryAgent.logEvent("Credits purchase successful");
					if (BuyAddCreditsFragment.mBuyCreditsHandler != null) {
						BuyAddCreditsFragment.mBuyCreditsHandler
								.sendEmptyMessage(0);
					}
				}

			}
		});

		if (NetworkUtil.isNetworkOn(TakePaymentActivity.this)) {
			networkThread.start();
		} else {
			Util.showToastMessage(TakePaymentActivity.this, getResources()
					.getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}
	}
}
