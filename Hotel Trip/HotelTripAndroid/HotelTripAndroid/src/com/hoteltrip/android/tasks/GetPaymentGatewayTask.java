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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.Const;

public class GetPaymentGatewayTask extends AsyncTask<Void, String, String> {

	private String szURL = AppValues.ms_szURL + "dataget/getPaymentGateway";

	Context mContext;
	Handler mHandler;

	public String szPaymentResponse = "";

	String szCharge, szCurrency, szBankRef, szBankRespCode, szBankAuthCode;

	public GetPaymentGatewayTask(Context context, Handler handler) {
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

			JSONObject jPaymentResponseObject = new JSONObject(
					szPaymentResponse);
			try {
				szCharge = jPaymentResponseObject.getString("amt");
				szCurrency = "764";
				szBankRef = jPaymentResponseObject.getString("tranRef");
				szBankRespCode = jPaymentResponseObject.getString("respCode");
				szBankAuthCode = jPaymentResponseObject
						.getString("refNumber");
			} catch (JSONException e) {

			} catch (Exception e) {

			}

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("charge", szCharge));
			nameValuePairs.add(new BasicNameValuePair("currency", szCurrency));
			nameValuePairs.add(new BasicNameValuePair("bankref", szBankRef));
			nameValuePairs.add(new BasicNameValuePair("bankrespcode",
					szBankRespCode));
			nameValuePairs.add(new BasicNameValuePair("bankauthcode",
					szBankAuthCode));

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
			JSONObject jPaymentGatewayResultObject = new JSONObject(result);

			if (jPaymentGatewayResultObject.has("PaymentGateway_Response")) {
				jPaymentGatewayResultObject = jPaymentGatewayResultObject
						.getJSONObject("PaymentGateway_Response");

				if (jPaymentGatewayResultObject.has("Error")) {
					if (mHandler != null) {
						// Message message = new Message();
						// message.what = Const.BOOKING_FAILED;
						// message.obj = jPaymentGatewayResultObject
						// .getJSONObject("Error").getString(
						// "ErrorDescription");
						// mHandler.sendMessage(message);
					}
				} else {
					if (jPaymentGatewayResultObject.has("Status")
							&& jPaymentGatewayResultObject.getString("Status")
									.equals("COMPLETED")) {
						String paymentCode = jPaymentGatewayResultObject
								.getString("PaymetnCode");
						if (mHandler != null) {
							Message message = new Message();
							message.what = Const.PAYMENT_SUCCESS;
							message.obj = paymentCode;
							mHandler.sendMessage(message);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

			if (mHandler != null) {
				Message message = new Message();
				message.what = Const.BOOKING_FAILED;
				message.obj = "Please click on Pay Now to try Booking again OR contact Hotel Trip Hotline number 9009009009 for further assitance!";
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
		Log.i("GetPaymentGatewayTask", "inputStreamToString()-result  " + total);

		return total;
	}

}
