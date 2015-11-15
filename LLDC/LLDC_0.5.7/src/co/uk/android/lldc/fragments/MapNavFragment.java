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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.async.DirectionsJSONParser;
import co.uk.android.lldc.custom.CustomMapTileProvider;
import co.uk.android.lldc.models.FacilityModel;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.models.TrailsWayPointModel;
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

public class MapNavFragment extends Fragment {
	MapView mapView;

	LocationManager locationManager;
	Location location = new Location("");
	LatLng CENTER;
	Polyline line = null;
	GoogleMap map;
	ArrayList<ServerModel> venueList;
	ArrayList<LatLng> markerPoints = new ArrayList<LatLng>();
	Bundle mBundle;

	View infoview;

	/** Venue Direction */
	LinearLayout rlVenueDirection;
	TextView tvVenueDirectionTitle, tvVenueDirectionDistance;

	boolean isMarkerSelected = false;

	String pageTitle = "";

	ProgressDialog dialog = null;

	ArrayList<TrailsWayPointModel> waypointList = null;

	ArrayList<Marker> markerList = new ArrayList<Marker>();

	Marker userMarker = null;

	public static Handler mMapNavFragmentHandler = null;

	boolean isDialogOpen = false;

	/** for locking scroll and zoom */
	@SuppressWarnings("unused")
	private GroundOverlay mGroundOverlay;

	// Location center = new Location("");

	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;

