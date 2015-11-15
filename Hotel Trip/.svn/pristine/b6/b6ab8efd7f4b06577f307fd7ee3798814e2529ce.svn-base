package com.hoteltrip.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hoteltrip.android.RoomDetailsActivity.ViewCancelPolicyTask;
import com.hoteltrip.android.adapters.HotelDataAdapter;
import com.hoteltrip.android.gallery.ImageGridActivity;
import com.hoteltrip.android.tasks.SearchHotelTask;
import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.CancelationPolicyData;
import com.hoteltrip.android.util.Const;
import com.hoteltrip.android.util.DateFetchedListener;
import com.hoteltrip.android.util.FavouriteTableActivity;
import com.hoteltrip.android.util.HotelData;
import com.hoteltrip.android.util.NumberPicker;
import com.hoteltrip.android.util.SearchHotelDataFetched;
import com.hoteltrip.android.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HotelDetailsActivity extends BaseActivity implements
		OnClickListener, DateFetchedListener, SearchHotelDataFetched {

	ToggleButton toggleFavoriteBtn;
	Long id;
	int pos;
	HotelData hotelData;

	TextView hotelPriceText;

	TextView hotelTitleText, hotelAddressText, tripAdvisorRatingCountText,
			checkInDateText, tvPromotionText;
	ImageView hotelImage, tripAdvisorImage;
	ImageButton cancellationimg_btn;
	ProgressDialog progressCancellationDialog, progressSearchDialog;
	RatingBar hotelRatingStar;

	NumberPicker noOfNightsPicker;
	private int UPPER_LIMIT = 30;
	private int LOWER_LIMIT = 1;

	LinearLayout ll_seeMorePics;
	RelativeLayout rel_reviews, rel_map;
	String m_szHotelID = "", szInternalCode = "";

	LinearLayout ll_room_types, ll_more_room_types, ll_see_more_room,
			ll_hotel_description, ll_hotel_description_text, ll_promotion,
			ll_promotion_text;
	Button booknow_btn;
	JSONArray jRoomCategArray;
	JSONObject jRoomCategObject;
	JSONObject joRoomCatg;
	boolean bIsMoreRoomsOpen = false, bIsHotelDescriptionTextOpen = false;
	RelativeLayout rlCancelation;
	private Calendar calendarToday;
	private SimpleDateFormat formatter, dateFormatter;

	static Float price;
	NumberPicker firstRoomRatePicker;
	String m_szCurrency;

	String m_szBfType, m_szRoomType, m_szRoomCode;

	ArrayList<String> listImageURL;

	String m_szHotelTripReviewImageURL = "", m_szHotelTripRatingCount = "";
	static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hotel_details);
		super.init("Hotel Details");
		btnMap.setVisibility(View.GONE);

		mContext = this;

		rel_reviews = (RelativeLayout) findViewById(R.id.rel_reviews);
		rel_map = (RelativeLayout) findViewById(R.id.rel_map);
		booknow_btn = (Button) findViewById(R.id.booknow_btn);

		booknow_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchHotelTask newSearchTask = new SearchHotelTask(
						HotelDetailsActivity.this, HotelDetailsActivity.this);
				newSearchTask.roomListDetails=AppValues.roomListDetails;
				newSearchTask.szCheckInDate=AppValues.szCheckInDate;
				newSearchTask.szDestCity=AppValues.szDestCity;
				newSearchTask.szDestCountry=AppValues.szDestCountry;
				newSearchTask.szHotelId=hotelData.getHotelId();
				newSearchTask.szNoOfNights=String.valueOf(AppValues.nNumberOfNights);
				newSearchTask.szPaxPassport=AppValues.szPaxPassport;
			
				newSearchTask.execute();
				progressSearchDialog = new ProgressDialog(
						HotelDetailsActivity.this);
				progressSearchDialog.setMessage("Checking for price change, if any. Please Wait...");
				progressSearchDialog.setIndeterminate(true);
				progressSearchDialog.setCancelable(false);
				progressSearchDialog.show();

			}
		});

		rlCancelation = (RelativeLayout) findViewById(R.id.rl_cancpolicy);
		cancellationimg_btn = (ImageButton) findViewById(R.id.nextarrowtop_ib);
		rlCancelation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new ViewCancelPolicyTask().execute();
			}

		});
		calendarToday = Calendar.getInstance();
		formatter = new SimpleDateFormat("EEE MMMM dd yyyy");
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		rel_reviews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent reviews = new Intent(HotelDetailsActivity.this,
						ReviewsActivity.class);
				reviews.putExtra("hotelid", m_szHotelID);
				startActivity(reviews);
			}
		});
		rel_map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent reviews = new Intent(HotelDetailsActivity.this,
						SearchResultsMapActivity.class);
				startActivity(reviews);
			}
		});
		if (getIntent().getExtras() != null) {
			id = getIntent().getExtras().getLong("id");
			pos = getIntent().getExtras().getInt("pos");
			Log.e("*********************", "***************" + id + " , " + pos);
			hotelData = AppValues.hotelDataList.get(pos);
			m_szHotelID = hotelData.getHotelId();
			szInternalCode = hotelData.getInternalCode();
		}
		tvPromotionText = (TextView) findViewById(R.id.tv_promotion_text);
		JSONObject joTemp = new JSONObject();
		try {
			Log.i(getClass().getSimpleName(), hotelData.getObject("RoomCateg")
					.toString());
			joRoomCatg = joTemp = hotelData.getObject("RoomCateg");

		} catch (Exception e) {
			try {
				Log.i(getClass().getSimpleName(),
						hotelData.getArray("RoomCateg").toString());
				joRoomCatg = joTemp = hotelData.getArray("RoomCateg")
						.getJSONObject(0);
			} catch (Exception we) {

			}

		}
		if (joTemp.optJSONObject("RoomType") != null) {
			try {
				joTemp = joTemp.getJSONObject("RoomType");
				if (joTemp.optJSONObject("Rate") != null)
					joTemp = joTemp.getJSONObject("Rate");
				else if (joTemp.optJSONArray("Rate") != null) {
					joTemp = joTemp.getJSONArray("Rate").getJSONObject(0);
				}
				joTemp = joTemp.getJSONObject("RoomRateInfo")
						.getJSONObject("Promotion")
						.getJSONObject("@attributes");
				if (joTemp.getString("Value").toLowerCase().equals("false")) {
					Log.i(getClass().getSimpleName(), "No Promotion Found"
							+ joTemp.getString("Name"));
					tvPromotionText.setText("No Promotion Found.");

				}

				else if (joTemp.getString("Value").toLowerCase().equals("true")) {
					Log.i(getClass().getSimpleName(),
							"Promotion " + joTemp.getString("Name"));
					tvPromotionText.setText(joTemp.getString("Name"));
				}

			} catch (JSONException e) {

				e.printStackTrace();
			}

		}

		ll_room_types = (LinearLayout) findViewById(R.id.ll_room_types);
		ll_more_room_types = (LinearLayout) findViewById(R.id.ll_more_room_types);
		ll_see_more_room = (LinearLayout) findViewById(R.id.ll_see_more_room);
		ll_hotel_description = (LinearLayout) findViewById(R.id.ll_hotel_description);
		ll_hotel_description_text = (LinearLayout) findViewById(R.id.ll_hotel_description_text);
		ll_promotion = (LinearLayout) findViewById(R.id.ll_promotion);
		ll_promotion_text = (LinearLayout) findViewById(R.id.ll_promotion_text);
		ll_hotel_description.setOnClickListener(this);
		ll_hotel_description_text.setVisibility(View.GONE);
		ll_promotion.setOnClickListener(this);
		ll_promotion_text.setVisibility(View.GONE);
		checkInDateText = (TextView) findViewById(R.id.tv_check_in_date);
		// checkInDateText.setText(formatter.format(calendarToday.getTime()));
		checkInDateText.setText(formatter.format(AppValues.checkinCalendar
				.getTime()));

		jRoomCategArray = hotelData.getArray("RoomCateg");
		if (jRoomCategArray != null)
			try {
				jRoomCategObject = jRoomCategArray.getJSONObject(0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			jRoomCategObject = hotelData.getObject("RoomCateg");

		if (jRoomCategArray == null || jRoomCategArray.length() <= 2) {
			ll_more_room_types.setVisibility(View.GONE);
			ll_see_more_room.setVisibility(View.GONE);
		} else {
			ll_see_more_room.setVisibility(View.VISIBLE);
			ll_see_more_room.setOnClickListener(this);
			ll_more_room_types.setVisibility(View.GONE);
		}

		// TODO: number picker integration
		noOfNightsPicker = (NumberPicker) findViewById(R.id.picker_number_of_nights);
		noOfNightsPicker.setRange(LOWER_LIMIT, UPPER_LIMIT);
		noOfNightsPicker.setFragmentManager(getSupportFragmentManager());
		noOfNightsPicker.setCurrent(AppValues.nNumberOfNights);
		noOfNightsPicker.setButtonsInvisible();
		noOfNightsPicker.setEnabled(false);

		toggleFavoriteBtn = (ToggleButton) findViewById(R.id.togglebtn_favorite);
		toggleFavoriteBtn.setVisibility(View.VISIBLE);
		checkFavouriteAndSetImage(m_szHotelID, toggleFavoriteBtn);

		toggleFavoriteBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					if (checkFavourites(m_szHotelID)) {
						removeFavourites();
						toggleFavoriteBtn.setChecked(true);
					} else {
						addFavourites();
						toggleFavoriteBtn.setChecked(false);
					}
					SearchResultsActivity.favoriteHandler.sendEmptyMessage(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		ll_seeMorePics = (LinearLayout) findViewById(R.id.ll_see_more_pics);
		hotelAddressText = (TextView) findViewById(R.id.tv_hotel_address);
		hotelTitleText = (TextView) findViewById(R.id.tv_hotel_title);
		hotelImage = (ImageView) findViewById(R.id.iv_hotel_image);
		hotelPriceText = (TextView) findViewById(R.id.tv_hotel_price);
		hotelRatingStar = (RatingBar) findViewById(R.id.starRating);
		tripAdvisorRatingCountText = (TextView) findViewById(R.id.tv_trip_advisor);
		tripAdvisorImage = (ImageView) findViewById(R.id.iv_trip_advisor);

		ll_seeMorePics.setOnClickListener(this);

		hotelTitleText.setText(hotelData.getHotelName());
		hotelAddressText.setText(HotelDataAdapter.mapAddress.get(hotelData
				.getHotelId()));
		hotelImage.setImageBitmap(HotelDataAdapter.mapImages.get(hotelData
				.getHotelId()));
		hotelRatingStar.setRating(hotelData.getRating());

		String text = "from <font size=24><b>%s %s </b></font> /night";

		// String text = "from %s %s /night";
		try {
			hotelPriceText.setText(Html.fromHtml(String.format(
					text,
					hotelData.getCurrency(),
					jRoomCategObject.getJSONObject("@attributes").getString(
							"Price"))));

			m_szBfType = jRoomCategObject.getJSONObject("@attributes")
					.getString("BFType");
			m_szRoomType = jRoomCategObject.getJSONObject("@attributes")
					.getString("Name");
			m_szRoomCode = jRoomCategObject.getJSONObject("@attributes")
					.getString("Code");

			// hotelPriceText.setTag(jRoomCategObject.getJSONObject("@attributes")
			// .getString("BFType"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hotelPriceText
					.setText(Html.fromHtml(String.format(text, "$", "0")));
		}

		try {
			text = "from <font size=22><b>%s</b></font> Reviews";
			// text = "from %s Reviews";
			m_szHotelTripReviewImageURL = hotelData
					.getObject("trip_advisor_data")
					.getJSONArray("hotel_location").getJSONObject(0)
					.getJSONObject("@attributes").getString("img_url");

			m_szHotelTripRatingCount = hotelData.getObject("trip_advisor_data")
					.getJSONArray("hotel_location").getJSONObject(0)
					.getJSONObject("@attributes").getString("reviewcnt");

			tripAdvisorRatingCountText.setText(Html.fromHtml(String.format(
					text, m_szHotelTripRatingCount)));
			ImageLoader.getInstance().displayImage(m_szHotelTripReviewImageURL,
					tripAdvisorImage);
		} catch (Exception e) {
			// e.printStackTrace();
			m_szHotelTripReviewImageURL = "";
			m_szHotelTripRatingCount = "0";
			tripAdvisorRatingCountText.setText(Html.fromHtml(String.format(
					text, "0")));
			tripAdvisorImage.setImageBitmap(null);
		}

		int childcount = ll_room_types.getChildCount();
		for (int i = 0; i < childcount; i++) {
			View v = ll_room_types.getChildAt(i);
			TextView totalRateText = (TextView) v
					.findViewById(R.id.tv_total_rate);
			TextView roomRateText = (TextView) v
					.findViewById(R.id.tv_room_rate);
			NumberPicker numberPicker = (NumberPicker) v
					.findViewById(R.id.picker_room_type);

			numberPicker.setRange(LOWER_LIMIT, UPPER_LIMIT);
			numberPicker.setFragmentManager(getSupportFragmentManager());

			try {
				text = "Total: <b>%s</b><font size=24 color=#f47c20> <b>%s </b></font>";

				if (totalRateText != null) {
					totalRateText.setText(Html.fromHtml(String.format(
							text,
							hotelData.getCurrency(),
							Float.parseFloat(jRoomCategObject.getJSONObject(
									"@attributes").getString("Price")))));
				}

				text = "at %s<font size=24 color=#81837f> <b>%s </b></font> / room";

				if (roomRateText != null) {
					roomRateText.setText(Html.fromHtml(String.format(
							text,
							hotelData.getCurrency(),
							Float.parseFloat(jRoomCategObject.getJSONObject(
									"@attributes").getString("Price")))));
				}

				numberPicker.setTextViewForUpdation(
						(TextView) v.findViewById(R.id.tv_total_rate),
						hotelData.getCurrency(),
						Float.parseFloat(jRoomCategObject.getJSONObject(
								"@attributes").getString("Price")));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		listImageURL = HotelDataAdapter.mapHotelImagesList.get(m_szHotelID);
		Random r = new Random();
		int randomNumber = 0;

		// Setting room details for rooms 1,2
		if (jRoomCategArray != null && jRoomCategArray.length() > 0) {
			ll_room_types.removeAllViews();
			for (int i = 0; i < 2 && i <= jRoomCategArray.length(); i++) {
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_room_type = inflater.inflate(R.layout.room_type_row,
						null);

				String price = "from <b>%s %s </b> /night";
				TextView hotelPriceText = (TextView) view_room_type
						.findViewById(R.id.tv_hotel_price);
				// String text = "from %s %s /night";
				try {
					hotelPriceText.setText(Html.fromHtml(String.format(
							price,
							hotelData.getCurrency(),
							jRoomCategArray.getJSONObject(i)
									.getJSONObject("@attributes")
									.getString("Price"))));

					// hotelPriceText.setTag(jRoomCategArray.getJSONObject(i)
					// .getJSONObject("@attributes").getString("BFType"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					hotelPriceText.setText(Html.fromHtml(String.format(price,
							"$", "0")));
				}

				String imageURL = "";
				if (listImageURL != null) {
					imageURL = listImageURL.get(randomNumber);
					randomNumber = r.nextInt(listImageURL.size() - 0) + 0;
					ImageView hotelImage = (ImageView) view_room_type
							.findViewById(R.id.iv_hotel_image);
					ImageLoader.getInstance()
							.displayImage(imageURL, hotelImage);
				}

				price = "from <font color=#f47c20><b>%s %s </b></font> /night";
				TextView roomRateText = (TextView) view_room_type
						.findViewById(R.id.tv_room_rate);
				m_szCurrency = hotelData.getCurrency();
				try {
					roomRateText.setText(Html.fromHtml(String.format(
							price,
							hotelData.getCurrency(),
							jRoomCategArray.getJSONObject(i)
									.getJSONObject("@attributes")
									.getString("Price"))));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				TextView totalRateText = (TextView) view_room_type
						.findViewById(R.id.tv_total_rate);
				NumberPicker numberPicker = (NumberPicker) view_room_type
						.findViewById(R.id.picker_room_type);

				numberPicker.setRange(LOWER_LIMIT, UPPER_LIMIT);
				numberPicker.setFragmentManager(getSupportFragmentManager());

				try {
					text = "Total: <b>%s</b><font color=#f47c20> <b>%s </b></font>";

					if (totalRateText != null) {
						totalRateText.setText(Html.fromHtml(String.format(
								text,
								hotelData.getCurrency(),
								Float.parseFloat(jRoomCategArray
										.getJSONObject(i)
										.getJSONObject("@attributes")
										.getString("Price")))));
					}

					text = "at %s<font color=#81837f> <b>%s </b></font> / room";

					if (roomRateText != null) {
						roomRateText.setText(Html.fromHtml(String.format(
								text,
								hotelData.getCurrency(),
								Float.parseFloat(jRoomCategArray
										.getJSONObject(i)
										.getJSONObject("@attributes")
										.getString("Price")))));
					}

					if (i == 0) {
						HotelDetailsActivity.price = Float
								.valueOf(jRoomCategArray.getJSONObject(i)
										.getJSONObject("@attributes")
										.getString("Price"));
						firstRoomRatePicker = numberPicker;
					}
					numberPicker.setTextViewForUpdation(
							totalRateText,
							hotelData.getCurrency(),
							Float.parseFloat(jRoomCategArray.getJSONObject(i)
									.getJSONObject("@attributes")
									.getString("Price")));
					String description = null;
					try {
						description = new JSONObject(
								HotelDataAdapter.mapHotelDetailsResponse
										.get(m_szHotelID)).getJSONObject(
								"GetHotelDetail_Response").getString(
								"Description");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					RelativeLayout openRoomDetailsView = (RelativeLayout) view_room_type
							.findViewById(R.id.rel_open_room_details);
					openRoomDetailsView
							.setOnClickListener(new roomDetailsClickListener(
									hotelData.getCurrency(), Float
											.parseFloat(jRoomCategArray
													.getJSONObject(i)
													.getJSONObject(
															"@attributes")
													.getString("Price")),
									imageURL, description, pos, m_szHotelID, i,
									jRoomCategArray.getJSONObject(i)
											.getJSONObject("@attributes")
											.getString("Name"), jRoomCategArray
											.getJSONObject(i)
											.getJSONObject("@attributes")
											.getString("BFType"),
									jRoomCategArray.getJSONObject(i)
											.getJSONObject("@attributes")
											.getString("Code"), hotelData
											.getInternalCode(), joRoomCatg));

					TextView textHotelRoomCategory = (TextView) view_room_type
							.findViewById(R.id.tvRoomCategoryName);
					textHotelRoomCategory.setText(jRoomCategArray
							.getJSONObject(i).getJSONObject("@attributes")
							.getString("Name"));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ll_room_types.addView(view_room_type);
			}
		}

		// Setting room details for rooms > 2
		if (jRoomCategArray != null && jRoomCategArray.length() >= 2) {
			ll_more_room_types.removeAllViews();
			for (int i = 2; i < jRoomCategArray.length(); i++) {
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view_room_type = inflater.inflate(R.layout.room_type_row,
						null);

				String price = "from <b>%s %s </b> /night";
				TextView hotelPriceText = (TextView) view_room_type
						.findViewById(R.id.tv_hotel_price);
				// String text = "from %s %s /night";
				try {
					hotelPriceText.setText(Html.fromHtml(String.format(
							price,
							hotelData.getCurrency(),
							jRoomCategArray.getJSONObject(i)
									.getJSONObject("@attributes")
									.getString("Price"))));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					hotelPriceText.setText(Html.fromHtml(String.format(price,
							"$", "0")));
				}
				String imageURL = "";
				if (listImageURL != null) {
					imageURL = listImageURL.get(randomNumber);
					randomNumber = r.nextInt(listImageURL.size() - 0) + 0;
					ImageView hotelImage = (ImageView) view_room_type
							.findViewById(R.id.iv_hotel_image);
					ImageLoader.getInstance()
							.displayImage(imageURL, hotelImage);
				}

				price = "from <font color=#f47c20><b>%s %s </b></font> /night";
				TextView roomRateText = (TextView) view_room_type
						.findViewById(R.id.tv_room_rate);
				try {
					roomRateText.setText(Html.fromHtml(String.format(
							price,
							hotelData.getCurrency(),
							jRoomCategArray.getJSONObject(i)
									.getJSONObject("@attributes")
									.getString("Price"))));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				TextView totalRateText = (TextView) view_room_type
						.findViewById(R.id.tv_total_rate);
				NumberPicker numberPicker = (NumberPicker) view_room_type
						.findViewById(R.id.picker_room_type);

				numberPicker.setRange(LOWER_LIMIT, UPPER_LIMIT);
				numberPicker.setFragmentManager(getSupportFragmentManager());

				try {
					text = "Total: <b>%s</b><font color=#f47c20> <b>%s </b></font>";

					if (totalRateText != null) {
						totalRateText.setText(Html.fromHtml(String.format(
								text,
								hotelData.getCurrency(),
								Float.parseFloat(jRoomCategArray
										.getJSONObject(i)
										.getJSONObject("@attributes")
										.getString("Price")))));
					}

					text = "at %s<font color=#81837f> <b>%s </b></font> / room";

					if (roomRateText != null) {
						roomRateText.setText(Html.fromHtml(String.format(
								text,
								hotelData.getCurrency(),
								Float.parseFloat(jRoomCategArray
										.getJSONObject(i)
										.getJSONObject("@attributes")
										.getString("Price")))));
					}

					numberPicker.setTextViewForUpdation(
							totalRateText,
							hotelData.getCurrency(),
							Float.parseFloat(jRoomCategObject.getJSONObject(
									"@attributes").getString("Price")));
					String description = null;
					try {
						description = new JSONObject(
								HotelDataAdapter.mapHotelDetailsResponse
										.get(m_szHotelID)).getJSONObject(
								"GetHotelDetail_Response").getString(
								"Description");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					RelativeLayout openRoomDetailsView = (RelativeLayout) view_room_type
							.findViewById(R.id.rel_open_room_details);
					openRoomDetailsView
							.setOnClickListener(new roomDetailsClickListener(
									hotelData.getCurrency(), Float
											.parseFloat(jRoomCategArray
													.getJSONObject(i)
													.getJSONObject(
															"@attributes")
													.getString("Price")),
									imageURL, description, pos, m_szHotelID, i,
									jRoomCategArray.getJSONObject(i)
											.getJSONObject("@RoomType")
											.getJSONObject("@attributes")
											.getString("Name"), jRoomCategArray
											.getJSONObject(i)
											.getJSONObject("@attributes")
											.getString("BFType"),
									jRoomCategObject.getJSONObject(
											"@attributes").getString("Code"),
									hotelData.getInternalCode(), joRoomCatg));

					TextView textHotelRoomCategory = (TextView) view_room_type
							.findViewById(R.id.tvRoomCategoryName);
					textHotelRoomCategory.setText(jRoomCategArray
							.getJSONObject(i).getJSONObject("@RoomType")
							.getJSONObject("@attributes").getString("Name"));

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ll_more_room_types.addView(view_room_type);
			}
		}

		// for room with only one type
		if (jRoomCategArray == null && jRoomCategObject != null) {
			ll_room_types.removeAllViews();

			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View view_room_type = inflater
					.inflate(R.layout.room_type_row, null);

			String price = "from <b>%s %s </b> /night";
			TextView hotelPriceText = (TextView) view_room_type
					.findViewById(R.id.tv_hotel_price);
			// String text = "from %s %s /night";
			try {
				hotelPriceText.setText(Html.fromHtml(String.format(price,
						hotelData.getCurrency(), jRoomCategObject
								.getJSONObject("@attributes")
								.getString("Price"))));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				hotelPriceText.setText(Html.fromHtml(String.format(price, "$",
						"0")));
			}

			String imageURL = "";
			if (listImageURL != null) {
				imageURL = listImageURL.get(randomNumber);
				randomNumber = r.nextInt(listImageURL.size() - 0) + 0;
				ImageView hotelImage = (ImageView) view_room_type
						.findViewById(R.id.iv_hotel_image);
				ImageLoader.getInstance().displayImage(imageURL, hotelImage);
			}

			price = "from <font color=#f47c20><b>%s %s </b></font> /night";
			TextView roomRateText = (TextView) view_room_type
					.findViewById(R.id.tv_room_rate);
			try {
				roomRateText.setText(Html.fromHtml(String.format(price,
						hotelData.getCurrency(), jRoomCategObject
								.getJSONObject("@attributes")
								.getString("Price"))));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			TextView totalRateText = (TextView) view_room_type
					.findViewById(R.id.tv_total_rate);
			NumberPicker numberPicker = (NumberPicker) view_room_type
					.findViewById(R.id.picker_room_type);

			numberPicker.setRange(LOWER_LIMIT, UPPER_LIMIT);
			numberPicker.setFragmentManager(getSupportFragmentManager());

			try {
				text = "Total: <b>%s</b><font color=#f47c20> <b>%s </b></font>";

				if (totalRateText != null) {
					totalRateText.setText(Html.fromHtml(String.format(
							text,
							hotelData.getCurrency(),
							Float.parseFloat(jRoomCategObject.getJSONObject(
									"@attributes").getString("Price")))));

					HotelDetailsActivity.price = Float
							.parseFloat(jRoomCategObject.getJSONObject(
									"@attributes").getString("Price"));
					firstRoomRatePicker = numberPicker;
				}

				text = "at %s<font color=#81837f> <b>%s </b></font> / room";

				if (roomRateText != null) {
					roomRateText.setText(Html.fromHtml(String.format(
							text,
							hotelData.getCurrency(),
							Float.parseFloat(jRoomCategObject.getJSONObject(
									"@attributes").getString("Price")))));
				}

				numberPicker.setTextViewForUpdation(
						totalRateText,
						hotelData.getCurrency(),
						Float.parseFloat(jRoomCategObject.getJSONObject(
								"@attributes").getString("Price")));
				String description = null;
				try {
					description = new JSONObject(
							HotelDataAdapter.mapHotelDetailsResponse
									.get(m_szHotelID)).getJSONObject(
							"GetHotelDetail_Response").getString("Description");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				RelativeLayout openRoomDetailsView = (RelativeLayout) view_room_type
						.findViewById(R.id.rel_open_room_details);
				openRoomDetailsView
						.setOnClickListener(new roomDetailsClickListener(
								hotelData.getCurrency(), Float
										.parseFloat(jRoomCategObject
												.getJSONObject("@attributes")
												.getString("Price")), imageURL,
								description, pos, m_szHotelID, -1,
								jRoomCategObject.getJSONObject("@RoomType")
										.getJSONObject("@attributes")
										.getString("Name"), jRoomCategObject
										.getJSONObject("@attributes")
										.getString("BFType"), jRoomCategObject
										.getJSONObject("@attributes")
										.getString("Code"), hotelData
										.getInternalCode(), joRoomCatg));

				TextView textHotelRoomCategory = (TextView) view_room_type
						.findViewById(R.id.tvRoomCategoryName);
				textHotelRoomCategory.setText(jRoomCategObject
						.getJSONObject("@RoomType")
						.getJSONObject("@attributes").getString("Name"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ll_room_types.addView(view_room_type);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_see_more_pics:
			Intent galleryIntent = new Intent(HotelDetailsActivity.this,
					ImageGridActivity.class);
			galleryIntent.putExtra("hotelid", m_szHotelID);
			startActivity(galleryIntent);
			break;
		case R.id.ll_see_more_room:
			bIsMoreRoomsOpen = !bIsMoreRoomsOpen;
			if (bIsMoreRoomsOpen) {
				ll_more_room_types.setVisibility(View.VISIBLE);
			} else {
				ll_more_room_types.setVisibility(View.GONE);
			}
			toggleLayoutSelectedImage(ll_see_more_room, bIsMoreRoomsOpen);
			break;
		case R.id.ll_hotel_description:
			bIsHotelDescriptionTextOpen = !bIsHotelDescriptionTextOpen;
			if (bIsHotelDescriptionTextOpen) {
				ll_hotel_description_text.setVisibility(View.VISIBLE);
				try {
					String szDescriptionText = new JSONObject(
							HotelDataAdapter.mapHotelDetailsResponse
									.get(m_szHotelID)).getJSONObject(
							"GetHotelDetail_Response").getString("Description");

					if (szDescriptionText == null
							|| szDescriptionText.equals("{}"))
						szDescriptionText = "";
					if(szDescriptionText.equals(""))
						szDescriptionText="No Description Found";
					((TextView) ll_hotel_description_text
							.findViewById(R.id.tv_hotel_description_text))
							.setText(szDescriptionText);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				ll_hotel_description_text.setVisibility(View.GONE);
			}
			toggleLayoutSelectedImage(ll_hotel_description,
					bIsHotelDescriptionTextOpen);
			break;

		case R.id.ll_promotion:
			if (ll_promotion_text.getVisibility() == View.GONE) {
				ll_promotion_text.setVisibility(View.VISIBLE);
				toggleLayoutSelectedImage(ll_promotion, true);
			} else {
				ll_promotion_text.setVisibility(View.GONE);
				toggleLayoutSelectedImage(ll_promotion, false);
			}
		default:
			break;
		}
	}

	void toggleLayoutSelectedImage(LinearLayout linearLayout, boolean value) {
		if (value) {
			((ImageView) linearLayout.findViewById(R.id.iv_toggle))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.zoomoutbutton));
		} else {
			((ImageView) linearLayout.findViewById(R.id.iv_toggle))
					.setImageDrawable(getResources().getDrawable(
							R.drawable.zoominbutton));
		}
	}

	@Override
	public void setSelectedDate(Calendar selectedCalendar) {
		// TODO Auto-generated method stub
		if (calendarToday.compareTo(selectedCalendar) == 1) {
			showMyDialog(Const.SELECT_DATE_BEFORE_ERROR_ID, null);
			checkInDateText.setText(formatter.format(calendarToday.getTime()));
		} else if (calculateOneYear(selectedCalendar)) {
			showMyDialog(Const.SELECT_DATE_GREATER_ERROR_ID, null);
			checkInDateText.setText(formatter.format(calendarToday.getTime()));
		} else {
			checkInDateText
					.setText(formatter.format(selectedCalendar.getTime()));
			// calendarToday = selectedCalendar;
		}
	}

	public void onDatePickerClick(final View v) {
		// showMyDialog(Const.DATE_PICKER_DIALOG_ID, this);
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

	class roomDetailsClickListener implements OnClickListener {
		Float price;
		String currency;
		String imageURL;
		String description;
		int position;
		String hotelID;
		int roomCategPosition;
		String roomType, bfType, internalCode, roomCode;
		JSONObject joRoomCatg;

		public roomDetailsClickListener(String currency, Float price,
				String imageURL, String description, int position,
				String hotelID, int roomCategPosition, String szRoomType,
				String szbfType, String roomCode, String szInternalCode,
				JSONObject joRoomCatg) {
			this.price = price;
			this.currency = currency;
			this.imageURL = imageURL;
			this.description = description;
			this.position = position;
			this.hotelID = hotelID;
			this.roomCategPosition = roomCategPosition;
			this.roomType = szRoomType;
			this.roomCode = roomCode;
			this.bfType = szbfType;
			this.internalCode = szInternalCode;
			this.joRoomCatg = joRoomCatg;
		}

		public void onClick(View v) {
			{
				Intent roomDetailsIntent = new Intent(
						HotelDetailsActivity.this, RoomDetailsActivity.class);
				Bundle bundleArgs = new Bundle();
				bundleArgs.putFloat("price", price);
				bundleArgs.putString("currency", currency);
				bundleArgs.putString("description", description);
				bundleArgs.putString("imageurl", imageURL);
				bundleArgs.putInt("position", position);
				bundleArgs.putString("hotelid", hotelID);
				bundleArgs.putInt("roomcategpos", roomCategPosition);
				bundleArgs.putString("bftype", bfType);
				bundleArgs.putString("roomtype", roomType);
				bundleArgs.putString("roomCode", roomCode);
				bundleArgs.putString("internalcode", internalCode);
				bundleArgs.putString("joRoomCatg", joRoomCatg.toString());
				roomDetailsIntent.putExtras(bundleArgs);

				Log.d("roomDetailsClickListener", "price is: " + price);
				Log.d("roomDetailsClickListener", "roomCategPosition is: "
						+ roomCategPosition);
				startActivity(roomDetailsIntent);

			}
		}
	}

	private boolean checkFavourites(String hotelID) throws IOException {
		FavouriteTableActivity favoritesTable = new FavouriteTableActivity(
				HotelDetailsActivity.this);
		try {
			// check if hotel exist in db
			favoritesTable.open();
			Log.d("",
					"is Hotel in Favourite "
							+ favoritesTable.isHotelFavorite(hotelID));
			if (favoritesTable.isHotelFavorite(hotelID))
				return true;
			else
				return false;

		} finally {
			if (favoritesTable != null)
				favoritesTable.close();
		}
	}

	void checkFavouriteAndSetImage(String hotelID,
			ToggleButton hotelFavoriteToggleButton) {
		try {
			// check if hotel exist in db and set the image accordingly on myfav
			// toggle button
			if (checkFavourites(hotelID)) {
				hotelFavoriteToggleButton.setChecked(false);
			} else {
				hotelFavoriteToggleButton.setChecked(true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void addFavourites() throws IOException {
		FavouriteTableActivity favoritesTable = new FavouriteTableActivity(
				HotelDetailsActivity.this);
		String hotelImageURL = "";
		if (listImageURL != null)
			hotelImageURL = listImageURL.get(0);
		try {
			// adding specific hotel in db
			favoritesTable.open();
			favoritesTable.insertRows(m_szHotelID, hotelTitleText.getText()
					.toString(), hotelAddressText.getText().toString(),
					hotelImageURL, String.valueOf(price),
					hotelData.getRating(), m_szHotelTripReviewImageURL,
					m_szHotelTripRatingCount, m_szCurrency);
		} finally {
			if (favoritesTable != null)
				favoritesTable.close();
		}
	}

	@SuppressWarnings("deprecation")
	private void removeFavourites() throws IOException {
		FavouriteTableActivity favoritesTable = new FavouriteTableActivity(
				HotelDetailsActivity.this);
		try {
			// removing hotel from favs
			favoritesTable.open();
			favoritesTable.deleteRow(m_szHotelID);
			Toast.makeText(HotelDetailsActivity.this,
					" Hotel removed from Favourites.", Toast.LENGTH_LONG)
					.show();

		} finally {
			if (favoritesTable != null)
				favoritesTable.close();
		}
	}

	class ViewCancelPolicyTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String szURL = "http://dev.pocketapp.co.uk/dev/hotel-trip/index.php/dataget/viewPrebookCancelPolicy";
			Log.e("ViewCancelPolicyTask", "URL is::  " + szURL);

			/*
			 * progressSearchDialog
			 * .setMessage("Searching Hotels. Please wait...");
			 * progressSearchDialog.setIndeterminate(true);
			 * progressSearchDialog.setCancelable(false);
			 */
			HotelDetailsActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					progressCancellationDialog = new ProgressDialog(
							HotelDetailsActivity.this);
					progressCancellationDialog
							.setMessage("Looking for cancellation policy. Please wait...");
					progressCancellationDialog.show();
				}
			});
			String response = null;
			try {

				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000); // timeout

				HttpPost postMethod = new HttpPost(szURL);
				postMethod.addHeader("Content-Type",
						"application/x-www-form-urlencoded");

				// postMethod.setEntity(new StringEntity(mainObject.toString(),
				// "utf-8"));

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						5);
				nameValuePairs.add(new BasicNameValuePair("checkin",
						AppValues.szCheckInDate));
				nameValuePairs.add(new BasicNameValuePair("checkout",
						AppValues.szCheckOutDate));
				nameValuePairs.add(new BasicNameValuePair("hotelid",
						m_szHotelID));
				nameValuePairs.add(new BasicNameValuePair("paxpassport",
						AppValues.szPaxPassport));
				nameValuePairs.add(new BasicNameValuePair("roominfo",
						AppValues.szRoomInfo));
				nameValuePairs.add(new BasicNameValuePair("internalcode",
						szInternalCode));
				postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				postMethod.setParams(httpParams);

				HttpResponse httpResponse = client.execute(postMethod);
				response = inputStreamToString(
						httpResponse.getEntity().getContent()).toString();

			} catch (Exception e) {

			}
			return response;

		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progressCancellationDialog != null)
				progressCancellationDialog.dismiss();
			cancellationpolicydialog(result);
		}
	}

	private StringBuilder inputStreamToString(InputStream content)
			throws IOException {
		// TODO Auto-generated method stub
		String line = "";
		StringBuilder total = new StringBuilder();

		BufferedReader rd = new BufferedReader(new InputStreamReader(content));
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}
		Log.i("ViewCancelPolicy", "inputStreamToString()-result  " + total);

		return total;
	}

	@SuppressWarnings("deprecation")
	private void cancellationpolicydialog(String cancellationPolicyJson) {
		// Cancellation Alert Dialog

		List<CancelationPolicyData> policyDataList = new ArrayList<CancelationPolicyData>();
		if (cancellationPolicyJson != null) {

			try {
				JSONObject jsonObject = new JSONObject(cancellationPolicyJson);
				jsonObject = jsonObject
						.getJSONObject("ViewCancelPolicy_Response");

				if (jsonObject.getJSONObject("Policies").optJSONArray("Policy") != null) {
					JSONArray jPolicyArray = (jsonObject
							.getJSONObject("Policies")).getJSONArray("Policy");

					for (int i = 0; i < jPolicyArray.length(); i++) {
						CancelationPolicyData policyData = new CancelationPolicyData();
						if (jPolicyArray.getJSONObject(i).has("RoomCatgCode")) {
							if (hotelData.getObject("RoomCateg") != null)
								policyData.setRoomCategory(hotelData
										.getObject("RoomCateg")

										.getJSONObject("@attributes")
										.getString("Name"));
							else if ((hotelData.getArray("RoomCateg") != null)) {
								policyData.setRoomCategory(jRoomCategArray
										.getJSONObject(i)
										.getJSONObject("@attributes")
										.getString("Name"));
							}
						}

						/*
						 * if
						 * (jPolicyArray.getJSONObject(i).has("RoomCatgCode"))
						 * policyData .setRoomCategory(jPolicyArray
						 * .getJSONObject(i).getString( "RoomCatgCode"));
						 */
						if (jPolicyArray.getJSONObject(i).has("ExCancelDays"))
							policyData.setPolicyCancelDays(Integer
									.parseInt(jPolicyArray.getJSONObject(i)
											.getString("ExCancelDays")));

						if (jPolicyArray.getJSONObject(i).has("ChargeType"))
							policyData.setChargeType(jPolicyArray
									.getJSONObject(i).getString("ChargeType"));

						if (jPolicyArray.getJSONObject(i).has("ChargeRate"))
							policyData.setChargeRate(jPolicyArray
									.getJSONObject(i).getString("ChargeRate"));

						if (jPolicyArray.getJSONObject(i).has("Description"))
							policyData.setDescription(jPolicyArray
									.getJSONObject(i).getString("Description"));
						if (jPolicyArray.getJSONObject(i).has("@attributes")) {
							policyData.setPolicyStartDate(jPolicyArray
									.getJSONObject(i)
									.getJSONObject("@attributes")
									.getString("FromDate"));
							policyData.setPolicyEndDate(jPolicyArray
									.getJSONObject(i)
									.getJSONObject("@attributes")
									.getString("ToDate"));
						}

						policyDataList.add(policyData);
					}
				} else if (jsonObject.getJSONObject("Policies").optJSONObject(
						"Policy") != null) {
					JSONObject jPolicyObject = (jsonObject
							.getJSONObject("Policies")).getJSONObject("Policy");

					for (int i = 0; i < jPolicyObject.length(); i++) {
						CancelationPolicyData policyData = new CancelationPolicyData();

						if (jPolicyObject.has("RoomCatgCode")) {
							if (hotelData.getObject("RoomCateg") != null)
								policyData.setRoomCategory(hotelData
										.getObject("RoomCateg")

										.getJSONObject("@attributes")
										.getString("Name"));
							else if ((hotelData.getArray("RoomCateg") != null)) {
								policyData.setRoomCategory(jRoomCategArray
										.getJSONObject(i)
										.getJSONObject("@attributes")
										.getString("Name"));
							}
						}
						/*
						 * if (jPolicyObject.has("RoomCatgCode"))
						 * policyData.setRoomCategory(jPolicyObject
						 * .getString("RoomCatgCode"));
						 */

						if (jPolicyObject.has("ExCancelDays"))
							policyData.setPolicyCancelDays(Integer
									.parseInt(jPolicyObject
											.getString("ExCancelDays")));

						if (jPolicyObject.has("ChargeType"))
							policyData.setChargeType(jPolicyObject
									.getString("ChargeType"));

						if (jPolicyObject.has("ChargeRate"))
							policyData.setChargeRate(jPolicyObject
									.getString("ChargeRate"));

						if (jPolicyObject.has("Description"))
							policyData.setDescription(jPolicyObject
									.getString("Description"));
						if (jPolicyObject.has("@attributes")) {
							policyData
									.setPolicyStartDate(jPolicyObject

									.getJSONObject("@attributes").getString(
											"FromDate"));
							policyData.setPolicyEndDate(jPolicyObject

							.getJSONObject("@attributes").getString("ToDate"));
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			TextView title = new TextView(this);
			title.setTextColor(Color.parseColor("#d16413"));
			title.setTypeface(Utils.getHelveticaNeue(HotelDetailsActivity.this));
			title.setText("Cancellation Policy");
			title.setPadding(10, 10, 10, 10);
			title.setGravity(Gravity.CENTER);
			title.setTextColor(Color.BLACK);
			title.setTextSize(20);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCustomTitle(title);
			String startDate, endDate;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-LLL-yyyy");
			String msg = "";

			Calendar calendar = Calendar.getInstance();
			Date date;

			for (CancelationPolicyData cancelationPolicyData : policyDataList) {
				Log.i(getClass().getSimpleName(),
						cancelationPolicyData.getPolicyStartDate());
				calendar.set(Integer.parseInt(cancelationPolicyData
						.getPolicyStartDate().split("-")[0]), Integer
						.parseInt(cancelationPolicyData.getPolicyStartDate()
								.split("-")[1]), Integer
						.parseInt(cancelationPolicyData.getPolicyStartDate()
								.split("-")[2]));
				date = calendar.getTime();

				startDate = sdf.format(date);
				Log.i(getClass().getSimpleName(), startDate);
				calendar.set(Integer.parseInt(cancelationPolicyData
						.getPolicyEndDate().split("-")[0]), Integer
						.parseInt(cancelationPolicyData.getPolicyEndDate()
								.split("-")[1]), Integer
						.parseInt(cancelationPolicyData.getPolicyEndDate()
								.split("-")[2]));
				date = calendar.getTime();
				endDate = sdf.format(date);
				if (cancelationPolicyData.getRoomCategory() != null)
					msg = msg + "<b>" + " &bull; ROOM CATEGORY : "
							+ cancelationPolicyData.getRoomCategory()
							+ "</b><br/>" + " &bull; <b>" + startDate + " to "
							+ endDate + "</b><br/>" + "You have to cancel "
							+ cancelationPolicyData.getPolicyCancelDays()
							+ " day(s) prior to the arrival date." + "<br/>";
				else
					msg = msg + "<b>" + " &bull;" + hotelData.getHotelName()
							+ "</b><br/>" + " &bull; <b>" + startDate + " to "
							+ endDate + "</b><br/>" + "You have to cancel "
							+ cancelationPolicyData.getPolicyCancelDays()
							+ " day(s) prior to the arrival date." + "<br/>";
				if (cancelationPolicyData.getChargeType().toLowerCase()
						.contains("percent")) {
					msg = msg
							+ "Otherwise "
							+ cancelationPolicyData.getChargeRate()
							+ " % cancellation charge for the entire stay will be applied."
							+ "<br/>";
				} else if (cancelationPolicyData.getChargeType().toLowerCase()
						.contains("amount")) {
					msg = msg
							+ "Otherwise "
							+ cancelationPolicyData.getCurrency()
							+ " "
							+ cancelationPolicyData.getChargeRate()
							+ " cancellation charge for the entire stay will be applied."
							+ "<br/>";
				} else if (cancelationPolicyData.getChargeType().toLowerCase()
						.contains("charge")) {
					msg = msg
							+ "Otherwise 100 % cancellation charge for the entire stay will be applied."
							+ "<br/>";
				} else if (cancelationPolicyData.getChargeType().toLowerCase()
						.contains("night")) {
					msg = msg
							+ "Otherwise "
							+ cancelationPolicyData.getChargeRate()
							+ " night/room cancellation charge stay will be applied."
							+ "<br/>";

				}

				msg = msg + "No show will be full charged." + "<br/>";

			}
			builder.setMessage(Html.fromHtml(msg));
			// builder.setMessage("This reservation is non refundable and can not be amended or cancelled. No refund will be made upon cancellation, late check-in, early check out or non arrival (no show). Any Extensions for the reservation require a new reservation.");
			if (policyDataList.isEmpty())
				builder.setMessage("No Cancellation Policy Found!");
			builder.setCancelable(false);

			builder.setNegativeButton("Close",
					new DialogInterface.OnClickListener() {
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

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mContext = null;
	}

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	@Override
	public void SearchHotelDataResult(String szResult) {
		if (progressSearchDialog != null)
			progressSearchDialog.dismiss();
		Float newPrice = new Float(0) ;
		if (szResult == null) {

			Toast.makeText(
					HotelDetailsActivity.this,
					"Network Error. Please check network connection and try again",
					Toast.LENGTH_LONG).show();
			return;
		}
		Log.i(getClass().getSimpleName(), "SearchHotelDataResult " + szResult);

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(szResult);
			JSONObject jhotelObject = jsonObject
					.getJSONObject("SearchHotel_Response");
			if (jhotelObject.optJSONArray("Hotel") != null) {
				jhotelObject = jhotelObject.getJSONArray("Hotel")
						.getJSONObject(0);
			} else if (jhotelObject.optJSONObject("Hotel") != null) {
				jhotelObject = jhotelObject.getJSONObject("Hotel");
			}
			if (jhotelObject.optJSONArray("RoomCateg") != null) {
				jhotelObject = jhotelObject.getJSONArray("RoomCateg")
						.getJSONObject(0);
			} else if (jhotelObject.optJSONObject("RoomCateg") != null) {
				jhotelObject = jhotelObject.getJSONObject("RoomCateg");
			}

			if (jhotelObject.getJSONObject("@attributes").getString("Name")
					.toLowerCase().equals(m_szRoomType.toLowerCase())) {
				newPrice = Float.parseFloat(jhotelObject.getJSONObject("@attributes").getString(
						"Price"));
			} else {
				JSONArray newJSONArray = jsonObject
						.getJSONObject("SearchHotel_Response")
						.getJSONObject("Hotel").getJSONArray("RoomCateg");
				for (int i = 1; i < newJSONArray.length(); i++) {

					jhotelObject = newJSONArray.getJSONObject(i);
					if (jhotelObject.getJSONObject("@attributes")
							.getString("Name").toLowerCase()
							.equals(m_szRoomType.toLowerCase())) {
						newPrice = Float.parseFloat(jhotelObject.getJSONObject("@attributes").getString(
								"Price"));
					}
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!newPrice.equals(price)) {
			Log.i(getClass().getSimpleName(), "Price Changed");
			TextView title = new TextView(this);
			title.setTextColor(Color.parseColor("#d16413"));
			title.setTypeface(Utils.getHelveticaNeue(HotelDetailsActivity.this));
			title.setText("Price Changed");
			title.setPadding(10, 10, 10, 10);
			title.setGravity(Gravity.CENTER);
			title.setTextColor(Color.BLACK);
			title.setTextSize(20);
            
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCustomTitle(title);
		
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent travellerDetailsIntent = new Intent(
									HotelDetailsActivity.this,
									TravellerDetailsTabActivity.class);

							String szRoomRate = String.format("%.2f", price
									* firstRoomRatePicker.getCurrent());
							szRoomRate = szRoomRate.replaceAll("\\.", "");
							szRoomRate = StringUtils.leftPad(szRoomRate, 12,
									"0");

							travellerDetailsIntent.putExtra("amount",
									String.valueOf(szRoomRate));
							travellerDetailsIntent.putExtra("currency",
									m_szCurrency);
							travellerDetailsIntent.putExtra("hotelid",
									m_szHotelID);
							travellerDetailsIntent.putExtra("posinlist", pos);
							travellerDetailsIntent.putExtra("roomcategpos", 0); // room
																				// categ
																				// is
																				// json
																				// object
							travellerDetailsIntent.putExtra("bftype",
									m_szBfType);
							travellerDetailsIntent.putExtra("roomtype",
									m_szRoomType);
							travellerDetailsIntent.putExtra("roomcode",
									m_szRoomCode);
							startActivity(travellerDetailsIntent);

						}

					});
			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();

						}

					});
			Spanned temp=Html.fromHtml("Price has been changed from  " + "<b>"
					+hotelData.getCurrency()+" "+"</b>"+"</b><font color=#f47c21><b>"+price
					+ " </b>" + "</font>"+"to "+ "<b>"
					+hotelData.getCurrency()+" "+"</b>"+"</b><font color=#f47c21><b>"+newPrice
					+ " </b>" + "</font>"+" Do you want to continue?");
			builder.setMessage(temp);
			AlertDialog alert = builder.show();
			TextView messageText = (TextView) alert
					.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER_VERTICAL);
			messageText.setTextColor(Color.BLACK);
			messageText.setTextSize(16);

		}
		else
		{
			Intent travellerDetailsIntent = new Intent(
					HotelDetailsActivity.this,
					TravellerDetailsTabActivity.class);

			String szRoomRate = String.format("%.2f", price
					* firstRoomRatePicker.getCurrent());
			szRoomRate = szRoomRate.replaceAll("\\.", "");
			szRoomRate = StringUtils.leftPad(szRoomRate, 12,
					"0");

			travellerDetailsIntent.putExtra("amount",
					String.valueOf(szRoomRate));
			travellerDetailsIntent.putExtra("currency",
					m_szCurrency);
			travellerDetailsIntent.putExtra("hotelid",
					m_szHotelID);
			travellerDetailsIntent.putExtra("posinlist", pos);
			travellerDetailsIntent.putExtra("roomcategpos", 0); // room
																// categ
																// is
																// json
																// object
			travellerDetailsIntent.putExtra("bftype",
					m_szBfType);
			travellerDetailsIntent.putExtra("roomtype",
					m_szRoomType);
			travellerDetailsIntent.putExtra("roomcode",
					m_szRoomCode);
			startActivity(travellerDetailsIntent);
		}

		// add 3 leading zeros in this number
		// String padded = String.format("%03d" , number);

	}

}
