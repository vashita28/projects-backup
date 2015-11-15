package com.android.cabapp.datastruct.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Cities {
	@Expose
	private String id;
	@Expose
	private String friendlyName;
	@Expose
	private String cityname;
	@Expose
	private String currencyCode;
	@Expose
	private String requiresIban;
	@Expose
	private String defaultWheelchairAccess;
	@Expose
	private String minimumPassengersPerCab;
	@Expose
	private String maximumPassengersPerCab;

	@Expose
	private List<FormCustomisation> formCustomisations = new ArrayList<FormCustomisation>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getRequiresIban() {
		return requiresIban;
	}

	public void setRequiresIban(String requiresIban) {
		this.requiresIban = requiresIban;
	}

	public String getDefaultWheelchairAccess() {
		return defaultWheelchairAccess;
	}

	public void setDefaultWheelchairAccess(String defaultWheelchairAccess) {
		this.defaultWheelchairAccess = defaultWheelchairAccess;
	}

	public String getMinimumPassengersPerCab() {
		return minimumPassengersPerCab;
	}

	public void setMinimumPassengersPerCab(String minimumPassengersPerCab) {
		this.minimumPassengersPerCab = minimumPassengersPerCab;
	}

	public String getMaximumPassengersPerCab() {
		return maximumPassengersPerCab;
	}

	public void setMaximumPassengersPerCab(String maximumPassengersPerCab) {
		this.maximumPassengersPerCab = maximumPassengersPerCab;
	}

	public List<FormCustomisation> getFormCustomisations() {
		return formCustomisations;
	}

	public void setFormCustomisations(List<FormCustomisation> formCustomisations) {
		this.formCustomisations = formCustomisations;
	}

}
