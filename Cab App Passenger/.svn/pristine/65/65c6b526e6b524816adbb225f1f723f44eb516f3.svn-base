package com.example.cabapppassenger.fragments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.adapter.AirportAdapter;
import com.example.cabapppassenger.datastruct.json.Airport;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumLocations;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumWhen;
import com.example.cabapppassenger.listeners.PreBookListener;
import com.example.cabapppassenger.model.LocationParcelable;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.AirportTask;
import com.example.cabapppassenger.task.GetAddressTask;
import com.example.cabapppassenger.task.GetAutoPlacesTask;
import com.example.cabapppassenger.task.GetAvailableCabsTask;
import com.example.cabapppassenger.task.GetFavouritesTask;
import com.example.cabapppassenger.task.GetLatLongTask;
import com.example.cabapppassenger.task.GetPincodeTask;
import com.example.cabapppassenger.task.SettingsTask;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.MapStateListener;
import com.example.cabapppassenger.util.TouchableMapFragment;
import com.example.cabapppassenger.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Hail_NowFragment extends RootFragment implements
		OnCustomBackPressedListener {

	private TouchableMapFragment mMapFragment;
	private ViewGroup mMapFrame;
	public GoogleMap mMap;
	Context mContext;
	TextView tv_cabs, tv_time, tv_postcodetv, tvPickUpLocation, tvPickUpOnMap,
			tvPickUpRecent, tvPickUpMyPlaces, tv_edit, tvAdd, tv_noairports;
	ListView lv_airports;
	public TextView tv_address;
	ScrollView sc_editlayout, sc_airport;
	ImageView iv_location, iv_airport;
	ImageButton imgbtn_add;
	Location currentLocation = null;
	DecimalFormatSymbols decimalFormatSymbols;
	DecimalFormat decimalFormat;
	PreBookListener listener;
	PreBookParcelable pbParcelable = null;
	RelativeLayout rel_address, rel_getaquicker_taxi, rel_noofcabs,
			rel_currentlocation, rel_cabapp_loc_text;
	public String onMapAddress, onMapPincode, placeId = null,
			PREF_NAME = "CabApp_Passenger", atv_txtchanged;

	double onMapLongitude, onMapLatitude;
	EditText etPickUpPincode;
	Editor editor;
	boolean flag_latlng = false, valuesUpdated = false;
	View view_slidingtoggle, now_view, prebook_view, view_editbackground;
	SharedPreferences shared_pref;

	public AutoCompleteTextView atvPickUpAddress;
	boolean flag_firsttime = true;
	Handler mHandler, latlng_handler, defaultLatLngHandler, airport_handler;
	View view;
	GetAutoPlacesTask placesTask;
	LatLng currentLoc = null;
	ArrayList<LatLng> points = null;
	GetPincodeTask pincodeTask;
	Bundle bundle_prebook, bundle_now;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pbParcelable = new PreBookParcelable();
		mContext = getActivity();

		Bundle toFragment = getActivity().getIntent().getBundleExtra(
				"toFragment");
		getActivity().getIntent().removeExtra("toFragment");
		if (toFragment != null)
			if (toFragment.containsKey("toFragment")
					&& toFragment.containsKey("bookingId")
					&& toFragment.containsKey("alertMessage")) {
				if (toFragment.getString("toFragment").equalsIgnoreCase(
						"chatFragment")) {
					ChatFragment edit;

					if (toFragment.getString("alertMessage").trim()
							.toLowerCase().equals("arrived at pick up"))
						edit = new ChatFragment(
								toFragment.getString("bookingId"), true, false);
					else if (toFragment.getString("sound").length() > 0)
						edit = new ChatFragment(
								toFragment.getString("bookingId"), true, true);
					else
						edit = new ChatFragment(
								toFragment.getString("bookingId"), false, false);
					FragmentTransaction ft = this.getParentFragment()
							.getChildFragmentManager().beginTransaction();
					ft.replace(R.id.frame_container, edit, "chatFragment");
					ft.addToBackStack("hailNowFragment");
					ft.commit();

				}

				else if (toFragment.getString("toFragment").equalsIgnoreCase(
						"driverFragment")
						&& toFragment.containsKey("bookingId")) {
					Bookings_Driver_DetailsFragment edit = new Bookings_Driver_DetailsFragment(
							false, toFragment.getString("bookingId"));
					FragmentTransaction ft = this.getParentFragment()
							.getChildFragmentManager().beginTransaction();
					ft.replace(R.id.frame_container, edit,
							"bookingsDriverDetailsFragment");
					ft.addToBackStack("hailNowFragment");
					ft.commit();

				}
			}

		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			if (Util.getLocation(getActivity()) != null) {
				currentLocation = Util.getLocation(getActivity());
			}
			SettingsTask settings = new SettingsTask(mContext, pbParcelable);
			settings.execute();

			GetFavouritesTask task = new GetFavouritesTask(mContext);
			task.fromhailnow = true;
			task.execute("");
		} else {
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();
		}
		MapsInitializer.initialize(getActivity().getApplicationContext());
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_hailnow, container, false);
		((MainFragmentActivity) mContext).onsetdata();
		decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setGroupingSeparator('.');
		decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);
		// taking reference

		tv_noairports = (TextView) view.findViewById(R.id.tv_noairports);
		tv_cabs = (TextView) view.findViewById(R.id.tv_cabs);
		tv_cabs.setText("0 Cab");
		iv_airport = (ImageView) view.findViewById(R.id.iv_airport);
		tv_address = (TextView) view.findViewById(R.id.tv_address);
		rel_currentlocation = (RelativeLayout) view
				.findViewById(R.id.rel_currentlocation);
		tvPickUpOnMap = (TextView) view.findViewById(R.id.tv_pick_up_onmap);
		tvPickUpLocation = (TextView) view
				.findViewById(R.id.tv_pick_up_location);

		tvPickUpMyPlaces = (TextView) view
				.findViewById(R.id.tv_pick_up_favorites);
		tvPickUpRecent = (TextView) view.findViewById(R.id.tv_pick_up_recent);
		rel_noofcabs = (RelativeLayout) view.findViewById(R.id.rel_noofcabs);
		tv_time = (TextView) view.findViewById(R.id.tv_time);
		tv_time.setText("0 Min");
		rel_getaquicker_taxi = (RelativeLayout) view
				.findViewById(R.id.rel_getaquicker_taxi);
		rel_cabapp_loc_text = (RelativeLayout) view
				.findViewById(R.id.rel_cabapp_loc_text);
		tvPickUpOnMap = (TextView) view.findViewById(R.id.tv_pick_up_onmap);
		tvPickUpRecent = (TextView) view.findViewById(R.id.tv_pick_up_recent);
		etPickUpPincode = (EditText) view.findViewById(R.id.et_pick_up_pincode);
		view_slidingtoggle = (View) view.findViewById(R.id.view_slidingtoggle);
		sc_airport = (ScrollView) view.findViewById(R.id.sc_airport);
		atvPickUpAddress = (AutoCompleteTextView) view
				.findViewById(R.id.atv_pick_up_address);
		view_editbackground = (View) view
				.findViewById(R.id.view_editbackground);
		sc_editlayout = (ScrollView) view.findViewById(R.id.sc_editlayout);
		now_view = (View) view.findViewById(R.id.now_view);
		tv_edit = (TextView) view.findViewById(R.id.tv_edit);
		prebook_view = (View) view.findViewById(R.id.prebook_view);
		tvAdd = (TextView) view.findViewById(R.id.tv_pick_up_top_right);
		lv_airports = (ListView) view.findViewById(R.id.lv_airports);

		rel_cabapp_loc_text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				prebooklistenermethod(true);
			}
		});
		rel_getaquicker_taxi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				prebooklistenermethod(true);
			}
		});
		prebook_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prebooklistenermethod(false);
			}
		});
		now_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bundle_now = new Bundle();
				pbParcelable.getDefaultDropOffLocation().setAddress1(
						"As Directed");

				/*
				 * pbParcelable.getMapPickUpLocation().setAddress1(onMapAddress);
				 * pbParcelable.getMapPickUpLocation().setPinCode(onMapPincode);
				 */
				pbParcelable.seteWhen(EnumWhen.ASAP);
				if (sc_editlayout.getVisibility() == View.GONE)
					pbParcelable.setePickUpSelected(EnumLocations.MAPLOCATION);
				pbParcelable.getPresentLocation().setLatitude(
						Double.longBitsToDouble(shared_pref.getLong(
								"presentLatitude", 0)));
				pbParcelable.getPresentLocation().setLongitude(
						Double.longBitsToDouble(shared_pref.getLong(
								"presentLongitude", 0)));
				pbParcelable.getMapPickUpLocation().setLatitude(
						Double.longBitsToDouble(shared_pref.getLong(
								"OnMapLatitude", 0)));
				pbParcelable.getMapPickUpLocation().setLongitude(
						Double.longBitsToDouble(shared_pref.getLong(
								"OnMapLongitude", 0)));

				pbParcelable.setValuesUpdated(false);
				pbParcelable.setPaymentTypeUpdated(false);
				bundle_now.putParcelable("pbParcelable", pbParcelable);

				BookingDetailsFragment edit = new BookingDetailsFragment();
				FragmentTransaction fragtrans = getParentFragment()
						.getChildFragmentManager().beginTransaction();
				fragtrans.replace(R.id.frame_container, edit,
						"bookingDetailsFragment"); // f1_container
				edit.setArguments(bundle_now);
				fragtrans.addToBackStack(null);
				fragtrans
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				fragtrans.commit();
			}
		});
		airport_handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle airport_data = (Bundle) msg.obj;
					if (airport_data != null
							&& airport_data.containsKey("Airport_Response"))

						parse_airport_data(airport_data.getString(
								"Airport_Response", ""));
					else {
						iv_airport.setImageDrawable(null);
						iv_airport
								.setBackgroundResource(R.drawable.airporticon);
					}
				}

			}

		};
		iv_airport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sc_airport.getVisibility() != View.VISIBLE) {
					iv_airport.setImageDrawable(null);
					iv_airport
							.setBackgroundResource(R.drawable.airporticon_selected);
					sc_editlayout.setVisibility(View.GONE);
					view_editbackground.setVisibility(View.GONE);
					view_editbackground.setClickable(false);
					rel_noofcabs.setVisibility(View.VISIBLE);
					rel_currentlocation.setVisibility(View.VISIBLE);
					tv_edit.setText("EDIT");

					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable
							&& pbParcelable.getPresentLocation().getLatitude() != 0
							&& pbParcelable.getPresentLocation().getLongitude() != 0) {
						AirportTask task = new AirportTask(mContext);
						task.latitude = pbParcelable.getPresentLocation()
								.getLatitude();
						task.longitude = pbParcelable.getPresentLocation()
								.getLongitude();
						task.handler = airport_handler;
						task.execute(Constant.passengerURL
								+ "ws/v2/passenger/airports/?accessToken="
								+ shared_pref.getString("AccessToken", ""));
					} else {
						Toast.makeText(mContext, "No network connection",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					iv_airport.setImageDrawable(null);
					iv_airport.setBackgroundResource(R.drawable.airporticon);
					sc_airport.setVisibility(View.GONE);
					rel_noofcabs.setVisibility(View.VISIBLE);
					rel_currentlocation.setVisibility(View.VISIBLE);
				}

			}
		});
		defaultLatLngHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					LocationParcelable[] selectedLocation = listener
							.getSelectedLocationValues(pbParcelable);
					pbParcelable.getDefaultPickUpLocation().setAddress1(
							selectedLocation[0].getAddress1());
					pbParcelable.getDefaultPickUpLocation().setPinCode(
							selectedLocation[0].getPinCode());
					pbParcelable.getDefaultPickUpLocation().setLatitude(
							bundleData.getDouble("latitude"));
					pbParcelable.getDefaultPickUpLocation().setLongitude(
							bundleData.getDouble("longitude"));
					currentLoc = new LatLng(pbParcelable
							.getDefaultPickUpLocation().getLatitude(),
							pbParcelable.getDefaultPickUpLocation()
									.getLongitude());
					mMap.clear();
					mMap.getUiSettings().setZoomControlsEnabled(false);
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							currentLoc, 15));

				}

			}

		};

		tv_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (tv_edit.getText().toString().equals("EDIT")) {
					DisplayMetrics dm = new DisplayMetrics();
					getActivity().getWindowManager().getDefaultDisplay()
							.getMetrics(dm);
					int width = dm.widthPixels;
					int height = dm.heightPixels;
					int dens = dm.densityDpi;
					double wi = (double) width / (double) dens;
					double hi = (double) height / (double) dens;
					double x = Math.pow(wi, 2);
					double y = Math.pow(hi, 2);
					String screenInches = decimalFormat.format(Math.sqrt(x + y));
					double finalValue;
					try {
						finalValue = (Double) decimalFormat.parse(screenInches);
						if (finalValue <= 3.89) {
							rel_noofcabs.setVisibility(View.GONE);
							rel_currentlocation.setVisibility(View.GONE);
						} else {
							rel_noofcabs.setVisibility(View.VISIBLE);
							rel_currentlocation.setVisibility(View.VISIBLE);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					tv_edit.setText("DONE");
					sc_airport.setVisibility(View.GONE);
					iv_airport.setImageDrawable(null);
					iv_airport.setBackgroundResource(R.drawable.airporticon);
					sc_editlayout.setVisibility(View.VISIBLE);
					view_editbackground.setVisibility(View.VISIBLE);
					view_editbackground.setClickable(true);

					if (onMapAddress != null
							&& pbParcelable.getPresentLocation().getAddress1()
									.matches(onMapAddress)) {
						// highlight current location

						tvPickUpLocation.performClick();
					} else {
						// highlight onmap
						listener.programmaticPickUpOnMapClick = true;
						tvPickUpOnMap.performClick();
					}
				} else {
					// done code
					sc_editlayout.setVisibility(View.GONE);
					view_editbackground.setVisibility(View.GONE);
					view_editbackground.setClickable(false);
					rel_noofcabs.setVisibility(View.VISIBLE);
					rel_currentlocation.setVisibility(View.VISIBLE);
					tv_edit.setText("EDIT");
					// for default need to find latlong

					LocationParcelable[] selectedLocation;
					selectedLocation = listener
							.getSelectedLocationValues(pbParcelable);

					tv_address.setText(selectedLocation[0].getAddress1());
					// change camera code according to the condition

					if (selectedLocation[0].getLatitude() != 0
							&& selectedLocation[0].getLongitude() != 0) {
						currentLoc = new LatLng(selectedLocation[0]
								.getLatitude(), selectedLocation[0]
								.getLongitude());
						mMap.clear();
						mMap.getUiSettings().setZoomControlsEnabled(false);
						mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
								currentLoc, 15));

					} else {
						GetLatLongTask task = new GetLatLongTask(mContext,
								selectedLocation[0].getAddress1(),
								selectedLocation[0].getPinCode(),
								defaultLatLngHandler);
						task.execute("");
					}
					pbParcelable.setePickUpSelected(EnumLocations.MAPLOCATION);
					// camera move
				}

			}
		});

		view_editbackground.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// dismiss edit dialog on touch of outside
				sc_editlayout.setVisibility(View.GONE);
				view_editbackground.setVisibility(View.GONE);
				view_editbackground.setClickable(false);
				rel_noofcabs.setVisibility(View.VISIBLE);
				rel_currentlocation.setVisibility(View.VISIBLE);
				tv_edit.setText("EDIT");

				pbParcelable.getMapPickUpLocation().setAddress1(onMapAddress);
				pbParcelable.getMapPickUpLocation().setPinCode(onMapPincode);
				pbParcelable.getMapPickUpLocation().setLatitude(onMapLatitude);
				pbParcelable.getMapPickUpLocation()
						.setLongitude(onMapLongitude);
				pbParcelable.setePickUpSelected(EnumLocations.MAPLOCATION);
				// change to old values in parceable

			}
		});

		iv_location = (ImageView) view.findViewById(R.id.iv_currentlocation);
		iv_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// move to current location
				mMap.clear();
				if (Util.getLocation(getActivity()) != null) {
					currentLocation = Util.getLocation(getActivity());
				}
				if (currentLocation != null) {
					currentLoc = new LatLng(currentLocation.getLatitude(),
							currentLocation.getLongitude());
					mMap.getUiSettings().setZoomControlsEnabled(false);
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							currentLoc, 15));
					getAddress(currentLocation.getLatitude(),
							currentLocation.getLongitude());
				}
			}
		});
		mMapFrame = (ViewGroup) view.findViewById(R.id.map_frame);

		mMapFragment = new TouchableMapFragment();
		FragmentTransaction fragmentTransaction = getChildFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.map_frame, mMapFragment);
		fragmentTransaction.commit();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					if (pbParcelable.getPresentLocation().getAddress1()
							.equals("")) {
						if (bundleData.containsKey("currentaddress")) {
							pbParcelable.getPresentLocation().setAddress1(
									bundleData.getString("currentaddress"));
						}
						if (bundleData.containsKey("currentpincode")) {
							pbParcelable.getPresentLocation().setPinCode(
									bundleData.getString("currentpincode"));
						}
						pbParcelable.getPresentLocation().setLatitude(
								Double.longBitsToDouble(shared_pref.getLong(
										"presentLatitude", 0)));
						pbParcelable.getPresentLocation().setLongitude(
								Double.longBitsToDouble(shared_pref.getLong(
										"presentLongitude", 0)));
					}
					if (bundleData.containsKey("onmapaddress")
							&& bundleData.containsKey("onmappincode")) {
						onMapAddress = bundleData.getString("onmapaddress");
						onMapPincode = bundleData.getString("onmappincode");
						if (onMapAddress != null && !onMapAddress.matches(""))
							tv_address.setText(onMapAddress);

						if (onMapAddress != null

						&& !onMapAddress.matches("")) {

							pbParcelable.getMapPickUpLocation().setAddress1(
									onMapAddress);
							pbParcelable.getMapPickUpLocation().setPinCode(
									onMapPincode);

							pbParcelable.getMapPickUpLocation().setLatitude(
									Double.longBitsToDouble(shared_pref
											.getLong("OnMapLatitude", 0)));
							pbParcelable.getMapPickUpLocation().setLongitude(
									Double.longBitsToDouble(shared_pref
											.getLong("OnMapLongitude", 0)));

						}
					}

					if (bundleData.containsKey("Available Cab Response")) {
						availablecab_jsonparser(bundleData.getString(
								"Available Cab Response", ""));
					}

					if (bundleData.containsKey("PRecent")) {

						Constant.flag_onhailnow_recent = true;
						RecentFragment edit = new RecentFragment(pbParcelable);
						FragmentManager fm = getParentFragment()
								.getChildFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						ft.replace(R.id.frame_container, edit);
						Bundle from = new Bundle();
						from.putString("PRecent", "");
						edit.setArguments(from);
						ft.addToBackStack("hailNowFragment");
						ft.commit();
						pbParcelable.setHailNowPopUpVisible(true);
						fm.executePendingTransactions();
						bundleData.clear();
					}
					if (bundleData.containsKey("pickUpOnMap")) {
						Constant.flag_from_onmap = true;

						OnMapFragment edit = new OnMapFragment(pbParcelable);
						FragmentManager fm = getParentFragment()
								.getChildFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						ft.replace(R.id.frame_container, edit, "onMapFragment");
						Bundle from = new Bundle();
						from.putString("pickUpOnMap", "");
						edit.setArguments(from);
						ft.addToBackStack("hailNowFragment");

						ft.commit();
						pbParcelable.setHailNowPopUpVisible(true);
						fm.executePendingTransactions();
						bundleData.clear();
					}
					if (bundleData.containsKey("PFavourites")) {
						Constant.flag_fromslider_favourites = false;

						Constant.flag_onhailnow_fav = true;
						FavoritesFragment edit = new FavoritesFragment(
								pbParcelable);
						FragmentManager fm = getParentFragment()
								.getChildFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						Bundle from = new Bundle();
						from.putString("PFavourites", "");
						edit.setArguments(from);
						ft.replace(R.id.frame_container, edit);
						ft.addToBackStack("hailNowFragment");
						ft.commit();
						pbParcelable.setHailNowPopUpVisible(true);
						fm.executePendingTransactions();
						bundleData.clear();
					}
					// if (bundleData.containsKey("latitude")
					// && bundleData.containsKey("longitude")) {
					// Double latitude, longitude;
					// String address = null, pincode = null;
					// latitude = bundleData.getDouble("latitude");
					// longitude = bundleData.getDouble("longitude");
					// pbParcelable.getMapPickUpLocation().setLatitude(
					// latitude);
					// pbParcelable.getMapPickUpLocation().setLongitude(
					// longitude);
					//
					// if (bundleData.containsKey("address")) {
					// address = bundleData.getString("address");
					// }
					// if (bundleData.containsKey("pincode")) {
					// pincode = bundleData.getString("pincode");
					// }
					// if (!flag_latlng) {
					// AddFavourites favourites = new AddFavourites(
					// mContext, address, pincode, latitude,
					// longitude, atvPickUpAddress);
					// favourites.execute(Constant.passengerURL
					// + "ws/v2/passenger/favourite/?accessToken="
					// + shared_pref.getString("AccessToken", ""));
					// } else {
					// currentLoc = new LatLng(latitude, longitude);
					// mMap.getUiSettings().setZoomControlsEnabled(false);
					// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					// currentLoc, 15));
					// }
					// }
				}

			}

		};
		listener = new PreBookListener(mContext, view, pbParcelable, mHandler);
		tvPickUpLocation.setOnClickListener(listener);
		tvPickUpOnMap.setOnClickListener(listener);
		tvPickUpRecent.setOnClickListener(listener);
		tvAdd.setOnClickListener(listener);
		tvPickUpMyPlaces.setOnClickListener(listener);

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

		etPickUpPincode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				/*
				 * if(!atvListClick) { atvListClick=false;
				 */
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
				// }

			}
		});
		atvPickUpAddress.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				// atv_txtchanged = s.toString();

				GetAutoPlacesTask placesTask = new GetAutoPlacesTask(mContext);
				placesTask.execute(s.toString());
				checkfav_pickup(s.toString().trim());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

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

			}

		});

		super.initWidgets(view, this.getClass().getName());
		return view;

	}

	@Override
	public void onResume() {
		super.onResume();
		// getActivity().getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		if (pbParcelable.isHailNowPopUpVisible()) {
			valuesUpdated = false;
			pbParcelable.setHailNowPopUpVisible(false);
			tv_edit.setText("DONE");
			sc_editlayout.setVisibility(View.VISIBLE);

			sc_editlayout.fullScroll(ScrollView.FOCUS_UP);
			view_slidingtoggle.setClickable(false);
			view_editbackground.setVisibility(View.VISIBLE);
			rel_noofcabs.setVisibility(View.GONE);
			rel_currentlocation.setVisibility(View.GONE);
			currentLoc = new LatLng(Double.longBitsToDouble(shared_pref
					.getLong("OnMapLatitude", 0)),
					Double.longBitsToDouble(shared_pref.getLong(
							"OnMapLongitude", 0)));
			mMap.clear();
			mMap.getUiSettings().setZoomControlsEnabled(false);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
			tv_address.setText(onMapAddress);

			if (pbParcelable.getePickUpSelected() == EnumLocations.FAVOURITE) {
				listener.programmaticPickUpFavClick = true;
				tvPickUpMyPlaces.performClick();
			}

			if (pbParcelable.getePickUpSelected() == EnumLocations.MAPLOCATION) {
				listener.programmaticPickUpOnMapClick = true;
				tvPickUpOnMap.performClick();
			} else if (pbParcelable.getePickUpSelected() == EnumLocations.RECENT) {
				listener.programmaticPickUpRecentClick = true;
				tvPickUpRecent.performClick();
			} else if (pbParcelable.getePickUpSelected() == EnumLocations.PRESENTLOCATION) {
				tvPickUpLocation.performClick();
			}
		}

		else {

			pbParcelable = new PreBookParcelable();
			listener.updateParcelableData(pbParcelable);

			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				if (Util.getLocation(getActivity()) != null) {
					currentLocation = Util.getLocation(getActivity());
					editor.putLong("presentLatitude", Double
							.doubleToRawLongBits(currentLocation.getLatitude()));
					editor.putLong("presentLongitude",
							Double.doubleToRawLongBits(currentLocation
									.getLongitude()));
					editor.commit();
					pbParcelable.getPresentLocation().setLatitude(
							currentLocation.getLatitude());

					pbParcelable.getPresentLocation().setLongitude(
							currentLocation.getLongitude());
					currentLoc = new LatLng(currentLocation.getLatitude(),
							currentLocation.getLongitude());
					mMap.getUiSettings().setZoomControlsEnabled(false);
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							currentLoc, 15));
					getAddress(pbParcelable.getPresentLocation().getLatitude(),
							pbParcelable.getPresentLocation().getLongitude());
				}

			} else {
				Toast.makeText(mContext, "No network connection",
						Toast.LENGTH_SHORT).show();
			}

		}

		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);

	}

	public void AddMarker(Double lat, Double lng, int icon) {

		if (lat != null && lng != null) {
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(new LatLng(lat, lng));
			markerOptions.icon(BitmapDescriptorFactory.fromResource(icon));
			mMap.addMarker(markerOptions);

		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

	}

	public void getAddress(double latitude, double longitude) {
		// get address of particular location
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			GetAddressTask task = new GetAddressTask(getActivity());
			task.latitude = latitude;
			task.longitude = longitude;
			task.noloading_flag = true;
			task.mHandler = mHandler;
			if (flag_firsttime && currentLocation != null) {
				task.flag_hail = true;
				editor.putLong("presentLatitude", Double
						.doubleToRawLongBits(currentLocation.getLatitude()));
				editor.putLong("presentLongitude", Double
						.doubleToRawLongBits(currentLocation.getLongitude()));
				editor.commit();
			}
			task.execute("");
			flag_firsttime = false;
		} else {
			Toast.makeText(getActivity(), "No network connection",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void prebooklistenermethod(boolean getQuick_cab) {

		if (getQuick_cab)
			pbParcelable.seteWhen(EnumWhen.ASAP);
		else
			pbParcelable.seteWhen(EnumWhen.PREBOOK);

		bundle_prebook = new Bundle();

		pbParcelable.setValuesUpdated(false);
		pbParcelable.setPaymentTypeUpdated(false);
		if (sc_editlayout.getVisibility() == View.GONE)
			pbParcelable.setePickUpSelected(EnumLocations.MAPLOCATION);

		bundle_prebook.putParcelable("pbParcelable", pbParcelable);

		Pre_BookFragment edit = new Pre_BookFragment();
		FragmentTransaction fragtrans = getParentFragment()
				.getChildFragmentManager().beginTransaction();
		fragtrans.replace(R.id.frame_container, edit, "preBookFragment"); // f1_container
		edit.setArguments(bundle_prebook);
		fragtrans.addToBackStack("hailNowFragment");
		fragtrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		fragtrans.commit();
	}

	public void updateOnMapValues(PreBookParcelable pbParcelable) {

		this.pbParcelable = pbParcelable;
	}

	@Override
	public void onPause() {

		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		super.onPause();
	}

	@SuppressWarnings("unused")
	public void availablecab_jsonparser(String response) {
		// TODO Auto-generated method stub
		Log.e("Cabs response in hailnow", response);
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

					distance = Javailablecabs_obj.getString("distance");
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
							tv_time.setText("0 Cab");

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
					mMap.clear();
					tv_time.setText("0 Min");
					tv_cabs.setText("0 Cab");
				}
			}
		} catch (Exception e) {
			try {
				if (response != null) {
					mMap.clear();
					tv_time.setText("0 Min");
					tv_cabs.setText("0 Cab");
					JSONObject jobj = new JSONObject(response);
					JSONArray jArray = jobj.getJSONArray("errors");
					if (jArray.length() > 0) {
						JSONObject jObj = jArray.getJSONObject(0);
						String message = jObj.getString("message");
						Log.i("****Available CAbs Error****", "" + message);

						if (message.toString().equals(
								"missing or invalid accessToken")) {
							Constant.logout(mContext, editor, true);
							Log.i("****Available Cab Error****", "" + message);
						} else {
							Toast.makeText(mContext, message,
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			e.printStackTrace();
		}
	}

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mMap = mMapFragment.getMap();

		if (currentLocation != null) {

			currentLoc = new LatLng(currentLocation.getLatitude(),
					currentLocation.getLongitude());
			if (mMap != null) {
				mMap.getUiSettings().setZoomControlsEnabled(false);
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc,
						15));
				new MapStateListener(mMap, mMapFragment, this.getActivity()) {
					@Override
					public void onMapTouched() {
						// Map touched
					}

					@Override
					public void onMapReleased() {
						// Map released
					}

					@Override
					public void onMapUnsettled() {
						// Map unsettled
					}

					@Override
					public void onMapSettled() {
						// Map settled
						if (Constant.flag_onhailnow_recent) {
							Constant.flag_onhailnow_recent = false;
							return;
						}
						if (Constant.flag_onhailnow_fav) {
							Constant.flag_onhailnow_fav = false;
							return;
						}
						if (Constant.flag_from_onmap) {
							Constant.flag_from_onmap = false;
							return;
						}
						if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
							if (sc_editlayout.getVisibility() == View.GONE) {
								if (currentLocation != null)

									getAddress(
											mMap.getCameraPosition().target.latitude,
											mMap.getCameraPosition().target.longitude);

								onMapLatitude = mMap.getCameraPosition().target.latitude;

								onMapLongitude = mMap.getCameraPosition().target.longitude;

								pbParcelable.getMapPickUpLocation()
										.setLatitude(onMapLatitude);
								pbParcelable.getMapPickUpLocation()
										.setLongitude(onMapLongitude);

								editor.putLong(
										"OnMapLatitude",
										Double.doubleToRawLongBits(mMap
												.getCameraPosition().target.latitude));
								editor.putLong(
										"OnMapLongitude",
										Double.doubleToRawLongBits(mMap
												.getCameraPosition().target.longitude));
								editor.commit();
								GetAvailableCabsTask cabs = new GetAvailableCabsTask(
										mContext);
								cabs.mHandler = mHandler;
								cabs.flag_noloading = true;
								cabs.latitude = mMap.getCameraPosition().target.latitude;
								cabs.longitude = mMap.getCameraPosition().target.longitude;
								cabs.execute(Constant.passengerURL
										+ "ws/v2/distance/driverswithinradius/?accessToken="
										+ shared_pref.getString("AccessToken",
												""));
							}
						} else {
							Toast.makeText(mContext, "No network connection",
									Toast.LENGTH_SHORT).show();
						}
					}
				};
			}
		}
	}

	protected void parse_airport_data(String airport_data) {
		// TODO Auto-generated method stub
		JSONObject airport_jobj;
		String airportid, airportName, address1, postcode, latitude, longitude, cityId, fixedPriceCurrency, fixedPrice;
		Double amount;
		try {
			Airport newairport;
			DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
			decimalFormatSymbols.setGroupingSeparator('.');
			DecimalFormat decimalFormat = new DecimalFormat("0.00",
					decimalFormatSymbols);
			List<Airport> list_airport = new ArrayList<Airport>();
			airport_jobj = new JSONObject(airport_data);
			if (tv_noairports != null)
				tv_noairports.setVisibility(View.GONE);
			if (airport_jobj != null) {
				JSONArray jArr_airports = airport_jobj.getJSONArray("airports");
				for (int i = 0; i < jArr_airports.length(); i++) {
					JSONObject jobj = jArr_airports.getJSONObject(i);
					//
					airportid = jobj.getString("airportId");
					airportName = jobj.getString("airportName");
					address1 = jobj.getString("address1");
					postcode = jobj.getString("postcode");
					latitude = jobj.getString("latitude");
					longitude = jobj.getString("longitude");
					cityId = jobj.getString("cityId");
					amount = jobj.getDouble("fixedPrice");
					fixedPrice = decimalFormat.format(amount);
					Log.i("****AMOUNT IN POST EXECUTE",
							decimalFormat.format(amount));

					fixedPriceCurrency = jobj.getString("fixedPriceCurrency");

					newairport = new Airport();
					newairport.setAirportid(airportid);
					newairport.setAddress1(address1);
					newairport.setAirportName(airportName);
					newairport.setPostcode(postcode);
					newairport.setLatitude(latitude);
					newairport.setLongitude(longitude);
					newairport.setCityId(cityId);
					newairport.setFixedPrice(fixedPrice);
					newairport.setFixedPriceCurrency(fixedPriceCurrency);

					list_airport.add(newairport);
				}
				if (list_airport != null && list_airport.size() > 0) {
					lv_airports.setAdapter(new AirportAdapter(mContext, this,
							list_airport, pbParcelable));
					sc_airport.setVisibility(View.VISIBLE);
					rel_noofcabs.setVisibility(View.GONE);
					rel_currentlocation.setVisibility(View.GONE);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSONObject jobj;
			if (tv_noairports != null)
				tv_noairports.setVisibility(View.VISIBLE);
			try {
				jobj = new JSONObject(airport_data);
				String message = jobj.getString("Message");
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

	private void checkfav_pickup(String address) {
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
					break;
				} else {
					tvAdd.setCompoundDrawablesWithIntrinsicBounds(
							R.drawable.favorites, 0, 0, 0);
				}
			}
			tvAdd.invalidate();
		}
	}

	public void updateHailNowData(PreBookParcelable pbParcelable) {
		this.pbParcelable = pbParcelable;
		this.valuesUpdated = true;
	}

	@Override
	public void onCustomBackPressed() {

		if (MainFragmentActivity.slidingMenu.isMenuShowing())
			MainFragmentActivity.slidingMenu.toggle();
		else if (sc_airport.getVisibility() == View.VISIBLE) {
			sc_airport.setVisibility(View.GONE);
			rel_noofcabs.setVisibility(View.VISIBLE);
			rel_currentlocation.setVisibility(View.VISIBLE);
			iv_airport.setBackgroundDrawable(null);
			iv_airport.setBackgroundResource(R.drawable.airporticon);

		} else if (sc_editlayout.getVisibility() == View.VISIBLE) {
			sc_editlayout.setVisibility(View.GONE);
			view_editbackground.setVisibility(View.GONE);
			view_editbackground.setClickable(false);
			rel_noofcabs.setVisibility(View.VISIBLE);
			rel_currentlocation.setVisibility(View.VISIBLE);
			tv_edit.setText("EDIT");

			pbParcelable.getMapPickUpLocation().setAddress1(onMapAddress);
			pbParcelable.getMapPickUpLocation().setPinCode(onMapPincode);
			pbParcelable.getMapPickUpLocation().setLatitude(onMapLatitude);
			pbParcelable.getMapPickUpLocation().setLongitude(onMapLongitude);
			pbParcelable.setePickUpSelected(EnumLocations.MAPLOCATION);
		} else
			((MainFragmentActivity) getActivity()).showQuitDialog();
	}
}
