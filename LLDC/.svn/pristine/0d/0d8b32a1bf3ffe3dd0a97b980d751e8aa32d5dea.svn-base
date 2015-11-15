package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
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
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.custom.CustomMapTileProvider;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.models.FacilityModel;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.tablet.HomeActivity;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.tablet.MapNavigationActivity;
import co.uk.android.lldc.tablet.VenueActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
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
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class SearchMapFragment extends Fragment implements OnClickListener {

	MapView mapView;

	// View view_include;
	GoogleMap map;

	ArrayList<ServerModel> venueList;

	ArrayList<LatLng> markerPoints = new ArrayList<LatLng>();

	private Bundle mBundle;

	View infoview;
	View rootView;

	/** For venue sliding panel */
	LinearLayout btndirection, venuetitleholder, topbarholder;
	View transperentView;
	TextView mapvenuetitle, venuesubtitle;// , tvdiscovemore;
	ImageView venuelargeimage;
	RelativeLayout venuedetails;
	// RelativeLayout largeimageholder;
	ServerModel selectedModel;
	Marker selectedMarker;
	HtmlTextView venuedescription;
	SlidingUpPanelLayout layout;

	int screenWidth = 0, screenHeight = 0;

	Marker userMarker = null;

	boolean isFullSlidingPanelVisible = false;

	@SuppressWarnings("unused")
	private GroundOverlay mGroundOverlay;

	Location location = new Location("");

	public static Handler mSearchMapFragment = null;

	String pageTitle = "";
	String selectedId = "";

	public SearchMapFragment(String pageTitle, String id) {
		// TODO Auto-generated constructor stub
		this.pageTitle = pageTitle;
		selectedId = id;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;
		LLDCApplication.LOCATION_REQUEST_INTERVAL = 5000;
		if (HomeActivity.mHomeActivityHandler != null) {
			HomeActivity.mHomeActivityHandler.sendEmptyMessage(1070);
		}
		onInitHandler();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_map_search, container,
				false);

		mapView = (MapView) rootView.findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
			e.printStackTrace();
		}

		mapView.onCreate(mBundle);
		setUpMapIfNeeded(rootView);

		/** For venue details */
		// view_include = (View) rootView.findViewById(R.id.slidingView);
		venuedetails = (RelativeLayout) rootView
				.findViewById(R.id.venuedetails);
		btndirection = (LinearLayout) rootView.findViewById(R.id.btndirection);
		venuetitleholder = (LinearLayout) rootView
				.findViewById(R.id.venuetitleholder);
		topbarholder = (LinearLayout) rootView.findViewById(R.id.topbarholder);
		mapvenuetitle = (TextView) rootView.findViewById(R.id.mapvenuetitle);
		venuesubtitle = (TextView) rootView.findViewById(R.id.venuesubtitle);
		venuedescription = (HtmlTextView) rootView
				.findViewById(R.id.venuedescription);

		venuedescription.setTypeface(Typeface.createFromAsset(getActivity()
				.getAssets(), "ROBOTO-LIGHT.TTF"));
		// tvdiscovemore = (TextView) rootView.findViewById(R.id.tvdiscovemore);
		venuelargeimage = (ImageView) rootView
				.findViewById(R.id.venuelargeimage);

		layout = (SlidingUpPanelLayout) rootView
				.findViewById(R.id.sliding_layout);

		mapvenuetitle.setOnClickListener(this);
		btndirection.setOnClickListener(this);
		venuelargeimage.setOnClickListener(this);
		venuetitleholder.setOnClickListener(this);

		ArrayList<View> Touchview = new ArrayList<View>();
		Touchview.add(btndirection);
		Touchview.add(venuelargeimage);
		layout.addTouchables(Touchview);

		layout.setFilterTouchesWhenObscured(true);

		layout.setPanelState(PanelState.COLLAPSED);
		layout.setPanelState(PanelState.EXPANDED);
		layout.setPanelState(PanelState.HIDDEN);

		layout.setPanelSlideListener(new PanelSlideListener() {
			@Override
			public void onPanelSlide(View panel, float slideOffset) {
				Log.i("", "onPanelSlide, offset " + slideOffset);
			}

			@Override
			public void onPanelExpanded(View panel) {
				Log.i("", "onPanelExpanded");
				// venuesubtitle.setVisibility(View.VISIBLE);
				// sliderindicator.setBackgroundResource(R.drawable.down_btn);
				venuedescription.setHtmlFromString(
						selectedModel.getLongDescription(), false);
				Drawable image = getActivity().getResources().getDrawable(
						R.drawable.down_btn);
				mapvenuetitle.setCompoundDrawablesWithIntrinsicBounds(null,
						null, image, null);
				venuesubtitle.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPanelCollapsed(View panel) {
				Log.i("", "onPanelCollapsed");
				venuedescription.setText(selectedModel.getShortDesc());
				Drawable image = getActivity().getResources().getDrawable(
						R.drawable.up_arrow);
				mapvenuetitle.setCompoundDrawablesWithIntrinsicBounds(null,
						null, image, null);

				venuesubtitle.setVisibility(View.GONE);

			}

			@Override
			public void onPanelAnchored(View panel) {
				Log.i("", "onPanelAnchored");
				// onShowFullSlidingPanel();
			}

			@Override
			public void onPanelHidden(View panel) {
				// TODO Auto-generated method stub

			}
		});

		if (mSearchMapFragment == null) {
			onInitHandler();
		}
		mSearchMapFragment.sendEmptyMessageDelayed(1011, 500);
		return rootView;
	}

	public void onInitHandler() {
		mSearchMapFragment = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1050) {

					if (LLDCApplication.isInsideThePark) {
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
						if (userMarker != null)
							animateMarker(userMarker, latlng, false);
					}

				} else if (message.what == 1011) {
					onGetFromDB();
					if (pageTitle.equals("IMLookingFor")) {
						onLoadFacilityMarkers();
					} else {
						onLoadMarkers();
					}
					LLDCApplication.removeSimpleProgressDialog();
				} else {
					// onCheckUserLocation();
					if (LLDCApplication.isInsideThePark)
						map.getUiSettings().setMyLocationButtonEnabled(true);
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
						if (!LLDCApplication.isDebug) {
							map.moveCamera(update);
						}
					}
					/* Recursively call handler every 100ms */
					sendEmptyMessageDelayed(0, 100);
				}
			}
		};
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		// onLoadMarkers();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		// map.clear();
	}

	@Override
	public void onDestroy() {
		try {
			LLDCApplication.LOCATION_REQUEST_INTERVAL = 30000;
			if (HomeActivity.mHomeActivityHandler != null) {
				HomeActivity.mHomeActivityHandler.sendEmptyMessage(1070);
			}
			mapView.onDestroy();
			map.clear();
			if (mSearchMapFragment != null) {
				mSearchMapFragment.removeMessages(0);
				mSearchMapFragment = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

	public void onDestroyView() {
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
		super.onDestroyView();
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
					map.getUiSettings().setMyLocationButtonEnabled(false);
					if (LLDCApplication.isInsideThePark)
						map.getUiSettings().setMyLocationButtonEnabled(true);

					map.setIndoorEnabled(true);
					map.moveCamera(CameraUpdateFactory.zoomTo(15));
					map.getUiSettings().setMapToolbarEnabled(false);
					map.getUiSettings().setRotateGesturesEnabled(false);
					map.getUiSettings().setTiltGesturesEnabled(false);

					// map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					map.setMapType(GoogleMap.MAP_TYPE_NONE);
					map.addTileOverlay(new TileOverlayOptions()
							.tileProvider(new CustomMapTileProvider(
									getResources().getAssets())));

					map.setInfoWindowAdapter(new InfoWindowAdapter() {
						@SuppressLint("InflateParams")
						public View getInfoWindow(Marker marker) {
							// inflating layout

							View v = getActivity().getLayoutInflater().inflate(
									R.layout.map_center, null);

							TextView venueName = (TextView) v
									.findViewById(R.id.venueTitle);
							TextView venueCount = (TextView) v
									.findViewById(R.id.eventcount);

							venueName.setText(marker.getTitle());
							venueName
									.setBackgroundResource(R.drawable.roundedinfowindow);
							venueCount
									.setText(marker.getSnippet().split(",")[2]);
							venueCount.setVisibility(View.GONE);

							return v;
						}

						public View getInfoContents(Marker arg0) {

							return null;
						}
					});

					map.setOnMapClickListener(new OnMapClickListener() {

						@Override
						public void onMapClick(LatLng arg0) {
							// TODO Auto-generated method stub
							// view_include.setVisibility(View.GONE);
							layout.setPanelState(PanelState.HIDDEN);
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

	private void onGetFromDB() {
		if (pageTitle.equals("SHOWME")) {
			String where = "";
			if (selectedId.equals("1")) {
				where = LLDCDataBaseHelper.COLUMN_CATEGORY
						+ " in ('1','3','5','7')";
			} else if (selectedId.equals("2")) {
				where = LLDCDataBaseHelper.COLUMN_CATEGORY
						+ " in ('2','3','6','7')";
			} else if (selectedId.equals("4")) {
				where = LLDCDataBaseHelper.COLUMN_CATEGORY
						+ " in ('4','5','6','7')";
			}
			// Venue, trails , facilities, events
			ArrayList<ServerModel> venueList1 = LLDCApplication.DBHelper
					.onGetVenueData(where);
			ArrayList<ServerModel> venueList2 = LLDCApplication.DBHelper
					.onGetFacilitiesData(where);
			venueList = new ArrayList<ServerModel>();
			for (int i = 0; i < venueList1.size(); i++) {
				venueList.add(venueList1.get(i));
			}
			for (int i = 0; i < venueList2.size(); i++) {
				venueList.add(venueList2.get(i));
			}

		} else if (pageTitle.equals("IMLookingFor")) {
			venueList = LLDCApplication.DBHelper.onSearchResultFacility(
					selectedId, LLDCDataBaseHelper.TABLE_VENUES);
			ArrayList<ServerModel> temp = LLDCApplication.DBHelper
					.onSearchResultFacility(selectedId,
							LLDCDataBaseHelper.TABLE_FACILITIES);
			for (int i = 0; i < temp.size(); i++) {
				venueList.add(temp.get(i));
			}
		}
	}

	private void onLoadMarkers() {
		try {

			if (venueList.size() == 0) {
				LLDCApplication.onShowToastMesssage(getActivity(),
						"No records found for search criteria...");
			}

			if (mapView.getViewTreeObserver().isAlive()) {

				onApplyBounding();

				location = LLDCApplication.getLocation(getActivity());
				if (location != null) {
					if (userMarker == null) {
						createUserMarker();
					}
				}

				LatLngBounds.Builder bld1 = new LatLngBounds.Builder();
				for (int i = 0; i < venueList.size(); i++) {
					LatLng latlng = new LatLng(Double.parseDouble(venueList
							.get(i).getLatitude().toString().trim()),
							Double.parseDouble(venueList.get(i).getLongitude()
									.toString().trim()));
					markerPoints.add(latlng);
					bld1.include(latlng);
					MarkerOptions markerOptions = new MarkerOptions()
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.locationpin));
					// Setting the position of the marker
					markerOptions.position(latlng);
					markerOptions.title(venueList.get(i).getName());
					markerOptions.snippet(venueList.get(i).getVenueId() + ",0,"
							+ venueList.get(i).getEventCount());
					// Add new marker to the Google Map Android
					// API V2
					map.addMarker(markerOptions);
				}
				map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
						LLDCApplication.parkCenter.getLatitude(),
						LLDCApplication.parkCenter.getLongitude())));
				map.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void onLoadFacilityMarkers() {
		try {

			if (venueList.size() == 0) {
				LLDCApplication.onShowToastMesssage(getActivity(),
						"No records found for search criteria...");
			}

			if (mapView.getViewTreeObserver().isAlive()) {

				onApplyBounding();

				location = LLDCApplication.getLocation(getActivity());
				if (location != null) {
					if (userMarker == null) {
						createUserMarker();
					}
				}

				LatLngBounds.Builder bld1 = new LatLngBounds.Builder();
				for (int i = 0; i < venueList.size(); i++) {
					ArrayList<FacilityModel> facility = venueList.get(i)
							.getVenueFacilityList();
					for (int j = 0; j < facility.size(); j++) {
						if (facility.get(j).getFacility_id().equals(selectedId)) {
							LatLng latlng = new LatLng(
									Double.parseDouble(facility.get(j).getLat()
											.toString().trim()),
									Double.parseDouble(facility.get(j).getLon()
											.toString().trim()));
							markerPoints.add(latlng);
							bld1.include(latlng);
							MarkerOptions markerOptions = new MarkerOptions();
							switch (Integer.parseInt(facility.get(j)
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
							}
							if (Integer.parseInt(facility.get(j)
									.getFacility_id()) > 6) {
								continue;
							}
							// Setting the position of the marker
							markerOptions.position(latlng);
							markerOptions.snippet("0,0,0");
							// Add new marker to the Google Map Android
							// API V2
							map.addMarker(markerOptions);
						}
					}
				}
				map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
						LLDCApplication.parkCenter.getLatitude(),
						LLDCApplication.parkCenter.getLongitude())));
				map.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
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

		if (mSearchMapFragment == null) {
			onInitHandler();
		}

		mSearchMapFragment.sendEmptyMessage(0);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btndirection) {
			LLDCApplication.selectedModel = selectedModel;
			Intent intent = new Intent(getActivity(),
					MapNavigationActivity.class);
			intent.putExtra("PAGETITLE", "Map");
			getActivity().startActivity(intent);

		} else if (v.getId() == R.id.venuelargeimage) {
			LLDCApplication.selectedModel = selectedModel;
			Intent intent = new Intent(getActivity(), VenueActivity.class);
			intent.putExtra("PAGETITLE", "Venue");
			getActivity().startActivity(intent);
		} else if (v.getId() == R.id.venuetitleholder) {
			if (isFullSlidingPanelVisible) {
				String[] tag = selectedMarker.getSnippet().split(",");
				selectedMarker.setSnippet(tag[0] + ",0," + tag[2]);
				selectedMarker.showInfoWindow();
				// onHideSlidingPanel();
				isFullSlidingPanelVisible = false;
			} else {
				// onShowFullSlidingPanel();
			}
		} else if (v.getId() == R.id.mapvenuetitle) {
			if (layout.getPanelState() == PanelState.EXPANDED) {
				layout.setPanelState(PanelState.COLLAPSED);
			} else if (layout.getPanelState() == PanelState.COLLAPSED) {
				layout.setPanelState(PanelState.EXPANDED);
			}
		}
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
