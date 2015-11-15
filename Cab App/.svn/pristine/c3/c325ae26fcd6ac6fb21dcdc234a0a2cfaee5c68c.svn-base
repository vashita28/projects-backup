package com.android.cabapp.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.ChatActivity;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.async.NoShowTask;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.model.SendMessage;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.DirectionsJSONParser;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.android.cabapp.view.OverlayCircle;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class JobAcceptedFragment extends RootFragment {
	private static final String TAG = JobAcceptedFragment.class.getSimpleName();

	RelativeLayout rl_btnIamOutside, rl_btnNoShow, rlMapIcon, rlContact,
			rlPayment;// rlBottomNormal
	LinearLayout llbottombarHistory, llBottomNormal, llHistoryJobHeader;
	// TextView tvPayment, tvShowMap,tvContact;

	// Overlay: ActiveJobs
	OverlayCircle overlayMyJobs, overlayArrivedAtPickUp,
			overlayContactPassenger, overlayPayment;
	RelativeLayout rlOverlayActiveJobs, rlOverlayArrivedAtPickUp,
			contentArrived, rlOverlayContactPassenger, contentContactPassenger,
			rlOverlayPayment, contentPayment;
	TextView tvSkipBottomArrived, tvNextBottomArrived, tvSkipTopArrived,
			tvNextTopArrived;
	LinearLayout llBottomButtonsOverlay, llTopButtonsArrivedOverlay;

	TextView txtPickupAddress, txtPickUpPinCode, txtDistancePickup,
			txtDropAddress, txtDropOffPinCode, txtDistanceDrop,
			txtPassengerCount, txtFare, txtLuggage, tvPassengerName, txtTime,
			txtDistanceUnit, tvContactHistory, tvContact, tvSendReceiptHistory,
			textPickUpTimeCounter, tvCountDownTime, tvJobStatus, txtJobTime;
	ImageView ivWheelChair, ivPayment, ivHearingImpaired, ivDown;

	Location currentLocation;
	HashMap<String, Distance> mapDistance = new HashMap<String, Distance>();

	String szPassengerNumber = "", szPassengerName = "";

	Timer mMinutesPassengerWaiting, mNoShowTimer;
	SimpleDateFormat dfDate_day = new SimpleDateFormat("HH:mm:ss");
	Calendar c = Calendar.getInstance();
	String currentDateandTime;
	String sPassengerdateTime;
	String szArrivedMessageTime, sJobID;

	Timer mCountdownTimer;
	ArrayList<LatLng> points = null;
	LatLng pickUpPosition;

	IntentFilter jobscancelledIntentFilter;

	ProgressDialog progressDialog;
	Context mContext;

	public JobAcceptedFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_job_accepted,
				container, false);
		mContext = getActivity();

		// Overlays:ActiveJobs,ArrivedAtPickUp,Contact,Payment
		llBottomButtonsOverlay = (LinearLayout) rootView
				.findViewById(R.id.llBottomButtonsOverlay);
		llTopButtonsArrivedOverlay = (LinearLayout) rootView
				.findViewById(R.id.llTopButtonsArrivedOverlay);
		rlOverlayActiveJobs = (RelativeLayout) rootView
				.findViewById(R.id.rlOverlayActiveJobs);
		rlOverlayArrivedAtPickUp = (RelativeLayout) rootView
				.findViewById(R.id.rlOverlayArrivedAtPickUp);
		contentArrived = (RelativeLayout) rootView
				.findViewById(R.id.contentArrived);
		overlayArrivedAtPickUp = (OverlayCircle) rootView
				.findViewById(R.id.overlayArrived);
		rlOverlayContactPassenger = (RelativeLayout) rootView
				.findViewById(R.id.rlOverlayContactPassenger);
		contentContactPassenger = (RelativeLayout) rootView
				.findViewById(R.id.contentContactPassenger);
		overlayContactPassenger = (OverlayCircle) rootView
				.findViewById(R.id.overlayContactPassenger);
		rlOverlayPayment = (RelativeLayout) rootView
				.findViewById(R.id.rlOverlayPayment);
		contentPayment = (RelativeLayout) rootView
				.findViewById(R.id.contentPayment);
		overlayPayment = (OverlayCircle) rootView
				.findViewById(R.id.overlayPayment);
		tvSkipBottomArrived = (TextView) rootView
				.findViewById(R.id.tvSkipBottomArrived);
		tvNextBottomArrived = (TextView) rootView
				.findViewById(R.id.tvNextBottomArrived);
		tvSkipTopArrived = (TextView) rootView
				.findViewById(R.id.tvSkipTopArrived);
		tvNextTopArrived = (TextView) rootView
				.findViewById(R.id.tvNextTopArrived);

		textPickUpTimeCounter = (TextView) rootView
				.findViewById(R.id.txtTimeStatus);
		tvCountDownTime = (TextView) rootView
				.findViewById(R.id.tvCountDownTime);
		rlContact = (RelativeLayout) rootView.findViewById(R.id.rlContact);
		rlPayment = (RelativeLayout) rootView.findViewById(R.id.rlPayment);
		rlMapIcon = (RelativeLayout) rootView.findViewById(R.id.rlMapIcon);
		rl_btnIamOutside = (RelativeLayout) rootView
				.findViewById(R.id.rl_btnIamOutside);
		rl_btnNoShow = (RelativeLayout) rootView
				.findViewById(R.id.rl_btnNoShow);
		llBottomNormal = (LinearLayout) rootView
				.findViewById(R.id.llBottomNormal);
		llbottombarHistory = (LinearLayout) rootView
				.findViewById(R.id.llbottombarHistory);
		tvContactHistory = (TextView) rootView
				.findViewById(R.id.tvContactHistory);
		tvContact = (TextView) rootView.findViewById(R.id.tvContact);
		tvSendReceiptHistory = (TextView) rootView
				.findViewById(R.id.tvSendReceiptHistory);
		txtDistanceUnit = (TextView) rootView
				.findViewById(R.id.txtDistanceUnit);
		ivDown = (ImageView) rootView.findViewById(R.id.ivDown);
		llHistoryJobHeader = (LinearLayout) rootView
				.findViewById(R.id.llHistoryJobHeader);
		tvJobStatus = (TextView) rootView.findViewById(R.id.tvJobStatus);
		txtJobTime = (TextView) rootView.findViewById(R.id.txtJobTime);
		tvPassengerName = (TextView) rootView
				.findViewById(R.id.tvPassengerName);
		txtPickupAddress = (TextView) rootView
				.findViewById(R.id.txtPickupAddress);
		txtPickUpPinCode = (TextView) rootView
				.findViewById(R.id.txtPickupPincode);
		txtDistancePickup = (TextView) rootView
				.findViewById(R.id.txtDistancePickup);
		txtDropAddress = (TextView) rootView.findViewById(R.id.txtDropAddress);
		txtDropOffPinCode = (TextView) rootView
				.findViewById(R.id.txtDropOffPinCode);
		txtDistanceDrop = (TextView) rootView
				.findViewById(R.id.txtDistanceDrop);
		txtPassengerCount = (TextView) rootView
				.findViewById(R.id.txtPassengerCount);
		txtFare = (TextView) rootView.findViewById(R.id.txtFare);
		txtLuggage = (TextView) rootView.findViewById(R.id.txtLuggage);
		txtTime = (TextView) rootView.findViewById(R.id.txtTime);
		ivWheelChair = (ImageView) rootView.findViewById(R.id.ivWheelchair);
		ivPayment = (ImageView) rootView.findViewById(R.id.ivPayment);
		ivHearingImpaired = (ImageView) rootView
				.findViewById(R.id.ivHearingImpaired);

		// tvContact = (TextView) rootView.findViewById(R.id.tvContact);
		// tvPayment = (TextView) rootView.findViewById(R.id.tvPayment);
		// tvShowMap = (TextView) rootView.findViewById(R.id.tvShowMap);
		// rlBottomNormal = (RelativeLayout) rootView
		// .findViewById(R.id.rlBottomNormal);

		// tvContact.setOnTouchListener(new TextTouchListener());
		// tvPayment.setOnTouchListener(new TextTouchListener());
		// tvShowMap.setOnTouchListener(new TextTouchListener());
		rlContact.setOnTouchListener(new TextTouchListener());
		rlPayment.setOnTouchListener(new TextTouchListener());
		rlMapIcon.setOnTouchListener(new TextTouchListener());
		rl_btnIamOutside.setOnTouchListener(new TextTouchListener());
		rl_btnNoShow.setOnTouchListener(new TextTouchListener());
		tvContactHistory.setOnTouchListener(new TextTouchListener());
		tvSendReceiptHistory.setOnTouchListener(new TextTouchListener());
		tvSkipBottomArrived.setOnTouchListener(new TextTouchListener());
		tvNextBottomArrived.setOnTouchListener(new TextTouchListener());
		tvSkipTopArrived.setOnTouchListener(new TextTouchListener());
		tvNextTopArrived.setOnTouchListener(new TextTouchListener());

		if (!Util.getIsOverlaySeen(getActivity(), TAG)) {

			if (MainActivity.slidingMenu != null)
				MainActivity.slidingMenu
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

			overlayMyJobs = (OverlayCircle) rootView
					.findViewById(R.id.overlayHeader);
			overlayMyJobs
					.setRadius(getResources().getDisplayMetrics().heightPixels / 8);
			overlayMyJobs
					.setYDistance(getResources().getDisplayMetrics().heightPixels / 7);
			overlayMyJobs
					.setXDistance(getResources().getDisplayMetrics().widthPixels
							- (getResources().getDisplayMetrics().widthPixels / 12));
			overlayMyJobs.setLayoutParams(new RelativeLayout.LayoutParams(
					getResources().getDisplayMetrics().widthPixels,
					getResources().getDisplayMetrics().heightPixels / 8
							+ getResources().getDisplayMetrics().heightPixels
							/ 7));
			llBottomButtonsOverlay.setVisibility(View.VISIBLE);
			rlOverlayActiveJobs.setVisibility(View.VISIBLE);
			rl_btnIamOutside.setEnabled(false);
			rlMapIcon.setEnabled(false);
			rlContact.setEnabled(false);
			rlPayment.setEnabled(false);
			rl_btnIamOutside.setVisibility(View.VISIBLE);
			rl_btnNoShow.setVisibility(View.GONE);
			rl_btnNoShow.setEnabled(false);
		}

		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((MainActivity) Util.mContext).setDontShowQuitDialog();

		Util.bIsJobAcceptedFragment = true;

		if (Util.getLocation(Util.mContext) != null)
			currentLocation = Util.getLocation(Util.mContext);

		mMainBundle = this.getArguments();
		if (mMainBundle != null
				&& mMainBundle.containsKey(Constants.IS_FROM_HISTORY)
				&& mMainBundle.getBoolean(Constants.IS_FROM_HISTORY)) {
			llBottomNormal.setVisibility(View.GONE);
			llbottombarHistory.setVisibility(View.VISIBLE);
			rl_btnIamOutside.setVisibility(View.GONE);
			rl_btnNoShow.setVisibility(View.GONE);
			rlMapIcon.setVisibility(View.GONE);

			// For Cancelled job: disabled send receipt and contact
			if (mMainBundle.containsKey(Constants.CANCELLED)
					&& mMainBundle.getString(Constants.CANCELLED) != null
					&& mMainBundle.getString(Constants.CANCELLED)
							.equalsIgnoreCase("true")) {
				disableChat();
				disableSendReceipt();
			}

			// IF history job:Hide distance
			llHistoryJobHeader.setVisibility(View.VISIBLE);
			rl_btnNoShow.setVisibility(View.GONE);
			txtDistanceUnit.setVisibility(View.GONE);
			txtDistancePickup.setVisibility(View.GONE);
			ivDown.setVisibility(View.GONE);
			txtDistanceDrop.setVisibility(View.GONE);
			txtTime.setVisibility(View.GONE);

			// Setting Job Status
			String szStatus = "", szCancelledStatus = "", szIsNoShow = "";
			if (mMainBundle.containsKey(Constants.CLIENT_PAID)) {
				szStatus = mMainBundle.getString(Constants.CLIENT_PAID);
			}
			if (mMainBundle.containsKey(Constants.CANCELLED)
					&& mMainBundle.getString(Constants.CANCELLED) != null)
				szCancelledStatus = mMainBundle.getString(Constants.CANCELLED);
			if (mMainBundle.containsKey(Constants.NO_SHOW_STATUS)
					&& mMainBundle.getString(Constants.NO_SHOW_STATUS) != null)
				szIsNoShow = mMainBundle.getString(Constants.NO_SHOW_STATUS);

			if (szCancelledStatus.equalsIgnoreCase("true")) {
				if (szIsNoShow.equals("y")) {
					tvJobStatus.setBackgroundColor(mContext.getResources()
							.getColor(R.color.textcolor_red));
					tvJobStatus.setText("NO SHOW");
				} else {
					tvJobStatus.setBackgroundColor(mContext.getResources()
							.getColor(R.color.textcolor_red));
					tvJobStatus.setText("CANCELLED");
				}
			} else if (szStatus.equals("unpaid")) {
				tvJobStatus.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textcolor_red));
				tvJobStatus.setText("CARD-OWED");// UNPAID
			} else if (szStatus.equals("paid")) {
				tvJobStatus.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textcolor_green));
				tvJobStatus.setText("CARD-PAID");
			} else {
				tvJobStatus.setBackgroundColor(mContext.getResources()
						.getColor(R.color.textcolor_green));
				tvJobStatus.setText("CASH");
			}

			// Time in history
			String date = Util.getTimeFormat(mMainBundle
					.getString(Constants.TIME));
			txtJobTime.setText(date);

		} else {
			llBottomNormal.setVisibility(View.VISIBLE);
			llbottombarHistory.setVisibility(View.GONE);
			rlMapIcon.setVisibility(View.VISIBLE);
		}

		if (mMainBundle != null) {
			if (Constants.isDebug)
				Log.i(TAG, "mMainBundle::  " + mMainBundle.toString());

			// Handle "Arrived at Pick Up"
			if (AppValues.mapArrivedAtPickUpMessages != null
					&& AppValues.mapArrivedAtPickUpMessages
							.containsKey(mMainBundle.get(Constants.JOB_ID))) {

				String szIsArrived = AppValues.mapArrivedAtPickUpMessages
						.get(mMainBundle.get(Constants.JOB_ID));
				if (szIsArrived.equals("true"))
					rl_btnIamOutside.setVisibility(View.GONE);

			} else if (mMainBundle.containsKey(Constants.JSON_GET_MESSAGE)) {
				isArrivedAtPickUpCkicked();
			}

			// NoShow
			if (llHistoryJobHeader.getVisibility() == View.GONE
					&& rl_btnIamOutside.getVisibility() == View.GONE) {
				sJobID = mMainBundle.getString(Constants.JOB_ID);
				szArrivedMessageTime = mMainBundle.getString(Constants.TIME);
				Log.e(TAG, "szArrivedMessageTime    " + szArrivedMessageTime);
				if (szArrivedMessageTime == null
						|| szArrivedMessageTime.isEmpty())
					isArrivedAtPickUpCkicked();
				if (szArrivedMessageTime != null
						&& !szArrivedMessageTime.isEmpty()) {
					mNoShowTimer = new Timer();
					mNoShowTimer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							NoShowEnabled(szArrivedMessageTime);
						}
					}, 0, 30000);
				}
			}

			if (mMainBundle.containsKey("isPreBook")
					&& !mMainBundle.getBoolean("isPreBook")) {

				// Countdown timer
				points = new ArrayList<LatLng>();

				if (!(rl_btnIamOutside.getVisibility() == View.VISIBLE)) {
					tvCountDownTime.setVisibility(View.GONE);
					if (mCountdownTimer != null)
						mCountdownTimer.cancel();
				} else {

					if (mMainBundle.containsKey("countdowntime")
							&& mMainBundle.getString("countdowntime") != null) {
						tvCountDownTime.setText(mMainBundle
								.getString("countdowntime"));
					}

					LatLng currentLoc = null;
					if (currentLocation != null) {
						currentLoc = new LatLng(currentLocation.getLatitude(),
								currentLocation.getLongitude());
					}
					pickUpPosition = new LatLng(Double.valueOf(mMainBundle
							.getString(Constants.PICK_UP_LOCATION_LAT)),
							Double.valueOf(mMainBundle
									.getString(Constants.PICK_UP_LOCATION_LNG)));
					points.clear();
					points.add(pickUpPosition);
					points.add(currentLoc);

					mCountdownTimer = new Timer();
					mCountdownTimer.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (Util.mContext != null)
								((Activity) Util.mContext)
										.runOnUiThread(new Runnable() {
											public void run() {
												ShowCountDown(points);
											}
										});
						}
					}, 0, 30000);
				}

				// PickupDateTime counter
				if (mMainBundle.containsKey(Constants.TIME)) {

					sPassengerdateTime = mMainBundle.getString(Constants.TIME);
					mMinutesPassengerWaiting = new Timer();

					if (!(rl_btnIamOutside.getVisibility() == View.VISIBLE)) {
						textPickUpTimeCounter.setVisibility(View.GONE);
						if (mMinutesPassengerWaiting != null)
							mMinutesPassengerWaiting.cancel(); // timer is
																// cancelled
																// when time
																// reaches 0
					} else {
						// instantiated
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								mMinutesPassengerWaiting.schedule(
										new TimerTask() {
											@Override
											public void run() {

												textPickUpTimeCounter
														.setVisibility(View.VISIBLE);
												currentDateandTime = dfDate_day
														.format(new Date());
												SimpleDateFormat format = new SimpleDateFormat(
														"HH:mm:ss");

												Date dPassengerJobtime, dCurrentTime;
												final int dif;
												try {
													dPassengerJobtime = format
															.parse(Util
																	.getTimeOnly(
																			(sPassengerdateTime))
																	.toString());
													dCurrentTime = format
															.parse(currentDateandTime);
													dif = (int) (dPassengerJobtime
															.getTime() / 60000 - dCurrentTime
															.getTime() / 60000);
													getActivity()
															.runOnUiThread(
																	new Runnable() {
																		public void run() {

																			if (dif < 0) {
																				textPickUpTimeCounter
																						.setTextColor(getResources()
																								.getColor(
																										R.color.button_red));
																			} else {
																				textPickUpTimeCounter
																						.setTextColor(getResources()
																								.getColor(
																										R.color.button_green));
																			}

																			textPickUpTimeCounter
																					.setText("("
																							+ dif
																							+ " mins)");

																			// if
																			// ((dif
																			// <
																			// -60)
																			// ||
																			// (dif
																			// >
																			// 60))
																			// {
																			// //
																			// More
																			// than
																			// 1
																			// hour
																			// if
																			// (Constants.isDebug)
																			// Log.e(TAG,
																			// "Diff >60 "
																			// +
																			// dif);
																			//
																			// textPickUpTimeCounter
																			// .setText("("
																			// +
																			// dif
																			// /
																			// 60
																			// +
																			// "h "
																			// +
																			// -(dif
																			// %
																			// 60)
																			// +
																			// " mins)");
																			// }
																			// else
																			// {
																			// if
																			// (Constants.isDebug)
																			// Log.e(TAG,
																			// "Diff <60 "
																			// +
																			// dif);
																			// textPickUpTimeCounter
																			// .setText("("
																			// +
																			// dif
																			// +
																			// " mins)");
																			// }
																			//
																			// if
																			// (Constants.isDebug)
																			// Log.e(TAG,
																			// "Time Passenger"
																			// +
																			// Util.getTimeOnly((sPassengerdateTime))
																			// +
																			// " Time Device  "
																			// +
																			// currentDateandTime
																			// +
																			// " Time Differnce "
																			// +
																			// dif);
																		}
																	});

												} catch (Exception e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											}
										}, 0, 60000);
							}
						});

						// 0 is the time in second from when this code is to be
						// executed
						// 60000 is time in millisecond after which it has to
						// repeat
					}
				}

			}

			// WheelChair handling
			String isWheelChairAcessRequired = mMainBundle
					.getString(Constants.WHEEL_CHAIR_ACCESS_REQUIRED);
			if (isWheelChairAcessRequired.equals("true"))
				ivWheelChair.setVisibility(View.VISIBLE);
			else
				ivWheelChair.setVisibility(View.GONE);

			// Hearing impairment handling
			String isHearingImpaired = mMainBundle
					.getString(Constants.HEARING_IMPAIRED);
			if (isHearingImpaired.equals("true"))
				ivHearingImpaired.setVisibility(View.VISIBLE);
			else
				ivHearingImpaired.setVisibility(View.GONE);

			// Passenger name:For walk up and CabPay Jobs
			szPassengerName = mMainBundle.getString(Constants.PASSENGER_NAME);
			if (szPassengerName.equalsIgnoreCase("Fake Passenger")) {
				tvPassengerName.setText("Walk up");
				disableChat();
			} else {
				enableChat();
				String szFirstName = szPassengerName.substring(0,
						szPassengerName.indexOf(" "));
				String szLastName = szPassengerName.substring(szPassengerName
						.indexOf(" "));
				tvPassengerName.setText(szFirstName.substring(0, 1) + "."
						+ szLastName);
			}

			if (szPassengerName.equalsIgnoreCase("fake passenger")) {
				Log.e(TAG, "Fake passenger:YES");

				// Set PickUpAddress
				if (mMainBundle.getString(Constants.PICK_UP_ADDRESS) == null
						|| mMainBundle.getString(Constants.PICK_UP_ADDRESS)
								.isEmpty())
					txtPickupAddress.setText("");
				else
					txtPickupAddress.setText(mMainBundle
							.getString(Constants.PICK_UP_ADDRESS));
				// Set PickUpPinCode
				if (mMainBundle.getString(Constants.PICK_UP_PINCODE) == null
						|| mMainBundle.getString(Constants.PICK_UP_PINCODE)
								.isEmpty()) {
					if (txtPickupAddress.getText().toString().isEmpty()) {
						txtPickUpPinCode.setText("Walk Up");
					} else
						txtPickUpPinCode.setText("");
				} else
					txtPickUpPinCode.setText(mMainBundle
							.getString(Constants.PICK_UP_PINCODE));

				// Set drop off add
				if (mMainBundle.getString(Constants.DROP_ADDRESS) == null
						|| mMainBundle.getString(Constants.DROP_ADDRESS)
								.isEmpty())
					txtDropAddress.setText("");
				else
					txtDropAddress.setText(mMainBundle
							.getString(Constants.DROP_ADDRESS));

				// Set DropOffPinCode
				if (mMainBundle.getString(Constants.DROP_OFF_PINCODE) == null
						|| mMainBundle.getString(Constants.DROP_OFF_PINCODE)
								.isEmpty()) {
					if (txtDropAddress.getText().toString().isEmpty()) {
						txtDropOffPinCode.setText("As Directed");
					} else
						txtDropOffPinCode.setText("");
					txtDistanceDrop.setText("-");

				} else
					txtDropOffPinCode.setText(mMainBundle
							.getString(Constants.DROP_OFF_PINCODE));
			} else {
				Log.e(TAG, "Fake passenger:No");
				txtPickupAddress.setText(mMainBundle
						.getString(Constants.PICK_UP_ADDRESS));
				txtPickUpPinCode.setText(mMainBundle
						.getString(Constants.PICK_UP_PINCODE));
				txtDropAddress.setText(mMainBundle
						.getString(Constants.DROP_ADDRESS));
				txtDropOffPinCode.setText(mMainBundle
						.getString(Constants.DROP_OFF_PINCODE));
			}
			// if (job.getDropLocation().getAddressLine1()
			// .equalsIgnoreCase("As Directed")) {
			// holder.txtDistanceDrop.setText("-");
			// No of passengers
			int nNoOfPassengers = Integer.parseInt(mMainBundle
					.getString(Constants.NO_OF_PASSENGERS));

			if (nNoOfPassengers <= 5
					&& Util.getCitySelectedLondon(Util.mContext))
				txtPassengerCount.setText("1-5");
			else if (nNoOfPassengers <= 4)
				txtPassengerCount.setText("1-4");
			else
				txtPassengerCount.setText(String.valueOf(nNoOfPassengers));

			// Fare:Fixed Price/ On Meter
			if (mMainBundle.containsKey(Constants.IS_FROM_HISTORY)
					&& mMainBundle.getBoolean(Constants.IS_FROM_HISTORY)) {
				if (mMainBundle.containsKey(Constants.AMOUNT)) {
					float cabMiles = 0;
					if (mMainBundle.containsKey(Constants.CAB_MILES)) {
						cabMiles = Float.valueOf(mMainBundle
								.getString(Constants.CAB_MILES));
					}
					float amt = Float.valueOf(mMainBundle
							.getString(Constants.AMOUNT)) + cabMiles;
					if (AppValues.driverSettings != null)
						txtFare.setText(AppValues.driverSettings
								.getCurrencySymbol()
								+ ""
								+ String.format(Locale.ENGLISH, "%.2f", amt));
					else
						txtFare.setText(String.format(Locale.ENGLISH, "%.2f",
								amt));
				}
			} else {
				String sFare = mMainBundle.getString(Constants.FARE);
				if (sFare == null || sFare.equals("") || sFare.isEmpty()) {
					txtFare.setText("On Meter");
				} else {
					if (AppValues.driverSettings != null
							&& AppValues.driverSettings.getCurrencySymbol() != null)
						txtFare.setText(AppValues.driverSettings
								.getCurrencySymbol() + sFare);
					else
						txtFare.setText(sFare);
				}
			}

			// Payment type
			if (mMainBundle.containsKey(Constants.JOB_PAYMENT_TYPE)
					&& mMainBundle.getString(Constants.JOB_PAYMENT_TYPE) != null
					&& mMainBundle.getString(Constants.JOB_PAYMENT_TYPE)
							.equals("cash"))
				ivPayment.setImageResource(R.drawable.cashblack);
			else
				ivPayment.setImageResource(R.drawable.paymentblack);

			// Distance pick up and drop off
			if (mMainBundle.getString(Constants.DISTANCE_PICKUP) != null
					&& mMainBundle.getString(Constants.DISTANCE_DROP) != null
					&& !mMainBundle.getString(Constants.DISTANCE_PICKUP)
							.equals("")
					&& !mMainBundle.getString(Constants.DISTANCE_DROP).equals(
							"")) {
				if (Constants.isDebug)
					Log.e(TAG, "DISTANCE_PICKUP DISTANCE_DROP not null");
				// txtDistancePickup.setText(mMainBundle
				// .getString(Constants.DISTANCE_PICKUP));
				// txtDistanceDrop.setText(mMainBundle
				// .getString(Constants.DISTANCE_DROP));
				setDistance();
			} else {
				CalculateDistanceTask calculateDistance = new CalculateDistanceTask();
				calculateDistance.jobID = mMainBundle
						.getString(Constants.JOB_ID);
				calculateDistance.pickupLat = mMainBundle
						.getString(Constants.PICK_UP_LOCATION_LAT);
				calculateDistance.pickupLng = mMainBundle
						.getString(Constants.PICK_UP_LOCATION_LNG);
				if (currentLocation != null) {
					calculateDistance.userLat = String.valueOf(currentLocation
							.getLatitude());
					calculateDistance.userLng = String.valueOf(currentLocation
							.getLongitude());
				}
				calculateDistance.dropoffLat = mMainBundle
						.getString(Constants.DROP_LOCATION_LAT);
				calculateDistance.dropoffLng = mMainBundle
						.getString(Constants.DROP_LOCATION_LNG);
				calculateDistance.txtviewDistanceDropoff = txtDistanceDrop;
				calculateDistance.txtviewDistancePickup = txtDistancePickup;
				calculateDistance.execute();

				if (Util.mContext != null) {
					txtDistanceUnit.setText(Util
							.getDistanceFormat(Util.mContext));
				}
			}

			// luggage comment
			if (mMainBundle.getString(Constants.LUGGAGE_NOTE) != null
					&& !mMainBundle.getString(Constants.LUGGAGE_NOTE)
							.equals(""))
				txtLuggage.setText("\""
						+ mMainBundle.getString(Constants.LUGGAGE_NOTE) + "\"");
			isFromMyJobs = mMainBundle.getBoolean(Constants.IS_FROM_MY_JOBS);
			// if
			// (mMainBundle.getString("dropAddress").equals("As Directed"))
			// txtDistanceDrop.setText("-");
			if (mMainBundle.getString(Constants.TIME) != null) {
				// Calendar calendarPickup = Calendar.getInstance();
				// calendarPickup.setTime(new Date(mMainBundle
				// .getString(Constants.TIME)));
				// txtTime.setText(String.format("%02d:%02d",
				// calendarPickup.get(Calendar.HOUR_OF_DAY),
				// calendarPickup.get(Calendar.MINUTE)));

				String date = Util.getTimeFormat(mMainBundle
						.getString(Constants.TIME));
				txtTime.setText(date);
			}

			if (mMainBundle.containsKey(Constants.PASSENGER_NUMBER))
				szPassengerNumber = mMainBundle
						.getString(Constants.PASSENGER_NUMBER);

			if (mMainBundle.containsKey("jobCompletedTime")) {
				String szJobCompletedTime = mMainBundle
						.getString("jobCompletedTime");
				if (szJobCompletedTime != null && !szJobCompletedTime.isEmpty()) {
					int nHoursInInbox = AppValues.driverSettings
							.getHoursInInbox();
					Date jobCompletedTime = Util
							.getDateFormatted(szJobCompletedTime);
					Calendar calendar = Calendar.getInstance();
					Date currentTime = calendar.getTime();
					calendar.setTime(jobCompletedTime);
					calendar.add(Calendar.HOUR_OF_DAY, nHoursInInbox);
					Date timeTillMessageAllowed = calendar.getTime();
					if (Constants.isDebug)
						Log.e(TAG, "currentTime::> " + currentTime
								+ "  timeTillMessageAllowed::> "
								+ timeTillMessageAllowed
								+ "   nHoursInInbox:> " + nHoursInInbox
								+ "   szJobCompletedTime::> "
								+ szJobCompletedTime);
					if (currentTime.before(timeTillMessageAllowed)) {
						enableChat();
					} else {
						disableChat();
					}
				}
			}

		}

		jobscancelledIntentFilter = new IntentFilter();
		jobscancelledIntentFilter.addAction("JobCancelled");

		LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager
				.getInstance(getActivity());
		locationBroadcastManager.registerReceiver(jobCancelledReceiver,
				jobscancelledIntentFilter);
	}

	private void isArrivedAtPickUpCkicked() {

		String response = mMainBundle.getString(Constants.JSON_GET_MESSAGE);
		Log.e(TAG, "response: " + response);
		JSONArray jsonResponse = null;
		try {
			jsonResponse = new JSONArray(response);
			int lengthJsonArr = jsonResponse.length();
			if (lengthJsonArr > 0) {
				for (int i = 0; i < lengthJsonArr; i++) {
					JSONObject jsonChildNode = jsonResponse.getJSONObject(i);
					String messageText = jsonChildNode.optString(
							Constants.MESSAGE).toString();
					if (Constants.isDebug)
						Log.i(TAG, "message::  " + messageText);
					if (messageText.equals(getResources().getString(
							R.string.arrived_at_pick_up))) {
						// szArrivedMessageTime = jsonChildNode.optString(
						// "messageTime").toString();
						rl_btnIamOutside.setVisibility(View.GONE);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void NoShowEnabled(String arrivedTime) {

		String szArrivedTime = arrivedTime;
		if (szArrivedTime != null && !szArrivedTime.isEmpty()) {
			int nMinutesOfNoShow = 0;
			if (AppValues.driverSettings != null
					&& AppValues.driverSettings.getNoShowInMinutes() != null)
				nMinutesOfNoShow = AppValues.driverSettings
						.getNoShowInMinutes();
			Date jobArrivedTime = Util.getDateFormatted(szArrivedTime);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(jobArrivedTime);
			calendar.add(Calendar.MINUTE, nMinutesOfNoShow);
			Date _dateArrivedTimeNoshow = calendar.getTime();

			calendar = Calendar.getInstance();
			Date currentTime = calendar.getTime();

			if (Constants.isDebug)
				Log.e(TAG, "szArrivedTime::> " + szArrivedTime
						+ "   _dateArrivedTimeNoshow::> "
						+ _dateArrivedTimeNoshow + "  currentTime::> "
						+ currentTime + "   nMinutesOfNoShow:> "
						+ nMinutesOfNoShow);
			if (currentTime.after(_dateArrivedTimeNoshow)) {
				if (getActivity() == null)
					return;
				if (getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							rl_btnNoShow.setVisibility(View.VISIBLE);
						}
					});
			} else {
				if (getActivity() == null)
					return;
				if (getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							rl_btnNoShow.setVisibility(View.GONE);
						}
					});

			}
		}

	}

	void NoShowDialog() {

		final AlertDialog noShowDialog = new AlertDialog.Builder(mContext)
				.setMessage("Are you sure you want to cancel the job?")
				.setPositiveButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {

						// TODO Auto-generated
					}
				})
				.setNegativeButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated
								if (!sJobID.isEmpty())
									if (NetworkUtil.isNetworkOn(Util.mContext)) {
										progressDialog = new ProgressDialog(
												mContext);
										progressDialog.setMessage("Loading...");
										progressDialog.setCancelable(false);
										progressDialog.show();

										NoShowTask noShowTask = new NoShowTask(
												mContext, progressDialog);
										noShowTask.szJobId = sJobID;
										noShowTask.execute();
									} else {
										Util.showToastMessage(
												mContext,
												getResources()
														.getString(
																R.string.no_network_error),
												Toast.LENGTH_LONG);
									}
							}
						}).create();
		noShowDialog.setCanceledOnTouchOutside(false);
		noShowDialog.show();
	}

	private BroadcastReceiver jobCancelledReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			AppValues.clearAllJobsList();
			Fragment fragment = null;
			fragment = new com.android.cabapp.fragments.JobsFragment();
			((MainActivity) Util.mContext)
					.setSlidingMenuPosition(Constants.JOBS_FRAGMENT);
			if (fragment != null)
				((MainActivity) Util.mContext).replaceFragment(fragment, false);

		}
	};

	public void ShowCountDown(ArrayList<LatLng> mPoints) {
		if (mPoints.size() >= 2) {
			LatLng origin = mPoints.get(0);
			LatLng dest = mPoints.get(1);

			// Getting URL to the Google Directions API
			if (origin != null && dest != null) {
				String url = getDirectionsUrl(origin, dest);

				DownloadTask downloadTask = new DownloadTask();

				// Start downloading json data from Google Directions API
				downloadTask.execute(url);
			}

		}
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			if (result != null) {
				ArrayList<LatLng> points = null;
				PolylineOptions lineOptions = null;
				String distance = "";
				String duration = "";

				if (result.size() < 1) {
					if (getActivity() != null)
						// Util.showToastMessage(getActivity(),
						// "No route is available currently.",
						// Toast.LENGTH_SHORT);
						return;
				}

				// Traversing through all the routes
				for (int i = 0; i < result.size(); i++) {
					points = new ArrayList<LatLng>();
					lineOptions = new PolylineOptions();

					// Fetching i-th route
					List<HashMap<String, String>> path = result.get(i);

					// Fetching all the points in i-th route
					for (int j = 0; j < path.size(); j++) {
						HashMap<String, String> point = path.get(j);

						if (j == 0) { // Get distance from the list
							distance = (String) point.get("distance");
							continue;
						} else if (j == 1) { // Get duration from the list
							duration = (String) point.get("duration");
							continue;
						}
						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);

						points.add(position);
					}

					// Adding all the points in the route to LineOptions
					lineOptions.addAll(points);
					lineOptions.width(9);
					lineOptions.color(Color.rgb(251, 111, 0));
					if (Constants.isDebug)
						Log.e(TAG, "Distance:" + distance + ", Duration:"
								+ duration);

				}
				if (rl_btnIamOutside.getVisibility() == View.VISIBLE) {
					tvCountDownTime.setText(duration);

					if (mMainBundle.containsKey("countdowntime")) {
						mMainBundle.remove("countdowntime");
					}
					mMainBundle.putString("countdowntime", duration);
				} else {
					if (mCountdownTimer != null)
						mCountdownTimer.cancel();
					tvCountDownTime.setVisibility(View.GONE);
				}
			}
		}
	}

	void enableChat() {
		tvContactHistory.setEnabled(true);
		tvContact.setEnabled(true);
	}

	void disableChat() {
		tvContactHistory.setEnabled(false);
		tvContactHistory.setTextColor(getResources().getColor(
				(R.color.textcolor_grey)));
		tvContactHistory.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.contactgray, 0, 0, 0);

		tvContact.setEnabled(false);
		tvContact.setTextColor(getResources()
				.getColor((R.color.textcolor_grey)));
		tvContact.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.contactgray, 0, 0, 0);
	}

	void disableSendReceipt() {

		tvSendReceiptHistory.setTextColor(getResources().getColor(
				(R.color.textcolor_grey)));
		tvSendReceiptHistory.setEnabled(false);
		tvSendReceiptHistory.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.send_receipt_grey, 0, 0, 0);
	}

	class TextTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				switch (v.getId()) {

				case R.id.rlContact:// tvContact

					if (!szPassengerName.equalsIgnoreCase("fake passenger")) {
						if (!ChatActivity.szJobID.isEmpty()) {
							ChatActivity.finishMe();
							ChatActivity.szJobID = "";
						}
						ChatActivity.szJobID = mMainBundle
								.getString(Constants.JOB_ID);
						Intent intent = new Intent(Util.mContext,
								ChatActivity.class);
						mMainBundle.putBoolean("getChatMessages", true);
						intent.putExtras(mMainBundle);
						startActivity(intent);
					}

					break;

				case R.id.rlPayment:// tvPayment
					if (rl_btnIamOutside.getVisibility() == View.VISIBLE) {
						AlertDialog quitAlertDialog = new AlertDialog.Builder(
								mContext)
								.setTitle("Alert")
								.setMessage(
										"Please click on Arrived at pick up in order to proceed with payment")
								.setPositiveButton("OK",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface argDialog,
													int argWhich) {
											}
										}).create();
						quitAlertDialog.setCanceledOnTouchOutside(false);
						quitAlertDialog.show();
					} else {
						if (mMainBundle != null
								&& mMainBundle.containsKey(Constants.FARE)
								& mMainBundle.getString(Constants.FARE) != null) {
							String sFixedPrice = mMainBundle
									.getString(Constants.FARE);

							mMainBundle.putString(Constants.METER_VALUE,
									sFixedPrice);
						}
						Fragment fragmentCabPay = new com.android.cabapp.fragments.CabPayFragment();
						if (fragmentCabPay != null) {
							mMainBundle.putBoolean(Constants.FROM_JOB_ACCEPTED,
									true);
							fragmentCabPay.setArguments(mMainBundle);
							((MainActivity) Util.mContext)
									.setSlidingMenuPosition(Constants.CAB_PAY_FRAGMENT);
							((MainActivity) Util.mContext).replaceFragment(
									fragmentCabPay, false);
						}
					}
					break;

				case R.id.rlMapIcon:// tvShowMap
					Fragment fragmentShowMap = new com.android.cabapp.fragments.JobsMapFragment();
					// mMainBundle.putBoolean(Constants.IS_IM_OUTSIDE_VISIBLE,
					// true);
					mMainBundle.putBoolean(Constants.FROM_SHOW_MAP_ICON, true);
					if (fragmentShowMap != null) {
						fragmentShowMap.setArguments(mMainBundle);
						// FragmentManager fragmentManager = ((FragmentActivity)
						// getActivity())
						// .getSupportFragmentManager();
						// fragmentManager.beginTransaction()
						// .replace(R.id.frame_container, fragment).commit();
						((MainActivity) Util.mContext).replaceFragment(
								fragmentShowMap, false);
					}
					break;

				case R.id.rl_btnIamOutside:

					if (NetworkUtil.isNetworkOn(Util.mContext)) {

						mMainBundle.putBoolean("isfromjobAccepted", true);
						SendMessageTask sendMessageTask = new SendMessageTask();
						sendMessageTask.jobID = mMainBundle
								.getString(Constants.JOB_ID);
						sendMessageTask.execute();
					} else {
						Util.showToastMessage(Util.mContext, getResources()
								.getString(R.string.no_network_error),
								Toast.LENGTH_LONG);
					}
					break;

				case R.id.tvContactHistory:
					if (!szPassengerName.equalsIgnoreCase("fake passenger")) {
						Intent intentContact = new Intent(Util.mContext,
								ChatActivity.class);
						mMainBundle.putBoolean("getChatMessages", true);
						intentContact.putExtras(mMainBundle);
						startActivity(intentContact);
					}
					break;

				case R.id.tvSendReceiptHistory:

					// if (szPassengerName.equalsIgnoreCase("fake passenger")) {
					//
					// } else {
					// }
					Fragment fragment = null;
					fragment = new com.android.cabapp.fragments.CabPayReceiptFragment();
					mMainBundle.putBoolean("isFromHistoryJobs", true);
					fragment.setArguments(mMainBundle);
					if (fragment != null)
						((MainActivity) Util.mContext).replaceFragment(
								fragment, false);

					break;

				case R.id.tvSkipBottomArrived:
					Util.setIsOverlaySeen(getActivity(), true, TAG);
					llBottomButtonsOverlay.setVisibility(View.GONE);
					rlOverlayActiveJobs.setVisibility(View.GONE);
					rl_btnIamOutside.setEnabled(true);
					rlMapIcon.setEnabled(true);
					rlContact.setEnabled(true);
					rlPayment.setEnabled(true);

					rl_btnNoShow.setVisibility(View.GONE);
					rl_btnNoShow.setEnabled(true);

					if (MainActivity.slidingMenu != null)
						MainActivity.slidingMenu
								.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;

				case R.id.tvNextBottomArrived:

					if (rlOverlayActiveJobs.getVisibility() == View.VISIBLE) {
						Log.e(TAG, "rlOverlayActiveJobs");
						llTopButtonsArrivedOverlay.setVisibility(View.VISIBLE);
						rlOverlayArrivedAtPickUp.setVisibility(View.VISIBLE);
						llBottomButtonsOverlay.setVisibility(View.GONE);
						rlOverlayActiveJobs.setVisibility(View.GONE);

						LayoutParams aLayoutParams1 = new LayoutParams(
								getResources().getDisplayMetrics().widthPixels,
								(int) (getResources().getDisplayMetrics().heightPixels / 1.8));

						contentArrived
								.setLayoutParams(new RelativeLayout.LayoutParams(
										aLayoutParams1));

						overlayArrivedAtPickUp.setRadius(getResources()
								.getDisplayMetrics().widthPixels);
						overlayArrivedAtPickUp
								.setYDistance(getResources()
										.getDisplayMetrics().widthPixels
										+ getResources().getDisplayMetrics().heightPixels
										/ 1.75f);
						overlayArrivedAtPickUp.setXDistance(getResources()
								.getDisplayMetrics().widthPixels / 2);

						aLayoutParams1 = new LayoutParams(getResources()
								.getDisplayMetrics().widthPixels,
								getResources().getDisplayMetrics().heightPixels);
						overlayArrivedAtPickUp
								.setLayoutParams(new RelativeLayout.LayoutParams(
										aLayoutParams1));
					}

					break;

				case R.id.tvSkipTopArrived:
					Util.setIsOverlaySeen(getActivity(), true, TAG);
					llBottomButtonsOverlay.setVisibility(View.GONE);
					if (llTopButtonsArrivedOverlay.getVisibility() == View.VISIBLE)
						llTopButtonsArrivedOverlay.setVisibility(View.GONE);
					if (rlOverlayArrivedAtPickUp.getVisibility() == View.VISIBLE)
						rlOverlayArrivedAtPickUp.setVisibility(View.GONE);
					if (rlOverlayContactPassenger.getVisibility() == View.VISIBLE)
						rlOverlayContactPassenger.setVisibility(View.GONE);
					if (rlOverlayPayment.getVisibility() == View.VISIBLE)
						rlOverlayPayment.setVisibility(View.GONE);
					rl_btnIamOutside.setEnabled(true);
					rlMapIcon.setEnabled(true);
					rlContact.setEnabled(true);
					rlPayment.setEnabled(true);

					rl_btnNoShow.setVisibility(View.GONE);
					rl_btnNoShow.setEnabled(true);

					if (MainActivity.slidingMenu != null)
						MainActivity.slidingMenu
								.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

					break;

				case R.id.tvNextTopArrived:

					if (rlOverlayArrivedAtPickUp.getVisibility() == View.VISIBLE) {
						rlOverlayArrivedAtPickUp.setVisibility(View.GONE);
						rlOverlayContactPassenger.setVisibility(View.VISIBLE);

						LayoutParams aLayoutParams = new LayoutParams(
								getResources().getDisplayMetrics().widthPixels,
								(int) (getResources().getDisplayMetrics().heightPixels / 1.5));
						contentContactPassenger
								.setLayoutParams(new RelativeLayout.LayoutParams(
										aLayoutParams));
						overlayContactPassenger.setRadius(getResources()
								.getDisplayMetrics().widthPixels / 3f);
						overlayContactPassenger
								.setYDistance(getResources()
										.getDisplayMetrics().heightPixels
										- (getResources().getDisplayMetrics().widthPixels / 7.2f));
						overlayContactPassenger.setXDistance(getResources()
								.getDisplayMetrics().widthPixels / 4.2f);

						aLayoutParams = new LayoutParams(getResources()
								.getDisplayMetrics().widthPixels,
								getResources().getDisplayMetrics().heightPixels);
						overlayContactPassenger
								.setLayoutParams(new RelativeLayout.LayoutParams(
										aLayoutParams));

					} else if (rlOverlayContactPassenger.getVisibility() == View.VISIBLE) {
						rlOverlayContactPassenger.setVisibility(View.GONE);
						rlOverlayPayment.setVisibility(View.VISIBLE);
						tvSkipTopArrived.setVisibility(View.GONE);
						tvNextTopArrived.setText("OKAY GOT IT!");

						LayoutParams aLayoutParams = new LayoutParams(
								getResources().getDisplayMetrics().widthPixels,
								(int) (getResources().getDisplayMetrics().heightPixels / 1.5));
						contentPayment
								.setLayoutParams(new RelativeLayout.LayoutParams(
										aLayoutParams));

						overlayPayment.setRadius(getResources()
								.getDisplayMetrics().widthPixels / 3f);
						overlayPayment
								.setYDistance(getResources()
										.getDisplayMetrics().heightPixels
										- (getResources().getDisplayMetrics().widthPixels / 7.2f));
						overlayPayment
								.setXDistance(getResources()
										.getDisplayMetrics().widthPixels
										- getResources().getDisplayMetrics().widthPixels
										/ 4.2f);

						aLayoutParams = new LayoutParams(getResources()
								.getDisplayMetrics().widthPixels,
								getResources().getDisplayMetrics().heightPixels);
						overlayPayment
								.setLayoutParams(new RelativeLayout.LayoutParams(
										aLayoutParams));

					} else if (rlOverlayPayment.getVisibility() == View.VISIBLE) {
						Util.setIsOverlaySeen(getActivity(), true, TAG);
						llTopButtonsArrivedOverlay.setVisibility(View.GONE);
						llBottomButtonsOverlay.setVisibility(View.GONE);
						rlOverlayPayment.setVisibility(View.GONE);
						rl_btnIamOutside.setEnabled(true);
						rlMapIcon.setEnabled(true);
						rlContact.setEnabled(true);
						rlPayment.setEnabled(true);

						rl_btnNoShow.setVisibility(View.GONE);
						rl_btnNoShow.setEnabled(true);

						if (MainActivity.slidingMenu != null)
							MainActivity.slidingMenu
									.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					}

					break;

				case R.id.rl_btnNoShow:
					NoShowDialog();
					break;

				}
				return true;
			} else
				return false;
		}
	}

	class CalculateDistanceTask extends AsyncTask<String, Void, String> {
		String pickupLat, pickupLng, dropoffLat, dropoffLng, userLat, userLng;
		// ViewHolder holder;
		TextView txtviewDistanceDropoff, txtviewDistancePickup;
		String szPickupToDropOffDistance = "", szUserToPickupDistance = "";
		String jobID;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url;
			// http://maps.googleapis.com/maps/api/distancematrix/json?origins=54.406505,18.67708&destinations=54.446251,18.570993&mode=driving&language=en-EN&sensor=false
			url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&language=en-EN&sensor=false";
			url = String.format(url, pickupLat, pickupLng, dropoffLat,
					dropoffLng);
			Log.e("NowJobsExpandableAdapter", "mapsURLPickupToDropOff:: " + url);

			Connection connection = new Connection(Util.mContext);
			connection.mapURL = url;
			connection.connect(Constants.MAP_DISTANCE);
			try {
				szPickupToDropOffDistance = Util.parseMapDistanceData(
						connection.inputStreamToString(connection.mInputStream)
								.toString(), Util.mContext);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&language=en-EN&sensor=false";
			url = String.format(url, userLat, userLng, pickupLat, pickupLng);
			Log.e("NowJobsExpandableAdapter", "mapsURLUserToPickup:: " + url);

			connection = new Connection(Util.mContext);
			connection.mapURL = url;
			connection.connect(Constants.MAP_DISTANCE);

			try {
				szUserToPickupDistance = Util.parseMapDistanceData(connection
						.inputStreamToString(connection.mInputStream)
						.toString(), Util.mContext);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (Util.mContext != null) {
				Log.e("onPostExecute",
						"Distance JobAccepted "
								+ Util.getDistanceFormat(Util.mContext));
				String szDropOffPinCode = txtDropOffPinCode.getText()
						.toString();

				String szDropAddress = txtDropAddress.getText().toString();

				if (szDropOffPinCode.equalsIgnoreCase("As Directed")
						|| szDropAddress.equalsIgnoreCase("As Directed"))
					txtviewDistanceDropoff.setText("-");
				else {
					if (!szPickupToDropOffDistance.equals("-")
							&& !szPickupToDropOffDistance.equals(""))
						txtviewDistanceDropoff.setText(Util.getDistance(
								Util.mContext,
								Double.parseDouble(szPickupToDropOffDistance),
								Util.getDistanceFormat(Util.mContext)));
					// txtviewDistanceDropoff.setText("0");
				}

				if (!szUserToPickupDistance.equals("-")
						&& !szUserToPickupDistance.equals(""))
					txtviewDistancePickup.setText(Util.getDistance(
							Util.mContext,
							Double.parseDouble(szUserToPickupDistance),
							Util.getDistanceFormat(Util.mContext)));
				else
					txtviewDistancePickup.setText("0");

				mMainBundle.putString(Constants.DISTANCE_PICKUP,
						szUserToPickupDistance);
				mMainBundle.putString(Constants.DISTANCE_DROP,
						szPickupToDropOffDistance);
			}

			Distance distance = new Distance();
			distance.szDistanceDropoff = txtviewDistanceDropoff.getText()
					.toString();
			distance.szDistancePickup = txtviewDistancePickup.getText()
					.toString();
			mapDistance.put(jobID, distance);
		}
	}

	class Distance {
		String szDistancePickup;
		String szDistanceDropoff;
	}

	public class SendMessageTask extends AsyncTask<String, Void, String> {
		String jobID;
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(Util.mContext);
			pDialog.setMessage("Sending message...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (Constants.isDebug)
				Log.d("SendMessageTask", "jobID:: " + jobID);
			SendMessage message = new SendMessage(jobID, Util.mContext,
					getResources().getString(R.string.arrived_at_pick_up),
					false);
			String response = message.sendMessages();
			try {
				JSONObject jObject = new JSONObject(response);
				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {
					FlurryAgent.logEvent("Driver arrived at pick up");
					DriverSettingDetails driverSettings = new DriverSettingDetails(
							Util.mContext);
					driverSettings.retriveDriverSettings(Util.mContext);
					return "success";
				}
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

			textPickUpTimeCounter.setVisibility(View.GONE);
			tvCountDownTime.setVisibility(View.GONE);

			if (pDialog != null)
				pDialog.dismiss();

			if (result != null && result.equals("success")) {

				AppValues.mapArrivedAtPickUpMessages.put(jobID, "true");
				// if (result != null && result.equals("success")) {
				Fragment fragment = new com.android.cabapp.fragments.JobsMapFragment();
				if (fragment != null) {
					// FragmentManager fragmentManager = ((FragmentActivity)
					// getActivity())
					// .getSupportFragmentManager();
					// fragmentManager.beginTransaction()
					// .replace(R.id.frame_container, fragment).commit();
					// mMainBundle.putBoolean(Constants.IS_IM_OUTSIDE_VISIBLE,
					// false);
					// mMainBundle.putBoolean(Constants.FROM_SHOW_MAP_ICON,
					// false);

					fragment.setArguments(mMainBundle);

					if (Util.mContext != null)
						((MainActivity) Util.mContext).replaceFragment(
								fragment, true);
				}
			} else {
				Util.showToastMessage(Util.mContext,
						"Error in network! Please try again.",
						Toast.LENGTH_LONG);
			}
		}

	}

	void setDistance() {
		try {

			txtDistanceUnit.setText(Util.getDistanceFormat(Util.mContext));

			if (!mMainBundle.getString(Constants.DISTANCE_DROP).equals("-")
					&& !mMainBundle.getString(Constants.DISTANCE_DROP).equals(
							"")) {
				txtDistanceDrop.setText(Util.getDistance(Util.mContext, Double
						.parseDouble(mMainBundle
								.getString(Constants.DISTANCE_DROP)), Util
						.getDistanceFormat(Util.mContext)));
				if (mMainBundle != null
						&& mMainBundle.getString(Constants.DROP_OFF_PINCODE) == null
						|| mMainBundle.getString(Constants.DROP_OFF_PINCODE)
								.isEmpty())
					txtDistanceDrop.setText("-");
			}

			if (!mMainBundle.getString(Constants.DISTANCE_PICKUP).equals("-")
					&& !mMainBundle.getString(Constants.DISTANCE_PICKUP)
							.equals("")) {
				txtDistancePickup.setText(Util.getDistance(Util.mContext,
						Double.parseDouble(mMainBundle
								.getString(Constants.DISTANCE_PICKUP)), Util
								.getDistanceFormat(Util.mContext)));
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mMinutesPassengerWaiting != null)
			try {
				mMinutesPassengerWaiting.cancel();
			} catch (Exception e) {

			}

		if (mCountdownTimer != null)
			try {
				mCountdownTimer.cancel();
			} catch (Exception e) {

			}

		if (mNoShowTimer != null)
			try {
				mNoShowTimer.cancel();
			} catch (Exception e) {

			}

		LocalBroadcastManager locationBroadcastManager = LocalBroadcastManager
				.getInstance(getActivity());
		locationBroadcastManager.unregisterReceiver(jobCancelledReceiver);
	}

}
