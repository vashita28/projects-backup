/*****************************************
 * Coded By- Manpreet
 * 
 * 
 ***********************************/

package com.example.cabapppassenger.fragments;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.adapter.CustomPlacesSimpleAdapter;
import com.example.cabapppassenger.adapter.PassengerCountSpinnerAdapter;
import com.example.cabapppassenger.listeners.PreBookListener;
import com.example.cabapppassenger.model.LocationParcelable;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.GetAddressTask;
import com.example.cabapppassenger.task.GetAutoPlacesTask;
import com.example.cabapppassenger.task.GetFixedPriceTask;
import com.example.cabapppassenger.task.GetLatLongTask;
import com.example.cabapppassenger.task.GetPincodeTask;
import com.example.cabapppassenger.task.PassengerCountTask;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.Util;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Pre_BookFragment extends RootFragment implements
		OnCustomBackPressedListener {

	public enum EnumLocations {
		PRESENTLOCATION, MAPLOCATION, DEFAULT, FAVOURITE, RECENT
	}

	public enum EnumWhen {
		ASAP, PREBOOK
	}

	public enum EnumPaymentType {
		CASH, BOTH
	}

	public enum EnumClickedFav {
		PICKUP, DROPOFF, NONE
	}

	/*
	 * Variables that use basic java datatypes
	 */
	Handler mHandler, mFixedPriceHandler, mPassengerCountHandler,
			pickUpLatLngHandler, dropOffLatLngHandler;
	Bundle bundleData;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";

	boolean valuesUpdated = false, doneClicked = false;
	String placeId = "";

	String[] passengersCount;

	int currentYear, currentMonth, currentDay;

	/*
	 * Variables that use basic java Android activity specific datatypes
	 */
	Context context;
	Resources res;

	/*
	 * Variables that use basic Android layout specific datatypes
	 */
	RelativeLayout rlAccessibilityOptions, rlDone;
	View vTopBar, vTopBarMaps;
	TextView tvPickUpAdd, tvPickUpLocation, tvPickUpOnMap, tvPickUpMyPlaces,
			tvDropOffAdd, tvDropOffLocation, tvDropOffOnMap, tvDropOffMyPlaces,
			tvAoWheelchairUser, tvAoHearingImpaired, tvAccessibilityOptions,
			tvComments, tvWhenTime, tvWhenDate, tvWhenAsap, tvWhenPreBook,
			tvPickUpRecent, tvDropOffRecent, tv_done;
	Spinner spinnerPassenger;
	AutoCompleteTextView atvPickUpAddress, atvDropOffAddress;
	EditText etPickUpPincode, etDropOffPincode, etComments;
	ImageView ivBack, iv_airport;
	public static ScrollView svMain;
	Handler addressHandler;

	/*
	 * Variables that use custom datatypes
	 */

	PreBookParcelable pbParcelable;

	Location currentLocation = null;
	PreBookListener listener;
	public static CustomPlacesSimpleAdapter atvAdapterResult;
	GetAutoPlacesTask placesTask;
	GetPincodeTask pincodeTask;

	Fragment previousFragment;

	public Pre_BookFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("Pre_BookFragment", "On Create");

		if (Util.getLocation(getActivity()) != null)
			currentLocation = Util.getLocation(getActivity());
		context = getActivity();
		shared_pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		res = getActivity().getResources();

		// coded by malik for recent nd fav handling
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg != null && msg.peekData() != null) {
					bundleData = (Bundle) msg.obj;

					if (bundleData.containsKey("DFavourites")) {
						FavoritesFragment edit = new FavoritesFragment(
								pbParcelable);
						FragmentManager fm = getParentFragment()
								.getChildFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Bundle from = new Bundle();
						from.putString("DFavourites", "");
						edit.setArguments(from);
						ft.replace(R.id.frame_container, edit,
								"favoritesFragment");
						ft.addToBackStack(null);
						ft.commit();
						bundleData.clear();
					}

					if (bundleData.containsKey("PFavourites")) {
						FavoritesFragment edit = new FavoritesFragment(
								pbParcelable);
						FragmentManager fm = getParentFragment()
								.getChildFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Bundle from = new Bundle();
						from.putString("PFavourites", "");
						edit.setArguments(from);
						ft.replace(R.id.frame_container, edit,
								"favoritesFragment");
						ft.addToBackStack("preBookFragment");
						ft.commit();
						bundleData.clear();
					}

					if (bundleData.containsKey("DRecent")) {
						RecentFragment edit = new RecentFragment(pbParcelable);
						FragmentManager fm = getParentFragment()
								.getChildFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Bundle from = new Bundle();
						from.putString("DRecent", "");
						edit.setArguments(from);
						ft.replace(R.id.frame_container, edit, "recentFragment");
						ft.addToBackStack("preBookFragment");
						ft.commit();
						bundleData.clear();
					}

					if (bundleData.containsKey("PRecent")) {
						RecentFragment edit = new RecentFragment(pbParcelable);
						FragmentManager fm = getParentFragment()
								.getChildFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Bundle from = new Bundle();
						from.putString("PRecent", "");
						edit.setArguments(from);
						ft.replace(R.id.frame_container, edit, "recentFragment");
						ft.addToBackStack("preBookFragment");
						ft.commit();
						bundleData.clear();
					}
					if (bundleData.containsKey("pickUpOnMap")) {
						// something going wrong here..on map values are not
						// coming
						Log.e("Log values in pickuponMap", pbParcelable
								.getMapPickUpLocation().getAddress1()
								.toString());
						OnMapFragment edit = new OnMapFragment(pbParcelable);
						FragmentManager fm = getParentFragment()
								.getChildFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Bundle from = new Bundle();
						from.putString("pickUpOnMap", "");
						edit.setArguments(from);
						ft.replace(R.id.frame_container, edit, "onMapFragment");
						ft.addToBackStack("preBookFragment");
						ft.commit();
						bundleData.clear();
					}
					if (bundleData.containsKey("dropOffOnMap")) {
						OnMapFragment edit = new OnMapFragment(pbParcelable);
						FragmentManager fm = getParentFragment()
								.getChildFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Bundle from = new Bundle();
						from.putString("dropOffOnMap", "");
						edit.setArguments(from);
						ft.replace(R.id.frame_container, edit, "onMapFragment");
						ft.addToBackStack("preBookFragment");
						ft.commit();
						bundleData.clear();
					}
					if (bundleData.containsKey("latitude")
							&& bundleData.containsKey("longitude")) {
						Double latitude, longitude;
						String address = null, pincode = null;
						latitude = bundleData.getDouble("latitude");
						longitude = bundleData.getDouble("longitude");

						if (bundleData.containsKey("address")) {
							address = bundleData.getString("address");
						}
						if (bundleData.containsKey("pincode")) {
							pincode = bundleData.getString("pincode");
						}
						/*
						 * if (true) {
						 * 
						 * if (address != null && latitude != null && longitude
						 * != null) { AddFavourites favourites = new
						 * AddFavourites( context, address, pincode, latitude,
						 * longitude); favourites .execute(Constant.passengerURL
						 * + "ws/v2/passenger/favourite/?accessToken=" +
						 * shared_pref.getString( "AccessToken", "")); } else {
						 * Toast.makeText( context,
						 * "Location not added as favourites, Please try again."
						 * , Toast.LENGTH_SHORT).show();
						 * 
						 * } } else if (true) { if (address != null && latitude
						 * != null && longitude != null) { AddFavourites
						 * favourites = new AddFavourites( context, address,
						 * pincode, latitude, longitude); favourites
						 * .execute(Constant.passengerURL +
						 * "ws/v2/passenger/favourite/?accessToken=" +
						 * shared_pref.getString( "AccessToken", "")); } else {
						 * Toast.makeText( context,
						 * "Location not added as favourites, Please try again."
						 * , Toast.LENGTH_SHORT).show();
						 * 
						 * } }
						 */

					}
				}
			}
		};

		mFixedPriceHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				navigate_bookingdetails();
			}
		};
		mPassengerCountHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				String maxPassenger, minPassenger;
				if (msg != null && msg.peekData() != null) {
					Bundle bundle_passenger_count = (Bundle) msg.obj;
					if (bundle_passenger_count
							.containsKey("passenger_count_min")
							&& bundle_passenger_count
									.containsKey("passenger_count_max")) {
						maxPassenger = bundle_passenger_count
								.getString("passenger_count_max");
						minPassenger = bundle_passenger_count
								.getString("passenger_count_min");

						if (Constant.arr_passengercount.size() > 0)
							Constant.arr_passengercount.clear();

						for (int i = Integer.parseInt(minPassenger.trim()); i <= Integer
								.parseInt(maxPassenger.trim()); i++) {
							// if (minPassenger.equals("4") && i == 4)
							// Constant.arr_passengercount.add("1-4");
							// else if (minPassenger.equals("5") && i == 5)
							// Constant.arr_passengercount.add("1-5");
							// else
							Constant.arr_passengercount.add("1-" + i);
						}

						spinnerPassenger
								.setAdapter(new PassengerCountSpinnerAdapter(
										context, R.layout.spinner_selector,
										Constant.arr_passengercount));

						Log.e("Passenger Count",
								"" + pbParcelable.getPassengerCount());
						spinnerPassenger.setSelection(pbParcelable
								.getPassengerCount());
					}
				}

			}
		};
		if (getArguments() != null) {
			savedInstanceState = getArguments();
			pbParcelable = (savedInstanceState.containsKey("pbParcelable") ? (PreBookParcelable) savedInstanceState
					.get("pbParcelable") : null);
			Log.e("Log values in pb getargs", pbParcelable
					.getMapPickUpLocation().getAddress1().toString());
			Log.d(getClass().getSimpleName(),
					"Passed bundle mapDropOffLocation"
							+ pbParcelable.getMapDropOffLocation()
									.getAddress1()
							+ "mapDropOffPincode "
							+ pbParcelable.getMapDropOffLocation().getPinCode()
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
							+ pbParcelable.geteWhen().name());

		}

		else {
			pbParcelable = new PreBookParcelable();
		}

		addressHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;
					pbParcelable
							.getPresentLocation()
							.setAddress1(
									bundleData.containsKey("currentaddress") ? bundleData
											.getString("currentaddress") : "");
					pbParcelable
							.getPresentLocation()
							.setPinCode(
									bundleData.containsKey("currentpincode") ? bundleData
											.getString("currentpincode") : "");
					Log.d("Pre_BookFragment", "Data From Handler"
							+ pbParcelable.getPresentLocation().getAddress1()
							+ "   "
							+ pbParcelable.getPresentLocation().getPinCode());

					/*
					 * pbParcelable.getPresentLocation().setAddress1(
					 * pbParcelable.getPresentLocation().getAddress1());
					 * pbParcelable.getPresentLocation().setPinCode(
					 * pbParcelable.getPresentLocation().getPinCode());
					 */

					pbParcelable.getMapPickUpLocation().setAddress1(
							pbParcelable.getPresentLocation().getAddress1());
					pbParcelable.getMapPickUpLocation().setPinCode(
							pbParcelable.getPresentLocation().getPinCode());

					if (((MainFragmentActivity) (getActivity())).nLastSelected == 1) {
						atvPickUpAddress.setText(pbParcelable
								.getPresentLocation().getAddress1());
						etPickUpPincode.setText(pbParcelable
								.getPresentLocation().getPinCode());
					}

					/*
					 * atvDropOffAddress.setText(pbParcelable.getMapDropOffLocation
					 * () .getAddress1());
					 * etDropOffPincode.setText(pbParcelable.
					 * getMapDropOffLocation() .getPinCode());
					 */

				}

			}

		};
		// if (((MainFragmentActivity) getActivity()).nLastSelected != 0) {
		//
		// if (Util.getLocation(getActivity()) != null)
		// getAddress(currentLocation.getLatitude(),
		// currentLocation.getLongitude());
		//
		// } else {
		// if (pbParcelable.getDefaultDropOffLocation().getAddress1()
		// .equals("")) {
		// InputMethodManager imm = (InputMethodManager) context
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		// }
		// }
		if (!pbParcelable.isValuesUpdated()) {

			if (shared_pref.getString("WheelChair", "") != null
					&& shared_pref.getString("WheelChair", "").equals("1")) {

				pbParcelable.setWheelChairSelected(true);

			} else {

				pbParcelable.setWheelChairSelected(false);

			}

			if (shared_pref.getString("HearingImpaired", "") != null
					&& shared_pref.getString("HearingImpaired", "").equals(
							"true")) {
				pbParcelable.setHearingImpairedSelected(true);

			} else {

				pbParcelable.setHearingImpairedSelected(false);
			}
		}

		// MainFragmentActivity.bIsQuitDialogToBeShown = true;

	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.i("Pre_BookFragment", "OnCreateView");

		View rootView = inflater.inflate(R.layout.fragment_prebook, container,
				false);
		super.initWidgets(rootView, this.getClass().getName());

		listener = new PreBookListener(context, rootView, pbParcelable,
				mHandler);

		/*
		 * Find all the controls from rootView
		 */
		rlAccessibilityOptions = (RelativeLayout) rootView
				.findViewById(R.id.rl_ao);
		ivBack = (ImageView) rootView.findViewById(R.id.ivBack);
		atvPickUpAddress = (AutoCompleteTextView) rootView
				.findViewById(R.id.atv_pick_up_address);

		tv_done = (TextView) rootView.findViewById(R.id.tv_done);
		atvDropOffAddress = (AutoCompleteTextView) rootView
				.findViewById(R.id.atv_drop_off_address);
		tvPickUpAdd = (TextView) rootView
				.findViewById(R.id.tv_pick_up_top_right);
		tvPickUpLocation = (TextView) rootView
				.findViewById(R.id.tv_pick_up_location);
		tvPickUpRecent = (TextView) rootView
				.findViewById(R.id.tv_pick_up_recent);
		tvDropOffRecent = (TextView) rootView
				.findViewById(R.id.tv_drop_off_recent);
		tvPickUpOnMap = (TextView) rootView.findViewById(R.id.tv_pick_up_onmap);
		tvPickUpMyPlaces = (TextView) rootView
				.findViewById(R.id.tv_pick_up_favorites);
		tvDropOffAdd = (TextView) rootView
				.findViewById(R.id.tv_drop_off_top_right);
		tvDropOffLocation = (TextView) rootView
				.findViewById(R.id.tv_drop_off_location);
		tvDropOffOnMap = (TextView) rootView
				.findViewById(R.id.tv_drop_off_onmap);
		tvDropOffMyPlaces = (TextView) rootView
				.findViewById(R.id.tv_drop_off_favorites);
		tvAoWheelchairUser = (TextView) rootView
				.findViewById(R.id.tv_ao_wheelchair_user);
		tvAoHearingImpaired = (TextView) rootView
				.findViewById(R.id.tv_ao_hearing_impaired);
		tvAccessibilityOptions = (TextView) rootView
				.findViewById(R.id.tv_ao_header);
		tvComments = (TextView) rootView.findViewById(R.id.tv_comments);
		spinnerPassenger = (Spinner) rootView
				.findViewById(R.id.spinner_passenger);
		etPickUpPincode = (EditText) rootView
				.findViewById(R.id.et_pick_up_pincode);
		etDropOffPincode = (EditText) rootView
				.findViewById(R.id.et_drop_off_pincode);
		etComments = (EditText) rootView.findViewById(R.id.et_comments);
		etComments.setVisibility(View.GONE);
		rlDone = (RelativeLayout) rootView.findViewById(R.id.rl_done);
		iv_airport = (ImageView) rootView.findViewById(R.id.iv_airport);
		vTopBar = (View) rootView.findViewById(R.id.inc_topbar);
		vTopBarMaps = (View) rootView.findViewById(R.id.inc_header);
		svMain = (ScrollView) rootView.findViewById(R.id.sv_main);

		tvWhenDate = (TextView) rootView.findViewById(R.id.tv_w_date);
		tvWhenTime = (TextView) rootView.findViewById(R.id.tv_w_time);
		tvWhenAsap = (TextView) rootView.findViewById(R.id.tv_w_asap);
		tvWhenPreBook = (TextView) rootView.findViewById(R.id.tv_w_prebook);
		/*
		 * Set on click listener on required controls
		 */
		tvAccessibilityOptions.setOnClickListener(listener);
		tvPickUpOnMap.setOnClickListener(listener);
		tvPickUpAdd.setOnClickListener(listener);
		tvPickUpLocation.setOnClickListener(listener);
		tvPickUpMyPlaces.setOnClickListener(listener);
		tvDropOffRecent.setOnClickListener(listener);
		tvPickUpRecent.setOnClickListener(listener);
		tvDropOffMyPlaces.setOnClickListener(listener);
		tvDropOffAdd.setOnClickListener(listener);
		tvDropOffLocation.setOnClickListener(listener);
		tvDropOffOnMap.setOnClickListener(listener);
		tvComments.setOnClickListener(listener);
		tvAoWheelchairUser.setOnClickListener(listener);
		tvAoHearingImpaired.setOnClickListener(listener);
		tvWhenAsap.setOnClickListener(listener);
		tvWhenPreBook.setOnClickListener(listener);

		tvWhenDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDatePickerFragment();

			}
		});

		tvWhenTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTimePickerFragment();

			}
		});

		tvWhenDate.setText(pbParcelable.getPickUpDateToShow());
		tvWhenTime.setText(pbParcelable.getPickUpTime());
		etComments.setText(pbParcelable.getComment());

		// passengersCount = res.getStringArray(R.array.passengers_count_4);
		// spinnerPassenger.setAdapter(new PassengerCountSpinnerAdapter(context,
		// R.layout.spinner_selector, passengersCount));
		// spinnerPassenger.setSelection(pbParcelable.getPassengerCount());
		iv_airport.setVisibility(View.GONE);
		rootView.findViewById(R.id.rl_prebook_fragment).requestFocus();

		/* present user location from Map fragment */

		pickUpLatLngHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					pbParcelable.getDefaultPickUpLocation().setLatitude(
							bundleData.getDouble("latitude"));
					pbParcelable.getDefaultPickUpLocation().setLongitude(
							bundleData.getDouble("longitude"));

					if (doneClicked) {
						doneClicked = false;
						if (pbParcelable.geteDropOffSelected() == EnumLocations.DEFAULT) {
							if ((pbParcelable.getDefaultDropOffLocation()
									.getLatitude() == 0 && pbParcelable
									.getDefaultDropOffLocation().getLongitude() == 0)
									&& !pbParcelable
											.getDefaultDropOffLocation()
											.getAddress1().trim()
											.equalsIgnoreCase("as directed"))

							{
								GetLatLongTask task = new GetLatLongTask(
										context, pbParcelable
												.getDefaultDropOffLocation()
												.getAddress1(), pbParcelable
												.getDefaultDropOffLocation()
												.getPinCode(),
										dropOffLatLngHandler);
								doneClicked = true;
								task.execute("");
							} else {
								updateFixedPrice();
							}

						} else {

							updateFixedPrice();
						}

					}
				}

			}
		};

		dropOffLatLngHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					pbParcelable.getDefaultDropOffLocation().setLatitude(
							bundleData.getDouble("latitude"));
					pbParcelable.getDefaultDropOffLocation().setLongitude(
							bundleData.getDouble("longitude"));
					if (bundleData.containsKey("address")
							&& bundleData.containsKey("pincode")) {
						pbParcelable.getDefaultDropOffLocation().setAddress1(
								bundleData.getString("address"));
						pbParcelable.getDefaultDropOffLocation().setPinCode(
								bundleData.getString("pincode"));
					}
					if (doneClicked) {
						doneClicked = false;

						if (pbParcelable.getePickUpSelected() == EnumLocations.DEFAULT) {
							if (pbParcelable.getDefaultPickUpLocation()
									.getLatitude() == 0
									&& pbParcelable.getDefaultPickUpLocation()
											.getLongitude() == 0)

							{
								GetLatLongTask task = new GetLatLongTask(
										context, pbParcelable
												.getDefaultPickUpLocation()
												.getAddress1(), pbParcelable
												.getDefaultPickUpLocation()
												.getPinCode(),
										dropOffLatLngHandler);
								doneClicked = true;
								task.execute("");
							} else {
								updateFixedPrice();
							}

						} else {

							updateFixedPrice();
						}

					}
				}

			}
		};

		if (((MainFragmentActivity) getActivity()).nLastSelected != 0) {

			if (Util.getLocation(getActivity()) != null)
				getAddress(currentLocation.getLatitude(),
						currentLocation.getLongitude());

		} else {
			if (pbParcelable.geteDropOffSelected()
					.equals(EnumLocations.DEFAULT)) {
				if (pbParcelable.getDefaultDropOffLocation().getAddress1()
						.equals("")) {
					// InputMethodManager imm = (InputMethodManager) context
					// .getSystemService(Context.INPUT_METHOD_SERVICE);
					// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
					// atvDropOffAddress.setFocusable(true);
					// atvDropOffAddress.requestFocus();
					new Handler().postDelayed(new Runnable() {

						public void run() {
							atvDropOffAddress.dispatchTouchEvent(MotionEvent
									.obtain(SystemClock.uptimeMillis(),
											SystemClock.uptimeMillis(),
											MotionEvent.ACTION_DOWN, 0, 0, 0));
							atvDropOffAddress.dispatchTouchEvent(MotionEvent
									.obtain(SystemClock.uptimeMillis(),
											SystemClock.uptimeMillis(),
											MotionEvent.ACTION_UP, 0, 0, 0));

						}
					}, 200);
				}
			}
		}
		/*
		 * EditText field for pickup and dropoff pincode
		 */
		etPickUpPincode.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0)
					etPickUpPincode.setSelection(etPickUpPincode.length(),
							etPickUpPincode.length());
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (pbParcelable.getePickUpSelected() == EnumLocations.DEFAULT
						&& !s.toString().equals(
								pbParcelable.getDefaultPickUpLocation()
										.getPinCode())) {
					pbParcelable
							.setDefaultPickUpLocation(new LocationParcelable());
					pbParcelable.getDefaultPickUpLocation().setPinCode(
							s.toString());
					pbParcelable.getDefaultPickUpLocation().setAddress1(
							atvPickUpAddress.getText().toString());
				}

				if (pbParcelable.getePickUpSelected() == EnumLocations.MAPLOCATION
						&& !s.toString().equals(
								pbParcelable.getMapPickUpLocation()
										.getPinCode())) {
					pbParcelable.setMapPickUpLocation(new LocationParcelable());
					pbParcelable.getMapPickUpLocation()
							.setPinCode(s.toString());
					pbParcelable.getMapPickUpLocation().setAddress1(
							atvPickUpAddress.getText().toString());
				}

			}

		});

		etDropOffPincode.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0)
					etDropOffPincode.setSelection(etDropOffPincode.length(),
							etDropOffPincode.length());
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (pbParcelable.geteDropOffSelected() == EnumLocations.DEFAULT
						&& !s.toString().equals(
								pbParcelable.getDefaultDropOffLocation()
										.getPinCode())) {
					pbParcelable
							.setDefaultDropOffLocation(new LocationParcelable());
					pbParcelable.getDefaultDropOffLocation().setPinCode(
							s.toString());
					pbParcelable.getDefaultDropOffLocation().setAddress1(
							atvDropOffAddress.getText().toString());
				}

			}

		});

		/* AutoTextView for pickup point location */

		atvPickUpAddress.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					LocationParcelable[] selectedValues = listener
							.getSelectedLocationValues(pbParcelable);

					pbParcelable.getDefaultPickUpLocation().setAddress1(
							selectedValues[0].getAddress1());
					pbParcelable.getDefaultPickUpLocation().setPinCode(
							selectedValues[0].getPinCode());
					pbParcelable.getDefaultPickUpLocation().setLatitude(
							selectedValues[0].getLatitude());
					pbParcelable.getDefaultPickUpLocation().setLongitude(
							selectedValues[0].getLongitude());

					listener.setPickUpSelectionToDefault();

				}

			}
		});
		atvPickUpAddress.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> selected = (HashMap<String, String>) parent
						.getItemAtPosition(position);

				pbParcelable.getDefaultPickUpLocation().setAddress1(
						selected.get("description"));

				placeId = selected.get("placeId");
				pincodeTask = new GetPincodeTask(getActivity(), placeId,
						"atvPickUpPlaces", listener.pickUpLatLngHandler,
						listener.pickupPincodeHandler);
				pincodeTask.execute();
				etPickUpPincode.setText("");

			}

		});

		atvPickUpAddress.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				GetAutoPlacesTask placesTask = new GetAutoPlacesTask(context);

				placesTask.execute(s.toString());
				// imgbtn_add.setBackgroundResource(0);
				// imgbtn_add.setBackgroundResource(R.drawable.favorites);
				checkfav(tvPickUpAdd, s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (pbParcelable.getePickUpSelected() == EnumLocations.DEFAULT
						&& !s.toString().equals(
								pbParcelable.getDefaultPickUpLocation()
										.getAddress1())) {
					pbParcelable
							.setDefaultPickUpLocation(new LocationParcelable());
					pbParcelable.getDefaultPickUpLocation().setAddress1(
							s.toString());
					pbParcelable.getDefaultPickUpLocation().setPinCode(
							etPickUpPincode.getText().toString());
				}
				if (pbParcelable.getePickUpSelected() == EnumLocations.MAPLOCATION
						&& !s.toString().equals(
								pbParcelable.getMapPickUpLocation()
										.getAddress1())) {
					pbParcelable.setMapPickUpLocation(new LocationParcelable());
					pbParcelable.getMapPickUpLocation().setAddress1(
							s.toString());
					pbParcelable.getMapPickUpLocation().setPinCode(
							etPickUpPincode.getText().toString());
				}
			}

		});
		etPickUpPincode.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					LocationParcelable[] selectedValues = listener
							.getSelectedLocationValues(pbParcelable);

					pbParcelable.getDefaultPickUpLocation().setAddress1(
							selectedValues[0].getAddress1());
					pbParcelable.getDefaultPickUpLocation().setPinCode(
							selectedValues[0].getPinCode());
					pbParcelable.getDefaultPickUpLocation().setLatitude(
							selectedValues[0].getLatitude());
					pbParcelable.getDefaultPickUpLocation().setLongitude(
							selectedValues[0].getLongitude());

					listener.setPickUpSelectionToDefault();
				}
			}
		});

		/* AutoTextView for drop off point location */

		atvDropOffAddress.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					LocationParcelable[] selectedValues = listener
							.getSelectedLocationValues(pbParcelable);

					pbParcelable.getDefaultDropOffLocation().setAddress1(
							selectedValues[1].getAddress1());
					pbParcelable.getDefaultDropOffLocation().setPinCode(
							selectedValues[1].getPinCode());
					pbParcelable.getDefaultDropOffLocation().setLatitude(
							selectedValues[1].getLatitude());
					pbParcelable.getDefaultDropOffLocation().setLongitude(
							selectedValues[1].getLongitude());

					listener.setDropOffSelectionToDefault();

				}

			}
		});
		atvDropOffAddress.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap<String, String> selected = (HashMap<String, String>) parent
						.getItemAtPosition(position);
				placeId = selected.get("placeId");
				pbParcelable.getDefaultDropOffLocation().setAddress1(
						selected.get("description"));

				GetPincodeTask pincodeTask = new GetPincodeTask(context,
						placeId, "atvDropOffPlaces", dropOffLatLngHandler,
						listener.dropOffPincodeHandler);
				pincodeTask.execute();
				etDropOffPincode.setText("");

			}

		});

		atvDropOffAddress.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				GetAutoPlacesTask placesTask = new GetAutoPlacesTask(context);

				placesTask.execute(s.toString());
				checkfav(tvDropOffAdd, s.toString());
				// imgbtn_add_drop.setBackgroundResource(0);
				// imgbtn_add_drop.setBackgroundResource(R.drawable.favorites);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (pbParcelable.geteDropOffSelected() == EnumLocations.DEFAULT
						&& !s.toString().equals(
								pbParcelable.getDefaultDropOffLocation()
										.getAddress1())) {
					pbParcelable
							.setDefaultDropOffLocation(new LocationParcelable());
					pbParcelable.getDefaultDropOffLocation().setAddress1(
							s.toString());
					pbParcelable.getDefaultDropOffLocation().setPinCode(
							etDropOffPincode.getText().toString());
				}
			}

		});
		etDropOffPincode.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					LocationParcelable[] selectedValues = listener
							.getSelectedLocationValues(pbParcelable);

					pbParcelable.getDefaultDropOffLocation().setAddress1(
							selectedValues[1].getAddress1());
					pbParcelable.getDefaultDropOffLocation().setPinCode(
							selectedValues[1].getPinCode());
					pbParcelable.getDefaultDropOffLocation().setLatitude(
							selectedValues[1].getLatitude());
					pbParcelable.getDefaultDropOffLocation().setLongitude(
							selectedValues[1].getLongitude());

					listener.setDropOffSelectionToDefault();

				}

			}
		});

		spinnerPassenger
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						pbParcelable.setPassengerCount(position);

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}

				});
		// Log.e("Last Selected", ""+((MainFragmentActivity)
		// getActivity()).nLastSelected);
		if (((MainFragmentActivity) getActivity()).nLastSelected == 0
				|| ((MainFragmentActivity) getActivity()).nLastSelected == 2) {
			vTopBar.setVisibility(View.GONE);
			vTopBarMaps.setVisibility(View.VISIBLE);
			svMain.layout(0, 0, 0, R.id.inc_header);
			if (MainFragmentActivity.slidingMenu != null)
				MainFragmentActivity.slidingMenu
						.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

			passengerCount();
			ivBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					pbParcelable.setBackPressed(true);
					hideKeybord(v);
					getParentFragment().getChildFragmentManager()
							.popBackStack();

				}
			});
		} else {
			// Constant.flag_fromslider_favourites = true;
			vTopBar.setVisibility(View.VISIBLE);
			vTopBarMaps.setVisibility(View.GONE);
			if (MainFragmentActivity.slidingMenu != null)
				MainFragmentActivity.slidingMenu
						.setTouchModeAbove(SlidingMenu.SLIDING_WINDOW);

		}

		rlDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeybord(v);
				// Fragment fBookingDetails = new BookingDetailsFragment();

				if (pbParcelable.geteWhen() == EnumWhen.PREBOOK) {
					// 23:00
					Calendar c = Calendar.getInstance();
					int hour = Integer.parseInt(pbParcelable.getPickUpTime()
							.substring(0, 2).trim());
					int minute = Integer.parseInt(pbParcelable.getPickUpTime()
							.substring(3, 5).trim());

					Log.i(getClass().getSimpleName(),
							pbParcelable.getPickUpDate());
					int selectedDay = Integer.parseInt(pbParcelable
							.getPickUpDate().substring(0,
									pbParcelable.getPickUpDate().indexOf("/")));
					int selectedMonth = Integer
							.parseInt(pbParcelable.getPickUpDate()
									.substring(
											pbParcelable.getPickUpDate()
													.indexOf("/") + 1,
											pbParcelable.getPickUpDate()
													.lastIndexOf("/"))) - 1;
					if (selectedMonth == -1) {
						selectedMonth = 11;
					}
					final int selectedYear = Integer.parseInt(pbParcelable
							.getPickUpDate().substring(
									pbParcelable.getPickUpDate().lastIndexOf(
											"/") + 1,
									pbParcelable.getPickUpDate().length()));
					Log.i(getClass().getSimpleName(), "selectedDay "
							+ selectedDay + " selectedYear " + selectedYear
							+ " selectedMonth " + selectedMonth);

					c.set(selectedYear, selectedMonth, selectedDay, hour,
							minute, 0);
					Date a = c.getTime();
					c = Calendar.getInstance();
					Date b = c.getTime();
					Log.i("Values of a and b", "" + a + " " + b);
					Log.i("Minus values", ""
							+ ((a.getTime() - b.getTime()) / 60000));
					if (((a.getTime() - b.getTime()) / 60000) > shared_pref
							.getLong("DropOffAddress_Time", 0)
							&& atvDropOffAddress.getText().toString()
									.equalsIgnoreCase("as directed")
							|| atvDropOffAddress.getText().toString().length() <= 0) {
						Constant.alertdialog("select_dropoff", mContext);
						return;
					}

					/*
					 * SimpleDateFormat mSDF = new SimpleDateFormat("HH:mm");
					 * mSDF.format(c.getTime());
					 */

				}

				FragmentManager fragmentManager = getParentFragment()
						.getChildFragmentManager();

				pbParcelable.setValuesUpdated(true);
				pbParcelable.setComment(etComments.getText().toString());

				doneClicked = true;
				if (tvWhenAsap.getCompoundDrawables()[1].getConstantState()
						.equals((context.getResources()
								.getDrawable(R.drawable.asapselected))
								.getConstantState()))
					pbParcelable.seteWhen(EnumWhen.ASAP);
				else
					pbParcelable.seteWhen(EnumWhen.PREBOOK);

				if (tvAoWheelchairUser.getCompoundDrawables()[1]
						.getConstantState()
						.equals((context.getResources()
								.getDrawable(R.drawable.wheelchair_userselected))
								.getConstantState()))
					pbParcelable.setWheelChairSelected(true);
				else
					pbParcelable.setWheelChairSelected(false);

				if (tvAoHearingImpaired.getCompoundDrawables()[1]
						.getConstantState()
						.equals((context.getResources()
								.getDrawable(R.drawable.hearing_impairedselected))
								.getConstantState()))
					pbParcelable.setHearingImpairedSelected(true);
				else
					pbParcelable.setHearingImpairedSelected(false);

				if (etComments.getVisibility() == View.VISIBLE)
					pbParcelable.setCommentExpanded(true);
				else
					pbParcelable.setCommentExpanded(false);

				if (rlAccessibilityOptions.getVisibility() == View.VISIBLE)
					pbParcelable.setAccessibiltyExpanded(true);
				else
					pbParcelable.setAccessibiltyExpanded(false);

				if (pbParcelable.getePickUpSelected() == EnumLocations.DEFAULT
						&& pbParcelable.getDefaultPickUpLocation()
								.getLatitude() == 0
						&& pbParcelable.getDefaultPickUpLocation()
								.getLongitude() == 0)

				{
					GetLatLongTask task = new GetLatLongTask(context,
							pbParcelable.getDefaultPickUpLocation()
									.getAddress1(), pbParcelable
									.getDefaultPickUpLocation().getPinCode(),
							pickUpLatLngHandler);
					task.execute("");
				}

				else if (pbParcelable.geteDropOffSelected() == EnumLocations.DEFAULT
						&& pbParcelable.getDefaultDropOffLocation()
								.getLatitude() == 0
						&& pbParcelable.getDefaultDropOffLocation()
								.getLongitude() == 0
						&& !pbParcelable.getDefaultDropOffLocation()
								.getAddress1().trim()
								.equalsIgnoreCase("as directed"))

				{
					GetLatLongTask task = new GetLatLongTask(context,
							pbParcelable.getDefaultDropOffLocation()
									.getAddress1(), pbParcelable
									.getDefaultDropOffLocation().getPinCode(),
							dropOffLatLngHandler);
					task.execute("");
				}

				else {
					updateFixedPrice();
				}
				/*
				 * if (pbParcelable.getPresentLocation().getAddress1() != null
				 * && pbParcelable.getMapDropOffLocation() != null &&
				 * pbParcelable.getFavouriteDropOffLocation() != null &&
				 * pbParcelable.getRecentDropOffLocation() != null) { if
				 * (pbParcelable.getPresentLocation().getAddress1()
				 * .toLowerCase().equals("as directed") ||
				 * pbParcelable.getMapDropOffLocation()
				 * .getAddress1().toLowerCase() .equals("as directed") ||
				 * pbParcelable.getFavouriteDropOffLocation()
				 * .getAddress1().toLowerCase() .equals("as directed") ||
				 * pbParcelable.getRecentDropOffLocation()
				 * .getAddress1().toLowerCase() .equals("as directed")) {
				 * navigate_bookingdetails(); } else updateFixedPrice();
				 */

			}

		});

		passengerCount();

		return rootView;

	}

	protected void updateFixedPrice() {

		LocationParcelable[] selectedLocation = listener
				.getSelectedLocationValues(pbParcelable);

		if (!selectedLocation[1].getAddress1().trim()
				.equalsIgnoreCase("as directed")) {
			GetFixedPriceTask getFixedPriceTask = new GetFixedPriceTask(
					context, Constant.passengerURL
							+ "ws/v2/passenger/fixedprice/?accessToken="
							+ shared_pref.getString("AccessToken", ""),
					selectedLocation[0].getLongitude(),
					selectedLocation[0].getLatitude(),
					selectedLocation[1].getLongitude(),
					selectedLocation[1].getLatitude(), pbParcelable,
					mFixedPriceHandler);
			getFixedPriceTask.execute();
		} else {
			navigate_bookingdetails();
		}

	}

	public void passengerCount() {

		if (pbParcelable.getePickUpSelected() == EnumLocations.PRESENTLOCATION) {
			PassengerCountTask getPassengerCountTask = new PassengerCountTask(
					context, mPassengerCountHandler);
			getPassengerCountTask.latitude = pbParcelable.getPresentLocation()
					.getLatitude();
			getPassengerCountTask.longitude = pbParcelable.getPresentLocation()
					.getLongitude();
			getPassengerCountTask.execute(Constant.passengerURL
					+ "/ws/v2/passenger/minimumpassenger/?accessToken="
					+ shared_pref.getString("AccessToken", ""));

		} else if (pbParcelable.getePickUpSelected() == EnumLocations.MAPLOCATION) {
			PassengerCountTask getPassengerCountTask = new PassengerCountTask(
					context, mPassengerCountHandler);
			getPassengerCountTask.latitude = pbParcelable
					.getMapPickUpLocation().getLatitude();
			getPassengerCountTask.longitude = pbParcelable
					.getMapPickUpLocation().getLongitude();
			getPassengerCountTask.execute(Constant.passengerURL
					+ "/ws/v2/passenger/minimumpassenger/?accessToken="
					+ shared_pref.getString("AccessToken", ""));
		}

		else if (pbParcelable.getePickUpSelected() == EnumLocations.FAVOURITE) {
			PassengerCountTask getPassengerCountTask = new PassengerCountTask(
					context, mPassengerCountHandler);
			getPassengerCountTask.latitude = pbParcelable
					.getFavouritePickUpLocation().getLatitude();
			getPassengerCountTask.longitude = pbParcelable
					.getFavouritePickUpLocation().getLongitude();
			getPassengerCountTask.execute(Constant.passengerURL
					+ "/ws/v2/passenger/minimumpassenger/?accessToken="
					+ shared_pref.getString("AccessToken", ""));
		}

		else if (pbParcelable.getePickUpSelected() == EnumLocations.RECENT) {
			PassengerCountTask getPassengerCountTask = new PassengerCountTask(
					context, mPassengerCountHandler);
			getPassengerCountTask.latitude = pbParcelable
					.getRecentPickUpLocation().getLatitude();
			getPassengerCountTask.longitude = pbParcelable
					.getRecentPickUpLocation().getLongitude();
			getPassengerCountTask.execute(Constant.passengerURL
					+ "/ws/v2/passenger/minimumpassenger/?accessToken="
					+ shared_pref.getString("AccessToken", ""));
		}

	}

	public Bundle getBundle() {
		Bundle preBookDetails = new Bundle();
		preBookDetails.putParcelable("pbParcelable", pbParcelable);

		return preBookDetails;

	}

	public void restoreLayout() {
		Log.i(getClass().getSimpleName(), "restoreLayout");
		int selectedDay = Integer.parseInt(pbParcelable.getPickUpDate()
				.substring(0, pbParcelable.getPickUpDate().indexOf("/")));
		int selectedMonth = Integer.parseInt(pbParcelable.getPickUpDate()
				.substring(pbParcelable.getPickUpDate().indexOf("/") + 1,
						pbParcelable.getPickUpDate().lastIndexOf("/"))) - 1;
		if (selectedMonth == -1) {
			selectedMonth = 0;
		}
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
			tvWhenTime.setText(pbParcelable.getPickUpTime());
			tvWhenDate.setText(pbParcelable.getPickUpDateToShow());
			Log.i(getClass().getSimpleName(), pbParcelable.getPickUpDate());
		}

		etComments.setText(pbParcelable.getComment());

		Log.e("Passenger Count", "" + pbParcelable.getPassengerCount());
		if (Constant.arr_passengercount != null
				&& Constant.arr_passengercount.size() > 0
				&& pbParcelable.getPassengerCount() != -1) {
			spinnerPassenger.setAdapter(new PassengerCountSpinnerAdapter(
					context, R.layout.spinner_selector,
					Constant.arr_passengercount));

			spinnerPassenger.setSelection(pbParcelable.getPassengerCount());
		}

		if (pbParcelable.getePickUpSelected() == EnumLocations.MAPLOCATION) {
			if (pbParcelable.getPresentLocation().getAddress1()
					.matches(pbParcelable.getMapPickUpLocation().getAddress1())) {

				tvPickUpLocation.performClick();
			} else {
				listener.programmaticPickUpOnMapClick = true;
				tvPickUpOnMap.performClick();
			}

		} else if (pbParcelable.getePickUpSelected() == EnumLocations.PRESENTLOCATION) {
			tvPickUpLocation.performClick();
		}

		else if (pbParcelable.getePickUpSelected() == EnumLocations.RECENT) {

			listener.programmaticPickUpRecentClick = true;
			tvPickUpRecent.performClick();
		}

		else if (pbParcelable.getePickUpSelected() == EnumLocations.FAVOURITE) {
			listener.programmaticPickUpFavClick = true;
			tvPickUpMyPlaces.performClick();

		} else if (pbParcelable.getePickUpSelected() == EnumLocations.DEFAULT) {

			atvPickUpAddress.setText(pbParcelable.getDefaultPickUpLocation()
					.getAddress1());
			etPickUpPincode.setText(pbParcelable.getDefaultPickUpLocation()
					.getPinCode());
			listener.setPickUpSelectionToDefault();

		}

		if (pbParcelable.geteDropOffSelected() == EnumLocations.MAPLOCATION) {
			listener.programmaticDropOffOnMapClick = true;
			tvDropOffOnMap.performClick();
		}

		else if (pbParcelable.geteDropOffSelected() == EnumLocations.PRESENTLOCATION) {
			tvDropOffLocation.performClick();
		}

		else if (pbParcelable.geteDropOffSelected() == EnumLocations.RECENT) {

			listener.programmaticDropOffRecentClick = true;
			tvDropOffRecent.performClick();

		} else if (pbParcelable.geteDropOffSelected() == EnumLocations.DEFAULT) {

			atvDropOffAddress.setText(pbParcelable.getDefaultDropOffLocation()
					.getAddress1());
			etDropOffPincode.setText(pbParcelable.getDefaultDropOffLocation()
					.getPinCode());

			listener.setDropOffSelectionToDefault();
		}

		else if (pbParcelable.geteDropOffSelected() == EnumLocations.FAVOURITE) {

			listener.programmaticDropOffFavClick = true;
			tvDropOffMyPlaces.performClick();

		}

		if (pbParcelable.isAccessibiltyExpanded()) {
			tvAccessibilityOptions.performClick();
		}
		if (pbParcelable.isCommentExpanded()) {
			etComments.performClick();
		}

		if (pbParcelable.isWheelChairSelected()) {
			tvAoWheelchairUser.performClick();
		}

		if (pbParcelable.isHearingImpairedSelected()) {
			tvAoHearingImpaired.performClick();
		}
		if (pbParcelable.geteWhen() == EnumWhen.ASAP) {
			tvWhenAsap.performClick();
		} else {
			tvWhenPreBook.performClick();
		}

	}

	public void updateOnMapValues(PreBookParcelable pbParcelable) {

		this.pbParcelable = pbParcelable;
		Log.e("Before Updating values", "" + valuesUpdated);
		valuesUpdated = true;
		Log.e("After Updating values", "" + valuesUpdated);
	}

	public void showDatePickerFragment() {
		DatePickerFragment datePicker = new DatePickerFragment(pbParcelable,
				tvWhenTime);
		datePicker.show(getParentFragment().getChildFragmentManager(),
				"datePicker");
	}

	public void showTimePickerFragment() {
		TimePickerFragment timePicker = new TimePickerFragment(pbParcelable);
		timePicker.show(getParentFragment().getChildFragmentManager(),
				"timePicker");
	}

	public void getAddress(double latitude, double longitude) {
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			GetAddressTask task = new GetAddressTask(getActivity());
			task.latitude = latitude;
			task.longitude = longitude;
			if (pbParcelable.getPresentLocation().getLatitude() == 0) {
				pbParcelable.getPresentLocation().setLatitude(latitude);
				pbParcelable.getPresentLocation().setLongitude(longitude);
			}
			pbParcelable.getMapPickUpLocation().setLatitude(latitude);
			pbParcelable.getMapPickUpLocation().setLongitude(longitude);

			task.flag_hail = false;
			task.mHandler = addressHandler;
			task.execute("");
		} else {
			Toast.makeText(getActivity(), "No network connection",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub

		super.onDetach();
	}

	@Override
	public void onResume() {
		Log.i("Pre_BookFragment", "On Resume");

		Log.e("Updating values in onresume", "" + valuesUpdated);
		// if (MainFragmentActivity.iFromScreen == 0 && valuesUpdated != true)
		if (EnumLocations.FAVOURITE == pbParcelable.getePickUpSelected()) {
			atvPickUpAddress.setText(pbParcelable.getFavouritePickUpLocation()
					.getAddress1());
		}
		if (EnumLocations.FAVOURITE == pbParcelable.geteDropOffSelected()) {
			atvDropOffAddress.setText(pbParcelable
					.getFavouriteDropOffLocation().getAddress1());
		}
		/* listener.eClickedFav = EnumClickedFav.NONE; */
		if (Constant.flag_bookingdetails_edit) {
			tv_done.setText("Confirm");
			Constant.flag_bookingdetails_edit = false;
		} else
			tv_done.setText("Done");

		restoreLayout();
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);

		super.onResume();

	}

	@Override
	public void onPause() {

		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

	}

	public void navigate_bookingdetails() {
		Fragment fBookingDetails;

		FragmentTransaction ft;
		FragmentManager fragmentManager = getParentFragment()
				.getChildFragmentManager();
		fBookingDetails = getParentFragment().getChildFragmentManager()
				.findFragmentByTag("bookingDetailsFragment");
		if (fBookingDetails != null) {
			((BookingDetailsFragment) fBookingDetails)
					.updateArguments(pbParcelable);
			getParentFragment().getChildFragmentManager()
					.popBackStackImmediate();

		} else {
			fBookingDetails = new BookingDetailsFragment();
			ft = fragmentManager.beginTransaction();
			ft.replace(R.id.frame_container, fBookingDetails,
					"bookingDetailsFragment");
			fBookingDetails.setArguments(getBundle());
			ft.addToBackStack("preBookFragment");
			ft.commit();

		}

	}

	private void checkfav(TextView tvAdd, String address) {
		// TODO Auto-generated method stub
		Log.e("Arr size", "" + Constant.arr_favourites.size());
		if (Constant.arr_favourites.size() > 0) {

			for (int i = 0; i < Constant.arr_favourites.size(); i++) {

				if (address
						.toLowerCase()
						.replace(",", "")
						.equalsIgnoreCase(
								Constant.arr_favourites.get(i).toLowerCase()
										.replace(",", ""))) {
					tvAdd.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.already_fev, 0, 0, 0);
					// imgbtn_add.setBackgroundResource(0);
					// imgbtn_add.setBackgroundResource(R.drawable.already_fev);
					break;
				} else {
					tvAdd.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.favorites, 0, 0, 0);
					// imgbtn_add.setBackgroundResource(0);
					// imgbtn_add.setBackgroundResource(R.drawable.favorites);
				}
			}
			tvAdd.invalidate();
			// imgbtn_add.invalidate();
		}
	}

	@Override
	public void onCustomBackPressed() {
		if (((MainFragmentActivity) getActivity()).nLastSelected == 1) {
			if (MainFragmentActivity.slidingMenu.isMenuShowing())
				MainFragmentActivity.slidingMenu.toggle();
			else
				((MainFragmentActivity) getActivity()).showQuitDialog();
		} else {
			getParentFragment().getChildFragmentManager().popBackStack();
		}

	}
}

