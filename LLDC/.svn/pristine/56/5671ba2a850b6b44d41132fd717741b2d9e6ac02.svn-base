package co.uk.android.lldc.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import co.uk.android.lldc.models.DashboardModel;
import co.uk.android.lldc.models.FacilityModel;
import co.uk.android.lldc.models.MediaModel;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.models.TheParkModel;
import co.uk.android.lldc.models.ThingsToDoModel;
import co.uk.android.lldc.models.TrailsWayPointModel;
import co.uk.android.lldc.tablet.LLDCApplication;

public class LLDCDataBaseHelper extends SQLiteOpenHelper {

	// String _id, name, shortDesc, longDescription, venueId, venueTitle,
	// thumbImage, largeImage, flag, isToday, activeDays, category, color,
	// order;
	//
	// long latitude, longitude, startDateTime, endDateTime;
	//
	// ArrayList<MediaModel> mediaList;

	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_EVENTS = "events";
	public static final String TABLE_VENUES = "venues";
	public static final String TABLE_TRAILS = "trails";
	public static final String TABLE_MEDIA = "media";
	public static final String TABLE_DASHBOARD = "dashboard";
	public static final String TABLE_STATIC_TRAILS = "staticTrails";
	public static final String TABLE_TRAILS_WAYPOINT = "waypointTrails";
	public static final String TABLE_THINGS_TO_DO = "thingsToDO";
	public static final String TABLE_THE_PARK = "thePark";
	public static final String TABLE_VENUE_FACILITIES = "venueFacilities";

	public static final String TABLE_FACILITIES = "facilities";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_SHORT_DESC = "shortDesc";
	public static final String COLUMN_LONG_DESC = "longDescription";
	public static final String COLUMN_VENUE_ID = "venueId";
	public static final String COLUMN_VENUE_TITLE = "venueTitle";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LONG = "long";
	public static final String COLUMN_START_DATE_TIME = "startDateTime";
	public static final String COLUMN_END_DATE_TIME = "endDateTime";
	public static final String COLUMN_THUMB_IMAGE = "thumbImage";
	public static final String COLUMN_LARGE_IMAGE = "largeImage";
	public static final String COLUMN_FLAG = "flag";
	public static final String COLUMN_IS_TODAY = "isToday";
	public static final String COLUMN_ACTIVE_DAYS = "activeDays";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_COLOR = "color";
	public static final String COLUMN_ORDER = "_order";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_ISLONDON2012TRAIL = "islondon2012trails";
	public static final String COLUMN_EVENT_COUNT = "event_count";
	public static final String COLUMN_MODEL_TYPE = "modelType";
	public static final String COLUMN_EVENT_SOCIAL_FLAG = "socialFlag";
	public static final String COLUMN_EVENT_SOCIAL_HANDLE = "socialhandle";
	public static final String COLUMN_EXCLUDEDFROM_SEARCH = "excludeFromSearch";
	private static final String COLUMN_TRAILS_ROUTEID = "routeId";
	public static final String COLUMN_TRAILS_START_LAT = "startTrailLat";
	public static final String COLUMN_TRAILS_START_LAN = "startTrailLong";
	public static final String COLUMN_TRAILS_END_LAT = "endTrailLat";
	private static final String COLUMN_TRAILS_END_LAN = "endTrailLong";

	private static final String DATABASE_NAME = "lldc.db";

	private static final String COLUMN_MEDIA_URL = "mediaUrl";
	private static final String COLUMN_MEDIA_TYPE = "mediaType";

	private static final String COLUMN_DASHBOARD_WELCOMEMSG_IN = "welcome_msg_in";
	private static final String COLUMN_DASHBOARD_WELCOMEMSG_OUT = "welcome_msg_out";
	private static final String COLUMN_DASHBOARD_WELCOMEIMAGE_IN = "welcome_image_in";
	private static final String COLUMN_DASHBOARD_WELCOMEIMAGE_OUT = "welcome_image_out";
	private static final String COLUMN_DASHBOARD_WELCOMEMINOR_NOTIFICATION = "welcome_minor_notification";
	private static final String COLUMN_DASHBOARD_WELCOMEMAJOR_NOTIFICATION = "welcome_major";
	private static final String COLUMN_DASHBOARD_TODAYSTIME = "welcome_todaystime";
	private static final String COLUMN_DASHBOARD_REPORT_EMAIL = "welcome_report_email";
	private static final String COLUMN_DASHBOARD_REPORT_EMAIL_SUBJECT = "welcome_report_email_subject";
	private static final String COLUMN_DASHBOARD_SOCIAL_UNAVAIL_MSG = "welcome_unavail_msg";

	private static final String COLUMN_STATIC_TRAILS_TITLE = "trails_title";
	private static final String COLUMN_STATIC_TRAILS_DESCRIPTION = "trails_desc";
	private static final String COLUMN_STATIC_TRAILS_IMAGE = "trails_image";
	private static final String COLUMN_STATIC_TRAILS_TYPE = "trails_type";

	private static final String COLUMN_TRAILS_PINTITLE = "pin_title";
	private static final String COLUMN_TRAILS_PINLAT = "pin_lat";
	private static final String COLUMN_TRAILS_PINIMAGE = "pin_image";
	private static final String COLUMN_TRAILS_PINDESC = "pin_desc";
	private static final String COLUMN_TRAILS_PINLONG = "pin_long";
	private static final String COLUMN_TRAILS_ID = "trails_id";
	private static final String COLUMN_TRAILS_QUERYSTRING = "queryString";
	private static final String COLUMN_TRAILS_ROUTENO = "routeNo";

	public static final String COLUMN_TEXT = "text";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_SORTORDER = "sortorder";
	public static final String COLUMN_DESC = "description";
	public static final String COLUMN_IMAGE = "image";

	public static final String COLUMN_FACILITY_ID = "fac_id";

	public static final String NELAT = "nelat";
	public static final String NELONG = "neLong";
	public static final String SWLAT = "swLat";
	public static final String SWLONG = "swLong";

	// Database creation sql statement
	private static final String DATABASE_CREATE_DASHBOARD = "create table "
			+ TABLE_DASHBOARD + "(" + COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_DASHBOARD_WELCOMEMSG_IN + " text, "
			+ COLUMN_DASHBOARD_WELCOMEMSG_OUT + " text, "
			+ COLUMN_DASHBOARD_WELCOMEIMAGE_IN + " text, "
			+ COLUMN_DASHBOARD_WELCOMEIMAGE_OUT + " text, "
			+ COLUMN_DASHBOARD_WELCOMEMINOR_NOTIFICATION + " text, "
			+ COLUMN_DASHBOARD_WELCOMEMAJOR_NOTIFICATION + " text,"
			+ COLUMN_DASHBOARD_TODAYSTIME + " text, "
			+ COLUMN_DASHBOARD_REPORT_EMAIL + " text, "
			+ COLUMN_DASHBOARD_REPORT_EMAIL_SUBJECT + " text, "
			+ COLUMN_DASHBOARD_SOCIAL_UNAVAIL_MSG + " text, " + COLUMN_LAT
			+ " text, " + COLUMN_LONG + " text, " + NELAT + " text, " + NELONG
			+ " text, " + SWLAT + " text, " + SWLONG + " text);";

	private static final String DATABASE_CREATE_STATIC_TRAILS = "create table "
			+ TABLE_STATIC_TRAILS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_STATIC_TRAILS_TITLE + " text, "
			+ COLUMN_STATIC_TRAILS_DESCRIPTION + " text, "
			+ COLUMN_STATIC_TRAILS_IMAGE + " text, "
			+ COLUMN_STATIC_TRAILS_TYPE + " text);";

