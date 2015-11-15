package com.android.cabapp.datastruct.json;

import com.google.gson.annotations.Expose;

public class SectorList {
	@Expose
	private String sectorId;
	@Expose
	private String sectorName;
	@Expose
	private String cityId;
	@Expose
	private String sectorNumber;
	@Expose
	private String active;

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getSectorNumber() {
		return sectorNumber;
	}

	public void setSectorNumber(String sectorNumber) {
		this.sectorNumber = sectorNumber;
	}

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

}
