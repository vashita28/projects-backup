package co.uk.pocketapp.bridgestone.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Util {
	static SharedPreferences prefs;
	static Editor editor;

	public static void setUserID(Context context, String userID) {

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putString("bridgestoneuserID", userID);
		editor.commit();
	}

	public static String getUserID(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("bridgestoneuserID", "");
	}

	public static void setLanguage(Context context, int nLanguagePosition) {

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putInt("bridgestoneLanguage", nLanguagePosition);
		editor.commit();
	}

	public static Integer getLanguage(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getInt("bridgestoneLanguage", 0);
	}

	public static void setPressureMeasurement(Context context,
			int nPressureMeasurementPosition) {

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putInt("bridgestonePressure", nPressureMeasurementPosition);
		editor.commit();
	}

	public static Integer getPressureMeasurement(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getInt("bridgestonePressure", 0);
	}

	public static void setPressureFilter(Context context, String pressureFilter) {

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putString("bridgestonePressureFilter", pressureFilter);
		editor.commit();
	}

	public static String getPressureFilter(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		// if (getPressureMeasurement(context) == 0)
		return prefs.getString("bridgestonePressureFilter", "7.0");
		// else
		// return prefs.getString("bridgestonePressureFilter", "100");

	}

	public static void setDeviceAddress(Context context, String deviceAddress) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putString("bridgestoneaddress", deviceAddress);
		editor.commit();
	}

	public static String getDeviceAddress(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("bridgestoneaddress", "");
	}

}
