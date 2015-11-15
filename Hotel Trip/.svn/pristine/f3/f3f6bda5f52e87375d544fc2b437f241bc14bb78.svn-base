package com.hoteltrip.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoteltrip.android.tasks.SearchHotelTask;
import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.CancelationPolicyData;
import com.hoteltrip.android.util.NumberPicker;
import com.hoteltrip.android.util.SearchHotelDataFetched;
import com.hoteltrip.android.util.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RoomDetailsActivity extends BaseActivity implements SearchHotelDataFetched {

	ImageView roomdesc_tb;
	TextView roomdesc_tv, tv_roomdesccontent, tv_totalprice, tv_perroomprice,
			pricettext, tvHotelRoomCategory, tvPromotionText;
	LinearLayout ll_promotion, ll_promotion_text;
	int i = 0;
	private int UPPER_LIMIT = 30;
	private int LOWER_LIMIT = 1;
	ImageView hotelimg;
	NumberPicker numberPicker;
	ImageView roomdesc_iv, cancellationpolicy_iv;
	ImageButton cancellationimg_btn, imgbtn_booknow;
	ProgressDialog progressCancellationDialog, progressSearchDialog;
	String szCurrency;
	Float price;
	String m_szHotelID;
	int posInList;
	int roomCategPosition;

	static Context mContext;
	String m_szBfType, m_szRoomType,m_szRoomCode, szInternalCode;
	JSONObject joRoomCatg;
	RelativeLayout rlCancelation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roomdetails);
		super.init("Room Details");
		btnMap.setVisibility(View.GONE);

		mContext = this;
		imgbtn_booknow = (ImageButton) findViewById(R.id.imgbtn_booknow);

		imgbtn_booknow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchHotelTask newSearchTask = new SearchHotelTask(
						RoomDetailsActivity.this, RoomDetailsActivity.this);
				newSearchTask.roomListDetails=AppValues.roomListDetails;
				newSearchTask.szCheckInDate=AppValues.szCheckInDate;
				newSearchTask.szDestCity=AppValues.szDestCity;
				newSearchTask.szDestCountry=AppValues.szDestCountry;
				newSearchTask.szHotelId=m_szHotelID;
				newSearchTask.szNoOfNights=String.valueOf(AppValues.nNumberOfNights);
				newSearchTask.szPaxPassport=AppValues.szPaxPassport;
			
				newSearchTask.execute();
				progressSearchDialog = new ProgressDialog(
						RoomDetailsActivity.this);
				progressSearchDialog.setMessage("Checking for price change, if any. Please Wait...");
				progressSearchDialog.setIndeterminate(true);
				progressSearchDialog.setCancelable(false);
				progressSearchDialog.show();

	/*			String szRoomRate = String.format("%.2f",
						price * numberPicker.getCurrent());
				szRoomRate = szRoomRate.replaceAll("\\.", "");
				szRoomRate = StringUtils.leftPad(szRoomRate, 12, "0");

				Intent travellerDetailsIntent = new Intent(
						RoomDetailsActivity.this,
						TravellerDetailsTabActivity.class);
				travellerDetailsIntent.putExtra("amount", szRoomRate);
				travellerDetailsIntent.putExtra("currency", szCurrency);
				travellerDetailsIntent.putExtra("hotelid", m_szHotelID);
				travellerDetailsIntent.putExtra("posinlist", posInList);
				travellerDetailsIntent.putExtra("roomcategpos",
						roomCategPosition);
				travellerDetailsIntent.putExtra("bftype", m_szBfType);
				travellerDetailsIntent.putExtra("roomtype", m_szRoomType);
				startActivity(travellerDetailsIntent);*/
			}
		});
		cancellationimg_btn = (ImageButton) findViewById(R.id.nextarrowtop_ib);
		tv_roomdesccontent = (TextView) findViewById(R.id.tv_roomdesccontent);
		tv_totalprice = (TextView) findViewById(R.id.tv_total_rate);
		hotelimg = (ImageView) findViewById(R.id.iv_hotel_image);
		pricettext = (TextView) findViewById(R.id.tv_hotel_price);
		tv_perroomprice = (TextView) findViewById(R.id.tv_room_rate);
		roomdesc_tv = (TextView) findViewById(R.id.tv_roomdesccontent);
		roomdesc_iv = (ImageView) findViewById(R.id.iv_roomdesc);
		numberPicker = (NumberPicker) findViewById(R.id.picker_room_type);
		roomdesc_tb = (ImageView) findViewById(R.id.togbtn_roomdescription);
		tvHotelRoomCategory = (TextView) findViewById(R.id.tvRoomCategoryName);
		ll_promotion = (LinearLayout) findViewById(R.id.ll_promotion);
		ll_promotion_text = (LinearLayout) findViewById(R.id.ll_promotion_text);
		tvPromotionText = (TextView) findViewById(R.id.tv_promotion_text);

		roomdesc_tb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				opendescriptiontext(v);
			}
		});
		// getting the data from hotel detail activity
		Bundle b = getIntent().getExtras();
		price = b.getFloat("price");
		szCurrency = b.getString("currency");
		String description = b.getString("description");
		String imageURL = b.getString("imageurl");
		m_szHotelID = b.getString("hotelid");
		posInList = b.getInt("position");
		roomCategPosition = b.getInt("roomcategpos");
		m_szBfType = b.getString("bftype");
		m_szRoomType = b.getString("roomtype");
		szInternalCode = b.getString("internalcode");
		m_szRoomCode=b.getString("roomCode");
		try {
			joRoomCatg = new JSONObject(b.getString("joRoomCatg"));
		} catch (JSONException e) {

			e.printStackTrace();
		}
		JSONObject joTemp = joRoomCatg;

		if (joTemp.optJSONObject("RoomType") != null) {
			try {
				joTemp = joTemp.getJSONObject("RoomType");
				if (joTemp.optJSONObject("Rate") != null)
					joTemp = joTemp.getJSONObject("Rate");
				else if (joTemp.optJSONArray("Rate") != null) {
					joTemp = joTemp.getJSONArray("Rate").getJSONObject(roomCategPosition);
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
		ll_promotion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (ll_promotion_text.getVisibility() == View.GONE) {
					ll_promotion_text.setVisibility(View.VISIBLE);
					toggleLayoutSelectedImage(ll_promotion, true);
				} else {
					ll_promotion_text.setVisibility(View.GONE);
					toggleLayoutSelectedImage(ll_promotion, false);
				}
			}
		});
		ll_promotion_text.setVisibility(View.GONE);
		tvHotelRoomCategory.setText(m_szRoomType);

		ImageLoader.getInstance().displayImage(imageURL, hotelimg);
		pricettext.setText(Html.fromHtml("<font size=20 color=#f47c21>"
				+ szCurrency + " " + "</font><b><font size=24 color=#f47c21>"
				+ price + " " + "</font></b><font size=18 color=#81837f>"
				+ " /night" + "</font>"));

		tv_perroomprice.setText(Html.fromHtml("<font size=18 color=#848484>"
				+ "at " + "</font>" + "<font size=20 color=#848484>"
				+ szCurrency + " " + "</font><b><font size=24 color=#848484>"
				+ price + " " + "</font></b><font size=18 color=#848484>"
				+ " /room" + "</font>"));

		if(description.equals("{}"))
			description="No Description Found";
		tv_roomdesccontent.setText(description);

		tv_totalprice.setText(szCurrency);
		numberPicker.setTextViewForUpdation(tv_totalprice, szCurrency, price);

		// full roomdesc imageview onclick listener
		roomdesc_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (i == 0) {
					roomdesc_tv.setVisibility(View.VISIBLE);
					roomdesc_tb.setImageDrawable(getResources().getDrawable(
							R.drawable.zoomoutbutton));
					i++;
				} else {
					roomdesc_tv.setVisibility(View.GONE);
					roomdesc_tb.setImageDrawable(getResources().getDrawable(
							R.drawable.zoominbutton));
					i = 0;
				}

			}
		});
		rlCancelation=(RelativeLayout) findViewById(R.id.rl_cancpolicy);
		rlCancelation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new ViewCancelPolicyTask().execute();
			}

		});

		numberPicker.setRange(LOWER_LIMIT, UPPER_LIMIT);
		numberPicker.setFragmentManager(getSupportFragmentManager());

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

						if (jPolicyArray.getJSONObject(i).has("RoomCatgCode"))
							policyData
									.setRoomCategory(m_szRoomType);

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
						if (jPolicyArray.getJSONObject(i).has("RoomCatgCode"))
							if (jPolicyArray.getJSONObject(i).getString("RoomCatgCode").equals(m_szRoomCode))
							policyDataList.add(policyData);
					}
				} else if (jsonObject.getJSONObject("Policies").optJSONObject(
						"Policy") != null) {
					JSONObject jPolicyObject = (jsonObject
							.getJSONObject("Policies")).getJSONObject("Policy");

					for (int i = 0; i < jPolicyObject.length(); i++) {
						CancelationPolicyData policyData = new CancelationPolicyData();

						if (jPolicyObject.has("RoomCatgCode"))
							policyData.setRoomCategory(jPolicyObject
									.getString("RoomCatgCode"));

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
						if (jPolicyObject.has("RoomCatgCode"))
							if (jPolicyObject.getString("RoomCatgCode").equals(m_szRoomCode))
						policyDataList.add(policyData);
					}
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			TextView title = new TextView(this);
			title.setTextColor(Color.parseColor("#d16413"));
			title.setTypeface(Utils.getHelveticaNeue(RoomDetailsActivity.this));
			title.setText("Cancellation Policy");
			title.setPadding(10, 10, 10, 10);
			title.setGravity(Gravity.CENTER);
			title.setTextColor(Color.BLACK);
			title.setTextSize(20);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCustomTitle(title);

			String msg = "";
			for (CancelationPolicyData cancelationPolicyData : policyDataList) {
				msg = msg + "<b>" + " &bull; ROOM CATEGORY : "
						+ cancelationPolicyData.getRoomCategory() + "</b><br/>"
						+ " &bull; <b>"
						+ cancelationPolicyData.getPolicyStartDate() + " to "
						+ cancelationPolicyData.getPolicyEndDate()
						+ "</b><br/>" + "You have to cancel "
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

	public void opendescriptiontext(View view) {

		if (i == 0) {
			// Visible the textview
			roomdesc_tv.setVisibility(View.VISIBLE);
			roomdesc_tb.setImageDrawable(getResources().getDrawable(
					R.drawable.zoomoutbutton));
			i++;

		} else {
			// Invisible the textview
			roomdesc_tv.setVisibility(View.GONE);
			roomdesc_tb.setImageDrawable(getResources().getDrawable(
					R.drawable.zoominbutton));
			i = 0;

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
			RoomDetailsActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					progressCancellationDialog = new ProgressDialog(
							RoomDetailsActivity.this);
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

	@Override
	public void SearchHotelDataResult(String szResult) {
		if (progressSearchDialog != null)
			progressSearchDialog.dismiss();
		Float newPrice = new Float(0) ;
		if (szResult == null) {

			Toast.makeText(
					RoomDetailsActivity.this,
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
			title.setTypeface(Utils.getHelveticaNeue(RoomDetailsActivity.this));
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
						
							String szRoomRate = String.format("%.2f",
									price * numberPicker.getCurrent());
							szRoomRate = szRoomRate.replaceAll("\\.", "");
							szRoomRate = StringUtils.leftPad(szRoomRate, 12, "0");

							Intent travellerDetailsIntent = new Intent(
									RoomDetailsActivity.this,
									TravellerDetailsTabActivity.class);
							travellerDetailsIntent.putExtra("amount", szRoomRate);
							travellerDetailsIntent.putExtra("currency", szCurrency);
							travellerDetailsIntent.putExtra("hotelid", m_szHotelID);
							travellerDetailsIntent.putExtra("posinlist", posInList);
							travellerDetailsIntent.putExtra("roomcategpos",
									roomCategPosition);
							travellerDetailsIntent.putExtra("bftype", m_szBfType);
							travellerDetailsIntent.putExtra("roomtype", m_szRoomType);
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
					+szCurrency+" "+"</b>"+"</b><font color=#f47c21><b>"+price
					+ " </b>" + "</font>"+"to "+ "<b>"
					+szCurrency+" "+"</b>"+"</b><font color=#f47c21><b>"+newPrice
					+ " </b>" + "</font>"+" Do you want to continue?");
			builder.setMessage(temp);
		//	builder.setMessage("Price has been changed from "+szCurrency+" "+price+" to "+szCurrency+" "+newPrice+". Do you want to continue?");
			AlertDialog alert = builder.show();
			TextView messageText = (TextView) alert
					.findViewById(android.R.id.message);
			messageText.setGravity(Gravity.CENTER_VERTICAL);
			messageText.setTextColor(Color.BLACK);
			messageText.setTextSize(16);

		}
			
		else
		{
			
			String szRoomRate = String.format("%.2f",
					price * numberPicker.getCurrent());
			szRoomRate = szRoomRate.replaceAll("\\.", "");
			szRoomRate = StringUtils.leftPad(szRoomRate, 12, "0");

			Intent travellerDetailsIntent = new Intent(
					RoomDetailsActivity.this,
					TravellerDetailsTabActivity.class);
			travellerDetailsIntent.putExtra("amount", szRoomRate);
			travellerDetailsIntent.putExtra("currency", szCurrency);
			travellerDetailsIntent.putExtra("hotelid", m_szHotelID);
			travellerDetailsIntent.putExtra("posinlist", posInList);
			travellerDetailsIntent.putExtra("roomcategpos",
					roomCategPosition);
			travellerDetailsIntent.putExtra("bftype", m_szBfType);
			travellerDetailsIntent.putExtra("roomtype", m_szRoomType);
			startActivity(travellerDetailsIntent);
		}
		// add 3 leading zeros in this number
		// String padded = String.format("%03d" , number);

	}

}
