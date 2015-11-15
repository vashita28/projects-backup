package uk.co.pocketapp.whotel.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.pocketapp.whotel.fragments.InsiderFragment;
import uk.co.pocketapp.whotel.util.CustomInsiderData;
import uk.co.pocketapp.whotel.util.Util;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class JsonParserTask extends AsyncTask<Void, Long, String> {
	// url to make request
	// private static String url =
	// "http://dev.pocketapp.co.uk/dev/whotel_wifi/administrator/api.php?q=get_insiders";

	// private static String url =
	// "http://dev.pocketapp.co.uk/dev/whotel_wifi/administrator/api.php?q=get_insiders_data";
	private static String url = Util.serverURL + "?q=get_insiders_data";

	// {
	// "rectlarge":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_1_rectlarge.jpg",
	// "page_url":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/read_content?id=1&table_type=hotel",
	// "h_contact": "404-876-6055",
	// "landscape":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_1_landscape.jpg",
	// "is_whotel": "YES",
	// "portrait":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_1_landscape.jpg",
	// "h_state": "Georgia",
	// "h_city": "Atlanta",
	// "h_desc":
	// "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknow",
	// "id": "1",
	// "h_long": "33.855021",
	// "h_country": "United States",
	// "h_category": "WATLANTA-DOWNTOWN",
	// "rectsmall":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_1_rectsmall.jpg",
	// "original":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_1_original.jpg",
	// "h_lati": "-84.364758",
	// "h_code": "GA 30326",
	// "h_address": "3400 Around Lenox Rd NE Atlanta, GA 30326",
	// "h_name": "Pocketapp"
	// }

	// {
	// "created_on" = "0000-00-00";
	// id = 4;
	// "in_address" = "1029 edgewood avenue ne atlanta ga. 30307";
	// "in_desc" = "$1oysters & $1 beers happenin' daily after 5 at the bar";
	// "in_lat" = "33.75727";
	// "in_long" = "-84.35428";
	// "in_name" = "One Eared Stag";
	// "in_priority" = 0;
	// "in_sequence" = 0;
	// "is_whotel" = "dev.pocketapp.co.uk/dev/whotel_wifi/administrator/";
	// landscape =
	// "dev.pocketapp.co.uk/dev/whotel_wifi/administrator/uploads/insiders/One_Eared_Stag_Oysters_375x175.jpg";
	// original =
	// "dev.pocketapp.co.uk/dev/whotel_wifi/administrator/uploads/insiders/One_Eared_Stag_Oysters_650x461.jpg";
	// "page_url" =
	// "dev.pocketapp.co.uk/dev/whotel_wifi/administrator/read_content?id=4&table_type=insiders";
	// portrait =
	// "dev.pocketapp.co.uk/dev/whotel_wifi/administrator/uploads/insiders/One_Eared_Stag_Oysters_310x425.jpg";
	// rectlarge =
	// "dev.pocketapp.co.uk/dev/whotel_wifi/administrator/uploads/insiders/One_Eared_Stag_Oysters_700x391.jpg";
	// rectsmall =
	// "dev.pocketapp.co.uk/dev/whotel_wifi/administrator/uploads/insiders/One_Eared_Stag_Oysters_650x461.jpg";
	// }

	// JSON Node names
	public final String ID = "id";
	public final String CREATEDON = "created_on";
	public final String ADDRESS = "in_address";// "h_address";
	public final String DESC = "in_desc";// "h_desc";
	public final String LATITUDE = "in_lat";// "h_lati";
	public final String LONGITUDE = "in_long";// "h_long";
	public final String NAME = "in_name";// "h_name";
	public final String PRIORITY = "in_priority";
	public final String SEQUENCE = "in_sequence";
	public final String IS_WHOTEL = "is_whotel";
	// public final String CITY = "h_city";
	// public final String STATE = "h_state";
	// public final String COUNTRY = "h_country";
	// public final String CODE = "h_code";
	// public final String CONTACT = "h_contact";
	// public final String CATEGORY = "h_category";

	public final String LANDSCAPE = "landscape";
	public final String ORIGINAL = "original";
	public final String PAGE_URL = "page_url";
	public final String PORTRAIT = "portrait";
	public final String RECTLARGE = "rectlarge";
	public final String RECTSMALL = "rectsmall";

	InsiderFragment insiderFragment;

	Context myContext;

	public ArrayList<CustomInsiderData> insiderDataList;

	public JsonParserTask(Context ctx, InsiderFragment insiderFragment) {
		this.myContext = ctx;
		this.insiderFragment = insiderFragment;
		insiderDataList = new ArrayList<CustomInsiderData>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... params) {
		// Hashmap for ListView:All objects data

		try {
			// StringBuilder response = new StringBuilder();
			String response = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // timeout
				// value
				HttpPost postMethod = new HttpPost(url);
				// List<NameValuePair> nameValuePairs = new
				// ArrayList<NameValuePair>(1);

				JSONObject objRequest = new JSONObject();
				objRequest.put("type", "insiders");
				if (Util.getLocation() != null) {
					objRequest.put("lat", Util.getLocation().getLatitude());
					objRequest.put("lng", Util.getLocation().getLongitude());
				}
				// nameValuePairs.add(new
				// BasicNameValuePair("type","hotspots"));

				postMethod.addHeader("Content-Type",
						"application/json; charset=utf-8"); // addHeader()
				postMethod.setEntity(new StringEntity(objRequest.toString(),
						"utf-8"));
				// request.setURI(new URI(url));
				// request.setParams(httpParams);
				HttpResponse httpResponse = client.execute(postMethod);
				response = inputStreamToString(
						httpResponse.getEntity().getContent()).toString();
				// Log.e("GetInsider", "response:: " + response);

				JSONArray json = new JSONArray(response);
				if (json != null && json.length() > 0) {

					if (json.getJSONObject(0).has("error_message")) {
						return json.getJSONObject(0).getString("error_message");
					}

					for (int i = 0; i < json.length(); i++) {
						JSONObject c = json.getJSONObject(i);

						CustomInsiderData insiderData = new CustomInsiderData();
						insiderData.setId(c.getString(ID));
						insiderData.setCreatedOn(c.getString(CREATEDON));
						insiderData.setInsiderAddress(c.getString(ADDRESS));
						insiderData.setInsiderDesc(c.getString(DESC));
						insiderData.setInsiderLati(c.getString(LATITUDE));
						insiderData.setInsiderLong(c.getString(LONGITUDE));
						insiderData.setInsiderName(c.getString(NAME));
						insiderData.setInsiderPriority(c.getString(PRIORITY));
						insiderData.setInsiderSequence(c.getString(SEQUENCE));
						insiderData.setInsiderIsWhotel(c.getString(IS_WHOTEL));
						insiderData.setInsiderLandscape(c.getString(LANDSCAPE));
						insiderData.setInsiderOriginal(c.getString(ORIGINAL));
						insiderData.setInsiderPageURL(c.getString(PAGE_URL));
						insiderData.setInsiderPortrait(c.getString(PORTRAIT));
						insiderData.setInsiderRectLarge(c.getString(RECTLARGE));
						insiderData.setInsiderRectsmall(c.getString(RECTSMALL));

						insiderDataList.add(insiderData);

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {

			Log.v("Exception ", "Exception:: " + e.toString());
		}

		return "success";
	}

	@Override
	protected void onPostExecute(String result) {
		// super.onPostExecute(o);
		if (result.equals("success")) {
			if (insiderDataList != null) {
				insiderFragment.onTaskCompleted(insiderDataList, true);
			} else {
				// Log.v("onPostExecute else", "null");
			}
		} else {
			Toast.makeText(myContext, result, Toast.LENGTH_LONG).show();
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
		return total;
	}
}
