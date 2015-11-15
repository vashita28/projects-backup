package com.android.cabapp.activity;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.AddScanCardJobsActivity.SpinnerOnTouchListener;
import com.android.cabapp.adapter.MonthYearSpinnerAdapter;
import com.android.cabapp.async.AddNewCardTask;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.DES;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.mobile.connect.PWConnect.PWProviderMode;
import com.mobile.connect.exception.PWError;
import com.mobile.connect.exception.PWException;
import com.mobile.connect.exception.PWProviderNotInitializedException;
import com.mobile.connect.listener.PWTokenObtainedListener;
import com.mobile.connect.listener.PWTransactionListener;
import com.mobile.connect.payment.PWPaymentParams;
import com.mobile.connect.payment.credit.PWCreditCardType;
import com.mobile.connect.provider.PWTransaction;
import com.mobile.connect.service.PWProviderBinder;

public class ScanCardActivity extends RootActivity {

	String TAG = ScanCardActivity.class.getName();
	private static final String MY_CARDIO_APP_TOKEN = "d1e2d2384af74d9fa58580784e05c1ed";
	private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int

	RelativeLayout rlScanCard;
	EditText etCardNumber, etSecurityNumber;
	TextView tvNextScanCard, tvHiddenNextScanCard;
	ImageView ivCardType, ivBack;

	Spinner spinnerMonth, spinnerYear;
	ArrayList<String> arr_year = new ArrayList<String>();
	ArrayList<String> arr_month = new ArrayList<String>();

	String cardtype;

	String a;
	int keyDel;

	final String APPLICATIONIDENTIFIER = "pw.cabapp.mcommerce.test";
	final String PROFILETOKEN = "505c76e7e5be4c0d83a31f575e9620b5";
	private PWProviderBinder _binder;

	PWCreditCardType temp;
	Handler mhandler;

	ProgressBar progressPayment;
	View activityRootView;

