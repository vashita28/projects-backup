package co.uk.pocketapp.gmd.db;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class GMDContentProvider extends ContentProvider {

	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "GMDContentProvider";
	private static final String DATABASE_NAME = "GMD.db";

	public static final String MAIN_TABLE_NAME = "main";
	public static final String SECOND_GENERATION_TABLE_NAME = "secondgeneration";
	public static final String THIRD_GENERATION_TABLE_NAME = "thirdgeneration";
	public static final String LEAF_NODE_TABLE_NAME = "leafnode";
	public static final String SUMMARY_VALUES_TABLE_NAME = "summaryvalues";
	public static final String MATERIAL_LOG_TABLE_NAME = "materiallog";

	// new structure
	public static final String MILL_TABLE_NAME = "mill";
	public static final String SERVICES_TABLE_NAME = "services";
	public static final String TASKS_TABLE_NAME = "tasks";
	public static final String CHILD_LEAF_TABLE_NAME = "childleaf";

	private static HashMap<String, String> mainProjectionMap;
	private static HashMap<String, String> secondgenerationProjectionMap;
	private static HashMap<String, String> thirdgenerationProjectionMap;
	private static HashMap<String, String> leafnodeProjectionMap;
	private static HashMap<String, String> summaryvaluesProjectionMap;
	private static HashMap<String, String> materiallogProjectionMap;

	// new
	private static HashMap<String, String> millProjectionMap;
	private static HashMap<String, String> servicesProjectionMap;
	private static HashMap<String, String> tasksProjectionMap;
	private static HashMap<String, String> childleafProjectionMap;

	private static final int CUSTOMQUERY = 1;
	private static final int MAIN = 2;
	private static final int MAIN_ID = 3;
	private static final int SECOND_GENERATION = 4;
	private static final int SECOND_GENERATION_ID = 5;
	private static final int THIRD_GENERATION = 6;
	private static final int THIRD_GENERATION_ID = 7;
	private static final int LEAF_NODE = 8;
	private static final int LEAF_NODE_ID = 9;
	private static final int SUMMARY_VALUES = 10;
	private static final int SUMMARY_VALUES_ID = 11;
	private static final int MATERIAL_LOG = 12;
	private static final int MATERIAL_LOG_ID = 13;

	// new
	private static final int MILL = 14;
	private static final int MILL_ID = 15;
	private static final int SERVICES = 16;
	private static final int SERVICES_ID = 17;
	private static final int TASKS = 18;
	private static final int TASKS_ID = 19;
	private static final int CHILDLEAF = 20;
	private static final int CHILDLEAF_ID = 21;

	private static final UriMatcher sUriMatcher;

	public static Context applicationContext;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "customquery", CUSTOMQUERY);

		sUriMatcher.addURI(DataProvider.AUTHORITY, "mainparent", MAIN);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "mainparent/#", MAIN_ID);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "secondgeneration",
				SECOND_GENERATION);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "secondgeneration/#",
				SECOND_GENERATION_ID);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "thirdgeneration",
				THIRD_GENERATION);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "thirdgeneration/#",
				THIRD_GENERATION_ID);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "leafnode", LEAF_NODE);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "leafnode/#", LEAF_NODE_ID);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "summaryvalues",
				SUMMARY_VALUES);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "summaryvalues/#",
				SUMMARY_VALUES_ID);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "materiallog", MATERIAL_LOG);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "materiallog/#",
				MATERIAL_LOG_ID);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "mill", MILL);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "mill/#", MILL_ID);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "services", SERVICES);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "services/#", SERVICES_ID);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "tasks", TASKS);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "tasks/#", TASKS_ID);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "childleaf", CHILDLEAF);
		sUriMatcher.addURI(DataProvider.AUTHORITY, "childleaf/#", CHILDLEAF_ID);

		mainProjectionMap = new HashMap<String, String>();
		mainProjectionMap.put(DataProvider.Main.ID, DataProvider.Main.ID);
		mainProjectionMap.put(DataProvider.Main.CHILD, DataProvider.Main.CHILD);

		secondgenerationProjectionMap = new HashMap<String, String>();
		secondgenerationProjectionMap.put(DataProvider.Second_Generation.ID,
				DataProvider.Second_Generation.ID);
		secondgenerationProjectionMap.put(
				DataProvider.Second_Generation.MAIN_CHILD_ID,
				DataProvider.Second_Generation.MAIN_CHILD_ID);
		secondgenerationProjectionMap.put(
				DataProvider.Second_Generation.CHILD_SECOND_GEN,
				DataProvider.Second_Generation.CHILD_SECOND_GEN);

		thirdgenerationProjectionMap = new HashMap<String, String>();
		thirdgenerationProjectionMap.put(DataProvider.Third_Generation.ID,
				DataProvider.Third_Generation.ID);
		thirdgenerationProjectionMap.put(
				DataProvider.Third_Generation.CHILD_SECOND_GEN_ID,
				DataProvider.Third_Generation.CHILD_SECOND_GEN_ID);
		thirdgenerationProjectionMap.put(
				DataProvider.Third_Generation.CHILD_THIRD_GEN,
				DataProvider.Third_Generation.CHILD_THIRD_GEN);

		leafnodeProjectionMap = new HashMap<String, String>();
		leafnodeProjectionMap.put(DataProvider.Leaf_Node.ID,
				DataProvider.Leaf_Node.ID);
		leafnodeProjectionMap.put(DataProvider.Leaf_Node.CHILD_LEAF_NODE,
				DataProvider.Leaf_Node.CHILD_LEAF_NODE);
		leafnodeProjectionMap.put(DataProvider.Leaf_Node.CHILD_THIRD_GEN_ID,
				DataProvider.Leaf_Node.CHILD_THIRD_GEN_ID);

		summaryvaluesProjectionMap = new HashMap<String, String>();
		summaryvaluesProjectionMap.put(DataProvider.Summary_Values.ID,
				DataProvider.Summary_Values.ID);
		summaryvaluesProjectionMap.put(DataProvider.Summary_Values.REPORT_ID,
				DataProvider.Summary_Values.REPORT_ID);
		summaryvaluesProjectionMap.put(
				DataProvider.Summary_Values.CHILD_LEAF_NODE_ID,
				DataProvider.Summary_Values.CHILD_LEAF_NODE_ID);
		summaryvaluesProjectionMap.put(
				DataProvider.Summary_Values.CHILD_THIRD_GEN_ID,
				DataProvider.Summary_Values.CHILD_THIRD_GEN_ID);
		summaryvaluesProjectionMap.put(
				DataProvider.Summary_Values.CHILD_MAIN_ID,
				DataProvider.Summary_Values.CHILD_MAIN_ID);
		summaryvaluesProjectionMap.put(
				DataProvider.Summary_Values.CHILD_SECOND_GEN_ID,
				DataProvider.Summary_Values.CHILD_SECOND_GEN_ID);
		summaryvaluesProjectionMap.put(
				DataProvider.Summary_Values.CHILD_LEAF_NODE,
				DataProvider.Summary_Values.CHILD_LEAF_NODE);
		summaryvaluesProjectionMap.put(
				DataProvider.Summary_Values.CHILD_THIRD_GEN,
				DataProvider.Summary_Values.CHILD_THIRD_GEN);
		summaryvaluesProjectionMap.put(DataProvider.Summary_Values.CHILD_MAIN,
				DataProvider.Summary_Values.CHILD_MAIN);
		summaryvaluesProjectionMap.put(
				DataProvider.Summary_Values.CHILD_SECOND_GEN,
				DataProvider.Summary_Values.CHILD_SECOND_GEN);

		summaryvaluesProjectionMap.put(DataProvider.Summary_Values.CHILD,
				DataProvider.Summary_Values.CHILD);
		summaryvaluesProjectionMap.put(DataProvider.Summary_Values.VALUE,
				DataProvider.Summary_Values.VALUE);

		materiallogProjectionMap = new HashMap<String, String>();
		materiallogProjectionMap.put(DataProvider.Material_Log.ID,
				DataProvider.Material_Log.ID);
		materiallogProjectionMap.put(
				DataProvider.Material_Log.MATERIAL_CATEGORY,
				DataProvider.Material_Log.MATERIAL_CATEGORY);
		materiallogProjectionMap.put(DataProvider.Material_Log.MATERIAL_INFO,
				DataProvider.Material_Log.MATERIAL_INFO);
		materiallogProjectionMap.put(DataProvider.Material_Log.MATERIAL_NAME,
				DataProvider.Material_Log.MATERIAL_NAME);
		materiallogProjectionMap.put(
				DataProvider.Material_Log.MATERIAL_REPORT_DATE,
				DataProvider.Material_Log.MATERIAL_REPORT_DATE);
		materiallogProjectionMap.put(DataProvider.Material_Log.REPORT_ID,
				DataProvider.Material_Log.REPORT_ID);

		// new
		millProjectionMap = new HashMap<String, String>();
		millProjectionMap.put(DataProvider.Mill.ID, DataProvider.Mill.ID);
		millProjectionMap.put(DataProvider.Mill.MILL_NAME,
				DataProvider.Mill.MILL_NAME);
		millProjectionMap.put(DataProvider.Mill.MILL_ID,
				DataProvider.Mill.MILL_ID);
		millProjectionMap.put(DataProvider.Mill.GMD_TYPE,
				DataProvider.Mill.GMD_TYPE);
		millProjectionMap.put(DataProvider.Mill.REPORT_ID,
				DataProvider.Mill.REPORT_ID);

		servicesProjectionMap = new HashMap<String, String>();
		servicesProjectionMap.put(DataProvider.Services.ID,
				DataProvider.Mill.ID);
		servicesProjectionMap.put(DataProvider.Services.REPORT_ID,
				DataProvider.Services.REPORT_ID);
		servicesProjectionMap.put(DataProvider.Services.MILL_ID,
				DataProvider.Services.MILL_ID);
		servicesProjectionMap.put(DataProvider.Services.ITEM_ID,
				DataProvider.Services.ITEM_ID);
		servicesProjectionMap.put(DataProvider.Services.PARENT_ID,
				DataProvider.Services.PARENT_ID);
		servicesProjectionMap.put(DataProvider.Services.ITEM_NAME,
				DataProvider.Services.ITEM_NAME);

		tasksProjectionMap = new HashMap<String, String>();
		tasksProjectionMap.put(DataProvider.Tasks.ID, DataProvider.Tasks.ID);
		tasksProjectionMap.put(DataProvider.Tasks.REPORT_ID,
				DataProvider.Tasks.REPORT_ID);
		tasksProjectionMap.put(DataProvider.Tasks.MILL_ID,
				DataProvider.Tasks.MILL_ID);
		tasksProjectionMap.put(DataProvider.Tasks.SERVICES_ITEM_ID,
				DataProvider.Tasks.SERVICES_ITEM_ID);
		tasksProjectionMap.put(DataProvider.Tasks.SERVICES_PARENT_ID,
				DataProvider.Tasks.SERVICES_PARENT_ID);
		tasksProjectionMap.put(DataProvider.Tasks.TASK_NAME,
				DataProvider.Tasks.TASK_NAME);
		tasksProjectionMap.put(DataProvider.Tasks.TASK_CONTENT,
				DataProvider.Tasks.TASK_CONTENT);
		tasksProjectionMap.put(DataProvider.Tasks.TASK_COMMENT,
				DataProvider.Tasks.TASK_COMMENT);
		tasksProjectionMap.put(DataProvider.Tasks.TASK_COMPLETED,
				DataProvider.Tasks.TASK_COMPLETED);
		tasksProjectionMap.put(DataProvider.Tasks.SERVICES_FIRSTGEN_ID,
				DataProvider.Tasks.SERVICES_FIRSTGEN_ID);
		tasksProjectionMap.put(DataProvider.Tasks.SERVICES_SECONDGEN_ID,
				DataProvider.Tasks.SERVICES_SECONDGEN_ID);
		tasksProjectionMap.put(DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID,
				DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID);
		tasksProjectionMap.put(DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID,
				DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID);

		childleafProjectionMap = new HashMap<String, String>();
		childleafProjectionMap.put(DataProvider.Child_Leaf.ID,
				DataProvider.Child_Leaf.ID);
		childleafProjectionMap.put(DataProvider.Child_Leaf.REPORT_ID,
				DataProvider.Child_Leaf.REPORT_ID);
		childleafProjectionMap.put(DataProvider.Child_Leaf.MILL_ID,
				DataProvider.Child_Leaf.MILL_ID);
		childleafProjectionMap.put(DataProvider.Child_Leaf.CHILD_LEAF,
				DataProvider.Child_Leaf.CHILD_LEAF);
	}

	private static class DBHelper extends SQLiteOpenHelper {
		Context context;

		DBHelper(Context argContext) {
			super(argContext, DATABASE_NAME, null, DATABASE_VERSION);
			context = argContext;
			applicationContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase argDB) {
			Log.w(TAG, "C R E A T I N G   D A T A B A S E   ");

			String table = "";

			table = "CREATE TABLE IF NOT EXISTS " + MAIN_TABLE_NAME + " (";
			table += DataProvider.Main.ID
					+ "  INTEGER PRIMARY KEY AUTOINCREMENT,";
			table += DataProvider.Main.CHILD + " varchar);";
			argDB.execSQL(table);

			table = "CREATE TABLE IF NOT EXISTS "
					+ SECOND_GENERATION_TABLE_NAME + " (";
			table += DataProvider.Second_Generation.ID
					+ "  INTEGER PRIMARY KEY AUTOINCREMENT,";
			table += DataProvider.Second_Generation.MAIN_CHILD_ID + " varchar,";
			table += DataProvider.Second_Generation.CHILD_SECOND_GEN
					+ " varchar);";
			argDB.execSQL(table);

			table = "CREATE TABLE IF NOT EXISTS " + THIRD_GENERATION_TABLE_NAME
					+ " (";
			table += DataProvider.Third_Generation.ID
					+ "  INTEGER PRIMARY KEY AUTOINCREMENT,";
			table += DataProvider.Third_Generation.CHILD_SECOND_GEN_ID
					+ " varchar,";
			table += DataProvider.Third_Generation.CHILD_THIRD_GEN
					+ " varchar);";
			argDB.execSQL(table);

			table = "CREATE TABLE IF NOT EXISTS " + LEAF_NODE_TABLE_NAME + " (";
			table += DataProvider.Leaf_Node.ID
					+ "  INTEGER PRIMARY KEY AUTOINCREMENT,";
			table += DataProvider.Leaf_Node.CHILD_LEAF_NODE + " varchar,";
			table += DataProvider.Leaf_Node.CHILD_THIRD_GEN_ID + " varchar);";
			argDB.execSQL(table);

			table = "CREATE TABLE IF NOT EXISTS " + SUMMARY_VALUES_TABLE_NAME
					+ " (";
			table += DataProvider.Summary_Values.ID
					+ "  INTEGER PRIMARY KEY AUTOINCREMENT,";
			table += DataProvider.Summary_Values.REPORT_ID + " varchar,";
			table += DataProvider.Summary_Values.CHILD_LEAF_NODE_ID
					+ " varchar,";
			table += DataProvider.Summary_Values.CHILD_THIRD_GEN_ID
					+ " varchar,";
			table += DataProvider.Summary_Values.CHILD_SECOND_GEN_ID
					+ " varchar,";
			table += DataProvider.Summary_Values.CHILD_MAIN_ID + " varchar,";
			table += DataProvider.Summary_Values.CHILD_LEAF_NODE + " varchar,";
			table += DataProvider.Summary_Values.CHILD_THIRD_GEN + " varchar,";
			table += DataProvider.Summary_Values.CHILD_SECOND_GEN + " varchar,";
			table += DataProvider.Summary_Values.CHILD_MAIN + " varchar,";
			table += DataProvider.Summary_Values.CHILD + " varchar,";
			table += DataProvider.Summary_Values.VALUE + " varchar);";
			argDB.execSQL(table);

			table = "CREATE TABLE IF NOT EXISTS " + MATERIAL_LOG_TABLE_NAME
					+ " (";
			table += DataProvider.Material_Log.ID
					+ "  INTEGER PRIMARY KEY AUTOINCREMENT,";
			table += DataProvider.Material_Log.MATERIAL_CATEGORY + " varchar,";
			table += DataProvider.Material_Log.MATERIAL_INFO + " varchar,";
			table += DataProvider.Material_Log.MATERIAL_NAME + " varchar,";
			table += DataProvider.Material_Log.MATERIAL_REPORT_DATE
					+ " varchar,";
			table += DataProvider.Material_Log.REPORT_ID + " varchar);";
			argDB.execSQL(table);

			table = "CREATE TABLE IF NOT EXISTS " + MILL_TABLE_NAME + " (";
			table += DataProvider.Mill.ID
					+ "  INTEGER PRIMARY KEY AUTOINCREMENT,";
			table += DataProvider.Mill.MILL_NAME + " varchar,";
			table += DataProvider.Mill.MILL_ID + " varchar,";
			table += DataProvider.Mill.GMD_TYPE + " varchar,";
			table += DataProvider.Mill.REPORT_ID + " varchar);";
			argDB.execSQL(table);

			table = "CREATE TABLE IF NOT EXISTS " + SERVICES_TABLE_NAME + " (";
			table += DataProvider.Services.ID
					+ "  INTEGER PRIMARY KEY AUTOINCREMENT,";
			table += DataProvider.Services.MILL_ID + " varchar,";
			table += DataProvider.Services.ITEM_ID + " varchar,";
			table += DataProvider.Services.PARENT_ID + " varchar,";
			table += DataProvider.Services.ITEM_NAME + " varchar,";
			table += DataProvider.Services.REPORT_ID + " varchar);";
			argDB.execSQL(table);

			table = "CREATE TABLE IF NOT EXISTS " + TASKS_TABLE_NAME + " (";
			table += DataProvider.Tasks.ID
					+ "  INTEGER PRIMARY KEY AUTOINCREMENT,";
			table += DataProvider.Tasks.MILL_ID + " varchar,";
			table += DataProvider.Tasks.SERVICES_ITEM_ID + " varchar,";
			table += DataProvider.Tasks.SERVICES_PARENT_ID + " varchar,";
			table += DataProvider.Tasks.TASK_NAME + " varchar,";
			table += DataProvider.Tasks.TASK_CONTENT + " varchar,";
			table += DataProvider.Tasks.TASK_COMMENT + " varchar,";
			table += DataProvider.Tasks.TASK_COMPLETED + " varchar,";
			table += DataProvider.Tasks.SERVICES_FIRSTGEN_ID + " varchar,";
			table += DataProvider.Tasks.SERVICES_SECONDGEN_ID + " varchar,";
			table += DataProvider.Tasks.SERVICES_FIRSTGEN_PARENT_ID
					+ " varchar,";
			table += DataProvider.Tasks.SERVICES_SECONDGEN_PARENT_ID
					+ " varchar,";
			table += DataProvider.Tasks.REPORT_ID + " varchar);";
			argDB.execSQL(table);

			table = "CREATE TABLE IF NOT EXISTS " + CHILD_LEAF_TABLE_NAME
					+ " (";
			table += DataProvider.Child_Leaf.ID
					+ "  INTEGER PRIMARY KEY AUTOINCREMENT,";
			table += DataProvider.Child_Leaf.MILL_ID + " varchar,";
			table += DataProvider.Child_Leaf.CHILD_LEAF + " varchar,";
			table += DataProvider.Child_Leaf.REPORT_ID + " varchar);";
			argDB.execSQL(table);
		}

		@Override
		public void onOpen(SQLiteDatabase argDB) {
		}

		@Override
		public void onUpgrade(SQLiteDatabase argDB, int argOldVersion,
				int argNewVersion) {// do need to change this method when the
									// user is doing an upgrade.
			Log.w(TAG,
					"U P G R A D I N G   D A T A B A S E   F R O M   V E R S I O N  "
							+ argOldVersion + "  T O  " + argNewVersion);

			onCreate(argDB);
		}
	}

	private SQLiteOpenHelper mOpenHelper;

	@Override
	public int delete(Uri argUri, String argWhere, String[] argWhereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		String rowId;
		switch (sUriMatcher.match(argUri)) {
		case CUSTOMQUERY:
			db.execSQL(argWhere);
			count = 0;// TODO need to look the logic for this
			break;
		case MAIN_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Main.ID + "=" + rowId;
		case MAIN:
			count = db.delete(MAIN_TABLE_NAME, argWhere, argWhereArgs);
			break;

		case SECOND_GENERATION_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Second_Generation.ID + "=" + rowId;
		case SECOND_GENERATION:
			count = db.delete(SECOND_GENERATION_TABLE_NAME, argWhere,
					argWhereArgs);
			break;

		case THIRD_GENERATION_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Third_Generation.ID + "=" + rowId;
		case THIRD_GENERATION:
			count = db.delete(THIRD_GENERATION_TABLE_NAME, argWhere,
					argWhereArgs);
			break;

		case LEAF_NODE_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Leaf_Node.ID + "=" + rowId;
		case LEAF_NODE:
			count = db.delete(LEAF_NODE_TABLE_NAME, argWhere, argWhereArgs);
			break;

		case SUMMARY_VALUES_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Summary_Values.ID + "=" + rowId;
		case SUMMARY_VALUES:
			count = db
					.delete(SUMMARY_VALUES_TABLE_NAME, argWhere, argWhereArgs);
			break;

		case MATERIAL_LOG_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Material_Log.ID + "=" + rowId;
		case MATERIAL_LOG:
			count = db.delete(MATERIAL_LOG_TABLE_NAME, argWhere, argWhereArgs);
			break;

		case CHILDLEAF_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Child_Leaf.ID + "=" + rowId;
		case CHILDLEAF:
			count = db.delete(CHILD_LEAF_TABLE_NAME, argWhere, argWhereArgs);
			break;
		case TASKS_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Tasks.ID + "=" + rowId;
		case TASKS:
			count = db.delete(TASKS_TABLE_NAME, argWhere, argWhereArgs);
			break;
		case SERVICES_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Services.ID + "=" + rowId;
		case SERVICES:
			count = db.delete(SERVICES_TABLE_NAME, argWhere, argWhereArgs);
			break;
		case MILL_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Mill.ID + "=" + rowId;
		case MILL:
			count = db.delete(MILL_TABLE_NAME, argWhere, argWhereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + argUri);
		}

		return count;
	}

	@Override
	public String getType(Uri argUri) {
		// TODO Auto-generated method stub
		switch (sUriMatcher.match(argUri)) {
		case MAIN:
			return DataProvider.Main.CONTENT_TYPE;
		case MAIN_ID:
			return DataProvider.Main.CONTENT_ITEM_TYPE;

		case SECOND_GENERATION:
			return DataProvider.Second_Generation.CONTENT_TYPE;
		case SECOND_GENERATION_ID:
			return DataProvider.Second_Generation.CONTENT_ITEM_TYPE;
		case THIRD_GENERATION:
			return DataProvider.Third_Generation.CONTENT_TYPE;
		case THIRD_GENERATION_ID:
			return DataProvider.Third_Generation.CONTENT_ITEM_TYPE;
		case LEAF_NODE:
			return DataProvider.Leaf_Node.CONTENT_TYPE;
		case LEAF_NODE_ID:
			return DataProvider.Leaf_Node.CONTENT_ITEM_TYPE;
		case SUMMARY_VALUES:
			return DataProvider.Summary_Values.CONTENT_TYPE;
		case SUMMARY_VALUES_ID:
			return DataProvider.Summary_Values.CONTENT_ITEM_TYPE;
		case MATERIAL_LOG:
			return DataProvider.Material_Log.CONTENT_TYPE;
		case MATERIAL_LOG_ID:
			return DataProvider.Material_Log.CONTENT_ITEM_TYPE;

		case TASKS:
			return DataProvider.Tasks.CONTENT_TYPE;
		case TASKS_ID:
			return DataProvider.Tasks.CONTENT_ITEM_TYPE;
		case CHILDLEAF:
			return DataProvider.Child_Leaf.CONTENT_TYPE;
		case CHILDLEAF_ID:
			return DataProvider.Child_Leaf.CONTENT_ITEM_TYPE;
		case SERVICES:
			return DataProvider.Services.CONTENT_TYPE;
		case SERVICES_ID:
			return DataProvider.Services.CONTENT_ITEM_TYPE;
		case MILL:
			return DataProvider.Mill.CONTENT_TYPE;
		case MILL_ID:
			return DataProvider.Mill.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + argUri);
		}
	}

	@Override
	public Uri insert(Uri argUri, ContentValues values) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Uri rowUri = null;
		long rowId;
		switch (sUriMatcher.match(argUri)) {
		case MAIN:
			rowId = db.insert(MAIN_TABLE_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.withAppendedId(
						DataProvider.Main.CONTENT_URI, rowId);
			}
			break;

		case SECOND_GENERATION:
			rowId = db.insert(SECOND_GENERATION_TABLE_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.withAppendedId(
						DataProvider.Second_Generation.CONTENT_URI, rowId);
			}
			break;

		case THIRD_GENERATION:
			rowId = db.insert(THIRD_GENERATION_TABLE_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.withAppendedId(
						DataProvider.Third_Generation.CONTENT_URI, rowId);
			}
			break;

		case LEAF_NODE:
			rowId = db.insert(LEAF_NODE_TABLE_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.withAppendedId(
						DataProvider.Leaf_Node.CONTENT_URI, rowId);
			}
			break;

		case SUMMARY_VALUES:
			rowId = db.insert(SUMMARY_VALUES_TABLE_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.withAppendedId(
						DataProvider.Summary_Values.CONTENT_URI, rowId);
			}
			break;
		case MATERIAL_LOG:
			rowId = db.insert(MATERIAL_LOG_TABLE_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.withAppendedId(
						DataProvider.Material_Log.CONTENT_URI, rowId);
			}
			break;
		case TASKS:
			rowId = db.insert(TASKS_TABLE_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.withAppendedId(
						DataProvider.Tasks.CONTENT_URI, rowId);
			}
			break;
		case SERVICES:
			rowId = db.insert(SERVICES_TABLE_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.withAppendedId(
						DataProvider.Services.CONTENT_URI, rowId);
			}
			break;
		case MILL:
			rowId = db.insert(MILL_TABLE_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.withAppendedId(
						DataProvider.Mill.CONTENT_URI, rowId);
			}
			break;
		case CHILDLEAF:
			rowId = db.insert(CHILD_LEAF_TABLE_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.withAppendedId(
						DataProvider.Child_Leaf.CONTENT_URI, rowId);
			}
			break;
		default:
			throw new SQLException("Failed to insert row into " + argUri);
		}
		return rowUri;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mOpenHelper = new DBHelper(getContext());

		return true;
	}

	@Override
	public Cursor query(Uri argUri, String[] argProjection,
			String argSelection, String[] argSelectionArgs, String argSortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String groupBy = null;
		String having = null;

		switch (sUriMatcher.match(argUri)) {
		case CUSTOMQUERY:
			return mOpenHelper.getReadableDatabase().rawQuery(argSelection,
					null);
		case MAIN:
			qb.setTables(MAIN_TABLE_NAME);
			if (argProjection == null) {
				qb.setProjectionMap(mainProjectionMap);
			}
			break;
		case MAIN_ID:
			qb.setTables(MAIN_TABLE_NAME);
			argSelection = DataProvider.Main.ID + "="
					+ argUri.getPathSegments().get(1);
			break;

		case SECOND_GENERATION:
			qb.setTables(SECOND_GENERATION_TABLE_NAME);
			if (argProjection == null) {
				qb.setProjectionMap(secondgenerationProjectionMap);
			}
			break;
		case SECOND_GENERATION_ID:
			qb.setTables(SECOND_GENERATION_TABLE_NAME);
			argSelection = DataProvider.Second_Generation.ID + "="
					+ argUri.getPathSegments().get(1);
			break;

		case THIRD_GENERATION:
			qb.setTables(THIRD_GENERATION_TABLE_NAME);
			if (argProjection == null) {
				qb.setProjectionMap(thirdgenerationProjectionMap);
			}
			break;
		case THIRD_GENERATION_ID:
			qb.setTables(THIRD_GENERATION_TABLE_NAME);
			argSelection = DataProvider.Third_Generation.ID + "="
					+ argUri.getPathSegments().get(1);
			break;

		case LEAF_NODE:
			qb.setTables(LEAF_NODE_TABLE_NAME);
			if (argProjection == null) {
				qb.setProjectionMap(leafnodeProjectionMap);
			}
			break;
		case LEAF_NODE_ID:
			qb.setTables(LEAF_NODE_TABLE_NAME);
			argSelection = DataProvider.Leaf_Node.ID + "="
					+ argUri.getPathSegments().get(1);
			break;

		case SUMMARY_VALUES:
			qb.setTables(SUMMARY_VALUES_TABLE_NAME);
			if (argProjection == null) {
				qb.setProjectionMap(summaryvaluesProjectionMap);
			}
			break;
		case SUMMARY_VALUES_ID:
			qb.setTables(SUMMARY_VALUES_TABLE_NAME);
			argSelection = DataProvider.Summary_Values.ID + "="
					+ argUri.getPathSegments().get(1);
			break;

		case MATERIAL_LOG:
			qb.setTables(MATERIAL_LOG_TABLE_NAME);
			if (argProjection == null) {
				qb.setProjectionMap(materiallogProjectionMap);
			}
			break;
		case MATERIAL_LOG_ID:
			qb.setTables(MATERIAL_LOG_TABLE_NAME);
			argSelection = DataProvider.Material_Log.ID + "="
					+ argUri.getPathSegments().get(1);
			break;
		case SERVICES:
			qb.setTables(SERVICES_TABLE_NAME);
			if (argProjection == null) {
				qb.setProjectionMap(servicesProjectionMap);
			}
			break;
		case SERVICES_ID:
			qb.setTables(SERVICES_TABLE_NAME);
			argSelection = DataProvider.Services.ID + "="
					+ argUri.getPathSegments().get(1);
			break;
		case TASKS:
			qb.setTables(TASKS_TABLE_NAME);
			if (argProjection == null) {
				qb.setProjectionMap(tasksProjectionMap);
			}
			break;
		case TASKS_ID:
			qb.setTables(TASKS_TABLE_NAME);
			argSelection = DataProvider.Tasks.ID + "="
					+ argUri.getPathSegments().get(1);
			break;
		case CHILDLEAF:
			qb.setTables(CHILD_LEAF_TABLE_NAME);
			if (argProjection == null) {
				qb.setProjectionMap(childleafProjectionMap);
			}
			break;
		case CHILDLEAF_ID:
			qb.setTables(CHILD_LEAF_TABLE_NAME);
			argSelection = DataProvider.Child_Leaf.ID + "="
					+ argUri.getPathSegments().get(1);
			break;
		case MILL:
			qb.setTables(MILL_TABLE_NAME);
			if (argProjection == null) {
				qb.setProjectionMap(millProjectionMap);
			}
			break;
		case MILL_ID:
			qb.setTables(MILL_TABLE_NAME);
			argSelection = DataProvider.Mill.ID + "="
					+ argUri.getPathSegments().get(1);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + argUri);
		}
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor curssor = qb.query(db, argProjection, argSelection,
				argSelectionArgs, groupBy, having, argSortOrder);
		return curssor;
	}

	@Override
	public int update(Uri argUri, ContentValues argValues, String argWhere,
			String[] argWhereArgs) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		int match = sUriMatcher.match(argUri);
		switch (match) {
		case MAIN_ID:
			String rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Main.ID + "=" + rowId;
			argValues.put(DataProvider.Main._ID, rowId);
		case MAIN:
			count = db.update(MAIN_TABLE_NAME, argValues, argWhere,
					argWhereArgs);
			break;
		case SECOND_GENERATION_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Second_Generation.ID + "=" + rowId;
			argValues.put(DataProvider.Second_Generation._ID, rowId);
		case SECOND_GENERATION:
			count = db.update(SECOND_GENERATION_TABLE_NAME, argValues,
					argWhere, argWhereArgs);
			break;
		case THIRD_GENERATION_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Third_Generation.ID + "=" + rowId;
			argValues.put(DataProvider.Third_Generation._ID, rowId);
		case THIRD_GENERATION:
			count = db.update(THIRD_GENERATION_TABLE_NAME, argValues, argWhere,
					argWhereArgs);
			break;
		case LEAF_NODE_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Leaf_Node.ID + "=" + rowId;
			argValues.put(DataProvider.Leaf_Node._ID, rowId);
		case LEAF_NODE:
			count = db.update(LEAF_NODE_TABLE_NAME, argValues, argWhere,
					argWhereArgs);
			break;
		case SUMMARY_VALUES_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Summary_Values.ID + "=" + rowId;
			argValues.put(DataProvider.Summary_Values._ID, rowId);
		case SUMMARY_VALUES:
			count = db.update(SUMMARY_VALUES_TABLE_NAME, argValues, argWhere,
					argWhereArgs);
			break;
		case MATERIAL_LOG_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Material_Log.ID + "=" + rowId;
			argValues.put(DataProvider.Material_Log._ID, rowId);
		case MATERIAL_LOG:
			count = db.update(MATERIAL_LOG_TABLE_NAME, argValues, argWhere,
					argWhereArgs);
			break;
		case CHILDLEAF_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Child_Leaf.ID + "=" + rowId;
			argValues.put(DataProvider.Child_Leaf._ID, rowId);
		case CHILDLEAF:
			count = db.update(CHILD_LEAF_TABLE_NAME, argValues, argWhere,
					argWhereArgs);
			break;
		case MILL_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Mill.ID + "=" + rowId;
			argValues.put(DataProvider.Mill._ID, rowId);
		case MILL:
			count = db.update(MILL_TABLE_NAME, argValues, argWhere,
					argWhereArgs);
			break;
		case SERVICES_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Services.ID + "=" + rowId;
			argValues.put(DataProvider.Services._ID, rowId);
		case SERVICES:
			count = db.update(SERVICES_TABLE_NAME, argValues, argWhere,
					argWhereArgs);
			break;
		case TASKS_ID:
			rowId = argUri.getPathSegments().get(1);
			argWhere = DataProvider.Tasks.ID + "=" + rowId;
			argValues.put(DataProvider.Tasks._ID, rowId);
		case TASKS:
			count = db.update(TASKS_TABLE_NAME, argValues, argWhere,
					argWhereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + argUri);
		}
		return count;
	}

}
