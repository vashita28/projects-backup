package com.android.cabapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.util.Util;

public class TakePaymentFragment extends RootFragment {

	RelativeLayout rlPaymentTaken, rlConfirmPayment, rlSendReceiptPopUp;
	TextView textPaymentHasBeenTaken, textSMS, textEmail, textNo, textSend,
			textCancel;
	EditText etCardNumber1, etCardNumber2, etCardNumber3, etCardNumber4,
			etExpiryDateMonth, etExpiryDateYear, etIssueNumber, etCVVNumber;

	public TakePaymentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_take_payment,
				container, false);
		super.initWidgets(rootView, this.getClass().getName());
		rlPaymentTaken = (RelativeLayout) rootView
				.findViewById(R.id.rlPaymentHasBeenTaken);
		rlConfirmPayment = (RelativeLayout) rootView
				.findViewById(R.id.rlbottombar);
		rlSendReceiptPopUp = (RelativeLayout) rootView
				.findViewById(R.id.rlSendReceiptPopUp);
		textPaymentHasBeenTaken = (TextView) rootView
				.findViewById(R.id.textPaymentHasBeenTaken);
		textSMS = (TextView) rootView.findViewById(R.id.textSMS);
		textEmail = (TextView) rootView.findViewById(R.id.textEmail);
		textNo = (TextView) rootView.findViewById(R.id.textNo);
		textSend = (TextView) rootView.findViewById(R.id.textSend);
		textCancel = (TextView) rootView.findViewById(R.id.textCancel);

		String paymentText = "<font color=#fd6f01>Payment has been taken <br> </font> <font color=#f5f5f5> Would you like to send the <br> passenger a receipt? </font>";
		textPaymentHasBeenTaken.setText(Html.fromHtml(paymentText));

		etCardNumber1 = (EditText) rootView.findViewById(R.id.etCardNumber1);
		etCardNumber2 = (EditText) rootView.findViewById(R.id.etCardNumber2);
		etCardNumber3 = (EditText) rootView.findViewById(R.id.etCardNumber3);
		etCardNumber4 = (EditText) rootView.findViewById(R.id.etCardNumber4);
		etCVVNumber = (EditText) rootView.findViewById(R.id.etCVVNumber);
		etExpiryDateMonth = (EditText) rootView
				.findViewById(R.id.etExpiryDateMonth);
		etExpiryDateYear = (EditText) rootView
				.findViewById(R.id.etExpiryDateYear);
		etIssueNumber = (EditText) rootView.findViewById(R.id.etIssueNumber);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		editTextChangeListener();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mMainBundle = this.getArguments();
		if (mMainBundle != null) {
			String totalValue = mMainBundle.getString("totalvalue");
			Log.d("TakePayment", "Total value:: " + totalValue);
		}

		((MainActivity) Util.mContext).setDontShowQuitDialog();

		rlPaymentTaken.setOnTouchListener(new TextTouchListener());
		rlConfirmPayment.setOnTouchListener(new TextTouchListener());
		textSMS.setOnTouchListener(new TextTouchListener());
		textEmail.setOnTouchListener(new TextTouchListener());
		textNo.setOnTouchListener(new TextTouchListener());
		textSend.setOnTouchListener(new TextTouchListener());
		textCancel.setOnTouchListener(new TextTouchListener());

	}

	class TextTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) {

				switch (v.getId()) {

				case R.id.rlPaymentHasBeenTaken:
					rlPaymentTaken.setVisibility(View.GONE);
					rlSendReceiptPopUp.setVisibility(View.GONE);
					break;
				case R.id.rlbottombar:// Confirm Payment
					if (etCardNumber1.getText().toString().length() < 4
							|| etCardNumber2.getText().toString().length() < 4
							|| etCardNumber3.getText().toString().length() < 4
							|| etCardNumber4.getText().toString().length() < 4) {
						Util.showToastMessage(Util.mContext,
								"Please enter valid card number",
								Toast.LENGTH_LONG);
					} else if (etExpiryDateMonth.getText().toString().length() == 0
							|| Integer.parseInt(etExpiryDateMonth.getText()
									.toString()) == 0
							|| Integer.parseInt(etExpiryDateMonth.getText()
									.toString()) > 12) {
						Util.showToastMessage(Util.mContext,
								"Please enter valid expiry month",
								Toast.LENGTH_LONG);
					} else if (etExpiryDateYear.getText().toString().length() == 0
							|| Integer.parseInt(etExpiryDateYear.getText()
									.toString()) < 14) {
						Util.showToastMessage(Util.mContext,
								"Please enter valid expiry year",
								Toast.LENGTH_LONG);
					} else {
						rlPaymentTaken.setVisibility(View.VISIBLE);
						rlSendReceiptPopUp.setVisibility(View.GONE);
					}
					break;
				case R.id.rlSendReceiptPopUp:
					rlPaymentTaken.setVisibility(View.GONE);
					rlSendReceiptPopUp.setVisibility(View.GONE);
					break;
				case R.id.textSMS:
					rlPaymentTaken.setVisibility(View.GONE);
					rlSendReceiptPopUp.setVisibility(View.VISIBLE);
					break;
				case R.id.textEmail:
					rlPaymentTaken.setVisibility(View.GONE);
					rlSendReceiptPopUp.setVisibility(View.VISIBLE);
					break;
				case R.id.textNo:
					rlPaymentTaken.setVisibility(View.GONE);
					rlSendReceiptPopUp.setVisibility(View.GONE);
					break;
				case R.id.textSend:
					rlPaymentTaken.setVisibility(View.GONE);
					rlSendReceiptPopUp.setVisibility(View.GONE);
					break;
				case R.id.textCancel:
					rlPaymentTaken.setVisibility(View.GONE);
					rlSendReceiptPopUp.setVisibility(View.GONE);
					break;

				}
			}

			return true;
		}
	}

	public void editTextChangeListener() {
		etCardNumber1.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) { // m_etActivationCode1.clearFocus();
				if (etCardNumber1.getText().length() > 3)
					etCardNumber2.requestFocus();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		etCardNumber2.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (etCardNumber2.getText().length() > 3) {
					etCardNumber2.clearFocus();
					etCardNumber3.requestFocus();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		etCardNumber3.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (etCardNumber3.getText().length() > 3) {
					etCardNumber3.clearFocus();
					etCardNumber4.requestFocus();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}
}
