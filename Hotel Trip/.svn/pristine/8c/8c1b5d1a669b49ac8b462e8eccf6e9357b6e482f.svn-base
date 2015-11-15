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
import com.hoteltrip.android.util.Utils;

public class PaymentTask extends AsyncTask<Void, String, String> {
	private String szURL = AppValues.ms_szURL + "payht/payprocess";
	Context mContext;
	Handler mHandler;
	public String szXMLData;

	public PaymentTask(Context context, Handler handler) {
		// TODO Auto-generated constructor stub
		this.mHandler = handler;
		this.mContext = context;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		String szEncrypted = Utils.aesEncrypt(szXMLData, Utils.szAESKey);
		Log.e("******************", "***************" + szEncrypted);
		String szDecrypted = Utils.aesDecrypt(szEncrypted, Utils.szAESKey);
		Log.e("******************", "***************" + szDecrypted);

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

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("data", szEncrypted));

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
			JSONObject jsonObject = new JSONObject(result);
			String szFailReason;

			if (jsonObject.has("status")) {
				if (jsonObject.getString("status").equalsIgnoreCase("F")) {

					if (jsonObject.has("failReason")) {
						szFailReason = jsonObject.getString("failReason");
						Message message = new Message();
						message.what = Const.PAYMENT_FAILED;
						message.obj = szFailReason;

						if (mHandler != null)
							mHandler.sendMessage(message);
					}
				}

				if (jsonObject.getString("status").equalsIgnoreCase("A")) {
					GetPaymentGatewayTask task = new GetPaymentGatewayTask(
							mContext, mHandler);
					task.szPaymentResponse = result;
					task.execute();
					// if (mHandler != null)
					// mHandler.sendEmptyMessage(Const.PAYMENT_SUCCESS);
				}
			}
			// else {
			// if (mHandler != null)
			// mHandler.sendEmptyMessage(1);
			// }
		} catch (Exception e) {
			e.printStackTrace();

			Message message = new Message();
			message.what = Const.PAYMENT_FAILED;
			message.obj = "Network Error. Please check network connection and try again";

			if (mHandler != null)
				mHandler.sendMessage(message);
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
		Log.i("PaymentTask", "inputStreamToString()-result  " + total);

		return total;
	}
}
