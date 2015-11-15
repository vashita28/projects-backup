package com.hoteltrip.android;

import com.crashlytics.android.Crashlytics;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.joda.time.DateTime;
import org.joda.time.Years;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.hoteltrip.android.util.Const;
import com.hoteltrip.android.util.DateFetchedListener;
import com.hoteltrip.android.util.NumberPicker;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends BaseSherlockActivity implements
		DateFetchedListener {

	private Button aboutBtn;
	private ToggleButton locToggleBtn;
	private static Button destSearchBtn;
	private Button searchBtn;
	private TextView dateInputText;
	private Calendar calendarToday;
	private String placeCriterion;
	private String nearbyCriterion;

	private int UPPER_LIMIT = 30;
	private int LOWER_LIMIT = 1;
	private LocationManager locManager;
	private SimpleDateFormat formatter;
	private Double userLat;
	private Double userLon;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Crashlytics.start(this);

		setContentView(R.layout.activity_main);
		formatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		LinearLayout customABView = (LinearLayout) getLayoutInflater().inflate(
				R.layout.main_custom_actionbar_view, null);
		customABView.setGravity(Gravity.RIGHT);
		actionBar.setCustomView(customABView, new ActionBar.LayoutParams(
				Gravity.RIGHT));
		actionBar.setDisplayShowCustomEnabled(true);

		aboutBtn = (Button) customABView.findViewById(R.id.aboutBtn);
		aboutBtn.setOnClickListener(onAboutClick);

		destSearchBtn = (Button) findViewById(R.id.destinationSearchBtn);
		destSearchBtn.setOnClickListener(onDestinationSearchClick);

		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(onSearchClick);

		dateInputText = (TextView) findViewById(R.id.dateInputText);

		calendarToday = Calendar.getInstance();

		// pre-populate the date input with current date
		dateInputText.setText(formatter.format(calendarToday.getTime()));

		// TODO: number picker integration
		NumberPicker numberPicker = (NumberPicker) findViewById(R.id.main_numberpicker);
		numberPicker.setRange(LOWER_LIMIT, UPPER_LIMIT);
		numberPicker.setFragmentManager(getSupportFragmentManager());

		locToggleBtn = (ToggleButton) findViewById(R.id.locationToggle);
		locToggleBtn.setOnCheckedChangeListener(toggleChanged);

		try {
			placeCriterion = getIntent().getExtras()
					.getString("placeCriterion");
		} catch (Exception e) {
			// this is to catch null pointer exception because there's no
			// defaultvalue for bundle extras in older APIs
		}

		try {
			nearbyCriterion = getIntent().getExtras().getString(
					"nearbyCriterion");
		} catch (Exception e) {
			// this is to catch null pointer exception because there's no
			// defaultvalue for bundle extras in older APIs
		}

		if (placeCriterion != null) {
			destSearchBtn.setText(placeCriterion);
			locToggleBtn.setChecked(false);
		} else if (nearbyCriterion != null) {
			destSearchBtn.setText(nearbyCriterion);
			locToggleBtn.setChecked(true);
		} else {
			locToggleBtn.setChecked(true); // as the default
		}

	}

	LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onLocationChanged(Location location) {
			Toast.makeText(
					getApplicationContext(),
					"X is " + location.getLatitude() + "and Y is "
							+ location.getLongitude(), Toast.LENGTH_LONG)
					.show();
			MainActivity.this.userLat = location.getLatitude();
			MainActivity.this.userLon = location.getLongitude();
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locManager != null) {
			locManager.removeUpdates(locationListener);
		}
	}

	OnCheckedChangeListener toggleChanged = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			if (isChecked == true) {

				// TODO: set current location and set text
				if (nearbyCriterion != null) {
					destSearchBtn.setText(nearbyCriterion);
				} else {
					destSearchBtn.setText("Current Location");
					// request location updates from location manager using best
					// location provider, but with gps as priority
					locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

					Criteria criteria = new Criteria();
					criteria.setAccuracy(Criteria.ACCURACY_FINE);

					String bestLocProvider = locManager.getBestProvider(
							criteria, true);
					Toast.makeText(getApplicationContext(), bestLocProvider,
							Toast.LENGTH_SHORT).show();
					locManager.requestLocationUpdates(bestLocProvider,
							Const.FIVE_MINUTES, 0, locationListener);
					// }

				}

			} else {
				// TODO: unset current location
				if (locManager != null) {
					locManager.removeUpdates(locationListener);
				}

				if (placeCriterion != null) {
					destSearchBtn.setText(placeCriterion);
				} else {
					destSearchBtn.setText("City Name");
				}
			}

		}
	};

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.menu_favorites:
			intent = new Intent(getApplicationContext(), MyFavouriteActivity.class);

			break;
		case R.id.menu_bookings:
			intent = new Intent(getApplicationContext(), MyBookingActivity.class);

			break;
		}
		startActivity(intent);

		return true;
	}

	View.OnClickListener onAboutClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(getApplicationContext(),
					AboutActivity.class));
		}
	};

	View.OnClickListener onSearchClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent searchIntent = new Intent(getApplicationContext(),
					SearchResultsActivity.class);

			// if location is toggled, just bypass the null check
			if (locToggleBtn.isChecked()) {

				if (nearbyCriterion != null) {
					// optionally if the nearbyCriterion isnt null, pass it to
					// search results
					searchIntent.putExtra("nearbyCriterion", nearbyCriterion);
				} else {
					// pass the lat lon to search results
					searchIntent.putExtra("userLat", MainActivity.this.userLat);
					searchIntent.putExtra("userLon", MainActivity.this.userLon);
				}

				startActivity(searchIntent);
			} else if (!locToggleBtn.isChecked()) {

				if (placeCriterion != null) {
					searchIntent.putExtra("placeCriterion", placeCriterion);
					startActivity(searchIntent);
				} else {
					showMyDialog(Const.SEARCH_CRITERIA_NULL_ID, null);
				}
			}
		}

	};

	View.OnClickListener onDestinationSearchClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent destinationSearchIntent = new Intent(
					getApplicationContext(), DestinationSearchActivity.class);

			if (locToggleBtn.isChecked()) {
				destinationSearchIntent.putExtra("tabIndex", 1);
			} else {
				destinationSearchIntent.putExtra("tabIndex", 0);
			}

			startActivity(destinationSearchIntent);
		}
	};

	public void onDatePickerClick(final View v) {
		showMyDialog(Const.DATE_PICKER_DIALOG_ID, this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void setSelectedDate(Calendar selectedCalendar) {

		if (calendarToday.compareTo(selectedCalendar) == 1) {
			showMyDialog(Const.SELECT_DATE_BEFORE_ERROR_ID, null);
			dateInputText.setText(formatter.format(calendarToday.getTime()));
		} else if (calculateOneYear(selectedCalendar)) {
			showMyDialog(Const.SELECT_DATE_GREATER_ERROR_ID, null);
			dateInputText.setText(formatter.format(calendarToday.getTime()));
		} else {
			dateInputText.setText(formatter.format(selectedCalendar.getTime()));
		}
	}

	private boolean calculateOneYear(Calendar selectedCal) {
		DateTime todayDate = DateTime.now();
		DateTime selectedDate = new DateTime(selectedCal.getTime());

		Years y = Years.yearsBetween(todayDate, selectedDate);
		Years oneYear = Years.ONE;

		if (y.isGreaterThan(oneYear)) {
			return true;
		} else {
			return false;
		}
	}

}
