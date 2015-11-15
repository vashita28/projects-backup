package com.hoteltrip.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

	public static final String TABLE_FAVS = "table_favHotel";
	public static final String ID = "_ID"; // 0
	public static final String HOTEL_ID = "hotelID"; // 1
	public static final String NAME = "hotelName"; // 2
	public static final String IMAGEURL = "imageurl";// 3
	public static final String ADDRESS = "hotelAddress";// 4
	public static final String RATING_STAR = "hotelrating";// 5
	public static final String PRICE = "hotelprice";// 6
	public static final String REVIEW_COUNT = "reviewcount";// 7
	public static final String REVIEW_URL = "reviewurl";// 8
	public static final String REVIEW_IMAGEURL = "reviewimgurl";// 9
	public static final String RANKING = "hotelranking";// 10
	public static final String CURRENCY = "currency";// 10

	/* ---------------------- FAVOURITE TABLE ---------------------- */

	// creating table my favs
	private final String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVS
			+ "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + HOTEL_ID
			+ " TEXT," + NAME + " TEXT," + IMAGEURL + " TEXT," + ADDRESS
			+ " TEXT," + RATING_STAR + " TEXT," + PRICE + " TEXT,"
			+ REVIEW_COUNT + " TEXT," + REVIEW_URL + " TEXT," + REVIEW_IMAGEURL
			+ " TEXT," + RANKING + " TEXT," + CURRENCY + " TEXT" + ")";

	/*---------------------------BOOKINGS TABLE-------------*/

	public static final String TABLE_BOOKING = "table_booking";
	public static final String BOOKING_ID = "bookingID"; // 1
	public static final String HOTEL_NAME = "hotelName"; // 2
	public static final String CHECK_IN_DATE = "checkindate";// 3
	public static final String CHECK_OUT_DATE = "checkoutdate";// 4
	public static final String PINCODE = "pincode";// 5
	public static final String STATUS = "status";// 6
	public static final String HOTEL_ADDRESS = "address";// 7
	public static final String AMOUNT = "amount";// 8

	// creating table my BOOKINGS
	private final String CREATE_BOOKINGS_TABLE = "CREATE TABLE "
			+ TABLE_BOOKING + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ BOOKING_ID + " TEXT," + HOTEL_NAME + " TEXT," + CHECK_IN_DATE
			+ " TEXT," + CHECK_OUT_DATE + " TEXT," + PINCODE + " TEXT,"
			+ STATUS + " TEXT," + HOTEL_ADDRESS + " TEXT," + AMOUNT + " TEXT"
			+ ")";

	public DataBaseHandler(Context context) {
		super(context, "Favourites_Hotel", null, 2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_FAVORITES_TABLE);
		db.execSQL(CREATE_BOOKINGS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}