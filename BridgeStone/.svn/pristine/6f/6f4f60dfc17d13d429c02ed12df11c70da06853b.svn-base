package co.uk.pocketapp.bridgestone.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import co.uk.pocketapp.bridgestone.util.Util;

public class UploadSensorValuesTask extends AsyncTask<Void, String, String> {
	// public ProgressBar progressBar;

	// TEST Environment:
	// String szURL =
	// "http://test-autot2i.bridgestone.eu/T2i_AutoPressure_1000/Service.asmx/ProcessReadings";

	// PRODUCTION Environment:
	String szURL = "http://autot2i.bridgestone.eu/t2i_autopressure_1000/service.asmx/ProcessReadings";

	// readings=ITC001,V3.2,B,150\n
	// M,589,295,0,0\n
	// 1,897,295,10,702\n
	// 2,895,295,10,446\n
	// 3,897,295,10,724\n
	// SENSORS\n
	// 0,1,000224,FD7,C4,3\n
	// 0,1,111189,1F4,B4,3\n

	public HashMap<String, String> mapSensorValues = new HashMap<String, String>();
	Handler mHandler;
	String readings = "";
	Context mContext;

	public UploadSensorValuesTask(Context context, Handler handler) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mHandler = handler;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		try {
			String firstLine = "%s,%s,B,150";

			readings = String.format(firstLine, Util.getUserID(mContext),
					"v0.1");
			readings = readings + "\n";

			// Line2:- M,000,000,0,0 - is hardcoded
			readings = readings + "M,000,000,0,0" + "\n";

			readings = readings + "SENSORS\n";

			String sensorValuesLine = "%s,%s,%s,%s,%s,%s\n";

			String header, fleetID, transmitterID, pressure, temperature, batteryVoltage;
			Iterator it = mapSensorValues.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				String sensorValue = pairs.getValue().toString();

				header = sensorValue.substring(1, 2);
				fleetID = sensorValue.substring(2, 3);
				transmitterID = sensorValue.substring(3, 9);
				pressure = sensorValue.substring(9, 12);
				temperature = sensorValue.substring(12, 14);
				batteryVoltage = sensorValue.substring(14, 15);

				readings = readings
						+ String.format(sensorValuesLine, header, fleetID,
								transmitterID, pressure, temperature,
								batteryVoltage);

			}
			// publishProgress("" + (int) ((40 * 100) / 100));
			Log.e("UploadSensorValues", "::Readings:: " + readings);
		} catch (Exception e) {

		}

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
			nameValuePairs.add(new BasicNameValuePair("readings", readings));
			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = client.execute(postMethod);
			response = inputStreamToString(
					httpResponse.getEntity().getContent()).toString();
		} catch (Exception e) {

		}

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result != null && result.contains("true")) {
			mHandler.sendEmptyMessage(0);
		} else {
			mHandler.sendEmptyMessage(1);
		}
	}

	/**
	 * Updating progress bar
	 * */
	protected void onProgressUpdate(String... progress) {
		// setting progress percentage
		// progressBar.setProgress(Integer.parseInt(progress[0]));
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
