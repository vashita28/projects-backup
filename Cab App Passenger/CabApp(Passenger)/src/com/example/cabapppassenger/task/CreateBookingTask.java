/*
 * Coded By Manpreet
 */

package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.activity.MainFragmentActivity;
import com.example.cabapppassenger.fragments.FindTaxiFragment;
import com.example.cabapppassenger.fragments.MyBookingsFragment;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumPaymentType;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;

public class CreateBookingTask extends AsyncTask<String, Void, String> {

	protected DefaultHttpClient client;
	HttpParams httpParams = new BasicHttpParams();
	String url, accessToken;
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	public InputStream mInputStream;
	Context mContext;
	ProgressDialog mDialog;
	PreBookParcelable pbParcelable;
	String createBookingResponse = "", pickupDateTime, locationData;
	float fixedPrice;
	SharedPreferences shared_pref;
	Editor editor;
	String PREF_NAME = "CabApp_Passenger";
	Fragment fragment;
	String truncatedPan;
	boolean isFixedPrice = false;

	public CreateBookingTask(Fragment fragment, Context context,
			PreBookParcelable pbParcelable, String pickupDateTime,
			String locationData, float fixedPrice, String truncatedPan,
			String url, String accessToken) {
		// TODO Auto-generated constructor stub
		this.fragment = fragment;
		this.mContext = context;
		;
		this.pickupDateTime = pickupDateTime;
		this.locationData = locationData;
		this.fixedPrice = fixedPrice;
		this.pbParcelable = pbParcelable;
		this.url = url + accessToken;
		this.accessToken = accessToken;
		isFixedPrice = pbParcelable.isFixedPrice();
		this.truncatedPan = truncatedPan;
		Log.d(getClass().getSimpleName(),
				"constructor" + " paymentType "
						+ pbParcelable.getePaymentType().name()
						+ " isHearingImpairedUser "
						+ pbParcelable.isHearingImpairedSelected()
						+ " isWheelchairUser "
						+ pbParcelable.isWheelChairSelected() + "locationData"
						+ locationData + "numberOfPassengers"
						+ pbParcelable.getPassengerCount() + "pickupDateTime"
						+ pickupDateTime + " isFixedPrice " + isFixedPrice
						+ " comments " + pbParcelable.getComment() + " url "
						+ url);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mDialog = showProgressDialog("Loading");
		mDialog.show();

	}

	@Override
	protected String doInBackground(String... urls) {

		return POST();
	}

