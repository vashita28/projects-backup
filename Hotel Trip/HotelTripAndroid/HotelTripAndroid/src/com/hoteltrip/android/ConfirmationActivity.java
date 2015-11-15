package com.hoteltrip.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.hoteltrip.android.adapters.HotelDataAdapter;
import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.BookingTableActivity;

public class ConfirmationActivity extends SherlockFragmentActivity {

	ImageView tripdetails_tb, summaryofcharges_tb, othersinfo_tb;
	RelativeLayout rel_tripdetailstext, rel_summtext, rel_otherstext,
			rel_tripdetails, rel_summary, rel_others;
	View tripdetailsbelowview, summarychargesbelowview, othersinfobelowview;
	int i = 0;
	TextView yourtripdetails_tv;
	Button home_btn;
	String hotelname, hoteladdress, hotelpincode, amount, checkindate,
			checkoutdate, status, bookingid;

	RelativeLayout rlFacebook, rlTwitter, rlEmail, rel_emailfeedback, rel_map;
	// new way: twitter::
	public static String TWITTER_CONSUMER_KEY = "P7Gm1puU7fkzS8pHrm7GjQ"; // consumer
	// key
	public static String TWITTER_CONSUMER_SECRET = "XFZl6R4Crzgb0wTBh1N3dTfKFownyKDIpEgO1bE1CUk"; // consumer
	// secret
	// key

	// Preference Constants
	public static String PREFERENCE_NAME = "twitter_oauth";
	public static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	public static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	public static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	public static final String TWITTER_CALLBACK_URL = "oauth://t4jsample"; // http://hoteltrip.com

	// Twitter
	public static Twitter twitter;
	public static RequestToken requestToken;

	// Shared Preferences
	public static SharedPreferences mSharedPreferences;

	// Progress dialog
	ProgressDialog pDialog;

	// Your Facebook APP ID
	private static String APP_ID = "369886466485093";// "1402791289969536"; //
														// // Replace your App
														// ID
														// here

	// Instance of Facebook Class
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";

	TextView bookingIDText, pinCodeText, hotelNameText, hotelAddressText,
			checkinDateText, checkoutDateText, primaryGuestText,
			maxOccupancyText, roomTypeText, noOfNightsText,
			hotelNameSummaryText, noOfRoomsSummaryText, totalPriceText,
			billedPersonText, contactPhoneText, contactEmailText,
			chargedToText, dateBookedSummaryText, confirmedOnOthersText,
			nameOthersText, emailOthersText, taxAndFeesText, subTotalText;

	TextView noOfRoomsText, hotelPriceText;

	String m_szBookingResponse = "";
	JSONObject jBookingResponse = new JSONObject();

