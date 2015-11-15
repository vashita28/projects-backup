package com.coudriet.tapsnap.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class AppStatus {
	
//**********************************************************************************
	
	    private static AppStatus _instance;
	    static Context context;
	    private ConnectivityManager connectivityManager;
	    boolean connected = false;
	    
//**********************************************************************************

	    public static AppStatus getInstance(Context ctx) {
	        context = ctx;
	        synchronized (AppStatus.class) {
	          
	        	if(null != _instance) {
			        return _instance;
			      } else {
			    	  
			    	_instance = new AppStatus();
			        return _instance;
			      }
	        }
	    }
	    
//**********************************************************************************

	    public boolean isOnline() {
	    	
	    	connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    	
	          if (connectivityManager != null)
	          {
	              NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
	              if (info != null)
	                  for (int i = 0; i < info.length; i++)
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          Log.v("Network connected", "nETWORK nAME: "+info[i].getTypeName());
	                          
	                          return true;
	                      }else{
	                    	  Log.v("Network not connected", "nETWORK nAME: "+info[i].getTypeName());
	                      }

	          }
	          
	        return connected;
	    }
	    
//**********************************************************************************

}
