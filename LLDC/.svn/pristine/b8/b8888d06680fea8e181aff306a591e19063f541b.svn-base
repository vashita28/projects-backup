package co.uk.android.lldc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap.CompressFormat;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.images.ImageCacheManager;
import co.uk.android.lldc.images.ImageCacheManager.CacheType;
import co.uk.android.lldc.models.RequestManager;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.models.TheParkModel;
import co.uk.android.lldc.models.UrgentClosureModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class LLDCApplication extends Application {
	private static int DISK_IMAGECACHE_SIZE = 1024*1024*10;
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100;  //PNG is lossless so quality is ignored but must be provided
	
	public void onCreate() {
		super.onCreate();
		init();
	};

	/** Volly*/
	/**
	 * Intialize the request manager and the image cache 
	 */
	private void init() {
		RequestManager.init(this);
		createImageCache();
	}
	
	/**
	 * Create the image cache. Uses Memory Cache by default. Change to Disk for a Disk based LRU implementation.  
	 */
	private void createImageCache(){
		ImageCacheManager.getInstance().init(this,
				this.getPackageCodePath()
				, DISK_IMAGECACHE_SIZE
				, DISK_IMAGECACHE_COMPRESS_FORMAT
				, DISK_IMAGECACHE_QUALITY
				, CacheType.MEMORY);
	}
	
	public static boolean isDebug = true;

	public static boolean bImagePreFetch = false;

	public static boolean isInsideThePark = false;

	public static int PARK_RADIUS = 1500; // 1.5 km

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

	public static String DEVGETBASEDATA = "http://dev.pocketapp.co.uk/dev/lldc/index.php/api/getbasedata";
	public static String PRODGETBASEDATA = "http://prod.pocketapp.co.uk/lldc/index.php/api/getbasedata";

	public static String EVENT_NAME_DEFAULT_COLOR = "#444444";

	public static ServerModel selectedModel = new ServerModel();

	public static TheParkModel selectedParkModel = new TheParkModel();

	public static String noSocialMessage = "";

	public static boolean isMajorNotificationShown = false;

	public static float MAX_ZOOM = 17f;
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

	public static double TRIALROUTEDURATION = 0f;

	public static double TRIALROUTEDISTANCE = 0f;
	public static double ROUTEDISTANCE = 0f;
	public static double NAVROUTEDISTANCE = 0f;
	public static double NAVROUTEDURATION = 0f;

	/********** Initialize database *********/
	public static void init(Context context) {
		if (DBHelper == null) {
			DBHelper = new LLDCDataBaseHelper(context);
		}
	}

	public static final int SPLASH_TIME_OUT = 2000;

	public static final String VERSION = "v 1.0.3";

	public static final String OUTSIDE_PARK_NAV_MESSAGE = "You appear to move outside of the park. Navigation feature shall stop working correctly. Close Navigation?";

	public static UrgentClosureModel urgentClosureModel = new UrgentClosureModel();

	public static SharedPreferences sharedpreferences;
	// 1800000
	public static long refershInterval = 1800000;
	public static long nextServerCall = 1800000; // 120000;

	public static int screenWidth = 0;

	public static int screenHeight = 0;

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
		try {
			if (context != null) {
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

}
