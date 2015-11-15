package uk.co.pocketapp.whotel.util;

public class CustomDealsData {

	String lattitude, longitude, phoneNumber, type, title, address;

	// "app_data": {
	// "lng": "-84.360391",
	// "phone": "404-681-4434",
	// "type": "deals",
	// "hotspot": "Parish",
	// "address": "240 N. Highland Ave NE Atlanta, GA 30307",
	// "lat": "33.762327"
	// }

	public void setLattitude(String lat) {
		this.lattitude = lat;
	}

	public String getLattitude() {
		return this.lattitude;
	}

	public void setLongitude(String lng) {
		this.longitude = lng;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setPhoneNumber(String phone) {
		this.phoneNumber = phone;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setDealsTitle(String title) {
		this.title = title;
	}

	public String getDealsTitle() {
		return this.title;
	}
}
