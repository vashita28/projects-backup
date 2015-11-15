package com.example.cabapppassenger.util;

import java.util.Locale;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class Util {

	public static void showToastMessage(Context context, String text,
			int duration) {
		Toast.makeText(context, text, duration).show();
	}

	public static Location getLocationByProvider(Context context,
			String provider) {
		Location location = null;
		if (!isProviderSupported(context, provider)) {
			return null;
		}
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		try {
			if (locationManager.isProviderEnabled(provider)) {
				location = locationManager.getLastKnownLocation(provider);
			}
		} catch (Exception e) {
			Log.d("", "Cannot acces Provider " + provider);
		}
		return location;
	}

	public static boolean isProviderSupported(Context context, String provider) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		boolean isProviderEnabled = false;
		try {
			isProviderEnabled = locationManager.isProviderEnabled(provider);
		} catch (Exception ex) {
		}
		return isProviderEnabled;

	}

	public static Location getLocation(Context context) {
		Location location = null;
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

		return location;
	}

	public static String getDistanceInMiles(double kilometers) {
		final double MILES_PER_KILOMETER = 0.621;
		double miles = kilometers * MILES_PER_KILOMETER;
		Log.e("Util", "miles:: " + miles);
		return String.format(Locale.ENGLISH, "%.1f", miles);
	}

	public static String getDistanceInKilometers(double miles) {
		final double KILOMETER_PER_MILES = 1.609344;
		double kilometers = miles * KILOMETER_PER_MILES;
		Log.e("Util", "Kilometers:: " + kilometers);
		return String.format(Locale.ENGLISH, "%.1f", kilometers);
	}
}
