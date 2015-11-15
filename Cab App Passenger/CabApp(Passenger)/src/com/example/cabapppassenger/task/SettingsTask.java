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
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;

public class SettingsTask extends AsyncTask<String, Void, String> {

	Context mContext;
	ProgressDialog mDialog;
	Editor editor;
	SharedPreferences shared_pref;
	Parcelable pbParcelable;
	String PREF_NAME = "CabApp_Passenger";

	public SettingsTask(Context context, Parcelable parceable) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		this.pbParcelable = parceable;

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		mDialog = showProgressDialog("Loading");
		if (mDialog != null)
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
				// String list =
				// "http://cabapp.dev.pocketapp.co.uk/ws/v2/passenger/settings/?accessToken="
				// + shared_pref.getString("AccessToken", "");
				String list = Constant.passengerURL
						+ "ws/v2/passenger/settings/?accessToken="
						+ shared_pref.getString("AccessToken", "");
				Log.e("Setting Request", list);
				HttpGet httpGet = new HttpGet(list);
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
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
			Log.i("Exception in Settings ", "" + e.getMessage());
		} catch (Exception e) {
			if (mDialog != null) {
				mDialog.dismiss();
			}

			Log.i("Exception in Settings ", e.getMessage());
		}

		Log.i("****RESULT****", "" + result);
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		try {
			if (mDialog != null)
				mDialog.dismiss();
			JSONObject jsonObject = new JSONObject(result);
			if (!result.equals("")) {
				Log.i("Json Result", "" + jsonObject.toString());
				if (jsonObject != null) {
					long incremental_value = jsonObject
							.getLong("radiusIncrementInterval");
					long dropOffAddressTimeIntervalInMinutes = jsonObject
							.getLong("dropOffAddressTimeIntervalInMinutes");
					String clientKey = jsonObject.getString("clientkey");
					long cancellation_timeout = jsonObject
							.getLong("cancelBookingAfterHailInMinutes");

					long driverTrackingUpdateFrequency = jsonObject
							.getLong("driverTrackingUpdateFrequency");

					editor.putLong("IncrementalValue", incremental_value);
					editor.putLong("DriverUpdate",
							driverTrackingUpdateFrequency);
					editor.putString("ClientKey", clientKey);
					editor.putLong("cancelBookingAfterHailInMinutes",
							cancellation_timeout);
					editor.putLong("DropOffAddress_Time",
							dropOffAddressTimeIntervalInMinutes);
					editor.commit();
					// Toast.makeText(mContext, "Booking Created Successfully",
					// Toast.LENGTH_SHORT).show();
					// if (cab_when.equals("asap")) {
					//
					// FindTaxiFragment edit = new FindTaxiFragment();
					// Bundle bundle = new Bundle();
					// bundle.putParcelable("pbParcelable", pbParcelable);
					//
					// edit.setArguments(bundle);
					// FragmentTransaction ft = fragment.getParentFragment()
					// .getChildFragmentManager().beginTransaction();
					// ft.replace(R.id.frame_container, edit,
					// "findTaxiFragment");
					//
					// ft.commit();
					//
					// } else if (cab_when.equals("prebook")) {
					//
					// MyBookingsFragment edit = new MyBookingsFragment();
					// FragmentTransaction ft = fragment.getParentFragment()
					// .getChildFragmentManager().beginTransaction();
					// ft.replace(R.id.frame_container, edit,
					// "myBookingsFragment");
					// ft.commit();
					// ((MainFragmentActivity) mContext).refresh_menu(2);
					// }
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				if (!result.equals("")) {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("Error");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("type");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Settings Error****", "" + message);
					} else
						Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
								.show();
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				Log.i("Error", e1.getMessage().toString());
			}

			Log.i("Exception in Settings ", "" + e.getMessage());
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
}
