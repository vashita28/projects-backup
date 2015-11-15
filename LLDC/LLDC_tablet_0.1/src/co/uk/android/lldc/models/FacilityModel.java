package co.uk.android.lldc.models;

import co.uk.android.lldc.tablet.LLDCApplication;

public class FacilityModel {

	String id = "", facility_id = "", lat = "", lon = "";
	int modelType = LLDCApplication.VENUE;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the facility_id
	 */
	public String getFacility_id() {
		return facility_id;
	}

	/**
	 * @param facility_id
	 *            the facility_id to set
	 */
	public void setFacility_id(String facility_id) {
		this.facility_id = facility_id;
	}

	/**
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public String getLon() {
		return lon;
	}

	/**
	 * @param lon
	 *            the lon to set
	 */
	public void setLon(String lon) {
		this.lon = lon;
	}

	/**
	 * @return the modelType
	 */
	public int getModelType() {
		return modelType;
	}

	/**
	 * @param modelType
	 *            the modelType to set
	 */
	public void setModelType(int modelType) {
		this.modelType = modelType;
	}

}
