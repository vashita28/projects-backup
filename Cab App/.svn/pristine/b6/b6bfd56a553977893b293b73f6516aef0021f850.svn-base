package com.handpoint.headstart.android.ui;

import static com.handpoint.headstart.android.HeadstartService.BROADCAST_CONNECTION_STATE_CHANGE;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_ERROR;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.handpoint.headstart.R;
import com.handpoint.headstart.android.HeadstartService;
import com.handpoint.headstart.android.HeadstartServiceConnection;
import com.handpoint.headstart.android.ParcelDeviceDescriptor;
import com.handpoint.headstart.api.DeviceConnectionState;
import com.handpoint.headstart.api.DeviceDescriptor;

/**
 * 
 * Default progress dialog during connection to remote device.
 *
 */
public class ConnectionProgressActivity extends Activity {
	
    private PowerManager.WakeLock mWakeLock;
    AlertDialog mErrorDialog;
    TextView mMessageView;
    HeadstartServiceConnection mConnection;
    DeviceDescriptor mDeviceDescriptor;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ParcelDeviceDescriptor parcel = getIntent().getParcelableExtra(HeadstartService.EXTRA_REMOTE_DEVICE);
		if (null != parcel) {
			mDeviceDescriptor = parcel.getResult();
		}
        registerBroadcastReceivers();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progress_dialog);
		
		TextView title = (TextView) findViewById(R.id.alertTitle);
		title.setText(R.string.connect_ped_title);
		mMessageView = (TextView) findViewById(R.id.message);
		mMessageView.setText(getString(R.string.connecting));
		ImageView icon = (ImageView) findViewById(R.id.icon);
		icon.setImageResource(R.drawable.icon);
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				cancelCurrentOperation();
			}
		});
    	mConnection = new HeadstartServiceConnection(this, false, new HeadstartServiceConnection.BindListener() {
			
			public void onBindCompleted() {
				if (mConnection.getService().getCurrentConnectionState() == DeviceConnectionState.CONNECTED) {
					finish();
					return;
				}
			}
		});
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(
        		PowerManager.SCREEN_DIM_WAKE_LOCK |
        		PowerManager.ON_AFTER_RELEASE, "com.handpoint.headstart.hal");
		mWakeLock.acquire();
	}
	
	private void registerBroadcastReceivers() {
		IntentFilter btServiceFilter = new IntentFilter();
        btServiceFilter.addAction(BROADCAST_CONNECTION_STATE_CHANGE);
        btServiceFilter.addAction(BROADCAST_ERROR);
        registerReceiver(mBtServiceReceiver, btServiceFilter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWakeLock.isHeld()) {
			mWakeLock.release();
		}
		unregisterReceiver(mBtServiceReceiver);
    	if (null != mConnection) {
    		mConnection.doUnbindService(false);
    	}
	}
	/**
	 * Sends command for cancel current operation to service
	 * 
	 */
	protected void cancelCurrentOperation() {
		if (mConnection.isBinded() && mConnection.getService().getCurrentConnectionState() == DeviceConnectionState.CONNECTING) {
			mConnection.getService().cancelConnectionProccess();
		}
		finish();
	}
		
	/**
	 * Broadcast receiver for handling events from service 
	 */
	private BroadcastReceiver mBtServiceReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BROADCAST_CONNECTION_STATE_CHANGE.equals(intent.getAction())) {
				int status = intent.getIntExtra(HeadstartService.EXTRA_PED_STATE, 0);
				if (status == DeviceConnectionState.CONNECTED) {
					finish();
				}
				if (status == DeviceConnectionState.DISCONNECTED) {
					finish();
				}
			} else if (BROADCAST_ERROR.equals(intent.getAction())) {
				finish();
			}
		}
		
	};	
}
