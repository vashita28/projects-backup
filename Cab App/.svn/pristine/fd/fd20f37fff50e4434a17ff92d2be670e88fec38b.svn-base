package com.handpoint.headstart.android;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 
 * Class for obtaining HeadstartService
 *
 */
public class HeadstartServiceConnection implements ServiceConnection {
	
	private HeadstartService mConnection;
	private Context ctx;
	boolean isBound;
	BindListener listener;

	/**
	 * Constructor
	 * @param ctx - android context
	 * @param start - should service stay alive after closing called client 
	 */
	public HeadstartServiceConnection(Context ctx, boolean start) {
		this.ctx = ctx;
		doBindService(start);
	}

	/**
	 * Constructor
	 * @param ctx - android context
	 * @param start - should service stay alive after binding
	 * @param listener - callback listener which will be performed after connect to service  
	 */
	public HeadstartServiceConnection(Context ctx, boolean start, BindListener listener) {
		this.ctx = ctx;
		this.listener = listener;
		doBindService(start);
	}

	public void onServiceConnected(ComponentName name, IBinder service) {
		mConnection = ((HeadstartService.LocalBinder)service).getService();
		if (null != listener) {
			listener.onBindCompleted();
		}
	}

	public void onServiceDisconnected(ComponentName name) {
		mConnection = null;

	}

	private void doBindService(boolean start) {
		Intent i = new Intent(ctx, HeadstartService.class);
		if (start) {
			ctx.getApplicationContext().startService(i);
		}
		ctx.getApplicationContext().bindService(i, this, Context.BIND_AUTO_CREATE);
	    isBound = true;
	}
	
	/**
	 * Unbind from service
	 * @param start - should service be stopped after unbinding
	 */
	public void doUnbindService(boolean start) {
	    if (isBound) {
	    	ctx.getApplicationContext().unbindService(this);
	        isBound = false;
			if (start) {
				Intent i = new Intent(ctx, HeadstartService.class);
				ctx.getApplicationContext().stopService(i);
			}
	    }
	}
	
	/**
	 * 
	 * @return is service binded
	 */
	public boolean isBinded() {
		return null != mConnection;
	}
	
	/**
	 * Returns HeadstartService object if connected
	 * @return HeadstartService
	 * @see HeadstartService
	 */
	public HeadstartService getService() {
		if (null == mConnection) {
			throw new IllegalStateException("Bind is not compeleted");
		}		
		return mConnection;
	}
	
	public interface BindListener {
		public void onBindCompleted();
	}
}
