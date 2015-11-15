package com.example.cabapppassenger.datastruct.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class BookingsList {

	@Expose
	private List<Bookings> bookings = new ArrayList<Bookings>();

	public List<Bookings> getBookings() {
		return bookings;
	}

	public void setBookings(List<Bookings> bookings) {
		this.bookings = bookings;
	}

}