package com.android.cabapp.model;

import java.io.InputStreamReader;
import java.io.Reader;

import android.content.Context;

import com.android.cabapp.datastruct.json.City;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.google.gson.Gson;

public class GetAllCityList {
	private static final String TAG = GetAllCityList.class.getSimpleName();

	Connection connection;
	Context mContext;

	public GetAllCityList(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		connection = new Connection(mContext);
	}

	public City getCityList(Context context) {
		connection.connect(Constants.CITY);
		City cityListResponse = null;

		Gson gson = new Gson();

		if (connection.mInputStream != null) {
			Reader reader = new InputStreamReader(connection.mInputStream);
			cityListResponse = gson.fromJson(reader, City.class);

		}
		return cityListResponse;

	}

}
