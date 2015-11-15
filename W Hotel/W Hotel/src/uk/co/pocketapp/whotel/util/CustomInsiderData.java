package uk.co.pocketapp.whotel.util;

/**
 * Created by Pramod on 9/26/13.
 */
public class CustomInsiderData {

	// "id": "1",
	// "h_name": "Pocketapp",
	// "h_address": "3400 Around Lenox Rd NE Atlanta, GA 30326",
	// "h_city": "Atlanta",
	// "h_state": "Georgia",
	// "h_country": "United States",
	// "h_code": "GA 30326",
	// "h_contact": "404-876-6055",
	// "h_category": "WATLANTA-DOWNTOWN",
	// "h_desc":
	// "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknow",
	// "h_long": "33.855021",
	// "h_lati": "-84.364758",
	// "original":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_1_original.jpg",
	// "landscape":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_1_landscape.jpg",
	// "portrait":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_1_landscape.jpg",
	// "rectsmall":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_1_rectsmall.jpg",
	// "rectlarge":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/uploads\/hotspots\/hotspots_1_rectlarge.jpg",
	// "is_whotel": "YES",
	// "page_url":
	// "http:\/\/dev.pocketapp.co.uk\/dev\/whotel_wifi\/administrator\/read_content?id=1&table_type=hotel"

	String id;
	String createdOn;
	String insiderAddress;
	String insiderDesc;
	String insiderLati;
	String insiderLong;
	String insiderName;
	String insiderPriority;
	String insiderSequence;
	String insiderIsWhotel;
	String insiderLandscape;
	String insiderOriginal;
	String insiderPageURL;
	String insiderPortrait;
	String insiderRectLarge;
	String insiderRectsmall;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getInsiderAddress() {
		return insiderAddress;
	}

	public void setInsiderAddress(String hotelAddress) {
		this.insiderAddress = hotelAddress;
	}

	public String getInsiderDesc() {
		return insiderDesc;
	}

	public void setInsiderDesc(String hotelDesc) {
		this.insiderDesc = hotelDesc;
	}

	public String getInsiderLati() {
		return insiderLati;
	}

	public void setInsiderLati(String hotelLati) {
		this.insiderLati = hotelLati;
	}

	public String getInsiderLong() {
		return insiderLong;
	}

	public void setInsiderLong(String hotelLong) {
		this.insiderLong = hotelLong;
	}

	public String getInsiderName() {
		return insiderName;
	}

	public void setInsiderName(String hotelName) {
		this.insiderName = hotelName;
	}

	public String getInsiderPriority() {
		return insiderPriority;
	}

	public void setInsiderPriority(String hotelPriority) {
		this.insiderPriority = hotelPriority;
	}

	public String getInsiderSequence() {
		return insiderSequence;
	}

	public void setInsiderSequence(String hotelSequence) {
		this.insiderSequence = hotelSequence;
	}

	public String getInsiderIsWhotel() {
		return insiderIsWhotel;
	}

	public void setInsiderIsWhotel(String hotelIsWhotel) {
		this.insiderIsWhotel = hotelIsWhotel;
	}

	public String getInsiderLandscape() {
		return insiderLandscape;
	}

	public void setInsiderLandscape(String hotelLandscape) {
		this.insiderLandscape = hotelLandscape;
	}

	public String getInsiderOriginal() {
		return insiderOriginal;
	}

	public void setInsiderOriginal(String hotelOriginal) {
		this.insiderOriginal = hotelOriginal;
	}

	public String getInsiderPageURL() {
		return insiderPageURL;
	}

	public void setInsiderPageURL(String hotelPageURL) {
		this.insiderPageURL = hotelPageURL;
	}

	public String getInsiderPortrait() {
		return insiderPortrait;
	}

	public void setInsiderPortrait(String hotelPortrait) {
		this.insiderPortrait = hotelPortrait;
	}

	public String getInsiderRectLarge() {
		return insiderRectLarge;
	}

	public void setInsiderRectLarge(String hotelRectLarge) {
		this.insiderRectLarge = hotelRectLarge;
	}

	public String getHotelInsidersmall() {
		return insiderRectsmall;
	}

	public void setInsiderRectsmall(String hotelRectsmall) {
		this.insiderRectsmall = hotelRectsmall;
	}

	// @Override
	// public String toString() {
	// return "CustomInsiderData{" +
	// "id='" + id + '\'' +
	// ", hotelName='" + hotelName + '\'' +
	// ", hotelAddress='" + hotelAddress + '\'' +
	// ", hotelCity='" + hotelCity + '\'' +
	// ", hotelState='" + hotelState + '\'' +
	// ", hotelCountry='" + hotelCountry + '\'' +
	// ", hotelCode='" + hotelCode + '\'' +
	// ", hotelContact='" + hotelContact + '\'' +
	// ", hotelCategory='" + hotelCategory + '\'' +
	// ", hotelDesc='" + hotelDesc + '\'' +
	// ", hotelLong='" + hotelLong + '\'' +
	// ", hotelLati='" + hotelLati + '\'' +
	// ", hotelOriginal='" + hotelOriginal + '\'' +
	// ", hotelLandscape='" + hotelLandscape + '\'' +
	// ", hotelPortrait='" + hotelPortrait + '\'' +
	// ", hotelRectsmall='" + hotelRectsmall + '\'' +
	// ", hotelLarge='" + hotelLarge + '\'' +
	// ", hotelIsWhotel='" + hotelIsWhotel + '\'' +
	// ", hotelPageURL='" + hotelPageURL + '\'' +
	// '}';
	// }

}