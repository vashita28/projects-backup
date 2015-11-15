package com.android.cabapp.datastruct.json;

import com.google.gson.annotations.Expose;

public class Payment {
	@Expose
	private Response response;

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

}
