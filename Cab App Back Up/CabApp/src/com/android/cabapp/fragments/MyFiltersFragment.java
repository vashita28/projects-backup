package com.android.cabapp.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.SignUp_Payment_Activity;
import com.android.cabapp.async.DriverDetailsTask;
import com.android.cabapp.model.AccountModification;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class MyFiltersFragment extends RootFragment {
	private static final String TAG = MyFiltersFragment.class.getSimpleName();

	TextView textCashAndCardFilter, textCashFilter, textYes, textNo;
	public static boolean isCardFilterSelected = false;
	public static boolean isCashFilterSelected = true;

	private ProgressDialog dialogMyFilterFragment;

	Context mContext;

	public static Handler mHandlerMyFilter;

	Bundle mBundle;

	public MyFiltersFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my_filters,
				container, false);
		super.initWidgets(rootView, this.getClass().getName());
		mContext = getActivity();

		textCashAndCardFilter = (TextView) rootView
				.findViewById(R.id.tvCashAndCardFilter);
		textCashFilter = (TextView) rootView
				.findViewById(R.id.tvCashOnlyFilter);

		if (!Util.getIsOverlaySeen(getActivity(),
				MyAccountFragment.class.getSimpleName())) {
			disableClick();
		}
		return rootView;
	}

	public void updateView() {
		if (AppValues.driverDetails.getPaymentType().toLowerCase()
				.equals("both")) {
			textCashAndCardFilter.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.allpayment, 0, 0);
			textCashAndCardFilter.setTextColor(getResources().getColor(
					R.color.textview_selected));
			isCardFilterSelected = true;
		} else {
			textCashAndCardFilter.setCompoundDrawablesWithIntrinsicBounds(0,
					R.drawable.allpaymentunselected, 0, 0);
			textCashAndCardFilter.setTextColor(getResources().getColor(
					R.color.textcolor_grey));

			isCardFilterSelected = false;
		}
	}

	public void disableClick() {
		Log.e(TAG, "disableClick");
		textCashAndCardFilter.setEnabled(false);
		textCashFilter.setEnabled(false);
	}

	public void enableClick() {
		textCashAndCardFilter.setEnabled(true);
		textCashFilter.setEnabled(true);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		textCashAndCardFilter.setOnTouchListener(new TextTouchListener());
		textCashFilter.setOnTouchListener(new TextTouchListener());

		if (AppValues.driverDetails == null) {
			dialogMyFilterFragment = new ProgressDialog(mContext);
			dialogMyFilterFragment.setMessage("Loading...");
			dialogMyFilterFragment.setCancelable(false);
			dialogMyFilterFragment.show();

			DriverDetailsTask driverDetailsTask = new DriverDetailsTask();
			driverDetailsTask.isFromMyFilters = true;
			driverDetailsTask.mContext = getActivity();
			driverDetailsTask.execute();
		} else {
			updateView();
		}

		mHandlerMyFilter = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if (dialogMyFilterFragment != null)
					dialogMyFilterFragment.dismiss();
				if (AppValues.driverDetails != null
						&& AppValues.driverDetails.getPaymentType()
								.toLowerCase().equals("both")) {
					textCashAndCardFilter
							.setCompoundDrawablesWithIntrinsicBounds(0,
									R.drawable.allpayment, 0, 0);
					textCashAndCardFilter.setTextColor(getResources().getColor(
							R.color.textview_selected));
					isCardFilterSelected = true;
				}

			}
		};
	}

	class TextTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				// toggleTextViewBackgroundFilter((TextView) v);

				switch (v.getId()) {
				case R.id.tvCashAndCardFilter:

					if (Constants.isDebug)
						Log.e(TAG, "isCardSelected: " + isCardFilterSelected);

					mBundle = new Bundle();
					if (!isCardFilterSelected)
						mBundle.putString(Constants.PAYMENT_TYPE, "both");
					else
						mBundle.putString(Constants.PAYMENT_TYPE, "cash");

					if (AppValues.driverDetails != null) {
						mBundle.putString(Constants.WORKING_CITY,
								AppValues.driverDetails.getWorkingCity());
						mBundle.putString(Constants.FIRSTNAME,
								AppValues.driverDetails.getFirstname());
						mBundle.putString(Constants.SURNAME,
								AppValues.driverDetails.getSurname());
						mBundle.putString(Constants.EMAIL_ADDRESS,
								AppValues.driverDetails.getEmail());
						mBundle.putString(Constants.USERNAME,
								AppValues.driverDetails.getUsername());
						mBundle.putString(Constants.REGISTRATION_PASSWORD,
								Util.getPassword(mContext));
						mBundle.putString(Constants.BADGE_COLOUR,
								AppValues.driverDetails.getBadgeColour());
						mBundle.putString(Constants.BADGE_NUMBER,
								AppValues.driverDetails.getBadgeNumber());
						mBundle.putString(Constants.DRIVER_BADGE_EXPIRY,
								AppValues.driverDetails.getBadgeExpiration());
						mBundle.putString(Constants.MOBILE_NUMBER,
								AppValues.driverDetails.getMobileNumber());
						mBundle.putString(Constants.COUNRTY_CODE,
								AppValues.driverDetails.getInternationalCode());
						mBundle.putString(
								Constants.WHEEL_CHAIR_ACCESS_REQUIRED,
								AppValues.driverDetails.getWheelchairAccess());
						mBundle.putString(Constants.VECHILE_MAXIMUM_PASSENGERS,
								String.valueOf(AppValues.driverDetails
										.getPassengerCapacity()));

						mBundle.putString(Constants.VECHILE_REGISTRATION_NO,
								AppValues.driverDetails.getVehicle()
										.getRegistration());
						mBundle.putString(Constants.VECHILE_MAKE,
								AppValues.driverDetails.getVehicle().getMake());
						mBundle.putString(Constants.VECHILE_COLOUR,
								AppValues.driverDetails.getVehicle()
										.getColour());
						mBundle.putString(Constants.VECHILE_MODEL,
								AppValues.driverDetails.getVehicle().getModel());

						// if (isCardFilterSelected) {
						if (AppValues.driverDetails.getBankDetails() == null) {

							final Dialog dialog = new Dialog(mContext,
									R.style.mydialogstyle);
							dialog.setContentView(R.layout.bank_details_dialog);
							dialog.setCanceledOnTouchOutside(false);
							dialog.setCancelable(false);
							dialog.show();
							textYes = (TextView) dialog
									.findViewById(R.id.tvYes);
							textNo = (TextView) dialog.findViewById(R.id.tvNo);
							textNo.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
							textYes.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									dialog.dismiss();

									Intent intentBankDetails = new Intent(
											getActivity(),
											SignUp_Payment_Activity.class);
									intentBankDetails
											.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									mBundle.putBoolean("isFromMyFilter", true);
									intentBankDetails.putExtras(mBundle);
									startActivity(intentBankDetails);
								}
							});

						} else {
							mBundle.putString(Constants.BANK_NAME,
									AppValues.driverDetails.getBankDetails()
											.getName());
							mBundle.putString(Constants.BANK_ACCOUNT_NAME,
									AppValues.driverDetails.getBankDetails()
											.getAccountName());
							mBundle.putString(Constants.BANK_ACCOUNT_NUMBER,
									AppValues.driverDetails.getBankDetails()
											.getAccountNumber());
							mBundle.putString(Constants.BANK_SORT_CODE,
									AppValues.driverDetails.getBankDetails()
											.getSortCode());

							if (NetworkUtil.isNetworkOn(Util.mContext)) {
								dialogMyFilterFragment = new ProgressDialog(
										mContext);
								dialogMyFilterFragment.setMessage("Loading...");
								dialogMyFilterFragment.setCancelable(false);
								dialogMyFilterFragment.show();

								MyFilterTask myFilterTask = new MyFilterTask();
								myFilterTask.bundle = mBundle;
								myFilterTask.execute();
							} else
								Util.showToastMessage(mContext, mContext
										.getString(R.string.no_network_error),
										Toast.LENGTH_LONG);
						}
						// }

					}

					break;

				case R.id.tvCashOnlyFilter:
					// isCashFilterSelected = toggleTextViewCheckBox(
					// textCashFilter, R.drawable.pound_selected,
					// R.drawable.pound_unselected, isCashFilterSelected);
					break;
				}
			}
			return true;
		}
	}

	public boolean toggleTextViewCheckBox(TextView tv, int selectedResId,
			int unselectedResId, boolean isSelected) {
		if (!isSelected) {
			isSelected = true;
			tv.setCompoundDrawablesWithIntrinsicBounds(0, selectedResId, 0, 0);
			tv.setTextColor(getResources().getColor(R.color.textview_selected));
		} else {
			isSelected = false;
			tv.setTextColor(getResources().getColor(R.color.textcolor_grey));
			tv.setCompoundDrawablesWithIntrinsicBounds(0, unselectedResId, 0, 0);
		}
		return isSelected;
	}

	// void toggleTextViewBackgroundFilter(TextView textViewSelected) {
	// textCashAndCardFilter.setCompoundDrawablesWithIntrinsicBounds(0,
	// R.drawable.cardunselectedfilter, 0, 0);
	// textCashFilter.setCompoundDrawablesWithIntrinsicBounds(0,
	// R.drawable.pound_unselected, 0, 0);
	//
	// textCashAndCardFilter.setTextColor(getResources().getColor(
	// R.color.textcolor_grey));
	// textCashFilter.setTextColor(getResources().getColor(
	// R.color.textcolor_grey));
	//
	// if (textViewSelected == textCashAndCardFilter)
	// textViewSelected.setCompoundDrawablesWithIntrinsicBounds(0,
	// R.drawable.cardselectedfilter, 0, 0);
	// else if (textViewSelected == textCashFilter)
	// textViewSelected.setCompoundDrawablesWithIntrinsicBounds(0,
	// R.drawable.pound_selected, 0, 0);
	//
	// textViewSelected.setTextColor(getResources().getColor(
	// R.color.textview_selected));
	//
	// }

	public class MyFilterTask extends AsyncTask<String, Void, String> {
		public Bundle bundle;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.e(TAG, "MyfilterTask call from my filter only::> ");
			AccountModification accModification = new AccountModification(
					mContext, bundle);
			String response = accModification.ModificationCredentials();
			Log.e(TAG, "MyfilterTask response::> " + response);
			try {
				JSONObject jObject = new JSONObject(response);
				String errorMessage = "";
				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {

					DriverAccountDetails driverAccount = new DriverAccountDetails(
							mContext);
					driverAccount.retriveAccountDetails(mContext);

					DriverSettingDetails driverSettings = new DriverSettingDetails(
							mContext);
					driverSettings.retriveDriverSettings(mContext);

					if (jObject.has("isauthorized")) {
						if (jObject.getString("isauthorized").equals("false")) {
							return "success";
						} else if (jObject.getString("isauthorized").equals(
								"true")) {
							errorMessage = "pending";
							return errorMessage;
						}
					}
					// return "success";
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						errorMessage = jErrorsArray.getJSONObject(0).getString(
								"message");
						return errorMessage;
					}
				}
				return "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return response;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialogMyFilterFragment != null)
				dialogMyFilterFragment.dismiss();

			if (result.equals("success")) {
				isCardFilterSelected = toggleTextViewCheckBox(
						textCashAndCardFilter, R.drawable.allpayment,
						R.drawable.allpaymentunselected, isCardFilterSelected);

				if (!isCardFilterSelected && Util.mContext != null) {
					AlertDialog cardDialog = new AlertDialog.Builder(
							Util.mContext)
							.setMessage(
									"You will only see cash jobs, turn on again to see card jobs")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface argDialog,
												int argWhich) {
										}
									}).create();
					cardDialog.setCanceledOnTouchOutside(false);
					cardDialog.show();
				}

				// Util.setIsAuthorised(mContext, false);
			}
			// else if (result.equals("pending")) {
			// Util.showToastMessage(mContext,
			// "Your account is in pending state.", Toast.LENGTH_LONG);
			// MainActivity.finishActivity();
			//
			// Intent intent = new Intent(getActivity(),
			// TutorialActivity.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// startActivity(intent);
			// }
			else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}

		}

	}

}
