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
import android.util.Log;

import com.hoteltrip.android.FindHotelActivity;
import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.SearchHotelDataFetched;

public class SearchHotelTask extends AsyncTask<Void, String, String> {

	// {
	// SearchHotel_Response: {
	// Result: "No Data Found."
	// }
	// }

	// http://dev.pocketapp.co.uk/dev/hotel-trip/index.php/dataget/hotelsearch/MA05110001/MA05110001/MA05110041/2013-11-27/1
	// dev.pocketapp.co.uk/dev/hotel-trip/index.php/dataget/hotelsearch/[PaxPassport]/[DestCountry]/[DestCity]/[checkInDate]/[nights]

	private String szURL = AppValues.ms_szURL
			+ "dataget/hotelsearch/%s/%s/%s/%s/%s";

	public String szCheckInDate = "", szPaxPassport = "", szDestCountry = "",
			szDestCity = "", szNoOfNights = "", szHotelId="";

	public List<FindHotelActivity.RoomOccupancyDetails> roomListDetails;

	SearchHotelDataFetched searchHotelDataFetched;
	Context mContext;

	public SearchHotelTask(SearchHotelDataFetched searchHotelDataFetched,
			Context context) {
		// TODO Auto-generated constructor stub
		this.searchHotelDataFetched = searchHotelDataFetched;
		this.mContext = context;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		szURL = String.format(szURL, szPaxPassport, szDestCountry, szDestCity,
				szCheckInDate, szNoOfNights);
		Log.e("SearchHotelTask", "URL is::  " + szURL);

		String response = null;
		try {
			// HttpClient client = new DefaultHttpClient();
			// HttpParams httpParams = new BasicHttpParams();
			// HttpConnectionParams.setConnectionTimeout(httpParams, 15000); //
			// timeout
			// HttpGet getMethod = new HttpGet(szURL);
			// HttpResponse httpResponse = client.execute(getMethod);
			// response = inputStreamToString(
			// httpResponse.getEntity().getContent()).toString();

			HttpClient client = new DefaultHttpClient();
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15000); // timeout

			HttpPost postMethod = new HttpPost(szURL);

			JSONObject roomObject = new JSONObject();
			int noOfAdults = 0;
			// JSONObject mainObject = new JSONObject();
			if (roomListDetails != null && roomListDetails.size() > 0) {
				// mainObject.put("roomnum", roomListDetails.size());
				for (int i = 0; i < roomListDetails.size(); i++) {

					JSONObject roomInfoObject = new JSONObject();
					roomInfoObject.put("adultnum",
							roomListDetails.get(i).numberOfAdults);
					noOfAdults = noOfAdults
							+ roomListDetails.get(i).numberOfAdults;
					JSONObject childObject = new JSONObject();
					if (roomListDetails.get(i).ageOfChild1 > 0)
						childObject.put("ChildAge1",
								roomListDetails.get(i).ageOfChild1);
					if (roomListDetails.get(i).ageOfChild2 > 0)
						childObject.put("ChildAge2",
								roomListDetails.get(i).ageOfChild2);

					roomInfoObject.put("childages", childObject);
					roomObject.put("RoomInfo" + (i + 1), roomInfoObject);
				}
				AppValues.nNumberOfAdults = noOfAdults;
				// mainObject.put("roominfo", roomObject);
			} else {
				AppValues.nNumberOfAdults = 1;
				JSONObject roomInfoObject = new JSONObject();
				roomInfoObject.put("adultnum", 1);
				JSONObject childObject = new JSONObject();
				roomInfoObject.put("childages", childObject);
				roomObject.put("RoomInfo1", roomInfoObject);
			}

			Log.d("SearchHotelTask", "JSON Data:: " + roomObject.toString());

			// postMethod.addHeader("Content-Type",
			// "application/json; charset=utf-8"); // addHeader()
			postMethod.addHeader("Content-Type",
					"application/x-www-form-urlencoded");

			// postMethod.setEntity(new StringEntity(mainObject.toString(),
			// "utf-8"));

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			if (roomListDetails != null)
				nameValuePairs.add(new BasicNameValuePair("roomnum", String
						.valueOf(roomListDetails.size())));
			else
				nameValuePairs.add(new BasicNameValuePair("roomnum", "1"));
			
			if(!szHotelId.equals(""))
			{
				nameValuePairs.add(new BasicNameValuePair("hotelid", szHotelId));
			}
			AppValues.szRoomInfo = roomObject.toString();
			nameValuePairs.add(new BasicNameValuePair("roominfo", roomObject
					.toString()));
			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			postMethod.setParams(httpParams);

			HttpResponse httpResponse = client.execute(postMethod);
			response = inputStreamToString(
					httpResponse.getEntity().getContent()).toString();

			// JSONObject jsonObject = new JSONObject(response);
			// Log.e("", "Created json object:: " + jsonObject.length());
			// JSONArray json = jsonObject.getJSONObject("SearchHotel_Response")
			// .getJSONArray("Hotel");
			// Log.e("", "Created json array:: " + json.length());
			//
			// if (json != null) {
			// for (int i = 0; i < json.length(); i++) {
			// JSONObject c = json.getJSONObject(i);
			//
			// Log.e("", "Created json object:: " + c.toString());
			// }
			// }

			// Gson gson = new Gson();
			// SearchHotel_Response search_hotel = gson.fromJson(response,
			// SearchHotel_Response.class);
			// // System.out.println(search_hotel.getTitle());
			// Hotel[] hotels = search_hotel.getHotel();
			// for (Hotel hotel : hotels) {
			// System.out.println(hotel.getHotelId());
			// }

			try {
				JSONObject jsonObject = new JSONObject(response);
				jsonObject = jsonObject.getJSONObject("SearchHotel_Response");
				if (jsonObject.has("Result")) {
					response = jsonObject.getString("Result");
					return response;
				} else if (jsonObject.has("Error")) {
					response = jsonObject.getString("Error");
					return response;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// JsonParser parser = new JsonParser();
			// // The JsonElement is the root node. It can be an object, array,
			// // null or
			// // java primitive.
			// JsonElement element = parser.parse(response);
			// // use the isxxx methods to find out the type of jsonelement. In
			// our
			// // example we know that the root object is the
			// SearchHotel_Response object and
			// // contains an array of Hotel objects
			// if (element.isJsonObject()) {
			// JsonObject jsonObject = element.getAsJsonObject();
			// JsonArray hotel = jsonObject.getAsJsonObject(
			// "SearchHotel_Response").getAsJsonArray("Hotel");
			// for (int i = 0; i < hotel.size(); i++) {
			// JsonObject dataset = hotel.get(i).getAsJsonObject();
			// System.out.println(dataset.getAsJsonObject("@attributes")
			// .get("HotelId").getAsString());
			// }
			// }
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
		searchHotelDataFetched.SearchHotelDataResult(result);
	}

	private StringBuilder inputStreamToString(InputStream content)
			throws IOException {
		// TODO Auto-generated method stub
		String line = "";
		StringBuilder total = new StringBuilder();

		BufferedReader rd = new BufferedReader(new InputStreamReader(content));
		while (rd != null && (line = rd.readLine()) != null) {
			total.append(line);
		}
		// Log.i("SearchHotelTask", "inputStreamToString()-result  " + total);

		return total;
	}
}
