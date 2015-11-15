package com.hoteltrip.android.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
	private Context mycontext;

	private String DB_PATH = "/data/data/com.hoteltrip.android/databases/";
	// private String DB_PATH =
	// mycontext.getApplicationContext().getPackageName()+"/databases/";
	private static String DB_NAME = "codes.db";// the extension may be
												// .sqlite or .db
	public SQLiteDatabase myDataBase;

	/*
	 * private String DB_PATH = "/data/data/" +
	 * mycontext.getApplicationContext().getPackageName() + "/databases/";
	 */

	public DataBaseHelper(Context context) throws IOException {
		super(context, DB_NAME, null, 1);
		this.mycontext = context;
		boolean dbexist = checkdatabase();
		if (dbexist) {
			// System.out.println("Database exists");
			opendatabase();
		} else {
			System.out.println("Database doesn't exist");
			createdatabase();
		}

	}

	public void createdatabase() throws IOException {
		boolean dbexist = checkdatabase();
		if (dbexist) {
			// System.out.println(" Database exists.");
		} else {
			this.getReadableDatabase();
			try {
				copydatabase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkdatabase() {
		boolean checkdb = false;
		try {
			String myPath = DB_PATH + DB_NAME;
			File dbfile = new File(myPath);
			checkdb = dbfile.exists();
		} catch (SQLiteException e) {
			System.out.println("Database doesn't exist");
		}

		return checkdb;
	}

	private void copydatabase() throws IOException {

		InputStream myinput = mycontext.getAssets().open(DB_NAME);
		String outfilename = DB_PATH + DB_NAME;

		OutputStream myoutput = new FileOutputStream(outfilename);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myinput.read(buffer)) > 0) {
			myoutput.write(buffer, 0, length);
		}

		// Close the streams
		myoutput.flush();
		myoutput.close();
		myinput.close();

	}

	public void opendatabase() throws SQLException {
		// Open the database
		String mypath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(mypath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	public synchronized void close() {
		if (myDataBase != null) {
			myDataBase.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public String[] getAllCountriesAndCitiesCombination() {
		Cursor cursor = myDataBase.query("codes", new String[] { "CityName",
				"CountryName" }, null, null, null, null, null);

		if (cursor.getCount() > 0) {
			String[] str = new String[cursor.getCount()];
			int i = 0;

			while (cursor.moveToNext()) {
				str[i] = cursor.getString(cursor.getColumnIndex("CityName"))
						+ ", "
						+ cursor.getString(cursor.getColumnIndex("CountryName"));
				i++;
			}
			return str;
		} else {
			return new String[] {};
		}
	}
}