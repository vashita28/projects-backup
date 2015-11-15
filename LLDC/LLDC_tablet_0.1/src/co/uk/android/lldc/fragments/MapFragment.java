package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import co.uk.android.lldc.fragments.tablet.VenueFragmentTablet;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.tablet.MapNavigationActivity;
import co.uk.android.lldc.utils.Constants;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

@SuppressLint("HandlerLeak")
public class MapFragment extends Fragment implements OnClickListener {
	private static final String TAG = MapFragment.class.getSimpleName();

	MapView mapView;

	// View view_include;
	GoogleMap map;

	ArrayList<ServerModel> venueList;

	ArrayList<LatLng> markerPoints = new ArrayList<LatLng>();

	private Bundle mBundle;

	View infoview;
	View rootView;

	/** For venue sliding panel */
	LinearLayout btndirection, venuetitleholder;
	View transperentView, viewDivider;
	TextView mapvenuetitle, venuesubtitle;// tvdiscovemore;
	ImageView venueSmallLmage, venuelargeimage;
	RelativeLayout venuedetails, llSmallImage;
	// RelativeLayout largeimageholder;
	ServerModel selectedModel;
	Marker selectedMarker;
	HtmlTextView venuedescription, venuedescriptionFullScreen;
	SlidingUpPanelLayout layout;

	Marker userMarker = null;

	boolean isFullSlidingPanelVisible = false;

	@SuppressWarnings("unused")
	private GroundOverlay mGroundOverlay;

	Location location = new Location("");

	public static Handler mMapFragmentHandler = null;

	String pageSubTitle = "";
	String selectedId = "";

