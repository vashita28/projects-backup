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
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
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
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.async.DirectionsJSONParser;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.models.TrailsWayPointModel;
import co.uk.android.lldc.utils.DistanceUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
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
	LinearLayout rlVenueDirection;
	TextView tvVenueDirectionTitle, tvVenueDirectionDistance;

	boolean isMarkerSelected = false;

	Marker marker = null;

	String pageTitle = "";

	ProgressDialog dialog = null;

	ArrayList<TrailsWayPointModel> waypointList = null;

	ArrayList<Marker> markerList = new ArrayList<Marker>();

	public static Handler mMapTrailsFragmentHandler = null;

	Location location = new Location("");

	Marker userMarker = null;

	/** for locking scroll and zoom */
	@SuppressWarnings("unused")
	private GroundOverlay mGroundOverlay;

	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;

		mMapTrailsFragmentHandler = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1050) {

					Bundle bundle = message.getData();

					Double lat = bundle.getDouble("LAT");
					Double lan = bundle.getDouble("LAN");
					location.setLatitude(lat);
					location.setLongitude(lan);

					LatLng latlng = new LatLng(lat, lan);

					if (userMarker == null) {
						createUserMarker();
					}
					if (userMarker != null)
						animateMarker(userMarker, latlng, false);

				} else if (message.what == 1040) {
					onLoadTrailsMarkers();
					onLoadTrails();
				} else {
					CameraPosition position = map.getCameraPosition();
					VisibleRegion region = map.getProjection()
							.getVisibleRegion();
					float zoom = 0;
					if (position.zoom < LLDCApplication.MIN_ZOOM)
						zoom = LLDCApplication.MIN_ZOOM;
					if (position.zoom > LLDCApplication.MAX_ZOOM)
						zoom = LLDCApplication.MAX_ZOOM;
					LatLng correction = LLDCApplication
							.getLatLngCorrection(region.latLngBounds);
					if (zoom != 0 || correction.latitude != 0
							|| correction.longitude != 0) {
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

		/** For venue Direction */
		rlVenueDirection = (LinearLayout) rootView
				.findViewById(R.id.rlVenueDirection);
		tvVenueDirectionTitle = (TextView) rootView
				.findViewById(R.id.tvVenueDirectionTitle);
		tvVenueDirectionDistance = (TextView) rootView
				.findViewById(R.id.tvVenueDirectionDistance);

		pageTitle = getActivity().getIntent().getStringExtra("PAGETITLE");

		mMapTrailsFragmentHandler.sendEmptyMessageDelayed(1040, 500);

		return rootView;
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
		mapView.onDestroy();
		mMapTrailsFragmentHandler = null;
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
					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					map.getUiSettings().setRotateGesturesEnabled(false);
					map.getUiSettings().setTiltGesturesEnabled(false);
					if (CENTER != null) {
						map.animateCamera(
								CameraUpdateFactory.newLatLng(CENTER), 1750,
								null);
					}
					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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

							if (!ImageUrl.equals("")) {
								ImageLoader
										.getInstance()
										.displayImage(
												ImageUrl
														+ "&width=150&height=150&crop-to-fit",
												venueImage);
								venueImage.setVisibility(View.VISIBLE);

							}
							if (marker.getSnippet().split(",")[1].equals("1")) {
								venueName.setTextColor(Color.WHITE);
								marker.setSnippet(marker.getSnippet()
										.split(",")[0]
										+ ",0,"
										+ marker.getSnippet().split(",")[2]);
							} else {
								onHideDirection();
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
		tvVenueDirectionDistance.setText(LLDCApplication.selectedModel
				.getActiveDays());
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

				boolean isFirstisLast = false;

				if (waypointList.size() > 1) {
					if (waypointList
							.get(0)
							.getPinLat()
							.equals(waypointList.get(waypointList.size() - 1)
									.getPinLat())) {
						if (waypointList
								.get(0)
								.getPinLong()
								.equals(waypointList.get(
										waypointList.size() - 1).getPinLong())) {
							isFirstisLast = true;
						}
					}
				}

				for (int i = 0; i < waypointList.size() - 1; i++) {
					if (isFirstisLast && i == waypointList.size() - 1) {
						return;
					}
					LatLng latlng = new LatLng(Double.parseDouble(waypointList
							.get(i).getPinLat().toString().trim()),
							Double.parseDouble(waypointList.get(i).getPinLong()
									.toString().trim()));
					MarkerOptions markerOptions = new MarkerOptions();
					if (i == 0) {
						markerOptions.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.locationpin));
					} else {
						markerOptions.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.trail_in));

					}
					// Setting the position of the marker
					markerOptions.position(latlng);
					markerOptions.title(waypointList.get(i).getPinTitle());
					if (isFirstisLast && i == 0) {
						markerOptions.title("Start & Finish");
					}
					markerOptions.snippet(waypointList.get(i).getTailsId()
							+ ",0," + i);
					bld1.include(latlng);
					markerPoints.add(latlng);
					// Add new marker to the Google Map Android
					// API V2
					Marker marker = map.addMarker(markerOptions);
				}

				if (waypointList
						.get(0)
						.getPinLat()
						.equals(waypointList.get(waypointList.size() - 1)
								.getPinLat())) {
					if (waypointList
							.get(0)
							.getPinLong()
							.equals(waypointList.get(waypointList.size() - 1)
									.getPinLong())) {

					} else {
						int i = waypointList.size() - 1;
						LatLng latlng = new LatLng(
								Double.parseDouble(waypointList.get(i)
										.getPinLat().toString().trim()),
								Double.parseDouble(waypointList.get(i)
										.getPinLong().toString().trim()));
						markerPoints.add(latlng);
						bld1.include(latlng);
						MarkerOptions markerOptions = new MarkerOptions();
						if (i == 0) {
							markerOptions.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.locationpin));
						}
						// Setting the position of the marker
						markerOptions.position(latlng);
						markerOptions.title(waypointList.get(i).getPinTitle());
						markerOptions.snippet(waypointList.get(i).getTailsId()
								+ ",0," + i);
						// Add new marker to the Google Map Android
						// API V2
						Marker marker = map.addMarker(markerOptions);
					}
				}

				mMapTrailsFragmentHandler.sendEmptyMessage(0);
				LatLngBounds bounds = bld1.build();
				map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 12));
				// map.animateCamera(CameraUpdateFactory.newLatLngZoom(new
				// LatLng(
				// LLDCApplication.parkCenter.getLatitude(),
				// LLDCApplication.parkCenter.getLongitude()), 15.0f));

				onShowDirection();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onLoadTrails() {
		try {

			if (waypointList.size() > 0) {
				Double startLat = Double.parseDouble(waypointList.get(0)
						.getPinLat());
				Double startLong = Double.parseDouble(waypointList.get(0)
						.getPinLong());

				int index = waypointList.size();

				Double endLat = Double.parseDouble(waypointList.get(index - 1)
						.getPinLat());
				Double endLong = Double.parseDouble(waypointList.get(index - 1)
						.getPinLong());

				LatLng origin = new LatLng(startLat, startLong); // markerList.get(0).getPosition();
				LatLng dest = new LatLng(endLat, endLong);// markerList.get(index
															// -
															// 1).getPosition();

				// Getting URL to the Google Directions API
				String url = getDirectionsUrl(origin, dest);

				if (LLDCApplication.isConnectingToInternet(getActivity())) {
					DownloadTask downloadTask = new DownloadTask();
					// Start downloading json data from Google Directions API
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

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = false;
		options.inDither = true;
		options.inSampleSize = 2;

		// Bitmap largeIcon =
		// BitmapFactory.decodeResource(getResources(),
		// R.drawable.mapoverlay);

		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.mapoverlay_1, options);

		BitmapDescriptor mImage = BitmapDescriptorFactory.fromBitmap(largeIcon);

		// BitmapDescriptor mImage = BitmapDescriptorFactory
		// .fromResource(R.drawable.mapoverlay);

		largeIcon.recycle();
		largeIcon = null;
		System.gc();

		bld.include(LLDCApplication.NEBOUND);
		bld.include(LLDCApplication.SWBOUND);

		LatLngBounds bounds = bld.build();
		map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
		mGroundOverlay = map.addGroundOverlay(new GroundOverlayOptions()
				.image(mImage).anchor(0, 1).positionFromBounds(bounds));

		mMapTrailsFragmentHandler.sendEmptyMessage(0);

	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";
		// Waypoints
		String waypoints = "waypoints=";
		for (int i = 0; i < markerList.size(); i++) {
			if (i == 0 || i == markerList.size() - 1) {
				continue;
			}
			Double startLat = Double.parseDouble(waypointList.get(i)
					.getPinLat());
			Double startLong = Double.parseDouble(waypointList.get(i)
					.getPinLong());

			LatLng point = new LatLng(startLat, startLong); // markerList.get(i).getPosition();

			waypoints += point.latitude + "," + point.longitude + "|";
		}

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&"
				+ waypoints;

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

		public DownloadTask() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle("Fetching trails data");
			dialog.setMessage("Please Wait...");
			dialog.show();
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
				ArrayList<LatLng> points = null;
				PolylineOptions lineOptions = null;

				if (result == null) {
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
					lineOptions.width(3);
					lineOptions.color(Color.RED);
				}

				// Drawing polyline in the Google Map for the i-th route
				map.addPolyline(lineOptions);
				// if (lineOptions != null) {
				// if (lineOptions.getPoints().size() > 0) {
				// drawDashedPolyLine(map, lineOptions.getPoints(),
				// Color.RED);
				// tvVenueDirectionDistance
				// .setText(LLDCApplication.ROUTEDURATION);
				// }
				// }
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

}
