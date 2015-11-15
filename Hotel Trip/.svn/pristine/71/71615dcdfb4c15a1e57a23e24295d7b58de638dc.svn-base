package com.hoteltrip.android;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.hoteltrip.android.receivers.NetworkBroadcastReceiver;
import com.hoteltrip.android.tasks.BookHotelTask;
import com.hoteltrip.android.tasks.LoginTask;
import com.hoteltrip.android.tasks.PaymentTask;
import com.hoteltrip.android.tasks.SignupTask;
import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.Const;
import com.hoteltrip.android.util.DataBaseHelper;
import com.hoteltrip.android.util.Utils;

public class TravellerDetailsTabActivity extends TabActivity implements
		OnTabChangeListener {

	TextView Travellersinfo, Memberslogin, greatdealsandoffers_tv,
			specialrequest_tv;
	TabHost tabHost;
	ImageButton paynow_btn, imgbtn_about;
	EditText ccbanknameTextBox, surnameTextBox, nameTextBox, emailTextBox,
			passwordTextBox, cardnumberfirstTextBox, cardnumbersecondTextBox,
			cardnumberfourthTextBox, cardnumberthirdTextBox, cvvcodeTextBox,
			ccbankname_et;
	Spinner countrynameSpinner, personaltitleSpinner, nationalitySpinner,
			yearSpinner, expirymonthsSpinner, cardTypeSpinner;
	CheckBox offersCheckBox;
	RelativeLayout travellerssecondrow;
	int currenttab = 0;
	Button btn_back;

	String szTimeStamp = "", szTransactionID = "", szCurrencyCode = "",
			szAmount = "", szCreditCardNumber = "", szValidityMonth = "",
			szValidityYear = "", szCVVCode = "", szBankCountryCode = "",
			szBankName = "", szCardHolderName = "", szCardHolderMail = "",
			szCardHolderSurname = "", szCardnumberfirst = "",
			szCardnumbersecond = "", szCardnumberthird = "",
			szCardnumberfourth = "";

	String szCountryName, szPaxPassport;
	String m_szHotelID;
	Handler mHandler;
	int posInList;
	int roomCategPosition;

	String szBFType, szRoomType;

	ProgressDialog mDialog;

	boolean bIsPaymentSuccess = false;

	TextView tvRoomHeader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_travelerinformation);

		if (getIntent().getExtras() != null) {
			szCurrencyCode = getIntent().getExtras().getString("currency");
			szAmount = getIntent().getExtras().getString("amount");
			m_szHotelID = getIntent().getExtras().getString("hotelid");
			posInList = getIntent().getExtras().getInt("posinlist");
			roomCategPosition = getIntent().getExtras().getInt("roomcategpos");
			szBFType = getIntent().getExtras().getString("bftype");
			szRoomType = getIntent().getExtras().getString("roomtype");
		}

		tvRoomHeader = (TextView) findViewById(R.id.tv_room);
		String szRoomHeader = "Room %s : %s Adult > %s (%s)";// "Room 1 : 1 Adult > SUPERIOR (BABF)";
		Log.e("TravellerDetails", "TravellerDetails-onCreate() "
				+ AppValues.nNumberOfRooms + " " + AppValues.nNumberOfAdults
				+ " " + szRoomType + " " + szBFType);
		if (szRoomType == null || szRoomType.equals("null"))
			szRoomType = "";

		tvRoomHeader.setText(String.format(szRoomHeader,
				AppValues.nNumberOfRooms, AppValues.nNumberOfAdults,
				szRoomType, szBFType));

		travellerssecondrow = (RelativeLayout) findViewById(R.id.travelerfourthtrow);
		ccbankname_et = (EditText) findViewById(R.id.et_ccbankname);
		cvvcodeTextBox = (EditText) findViewById(R.id.et_cvvcode);
		cardnumberfirstTextBox = (EditText) findViewById(R.id.et_cardnumbefirst);
		cardnumbersecondTextBox = (EditText) findViewById(R.id.et_cardnumbesecond);
		cardnumberfourthTextBox = (EditText) findViewById(R.id.et_cardnumberfourth);
		cardnumberthirdTextBox = (EditText) findViewById(R.id.et_cardnumbethird);
		paynow_btn = (ImageButton) findViewById(R.id.btn_paynow);
		surnameTextBox = (EditText) findViewById(R.id.et_surname);
		greatdealsandoffers_tv = (TextView) findViewById(R.id.tv_greatdealsandoffers);
		nameTextBox = (EditText) findViewById(R.id.et_name);
		emailTextBox = (EditText) findViewById(R.id.emailaddress_et);
		passwordTextBox = (EditText) findViewById(R.id.password_et);
		specialrequest_tv = (TextView) findViewById(R.id.tv_specialrequest);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setTypeface(Utils
				.getHelveticaNeueBold(TravellerDetailsTabActivity.this));
		imgbtn_about = (ImageButton) findViewById(R.id.imgbtn_about);
		imgbtn_about.setVisibility(View.GONE);
		offersCheckBox = (CheckBox) findViewById(R.id.cb_greatdealsandoffers);
		nationalitySpinner = (Spinner) findViewById(R.id.nationality_spinner);
		personaltitleSpinner = (Spinner) findViewById(R.id.spinner_personaltitle);

		final String[] bankcountrycodes = getResources().getStringArray(
				R.array.bankcountrycodes);
		yearSpinner = (Spinner) findViewById(R.id.spinner_year);
		expirymonthsSpinner = (Spinner) findViewById(R.id.spinner_expirymonths);
		cardTypeSpinner = (Spinner) findViewById(R.id.spinner_cardtype);
		countrynameSpinner = (Spinner) findViewById(R.id.spinner_countryname);
		countrynameSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int position, long id) {
						// TODO Auto-generated method stub
						int itemlselectedposition = position;
						String countrycode = countrynameSpinner
								.getSelectedItem().toString();

						szBankCountryCode = bankcountrycodes[itemlselectedposition];
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		btn_back.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		paynow_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				szCardHolderName = nameTextBox.getText().toString();
				szCardHolderSurname = surnameTextBox.getText().toString();
				szBankName = ccbankname_et.getText().toString();
				szCardnumberfirst = cardnumberfirstTextBox.getText().toString();
				szCardnumbersecond = cardnumbersecondTextBox.getText()
						.toString();
				szCardnumberthird = cardnumberthirdTextBox.getText().toString();
				szCardnumberfourth = cardnumberfourthTextBox.getText()
						.toString();
				szCVVCode = cvvcodeTextBox.getText().toString();
				szCardHolderMail = emailTextBox.getText().toString();
				szCreditCardNumber = szCardnumberfirst + szCardnumbersecond
						+ szCardnumberthird + szCardnumberfourth;
				szValidityYear = yearSpinner.getSelectedItem().toString();
				szValidityMonth = expirymonthsSpinner.getSelectedItem()
						.toString();

				if (AppValues.szName.equals("")
						&& (szCardHolderSurname.length() == 0
								|| szCardHolderName.length() == 0
								|| nationalitySpinner.getSelectedItemPosition() == 0
								|| szCardHolderMail.length() == 0 || passwordTextBox
								.getText().toString().length() == 0)) {
					emptyfieldsalertdialog("Please enter member info Or Proceed to login");
				} else if (AppValues.szName.equals("")
						&& passwordTextBox.getText().toString().length() < 6) {
					emptyfieldsalertdialog("Password length less than 6");
				} else if (!AppValues.szName.equals("")
						&& (szCardHolderSurname.length() > 0 && surnameTextBox
								.getText().toString().length() == 0)) {
					emptyfieldsalertdialog("Please enter Surname");
				} else if (!AppValues.szName.equals("")
						&& (szCardHolderName.length() == 0 && nameTextBox
								.getText().toString().length() > 0)) {
					emptyfieldsalertdialog("Please enter Name");
				} else if (ccbankname_et.length() == 0) {
					emptyfieldsalertdialog("Please enter Bank Name");
				} else if (szCardnumberfirst.length() < 4
						|| szCardnumbersecond.length() < 4
						|| szCardnumberthird.length() < 4
						|| szCardnumberfourth.length() < 4
						|| szCVVCode.length() < 3
						|| cardTypeSpinner.getSelectedItemPosition() == 0) {
					emptyfieldsalertdialog("Please enter Card Details properly");
				} else {
					if (!AppValues.szLoggedInEmail.equals("")) {
						szCardHolderMail = AppValues.szLoggedInEmail;
						if (szCardHolderName.equals(""))
							szCardHolderName = AppValues.szName;
						if (szCardHolderSurname.equals(""))
							szCardHolderSurname = AppValues.szSurName;
					}
					if (isEmailAddressValid(szCardHolderMail)) {
						// proceed
						if (AppValues.szName.equals("")) {
							szCountryName = nationalitySpinner
									.getSelectedItem().toString();

							DataBaseHelper dbHelper;
							Cursor cursor;
							try {
								dbHelper = new DataBaseHelper(
										TravellerDetailsTabActivity.this);

								String whereClause = "CountryName =?";
								// String[] whereArgs = new String[] {
								// "Bangkok",
								// "Thailand" };
								String[] whereArgs = new String[] { szCountryName };
								cursor = dbHelper.myDataBase.query("codes",
										null, whereClause, whereArgs, null,
										null, null);
								if (cursor.moveToFirst()) {
									szPaxPassport = cursor.getString(1);
								}
								Log.d("TravellerDetailsTabActivity",
										"CountryName:: " + szCountryName);
								Log.d("TravellerDetailsTabActivity",
										"CountryCode or PaxPassPort:: "
												+ szPaxPassport);
							} catch (Exception e) {

							}
						}

						if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

							if (AppValues.szLoggedInEmail.equals("")) {

								mDialog = showProgressDialog("Registering...");
								SignupTask task = new SignupTask(
										TravellerDetailsTabActivity.this,
										mHandler);
								task.szAddress = "";
								task.szCityCode = "";
								task.szCountryCode = szBankCountryCode;
								task.szEmail = szCardHolderMail;
								AppValues.szMemberName = task.szFirstName = szCardHolderName;
								AppValues.szMemberSurname = task.szLastName = szCardHolderSurname;
								AppValues.szMemberPrefix = task.szPrefix = personaltitleSpinner
										.getSelectedItem().toString();
								task.szPassword = passwordTextBox.getText()
										.toString();
								task.szPaxPassport = szPaxPassport;
								task.szPhone = "";
								if (offersCheckBox.isChecked())
									task.szSubscription = "Y";
								else
									task.szSubscription = "N";

								task.execute();
							} else {
								if (!bIsPaymentSuccess) {
									mDialog = showProgressDialog("Payment processing...");
									PaymentTask task = new PaymentTask(
											TravellerDetailsTabActivity.this,
											mHandler);
									task.szXMLData = createXMLStructure("", "",
											szCurrencyCode, szAmount,
											szCreditCardNumber,
											szValidityMonth, szValidityYear,
											szCVVCode, szBankCountryCode,
											szBankName, szCardHolderName,
											szCardHolderMail);
									task.execute();
								} else {
									mDialog = showProgressDialog("Booking Hotel...");

									AppValues.szMemberName = szCardHolderName;
									AppValues.szMemberSurname = szCardHolderSurname;
									AppValues.szMemberPrefix = personaltitleSpinner
											.getSelectedItem().toString();

									BookHotelTask task = new BookHotelTask(
											TravellerDetailsTabActivity.this,
											mHandler);
									task.posInList = posInList;
									task.szHotelID = m_szHotelID;
									task.roomCategPosition = roomCategPosition;
									task.execute();
								}
							}
						} else {
							Toast.makeText(TravellerDetailsTabActivity.this,
									"No network connection", Toast.LENGTH_LONG)
									.show();
						}

					} else {
						validemailalertdialog();
					}
				}
				// Toast.makeText(
				// TravellerDetailsTabActivity.this,
				// "" + szCardHolderName + szCardHolderSurname
				// + szBankName + szCreditCardNumber
				// + szCardHolderMail + szValidityYear
				// + szValidityMonth + szCVVCode
				// + szBankCountryCode + szCardHolderMail,
				// Toast.LENGTH_LONG).show();

			}
		});

		specialrequest_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent specialrequest = new Intent(
						TravellerDetailsTabActivity.this,
						SpecialRequestActivity.class);
				startActivity(specialrequest);
			}
		});
		greatdealsandoffers_tv
				.setText(Html
						.fromHtml("<font size=12 color=#000000>"
								+ "Yes! I want to hear about great deals, special offers, and news from"
								+ "</font><font size=14 color=#d16413>"
								+ " HotelTrip.com" + "</font>"));
		greatdealsandoffers_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = "http://www.hoteltrip.com/";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
		// Applying fonts
		cvvcodeTextBox.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		cardnumberfirstTextBox.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		cardnumbersecondTextBox.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		cardnumberfourthTextBox.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		cardnumberthirdTextBox.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));

		ccbankname_et.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		emailTextBox.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		passwordTextBox.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		nameTextBox.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		surnameTextBox.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));

		// Textview for tabs
		Travellersinfo = new TextView(this);
		Memberslogin = new TextView(this);
		Travellersinfo.setText("Traveller Details");
		Travellersinfo.setGravity(Gravity.CENTER);
		Travellersinfo.setTextColor(Color.parseColor("#d16413"));
		Travellersinfo.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		Travellersinfo.setTextSize(16);
		Memberslogin.setText("Member Login");
		Memberslogin.setGravity(Gravity.CENTER);
		Memberslogin.setTextColor(Color.parseColor("#000000"));
		Memberslogin.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		Memberslogin.setTextSize(16);
		Drawable backgroundDrawable = getResources().getDrawable(
				R.drawable.boxtypboders);

		tabHost = getTabHost();

		// Tab for travelersinfo
		TabSpec travelersinfo = tabHost.newTabSpec("Traveler Details");
		travelersinfo.setIndicator(Travellersinfo);
		Intent travelersIntent = new Intent(this, TravelersInfoActivity.class);
		travelersinfo.setContent(travelersIntent);

		// Tab for members login
		TabSpec memberslogin = tabHost.newTabSpec("Member Login");

		memberslogin.setIndicator(Memberslogin);

		Intent membersIntent = new Intent(this, MembersLoginActivity.class);
		memberslogin.setContent(membersIntent);
		tabHost.getTabWidget().setBackgroundDrawable(backgroundDrawable);
		tabHost.setOnTabChangedListener(this);

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.boxtypboders);
		}
		tabHost.addTab(travelersinfo); // Adding travelers tab
		tabHost.addTab(memberslogin); // Adding members login tab

		mHandler = new Handler() {

			@Override
			public void handleMessage(android.os.Message msg) {

				if (mDialog != null) {
					mDialog.dismiss();
				}

				if (msg.what == Const.REGISTRATION_SUCCESS) { // Registration
																// Success
					mDialog = showProgressDialog("Payment processing...");
					AppValues.szLoggedInEmail = szCardHolderMail;
					AppValues.szName = szCardHolderName;
					AppValues.szSurName = szCardHolderSurname;
					AppValues.szPaxPassport = szPaxPassport;
					AppValues.szPrefix = personaltitleSpinner.getSelectedItem()
							.toString();
					PaymentTask task = new PaymentTask(
							TravellerDetailsTabActivity.this, mHandler);
					task.szXMLData = createXMLStructure("", "", szCurrencyCode,
							szAmount, szCreditCardNumber, szValidityMonth,
							szValidityYear, szCVVCode, szBankCountryCode,
							szBankName, szCardHolderName, szCardHolderMail);
					task.execute();
				} else if (msg.what == Const.REGISTRATION_FAILED) { // Registration
					// Failed
					showAlertDialog("Registration failed!", (String) msg.obj);
				} else if (msg.what == Const.PAYMENT_SUCCESS) { // payment
																// success
					bIsPaymentSuccess = true;
					AppValues.szChargedTo = cardTypeSpinner.getSelectedItem()
							.toString() + " " + szCardnumberfourth;

					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
						mDialog = showProgressDialog("Booking Hotel...");

						AppValues.szMemberName = szCardHolderName;
						AppValues.szMemberSurname = szCardHolderSurname;
						AppValues.szMemberPrefix = personaltitleSpinner
								.getSelectedItem().toString();

						BookHotelTask task = new BookHotelTask(
								TravellerDetailsTabActivity.this, mHandler);
						task.posInList = posInList;
						task.szHotelID = m_szHotelID;
						task.roomCategPosition = roomCategPosition;
						task.szPaymentCode = (String) msg.obj;
						task.execute();
					} else {
						Toast.makeText(TravellerDetailsTabActivity.this,
								"No network connection", Toast.LENGTH_LONG)
								.show();
					}
					// finish();
					// Intent specialrequest = new Intent(
					// TravellerDetailsTabActivity.this,
					// ConfirmationActivity.class);
					// startActivity(specialrequest);
				} else if (msg.what == Const.PAYMENT_FAILED) {// payment failed
					showAlertDialog("Transaction failed!", (String) msg.obj);
					bIsPaymentSuccess = false;
				} else if (msg.what == Const.USER_ALREADY_EXISTS) { // User
																	// Already
																	// Exists so
																	// Call
																	// Login
					try {
						Toast.makeText(TravellerDetailsTabActivity.this,
								(String) msg.obj, Toast.LENGTH_LONG).show();
					} catch (Exception e) {

					}

					mDialog = showProgressDialog("Logging in...");
					LoginTask task = new LoginTask(
							TravellerDetailsTabActivity.this, mHandler);
					task.szEmail = szCardHolderMail;
					task.szPassword = passwordTextBox.getText().toString();
					task.execute();
				} else if (msg.what == Const.LOGIN_SUCCESS) { // Successful
																// login
					mDialog = showProgressDialog("Payment processing...");
					AppValues.szLoggedInEmail = szCardHolderMail;
					// AppValues.szName = szCardHolderName;
					PaymentTask task = new PaymentTask(
							TravellerDetailsTabActivity.this, mHandler);
					task.szXMLData = createXMLStructure("", "", szCurrencyCode,
							szAmount, szCreditCardNumber, szValidityMonth,
							szValidityYear, szCVVCode, szBankCountryCode,
							szBankName, szCardHolderName, szCardHolderMail);
					task.execute();
				} else if (msg.what == Const.BOOKING_FAILED) { // Booking failed

					showAlertDialog("Booking failed!", (String) msg.obj);

				} else if (msg.what == Const.BOOKING_SUCCESS) { // Booking
																// successsss
					finish();
					SearchResultsActivity.finishMe();
					HotelDetailsActivity.finishMe();
					RoomDetailsActivity.finishMe();
					// AppValues.szLoggedInEmail = "";
					// AppValues.szName = "";
					// AppValues.szSurName = "";
					// AppValues.szPaxPassport = "";
					// AppValues.szPrefix = "";

					Intent confirmationIntent = new Intent(
							TravellerDetailsTabActivity.this,
							ConfirmationActivity.class);
					confirmationIntent.putExtra("bookingresponse",
							(String) msg.obj);
					startActivity(confirmationIntent);

				} else {
					Toast.makeText(TravellerDetailsTabActivity.this,
							"Error registering user", Toast.LENGTH_LONG).show();
				}
			};
		};
		countrynameSpinner.setOnTouchListener(spinnerListener());
		personaltitleSpinner.setOnTouchListener(spinnerListener());
		nationalitySpinner.setOnTouchListener(spinnerListener());
		yearSpinner.setOnTouchListener(spinnerListener());
		expirymonthsSpinner.setOnTouchListener(spinnerListener());
		cardTypeSpinner.setOnTouchListener(spinnerListener());

		hideSoftKeyboard();

		editTextChangeListener();
		editOnKeyListener();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bIsPaymentSuccess = false;
	};

	OnTouchListener spinnerListener() {
		OnTouchListener spinnerListener;
		spinnerListener = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getApplicationContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		};
		return spinnerListener;
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		currenttab = tabHost.getCurrentTab();
		if (currenttab == 0) {
			Travellersinfo.setTextColor(Color.parseColor("#d16413"));
			Memberslogin.setTextColor(Color.parseColor("#000000"));
			travellerssecondrow.setVisibility(View.VISIBLE);
		} else {
			Memberslogin.setTextColor(Color.parseColor("#d16413"));
			Travellersinfo.setTextColor(Color.parseColor("#000000"));

			travellerssecondrow.setVisibility(View.GONE);
		}

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.boxtypboders);
		}

		// Adding bottom image on current tab
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
				.setBackgroundResource(R.drawable.selectedtab);
	}

	// Timestamp format ddMMyyHHmmss
	// Transaction ID autogenerate (20) max length
	// description (50) max length The following symbols are not allowed:
	// !@#$%^&*()<>
	// Amount amount: 1.5 = 000000000150 - last two places decimal
	// Signature String = merchantID + uniqueTransactionCode + amt
	// pan - card number
	// bank name - ICICI Bank (50)max length

	// 764
	// Thai Baht
	// 840
	// US Dollar
	// 702
	// Singapore Dollar
	// 392
	// Japan Yen
	// 826
	// Pound Sterling
	// 458
	// Malaysian Ringgit
	// 360
	// Indonesia Rupiah
	// 978
	// Euro

	String createXMLStructure(String szTimeStamp,
			String szUniqueTransactionCode, String szCurrencyCode,
			String szAmount, String szCreditCardNumber, String szValidityMonth,
			String szValidityYear, String szCVVCode, String szBankCountryCode,
			String szBankName, String szCardHolderName, String szCardHolderMail) {

		szTimeStamp = new SimpleDateFormat("ddMMyyHHmmss").format(Calendar
				.getInstance().getTime());
		szUniqueTransactionCode = getMd5Hash(
				String.valueOf(System.currentTimeMillis())).substring(0, 20);

		String xmlData = "<PaymentRequest>\r\n"
				+ "    <version>8.0</version>\r\n" + "    <timeStamp>"
				+ szTimeStamp
				+ "</timeStamp>\r\n"
				+ "    <merchantID>215</merchantID>\r\n"
				+ "    <uniqueTransactionCode>"
				+ szUniqueTransactionCode
				+ "</uniqueTransactionCode>\r\n"
				+ "    <desc>Hotel Trip Booking</desc>\r\n"
				+ "    <amt>"
				+ szAmount
				+ "</amt>\r\n"
				+ "    <currencyCode>"
				+ 764
				+ "</currencyCode>\r\n"
				+ "    <pan>"
				+ szCreditCardNumber
				+ "</pan>\r\n"
				+ "    <expiry>\r\n"
				+ "        <month>"
				+ szValidityMonth
				+ "</month>\r\n"
				+ "        <year>"
				+ szValidityYear
				+ "</year>\r\n"
				+ "    </expiry>\r\n"
				// + "    <storeCardUniqueID></storeCardUniqueID>\r\n"
				+ "    <securityCode>"
				+ szCVVCode
				+ "</securityCode>\r\n"
				+ "    <clientIP>46.137.157.1</clientIP>\r\n"
				+ "    <panCountry>"
				+ szBankCountryCode
				+ "</panCountry>\r\n"
				+ "    <panBank>"
				+ Utils.EncodeXML(szBankName)
				+ "</panBank>\r\n"
				+ "    <cardholderName>"
				+ Utils.EncodeXML(szCardHolderName)
				+ "</cardholderName>\r\n"
				+ "    <cardholderEmail>"
				+ Utils.EncodeXML(szCardHolderMail)
				+ "</cardholderEmail>\r\n"
				+ "    <payCategoryID></payCategoryID>\r\n"
				+ "    <userDefined1></userDefined1>\r\n"
				+ "    <userDefined2></userDefined2>\r\n"
				+ "    <userDefined3></userDefined3>\r\n"
				+ "    <userDefined4></userDefined4>\r\n"
				+ "    <userDefined5></userDefined5>\r\n"
				+ "    <storeCard>N</storeCard>\r\n"
				+ "    <ippTransaction>N</ippTransaction>\r\n"
				+ "    <installmentPeriod>3</installmentPeriod>\r\n"
				+ "    <interestType>C</interestType>\r\n"
				+ "    <recurring>N</recurring>\r\n"
				+ "    <invoicePrefix></invoicePrefix>\r\n"
				+ "    <recurringAmount></recurringAmount>\r\n"
				+ "    <allowAccumulate></allowAccumulate>\r\n"
				+ "    <maxAccumulateAmt>\r\n"
				+ "    </maxAccumulateAmt>\r\n"
				+ "    <recurringInterval></recurringInterval>\r\n"
				+ "    <recurringCount></recurringCount>\r\n"
				+ "    <chargeNextDate></chargeNextDate>\r\n"
				+ "    <hashValue></hashValue>\r\n" + "</PaymentRequest>";

		String szSignature = "merchantID" + "uniqueTransactionCode" + "amt";
		szSignature = "215" + szUniqueTransactionCode + szAmount;

		Log.e("TravellerDetailsTabActivity", "XML Data:: " + xmlData);

		return xmlData;
	}

	private void emptyfieldsalertdialog(String message) {
		// Cancellation Alert Dialog
		TextView title = new TextView(this);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCustomTitle(title);

		builder.setMessage(message);

		builder.setCancelable(false);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}

		});

		AlertDialog alert = builder.show();
		TextView messageText = (TextView) alert
				.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER_VERTICAL);
		messageText.setTextColor(Color.BLACK);
		messageText.setTextSize(16);

	}

	String getMd5Hash(String szSource) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bmessageDigest = md.digest(szSource.getBytes());
			// We convert the result into a big integer, so that we can convert
			// it into a Hexadecimal;
			BigInteger convertedToNumber = new BigInteger(1, bmessageDigest);
			String szmd5 = convertedToNumber.toString(16); // convert to hex
															// string

			// stuff with leading zeroes which are removed when we convert the
			// byte array to a bigInteger;
			if (szmd5.length() < 32) {
				while (szmd5.length() < 32)
					szmd5 = "0" + szmd5;
			}

			return szmd5;
		} catch (NoSuchAlgorithmException e) {
			Log.e("MD5", e.getMessage());
			return null;
		}
	}

	private void validemailalertdialog() {
		// Cancellation Alert Dialog
		TextView title = new TextView(this);
		title.setTextColor(Color.parseColor("#d16413"));
		title.setTypeface(Utils
				.getHelveticaNeue(TravellerDetailsTabActivity.this));
		title.setText("Alert");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCustomTitle(title);

		builder.setMessage("Please Enter Valid Email");

		builder.setCancelable(false);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();

			}

		});

		AlertDialog alert = builder.show();
		TextView messageText = (TextView) alert
				.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER_VERTICAL);
		messageText.setTextColor(Color.BLACK);
		messageText.setTextSize(16);

	}

	public boolean isEmailAddressValid(String email) {
		boolean isEmailValid = false;

		String strExpression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern objPattern = Pattern.compile(strExpression,
				Pattern.CASE_INSENSITIVE);
		Matcher objMatcher = objPattern.matcher(inputStr);
		if (objMatcher.matches()) {
			isEmailValid = true;
		}
		return isEmailValid;
	}

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(
				TravellerDetailsTabActivity.this);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		dialog.show();
		return dialog;
	}

	void showAlertDialog(String alertTitle, String alertMessage) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				TravellerDetailsTabActivity.this);
		alertDialog.setTitle(alertTitle);
		alertDialog.setMessage(alertMessage);
		alertDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}

				});

		alertDialog.show();
	}

	/**
	 * Hides the soft keyboard
	 */
	public void hideSoftKeyboard() {
		if (getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), 0);
		}
	}

	/**
	 * Shows the soft keyboard
	 */
	public void showSoftKeyboard(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		view.requestFocus();
		inputMethodManager.showSoftInput(view, 0);
	}

	public void editTextChangeListener() {
		cardnumberfirstTextBox.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) { // m_etActivationCode1.clearFocus();
				if (cardnumberfirstTextBox.getText().length() > 3)
					cardnumbersecondTextBox.requestFocus();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		cardnumbersecondTextBox.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (cardnumbersecondTextBox.getText().length() > 3) {
					cardnumbersecondTextBox.clearFocus();
					cardnumberthirdTextBox.requestFocus();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		cardnumberthirdTextBox.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (cardnumberthirdTextBox.getText().length() > 3) {
					cardnumberthirdTextBox.clearFocus();
					cardnumberfourthTextBox.requestFocus();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}

	private void editOnKeyListener() {
		cardnumbersecondTextBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					if (cardnumbersecondTextBox.getText().length() == 0)
						cardnumberfirstTextBox.requestFocus();
				}
				return false;
			}
		});
		cardnumberthirdTextBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					if (cardnumberthirdTextBox.getText().length() == 0)
						cardnumbersecondTextBox.requestFocus();
				}
				return false;
			}
		});
		cardnumberfourthTextBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_DEL) {
					if (cardnumberfourthTextBox.getText().length() == 0)
						cardnumberthirdTextBox.requestFocus();
				}
				return false;
			}
		});
	}

}