		mMapNavFragmentHandler = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1050) {

					Bundle bundle = message.getData();
					Double lat = bundle.getDouble("LAT");
					Double lan = bundle.getDouble("LAN");

					if (LLDCApplication.isDebug) {
						LLDCApplication.onShowToastMesssage(getActivity(),
								"Location send to Map Navigation Fragment Lat: "
										+ lat + "Long: " + lan);
					}

					location.setLatitude(lat);
					location.setLongitude(lan);

					LatLng latlng = new LatLng(lat, lan);

					if (userMarker == null) {
						createUserMarker();
					}

					if (!Constants.server_hit)
						onGetRoute();

					if (userMarker != null)
						animateMarker(userMarker, latlng, false);

					if (!LLDCApplication.isInsideThePark && !isDialogOpen)
						showOutSideParkDialog();

				} else if (message.what == 1040) {
					onLoadMarkers();
				} else if(message.what == 1041){
					Constants.server_hit = false;
				}else {
					CameraPosition position = map.getCameraPosition();
					VisibleRegion region = map.getProjection()
							.getVisibleRegion();
					float zoom = 0;
					if (position.zoom < LLDCApplication.MIN_ZOOM && !LLDCApplication.isDebug)
						zoom = LLDCApplication.MIN_ZOOM;
					if (position.zoom > LLDCApplication.MAX_ZOOM && !LLDCApplication.isDebug)
						zoom = LLDCApplication.MAX_ZOOM;
					
					LatLng correction = LLDCApplication
							.getLatLngCorrection(region.latLngBounds);
					
					if (!LLDCApplication.isDebug && (zoom != 0 || correction.latitude != 0
							|| correction.longitude != 0)) {
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
//					if (!LLDCApplication.isDebug) {
						sendEmptyMessageDelayed(0, 100);
//					}
				}
			}
		};

	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container,
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

		setMapView();

		/** For venue Direction */
		rlVenueDirection = (LinearLayout) rootView
				.findViewById(R.id.rlVenueDirection);
		tvVenueDirectionTitle = (TextView) rootView
				.findViewById(R.id.tvVenueDirectionTitle);
		tvVenueDirectionDistance = (TextView) rootView
				.findViewById(R.id.tvVenueDirectionDistance);

		pageTitle = getActivity().getIntent().getStringExtra("PAGETITLE");

		mMapNavFragmentHandler.sendEmptyMessageDelayed(1040, 500);

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
		// mGroundOverlay.remove();
		// map.clear();
	}

	@Override
	public void onDestroy() {
		mapView.onDestroy();
		mMapNavFragmentHandler = null;
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

	private void setMapView() {
		try {
			MapsInitializer.initialize(getActivity());

			switch (GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getActivity())) {
			case ConnectionResult.SUCCESS:
				if (mapView != null) {

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
					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

					map.setInfoWindowAdapter(new InfoWindowAdapter() {
						@SuppressWarnings("unused")
						@SuppressLint("InflateParams")
						public View getInfoWindow(Marker marker) {
							// inflating layout

							View v = getActivity().getLayoutInflater().inflate(
									R.layout.map_center, null);

							try {
								RelativeLayout rel_main = (RelativeLayout) v
										.findViewById(R.id.linearmain);
								TextView venueName = (TextView) v
										.findViewById(R.id.venueTitle);
								TextView venueCount = (TextView) v
										.findViewById(R.id.eventcount);

								ImageView venueImage = (ImageView) v
										.findViewById(R.id.venueImage);

								venueName.setText(marker.getTitle());

								venueCount.setText(marker.getSnippet().split(
										",")[2]);

								venueCount.setVisibility(View.GONE);

								if (marker.getSnippet().split(",")[1]
										.equals("1")) {
									venueName.setTextColor(Color.WHITE);
									venueName
											.setBackgroundResource(R.drawable.roundedinfowindow_selected);
									venueCount.setVisibility(View.VISIBLE);
									marker.setSnippet(marker.getSnippet()
											.split(",")[0]
											+ ",0,"
											+ marker.getSnippet().split(",")[2]);
								} else {
									onHideDirection();
								}

								CameraUpdate center = CameraUpdateFactory
										.newLatLng(marker.getPosition());
								map.moveCamera(center);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
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

	private void onLoadMarkers() {
		try {
			// venueList = LLDCApplication.DBHelper.onGetVenueData("");
			if (mapView.getViewTreeObserver().isAlive()) {

				// if (!LLDCApplication.isDebug) {
				onApplyBounding();
				// }

				location = LLDCApplication.getLocation(getActivity());
				if (location != null) {
					if (userMarker == null) {
						createUserMarker();
					}
				}
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

				if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.VENUE
						|| LLDCApplication.selectedModel.getModelType() == LLDCApplication.FACILITIES) {
					ArrayList<FacilityModel> facilityList = LLDCApplication.selectedModel
							.getVenueFacilityList();
					for (int i = 0; i < facilityList.size(); i++) {
						if (!facilityList.get(i).getLat().equals("")
								&& !facilityList.get(i).getLon().equals("")) {
							latlng = new LatLng(
									(Double.parseDouble(facilityList.get(i)
											.getLat().toString().trim())),
									(Double.parseDouble(facilityList.get(i)
											.getLon().toString().trim())) - 0.000001);
							markerPoints.add(latlng);

							switch (Integer.parseInt(facilityList.get(i)
									.getFacility_id())) {
							case 1:
								markerOptions = new MarkerOptions()
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.icon));
								markerOptions.title("Toilet");
								break;
							case 2:
								markerOptions = new MarkerOptions()
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.atm_pin));
								markerOptions.title("ATM");
								break;
							case 3:
								markerOptions = new MarkerOptions()
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.dis_p_pin));
								markerOptions.title("Disable Parking");
								break;
							case 4:
								markerOptions = new MarkerOptions()
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.playarea_pin));
								markerOptions.title("Family Play Area");
								break;
							case 5:
								markerOptions = new MarkerOptions()
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.parking_pin));
								markerOptions.title("Parking");
								break;
							case 6:
								markerOptions = new MarkerOptions()
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.food_pin));
								markerOptions.title("Food");
								break;
							default:
								// markerOptions = new MarkerOptions()
								// .icon(BitmapDescriptorFactory
								// .fromResource(R.drawable.trail_in));
								break;
							}
							if (Integer.parseInt(facilityList.get(i)
									.getFacility_id()) > 6) {
								continue;
							}
							// Setting the position of the marker
							markerOptions.position(latlng);
							markerOptions.snippet("0,0,0");
							map.addMarker(markerOptions);
						}
					}
				}

				map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));

				if (location != null) {
					latlng = new LatLng(location.getLatitude(),
							location.getLongitude());
				} else {
					latlng = new LatLng(
							LLDCApplication.parkCenter.getLatitude(),
							LLDCApplication.parkCenter.getLongitude());
				}

				map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));

				// if (LLDCApplication.isInsideThePark)
				onGetRoute();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onShowDirection() {
		rlVenueDirection.setVisibility(View.VISIBLE);
		tvVenueDirectionTitle.setText(LLDCApplication.selectedModel.getName());
		tvVenueDirectionDistance.setText("Loading...");
		tvVenueDirectionTitle.setTextColor(Color
				.parseColor(LLDCApplication.selectedModel.getColor()));
		tvVenueDirectionDistance.setTextColor(Color
				.parseColor(LLDCApplication.selectedModel.getColor()));
	}

	public void onCheckUser() {
		// location = LLDCApplication.getLocation(getActivity());
		if (location == null) {
			LLDCApplication.isInsideThePark = false;
			return;
		}

		double distance = DistanceUtil.distance(
				LLDCApplication.parkCenter.getLatitude(),
				LLDCApplication.parkCenter.getLongitude(),
				location.getLatitude(), location.getLongitude());

		if (userMarker != null) {
			userMarker.remove();
		}
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

		// double distance = DistanceUtil.distance(
		// center.getLatitude(),
		// center.getLongitude(), loc.getLatitude(),
		// loc.getLongitude());

		map.moveCamera(CameraUpdateFactory.newLatLng(latlng));

		BigDecimal bd = new BigDecimal(distance);
		BigDecimal res = bd.setScale(3, RoundingMode.DOWN);

		if (res.doubleValue() > 100) {
			LLDCApplication.isInsideThePark = false;
		} else {
			LLDCApplication.isInsideThePark = true;
		}
		animateMarker(userMarker, latlng, false);
		// HomeActivity.mHomeActivityHandler.sendEmptyMessage(1009);
	}

	public void onHideDirection() {
		// rlVenueDirection.setVisibility(View.GONE);
	}

	public void onGetRoute() {
		try {
			if (getActivity().getIntent().getStringExtra("PAGETITLE")
					.equals("Trails"))
				return;

			if (location.getLatitude() == 0 && location.getLongitude() == 0)
				return;

			onShowDirection();

			Double startLat = location.getLatitude();
			Double startLong = location.getLongitude();

			Double endLat = Double.parseDouble(LLDCApplication.selectedModel
					.getLatitude());
			Double endLong = Double.parseDouble(LLDCApplication.selectedModel
					.getLongitude());

			LatLng origin = new LatLng(startLat, startLong); // markerList.get(0).getPosition();
			LatLng dest = new LatLng(endLat, endLong);// markerList.get(index
														// -
														// 1).getPosition();

			String url = getRouteUrl(origin, dest);

			if (LLDCApplication.isConnectingToInternet(getActivity())) {
				DownloadTask downloadTask = new DownloadTask(false);
				// Start downloading json data from Google Directions API
				downloadTask.execute(url);
			} else
				LLDCApplication.onShowToastMesssage(getActivity(),
						"Unable to connect internet");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void onApplyBounding() {
		LatLngBounds.Builder bld = new LatLngBounds.Builder();

		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inPreferredConfig = Config.RGB_565;
		// options.inJustDecodeBounds = false;
		// options.inDither = true;
		// options.inSampleSize = 2;
		//
		// // Bitmap largeIcon =
		// // BitmapFactory.decodeResource(getResources(),
		// // R.drawable.mapoverlay);
		//
		// Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
		// R.drawable.mapoverlay_1, options);
		//
		// BitmapDescriptor mImage =
		// BitmapDescriptorFactory.fromBitmap(largeIcon);
		//
		// // BitmapDescriptor mImage = BitmapDescriptorFactory
		// // .fromResource(R.drawable.mapoverlay);
		//
		// largeIcon.recycle();
		// largeIcon = null;
		// System.gc();

		bld.include(LLDCApplication.NEBOUND);
		bld.include(LLDCApplication.SWBOUND);

		LatLngBounds bounds = bld.build();
		map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
		// mGroundOverlay = map.addGroundOverlay(new GroundOverlayOptions()
		// .image(mImage).anchor(0, 1).positionFromBounds(bounds));

		if (!LLDCApplication.isDebug)
			mMapNavFragmentHandler.sendEmptyMessage(0);

	}

	private String getRouteUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";
		// Waypoints
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
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

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		@SuppressWarnings("unused")
		boolean isTrails = false;

		public DownloadTask(boolean istrails) {
			// TODO Auto-generated constructor stub
			isTrails = istrails;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle("Fetching trails data");
			dialog.setMessage("Please Wait...");
		}

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

			try {
				Constants.server_hit = true;
				mMapNavFragmentHandler.sendEmptyMessageDelayed(1041, 30000);
				ArrayList<LatLng> points = null;
				PolylineOptions lineOptions = null;

				if (result == null) {

					LLDCApplication.onShowToastMesssage(getActivity(),
							"No routes available to show...");

					if (dialog != null & dialog.isShowing()) {
						dialog.dismiss();
					}

					return;
				}

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
					lineOptions.width(8);
					lineOptions.color(Color.RED);
				}
				// map.clear();
				if (line != null)
					line.remove();
				// Drawing polyline in the Google Map for the i-th route
				if (map == null) {
					return;
				}
				if (lineOptions != null) {
					line = map.addPolyline(lineOptions);
					tvVenueDirectionDistance
							.setText(LLDCApplication.ROUTEDURATION);
				}
				if (dialog != null & dialog.isShowing()) {
					dialog.dismiss();
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	void showOutSideParkDialog() {
		isDialogOpen = true;
		AlertDialog outSideParkAlertDialog = new AlertDialog.Builder(
				getActivity())
				.setTitle("Confirm")
				.setMessage(LLDCApplication.OUTSIDE_PARK_NAV_MESSAGE)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {
								getActivity().finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
						argDialog.dismiss();
					}
				}).create();
		outSideParkAlertDialog.setCanceledOnTouchOutside(false);
		outSideParkAlertDialog.show();
		return;
	}

}
