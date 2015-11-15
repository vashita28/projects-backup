package com.android.cabapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class CabAppService extends Service {
	private final IBinder mBinder = new LocalBinder();
	private Intent intentService = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("CabAppService", "onBind()");
		intentService = intent;
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public CabAppService getService() {
			return CabAppService.this;
		}
	}
}
