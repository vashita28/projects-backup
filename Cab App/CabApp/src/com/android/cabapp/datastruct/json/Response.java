package com.android.cabapp.datastruct.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Response {
	@Expose
	private String currencyCode;
	@Expose
	private String id;
	@Expose
	private List<Link> link = new ArrayList<Link>();
	@Expose
	private String merchantRefNum;
	@Expose
	private String mode;
	@Expose
	private Profile profile;
	@Expose
	private String totalAmount;
	@Expose
	private String type;

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Link> getLink() {
		return link;
	}

	public void setLink(List<Link> link) {
		this.link = link;
	}

	public String getMerchantRefNum() {
		return merchantRefNum;
	}

	public void setMerchantRefNum(String merchantRefNum) {
		this.merchantRefNum = merchantRefNum;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