	public MapFragment(String pageSubTitle, String selectedId) {
		// TODO Auto-generated constructor stub
		this.pageSubTitle = pageSubTitle;
		this.selectedId = selectedId;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBundle = savedInstanceState;
		LLDCApplication.LOCATION_REQUEST_INTERVAL = 5000;
		if (HomeActivityTablet.mHomeActivityTabletHandler != null) {
			HomeActivityTablet.mHomeActivityTabletHandler
					.sendEmptyMessage(1070);
		}
		onInitHandler();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		Log.e(TAG, "Device is Tablet...");
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(R.layout.fragment_map_tablet, container,
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
		llSmallImage = (RelativeLayout) rootView
				.findViewById(R.id.llSmallImage);

		venueSmallLmage = (ImageView) rootView
				.findViewById(R.id.venueSmallLmage);

		mapvenuetitle = (TextView) rootView.findViewById(R.id.mapvenuetitle);
		venuesubtitle = (TextView) rootView.findViewById(R.id.venuesubtitle);
		venuedescription = (HtmlTextView) rootView
				.findViewById(R.id.venuedescription);
		viewDivider = (View) rootView.findViewById(R.id.borderlayout);

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

			@SuppressWarnings("deprecation")
			@Override
			public void onPanelExpanded(View panel) {
				Log.i("", "onPanelExpanded");
				// venuesubtitle.setVisibility(View.VISIBLE);
				// sliderindicator.setBackgroundResource(R.drawable.down_btn);
				venuedescription.setHtmlFromString(
						selectedModel.getLongDescription(), false);
				Drawable image = getActivity().getResources().getDrawable(
						R.drawable.down_arrow);
				mapvenuetitle.setCompoundDrawablesWithIntrinsicBounds(null,
						null, image, null);
				viewDivider.setBackgroundColor(Color.WHITE);
				venuesubtitle.setVisibility(View.VISIBLE);
				venueSmallLmage.setVisibility(View.GONE);
				venuetitleholder.setPadding(15, 15, 45, 0);

			}

			@SuppressWarnings("deprecation")
			@Override
			public void onPanelCollapsed(View panel) {
				Log.i("", "onPanelCollapsed");
				venuedescription.setText(selectedModel.getShortDesc());
				Drawable image = getActivity().getResources().getDrawable(
						R.drawable.up_arrow);
				mapvenuetitle.setCompoundDrawablesWithIntrinsicBounds(null,
						null, image, null);
				viewDivider.setBackgroundColor(getResources().getColor(
						R.color.explorer_bg));
				venuesubtitle.setVisibility(View.VISIBLE);// GONE
				venueSmallLmage.setVisibility(View.VISIBLE);
				venuetitleholder.setPadding(15, 15, 0, 0);

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

		if (mMapFragmentHandler == null) {
			onInitHandler();
		}
		mMapFragmentHandler.sendEmptyMessageDelayed(1011, 500);
		return rootView;
	}

	public void onInitHandler() {
		mMapFragmentHandler = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1050) {

					try {
						if (LLDCApplication.isInsideThePark) {
							Bundle bundle = message.getData();

							Double lat = bundle.getDouble("LAT");
							Double lan = bundle.getDouble("LAN");

							if (LLDCApplication.isDebug) {
								LLDCApplication.onShowToastMesssage(
										getActivity(),
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
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (message.what == 1011) {
					onLoadMarkers();
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

		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText("Map");
		((HomeActivityTablet) getActivity()).ivBack.setVisibility(View.GONE);
		((HomeActivityTablet) getActivity()).showCommonSearchIcon();

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
			if (HomeActivityTablet.mHomeActivityTabletHandler != null) {
				HomeActivityTablet.mHomeActivityTabletHandler
						.sendEmptyMessage(1070);
			}
			mapView.onDestroy();
			map.clear();
			mMapFragmentHandler.removeMessages(0);
			mMapFragmentHandler = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
							if (marker.getSnippet().split(",")[1].equals("1")) {
								venueCount.setVisibility(View.VISIBLE);
								venueName.setTextColor(Color.WHITE);
								venueName
										.setBackgroundResource(R.drawable.roundedinfowindow_selected);
								// venueCount.setVisibility(View.VISIBLE);
								marker.setSnippet(marker.getSnippet()
										.split(",")[0]
										+ ",0,"
										+ marker.getSnippet().split(",")[2]);
							}

							// CameraUpdate center = CameraUpdateFactory
							// .newLatLng(marker.getPosition());
							// map.moveCamera(center);

							return v;
						}

						public View getInfoContents(Marker arg0) {

							return null;
						}
					});

					map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

						@SuppressWarnings("deprecation")
						@Override
						public void onInfoWindowClick(final Marker arg0) {
							// TODO Auto-generated method stub
							selectedMarker = arg0;
							if (!arg0.getTitle().equals("User")) {
								arg0.setSnippet(arg0.getSnippet().split(",")[0]
										+ ",1,"
										+ arg0.getSnippet().split(",")[2]);
								arg0.hideInfoWindow();
								arg0.showInfoWindow();
								for (int i = 0; i < venueList.size(); i++) {
									if (venueList
											.get(i)
											.getVenueId()
											.equals(arg0.getSnippet()
													.split(",")[0])) {
										selectedModel = venueList.get(i);
										break;
									}
								}

								if (LLDCApplication.isInsideThePark
										|| LLDCApplication.isDebug) {
									btndirection.setVisibility(View.VISIBLE);
								} else {
									btndirection.setVisibility(View.INVISIBLE);
								}
								int screenWidth = (int) (LLDCApplication.screenWidth / 5);
								String imagePathSmall = selectedModel
										.getLargeImage()
										+ "&width="
										+ screenWidth
										+ "&height="
										+ screenWidth + "&crop-to-fit";

								venueSmallLmage.getLayoutParams().height = screenWidth;
								venueSmallLmage.getLayoutParams().width = screenWidth;
								ImageLoader.getInstance().displayImage(
										imagePathSmall, venueSmallLmage);

								// venueSmallLmage.setImageUrl(imagePathSmall,
								// ImageCacheManager.getInstance()
								// .getImageLoader());
								// venueSmallLmage
								// .setDefaultImageResId(R.drawable.qeop_small_placeholder);
								// venueSmallLmage
								// .setErrorImageResId(R.drawable.small_placeholder);

								mapvenuetitle.setText(selectedModel.getName());
								Drawable image = getActivity().getResources()
										.getDrawable(R.drawable.up_arrow);
								mapvenuetitle
										.setCompoundDrawablesWithIntrinsicBounds(
												null, null, image, null);
								mapvenuetitle.setTextColor(Color
										.parseColor(selectedModel.getColor()));
								venuesubtitle.setText(selectedModel
										.getActiveDays());
								venuesubtitle.setTextColor(Color
										.parseColor(selectedModel.getColor()));
								venuesubtitle.setVisibility(View.GONE);
								venuedescription.setText(selectedModel
										.getShortDesc());
								String imagePath = selectedModel
										.getLargeImage();
								// + "&w="
								// + screenWidth
								// + "&h=" + screenHeight + "&crop-to-fit";

								ImageLoader.getInstance().displayImage(
										imagePath, venuelargeimage);

								// venuelargeimage.setImageUrl(imagePath,
								// ImageCacheManager.getInstance()
								// .getImageLoader());
								// venuelargeimage
								// .setDefaultImageResId(R.drawable.qeop_placeholder);
								// venuelargeimage
								// .setErrorImageResId(R.drawable.placeholder);
								layout.setAnchorPoint(0.4f);
								layout.setPanelState(PanelState.COLLAPSED);
							}
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

	private void onLoadMarkers() {
		try {
			if (pageSubTitle.equals("")) {
				venueList = LLDCApplication.DBHelper.onGetVenueData("");
			} else {
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
				venueList = LLDCApplication.DBHelper.onGetVenueData(where);
			}
			if (venueList.size() == 0) {
				LLDCApplication.onShowToastMesssage(getActivity(),
						"Unable to get venue list...");
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
				// LatLngBounds bounds = bld1.build();
				// map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,
				// 30));
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

		if (mMapFragmentHandler == null) {
			onInitHandler();
		}

		mMapFragmentHandler.sendEmptyMessage(0);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btndirection) {
			LLDCApplication.showSimpleProgressDialog(getActivity());
			LLDCApplication.selectedModel = selectedModel;
			// displayFragment(2);
			Intent intent = new Intent(getActivity(),
					MapNavigationActivity.class);
			intent.putExtra("PAGETITLE", "Map");
			getActivity().startActivity(intent);
		} else if (v.getId() == R.id.venuelargeimage) {
			/* call venue fragment */
			LLDCApplication.showSimpleProgressDialog(getActivity());
			LLDCApplication.selectedModel = selectedModel;
			displayFragment(1);
			// Intent intent = new Intent(getActivity(), VenueActivity.class);
			// intent.putExtra("PAGETITLE", "Venue");
			// getActivity().startActivity(intent);
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

	private void displayFragment(int position) {

		Fragment fragment = null;
		FragmentManager mFragmentManager = null;
		Bundle bundle = new Bundle();
		switch (position) {
		case 1:
			fragment = (Fragment) new VenueFragmentTablet();
			bundle.putString("PAGETITLE", "Venue");
			Constants.IS_FROM_MAP = true;
			mFragmentManager = getParentFragment().getChildFragmentManager();

			if (fragment != null) {
				fragment.setArguments(bundle);
				mFragmentManager
						.beginTransaction()
						.addToBackStack(MapFragment.class.getName())
						.add(R.id.frame_container_parent, fragment,
								VenueFragmentTablet.class.getName())
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			}
			break;

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
