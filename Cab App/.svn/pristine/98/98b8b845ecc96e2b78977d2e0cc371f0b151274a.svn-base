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
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.ChatActivity;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.async.DistanceTask;
import com.android.cabapp.async.NoShowTask;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.model.SendMessage;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.DirectionsJSONParser;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.android.cabapp.view.OverlayCircle;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class JobsMapFragment extends RootFragment {

	private static final String TAG = JobsMapFragment.class.getSimpleName();

	private SupportMapFragment mMapFragment;
	private ViewGroup mMapFrame;
	private GoogleMap mMap;
	ArrayList<LatLng> points = null;
	RelativeLayout rl_btnIamOutside, rl_btnNoShow, rlDetailsIcon, rlContact, rlPayment;
	Location currentLocation = null;
	boolean isShowMapIcon = false;
	boolean isCountDownTimerVisible = false;
	TextView tvPassengerName, tvCountdownTime, tvPickUpDistance, tvDropDistance, tvDO;
	LatLng pickUpPosition, dropPosition;
	Marker driverMarker;
	LinearLayout llPickup, llDropup;
	boolean bIsFromJobAccepted = false, bIsImOutsideClicked = false;

	Context mContext;
	String szPassengerName = "";

	Timer mCountdownTimer, updateLocationMarkersTimer, mNoShowTimer;

	String szArrivedMessageTime, sJobID;
	ProgressDialog progressDialog;

	OverlayCircle overlayMapToPickUp;
	RelativeLayout rlOverlays, rlOverlayMapToPickUp;
	TextView tvOkGotItMap;
	LinearLayout llTopButtonsOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// try {
		MapsInitializer.initialize(getActivity().getApplicationContext());
		// } catch (GooglePlayServicesNotAvailableException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_jobs_map, container, false);

		// Overlay:ActiveJobs
		rlOverlays = (RelativeLayout) view.findViewById(R.id.rlOverlays);
		llTopButtonsOverlay = (LinearLayout) view.findViewById(R.id.llTopButtonsOverlay);
		rlOverlayMapToPickUp = (RelativeLayout) view.findViewById(R.id.rlOverlayMapAtPickUp);
		tvOkGotItMap = (TextView) view.findViewById(R.id.tvOkGotItMap);
		overlayMapToPickUp = (OverlayCircle) view.findViewById(R.id.overlayMapAtPickUp);

		rl_btnIamOutside = (RelativeLayout) view.findViewById(R.id.rl_btnIamOutside);
		rl_btnNoShow = (RelativeLayout) view.findViewById(R.id.rl_btnNoShow);
		mMapFrame = (ViewGroup) view.findViewById(R.id.map_frame);
		tvPassengerName = (TextView) view.findViewById(R.id.tvPassengerName);
		tvCountdownTime = (TextView) view.findViewById(R.id.tvCountdownTime);
		tvPickUpDistance = (TextView) view.findViewById(R.id.textviewPUdis);
		tvDropDistance = (TextView) view.findViewById(R.id.textviewDOdis);
		tvDO = (TextView) view.findViewById(R.id.textviewDO);

		rlContact = (RelativeLayout) view.findViewById(R.id.rlContact);
		rlPayment = (RelativeLayout) view.findViewById(R.id.rlPayment);
		rlDetailsIcon = (RelativeLayout) view.findViewById(R.id.rlDetailsIcon);
		llPickup = (LinearLayout) view.findViewById(R.id.llPickup);
		llDropup = (LinearLayout) view.findViewById(R.id.llDropup);

		rlContact.setOnClickListener(new TextViewOnClickListener());
		rlPayment.setOnClickListener(new TextViewOnClickListener());
		rlDetailsIcon.setOnClickListener(new TextViewOnClickListener());
		tvOkGotItMap.setOnClickListener(new TextViewOnClickListener());
		rl_btnNoShow.setOnClickListener(new TextViewOnClickListener());

		rl_btnIamOutside.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMainBundle.putBoolean("isfromjobAccepted", true);
				llPickup.setVisibility(View.GONE);
				if (NetworkUtil.isNetworkOn(Util.mContext)) {
					// imOutsideClicked = true;
					SendMessageTask sendMessageTask = new SendMessageTask();
					sendMessageTask.jobID = mMainBundle.getString(Constants.JOB_ID);
					sendMessageTask.execute();
				} else {
					Util.showToastMessage(mContext, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
				}
			}
		});

		mMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.map_frame, mMapFragment);
		fragmentTransaction.commit();

		if (!Util.getIsOverlaySeen(getActivity(), TAG)) {
			llTopButtonsOverlay.setVisibility(View.VISIBLE);
			rlOverlays.setVisibility(View.VISIBLE);
			rlOverlayMapToPickUp.setVisibility(View.VISIBLE);
			overlayMapToPickUp.setRadius(getResources().getDisplayMetrics().widthPixels / 4);
			overlayMapToPickUp.setYDistance((getResources().getDisplayMetrics().widthPixels / 4) * 2.2f);
			overlayMapToPickUp.setXDistance(getResources().getDisplayMetrics().widthPixels / 2);
			overlayMapToPickUp.setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels,
					(int) (.8 * getResources().getDisplayMetrics().widthPixels)));
			mMapFrame.setEnabled(false);
			rlContact.setEnabled(false);
			rlPayment.setEnabled(false);
			rl_btnIamOutside.setEnabled(false);
			rlDetailsIcon.setEnabled(false);

			if (MainActivity.slidingMenu != null)
				MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		super.initWidgets(view, this.getClass().getName());
		return view;
	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.rlContact:
				if (!szPassengerName.equalsIgnoreCase("fake passenger")) {
					if (!ChatActivity.szJobID.isEmpty()) {
						ChatActivity.finishMe();
						ChatActivity.szJobID = "";
					}
					ChatActivity.szJobID = mMainBundle.getString(Constants.JOB_ID);
					Intent intent = new Intent(getActivity().getBaseContext(), ChatActivity.class);
					mMainBundle.putBoolean("getChatMessages", true);
					intent.putExtras(mMainBundle);
					startActivity(intent);
				}
				break;

			case R.id.rlPayment:
				if (rl_btnIamOutside.getVisibility() == View.VISIBLE) {
					AlertDialog quitAlertDialog = new AlertDialog.Builder(mContext).setTitle("Alert")
							.setMessage("Please click on Arrived at pick up in order to proceed with payment")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog, int argWhich) {
								}
							}).create();
					quitAlertDialog.setCanceledOnTouchOutside(false);
					quitAlertDialog.show();
				} else {
					Fragment fragmentCabPay = new com.android.cabapp.fragments.CabPayFragment();
					if (fragmentCabPay != null) {
						mMainBundle.putBoolean(Constants.FROM_JOB_ACCEPTED, true);
						fragmentCabPay.setArguments(mMainBundle);
						((MainActivity) mContext).setSlidingMenuPosition(Constants.CAB_PAY_FRAGMENT);
						((MainActivity) mContext).replaceFragment(fragmentCabPay, false);
					}
				}
				break;

			case R.id.rlDetailsIcon:// tvDetails
				Fragment fragmentJobAccepted = new com.android.cabapp.fragments.JobAcceptedFragment();
				if (fragmentJobAccepted != null) {
					fragmentJobAccepted.setArguments(mMainBundle);
					((MainActivity) mContext).replaceFragment(fragmentJobAccepted, false);
				}

				break;

			case R.id.rl_btnNoShow:
				NoShowDialog();
				break;

			case R.id.tvOkGotItMap:
				Util.setIsOverlaySeen(getActivity(), true, TAG);
				if (rlOverlayMapToPickUp.getVisibility() == View.VISIBLE) {
					rlOverlayMapToPickUp.setVisibility(View.GONE);
				}
				llTopButtonsOverlay.setVisibility(View.GONE);
				rlOverlays.setVisibility(View.GONE);
				rl_btnIamOutside.setEnabled(true);
				rlDetailsIcon.setEnabled(true);
				mMapFrame.setEnabled(true);
				rlContact.setEnabled(true);
				rlPayment.setEnabled(true);
				if (mMap != null) {
					mMap.setInfoWindowAdapter(new CustomInfoWindow(LayoutInflater.from(getActivity())));
					mMap.getUiSettings().setZoomControlsEnabled(true);
					mMap.getUiSettings().setScrollGesturesEnabled(true);
					mMap.getUiSettings().setAllGesturesEnabled(true);
					mMap.getUiSettings().setZoomGesturesEnabled(true);
				}

				if (MainActivity.slidingMenu != null)
					MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				break;
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		((MainActivity) getActivity()).setDontShowQuitDialog();

		mMainBundle = this.getArguments();
		Log.e(TAG, "mMainBundle: " + mMainBundle.toString());

		points = new ArrayList<LatLng>();

		if (mMainBundle != null) {

			if (Constants.isDebug)
				Log.e(TAG, "onResume-Bundle:: " + mMainBundle.toString());

			if (mMainBundle.containsKey("isfromjobAccepted")) {
				bIsFromJobAccepted = mMainBundle.getBoolean("isfromjobAccepted");
			}
			// Set bydefault countdown time coming from JobAccepted:
			if (rl_btnIamOutside.getVisibility() == View.VISIBLE) {
				if (mMainBundle.containsKey("countdowntime") && mMainBundle.getString("countdowntime") != null)
					tvCountdownTime.setText(mMainBundle.getString("countdowntime"));
			}

			// Is coming from map button if JobAccepted fragment
			isShowMapIcon = mMainBundle.getBoolean(Constants.FROM_SHOW_MAP_ICON);

			// To Handle "Arrived at Pick Up" if Visible
			if (AppValues.mapArrivedAtPickUpMessages != null
					&& AppValues.mapArrivedAtPickUpMessages.containsKey(mMainBundle.get(Constants.JOB_ID))) {

				String szIsArrived = AppValues.mapArrivedAtPickUpMessages.get(mMainBundle.get(Constants.JOB_ID));
				if (szIsArrived.equals("true"))
					rl_btnIamOutside.setVisibility(View.GONE);
				bIsImOutsideClicked = true;
				llDropup.setVisibility(View.GONE);
				if (mMainBundle.getString(Constants.DROP_ADDRESS).equalsIgnoreCase("As Directed"))
					llPickup.setVisibility(View.GONE);
			} else if (mMainBundle.containsKey(Constants.JSON_GET_MESSAGE)) {
				String response = mMainBundle.getString(Constants.JSON_GET_MESSAGE);

				JSONArray jsonResponse = null;
				try {
					jsonResponse = new JSONArray(response);
					int lengthJsonArr = jsonResponse.length();
					if (lengthJsonArr > 0) {
						for (int i = 0; i < lengthJsonArr; i++) {
							JSONObject jsonChildNode = jsonResponse.getJSONObject(i);
							String messageText = jsonChildNode.optString(Constants.MESSAGE).toString();
							if (Constants.isDebug)
								Log.i(TAG, "message::  " + messageText);
							if (messageText.equals(getResources().getString(R.string.arrived_at_pick_up))) {
								rl_btnIamOutside.setVisibility(View.GONE);
								bIsImOutsideClicked = true;
								llDropup.setVisibility(View.GONE);
								if (mMainBundle.getString(Constants.DROP_ADDRESS).equalsIgnoreCase("As Directed"))
									llPickup.setVisibility(View.GONE);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// NoShow
			CheckNoShow();

			setDistance();

			szPassengerName = mMainBundle.getString(Constants.PASSENGER_NAME);
			String szFirstName = szPassengerName.substring(0, szPassengerName.indexOf(" "));
			String szLastName = szPassengerName.substring(szPassengerName.indexOf(" "));
			tvPassengerName.setText(szFirstName.substring(0, 1) + "." + szLastName);

			if (mMainBundle.getString(Constants.DROP_ADDRESS).equalsIgnoreCase("As Directed"))
				llDropup.setVisibility(View.GONE);
			// tvDO.setVisibility(View.GONE);

			mMap = mMapFragment.getMap();
			if (mMap != null) {
				mMap.setInfoWindowAdapter(new CustomInfoWindow(LayoutInflater.from(getActivity())));
				mMap.setMyLocationEnabled(true);
				mMap.getUiSettings().setZoomControlsEnabled(false);
			}

			if (Util.getLocation(getActivity()) != null)
				currentLocation = Util.getLocation(getActivity());
			// else if (currentLocation == null)
			// currentLocation = Util.getLocationByProvider(getActivity(),
			// LocationManager.GPS_PROVIDER);
			// else if (currentLocation == null)
			// currentLocation = Util.getLocationByProvider(getActivity(),
			// LocationManager.NETWORK_PROVIDER);

			// currentLocation = AppValues.currentLocation;

			pickUpPosition = new LatLng(Double.valueOf(mMainBundle.getString(Constants.PICK_UP_LOCATION_LAT)),
					Double.valueOf(mMainBundle.getString(Constants.PICK_UP_LOCATION_LNG)));
			dropPosition = new LatLng(Double.valueOf(mMainBundle.getString(Constants.DROP_LOCATION_LAT)),
					Double.valueOf(mMainBundle.getString(Constants.DROP_LOCATION_LNG)));
			try {
				Log.e(TAG, "mMainBundle.getString(Constants.DROP_ADDRESS): " + mMainBundle.getString(Constants.DROP_ADDRESS));
				if (bIsFromJobAccepted && bIsImOutsideClicked) {
					// pick up to drop off
					Log.e(TAG, "map zoom:1");
					showPickUpToDropOffMarkers();

				} else if (isShowMapIcon && bIsImOutsideClicked) {
					// pick up to drop off
					Log.e(TAG, "map zoom:2");
					showPickUpToDropOffMarkers();

				} else if (bIsFromJobAccepted && !bIsImOutsideClicked) {
					// current to pick up
					Log.e(TAG, "map zoom:3");
					showCurrentToPickUpMarkers();

				} else if (isShowMapIcon && !bIsImOutsideClicked) {
					// current to pick up
					Log.e(TAG, "map zoom:4");
					showCurrentToPickUpMarkers();

				} else {
					// pick up to drop off
					Log.e(TAG, "map zoom:5");
					showPickUpToDropOffMarkers();
				}
			} catch (Exception e) {

			}
		}

		updateLocationMarkersTimer = new Timer();
		updateLocationMarkersTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				LatLng currentPosition = null;

				if (currentLocation != null) {
					currentPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
				}

				// update google map route
				// if (rl_btnIamOutside.getVisibility() == View.VISIBLE) {
				// // current location to pick up
				//
				// if (mMap != null) {
				// mMap.clear();
				// points.clear();
				// }
				// points.add(currentPosition);
				// if (pickUpPosition != null) {
				// points.add(pickUpPosition);
				// makeRoute(points);
				// }
				// } else {
				// current location to drop off
				// if (!mMainBundle.getString(Constants.DROP_ADDRESS)
				// .equalsIgnoreCase("As Directed")) {
				// mMap.clear();
				// points.clear();
				// points.add(currentPosition);
				// if (dropPosition != null) {
				// points.add(dropPosition);
				// makeRoute(points);
				// }
				// }
				// }

				DistanceTask calculateDistance = new DistanceTask();

				if (!bIsImOutsideClicked) {
					if (currentLocation != null) {
						calculateDistance.userLat = String.valueOf(currentLocation.getLatitude());
						calculateDistance.userLng = String.valueOf(currentLocation.getLongitude());
					}

					calculateDistance.pickupLat = mMainBundle.getString(Constants.PICK_UP_LOCATION_LAT);
					calculateDistance.pickupLng = mMainBundle.getString(Constants.PICK_UP_LOCATION_LNG);

					calculateDistance.dropoffLat = mMainBundle.getString(Constants.DROP_LOCATION_LAT);
					calculateDistance.dropoffLng = mMainBundle.getString(Constants.DROP_LOCATION_LNG);

					calculateDistance.txtviewDistancePickup = tvPickUpDistance;
					calculateDistance.txtviewDistancePickup = tvDropDistance;
				} else {
					if (currentLocation != null) {
						calculateDistance.userLat = String.valueOf(currentLocation.getLatitude());
						calculateDistance.userLng = String.valueOf(currentLocation.getLongitude());
					}
					calculateDistance.dropoffLat = mMainBundle.getString(Constants.DROP_LOCATION_LAT);
					calculateDistance.dropoffLng = mMainBundle.getString(Constants.DROP_LOCATION_LNG);

					calculateDistance.txtviewDistancePickup = tvDropDistance;
				}
				calculateDistance.execute();
			}
		}, 0, 10000);

		// if (updateLocationMarkersTimer != null)
		// updateLocationMarkersTimer.cancel();

		if (MainActivity.slidingMenu != null)
			MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

	}

	private void CheckNoShow() {
		if (rl_btnIamOutside.getVisibility() == View.GONE) {
			sJobID = mMainBundle.getString(Constants.JOB_ID);
			szArrivedMessageTime = mMainBundle.getString(Constants.TIME);
			Log.e(TAG, "szArrivedMessageTime    " + szArrivedMessageTime);
			if (szArrivedMessageTime != null && !szArrivedMessageTime.isEmpty()) {
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
	}

	private void NoShowEnabled(String arrivedTime) {

		String szArrivedTime = arrivedTime;
		if (szArrivedTime != null && !szArrivedTime.isEmpty()) {
			int nMinutesOfNoShow = AppValues.driverSettings.getNoShowInMinutes();
			Date jobArrivedTime = Util.getDateFormatted(szArrivedTime);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(jobArrivedTime);
			calendar.add(Calendar.MINUTE, nMinutesOfNoShow);
			Date _dateArrivedTimeNoshow = calendar.getTime();

			calendar = Calendar.getInstance();
			Date currentTime = calendar.getTime();

			if (Constants.isDebug)
				Log.e(TAG, "szArrivedTime::> " + szArrivedTime + "   _dateArrivedTimeNoshow::> " + _dateArrivedTimeNoshow
						+ "  currentTime::> " + currentTime + "   nMinutesOfNoShow:> " + nMinutesOfNoShow);
			if (currentTime.after(_dateArrivedTimeNoshow)) {
				if (getActivity() == null)
					return;
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

		final AlertDialog noShowDialog = new AlertDialog.Builder(mContext).setMessage("Are you sure you want to cancel the job?")
				.setPositiveButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {

						// TODO Auto-generated
					}
				}).setNegativeButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated
						if (!sJobID.isEmpty() && Util.mContext != null)
							if (NetworkUtil.isNetworkOn(Util.mContext)) {
								progressDialog = new ProgressDialog(Util.mContext);
								progressDialog.setMessage("Loading...");
								progressDialog.setCancelable(false);
								progressDialog.show();

								NoShowTask noShowTask = new NoShowTask(mContext, progressDialog);
								noShowTask.szJobId = sJobID;
								noShowTask.execute();
							} else {
								Util.showToastMessage(Util.mContext, getResources().getString(R.string.no_network_error),
										Toast.LENGTH_LONG);
							}
					}
				}).create();
		noShowDialog.setCanceledOnTouchOutside(false);
		noShowDialog.show();
	}

	void showPickUpToDropOffMarkers() {
		mMap.clear();
		points.clear();
		points.add(pickUpPosition);
		points.add(dropPosition);
		isCountDownTimerVisible = false;
		tvCountdownTime.setVisibility(View.GONE);
		// Pick up marker

		// if not as directed it will show pickup (marker as cabaround) and drop
		// of else only current location will be displayed
		if (!mMainBundle.getString(Constants.DROP_ADDRESS).equalsIgnoreCase("As Directed")) {

			/*
			 * AddMarker(Double.valueOf(mMainBundle
			 * .getString(Constants.PICK_UP_LOCATION_LAT)),
			 * Double.valueOf(mMainBundle
			 * .getString(Constants.PICK_UP_LOCATION_LNG)),
			 * R.drawable.cabsarround);// passenger
			 * 
			 * zoomMarker(Double.valueOf(mMainBundle
			 * .getString(Constants.PICK_UP_LOCATION_LAT)),
			 * Double.valueOf(mMainBundle
			 * .getString(Constants.PICK_UP_LOCATION_LNG)), pickUpPosition,
			 * dropPosition);
			 */
			// pick up will be current location ,
			// and marker will be shown on that

			// Drop marker
			AddMarker(Double.valueOf(mMainBundle.getString(Constants.DROP_LOCATION_LAT)),
					Double.valueOf(mMainBundle.getString(Constants.DROP_LOCATION_LNG)), R.drawable.dropoff);

			if (!mMainBundle.getString(Constants.DROP_ADDRESS).equalsIgnoreCase("As Directed"))
				makeRoute(points);

			/*
			 * //zoom is happening on current location
			 * zoomMarker(Double.valueOf(mMainBundle
			 * .getString(Constants.DROP_LOCATION_LAT)),
			 * Double.valueOf(mMainBundle
			 * .getString(Constants.DROP_LOCATION_LNG)), pickUpPosition,
			 * dropPosition);
			 */
		}
		// else {
		//
		// // double lat = Double.valueOf(mMainBundle
		// // .getString(Constants.PICK_UP_LOCATION_LAT));
		// // double lng = Double.valueOf(mMainBundle
		// // .getString(Constants.PICK_UP_LOCATION_LNG));
		// zoomMarker(Double.valueOf(mMainBundle
		// .getString(Constants.PICK_UP_LOCATION_LAT)),
		// Double.valueOf(mMainBundle
		// .getString(Constants.PICK_UP_LOCATION_LNG)),
		// pickUpPosition, pickUpPosition);
		// // mMap.animateCamera(CameraUpdateFactory.zoomIn());
		// // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
		// // new LatLng(lat, lng), 12.0f);
		// // mMap.animateCamera(cameraUpdate);
		// }

	}

	void showCurrentToPickUpMarkers() {

		LatLng currentLoc = null;
		if (currentLocation != null) {
			currentLoc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		}

		mMap.clear();
		points.clear();
		points.add(currentLoc);
		points.add(pickUpPosition);

		isCountDownTimerVisible = true;
		tvCountdownTime.setVisibility(View.VISIBLE);
		// Current Location marker
		/*
		 * //Already current location marker is visible onLocationchanged
		 * driverMarker = AddMarker(currentLoc.latitude, currentLoc.longitude,
		 * R.drawable.cabsarround);
		 */
		// Pick up marker
		AddMarker(Double.valueOf(mMainBundle.getString(Constants.PICK_UP_LOCATION_LAT)),
				Double.valueOf(mMainBundle.getString(Constants.PICK_UP_LOCATION_LNG)), R.drawable.passenger);
		/*
		 * //Already zoom is happening on current location
		 * zoomMarker(Double.valueOf(mMainBundle
		 * .getString(Constants.PICK_UP_LOCATION_LAT)),
		 * Double.valueOf(mMainBundle
		 * .getString(Constants.PICK_UP_LOCATION_LNG)), currentLoc,
		 * pickUpPosition);
		 */

		// Countdowntimer
		if (mMainBundle.containsKey("isPreBook") && !mMainBundle.getBoolean("isPreBook")) {
			makeRoute(points);

			/* not required to update route:check with sagar/sangam */
			if ((rl_btnIamOutside.getVisibility() == View.VISIBLE)) {
				mCountdownTimer = new Timer();
				mCountdownTimer.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (Constants.isDebug)
							Log.e(TAG, "mCountdownTimer");
						if (Util.mContext != null)
							((Activity) Util.mContext).runOnUiThread(new Runnable() {
								public void run() {
									makeRoute(points);
								}
							});
					}
				}, 0, 30000);
			}
		}
	}

	// void zoomMarker(double lat, double lng, LatLng Latlng1, LatLng Latlng2) {
	//
	// Log.e(TAG, "map zoom values:lat " + lat + "   lng: " + lng
	// + " Latlng1  " + Latlng1 + "  Latlng2  " + Latlng2);
	// LatLngBounds.Builder builder = new LatLngBounds.Builder();
	// builder.include(Latlng1);
	// builder.include(Latlng2);
	// LatLngBounds bounds = builder.build();
	// CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
	// new LatLng(lat, lng), 14.0f);
	// mMap.animateCamera(cameraUpdate);
	// mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
	// mMap.animateCamera(CameraUpdateFactory.zoomIn());
	//
	// }

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (MainActivity.slidingMenu != null)
			MainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

	}

	private class CustomInfoWindow implements InfoWindowAdapter {

		private LayoutInflater mInflater;

		public CustomInfoWindow(LayoutInflater inflater) {
			mInflater = inflater;
		}

		@Override
		public View getInfoContents(Marker marker) {
			// View popup = mInflater.inflate(R.layout.map_info_popup, null);
			// TextView title = (TextView) popup.findViewById(R.id.title);
			// TextView snippet = (TextView) popup.findViewById(R.id.snippet);
			// title.setText(marker.getTitle());
			// snippet.setText(marker.getSnippet());
			// FontUtils.setCustomFont(getActivity(), popup);
			// return popup;
			return null;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			return null;
		}
	}

	Marker AddMarker(Double lat, Double lng, int icon) {

		if (lat != null && lng != null) {

			MarkerOptions markerOptions = new MarkerOptions();

			markerOptions.position(new LatLng(lat, lng));
			markerOptions.icon(BitmapDescriptorFactory.fromResource(icon));

			return mMap.addMarker(markerOptions);

		}
		return null;
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

		return url;
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

			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

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

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

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
				MarkerOptions markerOptions = new MarkerOptions();
				String distance = "";
				String duration = "";

				if (Constants.isDebug)
					Log.e(TAG, "Routes-size:  " + result.size());

				if (result.size() < 1) {
					if (getActivity() != null)
						Util.showToastMessage(getActivity(), "No route is available currently.", Toast.LENGTH_SHORT);
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
					Log.e(TAG, "Distance:" + distance + ", Duration:" + duration);

				}
				if (isCountDownTimerVisible) {
					if (mMainBundle.containsKey("countdowntime")) {
						mMainBundle.remove("countdowntime");
					}
					mMainBundle.putString("countdowntime", duration);
					tvCountdownTime.setText(duration);
					// If format is 00:00:00
					// duration = duration.replaceAll("\\D+", "");
					// int hours = (int) (Integer.valueOf(duration) / 60);
					// int minutes = (int) (Integer.valueOf(duration) % 60);
					// tvCountdownTime.setText("" + String.format("%02d", hours)
					// + ":" + String.format("%02d", minutes) + ":"
					// + String.format("%02d", 00));
				} else {
					if (mCountdownTimer != null)
						mCountdownTimer.cancel();
					tvCountdownTime.setVisibility(View.GONE);
				}

				// Drawing polyline in the Google Map for the i-th route
				mMap.addPolyline(lineOptions);
			}
		}
	}

	void makeRoute(ArrayList<LatLng> mPoints) {
		if (mPoints.size() >= 2) {
			LatLng origin = mPoints.get(0);
			LatLng dest = mPoints.get(1);

			if (Constants.isDebug)
				Log.d("JobsMapFragment", "makeRoute:: origin " + origin + " destination:: " + dest);

			// Getting URL to the Google Directions API
			if (origin != null && dest != null) {
				String url = getDirectionsUrl(origin, dest);

				if (Constants.isDebug)
					Log.e(TAG, "url: " + url.toString());

				DownloadTask downloadTask = new DownloadTask();

				// Start downloading json data from Google Directions API
				downloadTask.execute(url);
				// mMap.animateCamera(CameraUpdateFactory.zoomIn());
				// CameraUpdate cameraUpdate = CameraUpdateFactory
				// .newLatLngZoom(new LatLng(origin.latitude,
				// origin.longitude), 14.0f);
				// mMap.animateCamera(cameraUpdate);
			}

		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		if (Constants.isDebug)
			Log.e("JobsMapFragment", "onLocationChanged:: " + location);

		// if (!bIsFromJobAccepted)
		if (currentLocation != null) {
			if (driverMarker != null)
				driverMarker.remove();
			// Current Location marker
			currentLocation = location;
			driverMarker = AddMarker(currentLocation.getLatitude(), currentLocation.getLongitude(), R.drawable.cabsarround);// driver_map

			zoomCurrentLocationMarker(currentLocation.getLatitude(), currentLocation.getLongitude());

			// Overlay: Disable map and zoom current location
			if (llTopButtonsOverlay.getVisibility() == View.VISIBLE) {

				mMap.getUiSettings().setAllGesturesEnabled(false);
				mMap.animateCamera(CameraUpdateFactory.zoomIn());
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(),
						currentLocation.getLongitude()), 17.0f);
				mMap.animateCamera(cameraUpdate);
			}
		}
	}

	void zoomCurrentLocationMarker(double lat, double lng) {
		LatLng latLng = new LatLng(lat, lng);
		CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 14.0f);
		mMap.animateCamera(yourLocation);
	}

	public class SendMessageTask extends AsyncTask<String, Void, String> {
		String jobID;
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Sending message...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (Constants.isDebug)
				Log.d("SendMessageTask", "jobID:: " + jobID);

			SendMessage message = new SendMessage(jobID, mContext, getResources().getString(R.string.arrived_at_pick_up), false);
			String response = message.sendMessages();
			try {
				JSONObject jObject = new JSONObject(response);
				if (jObject.has("success") && jObject.getString("success").equals("true")) {
					FlurryAgent.logEvent("Driver arrived at pick up");
					DriverSettingDetails driverSettings = new DriverSettingDetails(mContext);
					driverSettings.retriveDriverSettings(mContext);
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

			if (pDialog != null)
				pDialog.dismiss();

			if (result != null && result.equals("success")) {

				AppValues.mapArrivedAtPickUpMessages.put(jobID, "true");

				rl_btnIamOutside.setVisibility(View.GONE);

				if (getActivity() != null)
					Util.showToastMessage(getActivity(), "Message Sent", Toast.LENGTH_LONG);
				mMap.clear();
				points.clear();
				points.add(pickUpPosition);
				points.add(dropPosition);
				isCountDownTimerVisible = false;
				tvCountdownTime.setVisibility(View.GONE);
				// Pick up marker
				driverMarker = AddMarker(Double.valueOf(mMainBundle.getString(Constants.PICK_UP_LOCATION_LAT)),
						Double.valueOf(mMainBundle.getString(Constants.PICK_UP_LOCATION_LNG)), R.drawable.cabsarround);// R.drawable.passenger

				// Drop marker
				AddMarker(Double.valueOf(mMainBundle.getString(Constants.DROP_LOCATION_LAT)),
						Double.valueOf(mMainBundle.getString(Constants.DROP_LOCATION_LNG)), R.drawable.dropoff);

				if (!mMainBundle.getString(Constants.DROP_ADDRESS).equalsIgnoreCase("As Directed"))
					makeRoute(points);

				CheckNoShow();

			} else {
				Util.showToastMessage(getActivity(), "Error in network! Please try again.", Toast.LENGTH_LONG);
			}
		}
	}

	void setDistance() {

		// if (mMainBundle.getString(Constants.DISTANCE_PICKUP) != null)
		// tvPickUpDistance.setText(mMainBundle
		// .getString(Constants.DISTANCE_PICKUP) + " miles");
		// else
		// tvPickUpDistance.setText("");
		// if (mMainBundle.getString(Constants.DISTANCE_DROP) != null)
		// tvDropDistance.setText(mMainBundle
		// .getString(Constants.DISTANCE_DROP) + " miles");
		// else
		// tvDropDistance.setText("");
		try {

			if (Util.mContext != null) {

				if (!mMainBundle.getString(Constants.DISTANCE_DROP).equals("-")
						&& !mMainBundle.getString(Constants.DISTANCE_DROP).equals("")) {
					tvDropDistance.setText(Util.getDistance(mContext,
							Double.parseDouble(mMainBundle.getString(Constants.DISTANCE_DROP)), Util.getDistanceFormat(mContext))
							+ " " + Util.getDistanceFormat(mContext));
				}

				if (!mMainBundle.getString(Constants.DISTANCE_PICKUP).equals("-")
						&& !mMainBundle.getString(Constants.DISTANCE_PICKUP).equals("")) {
					tvPickUpDistance.setText(Util.getDistance(mContext,
							Double.parseDouble(mMainBundle.getString(Constants.DISTANCE_PICKUP)),
							Util.getDistanceFormat(mContext))
							+ " " + Util.getDistanceFormat(mContext));
				}

			}

			// else {
			//
			// if (!mMainBundle.getString(Constants.DISTANCE_DROP).equals("-")
			// && !mMainBundle.getString(Constants.DISTANCE_DROP)
			// .equals("")) {
			// tvDropDistance.setText(Util.getDistance(mContext, Double
			// .parseDouble(mMainBundle
			// .getString(Constants.DISTANCE_DROP)), "km")
			// + " km");
			// }
			//
			// if (!mMainBundle.getString(Constants.DISTANCE_PICKUP).equals(
			// "-")
			// && !mMainBundle.getString(Constants.DISTANCE_PICKUP)
			// .equals("")) {
			// tvPickUpDistance.setText(Util.getDistance(mContext, Double
			// .parseDouble(mMainBundle
			// .getString(Constants.DISTANCE_DROP)), "km")
			// + " km");
			// }
			//
			// }
		} catch (Exception e) {

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mCountdownTimer != null)
			try {
				mCountdownTimer.cancel();
			} catch (Exception e) {

			}
		if (updateLocationMarkersTimer != null)
			try {
				updateLocationMarkersTimer.cancel();
			} catch (Exception e) {

			}

		if (mNoShowTimer != null)
			try {
				mNoShowTimer.cancel();
			} catch (Exception e) {

			}
	}

}
