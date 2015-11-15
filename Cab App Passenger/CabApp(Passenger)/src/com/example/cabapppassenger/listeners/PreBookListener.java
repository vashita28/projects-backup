/*
 * Coded By Manpreet
 */

package com.example.cabapppassenger.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumLocations;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumWhen;
import com.example.cabapppassenger.model.LocationParcelable;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.task.AddFavourites;
import com.example.cabapppassenger.task.GetLatLongTask;
import com.example.cabapppassenger.util.Constant;

public class PreBookListener implements View.OnClickListener {

	/*
	 * Variables that use basic java datatypes
	 */

	public boolean programmaticPickUpOnMapClick, programmaticPickUpFavClick,
			programmaticPickUpRecentClick, programmaticDropOffOnMapClick,
			programmaticDropOffFavClick, programmaticDropOffRecentClick, isFav,
			isOnMap;
	/*
	 * Variables that use basic java Android activity specific datatypes
	 */
	Activity activity;
	Context context;
	Handler mhandler;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";

	/*
	 * Variables that use basic Android layout specific datatypes
	 */
	View view;
	TextView tvPickUpAdd, tvPickUpLocation, tvPickUpOnMap, tvPickUpMyPlaces,
			tvDropOffAdd, tvDropOffLocation, tvDropOffOnMap, tvDropOffMyPlaces,
			tvWhenAsap, tvWhenPreBook, tvWhenDate, tvWhenTime, tvPickUpRecent,
			tvDropOffRecent;

	AutoCompleteTextView atvPickUpAddress, atvDropOffAddress;
	EditText etPickUpPinCode, etDropOffPinCode;
	PreBookParcelable pbParcelable;
	ScrollView scrollView;

	public Handler pickUpLatLngHandler, pickupPincodeHandler,
			dropOffLatLngHandler, dropOffPincodeHandler;

	LocationParcelable[] selectedValues;