	private static final String DATABASE_CREATE_EVENT = "create table "
			+ TABLE_EVENTS + "(" + COLUMN_ID + " integer primary key, "
			+ COLUMN_NAME + " text, " + COLUMN_SHORT_DESC + " text, "
			+ COLUMN_LONG_DESC + " text, " + COLUMN_VENUE_ID + " text, "
			+ COLUMN_VENUE_TITLE + " text, " + COLUMN_LAT + " text, "
			+ COLUMN_LONG + " text, " + COLUMN_START_DATE_TIME + " text, "
			+ COLUMN_END_DATE_TIME + " text, " + COLUMN_THUMB_IMAGE + " text, "
			+ COLUMN_LARGE_IMAGE + " text, " + COLUMN_FLAG + " text, "
			+ COLUMN_IS_TODAY + " text, " + COLUMN_ACTIVE_DAYS + " text, "
			+ COLUMN_CATEGORY + " text, " + COLUMN_COLOR + " text, "
			+ COLUMN_ORDER + " text, " + COLUMN_MODEL_TYPE + " text, "
			+ COLUMN_ISLONDON2012TRAIL + " text, " + COLUMN_EVENT_SOCIAL_FLAG
			+ " text, " + COLUMN_EVENT_SOCIAL_HANDLE + " text, "
			+ COLUMN_EXCLUDEDFROM_SEARCH + " text);";

	private static final String DATABASE_CREATE_VENUES = "create table "
			+ TABLE_VENUES + "(" + COLUMN_ID + " integer primary key, "
			+ COLUMN_NAME + " text, " + COLUMN_SHORT_DESC + " text, "
			+ COLUMN_LONG_DESC + " text, " + COLUMN_VENUE_ID + " text, "
			+ COLUMN_VENUE_TITLE + " text, " + COLUMN_LAT + " text, "
			+ COLUMN_LONG + " text, " + COLUMN_START_DATE_TIME + " text, "
			+ COLUMN_END_DATE_TIME + " text, " + COLUMN_THUMB_IMAGE + " text, "
			+ COLUMN_LARGE_IMAGE + " text, " + COLUMN_FLAG + " text, "
			+ COLUMN_IS_TODAY + " text, " + COLUMN_ACTIVE_DAYS + " text, "
			+ COLUMN_CATEGORY + " text, " + COLUMN_COLOR + " text, "
			+ COLUMN_ORDER + " text, " + COLUMN_MODEL_TYPE + " text, "
			+ COLUMN_ISLONDON2012TRAIL + " text, " + COLUMN_EVENT_COUNT
			+ " text);";

	private static final String DATABASE_CREATE_FACILITIES = "create table "
			+ TABLE_FACILITIES + "(" + COLUMN_ID + " integer primary key, "
			+ COLUMN_NAME + " text, " + COLUMN_SHORT_DESC + " text, "
			+ COLUMN_LONG_DESC + " text, " + COLUMN_VENUE_ID + " text, "
			+ COLUMN_VENUE_TITLE + " text, " + COLUMN_LAT + " text, "
			+ COLUMN_LONG + " text, " + COLUMN_START_DATE_TIME + " text, "
			+ COLUMN_END_DATE_TIME + " text, " + COLUMN_THUMB_IMAGE + " text, "
			+ COLUMN_LARGE_IMAGE + " text, " + COLUMN_FLAG + " text, "
			+ COLUMN_IS_TODAY + " text, " + COLUMN_ACTIVE_DAYS + " text, "
			+ COLUMN_CATEGORY + " text, " + COLUMN_COLOR + " text, "
			+ COLUMN_ORDER + " text, " + COLUMN_MODEL_TYPE + " text, "
			+ COLUMN_ISLONDON2012TRAIL + " text);";

	private static final String DATABASE_CREATE_TRAILS = "create table "
			+ TABLE_TRAILS + "(" + COLUMN_ID + " integer primary key, "
			+ COLUMN_NAME + " text, " + COLUMN_SHORT_DESC + " text, "
			+ COLUMN_LONG_DESC + " text, " + COLUMN_VENUE_ID + " text, "
			+ COLUMN_VENUE_TITLE + " text, " + COLUMN_LAT + " text, "
			+ COLUMN_LONG + " text, " + COLUMN_START_DATE_TIME + " text, "
			+ COLUMN_END_DATE_TIME + " text, " + COLUMN_THUMB_IMAGE + " text, "
			+ COLUMN_LARGE_IMAGE + " text, " + COLUMN_FLAG + " text, "
			+ COLUMN_IS_TODAY + " text, " + COLUMN_ACTIVE_DAYS + " text, "
			+ COLUMN_CATEGORY + " text, " + COLUMN_COLOR + " text, "
			+ COLUMN_ORDER + " text, " + COLUMN_MODEL_TYPE + " text, "
			+ COLUMN_ISLONDON2012TRAIL + " text, " + COLUMN_DURATION
			+ " text, " + COLUMN_DISTANCE + " text, " + COLUMN_TRAILS_ROUTEID
			+ " text, " + COLUMN_TRAILS_START_LAT + " text, "
			+ COLUMN_TRAILS_START_LAN + " text, " + COLUMN_TRAILS_END_LAT
			+ " text, " + COLUMN_TRAILS_END_LAN + " text);";

	// 1 for event, 2 for venue, 3 for facilities, 4 for Trails
	private static final String DATABASE_CREATE_MEDIA = "create table "
			+ TABLE_MEDIA + "(" + COLUMN_ID + " text, " + COLUMN_MEDIA_URL
			+ " text, " + COLUMN_MEDIA_TYPE + " text);";

	private static final String DATABASE_CREATE_WAYPOINT = "create table "
			+ TABLE_TRAILS_WAYPOINT + "(" + COLUMN_TRAILS_ID + " text, "
			+ COLUMN_TRAILS_PINTITLE + " text, " + COLUMN_TRAILS_PINIMAGE
			+ " text, " + COLUMN_TRAILS_PINLAT + " text, "
			+ COLUMN_TRAILS_PINLONG + " text, " + COLUMN_TRAILS_PINDESC
			+ " text, " + COLUMN_TRAILS_QUERYSTRING + " text, "
			+ COLUMN_TRAILS_ROUTENO + " text);";

	private static final String DATABASE_CREATE_THINGS_TO_DO = "create table "
			+ TABLE_THINGS_TO_DO + "(" + COLUMN_ID + " text, " + COLUMN_NAME
			+ " text, " + COLUMN_TEXT + " text, " + COLUMN_TYPE + " text, "
			+ COLUMN_SORTORDER + " text, " + COLUMN_DESC + " text, "
			+ COLUMN_IMAGE + " text, " + COLUMN_COLOR + " text);";

	private static final String DATABASE_CREATE_THE_PARK = "create table "
			+ TABLE_THE_PARK + "(" + COLUMN_ID + " text, " + COLUMN_NAME
			+ " text, " + COLUMN_SHORT_DESC + " text, " + COLUMN_DESC
			+ " text, " + COLUMN_IMAGE + " text);";

	private static final String DATABASE_CREATE_VENUE_FACILITY = "create table "
			+ TABLE_VENUE_FACILITIES
			+ "("
			+ COLUMN_ID
			+ " text, "
			+ COLUMN_FACILITY_ID
			+ " text, "
			+ COLUMN_TRAILS_PINLAT
			+ " text, "
			+ COLUMN_TRAILS_PINLONG + " text, " + COLUMN_MODEL_TYPE + " text);";

