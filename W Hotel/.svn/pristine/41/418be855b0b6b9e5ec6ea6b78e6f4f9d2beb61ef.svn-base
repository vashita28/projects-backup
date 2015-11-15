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

import uk.co.pocketapp.whotel.fragments.HotelFragment;
import uk.co.pocketapp.whotel.util.CustomHotelData;
import uk.co.pocketapp.whotel.util.Util;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class JsonParserTaskGetHotel extends AsyncTask<Void, Long, String> {

	// private static String url =
	// "http://dev.pocketapp.co.uk/dev/whotel_wifi/administrator/api.php?q=get_hotels";
	private static String url = Util.serverURL + "?q=get_hotels";

	// {
	// "rectlarge":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_10_rectlarge.jpg",
	// "page_url":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/read_content?id=10&table_type=hotel",
	// "h_contact": "404-525-4400",
	// "landscape":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_10_landscape.jpg",
	// "is_whotel": "YES",
	// "portrait":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_10_landscape.jpg",
	// "h_state": "Georgia",
	// "h_city": "Atlanta",
	// "h_desc":
	// "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknow",
	// "id": "10",
	// "h_long": "-84.387041",
	// "h_country": "United States",
	// "h_category": "WATLANTA-BUCKHEAD",
	// "rectsmall":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_10_rectsmall.jpg",
	// "original":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_10_original.jpg",
	// "h_lati": "33.763879",
	// "h_code": "GA 30308",
	// "h_address": "320 Peachtree St NE Atlanta, GA 30308",
	// "h_name": "Max Lager's Grill"
	// }

	public final String ID = "id";
	public final String Hotel_Name = "h_name";
	public final String Hotel_Address = "h_address";
	public final String Hotel_City = "h_city";
	public final String Hotel_State = "h_state";
	public final String Hotel_Country = "h_country";
	public final String Hotel_Code = "h_code";
	public final String Hotel_Contact = "h_contact";
	public final String Hotel_Category = "h_category";
	public final String Hotel_Desc = "h_desc";
	public final String Hotel_longititude = "h_long";
	public final String Hotel_latitude = "h_lati";
	public final String Hotel_original = "original";
	public final String Hotel_landscape = "landscape";
	public final String Hotel_portrait = "portrait";
	public final String Hotel_rectsmall = "rectsmall";
	public final String Hotel_rectlarge = "rectlarge";
	public final String Hotel_Whotel = "is_whotel";
	public final String Hotel_url = "page_url";

	HotelFragment htFragment;
	Context myContext;
	public ArrayList<CustomHotelData> HotelDataList;

	public JsonParserTaskGetHotel(Context ctx, HotelFragment hotelFragment) {
		this.myContext = ctx;
		this.htFragment = hotelFragment;
		HotelDataList = new ArrayList<CustomHotelData>();
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

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
				objRequest.put("type", "hotel");
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

				// Log.e("GetHotel", "response:: " + response);

				JSONArray json = new JSONArray(response);
				if (json != null && json.length() > 0) {
					if (json.getJSONObject(0).has("error_message")) {
						return json.getJSONObject(0).getString("error_message");
					}

					for (int i = 0; i < json.length(); i++) {
						JSONObject c = json.getJSONObject(i);
						CustomHotelData hoteldata = new CustomHotelData();
						hoteldata.setId(c.getString(ID));
						hoteldata.setH_name(c.getString(Hotel_Name));
						hoteldata.setH_address(c.getString(Hotel_Address));
						hoteldata.setH_category(c.getString(Hotel_Category));
						hoteldata.setH_city(c.getString(Hotel_City));
						hoteldata.setH_code(c.getString(Hotel_Code));
						hoteldata.setH_contact(c.getString(Hotel_Contact));
						hoteldata.setH_country(c.getString(Hotel_Country));
						hoteldata.setH_desc(c.getString(Hotel_Desc));
						hoteldata.setH_lati(c.getString(Hotel_latitude));
						hoteldata.setH_long(c.getString(Hotel_longititude));
						hoteldata.setH_state(c.getString(Hotel_State));
						hoteldata.setIs_whotel(c.getString(Hotel_Whotel));
						hoteldata.setLandscape(c.getString(Hotel_landscape));
						hoteldata.setOriginal(c.getString(Hotel_original));
						hoteldata.setPage_url(c.getString(Hotel_url));
						hoteldata.setPortrait(c.getString(Hotel_portrait));
						hoteldata.setRectlarge(c.getString(Hotel_rectlarge));
						hoteldata.setRectsmall(c.getString(Hotel_rectsmall));

						HotelDataList.add(hoteldata);
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
			if (HotelDataList != null) {
				htFragment.onTaskCompleted(HotelDataList, true);
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
