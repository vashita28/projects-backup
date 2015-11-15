package com.hoteltrip.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hoteltrip.android.adapters.BookingDataAdapter;
import com.hoteltrip.android.util.BookingGetterSetter;
import com.hoteltrip.android.util.DataBaseHandler;

public class MyBookingActivity extends BaseActivity {

	ListView resultsListView;
	List<BookingGetterSetter> bookingList = new ArrayList<BookingGetterSetter>();
	BookingDataAdapter adapter;
	Context mContext = this;
	DataBaseHandler dbHelper;
	BookingGetterSetter book;
	TextView tv_nobookingdone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mybooking);
		super.init("My Booking");
		btnMap.setVisibility(View.GONE);
		btnBack.setVisibility(View.VISIBLE);
		resultsListView = (ListView) findViewById(R.id.mybookingList);

		getAllBookedHotels();

		if (bookingList != null && bookingList.size() > 0) {
			// if list is not null then iterate
			adapter = new BookingDataAdapter(mContext, bookingList);
			resultsListView.setAdapter(adapter);
		} else {
			// if list is null show no booking text
			resultsListView.setAdapter(null);
			tv_nobookingdone = (TextView) findViewById(R.id.tv_nobookingdone);
			tv_nobookingdone.setVisibility(View.VISIBLE);
		}

	}

	public List<BookingGetterSetter> getAllBookedHotels() {

		// Select All Query.
		String selectQuery = "SELECT  * FROM " + DataBaseHandler.TABLE_BOOKING;

		dbHelper = new DataBaseHandler(mContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor != null && cursor.moveToFirst()) {
			Log.i("getAllBookings", "List Size:: " + cursor.getCount());
			do {
				book = new BookingGetterSetter();
				book.setBooking_id(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.BOOKING_ID)));

				book.setHotel_name(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.HOTEL_NAME)));
				book.setPincode(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.PINCODE)));
				book.setAmount(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.AMOUNT)));
				book.setCheckindate(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.CHECK_IN_DATE)));
				book.setCheckoutdate(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.CHECK_OUT_DATE)));
				book.setHotel_address(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.HOTEL_ADDRESS)));
				book.setPayment_status(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.STATUS)));

				// Adding hotels to list
				bookingList.add(book);

			} while (cursor.moveToNext());

		}

		return bookingList;
	}
}
