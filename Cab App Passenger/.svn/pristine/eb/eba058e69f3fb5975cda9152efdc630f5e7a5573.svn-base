package com.example.cabapppassenger.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.GetFavouritesTask;
import com.example.cabapppassenger.task.SettingsTask;
import com.example.cabapppassenger.task.TrackDriverTask;
import com.example.cabapppassenger.util.RoutesParser;
import com.example.cabapppassenger.util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class TrackDriverFragment extends RootFragment implements
		OnCustomBackPressedListener {

	private SupportMapFragment mMapFragment;
	private ViewGroup mMapFrame;
	public GoogleMap mMap;
	Location currentLocation = null;
	Handler mHandler;
	Editor editor;
	SharedPreferences shared_pref;
	Geocoder geocoder;
	String PREF_NAME = "CabApp_Passenger";
	Timer timer;
	List<Address> address;
	ImageView ivBack;
	TimerTask timer_task;
	TextView tv_plswait, tv_done, tv_time, tv_cabs;
	LatLng currentLoc = null;
	String driver_id, driver_firstname, driver_secondname, driver_vehicleno,
			driver_vehiclemodel, driver_vehiclecolor;
	Double driver_latitude, driver_longitude, pickup_latitude, distance, time,
			pickup_longitude;
	Context mContext;
	DecimalFormatSymbols decimalFormatSymbols;
	DecimalFormat decimalFormat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		MapsInitializer.initialize(getActivity().getApplicationContext());
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setGroupingSeparator('.');
		decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);
		editor = shared_pref.edit();
		Bundle bundle = getArguments();
		if (bundle != null) {
			driver_id = bundle.getString("DriverId");
			driver_firstname = bundle.getString("DriverFirstname");
			driver_secondname = bundle.getString("DriverSecondname");
			driver_vehiclemodel = bundle.getString("VehicleModel");
			driver_vehicleno = bundle.getString("VehicleReg");
			driver_vehiclecolor = bundle.getString("VehicleColor");
			pickup_latitude = bundle.getDouble("Pickup_latitude", 0);
			pickup_longitude = bundle.getDouble("Pickup_longitude", 0);

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_trackdriver, container,
				false);
		ivBack = (ImageView) view.findViewById(R.id.ivBack);
		mMapFrame = (ViewGroup) view.findViewById(R.id.map_frame);
		tv_plswait = (TextView) view.findViewById(R.id.tv_plswait);
		if (driver_firstname != null && driver_secondname != null
				&& driver_vehiclecolor != null && driver_vehicleno != null
				&& !driver_firstname.equals("null")
				&& !driver_secondname.equals("null")
				&& !driver_vehicleno.equals("null")
				&& !driver_vehiclecolor.equals("null"))
			tv_plswait.setText(driver_firstname + " "
					+ driver_secondname.substring(0, 1) + " "
					+ driver_vehicleno + " " + driver_vehiclecolor);
		tv_cabs = (TextView) view.findViewById(R.id.tv_cabs);
		tv_time = (TextView) view.findViewById(R.id.tv_time);
		tv_done = (TextView) view.findViewById(R.id.tv_cancel);
		tv_done.setVisibility(View.VISIBLE);
		tv_done.setText("Done");
		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getParentFragment().getChildFragmentManager().popBackStack();
			}
		});
		tv_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HailNowParentFragment edit = new HailNowParentFragment();
				FragmentTransaction ft = getActivity()
						.getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.frame_container, edit, "hailNowParentFragment");
				ft.commit();
				((MainFragmentActivity) mContext).refresh_menu(0);
			}
		});
		mMapFragment = SupportMapFragment.newInstance();
		FragmentTransaction fragmentTransaction = getChildFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(R.id.map_frame, mMapFragment);
		fragmentTransaction.commit();

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				LatLngBounds.Builder bld = null;
				LatLng driver = null, passenger = null;
				if (msg != null && msg.peekData() != null) {
					Bundle bundledata = (Bundle) msg.obj;
					if (bundledata != null) {

						if (bundledata.containsKey("DriverLatitude")) {
							driver_latitude = bundledata.getDouble(
									"DriverLatitude", 0);
							Log.e("Driver Lat", "" + driver_latitude);
						}

						if (bundledata.containsKey("DriverLongitude")) {
							driver_longitude = bundledata.getDouble(
									"DriverLongitude", 0);
							Log.e("Driver Long", "" + driver_longitude);
						}
						// if (bundledata.containsKey("Distance")) {
						// distance = bundledata.getDouble("Distance", 0);
						// Log.e("Driver Distance", "" + distance);
						// }
						// if (bundledata.containsKey("Time")) {
						// time = bundledata.getDouble("Time", 0);
						// Log.e("Driver Long", "" + time);
						// }
						// update(distance, time);
						driver = new LatLng(driver_latitude, driver_longitude);
						passenger = new LatLng(pickup_latitude,
								pickup_longitude);

						bld = new LatLngBounds.Builder();

						bld.include(driver);
						bld.include(passenger);
					}
					if (driver_latitude != 0 && driver_longitude != 0)
						AddMarker(driver_latitude, driver_longitude,
								R.drawable.cabsarround);
					LatLngBounds bounds = bld.build();
					mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,
							50));
					if (pickup_latitude != null && pickup_longitude != null
							&& driver_latitude != null
							&& driver_longitude != null) {
						String url = getDirectionsUrl(driver, passenger);
						Log.e("URL", url);

						if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

							DownloadTask downloadTask = new DownloadTask();// Start
																			// downloading
																			// json
																			// data
																			// from
																			// Google
							// Directions
							// API
							downloadTask.execute(url);

							if (Util.getLocation(getActivity()) != null)
								currentLocation = Util
										.getLocation(getActivity());
							if (currentLocation != null) {
								currentLoc = new LatLng(
										currentLocation.getLatitude(),
										currentLocation.getLongitude());

								AddMarker(currentLocation.getLatitude(),
										currentLocation.getLongitude(),
										R.drawable.locationicon);
							}
						} else {
							Toast.makeText(mContext, "No network connection",
									Toast.LENGTH_SHORT).show();
						}
					}
					if (bundledata.containsKey("Available Cab Response")) {
						availablecab_jsonparser(bundledata.getString(
								"Available Cab Response", ""));
					}
					// if (driver_latitude != 0 && driver_longitude != 0)
					//
					// {
					// LatLng origin = new LatLng(driver_latitude,
					// driver_longitude);
					// LatLng dest = new LatLng(pickup_latitude,
					// pickup_longitude);
					// String url = getDirectionsUrl(origin, dest);
					//
					// DownloadTask downloadTask = new DownloadTask();
					//
					// // Start downloading json data from Google
					// // Directions API
					// downloadTask.execute(url);
					// }
				}
			}

		};

		timer = new Timer();
		timer_task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				// API Hit
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
							TrackDriverTask task = new TrackDriverTask(mContext);
							task.mHandler = mHandler;
							task.driver_id = driver_id;
							task.execute("");
						} else {
							Toast.makeText(mContext, "No network conncetion.",
									Toast.LENGTH_SHORT).show();
						}
					}

				});
			}
		};
		timer.scheduleAtFixedRate(timer_task, 0,
				shared_pref.getLong("DriverUpdate", 15) * 1000);
		super.initWidgets(view, this.getClass().getName());
		return view;
	}

	private void update(String distance, String time) {
		// TODO Auto-generated method stub
		if (time != null)
			tv_time.setText(time);
		else
			tv_time.setText("0 Min");

		// cabs
		if (distance != null)
			tv_cabs.setText(distance + " Mile");

		else
			tv_time.setText("0 Mile");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		mMap = mMapFragment.getMap();
		MainFragmentActivity.slidingMenu
				.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		if (pickup_latitude != 0 && pickup_longitude != 0)
			AddMarker(pickup_latitude, pickup_longitude, R.drawable.passenger);

		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(false);

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

	@SuppressWarnings("unused")
	public void availablecab_jsonparser(String response) {
		// TODO Auto-generated method stub
		JSONObject Javailablecabs_obj = null;
		String totalDrivers, time, distance;
		try {
			Javailablecabs_obj = new JSONObject(response);
			if (Javailablecabs_obj != null) {

				// time = Javailablecabs_obj.getString("eta");
				// if (time.equals("false"))
				// time = "0";
				// else if (time.equals("true"))
				// time = "1";

				// distance = Javailablecabs_obj.getString("distance");
				// editor.putString("CabTime", time);
				// editor.putString("Distance", distance);
				// editor.commit();
				// if (time != null)
				// if (time.matches("0") || time.matches("1"))
				// tv_time.setText(time + " Min");
				//
				// else if (time != null && !time.matches("0")
				// && !time.matches("1"))
				// tv_time.setText(time + " Mins");
				// else
				// tv_time.setText("0 Min");

				// cabs
				// if (distance != null)
				// if (distance.matches("0") || distance.matches("1"))
				// tv_cabs.setText(distance + " Mile");
				//
				// else if (distance != null && !distance.matches("0")
				// && !distance.matches("1"))
				// tv_cabs.setText(distance + " Miles");
				// else
				// tv_time.setText("0 Cab");

				JSONArray jArr_drivers = Javailablecabs_obj
						.getJSONArray("drivers");
				if (jArr_drivers != null && jArr_drivers.length() > 0) {
					for (int i = 0; i < jArr_drivers.length(); i++) {
						JSONObject jobj = jArr_drivers.getJSONObject(i);
						Double cablatitude = jobj.getDouble("latitude");
						Double cablongitude = jobj.getDouble("longitude");
						String forename = jobj.getString("forename");
						if (cablatitude != null && cablongitude != null
								&& cablatitude != 0.0 && cablongitude != 0.0) {
						}

					}
				}
			} else {
				tv_time.setText("0 Min");
				tv_cabs.setText("0 Mile");
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

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		timer.cancel();

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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((MainFragmentActivity) getActivity()).setCustomBackPressListener(this);
		if (MainFragmentActivity.slidingMenu != null)
			MainFragmentActivity.slidingMenu
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
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
				RoutesParser parser = new RoutesParser();

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
			// ArrayList<LatLng> points = null;
			// PolylineOptions lineOptions = null;
			// MarkerOptions markerOptions = new MarkerOptions();
			String distance = "";
			String duration = "";

			if (result.size() < 1) {
				Toast.makeText(mContext, "No Points", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				// points = new ArrayList<LatLng>();
				// lineOptions = new PolylineOptions();

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

					// double lat = Double.parseDouble(point.get("lat"));
					// double lng = Double.parseDouble(point.get("lng"));
					// LatLng position = new LatLng(lat, lng);

					// points.add(position);
				}

				// Adding all the points in the route to LineOptions
				// lineOptions.addAll(points);
				// lineOptions.width(2);
				// lineOptions.color(Color.RED);

			}
			distance = distance.replaceAll("[a-zA-Z]+", "");

			Log.e("Distance", distance + "" + duration);
			Double dist = Double.valueOf(distance.trim());
			dist = dist * 0.621371;
			String distance_val = decimalFormat.format(dist);
			update(distance_val, duration);
			// Drawing polyline in the Google Map for the i-th route
		}
	}

}