	public LLDCDataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		try {
			database.execSQL(DATABASE_CREATE_DASHBOARD);
			database.execSQL(DATABASE_CREATE_STATIC_TRAILS);
			database.execSQL(DATABASE_CREATE_EVENT);
			database.execSQL(DATABASE_CREATE_FACILITIES);
			database.execSQL(DATABASE_CREATE_TRAILS);
			database.execSQL(DATABASE_CREATE_VENUES);
			database.execSQL(DATABASE_CREATE_MEDIA);
			database.execSQL(DATABASE_CREATE_WAYPOINT);
			database.execSQL(DATABASE_CREATE_THINGS_TO_DO);
			database.execSQL(DATABASE_CREATE_THE_PARK);
			database.execSQL(DATABASE_CREATE_VENUE_FACILITY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(LLDCDataBaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DASHBOARD);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATIC_TRAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FACILITIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAILS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENUES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAILS_WAYPOINT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_THINGS_TO_DO);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_THE_PARK);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VENUE_FACILITIES);
		onCreate(db);
	}

	/***** Open database for insert,update,delete in syncronized manner *****/
	private static synchronized SQLiteDatabase open() throws SQLException {
		return LLDCApplication.DBHelper.getWritableDatabase();
	}

	@SuppressWarnings("unused")
	public void onInsertDashboardData(DashboardModel model) {
		long todo_id = 0;
		final SQLiteDatabase db = open();
		try {
			ContentValues values = new ContentValues();
			values.put(COLUMN_DASHBOARD_WELCOMEMSG_IN, model.getWelcomeMsgIn());
			values.put(COLUMN_DASHBOARD_WELCOMEMSG_OUT,
					model.getWelcomeMsgOut());
			values.put(COLUMN_DASHBOARD_WELCOMEIMAGE_IN,
					model.getWelcomeImageIn());
			values.put(COLUMN_DASHBOARD_WELCOMEIMAGE_OUT,
					model.getWelcomeImageOut());
			values.put(COLUMN_DASHBOARD_WELCOMEMINOR_NOTIFICATION,
					model.getMinorNotice());
			values.put(COLUMN_DASHBOARD_WELCOMEMAJOR_NOTIFICATION,
					model.getMajorNotice());
			values.put(COLUMN_DASHBOARD_TODAYSTIME, model.getTodaysDate());
			values.put(COLUMN_DASHBOARD_REPORT_EMAIL, model.getReportEmailTo());
			values.put(COLUMN_DASHBOARD_REPORT_EMAIL_SUBJECT,
					model.getReportEmailSubject());
			values.put(COLUMN_DASHBOARD_SOCIAL_UNAVAIL_MSG,
					model.getSocialUnavailMsg());
			values.put(COLUMN_LAT, model.getParkLat());
			values.put(COLUMN_LONG, model.getParkLon());
			values.put(NELAT, model.getNelat());
			values.put(NELONG, model.getNelong());
			values.put(SWLAT, model.getSwlat());
			values.put(SWLONG, model.getSwlong());
			todo_id = db.insert(TABLE_DASHBOARD, null, values);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void onInsertEventData(ArrayList<ServerModel> eventList) {
		final SQLiteDatabase db = open();
		try {
			for (int i = 0; i < eventList.size(); i++) {
				// if (isEventDataPresent(eventList.get(i).get_id(), db,
				// TABLE_EVENTS)) {
				// onUpdateEventData(eventList.get(i), db, TABLE_EVENTS);
				// } else {
				onInsertEvent(eventList.get(i), db, TABLE_EVENTS);
				onInsertMediaData(eventList.get(i).getMediaList(),
						LLDCApplication.EVENT, eventList.get(i).get_id(), db);
				// }
			}
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication
					.onShowLogCat("DB:onInsertEventData", e.getMessage());
		}
	}

	public void onInsertVenuesData(ArrayList<ServerModel> eventList) {
		final SQLiteDatabase db = open();
		try {
			for (int i = 0; i < eventList.size(); i++) {
				// if (isEventDataPresent(eventList.get(i).get_id(), db,
				// TABLE_VENUES)) {
				// onUpdateEventData(eventList.get(i), db, TABLE_VENUES);
				// } else {
				onInsertEvent(eventList.get(i), db, TABLE_VENUES);
				onInsertMediaData(eventList.get(i).getMediaList(),
						LLDCApplication.VENUE, eventList.get(i).get_id(), db);
				onInsertVenueFacility(eventList.get(i).getVenueFacilityList(),
						db);
				// }
			}
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onInsertVenuesData",
					"" + e.getMessage());
		}
	}

	public void onInsertFacilitiesData(ArrayList<ServerModel> eventList) {
		final SQLiteDatabase db = open();
		try {
			for (int i = 0; i < eventList.size(); i++) {
				// if (isEventDataPresent(eventList.get(i).get_id(), db,
				// TABLE_FACILITIES)) {
				// onUpdateEventData(eventList.get(i), db, TABLE_FACILITIES);
				// } else {
				onInsertEvent(eventList.get(i), db, TABLE_FACILITIES);
				onInsertMediaData(eventList.get(i).getMediaList(),
						LLDCApplication.FACILITIES, eventList.get(i).get_id(),
						db);
				onInsertVenueFacility(eventList.get(i).getVenueFacilityList(),
						db);
				// }
			}
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onInsertFacilitiesData",
					"" + e.getMessage());
		}
	}

	public void onInsertTrailsData(ArrayList<ServerModel> eventList) {
		final SQLiteDatabase db = open();
		try {
			for (int i = 0; i < eventList.size(); i++) {
				onInsertEvent(eventList.get(i), db, TABLE_TRAILS);
				onInsertMediaData(eventList.get(i).getMediaList(),
						LLDCApplication.TRAILS, eventList.get(i).get_id(), db);
				onInsertTrailsWayPoints(eventList.get(i).getWyapoitnList(), db);
			}
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onInsertTrailsData",
					"" + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	public void onInsertStaticTrailsData(ArrayList<String> eventList) {
		final SQLiteDatabase db = open();
		try {
			ContentValues values = new ContentValues();
			values.put(COLUMN_STATIC_TRAILS_TYPE, eventList.get(0));
			values.put(COLUMN_STATIC_TRAILS_TITLE, eventList.get(1));
			values.put(COLUMN_STATIC_TRAILS_DESCRIPTION, eventList.get(2));
			values.put(COLUMN_STATIC_TRAILS_IMAGE, eventList.get(3));
			long todo_id = db.insert(TABLE_STATIC_TRAILS, null, values);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onInsertTrailsData",
					"" + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	public void onInsertMediaData(ArrayList<MediaModel> eventList,
			int mediaType, String _id, SQLiteDatabase db) {
		try {
			for (int i = 0; i < eventList.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_ID, _id);
				values.put(COLUMN_MEDIA_TYPE, mediaType);
				values.put(COLUMN_MEDIA_URL, eventList.get(i).getImageUrl());
				long todo_id = db.insert(TABLE_MEDIA, null, values);
			}
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onInsertMediaData",
					"" + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	public void onInsertTrailsWayPoints(
			ArrayList<TrailsWayPointModel> waypointlist, SQLiteDatabase db) {
		try {
			for (int i = 0; i < waypointlist.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_TRAILS_ID, waypointlist.get(i).getTailsId());
				values.put(COLUMN_TRAILS_PINTITLE, waypointlist.get(i)
						.getPinTitle());
				values.put(COLUMN_TRAILS_PINLAT, waypointlist.get(i)
						.getPinLat());
				values.put(COLUMN_TRAILS_PINLONG, waypointlist.get(i)
						.getPinLong());
				values.put(COLUMN_TRAILS_PINIMAGE, waypointlist.get(i)
						.getPinImage());
				values.put(COLUMN_TRAILS_PINDESC, waypointlist.get(i)
						.getDescription());
				values.put(COLUMN_TRAILS_QUERYSTRING, waypointlist.get(i)
						.getQueryString());
				values.put(COLUMN_TRAILS_ROUTENO, waypointlist.get(i)
						.getRouteNumber());

				long todo_id = db.insert(TABLE_TRAILS_WAYPOINT, null, values);
				if (LLDCApplication.isDebug)
					LLDCApplication.onShowLogCat("WAYPOINTS INSERTING IN DB",
							"" + waypointlist.get(i).getPinLat() + " "
									+ waypointlist.get(i).getPinLong());
			}
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onInsertTrailsWayPoints",
					"" + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	public void onInsertThingsToDO(ArrayList<ThingsToDoModel> thingsList) {
		final SQLiteDatabase db = open();
		try {
			for (int i = 0; i < thingsList.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_ID, thingsList.get(i).getId());
				values.put(COLUMN_NAME, thingsList.get(i).getName());
				values.put(COLUMN_TEXT, thingsList.get(i).getText());
				values.put(COLUMN_TYPE, thingsList.get(i).getType());
				values.put(COLUMN_SORTORDER, thingsList.get(i).getSortOrder());
				values.put(COLUMN_DESC, thingsList.get(i).getDesc());
				values.put(COLUMN_IMAGE, thingsList.get(i).getImageURL());
				values.put(COLUMN_COLOR, thingsList.get(i).getColor());
				long todo_id = db.insert(TABLE_THINGS_TO_DO, null, values);

			}
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onInsertThingsToDO",
					e.getMessage());
		}

	}

	public void onInsertEvent(ServerModel event, SQLiteDatabase db,
			String tableName) {
		@SuppressWarnings("unused")
		long todo_id = 0;
		try {
			ContentValues values = new ContentValues();
			values.put(COLUMN_ID, event.get_id());
			values.put(COLUMN_NAME, event.getName());
			values.put(COLUMN_SHORT_DESC, event.getShortDesc());
			values.put(COLUMN_LONG_DESC, event.getLongDescription());
			values.put(COLUMN_VENUE_ID, event.getVenueId());
			values.put(COLUMN_VENUE_TITLE, event.getVenueTitle());
			values.put(COLUMN_THUMB_IMAGE, event.getThumbImage());
			values.put(COLUMN_LARGE_IMAGE, event.getLargeImage());
			values.put(COLUMN_LAT, event.getLatitude());
			values.put(COLUMN_LONG, event.getLongitude());
			values.put(COLUMN_START_DATE_TIME, event.getStartDateTime());
			values.put(COLUMN_END_DATE_TIME, event.getEndDateTime());
			values.put(COLUMN_FLAG, event.getFlag());
			values.put(COLUMN_IS_TODAY, event.getIsToday());
			values.put(COLUMN_ACTIVE_DAYS, event.getActiveDays());
			values.put(COLUMN_CATEGORY, event.getCategory());
			values.put(COLUMN_COLOR, event.getColor());
			values.put(COLUMN_ORDER, event.getOrder());
			values.put(COLUMN_MODEL_TYPE, event.getModelType() + "");
			values.put(COLUMN_ISLONDON2012TRAIL, event.getIsLondon2012());
			if (tableName.equals(TABLE_VENUES))
				values.put(COLUMN_EVENT_COUNT, event.getEventCount());

			if (tableName.equals(TABLE_EVENTS)) {
				values.put(COLUMN_EVENT_SOCIAL_FLAG, event.getSocialFlag());
				values.put(COLUMN_EVENT_SOCIAL_HANDLE, event.getSocialHandle());
				values.put(COLUMN_EXCLUDEDFROM_SEARCH,
						event.getExcludeFromSearch());
			}
			if (tableName.equals(TABLE_TRAILS)) {
				values.put(COLUMN_DURATION, event.getDuration());
				values.put(COLUMN_DISTANCE, event.getDistance());
				values.put(COLUMN_TRAILS_ROUTEID, event.getRouteId());
				values.put(COLUMN_TRAILS_START_LAT, event.getStartTrailLat());
				values.put(COLUMN_TRAILS_START_LAN, event.getStartTrailLong());
				values.put(COLUMN_TRAILS_END_LAT, event.getEndTrailLat());
				values.put(COLUMN_TRAILS_END_LAN, event.getEndTrailLong());
			}

			todo_id = db.insert(tableName, null, values);

		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onInsertEvent",
					"" + e.getMessage());
		}
	}

	public void onInsertThePark(ArrayList<TheParkModel> parkList) {
		final SQLiteDatabase db = open();
		try {
			for (int i = 0; i < parkList.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_ID, parkList.get(i).getId());
				values.put(COLUMN_NAME, parkList.get(i).getTitle());
				values.put(COLUMN_SHORT_DESC, parkList.get(i).getSubTitle());
				values.put(COLUMN_DESC, parkList.get(i).getDesc());
				values.put(COLUMN_IMAGE, parkList.get(i).getImageUrl());
				@SuppressWarnings("unused")
				long todo_id = db.insert(TABLE_THE_PARK, null, values);
			}
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onInsertThePark", e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	public void onInsertVenueFacility(
			ArrayList<FacilityModel> venueFacilityList, SQLiteDatabase db) {
		try {
			for (int i = 0; i < venueFacilityList.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(COLUMN_ID, venueFacilityList.get(i).getId());
				values.put(COLUMN_FACILITY_ID, venueFacilityList.get(i)
						.getFacility_id());
				values.put(COLUMN_TRAILS_PINLAT, venueFacilityList.get(i)
						.getLat());
				values.put(COLUMN_TRAILS_PINLONG, venueFacilityList.get(i)
						.getLon());
				values.put(COLUMN_MODEL_TYPE, venueFacilityList.get(i)
						.getModelType());
				long todo_id = db.insert(TABLE_VENUE_FACILITIES, null, values);
			}
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onInsertThePark", e.getMessage());
		}
	}

	public boolean isEventDataPresent(String eventId, SQLiteDatabase db,
			String tableName) {
		try {
			Cursor mCursor = db.rawQuery("Select * from " + tableName
					+ " where " + COLUMN_ID + " = '" + eventId + "'", null);

			if (mCursor.getCount() > 0) {
				mCursor.close();
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LLDCApplication.onShowLogCat("DB:isEventDataPresent",
					"" + e.getMessage());
		}
		return false;
	}

	public void onUpdateEventData(ServerModel event, SQLiteDatabase db,
			String tableName) {
		try {
			ContentValues values = new ContentValues();
			values.put(COLUMN_NAME, event.getName());
			values.put(COLUMN_SHORT_DESC, event.getShortDesc());
			values.put(COLUMN_LONG_DESC, event.getLongDescription());
			values.put(COLUMN_VENUE_ID, event.getVenueId());
			values.put(COLUMN_VENUE_TITLE, event.getVenueTitle());
			values.put(COLUMN_THUMB_IMAGE, event.getThumbImage());
			values.put(COLUMN_LARGE_IMAGE, event.getLargeImage());
			values.put(COLUMN_LAT, event.getLatitude());
			values.put(COLUMN_LONG, event.getLongitude());
			values.put(COLUMN_START_DATE_TIME, event.getStartDateTime());
			values.put(COLUMN_END_DATE_TIME, event.getEndDateTime());
			values.put(COLUMN_FLAG, event.getFlag());
			values.put(COLUMN_IS_TODAY, event.getIsToday());
			values.put(COLUMN_ACTIVE_DAYS, event.getActiveDays());
			values.put(COLUMN_CATEGORY, event.getCategory());
			values.put(COLUMN_COLOR, event.getColor());
			values.put(COLUMN_ORDER, event.getOrder());
			values.put(COLUMN_MODEL_TYPE, event.getModelType() + "");
			int rowid = db.update(tableName, values,
					COLUMN_ID + " ='" + event.get_id() + "'", null);
			LLDCApplication.onShowLogCat("DB:ClearEventTable",
					"Row count after delete " + rowid);
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:UpdateDataTable",
					"" + e.getMessage());
		}
	}

	public ArrayList<ServerModel> getEventData(String where) {
		final SQLiteDatabase db = open();
		ArrayList<ServerModel> eventList = new ArrayList<ServerModel>();
		try {
			Cursor mCursor = null;
			if (where.equals(""))
				mCursor = db.rawQuery("Select * from " + TABLE_EVENTS
						+ " ORDER BY " + COLUMN_START_DATE_TIME + " ASC", null);
			else
				// ASC
				mCursor = db.rawQuery("Select * from " + TABLE_EVENTS
						+ " where " + where + " ORDER BY "
						+ COLUMN_START_DATE_TIME + " ASC", null);

			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					ServerModel model = onGetData(mCursor, TABLE_EVENTS);
					ArrayList<MediaModel> mMediaList = onGetMediaData(
							LLDCApplication.EVENT, model.get_id(), db);
					model.setMediaList(mMediaList);
					eventList.add(model);
					mCursor.moveToNext();
				}
			}

			db.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventList;
	}

	public ArrayList<ServerModel> getTodaysEventData() {
		final SQLiteDatabase db = open();
		ArrayList<ServerModel> eventList = new ArrayList<ServerModel>();
		try {
			Cursor mCursor = db.rawQuery("Select * from " + TABLE_EVENTS
					+ " where " + COLUMN_IS_TODAY + " = '1'" + " ORDER BY "
					+ COLUMN_START_DATE_TIME + " DESC", null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					ServerModel model = onGetData(mCursor, TABLE_EVENTS);
					ArrayList<MediaModel> mMediaList = onGetMediaData(
							LLDCApplication.EVENT, model.get_id(), db);
					model.setMediaList(mMediaList);
					eventList.add(model);
					mCursor.moveToNext();
				}
			}

			db.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return eventList;
	}

	public ArrayList<ServerModel> onGetFacilitiesData(String where) {
		final SQLiteDatabase db = open();
		ArrayList<ServerModel> facilitiesList = new ArrayList<ServerModel>();
		try {
			Cursor mCursor = null;
			if (where.equals(""))
				mCursor = db.rawQuery("Select * from " + TABLE_FACILITIES
						+ " ORDER BY " + COLUMN_NAME + " ASC", null);
			else
				mCursor = db.rawQuery("Select * from " + TABLE_FACILITIES
						+ " where " + where + " ORDER BY " + COLUMN_NAME
						+ " ASC", null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					ServerModel model = onGetData(mCursor, TABLE_FACILITIES);
					ArrayList<MediaModel> mMediaList = onGetMediaData(
							LLDCApplication.FACILITIES, model.get_id(), db);
					model.setMediaList(mMediaList);

					ArrayList<FacilityModel> mVenueFacilityList = onGetVenueFacilities(
							model.get_id(), db, LLDCApplication.FACILITIES);
					model.setVenueFacilityList(mVenueFacilityList);

					facilitiesList.add(model);
					mCursor.moveToNext();
				}
			}

			db.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return facilitiesList;
	}

	public ArrayList<ServerModel> onGetVenueData(String where) {
		final SQLiteDatabase db = open();
		ArrayList<ServerModel> venuesList = new ArrayList<ServerModel>();
		try {
			Cursor mCursor = null;
			if (where.equals(""))
				mCursor = db.rawQuery("Select * from " + TABLE_VENUES
						+ " ORDER BY " + COLUMN_NAME + " ASC", null);
			else
				mCursor = db.rawQuery("Select * from " + TABLE_VENUES
						+ " where " + where + " ORDER BY " + COLUMN_NAME
						+ " ASC", null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					ServerModel model = onGetData(mCursor, TABLE_VENUES);

					ArrayList<MediaModel> mMediaList = onGetMediaData(
							LLDCApplication.VENUE, model.get_id(), db);
					model.setMediaList(mMediaList);

					ArrayList<FacilityModel> mVenueFacilityList = onGetVenueFacilities(
							model.get_id(), db, LLDCApplication.VENUE);
					model.setVenueFacilityList(mVenueFacilityList);

					venuesList.add(model);
					mCursor.moveToNext();
				}
			}

			db.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return venuesList;
	}

	public ArrayList<ServerModel> onGetTrailsData(String where) {
		final SQLiteDatabase db = open();
		ArrayList<ServerModel> trailsList = new ArrayList<ServerModel>();
		try {
			Cursor mCursor = null;
			if (where.equals(""))
				mCursor = db.rawQuery("Select * from " + TABLE_TRAILS
						+ " ORDER BY " + COLUMN_NAME + " ASC", null);
			else
				mCursor = db.rawQuery("Select * from " + TABLE_TRAILS
						+ " where " + where + " ORDER BY " + COLUMN_NAME
						+ " ASC", null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					ServerModel model = onGetData(mCursor, TABLE_TRAILS);
					ArrayList<MediaModel> mMediaList = onGetMediaData(
							LLDCApplication.TRAILS, model.get_id(), db);
					model.setMediaList(mMediaList);
					ArrayList<TrailsWayPointModel> waypointList = onGetTrailsWaypointList(
							model.get_id(), db);
					model.setWyapoitnList(waypointList);
					trailsList.add(model);
					mCursor.moveToNext();
				}
			}

			db.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return trailsList;
	}

	public ArrayList<ServerModel> getLondonTrail(String tableName, int mediaType) {
		final SQLiteDatabase db = open();
		ArrayList<ServerModel> trailsList = new ArrayList<ServerModel>();
		try {
			Cursor mCursor = db.rawQuery("Select * from " + tableName
					+ " where " + COLUMN_ISLONDON2012TRAIL + " = '1'"
					+ " ORDER BY " + COLUMN_NAME + " ASC;", null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					ServerModel tempList = onGetData(mCursor, tableName);
					ArrayList<MediaModel> mMediaList = onGetMediaData(
							mediaType, tempList.get_id(), db);
					tempList.setMediaList(mMediaList);
					
					if (tableName.equals(TABLE_VENUES)) {
						ArrayList<FacilityModel> mVenueFacilityList = onGetVenueFacilities(
								tempList.get_id(), db, LLDCApplication.VENUE);
						tempList.setVenueFacilityList(mVenueFacilityList);
					}
					if (tableName.equals(TABLE_FACILITIES)) {
						ArrayList<FacilityModel> mVenueFacilityList = onGetVenueFacilities(
								tempList.get_id(), db, LLDCApplication.FACILITIES);
						tempList.setVenueFacilityList(mVenueFacilityList);
					}
					
					trailsList.add(tempList);
					mCursor.moveToNext();
				}
			}

			db.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return trailsList;
	}

	public ArrayList<MediaModel> onGetMediaData(int mediaType, String id,
			SQLiteDatabase db) {
		ArrayList<MediaModel> mMediaList = new ArrayList<MediaModel>();
		try {

			Cursor mCursor = db.rawQuery("Select * from " + TABLE_MEDIA
					+ " where " + COLUMN_MEDIA_TYPE + " = '" + mediaType
					+ "' and " + COLUMN_ID + " = " + id + ";", null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					MediaModel model = new MediaModel();
					model.set_id(Long.parseLong(id));
					model.setMediaType((long) mediaType);
					model.setImageUrl(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_MEDIA_URL)));
					mMediaList.add(model);
					mCursor.moveToNext();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return mMediaList;
	}

	public ArrayList<TrailsWayPointModel> onGetTrailsWaypointList(
			String trailId, SQLiteDatabase db) {
		ArrayList<TrailsWayPointModel> waypointList = new ArrayList<TrailsWayPointModel>();

		try {

			Cursor mCursor = db.rawQuery("Select * from "
					+ TABLE_TRAILS_WAYPOINT + " where " + COLUMN_TRAILS_ID
					+ " = '" + trailId + "';", null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					TrailsWayPointModel model = new TrailsWayPointModel();
					model.setTailsId(trailId);
					model.setPinTitle(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TRAILS_PINTITLE)));
					model.setPinImage(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TRAILS_PINIMAGE)));
					model.setPinLat(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TRAILS_PINLAT)));
					model.setPinLong(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TRAILS_PINLONG)));
					model.setDescription(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TRAILS_PINDESC)));
					model.setQueryString(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TRAILS_QUERYSTRING)));
					model.setRouteNumber(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TRAILS_ROUTENO)));
					waypointList.add(model);
					mCursor.moveToNext();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return waypointList;
	}

	public ArrayList<ThingsToDoModel> onGetThingsToDo() {
		ArrayList<ThingsToDoModel> thingsToList = new ArrayList<ThingsToDoModel>();
		final SQLiteDatabase db = open();
		try {
			Cursor mCursor = null; // "ASC" (or) orderby +"DESC"
			mCursor = db.query(TABLE_THINGS_TO_DO, null, null, null, null,
					null, COLUMN_SORTORDER + " ASC");
			// mCursor = db.rawQuery("Select * from " + TABLE_THINGS_TO_DO,
			// null);

			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					ThingsToDoModel model = new ThingsToDoModel();
					model.setId(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_ID)));
					model.setName(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_NAME)));
					model.setText(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TEXT)));
					model.setType(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TYPE)));
					model.setDesc(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_DESC)));
					model.setSortOrder(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_SORTORDER)));
					model.setImageURL(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_IMAGE)));
					model.setColor(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_COLOR)));
					thingsToList.add(model);
					mCursor.moveToNext();
				}
			}

			db.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thingsToList;

	}

	public ArrayList<FacilityModel> onGetVenueFacilities(String id,
			SQLiteDatabase db, int modelType) {
		ArrayList<FacilityModel> mVenueFacilityList = new ArrayList<FacilityModel>();
		try {

			Cursor mCursor = db.rawQuery("Select * from "
					+ TABLE_VENUE_FACILITIES + " where " + COLUMN_ID + " = '"
					+ id + "' AND " + COLUMN_MODEL_TYPE + " in ('" + modelType
					+ "');", null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					FacilityModel model = new FacilityModel();
					model.setId(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_ID)));
					model.setFacility_id(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_FACILITY_ID)));
					model.setLat(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TRAILS_PINLAT)));
					model.setLon(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_TRAILS_PINLONG)));
					model.setModelType(modelType);
					mVenueFacilityList.add(model);
					mCursor.moveToNext();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return mVenueFacilityList;
	}

	public ServerModel onGetData(Cursor mCursor, String tableName) {
		ServerModel item = new ServerModel();
		try {
			item.set_id(mCursor.getString(mCursor.getColumnIndex(COLUMN_ID)));
			item.setName(mCursor.getString(mCursor.getColumnIndex(COLUMN_NAME)));
			item.setShortDesc(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_SHORT_DESC)));
			item.setLongDescription(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_LONG_DESC)));
			item.setVenueId(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_VENUE_ID)));
			item.setVenueTitle(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_VENUE_TITLE)));
			item.setLatitude(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_LAT)));
			item.setLongitude(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_LONG)));
			item.setStartDateTime(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_START_DATE_TIME)));
			item.setEndDateTime(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_END_DATE_TIME)));
			item.setThumbImage(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_THUMB_IMAGE)));
			item.setLargeImage(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_LARGE_IMAGE)));
			item.setFlag(mCursor.getString(mCursor.getColumnIndex(COLUMN_FLAG)));
			item.setIsToday(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_IS_TODAY)));
			item.setActiveDays(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_ACTIVE_DAYS)));
			item.setCategory(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_CATEGORY)));
			item.setColor(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_COLOR)));
			item.setOrder(mCursor.getString(mCursor
					.getColumnIndex(COLUMN_ORDER)));
			item.setModelType(mCursor.getInt(mCursor
					.getColumnIndex(COLUMN_MODEL_TYPE)));
			item.setIsLondon2012(mCursor.getInt(mCursor
					.getColumnIndex(COLUMN_ISLONDON2012TRAIL)));
			if (tableName.equals(TABLE_VENUES)) {
				item.setEventCount(mCursor.getInt(mCursor
						.getColumnIndex(COLUMN_EVENT_COUNT)));
			}

			if (tableName.equals(TABLE_TRAILS)) {
				item.setDistance(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DISTANCE)));
				item.setDuration(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DURATION)));
				item.setRouteId(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_TRAILS_ROUTEID)));
				item.setStartTrailLat(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_TRAILS_START_LAT)));
				item.setStartTrailLong(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_TRAILS_START_LAN)));
				item.setEndTrailLat(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_TRAILS_END_LAT)));
				item.setEndTrailLong(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_TRAILS_END_LAN)));
			}

			if (tableName.equals(TABLE_EVENTS)) {
				item.setSocialFlag(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_EVENT_SOCIAL_FLAG)));
				item.setSocialHandle(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_EVENT_SOCIAL_HANDLE)));
				item.setExcludeFromSearch(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_EXCLUDEDFROM_SEARCH)));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item;
	}

	public ArrayList<String> getStaticTrails(String trailType) {
		ArrayList<String> staticTrail = new ArrayList<String>();

		final SQLiteDatabase db = open();
		try {
			Cursor mCursor = db.rawQuery("Select * from " + TABLE_STATIC_TRAILS
					+ " where " + COLUMN_STATIC_TRAILS_TYPE + " = '"
					+ trailType + "';", null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				// for (int i = 0; i < mCursor.getCount(); i++) {
				staticTrail.add(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_STATIC_TRAILS_TITLE)));
				staticTrail.add(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_STATIC_TRAILS_DESCRIPTION)));
				staticTrail.add(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_STATIC_TRAILS_IMAGE)));
				mCursor.moveToNext();
				// }
			}

			db.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return staticTrail;
	}

	public ArrayList<TheParkModel> onGetTheParkData() {
		ArrayList<TheParkModel> theParkList = new ArrayList<TheParkModel>();

		final SQLiteDatabase db = open();
		try {
			Cursor mCursor = db.rawQuery("Select * from " + TABLE_THE_PARK,
					null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					TheParkModel parkModel = new TheParkModel();
					parkModel.setId(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_ID)));
					parkModel.setTitle(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_NAME)));
					parkModel.setSubTitle(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_SHORT_DESC)));
					parkModel.setDesc(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_DESC)));
					parkModel.setImageUrl(mCursor.getString(mCursor
							.getColumnIndex(COLUMN_IMAGE)));
					theParkList.add(parkModel);
					mCursor.moveToNext();
				}
			}

			db.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return theParkList;
	}

	public void onClearDashboardTable() {
		final SQLiteDatabase db = open();
		try {
			// db.delete(TABLE_EVENTS, null, null);
			db.execSQL("delete from " + TABLE_DASHBOARD);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:ClearDashboadTable",
					e.getMessage());
		}
	}

	public void onClearEventTable() {
		final SQLiteDatabase db = open();
		try {
			// db.delete(TABLE_EVENTS, null, null);
			db.execSQL("delete from " + TABLE_EVENTS);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:ClearEventTable", e.getMessage());
		}
	}

	public void onClearVenuesTable() {
		final SQLiteDatabase db = open();
		try {
			db.execSQL("delete from " + TABLE_VENUES);
			// db.delete(TABLE_VENUES, null, null);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:ClearEventTable", e.getMessage());
		}
	}

	public void onClearFacilitiesTable() {
		final SQLiteDatabase db = open();
		try {
			db.execSQL("delete from " + TABLE_FACILITIES);
			// db.delete(TABLE_FACILITIES, null, null);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:ClearEventTable", e.getMessage());
		}
	}

	public void onClearTrailsTable() {
		final SQLiteDatabase db = open();
		try {
			db.execSQL("delete from " + TABLE_TRAILS);
			// db.delete(TABLE_TRAILS, null, null);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:ClearEventTable", e.getMessage());
		}
	}

	public void onClearStaticTrailTable() {
		final SQLiteDatabase db = open();
		try {
			// db.delete(TABLE_EVENTS, null, null);
			db.execSQL("delete from " + TABLE_STATIC_TRAILS);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:ClearStaticTrailTable",
					e.getMessage());
		}
	}

	public void onClearMediaTable() {
		final SQLiteDatabase db = open();
		try {
			db.execSQL("delete from " + TABLE_MEDIA);
			// db.delete(TABLE_MEDIA, null, null);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:ClearEventTable", e.getMessage());
		}
	}

	public void onClearTrailWaypointTable() {
		final SQLiteDatabase db = open();
		try {
			// db.delete(TABLE_EVENTS, null, null);
			db.execSQL("delete from " + TABLE_TRAILS_WAYPOINT);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onClearTrailWaypointTable",
					e.getMessage());
		}
	}

	public ServerModel getSingleData(String _id, String tableName) {
		ServerModel model = new ServerModel();
		final SQLiteDatabase db = open();
		try {
			// db.execSQL("delete from "+ TABLE_VIDEO);
			Cursor mCursor = db.rawQuery("Select * from " + tableName
					+ " where " + COLUMN_ID + " = '" + _id + "';", null);

			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				model = onGetData(mCursor, tableName);
				ArrayList<MediaModel> mMediaList = onGetMediaData(
						model.getModelType(), model.get_id(), db);
				model.setMediaList(mMediaList);

				if (tableName.equals(TABLE_VENUES)) {
					ArrayList<FacilityModel> mVenueFacilityList = onGetVenueFacilities(
							model.get_id(), db, LLDCApplication.VENUE);
					model.setVenueFacilityList(mVenueFacilityList);
				}

				if (tableName.equals(TABLE_FACILITIES)) {
					ArrayList<FacilityModel> mVenueFacilityList = onGetVenueFacilities(
							model.get_id(), db, LLDCApplication.FACILITIES);
					model.setVenueFacilityList(mVenueFacilityList);
				}

				if (tableName.equals(TABLE_TRAILS)) {
					ArrayList<TrailsWayPointModel> waypointList = onGetTrailsWaypointList(
							model.get_id(), db);
					model.setWyapoitnList(waypointList);
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:ClearEventTable", e.getMessage());
		}
		return model;
	}

	public ArrayList<ServerModel> onGetSearchData(String searchText,
			String tableName, ArrayList<String> filterarr,
			ArrayList<String> showmeFilterArr) {
		ArrayList<ServerModel> modelList = new ArrayList<ServerModel>();

		final SQLiteDatabase db = open();
		try {
			// db.execSQL("delete from "+ TABLE_VIDEO);
			Cursor mCursor = null;
			String categoryId = "";
			String query = "";
			if (showmeFilterArr.size() > 0) {
				for (int i = 0; i < showmeFilterArr.size(); i++) {
					if (i == 0) {
						categoryId = showmeFilterArr.get(i);
					} else {
						categoryId = categoryId + ", " + showmeFilterArr.get(i);
					}
				}
			}
			if (tableName.equals(TABLE_VENUES)
					|| tableName.equals(TABLE_FACILITIES)
					&& filterarr.size() > 0) {
				String where = "";
				for (int i = 0; i < filterarr.size(); i++) {
					if (i == 0) {
						where = where + "'" + filterarr.get(i) + "'";
					} else {
						where = where + ", " + "'" + filterarr.get(i) + "'";
					}
				}
				if (filterarr.size() > 0) {
					if (showmeFilterArr.size() > 0) {
						query = "select b.* from (select DISTINCT " + COLUMN_ID
								+ " from " + TABLE_VENUE_FACILITIES + " where "
								+ COLUMN_FACILITY_ID + " in (" + where
								+ ")) as a, " + tableName + " b where a."
								+ COLUMN_ID + " = b." + COLUMN_ID + " and b."
								+ COLUMN_NAME + " like '%" + searchText
								+ "%' AND " + "a." + COLUMN_CATEGORY + " in ("
								+ categoryId + ");";

						mCursor = db.rawQuery(query, null);
					} else {
						query = "select b.* from (select DISTINCT " + COLUMN_ID
								+ " from " + TABLE_VENUE_FACILITIES + " where "
								+ COLUMN_FACILITY_ID + " in (" + where
								+ ")) as a, " + tableName + " b where a."
								+ COLUMN_ID + " = b." + COLUMN_ID + " and b."
								+ COLUMN_NAME + " like '%" + searchText + "%';";
						mCursor = db.rawQuery(query, null);
					}
				} else {
					if (showmeFilterArr.size() > 0) {
						query = "Select * from " + tableName + " where "
								+ COLUMN_NAME + " like '%" + searchText
								+ "%' OR " + COLUMN_LONG_DESC + " like '%"
								+ searchText + "%' AND " + COLUMN_CATEGORY
								+ " in (" + categoryId + ");";
						mCursor = db.rawQuery(query, null);
					} else {
						query = "Select * from " + tableName + " where "
								+ COLUMN_NAME + " like '%" + searchText
								+ "%' OR " + COLUMN_LONG_DESC + " like '%"
								+ searchText + "%';";
						mCursor = db.rawQuery(query, null);
					}
				}

			} else {
				if (showmeFilterArr.size() > 0) {
					query = "Select * from " + tableName + " where "
							+ COLUMN_NAME + " like '%" + searchText + "%' OR "
							+ COLUMN_LONG_DESC + " like '%" + searchText
							+ "%' AND " + COLUMN_CATEGORY + " in ("
							+ categoryId + ");";
					mCursor = db.rawQuery(query, null);
				} else {
					query = "Select * from " + tableName + " where "
							+ COLUMN_NAME + " like '%" + searchText + "%'  OR "
							+ COLUMN_LONG_DESC + " like '%" + searchText
							+ "%' ;";
					mCursor = db.rawQuery(query, null);
				}

			}
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					ServerModel model = onGetData(mCursor, tableName);
					ArrayList<MediaModel> mMediaList = onGetMediaData(
							model.getModelType(), model.get_id(), db);
					model.setMediaList(mMediaList);

					if (tableName.equals(TABLE_VENUES)) {
						ArrayList<FacilityModel> mVenueFacilityList = onGetVenueFacilities(
								model.get_id(), db, LLDCApplication.VENUE);
						model.setVenueFacilityList(mVenueFacilityList);
					}

					if (tableName.equals(TABLE_FACILITIES)) {
						ArrayList<FacilityModel> mVenueFacilityList = onGetVenueFacilities(
								model.get_id(), db, LLDCApplication.FACILITIES);
						model.setVenueFacilityList(mVenueFacilityList);
					}

					if (tableName.equals(TABLE_TRAILS)) {
						ArrayList<TrailsWayPointModel> waypointList = onGetTrailsWaypointList(
								model.get_id(), db);
						model.setWyapoitnList(waypointList);
					}

					modelList.add(model);
					mCursor.moveToNext();
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:ClearEventTable", e.getMessage());
		}
		return modelList;
	}

	public DashboardModel onGetDashBoardData() {
		final SQLiteDatabase db = open();
		DashboardModel model = new DashboardModel();
		try {
			Cursor mCursor = db.rawQuery("Select * from " + TABLE_DASHBOARD,
					null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				model.setWelcomeMsgIn(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DASHBOARD_WELCOMEMSG_IN)));
				model.setWelcomeMsgOut(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DASHBOARD_WELCOMEMSG_OUT)));
				model.setWelcomeImageIn(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DASHBOARD_WELCOMEIMAGE_IN)));
				model.setWelcomeImageOut(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DASHBOARD_WELCOMEIMAGE_OUT)));
				model.setMinorNotice(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DASHBOARD_WELCOMEMINOR_NOTIFICATION)));
				model.setMajorNotice(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DASHBOARD_WELCOMEMAJOR_NOTIFICATION)));
				model.setTodaysDate(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DASHBOARD_TODAYSTIME)));
				model.setReportEmailTo(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DASHBOARD_REPORT_EMAIL)));
				model.setReportEmailSubject(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DASHBOARD_REPORT_EMAIL_SUBJECT)));
				model.setSocialUnavailMsg(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_DASHBOARD_SOCIAL_UNAVAIL_MSG)));
				model.setParkLat(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_LAT)));
				model.setParkLon(mCursor.getString(mCursor
						.getColumnIndex(COLUMN_LONG)));
				model.setNelat(mCursor.getString(mCursor.getColumnIndex(NELAT)));
				model.setNelong(mCursor.getString(mCursor
						.getColumnIndex(NELONG)));
				model.setSwlat(mCursor.getString(mCursor.getColumnIndex(SWLAT)));
				model.setSwlong(mCursor.getString(mCursor
						.getColumnIndex(SWLONG)));
			}
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;

	}

	public void onClearThingsToDoTable() {
		// TODO Auto-generated method stub
		final SQLiteDatabase db = open();
		try {
			// db.delete(TABLE_EVENTS, null, null);
			db.execSQL("delete from " + TABLE_THINGS_TO_DO);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onClearTrailWaypointTable",
					e.getMessage());
		}
	}

	public void onClearTheParkTable() {
		// TODO Auto-generated method stub
		final SQLiteDatabase db = open();
		try {
			// db.delete(TABLE_EVENTS, null, null);
			db.execSQL("delete from " + TABLE_THE_PARK);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onClearTheParkTable",
					e.getMessage());
		}
	}

	public void onClearVenueFacilityTable() {
		// TODO Auto-generated method stub
		final SQLiteDatabase db = open();
		try {
			// db.delete(TABLE_EVENTS, null, null);
			db.execSQL("delete from " + TABLE_VENUE_FACILITIES);
			db.close();
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("DB:onClearVenueFacilityTable",
					e.getMessage());
		}
	}

	public ArrayList<ServerModel> search(String string,
			ArrayList<String> filterarr, ArrayList<String> showmeFilterArr) {
		// TODO Auto-generated method stub

		ArrayList<ServerModel> serachResult = new ArrayList<ServerModel>();

		ArrayList<ServerModel> eventList = new ArrayList<ServerModel>();
		ArrayList<ServerModel> trailsList = new ArrayList<ServerModel>();
		if (filterarr.size() == 0) {
			eventList = onGetSearchData(string, TABLE_EVENTS, filterarr,
					showmeFilterArr);
			trailsList = onGetSearchData(string, TABLE_TRAILS, filterarr,
					showmeFilterArr);
		}
		ArrayList<ServerModel> venuesList = onGetSearchData(string,
				TABLE_VENUES, filterarr, showmeFilterArr);
		ArrayList<ServerModel> facilityList = onGetSearchData(string,
				TABLE_FACILITIES, filterarr, showmeFilterArr);

		for (int i = 0; i < venuesList.size(); i++) {
			serachResult.add(venuesList.get(i));
		}
		for (int i = 0; i < trailsList.size(); i++) {
			serachResult.add(trailsList.get(i));
		}
		for (int i = 0; i < facilityList.size(); i++) {
			serachResult.add(facilityList.get(i));
		}
		for (int i = 0; i < eventList.size(); i++) {
			Log.e("LLDCDataBaseHelper",
					"tablename event:getExcludeFromSearch   "
							+ eventList.get(i).getExcludeFromSearch());

			if (eventList.get(i).getExcludeFromSearch().equals("0"))
				serachResult.add(eventList.get(i));
		}

		return serachResult;
	}

	public ArrayList<ServerModel> onSearchResultFacility(String where,
			String tableName) {
		ArrayList<ServerModel> searchResult = new ArrayList<ServerModel>();
		
		/*
		 *  For ATM we require LLDCApplication.FACILITIES
		 *  & other remaining items we require LLDCApplication.VENUE
		 */
		int mType = LLDCApplication.VENUE;
		
		if(where.equalsIgnoreCase("2")) {
			mType = LLDCApplication.FACILITIES;
		}
		
		String query = "select b.* from (select DISTINCT " + COLUMN_ID + ","
				+ COLUMN_MODEL_TYPE + " from " + TABLE_VENUE_FACILITIES
				+ " where " + COLUMN_FACILITY_ID + " in ('" + where
				+ "')) as a, " + tableName + " b where a." + COLUMN_ID
				+ " = b." + COLUMN_ID + " AND a." + COLUMN_MODEL_TYPE + " = b."
				+ COLUMN_MODEL_TYPE + ";";

		final SQLiteDatabase db = open();
		try {
			// db.execSQL("delete from "+ TABLE_VIDEO);
			Cursor mCursor = null;
			mCursor = db.rawQuery(query, null);
			if (mCursor != null && mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				for (int i = 0; i < mCursor.getCount(); i++) {
					ServerModel model = onGetData(mCursor, tableName);

					ArrayList<MediaModel> mMediaList = onGetMediaData(
							mType, model.get_id(), db);
					model.setMediaList(mMediaList);

					ArrayList<FacilityModel> mVenueFacilityList = onGetVenueFacilities(
							model.get_id(), db, mType);
					model.setVenueFacilityList(mVenueFacilityList);

					searchResult.add(model);
					mCursor.moveToNext();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			LLDCApplication.onShowLogCat("SearchResult", e.getMessage());
		}
		return searchResult;
	}

}