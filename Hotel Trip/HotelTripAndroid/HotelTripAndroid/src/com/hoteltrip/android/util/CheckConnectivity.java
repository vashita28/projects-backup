package com.hoteltrip.android.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnectivity {

    public static boolean isNetworkAvail(final Context context) {
            final ConnectivityManager cm = (ConnectivityManager) context
                            .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo info = cm.getActiveNetworkInfo();
            return (info != null);
    }
    
}
