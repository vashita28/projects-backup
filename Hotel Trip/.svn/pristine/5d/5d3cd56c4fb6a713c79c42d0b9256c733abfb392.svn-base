package com.hoteltrip.android.util;

import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class FavouriteTableActivity {

	private final String LOGTAG = "FavoritesTable";
	private SQLiteDatabase database;
	private DataBaseHandler dbHelper;
	private Context context;

	public FavouriteTableActivity(Context mContext) throws IOException {
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

	public void insertRows(String hotel_id, String hotel_name,
			String hotel_address, String imageURL, String price_text,
			Float rating, String review_image_url, String review_count,
			String currency) {
		// inserting rows into db

		ContentValues value = new ContentValues();
		try {
			value.put(DataBaseHandler.HOTEL_ID, hotel_id);
			value.put(DataBaseHandler.NAME, hotel_name);

			value.put(DataBaseHandler.IMAGEURL, imageURL);
			value.put(DataBaseHandler.ADDRESS, hotel_address);
			value.put(DataBaseHandler.RATING_STAR, rating);
			value.put(DataBaseHandler.PRICE, price_text);
			value.put(DataBaseHandler.REVIEW_IMAGEURL, review_image_url);
			value.put(DataBaseHandler.REVIEW_COUNT, review_count);
			value.put(DataBaseHandler.CURRENCY, currency);

			database.insert(DataBaseHandler.TABLE_FAVS, null, value);
			Log.d(LOGTAG, "insert data is " + hotel_name);

			Toast.makeText(context, "Hotel added to Favourites",
					Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Log.e(LOGTAG, "insertRows " + ex.toString());
		}
	}

	public Cursor getAllRows() {
		Cursor cursor = null;
		try {

			// code to get all rows from db
			cursor = database.query(DataBaseHandler.TABLE_FAVS, null, null,
					null, null, null, null);
		} catch (Exception ex) {
			Log.e(LOGTAG, "getAllRows " + ex.toString());

		}
		return cursor;
	}

	public boolean isHotelFavorite(String hotelID) {
		Cursor cursor = null;
		try {
			// checking does hotel exist in db
			cursor = database.query(DataBaseHandler.TABLE_FAVS, new String[] {
					DataBaseHandler.HOTEL_ID, DataBaseHandler.NAME },
					DataBaseHandler.HOTEL_ID + "='" + hotelID + "'", null,
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

	int deleteId;

	public void deleteRow(String hotel_id) {
		try {
			// deleting row from db
			deleteId = database.delete(DataBaseHandler.TABLE_FAVS,
					DataBaseHandler.HOTEL_ID + "=?", new String[] { hotel_id });
			Log.d(LOGTAG, "delete id " + deleteId);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d(LOGTAG, "delete id " + hotel_id);
			Log.e(LOGTAG, "delete FavouriteTable " + e.toString());
		}
	}

}
