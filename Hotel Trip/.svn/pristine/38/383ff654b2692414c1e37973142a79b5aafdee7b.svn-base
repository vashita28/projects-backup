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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.Const;
import com.hoteltrip.android.util.HotelData;

public class BookHotelTask extends AsyncTask<Void, String, String> {

	private String szURL = AppValues.ms_szURL + "dataget/bookHotel";

	Context mContext;
	Handler mHandler;

	public int posInList, roomCategPosition;
	public String szPaxPassport, szInternalCode, szHotelID, szPaymentCode;

	String szCheckInDate, szCheckOutDate;

	// {
	// "Cat1": {
	// "category_id": "WSMA05110034",
	// "category_name": "DELUXE",
	// "check_in": "2014-02-07",
	// "check_out": "2014-02-09",
	// "bftype": "ABF",
	// "RoomType": [{
	// "seq": 1,
	// "type_name": "Triple",
	// "price": "3000.00",
	// "adultnum": 2,
	// "childages": {
	// "ChildAge1": "2"
	// },
	// "person_info": {
	// "1": "Smith Peter, Mr.",
	// "2": "Smith Nancy, Mrs."
	// }
	// }, {
	// "seq": 2,
	// "type_name": "Triple",
	// "price": "3000.00",
	// "adultnum": 2,
	// "childages": {},
	// "person_info": {
	// "1": "Brown Darren, Mr.",
	// "2": "Brown Darcy, Mrs."
	// }
	// }]
	// }
	// }

	JSONArray jRoomTypeArray = null;
	JSONObject jRoomTypeObject = null;

	JSONArray roomtypeArray = new JSONArray();
	JSONArray roomInfoArray = null;

	JSONObject roomtypeObjects = null;

	JSONArray jRoomSeqArray = null;
	JSONObject jRoomSeqObject = null;

	JSONObject jChildAgesObject = null;
	JSONObject jPersonInfoObject = null;

	public BookHotelTask(Context context, Handler handler) {
		// TODO Auto-generated constructor stub
		this.mHandler = handler;
		this.mContext = context;
		szPaxPassport = AppValues.szPaxPassport;
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		HotelData hotelData = AppValues.hotelDataList.get(posInList);

		szInternalCode = hotelData.getInternalCode();
		szCheckInDate = hotelData.getCheckInDate();
		szCheckOutDate = hotelData.getCheckOutDate();
		AppValues.szCurrency = hotelData.getCurrency();

		JSONObject jRoomCategObject = null;

		Log.e("**********", "***********HOTEL POS:: " + posInList);
		Log.e("**********", "***********ROOM CATEG POS:: " + roomCategPosition);
		Log.e("**********", "***********HOTEL CHECKIN:: " + szCheckInDate);
		Log.e("**********", "***********HOTEL CHECKOUT:: " + szCheckOutDate);

		// if (roomCategPosition != -1 || roomCategPosition == 0)
		try {
			jRoomCategObject = hotelData.getArray("RoomCateg").getJSONObject(
					roomCategPosition);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			try {
				jRoomCategObject = hotelData.getObject("RoomCateg");
			} catch (Exception e2) {
				// e2.printStackTrace();
			}
		}

		String response = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 15000); // timeout

			HttpPost postMethod = new HttpPost(szURL);

			JSONObject roomObject = new JSONObject();

			JSONObject catObject = new JSONObject();

			try {
				jRoomTypeArray = jRoomCategObject.getJSONArray("RoomType");
			} catch (JSONException e) {
				jRoomTypeObject = jRoomCategObject.getJSONObject("RoomType");
				// e.printStackTrace();
			}

