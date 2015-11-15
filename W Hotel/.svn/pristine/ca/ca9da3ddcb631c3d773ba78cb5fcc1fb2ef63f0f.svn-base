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

import uk.co.pocketapp.whotel.fragments.FindHotspotFragment;
import uk.co.pocketapp.whotel.util.CustomHotSpotData;
import uk.co.pocketapp.whotel.util.Util;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class JsonParserGetHotSpot extends AsyncTask<Void, Long, String> {

	// private static String url =
	// "http://dev.pocketapp.co.uk/dev/whotel_wifi/administrator/api.php?q=get_hotspot";
	private static String url = Util.serverURL + "?q=get_hotspot";

	// {
	// "hs_contact": "678-666-5198",
	// "hs_code": "GA 30308",
	// "rectlarge":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_2_rectlarge.jpg",
	// "page_url":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/read_content?id=2&table_type=hotspots",
	// "hs_desc":
	// "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknow",
	// "hs_long": "-84.384534",
	// "hs_city": "Atlanta",
	// "landscape":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_2_landscape.jpg",
	// "portrait":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_2_landscape.jpg",
	// "id": "2",
	// "hs_lati": "33.776936",
	// "hs_state": "Georgia",
	// "hs_name": "Escorpion",
	// "rectsmall":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_2_rectsmall.jpg",
	// "original":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_2_original.jpg",
	// "hs_country": "United States",
	// "hs_category": "NIGHTLIFE",
	// "hs_add": "800 Peachtree St NE Atlanta, GA 30308"
	// }

	public final String ID = "id";
	public final String HotSpot_Name = "hs_name";
	public final String HotSpot_address = "hs_add";
	public final String HotSpot_City = "hs_city";
	public final String HotSpot_State = "hs_state";
	public final String HotSpot_Country = "hs_country";
	public final String HotSpot_Code = "hs_code";
	public final String HotSpot_Contact = "hs_contact";
	public final String HotSpot_Category = "hs_category";
	public final String HotSpot_Descripition = "hs_desc";
	public final String HotSpot_longititude = "hs_long";
	public final String HotSpot_latitude = "hs_lati";
	public final String HotSpot_original = "original";
	public final String HotSpot_landscape = "landscape";
	public final String HotSpot_protrait = "portrait";
	public final String HotSpot_rectsmall = "rectsmall";
	public final String HotSpot_rectlarge = "rectlarge";
	public final String HotSpot_Pageurl = "page_url";
	public final String HotSpot_IsWHotel = "is_whotel";

	FindHotspotFragment hotspotFragment;

	Context mContext;
	public ArrayList<CustomHotSpotData> hotspotDataList;

	public JsonParserGetHotSpot(Context ctx, FindHotspotFragment hotspotFragment) {
		this.mContext = ctx;
		this.hotspotFragment = hotspotFragment;
		hotspotDataList = new ArrayList<CustomHotSpotData>();
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

				HttpPost postMethod = new HttpPost(url);

				// List<NameValuePair> nameValuePairs = new
				// ArrayList<NameValuePair>(1);

				JSONObject objRequest = new JSONObject();
				objRequest.put("type", "hotspots");
				if (Util.getLocation() != null) {
					objRequest.put("lat", Util.getLocation().getLatitude());
					objRequest.put("lng", Util.getLocation().getLongitude());
				}
				// {"type":"hotspots","lat":"33.855021","lng":"-84.364758"}

				// nameValuePairs.add(new
				// BasicNameValuePair("type","hotspots"));

				postMethod.addHeader("Content-Type",
						"application/json; charset=utf-8"); // addHeader()
				postMethod.setEntity(new StringEntity(objRequest.toString(),
						"utf-8"));
				// postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs,
				// HTTP.UTF_8));

				HttpResponse httpResponse = client.execute(postMethod);
				response = inputStreamToString(
						httpResponse.getEntity().getContent()).toString();
				// Log.e("GetHotspot", "response:: " + response);

				JSONArray json = new JSONArray(response);
				if (json != null && json.length() > 0) {

					if (json.getJSONObject(0).has("error_message")) {
						return json.getJSONObject(0).getString("error_message");
					}

					for (int i = 0; i < json.length(); i++) {
						JSONObject c = json.getJSONObject(i);

						CustomHotSpotData hotspotdata = new CustomHotSpotData();
						hotspotdata.setId(c.getString(ID));
						hotspotdata.setHs_name(c.getString(HotSpot_Name));
						hotspotdata.setHs_add(c.getString(HotSpot_address));
						hotspotdata.setHs_category(c
								.getString(HotSpot_Category));
						hotspotdata.setHs_city(c.getString(HotSpot_City));
						hotspotdata.setHs_state(c.getString(HotSpot_State));
						hotspotdata.setHs_contact(c.getString(HotSpot_Contact));
						hotspotdata.setHs_country(c.getString(HotSpot_Country));
						hotspotdata.setHs_code(c.getString(HotSpot_Code));
						String szDesc = c.getString(HotSpot_Descripition);
						if (szDesc != null && szDesc.equalsIgnoreCase("null"))
							hotspotdata.setHs_desc("");
						else
							hotspotdata.setHs_desc(szDesc);
						hotspotdata.setHs_lati(c.getString(HotSpot_latitude));
						hotspotdata
								.setHs_long(c.getString(HotSpot_longititude));
						hotspotdata.setOriginal(c.getString(HotSpot_original));
						hotspotdata.setPage_url(c.getString(HotSpot_Pageurl));
						hotspotdata.setProtrait(c.getString(HotSpot_protrait));
						hotspotdata
								.setLandscape(c.getString(HotSpot_landscape));
						hotspotdata
								.setRectlarge(c.getString(HotSpot_rectlarge));
						hotspotdata
								.setRectsmall(c.getString(HotSpot_rectsmall));
						if (c.has(HotSpot_IsWHotel))
							hotspotdata.setHs_IsWhotel(c
									.getString(HotSpot_IsWHotel));
						else
							hotspotdata.setHs_IsWhotel("NO");

						hotspotDataList.add(hotspotdata);
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
			if (hotspotDataList != null) {
				hotspotFragment.onTaskCompleted(hotspotDataList, true);
			} else {
				// Log.v("onPostExecute else", "null");
			}
		} else {
			Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
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
