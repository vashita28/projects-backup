/*****************************************
 * Coded By- Manpreet
 * 
 * 
 ***********************************/

package com.example.cabapppassenger.fragments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumPaymentType;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumWhen;
import com.example.cabapppassenger.listeners.PreBookListener;
import com.example.cabapppassenger.model.LocationParcelable;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.AddNewCardTask;
import com.example.cabapppassenger.task.EditBookings;
import com.example.cabapppassenger.task.GetUserDetailsTask;
import com.example.cabapppassenger.task.PassengerCountTask;
import com.example.cabapppassenger.task.ValidateBookingTask;
import com.example.cabapppassenger.util.Constant;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class BookingDetailsFragment extends RootFragment implements
		OnCustomBackPressedListener {

	/*
	 * Variables that use basic java datatypes
	 */
	String PREF_NAME = "CabApp_Passenger";

	boolean valuesUpdated = false;

	String pickUpLocation, pickUpPincode, dropOffLocation, dropOffPincode,
			pickUpDateTime = "", truncatedPan = "", url, accessToken;

	double pickUpLatitude, pickUpLongitude, dropOffLatitude, dropOffLongitude;

	String[] passengerCount;
	Editor editor;
	SharedPreferences shared_pref;
	/*
	 * Variables that use basic Android layout specific datatypes
	 */
	TextView tvPaymentHeading, tvPaymentMethod, tvPickUpAddress,
			tvPickUpPincode, tvDropOffPincode, tvDropOffAddress, tvEdit,
			tvWhen, tvDone, tv_card, tv_cash, tv_carddetails;
	ImageView ivBack;
	RelativeLayout rlDone, rl_main_card, tv_addnew_card;
	Spinner sp_cardDetails;
	ArrayList<String> arr_passenger_card = new ArrayList<String>();
	/*
	 * Variables that use basic java Android activity specific datatypes
	 */
	ArrayList<ArrayList<String>> arr_usercard_pass = new ArrayList<ArrayList<String>>();
	Context context;
	Fragment fragment;
	Handler mHandler;
	Button btn_spinneritems;
	Resources res;
	boolean amend_booking = false;
	// boolean flag_card = false, flag_cash = true;
	Bundle preBookDetails;
	SharedPreferences sharedPreferences;
	/*
	 * Variables that use custom datatypes
	 */
	DecimalFormatSymbols decimalFormatSymbols;
	DecimalFormat decimalFormat;
	PreBookParcelable pbParcelable, previousParcelable;
	PreBookListener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("BookingDetailsFragment", "OnCreate");
		context = getActivity();
		fragment = this;
		decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setGroupingSeparator('.');
		decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);
		res = getActivity().getResources();
		shared_pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		preBookDetails = getArguments();

		sharedPreferences = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		if (preBookDetails != null
				&& preBookDetails.containsKey("AmendBooking")) {
			amend_booking = true;
		}
		if (preBookDetails != null
				&& preBookDetails.containsKey("pbParcelable")) {
			pbParcelable = (PreBookParcelable) (preBookDetails
					.containsKey("pbParcelable") ? preBookDetails
					.getParcelable("pbParcelable") : null);
			Log.d(getClass().getSimpleName(),
					"Passed bundle mapDropOffLocation"
							+ pbParcelable.getMapDropOffLocation()
									.getAddress1()
							+ "mapDropOffPincode "
							+ pbParcelable.getMapDropOffLocation().getPinCode()
							+ "mapPickUpLocation"
							+ pbParcelable.getRecentDropOffLocation()
									.getAddress1()
							+ "mapDropOffPincode "
							+ pbParcelable.getRecentDropOffLocation()
									.getPinCode()
							+ "mapPickUpLocation"
							+ pbParcelable.getFavouriteDropOffLocation()
									.getAddress1()
							+ "mapDropOffPincode "
							+ pbParcelable.getFavouritePickUpLocation()
									.getPinCode()
							+ "mapPickUpLocation"
							+ pbParcelable.getMapPickUpLocation().getAddress1()
							+ "mapPickUpPincode"
							+ pbParcelable.getMapPickUpLocation().getPinCode()
							+ "defaultDropOffLocation"
							+ " presentLatitude "
							+ pbParcelable.getPresentLocation().getLatitude()
							+ " presentLongitude "
							+ pbParcelable.getPresentLocation().getLongitude()
							+ " mapPickUpLatitude "
							+ pbParcelable.getMapPickUpLocation().getLatitude()
							+ "mapPickUpLongitude"
							+ pbParcelable.getMapPickUpLocation()
									.getLongitude()
							+ " mapDropOffLatitude "
							+ pbParcelable.getMapDropOffLocation()
									.getLatitude()
							+ " mapDropOffLongitude "
							+ pbParcelable.getMapDropOffLocation()
									.getLongitude() + " paymentType "
							+ pbParcelable.getePaymentType().name()
							+ " isHearingImpairedUser "
							+ pbParcelable.isHearingImpairedSelected()
							+ " isWheelchairUser "
							+ pbParcelable.isWheelChairSelected()
							+ "selectedPassengerCount"
							+ pbParcelable.getPassengerCount()
							+ "pickUpDateTime" + pbParcelable.getPickUpDate()
							+ " " + pbParcelable.getPickUpTime() + " "
							+ pbParcelable.getPickUpDateToShow() + " comments "
							+ pbParcelable.getComment() + " isAsapSelected "
							+ pbParcelable.geteWhen().name() + " on "
							+ pbParcelable.getePickUpSelected().name()
							+ " drop off "
							+ pbParcelable.geteDropOffSelected().name());
		} else
			pbParcelable = new PreBookParcelable();

		if (!pbParcelable.isPaymentTypeUpdated()) {
			truncatedPan = sharedPreferences.getString("truncatedPan", "");
			Constant.flag_payment_method = true;
			if (sharedPreferences.getString("PayingMethod", "").equals("Card")
					|| sharedPreferences.getString("PayingMethod", "").equals(
							"card")
					|| sharedPreferences.getString("PayingMethod", "").equals(
							"both")
					|| sharedPreferences.getString("PayingMethod", "").equals(
							"cash and card")) {
				pbParcelable.setePaymentType(EnumPaymentType.BOTH);
				pbParcelable.setTruncatedPan(truncatedPan);
			} else if (sharedPreferences.getString("PayingMethod", "").equals(
					"Cash")
					|| sharedPreferences.getString("PayingMethod", "").equals(
							"cash")
					|| sharedPreferences.getString("PayingMethod", "").equals(
							"")) {

				pbParcelable.setePaymentType(EnumPaymentType.CASH);

			}
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_bookingdetails,
				container, false);

		/*
		 * Find all the controls from rootView
		 */
		tv_card = (TextView) rootView.findViewById(R.id.tv_card);
		tv_cash = (TextView) rootView.findViewById(R.id.tv_cash);
		btn_spinneritems = (Button) rootView
				.findViewById(R.id.btn_spinneritems);
		tvPaymentHeading = (TextView) rootView
				.findViewById(R.id.tvPaymentMethodHeading);
		tv_carddetails = (TextView) rootView.findViewById(R.id.tv_carddetails);
		sp_cardDetails = (Spinner) rootView.findViewById(R.id.sp_carddetails);
		tvPaymentMethod = (TextView) rootView
				.findViewById(R.id.tvPaymentMethod);
		tvPickUpAddress = (TextView) rootView
				.findViewById(R.id.tvPickupAddress);
		tvDropOffAddress = (TextView) rootView
				.findViewById(R.id.tvDropOffAddress);
		tvDropOffPincode = (TextView) rootView
				.findViewById(R.id.tvDropOffPincode);
		tvPickUpPincode = (TextView) rootView
				.findViewById(R.id.tvPickupPincode);
		rl_main_card = (RelativeLayout) rootView
				.findViewById(R.id.rl_main_card);
		tvEdit = (TextView) rootView.findViewById(R.id.tv_edit);
		tv_addnew_card = (RelativeLayout) rootView
				.findViewById(R.id.rl_addcard);

		restoreLayout();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				if (msg.what == 0) {
					arr_usercard_pass = Constant.hashmap_passengercard
							.get(Constant.USER_CARD_KEY);
					if (arr_usercard_pass != null)
						user_carddetails(arr_usercard_pass);
					Log.e("Card Details", "");
				}
				if (msg != null && msg.peekData() != null) {

					Bundle bundledata = (Bundle) msg.obj;
					if (bundledata.containsKey("URL")) {
						String url = bundledata.getString("URL");

						AddNewCardFragment card = new AddNewCardFragment();
						if (url != null) {
							Bundle bundle = new Bundle();
							bundle.putParcelable("pbParcelable", pbParcelable);
							bundle.putString("URL", url);
							card.setArguments(bundle);
						}
						FragmentTransaction ft = getParentFragment()
								.getChildFragmentManager().beginTransaction();
						ft.addToBackStack("bookingDetailsFragment");
						ft.replace(R.id.frame_container, card,
								"addNewCardFragment");
						ft.commit();
					}
					if (bundledata.containsKey("passenger_count_min")
							&& bundledata.containsKey("passenger_count_max")) {
						if (bundledata.getString("passenger_count_min").equals(
								"5")
								&& bundledata.getString("passenger_count_max")
										.equals("8")) {

							if (Constant.arr_passengercount != null) {

								if (Constant.arr_passengercount.size() > 0)
									Constant.arr_passengercount.clear();
								Constant.arr_passengercount.add("1-5");
								Log.e("Constant Passenger Count", ""
										+ Constant.arr_passengercount.size());
							}

						} else {
							if (Constant.arr_passengercount != null) {

								if (Constant.arr_passengercount.size() > 0)
									Constant.arr_passengercount.clear();

								Constant.arr_passengercount.add("1-4");

								Log.e("Constant Passenger Count", ""
										+ Constant.arr_passengercount.size());
							}
						}
						Log.e("Passenger Count",
								"" + pbParcelable.getPassengerCount());
						if (Constant.arr_passengercount != null
								&& Constant.arr_passengercount.size() > 0) {
							if (pbParcelable.getBookingId().equals("")) {

								ValidateBookingTask validateBookingTask = new ValidateBookingTask(
										fragment, context, pickUpLatitude,
										pickUpLongitude, dropOffLatitude,
										dropOffLongitude, pbParcelable,
										Constant.arr_passengercount
												.get(pbParcelable
														.getPassengerCount()),
										pickUpDateTime, truncatedPan, url,
										accessToken);
								validateBookingTask.execute();
							} else {
								Log.i(getClass().getSimpleName(),
										pbParcelable.getBookingId() + "");
								EditBookings editBookingTask = new EditBookings(
										context, pickUpLatitude,
										pickUpLongitude, dropOffLatitude,
										dropOffLongitude,
										Constant.arr_passengercount
												.get(pbParcelable
														.getPassengerCount()),
										pbParcelable, truncatedPan, fragment,
										pickUpDateTime);
								editBookingTask.execute(Constant.passengerURL
										+ "ws/v2/passenger/bookings/"
										+ pbParcelable.getBookingId()
										+ "/?accessToken=" + accessToken);
							}
						}
					}
				}
			}

		};
		tv_addnew_card.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertdialog(context);
			}
		});
		tvWhen = (TextView) rootView.findViewById(R.id.tvTime);
		tvDone = (TextView) rootView.findViewById(R.id.tv_done);
		ivBack = (ImageView) rootView.findViewById(R.id.ivBack);
		rlDone = (RelativeLayout) rootView.findViewById(R.id.rl_done);

		if (pbParcelable.geteWhen() == EnumWhen.ASAP) {
			tvWhen.setText("ASAP");
			tvDone.setText("Hail");
		} else {
			tvWhen.setText(pbParcelable.getPickUpDateToShow() + " "
					+ pbParcelable.getPickUpTime());
			tvDone.setText("Book");
		}

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getParentFragment().getChildFragmentManager().popBackStack();

			}
		});

		rlDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeybord(v);
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
					url = Constant.passengerURL
							+ "ws/v2/passenger/validateBooking/?accessToken=";

					accessToken = sharedPreferences
							.getString("AccessToken", "");
					pbParcelable.setPaymentTypeUpdated(true);

					if (pbParcelable.geteWhen() == EnumWhen.ASAP) {
						pickUpDateTime = "asap";
					} else {
						pickUpDateTime = pbParcelable.getPickUpDate() + " "
								+ pbParcelable.getPickUpTime() + ":00";
					}
					// hard coded payment type, as it is only cash for now
					if (tv_cash.getCompoundDrawables()[1].getConstantState()
							.equals((context.getResources()
									.getDrawable(R.drawable.cashselected))
									.getConstantState())) {

						pbParcelable.setePaymentType(EnumPaymentType.CASH);

					} else if ((arr_passenger_card.size() > 0)) {
						pbParcelable.setePaymentType(EnumPaymentType.BOTH);
						if (arr_passenger_card.size() == 1) {
							if (!tv_carddetails.getText().toString()
									.matches("No card added"))
								truncatedPan = tv_carddetails
										.getText()
										.toString()
										.substring(
												tv_carddetails.getText()
														.toString().length() - 5,
												tv_carddetails.getText()
														.toString().length());
						} else if (arr_passenger_card.size() > 1) {
							truncatedPan = sp_cardDetails
									.getSelectedItem()
									.toString()
									.substring(
											sp_cardDetails.getSelectedItem()
													.toString().toString()
													.length() - 5,
											sp_cardDetails.getSelectedItem()
													.toString().toString()
													.length());
						}

						pbParcelable.setTruncatedPan(truncatedPan);

					} else {
						// no card added
						Constant.alertdialog("nocardadded", context);
						return;
					}
					passengerCount = res
							.getStringArray(R.array.passengers_count_4);
					Log.e("Passenger Count",
							"" + pbParcelable.getPassengerCount());
					Log.e("Array passenger",
							"" + Constant.arr_passengercount.size());
					if (Constant.arr_passengercount.size() <= 0) {
						PassengerCountTask getPassengerCountTask = new PassengerCountTask(
								context, mHandler);
						getPassengerCountTask.latitude = pickUpLatitude;
						getPassengerCountTask.longitude = pickUpLongitude;
						getPassengerCountTask
								.execute(Constant.passengerURL
										+ "/ws/v2/passenger/minimumpassenger/?accessToken="
										+ shared_pref.getString("AccessToken",
												""));
					} else {
						if (pbParcelable.getBookingId().equals("")) {

							ValidateBookingTask validateBookingTask = new ValidateBookingTask(
									fragment, context, pickUpLatitude,
									pickUpLongitude, dropOffLatitude,
									dropOffLongitude, pbParcelable,
									Constant.arr_passengercount
											.get(pbParcelable
													.getPassengerCount()),
									pickUpDateTime, truncatedPan, url,
									accessToken);
							validateBookingTask.execute();
						} else {
							Log.i(getClass().getSimpleName(),
									pbParcelable.getBookingId() + "");
							EditBookings editBookingTask = new EditBookings(
									context, pickUpLatitude, pickUpLongitude,
									dropOffLatitude, dropOffLongitude,
									Constant.arr_passengercount
											.get(pbParcelable
													.getPassengerCount()),
									pbParcelable, truncatedPan, fragment,
									pickUpDateTime);
							editBookingTask.execute(Constant.passengerURL
									+ "ws/v2/passenger/bookings/"
									+ pbParcelable.getBookingId()
									+ "/?accessToken=" + accessToken);
						}
					}
				} else {
					Toast.makeText(getActivity(), "No network connection.",
							Toast.LENGTH_SHORT).show();
				}

			}

		});

		tvEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Fragment fPreBook;
				FragmentManager fragmentManager = getParentFragment()
						.getChildFragmentManager();
				fPreBook = getParentFragment().getChildFragmentManager()
						.findFragmentByTag("preBookFragment");
				if (fPreBook == null
						|| ((MainFragmentActivity) (getActivity())).nLastSelected == 2) {

					FragmentTransaction ft = fragmentManager.beginTransaction();
					fPreBook = new Pre_BookFragment();
					fPreBook.setArguments(getBundle());
					ft.addToBackStack("bookingDetailsFragment");
					ft.replace(R.id.frame_container, fPreBook,
							"preBookFragment");
					ft.commit();
				}

				else {

					getParentFragment().getChildFragmentManager()
							.popBackStackImmediate();

				}

			}

		});

		previousParcelable = pbParcelable.getClone();
		listener = new PreBookListener(getActivity(), rootView, pbParcelable);
		super.initWidgets(rootView, this.getClass().getName());

		return rootView;
	}

	protected void user_carddetails(ArrayList<ArrayList<String>> arr_usercard) {
		// TODO Auto-generated method stub
		if (arr_usercard != null && arr_usercard.size() > 0) {
			arr_passenger_card.clear();
			for (int i = 0; i < arr_usercard.size(); i++) {
				arr_passenger_card.add("**** **** **** "
						+ arr_usercard.get(i).get(1));
			}
		}
		// else {
		// tv_carddetails.setVisibility(View.GONE);
		// sp_cardDetails.setVisibility(View.GONE);
		// }
		Log.e("Card Details", "" + arr_passenger_card.size());
		if (arr_passenger_card != null && arr_passenger_card.size() > 0
				&& arr_passenger_card.size() == 1) {
			tv_carddetails.setVisibility(View.VISIBLE);
			Log.e("Card number", arr_passenger_card.get(0));
			tv_carddetails.setText(arr_passenger_card.get(0));
			// sp_cardDetails.setVisibility(View.GONE);
			Log.e("Inside Card Details size 1", "Array size 1");

		} else if (arr_passenger_card != null && arr_passenger_card.size() > 1) {
			tv_carddetails.setVisibility(View.GONE);
			sp_cardDetails.setVisibility(View.VISIBLE);
			btn_spinneritems.setVisibility(View.VISIBLE);
			Log.e("Inside Card Details size greater than 1",
					"Array size greater than 1");
			ArrayAdapter<String> condadapter = new ArrayAdapter<String>(
					context, android.R.layout.simple_spinner_item,
					arr_passenger_card);
			condadapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_cardDetails.setAdapter(condadapter);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
		Log.e("Preferred Method",
				sharedPreferences.getString("PayingMethod", ""));
		Log.d(getParentFragment().getChildFragmentManager()
				.getBackStackEntryCount() + "", "Fragment count"
				+ getParentFragment().getChildFragmentManager()
						.getBackStackEntryCount());
		MainFragmentActivity.slidingMenu
				.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		if (pbParcelable.isBackPressed()) {

			pbParcelable.setBackPressed(false);
			pbParcelable = previousParcelable;

		}

		LocationParcelable[] selectedLocation = listener
				.getSelectedLocationValues(pbParcelable);

		// Storing values, taken from last screen

		pickUpLocation = selectedLocation[0].getAddress1();
		pickUpPincode = selectedLocation[0].getPinCode();
		pickUpLatitude = selectedLocation[0].getLatitude();
		pickUpLongitude = selectedLocation[0].getLongitude();

		dropOffLocation = selectedLocation[1].getAddress1();
		dropOffPincode = selectedLocation[1].getPinCode();
		dropOffLatitude = selectedLocation[1].getLatitude();
		dropOffLongitude = selectedLocation[1].getLongitude();

		tvPickUpAddress.setText(pickUpLocation);
		tvDropOffAddress.setText(dropOffLocation);

		tvPickUpPincode.setText(pickUpPincode);
		tvDropOffPincode.setText(dropOffPincode);

		tv_card.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectCard();
			}
		});

		tv_cash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// hideKeybord(v);
				selectCash();
			}
		});

		if (pbParcelable.getePaymentType() == EnumPaymentType.BOTH)
			selectCard();
		else
			selectCash();

		if (Constant.flag_added_newcard) {
			Constant.flag_added_newcard = false;
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				GetUserDetailsTask task = new GetUserDetailsTask(context);
				task.handler = mHandler;
				task.execute("");
			} else {
				Toast.makeText(context, "No network connection",
						Toast.LENGTH_SHORT).show();
			}
		}
		if (pbParcelable.isFixedPrice()) {
			tvPaymentMethod.setText(pbParcelable.getFixedPriceCurrency() + " "
					+ decimalFormat.format(pbParcelable.getFixedPrice()));
		}
		if (Constant.hashmap_passengercard != null)
			arr_usercard_pass = Constant.hashmap_passengercard
					.get(Constant.USER_CARD_KEY);
		if (arr_usercard_pass != null)
			user_carddetails(arr_usercard_pass);

		if (pbParcelable.getTruncatedPan() != null
				&& !pbParcelable.getTruncatedPan().equals("")) {
			if (arr_passenger_card.size() > 1) {
				for (int i = 0; i < arr_passenger_card.size(); i++) {
					String truncated_pan = arr_passenger_card.get(i).substring(
							arr_passenger_card.get(i).length() - 5,
							arr_passenger_card.get(i).length());
					if (truncated_pan.trim().equals(
							pbParcelable.getTruncatedPan().trim())) {
						sp_cardDetails.setSelection(i);
						break;

					}

				}
			}
		}

		super.onResume();

	}

	@Override
	public void onPause() {
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		super.onPause();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		Log.d("BookingDetailsFragment",
				"for updateOnMapValues mapPickUpLocation ");
		/*
		 * try {
		 * 
		 * Fragment fPreBook = getParentFragment().getChildFragmentManager()
		 * .findFragmentByTag("preBookFragment");
		 * 
		 * ((Pre_BookFragment) fPreBook).updateOnMapValues(pbParcelable);
		 * 
		 * } catch (Exception e) { Log.i(getClass().getSimpleName(),
		 * "Exception: preBookFragment or  preBookFragment1 not found."); }
		 */
		// } else {
		// Fragment fPreBook =
		// getActivity().getSupportFragmentManager().findFragmentByTag(
		// "preBookFragment1");
		// ((Pre_BookFragment) fPreBook).updateOnMapValues(null);
		// }
		super.onDetach();
	}

	public Bundle getBundle() {
		Bundle preBookDetails = new Bundle();
		preBookDetails.putParcelable("pbParcelable", pbParcelable);

		return preBookDetails;

	}

	public void updateArguments(PreBookParcelable pbParcelable) {
		valuesUpdated = true;
		this.pbParcelable = pbParcelable;

	}

	private void setTopDrawbleAndText(TextView textView, int topDrawableId,
			int textColorId) {
		textView.setCompoundDrawablesWithIntrinsicBounds(0, topDrawableId, 0, 0);
		textView.setTextColor(context.getResources().getColor(textColorId));
	}

	public void alertdialog(Context mContext) {
		TextView title = new TextView(mContext);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCustomTitle(title);
		builder.setMessage("£0.12 will be deducted from your account. Do you want to continue?");
		builder.setCancelable(false);

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
					AddNewCardTask task = new AddNewCardTask(context);
					task.handler = mHandler;
					task.execute("");
				} else {
					Toast.makeText(context, "No network connection.",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		AlertDialog alert = builder.show();
		TextView messageText = (TextView) alert
				.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER_VERTICAL);
		messageText.setTextColor(Color.BLACK);
		messageText.setTextSize(16);

	}

	void restoreLayout() {
		Log.i(getClass().getSimpleName(), "restoreLayout");
		int selectedDay = Integer.parseInt(pbParcelable.getPickUpDate()
				.substring(0, pbParcelable.getPickUpDate().indexOf("/")));
		int selectedMonth = Integer.parseInt(pbParcelable.getPickUpDate()
				.substring(pbParcelable.getPickUpDate().indexOf("/") + 1,
						pbParcelable.getPickUpDate().lastIndexOf("/"))) - 1;

		int selectedYear = Integer.parseInt(pbParcelable.getPickUpDate()
				.substring(pbParcelable.getPickUpDate().lastIndexOf("/") + 1,
						pbParcelable.getPickUpDate().length()));

		int hour = Integer.parseInt(pbParcelable.getPickUpTime()
				.substring(0, 2).trim());
		int minute = Integer.parseInt(pbParcelable.getPickUpTime()
				.substring(3, 5).trim());
		Calendar c = Calendar.getInstance();
		c.set(selectedYear, selectedMonth, selectedDay, hour, minute);
		Log.i(getClass().getSimpleName(), c + "");
		Calendar currentDateTime = Calendar.getInstance();
		if (currentDateTime.after(c)) {
			SimpleDateFormat sdf = new SimpleDateFormat("E dd LLL");
			pbParcelable.setPickUpDateToShow(sdf.format(new Date()));
			sdf = new SimpleDateFormat("dd/MM/yyyy");
			pbParcelable.setPickUpDate(sdf.format(new Date()));
			sdf = new SimpleDateFormat("HH:mm");
			pbParcelable.setPickUpTime(sdf.format(new Date()));
			Log.i(getClass().getSimpleName(), pbParcelable.getPickUpDate());
		}
		if (pbParcelable.getePaymentType() == EnumPaymentType.CASH)
			tv_cash.performClick();
		else if (pbParcelable.getePaymentType() == EnumPaymentType.BOTH)
			tv_card.performClick();
	}

	public void selectCard() {

		pbParcelable.setePaymentType(EnumPaymentType.BOTH);
		setTopDrawbleAndText(tv_card, R.drawable.card2selected,
				R.color.textview_selected);
		setTopDrawbleAndText(tv_cash, R.drawable.cash, R.color.textcolor_grey);
		rl_main_card.setVisibility(View.VISIBLE);
		Constant.flag_payment_method = true;
	}

	void selectCash() {
		pbParcelable.setePaymentType(EnumPaymentType.CASH);
		setTopDrawbleAndText(tv_cash, R.drawable.cashselected,
				R.color.textview_selected);
		setTopDrawbleAndText(tv_card, R.drawable.card2, R.color.textcolor_grey);
		rl_main_card.setVisibility(View.GONE);
		Constant.flag_payment_method = true;
	}

	public void updateValues(PreBookParcelable pbParcelable2) {
		pbParcelable = pbParcelable2;
		Log.i(getClass().getSimpleName(), "updateValues");

	}

	@Override
	public void onCustomBackPressed() {
		getParentFragment().getChildFragmentManager().popBackStack();
	}
}
