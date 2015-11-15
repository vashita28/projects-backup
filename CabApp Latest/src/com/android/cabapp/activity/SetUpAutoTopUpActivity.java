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

	private static final String TAG = SetUpAutoTopUpActivity.class.getSimpleName();

	TextView txtAuto, tvMinimumSpend, txtNoOfCredits, tvCostForNowJob, tvCostForFixedPriceJob, tvCredits;
	SeekBar seekCreditsbar;
	RelativeLayout rlSetUpAutoTopUp;

	int nPricePerCredit = 0, nMinProgress = 0, noOfCredits = 0;
	float totalValue = 0;

	static Context _context;

	Bundle mAutoTopUpBundle;
	Float costPerCredit = (float) 0.00, autoTopupMinimum = (float) 0.00;
	String currencySymbol;

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
		tvCredits = (TextView) findViewById(R.id.tvCredits);
		tvMinimumSpend = (TextView) findViewById(R.id.txtMinimumSpend);
		txtNoOfCredits = (TextView) findViewById(R.id.tvCreditsValue);
		// textTotalAmt = (TextView) findViewById(R.id.tvTotalAmt);
		// textPerCredit = (TextView) findViewById(R.id.tvCostPerCredit);
		tvCostForNowJob = (TextView) findViewById(R.id.tvCostForNowJob);
		tvCostForFixedPriceJob = (TextView) findViewById(R.id.tvCostForFixedPriceJob);
		seekCreditsbar = (SeekBar) findViewById(R.id.creditsbar);
		//tvTotal = (TextView) findViewById(R.id.tvTotal);
		rlSetUpAutoTopUp = (RelativeLayout) findViewById(R.id.rlSetUpAutoTopUp);
		rlSetUpAutoTopUp.setOnClickListener(new ViewOnClickListener());

		seekCreditsbar.setMax(100 - nMinProgress);
		
		if (AppValues.driverSettings != null) {

			costPerCredit = Float.parseFloat(String.valueOf(AppValues.driverSettings.getCostPerCredit()));
			autoTopupMinimum = Float.parseFloat(String.valueOf(AppValues.driverSettings.getMinimumCreditPurchase()));
			currencySymbol = AppValues.driverSettings.getCurrencySymbol();
			String costForBooking = String.valueOf(String.format("%.2f",
					Float.parseFloat(AppValues.driverSettings.getCreditCostForBooking()) * costPerCredit));
			String costForFixedPriceBooking = String.valueOf(String.format("%.2f",
					Float.parseFloat(AppValues.driverSettings.getCreditCostForFixedPriceBooking()) * costPerCredit));
			int sliderValue = seekCreditsbar.getProgress() + nMinProgress;

			txtAuto.setText("You will auto top up when your account reaches  " + AppValues.driverSettings.getCurrencySymbol()
					+ " 1.00");

			tvMinimumSpend.setText("MINIMUM SPEND " + currencySymbol + " "
					+ String.format("%.2f", costPerCredit * autoTopupMinimum));

			tvCredits.setText(currencySymbol);

			nMinProgress = Integer.parseInt(AppValues.driverSettings.getMinimumCreditPurchase());
			txtNoOfCredits.setText(String.format("%.2f",sliderValue*costPerCredit));

			// String szPerCredit =
			// "<font color=#000000> @ </font><font color=#fd6f01>"
			// + AppValues.driverSettings.getCurrencySymbol() + " "
			// + String.format("%.2f",
			// Float.valueOf(AppValues.driverSettings.getCostPerCredit()))
			// + " </font> <font color=#000000> per credit</font>";
			// textPerCredit.setText(Html.fromHtml(szPerCredit));

			tvCostForNowJob
					.setText(getResources().getString(R.string.costfornowjob) + " " + currencySymbol + "" + costForBooking);
			tvCostForFixedPriceJob.setText(getResources().getString(R.string.costforfixedpricejob) + " " + currencySymbol + ""
					+ costForFixedPriceBooking);

		}

		// Set Data From SetUpAutoTopUpEditActivity::
		if (mAutoTopUpBundle != null && mAutoTopUpBundle.containsKey(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT)) {
//			txtNoOfCredits.setText(String.valueOf(seekCreditsbar.getProgress()
//					+ mAutoTopUpBundle.getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT)));

			int sliderValue = seekCreditsbar.getProgress() + mAutoTopUpBundle.getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT);
			txtNoOfCredits.setText(String.format("%.2f",sliderValue*costPerCredit));
			
//			String totalText = "<font color=#fd6f01>= </font> <font color=#f5f5f5> Total</font>";
//			tvTotal.setText(Html.fromHtml(totalText));
			// updateTotalCost(mAutoTopUpBundle.getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT));

			// seekCreditsbar.setProgress(mAutoTopUpBundle
			// .getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT));

		} else {
			//txtNoOfCredits.setText(String.valueOf(seekCreditsbar.getProgress() + nMinProgress));
			//String totalText = "<font color=#fd6f01>= </font> <font color=#f5f5f5> Total</font>";
			//tvTotal.setText(Html.fromHtml(totalText));
			int sliderValue = seekCreditsbar.getProgress() + mAutoTopUpBundle.getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT);
			txtNoOfCredits.setText(String.format("%.2f",sliderValue*costPerCredit));
			// updateTotalCost(nMinProgress);
		}

		
		seekCreditsbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

				//txtNoOfCredits.setText(String.valueOf(progress + nMinProgress));

				noOfCredits = progress + nMinProgress;
				txtNoOfCredits.setText(String.format("%.2f",progress*costPerCredit));
				// txtNoOfCredits.setText(Integer.toString(progress));
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// updateTotalCost(noOfCredits);
			}
		});

	}

	// void updateTotalCost(int noOfCredits) {
	// if (AppValues.driverSettings != null) {
	// float costPerCredit =
	// Float.parseFloat(AppValues.driverSettings.getCostPerCredit());
	// totalValue = Float.parseFloat(String.format(Locale.ENGLISH, "%.2f",
	// costPerCredit * noOfCredits));
	// textTotalAmt.setText(AppValues.driverSettings.getCurrencySymbol() + " " +
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
			case R.id.rlSetUpAutoTopUp:

				
				Log.e(TAG, "BUYCREDITS_NOOFCREDITS:: " + txtNoOfCredits.getText().toString());
				Float noOfCredits = (Float.parseFloat(txtNoOfCredits.getText().toString())/ costPerCredit);
				int noOfCredit = Float.valueOf(noOfCredits).intValue();

				if (mAutoTopUpBundle == null)
					mAutoTopUpBundle = new Bundle();
				mAutoTopUpBundle.putInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT, noOfCredit);
				mAutoTopUpBundle.putFloat(Constants.AUTOTOPUP_TOTAL_AMOUNT, Float.parseFloat(txtNoOfCredits.getText().toString()));

				Intent intentCardActiviy = new Intent(SetUpAutoTopUpActivity.this, AutoTopUpCardActivity.class);
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
