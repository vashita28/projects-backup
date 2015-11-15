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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;

public class ValidateBookingTask extends AsyncTask<String, Void, String> {

	protected DefaultHttpClient client;
	HttpParams httpParams = new BasicHttpParams();
	String url, accessToken;
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	public InputStream mInputStream;
	Context mContext;
	ProgressDialog mDialog;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	double pickUpLatitude, pickUpLongitude, dropOffLatitude, dropOffLongitude;
	float fixedPrice = 0;
	Fragment fragment;
	String validatebooking_response = "", uniqueId, pickUpDateTime,
			passengerCount;
	String truncatedPan;
	PreBookParcelable pbParcelable;

	public ValidateBookingTask(Fragment fragment, Context context,
			double pickUpLatitude, double pickUpLongitude,
			double dropOffLatitude, double dropOffLongitude,
			PreBookParcelable pbParcelable, String passengerCount,
			String pickUpDateTime, String truncatedPan, String url,
			String accessToken) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.fragment = fragment;
		this.pickUpLatitude = pickUpLatitude;
		this.pickUpLongitude = pickUpLongitude;
		this.dropOffLatitude = dropOffLatitude;
		this.dropOffLongitude = dropOffLongitude;
		this.pbParcelable = pbParcelable;
		this.pickUpDateTime = pickUpDateTime;
		this.accessToken = accessToken;
		this.url = url + accessToken;
		this.passengerCount = passengerCount;
		this.truncatedPan = truncatedPan;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
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

				if (passengerCount.equals("1-4")) {
					passengerCount = "4";
					nameValuePairs.add(new BasicNameValuePair(
							"data[numberOfPassengers]", passengerCount));
				} else
					nameValuePairs.add(new BasicNameValuePair(
							"data[numberOfPassengers]", passengerCount));

				nameValuePairs.add(new BasicNameValuePair(
						"data[pickupData][type]", "map"));
				nameValuePairs.add(new BasicNameValuePair(
						"data[pickupData][latitude]", String
								.valueOf(pickUpLatitude)));
				nameValuePairs.add(new BasicNameValuePair(
						"data[pickupData][longitude]", String
								.valueOf(pickUpLongitude)));
				if (dropOffLongitude == 0)
					nameValuePairs.add(new BasicNameValuePair(
							"data[destinationData][type]", "as directed"));
				else
					nameValuePairs.add(new BasicNameValuePair(
							"data[destinationData][type]", "map"));
				nameValuePairs.add(new BasicNameValuePair(
						"data[destinationData][latitude]", String
								.valueOf(dropOffLatitude)));
				nameValuePairs.add(new BasicNameValuePair(
						"data[destinationData][longitude]", String
								.valueOf(dropOffLongitude)));

				Log.i(getClass().getSimpleName(), " numberOfPassengers "
						+ pbParcelable.getPassengerCount() + " pickUpLatitude "
						+ pickUpLatitude + " pickUpLongitude "
						+ pickUpLongitude + " dropOffLatitude "
						+ dropOffLatitude + " dropOffLongitude "
						+ dropOffLongitude + " url " + url);
				postRequest(url);

				Log.i("****RESULT****", "" + validatebooking_response);

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
			Log.i("Exception in Validate Booking ", "" + e.getMessage());
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext,
							"Error in booking, Please try again.",
							Toast.LENGTH_SHORT).show();
				}
			});
		}
		return validatebooking_response;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (mDialog != null)
			mDialog.dismiss();
		if (null != result) {
			try {
				JSONObject joValidateBookingObject = new JSONObject(result);

				if (joValidateBookingObject != null
						&& joValidateBookingObject.getString("success").equals(
								"true")) {

					Log.i(getClass().getSimpleName(),
							joValidateBookingObject.getString("uniqueId"));
					uniqueId = joValidateBookingObject.getString("uniqueId");
					if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
						Log.i(getClass().getSimpleName(), " uniqueId "
								+ uniqueId);
						url = Constant.passengerURL
								+ "ws/v2/passenger/bookings/?accessToken=";
						CreateBookingTask createBookingTask = new CreateBookingTask(
								fragment, mContext, pbParcelable,
								pickUpDateTime, uniqueId, fixedPrice,
								truncatedPan, url, accessToken);
						createBookingTask.execute();
					} else {
						Toast.makeText(mContext, "No network connection.",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					try {
						JSONObject jobj = new JSONObject(result);
						JSONArray jArray = jobj.getJSONArray("errors");

						JSONObject jObj = jArray.getJSONObject(0);
						String message = jObj.getString("message");
						Log.i(getClass().getSimpleName(), "" + message);
						Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
								.show();
					} catch (Exception e) {
						Toast.makeText(mContext,
								"Error booking a cab, Please try again.",
								Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}

				}

			} catch (Exception e1) {

				e1.printStackTrace();
				try {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("errors");
					for (int i = 0; i <= jArray.length(); i++) {
						JSONObject jObj = jArray.getJSONObject(i);
						String message = jObj.getString("message");
						if (message.matches("missing or invalid accessToken")) {
							Constant.logout(mContext, editor, true);
							Log.i("****Validate Booking Error****", ""
									+ message);
						} else
							Toast.makeText(mContext, "" + message,
									Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			}
			if (mInputStream != null) {
				try {
					{
						validatebooking_response = convertInputStreamToString(
								mInputStream).toString();

						Log.e(getClass().getSimpleName(), "JSONObject  LogIn"
								+ validatebooking_response);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
