package com.android.cabapp.activity;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.adapter.MonthYearSpinnerAdapter;
import com.android.cabapp.async.AddNewCardTask;
import com.android.cabapp.async.PaymentTask;
import com.android.cabapp.async.SendReceiptTask;
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

public class AddScanCardJobsActivity extends RootActivity {
	String TAG = AddScanCardJobsActivity.class.getName();

	private static final String MY_CARDIO_APP_TOKEN = "d1e2d2384af74d9fa58580784e05c1ed";
	private int MY_SCAN_REQUEST_CODE = 100;

	static Context _mContext;
	ImageView ivBack;

	Spinner spinnerMonth, spinnerYear;
	ArrayList<String> arr_year = new ArrayList<String>();
	ArrayList<String> arr_month = new ArrayList<String>();
	RelativeLayout rlScanCard;
	EditText etCardNumber, etSecurityNumber;
	TextView tvNextScanCard, tvHiddenNextScanCard;
	ImageView ivCardType;

	String cardtype = "", szToken = "", szFeesPaidBy = "";
	public static String cardTypeReturned = "", truncatedPan = "";
	public static String tokenReturned = "";
	public static String walkUpJobId = "";
	public static long cashBackReturned = 0;
	int isRegisteredCard = 0;

	public static boolean bIsWalkUp = false;

	String a;
	int keyDel;

	final String APPLICATIONIDENTIFIER = "pw.cabapp.mcommerce.test";
	final String PROFILETOKEN = "505c76e7e5be4c0d83a31f575e9620b5";

	PWCreditCardType temp;
	Handler mhandler;
	PWProviderBinder _binder;
	ProgressDialog progressDialog;
	View activityRootView;

	Bundle mBundle;

