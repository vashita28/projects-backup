package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class DeleteDocument {
	Connection connection;
	List<NameValuePair> nameValuePairs;
	String message;
	String documentID;

	public DeleteDocument(String ID, Context context) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.documentID = ID;
		connection.jobId = documentID;
	}

	public String getResponse() {
		connection.connect(Constants.DELETE_DOCUMENT);

		String response = "";
		try {
			if (connection.mInputStream != null) {

				response = connection.inputStreamToString(
						connection.mInputStream).toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}
}
