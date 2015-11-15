package com.hoteltrip.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.message.BasicHttpResponse;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.crashlytics.android.Crashlytics;
import com.hoteltrip.android.receivers.NetworkBroadcastReceiver;
import com.hoteltrip.android.tasks.SearchHotelTask;
import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.Const;
import com.hoteltrip.android.util.DataBaseHelper;
import com.hoteltrip.android.util.DateFetchedListener;
import com.hoteltrip.android.util.HotelData;
import com.hoteltrip.android.util.NumberPicker;
import com.hoteltrip.android.util.SearchHotelDataFetched;
import com.hoteltrip.android.util.Utils;

public class FindHotelActivity extends BaseSherlockActivity implements
		DateFetchedListener, SearchHotelDataFetched {

	private ImageButton imagebtnAbout;
	private ImageButton imgbtnDestination;
	private static Button destSearchBtn;
	public ImageButton destionationimgbtn;
	private ImageButton imagebtnSearch;
	private EditText dateInputText;
	private Calendar calendarToday;
	private String placeCriterion;
	private String nearbyCriterion;
	BasicHttpResponse httpResponse = null;
	Context context = this;
	AutoCompleteTextView destinationtextfield;
	private int UPPER_LIMIT = 30;
	private int LOWER_LIMIT = 1;
	private LocationManager locManager;
	private SimpleDateFormat formatter, dateFormatter;
	private Double userLat;
	public ProgressDialog locationDialog;
	View myfavView, mybookingView;
	// The minimum time beetwen updates in milliseconds

	private Double userLon;
	private String[] COUNTRIESNAMES;

	NumberPicker pickerNumberOfNights, pickerNumberOfRooms;
	ProgressDialog progressSearchDialog;

	LinearLayout ll_room_occupancy, ll_room_details;

	Handler mHandler;
	static CurrentValueForRoomOccupancy currentRoomsRequired;
	// TextView roomOccupancyDetailsText;
	ScrollView mainScrollView;

	List<FindHotelActivity.RoomOccupancyDetails> roomListDetails;

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getExtras() != null) {
			Bundle bundleRoomDetails = intent.getExtras();
			int totalNoOfRooms = bundleRoomDetails.getInt("totalrooms");
			Log.e("FindHotelActivity()", "total no of rooms:: "
					+ totalNoOfRooms);

			roomListDetails = new ArrayList<FindHotelActivity.RoomOccupancyDetails>();

			RoomOccupancyDetails roomDetails;
			for (int i = 0; i < totalNoOfRooms; i++) {
				roomDetails = new RoomOccupancyDetails();
				roomDetails.numberOfAdults = bundleRoomDetails
						.getInt("numberofadults" + (i + 1));
				roomDetails.numberOfChild = bundleRoomDetails
						.getInt("numberofchild" + (i + 1));
				roomDetails.ageOfChild1 = bundleRoomDetails
						.getInt("ageoffirstchild" + (i + 1));
				roomDetails.ageOfChild2 = bundleRoomDetails
						.getInt("ageofsecondchild" + (i + 1));
				roomListDetails.add(roomDetails);
			}

			pickerNumberOfRooms.setCurrent(totalNoOfRooms);
			ll_room_occupancy.removeAllViews();
			for (int i = 0; i < roomListDetails.size(); i++) {
				inflateAndAddLayout(i, roomListDetails);
			}
		}
	}

	void inflateAndAddLayout(int i,
			final List<FindHotelActivity.RoomOccupancyDetails> roomListDetails) {
		// for (int i = 0; i < currentRoomsRequired.currentSelectedValue - 1;
		// i++) {
		String szRoomOccupancyDetails = "<font color=#ce5f02>Room #%s:</font> <font color=#3C3F38>%s Adult(s), %s Child</font>";

		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view_room_occupancy_details = inflater.inflate(
				R.layout.room_occupancy_details_row, null);

		TextView roomOccupancyDetailsText = (TextView) view_room_occupancy_details
				.findViewById(R.id.tv_room_occupancy_details);

		if (roomListDetails != null) {
			int childCount = roomListDetails.get(i).numberOfChild;
			int adultCount = roomListDetails.get(i).numberOfAdults;
			String szChildCount = String.valueOf(childCount);
			if (childCount == 0) {
				szChildCount = "No";
			}

			String szAdultCount = String.valueOf(adultCount);

			roomOccupancyDetailsText
					.setText(Html.fromHtml(String.format(
							szRoomOccupancyDetails, i + 1, szAdultCount,
							szChildCount)));
		}

		else
			roomOccupancyDetailsText.setText(Html.fromHtml(String.format(
					szRoomOccupancyDetails, i + 1, "1", "No")));

		ll_room_occupancy.addView(view_room_occupancy_details);

		// }

		mainScrollView.post(new Runnable() {
			public void run() {
				mainScrollView.scrollTo(0, mainScrollView.getBottom());
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Crashlytics.start(this);

		setContentView(R.layout.activity_find_hotel);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		ArrayAdapter<String> adapter;
		myfavView = (View) findViewById(R.id.fav_view);
		mybookingView = (View) findViewById(R.id.booking_view);
		mainScrollView = (ScrollView) findViewById(R.id.sv_find_hotel);
		formatter = new SimpleDateFormat("EEE, MMM dd, yyyy");
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		imagebtnAbout = (ImageButton) findViewById(R.id.imgbtn_about);
		imagebtnAbout.setOnClickListener(onAboutClick);

		imagebtnSearch = (ImageButton) findViewById(R.id.imgbtn_search);
		imagebtnSearch.setOnClickListener(onSearchClick);

		dateInputText = (EditText) findViewById(R.id.et_checkin);
		calendarToday = Calendar.getInstance();

		// COUNTRIESNAMES = getResources().getStringArray(R.array.countrynames);

		try {
			COUNTRIESNAMES = new DataBaseHelper(FindHotelActivity.this)
					.getAllCountriesAndCitiesCombination();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// // Print out the values to the log
		// for (int i = 0; i < COUNTRIESNAMES.length; i++) {
		// Log.i(this.toString(), COUNTRIESNAMES[i]);
		// }

		myfavView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myfav = new Intent(FindHotelActivity.this,
						MyFavouriteActivity.class);
				startActivity(myfav);
			}
		});

		mybookingView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myfav = new Intent(FindHotelActivity.this,
						MyBookingActivity.class);
				startActivity(myfav);
			}
		});

		// adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_dropdown_item_1line, COUNTRIESNAMES);
		adapter = new ArrayAdapter<String>(this, R.layout.dropdown_list_item,
				COUNTRIESNAMES);
		destinationtextfield = (AutoCompleteTextView) findViewById(R.id.atv_destination);
		destinationtextfield.setTypeface(Utils
				.getHelveticaNeue(FindHotelActivity.this));
		destinationtextfield.setText("Bangkok, Thailand");
		destinationtextfield.setSelection(destinationtextfield.getText()
				.length());
		// pre-populate the date input with current date
		dateInputText.setTypeface(Utils
				.getHelveticaNeue(FindHotelActivity.this));
		dateInputText.setText(formatter.format(calendarToday.getTime()));
		dateInputText.setTag(calendarToday);
		destinationtextfield.setAdapter(adapter);

		destinationtextfield.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						destinationtextfield.getWindowToken(), 0);
			}

		});

		destinationtextfield.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							destinationtextfield.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});

		ll_room_occupancy = (LinearLayout) findViewById(R.id.ll_room_occupancy);
		ll_room_details = (LinearLayout) findViewById(R.id.ll_room_details);
		// roomOccupancyDetailsText = (TextView)
		// findViewById(R.id.tv_room_occupancy_details);
		//
		// String szRoomOccupancyDetails =
		// "<font color=#ce5f02>Room #%s:</font> <font color=#000>1 Adult(s), No Child</font>";
		// roomOccupancyDetailsText.setText(Html.fromHtml(String.format(
		// szRoomOccupancyDetails, 1)));
		//
		// View roomFirstOccupancyDetails = (View)
		// findViewById(R.id.room_first_occupancy_details);
		//
		// roomFirstOccupancyDetails.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(FindHotelActivity.this,
		// RoomOccupancyDetailsActivity.class);
		// startActivity(intent);
		// }
		// });

		inflateAndAddLayout(0, null);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);

				roomListDetails = new ArrayList<FindHotelActivity.RoomOccupancyDetails>();

				RoomOccupancyDetails roomDetails;
				for (int i = 0; i < pickerNumberOfRooms.getCurrent(); i++) {
					roomDetails = new RoomOccupancyDetails();
					roomDetails.numberOfAdults = 1;
					roomDetails.numberOfChild = 0;
					roomDetails.ageOfChild1 = 0;
					roomDetails.ageOfChild2 = 0;
					roomListDetails.add(roomDetails);
				}

				if (message.what == Const.INCREMENT) {
					inflateAndAddLayout(message.arg1 - 1, null);
				} else if (message.what == Const.DECREMENT) {
					ll_room_occupancy
							.removeView(ll_room_occupancy
									.getChildAt(currentRoomsRequired.currentSelectedValue));
				}
				mainScrollView.post(new Runnable() {
					public void run() {
						mainScrollView.scrollTo(0, mainScrollView.getBottom());
					}
				});
			}
		};

		// TODO: number picker integration
		pickerNumberOfNights = (NumberPicker) findViewById(R.id.picker_number_of_nights);
		pickerNumberOfNights.setRange(LOWER_LIMIT, UPPER_LIMIT);
		pickerNumberOfNights.setFragmentManager(getSupportFragmentManager());

		pickerNumberOfRooms = (NumberPicker) findViewById(R.id.picker_number_of_rooms);
		pickerNumberOfRooms.setRange(1, 6);
		currentRoomsRequired = new CurrentValueForRoomOccupancy();
		pickerNumberOfRooms.setCallBackAfterNumberChange(mHandler,
				currentRoomsRequired);
		pickerNumberOfRooms.setFragmentManager(getSupportFragmentManager());

		imgbtnDestination = (ImageButton) findViewById(R.id.imgbtn_destination);
		imgbtnDestination.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// progressSearch.setVisibility(View.VISIBLE);
				getMyCurrentLocation();
			}
		});

		try {
			placeCriterion = getIntent().getExtras()
					.getString("placeCriterion");
		} catch (Exception e) {
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
			// locToggleBtn.setChecked(false);
		} else if (nearbyCriterion != null) {
			destSearchBtn.setText(nearbyCriterion);
			// locToggleBtn.setChecked(true);
		} else {
			// locToggleBtn.setChecked(true); // as the default
		}

		// mybooking = (ImageButton) findViewById(R.id.imgbtn_mybooking);
		// myfav = (ImageButton) findViewById(R.id.imgbtn_myfav);
		// myfav.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intr = new Intent(context, MyFavouriteActivity.class);
		// startActivity(intr);
		// }
		// });
		// mybooking.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intr = new Intent(context, MyBookingActivity.class);
		// startActivity(intr);
		// }
		// });

		ll_room_details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FindHotelActivity.this,
						RoomOccupancyDetailsActivity.class);
				Bundle bundleRoomDetails = new Bundle();
				bundleRoomDetails.putInt("totalrooms",
						pickerNumberOfRooms.getCurrent());

				if (roomListDetails != null)
					for (int i = 0; i < roomListDetails.size(); i++) {
						bundleRoomDetails.putInt("numberofadults" + (i + 1),
								roomListDetails.get(i).numberOfAdults);
						bundleRoomDetails.putInt("numberofchild" + (i + 1),
								roomListDetails.get(i).numberOfChild);
						bundleRoomDetails.putInt("ageoffirstchild" + (i + 1),
								roomListDetails.get(i).ageOfChild1);
						bundleRoomDetails.putInt("ageofsecondchild" + (i + 1),
								roomListDetails.get(i).ageOfChild2);
					}

				intent.putExtras(bundleRoomDetails);

				startActivity(intent);
			}
		});

	}

	public class CurrentValueForRoomOccupancy {
		public int currentSelectedValue = 1;
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
			FindHotelActivity.this.userLat = location.getLatitude();
			FindHotelActivity.this.userLon = location.getLongitude();
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
			intent = new Intent(getApplicationContext(),
					MyFavouriteActivity.class);

			break;
		case R.id.menu_bookings:
			intent = new Intent(getApplicationContext(),
					MyBookingActivity.class);

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
			// Intent confirmationIntent = new Intent(getApplicationContext(),
			// ConfirmationActivity.class);
			// confirmationIntent.putExtra("bookingresponse",
			// AppValues.ms_szBookingResponse);
			// startActivity(confirmationIntent);

		}
	};

	View.OnClickListener onSearchClick = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				AppValues.nNumberOfRooms = pickerNumberOfRooms.getCurrent();
				AppValues.ms_szSearchHotelResponse = "";
				SearchHotelTask searchTask = new SearchHotelTask(
						FindHotelActivity.this, FindHotelActivity.this);

				String szDestination = destinationtextfield.getText()
						.toString();

				if (szDestination != null && szDestination.length() > 0) {

					String szCityName = "", szCountryName = "";

					try {
						szCityName = szDestination.substring(0,
								szDestination.lastIndexOf(",")).trim();
						szCountryName = szDestination.substring(
								szDestination.lastIndexOf(",") + 1).trim();
					} catch (Exception e) {
						e.printStackTrace();
					}
					Log.i("FindHotelActivity", "onSearchClick()-CityName:: "
							+ szCityName + ", CountryName:: " + szCountryName);

					if (szCityName.length() > 0 && szCountryName.length() > 0) {
						progressSearchDialog = new ProgressDialog(
								FindHotelActivity.this);
						progressSearchDialog
								.setMessage("Searching Hotels. Please wait...");
						progressSearchDialog.setIndeterminate(true);
						progressSearchDialog.setCancelable(false);
						progressSearchDialog.show();
						DataBaseHelper dbHelper;
						Cursor cursor;
						try {
							dbHelper = new DataBaseHelper(
									FindHotelActivity.this);

							String whereClause = "CityName =? AND CountryName =?";
							// String[] whereArgs = new String[] { "Bangkok",
							// "Thailand" };
							String[] whereArgs = new String[] { szCityName,
									szCountryName };
							cursor = dbHelper.myDataBase.query("codes", null,
									whereClause, whereArgs, null, null, null);
							if (cursor.moveToFirst()) {
								AppValues.szDestCity=searchTask.szDestCity = cursor.getString(3);
								AppValues.szDestCountry=searchTask.szDestCountry = cursor.getString(1);
								searchTask.szPaxPassport = cursor.getString(1);
								AppValues.szPaxPassport = searchTask.szPaxPassport;
								
							}

							// whereClause = "name =?";
							// // whereArgs = new String[] { "Thailand" };
							// whereArgs = new String[] { szCountryName };
							// cursor = dbHelper.myDataBase.query("paxpassport",
							// null, whereClause, whereArgs, null, null,
							// null);
							// if (cursor.moveToFirst())
							// searchTask.szPaxPassport = cursor.getString(1);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						searchTask.szNoOfNights = String
								.valueOf(pickerNumberOfNights.getCurrent());
						AppValues.nNumberOfNights = pickerNumberOfNights
								.getCurrent();

						Calendar calendarForSearch = (Calendar) dateInputText
								.getTag();
						searchTask.szCheckInDate = dateFormatter
								.format(calendarForSearch.getTime());
						AppValues.szCheckInDate = searchTask.szCheckInDate;
						AppValues.checkinCalendar = calendarForSearch;
						AppValues.roomListDetails=searchTask.roomListDetails = roomListDetails;
						searchTask.execute();

					} else {
						Toast.makeText(
								FindHotelActivity.this,
								"Please choose a valid destination from the list",
								Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(FindHotelActivity.this,
							"Please enter destination", Toast.LENGTH_LONG)
							.show();
				}
			} else
				Toast.makeText(FindHotelActivity.this, "No network connection",
						Toast.LENGTH_LONG).show();
		}

	};

	View.OnClickListener onDestinationSearchClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent destinationSearchIntent = new Intent(
					getApplicationContext(), DestinationSearchActivity.class);

			// if (locToggleBtn.isChecked()) {
			destinationSearchIntent.putExtra("tabIndex", 1);
			// } else {
			// destinationSearchIntent.putExtra("tabIndex", 0);
			// }

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
			dateInputText.setTag(calendarToday);
		} else if (calculateOneYear(selectedCalendar)) {
			showMyDialog(Const.SELECT_DATE_GREATER_ERROR_ID, null);
			dateInputText.setText(formatter.format(calendarToday.getTime()));
			dateInputText.setTag(calendarToday);
		} else {
			dateInputText.setText(formatter.format(selectedCalendar.getTime()));
			dateInputText.setTag(selectedCalendar);
			// calendarToday = selectedCalendar;
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

	@Override
	public void SearchHotelDataResult(String szResult) {
		// TODO Auto-generated method stub

		if (szResult != null && szResult.equals("No Data Found.")) {
			dismissSearchDialog();
			Toast.makeText(FindHotelActivity.this, "No Data Found.",
					Toast.LENGTH_LONG).show();
			return;
		} else if (szResult == null) {
			dismissSearchDialog();
			Toast.makeText(
					FindHotelActivity.this,
					"Network Error. Please check network connection and try again",
					Toast.LENGTH_LONG).show();
		} else if (szResult != null) {

			Intent searchIntent = new Intent(FindHotelActivity.this,
					SearchResultsActivity.class);

			AppValues.ms_szSelectedDestination = destinationtextfield.getText()
					.toString();
			AppValues.ms_szSearchHotelResponse = szResult;

			// JsonParser parser = new JsonParser();
			// The JsonElement is the root node. It can be an object, array,
			// null or
			// java primitive.
			// JsonElement element = parser.parse(szResult);
			// use the isxxx methods to find out the type of jsonelement. In our
			// example we know that the root object is the SearchHotel_Response
			// object and
			// contains an array of Hotel objects
			HotelData hotelData;
			ArrayList<HotelData> hotelDataList = new ArrayList<HotelData>();
			// if (element.isJsonObject()) {
			// JsonObject jsonObject = element.getAsJsonObject();
			try {
				JSONObject jsonObject = new JSONObject(szResult);
				JSONArray hotelArray = jsonObject.getJSONObject(
						"SearchHotel_Response").getJSONArray("Hotel");
				for (int i = 0; i < hotelArray.length(); i++) {
					hotelData = new HotelData();
					JSONObject jsonObjectHotel = hotelArray.getJSONObject(i);
					JSONObject jsonObjectHotelValues = jsonObjectHotel
							.getJSONObject("@attributes");
					try {
						hotelData.setObject("RoomCateg",
								jsonObjectHotel.getJSONObject("RoomCateg"));
					} catch (Exception e) {
						// e.printStackTrace();
						try {
							hotelData.setArray("RoomCateg",
									jsonObjectHotel.getJSONArray("RoomCateg"));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					hotelData.setObject("trip_advisor_data",
							jsonObjectHotel.getJSONObject("trip_advisor_data"));

					hotelData.setHotelId(jsonObjectHotelValues
							.getString("HotelId"));
					hotelData.setHotelName(jsonObjectHotelValues
							.getString("HotelName"));
					hotelData.setRating(Float.valueOf(jsonObjectHotelValues
							.getString("Rating")));
					hotelData.setCurrency(jsonObjectHotelValues
							.getString("Currency"));
					hotelData.setCheckInDate(jsonObjectHotelValues
							.getString("dtCheckIn"));
					hotelData.setCheckOutDate(jsonObjectHotelValues
							.getString("dtCheckOut"));
					AppValues.szCheckOutDate = jsonObjectHotelValues
							.getString("dtCheckOut");
					hotelData.setInternalCode(jsonObjectHotelValues
							.getString("InternalCode"));

					// JsonElement jsonElement =
					// jsonObjectHotel.get("RoomCateg");
					// if (jsonElement.isJsonObject())
					// hotelData.setObject("RoomCateg",
					// jsonElement.getAsJsonObject());
					//
					// jsonElement = jsonObjectHotel.get("trip_advisor_data");
					// if (jsonElement.isJsonObject())
					// hotelData.setObject("trip_advisor_data",
					// jsonElement.getAsJsonObject());
					hotelDataList.add(hotelData);
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					JSONObject jsonObject = new JSONObject(szResult);
					JSONObject jhotelObject = jsonObject.getJSONObject(
							"SearchHotel_Response").getJSONObject("Hotel");
					hotelData = new HotelData();
					JSONObject jsonObjectHotelValues = jhotelObject
							.getJSONObject("@attributes");
					try {
						hotelData.setObject("RoomCateg",
								jhotelObject.getJSONObject("RoomCateg"));
					} catch (Exception e1) {
						// e.printStackTrace();
						try {
							hotelData.setArray("RoomCateg",
									jhotelObject.getJSONArray("RoomCateg"));
						} catch (Exception e2) {
							e1.printStackTrace();
						}
					}
					hotelData.setObject("trip_advisor_data",
							jhotelObject.getJSONObject("trip_advisor_data"));

					hotelData.setHotelId(jsonObjectHotelValues
							.getString("HotelId"));
					hotelData.setHotelName(jsonObjectHotelValues
							.getString("HotelName"));
					hotelData.setRating(Float.valueOf(jsonObjectHotelValues
							.getString("Rating")));
					hotelData.setCurrency(jsonObjectHotelValues
							.getString("Currency"));

					hotelData.setCheckInDate(jsonObjectHotelValues
							.getString("dtCheckIn"));
					hotelData.setCheckOutDate(jsonObjectHotelValues
							.getString("dtCheckOut"));
					AppValues.szCheckOutDate = jsonObjectHotelValues
							.getString("dtCheckOut");
					hotelData.setInternalCode(jsonObjectHotelValues
							.getString("InternalCode"));

					// JsonElement jsonElement =
					// jsonObjectHotel.get("RoomCateg");
					// if (jsonElement.isJsonObject())
					// hotelData.setObject("RoomCateg",
					// jsonElement.getAsJsonObject());
					//
					// jsonElement = jsonObjectHotel.get("trip_advisor_data");
					// if (jsonElement.isJsonObject())
					// hotelData.setObject("trip_advisor_data",
					// jsonElement.getAsJsonObject());
					hotelDataList.add(hotelData);
				} catch (Exception e5) {
					e5.printStackTrace();
				}

			}
			// }

			AppValues.hotelDataList = hotelDataList;
			AppValues.ms_szSearchHotelResponse = "";

			// if location is toggled, just bypass the null check
			// if (locToggleBtn.isChecked()) {

			if (nearbyCriterion != null) {
				// optionally if the nearbyCriterion isnt null, pass it
				// to
				// search results
				searchIntent.putExtra("nearbyCriterion", nearbyCriterion);
			} else {
				// pass the lat lon to search results
				searchIntent
						.putExtra("userLat", FindHotelActivity.this.userLat);
				searchIntent
						.putExtra("userLon", FindHotelActivity.this.userLon);
			}

			dismissSearchDialog();
			startActivity(searchIntent);
			// } else if (!locToggleBtn.isChecked()) {

			if (placeCriterion != null) {
				searchIntent.putExtra("placeCriterion", placeCriterion);
				startActivity(searchIntent);
			}
			// else {
			// showMyDialog(Const.SEARCH_CRITERIA_NULL_ID, null);
			// }
			// }
		}
	}

	void dismissSearchDialog() {
		if (progressSearchDialog != null)
			progressSearchDialog.dismiss();
	}

	void getMyCurrentLocation() {
		String CityName = "";
		String StateName = "";
		String CountryName = "";

		Location location = null;
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		if (location != null) {

			try {
				// Getting address from found locations.
				Geocoder geocoder;

				List<Address> addresses;
				geocoder = new Geocoder(this, Locale.getDefault());
				addresses = geocoder.getFromLocation(location.getLatitude(),
						location.getLongitude(), 1);

				StateName = addresses.get(0).getAdminArea();
				CityName = addresses.get(0).getLocality();
				CountryName = addresses.get(0).getCountryName();

				destinationtextfield.setText(CityName + ", " + CountryName);

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(FindHotelActivity.this,
						"Error in connection. Please try again.",
						Toast.LENGTH_LONG)

				.show();
			}
		} else {
			Toast.makeText(FindHotelActivity.this,
					"Unable to get Current Location.Please try again.",
					Toast.LENGTH_LONG)

			.show();
		}
	}

	public class RoomOccupancyDetails {
		public int ageOfChild1;
		public int ageOfChild2;
		public int numberOfAdults;
		public int numberOfChild;
	}
}
