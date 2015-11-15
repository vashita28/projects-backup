package com.example.cabapppassenger.database;

import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MyFavouritesTable {

	private final String LOGTAG = "MyFavourites";
	private SQLiteDatabase database;
	private DataBaseHandler dbHelper;
	int deleteId;
	Context mContext;

	public MyFavouritesTable(Context context) throws IOException {
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

	public void insert_myfavourites(String email, String accessToken,
			String address, String postcode) {
		// inserting rows into db

		ContentValues value = new ContentValues();
		try {
			value.put(DataBaseHandler.EMAIL, email);
			value.put(DataBaseHandler.ACCESSTOKEN, accessToken);
			value.put(DataBaseHandler.ADDRESS, address);
			value.put(DataBaseHandler.POSTCODE, postcode);

			database.insertOrThrow(DataBaseHandler.TABLE_MY_FAVOURITES, null,
					value);
			Log.d(LOGTAG, "Inserted data is " + email + " " + address + " "
					+ postcode);

		} catch (Exception ex) {
			Log.e(LOGTAG, "Error in adding favs " + ex.toString());
		}
	}

	public Cursor getAllRows() {
		Cursor cursor = null;
		try {
			// code to get all rows from db
			cursor = database.query(DataBaseHandler.TABLE_MY_FAVOURITES, null,
					null, null, null, null, null);
		} catch (Exception ex) {
			Log.e(LOGTAG, "getAllRows " + ex.toString());

		}
		return cursor;
	}

	public boolean isValuationSaved(String address) {
		Cursor cursor = null;
		try {
			// checking does address exist in db
			cursor = database.query(DataBaseHandler.TABLE_MY_FAVOURITES,
					new String[] { DataBaseHandler.ADDRESS,
							DataBaseHandler.POSTCODE }, DataBaseHandler.ADDRESS
							+ "='" + address + "'", null, null, null, null);
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
			deleteId = database.delete(DataBaseHandler.TABLE_MY_FAVOURITES,
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

	// public void updateItem(int valuation_id, int user_id, String itemname,
	// String itemtype, String sub_category, String condition,
	// String additional_info, String reason, String status, String store,
	// String submitted_date, String valuation_date, String expiry_date,
	// Double amount) {
	//
	// ContentValues value = new ContentValues();
	// try {
	// value.put(DataBaseHandler.VALUATION_ID, valuation_id);
	// value.put(DataBaseHandler.USER_ID, user_id);
	// value.put(DataBaseHandler.ITEM_NAME, itemname);
	// value.put(DataBaseHandler.ITEM_TYPE, itemtype);
	// value.put(DataBaseHandler.SUB_CATEGORY_TYPE, sub_category);
	// value.put(DataBaseHandler.CONDITION_TYPE, condition);
	// value.put(DataBaseHandler.ADDITIONAL_INFO, additional_info);
	// value.put(DataBaseHandler.REASON, reason);
	// value.put(DataBaseHandler.STATUS, status);
	// value.put(DataBaseHandler.SUBMITTED_DATE, submitted_date);
	// value.put(DataBaseHandler.VALUATION_DATE, valuation_date);
	// value.put(DataBaseHandler.EXPIRY_DATE, expiry_date);
	// value.put(DataBaseHandler.AMOUNT, amount);
	// database.update(DataBaseHandler.TABLE_SAVE_VALUATION, value,
	// " valuation_id=" + valuation_id, null);
	// Log.d(LOGTAG, "updated data is " + valuation_id);
	// } catch (Exception ex) {
	// Log.e(LOGTAG, "updated " + ex.toString());
	// }
	// }
}