	public static boolean bIsTwitterSuccess = false;

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (bIsTwitterSuccess) {
			String status = "Hello!!  Hereee " + System.currentTimeMillis();
			new updateTwitterStatus().execute(status);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_confirmation);

		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().containsKey("bookingresponse")) {
				m_szBookingResponse = getIntent().getExtras().getString(
						"bookingresponse");
				try {
					jBookingResponse = new JSONObject(m_szBookingResponse);
					jBookingResponse = jBookingResponse
							.getJSONObject("BookHotel_Response");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// Check if twitter keys are set
		if (TWITTER_CONSUMER_KEY.trim().length() == 0
				|| TWITTER_CONSUMER_SECRET.trim().length() == 0) {
			// Internet Connection is not present
			Toast.makeText(ConfirmationActivity.this,
					"Please set your twitter oauth tokens first!",
					Toast.LENGTH_LONG).show();

			return;
		}

		// Shared Preferences
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);

		bookingIDText = (TextView) findViewById(R.id.tv_bookingid);
		pinCodeText = (TextView) findViewById(R.id.tv_pincode);
		hotelNameText = (TextView) findViewById(R.id.tv_hotelname_trip);
		hotelAddressText = (TextView) findViewById(R.id.tv_hoteladdress_trip);
		checkinDateText = (TextView) findViewById(R.id.tv_checkindate);
		checkoutDateText = (TextView) findViewById(R.id.tv_checkoutdate);
		primaryGuestText = (TextView) findViewById(R.id.tv_primareyguestname);
		maxOccupancyText = (TextView) findViewById(R.id.tv_maxoccupancy);
		noOfRoomsText = (TextView) findViewById(R.id.tv_no_of_rooms_confirmation);
		hotelPriceText = (TextView) findViewById(R.id.tv_hotelprice);
		roomTypeText = (TextView) findViewById(R.id.tv_roomtype);
		noOfNightsText = (TextView) findViewById(R.id.tv_noofnights);
		hotelNameSummaryText = (TextView) findViewById(R.id.tv_hotelname_summary);
		noOfRoomsSummaryText = (TextView) findViewById(R.id.tv_noofrooms_summary);
		totalPriceText = (TextView) findViewById(R.id.tv_triptotalprice);
		billedPersonText = (TextView) findViewById(R.id.tv_billedtoperson);
		contactPhoneText = (TextView) findViewById(R.id.tv_contactphoneno);
		contactEmailText = (TextView) findViewById(R.id.tv_contactemail);
		chargedToText = (TextView) findViewById(R.id.tv_chargedto);
		dateBookedSummaryText = (TextView) findViewById(R.id.tv_datebooked_summary);
		confirmedOnOthersText = (TextView) findViewById(R.id.tv_confirmedon_others);
		nameOthersText = (TextView) findViewById(R.id.tv_yourname_others);
		emailOthersText = (TextView) findViewById(R.id.tv_youremail_others);
		taxAndFeesText = (TextView) findViewById(R.id.tv_taxandfeeprice);
		subTotalText = (TextView) findViewById(R.id.tv_subtotal);

		summaryofcharges_tb = (ImageView) findViewById(R.id.summaryofcharges_tb);
		othersinfo_tb = (ImageView) findViewById(R.id.othersinfo_tb);
		tripdetails_tb = (ImageView) findViewById(R.id.tripdetails_tb);
		rel_tripdetails = (RelativeLayout) findViewById(R.id.rel_tripdetails);
		rel_tripdetailstext = (RelativeLayout) findViewById(R.id.rel_tripdetailstext);
		rel_others = (RelativeLayout) findViewById(R.id.rel_others);
		rel_otherstext = (RelativeLayout) findViewById(R.id.rel_otherstext);
		rel_summary = (RelativeLayout) findViewById(R.id.rel_summary);
		rel_summtext = (RelativeLayout) findViewById(R.id.rel_summtext);
		rel_map = (RelativeLayout) findViewById(R.id.rel_map);
		summarychargesbelowview = (View) findViewById(R.id.view8);
		othersinfobelowview = (View) findViewById(R.id.view10);
		tripdetailsbelowview = (View) findViewById(R.id.view6);
		yourtripdetails_tv = (TextView) findViewById(R.id.yourtripdetails_tv);
		yourtripdetails_tv.setText(Html
				.fromHtml("<b><font size=18 color=#000000>" + "Your"
						+ "</font></b>" + "<b><font size=20 color=#e67d26>"
						+ " booking details" + "</font></b>"));
		home_btn = (Button) findViewById(R.id.btn_home);
		rlFacebook = (RelativeLayout) findViewById(R.id.rel_facebook);
		rlTwitter = (RelativeLayout) findViewById(R.id.rel_twitter);
		rlEmail = (RelativeLayout) findViewById(R.id.rel_email);
		rel_emailfeedback = (RelativeLayout) findViewById(R.id.rel_emailfeedback);

		home_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		tripdetails_tb.setOnClickListener(new OnClickListener() {
			// Trip Details toggle button listener
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tripdetails_tbclick(v);
			}
		});

		summaryofcharges_tb.setOnClickListener(new OnClickListener() {
			// Summary of Charges toggle button listener
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				summarycharges_tbclick(v);
			}
		});
		othersinfo_tb.setOnClickListener(new OnClickListener() {
			// Others Info toggle button listener
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				others_tbclick(v);

			}
		});
		rel_map.setOnClickListener(new OnClickListener() {
			// Others Info toggle button listener
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent map = new Intent(ConfirmationActivity.this,
						SearchResultsMapActivity.class);
				startActivity(map);

			}
		});
		rel_tripdetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Click listener for entire row of tripdetails
				if (i == 0) {
					rel_tripdetailstext.setVisibility(View.GONE);
					tripdetailsbelowview.setVisibility(View.GONE);
					tripdetails_tb
							.setBackgroundResource(R.drawable.zoominbutton);
					i++;

				} else {
					rel_tripdetailstext.setVisibility(View.VISIBLE);
					tripdetailsbelowview.setVisibility(View.VISIBLE);
					tripdetails_tb
							.setBackgroundResource(R.drawable.zoomoutbutton);
					i = 0;

				}

			}
		});
		rel_summary.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Click listener for entire row of summary charges
				if (i == 0) {
					rel_summtext.setVisibility(View.GONE);
					summarychargesbelowview.setVisibility(View.GONE);
					summaryofcharges_tb
							.setBackgroundResource(R.drawable.zoominbutton);
					i++;
				} else {

					rel_summtext.setVisibility(View.VISIBLE);
					summarychargesbelowview.setVisibility(View.VISIBLE);
					summaryofcharges_tb
							.setBackgroundResource(R.drawable.zoomoutbutton);
					i = 0;
				}
			}
		});
		rel_others.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Click listener for entire row of others info
				if (i == 0) {
					rel_otherstext.setVisibility(View.GONE);
					othersinfobelowview.setVisibility(View.GONE);
					othersinfo_tb
							.setBackgroundResource(R.drawable.zoominbutton);
					i++;
				} else {

					rel_otherstext.setVisibility(View.VISIBLE);
					othersinfobelowview.setVisibility(View.VISIBLE);

					othersinfo_tb
							.setBackgroundResource(R.drawable.zoomoutbutton);
					i = 0;
				}
			}
		});

		rlFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginToFacebook();
			}
		});

		rlTwitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				loginToTwitter();

			}
		});

		rlEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("plain/text");
				startActivity(Intent.createChooser(intent, ""));
			}
		});

		rel_emailfeedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent = new Intent(Intent.ACTION_SEND);
				// intent.setType("plain/text");
				// startActivity(Intent.createChooser(intent, ""));

				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri
						.fromParts("mailto", "support@hoteltrip.com", null));
				emailIntent
						.putExtra(Intent.EXTRA_SUBJECT, "HotelTrip Feedback");
				startActivity(Intent.createChooser(emailIntent,
						"Email us feedback"));

			}
		});

		// setting Confirmation data
		String szHotelID = "";
		JSONObject jBookIDObject = null;
		JSONObject jHotelDetailsObject = null;
		try {
			jBookIDObject = jBookingResponse.getJSONObject("CompleteService")
					.getJSONObject("HBookId");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			if (jBookingResponse.has("ResNo"))
				bookingIDText.setText(jBookingResponse.getString("ResNo"));

			pinCodeText.setText(jBookIDObject.getJSONObject("@attributes")
					.getString("InternalCode"));

			szHotelID = jBookIDObject.getString("HotelId");

			jHotelDetailsObject = new JSONObject(
					HotelDataAdapter.mapHotelDetailsResponse.get(szHotelID))
					.getJSONObject("GetHotelDetail_Response");

			hotelNameText.setText(jHotelDetailsObject.getString("HotelName"));

			String szAddress = "";
			if (jHotelDetailsObject.has("Address1")
					&& jHotelDetailsObject.getString("Address1") != null
					&& !jHotelDetailsObject.getString("Address1").equals("{}"))
				szAddress = jHotelDetailsObject.getString("Address1");

			if (jHotelDetailsObject.has("Address2")
					&& jHotelDetailsObject.getString("Address2") != null
					&& !jHotelDetailsObject.getString("Address2").equals("{}"))
				szAddress = szAddress + ", "
						+ jHotelDetailsObject.getString("Address2");

			if (jHotelDetailsObject.has("Address3")
					&& jHotelDetailsObject.getString("Address3") != null
					&& !jHotelDetailsObject.getString("Address3").equals("{}"))
				szAddress = szAddress + ", "
						+ jHotelDetailsObject.getString("Address3");

			hotelAddressText.setText(szAddress);

			checkinDateText.setText(jBookIDObject.getJSONObject("Period")
					.getJSONObject("@attributes").getString("FromDt"));
			checkoutDateText.setText(jBookIDObject.getJSONObject("Period")
					.getJSONObject("@attributes").getString("ToDt"));

			if (!AppValues.szMemberName.equals("")
					&& !AppValues.szMemberName.equals(AppValues.szName))
				primaryGuestText.setText(AppValues.szMemberName);
			else
				primaryGuestText.setText(AppValues.szName);

			JSONObject jRoomObject = jBookIDObject
					.getJSONObject("RoomCatgInfo").getJSONObject("RoomCatg")
					.getJSONObject("Room");

			JSONArray jRoomseqArray = null;
			JSONObject jRoomseqObject = null;
			try {
				jRoomseqArray = jRoomObject.getJSONArray("SeqRoom");
			} catch (JSONException e) {
				jRoomseqObject = jRoomObject.getJSONObject("SeqRoom");
			}

			if (jRoomseqArray != null) {
				noOfRoomsText.setText(String.valueOf(jRoomseqArray.length()));
				float fTotalPrice = 0;
				int nTotalAdults = 0;
				String szRoomType = "";
				for (int i = 0; i < jRoomseqArray.length(); i++) {
					nTotalAdults = nTotalAdults
							+ Integer.parseInt(jRoomseqArray.getJSONObject(i)
									.getJSONObject("@attributes")
									.getString("AdultNum"));

					fTotalPrice = fTotalPrice
							+ Float.parseFloat(jRoomseqArray.getJSONObject(i)
									.getString("TotalPrice"));

					if (i > 0)
						szRoomType = szRoomType
								+ ","
								+ jRoomseqArray.getJSONObject(i)
										.getJSONObject("@attributes")
										.getString("RoomType");
					else
						szRoomType = szRoomType
								+ jRoomseqArray.getJSONObject(i)
										.getJSONObject("@attributes")
										.getString("RoomType");

				}

				maxOccupancyText.setText(String.valueOf(nTotalAdults));
				roomTypeText.setText(szRoomType);
				totalPriceText.setText(AppValues.szCurrency + " "
						+ String.valueOf(fTotalPrice));
				hotelPriceText.setText(AppValues.szCurrency + " "
						+ String.valueOf(fTotalPrice));

			} else {
				noOfRoomsText.setText("1");
				maxOccupancyText.setText(jRoomseqObject.getJSONObject(
						"@attributes").getString("AdultNum"));
				roomTypeText.setText(jRoomseqObject
						.getJSONObject("@attributes").getString("RoomType"));
				totalPriceText.setText(AppValues.szCurrency + " "
						+ jRoomseqObject.getString("TotalPrice"));
				hotelPriceText.setText(AppValues.szCurrency + " "
						+ jRoomseqObject.getString("TotalPrice"));
			}

			noOfNightsText.setText(String.valueOf(AppValues.nNumberOfNights));
			hotelNameSummaryText.setText(jHotelDetailsObject
					.getString("HotelName"));

			if (jRoomseqArray != null)
				noOfRoomsSummaryText.setText(String.valueOf(jRoomseqArray
						.length()));
			else
				noOfRoomsSummaryText.setText("1");

			billedPersonText.setText(AppValues.szName);

			contactPhoneText.setText(AppValues.szContactPhone);
			contactEmailText.setText(AppValues.szLoggedInEmail);
			chargedToText.setText(AppValues.szChargedTo);
			dateBookedSummaryText.setText(jBookIDObject.getJSONObject(
					"@attributes").getString("VoucherDt"));

			confirmedOnOthersText.setText(jBookIDObject.getJSONObject(
					"@attributes").getString("VoucherDt"));
			nameOthersText.setText(AppValues.szName);
			emailOthersText.setText(AppValues.szLoggedInEmail);

			taxAndFeesText.setText("");
			subTotalText.setText("");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			bookingid = bookingIDText.getText().toString();
			hotelname = hotelNameText.getText().toString();
			hoteladdress = hotelAddressText.getText().toString();
			hotelpincode = pinCodeText.getText().toString();
			amount = totalPriceText.getText().toString();
			checkindate = checkinDateText.getText().toString();
			checkoutdate = checkoutDateText.getText().toString();
			status = "Charged";
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (bookingid != null && !bookingid.equals("")
					&& !inbookings(bookingid)) {
				addtobookings();
			} else {
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void addtobookings() throws IOException {
		// TODO Auto-generated method stub
		BookingTableActivity bookingsTable = new BookingTableActivity(
				ConfirmationActivity.this);
		try {
			// adding specific booking in db
			bookingsTable.open();
			bookingsTable.insertRows(bookingid, hotelname, checkindate,
					checkoutdate, hotelpincode, status, hoteladdress, amount);
		} finally {
			if (bookingsTable != null)
				bookingsTable.close();
		}
	}

	private boolean inbookings(String bookingid) throws IOException {
		// TODO Auto-generated method stub
		BookingTableActivity bookingsTable = new BookingTableActivity(
				ConfirmationActivity.this);
		try {
			// check if booking exist in db
			bookingsTable.open();
			Log.d("",
					"is Hotel in Favourite "
							+ bookingsTable.isHotelFavorite(bookingid));
			if (bookingsTable.isHotelFavorite(bookingid))
				return true;
			else
				return false;

		} finally {
			if (bookingsTable != null)
				bookingsTable.close();
		}
	}

	public void loginToFacebook() {
		mSharedPreferences = getPreferences(MODE_PRIVATE);
		String access_token = mSharedPreferences.getString(
				"access_token_facebook1", null);
		long expires = mSharedPreferences
				.getLong("access_expires_facebook1", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {

			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					Facebook.FORCE_DIALOG_AUTH, new DialogListener() {

						@Override
						public void onCancel() {
							// Function to handle cancel event
						}

						@Override
						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = mSharedPreferences
									.edit();
							editor.putString("access_token_facebook1",
									facebook.getAccessToken());
							editor.putLong("access_expires_facebook1",
									facebook.getAccessExpires());
							editor.commit();

							postToWall();
						}

						@Override
						public void onError(DialogError error) {
							// Function to handle error

						}

						@Override
						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors

						}

					});
		} else {
			postToWall();
		}
	}

	public void postToWall() {
		// post on user's wall.

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Bundle parameters = new Bundle();
		parameters.putString("link", "http://hoteltrip.com");
		// parameters.putString("message",

		// "Just booked ticket" + " " + System.currentTimeMillis());
		parameters.putString(
				"message",
				"Booked Hotel at" + " "
						+ formatter.format(currentDate.getTime()));

		// parameters.putString("description", "My first ticket");
		parameters.putString("description", "Using HotelTrip Android app!");

		Toast.makeText(this, "Message posted successfully!!", Toast.LENGTH_LONG)
				.show();
		try {
			facebook.request("me/feed", parameters, "POST");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loginToTwitter() {
		// Check if already logged in
		// runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// Toast.makeText(getApplicationContext(),
		// "Status tweeted successfully", Toast.LENGTH_SHORT)
		// .show();
		// }
		// });
		if (!isTwitterLoggedInAlready()) {

			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
			builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
			Configuration configuration = builder.build();

			TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {

				requestToken = twitter
						.getOAuthRequestToken(TWITTER_CALLBACK_URL);

				Intent intent = new Intent(ConfirmationActivity.this,
						WebViewTwitterActivity.class);
				intent.putExtra("URI", requestToken.getAuthenticationURL());
				startActivity(intent);

			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {
			// user already logged into twitter
			String status = "Status" + System.currentTimeMillis();
			new updateTwitterStatus().execute(status);
		}
	}

	private boolean isTwitterLoggedInAlready() {
		// return twitter login status from Shared Preferences
		return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}

	class updateTwitterStatus extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ConfirmationActivity.this);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {

			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");

			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

				// Access Token
				String access_token = mSharedPreferences.getString(
						PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(
						PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);

				String string_img_url = "http://hoteltrip.com";
				String string_msg = "Booked ticket using HotelTrip Android App!";
				// here we have web url image so we have to make it as file to
				// upload

				// Now share both message & image to sharing activity

				// twitter4j.Status response = twitter.updateStatus(status);

				// twitter4j.Status response = twitter.updateStatus(string_msg
				// + " " + string_img_url.toString() + " "
				// + System.currentTimeMillis());
				twitter4j.Status response = twitter.updateStatus(string_msg
						+ " " + string_img_url.toString() + " " + "at "
						+ formatter.format(currentDate.getTime()));

				// twitter4j.Status response = twitter
				// .updateStatus("very run counts. Every bit of junk food hurts. Do a few pushups. Run another minute. No mercy, it's not a game, it's your life.");

				Log.d("Status", "status " + response.getText());
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			bIsTwitterSuccess = false;

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();
				}
			});
		}

	}

	public void summarycharges_tbclick(View view) {
		// summarycharges toggle method
		// boolean on = ((ToggleButton) view).isChecked();

		if (i == 0) {
			rel_summtext.setVisibility(View.GONE);
			summarychargesbelowview.setVisibility(View.GONE);
			summaryofcharges_tb.setBackgroundResource(R.drawable.zoominbutton);
			i++;
		} else {
			rel_summtext.setVisibility(View.VISIBLE);
			summarychargesbelowview.setVisibility(View.VISIBLE);
			summaryofcharges_tb.setBackgroundResource(R.drawable.zoomoutbutton);
			i = 0;
		}
	}

	public void tripdetails_tbclick(View view) {
		// tripdetails toggle method
		// boolean on = ((ToggleButton) view).isChecked();
		if (i == 0) {
			rel_tripdetailstext.setVisibility(View.GONE);
			tripdetailsbelowview.setVisibility(View.GONE);
			tripdetails_tb.setBackgroundResource(R.drawable.zoominbutton);
			i++;
		} else {
			rel_tripdetailstext.setVisibility(View.VISIBLE);
			tripdetailsbelowview.setVisibility(View.VISIBLE);
			tripdetails_tb.setBackgroundResource(R.drawable.zoomoutbutton);
			i = 0;
		}

	}

	public void others_tbclick(View view) {
		// others toggle method
		// boolean on = ((ToggleButton) view).isChecked();
		if (i == 0) {
			rel_otherstext.setVisibility(View.GONE);
			othersinfobelowview.setVisibility(View.GONE);
			othersinfo_tb.setBackgroundResource(R.drawable.zoominbutton);
			i++;
		} else {
			rel_otherstext.setVisibility(View.VISIBLE);
			othersinfobelowview.setVisibility(View.VISIBLE);
			othersinfo_tb.setBackgroundResource(R.drawable.zoomoutbutton);
			i = 0;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppValues.szLoggedInEmail = "";
		AppValues.szName = "";
		AppValues.szSurName = "";
		AppValues.szPaxPassport = "";
		AppValues.szPrefix = "";
	}

}
