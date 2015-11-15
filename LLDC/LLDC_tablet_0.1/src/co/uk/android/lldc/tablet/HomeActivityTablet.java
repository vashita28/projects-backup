package co.uk.android.lldc.tablet;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.MapFragment;
import co.uk.android.lldc.fragments.MapNavFragment;
import co.uk.android.lldc.fragments.MapTrailsFragment;
import co.uk.android.lldc.fragments.SearchMapFragment;
import co.uk.android.lldc.fragments.WelcomeFragment;
import co.uk.android.lldc.fragments.parent.EventsParentFragmentTablet;
import co.uk.android.lldc.fragments.parent.ExploreParentFragmentTablet;
import co.uk.android.lldc.fragments.parent.MapParentFragmentTablet;
import co.uk.android.lldc.fragments.parent.TheParkParentFragmentTablet;
import co.uk.android.lldc.fragments.parent.WelcomeParentFragmentTablet;
import co.uk.android.lldc.fragments.tablet.EventDetailsFragmentTablet;
import co.uk.android.lldc.fragments.tablet.ExploreListDetailsFragmentTablet;
import co.uk.android.lldc.fragments.tablet.VenueFragmentTablet;
import co.uk.android.lldc.service.LLDC_Sync_Servcie;
import co.uk.android.lldc.utils.Constants;
import co.uk.android.lldc.utils.DistanceUtil;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class HomeActivityTablet extends FragmentActivity implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	private static final String TAG = HomeActivityTablet.class.getSimpleName();

	public RelativeLayout rlSearchHeaderDefault, rlBackBtn, rlHeader;
	public TextView tvHeaderTitle;
	public ImageView ivBack, ivNavigation, ivCommonSearch;

	RelativeLayout rlWelcomeTab, rlMapTab, rlExploreTab, rlEventsTab,
			rlTheParkTab, rlRelaxTab, rlEntertainTab, rlActiveTab;

	public RelativeLayout rlHeadingOfFooter;

	RelativeLayout rlOpenedFooter;
	public LinearLayout llElementsOfFooter;
	public TextView tvWhatAreYouLookingToDo, tvWelcome, tvMap, tvExplore,
			tvEvents, tvThePark;
	View view1, view2, view3;
	public static View viewFooterBlur;

	public ImageView ivWelcomeHome, ivMap, ivExplore, ivEvents, ivThePark;

	public static Context mContext;

	FragmentManager mFragmentManager;
	public boolean bIsFooterOpen = false;

	int nLastSelected = -1;

	String provider;
	boolean bUserIsInSidePark;

	Location location;

	private GoogleApiClient mGoogleApiClient;

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(5000) // 5 seconds
			.setFastestInterval(16) // 16ms = 60fps
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	public static Handler mHomeActivityTabletHandler = null;

	Bundle bundle = new Bundle();

	Fragment fragment = null;
	FragmentManager mFragmentManagerSearchView = null;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FlurryAgent.init(this, LLDCApplication.flurryKey);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home_tablet);
		mContext = this;

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();

		bUserIsInSidePark = LLDCApplication.isInsideThePark;

		mHomeActivityTabletHandler = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1003) {

					try {
						mFragmentManager = getSupportFragmentManager();
						Fragment f = mFragmentManager
								.findFragmentById(R.id.frame_container);
						if (f instanceof WelcomeFragment) {
							((WelcomeFragment) f).onSetServerData();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (message.what == 1006) {
					try {
						Intent msgIntent = new Intent(HomeActivityTablet.this,
								LLDC_Sync_Servcie.class);
						startService(msgIntent);
					} catch (Exception e) {
						// TODO: handle exception
						LLDCApplication.onShowLogCat(
								"HomeActivity",
								"onInitiateApplication exception is "
										+ e.toString());
					}
				} else if (message.what == 1007) {
					displayView(2);
					rlSearchHeaderDefault.setVisibility(View.VISIBLE);
					showCommonSearchIcon();
					changeBg(2);
					mHomeActivityTabletHandler.sendEmptyMessageDelayed(1008,
							500);
				} else if (message.what == 1008) {
					hideFooterData();
				} else if (message.what == 1009) {
					mFragmentManager = getSupportFragmentManager();
					Fragment f = mFragmentManager
							.findFragmentById(R.id.frame_container);
					if (f instanceof WelcomeFragment) {
						if (LLDCApplication.isInsideThePark) {
							tvHeaderTitle.setText("Welcome");
						} else {
							tvHeaderTitle.setText("");
						}
					}

				} else if (message.what == 1100) {
					// rlbac_dim_layout.setVisibility(View.VISIBLE);
				} else if (message.what == 1102) {
					if (location == null) {
						LLDCApplication
								.onShowToastMesssage(
										getApplicationContext(),
										"Not able to find your location. We shall retry. Please do check your Location settings.");
					}
				} else if (message.what == 1070) {
					try {
						LocationServices.FusedLocationApi
								.requestLocationUpdates(mGoogleApiClient,
										LLDCApplication.getLocationRequest(),
										HomeActivityTablet.this);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if (message.what == 8001) {
					mFragmentManager = getSupportFragmentManager();
					mFragmentManager.popBackStack();

				}
			};
		};

		/* Common header */
		llElementsOfFooter = (LinearLayout) findViewById(R.id.llElementsOfFooter);
		rlSearchHeaderDefault = (RelativeLayout) findViewById(R.id.relMenu);
		rlBackBtn = (RelativeLayout) findViewById(R.id.rlBackBtn);
		tvHeaderTitle = (TextView) findViewById(R.id.tvPageTitle);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivNavigation = (ImageView) findViewById(R.id.ivNavigation);
		ivCommonSearch = (ImageView) findViewById(R.id.ivCommonSearch);
		rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);

		rlOpenedFooter = (RelativeLayout) findViewById(R.id.rlOpenFooter);
		viewFooterBlur = (View) findViewById(R.id.footerblur);

		rlWelcomeTab = (RelativeLayout) findViewById(R.id.rlWelcomeTab);
		rlMapTab = (RelativeLayout) findViewById(R.id.rlMapTab);
		rlExploreTab = (RelativeLayout) findViewById(R.id.rlExploreTab);
		rlEventsTab = (RelativeLayout) findViewById(R.id.rlEventsTab);
		rlTheParkTab = (RelativeLayout) findViewById(R.id.rlTheParkTab);
		rlRelaxTab = (RelativeLayout) findViewById(R.id.rlRelaxTab);
		rlEntertainTab = (RelativeLayout) findViewById(R.id.rlEntertainTab);
		rlActiveTab = (RelativeLayout) findViewById(R.id.rlActiveTab);
		rlHeadingOfFooter = (RelativeLayout) findViewById(R.id.rlHeadingOfFooter);
		tvWhatAreYouLookingToDo = (TextView) findViewById(R.id.tvWhatAreYouLookingToDo);
		view1 = (View) findViewById(R.id.viewUnderline);
		view2 = (View) findViewById(R.id.viewDivider2);
		view3 = (View) findViewById(R.id.viewDivider3);
		tvWelcome = (TextView) findViewById(R.id.tvWelcome);
		tvMap = (TextView) findViewById(R.id.tvMap);
		tvExplore = (TextView) findViewById(R.id.tvExplore);
		tvEvents = (TextView) findViewById(R.id.tvEvents);
		tvThePark = (TextView) findViewById(R.id.tvThePark);
		ivWelcomeHome = (ImageView) findViewById(R.id.ivWelcomeHome);
		ivMap = (ImageView) findViewById(R.id.ivMap);
		ivExplore = (ImageView) findViewById(R.id.ivExplore);
		ivEvents = (ImageView) findViewById(R.id.ivEvents);
		ivThePark = (ImageView) findViewById(R.id.ivThePark);

		rlWelcomeTab.setOnClickListener(new OnitemClickListener());
		rlMapTab.setOnClickListener(new OnitemClickListener());
		rlExploreTab.setOnClickListener(new OnitemClickListener());
		rlEventsTab.setOnClickListener(new OnitemClickListener());
		rlTheParkTab.setOnClickListener(new OnitemClickListener());
		rlHeadingOfFooter.setOnClickListener(new OnitemClickListener());
		rlRelaxTab.setOnClickListener(new OnitemClickListener());
		rlEntertainTab.setOnClickListener(new OnitemClickListener());
		rlActiveTab.setOnClickListener(new OnitemClickListener());
		rlOpenedFooter.setOnClickListener(new OnitemClickListener());
		viewFooterBlur.setOnClickListener(new OnitemClickListener());
		ivCommonSearch.setOnClickListener(new OnitemClickListener());
		rlBackBtn.setOnClickListener(new OnitemClickListener());
		ivNavigation.setOnClickListener(new OnitemClickListener());
		/* by default set welcome fragment */
		displayView(Constants.WELCOME_FRAGMENT);
		rlSearchHeaderDefault.setVisibility(View.GONE);

		Location location = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);

		if (location != null)
			onLocationChanged(location);

		mHomeActivityTabletHandler.sendEmptyMessageDelayed(1102, 60000);

		mHomeActivityTabletHandler.sendEmptyMessageDelayed(1006,
				LLDCApplication.nextServerCall);

		if (LLDCApplication.isDebug) {
			LLDCApplication.onShowToastMesssage(getApplicationContext(),
					"Application is running in DEBUG mode");
		}

	}

	public void showBackButton() {
		ivBack.setVisibility(View.VISIBLE);
		rlBackBtn.setEnabled(true);
	}

	public void hideBackButton() {
		ivBack.setVisibility(View.INVISIBLE);
		rlBackBtn.setEnabled(false);
	}

	public void showNavigationIcon() {
		/* if user is outside hide navigation button */
		if (!LLDCApplication.isInsideThePark) {
			ivNavigation.setVisibility(View.GONE);
		} else {
			ivNavigation.setVisibility(View.VISIBLE);
		}
		// ivNavigation.setVisibility(View.VISIBLE);
		ivCommonSearch.setVisibility(View.GONE);
	}

	public void showCommonSearchIcon() {
		ivNavigation.setVisibility(View.GONE);
		ivCommonSearch.setVisibility(View.VISIBLE);
	}

	public void hideFooterData() {
		rlHeadingOfFooter.setVisibility(View.VISIBLE);
		llElementsOfFooter.setVisibility(View.GONE);
		viewFooterBlur.setVisibility(View.GONE);
	}

	public void showFooterData() {
		rlHeadingOfFooter.setVisibility(View.GONE);
		llElementsOfFooter.setVisibility(View.VISIBLE);
		viewFooterBlur.setVisibility(View.VISIBLE);

	}

	void switchFooter(boolean isFooterOpen) {
		if (isFooterOpen) {
			bIsFooterOpen = false;
			rlHeadingOfFooter.setVisibility(View.VISIBLE);
			llElementsOfFooter.setVisibility(View.GONE);
			viewFooterBlur.setVisibility(View.GONE);
		} else {
			rlHeadingOfFooter.setVisibility(View.GONE);
			llElementsOfFooter.setVisibility(View.VISIBLE);
			viewFooterBlur.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(this, LLDCApplication.flurryKey);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGoogleApiClient.connect();
	}

	@Override
	public void onPause() {
		super.onPause();
		mGoogleApiClient.disconnect();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			mGoogleApiClient.disconnect();
			mHomeActivityTabletHandler.removeMessages(1006);
			mHomeActivityTabletHandler.removeMessages(1070);
			mHomeActivityTabletHandler = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class OnitemClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = null;

			switch (v.getId()) {

			case R.id.rlWelcomeTab:
				showFooterData();
				rlSearchHeaderDefault.setVisibility(View.GONE);
				displayView(Constants.WELCOME_FRAGMENT);
				changeBg(1);
				showCommonSearchIcon();
				break;

			case R.id.rlMapTab:
				hideFooterData();
				rlSearchHeaderDefault.setVisibility(View.VISIBLE);
				displayView(Constants.MAP_FRAGMENT);
				tvHeaderTitle.setText("Map");
				changeBg(2);
				showCommonSearchIcon();
				break;

			case R.id.rlExploreTab:
				hideFooterData();
				rlSearchHeaderDefault.setVisibility(View.VISIBLE);
				displayView(Constants.EXPLORE_FRAGMENT);
				tvHeaderTitle.setText("Explore");
				changeBg(3);
				break;

			case R.id.rlEventsTab:
				hideFooterData();
				rlSearchHeaderDefault.setVisibility(View.VISIBLE);
				displayView(Constants.EVENTS_FRAGMENT);
				tvHeaderTitle.setText("Events");
				changeBg(4);
				break;

			case R.id.rlTheParkTab:
				hideFooterData();
				rlSearchHeaderDefault.setVisibility(View.VISIBLE);
				displayView(Constants.THE_PARK_FRAGMENT);
				tvHeaderTitle.setText("The Park");
				changeBg(5);
				showCommonSearchIcon();
				break;

			case R.id.rlHeadingOfFooter:
				showFooterData();
				break;

			case R.id.rlOpenFooter:
				if (nLastSelected != Constants.WELCOME_FRAGMENT)
					hideFooterData();
				break;

			case R.id.rlRelaxTab:
				LLDCApplication
						.showSimpleProgressDialog(HomeActivityTablet.this);
				rlSearchHeaderDefault.setVisibility(View.VISIBLE);
				hideFooterData();
				displayView(Constants.RECOMMENDATIONS);
				bundle.putString("RECOMM_PAGETITLE", "relax");
				selectedNone();
				break;

			case R.id.rlEntertainTab:
				LLDCApplication
						.showSimpleProgressDialog(HomeActivityTablet.this);
				rlSearchHeaderDefault.setVisibility(View.VISIBLE);
				hideFooterData();
				displayView(Constants.RECOMMENDATIONS);
				bundle.putString("RECOMM_PAGETITLE", "entertain");
				selectedNone();
				break;

			case R.id.rlActiveTab:
				LLDCApplication
						.showSimpleProgressDialog(HomeActivityTablet.this);
				rlSearchHeaderDefault.setVisibility(View.VISIBLE);
				hideFooterData();
				displayView(Constants.RECOMMENDATIONS);
				bundle.putString("RECOMM_PAGETITLE", "active");
				selectedNone();
				break;

			case R.id.footerblur:
				// switchFooter(bIsFooterOpen);
				hideFooterData();
				break;

			case R.id.ivCommonSearch:
				LLDCApplication
						.showSimpleProgressDialog(HomeActivityTablet.this);
				intent = new Intent(HomeActivityTablet.this,
						SearchActivity.class);
				startActivity(intent);
				// startActivityForResult(intent, 0);
				overridePendingTransition(R.anim.right_in, R.anim.left_out);
				break;

			case R.id.rlBackBtn:

				break;

			case R.id.ivSearchBg:
				Log.e(TAG, "ivSearchBg");
				break;

			case R.id.ivNavigation:
				intent = new Intent(HomeActivityTablet.this,
						MapNavigationActivity.class);
				if (Constants.IS_TRAILS)
					intent.putExtra("PAGETITLE", "Trails");
				else
					intent.putExtra("PAGETITLE", "Map");
				try {
					LLDCApplication
							.showSimpleProgressDialog(HomeActivityTablet.this);
					startActivity(intent);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				break;
			}
		}
	}

	// Call Back method to get the Message form other Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// check if the request code is same as what is passed here it is 2
		Log.e(TAG, "onActivityResult requestCode::>" + requestCode);
		if (requestCode == RESULT_CANCELED) {// 0

			if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.EVENT) {
				bundle.putString("PAGETITLE", "Event");
				displaySearchView(1);

			} else if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.VENUE) {
				bundle.putString("PAGETITLE", "Venue");
				displaySearchView(2);
			} else if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.FACILITIES) {
				bundle.putString("PAGETITLE", "Facilities");
				displaySearchView(3);

			} else if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.TRAILS) {
				bundle.putString("PAGETITLE", "Trails");
				displaySearchView(3);

			}
		} else if (requestCode == RESULT_OK) {// 1
			Bundle mBundle = new Bundle();
			mBundle = data.getExtras();
			displaySearchItemView(1);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		if (bIsFooterOpen) {
			switchFooter(bIsFooterOpen);
		} else
			showQuitDialog();
	}

	void showQuitDialog() {
		AlertDialog quitAlertDialog = new AlertDialog.Builder(
				HomeActivityTablet.this)
				.setTitle("Exit")
				.setMessage("Confirm exit?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
						finish();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {
							}
						}).create();
		quitAlertDialog.setCanceledOnTouchOutside(false);
		quitAlertDialog.show();
		return;
	}

	/**
	 * Diplaying fragment view for selected tab in MenuActivity
	 * */
	private void displayView(int position) {

		LLDCApplication.showSimpleProgressDialog(HomeActivityTablet.this);

		Fragment fragment = null;
		switch (position) {
		case Constants.WELCOME_FRAGMENT:
			// rlHeader.setVisibility(View.GONE);
			// Location location = LocationServices.FusedLocationApi
			// .getLastLocation(mGoogleApiClient);
			// if (location != null)
			// onLocationChanged(location);
			viewFooterBlur.setVisibility(View.GONE);
			FlurryAgent.logEvent("Dashboard Screen");
			fragment = new co.uk.android.lldc.fragments.parent.WelcomeParentFragmentTablet();
			break;
		case Constants.MAP_FRAGMENT:
			rlHeader.setVisibility(View.VISIBLE);
			FlurryAgent.logEvent("Map Screen");
			fragment = new co.uk.android.lldc.fragments.parent.MapParentFragmentTablet();
			tvHeaderTitle.setText("Map");
			break;

		case Constants.EXPLORE_FRAGMENT:
			rlHeader.setVisibility(View.VISIBLE);
			FlurryAgent.logEvent("Explore Screen");
			fragment = new co.uk.android.lldc.fragments.parent.ExploreParentFragmentTablet();
			tvHeaderTitle.setText("Explore");
			break;

		case Constants.EVENTS_FRAGMENT:
			rlHeader.setVisibility(View.VISIBLE);
			FlurryAgent.logEvent("Event Screen");
			hideBackButton();
			fragment = new co.uk.android.lldc.fragments.parent.EventsParentFragmentTablet();
			tvHeaderTitle.setText("Events");
			break;

		case Constants.THE_PARK_FRAGMENT:
			rlHeader.setVisibility(View.VISIBLE);
			FlurryAgent.logEvent("The Park Screen");
			fragment = new co.uk.android.lldc.fragments.parent.TheParkParentFragmentTablet();
			tvHeaderTitle.setText("The Park");
			break;

		case Constants.RECOMMENDATIONS:
			rlHeader.setVisibility(View.VISIBLE);
			tvHeaderTitle.setText("Recommendations");
			fragment = new co.uk.android.lldc.fragments.parent.RecommendationaParentListingFragmentTablet();// RecommendationsListingFragment();
			fragment.setArguments(bundle);
			break;

		default:
			break;
		}

		try {
			// if (fragment != null && nLastSelected != position) {
			// if (position != Constants.RECOMMENDATIONS) {
			mFragmentManager = getSupportFragmentManager();
			mFragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
			// changeBg(position);

			// } else {
			// String szBackStackFragment = "";
			// if (WelcomeParentFragmentTablet.sWelcomeParentFragmentTablet !=
			// null) {
			// mFragmentManager =
			// WelcomeParentFragmentTablet.sWelcomeParentFragmentTablet
			// .getChildFragmentManager();
			// szBackStackFragment = WelcomeParentFragmentTablet.class
			// .getSimpleName();
			// showFooterData();
			// } else if (MapParentFragmentTablet.sMapParentFragmentTablet !=
			// null) {
			// mFragmentManager =
			// MapParentFragmentTablet.sMapParentFragmentTablet
			// .getChildFragmentManager();
			// szBackStackFragment = MapParentFragmentTablet.class
			// .getSimpleName();
			// } else if
			// (ExploreParentFragmentTablet.sExploreParentFragmentTablet !=
			// null) {
			// mFragmentManager =
			// ExploreParentFragmentTablet.sExploreParentFragmentTablet
			// .getChildFragmentManager();
			// szBackStackFragment = ExploreParentFragmentTablet.class
			// .getSimpleName();
			// } else if (EventsParentFragmentTablet.sEventsParentFragmentTablet
			// != null) {
			// mFragmentManager =
			// EventsParentFragmentTablet.sEventsParentFragmentTablet
			// .getChildFragmentManager();
			// szBackStackFragment = EventsParentFragmentTablet.class
			// .getSimpleName();
			// } else if (TheParkParentFragmentTablet.sParkParentFragmentTablet
			// != null) {
			//
			// mFragmentManager =
			// TheParkParentFragmentTablet.sParkParentFragmentTablet
			// .getChildFragmentManager();
			//
			// // if (Constants.FragmentRecommendationBack
			// // .equals(TheParkFragment.class.getSimpleName())) {
			// // mFragmentManager = TheParkFragment.sParkFragmentTablet
			// // .getParentFragment().getChildFragmentManager();
			// // szBackStackFragment =
			// // Constants.FragmentRecommendationBack;
			// // } else if (Constants.FragmentRecommendationBack
			// // .equals(StaticTrailsFragment.class.getSimpleName())) {
			// // mFragmentManager =
			// // StaticTrailsFragment.sStaticTrailsTablet
			// // .getParentFragment().getChildFragmentManager();
			// // szBackStackFragment =
			// // Constants.FragmentRecommendationBack;
			// // }
			// }
			//
			// FragmentManager manager = mFragmentManager;
			// FragmentTransaction transaction = manager.beginTransaction();
			//
			// Log.e(TAG, "Added fragment:: "
			// + Constants.FragmentRecommendationBack);
			//
			// // if (Constants.bIfRecommendationAdded) {
			// // transaction.disallowAddToBackStack();
			// // } else {
			//
			// String szLastAddedFragment = "";
			// if (mFragmentManager.getBackStackEntryCount() > 0) {
			// Log.e(TAG, "Back Added fragment yet: main activity "
			// + mFragmentManager.getBackStackEntryCount());
			// for (int i = 0; i < mFragmentManager
			// .getBackStackEntryCount(); i++) {
			// Log.e(TAG, "Back Added fragment NAMEs: main activity "
			// + i
			// + "  :   "
			// + mFragmentManager.getBackStackEntryAt(i)
			// .getName());
			// szLastAddedFragment = mFragmentManager
			// .getBackStackEntryAt(i).getName();
			// }
			//
			// if (Constants.FragmentRecommendationBack
			// .equals(szLastAddedFragment)) {
			// mFragmentManager.popBackStack();
			//
			// }
			//
			// for (int i = 0; i < mFragmentManager
			// .getBackStackEntryCount(); i++) {
			// if (mFragmentManager.getBackStackEntryAt(i).getName()
			// .contains("RecommendationsListingFragment"))
			// transaction.disallowAddToBackStack();
			// }
			//
			// // if (Constants.bIfRecommendationAdded) {
			// // for (int i = 0; i < mFragmentManager
			// // .getBackStackEntryCount(); i++) {
			// // if (mFragmentManager.getBackStackEntryAt(i)
			// // .getName()
			// // .contains("RecommendationsListingFragment"))
			// // transaction.remove(fragment);
			// // Log.e(TAG, "Removed");
			// // }
			// // }
			//
			// }
			// Log.e(TAG, "transaction.disallowAddToBackStack()::> "
			// + transaction.isAddToBackStackAllowed());
			// if (transaction.isAddToBackStackAllowed()) {
			// transaction
			//
			// // newInstance() is a static factory method.
			// .addToBackStack(
			// Constants.FragmentRecommendationBack)
			// .replace(
			// R.id.frame_container_parent,
			// fragment,
			// RecommendationsListingFragment.class
			// .getName())
			// .setTransition(
			// FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			// .commit();
			// }
			//
			// // mFragmentManager
			// // .beginTransaction()
			// // .addToBackStack(Constants.FragmentRecommendationBack)
			// // Constants.FragmentRecommendationBack
			// // .add(R.id.frame_container_parent, fragment,
			// // RecommendationsListingFragment.class.getName())
			// // .setTransition(
			// // FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			// // .commit();
			// // }
			//
			// }

			// update selected item and title, then close the drawer
			nLastSelected = position;

			// } else {
			// // error in creating fragment
			// Log.e(TAG, "Error in creating fragment");
			// }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void displaySearchView(int position) {

		String szFrameName = "";
		switch (position) {
		case 1:
			fragment = new EventDetailsFragmentTablet();
			szFrameName = EventDetailsFragmentTablet.class.getName();

			break;

		case 2:
			fragment = new VenueFragmentTablet();
			szFrameName = VenueFragmentTablet.class.getName();

			break;

		case 3:
			fragment = new ExploreListDetailsFragmentTablet();
			szFrameName = ExploreListDetailsFragmentTablet.class.getName();

			break;

		}
		if (fragment != null) {
			fragment.setArguments(bundle);

			if (WelcomeParentFragmentTablet.sWelcomeParentFragmentTablet != null) {
				mFragmentManager = WelcomeParentFragmentTablet.sWelcomeParentFragmentTablet
						.getChildFragmentManager();
			} else if (MapParentFragmentTablet.sMapParentFragmentTablet != null) {
				mFragmentManager = MapParentFragmentTablet.sMapParentFragmentTablet
						.getChildFragmentManager();
			} else if (ExploreParentFragmentTablet.sExploreParentFragmentTablet != null) {
				mFragmentManager = ExploreParentFragmentTablet.sExploreParentFragmentTablet
						.getChildFragmentManager();
			} else if (EventsParentFragmentTablet.sEventsParentFragmentTablet != null) {
				mFragmentManager = EventsParentFragmentTablet.sEventsParentFragmentTablet
						.getChildFragmentManager();
			} else if (TheParkParentFragmentTablet.sParkParentFragmentTablet != null) {
				mFragmentManager = TheParkParentFragmentTablet.sParkParentFragmentTablet
						.getChildFragmentManager();
			}

			mFragmentManager
					.beginTransaction()
					// .addToBackStack(arg0)
					.replace(R.id.frame_container_parent, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();

			// fragment.setArguments(bundle);
			// mFragmentManager = getSupportFragmentManager();
			// mFragmentManager.beginTransaction()
			// .replace(R.id.frame_container, fragment)
			// .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
			// .commit();
		}

	}

	private void displaySearchItemView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:

			// fragment = (Fragment) new SearchMapFragmentTablet();
			break;

		}

		if (fragment != null) {
			fragment.setArguments(bundle);

			if (WelcomeParentFragmentTablet.sWelcomeParentFragmentTablet != null) {
				mFragmentManager = WelcomeParentFragmentTablet.sWelcomeParentFragmentTablet
						.getChildFragmentManager();
			} else if (MapParentFragmentTablet.sMapParentFragmentTablet != null) {
				mFragmentManager = MapParentFragmentTablet.sMapParentFragmentTablet
						.getChildFragmentManager();
			} else if (ExploreParentFragmentTablet.sExploreParentFragmentTablet != null) {
				mFragmentManager = ExploreParentFragmentTablet.sExploreParentFragmentTablet
						.getChildFragmentManager();
			} else if (EventsParentFragmentTablet.sEventsParentFragmentTablet != null) {
				mFragmentManager = EventsParentFragmentTablet.sEventsParentFragmentTablet
						.getChildFragmentManager();
			} else if (TheParkParentFragmentTablet.sParkParentFragmentTablet != null) {
				mFragmentManager = TheParkParentFragmentTablet.sParkParentFragmentTablet
						.getChildFragmentManager();
			}

			mFragmentManager.beginTransaction()
					.replace(R.id.frame_container_parent, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}

	}

	void selectedNone() {
		tvWelcome.setTextColor(getResources().getColor(R.color.divider_tabs));
		tvMap.setTextColor(getResources().getColor(R.color.divider_tabs));
		tvExplore.setTextColor(getResources().getColor(R.color.divider_tabs));
		tvEvents.setTextColor(getResources().getColor(R.color.divider_tabs));
		tvThePark.setTextColor(getResources().getColor(R.color.divider_tabs));

		ivWelcomeHome.setImageResource(R.drawable.welcome_tab);
		ivMap.setImageResource(R.drawable.map_tab);
		ivExplore.setImageResource(R.drawable.explore_tab);
		ivEvents.setImageResource(R.drawable.events_tab);
		ivThePark.setImageResource(R.drawable.park_tab);
	}

	void changeBg(int nSelected) {

		tvWelcome.setTextColor(getResources().getColor(R.color.divider_tabs));
		tvMap.setTextColor(getResources().getColor(R.color.divider_tabs));
		tvExplore.setTextColor(getResources().getColor(R.color.divider_tabs));
		tvEvents.setTextColor(getResources().getColor(R.color.divider_tabs));
		tvThePark.setTextColor(getResources().getColor(R.color.divider_tabs));

		ivWelcomeHome.setImageResource(R.drawable.welcome_tab);
		ivMap.setImageResource(R.drawable.map_tab);
		ivExplore.setImageResource(R.drawable.explore_tab);
		ivEvents.setImageResource(R.drawable.events_tab);
		ivThePark.setImageResource(R.drawable.park_tab);

		if (nSelected == Constants.WELCOME_FRAGMENT) {

			tvWelcome.setTextColor(getResources().getColor(
					R.color.textcolor_pink));
			ivWelcomeHome.setImageResource(R.drawable.welcome_tab_selc);
		} else if (nSelected == Constants.MAP_FRAGMENT) {

			tvMap.setTextColor(getResources().getColor(R.color.textcolor_pink));
			ivMap.setImageResource(R.drawable.map_tab_selc);
		} else if (nSelected == Constants.EXPLORE_FRAGMENT) {

			tvExplore.setTextColor(getResources().getColor(
					R.color.textcolor_pink));
			ivExplore.setImageResource(R.drawable.explore_tab_selc);
		} else if (nSelected == Constants.EVENTS_FRAGMENT) {

			tvEvents.setTextColor(getResources().getColor(
					R.color.textcolor_pink));
			ivEvents.setImageResource(R.drawable.events_tab_selc);
		} else if (nSelected == Constants.THE_PARK_FRAGMENT) {

			tvThePark.setTextColor(getResources().getColor(
					R.color.textcolor_pink));
			ivThePark.setImageResource(R.drawable.park_tab_selc);
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

		double distance = DistanceUtil.distance(
				LLDCApplication.parkCenter.getLatitude(),
				LLDCApplication.parkCenter.getLongitude(), arg0.getLatitude(),
				arg0.getLongitude());

		BigDecimal bd = new BigDecimal(distance);
		BigDecimal res = bd.setScale(3, RoundingMode.DOWN);

		if (res.doubleValue() > LLDCApplication.PARK_RADIUS) {
			bUserIsInSidePark = false;
		} else {
			bUserIsInSidePark = true;
		}

		if (location == null)
			location = arg0;

		if (arg0.getLatitude() == location.getLatitude()
				&& arg0.getLongitude() == location.getLongitude()) {
			return;
		} else {
			location = arg0;
		}

		if (LLDCApplication.isDebug) {
			LLDCApplication.onShowToastMesssage(
					getApplicationContext(),
					"Location changed on Home Screen Lat: "
							+ arg0.getLatitude() + "Long: "
							+ arg0.getLongitude());
		}

		if (MapNavFragment.mMapNavFragmentHandler != null) {

			if (LLDCApplication.isDebug) {
				LLDCApplication.onShowToastMesssage(
						getApplicationContext(),
						"Location send to Map Navigation Fragment Lat: "
								+ arg0.getLatitude() + "Long: "
								+ arg0.getLongitude());
			}

			Message msg = new Message();
			Bundle b = new Bundle();
			b.putDouble("LAT", arg0.getLatitude());
			b.putDouble("LAN", arg0.getLongitude());
			msg.setData(b);
			msg.what = 1050;
			MapNavFragment.mMapNavFragmentHandler.sendMessage(msg);
		}

		if (MapFragment.mMapFragmentHandler != null) {

			if (LLDCApplication.isDebug) {
				LLDCApplication.onShowToastMesssage(
						getApplicationContext(),
						"Location send to Map Navigation Fragment Lat: "
								+ arg0.getLatitude() + "Long: "
								+ arg0.getLongitude());
			}

			Message msg = new Message();
			Bundle b = new Bundle();
			b.putDouble("LAT", arg0.getLatitude());
			b.putDouble("LAN", arg0.getLongitude());
			msg.setData(b);
			msg.what = 1050;
			MapFragment.mMapFragmentHandler.sendMessage(msg);
		}

		if (SearchMapFragment.mSearchMapFragment != null) {
			if (LLDCApplication.isDebug) {
				LLDCApplication.onShowToastMesssage(
						getApplicationContext(),
						"Location send to Map Search Fragment Lat: "
								+ arg0.getLatitude() + "Long: "
								+ arg0.getLongitude());
			}

			Message msg = new Message();
			Bundle b = new Bundle();
			b.putDouble("LAT", arg0.getLatitude());
			b.putDouble("LAN", arg0.getLongitude());
			msg.setData(b);
			msg.what = 1050;
			SearchMapFragment.mSearchMapFragment.sendMessage(msg);
		}

		if (MapTrailsFragment.mMapTrailsFragmentHandler != null) {
			if (LLDCApplication.isDebug) {
				LLDCApplication.onShowToastMesssage(
						getApplicationContext(),
						"Location send to Map Trails Fragment Lat: "
								+ arg0.getLatitude() + "Long: "
								+ arg0.getLongitude());
			}

			Message msg = new Message();
			Bundle b = new Bundle();
			b.putDouble("LAT", arg0.getLatitude());
			b.putDouble("LAN", arg0.getLongitude());
			msg.setData(b);
			msg.what = 1050;
			MapTrailsFragment.mMapTrailsFragmentHandler.sendMessage(msg);
		}

		if (LLDCApplication.isInsideThePark != bUserIsInSidePark) {
			LLDCApplication.isInsideThePark = bUserIsInSidePark;
			try {
				mFragmentManager = getSupportFragmentManager();
				Fragment f = mFragmentManager
						.findFragmentById(R.id.frame_container);
				if (f instanceof WelcomeFragment) {
					if (LLDCApplication.isInsideThePark) {
						tvHeaderTitle.setText("Welcome");
					} else {
						tvHeaderTitle.setText("");
					}
					((WelcomeFragment) f).onSetServerData();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		LLDCApplication
				.onShowToastMesssage(
						getApplicationContext(),
						"Not able to find your location. We shall retry. Please do check your Location settings.");
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		LocationServices.FusedLocationApi.requestLocationUpdates(
				mGoogleApiClient, LLDCApplication.getLocationRequest(), this);
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub

	}

}
