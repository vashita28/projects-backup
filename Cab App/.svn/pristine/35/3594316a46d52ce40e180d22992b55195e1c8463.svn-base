package com.android.cabapp.handpoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.cabapp.R;
import com.handpoint.api.Currency;
import com.handpoint.api.TransactionResult;

public class HandpointActivity extends Activity {

	HandPointClass mClass;
	EditText mHandPointEditTextBT, mPaymentEditTextBT;
	Button mConnectButton, mPaymentButton;
	TextView mConnectStatusTextView, mPaymentStatusTextView,
			paymentBTTextViewDeviceName;
	LinearLayout mLayout1;
	ScrollView mLayout2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_handpoint);

		mLayout1 = (LinearLayout) findViewById(R.id.layout1);
		mHandPointEditTextBT = (EditText) findViewById(R.id.chipandpindeviceEditTextBT);
		mConnectButton = (Button) findViewById(R.id.connectButtonBT);
		mConnectStatusTextView = (TextView) findViewById(R.id.connectBTTextViewStatus);

		// payment layout
		mLayout2 = (ScrollView) findViewById(R.id.layout2);
		mPaymentEditTextBT = (EditText) findViewById(R.id.payEditTextBT);
		mPaymentButton = (Button) findViewById(R.id.paymentButtonBT);
		mPaymentStatusTextView = (TextView) findViewById(R.id.paymentBTTextViewStatus);
		paymentBTTextViewDeviceName = (TextView) findViewById(R.id.paymentBTTextViewDeviceName);

		mConnectButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mHandPointEditTextBT.getText().toString().length() > 0) {
					mConnectButton.setVisibility(View.GONE);
					mConnectStatusTextView.setVisibility(View.VISIBLE);
					mClass = new HandPointClass(HandpointActivity.this,
							mCallbackListener, "PP0513901425");
				} else {
					showAlert("Please enter Handpoint Device name");
				}
			}
		});

		mPaymentButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mPaymentEditTextBT.getText().toString().length() > 0) {
					mPaymentButton.setVisibility(View.GONE);
					mPaymentStatusTextView.setText("Connecting");
					mPaymentStatusTextView.setVisibility(View.VISIBLE);
					mClass.pay(mPaymentEditTextBT.getText().toString(),
							Currency.GBP);
				} else {
					showAlert("Please enter some amount");
				}
			}
		});

	}

	CallbackListener mCallbackListener = new CallbackListener() {

		@Override
		public void onDeviceConnect(final String devicename) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mConnectStatusTextView.setText(devicename);
					paymentBTTextViewDeviceName.setText("You are connected to "
							+ devicename);
					mLayout1.setVisibility(View.GONE);
					mLayout2.setVisibility(View.VISIBLE);
				}
			});
		}

		@Override
		public void onTransactionComplete(final String response) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mPaymentStatusTextView.setText(response);
					mPaymentButton.setVisibility(View.VISIBLE);
					mPaymentStatusTextView.setVisibility(View.VISIBLE);
				}
			});
		}

		@Override
		public void onDeviceNotFound() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTransactionComplete(TransactionResult transactionResult) {
			// TODO Auto-generated method stub

		}
	};

	private void showAlert(String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(
				HandpointActivity.this).create();

		// Setting Dialog Title
		alertDialog.setTitle("Handpoint");

		// Setting Dialog Message
		alertDialog.setMessage(msg);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

}