	public PreBookListener(final Context context, View view,
			PreBookParcelable pbParcelableLocal, Handler handler) {
		super();
		this.context = context;
		activity = (Activity) context;
		this.view = view;
		scrollView = (ScrollView) view.findViewById(R.id.sv_main);
		shared_pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		this.pbParcelable = pbParcelableLocal;
		editor.putString("Parcelable", pbParcelable.toString());
		editor.commit();
		this.mhandler = handler;
		tvPickUpAdd = (TextView) view.findViewById(R.id.tv_pick_up_top_right);
		tvPickUpLocation = (TextView) view
				.findViewById(R.id.tv_pick_up_location);
		tvPickUpOnMap = (TextView) view.findViewById(R.id.tv_pick_up_onmap);
		tvPickUpRecent = (TextView) view.findViewById(R.id.tv_pick_up_recent);
		tvPickUpMyPlaces = (TextView) view
				.findViewById(R.id.tv_pick_up_favorites);
		tvDropOffRecent = (TextView) view.findViewById(R.id.tv_drop_off_recent);
		tvDropOffAdd = (TextView) view.findViewById(R.id.tv_drop_off_top_right);
		tvDropOffLocation = (TextView) view
				.findViewById(R.id.tv_drop_off_location);
		tvDropOffOnMap = (TextView) view.findViewById(R.id.tv_drop_off_onmap);
		tvDropOffMyPlaces = (TextView) view
				.findViewById(R.id.tv_drop_off_favorites);
		atvPickUpAddress = (AutoCompleteTextView) view
				.findViewById(R.id.atv_pick_up_address);
		etPickUpPinCode = (EditText) view.findViewById(R.id.et_pick_up_pincode);
		atvDropOffAddress = (AutoCompleteTextView) view
				.findViewById(R.id.atv_drop_off_address);
		etDropOffPinCode = (EditText) view
				.findViewById(R.id.et_drop_off_pincode);
		tvWhenAsap = (TextView) view.findViewById(R.id.tv_w_asap);
		tvWhenPreBook = (TextView) view.findViewById(R.id.tv_w_prebook);
		tvWhenDate = (TextView) view.findViewById(R.id.tv_w_date);
		tvWhenTime = (TextView) view.findViewById(R.id.tv_w_time);
		programmaticDropOffFavClick = programmaticDropOffOnMapClick = programmaticDropOffRecentClick = programmaticPickUpFavClick = programmaticPickUpOnMapClick = programmaticPickUpRecentClick = false;

		pickUpLatLngHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					selectedValues = getSelectedLocationValues(pbParcelable);
					pbParcelable.getDefaultPickUpLocation().setAddress1(
							selectedValues[0].getAddress1());
					pbParcelable.getDefaultPickUpLocation().setPinCode(
							selectedValues[0].getPinCode());
					pbParcelable.getDefaultPickUpLocation().setLatitude(
							bundleData.getDouble("latitude"));
					pbParcelable.getDefaultPickUpLocation().setLongitude(
							bundleData.getDouble("longitude"));
					if (isFav) {

						isFav = false;
						AddFavourites favourites = new AddFavourites(context,
								pbParcelable.getDefaultPickUpLocation()
										.getAddress1(), pbParcelable
										.getDefaultPickUpLocation()
										.getPinCode(), pbParcelable
										.getDefaultPickUpLocation()
										.getLatitude(), pbParcelable
										.getDefaultPickUpLocation()
										.getLongitude(), tvPickUpAdd);
						favourites.execute(Constant.passengerURL
								+ "ws/v2/passenger/favourite/?accessToken="
								+ shared_pref.getString("AccessToken", ""));

					}

					if (isOnMap) {
						isOnMap = false;
						pbParcelable.getMapPickUpLocation().setAddress1(
								pbParcelable.getDefaultPickUpLocation()
										.getAddress1());
						pbParcelable.getMapPickUpLocation().setPinCode(
								pbParcelable.getDefaultPickUpLocation()
										.getPinCode());
						pbParcelable.getMapPickUpLocation().setLatitude(
								pbParcelable.getDefaultPickUpLocation()
										.getLatitude());
						pbParcelable.getMapPickUpLocation().setLongitude(
								pbParcelable.getDefaultPickUpLocation()
										.getLongitude());
						msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("pickUpOnMap", "");
						msg.obj = bundle;
						msg.setData(bundle);
						mhandler.sendMessage(msg);
					}

				}

			}
		};

		dropOffLatLngHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					selectedValues = getSelectedLocationValues(pbParcelable);
					pbParcelable.getDefaultDropOffLocation().setAddress1(
							selectedValues[1].getAddress1());
					pbParcelable.getDefaultDropOffLocation().setPinCode(
							selectedValues[1].getPinCode());
					pbParcelable.getDefaultDropOffLocation().setLatitude(
							bundleData.getDouble("latitude"));
					pbParcelable.getDefaultDropOffLocation().setLongitude(
							bundleData.getDouble("longitude"));
					if (isFav) {
						isFav = false;
						AddFavourites favourites = new AddFavourites(context,
								pbParcelable.getDefaultDropOffLocation()
										.getAddress1(), pbParcelable
										.getDefaultDropOffLocation()
										.getPinCode(), pbParcelable
										.getDefaultDropOffLocation()
										.getLatitude(), pbParcelable
										.getDefaultDropOffLocation()
										.getLongitude(), tvDropOffAdd);
						favourites.execute(Constant.passengerURL
								+ "ws/v2/passenger/favourite/?accessToken="
								+ shared_pref.getString("AccessToken", ""));

					}
					if (isOnMap) {
						isOnMap = false;
						pbParcelable.getMapDropOffLocation().setAddress1(
								pbParcelable.getDefaultDropOffLocation()
										.getAddress1());
						pbParcelable.getMapDropOffLocation().setPinCode(
								pbParcelable.getDefaultDropOffLocation()
										.getPinCode());
						pbParcelable.getMapDropOffLocation().setLatitude(
								pbParcelable.getDefaultDropOffLocation()
										.getLatitude());
						pbParcelable.getMapDropOffLocation().setLongitude(
								pbParcelable.getDefaultDropOffLocation()
										.getLongitude());
						msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("dropOffOnMap", "");
						msg.obj = bundle;
						msg.setData(bundle);
						mhandler.sendMessage(msg);
					}
				}

			}
		};

		dropOffPincodeHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					pbParcelable.getDefaultDropOffLocation().setPinCode(
							bundleData.getString("pincode"));
					etDropOffPinCode.setText(pbParcelable
							.getDefaultDropOffLocation().getPinCode());

				}

			}
		};

		pickupPincodeHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				if (msg != null && msg.peekData() != null) {
					Bundle bundleData = (Bundle) msg.obj;

					pbParcelable.getDefaultPickUpLocation().setPinCode(
							bundleData.getString("pincode"));
					etPickUpPinCode.setText(pbParcelable
							.getDefaultPickUpLocation().getPinCode());

				}

			}
		};
	}

	public PreBookListener(Context mcontext, View view,
			PreBookParcelable pbparceable) {
		super();
		this.context = mcontext;
		activity = (Activity) context;
		this.view = view;
		this.pbParcelable = pbparceable;
		shared_pref = context.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
	}

	public void setParcebledata(PreBookParcelable pbParcelable) {
		this.pbParcelable = pbParcelable;
	}

	@Override
	public void onClick(View v) {

		// hideKeybord(v);
		if (((Activity) context).getCurrentFocus() != null)
			((Activity) context).getCurrentFocus().clearFocus();
		if (pbParcelable == null)
			return;

		switch (v.getId()) {

		case R.id.tv_ao_header:
			// hideKeybord(v);
			toggleVisiblityAndIcons(v, R.drawable.accessibility);
			break;

		case R.id.tv_comments:
			toggleVisiblityAndIcons(v, R.drawable.comment);
			break;

		case R.id.tv_pick_up_location:
			pbParcelable.setePickUpSelected(EnumLocations.PRESENTLOCATION);
			pbParcelable.setDefaultPickUpLocation(new LocationParcelable());

			// hideKeybord(v);
			setTopDrawbleAndText(tvPickUpOnMap, R.drawable.onmap,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvPickUpLocation, R.drawable.track,
					R.color.textview_selected);
			setTopDrawbleAndText(tvPickUpRecent, R.drawable.recent,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvPickUpMyPlaces, R.drawable.favorites2,
					R.color.textcolor_grey);
			// pbParcelable.setStateSelected("CurrentLocation");
			atvPickUpAddress.setText(pbParcelable.getPresentLocation()
					.getAddress1());
			// setSelection_atveditext(atvPickUpAddress);

			etPickUpPinCode.setText(pbParcelable.getPresentLocation()
					.getPinCode());

			// setSelection_editext(etPickUpPinCode);
			/*
			 * etPickUpPinCode.setFocusable(false);
			 * atvPickUpAddress.setFocusable(false);
			 */

			break;

		case R.id.tv_pick_up_favorites:
			hideKeybord(v);
			setTopDrawbleAndText(tvPickUpLocation, R.drawable.trackgray,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvPickUpOnMap, R.drawable.onmap,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvPickUpRecent, R.drawable.recent,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvPickUpMyPlaces,
					R.drawable.favorites2selected, R.color.textview_selected);
			// pbParcelable.setDefaultPickUpLocation(new LocationParcelable());

			/*
			 * atvPickUpAddress.setFocusable(false);
			 * etPickUpPinCode.setFocusable(false);
			 */

			if (mhandler != null && !programmaticPickUpFavClick) {
				programmaticPickUpFavClick = false;
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("PFavourites", "");
				msg.obj = bundle;
				msg.setData(bundle);
				mhandler.sendMessage(msg);
			} else {
				if (pbParcelable.getFavouritePickUpLocation().getAddress1()
						.equals("null"))
					atvPickUpAddress.setText("");
				else
					atvPickUpAddress.setText(pbParcelable
							.getFavouritePickUpLocation().getAddress1());
				if (pbParcelable.getFavouritePickUpLocation().getPinCode()
						.equals("null"))
					etPickUpPinCode.setText("");
				else
					etPickUpPinCode.setText(pbParcelable
							.getFavouritePickUpLocation().getPinCode());
			}
			if (programmaticPickUpFavClick)
				programmaticPickUpFavClick = false;
			break;

		case R.id.tv_pick_up_top_right:
			hideKeybord(v);

			if (atvPickUpAddress.getText().toString().trim().equals("")) {
				Toast.makeText(context, "Please enter address",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (isFavorite(atvPickUpAddress.getText().toString().trim()
					.toLowerCase())) {
				Toast.makeText(context,
						"This location is already added as your favourite",
						Toast.LENGTH_SHORT).show();
				return;
			}

			selectedValues = getSelectedLocationValues(pbParcelable);
			addFavourite(tvPickUpAdd, selectedValues[0]);

			break;

		case R.id.tv_drop_off_top_right:
			hideKeybord(v);

			if (atvDropOffAddress.getText().toString()
					.equalsIgnoreCase("as directed")) {
				Toast.makeText(context,
						"As directed cannot be added as favourite",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (atvDropOffAddress.getText().toString().trim().equals("")) {
				Toast.makeText(context, "Please enter address",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (isFavorite(atvDropOffAddress.getText().toString().trim()
					.toLowerCase())) {
				Toast.makeText(context,
						"This location is already added as your favourite",
						Toast.LENGTH_SHORT).show();
				return;
			}
			selectedValues = getSelectedLocationValues(pbParcelable);
			addFavourite(tvDropOffAdd, selectedValues[1]);

			break;

		case R.id.tv_pick_up_recent:
			hideKeybord(v);
			setTopDrawbleAndText(tvPickUpLocation, R.drawable.trackgray,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvPickUpOnMap, R.drawable.onmap,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvPickUpRecent, R.drawable.recent2,
					R.color.textview_selected);
			setTopDrawbleAndText(tvPickUpMyPlaces, R.drawable.favorites2,
					R.color.textcolor_grey);
			/*
			 * atvPickUpAddress.setFocusable(false);
			 * etPickUpPinCode.setFocusable(false);
			 */
			// pbParcelable.setDefaultPickUpLocation(new LocationParcelable());

			if (mhandler != null && !programmaticPickUpRecentClick) {
				programmaticPickUpRecentClick = false;
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("PRecent", "");
				msg.obj = bundle;
				msg.setData(bundle);
				mhandler.sendMessage(msg);
			} else {
				if (pbParcelable.getRecentPickUpLocation().getAddress1()
						.matches("null"))
					atvPickUpAddress.setText("");
				else
					atvPickUpAddress.setText(pbParcelable
							.getRecentPickUpLocation().getAddress1());

				if (pbParcelable.getRecentPickUpLocation().getPinCode()
						.matches("null"))

					etPickUpPinCode.setText("");
				else
					etPickUpPinCode.setText(pbParcelable
							.getRecentPickUpLocation().getPinCode());
			}

			if (programmaticPickUpRecentClick)
				programmaticPickUpRecentClick = false;

			break;
		case R.id.tv_pick_up_onmap:
			hideKeybord(v);
			// pbParcelable.setePickUpSelected(EnumLocations.MAPLOCATION);
			setTopDrawbleAndText(tvPickUpOnMap, R.drawable.onmapselected,
					R.color.textview_selected);
			setTopDrawbleAndText(tvPickUpLocation, R.drawable.trackgray,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvPickUpRecent, R.drawable.recent,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvPickUpMyPlaces, R.drawable.favorites2,
					R.color.textcolor_grey);
			// pbParcelable.setDefaultPickUpLocation(new LocationParcelable());
			Log.e("Log values", pbParcelable.getMapPickUpLocation()
					.getAddress1().toString());

			if (mhandler != null && !programmaticPickUpOnMapClick) {
				programmaticPickUpOnMapClick = false;
				if (pbParcelable.getePickUpSelected() == EnumLocations.DEFAULT) {
					if (pbParcelable.getDefaultPickUpLocation().getLatitude() == 0
							&& pbParcelable.getDefaultDropOffLocation()
									.getLongitude() == 0) {

						isOnMap = true;
						GetLatLongTask task = new GetLatLongTask(context,
								pbParcelable.getDefaultPickUpLocation()
										.getAddress1(), pbParcelable
										.getDefaultPickUpLocation()
										.getPinCode(), pickUpLatLngHandler);
						task.execute("");

					} else {
						pbParcelable.getMapPickUpLocation().setAddress1(
								pbParcelable.getDefaultPickUpLocation()
										.getAddress1());
						pbParcelable.getMapPickUpLocation().setPinCode(
								pbParcelable.getDefaultPickUpLocation()
										.getPinCode());
						pbParcelable.getMapPickUpLocation().setLatitude(
								pbParcelable.getDefaultPickUpLocation()
										.getLatitude());
						pbParcelable.getMapPickUpLocation().setLongitude(
								pbParcelable.getDefaultPickUpLocation()
										.getLongitude());
						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("pickUpOnMap", "");
						msg.obj = bundle;
						msg.setData(bundle);
						mhandler.sendMessage(msg);

					}
				} 

				else {
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("pickUpOnMap", "");
					msg.obj = bundle;
					msg.setData(bundle);
					mhandler.sendMessage(msg);
				}

			} else {
				atvPickUpAddress.setText(pbParcelable.getMapPickUpLocation()
						.getAddress1());
				etPickUpPinCode.setText(pbParcelable.getMapPickUpLocation()
						.getPinCode());
			}
			if (programmaticPickUpOnMapClick)
				programmaticPickUpOnMapClick = false;

			// atvPickUpAddress.setFocusableInTouchMode(true);
			// etPickUpPinCode.setFocusableInTouchMode(true);

			// atvPickUpAddress.setText(pbParcelable.getMapPickUpLocation()
			// .getAddress1());
			// setSelection_atveditext(atvPickUpAddress);

			// etPickUpPinCode.setText(pbParcelable.getMapPickUpLocation()
			// .getPinCode());
			// setSelection_editext(etPickUpPinCode);
			// tvPickUpOnMap.setFocusableInTouchMode(true);
			// atvPickUpAddress.requestFocus();
			// InputMethodManager imm = (InputMethodManager) context
			// .getSystemService(Context.INPUT_METHOD_SERVICE);
			// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

			break;

		case R.id.tv_drop_off_location:
			hideKeybord(v);
			pbParcelable.seteDropOffSelected(EnumLocations.PRESENTLOCATION);
			setTopDrawbleAndText(tvDropOffOnMap, R.drawable.onmap,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvDropOffLocation, R.drawable.track,
					R.color.textview_selected);
			setTopDrawbleAndText(tvDropOffRecent, R.drawable.recent,
					R.color.textcolor_grey);

			/*
			 * atvDropOffAddress.setFocusable(false);
			 * etDropOffPinCode.setFocusable(false);
			 */

			atvDropOffAddress.setText(pbParcelable.getPresentLocation()
					.getAddress1());
			etDropOffPinCode.setText(pbParcelable.getPresentLocation()
					.getPinCode());
			setTopDrawbleAndText(tvDropOffMyPlaces, R.drawable.favorites2,
					R.color.textcolor_grey);
			pbParcelable.setDefaultDropOffLocation(new LocationParcelable());

			break;

		case R.id.tv_drop_off_favorites:
			hideKeybord(v);
			setTopDrawbleAndText(tvDropOffLocation, R.drawable.trackgray,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvDropOffOnMap, R.drawable.onmap,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvDropOffRecent, R.drawable.recent,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvDropOffMyPlaces,
					R.drawable.favorites2selected, R.color.textview_selected);
			// pbParcelable.setDefaultDropOffLocation(new LocationParcelable());
			/*
			 * atvDropOffAddress.setFocusable(false);
			 * etDropOffPinCode.setFocusable(false);
			 */

			if (mhandler != null && !programmaticDropOffFavClick) {
				programmaticDropOffFavClick = false;
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("DFavourites", "");
				msg.obj = bundle;
				msg.setData(bundle);
				mhandler.sendMessage(msg);
			} else {
				if (pbParcelable.getFavouriteDropOffLocation().getAddress1()
						.equals("null"))
					atvDropOffAddress.setText("");
				else
					atvDropOffAddress.setText(pbParcelable
							.getFavouriteDropOffLocation().getAddress1());
				if (pbParcelable.getFavouriteDropOffLocation().getPinCode()
						.equals("null"))
					etDropOffPinCode.setText("");
				else
					etDropOffPinCode.setText(pbParcelable
							.getFavouriteDropOffLocation().getPinCode());
			}
			if (programmaticDropOffFavClick)
				programmaticDropOffFavClick = false;

			break;

		case R.id.tv_drop_off_recent:
			hideKeybord(v);
			setTopDrawbleAndText(tvDropOffOnMap, R.drawable.onmap,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvDropOffLocation, R.drawable.trackgray,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvDropOffRecent, R.drawable.recent2,
					R.color.textview_selected);
			setTopDrawbleAndText(tvDropOffMyPlaces, R.drawable.favorites2,
					R.color.textcolor_grey);

			// pbParcelable.setDefaultDropOffLocation(new LocationParcelable());
			/*
			 * atvDropOffAddress.setFocusable(false);
			 * etDropOffPinCode.setFocusable(false);
			 */

			if (mhandler != null && !programmaticDropOffRecentClick) {
				programmaticDropOffRecentClick = false;
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putString("DRecent", "");
				msg.obj = bundle;
				msg.setData(bundle);
				mhandler.sendMessage(msg);
			} else {
				if (pbParcelable.getRecentDropOffLocation().getAddress1()
						.matches("null"))
					atvDropOffAddress.setText("");
				else
					atvDropOffAddress.setText(pbParcelable
							.getRecentDropOffLocation().getAddress1());

				if (pbParcelable.getRecentDropOffLocation().getPinCode()
						.matches("null"))

					etDropOffPinCode.setText("");
				else
					etDropOffPinCode.setText(pbParcelable
							.getRecentDropOffLocation().getPinCode());
			}
			if (programmaticDropOffRecentClick)
				programmaticDropOffRecentClick = false;

			break;
		case R.id.tv_drop_off_onmap:
			hideKeybord(v);
			// pbParcelable.seteDropOffSelected(EnumLocations.MAPLOCATION);
			setTopDrawbleAndText(tvDropOffOnMap, R.drawable.onmapselected,
					R.color.textview_selected);
			setTopDrawbleAndText(tvDropOffLocation, R.drawable.trackgray,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvDropOffRecent, R.drawable.recent,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvDropOffMyPlaces, R.drawable.favorites2,
					R.color.textcolor_grey);
			// pbParcelable.setDefaultDropOffLocation(new LocationParcelable());
			/*
			 * atvDropOffAddress.setFocusableInTouchMode(true);
			 * etDropOffPinCode.setFocusableInTouchMode(true);
			 */
			if (mhandler != null && !programmaticDropOffOnMapClick) {
				programmaticDropOffOnMapClick = false;
				if (pbParcelable.geteDropOffSelected() == EnumLocations.DEFAULT
						&& !pbParcelable.getDefaultDropOffLocation()
								.getAddress1().equalsIgnoreCase("as directed")
						&& !pbParcelable.getDefaultDropOffLocation()
								.getAddress1().equalsIgnoreCase("")) {
					if (pbParcelable.getDefaultDropOffLocation().getLatitude() == 0
							&& pbParcelable.getDefaultDropOffLocation()
									.getLongitude() == 0) {

						isOnMap = true;
						GetLatLongTask task = new GetLatLongTask(context,
								pbParcelable.getDefaultDropOffLocation()
										.getAddress1(), pbParcelable
										.getDefaultPickUpLocation()
										.getPinCode(), dropOffLatLngHandler);
						task.execute("");

					} else {
						pbParcelable.getMapDropOffLocation().setAddress1(
								pbParcelable.getDefaultDropOffLocation()
										.getAddress1());
						pbParcelable.getMapDropOffLocation().setPinCode(
								pbParcelable.getDefaultDropOffLocation()
										.getPinCode());
						pbParcelable.getMapDropOffLocation().setLatitude(
								pbParcelable.getDefaultDropOffLocation()
										.getLatitude());
						pbParcelable.getMapDropOffLocation().setLongitude(
								pbParcelable.getDefaultDropOffLocation()
										.getLongitude());
						Message msg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString("dropOffOnMap", "");
						msg.obj = bundle;
						msg.setData(bundle);
						mhandler.sendMessage(msg);

					}
				} else {
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("dropOffOnMap", "");
					msg.obj = bundle;
					msg.setData(bundle);
					mhandler.sendMessage(msg);
				}
			} else {
				atvDropOffAddress.setText(pbParcelable.getMapDropOffLocation()
						.getAddress1());
				etDropOffPinCode.setText(pbParcelable.getMapDropOffLocation()
						.getPinCode());
			}
			if (programmaticDropOffOnMapClick)
				programmaticDropOffOnMapClick = false;

			/*
			 * atvDropOffAddress.setText(pbParcelable.getMapDropOffLocation()
			 * .getAddress1()); setSelection_atveditext(atvDropOffAddress);
			 * etDropOffPinCode.setText(pbParcelable.getMapDropOffLocation()
			 * .getPinCode()); setSelection_editext(etDropOffPinCode);
			 * tvDropOffOnMap.setFocusableInTouchMode(true);
			 */

			break;

		case R.id.tv_ao_wheelchair_user:
			// hideKeybord(v);
			if (((TextView) v).getCompoundDrawables()[1].getConstantState()
					.equals((context.getResources()
							.getDrawable(R.drawable.wheelchair_user))
							.getConstantState())) {
				pbParcelable.setWheelChairSelected(true);
				setTopDrawbleAndText(((TextView) v),
						R.drawable.wheelchair_userselected,
						R.color.textview_selected);

			} else {
				pbParcelable.setWheelChairSelected(false);
				setTopDrawbleAndText(((TextView) v),
						R.drawable.wheelchair_user, R.color.textcolor_grey);
			}
			break;

		case R.id.tv_ao_hearing_impaired:
			// hideKeybord(v);
			if (((TextView) v).getCompoundDrawables()[1].getConstantState()
					.equals((context.getResources()
							.getDrawable(R.drawable.hearing_impaired))
							.getConstantState())) {
				pbParcelable.setHearingImpairedSelected(true);
				setTopDrawbleAndText(((TextView) v),
						R.drawable.hearing_impairedselected,
						R.color.textview_selected);

			} else {
				// hideKeybord(v);
				pbParcelable.setHearingImpairedSelected(false);
				setTopDrawbleAndText(((TextView) v),
						R.drawable.hearing_impaired, R.color.textcolor_grey);

			}
			break;

		case R.id.tv_w_asap:
			// hideKeybord(v);

			pbParcelable.seteWhen(EnumWhen.ASAP);

			setTopDrawbleAndText(tvWhenAsap, R.drawable.asapselected,
					R.color.textview_selected);
			setTopDrawbleAndText(tvWhenPreBook, R.drawable.prebook,
					R.color.textcolor_grey);
			tvWhenDate.setVisibility(View.GONE);
			tvWhenTime.setVisibility(View.GONE);

			break;

		case R.id.tv_w_prebook:
			// hideKeybord(v);
			pbParcelable.seteWhen(EnumWhen.PREBOOK);
			setTopDrawbleAndText(tvWhenAsap, R.drawable.asap,
					R.color.textcolor_grey);
			setTopDrawbleAndText(tvWhenPreBook, R.drawable.prebookselected,
					R.color.textview_selected);
			tvWhenDate.setVisibility(View.VISIBLE);
			tvWhenTime.setVisibility(View.VISIBLE);
			break;

		}

		if (tvDropOffOnMap != null) {
			tvDropOffOnMap
					.setOnFocusChangeListener(new OnFocusChangeListener() {

						@Override
						public void onFocusChange(View v, boolean hasFocus) {

							InputMethodManager imm = (InputMethodManager) context
									.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
									0);
						}
					});
		}

	}

	public void addFavourite(TextView tvFavourite,
			LocationParcelable selectedLocation) {
		if (selectedLocation.getLatitude() != 0
				&& selectedLocation.getLongitude() != 0) {
			AddFavourites favourites = new AddFavourites(context,
					selectedLocation.getAddress1(),
					selectedLocation.getPinCode(),
					selectedLocation.getLatitude(),
					selectedLocation.getLongitude(), tvFavourite);
			favourites.execute(Constant.passengerURL
					+ "ws/v2/passenger/favourite/?accessToken="
					+ shared_pref.getString("AccessToken", ""));
		} else {
			isFav = true;
			GetLatLongTask task = new GetLatLongTask(context,
					selectedLocation.getAddress1(),
					selectedLocation.getPinCode(), pickUpLatLngHandler);
			task.execute("");
		}
	}

	private void toggleVisiblityAndIcons(View v, int viewRightDrawableId) {
		// hideKeybord(v);
		if (v.getId() == R.id.tv_ao_header) {
			RelativeLayout rlAo = (RelativeLayout) (view
					.findViewById(R.id.rl_ao));

			rlAo.setVisibility(rlAo.isShown() ? View.GONE : View.VISIBLE);
		}

		else if (v.getId() == R.id.tv_comments) {
			EditText etComments = (EditText) (view
					.findViewById(R.id.et_comments));
			etComments.setVisibility(etComments.isShown() ? View.GONE
					: View.VISIBLE);
			etComments.requestFocus();
			scrollView.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					scrollView.fullScroll(View.FOCUS_DOWN);
				}
			});
		}

		if (((TextView) v).getCompoundDrawables()[2].getConstantState().equals(
				(context.getResources().getDrawable(R.drawable.addcomment))
						.getConstantState()))
			((TextView) v).setCompoundDrawablesWithIntrinsicBounds(
					viewRightDrawableId, 0, R.drawable.unavailable, 0);
		else
			((TextView) v).setCompoundDrawablesWithIntrinsicBounds(
					viewRightDrawableId, 0, R.drawable.addcomment, 0);
	}

	// TO

	// Change the layout style (top drawable and text color) of Location, onMap,
	// and MyPlaces button on selection

	private void setTopDrawbleAndText(TextView textView, int topDrawableId,
			int textColorId) {
		textView.setCompoundDrawablesWithIntrinsicBounds(0, topDrawableId, 0, 0);
		textView.setTextColor(context.getResources().getColor(textColorId));
	}

	public void setSelection_editext(EditText edittext) {
		int textlength = edittext.length();
		edittext.setSelection(textlength, textlength);
	}

	public void setSelection_atveditext(AutoCompleteTextView atvedittext) {
		int textlength = atvedittext.length();
		atvedittext.setSelection(textlength, textlength);
	}

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	public void setDropOffSelectionToDefault() {
		setTopDrawbleAndText(tvDropOffOnMap, R.drawable.onmap,
				R.color.textcolor_grey);
		setTopDrawbleAndText(tvDropOffLocation, R.drawable.trackgray,
				R.color.textcolor_grey);
		setTopDrawbleAndText(tvDropOffRecent, R.drawable.recent,
				R.color.textcolor_grey);
		setTopDrawbleAndText(tvDropOffMyPlaces, R.drawable.favorites2,
				R.color.textcolor_grey);
		pbParcelable.seteDropOffSelected(EnumLocations.DEFAULT);

	}

	public void setPickUpSelectionToDefault() {
		setTopDrawbleAndText(tvPickUpOnMap, R.drawable.onmap,
				R.color.textcolor_grey);
		setTopDrawbleAndText(tvPickUpLocation, R.drawable.trackgray,
				R.color.textcolor_grey);
		setTopDrawbleAndText(tvPickUpRecent, R.drawable.recent,
				R.color.textcolor_grey);
		setTopDrawbleAndText(tvPickUpMyPlaces, R.drawable.favorites2,
				R.color.textcolor_grey);

		pbParcelable.setePickUpSelected(EnumLocations.DEFAULT);

	}

	public LocationParcelable[] getSelectedLocationValues(
			PreBookParcelable pbParcelable) {
		LocationParcelable[] selectedLocation = new LocationParcelable[2];
		selectedLocation[0] = new LocationParcelable();
		selectedLocation[1] = new LocationParcelable();
		if (pbParcelable.getePickUpSelected() == EnumLocations.MAPLOCATION) {
			selectedLocation[0].setAddress1(pbParcelable.getMapPickUpLocation()
					.getAddress1());
			selectedLocation[0].setAddress1(pbParcelable.getMapPickUpLocation()
					.getAddress1());
			selectedLocation[0].setPinCode(pbParcelable.getMapPickUpLocation()
					.getPinCode());
			selectedLocation[0].setLatitude(pbParcelable.getMapPickUpLocation()
					.getLatitude());
			selectedLocation[0].setLongitude(pbParcelable
					.getMapPickUpLocation().getLongitude());

		} else if (pbParcelable.getePickUpSelected() == EnumLocations.PRESENTLOCATION) {

			selectedLocation[0].setAddress1(pbParcelable.getPresentLocation()
					.getAddress1());
			selectedLocation[0].setPinCode(pbParcelable.getPresentLocation()
					.getPinCode());
			selectedLocation[0].setLatitude(pbParcelable.getPresentLocation()
					.getLatitude());
			selectedLocation[0].setLongitude(pbParcelable.getPresentLocation()
					.getLongitude());

		} else if (pbParcelable.getePickUpSelected() == EnumLocations.RECENT) {
			selectedLocation[0].setAddress1(pbParcelable
					.getRecentPickUpLocation().getAddress1());
			selectedLocation[0].setPinCode(pbParcelable
					.getRecentPickUpLocation().getPinCode());
			selectedLocation[0].setLatitude(pbParcelable
					.getRecentPickUpLocation().getLatitude());
			selectedLocation[0].setLongitude(pbParcelable
					.getRecentPickUpLocation().getLongitude());

		} else if (pbParcelable.getePickUpSelected() == EnumLocations.FAVOURITE) {
			selectedLocation[0].setAddress1(pbParcelable
					.getFavouritePickUpLocation().getAddress1());
			selectedLocation[0].setPinCode(pbParcelable
					.getFavouritePickUpLocation().getPinCode());
			selectedLocation[0].setLatitude(pbParcelable
					.getFavouritePickUpLocation().getLatitude());
			selectedLocation[0].setLongitude(pbParcelable
					.getFavouritePickUpLocation().getLongitude());

		} else if (pbParcelable.getePickUpSelected() == EnumLocations.DEFAULT) {
			selectedLocation[0].setAddress1(pbParcelable
					.getDefaultPickUpLocation().getAddress1());
			selectedLocation[0].setPinCode(pbParcelable
					.getDefaultPickUpLocation().getPinCode());
			selectedLocation[0].setLatitude(pbParcelable
					.getDefaultPickUpLocation().getLatitude());
			selectedLocation[0].setLongitude(pbParcelable
					.getDefaultPickUpLocation().getLongitude());

		}

		if (pbParcelable.geteDropOffSelected() == EnumLocations.MAPLOCATION) {

			selectedLocation[1].setAddress1(pbParcelable
					.getMapDropOffLocation().getAddress1());
			selectedLocation[1].setPinCode(pbParcelable.getMapDropOffLocation()
					.getPinCode());
			selectedLocation[1].setLatitude(pbParcelable
					.getMapDropOffLocation().getLatitude());
			selectedLocation[1].setLongitude(pbParcelable
					.getMapDropOffLocation().getLongitude());

		} else if (pbParcelable.geteDropOffSelected() == EnumLocations.PRESENTLOCATION) {

			selectedLocation[1].setAddress1(pbParcelable.getPresentLocation()
					.getAddress1());
			selectedLocation[1].setPinCode(pbParcelable.getPresentLocation()
					.getPinCode());
			selectedLocation[1].setLatitude(pbParcelable.getPresentLocation()
					.getLatitude());
			selectedLocation[1].setLongitude(pbParcelable.getPresentLocation()
					.getLongitude());

		} else if (pbParcelable.geteDropOffSelected() == EnumLocations.RECENT) {

			selectedLocation[1].setAddress1(pbParcelable
					.getRecentDropOffLocation().getAddress1());
			selectedLocation[1].setPinCode(pbParcelable
					.getRecentDropOffLocation().getPinCode());
			selectedLocation[1].setLatitude(pbParcelable
					.getRecentDropOffLocation().getLatitude());
			selectedLocation[1].setLongitude(pbParcelable
					.getRecentDropOffLocation().getLongitude());

		} else if (pbParcelable.geteDropOffSelected() == EnumLocations.FAVOURITE) {

			selectedLocation[1].setAddress1(pbParcelable
					.getFavouriteDropOffLocation().getAddress1());
			selectedLocation[1].setPinCode(pbParcelable
					.getFavouriteDropOffLocation().getPinCode());
			selectedLocation[1].setLatitude(pbParcelable
					.getFavouriteDropOffLocation().getLatitude());
			selectedLocation[1].setLongitude(pbParcelable
					.getFavouriteDropOffLocation().getLongitude());
		}

		else if (pbParcelable.geteDropOffSelected() == EnumLocations.DEFAULT) {

			selectedLocation[1].setAddress1(pbParcelable
					.getDefaultDropOffLocation().getAddress1());
			selectedLocation[1].setPinCode(pbParcelable
					.getDefaultDropOffLocation().getPinCode());
			selectedLocation[1].setLatitude(pbParcelable
					.getDefaultDropOffLocation().getLatitude());
			selectedLocation[1].setLongitude(pbParcelable
					.getDefaultDropOffLocation().getLongitude());
		}

		return selectedLocation;
	}

	/*
	 * public void addFavourite(Context mContext, String address, String
	 * pincode, Double latitude, Double longitude) { // TODO Auto-generated
	 * method stub if (eClickedFav == EnumClickedFav.PICKUP) { AddFavourites
	 * favourites = new AddFavourites(mContext, address, pincode, latitude,
	 * longitude, atvPickUpAddress); favourites.execute(Constant.passengerURL +
	 * "ws/v2/passenger/favourite/?accessToken=" +
	 * shared_pref.getString("AccessToken", "")); } else if (eClickedFav ==
	 * EnumClickedFav.PICKUP) { AddFavourites favourites = new
	 * AddFavourites(mContext, address, pincode, latitude, longitude,
	 * atvDropOffAddress); favourites.execute(Constant.passengerURL +
	 * "ws/v2/passenger/favourite/?accessToken=" +
	 * shared_pref.getString("AccessToken", "")); } }
	 */

	public void updateParcelableData(PreBookParcelable pbParcelable) {
		this.pbParcelable = pbParcelable;
	}

	private boolean isFavorite(String address) {
		// TODO Auto-generated method stub
		Log.e("Arr size", "" + Constant.arr_favourites.size());
		if (Constant.arr_favourites.size() > 0) {

			for (int i = 0; i < Constant.arr_favourites.size(); i++) {

				if (address
						.toLowerCase()
						.replace(",", "")
						.equalsIgnoreCase(
								Constant.arr_favourites.get(i).toLowerCase()
										.replace(",", ""))) {

					return true;

				}
			}

		}
		return false;

	}

}
