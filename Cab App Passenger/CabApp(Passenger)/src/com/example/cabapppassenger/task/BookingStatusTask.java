package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;

public class BookingStatusTask extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	public Handler mHandler;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	String bookingId, mobile_no, payment_type, pickupdate_time,
			pickup_address1, pickup_address2, drop_latitude, drop_longitude,
			drop_address1, drop_address2, note, cabshare, currentStatus,
			driver_firstname, driver_lastname, driver_badgenumber,
			driver_mobileNumber, driver_internationalcode, driver_latitude,
			driver_longitude, vehicle_registration, vehicle_make,
			vehicle_model, vehicle_color, cabpin, driver_id;
	Double pickup_latitude, pickup_longitude;

	public BookingStatusTask(Context context, String bookingId) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.bookingId = bookingId;
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

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}

	@Override
	protected String doInBackground(String... url) {
		String result = "";
		InputStream inputStream = null;
		try {
			if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
				String address = Constant.passengerURL
						+ "ws/v2/passenger/bookings/" + bookingId
						+ "/?accessToken="
						+ shared_pref.getString("AccessToken", "");
				Log.e("Recent Request", address);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
				HttpGet httpGet = new HttpGet(address);
				HttpClient client = new DefaultHttpClient(httpParams);

				HttpResponse httpResponse = client.execute(httpGet);
				inputStream = httpResponse.getEntity().getContent();
				if (inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";
			} else {
				Toast.makeText(mContext, "No network connection",
						Toast.LENGTH_SHORT).show();
				if (mDialog != null)
					mDialog.dismiss();
			}

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
			Log.i("Connection Time out in Booking Status Task",
					"" + e.getMessage());
		} catch (Exception e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext,
							"Unable to get booking data, Please try again.",

							Toast.LENGTH_SHORT).show();
				}
			});
			Log.i("Exception in Booking Status Task", "" + e.getMessage());
		}

		Log.i("****RESULT****", "" + result);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (mDialog != null)
			mDialog.dismiss();
		JSONObject jsonObject, Jobj_droplocation, Jobj_pickuplocation, Jobj_status, Jobj_driver, Jobj_vehicle, Jobj_location;

		try {
			if (!result.equals("")) {
				jsonObject = new JSONObject(result);
				bookingId = jsonObject.getString("id");
				mobile_no = jsonObject.getString("mobileNumber");
				payment_type = jsonObject.getString("paymentType");
				pickupdate_time = jsonObject.getString("pickupDateTime");
				note = jsonObject.getString("note");
				cabshare = jsonObject.getString("cabShare");
				cabpin = jsonObject.getString("cabpin");
				Jobj_pickuplocation = jsonObject
						.getJSONObject("pickupLocation");
				pickup_latitude = Jobj_pickuplocation.getDouble("latitude");
				pickup_longitude = Jobj_pickuplocation.getDouble("longitude");
				pickup_address1 = Jobj_pickuplocation.getString("addressLine1");
				pickup_address2 = Jobj_pickuplocation.getString("addressLine2");

				// droplocation
				Jobj_droplocation = jsonObject.getJSONObject("dropLocation");
				drop_latitude = Jobj_droplocation.getString("latitude");
				drop_longitude = Jobj_droplocation.getString("longitude");
				drop_address1 = Jobj_droplocation.getString("addressLine1");
				drop_address2 = Jobj_droplocation.getString("addressLine2");

				// status object
				Jobj_status = jsonObject.getJSONObject("status");
				currentStatus = Jobj_status.getString("currentStatus");

				// driver object
				Jobj_driver = Jobj_status.getJSONObject("driver");
				driver_firstname = Jobj_driver.getString("firstname");
				driver_id = Jobj_driver.getString("driverId");
				driver_lastname = Jobj_driver.getString("surname");
				driver_badgenumber = Jobj_driver.getString("badgeNumber");
				driver_mobileNumber = Jobj_driver.getString("mobileNumber");
				driver_internationalcode = Jobj_driver
						.getString("internationalCode");

				// vehicle
				Jobj_vehicle = Jobj_driver.getJSONObject("vehicle");
				vehicle_registration = Jobj_vehicle.getString("registration");
				vehicle_make = Jobj_vehicle.getString("make");
				vehicle_model = Jobj_vehicle.getString("model");
				vehicle_color = Jobj_vehicle.getString("colour");

				// vehicle
				Jobj_location = Jobj_driver.getJSONObject("location");
				driver_latitude = Jobj_location.getString("latitude");
				driver_longitude = Jobj_location.getString("longitude");

				if (mHandler != null)
					passvalues_bokingdetails(bookingId, pickupdate_time,
							pickup_address1, pickup_address2, drop_address1,
							drop_address2, driver_firstname, driver_lastname,
							driver_badgenumber, driver_mobileNumber,
							driver_internationalcode, vehicle_model,
							vehicle_registration, cabpin, pickup_latitude,
							pickup_longitude, driver_id, vehicle_color);

			}

		} catch (Exception e) {
			try {
				if (result != null) {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("errors");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Booking Error****", "" + message);
					} else
						Toast.makeText(mContext, "" + message,
								Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				Log.i("Exception in Booking Status Task Post Execute",
						"" + e.getMessage());
			}

			Log.i("Exception in Booking Status Task Post Execute",
					"" + e.getMessage());
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

	public void passvalues_bokingdetails(String id, String pickupdate_time,
			String pickup_address1, String pickup_address2,
			String drop_address1, String drop_address2,
			String driver_firstname, String driver_lastname,
			String driver_badgenumber, String driver_mobileNumber,
			String driver_internationalcode, String vehicle_model,
			String vehicle_registration, String cabpin, Double pickup_latitude,
			Double pickup_longitude, String driver_id, String vehicle_color) {

		Bundle bundle = new Bundle();
		bundle.putString("Booking Id", id);
		bundle.putString("CabPin", cabpin);
		bundle.putString("Pickupdate_time", pickupdate_time);
		bundle.putString("Pickup_addr1", pickup_address1);
		bundle.putString("Pickup_addr2", pickup_address2);
		bundle.putString("Drop_addr1", drop_address1);
		bundle.putString("Drop_addr2", drop_address2);
		bundle.putString("Driver_firstname", driver_firstname);
		bundle.putString("Driver_lastname", driver_lastname);
		bundle.putString("Driver_badgenumber", driver_badgenumber);
		bundle.putString("Driver_mobileno", driver_mobileNumber);
		bundle.putString("Driver_internationalno", driver_internationalcode);
		bundle.putString("Vehicle_model", vehicle_model);
		bundle.putString("Vehicle_registration", vehicle_registration);
		bundle.putDouble("Pickup_latitude", pickup_latitude);
		bundle.putDouble("Pickup_longitude", pickup_longitude);
		bundle.putString("DriverId", driver_id);
		bundle.putString("Vehicle_Color", vehicle_color);

		Message msg = new Message();
		msg.obj = bundle;
		msg.setData(bundle);
		mHandler.sendMessage(msg);

	}
}
