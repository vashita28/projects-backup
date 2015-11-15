package com.android.cabapp.activity;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class AutoTopUpEditActivity extends RootActivity {

	private static final String TAG = AutoTopUpEditActivity.class.getSimpleName();

	ImageView ivCardType;
	TextView tvEditConfirm, tvOrderDetails, textCardName, textNoOfCredits, textPricePerCredit, textAutoTopUpStatus,
			textTotalCost;
	RelativeLayout rlEditConfirm;
	static Context _context;

	Bundle mAutoTopUpEditBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_autotopup_confirmandedit);
		super.initWidgets();
		_context = AutoTopUpEditActivity.this;

		mAutoTopUpEditBundle = getIntent().getExtras();

		ivCardType = (ImageView) findViewById(R.id.ivCard);
		tvEditConfirm = (TextView) findViewById(R.id.tvEditConfirm);
		rlEditConfirm = (RelativeLayout) findViewById(R.id.rlAutoTopUpEditConfirm);
		tvOrderDetails = (TextView) findViewById(R.id.textOrderDetails);
		tvEditConfirm.setText("Edit");
		tvOrderDetails.setText("Auto Top Up Details");
		textCardName = (TextView) findViewById(R.id.textPaymentMethodData);
		textNoOfCredits = (TextView) findViewById(R.id.textCreditsData);
		textPricePerCredit = (TextView) findViewById(R.id.textPricePerCreditData);
		textAutoTopUpStatus = (TextView) findViewById(R.id.textAutoTopUpData);
		textTotalCost = (TextView) findViewById(R.id.textTotalData);
		rlEditConfirm.setOnClickListener(new ViewOnClickListener());

		if (mAutoTopUpEditBundle != null && mAutoTopUpEditBundle.containsKey(Constants.AUTOTOPUP_LASTTRAN_BRAND)) {

			if (Util.cardsCodeHashMap.get(mAutoTopUpEditBundle.getString(Constants.AUTOTOPUP_LASTTRAN_BRAND).toLowerCase()) != null)
				ivCardType.setBackgroundResource(Util.cardsCodeHashMap.get(mAutoTopUpEditBundle.getString(
						Constants.AUTOTOPUP_LASTTRAN_BRAND).toLowerCase()));
			else
				ivCardType.setBackgroundResource(0);

			textCardName.setText("**** **** **** " + mAutoTopUpEditBundle.getString(Constants.AUTOTOPUP_LASTTRAN_TRUNCATEDPAN));

			textNoOfCredits.setText(String.valueOf(mAutoTopUpEditBundle.getInt(Constants.AUTOTOPUP_LASTTRAN_CREDITCOUNT)));

			if (AppValues.driverSettings != null) {
				textPricePerCredit.setText(AppValues.driverSettings.getCurrencySymbol() + ""
						+ String.format(Locale.ENGLISH, "%.2f", Float.valueOf(AppValues.driverSettings.getCostPerCredit())));

				textTotalCost.setText(AppValues.driverSettings.getCurrencySymbol() + ""
						+ String.format(Locale.ENGLISH, "%.2f", mAutoTopUpEditBundle.getFloat(Constants.AUTOTOPUP_LASTTRAN_AMT)));

				if (AppValues.driverSettings.getIsAutoTopup() == 1)
					textAutoTopUpStatus.setText("Yes");
				else
					textAutoTopUpStatus.setText("No");
			}
		}

	}

	class ViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.rlAutoTopUpEditConfirm:

				Intent intentSetUpAutoTopUpActiviy = new Intent(AutoTopUpEditActivity.this, SetUpAutoTopUpActivity.class);
				intentSetUpAutoTopUpActiviy.putExtras(mAutoTopUpEditBundle);
				startActivity(intentSetUpAutoTopUpActiviy);

				break;

			}
		}
	}

	public static void finishActivity() {

		if (_context != null)
			((Activity) _context).finish();
	}

}