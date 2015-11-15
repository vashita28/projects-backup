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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.Const;

public class LoginTask extends AsyncTask<Void, String, String> {

	private String szURL = AppValues.ms_szURL + "dataget/viewMemberInfo";

	Context mContext;
	Handler mHandler;

	public String szEmail, szPassword;

	public LoginTask(Context context, Handler handler) {
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

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("user_email", szEmail));
			nameValuePairs.add(new BasicNameValuePair("user_pass", szPassword));
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

		String szLoginInfo, szLoginStatus, szName, szEmail, szPaxPassport, szPrefix;
		try {
			JSONObject jsonObject = new JSONObject(result);
			jsonObject = jsonObject.getJSONObject("MemberInfo_Response");
			if (jsonObject.has("LoginInfo")) { // successful
				szLoginInfo = jsonObject.getString("LoginInfo");
				AppValues.szName = szName = jsonObject.getString("Name");
				szEmail = jsonObject.getString("Email");
				szPrefix = jsonObject.getString("Prefix");
				szPaxPassport = jsonObject.getString("PaxPassport");
				AppValues.szSurName = jsonObject.getString("Surname");
				if (!jsonObject.getString("Telephone").equals("{}"))
					AppValues.szContactPhone = jsonObject
							.getString("Telephone");
				AppValues.szPaxPassport = szPaxPassport;
				AppValues.szPrefix = szPrefix;

				if (jsonObject.has("LoginInfo")) {
					jsonObject = jsonObject.getJSONObject("LoginInfo");

					AppValues.szAgentID = jsonObject.getString("AgentId");
					AppValues.szUserName = jsonObject.getString("Username");
					AppValues.szPassword = szPassword;

					// "AgentId": "HTMB",
					// "Username": "KANAD63",
					// "Password": "kanad123"
				}

				if (mHandler != null) {
					Message message = new Message();
					message.what = Const.LOGIN_SUCCESS;
					Bundle bundle = new Bundle();
					bundle.putString("email", szEmail);
					bundle.putString("name", szName);
					message.obj = bundle;
					mHandler.sendMessage(message);
				}
			} else if (jsonObject.has("LoginStatus")) { // unsuccessful
				szLoginStatus = jsonObject.getString("LoginStatus");
				if (szLoginStatus != null
						&& szLoginStatus.equals("Unsuccessful")) {
					String szError = jsonObject.getString("Error");
					if (mHandler != null) {
						Message message = new Message();
						message.what = Const.LOGIN_FAILED;
						Bundle bundle = new Bundle();
						bundle.putString("error", szError);
						message.obj = bundle;
						mHandler.sendMessage(message);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

			if (mHandler != null) {
				Message message = new Message();
				message.what = Const.LOGIN_FAILED;
				Bundle bundle = new Bundle();
				bundle.putString("error",
						"Network Error. Please check network connection and try again");
				message.obj = bundle;
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
		Log.i("LoginTask", "inputStreamToString()-result  " + total);

		return total;
	}

}
