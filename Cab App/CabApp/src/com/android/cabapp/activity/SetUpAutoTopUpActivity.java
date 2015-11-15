package com.android.cabapp.activity;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;

public class SetUpAutoTopUpActivity extends RootActivity {

	private static final String TAG = SetUpAutoTopUpActivity.class
			.getSimpleName();

	TextView txtAuto, tvMinimumSpend, txtNoOfCredits, textTotalAmt,
			textPerCredit, tvTotal;
	SeekBar seekCreditsbar;
	RelativeLayout rlSetUpAutoTopUp;

	int nPricePerCredit = 0, nMinProgress = 0, noOfCredits = 0;
	float totalValue = 0;

	static Context _context;

	Bundle mAutoTopUpBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autotopup);
		super.initWidgets();
		_context = this;

		mAutoTopUpBundle = getIntent().getExtras();

		txtAuto = (TextView) findViewById(R.id.txtAuto);
		tvMinimumSpend = (TextView) findViewById(R.id.txtMinimumSpend);
		txtNoOfCredits = (TextView) findViewById(R.id.tvCreditsValue);
		textTotalAmt = (TextView) findViewById(R.id.tvTotalAmt);
		textPerCredit = (TextView) findViewById(R.id.tvCostPerCredit);
		seekCreditsbar = (SeekBar) findViewById(R.id.creditsbar);
		tvTotal = (TextView) findViewById(R.id.tvTotal);
		rlSetUpAutoTopUp = (RelativeLayout) findViewById(R.id.rlSetUpAutoTopUp);
		rlSetUpAutoTopUp.setOnClickListener(new ViewOnClickListener());

		if (AppValues.driverSettings != null) {
			if (AppValues.driverSettings.getAutoTopupMinimum() != null) {
				txtAuto.setText("You will auto top up when your account\n reaches  "
						+ AppValues.driverSettings.getAutoTopupMinimum()
						+ " credits");
			}

			tvMinimumSpend.setText("MINIMUM SPEND "
					+ AppValues.driverSettings.getCurrencySymbol() + " "
					+ AppValues.driverSettings.getCardPaymentMinimumAmount());

			nMinProgress = Integer.parseInt(AppValues.driverSettings
					.getMinimumCreditPurchase());
			// txtNoOfCredits.setText(String.valueOf(seekCreditsbar.getProgress()
			// + nMinProgress));

			String szPerCredit = "<font color=#000000> @ </font><font color=#fd6f01>"
					+ AppValues.driverSettings.getCurrencySymbol()
					+ " "
					+ String.format("%.2f", Float
							.valueOf(AppValues.driverSettings
									.getCostPerCredit()))
					+ " </font> <font color=#000000> per credit</font>";
			textPerCredit.setText(Html.fromHtml(szPerCredit));
		}

		// Set Data From SetUpAutoTopUpEditActivity::
		if (mAutoTopUpBundle != null
				&& mAutoTopUpBundle
						.containsKey(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT)) {
			txtNoOfCredits.setText(String.valueOf(seekCreditsbar.getProgress()
					+ mAutoTopUpBundle
							.getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT)));

			String totalText = "<font color=#fd6f01>= </font> <font color=#f5f5f5> Total</font>";
			tvTotal.setText(Html.fromHtml(totalText));
			updateTotalCost(mAutoTopUpBundle
					.getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT));

			// seekCreditsbar.setProgress(mAutoTopUpBundle
			// .getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT));

		} else {
			txtNoOfCredits.setText(String.valueOf(seekCreditsbar.getProgress()
					+ nMinProgress));
			String totalText = "<font color=#fd6f01>= </font> <font color=#f5f5f5> Total</font>";
			tvTotal.setText(Html.fromHtml(totalText));

			updateTotalCost(nMinProgress);
		}

		seekCreditsbar.setMax(100 - nMinProgress);
		seekCreditsbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						txtNoOfCredits.setText(String.valueOf(progress
								+ nMinProgress));

						noOfCredits = progress + nMinProgress;
						// txtNoOfCredits.setText(Integer.toString(progress));
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						updateTotalCost(noOfCredits);
					}
				});

	}

	void updateTotalCost(int noOfCredits) {
		if (AppValues.driverSettings != null) {
			float costPerCredit = Float.parseFloat(AppValues.driverSettings
					.getCostPerCredit());
			totalValue = Float.parseFloat(String.format(Locale.ENGLISH, "%.2f",
					costPerCredit * noOfCredits));
			textTotalAmt.setText(AppValues.driverSettings.getCurrencySymbol()
					+ " " + String.format("%.2f", totalValue));

		}

	}

	class ViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.rlSetUpAutoTopUp:

				int nNoOfInteger = Integer.valueOf(txtNoOfCredits.getText()
						.toString());

				if (mAutoTopUpBundle == null)
					mAutoTopUpBundle = new Bundle();
				mAutoTopUpBundle.putInt(
						Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT, nNoOfInteger);
				mAutoTopUpBundle.putFloat(Constants.AUTOTOPUP_TOTAL_AMOUNT,
						totalValue);

				Intent intentCardActiviy = new Intent(
						SetUpAutoTopUpActivity.this,
						AutoTopUpCardActivity.class);
				intentCardActiviy.putExtras(mAutoTopUpBundle);
				intentCardActiviy.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentCardActiviy);

				break;

			}
		}
	}

	public static void finishActivity() {

		if (_context != null)
			((Activity) _context).finish();
	}

}
