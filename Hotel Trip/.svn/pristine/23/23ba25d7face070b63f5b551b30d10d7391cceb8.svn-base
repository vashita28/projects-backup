package com.hoteltrip.android.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.Const;

public class SignupTask extends AsyncTask<Void, String, String> {
	private String szURL = AppValues.ms_szURL + "dataget/createUser";
	Context mContext;
	Handler mHandler;

	public String szEmail, szPassword;

	public String szPrefix, szFirstName, szLastName, szPaxPassport, szAddress,
			szCountryCode, szCityCode, szPhone, szSubscription;

	public SignupTask(Context context, Handler handler) {
		// TODO Auto-generated constructor stub
		this.mHandler = handler;
		this.mContext = context;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		String response = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15000); // timeout

			HttpPost postMethod = new HttpPost(szURL);

			// postMethod.addHeader("Content-Type",
			// "application/json; charset=utf-8"); // addHeader()
			postMethod.addHeader("Content-Type",
					"application/x-www-form-urlencoded");

			// postMethod.setEntity(new StringEntity(mainObject.toString(),
			// "utf-8"));

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					11);
			// 1. prefix - One of the following values is accepted: Mr., Ms.,
			// Miss, Mrs.
			nameValuePairs.add(new BasicNameValuePair("prefix", szPrefix));
			// 2. fname - First Name of user. Max length 25 characters.
			nameValuePairs.add(new BasicNameValuePair("fname", szFirstName));
			// 3. lname - Last name of user. Max length 25 characters.
			nameValuePairs.add(new BasicNameValuePair("lname", szLastName));
			// 4. user_email - Email address of user. Max length 50 chars.
			nameValuePairs.add(new BasicNameValuePair("user_email", szEmail));
			// 5. user_pass - Password of the user. Max length 15 chars.
			nameValuePairs.add(new BasicNameValuePair("user_pass", szPassword));
			// 6. paxpassport - Country code of user's nationality from the
			// database you have. Max 15 chars.
			nameValuePairs.add(new BasicNameValuePair("paxpassport",
					szPaxPassport));
			// 7. user_address - Address of user. Max 100 chars. Optional field.
			nameValuePairs
					.add(new BasicNameValuePair("user_address", szAddress));
			// 8. country_code - Country code of user's nation from the database
			// you have.
			nameValuePairs.add(new BasicNameValuePair("country_code",
					szCountryCode));
			// 9. city_code - City code of user's city from the database you
			// have. Optional field.
			nameValuePairs.add(new BasicNameValuePair("city_code", szCityCode));
			// 10. user_phone - User's phone no. Max 15 chars. Optional field.
			nameValuePairs.add(new BasicNameValuePair("user_phone", szPhone));
			// 11. user_subscribe - Does user want to subscribe to Hotel Trip
			// email newsletter? Possible values: Y or N.
			nameValuePairs.add(new BasicNameValuePair("user_subscribe",
					szSubscription));

			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			postMethod.setParams(httpParams);

			HttpResponse httpResponse = client.execute(postMethod);
			response = inputStreamToString(
					httpResponse.getEntity().getContent()).toString();
		}

		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		try {
			JSONObject jsonObjectMain = new JSONObject(result);
			JSONObject jsonObject = jsonObjectMain
					.getJSONObject("SignUp_Response");
			if (jsonObject.has("SignStatus")
					&& jsonObject.getString("SignStatus").equals("Success")) {
				if (jsonObject.has("LoginInfo")) {
					jsonObject = jsonObject.getJSONObject("LoginInfo");

					AppValues.szAgentID = jsonObject.getString("AgentId");
					AppValues.szUserName = jsonObject.getString("Username");
					// AppValues.szPassword = jsonObject.getString("Password");
					AppValues.szPassword = szPassword;
					// "AgentId": "HTMB",
					// "Username": "KANAD63",
					// "Password": "kanad123"
				}
				if (mHandler != null)
					mHandler.sendEmptyMessage(Const.REGISTRATION_SUCCESS);

			} else if (jsonObject.has("SignStatus")
					&& jsonObject.getString("SignStatus")
							.equals("Unsuccessful")) {
				// jsonObject.getString("Error").equalsIgnoreCase(
				// "Your email is duplicate.");
				if (mHandler != null) {
					Message message = new Message();
					message.what = Const.USER_ALREADY_EXISTS;
					message.obj = jsonObject.getString("Error");
					mHandler.sendMessage(message);
				}
			} else if (jsonObject.has("EditStatus")
					&& jsonObject.getString("EditStatus")
							.equals("Unsuccessful")) {
				// jsonObject.getString("Error").equalsIgnoreCase(
				// "PaxPassport must required.");
				String szErrorMessage = "";
				if (jsonObject.getString("Error").equalsIgnoreCase(
						"PaxPassport must required."))
					szErrorMessage = "Current nationality not supported!";
				else
					szErrorMessage = jsonObject.getString("Error");

				if (mHandler != null) {
					Message message = new Message();
					message.what = Const.REGISTRATION_FAILED;
					message.obj = szErrorMessage;
					mHandler.sendMessage(message);
				}

			} else {
				if (mHandler != null) {
					Message message = new Message();
					message.what = Const.REGISTRATION_FAILED;
					message.obj = "Network Error. Please check network connection and try again";
					mHandler.sendMessage(message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (mHandler != null) {
				Message message = new Message();
				message.what = Const.REGISTRATION_FAILED;
				message.obj = "Network Error. Please check network connection and try again";
				mHandler.sendMessage(message);
			}
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
		Log.i("SignupTask", "inputStreamToString()-result  " + total);

		return total;
	}

}
