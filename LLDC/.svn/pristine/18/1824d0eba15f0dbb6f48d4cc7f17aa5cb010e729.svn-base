package co.uk.android.lldc;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.models.TheParkModel;
import co.uk.android.lldc.models.UrgentClosureModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class LLDCApplication extends Application {



	public void onCreate() {
		super.onCreate();

		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

		@SuppressWarnings("deprecation")
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.showStubImage(R.drawable.imgnt_placeholder)
				.bitmapConfig(Bitmap.Config.ALPHA_8)
				.imageScaleType(ImageScaleType.EXACTLY).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.discCacheExtraOptions(400, 480, CompressFormat.PNG, 100, null)
				.defaultDisplayImageOptions(defaultOptions)
				.threadPoolSize(1)
				// extra
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.writeDebugLogs().build();

		ImageLoader.getInstance().init(config);

	};

	public static boolean isDebug = true;
	
	public static boolean isInsideThePark = false;
	
	// 1 for event, 2 for venue, 3 for facilities, 4 for Trails
	
	public static int EVENT = 1, VENUE = 2, FACILITIES = 3, TRAILS = 4;
	
	public static String FLAG_NORMAL = "1", FLAG_HIGHLIGHT = "2";
	
	// 1 => Toilets 2 => ATM 3 => Disabled parking
	// 4 => Family play areas 5 => Venue parking 6 =>Food and drink
	
	public static int VENUE_FAC_ID_TOILET = 1, VENUE_FAC_ID_ATM = 2,
			VENUE_FAC_ID_DIS_PARKING = 3, VENUE_FAC_ID_FAMILY_PLAY_AREA = 4,
			VENUE_FAC_ID_PARKING = 5, VENUE_FAC_ID_FOOD_DRINK = 6;
	
	public static String VENUE_FAC_ID_UNICODE_1 = "\ue805",
			VENUE_FAC_ID_UNICODE_2 = "\ue800",
			VENUE_FAC_ID_UNICODE_3 = "\ue801",
			VENUE_FAC_ID_UNICODE_4 = "\ue803",
			VENUE_FAC_ID_UNICODE_5 = "\ue804",
			VENUE_FAC_ID_UNICODE_6 = "\ue802";
	
	public static String EVENT_NAME_DEFAULT_COLOR = "#444444";
	
	public static ServerModel selectedModel = new ServerModel();
	
	public static TheParkModel selectedParkModel = new TheParkModel();
	
	public static String noSocialMessage = "";
	
	public static boolean isMajorNotificationShown = false;
	
	public static float MAX_ZOOM = 16.5f;
	public static float MIN_ZOOM = 15f;
	public static float MIN_ZOOM_FOR_FLING = 15f;
	// 51.527171, -0.041502 Sothwest
	// 51.562105, 0.014106 northeast
	public static LatLng NEBOUND = new LatLng(51.562105, 0.014106);
	public static LatLng SWBOUND = new LatLng(51.527171, -0.041502);
	public static final LatLngBounds MAPBOUNDARY = new LatLngBounds(SWBOUND,
			NEBOUND);
	
	public static double DEF_LATITUDE = -41.78;
	public static double DEF_LONGITUDE = 173.02;
	public static float DEF_ZOOM = 15f;
	
	public static String flurryKey = "QSWKKS6NKTYVHM2BPP6V";
	// public Typeface font_CalibriBold, font_helveticaNeueItalic,
	// font_helveticaneue;
	public static LLDCDataBaseHelper DBHelper = null;

	public static Location parkCenter = new Location("");

	public static String ROUTEDURATION = "";

	/********** Initialize database *********/
	public static void init(Context context) {
		if (DBHelper == null) {
			DBHelper = new LLDCDataBaseHelper(context);
		}
	}

	public static final int SPLASH_TIME_OUT = 2000;

	// public static DashboardModel dashBoardData = new DashboardModel();

	public static UrgentClosureModel urgentClosureModel = new UrgentClosureModel();

	public static SharedPreferences sharedpreferences;
	// 1800000
	public static long refershInterval = 1800000;
	public static long nextServerCall = 1800000; // 120000;

	public static void setServerTime(Context context, String time) {
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedpreferences.edit();

		editor.putString("SERVERTIME", time);
		editor.commit();
	}

	public static String getServerTime(Context context) {
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedpreferences.getString("SERVERTIME", "0");
	}

	public static void onShowLogCat(String tag, String msg) {
		if (!LLDCApplication.isDebug) {
			return;
		}
		Log.v(tag, msg);
	}

	public static void onShowToastMesssage(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static boolean isConnectingToInternet(Context _context) {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	public static Location getLocation(Context context) {
		Location location = null;
		try {
			LocationManager locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			if (locationManager != null) {
				location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (location == null) {
				location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Returns the correction for Lat and Lng if camera is trying to get outside
	 * of visible map
	 * 
	 * @param cameraBounds
	 *            Current camera bounds
	 * @return Latitude and Longitude corrections to get back into bounds.
	 */
	public static LatLng getLatLngCorrection(LatLngBounds cameraBounds) {
		double latitude = 0, longitude = 0;
		// 51.527171, -0.041502 Sothwest
		// 51.562105, 0.014106 northeast

		if (cameraBounds.southwest.latitude < LLDCApplication.SWBOUND.latitude) {
			latitude = LLDCApplication.SWBOUND.latitude
					- cameraBounds.southwest.latitude;
		}
		if (cameraBounds.southwest.longitude < LLDCApplication.SWBOUND.longitude) {
			longitude = LLDCApplication.SWBOUND.longitude
					- cameraBounds.southwest.longitude;
		}
		if (cameraBounds.northeast.latitude > LLDCApplication.NEBOUND.latitude) {
			latitude = LLDCApplication.NEBOUND.latitude
					- cameraBounds.northeast.latitude;
		}
		if (cameraBounds.northeast.longitude > LLDCApplication.NEBOUND.longitude) {
			longitude = LLDCApplication.NEBOUND.longitude
					- cameraBounds.northeast.longitude;
		}
		return new LatLng(latitude, longitude);
	}

	public static int dpToPx(int dp) {
		return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
	}

//	private boolean writeLogMessage(int filePriority, int msgPriority,
//			String tag, String message) {
//		boolean messageWritten = false;
//		FileLog.open("sdcard/logFile.log", filePriority, 100000);
//		FileLog.delete();
//		FileLog.open("sdcard/logFile.log", filePriority, 100000);
//		switch (msgPriority) {
//		case Log.VERBOSE:
//			FileLog.v(tag, message);
//			break;
//		case Log.DEBUG:
//			FileLog.d(tag, message);
//			break;
//		case Log.INFO:
//			FileLog.i(tag, message);
//			break;
//		case Log.WARN:
//			FileLog.w(tag, message);
//			break;
//		case Log.ERROR:
//			FileLog.e(tag, message);
//			break;
//		}
//		File f = new File("sdcard/logFile.log");
//		messageWritten = (f.length() > 0);
//		FileLog.close();
//		return messageWritten;
//	}

}
