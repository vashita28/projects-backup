package uk.co.pocketapp.whotel.ui;

import uk.co.pocketapp.whotel.FragmentLoad;
import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.fragments.DealsFragment;
import uk.co.pocketapp.whotel.fragments.FindHotspotFragment;
import uk.co.pocketapp.whotel.fragments.HotelFragment;
import uk.co.pocketapp.whotel.fragments.HotelInfoFragment;
import uk.co.pocketapp.whotel.fragments.InsiderFragment;
import uk.co.pocketapp.whotel.fragments.MapGetDirectionFragment;
import uk.co.pocketapp.whotel.fragments.MapLocationFragment;
import uk.co.pocketapp.whotel.fragments.MapLocationHotelAndHotspotFragment;
import uk.co.pocketapp.whotel.fragments.WebViewFragment;
import uk.co.pocketapp.whotel.util.Util;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mblox.engage.MbloxManager;
import com.mblox.engage.util.SyncOnLocationChangeService;

public class MainActivity extends RootActivity implements View.OnClickListener,
		FragmentLoad {

	Button btn_insider, btn_deals, btn_hotel, btn_SPG, btn_hotspot,
			btn_SPG_Save, btn_SPG_Close;
	ImageView imageview_menu,
			imageview_menu_tappable,// imageview_map
			imageviewMyLocation, imageview_map_selection,
			imageview_list_selection;
	RelativeLayout relative_Map, relative_List;
	TextView textview_list, textview_map;

	Marker currentLocationMarker = null;

	boolean isMapOpen = false;

	LinearLayout linearSpgNumber;

	TextView textview_header;
	EditText et_Spg_Number;// et_Search

	Fragment mNewFragmentContent = null;// , mPreviousFragment = null;
	boolean bIsBtnSelected = false;

	public FragmentLoad mfragmentLoad;

	ProgressDialog mapDialog;

	RelativeLayout relative_searchPanel, relative_header, relative_Divider;

	public static Location currentLocation;

	static android.support.v4.app.FragmentManager fragmentManager;

	int nSlidingMenuItemPosition = -1;

	boolean bIsMenuSelectionChanged = false;

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent.getExtras() != null) {
			String szIsFromPushNotification = intent.getExtras().getString(
					"isPush");
			// Log.d("MainActivity()", "onNewIntent PUSH"
			// + szIsFromPushNotification);

			if (szIsFromPushNotification != null
					&& szIsFromPushNotification.equals("true")) {
				mNewFragmentContent = new DealsFragment(mfragmentLoad);
				switchFragment(mNewFragmentContent);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		super.init();

		mfragmentLoad = this;

		currentLocation = getLocationByProvider(LocationManager.GPS_PROVIDER);

		if (currentLocation == null)
			currentLocation = getLocationByProvider(LocationManager.NETWORK_PROVIDER);

		relative_header = (RelativeLayout) findViewById(R.id.rel_header);
		relative_searchPanel = (RelativeLayout) findViewById(R.id.rel_search_panel);

		textview_header = (TextView) findViewById(R.id.textview_header);

		// et_Search = (EditText) findViewById(R.id.et_search);
		// et_Search.setTypeface(whotelApplication.font_Book);
		// et_Search.setIncludeFontPadding(false);

		// et_Search.setHintTextColor(getResources().getColor(
		// R.color.search_font_color));

		et_Spg_Number = (EditText) findViewById(R.id.et_spg_number);

		btn_insider = (Button) findViewById(R.id.btn_insider);
		btn_deals = (Button) findViewById(R.id.btn_deals);
		btn_hotel = (Button) findViewById(R.id.btn_hotel);
		btn_SPG = (Button) findViewById(R.id.btn_spg);
		btn_hotspot = (Button) findViewById(R.id.btn_hotspot);
		btn_SPG_Save = (Button) findViewById(R.id.btn_spg_save);
		btn_SPG_Close = (Button) findViewById(R.id.btn_spg_close);

		linearSpgNumber = (LinearLayout) findViewById(R.id.ll_spg_number_layout);

		btn_insider.setOnClickListener(this);
		btn_deals.setOnClickListener(this);
		btn_hotel.setOnClickListener(this);
		btn_SPG.setOnClickListener(this);
		btn_hotspot.setOnClickListener(this);
		btn_SPG_Save.setOnClickListener(this);
		btn_SPG_Close.setOnClickListener(this);

		btn_insider.setTypeface(whotelApplication.font_Bold);
		btn_insider.setIncludeFontPadding(false);
		btn_deals.setTypeface(whotelApplication.font_Bold);
		btn_deals.setIncludeFontPadding(false);
		btn_hotel.setTypeface(whotelApplication.font_Bold);
		btn_hotel.setIncludeFontPadding(false);
		btn_SPG.setTypeface(whotelApplication.font_Bold);
		btn_SPG.setIncludeFontPadding(false);
		btn_hotspot.setTypeface(whotelApplication.font_Bold);
		btn_hotspot.setIncludeFontPadding(false);
		btn_SPG_Save.setTypeface(whotelApplication.font_BoldItalic);
		btn_SPG_Save.setIncludeFontPadding(false);
		btn_SPG_Close.setTypeface(whotelApplication.font_BoldItalic);
		btn_SPG_Close.setIncludeFontPadding(false);

		imageviewMyLocation = (ImageView) findViewById(R.id.imageview_gpslock);
		imageview_menu = (ImageView) findViewById(R.id.imageview_menu);
		imageview_menu_tappable = (ImageView) findViewById(R.id.imageview_menu_tappable);
		imageview_menu_tappable.setOnClickListener(this);

		relative_Divider = (RelativeLayout) findViewById(R.id.rl_divider);

		mNewFragmentContent = new InsiderFragment(mfragmentLoad);
		fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.main_frame, mNewFragmentContent)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
		// mPreviousFragment = mNewFragmentContent;
		btn_insider.setBackgroundResource(R.drawable.whotel_menu_insider_pink);

		imageview_map_selection = (ImageView) findViewById(R.id.imageview_top_white);
		imageview_list_selection = (ImageView) findViewById(R.id.imageview_top_pink);
		textview_list = (TextView) findViewById(R.id.textview_list);
		textview_map = (TextView) findViewById(R.id.textview_map);
		textview_list.setIncludeFontPadding(false);
		textview_map.setIncludeFontPadding(false);

		relative_Map = (RelativeLayout) findViewById(R.id.rl_map);
		relative_Map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideKeyBoard();

				imageview_map_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_pink));
				imageview_list_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_white_half));

				textview_list.setTextColor(getResources().getColor(
						R.color.textcolor_pink));
				textview_map.setTextColor(getResources()
						.getColor(R.color.white));

				if (!isMapOpen) {
					// My location visible
					imageviewMyLocation.setVisibility(View.VISIBLE);
					relative_Divider.setVisibility(View.VISIBLE);
					// imageview_map.setClickable(false);
					mapDialog = new ProgressDialog(MainActivity.this);
					mapDialog.setMessage("Loading map. Please wait...");
					mapDialog.setIndeterminate(false);
					mapDialog.setCancelable(false);
					mapDialog.show();

					final FragmentTransaction ft = fragmentManager
							.beginTransaction();

					if (mNewFragmentContent != null
							&& mNewFragmentContent.getClass().equals(
									DealsFragment.class)) { // deals on map
						mNewFragmentContent = new MapLocationFragment(
								mfragmentLoad, DealsFragment.messageDealsList,
								null, mapDialog);
						ft.replace(R.id.main_frame, mNewFragmentContent,
								"MapLocationFragment");
					} else if (
					// insider on map
					mNewFragmentContent != null
							&& mNewFragmentContent.getClass().equals(
									InsiderFragment.class)) {
						mNewFragmentContent = new MapLocationFragment(
								mfragmentLoad, null,
								InsiderFragment.insiderDataList, mapDialog);
						ft.replace(R.id.main_frame, mNewFragmentContent,
								"MapLocationFragment");
					} else if (
					// hotels on map
					mNewFragmentContent != null
							&& mNewFragmentContent.getClass().equals(
									HotelFragment.class)) {
						mNewFragmentContent = new MapLocationHotelAndHotspotFragment(
								mfragmentLoad, HotelFragment.hotelDataList,
								null, mapDialog);
						ft.replace(R.id.main_frame, mNewFragmentContent,
								"MapLocationHotelAndHotspotFragment");
					} else if (
					// hotspot on map
					mNewFragmentContent != null
							&& mNewFragmentContent.getClass().equals(
									FindHotspotFragment.class)) {
						mNewFragmentContent = new MapLocationHotelAndHotspotFragment(
								mfragmentLoad, null,
								FindHotspotFragment.hotspotDataList, mapDialog);
						ft.replace(R.id.main_frame, mNewFragmentContent,
								"MapLocationHotelAndHotspotFragment");
					}

					// switchFragment(mNewFragmentContent);

					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
							.addToBackStack(null).commit();
					menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
					// imageview_map
					// .setImageResource(R.drawable.whotel_insidermap_list_icon);
					// mPreviousFragment = mNewFragmentContent;
				}
				// else {
				// // getSupportFragmentManager().beginTransaction()
				// // .remove(mNewFragmentContent).commit();
				// fragmentManager.popBackStack();
				// isMapOpen = false;
				// imageview_map
				// .setImageResource(R.drawable.whotel_insider_earth_icon);
				// // My location disabled
				// imageviewMyLocation.setVisibility(View.GONE);
				// relative_Divider.setVisibility(View.GONE);
				// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				// // mNewFragmentContent = null;
				// }

			}
		});

		relative_List = (RelativeLayout) findViewById(R.id.rl_list);
		relative_List.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				imageview_map_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_white_full));
				imageview_list_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_pink_half));

				textview_list.setTextColor(getResources().getColor(
						R.color.white));
				textview_map.setTextColor(getResources().getColor(
						R.color.textcolor_pink));

				// getSupportFragmentManager().beginTransaction()
				// .remove(mNewFragmentContent).commit();
				fragmentManager.popBackStack();
				isMapOpen = false;
				// imageview_map
				// .setImageResource(R.drawable.whotel_insider_earth_icon);
				// My location disabled
				imageviewMyLocation.setVisibility(View.GONE);
				relative_Divider.setVisibility(View.GONE);
				menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				// mNewFragmentContent = null;
			}
		});

		// imageview_map = (ImageView) findViewById(R.id.imageview_choice_menu);
		// imageview_map.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// hideKeyBoard();
		//
		// if (!isMapOpen) {
		// // My location visible
		// imageviewMyLocation.setVisibility(View.VISIBLE);
		// relative_Divider.setVisibility(View.VISIBLE);
		// imageview_map.setClickable(false);
		// mapDialog = new ProgressDialog(MainActivity.this);
		// mapDialog.setMessage("Loading map. Please wait...");
		// mapDialog.setIndeterminate(false);
		// mapDialog.setCancelable(false);
		// mapDialog.show();
		//
		// final FragmentTransaction ft = fragmentManager
		// .beginTransaction();
		//
		// if (mNewFragmentContent != null
		// && mNewFragmentContent.getClass().equals(
		// DealsFragment.class)) { // deals on map
		// mNewFragmentContent = new MapLocationFragment(
		// mfragmentLoad, DealsFragment.messageDealsList,
		// null, mapDialog);
		// ft.replace(R.id.main_frame, mNewFragmentContent,
		// "MapLocationFragment");
		// } else if (
		// // insider on map
		// mNewFragmentContent != null
		// && mNewFragmentContent.getClass().equals(
		// InsiderFragment.class)) {
		// mNewFragmentContent = new MapLocationFragment(
		// mfragmentLoad, null,
		// InsiderFragment.insiderDataList, mapDialog);
		// ft.replace(R.id.main_frame, mNewFragmentContent,
		// "MapLocationFragment");
		// } else if (
		// // hotels on map
		// mNewFragmentContent != null
		// && mNewFragmentContent.getClass().equals(
		// HotelFragment.class)) {
		// mNewFragmentContent = new MapLocationHotelAndHotspotFragment(
		// mfragmentLoad, HotelFragment.hotelDataList,
		// null, mapDialog);
		// ft.replace(R.id.main_frame, mNewFragmentContent,
		// "MapLocationHotelAndHotspotFragment");
		// } else if (
		// // hotspot on map
		// mNewFragmentContent != null
		// && mNewFragmentContent.getClass().equals(
		// FindHotspotFragment.class)) {
		// mNewFragmentContent = new MapLocationHotelAndHotspotFragment(
		// mfragmentLoad, null,
		// FindHotspotFragment.hotspotDataList, mapDialog);
		// ft.replace(R.id.main_frame, mNewFragmentContent,
		// "MapLocationHotelAndHotspotFragment");
		// }
		//
		// // switchFragment(mNewFragmentContent);
		//
		// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
		// .addToBackStack(null).commit();
		// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// imageview_map
		// .setImageResource(R.drawable.whotel_insidermap_list_icon);
		// // mPreviousFragment = mNewFragmentContent;
		// } else {
		// // getSupportFragmentManager().beginTransaction()
		// // .remove(mNewFragmentContent).commit();
		// fragmentManager.popBackStack();
		// isMapOpen = false;
		// imageview_map
		// .setImageResource(R.drawable.whotel_insider_earth_icon);
		// // My location disabled
		// imageviewMyLocation.setVisibility(View.GONE);
		// relative_Divider.setVisibility(View.GONE);
		// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// // mNewFragmentContent = null;
		// }
		// }
		// });

		imageviewMyLocation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentLocation = getLocationByProvider(LocationManager.GPS_PROVIDER);

				if (currentLocation == null)
					currentLocation = getLocationByProvider(LocationManager.NETWORK_PROVIDER);
				// // Log.d("MainActivity", " my current location " +
				// // currentLocation);

				if (currentLocation != null) {
					if (mNewFragmentContent != null
							&& mNewFragmentContent.getClass().equals(
									MapLocationFragment.class)) { // deals,
																	// insider
																	// on map
						((MapLocationFragment) mNewFragmentContent)
								.drawMarkerCurrentLocation(currentLocation);
					} else if (mNewFragmentContent != null
							&& mNewFragmentContent.getClass().equals(
									MapLocationHotelAndHotspotFragment.class)) {
						((MapLocationHotelAndHotspotFragment) mNewFragmentContent)
								.drawMarkerCurrentLocation(currentLocation);
					}
				} else {
					Toast.makeText(MainActivity.this,
							"Not able to get a fix on your location",
							Toast.LENGTH_LONG).show();
				}

			}
		});

		// et_Search.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// et_Search.setCursorVisible(true);
		// return false;
		// }
		// });

		// et_Search.setOnEditorActionListener(new OnEditorActionListener() {
		// @Override
		// public boolean onEditorAction(TextView v, int actionId,
		// KeyEvent event) {
		// // TODO Auto-generated method stub
		// if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		// {
		// if (mNewFragmentContent != null
		// && mNewFragmentContent.getClass().equals(
		// DealsFragment.class)) {
		// ((DealsFragment) mNewFragmentContent)
		// .dealsListSearch(et_Search.getText()
		// .toString());
		// } else if (mNewFragmentContent != null
		// && mNewFragmentContent.getClass().equals(
		// InsiderFragment.class)) {
		// ((InsiderFragment) mNewFragmentContent)
		// .insiderListSearch(et_Search.getText()
		// .toString());
		// } else if (mNewFragmentContent != null
		// && mNewFragmentContent.getClass().equals(
		// FindHotspotFragment.class)) {
		// ((FindHotspotFragment) mNewFragmentContent)
		// .hotspotListSearch(et_Search.getText()
		// .toString());
		// } else if (mNewFragmentContent != null
		// && mNewFragmentContent.getClass().equals(
		// HotelFragment.class)) {
		//
		// ((HotelFragment) mNewFragmentContent)
		// .hotelListSearch(et_Search.getText()
		// .toString());
		// } else if (mNewFragmentContent != null
		// && mNewFragmentContent.getClass().equals(
		// MapLocationFragment.class)) {
		//
		// ((MapLocationFragment) mNewFragmentContent)
		// .locationSearch(et_Search.getText()
		// .toString());
		// } else if (mNewFragmentContent != null
		// && mNewFragmentContent
		// .getClass()
		// .equals(MapLocationHotelAndHotspotFragment.class)) {
		//
		// ((MapLocationHotelAndHotspotFragment) mNewFragmentContent)
		// .locationSearch(et_Search.getText()
		// .toString());
		// }
		//
		// hideKeyBoard();
		// // et_Search.setText("");
		//
		// }
		// return true;
		// }
		// return false;
		// }
		// });

		// et_Search.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		// // if (mNewFragmentContent != null
		// // && mNewFragmentContent.getClass().equals(
		// // DealsFragment.class))
		// // ((DealsFragment) mNewFragmentContent).dealsListSearch(s);
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		SyncOnLocationChangeService.setInterval(this, (5 * 60 * 1000));
		SyncOnLocationChangeService.setMinDistance(this, 200);
		SyncOnLocationChangeService.startService(this);
		MbloxManager.getInstance(this).registerAppOpen();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Util.mContext = this;
		// et_Search.setCursorVisible(false);

		if (mfragmentLoad == null)
			mfragmentLoad = this;

		if (getIntent().getExtras() != null) {
			String szIsFromPushNotification = getIntent().getExtras()
					.getString("isPush");
			// Log.d("MainActivity()", "on resume PUSH" +
			// szIsFromPushNotification);

			if (szIsFromPushNotification != null
					&& szIsFromPushNotification.equals("true")) {
				mNewFragmentContent = new DealsFragment(mfragmentLoad);
				switchFragment(mNewFragmentContent);
			}
		}
	}

	@Override
	public void onClick(View v) {

		if (nSlidingMenuItemPosition != v.getId()) {
			nSlidingMenuItemPosition = v.getId();
			bIsMenuSelectionChanged = true;
		} else
			nSlidingMenuItemPosition = -1;

		if (nSlidingMenuItemPosition != R.id.btn_spg
				&& nSlidingMenuItemPosition != R.id.btn_spg_save
				&& nSlidingMenuItemPosition != R.id.btn_spg_close)
			menu.toggle();

		if (nSlidingMenuItemPosition == R.id.btn_spg) {
			resetButtonBackground();
			btn_SPG.setBackgroundResource(R.drawable.whotel_spg_number_my_spg_btn);
			linearSpgNumber.setVisibility(View.VISIBLE);
		} else if (nSlidingMenuItemPosition == R.id.btn_spg_save) {
			MySpgSaveBtn();
			hideKeyBoard();
		} else if (nSlidingMenuItemPosition == R.id.btn_spg_close) {
			linearSpgNumber.setVisibility(View.INVISIBLE);
			et_Spg_Number.setText("");
			hideKeyBoard();
		}
	}

	public void MySpgSaveBtn() {
		if (et_Spg_Number.getText().length() <= 0) {
			AlertDialog m_AlertDialog = new AlertDialog.Builder(
					MainActivity.this)
					.setTitle("Alert!")
					.setMessage("Field is mandatory")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {

								}
							}).create();
			m_AlertDialog.setCanceledOnTouchOutside(false);
			m_AlertDialog.show();
		} else {
			linearSpgNumber.setVisibility(View.INVISIBLE);
			et_Spg_Number.setText("");
		}
	}

	void toggleButtonBackground(Button btn) {
		resetButtonBackground();
		btn.setBackgroundResource(R.drawable.whotel_menu_insider_pink);
		// else {
		// btn.setBackgroundResource(R.drawable.whotel_menu_insider_grey);
		// }
	}

	void resetButtonBackground() {
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		isMapOpen = false;
		btn_insider.setBackgroundResource(R.drawable.whotel_menu_insider_grey);
		btn_deals.setBackgroundResource(R.drawable.whotel_menu_insider_grey);
		btn_hotel.setBackgroundResource(R.drawable.whotel_menu_insider_grey);
		btn_hotspot.setBackgroundResource(R.drawable.whotel_menu_insider_grey);
		btn_SPG.setBackgroundResource(R.drawable.whotel_menu_insider_grey);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		try {
			hideKeyBoard();
		} catch (Exception e) {
			e.printStackTrace();
		}
		linearSpgNumber.setVisibility(View.INVISIBLE);
		// et_Search.setText("");
		fragmentManager.beginTransaction().replace(R.id.main_frame, fragment)
				.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();

		// imageview_map.setImageResource(R.drawable.whotel_insider_earth_icon);
		relative_Divider.setVisibility(View.GONE);
		imageviewMyLocation.setVisibility(View.GONE);
		isMapOpen = false;
		// mPreviousFragment = fragment;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// No call for super(). Bug on API Level > 11.
	}

	void hideKeyBoard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(et_Search.getWindowToken(), 0);
		// et_Search.setCursorVisible(false);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.main, menu);
	// return true;
	// }

	@Override
	void menuOpenedEvent() {
		// TODO Auto-generated method stub
		super.menuOpenedEvent();
		imageview_menu.setImageResource(R.drawable.whotel_pink_icon);
		// mNewFragmentContent = null;
		bIsBtnSelected = false;
		hideKeyBoard();
		bIsMenuSelectionChanged = false;
	}

	@Override
	void menuClosedEvent() {
		// TODO Auto-generated method stub
		super.menuClosedEvent();
		imageview_menu.setImageResource(R.drawable.whotel_insider_more_icon);
		hideKeyBoard();

		// mNewFragmentContent = null;
		if (nSlidingMenuItemPosition != -1 && bIsMenuSelectionChanged)
			switch (nSlidingMenuItemPosition) {
			case R.id.btn_insider:
				imageview_map_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_white_full));
				imageview_list_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_pink_half));

				textview_list.setTextColor(getResources().getColor(
						R.color.white));
				textview_map.setTextColor(getResources().getColor(
						R.color.textcolor_pink));

				// toggleButtonBackground(btn_insider);
				// textview_header.setText(getString(R.string.insider_title));
				mNewFragmentContent = new InsiderFragment(mfragmentLoad);
				bIsBtnSelected = true;
				break;
			case R.id.btn_deals:
				imageview_map_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_white_full));
				imageview_list_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_pink_half));

				textview_list.setTextColor(getResources().getColor(
						R.color.white));
				textview_map.setTextColor(getResources().getColor(
						R.color.textcolor_pink));
				// toggleButtonBackground(btn_deals);
				// textview_header.setText(getString(R.string.deals_title));
				mNewFragmentContent = new DealsFragment(mfragmentLoad);
				bIsBtnSelected = true;
				break;
			case R.id.btn_hotel:
				imageview_map_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_white_full));
				imageview_list_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_pink_half));

				textview_list.setTextColor(getResources().getColor(
						R.color.white));
				textview_map.setTextColor(getResources().getColor(
						R.color.textcolor_pink));
				toggleButtonBackground(btn_hotel);
				textview_header.setText(getString(R.string.hotel_title));
				mNewFragmentContent = new HotelFragment(mfragmentLoad);
				bIsBtnSelected = true;
				break;
			case R.id.btn_hotspot:
				imageview_map_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_white_full));
				imageview_list_selection.setImageDrawable(getResources()
						.getDrawable(R.drawable.whotel_insider_top_pink_half));

				textview_list.setTextColor(getResources().getColor(
						R.color.white));
				textview_map.setTextColor(getResources().getColor(
						R.color.textcolor_pink));
				toggleButtonBackground(btn_hotspot);
				textview_header.setText(getString(R.string.findhotspot_title));
				mNewFragmentContent = new FindHotspotFragment(mfragmentLoad);
				bIsBtnSelected = true;
				break;
			default:
				break;
			}

		if (bIsBtnSelected && mNewFragmentContent != null)
			switchFragment(mNewFragmentContent);

		if (isMapOpen) {
			// imageview_map
			// .setImageResource(R.drawable.whotel_insidermap_list_icon);
		} else {
			// imageview_map
			// .setImageResource(R.drawable.whotel_insider_earth_icon);
			relative_Divider.setVisibility(View.GONE);
			imageviewMyLocation.setVisibility(View.GONE);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(DealsFragment.class)) {
			((DealsFragment) mNewFragmentContent).onBackPressed();

			if (((DealsFragment) mNewFragmentContent).bIsSearchOngoing) {
				((DealsFragment) mNewFragmentContent).resetList();
				// et_Search.setText("");
				return;
			}
		}

		else if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(InsiderFragment.class)) {
			((InsiderFragment) mNewFragmentContent).onBackPressed();

			if (((InsiderFragment) mNewFragmentContent).bIsSearchOngoing) {
				((InsiderFragment) mNewFragmentContent).resetList();
				// et_Search.setText("");
				return;
			}
		}

		else if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(HotelFragment.class)) {
			((HotelFragment) mNewFragmentContent).onBackPressed();

			if (((HotelFragment) mNewFragmentContent).bIsSearchOngoing) {
				((HotelFragment) mNewFragmentContent).resetList();
				// et_Search.setText("");
				return;
			}
		}

		else if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(
						FindHotspotFragment.class)) {
			((FindHotspotFragment) mNewFragmentContent).onBackPressed();

			if (((FindHotspotFragment) mNewFragmentContent).bIsSearchOngoing) {
				((FindHotspotFragment) mNewFragmentContent).resetList();
				// et_Search.setText("");
				return;
			}
		}

		else if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(
						MapLocationFragment.class))
			((MapLocationFragment) mNewFragmentContent).onBackPressed();

		else if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(
						MapLocationHotelAndHotspotFragment.class))
			((MapLocationHotelAndHotspotFragment) mNewFragmentContent)
					.onBackPressed();

		else if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(WebViewFragment.class))
			((WebViewFragment) mNewFragmentContent).onBackPressed();

		else if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(
						MapGetDirectionFragment.class))
			((MapGetDirectionFragment) mNewFragmentContent).onBackPressed();

		if (menu.isMenuShowing()) {
			menu.toggle();
			return;
		}

		else if (mNewFragmentContent.getClass().equals(DealsFragment.class)
				|| mNewFragmentContent.getClass().equals(InsiderFragment.class)
				|| mNewFragmentContent.getClass().equals(
						FindHotspotFragment.class)
				|| mNewFragmentContent.getClass().equals(HotelFragment.class)) {
			AlertDialog quitAlertDialog = new AlertDialog.Builder(
					MainActivity.this)
					.setTitle("Exit")
					.setMessage("Confirm exit?")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
									int pid = android.os.Process.myPid();
									android.os.Process.killProcess(pid);
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

		super.onBackPressed();
	}

	@Override
	public void onWebviewFragmentLoad(WebViewFragment webViewFragment) {
		// TODO Auto-generated method stub
		relative_searchPanel.setVisibility(View.GONE);
		mNewFragmentContent = webViewFragment;
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	@Override
	public void onInsiderFragmentLoad(InsiderFragment insiderFragment) {
		textview_header.setText(getString(R.string.insider_title));
		resetButtonBackground();
		btn_insider.setBackgroundResource(R.drawable.whotel_menu_insider_pink);
		mNewFragmentContent = insiderFragment;
		// et_Search.setText("");
		topBarVisible();
	}

	@Override
	public void onDealsFragmentLoad(DealsFragment dealsFragment) {
		// TODO Auto-generated method stub
		textview_header.setText(getString(R.string.deals_title));
		resetButtonBackground();
		btn_deals.setBackgroundResource(R.drawable.whotel_menu_insider_pink);
		mNewFragmentContent = dealsFragment;

		if (MbloxManager.getInstance(this).getInbox().getMessages() != null
				&& MbloxManager.getInstance(this).getInbox().getMessages()
						.isEmpty()) {
			syncMessagesFromMBlox();
		}

		topBarVisible();
		// et_Search.setText("");
	}

	@Override
	public void onMapFragmentLoad(MapLocationFragment mapLocationFragment) {
		// TODO Auto-generated method stub
		// imageview_map.setImageResource(R.drawable.whotel_insidermap_list_icon);
		isMapOpen = true;
		mNewFragmentContent = mapLocationFragment;
		// et_Search.setText("");
		// imageview_map.setClickable(true);
		imageviewMyLocation.setVisibility(View.VISIBLE);
		relative_Divider.setVisibility(View.VISIBLE);
		topBarVisible();
	}

	@Override
	public void onMapHotelAndHotspotFragmentLoad(
			MapLocationHotelAndHotspotFragment mapLocationHotelAndHotspotFragment) {
		// TODO Auto-generated method stub
		// imageview_map.setImageResource(R.drawable.whotel_insidermap_list_icon);
		isMapOpen = true;
		mNewFragmentContent = mapLocationHotelAndHotspotFragment;
		// et_Search.setText("");
		// imageview_map.setClickable(true);
		imageviewMyLocation.setVisibility(View.VISIBLE);
		relative_Divider.setVisibility(View.VISIBLE);
		topBarVisible();
	}

	@Override
	public void onWebViewFragmentClosed() {
		// TODO Auto-generated method stub
		Log.d("MainActivity", "***Web View Closed on Key***");
		topBarVisible();
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	@Override
	public void onMapGetDirectionFragmentLoad(
			MapGetDirectionFragment mapgetdirectionFragment) {
		// TODO Auto-generated method stub
		relative_searchPanel.setVisibility(View.GONE);
		mNewFragmentContent = mapgetdirectionFragment;
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	@Override
	public void onMapGetDirectionFragmentClosed() {
		// TODO Auto-generated method stub
		Log.d("MainActivity", "***MapGetDirection Closed on Key***");
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	@Override
	public void onMapFragmentClosed() {
		// TODO Auto-generated method stub
		Log.d("MainActivity", "***MapLocation View Closed on Key***");

		// if (isMapOpen)
		// imageview_map
		// .setImageResource(R.drawable.whotel_insidermap_list_icon);
		// else
		// imageview_map.setImageResource(R.drawable.whotel_insider_earth_icon);
		relative_Divider.setVisibility(View.GONE);
		imageviewMyLocation.setVisibility(View.GONE);
		isMapOpen = false;
	}

	@Override
	public void onMapHotelAndHotspotFragmentClosed() {
		// TODO Auto-generated method stub
		Log.d("MainActivity",
				"***MapHotelAndHotspotFragment View Closed on Key***");

		// imageview_map.setImageResource(R.drawable.whotel_insider_earth_icon);
		relative_Divider.setVisibility(View.GONE);
		imageviewMyLocation.setVisibility(View.GONE);
		isMapOpen = false;
	}

	@Override
	void updateView() {
		// TODO Auto-generated method stub
		// super.updateView();
		try {
			if (mNewFragmentContent != null
					&& mNewFragmentContent.getClass().equals(
							DealsFragment.class)) {
				((DealsFragment) mNewFragmentContent).dealsUpdateView();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void refreshSyncMBlox() {
		syncMessagesFromMBlox();
	}

	@Override
	void syncMessagesFromMBlox() {
		// TODO Auto-generated method stub
		Log.d("MainActivity", "MainActivity-syncMessagesFromMBlox()");
		super.syncMessagesFromMBlox();
	}

	@Override
	public void onHotSpotFragmentLoad(FindHotspotFragment hotspotFragment) {
		// TODO Auto-generated method stub
		textview_header.setText(getString(R.string.findhotspot_title));
		resetButtonBackground();
		btn_hotspot.setBackgroundResource(R.drawable.whotel_menu_insider_pink);
		mNewFragmentContent = hotspotFragment;
		// et_Search.setText("");
		topBarVisible();
	}

	@Override
	public void onHotelFragmentLoad(HotelFragment hotelFragment) {
		// TODO Auto-generated method stub
		textview_header.setText(getString(R.string.hotel_title));
		resetButtonBackground();
		btn_hotel.setBackgroundResource(R.drawable.whotel_menu_insider_pink);
		mNewFragmentContent = hotelFragment;
		// et_Search.setText("");
		topBarVisible();
	}

	@Override
	public void onHotelInfoFragmentLoad(HotelInfoFragment hotelInfoFragment) {
		relative_header.setVisibility(View.GONE);
		relative_searchPanel.setVisibility(View.GONE);
	}

	@Override
	public void onHotelInfoFragmentClosed() {
		topBarVisible();
	}

	void topBarVisible() {
		relative_header.setVisibility(View.VISIBLE);
		relative_searchPanel.setVisibility(View.VISIBLE);
	}

	private Location getLocationByProvider(String provider) {
		Location location = null;
		if (!isProviderSupported(provider)) {
			return null;
		}
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean isProviderEnabled = false;
		try {
			isProviderEnabled = locationManager.isProviderEnabled(provider);
		} catch (Exception ex) {
		}
		return isProviderEnabled;

	}

	@Override
	public void noMatchFound() {
		// TODO Auto-generated method stub
		if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(DealsFragment.class)) {
			((DealsFragment) mNewFragmentContent).showNoMatchFoundDialog();
		} else if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(
						FindHotspotFragment.class)) {
			((FindHotspotFragment) mNewFragmentContent)
					.showNoMatchFoundDialog();
		} else if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(HotelFragment.class)) {
			((HotelFragment) mNewFragmentContent).showNoMatchFoundDialog();
		} else if (mNewFragmentContent != null
				&& mNewFragmentContent.getClass().equals(InsiderFragment.class)) {
			((InsiderFragment) mNewFragmentContent).showNoMatchFoundDialog();
		}
	}

	@Override
	public void emptySearchText() {
		// et_Search.setText("");
	}

}
