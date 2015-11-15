package co.uk.android.lldc.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.async.DirectionsJSONParser;
import co.uk.android.lldc.custom.CustomMapTileProvider;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.models.TrailsWayPointModel;
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.tablet.MapNavigationActivity;
import co.uk.android.lldc.utils.Constants;
import co.uk.android.lldc.utils.DistanceUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MapTrailsFragment extends Fragment implements OnClickListener {
	MapView mapView;

	LocationManager locationManager;

	LatLng CENTER;

	GoogleMap map;

	ArrayList<ServerModel> venueList;

	ArrayList<LatLng> markerPoints = new ArrayList<LatLng>();

	private Bundle mBundle;

	View infoview;

	/** Venue Direction */
	RelativeLayout rlVenueDirection, rlCancel;;
	TextView tvVenueDirectionTitle, tvVenueDirectionDistance;

	boolean isMarkerSelected = false;

	Marker marker = null;

	String pageTitle = "";

	ProgressDialog dialog = null;

	double totalDistance = 0f;

	ArrayList<TrailsWayPointModel> waypointList = null;

	ArrayList<Marker> markerList = new ArrayList<Marker>();

	public static Handler mMapTrailsFragmentHandler = null;

	Location location = null;

	Marker userMarker = null;

	boolean sendOriginDest = false;

	Polyline line = null;

	/** for locking scroll and zoom */
	@SuppressWarnings("unused")
	private GroundOverlay mGroundOverlay;

	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;
		LLDCApplication.LOCATION_REQUEST_INTERVAL = 5000;
		if (HomeActivityTablet.mHomeActivityTabletHandler != null) {
			HomeActivityTablet.mHomeActivityTabletHandler
					.sendEmptyMessage(1070);
		}
		Constants.server_hit = false;
		mMapTrailsFragmentHandler = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1050) {

					Bundle bundle = message.getData();

					Double lat = bundle.getDouble("LAT");
					Double lan = bundle.getDouble("LAN");
					if (location == null) {
						location = new Location("");
					}
					location.setLatitude(lat);
					location.setLongitude(lan);

					LatLng latlng = new LatLng(lat, lan);

					if (userMarker == null) {
						createUserMarker();
					}
					if (userMarker != null)
						animateMarker(userMarker, latlng, false);
					if (!Constants.server_hit)
						onLoadTrails();
				} else if (message.what == 1040) {
					onLoadTrailsMarkers();
					LLDCApplication.removeSimpleProgressDialog();
				} else if (message.what == 1041) {
					Constants.server_hit = false;
				} else {
					CameraPosition position = map.getCameraPosition();
					VisibleRegion region = map.getProjection()
							.getVisibleRegion();
					float zoom = 0;
					if (position.zoom < LLDCApplication.MIN_ZOOM
							&& !LLDCApplication.isDebug)
						zoom = LLDCApplication.MIN_ZOOM;
					if (position.zoom > LLDCApplication.MAX_ZOOM
							&& !LLDCApplication.isDebug)
						zoom = LLDCApplication.MAX_ZOOM;
					LatLng correction = LLDCApplication
							.getLatLngCorrection(region.latLngBounds);
					if (!LLDCApplication.isDebug
							&& (zoom != 0 || correction.latitude != 0 || correction.longitude != 0)) {
						zoom = (zoom == 0) ? position.zoom : zoom;
						double lat = position.target.latitude
								+ correction.latitude;
						double lon = position.target.longitude
								+ correction.longitude;
						CameraPosition newPosition = new CameraPosition(
								new LatLng(lat, lon), zoom, position.tilt,
								position.bearing);
						CameraUpdate update = CameraUpdateFactory
								.newCameraPosition(newPosition);
						map.moveCamera(update);
					}
					/* Recursively call handler every 100ms */
					sendEmptyMessageDelayed(0, 100);
				}

			};
		};
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map_nav, container,
				false);

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
		/** For venue Direction */
		rlVenueDirection = (RelativeLayout) rootView
				.findViewById(R.id.rlVenueDirection);
		tvVenueDirectionTitle = (TextView) rootView
				.findViewById(R.id.tvVenueDirectionTitle);
		tvVenueDirectionDistance = (TextView) rootView
				.findViewById(R.id.tvVenueDirectionDistance);

		rlCancel = (RelativeLayout) rootView.findViewById(R.id.rlCancel);
		rlCancel.setOnClickListener(new OnitemClickListener());

		pageTitle = getActivity().getIntent().getStringExtra("PAGETITLE");

		mMapTrailsFragmentHandler.sendEmptyMessageDelayed(1040, 1000);

		LLDCApplication.TRIALROUTEDURATION = 0f;
		LLDCApplication.TRIALROUTEDISTANCE = 0f;

		return rootView;
	}

	class OnitemClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.rlCancel:
				MapNavigationActivity.finishActivity();
				break;

			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	public void onLoadSingleMarker() {
		LatLng latlng = new LatLng(
				Double.parseDouble(LLDCApplication.selectedModel.getLatitude()
						.toString().trim()),
				Double.parseDouble(LLDCApplication.selectedModel.getLongitude()
						.toString().trim()));
		markerPoints.add(latlng);
		MarkerOptions markerOptions = new MarkerOptions()
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.locationpin));
		// Setting the position of the marker
		markerOptions.position(latlng);
		markerOptions.title(LLDCApplication.selectedModel.getName());
		markerOptions
				.snippet(LLDCApplication.selectedModel.getVenueId() + ",1");
		// Add new marker to the Google Map Android
		// API V2
		Marker marker = map.addMarker(markerOptions);
		marker.showInfoWindow();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		// mGroundOverlay.remove();
		// map.clear();
	}

	@Override
	public void onDestroy() {
		LLDCApplication.LOCATION_REQUEST_INTERVAL = 30000;
		if (HomeActivityTablet.mHomeActivityTabletHandler != null) {
			HomeActivityTablet.mHomeActivityTabletHandler
					.sendEmptyMessage(1070);
		}
		mapView.onDestroy();
		if (mMapTrailsFragmentHandler != null) {
			mMapTrailsFragmentHandler.removeMessages(1050);
			mMapTrailsFragmentHandler.removeMessages(0);
			mMapTrailsFragmentHandler = null;
		}
		super.onDestroy();
	}

	public void onDestroyView() {
		super.onDestroyView();
		try {
			Fragment fragment = this;
			FragmentTransaction ft = getActivity().getSupportFragmentManager()
					.beginTransaction();
			ft.remove(fragment);
			ft.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setUpMapIfNeeded(View inflatedView) {
		if (map == null) {
			map = ((MapView) inflatedView.findViewById(R.id.mapView)).getMap();
			if (map != null) {
				setMapView();
			}
		}
	}

	private void setMapView() {
		try {
			MapsInitializer.initialize(getActivity());

			switch (GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getActivity())) {
			case ConnectionResult.SUCCESS:
				if (mapView != null) {

					locationManager = ((LocationManager) getActivity()
							.getSystemService(Context.LOCATION_SERVICE));

					Boolean localBoolean = Boolean.valueOf(locationManager
							.isProviderEnabled("network"));

					if (localBoolean.booleanValue()) {

						CENTER = new LatLng(
								LLDCApplication.parkCenter.getLatitude(),
								LLDCApplication.parkCenter.getLongitude());

					} else {

					}
					map = mapView.getMap();
					if (map == null) {

						Log.d("", "Map Fragment Not Found or no Map in it!!");

					}

					map.clear();
					// map.setOnMarkerClickListener(this);
					map.setIndoorEnabled(true);
					map.setMyLocationEnabled(false);
					map.moveCamera(CameraUpdateFactory.zoomTo(15));
					map.getUiSettings().setMapToolbarEnabled(false);
					// map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					map.setMapType(GoogleMap.MAP_TYPE_NONE);
					map.addTileOverlay(new TileOverlayOptions()
							.tileProvider(new CustomMapTileProvider(
									getResources().getAssets())));

					map.getUiSettings().setRotateGesturesEnabled(false);
					map.getUiSettings().setTiltGesturesEnabled(false);
					if (CENTER != null) {
						map.animateCamera(
								CameraUpdateFactory.newLatLng(CENTER), 1750,
								null);
					}
					map.setInfoWindowAdapter(new InfoWindowAdapter() {
						@SuppressLint("InflateParams")
						public View getInfoWindow(Marker marker) {
							// inflating layout

							View v = getActivity().getLayoutInflater().inflate(
									R.layout.mapcentre, null);

							TextView venueName = (TextView) v
									.findViewById(R.id.venueTitle);

							ImageView venueImage = (ImageView) v
									.findViewById(R.id.venueImage);

							venueName.setText(marker.getTitle());

							venueImage.setVisibility(View.GONE);
							String ImageUrl = waypointList.get(
									Integer.parseInt(marker.getSnippet().split(
											",")[2])).getPinImage();

							if (marker.getSnippet().split(",")[1].equals("1")) {
								venueName.setTextColor(Color.WHITE);
								marker.setSnippet(marker.getSnippet()
										.split(",")[0]
										+ ",0,"
										+ marker.getSnippet().split(",")[2]);
							} else {
								onHideDirection();
							}
							if (!ImageUrl.equals("")) {
								imagepickerdialog(
										marker.getTitle(),
										waypointList.get(
												Integer.parseInt(marker
														.getSnippet()
														.split(",")[2]))
												.getDescription(), ImageUrl);

								venueImage.setVisibility(View.GONE);
							}

							return v;
						}

						public View getInfoContents(Marker arg0) {

							return null;
						}
					});

				}
				break;
			case ConnectionResult.SERVICE_MISSING:

				break;
			case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:

				break;
			default:

			}
		} catch (Exception e) {

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btndirection) {
			onShowDirection();
		} else if (v.getId() == R.id.tvdiscovemore) {

		}
	}

	public void onShowDirection() {

		rlVenueDirection.setVisibility(View.VISIBLE);
		tvVenueDirectionTitle.setText(LLDCApplication.selectedModel.getName());
		if (LLDCApplication.TRIALROUTEDURATION == 0) {
			tvVenueDirectionDistance.setText(LLDCApplication.selectedModel
					.getActiveDays());
		} else {
			int[] time = splitToComponentTimes(new BigDecimal(round(
					(LLDCApplication.TRIALROUTEDURATION), 2)));
			// if (time[0] != 0) {
			tvVenueDirectionDistance.setText(time[0] + ":" + time[1] + " mins");
			LLDCApplication.TRIALROUTEDURATION = 0;
		}
		tvVenueDirectionTitle.setTextColor(Color
				.parseColor(LLDCApplication.selectedModel.getColor()));
		tvVenueDirectionDistance.setTextColor(Color
				.parseColor(LLDCApplication.selectedModel.getColor()));
	}

	public void onHideDirection() {
		// rlVenueDirection.setVisibility(View.GONE);
	}

	@SuppressWarnings("unused")
	private void onLoadTrailsMarkers() {
		try {
			waypointList = LLDCApplication.selectedModel.getWyapoitnList();
			markerList.clear();
			markerPoints.clear();
			if (mapView.getViewTreeObserver().isAlive()) {

				onApplyBounding();

				location = LLDCApplication.getLocation(getActivity());
				if (location != null) {
					if (userMarker == null) {
						createUserMarker();
					}
				}

				LatLngBounds.Builder bld1 = new LatLngBounds.Builder();

				for (int i = 0; i < waypointList.size(); i++) {
					LatLng latlng = new LatLng(Double.parseDouble(waypointList
							.get(i).getPinLat().toString().trim()),
							Double.parseDouble(waypointList.get(i).getPinLong()
									.toString().trim()));
					MarkerOptions markerOptions = new MarkerOptions();
					// if (i == 0) {
					// markerOptions.icon(BitmapDescriptorFactory
					// .fromResource(R.drawable.locationpin));
					// } else {
					markerOptions.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.trail_in));

					// }
					// Setting the position of the marker
					markerOptions.position(latlng);
					markerOptions.title(waypointList.get(i).getPinTitle());
					markerOptions.snippet(waypointList.get(i).getTailsId()
							+ ",0," + i);
					bld1.include(latlng);
					markerPoints.add(latlng);
					// Add new marker to the Google Map Android
					// API V2
					Marker marker = map.addMarker(markerOptions);
				}

				mMapTrailsFragmentHandler.sendEmptyMessage(0);
				LatLngBounds bounds = bld1.build();
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 12));
				onLoadTrails();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onLoadTrails() {
		try {

			if (waypointList.size() > 0) {

				// Getting URL to the Google Directions API
				String url = "";
				LatLng origin = null;
				if (location != null) {
					origin = new LatLng(location.getLatitude(),
							location.getLongitude());
				}
				Double lat = Double.parseDouble(LLDCApplication.selectedModel
						.getEndTrailLat());
				Double lan = Double.parseDouble(LLDCApplication.selectedModel
						.getEndTrailLong());

				LatLng dest = new LatLng(lat, lan);

				url = getDirectionUrl(origin, dest);

				if (LLDCApplication.isConnectingToInternet(getActivity())) {
					DownloadTask downloadTask = new DownloadTask(0);
					// Start downloading json data from Google
					// Directions
					// API
					downloadTask.execute(url);
				} else

					LLDCApplication.onShowToastMesssage(getActivity(),
							"Unable to connect internet");

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void onApplyBounding() {
		LatLngBounds.Builder bld = new LatLngBounds.Builder();

		bld.include(LLDCApplication.NEBOUND);
		bld.include(LLDCApplication.SWBOUND);

		LatLngBounds bounds = bld.build();
		map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));

		mMapTrailsFragmentHandler.sendEmptyMessage(0);

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

	private String getDirectionUrl(LatLng origin, LatLng dest) {

		if (origin == null) {
			sendOriginDest = false;
		}

		String url = "";
		if (LLDCApplication.isDebug) {
			url = LLDCApplication.DEVGETBASEDATA;
		} else {
			url = LLDCApplication.PRODGETBASEDATA;
		}
		if (!sendOriginDest) {
			sendOriginDest = true;
			url = url + "/lldcroutes.json?route_id="
					+ LLDCApplication.selectedModel.getRouteId();
		} else {
			url = url + "/lldcroutes.json?origin=" + origin.latitude + ","
					+ origin.longitude + "&destination=" + dest.latitude + ","
					+ dest.longitude + "&route_id="
					+ LLDCApplication.selectedModel.getRouteId();
		}
		return url;

	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		int routeNo = 0;

		public DownloadTask(int routeNo) {
			// TODO Auto-generated constructor stub
			this.routeNo = routeNo;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle("Fetching trails data");
			dialog.setMessage("Please Wait...");
			// if (line == null) {
			// dialog.show();
			// }
		}

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";
			if (LLDCApplication.isDebug)
				LLDCApplication.onShowToastMesssage(getActivity(),
						"Message received" + url[0]);
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
			if (LLDCApplication.isDebug)
				LLDCApplication.onShowToastMesssage(getActivity(),
						"Message Arrived" + result);
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}

			if (result.equals("")) {
				LLDCApplication.onShowToastMesssage(getActivity(),
						"No Trail's available");
				return;
			}

			ParserTask parserTask = new ParserTask(routeNo);
			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		int routeNo = 0;

		public ParserTask(int routeNo) {
			// TODO Auto-generated constructor stub
			this.routeNo = routeNo;
		}

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
				routes = parser.parse(jObject, routeNo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {

			try {
				Constants.server_hit = true;
				if (mMapTrailsFragmentHandler == null) {
					return;
				}
				mMapTrailsFragmentHandler.sendEmptyMessageDelayed(1041, 5000);
				if (result == null) {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
					return;
				}

				if (map == null) {
					return;
				}
				onShowDirection();
				if (LLDCApplication.TRIALROUTEDURATION != 0) {
					int[] time = splitToComponentTimes(new BigDecimal(round(
							(LLDCApplication.TRIALROUTEDURATION), 2)));
					tvVenueDirectionDistance.setText(time[0] + ":" + time[1]
							+ " mins");
				}
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (line != null && dialog.isShowing()) {
					return;
				}
				ArrayList<LatLng> points = null;
				PolylineOptions lineOptions = null;
				// Traversing through all the routes
				for (int i = 0; i < result.size(); i++) {
					points = new ArrayList<LatLng>();
					lineOptions = new PolylineOptions();
					// Fetching i-th route
					// List<HashMap<String, String>> path = result.get(i);
					List<HashMap<String, String>> path = result.get(i);

					// Fetching all the points in i-th route
					for (int j = 0; j < path.size(); j++) {
						HashMap<String, String> point = path.get(j);

						double lat = Double.parseDouble(point.get("lat"));
						double lng = Double.parseDouble(point.get("lng"));
						if (lat != 0 && lng != 0) {
							LatLng position = new LatLng(lat, lng);
							points.add(position);
						}
					}

					// Adding all the points in the route to LineOptions
				}
				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(5);
				lineOptions.color(Color.parseColor("#e6007e"));
				lineOptions.geodesic(true);
				lineOptions.zIndex(10000f);
				// Drawing polyline in the Google Map for the i-th route
				if (lineOptions != null) {
					line = map.addPolyline(lineOptions);
				}

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static int[] splitToComponentTimes(BigDecimal biggy) {
		long longVal = biggy.longValue();
		int hours = (int) longVal / 3600;
		int remainder = (int) longVal - hours * 3600;
		int mins = remainder / 60;
		remainder = remainder - mins * 60;
		int secs = remainder;

		int[] ints = { hours, mins, secs };
		return ints;
	}

	@SuppressWarnings("unused")
	private void drawDashedPolyLine(GoogleMap mMap, List<LatLng> listOfPoints,
			int color) {
		/* Boolean to control drawing alternate lines */
		boolean added = false;
		for (int i = 0; i < listOfPoints.size() - 1; i++) {
			/* Get distance between current and next point */
			double distance = getConvertedDistance(listOfPoints.get(i),
					listOfPoints.get(i + 1));

			/* If distance is less than 0.002 kms */
			if (distance < 0.002) {
				if (!added) {
					mMap.addPolyline(new PolylineOptions()
							.add(listOfPoints.get(i))
							.add(listOfPoints.get(i + 1)).color(color));
					added = true;
				} else {/* Skip this piece */
					added = false;
				}
			} else {
				/* Get how many divisions to make of this line */
				int countOfDivisions = (int) ((distance / 0.002));

				/* Get difference to add per lat/lng */
				double latdiff = (listOfPoints.get(i + 1).latitude - listOfPoints
						.get(i).latitude) / countOfDivisions;
				double lngdiff = (listOfPoints.get(i + 1).longitude - listOfPoints
						.get(i).longitude) / countOfDivisions;

				/*
				 * Last known indicates start point of polyline. Initialized to
				 * ith point
				 */
				LatLng lastKnowLatLng = new LatLng(
						listOfPoints.get(i).latitude,
						listOfPoints.get(i).longitude);
				for (int j = 0; j < countOfDivisions; j++) {

					/* Next point is point + diff */
					LatLng nextLatLng = new LatLng(lastKnowLatLng.latitude
							+ latdiff, lastKnowLatLng.longitude + lngdiff);
					if (!added) {
						mMap.addPolyline(new PolylineOptions()
								.add(lastKnowLatLng).add(nextLatLng)
								.color(color));
						added = true;
					} else {
						added = false;
					}
					lastKnowLatLng = nextLatLng;
				}
			}
		}
	}

	private double getConvertedDistance(LatLng latlng1, LatLng latlng2) {
		double distance = DistanceUtil.distance(latlng1.latitude,
				latlng1.longitude, latlng2.latitude, latlng2.longitude);
		BigDecimal bd = new BigDecimal(distance);
		BigDecimal res = bd.setScale(3, RoundingMode.DOWN);
		return res.doubleValue();
	}

	public void createUserMarker() {
		if (map != null && location != null && location.getLatitude() != 0
				&& location.getLongitude() != 0) {
			LatLng latlng = new LatLng(location.getLatitude(),
					location.getLongitude());
			markerPoints.add(latlng);
			MarkerOptions markerOptions = new MarkerOptions()
					.icon(BitmapDescriptorFactory.fromResource(R.drawable.user));
			// Setting the position of the marker
			markerOptions.position(latlng);
			markerOptions.title("User");
			markerOptions.snippet("1" + ",0,0");
			userMarker = map.addMarker(markerOptions);
		}
	}

	public void animateMarker(final Marker marker, final LatLng toPosition,
			final boolean hideMarker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = map.getProjection();
		Point startPoint = proj.toScreenLocation(marker.getPosition());
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 500;

		final LinearInterpolator interpolator = new LinearInterpolator();

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

	public void imagepickerdialog(String title, String description,
			String imageUrl) {
		final Dialog imageDialog = new Dialog(getActivity(),
				android.R.style.Theme_Translucent);

		imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		imageDialog.setContentView(R.layout.dialog_trails);
		imageDialog.setCancelable(true);
		imageDialog.setCanceledOnTouchOutside(true);

		ImageView iv_trails = (ImageView) imageDialog
				.findViewById(R.id.iv_dialog_trails);
		TextView tv_trails_title = (TextView) imageDialog
				.findViewById(R.id.tv_trails_dialog_title);
		TextView tv_trails_desc = (TextView) imageDialog
				.findViewById(R.id.tv_trails_dialog_desc);
		RelativeLayout rel_trails_close = (RelativeLayout) imageDialog
				.findViewById(R.id.rel_close);
		rel_trails_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (imageDialog != null && imageDialog.isShowing()) {
					imageDialog.dismiss();
				}
			}
		});

		// ImageLoader.getInstance().displayImage(
		// imageUrl + "&width=" + LLDCApplication.screenWidth
		// + "&height=300", iv_trails);

		ImageLoader.getInstance().displayImage(imageUrl, iv_trails);

		// iv_trails.setImageUrl(imageUrl, ImageCacheManager.getInstance()
		// .getImageLoader());
		// iv_trails.setDefaultImageResId(R.drawable.qeop_placeholder);
		// iv_trails.setErrorImageResId(R.drawable.imgnt_placeholder);

		tv_trails_title.setText(title);
		tv_trails_desc.setText(description);

		imageDialog.show();

	}
}
