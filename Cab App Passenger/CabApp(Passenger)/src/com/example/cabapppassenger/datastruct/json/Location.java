package com.example.cabapppassenger.datastruct.json;

import com.google.gson.annotations.Expose;

public class Location {

	@Expose
	private String latitude;
	@Expose
	private String longitude;
	@Expose
	private String addressLine1;
	@Expose
	private String addressLine2;
	@Expose
	private String addressLine3;
	@Expose
	private String addressLine4;
	@Expose
	private String postcode;

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getAddressLine4() {
		return addressLine4;
	}

	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}

	public String getPostCode() {
		return postcode;
	}

	public void setPostCode(String postCode) {
		this.postcode = postCode;
	}

}