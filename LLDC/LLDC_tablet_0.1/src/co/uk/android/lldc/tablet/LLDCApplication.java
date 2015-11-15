package co.uk.android.lldc.tablet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.app.Application;
import android.app.ProgressDialog;
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
import co.uk.android.lldc.R;
import co.uk.android.lldc.database.LLDCDataBaseHelper;
import co.uk.android.lldc.images.ImageCacheManager;
import co.uk.android.lldc.images.ImageCacheManager.CacheType;
import co.uk.android.lldc.models.RequestManager;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.models.TheParkModel;
import co.uk.android.lldc.models.UrgentClosureModel;

import com.google.android.gms.location.LocationRequest;
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
	private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100; // PNG is lossless so
														// quality is ignored
														// but must be provided

	public void onCreate() {
		super.onCreate();

		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

		@SuppressWarnings("deprecation")
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				// .showStubImage(R.drawable.qeop_placeholder)
				.bitmapConfig(Bitmap.Config.ALPHA_8)
				.imageScaleType(ImageScaleType.EXACTLY).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.memoryCacheSize(2 * 1024 * 1024)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				// .discCacheExtraOptions(400, 480, CompressFormat.PNG, 100,
				// null)
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

		init();

	};

	/** Volley */
	/**
	 * Intialize the request manager and the image cache
	 */
	private void init() {
		RequestManager.init(this);
		createImageCache();
	}

	/**
	 * Create the image cache. Uses Memory Cache by default. Change to Disk for
	 * a Disk based LRU implementation.
	 */
	private void createImageCache() {
		ImageCacheManager.getInstance().init(this, this.getPackageCodePath(),
				DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT,
				DISK_IMAGECACHE_QUALITY, CacheType.MEMORY);
	}

	private static ProgressDialog mProgressDialog;

	public static String mBaseDir = "";

	public static void showSimpleProgressDialog(Context context) {
		try {
			if (mProgressDialog == null) {
				mProgressDialog = ProgressDialog
						.show(context, "", "Loading...");
				mProgressDialog.setCancelable(false);
			}

			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}

		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();
		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeSimpleProgressDialog() {
		try {
			if (mProgressDialog != null) {
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
					mProgressDialog = null;
				}
			}
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();

		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	public static String PARK_WIFI_UEP_URL = "https://queenelizabetholympicpark.wifispark.net";

	public static String EVENT_NAME_DEFAULT_COLOR = "#444444";

	public static ServerModel selectedModel = new ServerModel();

	public static TheParkModel selectedParkModel = new TheParkModel();

	public static String noSocialMessage = "";

	public static String navRouteId = "";

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

	public static final String VERSION = "v 1.0.6.2 Dev";

	public static final String OUTSIDE_PARK_NAV_MESSAGE = "You appear to move outside of the park. Navigation feature shall stop working correctly. Close Navigation?";

	public static long LOCATION_REQUEST_INTERVAL = 30000;

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

	public static String mapTileFileName = "";
	public static String mapTileFileURL = "";

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

	public static void setMapTileFileName(Context context, String msg) {
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedpreferences.edit();

		editor.putString("MAPTILEFILENAME", msg);
		editor.commit();
	}

	public static String getMapTileFileName(Context context) {
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedpreferences.getString("MAPTILEFILENAME", "");
	}

	public static void setMapTileNameFromCMS(Context context, String msg) {
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedpreferences.edit();

		editor.putString("MAPTILEFILENAMECMS", msg);
		editor.commit();
	}

	public static String getMapTileNameFromCMS(Context context) {
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedpreferences.getString("MAPTILEFILENAMECMS", "");
	}

	public static void setMapTileURLFromCMS(Context context, String msg) {
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedpreferences.edit();

		editor.putString("MAPTILEFILEURLCMS", msg);
		editor.commit();
	}

	public static String getMapTileURLFromCMS(Context context) {
		sharedpreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedpreferences.getString("MAPTILEFILEURLCMS", "");
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

	public static LocationRequest getLocationRequest() {
		LocationRequest REQUEST = LocationRequest.create()
				.setInterval(LLDCApplication.LOCATION_REQUEST_INTERVAL) // 5
																		// seconds
				.setFastestInterval(50) // 16ms = 60fps
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		return REQUEST;
	}

	/** extracting zip file */
	public static void unzip(String zipFile, String location)
			throws IOException {
		int size;
		byte[] buffer = new byte[2048];

		try {
			if (!location.endsWith("/")) {
				location += "/";
			}
			File f = new File(location);
			if (!f.isDirectory()) {
				f.mkdirs();
			}
			ZipInputStream zin = new ZipInputStream(new BufferedInputStream(
					new FileInputStream(zipFile), 2048));
			try {
				ZipEntry ze = null;
				while ((ze = zin.getNextEntry()) != null) {
					String path = location + ze.getName();
					File unzipFile = new File(path);
					System.out.println("Extracting file: " + path);
					if (ze.isDirectory()) {
						if (!unzipFile.isDirectory()) {
							unzipFile.mkdirs();
						}
					} else {
						// check for and create parent directories if they don't
						// exist
						File parentDir = unzipFile.getParentFile();
						if (null != parentDir) {
							if (!parentDir.isDirectory()) {
								parentDir.mkdirs();
							}
						}

						// unzip the file
						FileOutputStream out = new FileOutputStream(unzipFile,
								false);
						BufferedOutputStream fout = new BufferedOutputStream(
								out, 2048);
						try {
							while ((size = zin.read(buffer, 0, 2048)) != -1) {
								fout.write(buffer, 0, size);
							}

							zin.closeEntry();
						} finally {
							fout.flush();
							fout.close();
						}
					}
				}
			} finally {
				zin.close();
			}
		} catch (Exception e) {
			Log.e("e", "Unzip exception", e);
		}
	}

}
