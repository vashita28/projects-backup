package com.coudriet.snapnshare.android;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.PushService;

public class MainApplicationStartup extends Application {
	
	@Override
	public void onCreate() { 
		super.onCreate();
		
		//initialize parse account by using appid and clientkey
	    Parse.initialize(this, "NX3OL6mQPEYHHmTX03NZvhnhnvr0kNQf1YqJ5u5c", "AsLbn7qBEaN8uTLyFdoF5R37fPepknYu9Lo53QBh");
	    
	    PushService.setDefaultPushCallback(this, MainActivity.class);
	    
	    ParseFacebookUtils.initialize(getString(R.string.app_id));
	}
}
