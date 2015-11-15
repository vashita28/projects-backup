package com.android.cabapp.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.adapter.CustomGridAdapter;
import com.android.cabapp.datastruct.json.SectorList;
import com.android.cabapp.model.Registration;
import com.android.cabapp.model.SectorForCity;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;

public class SignUp_DriverDetails_Activity extends RootActivity {

	private static final String TAG = SignUp_DriverDetails_Activity.class.getSimpleName();

	EditText etBadgeNumber;// , etSuburbanAreaData;
	TextView textNext, textHiddenNext, textDay, textMonth, textYear, tvDriverDetailsNote, txtEdit, textBadgecolorGreen,
			tvSuburbanArea, textBadgecolorYellow, textBadgeNumber, textDriverBadgeExpiry, textYes, textNo;
	LinearLayout llDriverBadgeExpiryDate, llBadgeColor;
	ImageView ivRegister3;
	RelativeLayout rlTopPoint, rlSuburban;
	ArrayList<String> documentList;
	GridView gridSuburbanAreas;

	public static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	static final int DATE_DIALOG_ID = 999;
	public static final int TAKE_PHOTO_REQUEST = 0;
	public static final int PICK_PHOTO_REQUEST = 1;

	Calendar calendar;
	private int year, currentYear;
	private int month, currentMonth;
	private int day, currentDay;
	String dateSelected = "";
	String oldDay = "", oldMonth = "", oldYear = "";
	static String szBadgeColor = Constants.BADGE_COLOUR_GREEN;

	Bundle driverDetailsBundle;
	boolean isComingFromEditMyAccount;
	private ProgressDialog dialogDriverDetails;

	View activityRootView;

