package com.hoteltrip.android.util;

public class BookingGetterSetter {

	String hotel_name;
	String payment_status;
	String hotel_address;
	String amount;
	String checkindate;
	String checkoutdate;
	String booking_id;
	String pincode;

	public BookingGetterSetter() {

	}

	public BookingGetterSetter(String bookingid, String hotel_name,
			String hoteladdress, String amount, String checkindate,
			String checkoutdate, String paymentstatus, String pincode) {
		// TODO Auto-generated constructor stub
		this.hotel_name = hotel_name;
		this.payment_status = paymentstatus;
		this.hotel_address = hoteladdress;
		this.amount = amount;
		this.checkindate = checkindate;
		this.checkoutdate = checkoutdate;
		this.booking_id = bookingid;
		this.pincode = pincode;
	}

	public String getHotel_address() {
		return hotel_address;
	}

	public void setHotel_address(String hotel_address) {
		this.hotel_address = hotel_address;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCheckindate() {
		return checkindate;
	}

	public void setCheckindate(String checkindate) {
		this.checkindate = checkindate;
	}

	public String getCheckoutdate() {
		return checkoutdate;
	}

	public void setCheckoutdate(String checkoutdate) {
		this.checkoutdate = checkoutdate;
	}

	public String getHotel_name() {
		return hotel_name;
	}

	public void setHotel_name(String hotel_name) {
		this.hotel_name = hotel_name;
	}

	public String getPayment_status() {
		return payment_status;
	}

	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}

	public String getBooking_id() {
		return booking_id;
	}

	public void setBooking_id(String booking_id) {
		this.booking_id = booking_id;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

}