class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	private PreBookParcelable pbParcelable;
	TextView tvWhenTime;

	public DatePickerFragment(PreBookParcelable pbParcelable,
			TextView tvWhenTime) {
		super();
		this.pbParcelable = pbParcelable;
		this.tvWhenTime = tvWhenTime;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		final int year = c.get(Calendar.YEAR);
		final int month = c.get(Calendar.MONTH);
		final int day = c.get(Calendar.DAY_OF_MONTH);
		Log.i(getClass().getSimpleName(), pbParcelable.getPickUpDate());
		int selectedDay = Integer.parseInt(pbParcelable.getPickUpDate()
				.substring(0, pbParcelable.getPickUpDate().indexOf("/")));
		int selectedMonth = Integer.parseInt(pbParcelable.getPickUpDate()
				.substring(pbParcelable.getPickUpDate().indexOf("/") + 1,
						pbParcelable.getPickUpDate().lastIndexOf("/"))) - 1;

		final int selectedYear = Integer.parseInt(pbParcelable.getPickUpDate()
				.substring(pbParcelable.getPickUpDate().lastIndexOf("/") + 1,
						pbParcelable.getPickUpDate().length()));
		Log.i(getClass().getSimpleName(), "selectedDay " + selectedDay
				+ " selectedYear " + selectedYear + " selectedMonth "
				+ selectedMonth);
		// Create a new instance of DatePickerDialog and return it
		return (new DatePickerDialog(getActivity(), this, selectedYear,
				selectedMonth, selectedDay) {
			@Override
			public void onDateChanged(DatePicker view, int currentyear,
					int monthOfYear, int dayOfMonth) {

				if (currentyear < year)
					view.updateDate(year, month, day);

				if (monthOfYear < month && currentyear == year)
					view.updateDate(year, month, day);

				if (dayOfMonth < day && currentyear == year
						&& monthOfYear == month)
					view.updateDate(year, month, day);

			}
		});
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		Log.i(getClass().getSimpleName(), year + " " + (month + 1) + " " + day);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/LL/yyyy");
		pbParcelable.setPickUpDate(day + "/" + (month + 1) + "/" + year);
		Log.i(getClass().getSimpleName(), pbParcelable.getPickUpDate());
		sdf = new SimpleDateFormat("E dd LLL");
		String formattedDate = sdf.format(c.getTime());
		pbParcelable.setPickUpDateToShow(formattedDate);
		TextView tvDate = (TextView) getActivity().findViewById(R.id.tv_w_date);
		tvDate.setText(formattedDate);
		c = Calendar.getInstance();
		if (c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == month
				&& c.get(Calendar.DAY_OF_MONTH) == day) {
			sdf = new SimpleDateFormat("HH:mm");
			pbParcelable.setPickUpTime(sdf.format(new Date()));
			tvWhenTime.setText(pbParcelable.getPickUpTime());
		}
	}

}

