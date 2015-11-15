package com.example.cabapppassenger.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.activity.MainFragmentActivity.OnCustomBackPressedListener;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumLocations;
import com.example.cabapppassenger.model.LocationParcelable;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.GetAddressTask;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.MapStateListener;
import com.example.cabapppassenger.util.TouchableMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class OnMapFragment extends RootFragment implements
		OnCustomBackPressedListener {

	View view;
	private TouchableMapFragment mMapFragment;
	private ViewGroup mMapFrame;
	Context mContext;
	Editor editor;
	SharedPreferences shared_pref;
	Handler mhandler;
	TextView tv_address, tv_Done;
	ImageView iv_back;
	String PREF_NAME = "CabApp_Passenger";
	Location currentLocation = null;
	String onMapAddress, onMapPincode;
	LatLng currentLoc = null;
	public GoogleMap mMap;
	Double onmap_latitude, onmap_longitude;
	ArrayList<LatLng> points = null;
	PreBookParcelable pbParcelable = null;
	Bundle from;
	ImageView iv_currentlocation;
	Fragment previousFragment;

	public OnMapFragment(PreBookParcelable pbParcelable) {
		super();
		this.pbParcelable = pbParcelable;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// pbParcelable = new PreBookParcelable();
		from = getArguments();
		mContext = getActivity();
		MapsInitializer.initialize(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_onmap, container, false);
		// getActivity().getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		iv_currentlocation = (ImageView) view
				.findViewById(R.id.iv_currentlocation);
		mMapFrame = (ViewGroup) view.findViewById(R.id.map_frame);
		tv_address = (TextView) view.findViewById(R.id.tv_address);
		tv_Done = (TextView) view.findViewById(R.id.tvDone);
		mMapFragment = new TouchableMapFragment();
		FragmentTransaction fragmentTransaction = getChildFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.map_frame, mMapFragment);
		fragmentTransaction.commit();
		iv_back = (ImageView) view.findViewById(R.id.ivBack);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					String backStackName = getParentFragment()
							.getChildFragmentManager()
							.getBackStackEntryAt(
									getParentFragment()
											.getChildFragmentManager()
											.getBackStackEntryCount() - 1)
							.getName();
					previousFragment = getParentFragment()
							.getChildFragmentManager().findFragmentByTag(
									backStackName);

				} catch (Exception e) {
					Log.i(getClass().getSimpleName(),
							e.getMessage() == null ? "exception" : e
									.getMessage());
				}

				getParentFragment().getChildFragmentManager().popBackStack();
			}
		});

		iv_currentlocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMap.clear();
				if (currentLocation != null) {
					currentLoc = new LatLng(currentLocation.getLatitude(),
							currentLocation.getLongitude());
					onmap_latitude = currentLocation.getLatitude();
					onmap_longitude = currentLocation.getLongitude();
					getAddress(currentLocation.getLatitude(),
							currentLocation.getLongitude());
					AddMarker(currentLoc.latitude, currentLoc.longitude,
							R.drawable.passenger);
					mMap.getUiSettings().setZoomControlsEnabled(false);
					mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
							currentLoc, 15));
				}
			}
		});

		mhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg != null && msg.peekData() != null) {

					Bundle bundle_data = (Bundle) msg.obj;
					if (bundle_data.containsKey("onmapaddress")) {
						onMapAddress = bundle_data.getString("onmapaddress");
						tv_address.setText(onMapAddress);
					}
					if (bundle_data.containsKey("onmappincode")) {
						onMapPincode = bundle_data.getString("onmappincode");
					}

				}
			}

		};
		tv_Done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/* pbParcelable.setStateSelected("OnMap"); */

				if (from.containsKey("pickUpOnMap")) {
					pbParcelable.setePickUpSelected(EnumLocations.MAPLOCATION);
					pbParcelable.getMapPickUpLocation().setAddress1(
							onMapAddress);
					pbParcelable.getMapPickUpLocation()
							.setPinCode(onMapPincode);
					pbParcelable.getMapPickUpLocation().setLatitude(
							onmap_latitude);
					pbParcelable.getMapPickUpLocation().setLongitude(
							onmap_longitude);
					pbParcelable
							.setDefaultPickUpLocation(new LocationParcelable());

				} else if (from.containsKey("dropOffOnMap")) {
					pbParcelable.seteDropOffSelected(EnumLocations.MAPLOCATION);
					pbParcelable.getMapDropOffLocation().setAddress1(
							onMapAddress);
					pbParcelable.getMapDropOffLocation().setPinCode(
							onMapPincode);
					pbParcelable.getMapDropOffLocation().setLatitude(
							onmap_latitude);
					pbParcelable.getMapDropOffLocation().setLongitude(
							onmap_longitude);
					pbParcelable
							.setDefaultDropOffLocation(new LocationParcelable());

				}

				try {
					String backStackName = getParentFragment()
							.getChildFragmentManager()
							.getBackStackEntryAt(
									getParentFragment()
											.getChildFragmentManager()
											.getBackStackEntryCount() - 1)
							.getName();
					previousFragment = getParentFragment()
							.getChildFragmentManager().findFragmentByTag(
									backStackName);

				} catch (Exception e) {
					Log.i(getClass().getSimpleName(),
							e.getMessage() == null ? "exception" : e
									.getMessage());
				}

				getParentFragment().getChildFragmentManager().popBackStack();
			}
		});
		super.initWidgets(view, this.getClass().getName());
		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mMap = mMapFragment.getMap();
		if (Constant.getLocation(getActivity()) != null) {
			currentLocation = Constant.getLocation(getActivity());
		} else
			Toast.makeText(mContext,
					"Unable to get current location, please try again.",
					Toast.LENGTH_SHORT).show();
		Log.e("onMap Values in parceable", pbParcelable.getMapPickUpLocation()
				.getAddress1());
		if (from.containsKey("pickUpOnMap")) {

			if (pbParcelable.getMapPickUpLocation().getLatitude() == 0
					&& pbParcelable.getMapPickUpLocation().getLongitude() == 0) {
				onmap_latitude = pbParcelable.getPresentLocation()
						.getLatitude();
				onmap_longitude = pbParcelable.getPresentLocation()
						.getLongitude();
				onMapAddress = pbParcelable.getPresentLocation().getAddress1();
				onMapPincode = pbParcelable.getPresentLocation().getPinCode();

			} else {
				onmap_latitude = pbParcelable.getMapPickUpLocation()
						.getLatitude();
				onmap_longitude = pbParcelable.getMapPickUpLocation()
						.getLongitude();
				onMapAddress = pbParcelable.getMapPickUpLocation()
						.getAddress1();
				onMapPincode = pbParcelable.getMapPickUpLocation().getPinCode();

			}

		} else if (from.containsKey("dropOffOnMap")) {

			if (pbParcelable.getMapDropOffLocation().getLatitude() == 0
					&& pbParcelable.getMapDropOffLocation().getLongitude() == 0) {
				onmap_latitude = pbParcelable.getPresentLocation()
						.getLatitude();
				onmap_longitude = pbParcelable.getPresentLocation()
						.getLongitude();
				onMapAddress = pbParcelable.getPresentLocation().getAddress1();
				onMapPincode = pbParcelable.getPresentLocation().getPinCode();

			} else {
				onmap_latitude = pbParcelable.getMapDropOffLocation()
						.getLatitude();
				onmap_longitude = pbParcelable.getMapDropOffLocation()
						.getLongitude();
				onMapAddress = pbParcelable.getMapDropOffLocation()
						.getAddress1();
				onMapPincode = pbParcelable.getMapDropOffLocation()
						.getPinCode();

			}

		}
		tv_address.setText(onMapAddress);
		currentLoc = new LatLng(onmap_latitude, onmap_longitude);
		points = new ArrayList<LatLng>();
		points.add(currentLoc);
		// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
		// getAddress(pbParcelable.getPresentLocation().getLatitude(),
		// pbParcelable.getPresentLocation().getLongitude());
		// AddMarker(currentLoc.latitude, currentLoc.longitude,
		// R.drawable.passenger);
		mMap.getUiSettings().setZoomControlsEnabled(false);
		// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
		/* } */
		if (mMap != null) {
			mMap.getUiSettings().setZoomControlsEnabled(false);
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 15));
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
					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
						if (currentLocation != null)
							getAddress(
									mMap.getCameraPosition().target.latitude,
									mMap.getCameraPosition().target.longitude);

						onmap_latitude = mMap.getCameraPosition().target.latitude;

						onmap_longitude = mMap.getCameraPosition().target.longitude;

					} else {
						Toast.makeText(mContext, "No network connection",
								Toast.LENGTH_SHORT).show();
					}

				}

			};
		}
		// if (mMap != null) {
		//
		// mMap.setOnMarkerDragListener(new OnMarkerDragListener() {
		//
		// @Override
		// public void onMarkerDragStart(Marker marker) {
		// // TODO Auto-generated method stub
		// }
		//
		// @Override
		// public void onMarkerDragEnd(Marker marker) {
		// // TODO Auto-generated method stub
		// Log.i("After Drag Marker Lat Long",
		// "" + marker.getPosition().latitude + " "
		// + marker.getPosition().longitude);
		// mMap.animateCamera(CameraUpdateFactory.newLatLng(marker
		// .getPosition()));
		// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
		// onmap_latitude = marker.getPosition().latitude;
		// onmap_longitude = marker.getPosition().longitude;
		// getAddress(onmap_latitude, onmap_longitude);
		//
		// } else {
		// Toast.makeText(mContext, "No network connection",
		// Toast.LENGTH_SHORT).show();
		//
		// }
		// }
		//
		// @Override
		// public void onMarkerDrag(Marker marker) {
		// // TODO Auto-generated method stub
		// mMap.animateCamera(CameraUpdateFactory.newLatLng(marker
		// .getPosition()));
		//
		// }
		// });
		//
		// }
	}

	public void AddMarker(Double lat, Double lng, int icon) {

		if (lat != null && lng != null) {
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.draggable(true);
			markerOptions.position(new LatLng(lat, lng));
			markerOptions.icon(BitmapDescriptorFactory.fromResource(icon));

			mMap.addMarker(markerOptions);

		}
	}

	@Override
	public void onPause() {

		((MainFragmentActivity) getActivity()).setCustomBackPressListener(null);
		super.onPause();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub

		super.onDetach();
		// pbParcelable.setHailNowPopUpVisible(true);
		/*
		 * try {
		 * 
		 * ((Pre_BookFragment) previousFragment)
		 * .updateOnMapValues(pbParcelable); } catch (Exception e) { try {
		 * ((Hail_NowFragment) previousFragment)
		 * .updateHailNowData(pbParcelable); } catch (Exception b) {
		 * Log.i(getClass().getSimpleName(), "No fragment found on detach"); }
		 * e.printStackTrace(); }
		 */
	}

	public void getAddress(double latitude, double longitude) {
		// get address of particular location
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			GetAddressTask task = new GetAddressTask(getActivity());
			task.latitude = latitude;
			task.longitude = longitude;
			task.noloading_flag = true;
			task.mHandler = mhandler;
			if (currentLocation != null) {
				task.flag_hail = true;
				editor.putLong("presentLatitude", Double
						.doubleToRawLongBits(currentLocation.getLatitude()));
				editor.putLong("presentLongitude", Double
						.doubleToRawLongBits(currentLocation.getLongitude()));
				editor.commit();
			}
			task.execute("");
		} else {
			Toast.makeText(getActivity(), "No network connection",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);

		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

	}

	@Override
	public void onCustomBackPressed() {

		Log.i(getClass().getSimpleName(), "onCustomBackPressed");
		getParentFragment().getChildFragmentManager().popBackStack();

	}
}