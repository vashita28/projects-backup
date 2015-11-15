package com.handpoint.headstart.android.ui;

import static com.handpoint.headstart.android.HeadstartService.BACK_ACTION_BLUETOOTH;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_BACK_RESULT;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.handpoint.headstart.R;
import com.handpoint.headstart.android.HeadstartService;

/**
 * Default dialog for managing bluetooth state.
 *
 */
public class BluetoothDialogActivity extends Activity {

	public static final String EXTRA_TURN_OFF = "com.handpoint.headstart.hal.bt.turnoff"; 
	public static final String EXTRA_BLUETOOTH_STATE = "com.handpoint.headstart.hal.bt.state"; 
	
	BluetoothAdapter mBluetoothAdapter;
	ProgressDialog mEnablingBtDialog;
    int mState;
    boolean isTurnOff;
    
    TextView messageView;
    Button positiveButton;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_dialog);
		
		TextView title = (TextView) findViewById(R.id.alertTitle);
		title.setText(R.string.bt_enable_dialog_title);
		isTurnOff = getIntent().getBooleanExtra(EXTRA_TURN_OFF, false); 
		messageView = (TextView) findViewById(R.id.message);
		if (isTurnOff) {
			messageView.setText(R.string.bt_disable_dialog_text);
		} else {
			messageView.setText(R.string.bt_enable_dialog_text);
		}
		positiveButton = (Button) findViewById(R.id.button1);
		positiveButton.setText(R.string.yes);
		positiveButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {				
				if ((!isTurnOff && !mBluetoothAdapter.enable()) || (isTurnOff && !mBluetoothAdapter.disable())) {
					messageView.setText(R.string.bt_operation_error_text);
					positiveButton.setText(R.string.try_again);
				}
			}
		});
		Button negativeButton = (Button) findViewById(R.id.button2);
		negativeButton.setText(R.string.no);
		negativeButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
        registerReceiver(mBtStateListener, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
        	finish();
        }
        mState = mBluetoothAdapter.getState();
	}
    /**
     * Bluetooth change state listener
     */
    private BroadcastReceiver mBtStateListener = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
			switch (state) {
			case BluetoothAdapter.STATE_ON:
			case BluetoothAdapter.STATE_OFF:
				if (null != mEnablingBtDialog) {
					mEnablingBtDialog.dismiss();
					mEnablingBtDialog = null;
				}
				mState = state;
				finish();
				break;
			case BluetoothAdapter.STATE_TURNING_ON:
				mEnablingBtDialog = 
					ProgressDialog.show(
							context, 
							getString(R.string.bt_enable_dialog_title), 
							getString(R.string.turning_on_bt));
				mEnablingBtDialog.setCancelable(false);
			break;
			case BluetoothAdapter.STATE_TURNING_OFF:
				mEnablingBtDialog = 
					ProgressDialog.show(
							context, 
							getString(R.string.bt_enable_dialog_title), 
							getString(R.string.turning_off_bt));
				mEnablingBtDialog.setCancelable(false);
			break;
			}
		}
	};

	/**
	 * On destroy activity sends intent with action "com.handpoint.headstart.hal.bt.back" and
	 * extra parameter "com.handpoint.headstart.hal.extra_back_status" with status of 
	 * operation (completed or cancelled)  
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBtStateListener);
		Intent intent = new Intent(BACK_ACTION_BLUETOOTH);
		intent.putExtra(
				EXTRA_BACK_RESULT, 
				BluetoothAdapter.STATE_ON == mState ? 
						HeadstartService.BACK_RESULT_OK: HeadstartService.BACK_RESULT_CANCEL);
		sendBroadcast(intent);
	}
}
