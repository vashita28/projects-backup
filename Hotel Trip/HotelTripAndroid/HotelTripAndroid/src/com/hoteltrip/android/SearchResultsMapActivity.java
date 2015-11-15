package com.hoteltrip.android;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.LinearGradient;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.hoteltrip.android.domain.Hotel;
import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.Utils;

public class SearchResultsMapActivity extends BaseActivity implements
		OnClickListener {

	Context context = this;
	Marker marker;
	LatLng myLocation = null;
	Button list_btn;
	Button btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resultsmap);
		btn_back = (Button) findViewById(R.id.btn_Back);
		btn_back.setTypeface(Utils
				.getHelveticaNeueBold(SearchResultsMapActivity.this));
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		// Get a handle to the Map Fragment
		final GoogleMap map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		if (map != null) {
			// map.setMyLocationEnabled(true);

			// to display layout on click of marker
			map.setInfoWindowAdapter(new InfoWindowAdapter() {

				public View getInfoWindow(Marker arg0) {
					// inflating layout
					View v = getLayoutInflater().inflate(
							R.layout.mapcentre_layout, null);
					return v;
				}

				public View getInfoContents(Marker arg0) {
					return null;
				}
			});

			Location location = AppValues
					.getLocation(SearchResultsMapActivity.this);

			if (location != null) {
				myLocation = new LatLng(location.getLatitude(),
						location.getLongitude());
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
						12.0f));
				marker = map.addMarker(new MarkerOptions().position(myLocation)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.maplocater)));
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
