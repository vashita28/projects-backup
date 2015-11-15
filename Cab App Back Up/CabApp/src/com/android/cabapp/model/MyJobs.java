package com.android.cabapp.model;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.android.cabapp.datastruct.json.MyJobsList;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.google.gson.Gson;

public class MyJobs {
	Connection connection;
	List<NameValuePair> nameValuePairs;

	public MyJobs(Context context) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		nameValuePairs = new ArrayList<NameValuePair>();
	}

	public MyJobsList getMyJobsList() {
		MyJobsList myJobsListResponse = null;
		connection.connect(Constants.MY_JOBS_FRAGMENT);

		if (connection.mInputStream != null) {
			Gson gson = new Gson();
			Reader reader = new InputStreamReader(connection.mInputStream);
			myJobsListResponse = gson.fromJson(reader, MyJobsList.class);
		}

		return myJobsListResponse;

	}
}