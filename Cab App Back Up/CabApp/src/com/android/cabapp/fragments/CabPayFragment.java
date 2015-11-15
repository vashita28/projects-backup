package com.android.cabapp.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.AddScanCardJobsActivity;
import com.android.cabapp.activity.DeviceListActivity;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.adapter.PaymentTypeAdapter;
import com.android.cabapp.async.ChipAndPinTask;
import com.android.cabapp.async.GetPaymentURLTask;
import com.android.cabapp.async.PaymentTask;
import com.android.cabapp.async.SendReceiptTask;
import com.android.cabapp.datastruct.json.Card;
import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.handpoint.CallbackListener;
import com.android.cabapp.handpoint.HandPointClass;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.handpoint.api.Currency;
import com.handpoint.api.TransactionResult;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class CabPayFragment extends RootFragment {
	private static final String TAG = CabPayFragment.class.getSimpleName();

	Spinner spinnerPaymentMethod;
	TextView textAdditionalCardFees, textTotal, textSMS, textEmail,
			textTotalValue, tvEmail_SMS, tvSendReceipt, textMeterSymbol,
			textTipSymbol, textTotalSymbol, textReceiptYes, textReceiptNo;

	EditText etMeterValue, etTipValue, etSRPickUpAdd1, etSRPickUpAdd2,
			etSRDropOffAdd1, etSRDropOffAdd2; // etSREmailOrCellNumber
	RelativeLayout rlTakePayment, rlCashReceipt;// rlCashReceipt,rlContent,rlBottomBar
	private PaymentTypeAdapter adapterPaymentType;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	boolean isEmail = false, isCard = false, isTipAdded = false,
			isFromFareCalculator = false;
	float cardFees = 0, totalValue = 0;

	public static Handler mHandler;

	String szJobID = "", szCardBrand = "", szTruncatedPan = "";
	int spinnerItemSelectedPosition = -1;
	List<Card> cardsList;
	String symbol = "", szPaymentMethod = "";
	// String jobPaymentType = "";

	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	private static final int REQUEST_CONNECT_TO_PED = 1;
	private static final int REQUEST_ENABLE_BT = 3;

	ProgressDialog mConnectingDialog;

	String szMeterAmt = "", szTipValue = "", szTotalValue = "";

	// Sending while calling SendReceipt
	String sJobId = "", sType = "", sAmount = "", sTip = "0", sFees = "",
			sEmail = "", sMobile = "", sInterCode = "", sPaymentType = "",
			sPickUp = "", sDropOff = "";

	HandPointClass mHandPointClass;
	Timer handpointTimer, connectionTimeoutTimer;

	boolean bIsPaymentProcessOn = false;

	String szMinimumCardFees = "", additionalCardText = "";
	float fMinimumCardFees = 0, fCabMiles = 0;

	// Overlay
	RelativeLayout rlCabAppOverlayHeader, rlCabPayOkyGotIt;

	public CabPayFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Util.hideSoftKeyBoard(Util.mContext, etMeterValue);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// getActivity().getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
		// | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		View rootView = inflater.inflate(R.layout.fragment_cab_pay, container,
				false);
		super.initWidgets(rootView, this.getClass().getName());

		// Overlay:
		rlCabAppOverlayHeader = (RelativeLayout) rootView
				.findViewById(R.id.rlCabAppOverlayHeader);
		rlCabPayOkyGotIt = (RelativeLayout) rootView
				.findViewById(R.id.rlCabPayOkyGotIt);
		rlCabPayOkyGotIt.setOnTouchListener(new TextTouchListener());

		textCashBack.setVisibility(View.GONE);
		rlTakePayment = (RelativeLayout) rootView
				.findViewById(R.id.rlTakePaymentBottom);
		rlCashReceipt = (RelativeLayout) rootView
				.findViewById(R.id.rlCashReceiptBottom);

		// textSMS = (TextView) rootView.findViewById(R.id.textSMS);
		// textEmail = (TextView) rootView.findViewById(R.id.textEmail);
		textAdditionalCardFees = (TextView) rootView
				.findViewById(R.id.tv_AdditionalCardFees);

		textTotal = (TextView) rootView.findViewById(R.id.tv_Total);
		spinnerPaymentMethod = (Spinner) rootView
				.findViewById(R.id.spinner_selectPayment);
		// rlContent = (RelativeLayout) rootView.findViewById(R.id.rlContent);

		// rlCashReceipt = (RelativeLayout) rootView
		// .findViewById(R.id.rlCashReceiptLayout);

		etMeterValue = (EditText) rootView
				.findViewById(R.id.etMeterAmountValue);
		etTipValue = (EditText) rootView.findViewById(R.id.etTipValue);
		textTotalValue = (TextView) rootView.findViewById(R.id.tvTotalValue);

		// tvEmail_SMS = (TextView) rootView.findViewById(R.id.tvEmail_SMS);
		// rlBottomBar = (RelativeLayout)
		// rootView.findViewById(R.id.rlbottombar);
		// rlBottomBar2 = (RelativeLayout) rootView
		// .findViewById(R.id.rlbottombar2);
		// etSREmailOrCellNumber = (EditText) rootView
		// .findViewById(R.id.etEmailOrCellNumber);
		// etSRPickUpAdd1 = (EditText) rootView.findViewById(R.id.etPickUpAdd1);
		// etSRPickUpAdd2 = (EditText) rootView.findViewById(R.id.etPickUpAdd2);
		// etSRDropOffAdd1 = (EditText)
		// rootView.findViewById(R.id.etDropOffAdd1);
		// etSRDropOffAdd2 = (EditText)
		// rootView.findViewById(R.id.etDropOffAdd2);
		// tvSendReceipt = (TextView) rootView.findViewById(R.id.tvSendReceipt);

		textMeterSymbol = (TextView) rootView.findViewById(R.id.txtMeterSymbol);
		textTipSymbol = (TextView) rootView.findViewById(R.id.txtTipSymbol);
		textTotalSymbol = (TextView) rootView.findViewById(R.id.txtTotalSymbol);

		// Locale loc = new Locale("en", "UK");
		// if (loc.getCountry().equals("UK")) {
		// loc = new Locale(loc.getLanguage(), "GB");
		// }
		// Currency currency = Currency.getInstance(loc);
		// String symbol = currency.getSymbol();

		if (AppValues.driverSettings != null) {
			symbol = AppValues.driverSettings.getCurrencySymbol();
			textMeterSymbol.setText(symbol);
			textTipSymbol.setText(symbol);
			textTotalSymbol.setText(symbol);
		}

		// String additionalCardText =
		// "<font color=#f5f5f5>+ </font> <font color=#fd6f01>"
		// + symbol
		// + "0.00</font> <font color=#f5f5f5> additional card fee</font>";
		// textAdditionalCardFees.setText(Html.fromHtml(additionalCardText));

		String totalText = "<font color=#fd6f01>= </font> <font color=#f5f5f5> Total</font>";
		textTotal.setText(Html.fromHtml(totalText));

		spinnerPaymentMethod
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View v,
							int position, long id) {
						// TODO Auto-generated method stub
						setSpinnerFirstItemBackground(parent);

						szPaymentMethod = ((TextView) v
								.findViewById(R.id.tvContent)).getText()
								.toString();
						if (szPaymentMethod.equals("Cash")) {
							textAdditionalCardFees
									.setVisibility(View.INVISIBLE);
							isCard = false;
							// textNext.setEnabled(false);
							rlTakePayment.setVisibility(View.GONE);
							rlCashReceipt.setVisibility(View.VISIBLE);
						} else {
							spinnerItemSelectedPosition = position;
							textAdditionalCardFees.setVisibility(View.VISIBLE);
							isCard = true;
							// textNext.setEnabled(true);
							rlCashReceipt.setVisibility(View.GONE);
							rlTakePayment.setVisibility(View.VISIBLE);
						}

						String szMeterValue = "0", szTipValue = "0";
						if (etMeterValue.getText().toString().length() > 0) {
							szMeterValue = etMeterValue.getText().toString();
						}

						if (etTipValue.getText().toString().length() > 0) {
							szTipValue = etTipValue.getText().toString();
						}

						if (fCabMiles > 0) {
							totalValue = Float.parseFloat(szMeterValue)
									+ Float.parseFloat(szTipValue) - fCabMiles;
						} else
							totalValue = Float.parseFloat(szMeterValue)
									+ Float.parseFloat(szTipValue);

						if (isCard) {
							totalValue = Float.parseFloat(String.format(
									Locale.ENGLISH, "%.2f",
									(float) (totalValue + cardFees)));
							isTipAdded = true;
						} else {
							isTipAdded = false;
						}

						if (Constants.isDebug)
							Log.e(CabPayFragment.class.getName(),
									"Meter Value:: " + szMeterValue
											+ " TipValue:: " + szTipValue
											+ " Total Value:: " + totalValue
											+ " Card Fees:: " + cardFees);

						textTotalValue.setText(String.format(Locale.ENGLISH,
								"%.2f", totalValue));

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						setSpinnerFirstItemBackground(parent);
					}
				});

		if (!Util.getIsOverlaySeen(getActivity(), TAG)) {
			rlCabAppOverlayHeader.setVisibility(View.VISIBLE);
			rlCabPayOkyGotIt.setVisibility(View.VISIBLE);
			rlTakePayment.setEnabled(false);
			spinnerPaymentMethod.setEnabled(false);
			etMeterValue.setEnabled(false);
			etTipValue.setEnabled(false);

			if (MainActivity.slidingMenu != null)
				MainActivity.slidingMenu
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		bIsPaymentProcessOn = false;
		return rootView;
	}

	void setInitialCardFeesText() {
		if (AppValues.driverSettings != null) {
			szMinimumCardFees = AppValues.driverSettings
					.getCardPaymentMinimumAmount();
			fMinimumCardFees = Float.valueOf(szMinimumCardFees);
		}
		/* if cabmiles coming */
		if (fCabMiles > 0) {
			additionalCardText = "<font color=#f5f5f5>"
					+ "Card fee 3.90% min fee=  </font>"
					+ " <font color=#fd6f01>" + symbol
					+ String.format(Locale.ENGLISH, "%.2f", fMinimumCardFees)
					+ "</font>" + "<br>" + "<font color=#f5f5f5>"
					+ "cab:miles =  </font>" + " <font color=#fd6f01>" + "-"
					+ symbol + String.format(Locale.ENGLISH, "%.2f", fCabMiles)
					+ "</font>";
			textAdditionalCardFees.setText(Html.fromHtml(additionalCardText));
		} else {
			additionalCardText = "<font color=#f5f5f5>"
					+ "Card fee 3.90% min fee=  </font>"
					+ " <font color=#fd6f01>" + symbol
					+ String.format(Locale.ENGLISH, "%.2f", fMinimumCardFees)
					+ "</font>";
			textAdditionalCardFees.setText(Html.fromHtml(additionalCardText));
		}
		// additionalCardText = "<font color=#f5f5f5>"
		// + "Card fee 3.90% min fee=  </font>" + " <font color=#fd6f01>"
		// + symbol
		// + String.format(Locale.ENGLISH, "%.2f", fMinimumCardFees)
		// + "</font>";// Minimum Card Fee
		// textAdditionalCardFees.setText(Html.fromHtml(additionalCardText));
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		String szJobPaymentType = "";

		bIsPaymentProcessOn = false;
		mMainBundle = this.getArguments();
		if (mMainBundle != null) {
			if (Constants.isDebug)
				Log.e(TAG, "Main Bundle::> " + mMainBundle.toString());
		}

		if (mMainBundle != null
				&& mMainBundle.containsKey(Constants.JOB_PAYMENT_TYPE))
			szJobPaymentType = mMainBundle
					.getString(Constants.JOB_PAYMENT_TYPE);

		if (mMainBundle != null && mMainBundle.containsKey(Constants.CAB_MILES)
				&& mMainBundle.get(Constants.CAB_MILES) != null)
			fCabMiles = Float.valueOf(mMainBundle.get(Constants.CAB_MILES)
					.toString());

		etMeterValue.setFilters(new InputFilter[] { new DecimalFilter() });
		etTipValue.setFilters(new InputFilter[] { new DecimalFilter() });

		rlTakePayment.setOnTouchListener(new TextTouchListener());
		rlCashReceipt.setOnTouchListener(new TextTouchListener());
		// textSendCashReceipt.setOnTouchListener(new TextTouchListener());
		// textSMS.setOnTouchListener(new TextTouchListener());
		// textEmail.setOnTouchListener(new TextTouchListener());
		// rlCashReceipt.setOnTouchListener(new TextTouchListener());
		etMeterValue.addTextChangedListener(new MeterTextWatcher());
		etTipValue.addTextChangedListener(new MeterTextWatcher());
		// tvSendReceipt.setOnTouchListener(new TextTouchListener());

		etMeterValue.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					setRoundOffMeterValue();
				}
			}
		});

		etTipValue.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus)
					etTipValue.setCursorVisible(true);
			}
		});

		etMeterValue.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					setRoundOffMeterValue();
				}
				return false;
			}
		});

		etTipValue.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					setRoundOffTipValue();
				}
				return false;
			}
		});

		cardsList = new ArrayList<Card>();
		cardsList.clear();

		String paymentType = "";
		if (AppValues.driverDetails != null) {
			paymentType = AppValues.driverDetails.getPaymentType();
			if (paymentType.toLowerCase().equals("both")) {
				/* job coming from myjobs-fragment flow, passenger's card added */
				if (mMainBundle != null
						&& mMainBundle.containsKey(Constants.IS_FROM_MY_JOBS)
						&& mMainBundle.getBoolean(Constants.IS_FROM_MY_JOBS)) {

					cardsList.addAll(Util.getCardsList());
					setSpinnerFromCardList(paymentType);
				}
				/* job coming from now-fragment flow, passenger's card added */
				else if (mMainBundle != null
						&& mMainBundle.containsKey(Constants.JOB_ID)
						&& Util.getCardsList() != null
						&& Util.getCardsList().size() > 0) {
					cardsList.addAll(Util.getCardsList());
					setSpinnerFromCardList(paymentType);
				} else if (AppValues.mNowJobsList != null
						&& mMainBundle != null
						&& mMainBundle.containsKey("position")
						&& AppValues.mNowJobsList.size() > 0) {
					try {
						Job currentSelectedJob = AppValues.mNowJobsList
								.get(mMainBundle.getInt("position"));
						if (currentSelectedJob.getPaymentType()
								.equalsIgnoreCase("both")
								|| currentSelectedJob.getPaymentType()
										.equalsIgnoreCase("card"))
							cardsList.addAll(currentSelectedJob.getCard());
						setSpinnerFromCardList(paymentType);

					} catch (Exception e) {
						// TODO: handle exception
						setDefaultCashCardAdapter(szJobPaymentType);
					}
				} else {
					setDefaultCashCardAdapter(szJobPaymentType);
				}
			} else {
				cardsList = new ArrayList<Card>();
				cardsList.clear();
				Card newItemCash = new Card();
				newItemCash.setBrand("Cash");
				newItemCash.setTruncatedPan("");
				cardsList.add(newItemCash);

				adapterPaymentType = new PaymentTypeAdapter(Util.mContext,
						cardsList);
				spinnerPaymentMethod.setAdapter(adapterPaymentType);
			}
		} else {
			setDefaultCashCardAdapter(szJobPaymentType);
		}

		if (mMainBundle != null) {

			if (mMainBundle.containsKey(Constants.CARD_FEE_VALUE)
					&& !mMainBundle.getString(Constants.CARD_FEE_VALUE)
							.isEmpty()) {
				cardFees = Float.parseFloat(mMainBundle
						.getString(Constants.CARD_FEE_VALUE));
			}

			if (mMainBundle.containsKey(Constants.PAYMENT_METHOD_DETAILS)) {
				String szPaymentType = mMainBundle
						.getString(Constants.PAYMENT_METHOD_DETAILS);
				if (Constants.isDebug)
					Log.e("CabPayFragment", "Payment Method Details:: "
							+ szPaymentType);

				for (int i = 0; i < adapterPaymentType.getCount(); i++) {

					Card cardDetails = (Card) adapterPaymentType.getItem(i);

					String szPaymentTypeInAdapter = cardDetails.getBrand()
							.toString();

					if (!cardDetails.getTruncatedPan().equals(""))
						szPaymentTypeInAdapter = szPaymentTypeInAdapter + "-"
								+ cardDetails.getTruncatedPan();

					if (szPaymentType.equalsIgnoreCase(szPaymentTypeInAdapter)) {
						spinnerPaymentMethod.setSelection(i);
						break;
					}
				}
			}

			if (mMainBundle.containsKey(Constants.IS_FROM_FARE_CALCULATOR)) {
				isFromFareCalculator = mMainBundle
						.getBoolean(Constants.IS_FROM_FARE_CALCULATOR);
				if (isFromFareCalculator) {
					etMeterValue.setText(mMainBundle
							.getString(Constants.FC_METER_VALUE));
				}

			}

			if (mMainBundle.containsKey(Constants.METER_VALUE)) {
				etMeterValue.setText(mMainBundle
						.getString(Constants.METER_VALUE));
				etTipValue.setText(mMainBundle.getString(Constants.TIP_VALUE));
				textTotalValue.setText(mMainBundle
						.getString(Constants.TOTAL_VALUE));
			} else if (etMeterValue.getText().toString().isEmpty()
					&& mMainBundle.containsKey(Constants.FARE)) {
				etMeterValue.setText(mMainBundle.getString(Constants.FARE));
			}

			if (mMainBundle.containsKey("fromjobaccepted")) {
				boolean bIsFromJobAccepted = mMainBundle
						.getBoolean("fromjobaccepted");

				if (bIsFromJobAccepted) {
					ivBack.setVisibility(View.VISIBLE);
					relMenu.setVisibility(View.GONE);
					// rlBottomBar.setVisibility(View.VISIBLE);
					// rlBottomBar2.setVisibility(View.VISIBLE);
				}
			}

			if (mMainBundle.containsKey(Constants.JOB_ID))
				szJobID = mMainBundle.getString(Constants.JOB_ID);

		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(final Message message) {
				super.handleMessage(message);

				if (message.what == 100) {

					if (Constants.isDebug)
						Log.e(TAG, "handler cab pay:card ");

					// Case 1:Always get Job ID
					if (mMainBundle != null
							&& mMainBundle.containsKey(Constants.JOB_ID)) {
						SendReceiptDirectly("card");
					} else {
						ShowSendReceiptDialog("card");
					}
					/* Old flow */
					// final Dialog dialog = new Dialog(mContext,
					// R.style.mydialogstyle);
					// dialog.setContentView(R.layout.receipt_dialog);
					// dialog.setCanceledOnTouchOutside(false);
					// dialog.setCancelable(false);
					// dialog.show();
					// szMeterAmt = etMeterValue.getText().toString().trim();
					// szTipValue = etTipValue.getText().toString().trim();
					// szTotalValue =
					// textTotalValue.getText().toString().trim();
					//
					// textReceiptYes = (TextView)
					// dialog.findViewById(R.id.tvYes);
					// textReceiptNo = (TextView)
					// dialog.findViewById(R.id.tvNo);
					// textReceiptNo.setOnClickListener(new OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// // TODO Auto-generated method stub
					// dialog.dismiss();
					// // Check if walkin or not(if walk up card then
					// // receipt should go else not)
					//
					// if (NetworkUtil.isNetworkOn(getActivity())) {
					// SendReceiptTask sendReceiptTask = new SendReceiptTask();
					// sendReceiptTask.mContext = mContext;
					//
					// if (mMainBundle != null
					// && mMainBundle
					// .containsKey(Constants.JOB_ID))
					// sendReceiptTask.szJobID = mMainBundle
					// .getString(Constants.JOB_ID);
					// else
					// sendReceiptTask.szJobID = "";
					//
					// sendReceiptTask.szType = "none";
					// sendReceiptTask.szAmount = szTotalValue;
					// sendReceiptTask.szTip = szTipValue;
					// sendReceiptTask.szFee = String.format(
					// Locale.ENGLISH, "%.2f", cardFees);
					//
					// sendReceiptTask.szEmail = "";
					// sendReceiptTask.szMobile = "";
					// sendReceiptTask.szInternationalCode = "";
					//
					// if (szPaymentMethod.equalsIgnoreCase("card"))
					// sendReceiptTask.szPaymentType = "card";
					// else
					// sendReceiptTask.szPaymentType = "cash";
					//
					// if (mMainBundle != null
					// && mMainBundle
					// .containsKey(Constants.PICK_UP_ADDRESS)) {
					// sendReceiptTask.szPU = mMainBundle
					// .getString(Constants.PICK_UP_ADDRESS);
					// sendReceiptTask.szDO = mMainBundle
					// .getString(Constants.DROP_ADDRESS);
					// } else {
					// sendReceiptTask.szPU = "";
					// sendReceiptTask.szDO = "";
					// }
					// sendReceiptTask.execute();
					// } else {
					// Util.showToastMessage(
					// getActivity(),
					// getResources().getString(
					// R.string.no_network_error),
					// Toast.LENGTH_LONG);
					// }
					//
					// }
					// // else {
					// // // Job Completed
					// // Fragment fragment = null;
					// // fragment = new
					// // com.android.cabapp.fragments.JobsFragment();
					// // ((MainActivity) mContext)
					// // .setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
					// // if (fragment != null)
					// // ((MainActivity) mContext).replaceFragment(
					// // fragment, true);
					// // }
					// });
					// textReceiptYes.setOnClickListener(new OnClickListener() {
					//
					// @Override
					// public void onClick(View v) {
					// // TODO Auto-generated method stub
					// dialog.dismiss();
					// if (mMainBundle == null)
					// mMainBundle = new Bundle();
					//
					// mMainBundle.putString(Constants.TOTAL_VALUE,
					// szTotalValue);
					// mMainBundle.putString(Constants.METER_VALUE,
					// szMeterAmt);
					// mMainBundle.putString(Constants.TIP_VALUE,
					// szTipValue);
					// mMainBundle.putString(Constants.JOB_ID, szJobID);
					// mMainBundle.putString(Constants.CARD_FEE_VALUE,
					// (String.format(Locale.ENGLISH, "%.2f",
					// cardFees)));
					//
					// // Payment type:
					// mMainBundle.putString(
					// Constants.PAYMENT_TYPE_CABPAY, "card");
					//
					// mMainBundle.putString(
					// Constants.PAYMENT_METHOD_DETAILS,
					// szPaymentMethod);
					//
					// Fragment fragment = new
					// com.android.cabapp.fragments.CabPayReceiptFragment();
					// fragment.setArguments(mMainBundle);
					//
					// try {
					// if (fragment != null)
					// ((MainActivity) mContext).replaceFragment(
					// fragment, true);
					// } catch (Exception e) {
					// if (fragment != null)
					// ((MainActivity) mContext).replaceFragment(
					// fragment, false);
					// }
					// }
					// });

					return;
				}

				Fragment fragment = null;
				fragment = new com.android.cabapp.fragments.JobsFragment();
				((MainActivity) Util.mContext)
						.setSlidingMenuPosition(Constants.JOBS_FRAGMENT);

				if (fragment != null)
					((MainActivity) Util.mContext).replaceFragment(fragment,
							true);
			}
		};

	}

	void setSpinnerFromCardList(String szPaymentType) {
		String szJobPaymentType = szPaymentType;
		if (cardsList.size() > 0) {
			if (szJobPaymentType.equalsIgnoreCase("cash")) {
				cardsList.clear();
				Card newItem = new Card();
				newItem.setBrand("Cash");
				newItem.setTruncatedPan("");
				cardsList.add(newItem);

				Card newItemCard = new Card();
				newItemCard.setBrand("Card");
				newItemCard.setTruncatedPan("");
				cardsList.add(newItemCard);
			} else {
				Card newItemCard = new Card();
				newItemCard.setBrand("Card");
				newItemCard.setTruncatedPan("");
				cardsList.add(newItemCard);
				Card newItem = new Card();
				newItem.setBrand("Cash");
				newItem.setTruncatedPan("");
				cardsList.add(newItem);
			}
			adapterPaymentType = new PaymentTypeAdapter(Util.mContext,
					cardsList);
			spinnerPaymentMethod.setAdapter(adapterPaymentType);
			setInitialCardFeesText();
		} else {
			setDefaultCashCardAdapter(szJobPaymentType);
		}
	}

	void setDefaultCashCardAdapter(String szJobPaymentType) {
		cardsList = new ArrayList<Card>();
		cardsList.clear();
		if (szJobPaymentType.equalsIgnoreCase("cash")) {
			cardsList.clear();
			Card newItem = new Card();
			newItem.setBrand("Cash");
			newItem.setTruncatedPan("");
			cardsList.add(newItem);

			Card newItemCard = new Card();
			newItemCard.setBrand("Card");
			newItemCard.setTruncatedPan("");
			cardsList.add(newItemCard);
		} else {
			Card newItemCard = new Card();
			newItemCard.setBrand("Card");
			newItemCard.setTruncatedPan("");
			cardsList.add(newItemCard);
			Card newItem = new Card();
			newItem.setBrand("Cash");
			newItem.setTruncatedPan("");
			cardsList.add(newItem);
		}

		adapterPaymentType = new PaymentTypeAdapter(Util.mContext, cardsList);
		spinnerPaymentMethod.setAdapter(adapterPaymentType);

		setInitialCardFeesText();
	}

	public class DecimalFilter extends DigitsKeyListener implements InputFilter {
		public DecimalFilter() {
			super(false, true);
		}

		private int digits = 2;

		public void setDigits(int d) {
			digits = d;
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			CharSequence out = super.filter(source, start, end, dest, dstart,
					dend);

			// if changed, replace the source
			if (out != null) {
				source = out;
				start = 0;
				end = out.length();
			}

			int len = end - start;

			// if deleting, source is empty
			// and deleting can't break anything
			if (len == 0) {
				return source;
			}

			int dlen = dest.length();

			// Find the position of the decimal .
			for (int i = 0; i < dstart; i++) {
				if (dest.charAt(i) == '.') {
					// being here means, that a number has
					// been inserted after the dot
					// check if the amount of digits is right
					return (dlen - (i + 1) + len > digits) ? ""
							: new SpannableStringBuilder(source, start, end);
				}
			}

			for (int i = start; i < end; ++i) {
				if (source.charAt(i) == '.') {
					// being here means, dot has been inserted
					// check if the amount of digits is right
					if ((dlen - dend) + (end - (i + 1)) > digits)
						return "";
					else
						break; // return new SpannableStringBuilder(source,
								// start, end);
				}
			}

			// if the dot is after the inserted part,
			// nothing can break
			return new SpannableStringBuilder(source, start, end);
		}
	}

	class MeterTextWatcher implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String szMeterValue = "0", szTipValue = "0";
			if (etMeterValue.getText().toString().length() > 0) {
				szMeterValue = etMeterValue.getText().toString();
			}

			if (etTipValue.getText().toString().length() > 0) {
				szTipValue = etTipValue.getText().toString();
			}

			// tipValue = (float) (0.0399 * Float.parseFloat(szMeterValue));

			float tipPercentage = (float) 0.0399;

			if (AppValues.driverSettings != null)
				tipPercentage = Float.parseFloat(AppValues.driverSettings
						.getCardPaymentPercentageFee()) / 100;
			cardFees = Float.parseFloat(String
					.format(Locale.ENGLISH, "%.2f",
							(float) (tipPercentage * (Float
									.parseFloat(szMeterValue) + Float
									.parseFloat(szTipValue)))));

			if (fMinimumCardFees > cardFees) {
				cardFees = fMinimumCardFees;

				if (fCabMiles > 0) {
					additionalCardText = "<font color=#f5f5f5>"
							+ "Card fee 3.90% min fee=  </font>"
							+ " <font color=#fd6f01>"
							+ symbol
							+ String.format(Locale.ENGLISH, "%.2f",
									fMinimumCardFees) + "</font>" + "<br>"
							+ "<font color=#f5f5f5>" + "cab:miles =  </font>"
							+ " <font color=#fd6f01>" + "-" + symbol
							+ String.format(Locale.ENGLISH, "%.2f", fCabMiles)
							+ "</font>";
				} else {
					additionalCardText = "<font color=#f5f5f5>"
							+ "Card fee 3.90% min fee=  </font>"
							+ " <font color=#fd6f01>"
							+ symbol
							+ String.format(Locale.ENGLISH, "%.2f",
									fMinimumCardFees) + "</font>";// Minimum
																	// Card Fee
				}
			} else {
				float fCardPaymentPercentageFees = 0;
				if (AppValues.driverSettings != null
						&& AppValues.driverSettings
								.getCardPaymentPercentageFee() != null)
					fCardPaymentPercentageFees = Float
							.valueOf(AppValues.driverSettings
									.getCardPaymentPercentageFee());

				if (fCabMiles > 0) {
					additionalCardText = "<font color=#f5f5f5>"
							+ String.format("%.2f", fCardPaymentPercentageFees)
							+ "% Card Fee =  </font>" + " <font color=#fd6f01>"
							+ symbol
							+ String.format(Locale.ENGLISH, "%.2f", cardFees)
							+ "</font>" + "<br>" + "<font color=#f5f5f5>"
							+ "cab:miles =  </font>" + " <font color=#fd6f01>"
							+ "-" + symbol
							+ String.format(Locale.ENGLISH, "%.2f", fCabMiles)
							+ "</font>";
				} else {
					additionalCardText = "<font color=#f5f5f5>"
							+ String.format("%.2f", fCardPaymentPercentageFees)
							+ "% Card Fee =  </font>" + " <font color=#fd6f01>"
							+ symbol
							+ String.format(Locale.ENGLISH, "%.2f", cardFees)
							+ "</font>";
				}

				// additionalCardText = "<font color=#f5f5f5>"
				// + String.format("%.2f", fCardPaymentPercentageFees)
				// + "% Card Fee =  </font>" + " <font color=#fd6f01>"
				// + symbol
				// + String.format(Locale.ENGLISH, "%.2f", cardFees)
				// + "</font>";
			}
			textAdditionalCardFees.setText(Html.fromHtml(additionalCardText));

			totalValue = 0;
			if (fCabMiles > 0) {
				totalValue = Float.parseFloat(szMeterValue)
						+ Float.parseFloat(szTipValue) - fCabMiles;
			} else
				totalValue = Float.parseFloat(szMeterValue)
						+ Float.parseFloat(szTipValue);
			if (isCard) {
				totalValue = Float.parseFloat(String.format(Locale.ENGLISH,
						"%.2f", (float) (totalValue + cardFees)));
				isTipAdded = true;
			} else {
				if (isTipAdded) {
					totalValue = Float.parseFloat(String.format(Locale.ENGLISH,
							"%.2f", (float) (totalValue - cardFees)));
					isTipAdded = false;
				}
			}

			if (Constants.isDebug)
				Log.e(CabPayFragment.class.getName(), "Meter Value:: "
						+ szMeterValue + " TipValue:: " + szTipValue
						+ " Total Value:: " + totalValue + " Card Fees:: "
						+ cardFees);

			textTotalValue.setText(String.format(Locale.ENGLISH, "%.2f",
					totalValue));
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

	}

	class TextTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub

			float fLargeTransactionAmount = 100;// Bydefault 100
			if (AppValues.driverSettings != null)
				fLargeTransactionAmount = Float
						.valueOf(AppValues.driverSettings
								.getLargeTransactionAmount());

			if (event.getAction() == MotionEvent.ACTION_DOWN) {

				switch (v.getId()) {
				// case R.id.rlCashReceiptLayout:
				// rlCashReceipt.setVisibility(View.GONE);
				// break;
				case R.id.rlTakePaymentBottom:
					Util.hideSoftKeyBoard(Util.mContext, rlTakePayment);

					if (!etMeterValue.getText().toString().isEmpty()) {

						if (totalValue > fLargeTransactionAmount) {
							showTransactionAlertForCard();
						} else {
							if (mMainBundle != null
									&& mMainBundle
											.containsKey(Constants.JOB_ID)
									&& mMainBundle.getString(Constants.JOB_ID) != null) {

								// Card card = (Card) spinnerPaymentMethod
								// .getSelectedItem();
								// String spinnerSelectedItem = card.getBrand();

								callPaymentDetailFragment();

							} else {
								if (mMainBundle == null)
									mMainBundle = new Bundle();

								// If total value is less than
								// getLargeTransactionAmount
								// if (!bIsPaymentProcessOn)
								// ProceedWithCardPayment();
								callPaymentDetailFragment();

							}
						}
					} else {
						Util.showToastMessage(Util.mContext,
								"Please enter Meter Amount", Toast.LENGTH_LONG);

					}
					// Intent takePaymentIntent = new Intent(getActivity(),
					// TakePaymentActivity.class);

					// For jobs with job ID
					// http://cabapp.pocketapp.co.uk/ws/v2/driver/payment?bookingId=202&totalAmount=700&fee=300&feePaidBy=passenger&tip=0&amount=400

					// For cabpay
					// http://cabapp.pocketapp.co.uk/ws/v2/driver/payment?driverId=17&totalAmount=700&fee=300&feePaidBy=passenger&tip=0&amount=400

					// String fee = "0";
					//
					// if (isTipAdded)
					// fee = etTipValue.getText().toString();
					//
					// String szPaymentURL = "";
					// if (!szJobID.equals(""))
					// szPaymentURL = Constants.driverURL
					// + "payment?bookingID=" + szJobID
					// + "&totalAmount="
					// + textTotalValue.getText().toString() + "&fee="
					// + fee + "&tip="
					// + etTipValue.getText().toString() + "&amount="
					// + etMeterValue.getText().toString();
					// else
					// szPaymentURL = Constants.driverURL
					// + "payment?driverId=" + Constants.accessToken
					// + "&totalAmount="
					// + textTotalValue.getText().toString() + "&fee="
					// + fee + "&tip="
					// + etTipValue.getText().toString() + "&amount="
					// + etMeterValue.getText().toString();
					//
					// if (AppValues.driverSettings != null)
					// szPaymentURL = szPaymentURL
					// + "&feePaidBy="
					// + AppValues.driverSettings
					// .getCardPaymentFeePaidBy();

					// takePaymentIntent.putExtra("totalvalue", textTotalValue
					// .getText().toString());
					// takePaymentIntent.putExtra("metervalue", etMeterValue
					// .getText().toString());
					// takePaymentIntent.putExtra("tipvalue",
					// etTipValue.getText()
					// .toString());

					// takePaymentIntent.putExtra("paymentURL", szPaymentURL);
					// startActivity(takePaymentIntent);

					break;
				case R.id.rlCashReceiptBottom:
					Util.hideSoftKeyBoard(Util.mContext, rlCashReceipt);

					szMeterAmt = etMeterValue.getText().toString().trim();
					szTipValue = etTipValue.getText().toString().trim();
					szTotalValue = textTotalValue.getText().toString().trim();

					if (szMeterAmt.isEmpty()) {
						Util.showToastMessage(Util.mContext,
								"Please enter Meter Amount.", Toast.LENGTH_LONG);
					} else {
						if (totalValue > fLargeTransactionAmount) {
							showTransactionAlertForCash();
						} else {
							ProceedWithCashPayment();
						}

					}

					break;

				case R.id.rlCabPayOkyGotIt:
					Util.setIsOverlaySeen(getActivity(), true, TAG);
					rlCabAppOverlayHeader.setVisibility(View.GONE);
					rlCabPayOkyGotIt.setVisibility(View.GONE);
					rlTakePayment.setEnabled(true);
					spinnerPaymentMethod.setEnabled(true);
					etMeterValue.setEnabled(true);
					etTipValue.setEnabled(true);

					if (MainActivity.slidingMenu != null)
						MainActivity.slidingMenu
								.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;

				}
			}

			return true;
		}
	}

	void callPaymentDetailFragment() {

		if (cardsList != null) {
			if (!(spinnerItemSelectedPosition == cardsList.size())) {
				if (mMainBundle == null)
					mMainBundle = new Bundle();
				mMainBundle.putString(Constants.CARD_FEE_VALUE,
						(String.valueOf(cardFees)));
				mMainBundle.putString("brand",
						cardsList.get(spinnerItemSelectedPosition).getBrand());
				mMainBundle.putString("truncatedPan",
						cardsList.get(spinnerItemSelectedPosition)
								.getTruncatedPan());
				mMainBundle.putString(Constants.PAYMENT_CARD_ISSELECTED,
						cardsList.get(spinnerItemSelectedPosition)
								.getIsSelected());
				mMainBundle.putString(Constants.PAYMENT_ACCESS_TOKEN_VALUE,
						cardsList.get(spinnerItemSelectedPosition)
								.getPaymentToken());
			}
		}

		mMainBundle.putString(Constants.TOTAL_VALUE, textTotalValue.getText()
				.toString());
		mMainBundle.putString(Constants.METER_VALUE, etMeterValue.getText()
				.toString());
		mMainBundle.putString(Constants.TIP_VALUE, etTipValue.getText()
				.toString());
		sFees = String.valueOf(cardFees);
		mMainBundle.putString(Constants.CARD_FEE_VALUE, sFees);

		if (mMainBundle != null && mMainBundle.containsKey(Constants.JOB_ID)
				&& mMainBundle.getString(Constants.JOB_ID) != null) {
			mMainBundle.putBoolean(Constants.CABPAY_WALKUP, false);
		} else {
			mMainBundle.putBoolean(Constants.CABPAY_WALKUP, true);
		}

		// Payment type:szPaymentMethod: always card
		mMainBundle.putString(Constants.CABPAY_PAYMENT_TYPE, "card");

		Fragment fragment = new com.android.cabapp.fragments.PaymentDetailsFragment();
		if (fragment != null) {
			fragment.setArguments(mMainBundle);
			((MainActivity) Util.mContext).replaceFragment(fragment, false);
		}

	}

	void callPaymentTask() {
		if (NetworkUtil.isNetworkOn(Util.mContext)) {

			// PaymentTask paymentTask = new PaymentTask();
			//
			// if (!bIsWalkUp) {
			// paymentTask.jobId = mBundle
			// .getString(Constants.JOB_ID);
			// paymentTask.cabMiles = Float
			// .valueOf(mBundle
			// .getString(Constants.CAB_MILES));
			// }
			// paymentTask.meterAmount = mBundle
			// .getString(Constants.METER_VALUE);
			// paymentTask.totalAmount = mBundle
			// .getString(Constants.TOTAL_VALUE);
			// paymentTask.tip = mBundle
			// .getString(Constants.TIP_VALUE);
			// paymentTask.cardFees = mBundle
			// .getString(Constants.CARD_FEE_VALUE);
			// paymentTask.szFeesPaidBy = szFeesPaidBy;
			// paymentTask.szCardPayworkToken = tokenReturned;
			// paymentTask.isRegisteredCard = String
			// .valueOf(isRegisteredCard);
			// paymentTask.szCardType = cardTypeReturned;
			// paymentTask.mHandler = mhandler;
			// paymentTask.isWalkUp = bIsWalkUp;

			// paymentTask.execute();
		} else {
			Util.showToastMessage(Util.mContext,
					getResources().getString(R.string.no_network_error),
					Toast.LENGTH_LONG);
		}
	}

	void showTransactionAlertForCard() {
		AlertDialog quitAlertDialog = new AlertDialog.Builder(Util.mContext)
				.setTitle("Alert!")
				.setMessage(
						"This is a large transaction. Would you like to proceed?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {

								// if (!bIsPaymentProcessOn)
								// ProceedWithCardPayment();
								callPaymentDetailFragment();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
					}
				}).create();
		quitAlertDialog.setCanceledOnTouchOutside(false);
		quitAlertDialog.show();
	}

	void showTransactionAlertForCash() {
		AlertDialog quitAlertDialog = new AlertDialog.Builder(Util.mContext)
				.setTitle("Alert!")
				.setMessage(
						"This is a large transaction. Would you like to proceed?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {

								ProceedWithCashPayment();

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
					}
				}).create();
		quitAlertDialog.setCanceledOnTouchOutside(false);
		quitAlertDialog.show();
	}

	void ProceedWithCashPayment() {
		((Activity) Util.mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				// Case 1: If only cap:pay jobs and no
				// History
				// and No MyJobs(Active)
				if (mMainBundle != null
						&& mMainBundle.containsKey(Constants.JOB_ID)) {
					SendReceiptDirectly("cash");
				} else {
					// No Job ID Direct Walk Up
					ShowSendReceiptDialog("cash");
				}

			}
		});
	}

	void SendReceiptDirectly(String paymentType) {

		szPaymentMethod = paymentType;

		szMeterAmt = etMeterValue.getText().toString().trim();
		szTipValue = etTipValue.getText().toString().trim();
		szTotalValue = textTotalValue.getText().toString().trim();

		if (mMainBundle != null) {
			sJobId = mMainBundle.getString(Constants.JOB_ID);
			sType = "email";
			sAmount = szTotalValue;
			sTip = szTipValue;
			sFees = String.valueOf(cardFees);// String.format("%.2f",
												// cardFees);
			sEmail = "";
			sMobile = "";
			sInterCode = "";
			sPaymentType = szPaymentMethod;
			if (mMainBundle != null
					&& mMainBundle.containsKey(Constants.PICK_UP_ADDRESS)) {
				sPickUp = mMainBundle.getString(Constants.PICK_UP_ADDRESS);
				sDropOff = mMainBundle.getString(Constants.DROP_ADDRESS);
			} else {
				sPickUp = "";
				sDropOff = "";
			}

			if (szPaymentMethod.equals("cash")) {
				CallSendReceipt();
			} else {
				if (NetworkUtil.isNetworkOn(Util.mContext)) {
					SendReceiptTask sendReceiptTask = new SendReceiptTask();
					sendReceiptTask.mContext = Util.mContext;

					if (mMainBundle != null
							&& mMainBundle.containsKey(Constants.JOB_ID))
						sendReceiptTask.szJobID = mMainBundle
								.getString(Constants.JOB_ID);
					else
						sendReceiptTask.szJobID = "";

					sendReceiptTask.szType = "email";
					sendReceiptTask.szAmount = szTotalValue;
					sendReceiptTask.szTip = szTipValue;
					sendReceiptTask.szFee = String.format(Locale.ENGLISH,
							"%.2f", cardFees);

					sendReceiptTask.szEmail = "";
					sendReceiptTask.szMobile = "";
					sendReceiptTask.szInternationalCode = "";

					// if (szPaymentMethod.equalsIgnoreCase("card"))
					sendReceiptTask.szPaymentType = "card";
					// else
					// / sendReceiptTask.szPaymentType = "cash";

					if (mMainBundle != null
							&& mMainBundle
									.containsKey(Constants.PICK_UP_ADDRESS)) {
						sendReceiptTask.szPU = mMainBundle
								.getString(Constants.PICK_UP_ADDRESS);
						sendReceiptTask.szDO = mMainBundle
								.getString(Constants.DROP_ADDRESS);
					} else {
						sendReceiptTask.szPU = "";
						sendReceiptTask.szDO = "";
					}
					sendReceiptTask.execute();
				} else {
					Util.showToastMessage(Util.mContext, getResources()
							.getString(R.string.no_network_error),
							Toast.LENGTH_LONG);
				}
			}

			// }
			// else {
			// // Job Completed
			// Fragment fragment = null;
			// fragment = new
			// com.android.cabapp.fragments.JobsFragment();
			// ((MainActivity) mContext)
			// .setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
			// if (fragment != null)
			// ((MainActivity) mContext).replaceFragment(
			// fragment, true);
			// }
			// });
		}

	}

	void ShowSendReceiptDialog(String paymentType) {

		if (Constants.isDebug)
			Log.e("CabPayFragment", "ShowSendReceiptDialog");

		szPaymentMethod = paymentType;

		szMeterAmt = etMeterValue.getText().toString().trim();
		szTipValue = etTipValue.getText().toString().trim();
		szTotalValue = textTotalValue.getText().toString().trim();

		final AlertDialog receiptDialog = new AlertDialog.Builder(Util.mContext)
				.setMessage("Would you like to send passenger a receipt?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {

								// TODO Auto-generated
								// method
								// stub

								if (mMainBundle == null)
									mMainBundle = new Bundle();

								mMainBundle.putString(Constants.TOTAL_VALUE,
										szTotalValue);
								mMainBundle.putString(Constants.METER_VALUE,
										szMeterAmt);
								mMainBundle.putString(Constants.TIP_VALUE,
										szTipValue);
								if (szJobID != null)
									mMainBundle.putString("jobId", szJobID);
								else
									mMainBundle.putString("jobId", "");
								mMainBundle.putString(Constants.CARD_FEE_VALUE,
										(String.format(Locale.ENGLISH, "%.2f",
												cardFees)));

								// Payment type:
								mMainBundle.putString(
										Constants.CABPAY_PAYMENT_TYPE,
										szPaymentMethod);

								mMainBundle.putString(
										Constants.PAYMENT_METHOD_DETAILS,
										szPaymentMethod);

								Fragment fragment = null;
								fragment = new com.android.cabapp.fragments.CabPayReceiptFragment();
								fragment.setArguments(mMainBundle);
								if (fragment != null)
									((MainActivity) Util.mContext)
											.replaceFragment(fragment, false);

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated
						// method
						// stub

						// Check if walkin or not
						// if (mMainBundle != null
						// && mMainBundle
						// .containsKey(Constants.JOB_ID))
						// {

						if (mMainBundle != null
								&& mMainBundle.containsKey(Constants.JOB_ID))
							sJobId = mMainBundle.getString(Constants.JOB_ID);
						else
							sJobId = "";

						sType = "none";
						sAmount = szTotalValue;
						sTip = szTipValue;
						sFees = String.format(Locale.ENGLISH, "%.2f", cardFees);
						sEmail = "";
						sMobile = "";
						sInterCode = "";
						sPaymentType = szPaymentMethod;
						if (mMainBundle != null
								&& mMainBundle
										.containsKey(Constants.PICK_UP_ADDRESS)) {
							sPickUp = mMainBundle
									.getString(Constants.PICK_UP_ADDRESS);
							sDropOff = mMainBundle
									.getString(Constants.DROP_ADDRESS);
						} else {
							sPickUp = "";
							sDropOff = "";
						}

						CallSendReceipt();

						// if (NetworkUtil.isNetworkOn(getActivity())) {
						// // if (!mMainBundle
						// // .get(Constants.JOB_ID)
						// // .equals("")) {
						// SendReceiptTask sendReceiptTask = new
						// SendReceiptTask();
						//
						// sendReceiptTask.mContext = mContext;
						// if (mMainBundle != null
						// && mMainBundle.containsKey(Constants.JOB_ID))
						// sendReceiptTask.szJobID = mMainBundle
						// .getString(Constants.JOB_ID);
						// else
						// sendReceiptTask.szJobID = "";
						// sendReceiptTask.szType = "none";
						// sendReceiptTask.szAmount = szTotalValue;
						// sendReceiptTask.szTip = szTipValue;
						// sendReceiptTask.szFee =
						// String.format(Locale.ENGLISH,
						// "%.2f", cardFees);
						//
						// sendReceiptTask.szEmail = "";
						// sendReceiptTask.szMobile = "";
						// sendReceiptTask.szInternationalCode = "";
						// sendReceiptTask.szPaymentType =
						// jobPaymentType;
						//
						// if (mMainBundle != null
						// && mMainBundle
						// .containsKey(Constants.PICK_UP_ADDRESS)) {
						// sendReceiptTask.szPU = mMainBundle
						// .getString(Constants.PICK_UP_ADDRESS);
						// sendReceiptTask.szDO = mMainBundle
						// .getString(Constants.DROP_ADDRESS);
						// } else {
						// sendReceiptTask.szPU = "";
						// sendReceiptTask.szDO = "";
						// }
						// sendReceiptTask.execute();
						// // }
						// } else {
						// Util.showToastMessage(getActivity(),
						// getResources()
						// .getString(R.string.no_network_error),
						// Toast.LENGTH_LONG);
						// }

						// } else {
						// // Job Completed
						// Fragment fragment = null;
						// fragment = new
						// com.android.cabapp.fragments.JobsFragment();
						// ((MainActivity) mContext)
						// .setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
						// if (fragment != null)
						// ((MainActivity) mContext)
						// .replaceFragment(
						// fragment,
						// true);
						// }
					}
				}).create();
		receiptDialog.setCanceledOnTouchOutside(false);
		receiptDialog.show();

		// final Dialog dialog = new Dialog(Util.mContext,
		// R.style.mydialogstyle);
		// dialog.setContentView(R.layout.receipt_dialog);
		// dialog.setCanceledOnTouchOutside(false);
		// dialog.setCancelable(false);
		// dialog.show();
		//
		// textReceiptYes = (TextView) dialog.findViewById(R.id.tvYes);
		// textReceiptNo = (TextView) dialog.findViewById(R.id.tvNo);
		// textReceiptNo.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// ((Activity) Util.mContext)
		// .runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// dialog.dismiss();
		// }
		// });
		// }
		// });
		// textReceiptYes.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// ((Activity) Util.mContext)
		// .runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method
		// // stub
		// dialog.dismiss();
		// }
		// });
		// }
		// });
	}

	void CallSendReceipt() {

		if (NetworkUtil.isNetworkOn(Util.mContext)) {

			SendReceiptTask sendReceiptTask = new SendReceiptTask();

			sendReceiptTask.mContext = Util.mContext;
			sendReceiptTask.szJobID = sJobId;
			sendReceiptTask.szType = sType;
			sendReceiptTask.szAmount = sAmount;
			sendReceiptTask.szTip = sTip;
			sendReceiptTask.szFee = String.valueOf(cardFees);
			sendReceiptTask.szEmail = sEmail;
			sendReceiptTask.szMobile = sMobile;
			sendReceiptTask.szInternationalCode = sInterCode;
			sendReceiptTask.szPaymentType = sPaymentType;

			sendReceiptTask.szPU = sPickUp;
			sendReceiptTask.szDO = sDropOff;
			sendReceiptTask.execute();
		} else {
			Util.showToastMessage(Util.mContext,
					getResources().getString(R.string.no_network_error),
					Toast.LENGTH_LONG);
		}
	}

	void setSpinnerFirstItemBackground(AdapterView<?> parent) {
		parent.getChildAt(0).setBackgroundColor(
				getResources().getColor(R.color.textview_unselected));
	}

	void checkForPOSDevice() {
		// Get local Bluetooth adapter
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the Bluetooth session
		} else {
			connectToPED();
		}
	}

	void connectToPED() {
		// Intent serverIntent = new Intent(getActivity(),
		// com.android.cabapp.activity.DeviceListActivity.class);
		// startActivityForResult(serverIntent, REQUEST_CONNECT_TO_PED);

		((Activity) Util.mContext).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				showConnectingDialog();

				if (mHandPointClass == null)
					mHandPointClass = new HandPointClass(Util.mContext,
							mCallbackListener, Util
									.getPOSDeviceName(Util.mContext));
				else
					mHandPointClass.initApi(Util.mContext);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CONNECT_TO_PED:
			if (resultCode == Activity.RESULT_OK) {
				// get result from device discovery handler and connect to
				// device
				if (data != null
						&& !(data
								.getExtras()
								.getString(
										com.android.cabapp.activity.DeviceListActivity.EXTRA_DEVICE_ADDRESS)
								.equals(""))) {
					showConnectingDialog();
					if (mHandPointClass == null)
						mHandPointClass = new HandPointClass(Util.mContext,
								mCallbackListener, data.getExtras().getString(
										DeviceListActivity.EXTRA_DEVICE_NAME));
					else
						mHandPointClass.initApi(Util.mContext);
				} else {
					Util.showToastMessage(Util.mContext,
							"Proceeding with Payment without device",
							Toast.LENGTH_LONG);
					if (!bIsPaymentProcessOn)
						proceedWithPaymentWithoutHandpoint();
				}
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a bluetooth session
				try {
					connectToPED();
				} catch (Exception e) {

				}
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Util.showToastMessage(Util.mContext,
						getResources().getString(R.string.bt_not_enabled),
						Toast.LENGTH_SHORT);
			}
			break;
		}

	}

	void showConnectingDialog() {
		mConnectingDialog = new ProgressDialog(Util.mContext);
		mConnectingDialog.setCanceledOnTouchOutside(false);
		mConnectingDialog.setCancelable(false);
		// mConnectingDialog.setMessage(getResources().getString(
		// R.string.connecting));
		mConnectingDialog
				.setMessage("Handpoint device entry found in account settings. Trying to connect. Please wait...");

		mConnectingDialog.show();
	}

	private void showChipAndPinPaymentFailedDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(Util.mContext);
		builder.setMessage("Payment using Chip and Pin failed!")
				.setCancelable(false)
				.setPositiveButton("Retry",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								showConnectingDialog();
								if (mHandPointClass == null)
									mHandPointClass = new HandPointClass(
											Util.mContext,
											mCallbackListener,
											Util.getPOSDeviceName(Util.mContext));
								else
									mHandPointClass.initApi(Util.mContext);
							}
						})
				.setNegativeButton(getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if (!bIsPaymentProcessOn)
									proceedWithPaymentWithoutHandpoint();
							}
						});
		builder.setTitle("Optimal Payment");
		AlertDialog alert = builder.create();
		alert.setCancelable(false);
		alert.show();
	}

	void dismissConnectingDialog() {
		if (mConnectingDialog != null) {
			mConnectingDialog.dismiss();
			mConnectingDialog = null;
		}
	}

	CallbackListener mCallbackListener = new CallbackListener() {

		@Override
		public void onDeviceConnect(final String devicename) {
			// TODO Auto-generated method stub

			Log.e("CabPayFragment", "onDeviceConnect");
			((Activity) Util.mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mConnectingDialog != null)
						mConnectingDialog
								.setMessage("Handpoint device connected. Initiating payment process! Please wait...");
				}
			});

			HandpointTimerTask handpointTimerTask = new HandpointTimerTask();
			handpointTimer = new Timer();
			handpointTimer.schedule(handpointTimerTask, 5000);

			ConnectionTimeoutTimerTask connectionTask = new ConnectionTimeoutTimerTask();
			connectionTimeoutTimer = new Timer();
			connectionTimeoutTimer.schedule(connectionTask, 25000);
		}

		@Override
		public void onTransactionComplete(final String response) {
			// TODO Auto-generated method stub
			Log.e("CabPayFragment", "onTransactionComplete");
			((Activity) Util.mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					dismissConnectingDialog();
					if (response.contains("DECLINED")
							|| response.contains("PROCESSED")
							|| response.contains("FAILED")
							|| response.contains("CANCELLED")) {
						Util.showToastMessage(Util.mContext,
								"Error processing payment!", Toast.LENGTH_LONG);
						showChipAndPinPaymentFailedDialog();
					}
				}
			});
		}

		@Override
		public void onDeviceNotFound() {
			// TODO Auto-generated method stub
			Log.e("CabPayFragment", "onDeviceNotFound");
			((Activity) Util.mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					dismissConnectingDialog();
					Util.showToastMessage(
							Util.mContext,
							"Unable to connect with Handpoint Device. Proceeding with Payment without device",
							Toast.LENGTH_LONG);
					if (!bIsPaymentProcessOn)
						proceedWithPaymentWithoutHandpoint();
				}
			});
		}

		@Override
		public void onTransactionComplete(
				final TransactionResult transactionResult) {
			// TODO Auto-generated method stub
			dismissConnectingDialog();
			// Util.showToastMessage(getActivity(),
			// "Chip And Pin payment successful!", Toast.LENGTH_LONG);

			((Activity) Util.mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ProgressDialog pDialog = new ProgressDialog(Util.mContext);
					pDialog.setMessage("Processing payment details...");
					pDialog.setCancelable(false);
					pDialog.show();

					ChipAndPinTask chipAndPinTask = new ChipAndPinTask();
					chipAndPinTask.bundle = mMainBundle;
					chipAndPinTask.transactionResult = transactionResult;
					chipAndPinTask.szTip = etTipValue.getText().toString();
					chipAndPinTask.szJobID = szJobID;
					chipAndPinTask.szTotalValue = String.valueOf(totalValue);
					chipAndPinTask.szCardFees = String.valueOf(cardFees);
					chipAndPinTask.bIsFromCabPayFragment = true;
					chipAndPinTask.mContext = Util.mContext;
					chipAndPinTask.pDialog = pDialog;
					chipAndPinTask.execute();
				}
			});
		}
	};

	void ProceedWithCardPayment() {
		// Proceed with payment
		if (Constants.isDebug)
			Log.e("CabPayFragment", "ProceedWithCardPayment");

		if (szJobID.isEmpty()) {

			if (!Util.getPOSDeviceName(Util.mContext).isEmpty())
				checkForPOSDevice();
			else
				proceedWithPaymentWithoutHandpoint();

			// GetPaymentURLTask
			// getPaymentURLTask
			// = new
			// GetPaymentURLTask();
			// getPaymentURLTask.szURL
			// = szPaymentURL;
			// getPaymentURLTask.mBundle
			// = mMainBundle;
			// getPaymentURLTask.mContext
			// = getActivity();
			// getPaymentURLTask.execute();
		} else {
			Fragment fragment = new com.android.cabapp.fragments.PaymentDetailsFragment();
			if (fragment != null) {
				if (mMainBundle == null)
					mMainBundle = new Bundle();

				if (cardsList != null) {
					if (!(spinnerItemSelectedPosition == cardsList.size())) {
						mMainBundle.putString(Constants.CARD_FEE_VALUE, (String
								.format(Locale.ENGLISH, "%.2f", cardFees)));
						mMainBundle.putString("brand",
								cardsList.get(spinnerItemSelectedPosition)
										.getBrand());
						mMainBundle.putString("truncatedPan",
								cardsList.get(spinnerItemSelectedPosition)
										.getTruncatedPan());
					}
				}

				mMainBundle.putString(Constants.TOTAL_VALUE, textTotalValue
						.getText().toString());
				mMainBundle.putString(Constants.METER_VALUE, etMeterValue
						.getText().toString());
				mMainBundle.putString(Constants.TIP_VALUE, etTipValue.getText()
						.toString());
				mMainBundle.putString("jobId", szJobID);

				// Payment type:
				mMainBundle.putString(Constants.CABPAY_PAYMENT_TYPE,
						szPaymentMethod);

				mMainBundle.putString(Constants.PAYMENT_METHOD_DETAILS,
						szPaymentMethod);
				fragment.setArguments(mMainBundle);
				((MainActivity) Util.mContext).replaceFragment(fragment, false);
			}
		}
	}

	class HandpointTimerTask extends TimerTask {
		public void run() {
			int nTotalValue = (int) (Float.parseFloat(textTotalValue.getText()
					.toString()) * 100);

			mHandPointClass.pay(String.valueOf(nTotalValue), Currency.GBP);
			handpointTimer.cancel();
		}
	}

	class ConnectionTimeoutTimerTask extends TimerTask {
		public void run() {
			if (mConnectingDialog != null && mConnectingDialog.isShowing()) {
				// dismissConnectingDialog();
				// Util.showToastMessage(
				// getActivity(),
				// "Error establishing connection with the device! Please try again",
				// Toast.LENGTH_LONG);
				mConnectingDialog.setCancelable(true);
			}
			connectionTimeoutTimer.cancel();
		}
	}

	void proceedWithPaymentWithoutHandpoint() {
		if (Constants.isDebug)
			Log.e("CabPayFragment", "proceedWithPaymentWithoutHandpoint");
		bIsPaymentProcessOn = true;

		int nTotalValue = 0, nTip = 0, nFee = 0, nMeterValue = 0;

		String szTip = etTipValue.getText().toString();

		if (szTip.isEmpty())
			szTip = "0";

		// nTotalValue = (int) (Float.parseFloat(textTotalValue.getText()
		// .toString()) * 100);
		nMeterValue = (int) (Float
				.parseFloat(etMeterValue.getText().toString()) * 100);
		nFee = (int) (Float.parseFloat((String.format(Locale.ENGLISH, "%.2f",
				cardFees))) * 100);
		nTip = (int) (Float.parseFloat(szTip) * 100);

		nTotalValue = nMeterValue + nFee + nTip;

		String szPaymentURL = Constants.driverURL + "payment?accessToken="
				+ Constants.accessToken + "&totalAmount=" + nTotalValue
				+ "&fee=" + nFee + "&tip=" + nTip + "&amount=" + nMeterValue;

		if (AppValues.driverSettings != null)
			szPaymentURL = szPaymentURL + "&feePaidBy="
					+ AppValues.driverSettings.getCardPaymentFeePaidBy();

		if (mMainBundle == null)
			mMainBundle = new Bundle();

		mMainBundle.putString(Constants.TOTAL_VALUE, textTotalValue.getText()
				.toString());
		mMainBundle.putString(Constants.TIP_VALUE, szTip);

		// mConnectingDialog = new ProgressDialog(Util.mContext);
		// mConnectingDialog.setCanceledOnTouchOutside(false);
		// mConnectingDialog.setCancelable(false);
		// mConnectingDialog.setMessage("Loading...");
		// mConnectingDialog.show();

		GetPaymentURLTask getPaymentURLTask = new GetPaymentURLTask();
		getPaymentURLTask.szURL = szPaymentURL;
		getPaymentURLTask.mBundle = mMainBundle;
		getPaymentURLTask.mContext = Util.mContext;
		getPaymentURLTask.execute();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bIsPaymentProcessOn = false;
	}

	void setRoundOffMeterValue() {
		String userMeterInput = etMeterValue.getText().toString();

		if (userMeterInput.toString().matches("")) {
			userMeterInput = "0.00";
		} else {
			float floatValue = Float.parseFloat(userMeterInput);
			userMeterInput = String.format("%.2f", floatValue);
		}
		etMeterValue.setText(userMeterInput);
		etMeterValue.setSelection(userMeterInput.length());
	}

	void setRoundOffTipValue() {
		String userTipInput = etTipValue.getText().toString();

		if (userTipInput.toString().matches("")) {
			userTipInput = "0.00";
		} else {
			float floatValue = Float.parseFloat(userTipInput);
			userTipInput = String.format("%.2f", floatValue);
		}
		etTipValue.setText(userTipInput);
		etTipValue.setSelection(userTipInput.length());
		etTipValue.setCursorVisible(false);
	}

}