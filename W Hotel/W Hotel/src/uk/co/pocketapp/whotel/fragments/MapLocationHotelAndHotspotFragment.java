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
import uk.co.pocketapp.whotel.adapters.FilterAdapter;
import uk.co.pocketapp.whotel.customview.MapWrapperLayout;
import uk.co.pocketapp.whotel.customview.OnInfoWindowElemTouchListenerButton;
import uk.co.pocketapp.whotel.util.CustomHotSpotData;
import uk.co.pocketapp.whotel.util.CustomHotelData;
import uk.co.pocketapp.whotel.util.DirectionsJSONParser;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
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
public class MapLocationHotelAndHotspotFragment extends SherlockFragment
		implements GoogleMap.OnMyLocationChangeListener {

	ArrayList<Points> points;

	private GoogleMap map;

	ViewGroup infoWindow;
	TextView infoTitle;
	TextView infoSnippet;
	// TextView tv_get_directions;
	Button btn_get_direction, btn_hotel_info;
	private OnInfoWindowElemTouchListenerButton infoButtonDirectionListener,
			infoButtonHotelInfoListener;

	static FragmentLoad fragmentLoad;
	String TAG = "MapLocationHotelAndHotspotFragment";

	Location myLocation = null;
	Marker previousMarker = null;

	ProgressDialog pDialog;
	MapWrapperLayout mapWrapperLayout;

	ArrayList<CustomHotelData> originalHotelDataList, hotelDataList;
	ArrayList<CustomHotSpotData> originalHotspotDataList, hotspotDataList;

	RelativeLayout relative_filterHeader;
	ListView listFilterItems;
	// ImageView imageview_filterHeader;
	TextView textview_filterHeader;
	ProgressDialog mapDialog;

	EasyTracker easyTracker;

	public MapLocationHotelAndHotspotFragment(FragmentLoad fragmentLoad,
			ArrayList<CustomHotelData> hotelDataList,
			ArrayList<CustomHotSpotData> hotspotDataList,
			ProgressDialog mapDialog) {
		// TODO Auto-generated constructor stub
		if (fragmentLoad != null)
			MapLocationHotelAndHotspotFragment.fragmentLoad = fragmentLoad;
		this.originalHotelDataList = hotelDataList;
		this.hotelDataList = hotelDataList;
		this.originalHotspotDataList = hotspotDataList;
		this.hotspotDataList = hotspotDataList;
		this.mapDialog = mapDialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_map_hotspot, container,
				false);
		mapWrapperLayout = (MapWrapperLayout) view
				.findViewById(R.id.map_relative_layout);
		listFilterItems = (ListView) view.findViewById(R.id.filterList);
		listFilterItems.setVisibility(View.GONE);
		relative_filterHeader = (RelativeLayout) view
				.findViewById(R.id.rel_filter_header);
		// imageview_filterHeader = (ImageView) view
		// .findViewById(R.id.imageview_filter_header);
		textview_filterHeader = (TextView) view
				.findViewById(R.id.textview_filter_header);
		return view;
	}

	public void onDestroyView() {
		super.onDestroyView();
		Log.d("MapLocationFragment", "onDestroy-()");
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
			ft.add(R.id.main_frame, new MapLocationHotelAndHotspotFragment(
					fragmentLoad, hotelDataList, hotspotDataList, mapDialog),
					TAG);
			ft.commit();
		}

		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (easyTracker != null) {
			// This screen name value will remain set on the tracker and sent
			// with
			// hits until it is set to a new value or to null.
			easyTracker.set(Fields.SCREEN_NAME, "Map");

			easyTracker.send(MapBuilder.createAppView().build());
		}

	}

	public void drawMarkerCurrentLocation(Location location) {
		if (location != null) {
			myLocation = location;
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		MapMarkerLoadTask task = new MapMarkerLoadTask();
		task.execute();

		SupportMapFragment smf = (SupportMapFragment) getActivity()
				.getSupportFragmentManager()
				.findFragmentById(R.id.map_location);

		// Getting GoogleMap object from the fragment
		if (smf != null) {
			map = smf.getMap();
			// map.animateCamera(CameraUpdateFactory.zoomTo(8.0f));
		}

		if (map != null) {

			map.setOnMyLocationChangeListener(MapLocationHotelAndHotspotFragment.this);

			// Enabling MyLocation Layer of Google Map
			map.setMyLocationEnabled(false);
			map.setTrafficEnabled(true);

			map.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					// TODO Auto-generated method stub
					// map.animateCamera(CameraUpdateFactory.zoomIn());
					// CameraPosition cameraPosition = new
					// CameraPosition.Builder().target(marker.getPosition()).zoom(13.0f).build();
					// CameraUpdate cameraUpdate =
					// CameraUpdateFactory.newCameraPosition(cameraPosition);
					// map.moveCamera(cameraUpdate);

					if (marker.equals(previousMarker)) {
						previousMarker.hideInfoWindow();
						return true;
					}
					return false;
				}
			});
		}

		relative_filterHeader.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String szDeviceModel = android.os.Build.MODEL;
				if (listFilterItems.getVisibility() == View.GONE) {
					listFilterItems.setVisibility(View.VISIBLE);
					ArrayList<String> items = new ArrayList<String>();
					String[] filter_list = null;
					if (originalHotspotDataList != null) {
						filter_list = (getResources()
								.getStringArray(R.array.hotspot_filter_data));
						// if (szDeviceModel != null
						// && szDeviceModel.equals("GT-I9300")) {
						// imageview_filterHeader.getLayoutParams().width = 340;
						// listFilterItems.getLayoutParams().width = 340;
						// } else if (szDeviceModel != null
						// && szDeviceModel.equals("Nexus 4")) {
						// imageview_filterHeader.getLayoutParams().width = 380;
						// listFilterItems.getLayoutParams().width = 380;
						// } else {
						// imageview_filterHeader.getLayoutParams().width = 260;
						// listFilterItems.getLayoutParams().width = 260;
						// }

						listFilterItems.getLayoutParams().width = textview_filterHeader
								.getWidth();
					} else if (originalHotelDataList != null) {
						filter_list = (getResources()
								.getStringArray(R.array.hotel_filter_data));
						// if (szDeviceModel != null
						// && szDeviceModel.equals("GT-I9300")) {
						// imageview_filterHeader.getLayoutParams().width = 470;
						// listFilterItems.getLayoutParams().width = 470;
						// } else if (szDeviceModel != null
						// && szDeviceModel.equals("Nexus 4")) {
						// imageview_filterHeader.getLayoutParams().width = 460;
						// listFilterItems.getLayoutParams().width = 460;
						// } else {
						// imageview_filterHeader.getLayoutParams().width = 350;
						// listFilterItems.getLayoutParams().width = 350;
						// }

						listFilterItems.getLayoutParams().width = textview_filterHeader
								.getWidth();
					} else {
						Toast.makeText(getActivity(),
								"Please wait for data to load",
								Toast.LENGTH_LONG).show();
						return;
					}

					for (int i = 0; i < filter_list.length; i++) {
						items.add(filter_list[i]);
					}
					// try {
					// imageview_filterHeader
					// .setBackground(getResources()
					// .getDrawable(
					// R.drawable.whotel_search_list_dropdown_top_bg));
					// } catch (Exception e) {
					// imageview_filterHeader
					// .setBackgroundDrawable(getResources()
					// .getDrawable(
					// R.drawable.whotel_search_list_dropdown_top_bg));
					// }

					listFilterItems.setAdapter(new FilterAdapter(getActivity(),
							items));
				} else {
					if (textview_filterHeader.getText().toString()
							.equalsIgnoreCase("all")) {
						// imageview_filterHeader.getLayoutParams().width =
						// LayoutParams.WRAP_CONTENT;
						// try {
						// imageview_filterHeader
						// .setBackground(getResources()
						// .getDrawable(
						// R.drawable.whotel_search_list_white));
						// } catch (Exception e) {
						// imageview_filterHeader
						// .setBackgroundDrawable(getResources()
						// .getDrawable(
						// R.drawable.whotel_search_list_white));
						// }
					}
					listFilterItems.setVisibility(View.GONE);
				}
			}
		});

		listFilterItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {
				// TODO Auto-generated method stub
				listFilterItems.setVisibility(View.GONE);

				TextView txtItem = (TextView) view
						.findViewById(R.id.textview_item);
				String szFilterValue = txtItem.getText().toString();
				if (originalHotelDataList != null)
					szFilterValue = txtItem.getText().toString()
							.replaceAll(" ", "");

				textview_filterHeader.setText(szFilterValue);
				locationSearchByFilter(szFilterValue);

				if (szFilterValue.equalsIgnoreCase("all")) {
					// imageview_filterHeader.getLayoutParams().width =
					// LayoutParams.WRAP_CONTENT;
					// try {
					// imageview_filterHeader.setBackground(getResources()
					// .getDrawable(
					// R.drawable.whotel_search_list_white));
					// } catch (Exception e) {
					// imageview_filterHeader.setBackgroundDrawable(getResources()
					// .getDrawable(R.drawable.whotel_search_list_white));
					// }
				}
			}
		});
	}

	private void drawMarker(final ArrayList<Points> points) throws Exception {

		// Log.d(TAG, "hotspot hotel Points Size " + points.size());

		Double lat, lng;
		String szIsWHotel;
		String szCategory;
		String szAddress;
		String szName;
		String szDesc;
		String szImage_url;

		for (int i = 0; i < points.size(); i++) {

			// Getting the latitude of the i-th location
			lat = points.get(i).lattitude;

			// Getting the longitude of the i-th location
			lng = points.get(i).longitude;

			if (i == 0) {
				map.animateCamera(CameraUpdateFactory.zoomIn());
				if (lat != null && lng != null) {
					CameraUpdate cameraUpdate = CameraUpdateFactory
							.newLatLngZoom(new LatLng(lat, lng), 14.0f);
					map.animateCamera(cameraUpdate);
				}
			}

			szIsWHotel = points.get(i).isWHotel;
			szCategory = points.get(i).category;
			szAddress = points.get(i).address;
			szName = points.get(i).name;
			szDesc = points.get(i).desc;
			szImage_url = points.get(i).image_url;

			if (lat != null && lng != null) {
				// Drawing marker on the map
				// Creating an instance of MarkerOptions
				MarkerOptions markerOptions = new MarkerOptions();

				infoWindow = (ViewGroup) getSherlockActivity()
						.getLayoutInflater().inflate(
								R.layout.custom_marker_popup_new, null);
				infoTitle = (TextView) infoWindow
						.findViewById(R.id.tv_marker_title);
				infoSnippet = (TextView) infoWindow
						.findViewById(R.id.tv_marker_details);
				// tv_get_directions = (TextView) infoWindow
				// .findViewById(R.id.tv_get_directions);
				btn_get_direction = (Button) infoWindow
						.findViewById(R.id.btn_get_direction);
				Typeface font_BoldItalic = Typeface.createFromAsset(
						getActivity().getAssets(), "WSansNew-BoldItalic.otf");
				btn_get_direction.setTypeface(font_BoldItalic);
				btn_get_direction.setIncludeFontPadding(false);
				btn_hotel_info = (Button) infoWindow
						.findViewById(R.id.btn_hotel_info);

				// Setting custom OnTouchListener which deals with the pressed
				// state
				// so it shows up
				this.infoButtonDirectionListener = new OnInfoWindowElemTouchListenerButton(
						btn_get_direction, getResources().getDrawable(
								R.drawable.direction_btn_normal),
						getResources().getDrawable(
								R.drawable.direction_btn_pressed)) {
					@Override
					protected void onClickConfirmed(View v, Marker marker,
							String category, String address, String name,
							String desc, String image_url) {
						// Here we can perform some action triggered after
						// clicking the button
						// Toast.makeText(getActivity(),
						// marker.getTitle() + "'s button clicked!",
						// Toast.LENGTH_SHORT).show();
						LatLng markerLatLng = marker.getPosition();
						Log.v("INFO WINDOW", "INFO WINDOW"
								+ markerLatLng.latitude + " ---  "
								+ markerLatLng.longitude);
						marker.hideInfoWindow();
						// task.source = getLocation();
						// task.source = map.getMyLocation();

						// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable)
						// {
						// GetDirection task = new GetDirection();
						// task.source = myLocation;
						// task.destination = markerLatLng;
						//
						// // CameraUpdate cameraUpdate = CameraUpdateFactory
						// // .newLatLngZoom(new LatLng(
						// // markerLatLng.latitude,
						// // markerLatLng.longitude), 5);
						// // map.animateCamera(cameraUpdate);
						//
						// if (myLocation != null) {
						// pDialog = new ProgressDialog(getActivity());
						// pDialog.setMessage("Loading route. Please wait...");
						// pDialog.setIndeterminate(false);
						// pDialog.setCancelable(true);
						// pDialog.show();
						// task.execute();
						// } else {
						// if (!isGPSActivated())
						// showGPSAlertDialog();
						// else
						// showAlertDialog();
						// }
						// } else
						// Toast.makeText(getActivity(),
						// "No network, please try after some time",
						// Toast.LENGTH_LONG).show();

						if (myLocation != null && markerLatLng != null) {
							final Intent intent = new Intent(
									Intent.ACTION_VIEW,
									Uri.parse("http://maps.google.com/maps?"
											+ "saddr="
											+ myLocation.getLatitude() + ","
											+ myLocation.getLongitude()
											+ "&daddr=" + markerLatLng.latitude
											+ "," + markerLatLng.longitude));
							intent.setClassName("com.google.android.apps.maps",
									"com.google.android.maps.MapsActivity");
							startActivity(intent);
						} else if (myLocation == null) {
							Toast.makeText(
									getActivity(),
									"Location not found. Please update your location",
									Toast.LENGTH_LONG).show();
						} else if (markerLatLng == null) {
							Toast.makeText(getActivity(),
									"Destination location not found.",
									Toast.LENGTH_LONG).show();
						}
					}
				};
				this.btn_get_direction
						.setOnTouchListener(infoButtonDirectionListener);

				if (hotelDataList != null) { // info window applicable only for
												// hotels
					this.infoButtonHotelInfoListener = new OnInfoWindowElemTouchListenerButton(
							btn_hotel_info, getResources().getDrawable(
									R.drawable.hotel_info_btn_background),
							getResources().getDrawable(
									R.drawable.hotel_info_btn_background)) {
						@Override
						protected void onClickConfirmed(View v, Marker marker,
								String category, String address, String name,
								String desc, String image_url) {
							// Here we can perform some action triggered after
							// clicking the button
							Log.d("OnInfoWindowElemTouchListenerButton",
									"OnInfoWindowElemTouchListenerButton onClickConfirmed"
											+ name + " , " + address + " , "
											+ desc + " , " + category);
							Fragment hotelInfoFragment = new HotelInfoFragment(
									fragmentLoad);
							Bundle bundleArgs = new Bundle();
							bundleArgs.putString("category", category);
							bundleArgs.putString("address", address);
							bundleArgs.putString("name", name);
							bundleArgs.putString("desc", desc);
							bundleArgs.putString("image_url", image_url);

							hotelInfoFragment.setArguments(bundleArgs);

							getActivity()
									.getSupportFragmentManager()
									.beginTransaction()
									.replace(R.id.main_frame, hotelInfoFragment)
									.setTransition(
											FragmentTransaction.TRANSIT_FRAGMENT_FADE)
									.addToBackStack(null).commit();

							// Toast.makeText(getActivity(),
							// marker.getTitle() + "'s btn clicked!",
							// Toast.LENGTH_SHORT).show();
						}
					};
					this.btn_hotel_info
							.setOnTouchListener(infoButtonHotelInfoListener);
				}

				map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
					@Override
					public View getInfoWindow(Marker marker) {
						// Setting up the infoWindow with current's marker info
						String szTitle, szSnippet;
						szTitle = marker.getTitle();
						szSnippet = marker.getSnippet();

						infoTitle.setText(szTitle);
						infoSnippet.setText(szSnippet);
						infoButtonDirectionListener.setMarker(marker);
						if (hotelDataList != null) {
							infoButtonHotelInfoListener.setMarker(marker);
							for (int i = 0; i < points.size(); i++) {
								if (points.get(i).address.equals(szSnippet)
										&& points.get(i).name.equals(szTitle)) {
									infoButtonHotelInfoListener
											.setMarkerDetails(
													points.get(i).category,
													points.get(i).address,
													points.get(i).name,
													points.get(i).desc,
													points.get(i).image_url);
									break;
								}
							}

						}
						// We must call this to set the current marker and
						// infoWindow references
						// to the MapWrapperLayout
						mapWrapperLayout.setMarkerWithInfoWindow(marker,
								infoWindow);

						// infoButtonListener.setMarker(marker);
						// We must call this to set the current marker and
						// infoWindow references
						// to the MapWrapperLayout
						// mapWrapperLayout.setMarkerWithInfoWindow(marker,
						// infoWindow);
						return infoWindow;
					}

					@Override
					public View getInfoContents(Marker marker) {

						return null;
					}
				});

				markerOptions.position(new LatLng(lat, lng));
				if (szIsWHotel != null && szIsWHotel.equalsIgnoreCase("yes")) {
					markerOptions
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.whotel_insider_map_purple_pin_w));
				} else {
					markerOptions
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.whotel_insider_map_pink_pin));
				}

				if (hotelDataList != null) {
					markerOptions.title(hotelDataList.get(i).getH_name())
							.snippet(hotelDataList.get(i).getH_address());
				} else if (hotspotDataList != null) {
					markerOptions.title(hotspotDataList.get(i).getHs_name())
							.snippet(hotspotDataList.get(i).getHs_address());
				}
				// Adding marker on the Google Map
				map.addMarker(markerOptions);
				// }

			}
		}

	}

	// Convert a view to bitmap
	Bitmap createDrawableFromView(Activity activity, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		view.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels,
				displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	@Override
	public void onMyLocationChange(Location location) {
		Log.d("MapLocationFragment", "**********My Location Changed" + location);
		myLocation = location;
		// drawMarkerCurrentLocation(myLocation);
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

	public void onBackPressed() {
		Log.d("********************", "MapLocationFragment on Key Back");
		fragmentLoad.onMapHotelAndHotspotFragmentClosed();
	}

	class MapMarkerLoadTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// Getting Google Play availability status
			int status = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getActivity());

			// Showing status
			if (status != ConnectionResult.SUCCESS) { // Google Play Services
				// are
				// not available

				int requestCode = R.drawable.whotel_insider_earth_icon;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
						getActivity(), requestCode);
				dialog.show();

			} else {

				// Getting GoogleMap object from the fragment
				if (map != null) {

					// Getting LocationManager object from System Service
					// LOCATION_SERVICE
					LocationManager locationManager = (LocationManager) getActivity()
							.getSystemService(Context.LOCATION_SERVICE);
					LocationProvider provider = null;
					// Getting the name of the best provider
					if (locationManager != null) {
						try {
							provider = locationManager
									.getProvider(LocationManager.GPS_PROVIDER);
						} catch (Exception e) {
							// TODO: handle exception
						}
					}

					// MapWrapperLayout initialization
					// 39 - default marker height
					// 20 - offset between the default InfoWindow bottom edge
					// and it's content bottom edge
					mapWrapperLayout.init(map,
							getPixelsFromDp(getActivity(), 39 + 20));

					populateMarkerPoints();

				}
			}
			if (mapDialog != null) {
				mapDialog.dismiss();
				mapDialog = null;
			}
			fragmentLoad
					.onMapHotelAndHotspotFragmentLoad(MapLocationHotelAndHotspotFragment.this);
		}
	}

	int getPixelsFromDp(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
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

	/**
	 * get the last known location from a specific provider (network/gps)
	 */
	private Location getLocationByProvider(String provider) {
		Location location = null;
		if (!isProviderSupported(provider)) {
			return null;
		}
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		try {
			if (locationManager.isProviderEnabled(provider)) {
				location = locationManager.getLastKnownLocation(provider);
			}
		} catch (Exception e) {
			Log.d("", "Cannot acces Provider " + provider);
		}
		return location;
	}

	private boolean isProviderSupported(String provider) {
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		boolean isProviderEnabled = false;
		try {
			isProviderEnabled = locationManager.isProviderEnabled(provider);
		} catch (Exception ex) {
		}
		return isProviderEnabled;

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

	class Points {
		double lattitude;
		double longitude;
		String isWHotel, category = "", name = "", address = "", desc = "",
				image_url = "";
	}

	public void locationSearch(CharSequence inputText) {

		if (inputText == null || inputText.length() == 0) {
			if (originalHotelDataList == null) {
				hotspotDataList = originalHotspotDataList;
			} else if (originalHotspotDataList == null) {
				hotelDataList = originalHotelDataList;
			}
		} else {
			inputText = inputText.toString().toLowerCase();

			if (originalHotspotDataList != null) {
				ArrayList<CustomHotSpotData> tempHotspotDataList = new ArrayList<CustomHotSpotData>();
				for (int i = 0; i < originalHotspotDataList.size(); i++) {
					String Name, Address;
					try {
						Name = originalHotspotDataList.get(i).getHs_name()
								.toString();
						Address = originalHotspotDataList.get(i)
								.getHs_address().toString();

						if (Name.toLowerCase().contains(inputText.toString())
								|| Address.toLowerCase().contains(
										inputText.toString())) {
							tempHotspotDataList.add(originalHotspotDataList
									.get(i));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				hotspotDataList = tempHotspotDataList;
				// Log.d(TAG,
				// "hotspotDataList RESULT COUNT" + hotspotDataList.size());
				// Log.d(TAG,
				// "hotspotDataList RESULT " + hotspotDataList.toString());
			}

			else if (originalHotelDataList != null) {
				ArrayList<CustomHotelData> tempHotelDataList = new ArrayList<CustomHotelData>();
				for (int i = 0; i < originalHotelDataList.size(); i++) {
					String Name, Address;
					try {
						Name = originalHotelDataList.get(i).getH_name()
								.toString();
						Address = originalHotelDataList.get(i).getH_address()
								.toString();

						if (Name.toLowerCase().contains(inputText.toString())
								|| Address.toLowerCase().contains(
										inputText.toString())) {
							tempHotelDataList.add(originalHotelDataList.get(i));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				hotelDataList = tempHotelDataList;
				// Log.d(TAG, "hotelDataList RESULT COUNT" +
				// hotelDataList.size());
				// Log.d(TAG, "hotelDataList RESULT " +
				// hotelDataList.toString());

			}
		}

		populateMarkerPoints();
	}

	public void locationSearchByFilter(CharSequence inputText) {
		if (inputText.equals("ALL") || inputText.equals("all")) {
			if (originalHotelDataList == null) {
				hotspotDataList = originalHotspotDataList;
			} else if (originalHotspotDataList == null) {
				hotelDataList = originalHotelDataList;
			}
		} else {
			if (originalHotspotDataList != null) {
				ArrayList<CustomHotSpotData> tempHotspotDataList = new ArrayList<CustomHotSpotData>();
				for (int i = 0; i < originalHotspotDataList.size(); i++) {
					String szCategory;
					try {
						szCategory = originalHotspotDataList.get(i)
								.getHs_category().toString();
						if (szCategory.toString().equalsIgnoreCase(
								inputText.toString()))
							tempHotspotDataList.add(originalHotspotDataList
									.get(i));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				hotspotDataList = tempHotspotDataList;
				// Log.d(TAG,
				// "hotspotDataList RESULT COUNT" + hotspotDataList.size());
				// Log.d(TAG,
				// "hotspotDataList RESULT " + hotspotDataList.toString());
			}

			else if (originalHotelDataList != null) {
				ArrayList<CustomHotelData> tempHotelDataList = new ArrayList<CustomHotelData>();
				for (int i = 0; i < originalHotelDataList.size(); i++) {
					String szCategory;
					try {
						szCategory = originalHotelDataList.get(i)
								.getH_category().toString();
						if (szCategory.toString().equalsIgnoreCase(
								inputText.toString()))
							tempHotelDataList.add(originalHotelDataList.get(i));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				hotelDataList = tempHotelDataList;
				// Log.d(TAG, "hotelDataList RESULT COUNT" +
				// hotelDataList.size());
				// Log.d(TAG, "hotelDataList RESULT " +
				// hotelDataList.toString());

			}
		}

		populateMarkerPoints();
	}

	void populateMarkerPoints() {

		Handler handler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				map.clear();
				points = new ArrayList<Points>();

				if (hotelDataList != null) {
					for (int i = 0; i < hotelDataList.size(); i++) {
						Points markerPoints = new Points();
						markerPoints.lattitude = Double
								.parseDouble(hotelDataList.get(i).getH_lati());
						markerPoints.longitude = Double
								.parseDouble(hotelDataList.get(i).getH_long());
						markerPoints.isWHotel = hotelDataList.get(i)
								.getIs_whotel();

						markerPoints.category = hotelDataList.get(i)
								.getH_category();
						markerPoints.address = hotelDataList.get(i)
								.getH_address();
						markerPoints.name = hotelDataList.get(i).getH_name();
						markerPoints.desc = hotelDataList.get(i).getH_desc();
						markerPoints.image_url = hotelDataList.get(i)
								.getRectlarge();

						points.add(markerPoints);
					}
					try {
						drawMarker(points);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (hotspotDataList != null
						&& hotspotDataList.size() > 0) {

					for (int i = 0; i < hotspotDataList.size(); i++) {
						try {
							Points markerPoints = new Points();
							markerPoints.lattitude = Double
									.valueOf(hotspotDataList.get(i)
											.getHs_lati());
							markerPoints.longitude = Double
									.valueOf(hotspotDataList.get(i)
											.getHs_long());
							markerPoints.isWHotel = hotspotDataList.get(i)
									.getHs_IsWhotel();
							markerPoints.category = hotspotDataList.get(i)
									.getHs_category();
							markerPoints.address = hotspotDataList.get(i)
									.getHs_address();
							markerPoints.name = hotspotDataList.get(i)
									.getHs_name();
							markerPoints.desc = hotspotDataList.get(i)
									.getHs_desc();
							markerPoints.image_url = hotspotDataList.get(i)
									.getRectlarge();

							points.add(markerPoints);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						drawMarker(points);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		handler.postDelayed(r, 100);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mapDialog != null && mapDialog.isShowing()) {
			mapDialog.dismiss();
		}
	}
}
