package com.hoteltrip.android.util;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class BookingTableActivity {
	private final String LOGTAG = "BookingsTable";
	private SQLiteDatabase database;
	private DataBaseHandler dbHelper;
	private Context context;

	public BookingTableActivity(Context mContext) throws IOException {
		dbHelper = new DataBaseHandler(mContext);
		context = mContext;
	}

	public void open() {
		try {
			// opening the db
			database = dbHelper.getWritableDatabase();
		} catch (Exception exception) {
			Log.e(LOGTAG, "open " + exception.toString());
		}
	}

	public void close() {
		try {
			// closing the db
			dbHelper.close();
		} catch (Exception exception) {
			Log.e(LOGTAG, "close " + exception.toString());
		}
	}

	public boolean isHotelFavorite(String bookingID) {
		Cursor cursor = null;
		try {
			// checking does hotel exist in db
			cursor = database.query(DataBaseHandler.TABLE_BOOKING,
					new String[] { DataBaseHandler.BOOKING_ID,
							DataBaseHandler.HOTEL_NAME },
					DataBaseHandler.BOOKING_ID + "='" + bookingID + "'", null,
					null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				return true;
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return false;
	}

	public void insertRows(String booking_id, String hotel_name,
			String checkindate, String checkoutdate, String pincode,
			String status, String hotel_address, String amount) {
		// inserting rows into db

		ContentValues value = new ContentValues();
		try {
			value.put(DataBaseHandler.BOOKING_ID, booking_id);
			value.put(DataBaseHandler.HOTEL_NAME, hotel_name);
			value.put(DataBaseHandler.CHECK_IN_DATE, checkindate);
			value.put(DataBaseHandler.CHECK_OUT_DATE, checkoutdate);
			value.put(DataBaseHandler.PINCODE, pincode);
			value.put(DataBaseHandler.STATUS, status);
			value.put(DataBaseHandler.HOTEL_ADDRESS, hotel_address);
			value.put(DataBaseHandler.AMOUNT, amount);

			database.insert(DataBaseHandler.TABLE_BOOKING, null, value);
			Log.d(LOGTAG, "insert data is " + hotel_name);

			Toast.makeText(context, "Hotel added to Bookings List",
					Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Log.e(LOGTAG, "insertRows " + ex.toString());
		}
	}

	public Cursor getAllRows() {
		Cursor cursor = null;
		try {

			// code to get all rows from db
			cursor = database.query(DataBaseHandler.TABLE_BOOKING, null, null,
					null, null, null, null);
		} catch (Exception ex) {
			Log.e(LOGTAG, "getAllRows " + ex.toString());

		}
		return cursor;
	}

	int deleteId;

	// public void deleteRow(String booking_id) {
	// try {
	// // deleting row from db
	// deleteId = database.delete(DataBaseHandler.TABLE_BOOKING,
	// DataBaseHandler.BOOKING_ID + "=?",
	// new String[] { booking_id });
	// Log.d(LOGTAG, "delete id " + deleteId);
	// } catch (Exception e) {
	// // TODO: handle exception
	// Log.d(LOGTAG, "delete id " + booking_id);
	// Log.e(LOGTAG, "delete FavouriteTable " + e.toString());
	// }
	// }
}
