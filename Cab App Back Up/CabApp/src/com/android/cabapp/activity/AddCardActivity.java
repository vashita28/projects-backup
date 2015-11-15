package com.android.cabapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class AddCardActivity extends RootActivity {

	String TAG = AddCardActivity.class.getName();

	WebView wvPayment;
	ImageView ivLogo;
	String szURL = "";
	ProgressBar progressAddCard;

	boolean bIsSuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_card);
		mContext = this;

		szURL = getIntent().getExtras().getString("paymentURL");
		wvPayment = (WebView) findViewById(R.id.wvPaymentAddCard);
		progressAddCard = (ProgressBar) findViewById(R.id.progressAddCard);

		ivLogo = (ImageView) findViewById(R.id.ivLogo);

		WebSettings settings = wvPayment.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		wvPayment.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		final AlertDialog alertDialog = new AlertDialog.Builder(
				AddCardActivity.this).create();

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

				Log.e("AddcardActivity", "onPageStarted:: " + url);

				if (url.contains("success")) {
					// Call driver setting API to update Settings
					if (!bIsSuccess) {
						bIsSuccess = true;
						fetchDriverDetailsData();
					}
				}// similarly check for error, timeout, hold, decline and
					// show message
				else if (url.contains("error")) {
					Util.showToastMessage(
							AddCardActivity.this,
							"Error processing payment request. Please try again!",
							Toast.LENGTH_LONG);
				} else if (url.contains("timeout")) {
					Util.showToastMessage(AddCardActivity.this,
							"Payment request has timed out. Please try again!",
							Toast.LENGTH_LONG);
				} else if (url.contains("hold")) {
					Util.showToastMessage(AddCardActivity.this,
							"Payment is on hold. Please try again!",
							Toast.LENGTH_LONG);
				} else if (url.contains("decline")) {
					Util.showToastMessage(AddCardActivity.this,
							"Payment declined. Please try again!",
							Toast.LENGTH_LONG);
				}
			}

			public void onPageFinished(WebView view, String url) {
				if (Constants.isDebug)
					Log.i(TAG, "Finished loading URL: " + url);
				if (progressAddCard != null && progressAddCard.isShown()) {
					progressAddCard.setVisibility(View.GONE);
				}
				// https://pay.test.netbanx.com/cabapp-rest/tx_payment/en
			}

			private FragmentManager getSupportFragmentManager() {
				// TODO Auto-generated method stub
				return null;
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if (progressAddCard.isShown()) {
					progressAddCard.setVisibility(View.GONE);
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
		progressAddCard.setVisibility(View.VISIBLE);
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

				// code to redirect to BuyCreditsCardActivity
				finish();

			}
		});

		if (NetworkUtil.isNetworkOn(AddCardActivity.this)) {
			networkThread.start();
		} else {
			Util.showToastMessage(AddCardActivity.this, getResources()
					.getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}
	}

}
