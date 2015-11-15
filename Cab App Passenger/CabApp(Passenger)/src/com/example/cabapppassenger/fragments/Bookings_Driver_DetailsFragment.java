package com.example.cabapppassenger.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.BookingStatusTask;
import com.example.cabapppassenger.task.SendReceiptTask;
import com.example.cabapppassenger.util.Constant;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@SuppressLint("ValidFragment")
public class Bookings_Driver_DetailsFragment extends RootFragment implements
		OnCustomBackPressedListener {

	Handler mHandler;
	Context mContext;
	TextView tvPickupAddress, tvDropOffAddress, tvPaymentMethod, tvTime,
			tv_username, tv_badgenumber, tv_mobile_no, tv_vehicle_no,
			tv_cabpin, tv_cancel;
	String cabpin, date_time, pickup_addr1, pickup_addr2, drop_addr1,
			drop_addr2, driver_firstname, driver_surname, driver_badgenumber,
			driver_mobileno, driver_internationcode, vehicle_model,
			vehicle_color, vehicle_registration, driver_id;
	Double latitude, longitude;
	ImageView ivBack;
	RelativeLayout rl_track, rl_contact, rl_history, rl_receipt;
	LinearLayout ll_active;
	boolean isHistory = false, isCompleted = false;
	String PREF_NAME = "CabApp_Passenger", accessToken, bookingId;
	SharedPreferences sharedPreferences;
	AlertDialog jobcompleted;

	public Bookings_Driver_DetailsFragment(boolean isHistory, String bookingId) {
		this.isHistory = isHistory;
		this.isCompleted = isHistory;
		this.bookingId = bookingId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_bookings_drivers,
				container, false);
		mContext = getActivity();
		tvPickupAddress = (TextView) rootView
				.findViewById(R.id.tvPickupAddress);
		ivBack = (ImageView) rootView.findViewById(R.id.ivBack);
		rl_track = (RelativeLayout) rootView.findViewById(R.id.rl_track);
		tvDropOffAddress = (TextView) rootView
				.findViewById(R.id.tvDropOffAddress);
		tvPaymentMethod = (TextView) rootView
				.findViewById(R.id.tvPaymentMethod);
		tvTime = (TextView) rootView.findViewById(R.id.tvTime);
		tv_cabpin = (TextView) rootView.findViewById(R.id.tv_cabpin);
		tvPaymentMethod = (TextView) rootView
				.findViewById(R.id.tvPaymentMethod);
		tv_username = (TextView) rootView.findViewById(R.id.tv_username);
		tv_badgenumber = (TextView) rootView.findViewById(R.id.tv_badgenumber);
		tv_mobile_no = (TextView) rootView.findViewById(R.id.tv_mobile_no);
		tv_vehicle_no = (TextView) rootView.findViewById(R.id.tv_vehicle_no);
		tv_cancel = (TextView) rootView.findViewById(R.id.tv_cancel);
		rl_contact = (RelativeLayout) rootView.findViewById(R.id.rl_contact);
		rl_history = (RelativeLayout) rootView.findViewById(R.id.rl_history);
		ll_active = (LinearLayout) rootView.findViewById(R.id.ll_active);

		if (isHistory) {
			rl_history.setVisibility(View.VISIBLE);
			ll_active.setVisibility(View.GONE);
			tv_mobile_no.setVisibility(View.GONE);
			tv_cancel.setVisibility(View.GONE);
		} else {
			rl_history.setVisibility(View.GONE);
			ll_active.setVisibility(View.VISIBLE);
			tv_cancel.setVisibility(View.VISIBLE);
		}
		sharedPreferences = getActivity().getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		accessToken = sharedPreferences.getString("AccessToken", "");
		// bookingId = sharedPreferences.getString("Booking Id", "");

		rl_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SendReceiptTask sendReceiptTask = new SendReceiptTask(mContext,
						Constant.passengerURL
								+ "/ws/v2/passenger/receipt/?accessToken="
								+ accessToken, bookingId);

				sendReceiptTask.execute();

			}
		});

		rl_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChatFragment edit = new ChatFragment(bookingId, false, false);

				FragmentTransaction ft = getParentFragment()
						.getChildFragmentManager().beginTransaction();
				ft.replace(R.id.frame_container, edit, "chatFragment");
				ft.addToBackStack("bookingDriverDetailsFragment");
				Bundle bundle = new Bundle();
				bundle.putString("driverMobileNumber", driver_mobileno);
				bundle.putString("driverFirstName", driver_firstname);
				bundle.putString("driverLastName", driver_surname);
				edit.setArguments(bundle);
				ft.commit();
				/*
				 * getActivity().getSupportFragmentManager().
				 * saveFragmentInstanceState
				 * (Bookings_Driver_DetailsFragment.this); Intent intent = new
				 * Intent(getActivity().getBaseContext(), MyChatActivity.class);
				 * 
				 * intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 * startActivity(intent);
				 */
			}
		});
		rl_track.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				if (driver_id != null && vehicle_registration != null)
					bundle.putString("DriverId", driver_id);
				bundle.putString("VehicleReg", vehicle_registration);
				bundle.putString("DriverFirstname", driver_firstname);
				bundle.putString("VehicleModel", vehicle_model);
				bundle.putString("VehicleColor", vehicle_color);
				bundle.putString("DriverSecondname", driver_surname);
				if (latitude != null)
					bundle.putDouble("Pickup_latitude", latitude);
				if (longitude != null)
					bundle.putDouble("Pickup_longitude", longitude);
				TrackDriverFragment edit = new TrackDriverFragment();
				FragmentTransaction ft = getParentFragment()
						.getChildFragmentManager().beginTransaction();
				edit.setArguments(bundle);
				ft.replace(R.id.frame_container, edit, "trackDriverFragment");
				ft.addToBackStack("bookingsDriverDetailsFragment");
				ft.commit();
			}
		});
		tv_cancel = (TextView) rootView.findViewById(R.id.tv_cancel);
		tv_cancel.setText("Cancel");
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// HailNowParentFragment edit = new HailNowParentFragment();
				// FragmentTransaction ft = getActivity()
				// .getSupportFragmentManager().beginTransaction();
				// ft.replace(R.id.frame_container, edit,
				// "hailNowParentFragment");
				// ft.commit();
				// ((MainFragmentActivity) mContext).refresh_menu(0);
				Constant.cancel_dialog("cancel_sure", mContext, bookingId,
						mHandler);
				// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				// CancelBookingTask task = new CancelBookingTask(mContext);
				// task.booking_id = shared_pref.getString("Booking Id", "");
				// task.handler = mHandler;
				// task.execute();
				// } else {
				// Toast.makeText(mContext, "No network connection.",
				// Toast.LENGTH_SHORT).show();
				// }
				//
			}
		});
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (FindTaxiFragment.isActive) {
					FindTaxiFragment.isActive = false;
					HailNowParentFragment edit = new HailNowParentFragment();
					FragmentTransaction ft = getActivity()
							.getSupportFragmentManager().beginTransaction();
					ft.replace(R.id.frame_container, edit,
							"hailNowParentFragment");
					ft.commit();
					((MainFragmentActivity) mContext).refresh_menu(0);
				} else {
					getParentFragment().getChildFragmentManager()
							.popBackStack();
				}
			}
		});

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				if (msg.what == 3) {
					jobcompleted("jobcompleted", mContext);
					// when job is complete
					// rl_history.setVisibility(View.VISIBLE);
					// ll_active.setVisibility(View.GONE);
					// tv_mobile_no.setVisibility(View.GONE);
					// tv_cancel.setVisibility(View.GONE);

				}

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					if (bundleData.containsKey("CancelBooking")) {

						HailNowParentFragment edit = new HailNowParentFragment();
						FragmentTransaction ft = getActivity()
								.getSupportFragmentManager().beginTransaction();
						ft.replace(R.id.frame_container, edit,
								"hailNowParentFragment");
						ft.commit();
						((MainFragmentActivity) mContext).refresh_menu(0);
						Toast.makeText(
								mContext,
								"Your booking has been cancelled successfully.",
								Toast.LENGTH_SHORT).show();
					}

					if (bundleData != null
							&& bundleData.containsKey("Booking Id"))
						displayvalues(bundleData);

				}
			}
		};
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			BookingStatusTask task = new BookingStatusTask(mContext, bookingId);
			task.mHandler = mHandler;
			task.execute("");
		} else {
			Toast.makeText(MainFragmentActivity.getInstance(),
					"No network connection.", Toast.LENGTH_SHORT).show();
		}
		super.initWidgets(rootView, this.getClass().getName());
		return rootView;
	}

	public void displayvalues(Bundle bundledata) {
		// TODO Auto-generated method stub
		if (bundledata != null) {
			// bookingid = bundledata.getString("Booking Id");
			cabpin = bundledata.getString("CabPin");
			date_time = bundledata.getString("Pickupdate_time");
			pickup_addr1 = bundledata.getString("Pickup_addr1");
			pickup_addr2 = bundledata.getString("Pickup_addr2");
			drop_addr1 = bundledata.getString("Drop_addr1");
			drop_addr2 = bundledata.getString("Drop_addr2");
			driver_firstname = bundledata.getString("Driver_firstname");
			driver_surname = bundledata.getString("Driver_lastname");
			driver_badgenumber = bundledata.getString("Driver_badgenumber");
			driver_mobileno = bundledata.getString("Driver_mobileno");
			driver_internationcode = bundledata
					.getString("Driver_internationalno");
			vehicle_model = bundledata.getString("Vehicle_model");
			vehicle_registration = bundledata.getString("Vehicle_registration");
			latitude = bundledata.getDouble("Pickup_latitude");
			longitude = bundledata.getDouble("Pickup_longitude");
			driver_id = bundledata.getString("DriverId");
			vehicle_color = bundledata.getString("Vehicle_Color");

			tvPickupAddress.setText(pickup_addr1);
			if (cabpin != null && !cabpin.matches("null")
					&& !cabpin.matches(""))

				tv_cabpin.setText(cabpin);

			else
				tv_cabpin.setText("- - -");
			tvDropOffAddress.setText(drop_addr1);

			SimpleDateFormat sdf = new SimpleDateFormat("E dd LLL");
			Calendar c = Calendar.getInstance();
			int selectedDay = Integer.parseInt(date_time.substring(0,
					date_time.indexOf("/")));
			// int selectedMonth = Integer.parseInt(date_time.substring(
			// date_time.indexOf("/") + 1, date_time.lastIndexOf("/"))) - 1;
			int selectedMonth = Integer.parseInt(date_time.split("/")[1]
					.substring(0, 2).trim()) - 1;
			// Log.e("Selected Day", "" + selectedMonth);
			// if (selectedMonth == -1) {
			// selectedMonth = 11;
			// }
			Log.e("Date", date_time);
			int hour = Integer.parseInt(date_time.split(" ")[1].substring(0, 2)
					.trim());
			int minute = Integer.parseInt(date_time.split(" ")[1].substring(3,
					5).trim());

			Log.i(getClass().getSimpleName(), "selectedDay " + selectedDay
					+ "hour and minute " + hour + minute + " selectedMonth "
					+ selectedMonth);

			c.set(0, selectedMonth, selectedDay, hour, minute, 0);
			String pickUpDateToShow = sdf.format(c.getTime());

			tvTime.setText(pickUpDateToShow + " " + hour + ":" + minute);
			tv_username.setText(driver_firstname + " "
					+ driver_surname.substring(0, 1));
			tv_badgenumber.setText(driver_badgenumber);
			tv_mobile_no.setText(driver_mobileno);
			tv_vehicle_no.setText(vehicle_registration + " " + vehicle_model);
			tvPaymentMethod.setText("On Meter");
		}

	}

	@Override
	public void onResume() {
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
		super.onResume();

	}

	@Override
	public void onCustomBackPressed() {
		Log.i(getClass().getSimpleName(), "onCustomBackPressed");
		getParentFragment().getChildFragmentManager().popBackStack();

	}

	@Override
	public void onPause() {
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		super.onPause();
	}

	public void jobcompleted(String message, final Context mContext) {
		TextView title = new TextView(mContext);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCustomTitle(title);

		if (message == "jobcompleted") {
			builder.setMessage("This booking is already completed.");
		}

		builder.setCancelable(false);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				try {
					dialog.cancel();
					rl_history.setVisibility(View.VISIBLE);
					ll_active.setVisibility(View.GONE);
					tv_mobile_no.setVisibility(View.GONE);
					tv_cancel.setVisibility(View.GONE);
				} catch (Exception e) {

				}
			}
		});
		if (jobcompleted == null && builder != null)
			jobcompleted = builder.show();
		else if (jobcompleted != null && builder != null) {
			jobcompleted.dismiss();
			jobcompleted = builder.show();
		}

	}
}
