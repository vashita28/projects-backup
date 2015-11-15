package com.android.cabapp.datastruct.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class DriverDetails {

	@Expose
	private String workingCity;
	@Expose
	private String firstname;
	@Expose
	private String surname;
	@Expose
	private String email;
	@Expose
	private String username;
	@Expose
	private String cityId;
	@Expose
	private String badgeColour;
	@Expose
	private String badgeNumber;
	@Expose
	private String badgeExpiration;
	@Expose
	private String sector;
	@Expose
	private String mobileNumber;
	@Expose
	private String internationalCode;
	@Expose
	private String paymentType;
	@Expose
	private Vehicle vehicle;
	@Expose
	private Integer credits;
	@Expose
	private String wheelchairAccess;
	@Expose
	private Integer passengerCapacity;
	@Expose
	private String availability;
	@Expose
	private String billingAddress1;
	@Expose
	private String billingAddress2;
	@Expose
	private String billingCity;
	@Expose
	private String billingCounty;
	@Expose
	private String billingPostcode;
	@Expose
	private List<Document> document = new ArrayList<Document>();
	@Expose
	private BankDetails bankDetails;
	@Expose
	private List<Cards> cards = new ArrayList<Cards>();

	public String getWorkingCity() {
		return workingCity;
	}

	public void setWorkingCity(String workingCity) {
		this.workingCity = workingCity;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getBadgeColour() {
		return badgeColour;
	}

	public void setBadgeColour(String badgeColour) {
		this.badgeColour = badgeColour;
	}

	public String getBadgeNumber() {
		return badgeNumber;
	}

	public void setBadgeNumber(String badgeNumber) {
		this.badgeNumber = badgeNumber;
	}

	public String getBadgeExpiration() {
		return badgeExpiration;
	}

	public void setBadgeExpiration(String badgeExpiration) {
		this.badgeExpiration = badgeExpiration;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getInternationalCode() {
		return internationalCode;
	}

	public void setInternationalCode(String internationalCode) {
		this.internationalCode = internationalCode;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Integer getCredits() {
		return credits;
	}

	public void setCredits(Integer credits) {
		this.credits = credits;
	}

	public String getWheelchairAccess() {
		return wheelchairAccess;
	}

	public void setWheelchairAccess(String wheelchairAccess) {
		this.wheelchairAccess = wheelchairAccess;
	}

	public Integer getPassengerCapacity() {
		return passengerCapacity;
	}

	public void setPassengerCapacity(Integer passengerCapacity) {
		this.passengerCapacity = passengerCapacity;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getBillingAddress1() {
		return billingAddress1;
	}

	public void setBillingAddress1(String billingAddress1) {
		this.billingAddress1 = billingAddress1;
	}

	public String getBillingAddress2() {
		return billingAddress2;
	}

	public void setBillingAddress2(String billingAddress2) {
		this.billingAddress2 = billingAddress2;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingCounty() {
		return billingCounty;
	}

	public void setBillingCounty(String billingCounty) {
		this.billingCounty = billingCounty;
	}

	public String getBillingPostcode() {
		return billingPostcode;
	}

	public void setBillingPostcode(String billingPostcode) {
		this.billingPostcode = billingPostcode;
	}

	public List<Document> getDocument() {
		return document;
	}

	public void setDocument(List<Document> document) {
		this.document = document;
	}

	public BankDetails getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(BankDetails bankDetails) {
		this.bankDetails = bankDetails;
	}

	public List<Cards> getCards() {
		return cards;
	}

	public void setCards(List<Cards> cards) {
		this.cards = cards;
	}

	public class Document {

		@Expose
		private String id;
		@Expose
		private String filename;
		@Expose
		private String thumb;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getThumbnail() {
			return thumb;
		}

		public void setThumbnail(String thumb) {
			this.thumb = thumb;
		}
	}

	public class BankDetails {

		@Expose
		private String name;
		@Expose
		private String accountName;
		@Expose
		private String accountNumber;
		@Expose
		private String sortCode;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAccountName() {
			return accountName;
		}

		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}

		public String getSortCode() {
			return sortCode;
		}

		public void setSortCode(String sortCode) {
			this.sortCode = sortCode;
		}

	}

	public class Vehicle {

		@Expose
		private String registration;
		@Expose
		private String make;
		@Expose
		private String model;
		@Expose
		private String colour;

		public String getRegistration() {
			return registration;
		}

		public void setRegistration(String registration) {
			this.registration = registration;
		}

		public String getMake() {
			return make;
		}

		public void setMake(String make) {
			this.make = make;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public String getColour() {
			return colour;
		}

		public void setColour(String colour) {
			this.colour = colour;
		}

	}

	public class Cards {

		@Expose
		private String brand;
		@Expose
		private String driverCardId;
		@Expose
		private String isSelected;
		@Expose
		private String truncatedPan;
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

		public String getDriverCardId() {
			return driverCardId;
		}

		public void setDriverCardId(String driverCardId) {
			this.driverCardId = driverCardId;
		}

		public String getTruncatedPan() {
			return truncatedPan;
		}

		public String getIsSelected() {
			return isSelected;
		}

		public void setIsSelected(String isSelected) {
			this.isSelected = isSelected;
		}

		public void setTruncatedPan(String truncatedPan) {
			this.truncatedPan = truncatedPan;
		}
	}

}
