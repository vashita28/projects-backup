package com.example.cabapppassenger.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

	public static final String TABLE_MY_FAVOURITES = "table_myfavourites";
	public static final String TABLE_RECENT = "table_recent";
	public static final String ID = "_ID"; // 1
	public static final String EMAIL = "email";// 2
	public static final String ADDRESS = "address";// 3
	public static final String POSTCODE = "postcode";// 4
	public static final String ACCESSTOKEN = "accessToken";// 5

	private final String CREATE_FAVOURITES_TABLE = "CREATE TABLE "
			+ TABLE_MY_FAVOURITES + "(" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + EMAIL + " TEXT,"
			+ ADDRESS + " TEXT," + POSTCODE + " TEXT," + ACCESSTOKEN + " TEXT"
			+ ")";

	private final String CREATE_RECENT_TABLE = "CREATE TABLE " + TABLE_RECENT
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + EMAIL
			+ " TEXT," + ADDRESS + " TEXT," + POSTCODE + " TEXT," + ACCESSTOKEN
			+ " TEXT" + ")";

	public DataBaseHandler(Context context) {
		super(context, "CabAppPassenger", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_RECENT_TABLE);
		db.execSQL(CREATE_FAVOURITES_TABLE);

	}

	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DataBaseHandler.TABLE_MY_FAVOURITES, null, null);
		db.delete(DataBaseHandler.TABLE_RECENT, null, null);
		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}