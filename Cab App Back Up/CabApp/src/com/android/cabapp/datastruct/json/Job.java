package com.android.cabapp.datastruct.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;

public class Job {

	@Expose
	private String id;
	@Expose
	private String name;
	@Expose
	private String mobileNumber;
	@Expose
	private String internationalCode;
	@Expose
	private Integer numberOfPassengers;
	@Expose
	private String paymentType;
	@Expose
	private String pickupDateTime;
	@Expose
	private Location pickupLocation;
	@Expose
	private Location dropLocation;
	@Expose
	private String note;
	@Expose
	private String cabShare;
	@Expose
	private String wheelchairAccessRequired;
	@Expose
	private String isHearingImpaired;
	@Expose
	private String fixedPriceAmount;
	@Expose
	private Object completedAt;
	@Expose
	private String cancelled;
	@Expose
	private String amount;
	@Expose
	private String clientpaid;
	@Expose
	private String emailAddress;
	@Expose
	private String cabMiles;
	@Expose
	private String isNoshow;
	@Expose
	private String status;

	@Expose
	private List<Card> card = new ArrayList<Card>();
	@Expose
	private List<JsonArray> messages = new ArrayList<JsonArray>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCabMiles() {
		return cabMiles;
	}

	public void setCabMiles(String cabMiles) {
		this.cabMiles = cabMiles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getInternationalCode() {
		return internationalCode;
	}

	public void setInternationalCode(String internationalCode) {
		this.internationalCode = internationalCode;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getNumberOfPassengers() {
		return numberOfPassengers;
	}

	public void setNumberOfPassengers(Integer numberOfPassengers) {
		this.numberOfPassengers = numberOfPassengers;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPickupDateTime() {
		return pickupDateTime;
	}

	public void setPickupDateTime(String pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}

	public Location getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(Location location) {
		this.pickupLocation = location;
	}

	public Location getDropLocation() {
		return dropLocation;
	}

	public void setDropLocation(Location dropLocation) {
		this.dropLocation = dropLocation;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCabShare() {
		return cabShare;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public void setCabShare(String cabShare) {
		this.cabShare = cabShare;
	}

	public String getWheelchairAccessRequired() {
		return wheelchairAccessRequired;
	}

	public void setWheelchairAccessRequired(String wheelchairAccessRequired) {
		this.wheelchairAccessRequired = wheelchairAccessRequired;
	}

	public String getIsHearingImpaired() {
		return isHearingImpaired;
	}

	public void setIsHearingImpaired(String isHearingImpaired) {
		this.isHearingImpaired = isHearingImpaired;
	}

	public String getFixedPriceAmount() {
		return fixedPriceAmount;
	}

	public void setFixedPriceAmount(String fixedPriceAmount) {
		this.fixedPriceAmount = fixedPriceAmount;
	}

	public Object getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Object completedAt) {
		this.completedAt = completedAt;
	}

	public String getCancelled() {
		return cancelled;
	}

	public void setCancelled(String cancelled) {
		this.cancelled = cancelled;
	}

	public String getClientpaid() {
		return clientpaid;
	}

	public void setClientpaid(String clientpaid) {
		this.clientpaid = clientpaid;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getIsNoshow() {
		return isNoshow;
	}

	public void setIsNoshow(String isNoshow) {
		this.isNoshow = isNoshow;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<JsonArray> getMessages() {
		return messages;
	}

	public void setMessages(List<JsonArray> messages) {
		this.messages = messages;
	}

	public List<Card> getCard() {
		return card;
	}

	public void setCard(List<Card> card) {
		this.card = card;
	}

}