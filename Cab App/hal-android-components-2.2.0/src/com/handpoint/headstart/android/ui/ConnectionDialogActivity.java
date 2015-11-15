package com.handpoint.headstart.android.ui;

import static com.handpoint.headstart.android.HeadstartService.ACTION_CONNECT_PED;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_BACK_RESULT;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_REMOTE_DEVICE;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.handpoint.headstart.R;
import com.handpoint.headstart.android.HeadstartService;
import com.handpoint.headstart.android.ParcelDeviceDescriptor;
import com.handpoint.headstart.api.DeviceDescriptor;
import com.handpoint.util.logging.ApplicationLogger;
import com.handpoint.util.logging.Level;
import com.handpoint.util.logging.Logger;

/**
 * Default custom connection dialog.
 *
 */
public class ConnectionDialogActivity extends Activity {

	private static final String TAG = ConnectionDialogActivity.class.getSimpleName();
	private static Logger logger = ApplicationLogger.getLogger(TAG);
	
    private static final int REQUEST_CONNECT_TO_PED_RESULT_CODE = 1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger.log(Level.FINEST, "onCreate()");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_dialog);
		
		TextView title = (TextView) findViewById(R.id.alertTitle);
		title.setText(R.string.connect_ped_title);
		TextView messageView = (TextView) findViewById(R.id.message);
		messageView.setText(R.string.connect_ped_message);
		Button positiveButton = (Button) findViewById(R.id.button1);
		positiveButton.setText(R.string.yes);
		positiveButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
	            Intent serverIntent = new Intent(ConnectionDialogActivity.this, DeviceListActivity.class);
	            startActivityForResult(serverIntent, REQUEST_CONNECT_TO_PED_RESULT_CODE);
			}
		});
		Button negativeButton = (Button) findViewById(R.id.button2);
		negativeButton.setText(R.string.no);
		negativeButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				onCancelNotification();
				finish();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (REQUEST_CONNECT_TO_PED_RESULT_CODE == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				ParcelDeviceDescriptor parcel = (ParcelDeviceDescriptor)data.getParcelableExtra(DeviceListActivity.EXTRA_DEVICE);
				if (null != parcel) {
					makeConnectToPed(parcel.getResult());
				} else {
					//TODO: handle no device description in Intent
				}
				finish();
			} else {
				onCancelNotification();
				finish();
			}
		}
	}

	/**
	 * Sends "com.handpoint.headstart.hal.ACTION_CONNECT_PED" intent with extra parameter - device descriptor
	 * @param descriptor
	 */
	private void makeConnectToPed(DeviceDescriptor descriptor) {
		Intent startIntent = new Intent(this, HeadstartService.class);
		startIntent.setAction(ACTION_CONNECT_PED);
		startIntent.putExtra(EXTRA_REMOTE_DEVICE, new ParcelDeviceDescriptor(descriptor));
		startService(startIntent);
	}

	/**
	 * Send "com.handpoint.headstart.hal.connection.back" intent for notification about cancelling connection.
	 */
	private void onCancelNotification() {
		logger.log(Level.FINE, "Send broadcast to BT service about cancel connection");
		Intent intent = new Intent(HeadstartService.BACK_ACTION_CONNECT);
		intent.putExtra(EXTRA_BACK_RESULT, HeadstartService.BACK_RESULT_CANCEL);
		sendBroadcast(intent);		
	}
	
}
