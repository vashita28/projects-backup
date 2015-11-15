package co.uk.peeki.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import co.uk.peeki.ui.DataModelPhoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqLiteHelper extends SQLiteOpenHelper {

	private static String TAG = "MYSQLITEHELPER";
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "Peeki";
	// Table Name
	private static final String TABLE_PHOTOS = "photos";
	// Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_PHOTO_PATH = "photopath";
	private static final String KEY_LOCKED = "locked";
	private static final String KEY_DATE = "date";

	public MySqLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);// super(context,
	}

	public MySqLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_TABLE_PHOTOS = "CREATE TABLE " + TABLE_PHOTOS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ KEY_PHOTO_PATH + " TEXT, " + KEY_LOCKED + " BOOL, "
				+ KEY_DATE + " DATE " + ")";

		db.execSQL(CREATE_TABLE_PHOTOS);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS galleryphotos");

		// create fresh galleryphotos table
		this.onCreate(db);

	}

	public void addPhoto(DataModelPhoto photo) {
		// for logging
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		// values.put(KEY_ID, hotel.getId());
		values.put(KEY_PHOTO_PATH, photo.getImagePath()); // get imagepath
		values.put(KEY_LOCKED, photo.isBlocked()); // get isBlocked
		values.put(KEY_DATE, photo.getDate().toString());// get date

		Cursor cursor = null;
		try {
			cursor = db.query(TABLE_PHOTOS, null, KEY_PHOTO_PATH + " = ?",
					new String[] { photo.getImagePath() }, null, null, null);
			if (cursor != null && cursor.moveToFirst() == false) {
				if (photo != null)
					Log.d(TAG, "******DATABASE addPhoto**********");
				db.insert(TABLE_PHOTOS, null, values);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}

		// db.insert(TABLE_PHOTOS, // table
		// null, // nullColumnHack
		// values); // key/value -> keys = column names/ values
		// // = column
		// // values

		// 4. close
		db.close();
	}

	// Get all Photos
	public List<DataModelPhoto> getAllPhotos() {
		List<DataModelPhoto> photos = new LinkedList<DataModelPhoto>();

		// 1. build the query
		String query = "SELECT  * FROM " + TABLE_PHOTOS;

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		// 3. go over each row, build book and add it to list
		DataModelPhoto photo = null;
		if (cursor != null && cursor.moveToFirst()) {
			do {

				photo = new DataModelPhoto();
				photo.setId(Integer.parseInt(cursor.getString(0)));
				photo.setImagePath(cursor.getString(1));
				photo.setBlocked(cursor.getInt(2) > 0);
				photo.setDate(cursor.getString(3));
				Log.v(TAG,
						"Get all data ::Boolean value :: "
								+ cursor.getString(2) + "boolean value:: 2   "
								+ (cursor.getInt(2) > 0) + "   Date "
								+ cursor.getString(3));
				// Add each photo to list(photos)
				photos.add(photo);
			} while (cursor.moveToNext());

			// cursor.close();
		}
		db.close();
		// return all photos
		return photos;
	}
	// Get all Photos
	public ArrayList<String> getTotalPhotos() {
		ArrayList<String> photos = new ArrayList<String>();

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_PHOTOS, null, null, null,
				null, null, "date" + " DESC");

		// 3. go over each row, build book and add it to list
		// DataModelPhoto photo = null;
		if (cursor != null && cursor.moveToFirst()) {
			do {
				photos.add(cursor.getString(1));
			} while (cursor.moveToNext());
			// cursor.close();
		}
		db.close();
		// return all photos
		return photos;
	}

	// Get blocked Photos
	public ArrayList<String> getAllBlockedPhotos() {
		ArrayList<String> photos = new ArrayList<String>();

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_PHOTOS, null, KEY_LOCKED + "='1'", null,
				null, null, "date" + " DESC");

		// 3. go over each row, build book and add it to list
		// DataModelPhoto photo = null;
		if (cursor != null && cursor.moveToFirst()) {
			do {
				photos.add(cursor.getString(1));
			} while (cursor.moveToNext());
			// cursor.close();
		}
		db.close();
		// return all photos
		return photos;
	}

	public ArrayList<String> getAllUnblockedPhotos() {
		ArrayList<String> photos = new ArrayList<String>();

		// 2. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_PHOTOS, null, KEY_LOCKED + "='0'", null,
				null, null, "date" + " DESC");

		// 3. go over each row, build book and add it to list
		// DataModelPhoto photo = null;
		if (cursor != null && cursor.moveToFirst()) {
			do {
				photos.add(cursor.getString(1));
			} while (cursor.moveToNext());
			// cursor.close();
		}
		db.close();
		// return all photos
		return photos;
	}

	// Get each photo
	public Cursor query() {
		// get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_PHOTOS, null, null, null, null, null,
				null);
		return cursor;
	}

	public int delete(String imagePath) {
		// get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		int deleted = db.delete(TABLE_PHOTOS, KEY_PHOTO_PATH + " = ?",
				new String[] { imagePath });
		db.close();
		return deleted;
	}

	// Update
	public int updatePhotos(String imagePath, boolean isSelected) {
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_LOCKED, isSelected);

		// 3. updating row
		int i = db.update(TABLE_PHOTOS, // table
				values, // column/value
				KEY_PHOTO_PATH + " = ?", // selections
				new String[] { imagePath }); // selection
												// args

		// 4. close
		db.close();

		Log.v(TAG, "Upadation in database  " + i);

		return i;
	}

	// Delete all records:
	public int deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(TABLE_PHOTOS, null, null);
	}

}