	ArrayList<String> listNoOfSectors = new ArrayList<String>();
	List<String> listSelectedBadges = new ArrayList<String>();
	CustomGridAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_driverdetails);
		mContext = this;
		driverDetailsBundle = getIntent().getExtras();
		documentList = new ArrayList<String>();

		calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
		currentYear = calendar.get(Calendar.YEAR);
		currentMonth = calendar.get(Calendar.MONTH);
		currentDay = calendar.get(Calendar.DAY_OF_MONTH);

		activityRootView = findViewById(R.id.relMain);
		etBadgeNumber = (EditText) findViewById(R.id.etBadgeNumber);
		textDay = (TextView) findViewById(R.id.tvDay);
		textMonth = (TextView) findViewById(R.id.tvMonth);
		textYear = (TextView) findViewById(R.id.tvYear);
		rlTopPoint = (RelativeLayout) findViewById(R.id.rlTopPoint);
		llDriverBadgeExpiryDate = (LinearLayout) findViewById(R.id.llDriverBadgeExpiryDate);
		tvDriverDetailsNote = (TextView) findViewById(R.id.tvDriverDetails);
		ivRegister3 = (ImageView) findViewById(R.id.ivRegister3);
		llBadgeColor = (LinearLayout) findViewById(R.id.llBadgeColor);
		textBadgecolorGreen = (TextView) findViewById(R.id.textBadgecolorGreen);
		textBadgecolorYellow = (TextView) findViewById(R.id.textBadgecolorYellow);
		tvSuburbanArea = (TextView) findViewById(R.id.tvSuburbanArea);
		rlSuburban = (RelativeLayout) findViewById(R.id.rlSuburban);
		// etSuburbanAreaData = (EditText)
		// findViewById(R.id.etSuburbanAreaData);
		txtEdit = (TextView) findViewById(R.id.tvEdit);
		txtEdit.setVisibility(View.GONE);
		textBadgeNumber = (TextView) findViewById(R.id.tvBadgeNumber);
		textDriverBadgeExpiry = (TextView) findViewById(R.id.tvDriverBadgeExpiry);
		textNext = (TextView) findViewById(R.id.tvNext);
		textHiddenNext = (TextView) findViewById(R.id.tvHiddenNext);
		gridSuburbanAreas = (GridView) findViewById(R.id.gridSuburbanAreas);
		textNext.setOnClickListener(new TextViewOnCLickListener());
		textHiddenNext.setOnClickListener(new TextViewOnCLickListener());

		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect r = new Rect();
				// r will be populated with the coordinates of your view
				// that area still visible.
				activityRootView.getWindowVisibleDisplayFrame(r);
				int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
				if (heightDiff > 100) {
					textHiddenNext.setVisibility(View.VISIBLE);
					textNext.setVisibility(View.GONE);
				} else {
					textHiddenNext.setVisibility(View.GONE);
					textNext.setVisibility(View.VISIBLE);
				}
			}
		});

		// Set current date +1 in as expiry
		// textDay.setText(new StringBuilder().append(currentDay + 1));
		// String sMonth = MonthName(month);
		// textMonth.setText(sMonth);
		// textYear.setText(new StringBuilder().append(currentYear));

		// If coming from edit details and city selected is not London
		if (AppValues.driverDetails != null) {
			if (!AppValues.driverDetails.getCityId().equals("1")) {
				Util.setCitySelectedLondon(mContext, false);
				llBadgeColor.setVisibility(View.GONE);
				rlSuburban.setVisibility(View.GONE);
			}
		}

		if (Util.mContext != null && NetworkUtil.isNetworkOn(Util.mContext)) {
			SectorTask sectorTask = new SectorTask();
			sectorTask.execute();
		} else if (Util.mContext != null) {
			Util.showToastMessage(Util.mContext, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
		}

		// if city is london: storing badge color
		if (AppValues.driverDetails != null && AppValues.driverDetails.getBadgeColour() != null) {
			szBadgeColor = AppValues.driverDetails.getBadgeColour();
		}

		if (driverDetailsBundle != null && driverDetailsBundle.containsKey(Constants.WORKING_CITY)) {
			String szWorking = driverDetailsBundle.getString(Constants.WORKING_CITY);

			if (szWorking.equals(Constants.WORKING_CITY_LONDON)) {
				Util.setCitySelectedLondon(mContext, true);
				llBadgeColor.setVisibility(View.VISIBLE);
				if (szBadgeColor.equals(Constants.BADGE_COLOUR_YELLOW))
					rlSuburban.setVisibility(View.VISIBLE);
				else
					rlSuburban.setVisibility(View.GONE);
			} else {
				Util.setCitySelectedLondon(mContext, false);
				llBadgeColor.setVisibility(View.GONE);
				rlSuburban.setVisibility(View.GONE);
			}
		}

		if (driverDetailsBundle != null) {
			if (driverDetailsBundle.containsKey(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS)) {
				isComingFromEditMyAccount = driverDetailsBundle.getBoolean(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS);
				if (isComingFromEditMyAccount) {
					if (Util.getCitySelectedLondon(mContext)) {
						llBadgeColor.setVisibility(View.VISIBLE);
					}

					rlTopPoint.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
					rlTopPoint.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
					tvDriverDetailsNote.setText(getResources().getString(R.string.driver_details));
					tvDriverDetailsNote.setGravity(Gravity.CENTER);
					ivRegister3.setVisibility(View.GONE);
					textBadgeNumber.setText("* BADGE NUMBER");
					textDriverBadgeExpiry.setText("* DRIVER BADGE EXPIRY");

					if (AppValues.driverDetails != null) {

						if (Util.getCitySelectedLondon(mContext)) {
							if (AppValues.driverDetails.getBadgeColour() != null) {
								llBadgeColor.setVisibility(View.VISIBLE);

								if (Constants.isDebug)
									Log.e(TAG, "Badge colour-From driverDetails: " + AppValues.driverDetails.getBadgeColour()
											+ " szBadgeColor: " + szBadgeColor);

								if (AppValues.driverDetails.getBadgeColour().equals(Constants.BADGE_COLOUR_YELLOW)) {
									rlSuburban.setVisibility(View.VISIBLE);
									tvSuburbanArea.setText("* SUBURBAN AREAS");
									// etSuburbanAreaData
									// .setText(AppValues.driverDetails
									// .getSector());
									// etSuburbanAreaData
									// .setSelection(etSuburbanAreaData
									// .getText().length());

									setBadgeColour(textBadgecolorYellow, textBadgecolorGreen,
											getResources().getColor(R.color.badgecolor_yellow),
											getResources().getColor(R.color.badgecolor_unselected), Constants.BADGE_COLOUR_YELLOW);
									rlSuburban.setVisibility(View.VISIBLE);
								} else {
									setBadgeColour(textBadgecolorGreen, textBadgecolorYellow,
											getResources().getColor(R.color.badgecolor_green),
											getResources().getColor(R.color.badgecolor_unselected), Constants.BADGE_COLOUR_GREEN);
									rlSuburban.setVisibility(View.GONE);
								}
							}
						}
						etBadgeNumber.setText(AppValues.driverDetails.getBadgeNumber());

						etBadgeNumber.setSelection(etBadgeNumber.getText().length());

						String date = AppValues.driverDetails.getBadgeExpiration();
						if (Constants.isDebug)
							Log.e(TAG, "date::  " + date);
						oldMonth = date.substring(date.indexOf("/") + 1, date.lastIndexOf("/"));
						oldDay = date.substring(0, date.indexOf("/"));
						oldYear = date.substring(date.lastIndexOf("/") + 1);

						String sMonthName = MonthName(Integer.valueOf(oldMonth) - 1);

						textMonth.setText(sMonthName);
						textDay.setText(oldDay);
						textYear.setText(oldYear);
						year = Integer.valueOf(oldYear);
						month = Integer.parseInt(oldMonth) - 1;
						// - 1;//
						// nMonth//oldMonth
						day = Integer.valueOf(oldDay);

					}
				}
			}
		}

		if (AppValues.mapRegistrationData.containsKey(Constants.BADGE_NUMBER)) {

			if (Util.getCitySelectedLondon(mContext)) {
				llBadgeColor.setVisibility(View.VISIBLE);
				if (AppValues.mapRegistrationData.containsKey(Constants.BADGE_COLOUR)) {
					if (AppValues.mapRegistrationData.get(Constants.BADGE_COLOUR).equals(Constants.BADGE_COLOUR_YELLOW)) {

						setBadgeColour(textBadgecolorYellow, textBadgecolorGreen,
								getResources().getColor(R.color.badgecolor_yellow),
								getResources().getColor(R.color.badgecolor_unselected), Constants.BADGE_COLOUR_YELLOW);
						rlSuburban.setVisibility(View.VISIBLE);
					} else {

						setBadgeColour(textBadgecolorGreen, textBadgecolorYellow,
								getResources().getColor(R.color.badgecolor_green),
								getResources().getColor(R.color.badgecolor_unselected), Constants.BADGE_COLOUR_GREEN);
						rlSuburban.setVisibility(View.GONE);
					}
				}
			}

			if (AppValues.mapRegistrationData.containsKey(Constants.BADGE_COLOUR)
					&& AppValues.mapRegistrationData.get(Constants.BADGE_COLOUR).equals(Constants.BADGE_COLOUR_YELLOW)
					&& AppValues.mapRegistrationData.containsKey(Constants.SECTOR)) {
				// etSuburbanAreaData.setText(AppValues.mapRegistrationData
				// .get(Constants.SECTOR));
				// etSuburbanAreaData.setSelection(etSuburbanAreaData.getText()
				// .length());
			}

			etBadgeNumber.setText(AppValues.mapRegistrationData.get(Constants.BADGE_NUMBER));

			etBadgeNumber.setSelection(etBadgeNumber.getText().length());

			String date = AppValues.mapRegistrationData.get(Constants.DRIVER_BADGE_EXPIRY);
			if (Constants.isDebug)
				Log.e(TAG, "date::  " + date);
			oldMonth = date.substring(date.indexOf("/") + 1, date.lastIndexOf("/"));
			oldDay = date.substring(0, date.indexOf("/"));
			oldYear = date.substring(date.lastIndexOf("/") + 1);
			textMonth.setText(oldMonth);
			textDay.setText(oldDay);
			textYear.setText(oldYear);
			year = Integer.valueOf(oldYear);
			int nMonth = MonthCount(oldMonth);

			month = Integer.valueOf(nMonth) - 1;// - 1;// nMonth//oldMonth
			day = Integer.valueOf(oldDay);
		}

		llDriverBadgeExpiryDate.setOnClickListener(new TextViewOnCLickListener());
		textBadgecolorGreen.setOnClickListener(new TextViewOnCLickListener());
		textBadgecolorYellow.setOnClickListener(new TextViewOnCLickListener());

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

	}

	class TextViewOnCLickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tvNext:
				nextButtonCall();
				break;

			case R.id.tvHiddenNext:
				nextButtonCall();
				break;

			case R.id.llDriverBadgeExpiryDate:
				showDialog(DATE_DIALOG_ID);
				break;

			case R.id.textBadgecolorGreen:

				setBadgeColour(textBadgecolorGreen, textBadgecolorYellow, getResources().getColor(R.color.badgecolor_green),
						getResources().getColor(R.color.badgecolor_unselected), Constants.BADGE_COLOUR_GREEN);
				rlSuburban.setVisibility(View.GONE);
				// etSuburbanAreaData.setText("");

				break;

			case R.id.textBadgecolorYellow:
				if (listNoOfSectors == null || listNoOfSectors.size() == 0) {
					if (Util.mContext != null && NetworkUtil.isNetworkOn(Util.mContext)) {
						SectorTask sectorTask = new SectorTask();
						sectorTask.execute();
					} else if (Util.mContext != null) {
						Util.showToastMessage(Util.mContext, getResources().getString(R.string.no_network_error),
								Toast.LENGTH_LONG);
					}
				}
				setBadgeColour(textBadgecolorYellow, textBadgecolorGreen, getResources().getColor(R.color.badgecolor_yellow),
						getResources().getColor(R.color.badgecolor_unselected), Constants.BADGE_COLOUR_YELLOW);
				rlSuburban.setVisibility(View.VISIBLE);
				break;

			}
		}
	}

	void nextButtonCall() {
		if (mContext != null)
			Util.hideSoftKeyBoard(mContext, textNext);

		String sBadgeNumber = etBadgeNumber.getText().toString().trim();
		String sDay = textDay.getText().toString().trim();
		// String sSector = etSuburbanAreaData.getText().toString().trim();
		String sSelectedBagdeNumbers = listSelectedBadges.toString();
		sSelectedBagdeNumbers = sSelectedBagdeNumbers.replace("[", "");
		sSelectedBagdeNumbers = sSelectedBagdeNumbers.replace("]", "");

		if (sBadgeNumber.isEmpty()) {
			Util.showToastMessage(mContext, "Please complete all fields", Toast.LENGTH_LONG);
		} else if (sDay.isEmpty()) {
			Util.showToastMessage(mContext, "Please select driver bagde expiry date", Toast.LENGTH_LONG);
		} else if (Util.getCitySelectedLondon(mContext) && szBadgeColor.equals(Constants.BADGE_COLOUR_YELLOW)
				&& listSelectedBadges.size() <= 0) {
			Util.showToastMessage(mContext, "Please complete all fields",// Please
																			// select
																			// suburban
																			// areas
					Toast.LENGTH_LONG);
		} else {
			if (isComingFromEditMyAccount) {
				driverDetailsBundle.putBoolean(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS, true);
			}
			driverDetailsBundle.putString(Constants.BADGE_NUMBER, sBadgeNumber);
			// if (!dateSelected.equals(""))
			// driverDetailsBundle.putString(
			// Constants.DRIVER_BADGE_EXPIRY, dateSelected);
			// else {
			dateSelected = textDay.getText().toString() + "/" + textMonth.getText().toString() + "/"
					+ textYear.getText().toString();// -
			driverDetailsBundle.putString(Constants.DRIVER_BADGE_EXPIRY, dateSelected);
			// }
			if (Util.getCitySelectedLondon(mContext)) {

				driverDetailsBundle.putString(Constants.BADGE_COLOUR, szBadgeColor);

				AppValues.mapRegistrationData.put(Constants.BADGE_COLOUR, szBadgeColor);

				if (szBadgeColor.equals(Constants.BADGE_COLOUR_YELLOW)) {
					driverDetailsBundle.putString(Constants.SECTOR, sSelectedBagdeNumbers);
					AppValues.mapRegistrationData.put(Constants.SECTOR, sSelectedBagdeNumbers);
				}
			}

			AppValues.mapRegistrationData.put(Constants.BADGE_NUMBER, sBadgeNumber);
			AppValues.mapRegistrationData.put(Constants.DRIVER_BADGE_EXPIRY, textDay.getText().toString() + "/"
					+ textMonth.getText().toString() + "/" + textYear.getText().toString());

			if (!isComingFromEditMyAccount) {
				if (NetworkUtil.isNetworkOn(mContext)) {

					dialogDriverDetails = new ProgressDialog(SignUp_DriverDetails_Activity.this);
					dialogDriverDetails.setMessage("Loading...");
					dialogDriverDetails.setCancelable(false);
					dialogDriverDetails.show();

					RegistrationTaskDriverDetails registrationTask = new RegistrationTaskDriverDetails();
					registrationTask.bundle = driverDetailsBundle;
					registrationTask.execute();
				} else {
					Util.showToastMessage(mContext, getResources().getString(R.string.no_network_error), Toast.LENGTH_LONG);
				}
			} else {

				final AlertDialog dialog = new AlertDialog.Builder(mContext)
						.setMessage("Changing of marked fields will put your account into a pending state")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog, int argWhich) {
								Intent intent = new Intent(SignUp_DriverDetails_Activity.this,
										SignUp_VehicleDetails_Activity.class);
								intent.putExtras(driverDetailsBundle);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();

				// final Dialog dialog = new Dialog(mContext,
				// R.style.mydialogstyle);
				// dialog.setContentView(R.layout.confirmation_dialog);
				// dialog.setCanceledOnTouchOutside(false);
				// // dialog.setTitle("Alert!!");
				// dialog.setCancelable(false);
				// dialog.show();
				// textYes = (TextView) dialog.findViewById(R.id.tvYes);
				// textNo = (TextView) dialog.findViewById(R.id.tvNo);
				// textNo.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// dialog.dismiss();
				// }
				// });
				// textYes.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // TODO Auto-generated method stub
				// dialog.dismiss();
				//
				// Intent intent = new Intent(
				// SignUp_DriverDetails_Activity.this,
				// SignUp_VehicleDetails_Activity.class);
				// intent.putExtras(driverDetailsBundle);
				// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// startActivity(intent);
				// }
				// });
			}
		}
	}

	void setBadgeColour(TextView tvSelected, TextView tvUnselected, int selectedColor, int unselectedColor, String badgeColor) {
		tvSelected.setTextColor(selectedColor);
		tvUnselected.setTextColor(unselectedColor);
		szBadgeColor = badgeColor;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case DATE_DIALOG_ID:
			// set date picker as current date
			DatePickerDialog _date = new DatePickerDialog(this, datePickerListener, year, month, day) {

				@Override
				public void onDateChanged(DatePicker view, int currentyear, int monthOfYear, int dayOfMonth) {

					year = calendar.get(Calendar.YEAR);
					month = calendar.get(Calendar.MONTH);
					day = calendar.get(Calendar.DAY_OF_MONTH);

					if (currentyear < year)
						view.updateDate(year, month, day);

					if (monthOfYear < month && currentyear == year)
						view.updateDate(year, month, day);

					if (dayOfMonth < day && currentyear == year && monthOfYear == month)
						view.updateDate(year, month, day);
				}
			};
			return _date;

		}
		return null;

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			String sMonth = MonthName(month);

			if (currentYear == year && currentMonth == month && currentDay == day) {
				Toast.makeText(mContext, "You cannot select current date!", Toast.LENGTH_LONG).show();
				textDay.setText("");
				textMonth.setText("");
				textYear.setText("");

				initialiseDatePicker();
			} else {
				textDay.setText(new StringBuilder().append(day));
				textMonth.setText(sMonth);// new StringBuilder().append(month +
											// 1)
				textYear.setText(new StringBuilder().append(year));
				dateSelected = textDay.getText().toString() + "-" + textMonth.getText().toString() + "-"
						+ textYear.getText().toString();
			}
			initialiseDatePicker();
		}
	};

	private String MonthName(int month) {
		String currentMonth = null;

		switch (month) {
		case Calendar.JANUARY:
			currentMonth = "January";
			break;
		case Calendar.FEBRUARY:
			currentMonth = "February";
			break;
		case Calendar.MARCH:
			currentMonth = "March";
			break;
		case Calendar.APRIL:
			currentMonth = "April";
			break;
		case Calendar.MAY:
			currentMonth = "May";
			break;
		case Calendar.JUNE:
			currentMonth = "June";
			break;
		case Calendar.JULY:
			currentMonth = "July";
			break;
		case Calendar.AUGUST:
			currentMonth = "August";
			break;
		case Calendar.SEPTEMBER:
			currentMonth = "September";
			break;
		case Calendar.OCTOBER:
			currentMonth = "October";
			break;
		case Calendar.NOVEMBER:
			currentMonth = "November";
			break;
		case Calendar.DECEMBER:
			currentMonth = "December";
			break;
		}

		return currentMonth;

	}

	private int MonthCount(String month) {
		// int currentMonth = Integer.valueOf(month);

		if (month.equalsIgnoreCase("January"))
			currentMonth = 1;
		else if (month.equalsIgnoreCase("February"))
			currentMonth = 2;
		else if (month.equalsIgnoreCase("March"))
			currentMonth = 3;
		else if (month.equalsIgnoreCase("April"))
			currentMonth = 4;
		else if (month.equalsIgnoreCase("May"))
			currentMonth = 5;
		else if (month.equalsIgnoreCase("June"))
			currentMonth = 6;
		else if (month.equalsIgnoreCase("July"))
			currentMonth = 7;
		else if (month.equalsIgnoreCase("August"))
			currentMonth = 8;
		else if (month.equalsIgnoreCase("September"))
			currentMonth = 9;
		else if (month.equalsIgnoreCase("October"))
			currentMonth = 10;
		else if (month.equalsIgnoreCase("November"))
			currentMonth = 11;
		else if (month.equalsIgnoreCase("December"))
			currentMonth = 12;

		return currentMonth;

	}

	public void initialiseDatePicker() {
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
	}

	static void finishActivity() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	public class RegistrationTaskDriverDetails extends AsyncTask<String, Void, JSONArray> {
		public Bundle bundle;
		JSONArray jErrorsArray;

		@Override
		protected JSONArray doInBackground(String... params) {
			// TODO Auto-generated method stub
			Registration registration = new Registration(SignUp_DriverDetails_Activity.this, bundle);
			String response = registration.RegistrationCredentials();
			if (Constants.isDebug)
				Log.e(TAG, "Registration response onSignUp DriverDetails::> " + response);
			try {
				JSONObject jObject = new JSONObject(response);

				if (jObject.has("errors")) {
					JSONArray jErrorsArray = jObject.getJSONArray("errors");
					return jErrorsArray;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jErrorsArray;

		}

		@Override
		protected void onPostExecute(JSONArray errorJsonArray) {
			// TODO Auto-generated method stub
			super.onPostExecute(errorJsonArray);

			if (mContext != null && dialogDriverDetails != null)
				dialogDriverDetails.dismiss();

			boolean isValid = false;
			try {
				int size = errorJsonArray.length();
				for (int i = 0; i < size; i++) {
					String errorMessage = errorJsonArray.getJSONObject(i).getString("message");
					if (Constants.isDebug)
						Log.e(TAG, "errorMessage :" + errorMessage);
					if (errorMessage.equalsIgnoreCase("A driver has been approved with that badge number")) {
						Util.showToastMessage(mContext, errorMessage, Toast.LENGTH_LONG);
						isValid = false;
						break;
					} else {
						isValid = true;
					}
				}
				if (Constants.isDebug)
					Log.e(TAG, "isValid DriverDetails: " + isValid);
				if (isValid) {
					Intent intent = new Intent(SignUp_DriverDetails_Activity.this, SignUp_VehicleDetails_Activity.class);
					intent.putExtras(driverDetailsBundle);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	class SectorTask extends AsyncTask<String, Void, String> {
		List<SectorList> sectorListResponse;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialogDriverDetails = new ProgressDialog(mContext);
			dialogDriverDetails.setMessage("Loading...");
			dialogDriverDetails.setCancelable(false);
			dialogDriverDetails.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// // TODO Auto-generated method stub
			SectorForCity sectorList = new SectorForCity(mContext);
			sectorListResponse = sectorList.getSectorList(mContext);
			if (sectorListResponse != null) {
				AppValues.sectorList = sectorListResponse;
				Log.e(TAG, "SectorList: " + sectorListResponse);

				for (int i = 0; i < sectorListResponse.size(); i++) {
					listNoOfSectors.add(sectorListResponse.get(i).getSectorNumber());
				}
				listNoOfSectors.add(String.valueOf(sectorListResponse.size() + 1));
			}
			return "success";

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialogDriverDetails != null)
				dialogDriverDetails.dismiss();

			if (result.equals("success")) {
				if (AppValues.driverDetails != null && AppValues.driverDetails.getSector() != null) {
					String sSelectedBadges = AppValues.driverDetails.getSector().toString();
					String[] items = sSelectedBadges.split(",");
					for (int i = 0; i < items.length; i++) {
						if (!listSelectedBadges.contains(items[i].trim())) {
							listSelectedBadges.add(items[i].trim());
						}
					}
				}

				adapter = new CustomGridAdapter(mContext, listNoOfSectors);
				gridSuburbanAreas.setAdapter(adapter);
				if (adapter != null)
					gridSuburbanAreas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							TextView textview = (TextView) view.findViewById(R.id.grid_text);
							if (!listSelectedBadges.contains(String.valueOf(position + 1))) {
								listSelectedBadges.add(String.valueOf(position + 1));
								textview.setTextColor(getResources().getColor(R.color.textview_selected));
							} else {
								listSelectedBadges.remove(String.valueOf(position + 1));
								textview.setTextColor(getResources().getColor(R.color.textview_unselected));
							}

						}

					});
			} else {
				Util.showToastMessage(Util.mContext, result, Toast.LENGTH_LONG);
			}

		}
	}

}