	public String POST() {
		try {
			String result = "";
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
				client = new DefaultHttpClient(httpParams);
				shared_pref = mContext.getSharedPreferences(PREF_NAME,
						Context.MODE_PRIVATE);
				editor = shared_pref.edit();

				if (shared_pref.getString("MobileNo", "") != null
						&& !shared_pref.getString("MobileNo", "").matches(
								"null")) {
					nameValuePairs.add(new BasicNameValuePair(
							"data[mobileNumber]", shared_pref.getString(
									"MobileNo", "")));

				} else {
					nameValuePairs.add(new BasicNameValuePair(
							"data[mobileNumber]", shared_pref.getString(
									"mobileNumber", "")));
				}

				if (pbParcelable.getePaymentType() == EnumPaymentType.BOTH) {
					nameValuePairs.add(new BasicNameValuePair(
							"data[paymentType]", "both"));
					nameValuePairs.add(new BasicNameValuePair(
							"data[truncatedPan]", truncatedPan.trim()));
				} else {
					nameValuePairs.add(new BasicNameValuePair(
							"data[paymentType]", "cash"));
				}
				Log.e("Truncated pan coming in", truncatedPan);
				nameValuePairs.add(new BasicNameValuePair(
						"data[pickupDateTime]", pickupDateTime));
				nameValuePairs.add(new BasicNameValuePair("data[locationData]",
						locationData));
				nameValuePairs.add(new BasicNameValuePair("data[cabShare]",
						"false"));

				nameValuePairs.add(new BasicNameValuePair("data[fixedPrice]",
						String.valueOf(isFixedPrice)));
				nameValuePairs.add(new BasicNameValuePair("data[note]",
						pbParcelable.getComment()));

				nameValuePairs.add(new BasicNameValuePair(
						"data[wheelchairAccessRequired]", String
								.valueOf(pbParcelable.isWheelChairSelected())));

				nameValuePairs.add(new BasicNameValuePair(
						"data[isHearingImpaired]", String.valueOf(pbParcelable
								.isHearingImpairedSelected())));

				postRequest(url);
				Log.e("Request of Create booking", "" + nameValuePairs);
				Log.i(getClass().getSimpleName(), "" + createBookingResponse);

			} else {
				if (mDialog != null)
					mDialog.dismiss();
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext, "No network connection",
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		} catch (Exception e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext,
							"Error in booking creation, Please try again.",
							Toast.LENGTH_SHORT).show();
				}
			});
			Log.i("Exception in Create Booking ", "" + e.getMessage());

		}
		return createBookingResponse;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (mDialog != null)
			mDialog.dismiss();
		if (!result.equals("")) {
			try {
				JSONObject joValidateBookingObject = new JSONObject(result);

				if (joValidateBookingObject != null
						&& joValidateBookingObject.getString("success").equals(
								"true")) {
					Log.i(getClass().getSimpleName(), "TRUE");
					String booking_id = joValidateBookingObject.getString("id");
					editor.putString("Booking Id", booking_id);
					editor.commit();

					Log.e("ASAP or PREBOOK", pickupDateTime);
					// SettingsTask task = new SettingsTask(mContext, fragment,
					// pbParcelable);

					if (pickupDateTime.matches("asap")) {

						// task.cab_when = "asap";
						// task.execute();
						// MainFragmentActivity.isVisible = false;
						FindTaxiFragment edit = new FindTaxiFragment(
								booking_id.trim());

						Bundle bundle = new Bundle();
						bundle.putParcelable("pbParcelable", pbParcelable);

						edit.setArguments(bundle);
						FragmentTransaction ft = fragment.getParentFragment()
								.getChildFragmentManager().beginTransaction();
						ft.replace(R.id.frame_container, edit,
								"findTaxiFragment");
						/*
						 * ((MainFragmentActivity) mContext)
						 * .refresh_menu_prebook();
						 */

						// ft.addToBackStack("bookingDetailsFragment");
						ft.commit();
					} else {
						// task.cab_when = "prebook";
						// task.execute();
						// MainFragmentActivity.bIsQuitDialogToBeShown = false;
						Toast.makeText(mContext,
								"Booking Created Successfully",
								Toast.LENGTH_SHORT).show();
						MyBookingsFragment edit = new MyBookingsFragment();
						FragmentTransaction ft = fragment.getParentFragment()
								.getChildFragmentManager().beginTransaction();
						ft.replace(R.id.frame_container, edit,
								"myBookingsFragment");
						// ft.addToBackStack("hailNowFragment");
						ft.commit();
						((MainFragmentActivity) mContext).refresh_menu(2);
					}

				} else {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("errors");

					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Cab booking Error****", "" + message);
					} else
						Toast.makeText(mContext, "" + message,
								Toast.LENGTH_SHORT).show();

				}

			} catch (Exception e1) {

				JSONObject jobj;
				try {
					jobj = new JSONObject(result);

					JSONArray jArray = jobj.getJSONArray("errors");

					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Create Booking Error****", "" + message);
					} else
						Toast.makeText(mContext, "" + message,
								Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
				e1.printStackTrace();
			}
		}
	}

	public void postRequest(String url) {
		// postMethod.addHeader("Content-Type",
		// "application/json; charset=utf-8"); // addHeader()
		// this.url = url;
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		// httpPost.addHeader("Content-Type", "application/json");
		try {
			if (nameValuePairs != null)
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		executeRequest(httpPost, null);

		// postMethod.setEntity(new StringEntity(mainObject.toString(),
		// "utf-8"));
	}

	public void executeRequest(HttpPost post, HttpGet get) {
		try {
			HttpResponse httpResponse;
			try {
				if (get == null)
					httpResponse = client.execute(post);
				else
					httpResponse = client.execute(get);

				Log.w(getClass().getSimpleName(), "executeRequest::URL:: "
						+ url);
				final int statusCode = httpResponse.getStatusLine()
						.getStatusCode();

				if (statusCode != HttpStatus.SC_OK) {
					Log.w(getClass().getSimpleName(), "Error " + statusCode
							+ " for URL " + url);
					mInputStream = null;
					return;
				}

				HttpEntity getResponseEntity = httpResponse.getEntity();
				mInputStream = getResponseEntity.getContent();
			} catch (ConnectTimeoutException e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mContext,
								"Connection timed out, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.i("Connection Time out in Create Booking ",
						"" + e.getMessage());
			}
			if (mInputStream != null) {
				try {
					{
						createBookingResponse = convertInputStreamToString(
								mInputStream).toString();

						try {
							JSONObject bookingResponse = new JSONObject(
									createBookingResponse);
							pbParcelable.setBookingId(bookingResponse
									.getString("id"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						Log.e("", "JSONObject  Create Booking"
								+ createBookingResponse);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.i("Exception in Create Booking ", "" + e.getMessage());
				}
			}

		} catch (IOException e) {
			if (post != null)
				post.abort();
			else
				get.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
			mInputStream = null;
		}
	}

	private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;
		inputStream.close();
		return result;
	}

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}
}