	private ServiceConnection _serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			_binder = (PWProviderBinder) service;
			// we have a connection to the service
			try {
				_binder.initializeProvider(PWProviderMode.TEST, APPLICATIONIDENTIFIER, PROFILETOKEN);
				_binder.addTokenObtainedListener(new PWTokenObtainedListener() {

					@Override
					public void obtainedToken(String token, PWTransaction arg1) {
						// TODO Auto-generated method stub
						Log.e("Token", token);
						Util.setPaymentAccessToken(mContext, token);
						// addcard_task(token, etCardNumber.getText().toString()
						// .replaceAll(" ", ""));
						addcard_task(new DES().encrypt(token, Constants.szDESSecretKey), etCardNumber.getText().toString());
					}

				});
				_binder.addTransactionListener(new PWTransactionListener() {

					@Override
					public void creationAndRegistrationFailed(PWTransaction transaction, PWError error) {

						Log.e("com.payworks.customtokenization.TokenizationActivity", error.getErrorMessage());
						if (progressPayment.isShown()) {
							progressPayment.setVisibility(View.GONE);
						}
						Toast.makeText(mContext, "Error adding card. Please try again.", Toast.LENGTH_SHORT).show();

					}

					@Override
					public void creationAndRegistrationSucceeded(PWTransaction transaction) {
						try {
							_binder.obtainToken(transaction);
						} catch (PWException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void transactionFailed(PWTransaction arg0, PWError error) {
						Log.e("com.payworks.customtokenization.TokenizationActivity", error.getErrorMessage());
						// TODO Auto-generated method stub
						if (progressPayment.isShown()) {
							progressPayment.setVisibility(View.GONE);
						}
						Toast.makeText(mContext, "Error adding card. Please try again.", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void transactionSucceeded(PWTransaction transaction) {
						// our debit succeeded

					}

				});

			} catch (PWException ee) {
				// error initializing the provider
				Log.e("Error", "Error intitalizing the provider");
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// super.initWidgets();
		setContentView(R.layout.activity_scan_card);
		mContext = this;
		mContext.startService(new Intent(mContext, com.mobile.connect.service.PWConnectService.class));
		mContext.bindService(new Intent(mContext, com.mobile.connect.service.PWConnectService.class), _serviceConnection,
				Context.BIND_AUTO_CREATE);

		activityRootView = findViewById(R.id.relMain);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
		spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
		tvNextScanCard = (TextView) findViewById(R.id.tvNextScanCard);
		tvHiddenNextScanCard = (TextView) findViewById(R.id.tvHiddenNextScanCard);
		etCardNumber = (EditText) findViewById(R.id.etCardNumber);
		etSecurityNumber = (EditText) findViewById(R.id.etSecurityNumber);
		progressPayment = (ProgressBar) findViewById(R.id.progressPayment);
		ivCardType = (ImageView) findViewById(R.id.ivCard);
		rlScanCard = (RelativeLayout) findViewById(R.id.rlScanCard);
		ivBack.setOnClickListener(new TextViewOnClickListener());
		tvNextScanCard.setOnClickListener(new TextViewOnClickListener());
		tvHiddenNextScanCard.setOnClickListener(new TextViewOnClickListener());
		rlScanCard.setOnClickListener(new TextViewOnClickListener());
		spinnerMonth.setOnTouchListener(new SpinnerOnTouchListener());
		spinnerYear.setOnTouchListener(new SpinnerOnTouchListener());

		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect r = new Rect();
				// r will be populated with the coordinates of your view
				// that area still visible.
				activityRootView.getWindowVisibleDisplayFrame(r);

				int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
				if (heightDiff > 100) {
					tvHiddenNextScanCard.setVisibility(View.VISIBLE);
					tvNextScanCard.setVisibility(View.GONE);
				} else {
					tvHiddenNextScanCard.setVisibility(View.GONE);
					tvNextScanCard.setVisibility(View.VISIBLE);
				}
			}
		});

		etCardNumber.addTextChangedListener(new TextWatcher() {

			@SuppressWarnings("deprecation")
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

				if (count == 0)
					keyDel = 1;

				boolean flag = true;
				String eachBlock[] = etCardNumber.getText().toString().trim().split(" "); // "-"
				for (int i = 0; i < eachBlock.length; i++) {
					if (eachBlock[i].length() > 4) {
						flag = false;
					}
				}
				if (flag) {

					if (keyDel == 0) {

						if (((etCardNumber.getText().length() + 1) % 5) == 0) {

							if (etCardNumber.getText().toString().trim().split(" ").length <= 4) {// "-"
								etCardNumber.setText(etCardNumber.getText() + " ");// "-"
								etCardNumber.setSelection(etCardNumber.getText().length());
							}
						}
						a = etCardNumber.getText().toString().trim();
					} else {
						a = etCardNumber.getText().toString().trim();
						keyDel = 0;
					}

				} else {
					etCardNumber.setText(a);
					etCardNumber.setSelection(etCardNumber.getText().length());
				}

				try {

					CreditCard card = new CreditCard(s.toString().trim().replaceAll(" ", ""), 0, 0, "", "");

					if (Util.cardsCodeHashMap.get(card.getCardType().toString().trim().toLowerCase()) != null)
						ivCardType.setBackgroundResource(Util.cardsCodeHashMap.get(card.getCardType().toString().trim()
								.toLowerCase()));
					else
						ivCardType.setBackgroundResource(0);

					if (card.getCardType() != null) {
						Log.e("Card Type", "" + card.getCardType());
						cardtype = card.getCardType().toString().trim();
						if (!card.getCardType().toString().trim().toUpperCase().equals("UNKNOWN")
								&& PWCreditCardType.valueOf(card.getCardType().toString().trim().toUpperCase()) != null)
							temp = PWCreditCardType.valueOf(card.getCardType().toString().trim().toUpperCase());
						Log.e("PW CARD TYPE", "" + temp);
					}

				} catch (Exception e) {

				}

			}

			//
			// @Override
			// public void onTextChanged(CharSequence s, int start, int before,
			// int count) {
			//
			// boolean flag = true;
			// String eachBlock[] = etCardNumber.getText().toString()
			// .split(" "); // "-"
			// for (int i = 0; i < eachBlock.length; i++) {
			// if (eachBlock[i].length() > 5) {// 4
			// flag = false;
			// }
			// }
			// if (flag) {
			//
			// etCardNumber.setOnKeyListener(new OnKeyListener() {
			//
			// @Override
			// public boolean onKey(View v, int keyCode, KeyEvent event) {
			//
			// if (keyCode == KeyEvent.KEYCODE_DEL)
			// keyDel = 1;
			// return false;
			// }
			// });
			//
			// if (keyDel == 0) {
			//
			// if (((etCardNumber.getText().length() + 1) % 5) == 0) {
			//
			// if (etCardNumber.getText().toString().split(" ").length <= 3) {//
			// "-"
			// etCardNumber.setText(etCardNumber.getText()
			// + " ");// "-"
			// etCardNumber.setSelection(etCardNumber
			// .getText().length());
			// }
			// }
			// a = etCardNumber.getText().toString();
			// } else {
			// a = etCardNumber.getText().toString();
			// keyDel = 0;
			// }
			//
			// } else {
			// etCardNumber.setText(a);
			// etCardNumber.setSelection(etCardNumber.getText().length());
			// }
			//
			// try {
			//
			// CreditCard card = new CreditCard(s.toString(), 0, 0, "", "");
			//
			// if (Util.cardsCodeHashMap.get(card.getCardType().toString()
			// .toLowerCase()) != null)
			// ivCardType.setBackgroundResource(Util.cardsCodeHashMap
			// .get(card.getCardType().toString()
			// .toLowerCase()));
			// else
			// ivCardType.setBackgroundResource(0);
			//
			// if (card.getCardType() != null) {
			// Log.e("Card Type", "" + card.getCardType());
			// cardtype = card.getCardType().toString();
			// if (!card.getCardType().toString().toUpperCase()
			// .equals("UNKNOWN")
			// && PWCreditCardType.valueOf(card.getCardType()
			// .toString().toUpperCase()) != null)
			// temp = PWCreditCardType.valueOf(card.getCardType()
			// .toString().toUpperCase());
			// Log.e("PW CARD TYPE", "" + temp);
			// }
			//
			// } catch (Exception e) {
			//
			// }
			//
			// }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		if (arr_year.size() <= 0) {
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			String strYear = String.valueOf(year).substring(2, 4);
			arr_year.add("YYYY");
			for (int i = Integer.parseInt(strYear); i <= Integer.parseInt(strYear) + 20; i++) {

				arr_year.add("20" + i);
			}
			if (arr_year != null && arr_year.size() > 0) {

				spinnerYear.setAdapter(new MonthYearSpinnerAdapter(mContext, year, arr_year));
			}
		}
		if (arr_month.size() <= 0) {
			arr_month.add("MM");
			for (int i = 1; i <= 12; i++) {
				if (i < 10)
					arr_month.add("0" + i);
				else
					arr_month.add("" + i);
			}
			if (arr_month != null && arr_month.size() > 0) {

				spinnerMonth.setAdapter(new MonthYearSpinnerAdapter(mContext, 0, arr_month));
			}
		}

		mhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (msg.what == 1) {
					// if (getActivity() != null)
					// getParentFragment().getChildFragmentManager()
					// .popBackStack();
					Constants.flag_added_newcard = true;
					Log.e(TAG, "Successfully card added");
					finish();
				}
			}
		};

	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tvNextScanCard:
				nextButtonCall();
				break;

