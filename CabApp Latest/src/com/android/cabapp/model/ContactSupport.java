package com.android.cabapp.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;

public class ContactSupport {
	Connection connection;
	List<NameValuePair> nameValuePairs;
	String message;

	public ContactSupport(String message, Context context) {
		// TODO Auto-generated constructor stub
		connection = new Connection(context);
		nameValuePairs = new ArrayList<NameValuePair>();
		this.message = message;
	}

	public String getResponse() {
		nameValuePairs.add(new BasicNameValuePair("data[message]", message));
		connection.prepareConnection(nameValuePairs);
		connection.connect(Constants.CONTACT_SUPPORT);

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
