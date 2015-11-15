package com.example.cabapppassenger.fragments;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.CancelBookingTask;
import com.example.cabapppassenger.task.UpdateRadiusTask;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class FindTaxiFragment extends RootFragment implements
		OnCustomBackPressedListener {

	ImageView iv_back;
	private SupportMapFragment mMapFragment;
	private ViewGroup mMapFrame;
	public GoogleMap mMap;
	LatLng currentLoc = null;
	TextView tv_time, tv_cabs, tv_cancel, tv_plswait;
	String PREF_NAME = "CabApp_Passenger";
	Location currentLocation = null;
	Editor editor;
	Timer timer_animation, timer_cancel_booking, timer_topbar_text;// timer_topbar_text
	SharedPreferences shared_pref;
	Handler mHandler;
	TimerTask timer_task_animation, timer_task_cancel_booking,
			timer_task_topbar_text;// timer_task_topbar_text
	Context mContext;
	public static Dialog pickerdialog;
	ImageView progressSpinner;
	private Animation mAnimation;
	Animation animZoomin, animZoomout;
	PreBookParcelable pbParcelable;
	int zoomout = 17;
	AlertDialog alert_cancel;
	String booking_id;
	public static boolean isActive;

	public FindTaxiFragment(String bookingId) {
		booking_id = bookingId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		savedInstanceState = getArguments();
		pbParcelable = (savedInstanceState.containsKey("pbParcelable") ? (PreBookParcelable) savedInstanceState
				.get("pbParcelable") : null);
		isActive = true;
		mContext = getActivity();
		pickerdialog = new Dialog(mContext, android.R.style.Theme_Translucent);
		pickerdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		MapsInitializer.initialize(getActivity().getApplicationContext());
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			if (Util.getLocation(getActivity()) != null) {
				currentLocation = Util.getLocation(getActivity());
				editor.putLong("OnMapLatitude", Double
						.doubleToRawLongBits(currentLocation.getLatitude()));
				editor.putLong("OnMapLongitude", Double
						.doubleToRawLongBits(currentLocation.getLongitude()));
				editor.commit();
			}
		} else {
			Toast.makeText(mContext, "No network connection.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_findtaxi, container,
				false);
		progressSpinner = (ImageView) rootView
				.findViewById(R.id.progressSpinner);
		tv_plswait = (TextView) rootView.findViewById(R.id.tv_plswait);
		tv_plswait.setSelected(true);
		tv_plswait.setEnabled(true);
		// tv_plswait.setEllipsize(TruncateAt.MARQUEE);
		// tv_plswait.setMarqueeRepeatLimit(-1);

		// DisplayMetrics dm = getResources().getDisplayMetrics();
		//
		// TranslateAnimation m_ta = new TranslateAnimation(Animation.ABSOLUTE,
		// -10,
		// Animation.ABSOLUTE, -10f,
		// Animation.RELATIVE_TO_SELF, 0.5,
		// Animation.RELATIVE_TO_PARENT, 0.8);
		// m_ta.setDuration(10000);
		// // m_ta.setInterpolator(new LinearInterpolator());
		// m_ta.setRepeatCount(Animation.INFINITE);
		// tv_plswait.startAnimation(m_ta);

		animZoomin = AnimationUtils.loadAnimation(getActivity(),
				R.anim.zoomin_animation);
		animZoomout = AnimationUtils.loadAnimation(getActivity(),
				R.anim.zoomout_animation);
		progressSpinner.startAnimation(animZoomin);

		animZoomin.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				progressSpinner.startAnimation(animZoomout);
			}
		});

		animZoomout.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				progressSpinner.startAnimation(animZoomin);
			}
		});

		iv_back = (ImageView) rootView.findViewById(R.id.ivBack);
		iv_back.setVisibility(View.VISIBLE);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchcab_dialog();
			}
		});
		tv_cancel = (TextView) rootView.findViewById(R.id.tv_cancel);
		tv_cancel.setVisibility(View.VISIBLE);
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchcab_dialog();
			}
		});
		tv_cabs = (TextView) rootView.findViewById(R.id.tv_cabs);
		tv_time = (TextView) rootView.findViewById(R.id.tv_time);
		mMapFrame = (ViewGroup) rootView.findViewById(R.id.map_frame);
		mMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getChildFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.map_frame, mMapFragment);
		fragmentTransaction.commit();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					if (bundleData.containsKey("Available Cab Response")) {
						availablecab_jsonparser(bundleData.getString(
								"Available Cab Response", ""));
					}
					if (bundleData.containsKey("Jobtimeout")) {
						cancelbookingdialog("jobtimeout", mContext);
						canceloperations();
					}
					if (bundleData.containsKey("CancelBooking")) {
						cancelbookingdialog("cancelbooking", mContext);
						canceloperations();
					}
					// tv_plswait.requestFocus();
				}
			}
		};

		timer_animation = new Timer();
		timer_task_animation = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				// API Hit
				if (getActivity() != null) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							if (pickerdialog.isShowing()) {
								pickerdialog.dismiss();
							}
							searchcab_dialog();
							if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
								UpdateRadiusTask task = new UpdateRadiusTask(
										mContext);
								task.booking_id = shared_pref.getString(
										"Booking Id", "");
								task.mHandler = mHandler;
								task.execute(Constant.passengerURL
										+ "ws/v2/passenger/bookings/updateRadius/?accessToken="
										+ shared_pref.getString("AccessToken",
												""));
								if (zoomout <= 12)
									zoomout = 17;
								mMap.moveCamera(CameraUpdateFactory
										.newLatLngZoom(currentLoc, zoomout));
								zoomout--;

								Log.e("Zoom", "" + zoomout);
							} else {
								Toast.makeText(mContext,
										"No network connection.",
										Toast.LENGTH_SHORT).show();
							}
						}

					});
				}
			}
		};
		timer_cancel_booking = new Timer();
		timer_task_cancel_booking = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (getActivity() != null) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							if (pickerdialog != null
									&& pickerdialog.isShowing()) {
								pickerdialog.dismiss();
							}
							if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
								CancelBookingTask task = new CancelBookingTask(
										mContext);
								task.jobtimeout = true;
								task.booking_id = booking_id;
								task.handler = mHandler;
								task.execute();
							} else {
								Toast.makeText(mContext,
										"No network connection.",
										Toast.LENGTH_SHORT).show();
							}
						}

					});
				}
			}
		};

		timer_topbar_text = new Timer();
		timer_task_topbar_text = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (getActivity() != null) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {

							if (!tv_plswait
									.getText()
									.equals("It's quite busy, we're still searching..."))
								tv_plswait
										.setText("Please wait while we find you a taxi...It's quite busy, we're still searching...");
						}

					});
				}
			}
		};

		timer_cancel_booking.scheduleAtFixedRate(timer_task_cancel_booking,
				shared_pref.getLong("jobCancellationTimeout", 15) * 60000,
				shared_pref.getLong("jobCancellationTimeout", 15) * 60000);

		timer_topbar_text.scheduleAtFixedRate(timer_task_topbar_text, 15000,
				15000);

		timer_animation.scheduleAtFixedRate(timer_task_animation,
				shared_pref.getLong("IncrementalValue", 15) * 1000,
				shared_pref.getLong("IncrementalValue", 15) * 1000);

		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		tv_plswait.requestFocus();
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

			UpdateRadiusTask task = new UpdateRadiusTask(mContext);
			task.booking_id = shared_pref.getString("Booking Id", "");
			task.mHandler = mHandler;
			task.execute(Constant.passengerURL
					+ "ws/v2/passenger/bookings/updateRadius/?accessToken="
					+ shared_pref.getString("AccessToken", ""));
			// }

		} else {
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mMap = mMapFragment.getMap();
		mMap.getUiSettings().setAllGesturesEnabled(false);
		mMap.getUiSettings().setZoomControlsEnabled(false);
		if (currentLocation != null) {
			currentLoc = new LatLng(currentLocation.getLatitude(),
					currentLocation.getLongitude());
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
		}
	}

	@SuppressWarnings("unused")
	public void availablecab_jsonparser(String response) {
		// TODO Auto-generated method stub
		JSONObject Javailablecabs_obj = null;
		String totalDrivers, time, distance;
		try {
			Javailablecabs_obj = new JSONObject(response);
			if (Javailablecabs_obj != null) {

				totalDrivers = Javailablecabs_obj.getString("totalDrivers");
				if (!totalDrivers.equals("0")) {
					time = Javailablecabs_obj.getString("eta");
					if (time.equals("false"))
						time = "0";
					else if (time.equals("true"))
						time = "1";

					// distance = Javailablecabs_obj.getString("distance");
					editor.putString("CabTime", time);
					editor.putString("TotalCabs", totalDrivers);
					editor.commit();
					if (time != null)
						if (time.matches("0") || time.matches("1"))
							tv_time.setText(time + " Min");

						else if (time != null && !time.matches("0")
								&& !time.matches("1"))
							tv_time.setText(time + " Mins");
						else
							tv_time.setText("0 Min");

					// cabs
					if (totalDrivers != null)
						if (totalDrivers.matches("0")
								|| totalDrivers.matches("1"))
							tv_cabs.setText(totalDrivers + " Cab");

						else if (totalDrivers != null
								&& !totalDrivers.matches("0")
								&& !totalDrivers.matches("1"))
							tv_cabs.setText(totalDrivers + " Cabs");

						else
							tv_cabs.setText("0 Cabs");

					JSONArray jArr_drivers = Javailablecabs_obj
							.getJSONArray("drivers");
					if (jArr_drivers != null && jArr_drivers.length() > 0) {
						mMap.clear();
						for (int i = 0; i < jArr_drivers.length(); i++) {
							JSONObject jobj = jArr_drivers.getJSONObject(i);
							Double cablatitude = jobj.getDouble("latitude");
							Double cablongitude = jobj.getDouble("longitude");
							String forename = jobj.getString("forename");
							if (cablatitude != null && cablongitude != null
									&& cablatitude != 0.0
									&& cablongitude != 0.0) {
								AddMarker(cablatitude, cablongitude,
										R.drawable.cabsarround);
							}

						}
					}
				} else {
					tv_time.setText("0 Min");
					tv_cabs.setText("0 Cab");
				}
			}
		} catch (Exception e) {
			try {
				if (response != null) {
					JSONObject jobj = new JSONObject(response);
					JSONArray jArray = jobj.getJSONArray("errors");
					for (int i = 0; i <= jArray.length(); i++) {
						JSONObject jObj = jArray.getJSONObject(i);
						String message = jObj.getString("message");
						Log.i("****Login Error****", "" + message);
						Toast.makeText(mContext, "" + message,
								Toast.LENGTH_SHORT).show();
					}
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
	}

	public void AddMarker(Double lat, Double lng, int icon) {

		if (lat != null && lng != null) {
			MarkerOptions markerOptions = new MarkerOptions();
			// markerOptions.draggable(true);
			markerOptions.position(new LatLng(lat, lng));
			markerOptions.icon(BitmapDescriptorFactory.fromResource(icon));

			mMap.addMarker(markerOptions);

		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// if (timer_animation != null) {// && timer_topbar_text != null
		// timer_animation.purge();
		// // timer_topbar_text.purge();
		// }
		//
		// if (timer_task_animation != null) {// && timer_task_topbar_text !=
		// null
		// timer_task_animation.cancel();
		// // timer_task_topbar_text.cancel();
		// }
		// if (timer_cancel_booking != null)
		// timer_cancel_booking.purge();
		// if (timer_task_cancel_booking != null)
		// timer_task_cancel_booking.cancel();
		// // timer_task_animation.cancel();
		// if (pickerdialog != null)
		// pickerdialog.dismiss();
		// if (animZoomin != null)
		// animZoomin.cancel();
		// if (animZoomout != null)
		// animZoomout.cancel();
		canceloperations();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		// if (timer_animation != null) {
		// timer_animation.purge();
		// // timer_topbar_text.purge();
		// }
		// if (timer_cancel_booking != null)
		// timer_cancel_booking.purge();
		// if (timer_task_cancel_booking != null)
		// timer_task_cancel_booking.cancel();
		// if (timer_task_animation != null) {
		// timer_task_animation.cancel();
		// // timer_task_topbar_text.cancel();
		// }
		// if (pickerdialog != null)
		// pickerdialog.dismiss();
		// if (animZoomin != null)
		// animZoomin.cancel();
		// if (animZoomout != null)
		// animZoomout.cancel();
		canceloperations();
	}

	public void searchcab_dialog() {
		tv_plswait.requestFocus();
		pickerdialog.setContentView(R.layout.alertdialog_bookingcab);
		pickerdialog.setCancelable(true);
		pickerdialog.setCanceledOnTouchOutside(true);

		final Button btn_amend = (Button) pickerdialog
				.findViewById(R.id.btn_amend);
		final Button btn_cancel = (Button) pickerdialog
				.findViewById(R.id.btn_cancel);
		final Button btn_continue = (Button) pickerdialog
				.findViewById(R.id.btn_continue);

		// if button is clicked, open the camera
		btn_amend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pickerdialog.dismiss();

				Fragment fBookingDetails = getParentFragment()
						.getChildFragmentManager().findFragmentByTag(
								"bookingDetailsFragment");
				Bundle bundle = new Bundle();
				bundle.putString("AmendBooking",
						shared_pref.getString("Booking Id", ""));
				// fBookingDetails.setArguments(bundle);

				if (fBookingDetails != null) {

					FragmentTransaction ft = getParentFragment()
							.getChildFragmentManager().beginTransaction();
					ft.replace(R.id.frame_container, fBookingDetails);
					ft.commit();
				}
			}
		});
		// if button is clicked, open the camera
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				pickerdialog.dismiss();
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
					CancelBookingTask task = new CancelBookingTask(mContext);
					task.booking_id = booking_id;
					task.handler = mHandler;
					task.execute();
				} else {
					Toast.makeText(mContext, "No network connection.",
							Toast.LENGTH_SHORT).show();
				}
				// task.execute(Constant.passengerURL +
				// "ws/v2/passenger/bookings"
				// + shared_pref.getString("Booking Id", "")
				// + "/delete?accessToken="
				// + shared_pref.getString("AccessToken", ""));
				// MainFragmentActivity.bIsQuitDialogToBeShown = false;
				//
				// Hail_NowFragment edit = new Hail_NowFragment();
				// FragmentTransaction ft = getParentFragment()
				// .getChildFragmentManager().beginTransaction();
				// ft.replace(R.id.frame_container, edit, "hailNowFragment");
				// ft.commit();
				// ((MainFragmentActivity) mContext).refresh_menu(0);
			}
		});

		// if button is clicked, open gallery
		btn_continue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				pickerdialog.dismiss();
			}
		});
		pickerdialog.show();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public void onDetach() {

		Fragment fPreBook = getParentFragment().getChildFragmentManager()
				.findFragmentByTag("bookingDetailsFragment");
		try {
			((BookingDetailsFragment) fPreBook).updateValues(pbParcelable);
		} catch (Exception e) {

		}

		super.onDetach();
	}

	@Override
	public void onCustomBackPressed() {
		searchcab_dialog();

	}

	public void cancelbookingdialog(final String message, final Context mContext) {
		TextView title = new TextView(mContext);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCustomTitle(title);

		if (message == "jobtimeout") {
			builder.setMessage("Sorry! Your booking has been cancelled.");
		}
		if (message == "cancelbooking") {
			builder.setMessage("Your booking has been cancelled successfully.");
		}

		builder.setCancelable(false);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				HailNowParentFragment edit = new HailNowParentFragment();
				FragmentTransaction ft = getParentFragment()
						.getChildFragmentManager().beginTransaction();
				ft.replace(R.id.frame_container, edit, "hailNowParentFragment");
				ft.commit();
				((MainFragmentActivity) mContext).refresh_menu(0);
			}
		});

		if (alert_cancel == null && builder != null)
			alert_cancel = builder.show();
		else if (alert_cancel != null && builder != null) {
			alert_cancel.dismiss();
			alert_cancel = builder.show();
		}

	}

	void canceloperations() {
		if (timer_animation != null && timer_topbar_text != null) {
			timer_animation.purge();
			timer_topbar_text.purge();
		}
		if (pickerdialog != null)
			pickerdialog.dismiss();

		if (timer_cancel_booking != null)
			timer_cancel_booking.purge();
		if (timer_task_cancel_booking != null)
			timer_task_cancel_booking.cancel();

		if (timer_task_animation != null && timer_task_topbar_text != null) {
			timer_task_animation.cancel();
			timer_task_topbar_text.cancel();
		}

		if (animZoomin != null) {
			animZoomin.cancel();
			animZoomout.reset();
		}
		if (animZoomout != null) {
			animZoomout.cancel();
			animZoomout.reset();
		}
	}
}