class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	private PreBookParcelable pbParcelable;
	int hour, minute;
	Calendar c;

	public TimePickerFragment(PreBookParcelable pbParcelable) {
		super();
		this.pbParcelable = pbParcelable;

	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		c = Calendar.getInstance();
		hour = Integer.parseInt(pbParcelable.getPickUpTime().substring(0, 2)
				.trim());
		minute = Integer.parseInt(pbParcelable.getPickUpTime().substring(3, 5)
				.trim());

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity())) {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay,
					int minutes) {
				// TODO Auto-generated method stub

				Log.i(getClass().getSimpleName(), pbParcelable.getPickUpDate());
				SimpleDateFormat sdf = new SimpleDateFormat("E dd LLL");
				String formattedDate = sdf.format(c.getTime());
				if (pbParcelable.getPickUpDateToShow().matches(formattedDate)) {
					Log.e("Time Changed",
							"Time Change" + " hourOfDay " + hourOfDay
									+ " Calendar.HOUR_OF_DAY "
									+ c.get(Calendar.HOUR_OF_DAY) + " minutes "
									+ minutes + " Calendar.MINUTE "
									+ c.get(Calendar.MINUTE));
					boolean validTime;
					if (hourOfDay < c.get(Calendar.HOUR_OF_DAY)) {
						validTime = false;
					} else if (hourOfDay == c.get(Calendar.HOUR_OF_DAY)) {
						validTime = minutes >= c.get(Calendar.MINUTE);
					}

					else {
						validTime = true;
					}

					if (validTime) {
						hour = hourOfDay;
						minute = minutes;
					} else {
						if (hourOfDay < c.get(Calendar.HOUR_OF_DAY))
							updateTime(c.get(Calendar.HOUR_OF_DAY), minutes);
						else if (hourOfDay == c.get(Calendar.HOUR_OF_DAY)
								&& minutes < c.get(Calendar.MINUTE)) {
							updateTime(c.get(Calendar.HOUR_OF_DAY),
									c.get(Calendar.MINUTE));
						} else
							updateTime(hour, minute);
					}
				}
			}

		};
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

		c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		SimpleDateFormat mSDF = new SimpleDateFormat("HH:mm");
		String formattedTime = mSDF.format(c.getTime());
		pbParcelable.setPickUpTime(formattedTime);
		TextView tvTime = (TextView) getActivity().findViewById(R.id.tv_w_time);
		tvTime.setText(formattedTime);

	}

}