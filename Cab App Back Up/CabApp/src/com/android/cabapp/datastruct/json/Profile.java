package com.android.cabapp.datastruct.json;

import com.google.gson.annotations.Expose;

public class Profile {
	@Expose
	private String id;
	@Expose
	private String paymentToken;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPaymentToken() {
		return paymentToken;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}
}
