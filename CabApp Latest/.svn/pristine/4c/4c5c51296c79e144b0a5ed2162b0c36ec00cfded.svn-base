package com.android.cabapp.fragments;

import io.mpos.Mpos;
import io.mpos.accessories.AccessoryFamily;
import io.mpos.paymentdetails.ApplicationInformation;
import io.mpos.provider.ProviderMode;
import io.mpos.transactionprovider.PaymentProcess;
import io.mpos.transactionprovider.PaymentProcessDetails;
import io.mpos.transactionprovider.PaymentProcessWithRegistrationListener;
import io.mpos.transactionprovider.TransactionProvider;
import io.mpos.transactions.Transaction;
import io.mpos.transactions.TransactionTemplate;
import io.mpos.transactions.receipts.Receipt;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.AddScanCardJobsActivity;
import com.android.cabapp.activity.DeviceListActivity;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.activity.PaymentWithUrlActivity;
import com.android.cabapp.async.ChipAndPinTask;
import com.android.cabapp.async.GetPaymentURLTask;
import com.android.cabapp.async.MPosChipAndPinTask;
import com.android.cabapp.async.MPosSendReceiptTask;
import com.android.cabapp.async.PaymentTask;
import com.android.cabapp.async.SendReceiptTask;
import com.android.cabapp.handpoint.CallbackListener;
import com.android.cabapp.handpoint.HandPointClass;
import com.android.cabapp.model.CabPinValidation;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.android.cabapp.view.CustomSignatureView;
import com.handpoint.api.Currency;
import com.handpoint.api.TransactionResult;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class PaymentDetailsFragment extends RootFragment {
	private static final String TAG = PaymentDetailsFragment.class.getSimpleName();

	final String MERCHANT_IDENTIFIER = "e42bb522-6e78-48f7-94c1-b57b52b2d7cb";
	final String MERCHANT_SECRET_KEY = "OVU888PmCzGhXdvODkdwXqehi71czCzy";

	TextView textPaymentMethod, textMeterAmt, textTip, textCardFee, textCabMiles, textTotal, textPassengerCabPin, tvUpdatingText;
	RelativeLayout rlConfirm, rlCabPin, rlProgressLayout, rlAbortButton;
	EditText etCabPin1;
	String szJobID = "", szTotalValue = "", szTip = "", szFee = "", szCabMiles = "", szMeterValue = "", sTruncatedPan = "",
			sBrand = "", sMerchantId = "", sTerminalId = "", sEntryMode = "", sAID = "", sPWID = "", sAuthorization = "";

	int nTotalValue = 0, nTip = 0, nFee = 0, nMeterValue = 0, nCabmiles = 0, nTotalValueAfterAddition = 0;

	private ProgressDialog dialogPaymentDetails;

	public static Handler mHandler;
	static Context mContext;

	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	private static final int REQUEST_CONNECT_TO_PED = 1;
	private static final int REQUEST_ENABLE_BT = 3;

	ProgressDialog mConnectingDialog;
	static boolean bTransactionCompleted = false;

	HandPointClass mHandPointClass;
	Timer handpointTimer, connectionTimeoutTimer;
	float totalAmt = 0;
	public static String JobIdReturned = "";
	public boolean isWalkup = false;
	String szTransactionId = "";
	int nPaymentStatus;
	PaymentProcess paymentProcess = null;

	// Overlay
	RelativeLayout rlCabPinOkyGotIt, rlCabPinOverlay;
	ImageView ivBack;

	// Digital Signature
	RelativeLayout rlPaymentDetails, rlDigitalSignature, rlBtnCancel, rlBtnPay;
	TextView tvClearScreen, tvAmt;
	CustomSignatureView customSignatureView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mContext = getActivity();
		View rootView = inflater.inflate(R.layout.fragment_payment_details, container, false);
		super.initWidgets(rootView, this.getClass().getName());

		rlConfirm = (RelativeLayout) rootView.findViewById(R.id.rlConfirm);
		rlCabPin = (RelativeLayout) rootView.findViewById(R.id.rlCabPin);
		etCabPin1 = (EditText) rootView.findViewById(R.id.etCabPin1);
		textPaymentMethod = (TextView) rootView.findViewById(R.id.textPaymentMethodData);
		textMeterAmt = (TextView) rootView.findViewById(R.id.textMeterAmtData);
		textTip = (TextView) rootView.findViewById(R.id.textTipData);
		textCardFee = (TextView) rootView.findViewById(R.id.textCardFeeData);
		textCabMiles = (TextView) rootView.findViewById(R.id.textCabMilesData);
		textTotal = (TextView) rootView.findViewById(R.id.textTotalData);
		tvUpdatingText = (TextView) rootView.findViewById(R.id.tvUpdatingProgress);
		textPassengerCabPin = (TextView) rootView.findViewById(R.id.textPassengerCabPin);
		rlAbortButton = (RelativeLayout) rootView.findViewById(R.id.rlAbortButton);
		rlAbortButton.setVisibility(View.GONE);
		rlConfirm.setOnClickListener(new TextOnClickListener());
		rlAbortButton.setOnClickListener(new TextOnClickListener());
		rlCabPinOverlay = (RelativeLayout) rootView.findViewById(R.id.rlCabPinOverlayData);
		rlCabPinOkyGotIt = (RelativeLayout) rootView.findViewById(R.id.rlCabPinOkyGotIt);
		rlCabPinOkyGotIt.setOnClickListener(new TextOnClickListener());
		rlProgressLayout = (RelativeLayout) rootView.findViewById(R.id.rlProgressLayout);
		ivBack = (ImageView) rootView.findViewById(R.id.ivBack);

		// customSignatureView = new CustomSignatureView(mContext);
		rlPaymentDetails = (RelativeLayout) rootView.findViewById(R.id.rlPaymentDetailsLayout);
		rlDigitalSignature = (RelativeLayout) rootView.findViewById(R.id.rlDigitalSignatureLayout);
		rlBtnCancel = (RelativeLayout) rootView.findViewById(R.id.rl_BtnCancel);
		rlBtnPay = (RelativeLayout) rootView.findViewById(R.id.rl_btnPay);
		tvClearScreen = (TextView) rootView.findViewById(R.id.tvClear);
		tvAmt = (TextView) rootView.findViewById(R.id.tvAmount);
		customSignatureView = (CustomSignatureView) rootView.findViewById(R.id.customSignatureArea);

		rlBtnCancel.setOnClickListener(new TextOnClickListener());
		rlBtnPay.setOnClickListener(new TextOnClickListener());
		tvClearScreen.setOnClickListener(new TextOnClickListener());

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	class TextOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.rlConfirm:
				String szChipPin = "";
				if (textPaymentMethod.getText().toString().equalsIgnoreCase("card")) {

					if (AppValues.driverSettings != null && AppValues.driverSettings.getChipAndPin() != null) {
						szChipPin = AppValues.driverSettings.getChipAndPin();
					}

					if (szChipPin != null && !szChipPin.isEmpty()) {

						/* Dialog call: */
						selectMethodCall(true);
					} else {
						selectMethodCall(false);
						// CallScanCardActivity();
					}
				} else {

					Util.hideSoftKeyBoard(Util.mContext, rlConfirm);
					String szCabPin1 = etCabPin1.getText().toString().trim();
					if (rlCabPin.getVisibility() == View.GONE) {
						CallScanCardActivity();
					} else {
						if (NetworkUtil.isNetworkOn(Util.mContext)) {

							CabPinValidationTask cabPinTask = new CabPinValidationTask();
							cabPinTask.szCabPin = szCabPin1;
							cabPinTask.szJobId = szJobID;
							cabPinTask.execute();

						} else {
							Util.showToastMessage(Util.mContext, getResources().getString(R.string.no_network_error),
									Toast.LENGTH_LONG);
						}
					}
				}

				// if (rlCabPin.getVisibility() == View.GONE) {
				// proceedWithPaymentWithoutHandpoint();
				// } else {
				// if (szCabPin1.isEmpty()) {// || szCabPin2.isEmpty()
				// Util.showToastMessage(Util.mContext,
				// "Please enter Cab pin.", Toast.LENGTH_LONG);
				// } else {
				// String szPaymentURL = "";
				// String szTruncatedPan = "";
				//
				// if (mMainBundle.containsKey("truncatedPan"))
				// szTruncatedPan = mMainBundle
				// .getString("truncatedPan");
				//
				// if (!szTruncatedPan.isEmpty()) {
				// szPaymentURL = Constants.driverURL
				// + "payment?bookingId=" + szJobID
				// + "&totalAmount=" + nTotalValue + "&fee="
				// + nFee + "&tip=" + nTip + "&amount="
				// + nMeterValue + "&truncatedPan="
				// + szTruncatedPan;
				// } else if (!szJobID.equals(""))
				// szPaymentURL = Constants.driverURL
				// + "payment?bookingId=" + szJobID
				// + "&totalAmount=" + nTotalValue + "&fee="
				// + nFee + "&tip=" + nTip + "&amount="
				// + nMeterValue;
				// else
				// szPaymentURL = Constants.driverURL
				// + "payment?accessToken="
				// + Constants.accessToken + "&totalAmount="
				// + nTotalValue + "&fee=" + nFee + "&tip="
				// + nTip + "&amount=" + nMeterValue;
				//
				// if (AppValues.driverSettings != null)
				// szPaymentURL = szPaymentURL
				// + "&feePaidBy="
				// + AppValues.driverSettings
				// .getCardPaymentFeePaidBy();
				//
				// if (Constants.isDebug)
				// Log.e(TAG, "szPaymentURL::> " + szPaymentURL);
				//
				// if (NetworkUtil.isNetworkOn(Util.mContext)) {
				//
				// CabPinValidationTask cabPinTask = new CabPinValidationTask();
				// cabPinTask.szCabPin = szCabPin1;// + szCabPin2
				// cabPinTask.szJobId = szJobID;
				// cabPinTask.szPaymentURL = szPaymentURL;
				// cabPinTask.execute();
				//
				// } else {
				// Util.showToastMessage(Util.mContext, getResources()
				// .getString(R.string.no_network_error),
				// Toast.LENGTH_LONG);
				// }
				// }
				// }
				break;

			case R.id.rlCabPinOkyGotIt:
				Util.setIsOverlaySeen(getActivity(), true, TAG);
				rlCabPinOverlay.setVisibility(View.GONE);
				rlCabPinOkyGotIt.setVisibility(View.GONE);
				ivBack.setEnabled(true);
				if (MainActivity.slidingMenu != null)
					MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				break;

			case R.id.rlAbortButton:
				// bTransactionCompleted = true;
				abortPayment();
				mPosSendReceiptCall();
				break;

			case R.id.rl_BtnCancel:
				rlPaymentDetails.setVisibility(View.VISIBLE);
				rlDigitalSignature.setVisibility(View.GONE);
				// bTransactionCompleted = true;

				if (MainActivity.slidingMenu != null)
					MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				paymentProcess.continueWithCustomerSignature(null, false);
				customSignatureView.clearSignature();
				mPosSendReceiptCall();

				break;

			case R.id.rl_btnPay:
				// saveImage(customSignatureView.getSignature());

				paymentProcess.continueWithCustomerSignature(customSignatureView.getSignature(),// bm
						true);
				rlPaymentDetails.setVisibility(View.VISIBLE);
				rlDigitalSignature.setVisibility(View.GONE);
				customSignatureView.clearSignature();

				break;

			case R.id.tvClear:
				customSignatureView.clearSignature();
				break;
			}
		}
	}

	public void abortPayment() {
		if (paymentProcess != null)
			paymentProcess.requestAbort();
	}

	void selectMethodCall(boolean bIsChipAndPin) {
		boolean bIsChipPinPresent = bIsChipAndPin;

		final Dialog dialog = new Dialog(mContext, R.style.mydialogstyle);
		dialog.setContentView(R.layout.dialog_select_payment_type);
		dialog.setCanceledOnTouchOutside(false);

		// Grab the window of the dialog, and change the width
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		// This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);

		TextView tvChipAndPin = (TextView) dialog.findViewById(R.id.tvChipAndPin);
		TextView tvSendLink = (TextView) dialog.findViewById(R.id.tvSendLink);
		TextView tvManual = (TextView) dialog.findViewById(R.id.tvManual);
		TextView tvClose = (TextView) dialog.findViewById(R.id.tvClose);

		if (bIsChipPinPresent)
			tvChipAndPin.setVisibility(View.VISIBLE);
		else
			tvChipAndPin.setVisibility(View.GONE);

		dialog.setCancelable(false);
		dialog.show();
		tvChipAndPin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Check bluetooth
				CheckBlueTooth();
				dialog.dismiss();
			}
		});
		tvSendLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), PaymentWithUrlActivity.class);
				intent.putExtras(mMainBundle);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		tvManual.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CallScanCardActivity();
				dialog.dismiss();
			}
		});

		tvClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

	void CallScanCardActivity() {
		// call AddScanCardJobsActivity:
		Intent intent = new Intent(getActivity(), AddScanCardJobsActivity.class);
		intent.putExtras(mMainBundle);
		startActivity(intent);
	}

	void CheckBlueTooth() {

		if (mBluetoothAdapter == null) {
			// Device does not support Bluetooth
			Util.showToastMessage(mContext, "Your device does not support bluetooth", Toast.LENGTH_LONG);
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				// Bluetooth is not enable :)
				Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			} else {
				transaction();
			}
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		mContext = Util.mContext;
		mMainBundle = this.getArguments();
		if (mMainBundle != null) {
			Log.e(TAG, "mMmain bundle jobid:PRESENT " + mMainBundle.toString());
			String szPaymentMethod = mMainBundle.getString("brand");

			if (!mMainBundle.getString("truncatedPan").equals(""))
				szPaymentMethod = szPaymentMethod + "-" + mMainBundle.getString("truncatedPan");
			else
				szPaymentMethod = szPaymentMethod.substring(0, 1).toUpperCase() + szPaymentMethod.substring(1);

			textPaymentMethod.setText(szPaymentMethod);

			if (textPaymentMethod.getText().equals("Card")) {
				Log.e(TAG, "Card type: TRUE");
				rlCabPin.setVisibility(View.GONE);
			}

			if (rlCabPin.getVisibility() == View.VISIBLE) {
				if (!Util.getIsOverlaySeen(getActivity(), TAG)) {
					rlCabPinOverlay.setVisibility(View.VISIBLE);
					ivBack.setEnabled(false);
					if (MainActivity.slidingMenu != null)
						MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
			}

			szJobID = mMainBundle.getString(Constants.JOB_ID);
			if (szJobID == null || szJobID.isEmpty())
				isWalkup = true;
			else
				isWalkup = false;
			szTotalValue = mMainBundle.getString(Constants.TOTAL_VALUE);
			szTip = mMainBundle.getString(Constants.TIP_VALUE);
			szFee = mMainBundle.getString(Constants.CARD_FEE_VALUE);
			szMeterValue = mMainBundle.getString(Constants.METER_VALUE);
			szCabMiles = mMainBundle.getString(Constants.CAB_MILES);
			Log.e(TAG, "szCabMiles:   " + szCabMiles);
			if (szTip == null || szTip.isEmpty())
				szTip = Constants.ZEROVALUE;

			if (szFee == null || szFee.isEmpty())
				szFee = Constants.ZEROVALUE;

			if (szCabMiles == null || szCabMiles.isEmpty())
				szCabMiles = Constants.ZEROVALUE;

			if (!szTotalValue.isEmpty())
				nTotalValue = (int) (Float.parseFloat(szTotalValue) * 100);
			if (!szMeterValue.isEmpty())
				nMeterValue = (int) (Float.parseFloat(szMeterValue) * 100);
			if (!szFee.isEmpty())
				nFee = (int) (Float.parseFloat(szFee) * 100);
			if (!szTip.isEmpty())
				nTip = (int) (Float.parseFloat(szTip) * 100);

			if (Constants.isDebug) {
				nTotalValueAfterAddition = nMeterValue + nFee + nTip;
				Log.d("PaymentDetailsFragment", "Payment Total Value:: " + nTotalValue + " Total Value after addition:: "
						+ nTotalValueAfterAddition);
			}

			String szCurrenySymbol = "";
			if (AppValues.driverSettings != null)
				szCurrenySymbol = AppValues.driverSettings.getCurrencySymbol();

			totalAmt = Float.parseFloat(szMeterValue) + Float.parseFloat(szFee) + Float.parseFloat(szTip)
					- Float.parseFloat(szCabMiles);

			textMeterAmt.setText(szCurrenySymbol + " " + szMeterValue);
			textTip.setText(szCurrenySymbol + " " + szTip);
			textCardFee.setText(szCurrenySymbol + " " + szFee);
			textTotal.setText(szCurrenySymbol + " " + String.format(Locale.ENGLISH, "%.2f", totalAmt));
			if (szCabMiles.isEmpty()) {
				szCabMiles = Constants.ZEROVALUE;
				textCabMiles.setText("- " + szCurrenySymbol + " " + szCabMiles);
			} else {
				textCabMiles.setText("- " + szCurrenySymbol + " " + szCabMiles);
			}
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Bundle mBundleReturned;

				if (msg.what == 2) {
					mBundleReturned = (Bundle) msg.getData();
					if (mBundleReturned != null && mBundleReturned.getBoolean("success")) {
						JobIdReturned = mBundleReturned.getString("jobId");
						Util.setCashBack(getActivity(), String.valueOf(mBundleReturned.getLong("cashBackReturned")));

						CallSendReceipt();

					} else {
						Log.e(TAG, "Error: payment");
					}
				} else if (msg.what == 5) {

					mBundleReturned = (Bundle) msg.getData();
					Log.e(TAG, "mBundleReturned: " + mBundleReturned.toString());
					JobIdReturned = mBundleReturned.getString("JobIDReturn");
					Util.setCashBack(getActivity(), mBundleReturned.getString("cashbackValueReturn"));

				}
			}
		};

		/* Old payment flow */
		// mHandler = new Handler() {
		// @Override
		// public void handleMessage(final Message message) {
		// super.handleMessage(message);
		//
		// mMainBundle = (Bundle) message.obj;
		//
		// if (mMainBundle != null)
		// if (Constants.isDebug)
		// Log.e(TAG,
		// "mMainBundle:inside handler Payment details fragment "
		// + mMainBundle);
		//
		// szJobID = mMainBundle.getString(Constants.JOB_ID);
		// szTotalValue = mMainBundle.getString(Constants.TOTAL_VALUE);
		// szTip = mMainBundle.getString(Constants.TIP_VALUE);
		// szFee = mMainBundle.getString(Constants.CARD_FEE_VALUE);
		// szMeterValue = mMainBundle.getString(Constants.METER_VALUE);
		//
		// ((Activity) Util.mContext).runOnUiThread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// // final Dialog dialog = new Dialog(mContext,
		// // R.style.mydialogstyle);
		// // dialog.setContentView(R.layout.receipt_dialog);
		// // dialog.setCanceledOnTouchOutside(false);
		// // dialog.setCancelable(false);
		// // dialog.show();
		// // TextView textReceiptYes, textReceiptNo;
		// //
		// // textReceiptYes = (TextView) dialog
		// // .findViewById(R.id.tvYes);
		// // textReceiptNo = (TextView) dialog
		// // .findViewById(R.id.tvNo);
		// // textReceiptNo.setOnClickListener(new
		// // OnClickListener() {
		// //
		// // @Override
		// // public void onClick(View v) {
		// // // TODO Auto-generated method stub
		// // dialog.dismiss();
		// // Check if walkin or not
		// if (!szJobID.isEmpty()) {
		// if (NetworkUtil.isNetworkOn(Util.mContext)) {
		// SendReceiptTask sendReceiptTask = new SendReceiptTask();
		// sendReceiptTask.mContext = mContext;
		// sendReceiptTask.szJobID = szJobID;
		// sendReceiptTask.szType = "email";
		// sendReceiptTask.szAmount = szTotalValue;
		// sendReceiptTask.szTip = szTip;
		// sendReceiptTask.szFee = szFee;
		//
		// String szEmail = Util
		// .getEmailOrUserName(mContext);
		// sendReceiptTask.szEmail = szEmail;
		// sendReceiptTask.szMobile = "";
		// sendReceiptTask.szInternationalCode = "";
		// sendReceiptTask.szPaymentType = "card";
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
		// Util.mContext,
		// getResources().getString(
		// R.string.no_network_error),
		// Toast.LENGTH_LONG);
		// }
		//
		// } else {
		// // Job Completed
		// Fragment fragment = null;
		// fragment = new com.android.cabapp.fragments.MyJobsFragment();
		// ((MainActivity) mContext)
		// .setSlidingMenuPosition(Constants.MY_JOBS_FRAGMENT);
		// if (fragment != null)
		// ((MainActivity) mContext).replaceFragment(
		// fragment, true);
		// }
		// // }
		// // });
		// // textReceiptYes
		// // .setOnClickListener(new OnClickListener() {
		// //
		// // @Override
		// // public void onClick(View v) {
		// // // TODO Auto-generated method stub
		// // dialog.dismiss();
		// //
		// // if (mMainBundle == null)
		// // mMainBundle = new Bundle();
		// //
		// // mMainBundle.putString(
		// // Constants.TOTAL_VALUE,
		// // szTotalValue);
		// // mMainBundle.putString(
		// // Constants.METER_VALUE,
		// // szMeterValue);
		// // mMainBundle.putString(
		// // Constants.TIP_VALUE, szTip);
		// // mMainBundle.putString(Constants.JOB_ID,
		// // szJobID);
		// // mMainBundle
		// // .putString(
		// // Constants.CARD_FEE_VALUE,
		// // szFee);
		// //
		// // // Payment type:
		// // mMainBundle.putString(
		// // Constants.PAYMENT_TYPE_CABPAY,
		// // "card");
		// //
		// // mMainBundle
		// // .putString(
		// // Constants.PAYMENT_METHOD_DETAILS,
		// // "card");
		// //
		// // Fragment fragment = null;
		// // fragment = new
		// // com.android.cabapp.fragments.CabPayReceiptFragment();//
		// // JobsFragment
		// // fragment.setArguments(mMainBundle);
		// // // ((MainActivity) mContext)
		// // //
		// // .setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
		// //
		// // if (message.what == 100) {
		// // if (fragment != null)
		// // ((MainActivity) mContext)
		// // .replaceFragment(
		// // fragment, false);
		// // } else {
		// // if (fragment != null)
		// // ((MainActivity) mContext)
		// // .replaceFragment(
		// // fragment, true);
		// // }
		// // }
		// // });
		//
		// }
		// });
		//
		// }
		// };

	}

	void callMposSendReceipt(boolean bIsWalkUp, String sendingType, boolean bIsShowToastForSuccess) {
		if (NetworkUtil.isNetworkOn(Util.mContext)) {

			MPosSendReceiptTask MPosSendReceiptTask = new MPosSendReceiptTask();
			MPosSendReceiptTask.bIsWalkUp = bIsWalkUp;

			MPosSendReceiptTask.szPaymentType = "card";
			MPosSendReceiptTask.szEmail = "";
			MPosSendReceiptTask.szMobile = "";
			MPosSendReceiptTask.szInternationalCode = "";
			MPosSendReceiptTask.szPU = "";
			MPosSendReceiptTask.szDO = "";

			MPosSendReceiptTask.mContext = Util.mContext;
			MPosSendReceiptTask.szJobID = JobIdReturned;
			MPosSendReceiptTask.szAmount = szTotalValue;
			MPosSendReceiptTask.szTip = szTip;
			MPosSendReceiptTask.szCardFees = szFee;
			MPosSendReceiptTask.szTransactionId = szTransactionId;
			MPosSendReceiptTask.szPaymentStatus = String.valueOf(nPaymentStatus);
			MPosSendReceiptTask.szSendingType = sendingType;

			MPosSendReceiptTask.szTruncatedPan = sTruncatedPan;
			MPosSendReceiptTask.szBrand = sBrand;
			MPosSendReceiptTask.szMerchantId = sMerchantId;
			MPosSendReceiptTask.szTerminalId = sTerminalId;
			MPosSendReceiptTask.szEntryMode = sEntryMode;
			MPosSendReceiptTask.szAID = sAID;
			MPosSendReceiptTask.szPWID = sPWID;
			MPosSendReceiptTask.szAuthorization = sAuthorization;
			MPosSendReceiptTask.bIsShowSuccessToast = bIsShowToastForSuccess;
			
			Float meterAmount = Float.parseFloat(szTotalValue)-Float.parseFloat(szFee)-Float.parseFloat(szTip);
			MPosSendReceiptTask.szmeterAmount = Float.toString(meterAmount);

			MPosSendReceiptTask.execute();
		} else {
			Util.showToastMessage(Util.mContext, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}
	}

	void ShowMposWalkUpSendReceiptDialog() {

		if (Constants.isDebug)
			Log.e("CabPayFragment", "ShowSendReceiptDialog");

		final AlertDialog receiptDialog = new AlertDialog.Builder(Util.mContext).setTitle("Transaction successful!")
				.setMessage("You have successfully completed your transaction.Do you want receipt?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {

						// TODO Auto-generated
						// method
						// stub

						if (mMainBundle == null)
							mMainBundle = new Bundle();

						mMainBundle.putString(Constants.TOTAL_VALUE, szTotalValue);
						mMainBundle.putString(Constants.METER_VALUE, szMeterValue);
						mMainBundle.putString(Constants.TIP_VALUE, szTip);
						mMainBundle.putString("jobId", JobIdReturned);
						mMainBundle.putString(Constants.CARD_FEE_VALUE, szFee);

						// Payment type:
						mMainBundle.putString(Constants.CABPAY_PAYMENT_TYPE, "card");

						mMainBundle.putString(Constants.PAYMENT_METHOD_DETAILS, "card");
						mMainBundle.putBoolean(Constants.MPOS_WALKUP, true);
						mMainBundle.putString(Constants.MPOS_TRANSACTION_ID, szTransactionId);
						mMainBundle.putString(Constants.MPOS_PAYMENT_STATUS, String.valueOf(nPaymentStatus));

						mMainBundle.putString(Constants.MPOS_TRUNCATEDPAN, sTruncatedPan);
						mMainBundle.putString(Constants.MPOS_BRAND, sBrand);
						mMainBundle.putString(Constants.MPOS_MERCHANT_ID, sMerchantId);
						mMainBundle.putString(Constants.MPOS_TERMINAL_ID, sTerminalId);
						mMainBundle.putString(Constants.MPOS_ENTRY_MODE, sEntryMode);
						mMainBundle.putString(Constants.MPOS_AID, sAID);
						mMainBundle.putString(Constants.MPOS_PWID, sPWID);
						mMainBundle.putString(Constants.MPOS_AUTHORIZATION, sAuthorization);

						mMainBundle.putBoolean(Constants.CABPAY_WALKUP, true);

						Fragment fragment = null;
						fragment = new com.android.cabapp.fragments.CabPayReceiptFragment();
						fragment.setArguments(mMainBundle);
						if (fragment != null)
							((MainActivity) Util.mContext).replaceFragment(fragment, false);

					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated
						// method
						// stub
						if (NetworkUtil.isNetworkOn(Util.mContext)) {

							MPosSendReceiptTask MPosSendReceiptTask = new MPosSendReceiptTask();
							MPosSendReceiptTask.bIsWalkUp = true;

							dialogPaymentDetails = new ProgressDialog(Util.mContext);
							dialogPaymentDetails.setMessage("Updating...");
							dialogPaymentDetails.setCancelable(false);
							dialogPaymentDetails.show();
							MPosSendReceiptTask.pDialog = dialogPaymentDetails;
							MPosSendReceiptTask.szPaymentType = "card";
							MPosSendReceiptTask.szEmail = "";
							MPosSendReceiptTask.szMobile = "";
							MPosSendReceiptTask.szInternationalCode = "";
							MPosSendReceiptTask.szPU = "";
							MPosSendReceiptTask.szDO = "";

							MPosSendReceiptTask.mContext = Util.mContext;
							MPosSendReceiptTask.szJobID = JobIdReturned;
							MPosSendReceiptTask.szAmount = szTotalValue;
							MPosSendReceiptTask.szTip = szTip;
							MPosSendReceiptTask.szCardFees = szFee;
							MPosSendReceiptTask.szTransactionId = szTransactionId;
							MPosSendReceiptTask.szPaymentStatus = String.valueOf(nPaymentStatus);
							MPosSendReceiptTask.szSendingType = "none";

							MPosSendReceiptTask.szTruncatedPan = sTruncatedPan;
							MPosSendReceiptTask.szBrand = sBrand;
							MPosSendReceiptTask.szMerchantId = sMerchantId;
							MPosSendReceiptTask.szTerminalId = sTerminalId;
							MPosSendReceiptTask.szEntryMode = sEntryMode;
							MPosSendReceiptTask.szAID = sAID;
							MPosSendReceiptTask.szPWID = sPWID;
							MPosSendReceiptTask.szAuthorization = sAuthorization;

							MPosSendReceiptTask.execute();
						} else {
							Util.showToastMessage(Util.mContext, getResources().getString(R.string.no_network_error),
									Toast.LENGTH_LONG);
						}

					}
				}).create();
		receiptDialog.setCanceledOnTouchOutside(false);
		receiptDialog.show();

	}

	void CallSendReceipt() {

		if (NetworkUtil.isNetworkOn(Util.mContext)) {

			SendReceiptTask sendReceiptTask = new SendReceiptTask();

			SendReceiptTask.bIsWalkUp = false;

			sendReceiptTask.szPaymentType = "card";
			sendReceiptTask.szType = "email";
			sendReceiptTask.szEmail = mMainBundle.getString(Constants.PASSENGER_EMAIL);
			sendReceiptTask.szMobile = "";
			sendReceiptTask.szInternationalCode = "";

			sendReceiptTask.mContext = Util.mContext;
			sendReceiptTask.szJobID = JobIdReturned;

			sendReceiptTask.szAmount = String.valueOf(totalAmt);
			sendReceiptTask.szTip = String.valueOf(mMainBundle.getString(Constants.TIP_VALUE));
			sendReceiptTask.szFee = String.valueOf(mMainBundle.getString(Constants.CARD_FEE_VALUE));

			sendReceiptTask.execute();
		} else {
			Util.showToastMessage(Util.mContext, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}
	}

	class CabPinValidationTask extends AsyncTask<String, Void, String> {
		public String szCabPin, szJobId, szPaymentURL;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogPaymentDetails = new ProgressDialog(Util.mContext);
			dialogPaymentDetails.setMessage("Loading...");
			dialogPaymentDetails.setCancelable(false);
			dialogPaymentDetails.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// // TODO Auto-generated method stub
			CabPinValidation cabPinValidation = new CabPinValidation(Util.mContext, szCabPin, szJobId);
			String response = cabPinValidation.isCabPinValidResponse();
			try {
				JSONObject jObject = new JSONObject(response);

				if (jObject.has("success") && jObject.getString("success").equalsIgnoreCase("Valid")) {
					return "success";
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						String errorMessage = jErrorsArray.getJSONObject(0).getString("message");
						return errorMessage;
					}
				}
				return "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}

			return response;

			// return "success";

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialogPaymentDetails != null)
				dialogPaymentDetails.dismiss();

			if (result.equals("success")) {

				CallPayment();
				// checkForPOSDevice();
				// proceedWithPaymentWithoutHandpoint();
			} else {
				Util.showToastMessage(Util.mContext, "Invalid Cab Pin!", Toast.LENGTH_LONG);
			}

		}
	}

	void CallPayment() {

		if (NetworkUtil.isNetworkOn(Util.mContext)) {
			String szFeesPaidBy = "";

			PaymentTask paymentTask = new PaymentTask();
			paymentTask.jobId = mMainBundle.getString(Constants.JOB_ID);
			Log.e(TAG,
					"Hail job: bundle job id: " + mMainBundle.getString(Constants.JOB_ID) + "    paymentTask.jobId: "
							+ paymentTask.jobId + "  Brand:: " + mMainBundle.getString("brand") + "  TruncatedPan:: "
							+ mMainBundle.getString("truncatedPan"));

			if (AppValues.driverSettings != null && AppValues.driverSettings.getCardPaymentFeePaidBy() != null)
				szFeesPaidBy = AppValues.driverSettings.getCardPaymentFeePaidBy();
			if (mMainBundle != null && mMainBundle.containsKey(Constants.CAB_MILES)
					&& mMainBundle.getString(Constants.CAB_MILES) != null)
				paymentTask.cabmiles = mMainBundle.getString(Constants.CAB_MILES);

			paymentTask.meterAmount = mMainBundle.getString(Constants.METER_VALUE);
			paymentTask.totalAmount = String.valueOf(totalAmt);
			paymentTask.tip = mMainBundle.getString(Constants.TIP_VALUE);
			paymentTask.cardFees = mMainBundle.getString(Constants.CARD_FEE_VALUE);
			paymentTask.szFeesPaidBy = szFeesPaidBy;
			paymentTask.szCardPayworkToken = mMainBundle.getString(Constants.PAYMENT_ACCESS_TOKEN_VALUE);
			paymentTask.isRegisteredCard = String.valueOf(mMainBundle.getString(Constants.PAYMENT_CARD_ISSELECTED));
			paymentTask.szCardBrand = mMainBundle.getString("brand");
			paymentTask.szTruncatedPan = mMainBundle.getString("truncatedPan");
			paymentTask.isWalkUp = false;
			paymentTask.mHandler = mHandler;

			paymentTask.execute();
		} else {
			Util.showToastMessage(Util.mContext, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}

	}

	void checkForPOSDevice() {
		// Get local Bluetooth adapter
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
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

				mHandPointClass = new HandPointClass(Util.mContext, mCallbackListener, Util.getPOSDeviceName(Util.mContext));
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
						&& !(data.getExtras().getString(com.android.cabapp.activity.DeviceListActivity.EXTRA_DEVICE_ADDRESS)
								.equals(""))) {
					showConnectingDialog();

					mHandPointClass = new HandPointClass(Util.mContext, mCallbackListener, data.getExtras().getString(
							DeviceListActivity.EXTRA_DEVICE_NAME));
				} else {
					Util.showToastMessage(Util.mContext, "Proceeding with Payment without device", Toast.LENGTH_LONG);
					proceedWithPaymentWithoutHandpoint();
				}
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a bluetooth session
				try {
					Toast.makeText(getActivity(), "Bluetooth connected!", Toast.LENGTH_LONG).show();
					// connectToPED();
					transaction();
				} catch (Exception e) {

				}
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Util.showToastMessage(Util.mContext, getResources().getString(R.string.bt_not_enabled), Toast.LENGTH_SHORT);
			}
			break;
		}

	}

	private void transaction() {

		if (!bTransactionCompleted) {
			bTransactionCompleted = true;

			Log.e(TAG, "Transaction started");

			rlProgressLayout.setVisibility(View.VISIBLE);
			rlConfirm.setEnabled(false);
			// dialogPaymentDetails = new ProgressDialog(mContext);
			// dialogPaymentDetails.setMessage("Loading...");
			// dialogPaymentDetails.setCancelable(false);
			// dialogPaymentDetails.show();
			tvUpdatingText.setText("Loading...");

			/*
			 * This is test mode
			 */
			// final TransactionProvider transactionProvider =
			// Mpos.createTransactionProvider(mContext, ProviderMode.TEST,
			// MERCHANT_IDENTIFIER, MERCHANT_SECRET_KEY);

			/*
			 * This is mock mode
			 */
			final TransactionProvider transactionProvider = Mpos.createTransactionProvider(mContext, ProviderMode.TEST,
					MERCHANT_IDENTIFIER, MERCHANT_SECRET_KEY);

			Log.e(TAG, "totalAmt: " + totalAmt);
			String szCurrency = "";
			if (AppValues.driverSettings != null && AppValues.driverSettings.getCurrencyCode() != null)
				szCurrency = AppValues.driverSettings.getCurrencyCode();

			/*
			 * Since Cab:app currently supports only two payment mode EURO and
			 * GBP we are putting an if else condition. On multi payment we
			 * should support other way
			 */
			Boolean isEuro = AppValues.driverSettings.getCurrencyCode() == "EUR";
			TransactionTemplate transactionTemplate;
			if (isEuro) {
				transactionTemplate = transactionProvider.createChargeTransactionTemplate(new BigDecimal(totalAmt),
						io.mpos.transactions.Currency.EUR, "Chip and Pin Payment", "custom identifier");
			} else {
				transactionTemplate = transactionProvider.createChargeTransactionTemplate(new BigDecimal(totalAmt),
						io.mpos.transactions.Currency.GBP, "Chip and Pin Payment", "custom identifier");
			}

			Log.e(TAG, "transactionTemplate " + transactionTemplate.toString());

			/*
			 * AccessoryFamily.MIURA_MPI should be replace with
			 * AccessoryFamily.MOCK and vice-versa
			 */

			paymentProcess = transactionProvider.startPayment(transactionTemplate, AccessoryFamily.MIURA_MPI,
					new PaymentProcessWithRegistrationListener() {
						@Override
						public void onRegistered(PaymentProcess paymentProcess, Transaction transaction) {
							Log.d("mpos", "transaction identifier is: " + transaction.getIdentifier()
									+ ". Store it in your backend so that you can always query its status.");

							szTransactionId = transaction.getIdentifier();
							// dialogPaymentDetails
							// .setMessage("Processing payment details...");
							// dialogPaymentDetails.setCancelable(false);
							// dialogPaymentDetails.show();
							tvUpdatingText.setText("Processing payment details...");

							MPosChipAndPinTask mPosChipPinTask = new MPosChipAndPinTask();
							mPosChipPinTask.mContext = mContext;
							mPosChipPinTask.szTip = szTip;
							mPosChipPinTask.szCardFees = szFee;
							mPosChipPinTask.szMeterValue = szMeterValue;
							mPosChipPinTask.szTotalValue = szTotalValue;
							mPosChipPinTask.szTransactionId = szTransactionId;
							// mPosChipPinTask.pDialog = dialogPaymentDetails;
							mPosChipPinTask.execute();
						}

						@Override
						public void onCompleted(PaymentProcess paymentProcess, Transaction transaction,
								PaymentProcessDetails paymentProcessDetails) {

							rlProgressLayout.setVisibility(View.GONE);
							rlConfirm.setEnabled(true);
							// if (dialogPaymentDetails != null)
							// dialogPaymentDetails.dismiss();
							if (transaction != null && transaction.getStatus() != null) {
								bTransactionCompleted = false;

								Receipt receipt = paymentProcess.getReceiptFactory().createShopperReceipt(transaction);

								if (transaction != null && transaction.getStatus() != null) {
									nPaymentStatus = transaction.getStatus().ordinal();
									Log.d("mpos", "completed, transaction status is " + transaction.getStatus()
											+ "  ChipAndPinAPISuccess: " + MPosChipAndPinTask.isChipAndPinAPISuccess
											+ " number:: " + transaction.getStatus().ordinal());
									rlProgressLayout.setVisibility(View.GONE);

									/*
									 * Whatever the status will come, hit mpos
									 * send receipt api , type="none" and and
									 * show the status on pop up, then if a job
									 * is a walk up show walk up pop up and hit
									 * send receipt else directly hit mpos send
									 * receipt
									 */

									String sStatus = "";
									if (transaction.getStatus().ordinal() == 3) {
										if (receipt != null) {

											try {
												Log.v(TAG, " **************** :: Mpos Receipt :: ****************");
												/*
												 * truncated PAN eg : XXXXXXXXXXXX2138
												 */
												Log.v(TAG, "sTruncatedPan(Account) LABEL :: "
														+ receipt.getPaymentDetails().get(1).getLabel() + "   VALUE:: "
														+ receipt.getPaymentDetails().get(1).getValue());
												/*
												 * Card Brand VISA / MASTERCARD / AMEX
												 */
												Log.v(TAG, "sBrand(Card) LABEL :: "
														+ receipt.getPaymentDetails().get(0).getLabel() + "   VALUE :: "
														+ receipt.getPaymentDetails().get(0).getValue());
												/*
												 * MerchantID
												 */
												Log.v(TAG, "sMerchantId(Merchant ID)  LABEL ::  "
														+ receipt.getClearingDetails().get(2).getLabel() + "   VALUE :: "
														+ receipt.getClearingDetails().get(2).getValue());
												/*
												 * TerminalID
												 */
												Log.v(TAG, "sTerminalId(Terminal Id)  LABEL ::  "
														+ receipt.getClearingDetails().get(3).getLabel() + "   VALUE :: "
														+ receipt.getClearingDetails().get(3).getValue());
												/*
												 * AID
												 */
												Log.v(TAG, "szEntryMode(Entry Mode)  LABEL::  "
														+ receipt.getPaymentDetails().get(2).getLabel() + "   VALUE:: "
														+ receipt.getPaymentDetails().get(2).getValue());
												/*
												 * PWID
												 */
												Log.v(TAG, "szPWID(PWID)  LABEL::  " + receipt.getIdentifier().getLabel()
														+ "   VALUE:: " + receipt.getIdentifier().getValue());
												/*
												 * Authorization
												 */
												Log.v(TAG, "szAuthorization(Authorization)  LABEL::  "
														+ receipt.getClearingDetails().get(1).getLabel() + "   VALUE:: "
														+ receipt.getClearingDetails().get(1).getValue());
												Log.v(TAG, " **************** :: Mpos Receipt :: ****************");

												sTruncatedPan = receipt.getPaymentDetails().get(1).getValue();
												sBrand = receipt.getPaymentDetails().get(0).getValue();
												sMerchantId = receipt.getClearingDetails().get(2).getValue();
												sTerminalId = receipt.getClearingDetails().get(3).getValue();
												sEntryMode = receipt.getPaymentDetails().get(2).getValue();
												// sAID = "",
												sPWID = receipt.getIdentifier().getValue();
												sAuthorization = receipt.getClearingDetails().get(1).getValue();
											} catch (Exception e) {
												// TODO: handle exception
												Log.e(TAG, "onCompleted :: " + e.getMessage());
											}
										}

										// On success hit mPosSendReceipt
										//callMposSendReceipt(false, "none", false);
										
										// MPTransactionStatusApproved
										if (MPosChipAndPinTask.isChipAndPinAPISuccess) {
											if (isWalkup) {
												Log.e(TAG, "Mpos walk up job");
												ShowMposWalkUpSendReceiptDialog();
											} else {
												Log.e(TAG, "Mpos cab pay job");
												callMposSendReceipt(false, "email", true);
											}
										} else {
											// call mpostask payment api again
											// Send receipt
											callMposSendReceipt(false, "none", false);

										}
									} else if (transaction.getStatus().ordinal() == 0) {
										/* MPTransactionStatusUnknown */
										// sStatus = "Device not found";
										sStatus = transaction.getStatus().toString();
										mPosSendReceiptCall();
										callDefaultSendReceipt(sStatus);

										// Util.showToastMessage(
										// mContext,
										// "Chip and pin device not found",
										// Toast.LENGTH_LONG);

									} else if (transaction.getStatus().ordinal() == 1) {
										/* Accessory error */
										// sStatus = "Device not found";
										sStatus = transaction.getStatus().toString();
										mPosSendReceiptCall();
										callDefaultSendReceipt(sStatus);
										// Util.showToastMessage(
										// mContext,
										// "Chip and pin device not found",
										// Toast.LENGTH_LONG);

									} else if (transaction.getStatus().ordinal() == 2) {
										/* MPTransactionStatusPending */
										// sStatus = "Transaction Pending";
										sStatus = transaction.getStatus().toString();
										mPosSendReceiptCall();
										callDefaultSendReceipt(sStatus);
										// Util.showToastMessage(
										// mContext,
										// "Transaction Pending. Please try again",
										// Toast.LENGTH_LONG);

									} else if (transaction.getStatus().ordinal() == 4) {
										/* MPTransactionStatusDeclined */
										// sStatus = "Transaction Declined";
										sStatus = transaction.getStatus().toString();
										mPosSendReceiptCall();
										callDefaultSendReceipt(sStatus);
										// Util.showToastMessage(
										// mContext,
										// "Transaction Declined. Please try again",
										// Toast.LENGTH_LONG);

									} else if (transaction.getStatus().ordinal() == 5) {
										/* MPTransactionStatusAborted */
										// sStatus = "Transaction Aborted";
										sStatus = transaction.getStatus().toString();
										mPosSendReceiptCall();
										callDefaultSendReceipt(sStatus);
										// Util.showToastMessage(
										// mContext,
										// "Transaction Aborted. Please try again",
										// Toast.LENGTH_LONG);

									} else if (transaction.getStatus().ordinal() == 6) {
										/* MPTransactionStatusError */
										// sStatus =
										// "Some error occured while transaction. Please try again later";
										sStatus = transaction.getStatus().toString();
										mPosSendReceiptCall();
										callDefaultSendReceipt(sStatus);
										// Util.showToastMessage(
										// mContext,
										// "Error occured. Please try again",
										// Toast.LENGTH_LONG);

									}
								}

							} else {
								Util.showToastMessage(mContext, "Error in transaction.Please try again", Toast.LENGTH_LONG);
							}

							// completedTextView
							// .setText("completed, transaction status is "
							// + transaction.getStatus());
							// CALL THE API THAT THE TRANSACTION IS
							// COMPLETED/ERROR
						}

						@Override
						public void onStatusChanged(final PaymentProcess paymentProcess, Transaction transaction,
								PaymentProcessDetails paymentProcessDetails) {
							Log.d("mpos", "status changed: " + Arrays.toString(paymentProcessDetails.getInformation()));

							String szStatus = Arrays.toString(paymentProcessDetails.getInformation());
							Log.e(TAG, "szStatus: BEFORE" + szStatus);

							szStatus = szStatus.replaceAll("\\s+", " ").replaceAll("\\[", "").replaceAll("\\]", "");

							Log.e(TAG, "szStatus: AFTER" + szStatus);
							// szStatus = szStatus.replaceAll("]", "");
							// dialogPaymentDetails.setMessage(szStatus);
							tvUpdatingText.setText(szStatus);

							if (transaction != null) {
								Log.e(TAG, "szStatus: ABORT STATUS" + transaction.canBeAborted());
								rlAbortButton.setVisibility(transaction != null && transaction.canBeAborted() ? View.VISIBLE
										: View.INVISIBLE);
							}

							// statusTextView.setText(Arrays
							// .toString(paymentProcessDetails
							// .getInformation()));
							// SET THE TEXT OF THE LOADING INDICATOR
						}

						@Override
						public void onCustomerSignatureRequired(PaymentProcess paymentProcess, Transaction transaction) {
							// in a live app, this image comes from your
							// signature
							// screen

							// Bitmap.Config conf = Bitmap.Config.ARGB_8888;
							// Bitmap bm = Bitmap.createBitmap(1, 1, conf);
							// paymentProcess.continueWithCustomerSignature(bm
							// true);
							Log.e(TAG, "Required customer signature:: ");

							rlPaymentDetails.setVisibility(View.GONE);
							rlDigitalSignature.setVisibility(View.VISIBLE);
							if (AppValues.driverSettings != null && AppValues.driverSettings.getCurrencySymbol() != null)
								tvAmt.setText(AppValues.driverSettings.getCurrencySymbol() + ""
										+ String.format(Locale.ENGLISH, "%.2f", (float) (totalAmt)));
							else {
								tvAmt.setText(String.format(Locale.ENGLISH, "%.2f", (float) (totalAmt)));
							}

							if (MainActivity.slidingMenu != null)
								MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

						}

						@Override
						public void onCustomerVerificationRequired(PaymentProcess paymentProcess, Transaction transaction) {
							// in a live app, you would ask the customer to
							// identify
							// himself
							paymentProcess.continueWithCustomerIdentityVerified(true);
						}

						@Override
						public void onApplicationSelectionRequired(PaymentProcess paymentProcess, Transaction transaction,
								List<ApplicationInformation> applicationInformations) {
							// This happens only for readers that don't support
							// application selection on their screen
							paymentProcess.continueWithSelectedApplication(applicationInformations.get(0));
						}
					});
		} else {
			Toast.makeText(mContext, "A transaction is under process", Toast.LENGTH_LONG).show();
		}
	}

	void mPosSendReceiptCall() {
		if (isWalkup)
			callMposSendReceipt(true, "none", false);
		else
			callMposSendReceipt(false, "none", false);
	}

	void callDefaultSendReceipt(String szStatus) {
		AlertDialog TransactionAlertDialog = new AlertDialog.Builder(mContext).setTitle("Alert!").setMessage(szStatus)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {

					}
				}).create();
		TransactionAlertDialog.setCanceledOnTouchOutside(false);
		TransactionAlertDialog.show();

	}

	void showConnectingDialog() {
		// MyTimerTask myTask = new MyTimerTask();
		// myTask.bIsCancelConnectDialog = true;
		// myTimer = new Timer();
		// myTimer.schedule(myTask, 15000, 1500);

		mConnectingDialog = new ProgressDialog(Util.mContext);
		mConnectingDialog.setCanceledOnTouchOutside(false);
		mConnectingDialog.setCancelable(false);
		// mConnectingDialog.setMessage(getResources().getString(
		// R.string.connecting));
		mConnectingDialog.setMessage("Handpoint device entry found in account settings. Trying to connect. Please wait...");
		mConnectingDialog.show();
	}

	private void showChipAndPinPaymentFailedDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(Util.mContext);
		builder.setMessage("Payment using Chip and Pin failed!").setCancelable(false)
				.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						showConnectingDialog();
						mHandPointClass = new HandPointClass(Util.mContext, mCallbackListener, Util
								.getPOSDeviceName(Util.mContext));
					}
				}).setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
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

			Log.e("PaymentDetailsFragment", "onDeviceConnect");
			((Activity) Util.mContext).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mConnectingDialog != null)
						mConnectingDialog.setMessage("Handpoint device connected. Initiating payment process! Please wait...");
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
					if (response.contains("DECLINED") || response.contains("PROCESSED") || response.contains("FAILED")
							|| response.contains("CANCELLED")) {
						Util.showToastMessage(mContext, "Error processing payment!", Toast.LENGTH_LONG);
						showChipAndPinPaymentFailedDialog();
					}
				}
			});
		}

		@Override
		public void onDeviceNotFound() {
			// TODO Auto-generated method stub
			((Activity) Util.mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					dismissConnectingDialog();
					Util.showToastMessage(Util.mContext,
							"Unable to connect with Handpoint Device. Proceeding with Payment without device", Toast.LENGTH_LONG);
					proceedWithPaymentWithoutHandpoint();
				}
			});
		}

		@Override
		public void onTransactionComplete(final TransactionResult transactionResult) {
			// TODO Auto-generated method stub
			dismissConnectingDialog();
			// Util.showToastMessage(getActivity(),
			// "Chip And Pin payment successful!", Toast.LENGTH_LONG);

			((Activity) Util.mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					ProgressDialog pDialog = new ProgressDialog(mContext);
					pDialog.setMessage("Processing payment details...");
					pDialog.setCancelable(false);
					pDialog.show();

					ChipAndPinTask chipAndPinTask = new ChipAndPinTask();
					chipAndPinTask.bundle = mMainBundle;
					chipAndPinTask.transactionResult = transactionResult;
					chipAndPinTask.szTip = szTip;
					chipAndPinTask.szJobID = szJobID;
					chipAndPinTask.szTotalValue = szTotalValue;
					chipAndPinTask.szCardFees = szFee;
					chipAndPinTask.bIsFromCabPayFragment = false;
					chipAndPinTask.mContext = Util.mContext;
					chipAndPinTask.pDialog = pDialog;
					chipAndPinTask.execute();
				}
			});
		}
	};

	class HandpointTimerTask extends TimerTask {

		public void run() {
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

		String szPaymentURL = Constants.driverURL + "payment?accessToken=" + Constants.accessToken + "&totalAmount="
				+ nTotalValue + "&fee=" + nFee + "&tip=" + nTip + "&amount=" + nMeterValue;

		if (AppValues.driverSettings != null)
			szPaymentURL = szPaymentURL + "&feePaidBy=" + AppValues.driverSettings.getCardPaymentFeePaidBy();

		if (mMainBundle == null)
			mMainBundle = new Bundle();

		mMainBundle.putString(Constants.TOTAL_VALUE, szTotalValue);
		mMainBundle.putString(Constants.TIP_VALUE, szTip);

		// dialogPaymentDetails = new ProgressDialog(mContext);
		// dialogPaymentDetails.setMessage("Loading...");
		// dialogPaymentDetails.setCancelable(false);

		GetPaymentURLTask getPaymentURLTask = new GetPaymentURLTask();
		getPaymentURLTask.szURL = szPaymentURL;
		getPaymentURLTask.mBundle = mMainBundle;
		getPaymentURLTask.mContext = mContext;
		// getPaymentURLTask.pDialog = dialogPaymentDetails;
		getPaymentURLTask.execute();
	}

	/**
	 * save the signature to an sd card directory
	 * 
	 * @param signature
	 *            bitmap
	 */
	final void saveImage(Bitmap signature) {

		String root = Environment.getExternalStorageDirectory().toString();

		// the directory where the signature will be saved
		File myDir = new File(root + "/saved_signature");

		// make the directory if it does not exist yet
		if (!myDir.exists()) {
			myDir.mkdirs();
		}

		// set the file name of your choice
		String fname = "signature.png";

		// in our case, we delete the previous file, you can remove this
		File file = new File(myDir, fname);
		if (file.exists()) {
			file.delete();
		}

		try {

			// save the signature
			FileOutputStream out = new FileOutputStream(file);
			signature.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();

			Toast.makeText(mContext, "Signature saved.", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
