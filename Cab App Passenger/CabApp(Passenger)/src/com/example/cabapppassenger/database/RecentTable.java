package com.example.cabapppassenger.database;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecentTable {

	private final String LOGTAG = "Recent";
	private SQLiteDatabase database;
	private DataBaseHandler dbHelper;
	int deleteId;
	Context mContext;

	public RecentTable(Context context) throws IOException {
		dbHelper = new DataBaseHandler(context);
		mContext = context;
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

	public void insert_recent(String email, String accessToken, String address,
			String postcode) {
		// inserting rows into db

		ContentValues value = new ContentValues();
		try {
			value.put(DataBaseHandler.EMAIL, email);
			value.put(DataBaseHandler.ACCESSTOKEN, accessToken);
			value.put(DataBaseHandler.ADDRESS, address);
			value.put(DataBaseHandler.POSTCODE, postcode);

			database.insertOrThrow(DataBaseHandler.TABLE_RECENT, null, value);
			Log.d(LOGTAG, "Inserted data is " + email + " " + address + " "
					+ postcode);

		} catch (Exception ex) {
			Log.e(LOGTAG, "Error in adding recent " + ex.toString());
		}
	}

	public Cursor getAllRows() {
		Cursor cursor = null;
		try {
			// code to get all rows from db
			cursor = database.query(DataBaseHandler.TABLE_RECENT, null, null,
					null, null, null, null);
		} catch (Exception ex) {
			Log.e(LOGTAG, "getAllRows " + ex.toString());

		}
		return cursor;
	}

	public boolean isrecentAdded(String address) {
		Cursor cursor = null;
		try {
			// checking does address exist in db
			cursor = database.query(DataBaseHandler.TABLE_RECENT, new String[] {
					DataBaseHandler.ADDRESS, DataBaseHandler.POSTCODE },
					DataBaseHandler.ADDRESS + "='" + address + "'", null, null,
					null, null);
			if (cursor != null && cursor.moveToFirst()) {
				return true;
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return false;
	}

	public void deleteRow(String address) {
		try {
			// deleting address from db
			deleteId = database.delete(DataBaseHandler.TABLE_RECENT,
					DataBaseHandler.ADDRESS + "=?", new String[] { address });
			Log.d(LOGTAG, "deleted address is " + deleteId + " " + address);
		} catch (Exception e) {
			// TODO: handle exception
			Log.d(LOGTAG, "deleted address is " + address);
			Log.e(LOGTAG, "delete additional info " + e.toString());
		}
	}

	// public List<ValuationGetterSetter> getdetails_aboutvaluation(int
	// position) {
	// Cursor cursor = null;
	// try {
	// // checking does valuation exist in db
	// cursor = database.query(DataBaseHandler.TABLE_SAVE_VALUATION,
	// new String[] { DataBaseHandler.VALUATION_ID,
	// DataBaseHandler.USER_ID },
	// DataBaseHandler.VALUATION_ID + "='" + position + "'", null,
	// null, null, null);
	// if (cursor != null && cursor.moveToFirst()) {
	// }
	// } finally {
	// if (cursor != null)
	// cursor.close();
	// }
	// return templistt;
	// }

	// public void delete_db() {
	// database.delete(DataBaseHandler.TABLE_SAVE_VALUATION, null, null);
	// }

	public void updateItem(String email, String accessToken, String address,
			String postcode) {

		ContentValues value = new ContentValues();
		try {
			value.put(DataBaseHandler.EMAIL, email);
			value.put(DataBaseHandler.ACCESSTOKEN, accessToken);
			value.put(DataBaseHandler.ADDRESS, address);
			value.put(DataBaseHandler.POSTCODE, postcode);

			database.update(DataBaseHandler.TABLE_RECENT, value, " email="
					+ email, null);
			Log.d(LOGTAG, "updated data is " + email);
		} catch (Exception ex) {
			Log.e(LOGTAG, "updated " + ex.toString());
		}
	}
}