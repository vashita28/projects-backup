package com.android.cabapp.model;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;

import com.android.cabapp.datastruct.json.SectorList;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SectorForCity {
	private static final String TAG = SectorForCity.class.getSimpleName();

	Connection connection;
	Context mContext;

	public SectorForCity(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		connection = new Connection(mContext);
	}

	public List<SectorList> getSectorList(Context context) {
		connection.connect(Constants.SECTOR_CITY);
		List<SectorList> sectorListResponse = null;

		final GsonBuilder builder = new GsonBuilder();
		final Gson gson = builder.create();

		if (connection.mInputStream != null) {
			Reader reader = new InputStreamReader(connection.mInputStream);
			Type type = new TypeToken<List<SectorList>>() {
			}.getType();
			sectorListResponse = gson.fromJson(reader, type);
		}
		return sectorListResponse;

	}
}
