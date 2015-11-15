package uk.co.pocketapp.whotel.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.pocketapp.whotel.FragmentLoad;
import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.customview.MapWrapperLayout;
import uk.co.pocketapp.whotel.receivers.NetworkBroadcastReceiver;
import uk.co.pocketapp.whotel.util.DirectionsJSONParser;
import uk.co.pocketapp.whotel.util.Util;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

@SuppressLint("ValidFragment")
public class MapGetDirectionFragment extends SherlockFragment {
	private GoogleMap map;

	FragmentLoad fragmentLoad;
	String TAG = "MapGetDirectionFragment";

	Marker previousMarker = null;

	MapWrapperLayout mapWrapperLayout;
	ProgressDialog pDialog;
	Location sourceLocation;
	LatLng destinationLatLng;
	String szIsWhotel;

	public MapGetDirectionFragment(FragmentLoad fragmentLoad,
			LatLng destinationLatLng, String szIsWhotel) {
		// TODO Auto-generated constructor stub
		this.fragmentLoad = fragmentLoad;
		this.destinationLatLng = destinationLatLng;
		this.szIsWhotel = szIsWhotel;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		mapWrapperLayout = (MapWrapperLayout) view
				.findViewById(R.id.map_relative_layout);
		return view;
	}

	public void onDestroyView() {
		super.onDestroyView();
		Log.d("MapGetDirectionFragment", "onDestroy-()");
		try {
			SupportMapFragment mapFragment = (SupportMapFragment) getActivity()
					.getSupportFragmentManager().findFragmentById(
							R.id.map_location);
			android.support.v4.app.FragmentTransaction ft = getActivity()
					.getSupportFragmentManager().beginTransaction();
			ft.remove(mapFragment);
			ft.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		fragmentLoad.onMapGetDirectionFragmentLoad(this);

		SupportMapFragment smf = (SupportMapFragment) getActivity()
				.getSupportFragmentManager()
				.findFragmentById(R.id.map_location);

		// Getting GoogleMap object from the fragment
		if (smf.getMap() != null && map == null) {
			map = smf.getMap();
			// map.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
		}

		if (map != null) {

			// Enabling MyLocation Layer of Google Map
			map.setMyLocationEnabled(false);
			map.setTrafficEnabled(true);

			map.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					// TODO Auto-generated method stub
					if (marker.equals(previousMarker)) {
						previousMarker.hideInfoWindow();
						return true;
					}
					return false;
				}
			});
		}

		sourceLocation = Util.getLocation();
		drawMarkerCurrentLocation(sourceLocation);
		drawMarkerDestination(destinationLatLng);

		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			GetDirection task = new GetDirection();
			task.source = sourceLocation;
			task.destination = destinationLatLng;

			// CameraUpdate cameraUpdate = CameraUpdateFactory
			// .newLatLngZoom(new LatLng(
			// markerLatLng.latitude,
			// markerLatLng.longitude), 5);
			// map.animateCamera(cameraUpdate);

			if (sourceLocation != null) {
				pDialog = new ProgressDialog(getActivity());
				pDialog.setMessage("Loading route. Please wait...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				task.execute();
			} else {
				if (!isGPSActivated())
					showGPSAlertDialog();
				else
					showAlertDialog();
			}
		} else
			Toast.makeText(getActivity(),
					"No network, please try after some time", Toast.LENGTH_LONG)
					.show();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (getActivity().getSupportFragmentManager().findFragmentByTag(TAG) == null) {
			final FragmentTransaction ft = getActivity()
					.getSupportFragmentManager().beginTransaction();
			ft.add(R.id.main_frame, new MapGetDirectionFragment(fragmentLoad,
					destinationLatLng, szIsWhotel), TAG);
			ft.commit();
		}

		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void drawMarkerDestination(LatLng destLatLng) {
		if (destLatLng != null) {

			if (szIsWhotel != null && szIsWhotel.equalsIgnoreCase("yes")) {
				map.addMarker(new MarkerOptions()
						.position(destLatLng)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.whotel_insider_map_purple_pin_w)));
			} else {
				map.addMarker(new MarkerOptions()
						.position(destLatLng)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.whotel_insider_map_pink_pin)));
			}
		}
	}

	public void drawMarkerCurrentLocation(Location location) {
		if (location != null) {
			if (previousMarker != null)
				previousMarker.remove();
			LatLng currentPosition = new LatLng(location.getLatitude(),
					location.getLongitude());
			map.setMyLocationEnabled(false);
			previousMarker = map.addMarker(new MarkerOptions().position(
					currentPosition).icon(
					BitmapDescriptorFactory
							.fromResource(R.drawable.current_location_icon)));

			map.animateCamera(CameraUpdateFactory.zoomIn());
			CameraUpdate cameraUpdate = CameraUpdateFactory
					.newLatLngZoom(
							new LatLng(location.getLatitude(), location
									.getLongitude()), 14.0f);
			map.animateCamera(cameraUpdate);
		}
	}

	class GetDirection extends AsyncTask<String, String, String> {
		List<LatLng> polyz;
		JSONArray array;

		public Location source;
		public LatLng destination;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... args) {

			String stringUrl = getDirectionsUrl(source, destination);

			Log.d("MapLocationFragment-GetDirection", " MAP DIRECTION URL ::"
					+ stringUrl);

			// StringBuilder response = new StringBuilder();
			String response = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // timeout
				// value
				HttpGet request = new HttpGet();
				request.setURI(new URI(stringUrl));
				request.setParams(httpParams);
				HttpResponse httpResponse = client.execute(request);
				response = inputStreamToString(
						httpResponse.getEntity().getContent()).toString();

				// // Log.d("*************", " RESULTTT RESPONSE ::" +
				// response);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return response;

		}

		protected void onPostExecute(String result) {
			ParserTask parserTask = new ParserTask();
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
		}
	}

	// * A class to parse the Google Places in JSON format*/

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
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			MarkerOptions markerOptions = new MarkerOptions();

			if (result != null && result.size() > 0) {
				// Traversing through all the routes
				for (int i = 0; i < result.size(); i++) {
					points = new ArrayList<LatLng>();
					lineOptions = new PolylineOptions();

					// Fetching i-th route
					List<HashMap<String, String>> path = result.get(i);

					// Fetching all the points in i-th route
					for (int j = 0; j < path.size(); j++) {
						HashMap<String, String> point = path.get(j);

						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						LatLng position = new LatLng(lat, lng);

						points.add(position);
					}

					// Adding all the points in the route to LineOptions
					lineOptions.addAll(points);
					lineOptions.width(5);
					lineOptions
							.color(getResources().getColor(R.color.map_path));
				}

				// Drawing polyline in the Google Map for the i-th route
				map.addPolyline(lineOptions);
			} else {
				Toast.makeText(getActivity(), "No direction found!",
						Toast.LENGTH_LONG).show();
			}
			if (pDialog != null) {
				pDialog.dismiss();
				pDialog = null;
			}
		}
	}

	private StringBuilder inputStreamToString(InputStream content)
			throws IOException {
		// TODO Auto-generated method stub
		String line = "";
		StringBuilder total = new StringBuilder();

		BufferedReader rd = new BufferedReader(new InputStreamReader(content));
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}
		return total;
	}

	private String getDirectionsUrl(Location origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.getLatitude() + ","
				+ origin.getLongitude();

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

	void showGPSAlertDialog() {

		new AlertDialog.Builder(getActivity())
				.setMessage("Enable GPS")
				.setCancelable(false)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						getActivity()
								.startActivity(
										new Intent(
												Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).create().show();
	}

	void showAlertDialog() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
		alertDialog.setTitle("Location not found!");
		alertDialog.setMessage("Unable to get a fix on your location.");
		alertDialog.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});

		alertDialog.show();
	}

	private boolean isGPSActivated() {
		return ((LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE))
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	public void onBackPressed() {
		fragmentLoad.onWebViewFragmentClosed();
	}

}
