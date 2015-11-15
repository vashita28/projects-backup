package co.uk.android.lldc.tablet;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.MapFragment;
import co.uk.android.lldc.fragments.MapNavFragment;
import co.uk.android.lldc.fragments.MapTrailsFragment;
import co.uk.android.lldc.fragments.SearchMapFragment;
import co.uk.android.lldc.utils.DistanceUtil;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class MapNavigationActivity extends FragmentActivity implements
		ConnectionCallbacks, LocationListener, OnConnectionFailedListener {

	static Context mContext;

	TextView tvPageTitle;
	RelativeLayout rlBackBtn;

	FragmentManager mFragmentManager;

	public boolean bIsFooterOpen = false;

	String mPageTitle = "";

	String pageSubTitle = "";
	String selectedId = "";

	LocationManager locationManager;
	String provider;

	Location location;

	private GoogleApiClient mGoogleApiClient;

	// ImageView ivSerach, ivNavigation;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map_navigation_tablet);

		mContext = this;

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();

		tvPageTitle = (TextView) findViewById(R.id.tvPageTitle);
		rlBackBtn = (RelativeLayout) findViewById(R.id.rlBackBtn);
		tvPageTitle.setText("Map");

		if (getIntent().getExtras() != null
				&& getIntent().getExtras().get("PAGETITLE") != null)
			mPageTitle = getIntent().getExtras().getString("PAGETITLE");

		rlBackBtn.setOnClickListener(new OnitemClickListener());

		location = LocationServices.FusedLocationApi
				.getLastLocation(mGoogleApiClient);
		if (location != null) {
			onLocationChanged(location);
		}

		// hideFooterData();
		if (mPageTitle.equals("Search")) {
			mPageTitle = "Map";
			pageSubTitle = getIntent().getExtras().getString("PAGESUBTITLE");
			selectedId = getIntent().getExtras().getString("SELID");
			if (pageSubTitle.equals("SHOWME")) {
				displayView(3);
			} else
				displayView(2);
		} else
			displayView(1);
		// tvHeaderTitle.setText(mPageTitle);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			mGoogleApiClient.disconnect();
			// mHomeActivityHandler.removeMessages(1006);
			// mHomeActivityHandler.removeMessages(1070);
			// mHomeActivityHandler = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void displayView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:
			if (mPageTitle.equals("Trails"))
				fragment = (Fragment) new MapTrailsFragment();
			else
				fragment = (Fragment) new MapNavFragment();
			break;
		case 2:
			fragment = (Fragment) new SearchMapFragment(pageSubTitle,
					selectedId);
			break;
		case 3:
			fragment = (Fragment) new MapFragment(pageSubTitle, selectedId);
			break;
		}

		if (fragment != null) {
			mFragmentManager = getSupportFragmentManager();
			mFragmentManager.beginTransaction()
					.add(R.id.frame_container, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.commit();
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		try {
			String mPageTitle = intent.getExtras().getString("PAGETITLE");
			if (mPageTitle.equals("Search")) {
				mPageTitle = "Map";
				pageSubTitle = getIntent().getExtras()
						.getString("PAGESUBTITLE");
				selectedId = getIntent().getExtras().getString("SELID");
				if (pageSubTitle.equals("SHOWME")) {
					displayView(3);
				} else
					displayView(2);
			} else
				displayView(1);
			// tvHeaderTitle.setText(mPageTitle);
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

			case R.id.relSearch:
				try {
					LLDCApplication
							.showSimpleProgressDialog(MapNavigationActivity.this);
					intent = new Intent(MapNavigationActivity.this,
							SearchActivity.class);
					intent.putExtra("PAGETITLE", "Search");
					startActivity(intent);
					overridePendingTransition(R.anim.right_in, R.anim.left_out);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case R.id.rlBackBtn:
				finish();
				break;
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (bIsFooterOpen) {
			// switchFooter(bIsFooterOpen);
		} else if (!pageSubTitle.equals("")) {
			mFragmentManager = getSupportFragmentManager();
			Fragment f = mFragmentManager
					.findFragmentById(R.id.frame_container);
			if (f instanceof MapNavFragment) {
				mFragmentManager
						.beginTransaction()
						.remove(f)
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.commit();
			} else {
				MapNavigationActivity.this.finish();
			}
		} else
			super.onBackPressed();
	}

	public static void finishActivity() {
		if (mContext != null)
			((Activity) mContext).finish();
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

		// if (res.doubleValue() > LLDCApplication.PARK_RADIUS) {
		// bUserIsInSidePark = false;
		// } else {
		// bUserIsInSidePark = true;
		// }

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
