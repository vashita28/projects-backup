package co.uk.android.lldc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Connection {

	protected DefaultHttpClient client;
	HttpParams httpParams = new BasicHttpParams();
	String url, szSocialHandle;
	public InputStream mInputStream;
	Context mContext;

	public Connection(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;

		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		HttpConnectionParams.setConnectionTimeout(httpParams,
				Constants.connection_timeout);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		HttpConnectionParams.setConnectionTimeout(httpParams,
				Constants.socket_timeout);
		client = new DefaultHttpClient(httpParams);

	}

	public void connect(int type, String url) {
		switch (type) {

		case Constants.GET_MEDIA_LIST:// 291436538
			String urlSocialMedia = "https://api.instagram.com/v1/tags/"
					+ szSocialHandle
					+ "/media/recent?client_id=df74c8bb778b4799aceaf17d684bd30b";

			// String urlSocialMedia =
			// "https://api.instagram.com/v1/locations/7111321/media/recent?client_id=df74c8bb778b4799aceaf17d684bd30b";
			getRequest(urlSocialMedia);
			break;

		case Constants.GET_MEDIA_NEXT_PAGE:
			getRequest(url);
			break;

		case Constants.GET_WIFI_LIST:
			String wifiURL = "http://prod.pocketapp.co.uk/lldc/index.php/api/getbasedata/wifiname";
			getRequest(wifiURL);
			break;

		}
	}

	public void getRequest(String url) {
		// getRequest.addHeader("Content-Type",
		// "application/json; charset=utf-8"); // addHeader()
		this.url = url;
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");
		// httpGet.addHeader("Content-Type", "application/json");
		executeRequest(httpGet);
	}

	String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}

	public void executeRequest(HttpGet get) {
		try {
			HttpResponse httpResponse;

			httpResponse = client.execute(get);

			if (Constants.isDebug)
				Log.v(getClass().getSimpleName(), "executeRequest::URL:: "
						+ url);

			final int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				if (Constants.isDebug)
					Log.v(getClass().getSimpleName(), "Error " + statusCode
							+ " for URL " + url);

				mInputStream = null;

				if (statusCode == 403) {// Log Out
				} else {
					showToastMessageForHTTPError("Error fetching data. Please try again");
				}
				return;
			}

			HttpEntity getResponseEntity = httpResponse.getEntity();
			mInputStream = getResponseEntity.getContent();

		} catch (IOException e) {
			get.abort();
			if (Constants.isDebug)
				Log.v(getClass().getSimpleName(), "Error for URL " + url, e);
			mInputStream = null;
		} catch (Exception e) {
			// TODO: handle exception
			mInputStream = null;
			showToastMessageForHTTPError("Error fetching data. Please try again");
		}
	}

	void showToastMessageForHTTPError(final String szMessage) {
		try {
			if (mContext != null)
				((Activity) mContext).runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, szMessage, Toast.LENGTH_LONG)
								.show();

					}
				});
		} catch (Exception e) {

		}
	}

	public String inputStreamToString(InputStream content) throws IOException {
		// TODO Auto-generated method stub
		String line = "";
		StringBuilder total = new StringBuilder();

		if (content != null) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					content, "UTF-8"));
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		}

		return total.toString();
	}

}