			if (jRoomTypeObject != null) { // Room Type is only 1
				try {
					// roomtypeObjects = new JSONObject();
					// roomtypeObjects.put("seq", 1);
					//
					// createJSONStructure(roomtypeObjects, jRoomTypeObject,
					// jRoomSeqArray, jRoomSeqObject, jPersonInfoObject,
					// jChildAgesObject);
					//
					// roomtypeObjects.put("childages", jChildAgesObject);
					// roomtypeObjects.put("person_info", jPersonInfoObject);
					//
					// catObject.put("RoomType", roomtypeObjects);

					jChildAgesObject = new JSONObject();
					jPersonInfoObject = new JSONObject();

					roomtypeObjects = new JSONObject();
					// roomtypeObjects.put("seq", 1);

					// jRoomTypeObject = jRoomTypeArray.getJSONObject(1);

					createJSONStructure();

					roomtypeObjects.put("RoomInfo", (Object) roomInfoArray);

					roomtypeArray.put(roomtypeObjects);
					catObject.put("RoomType", (Object) roomtypeArray);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (jRoomTypeArray != null) { // Room Type more than 1
				try {
					for (int i = 0; i < jRoomTypeArray.length(); i++) {

						jChildAgesObject = new JSONObject();
						jPersonInfoObject = new JSONObject();

						roomtypeObjects = new JSONObject();
						// roomtypeObjects.put("seq", i + 1);

						jRoomTypeObject = jRoomTypeArray.getJSONObject(i);

						createJSONStructure();

						roomtypeObjects.put("RoomInfo", (Object) roomInfoArray);

						roomtypeArray.put(roomtypeObjects);

					}
					catObject.put("RoomType", (Object) roomtypeArray);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			catObject.put(
					"category_id",
					jRoomCategObject.getJSONObject("@attributes").getString(
							"Code"));
			catObject.put(
					"category_name",
					jRoomCategObject.getJSONObject("@attributes").getString(
							"Name"));
			catObject.put("check_in", szCheckInDate);
			catObject.put("check_out", szCheckOutDate);
			catObject.put(
					"bftype",
					jRoomCategObject.getJSONObject("@attributes").getString(
							"BFType"));

			roomObject.put("Cat1", catObject);
			// postMethod.addHeader("Content-Type",
			// "application/json; charset=utf-8"); // addHeader()
			postMethod.addHeader("Content-Type",
					"application/x-www-form-urlencoded");

			// postMethod.setEntity(new StringEntity(mainObject.toString(),
			// "utf-8"));

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			// 1. paxpassport: Char, max length 12. Nationality code of the
			// passenger who is booking.
			nameValuePairs.add(new BasicNameValuePair("paxpassport",
					szPaxPassport));
			// 2. internalcode: Char, max length 50. Travflex Internal Code. You
			// can get this code from response of SearchHotels
			nameValuePairs.add(new BasicNameValuePair("internalcode",
					szInternalCode));
			// 3. hotelid: Char, max length 15. Travflex Hotel Code for which
			// user is booking.
			nameValuePairs.add(new BasicNameValuePair("hotelid", szHotelID));
			// 4. roomdata: JSON format:
			Log.e("BookHotelTask()",
					"ROOM DATA JSON is :: " + roomObject.toString());
			nameValuePairs.add(new BasicNameValuePair("roomdata", roomObject
					.toString()));
			// 5. "paymentcode" to BookHotel service
			nameValuePairs.add(new BasicNameValuePair("paymentcode",
					szPaymentCode));

			nameValuePairs.add(new BasicNameValuePair("agent_id",
					AppValues.szAgentID));
			nameValuePairs.add(new BasicNameValuePair("login_name",
					AppValues.szUserName));
			nameValuePairs.add(new BasicNameValuePair("password",
					AppValues.szPassword));

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
			JSONObject jSearchResultObject = new JSONObject(result);

			if (jSearchResultObject.has("BookHotel_Response")) {
				jSearchResultObject = jSearchResultObject
						.getJSONObject("BookHotel_Response");

				if (jSearchResultObject.has("Error")) {
					if (mHandler != null) {
						Message message = new Message();
						message.what = Const.BOOKING_FAILED;
						message.obj = jSearchResultObject
								.getJSONObject("Error").getString(
										"ErrorDescription");
						mHandler.sendMessage(message);
					}
				} else {
					if (jSearchResultObject.has("ResNo")) {
						if (mHandler != null) {
							Message message = new Message();
							message.what = Const.BOOKING_SUCCESS;
							message.obj = result;
							mHandler.sendMessage(message);
							AppValues.ms_szBookingResponse = result;
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
		Log.i("BookHotelTask", "inputStreamToString()-result  " + total);

		return total;
	}

	void createJSONStructure() {
		try {
			roomtypeObjects.put(
					"type_name",
					jRoomTypeObject.getJSONObject("@attributes").getString(
							"TypeName"));
			roomtypeObjects.put(
					"price",
					jRoomTypeObject.getJSONObject("@attributes").getString(
							"TotalPrice"));
		} catch (JSONException e) {

		}

		JSONArray jRateArray = null;
		JSONObject jRateObject = null;

		try {
			jRateArray = jRoomTypeObject.getJSONArray("Rate");
			if (jRateArray != null) {
				jRateObject = jRateArray.getJSONObject(0);
			}

		} catch (JSONException e) {
			// TODO: handle exception
			try {
				jRateObject = jRoomTypeObject.getJSONObject("Rate");
			} catch (JSONException e1) {

			}

		}

		try {
			jRoomSeqArray = jRateObject.getJSONObject("RoomRate").getJSONArray(
					"RoomSeq");
		} catch (JSONException e) {
			// e.printStackTrace();
			try {
				jRoomSeqObject = jRateObject.getJSONObject("RoomRate")
						.getJSONObject("RoomSeq");
			} catch (JSONException e1) {

			}
		}

		JSONObject roomInfoObject;

		if (jRoomSeqArray != null) { // more than 1 room
			roomInfoArray = new JSONArray();
			for (int i = 0; i < jRoomSeqArray.length(); i++) {

				roomInfoObject = new JSONObject();
				jChildAgesObject = new JSONObject();
				jPersonInfoObject = new JSONObject();

				int adultNum = 1;
				try {
					adultNum = Integer
							.parseInt(jRoomSeqArray.getJSONObject(i)
									.getJSONObject("@attributes")
									.getString("AdultNum"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}

				try {
					roomInfoObject.put(
							"seq",
							Integer.parseInt(jRoomSeqArray.getJSONObject(i)
									.getJSONObject("@attributes")
									.getString("No")));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
				}

				try {
					roomInfoObject.put("RoomPrice", jRoomSeqArray
							.getJSONObject(i).getJSONObject("@attributes")
							.getString("RoomPrice"));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
				}

				try {
					// roomtypeObjects.put("adultnum", adultNum);
					roomInfoObject.put("adultnum", adultNum);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}

				Log.e("BookHotelTask", "Names:: " + AppValues.szName
						+ " :: MemberName:: " + AppValues.szMemberName);

				for (int j = 1; j <= adultNum; j++) {
					String szName, szPrefix, szSurname;
					if (!AppValues.szMemberName.equals("")
							&& !AppValues.szMemberName.equals(AppValues.szName)) {
						szName = AppValues.szMemberName;
						szPrefix = AppValues.szMemberPrefix;
						szSurname = AppValues.szMemberSurname;
					} else {
						szName = AppValues.szName;
						szPrefix = AppValues.szPrefix;
						szSurname = AppValues.szSurName;
					}

					try {
						jPersonInfoObject.put(String.valueOf(j), szSurname
								+ " " + szName + "," + szPrefix);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
					}
				}

				// try {
				// int childCount = Integer.parseInt(jRoomSeqArray
				// .getJSONObject(i).getJSONObject("@attributes")
				// .getString("ChildNum"));
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				try {
					if (jRoomSeqArray.getJSONObject(i)
							.getJSONObject("@attributes").has("ChildAge1")) {

						String szChildAge1 = jRoomSeqArray.getJSONObject(i)
								.getJSONObject("@attributes")
								.getString("ChildAge1");

						if (szChildAge1 == null || szChildAge1.isEmpty()) {
							szChildAge1 = "0";
						}

						if (Integer.parseInt(szChildAge1) > 0)
							jChildAgesObject.put("ChildAge1", szChildAge1);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}

				try {
					if (jRoomSeqArray.getJSONObject(i)
							.getJSONObject("@attributes").has("ChildAge2")) {

						String szChildAge2 = jRoomSeqArray.getJSONObject(i)
								.getJSONObject("@attributes")
								.getString("ChildAge2");

						if (szChildAge2 == null || szChildAge2.isEmpty()) {
							szChildAge2 = "0";
						}

						if (Integer.parseInt(szChildAge2) > 0)
							jChildAgesObject.put("ChildAge2", szChildAge2);

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}

				try {
					roomInfoObject.put("childages", jChildAgesObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					roomInfoObject.put("person_info", jPersonInfoObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				roomInfoArray.put(roomInfoObject);
			}
		}

		if (jRoomSeqObject != null) { // only 1 room
			roomInfoArray = new JSONArray();
			roomInfoObject = new JSONObject();
			int adultNum = 1;
			try {
				adultNum = Integer.parseInt(jRoomSeqObject.getJSONObject(
						"@attributes").getString("AdultNum"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			try {
				// roomtypeObjects.put("adultnum", adultNum);
				roomInfoObject.put("adultnum", adultNum);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			try {
				roomInfoObject.put(
						"seq",
						Integer.parseInt(jRoomSeqObject.getJSONObject(
								"@attributes").getString("No")));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
			}

			try {
				roomInfoObject.put(
						"RoomPrice",
						jRoomSeqObject.getJSONObject("@attributes").getString(
								"RoomPrice"));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
			}

			for (int j = 1; j <= adultNum; j++) {
				String szName, szPrefix, szSurname;

				if (!AppValues.szMemberName.equals("")
						&& !AppValues.szMemberName.equals(AppValues.szName)) {
					szName = AppValues.szMemberName;
					szPrefix = AppValues.szMemberPrefix;
					szSurname = AppValues.szMemberSurname;
				} else {
					szName = AppValues.szName;
					szPrefix = AppValues.szPrefix;
					szSurname = AppValues.szSurName;
				}

				try {
					jPersonInfoObject.put(String.valueOf(j), szSurname + " "
							+ szName + "," + szPrefix);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}

			// try {
			// int childCount = Integer.parseInt(jRoomSeqObject.getJSONObject(
			// "@attributes").getString("ChildNum"));
			// } catch (NumberFormatException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			try {
				if (jRoomSeqObject.getJSONObject("@attributes")
						.has("ChildAge1")) {

					String szChildAge1 = jRoomSeqObject.getJSONObject(
							"@attributes").getString("ChildAge1");

					if (szChildAge1 == null || szChildAge1.isEmpty()) {
						szChildAge1 = "0";
					}

					if (Integer.parseInt(szChildAge1) > 0)
						jChildAgesObject.put("ChildAge1", szChildAge1);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			try {
				if (jRoomSeqObject.getJSONObject("@attributes")
						.has("ChildAge2")) {
					String szChildAge2 = jRoomSeqObject.getJSONObject(
							"@attributes").getString("ChildAge2");

					if (szChildAge2 == null || szChildAge2.isEmpty()) {
						szChildAge2 = "0";
					}

					if (Integer.parseInt(szChildAge2) > 0)
						jChildAgesObject.put("ChildAge2", szChildAge2);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			try {
				roomInfoObject.put("childages", jChildAgesObject);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				roomInfoObject.put("person_info", jPersonInfoObject);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			roomInfoArray.put(roomInfoObject);

		}
	}
}
