package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.tablet.LLDCApplication;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NewMapNavFragment extends Fragment {

	MapView mapView;
	Bundle mBundle;
	Location location;
	GoogleMap map;
	ArrayList<LatLng> markerPoints = new ArrayList<LatLng>();
	/** Venue Direction */
	LocationManager loc_mgr;
	LinearLayout rlVenueDirection;

	LocationListener listener;
	TextView tvVenueDirectionTitle, tvVenueDirectionDistance;
	Marker userMarker = null;
	String pageTitle = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);
		loc_mgr = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		listener = new LocationListener() {

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLocationChanged(Location loc) {
				// TODO Auto-generated method stub

				location = loc;
				if (userMarker != null) {
					LatLng latlng_user = new LatLng(loc.getLatitude(),
							loc.getLongitude());
					animateMarker(userMarker, latlng_user, false);

				} else {
					// LatLng latlng_user = new LatLng(loc.getLatitude(),
					// loc.getLongitude());
					// markerPoints.add(latlng_user);
					// MarkerOptions markerOptions = new MarkerOptions()
					// .icon(BitmapDescriptorFactory.fromResource(R.drawable.user));
					// // Setting the position of the marker
					// markerOptions.position(latlng_user);
					// markerOptions.title("User");
					// markerOptions.snippet("1" + ",0,0");
					// userMarker = map.addMarker(markerOptions);
					// map.animateCamera(CameraUpdateFactory
					// .newLatLngZoom(latlng_user, 14));+
					LLDCApplication.onShowToastMesssage(getActivity(),
							"Maker null mila");
				}
				// String msg = "Updated Location: "
				// + Double.toString(location.getLatitude()) + ","
				// + Double.toString(location.getLongitude());
				// Toast.makeText(getActivity(), msg,
				// Toast.LENGTH_SHORT).show();
			}
		};
		loc_mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				listener);
		loc_mgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
				listener);
		mapView = (MapView) rootView.findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);

		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mapView.onCreate(mBundle);
		setUpMapIfNeeded(rootView);

		// setMapView();
		/** For venue Direction */
		rlVenueDirection = (LinearLayout) rootView
				.findViewById(R.id.rlVenueDirection);
		tvVenueDirectionTitle = (TextView) rootView
				.findViewById(R.id.tvVenueDirectionTitle);
		tvVenueDirectionDistance = (TextView) rootView
				.findViewById(R.id.tvVenueDirectionDistance);
		pageTitle = getActivity().getIntent().getStringExtra("PAGETITLE");

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onDestroy() {
		mapView.onDestroy();
		super.onDestroy();
	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (map == null) {
			map = ((MapView) inflatedView.findViewById(R.id.mapView)).getMap();
			if (map != null) {
				setMapView();
			}
		}
	}

	public void setMapView() {
		try {
			// MapsInitializer.initialize(getActivity());
			switch (GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getActivity())) {
			case ConnectionResult.SUCCESS:
				if (mapView != null) {
					map = mapView.getMap();
					if (map == null) {
						Log.d("", "Map Fragment Not Found or no Map in it!!");
					}
					map.clear();
					map.setIndoorEnabled(true);
					map.setMyLocationEnabled(false);
					map.moveCamera(CameraUpdateFactory.zoomTo(15));
					map.getUiSettings().setMapToolbarEnabled(false);
					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					map.getUiSettings().setRotateGesturesEnabled(false);
					map.getUiSettings().setTiltGesturesEnabled(false);
					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					onLoadMarkers();
				}
				break;
			default:

			}
		} catch (Exception e) {

		}

	}

	public void onLoadMarkers() {
		try {
			if (mapView.getViewTreeObserver().isAlive()) {

				LatLng latlng = new LatLng(
						Double.parseDouble(LLDCApplication.selectedModel
								.getLatitude().toString().trim()),
						Double.parseDouble(LLDCApplication.selectedModel
								.getLongitude().toString().trim()));
				markerPoints.add(latlng);
				MarkerOptions markerOptions = new MarkerOptions()
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.locationpin));
				// Setting the position of the marker
				markerOptions.position(latlng);
				markerOptions.title(LLDCApplication.selectedModel.getName());
				markerOptions
						.snippet(LLDCApplication.selectedModel.get_id() + ",0,"
								+ LLDCApplication.selectedModel.getEventCount());
				// Add new marker to the Google Map Android
				// API V2
				Marker marker = map.addMarker(markerOptions);
				marker.showInfoWindow();
				location = LLDCApplication.getLocation(getActivity());
				// if (LLDCApplication.isInsideThePark) {
				LLDCApplication.isInsideThePark = true;
				if (location != null && LLDCApplication.isInsideThePark) {
					LatLng latlng_user = new LatLng(location.getLatitude(),
							location.getLongitude());
					markerPoints.add(latlng_user);
					markerOptions = new MarkerOptions()
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.user));
					// Setting the position of the marker
					markerOptions.position(latlng_user);
					markerOptions.title("User");
					markerOptions.snippet("1" + ",0,0");
					userMarker = map.addMarker(markerOptions);
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(
							latlng_user, 14));
				} else {
					map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,
							14));
					map.moveCamera(CameraUpdateFactory
							.newLatLngZoom(latlng, 15));

					LLDCApplication
							.onShowToastMesssage(
									getActivity(),
									"Appears you are outside of the park. Navigation will work when you car inside the park...");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TODO Auto-generated method stub
	// location = loc;
	// if (userMarker != null) {
	// LatLng latlng_user = new LatLng(loc.getLatitude(),
	// loc.getLongitude());
	// MarkerOptions a = new MarkerOptions().position(latlng_user);
	// Marker m = map.addMarker(a);
	// m.setPosition(new LatLng(50, 5));
	//
	// } else {
	// // LatLng latlng_user = new LatLng(loc.getLatitude(),
	// // loc.getLongitude());
	// // markerPoints.add(latlng_user);
	// // MarkerOptions markerOptions = new MarkerOptions()
	// // .icon(BitmapDescriptorFactory.fromResource(R.drawable.user));
	// // // Setting the position of the marker
	// // markerOptions.position(latlng_user);
	// // markerOptions.title("User");
	// // markerOptions.snippet("1" + ",0,0");
	// // userMarker = map.addMarker(markerOptions);
	// // map.animateCamera(CameraUpdateFactory
	// // .newLatLngZoom(latlng_user, 14));+
	// LLDCApplication.onShowToastMesssage(getActivity(),
	// "Maker null mila");
	// }

	public void animateMarker(final Marker marker, final LatLng toPosition,
			final boolean hideMarker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = map.getProjection();
		Point startPoint = proj.toScreenLocation(marker.getPosition());
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 500;

		final Interpolator interpolator = new LinearInterpolator();

		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * toPosition.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * toPosition.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));

				if (t < 1.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				} else {
					if (hideMarker) {
						marker.setVisible(false);
					} else {
						marker.setVisible(true);
					}
				}
			}
		});
	}
}
