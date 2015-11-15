package com.hoteltrip.android.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class HotelData {

	// "HotelId": "WSMA0901011425",
	// "HotelName": "Grand Pinnacle Bangkok",
	// "Rating": "2",
	// "Currency": "THB",
	// "MarketName": "NONE",
	// "dtCheckIn": "2013-12-10",
	// "dtCheckOut": "2013-12-11",
	// "CancelPolicyId": "hkCJ8Jxem6SEI7NlkldxgQ9u",
	// "InternalCode": "CL098-CL918",
	// "avail": "True"

	private String hotelId;
	private String hotelName;
	private float rating;
	private String currency;
	private String checkInDate;
	private String checkOutDate;
	private String internalCode;

	private Map<String, JSONObject> otherObjects = new HashMap<String, JSONObject>();
	private Map<String, JSONArray> otherArrays = new HashMap<String, JSONArray>();

	public void setHotelId(String HotelId) {
		this.hotelId = HotelId;
	}

	public String getHotelId() {
		return hotelId;
	}

	public void setHotelName(String HotelName) {
		this.hotelName = HotelName;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setRating(float Rating) {
		this.rating = Rating;
	}

	public float getRating() {
		return rating;
	}

	public void setCurrency(String Currency) {
		this.currency = Currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCheckInDate(String checkInDate) {
		this.checkInDate = checkInDate;
	}

	public String getCheckInDate() {
		return checkInDate;
	}

	public void setCheckOutDate(String checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getCheckOutDate() {
		return checkOutDate;
	}

	public void setInternalCode(String InternalCode) {
		this.internalCode = InternalCode;
	}

	public String getInternalCode() {
		return internalCode;
	}

	public JSONObject getObject(String name) {
		return otherObjects.get(name);
	}

	public void setObject(String name, JSONObject jsonObject) {
		otherObjects.put(name, jsonObject);
	}

	public JSONArray getArray(String name) {
		return otherArrays.get(name);
	}

	public void setArray(String name, JSONArray jsonArray) {
		otherArrays.put(name, jsonArray);
	}

	public Map<String, JSONObject> getObjectMap() {
		return otherObjects;
	}

	public Map<String, JSONArray> getArrayMap() {
		return otherArrays;
	}

	@Override
	public String toString() {
		String szReturn = "";
		szReturn = getClass().getName() + "[" + "array="
				+ getArray("RoomCateg") + ", " + "object="
				+ getObject("RoomCateg") + ", " + "]";
		return szReturn;
	}

}