	private ServiceConnection _serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// we have a connection to the service
			_binder = (PWProviderBinder) service;
			try {

				_binder.initializeProvider(PWProviderMode.TEST,
						APPLICATIONIDENTIFIER, PROFILETOKEN);
				_binder.addTokenObtainedListener(new PWTokenObtainedListener() {

					@Override
					public void obtainedToken(final String token,
							PWTransaction arg1) {
						// TODO Auto-generated method stub
						Log.e("Token", token);
						szToken = token;
						// addcard_task(token,
						// etCardNumber.getText().toString()
						// .replaceAll(" ", ""));
						addcard_task(new DES().encrypt(token,
								Constants.szDESSecretKey), etCardNumber
								.getText().toString());
					}

				});
				_binder.addTransactionListener(new PWTransactionListener() {

					@Override
					public void creationAndRegistrationFailed(
							PWTransaction transaction, PWError error) {

						Log.e("com.payworks.customtokenization.TokenizationActivity",
								error.getErrorMessage());

						runOnUiThread(new Runnable() {
							public void run() {
								if (progressDialog != null)
									progressDialog.dismiss();

								Toast.makeText(_mContext,
										"Error adding card. Please try again.",
										Toast.LENGTH_SHORT).show();
							}
						});

					}

					@Override
					public void creationAndRegistrationSucceeded(
							PWTransaction transaction) {
						try {
							_binder.obtainToken(transaction);
						} catch (PWException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void transactionFailed(PWTransaction arg0,
							PWError error) {
						Log.e("com.payworks.customtokenization.TokenizationActivity",
								error.getErrorMessage());
						// TODO Auto-generated method stub

						runOnUiThread(new Runnable() {
							public void run() {

								if (progressDialog != null)
									progressDialog.dismiss();

								Util.showToastMessage(_mContext,
										"Error adding card. Please try again.",
										Toast.LENGTH_SHORT);
							}
						});

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
			// }
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.e("AddScanCard", "onServiceDisconnected");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_scan_card_jobs);
		_mContext = this;
		_mContext.startService(new Intent(_mContext,
				com.mobile.connect.service.PWConnectService.class));
		_mContext.bindService(new Intent(_mContext,
				com.mobile.connect.service.PWConnectService.class),
				_serviceConnection, Context.BIND_AUTO_CREATE);

		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			if (Constants.isDebug)
				Log.e(TAG, "Bundle::> " + mBundle.toString());
		}
		if (mBundle != null) {
			if (mBundle.containsKey(Constants.CABPAY_WALKUP)) {
				bIsWalkUp = mBundle.getBoolean(Constants.CABPAY_WALKUP);
				isRegisteredCard = 0;

			} else if (mBundle.containsKey(Constants.JOB_ID)
					&& mBundle.getString(Constants.JOB_ID) != null) {
				bIsWalkUp = false;
				isRegisteredCard = 0;
			}

			if (AppValues.driverSettings != null
					&& AppValues.driverSettings.getCardPaymentFeePaidBy() != null)
				szFeesPaidBy = AppValues.driverSettings
						.getCardPaymentFeePaidBy();
		}

		ivBack = (ImageView) findViewById(R.id.ivBack);
		spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
		spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
		etCardNumber = (EditText) findViewById(R.id.etCardNumber);
		etSecurityNumber = (EditText) findViewById(R.id.etSecurityNumber);
		ivCardType = (ImageView) findViewById(R.id.ivCard);
		rlScanCard = (RelativeLayout) findViewById(R.id.rlScanCard);
		tvNextScanCard = (TextView) findViewById(R.id.tvNextScanCard);
		tvHiddenNextScanCard = (TextView) findViewById(R.id.tvHiddenNextScanCard);
		activityRootView = findViewById(R.id.relMain);
		rlScanCard.setOnClickListener(new TextViewOnClickListener());
		tvNextScanCard.setOnClickListener(new TextViewOnClickListener());
		tvHiddenNextScanCard.setOnClickListener(new TextViewOnClickListener());
		ivBack.setOnClickListener(new TextViewOnClickListener());
		spinnerMonth.setOnTouchListener(new SpinnerOnTouchListener());
		spinnerYear.setOnTouchListener(new SpinnerOnTouchListener());

		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						// r will be populated with the coordinates of your view
						// that area still visible.
						activityRootView.getWindowVisibleDisplayFrame(r);

						int heightDiff = activityRootView.getRootView()
								.getHeight() - (r.bottom - r.top);
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
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

				if (count == 0)
					keyDel = 1;

				boolean flag = true;
				String eachBlock[] = etCardNumber.getText().toString().trim()
						.split(" "); // "-"
				for (int i = 0; i < eachBlock.length; i++) {
					if (eachBlock[i].length() > 4) {
						flag = false;
					}
				}
				if (flag) {

					if (keyDel == 0) {

						if (((etCardNumber.getText().length() + 1) % 5) == 0) {

							if (etCardNumber.getText().toString().trim()
									.split(" ").length <= 4) {// "-"
								etCardNumber.setText(etCardNumber.getText()
										+ " ");// "-"
								etCardNumber.setSelection(etCardNumber
										.getText().length());
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

					CreditCard card = new CreditCard(s.toString().trim()
							.replaceAll(" ", ""), 0, 0, "", "");

					if (Util.cardsCodeHashMap.get(card.getCardType().toString()
							.trim().toLowerCase()) != null)
						ivCardType.setBackgroundResource(Util.cardsCodeHashMap
								.get(card.getCardType().toString().trim()
										.toLowerCase()));
					else
						ivCardType.setBackgroundResource(0);

					if (card.getCardType() != null) {
						Log.e("Card Type", "" + card.getCardType());
						cardtype = card.getCardType().toString().trim();
						if (!card.getCardType().toString().trim().toUpperCase()
								.equals("UNKNOWN")
								&& PWCreditCardType.valueOf(card.getCardType()
										.toString().trim().toUpperCase()) != null)
							temp = PWCreditCardType.valueOf(card.getCardType()
									.toString().trim().toUpperCase());
						Log.e("PW CARD TYPE", "" + temp);
					}

				} catch (Exception e) {

				}
			}

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
			// Log.e("Card Type",
			// "" + card.getCardType() + "  truncatedPan: "
			// + card.getLastFourDigitsOfCardNumber());
			// cardtype = card.getCardType().toString();
			// truncatedPan = card.getLastFourDigitsOfCardNumber();
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
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
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
			for (int i = Integer.parseInt(strYear); i <= Integer
					.parseInt(strYear) + 20; i++) {

				arr_year.add("20" + i);
			}
			if (arr_year != null && arr_year.size() > 0) {

				spinnerYear.setAdapter(new MonthYearSpinnerAdapter(_mContext,
						year, arr_year));
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

				spinnerMonth.setAdapter(new MonthYearSpinnerAdapter(_mContext,
						0, arr_month));
			}
		}

		mhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle mBundleReturned;
				// if (msg.what == 1 || msg.what == 0) {
				// Util.bIsTokenObtained = false;
				// }

				if (msg.what == 1) {
					Constants.flag_added_newcard = true;
					Log.e(TAG, "Successfully card added");

					mBundle.putString(Constants.PAYMENT_ACCESS_TOKEN_VALUE,
							szToken);
					mBundle.putString(Constants.CABPAY_IS_CARD_REGISTERED,
							String.valueOf(isRegisteredCard));
					mBundle.putString(Constants.CABPAY_FEES_PAID_BY,
							szFeesPaidBy);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (NetworkUtil.isNetworkOn(Util.mContext)) {

								PaymentTask paymentTask = new PaymentTask();

								if (!bIsWalkUp) {
									paymentTask.jobId = mBundle
											.getString(Constants.JOB_ID);
									if (mBundle != null
											&& mBundle
													.containsKey(Constants.CAB_MILES)
											&& mBundle
													.getString(Constants.CAB_MILES) != null)
										paymentTask.cabmiles = mBundle
												.getString(Constants.CAB_MILES);

								}
								paymentTask.meterAmount = mBundle
										.getString(Constants.METER_VALUE);
								paymentTask.totalAmount = mBundle
										.getString(Constants.TOTAL_VALUE);
								paymentTask.tip = mBundle
										.getString(Constants.TIP_VALUE);
								paymentTask.cardFees = mBundle
										.getString(Constants.CARD_FEE_VALUE);
								paymentTask.szFeesPaidBy = szFeesPaidBy;
								paymentTask.szCardPayworkToken = tokenReturned;
								paymentTask.isRegisteredCard = String
										.valueOf(isRegisteredCard);
								paymentTask.szCardBrand = cardTypeReturned;
								paymentTask.szTruncatedPan = truncatedPan;
								paymentTask.mHandler = mhandler;
								paymentTask.isWalkUp = bIsWalkUp;

								// brand =cardtype
								// truncatedPan

								paymentTask.execute();
							} else {
								Util.showToastMessage(
										Util.mContext,
										getResources().getString(
												R.string.no_network_error),
										Toast.LENGTH_LONG);
							}
						}
					});
				} else if (msg.what == 2) {
					mBundleReturned = (Bundle) msg.getData();
					if (mBundleReturned != null
							&& mBundleReturned.getBoolean("success")) {
						Log.e(TAG,
								"mBundleReturned: "
										+ mBundleReturned.toString());
						walkUpJobId = mBundleReturned.getString("jobId");
						cashBackReturned = mBundleReturned
								.getLong("cashBackReturned");
						if (bIsWalkUp)
							ShowSendReceiptDialog();
						else
							CallSendReceipt();
					} else {
						Log.e(TAG, "Error: Walk up payment");
					}
				}
			}
		};

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}

		if (spinnerMonth != null && spinnerYear != null) {
			spinnerYear.setAdapter(null);
			spinnerMonth.setAdapter(null);
		}

		if (arr_year != null && arr_year.size() > 0 || arr_month != null
				&& arr_month.size() > 0) {
			arr_year.clear();
			arr_month.clear();
		}
		_mContext.unbindService(_serviceConnection);
		_mContext.stopService(new Intent(_mContext,
				com.mobile.connect.service.PWConnectService.class));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		_mContext = this;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		String resultStr;
		if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
			CreditCard scanResult = data
					.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

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
		} else {
			resultStr = "";
		}
		etCardNumber.setText(resultStr);
		etCardNumber.setSelection(etCardNumber.getText().length());
		Util.hideSoftKeyBoard(_mContext, etCardNumber);

	}

	void ShowSendReceiptDialog() {

		final AlertDialog receiptDialog = new AlertDialog.Builder(_mContext)
				.setMessage("Would you like to send passenger a receipt?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {

								// TODO Auto-generated
								if (mBundle == null)
									mBundle = new Bundle();

								mBundle.putString(Constants.JOB_ID, walkUpJobId);
								SendReceiptTask.bIsWalkUp = bIsWalkUp;

								((AddScanCardJobsActivity) _mContext).finish();
								Fragment fragment = null;
								fragment = new com.android.cabapp.fragments.CabPayReceiptFragment();
								fragment.setArguments(mBundle);
								if (fragment != null)
									((MainActivity) Util.mContext)
											.replaceFragment(fragment, true);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated
						CallSendReceipt();

					}
				}).create();
		receiptDialog.setCanceledOnTouchOutside(false);
		receiptDialog.show();
	}

	void CallSendReceipt() {

		if (NetworkUtil.isNetworkOn(Util.mContext)) {

			SendReceiptTask sendReceiptTask = new SendReceiptTask();

			SendReceiptTask.bIsWalkUp = bIsWalkUp;
			Log.e(TAG, "CallSendReceipt bIsWalkUp: " + bIsWalkUp);
			if (bIsWalkUp) {
				sendReceiptTask.mContext = Util.mContext;
				sendReceiptTask.szJobID = walkUpJobId;
				sendReceiptTask.szType = "none";
				sendReceiptTask.szEmail = "";
				sendReceiptTask.szMobile = "";
				sendReceiptTask.szInternationalCode = "";
				sendReceiptTask.szPaymentType = "card";
				sendReceiptTask.szPU = "";
				sendReceiptTask.szDO = "";
			} else {
				sendReceiptTask.szType = "email";
				sendReceiptTask.szEmail = mBundle
						.getString(Constants.PASSENGER_EMAIL);
				sendReceiptTask.szMobile = "";
				sendReceiptTask.szInternationalCode = "";

				sendReceiptTask.mContext = Util.mContext;
				sendReceiptTask.szJobID = mBundle.getString(Constants.JOB_ID);
			}
			sendReceiptTask.szAmount = String.valueOf(mBundle
					.getString(Constants.TOTAL_VALUE));
			sendReceiptTask.szTip = String.valueOf(mBundle
					.getString(Constants.TIP_VALUE));
			sendReceiptTask.szFee = String.valueOf(mBundle
					.getString(Constants.CARD_FEE_VALUE));

			sendReceiptTask.execute();

			((AddScanCardJobsActivity) _mContext).finish();
		} else {
			Util.showToastMessage(Util.mContext,
					getResources().getString(R.string.no_network_error),
					Toast.LENGTH_LONG);
		}
	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.rlScanCard:

				// TODO Auto-generated method stub
				Log.e(TAG, "rlScanCard");
				if (CardIOActivity
						.canReadCardWithCamera(AddScanCardJobsActivity.this)) {
					Intent scanIntent = new Intent(
							AddScanCardJobsActivity.this, CardIOActivity.class);

					// required for authentication with card.io
					scanIntent.putExtra(CardIOActivity.EXTRA_APP_TOKEN,
							MY_CARDIO_APP_TOKEN);

					// customize these values to suit your needs.
					scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY,
							true); // default:
									// true
					scanIntent
							.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false);

					scanIntent.putExtra(
							CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);

					scanIntent.putExtra(
							CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false);
					startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);

				} else {
				}

				break;

			case R.id.tvNextScanCard:
				nextButtonCall();

				break;

			case R.id.tvHiddenNextScanCard:
				nextButtonCall();

				break;

			case R.id.ivBack:
				finish();
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
				Util.hideSoftKeyBoard(_mContext, etCardNumber);
				break;

			case R.id.spinnerYear:
				Util.hideSoftKeyBoard(_mContext, etCardNumber);
				break;
			}
			return false;
		}

	}

	void nextButtonCall() {
		String szException = "";

		Util.hideSoftKeyBoard(_mContext, tvNextScanCard);
		PWPaymentParams paymentParams = null;
		if (etCardNumber.getText().toString().trim().length() < 16) {
			Util.showToastMessage(_mContext,
					"Card number should be minimum 13 digits",
					Toast.LENGTH_LONG);
		} else if (etCardNumber.getText().toString().trim().length() > 0
				&& etSecurityNumber.getText().toString().trim().length() > 0
				&& spinnerMonth.getSelectedItem() != null
				&& spinnerMonth.getSelectedItemPosition() != 0
				// && spinnerMonth.getSelectedItem().toString().length() > 0
				&& spinnerYear.getSelectedItem() != null
				&& spinnerYear.getSelectedItemPosition() != 0
				&& spinnerYear.getSelectedItem().toString().length() > 0) {

			try {
				Log.e("Card Number", etCardNumber.getText().toString());

				progressDialog = new ProgressDialog(_mContext);
				progressDialog.setMessage("Loading...");
				progressDialog.setCancelable(false);
				progressDialog.show();

				if (AppValues.driverDetails != null
						&& AppValues.driverDetails.getFirstname() != null) {
					paymentParams = _binder
							.getPaymentParamsFactory()
							.createCreditCardTokenizationParams(
									AppValues.driverDetails.getFirstname()
											+ " "
											+ AppValues.driverDetails
													.getSurname(),
									temp,
									etCardNumber.getText().toString()
											.replaceAll(" ", ""),
									spinnerYear.getSelectedItem().toString(),
									spinnerMonth.getSelectedItem().toString(),
									etSecurityNumber.getText().toString());

					try {
						_binder.createAndRegisterObtainTokenTransaction(paymentParams);
					} catch (PWException e) {
						if (progressDialog != null)
							progressDialog.dismiss();
						szException = e.getError().getErrorMessage();
						Util.showToastMessage(_mContext, szException,
								Toast.LENGTH_LONG);
						e.printStackTrace();
					}
				} else {
					if (progressDialog != null)
						progressDialog.dismiss();
					Util.showToastMessage(_mContext,
							"Error fetching data. Please try again",
							Toast.LENGTH_LONG);
				}

			} catch (PWProviderNotInitializedException e) {
				if (progressDialog != null)
					progressDialog.dismiss();
				szException = e.getError().getErrorMessage();
				Util.showToastMessage(_mContext, szException, Toast.LENGTH_LONG);
				e.printStackTrace();
				return;
			} catch (PWException e) {
				if (progressDialog != null)
					progressDialog.dismiss();
				szException = e.getError().getErrorMessage();
				Util.showToastMessage(_mContext, szException, Toast.LENGTH_LONG);
				e.printStackTrace();
				return;
			}
		} else {
			Util.showToastMessage(_mContext, "Please complete all fields",
					Toast.LENGTH_LONG);
		}
	}

	public void addcard_task(final String token, final String cardnumber) {
		// TODO Auto-generated method stub

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AddNewCardTask addCardTask = new AddNewCardTask(_mContext);

				ProgressDialog pDialog = new ProgressDialog(Util.mContext);
				pDialog.setCanceledOnTouchOutside(false);
				pDialog.setCancelable(false);
				pDialog.setMessage("Loading...");
				pDialog.show();
				addCardTask.mDialog = pDialog;

				if (NetworkUtil.isNetworkOn(Util.mContext)) {
					addCardTask.cardType = cardtype;
					addCardTask.token = token;
					truncatedPan = cardnumber.substring(cardnumber.trim()
							.length() - 4, cardnumber.trim().length());
					addCardTask.truncatedPan = truncatedPan;
					addCardTask.handler = mhandler;
					addCardTask.execute();
					Log.e("Truncated Pan", cardnumber.substring(cardnumber
							.trim().length() - 4, cardnumber.trim().length()));
				} else {
					Util.showToastMessage(
							_mContext,
							getResources().getString(R.string.no_network_error),
							Toast.LENGTH_LONG);
				}
			}
		});
	}
}
