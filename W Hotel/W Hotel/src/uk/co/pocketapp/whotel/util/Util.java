package uk.co.pocketapp.whotel.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;

/**
 * Created by Pramod on 8/27/13.
 */
public class Util {
	static SharedPreferences prefs;
	static SharedPreferences.Editor editor;
	public static Context mContext;

	public static final String serverURL = "http://50.243.204.171:8222/starwood/Test/whotel_wifi/client_api.php";

	public static void setAgreeContinueAccepted(Context context,
			boolean bIsTCAccepted) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putBoolean("whotel_tcaccepted", bIsTCAccepted);
		editor.commit();
	}

	public static boolean getAgreeContinueAccepted(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("whotel_tcaccepted", false);
	}

	public static Location getLocation() {
		Location location = null;
		LocationManager locationManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager != null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}
		if (location == null) {
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		return location;
	}

}