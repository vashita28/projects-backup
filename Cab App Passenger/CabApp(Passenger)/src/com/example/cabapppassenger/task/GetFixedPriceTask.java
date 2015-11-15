package com.example.cabapppassenger.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.util.Constant;

public class GetFixedPriceTask extends AsyncTask<String, Void, String> {

	protected DefaultHttpClient client;
	HttpParams httpParams = new BasicHttpParams();
	String url, logInResponse, message;
	PreBookParcelable pbParcelable;
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	public InputStream mInputStream;
	Context mContext;
	ProgressDialog mDialog;
	double pickUpLatitude, pickUpLongitude, dropOffLatitude, dropOffLongitude;
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	Handler mHandler;
	DecimalFormatSymbols decimalFormatSymbols;
	DecimalFormat decimalFormat;

	public GetFixedPriceTask(Context context, String url,
			double pickUpLongitude, double pickUpLatitude,
			double dropOffLongitude, double dropOffLatitude,
			PreBookParcelable pbParcelable, Handler mHandler) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.pbParcelable = pbParcelable;
		this.url = url;
		this.pickUpLongitude = pickUpLongitude;
		this.pickUpLatitude = pickUpLatitude;
		this.dropOffLatitude = dropOffLatitude;
		this.dropOffLongitude = dropOffLongitude;
		this.mHandler = mHandler;
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setGroupingSeparator('.');
		decimalFormat = new DecimalFormat("0.00", decimalFormatSymbols);
		/*
		 * nameValuePairs.add(new BasicNameValuePair( "data[message]",
		 * message));
		 */

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
		nameValuePairs.add(new BasicNameValuePair("data[pickupData][latitude]",
				pickUpLatitude + ""));
		nameValuePairs.add(new BasicNameValuePair(
				"data[pickupData][longitude]", pickUpLongitude + ""));
		nameValuePairs.add(new BasicNameValuePair(
				"data[destinationData][latitude]", dropOffLatitude + ""));
		nameValuePairs.add(new BasicNameValuePair(
				"data[destinationData][longitude]", dropOffLongitude + ""));
		return POST();
	}

	public String POST() {

		String result = "";
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			try {

				HttpConnectionParams.setConnectionTimeout(httpParams, 10000); // timeout
				client = new DefaultHttpClient(httpParams);

				postRequest(url);
			} catch (Exception e) {
				if (mDialog != null) {
					mDialog.dismiss();
				}
				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(
								mContext,
								"Error in fetching fixed price, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				});
				// Log.d("InputStream", e.getLocalizedMessage());
			}

			Log.i(getClass().getSimpleName(), "" + logInResponse);

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
		return logInResponse;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		if (mDialog != null)
			mDialog.dismiss();
		if (null != result) {
			try {

				Log.e(getClass().getSimpleName(), result);
				/*
				 * Message msg = new Message(); Bundle bundle = new Bundle();
				 * bundle.putString("bookingsData", result); msg.obj = bundle;
				 * msg.setData(bundle);
				 */
				mHandler.sendEmptyMessage(0);

			} catch (Exception e1) {

				e1.printStackTrace();
				try {
					JSONObject jobj = new JSONObject(result);
					JSONArray jArray = jobj.getJSONArray("errors");
					JSONObject jObj = jArray.getJSONObject(0);
					String message = jObj.getString("message");
					if (message.matches("missing or invalid accessToken")) {
						Constant.logout(mContext, editor, true);
						Log.i("****Fixed Price Error****", "" + message);
					} else
						Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
								.show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public void executeRequest(HttpPost post, HttpGet get) {
		try {
			HttpResponse httpResponse;
			String fixedPrice;
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
					Toast.makeText(mContext,
							"Not able to find fixed price, Please try again.",
							Toast.LENGTH_SHORT).show();
					mInputStream = null;
					Toast.makeText(mContext,
							"Not able to find fixed price, Please try again.",
							Toast.LENGTH_SHORT).show();
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
						logInResponse = convertInputStreamToString(mInputStream)
								.toString();
						Log.e("Fixed Price response", logInResponse);

						Log.e(getClass().getSimpleName(), "JSONObject  LogIn"
								+ logInResponse);
						JSONObject joFixedPrice = new JSONObject();
						try {
							joFixedPrice = new JSONObject(logInResponse);
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (joFixedPrice.has("success"))
							try {
								if (joFixedPrice.getString("success").equals(
										"true")) {

									if (joFixedPrice.has("isFixedPrice")) {
										if (joFixedPrice.getString(
												"isFixedPrice").equals("true")) {
											pbParcelable.setIsFixedPrice(true);
											// fixedPrice = decimalFormat
											// .format(joFixedPrice
											// .getDouble("price"));
											pbParcelable
													.setFixedPriceCurrency(joFixedPrice
															.getString("currency"));

											pbParcelable
													.setFixedPrice(joFixedPrice
															.getDouble("price"));

										} else {
											pbParcelable.setIsFixedPrice(false);
										}
									}

								} else {
									Toast.makeText(
											mContext,
											"Not able to find fixed price, Please try again.",
											Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								Toast.makeText(
										mContext,
										"Not able to find fixed price, Please try again.",
										Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
						// pbParcelable.setIsFixedPrice()
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

	public void getRequest(String url) {
		// getRequest.addHeader("Content-Type",
		// "application/json; charset=utf-8"); // addHeader()
		this.url = url;
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
		// httpGet.addHeader("Content-Type", "application/json");
		executeRequest(null, httpGet);
	}

	public void postRequest(String url) {
		// postMethod.addHeader("Content-Type",
		// "application/json; charset=utf-8"); // addHeader()
		this.url = url;

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

		Log.e("FIXED PRICE REQUEST", nameValuePairs.toString());

		// postMethod.setEntity(new StringEntity(mainObject.toString(),
		// "utf-8"));
	}

	ProgressDialog showProgressDialog(String message) {
		ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.setMessage(message + "...");
		return dialog;
	}
}