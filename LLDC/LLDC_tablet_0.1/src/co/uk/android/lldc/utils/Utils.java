package co.uk.android.lldc.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import co.uk.android.lldc.R;

public class Utils {

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

	static SharedPreferences prefs;
	static SharedPreferences.Editor editor;

	// public static boolean bIsTablet = false;

	@SuppressLint("NewApi")
	public static void ChangeStatusBar(Context mContext) {
		Window window = ((Activity) mContext).getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.setStatusBarColor(mContext.getResources().getColor(
				R.color.textcolor_pink));
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	static void initSharedPref(Context context) {
		if (prefs == null)
			prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (editor == null)
			editor = prefs.edit();
	}

	// public static void setIsTabletFlag(Context context, boolean
	// bIsAuthorised) {
	// initSharedPref(context);
	// editor.putBoolean(Constants.IS_TABLET, bIsAuthorised);
	// editor.commit();
	// }
	//
	// public static Boolean getIsTabletFlag(Context context) {
	// initSharedPref(context);
	// return prefs.getBoolean(Constants.IS_TABLET, true);
	// }
}
