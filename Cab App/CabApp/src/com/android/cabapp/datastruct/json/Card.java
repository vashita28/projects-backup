package com.android.cabapp.datastruct.json;

import com.google.gson.annotations.Expose;

public class Card {
	@Expose
	private String brand;
	@Expose
	private String truncatedPan;
	@Expose
	private String isSelected;
	@Expose
	private String paymentToken;

	public String getPaymentToken() {
		return paymentToken;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getTruncatedPan() {
		return truncatedPan;
	}

	public void setTruncatedPan(String truncatedPan) {
		this.truncatedPan = truncatedPan;
	}

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}
}