			case R.id.tvHiddenNextScanCard:
				nextButtonCall();
				break;

			case R.id.ivBack:
				finish();
				break;

			case R.id.rlScanCard:
				CardScan();
				break;

			}
		}
	}

	class SpinnerOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.spinnerMonth:
				Util.hideSoftKeyBoard(mContext, etCardNumber);
				break;

			case R.id.spinnerYear:
				Util.hideSoftKeyBoard(mContext, etCardNumber);
				break;
			}
			return false;
		}

	}

	void nextButtonCall() {
		String szException = "";

		// TODO Auto-generated method stub
		PWPaymentParams paymentParams = null;
		if (etCardNumber.getText().toString().trim().length() < 16) {
			Util.showToastMessage(ScanCardActivity.this, "Card number should be minimum 13 digits", Toast.LENGTH_LONG);
		} else if (etCardNumber.getText().toString().trim().length() > 0
				&& etSecurityNumber.getText().toString().trim().length() > 0 && spinnerMonth.getSelectedItem() != null
				&& spinnerMonth.getSelectedItemPosition() != 0 && spinnerMonth.getSelectedItem().toString().length() > 0
				&& spinnerYear.getSelectedItem() != null && spinnerYear.getSelectedItemPosition() != 0
				&& spinnerYear.getSelectedItem().toString().length() > 0) {

			try {
				Log.e("Card Number", etCardNumber.getText().toString());
				progressPayment.setVisibility(View.VISIBLE);
				paymentParams = _binder.getPaymentParamsFactory().createCreditCardTokenizationParams(
						AppValues.driverDetails.getFirstname() + " " + AppValues.driverDetails.getSurname(), temp,
						etCardNumber.getText().toString().replaceAll(" ", ""), spinnerYear.getSelectedItem().toString(),
						spinnerMonth.getSelectedItem().toString(), etSecurityNumber.getText().toString());

				try {
					_binder.createAndRegisterObtainTokenTransaction(paymentParams);
				} catch (PWException e) {
					if (progressPayment.isShown()) {
						progressPayment.setVisibility(View.GONE);
					}
					szException = e.getError().getErrorMessage();
					Util.showToastMessage(mContext, szException, Toast.LENGTH_LONG);
					e.printStackTrace();
				}

			} catch (PWProviderNotInitializedException e) {
				if (progressPayment.isShown()) {
					progressPayment.setVisibility(View.GONE);
				}
				szException = e.getError().getErrorMessage();
				Util.showToastMessage(mContext, szException, Toast.LENGTH_LONG);
				e.printStackTrace();
				return;
			} catch (PWException e) {
				if (progressPayment.isShown()) {
					progressPayment.setVisibility(View.GONE);
				}
				szException = e.getError().getErrorMessage();
				Util.showToastMessage(mContext, szException, Toast.LENGTH_LONG);
				e.printStackTrace();
				return;
			}

		}

	}

	void CardScan() {

		// TODO Auto-generated method stub
		Log.e(TAG, "rlScanCard");
		if (CardIOActivity.canReadCardWithCamera(ScanCardActivity.this)) {
			// scanButton.setText("Scan a credit card with card.io");

			Intent scanIntent = new Intent(ScanCardActivity.this, CardIOActivity.class);

			// required for authentication with card.io
			scanIntent.putExtra(CardIOActivity.EXTRA_APP_TOKEN, MY_CARDIO_APP_TOKEN);

			// customize these values to suit your needs.
			scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default:
																			// true
			scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default:
																			// false
			scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default:
																					// false

			// hides the manual entry button
			// if set, developers should provide their own manual entry
			// mechanism in
			// the app
			scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default:
																					// false

			// MY_SCAN_REQUEST_CODE is arbitrary and is only used within
			// this
			// activity.
			startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);

		} else {
			// scanButton.setText("Enter credit card information");
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (spinnerMonth != null && spinnerYear != null) {
			spinnerYear.setAdapter(null);
			spinnerMonth.setAdapter(null);
		}

		if (arr_year != null && arr_year.size() > 0 || arr_month != null && arr_month.size() > 0) {
			arr_year.clear();
			arr_month.clear();
		}

		mContext.unbindService(_serviceConnection);
		mContext.stopService(new Intent(mContext, com.mobile.connect.service.PWConnectService.class));
	}

	public void addcard_task(final String token, final String cardnumber) {
		// TODO Auto-generated method stub

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (progressPayment.isShown()) {
					progressPayment.setVisibility(View.GONE);
				}

				if (NetworkUtil.isNetworkOn(Util.mContext)) {
					AddNewCardTask card = new AddNewCardTask(mContext);
					card.cardType = cardtype;
					card.token = token;
					card.truncatedPan = cardnumber.substring(cardnumber.trim().length() - 4, cardnumber.trim().length());
					card.handler = mhandler;
					card.execute();
					Log.e("Truncated Pan", cardnumber.substring(cardnumber.trim().length() - 4, cardnumber.trim().length()));
				} else {
					Util.showToastMessage(mContext, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String resultStr;
		if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
			CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

			// Never log a raw card number. Avoid displaying it, but if
			// necessary use getFormattedCardNumber()
			// resultStr = "Card Number: " + scanResult.getRedactedCardNumber()
			// + "\n";
			resultStr = scanResult.getFormattedCardNumber();
			Log.e(TAG, "Card Number: " + resultStr);

			// Do something with the raw number, e.g.:
			// myService.setCardNumber( scanResult.cardNumber );

			if (scanResult.isExpiryValid()) {
				// resultStr += "Expiration Date: " + scanResult.expiryMonth +
				// "/"
				// + scanResult.expiryYear + "\n";
				int expMonth = scanResult.expiryMonth;
				int expYear = scanResult.expiryYear;
				if (expMonth != 0) {
					for (int i = 1; i < arr_month.size(); i++) {
						if (expMonth == Integer.valueOf(arr_month.get(i))) {
							spinnerMonth.setSelection(i);
							break;
						}
					}
				}
				if (expYear != 0) {
					for (int i = 1; i < arr_year.size(); i++) {
						if (expYear == Integer.valueOf(arr_year.get(i))) {
							spinnerYear.setSelection(i);
							break;
						}
					}
				}

			}

			// if (scanResult.cvv != null) {
			// // Never log or display a CVV
			// resultStr += "CVV has " + scanResult.cvv.length()
			// + " digits.\n";
			// }

			// if (scanResult.postalCode != null) {
			// resultStr += "Postal Code: " + scanResult.postalCode + "\n";
			// }
		} else {
			// resultStr = "Scan was canceled.";
			resultStr = "";
		}
		etCardNumber.setText(resultStr);
		etCardNumber.setSelection(etCardNumber.getText().length());
		Util.hideSoftKeyBoard(mContext, etCardNumber);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

		if (arr_year.size() <= 0) {
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			String strYear = String.valueOf(year).substring(2, 4);
			arr_year.add("YYYY");
			for (int i = Integer.parseInt(strYear); i <= Integer.parseInt(strYear) + 20; i++) {

				arr_year.add("20" + i);
			}
			if (arr_year != null && arr_year.size() > 0) {

				spinnerYear.setAdapter(new MonthYearSpinnerAdapter(mContext, year, arr_year));
			}
		}
		if (arr_month.size() <= 0) {
			arr_month.add("MM");
			for (int i = 1; i <= 12; i++) {
				if (i < 10)
					arr_month.add("0" + i);
				else
					arr_month.add("" + i);
			}
			if (arr_month != null && arr_month.size() > 0) {

				spinnerMonth.setAdapter(new MonthYearSpinnerAdapter(mContext, 0, arr_month));
			}
		}
	}

}
