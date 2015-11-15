package com.handpoint.headstart.android.ui;

import static com.handpoint.headstart.android.HeadstartService.ACTION_HANDLE_TIMEOUT;
import static com.handpoint.headstart.android.HeadstartService.ACTION_VERIFY_SIGNATURE;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_ERROR;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_OPERATION_CANCEL;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_FINANCIAL_TRANSACTION_FINISHED;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_TRANSACTION_STATE_CHANGE;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_ERROR_DESCRIPTION;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_ERROR_CODE;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_FINANCIAL_RESULT;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_MERCHANT_RECEIPT_TEXT;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_TRANSACTION_STATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import com.handpoint.headstart.android.ParcelDeviceState;
import com.handpoint.headstart.android.ParcelFinancialTransactionResult;
import com.handpoint.headstart.api.FinancialTransactionResult;
import com.handpoint.headstart.api.HeadstartOperationException;
import com.handpoint.headstart.eft.DeviceState;
import com.handpoint.util.logging.ApplicationLogger;
import com.handpoint.util.logging.Level;
import com.handpoint.util.logging.Logger;

/**
 * 
 * Default progress dialog during financial transaction.
 *
 */
public class FTProgressActivity extends Activity {

	private static final String TAG = FTProgressActivity.class.getSimpleName();
	private static Logger logger = ApplicationLogger.getLogger(TAG);
	
	private static final int BUTTON_TAG_CANCEL = 1;
	private static final int BUTTON_TAG_DONE = 2;
	
	private static final int DIALOG_TIMEOUT_ID = 0;
	
    private PowerManager.WakeLock mWakeLock;
    TextView mMessageView;
    HeadstartServiceConnection mConnection;
	int mOperationType = 0;
	String mCurrency;
	Button mButton;
	boolean mRegistered;
	String mErrorMessage;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progress_dialog);
		
		TextView title = (TextView) findViewById(R.id.alertTitle);
		title.setText(R.string.ft_progress_title);
		mMessageView = (TextView) findViewById(R.id.message);
		mMessageView.setText(R.string.ft_started);
		ImageView icon = (ImageView) findViewById(R.id.icon);
		icon.setImageResource(R.drawable.icon);
		mButton = (Button) findViewById(R.id.cancelButton);
        mButton.setTag(BUTTON_TAG_CANCEL);
		mButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Integer action = (Integer)v.getTag();
				
				switch (action.intValue()) {
				case BUTTON_TAG_CANCEL:
					cancelCurrentOperation();
					break;

				default:
					doneCurrentOperation();
					break;
				}
			}
		});
		
		initErrorState(savedInstanceState, getIntent());		
    	mConnection = new HeadstartServiceConnection(this, false, new HeadstartServiceConnection.BindListener() {
			
			public void onBindCompleted() {
				updateViewState();
			}
		});
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(
        		PowerManager.SCREEN_DIM_WAKE_LOCK |
        		PowerManager.ON_AFTER_RELEASE, "com.handpoint.headstart.hal");
		mWakeLock.acquire();
	}
	
	void updateViewState() {
		if (!mConnection.isBinded()) {
			return;
		}		
		if (null != mErrorMessage) {
			onTransactionError(mErrorMessage, 0);
		} else if (!mConnection.getService().isInTransaction()) {
			finish();
		} else {
			onDeviceState(mConnection.getService().getDeviceState());
		}
	}
	
	private void initErrorState(Bundle savedInstanceState, final Intent creator) {
		mErrorMessage = 
				creator.hasExtra(HeadstartService.EXTRA_ERROR_DESCRIPTION) ?
						creator.getStringExtra(HeadstartService.EXTRA_ERROR_DESCRIPTION) :
							null != savedInstanceState ?
						savedInstanceState.getString(HeadstartService.EXTRA_ERROR_DESCRIPTION) :
							null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWakeLock.isHeld()) {
			mWakeLock.release();
		}
    	if (null != mConnection) {
    		mConnection.doUnbindService(false);
    	}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerBroadcastReceivers();
		mRegistered = true;
		updateViewState();
		if (HeadstartService.ACTION_HANDLE_TIMEOUT.equals(getIntent().getAction())) {
			showDialog(DIALOG_TIMEOUT_ID);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mRegistered) {
			unregisterReceiver(mBtServiceReceiver);
			mRegistered = false;
		}
	}
	
	/**
	 * Sends command for cancel current operation to service
	 * 
	 */
	protected void cancelCurrentOperation() {
		try {
            mConnection.getService().cancelFinancialTransaction();
			mMessageView.setText(R.string.cancelling);										
		} catch (HeadstartOperationException e) {
			logger.log(Level.SEVERE, "Error canceling Financial Transaction", e);
		}
	}
	
	protected void doneCurrentOperation() {
		finish();
	}

	public void onDeviceState(DeviceState deviceState) {
		logger.log(Level.FINE, "Financial Transaction state changed: " + deviceState.getState() + "=" + deviceState.getStatusMessage());
		mMessageView.setText(deviceState.getStatusMessage());
		mButton.setEnabled(deviceState.isCancelAllowed());
	}

	public void onTransactionCompleted(FinancialTransactionResult result) {
		logger.log(Level.FINE, "Financial Transaction completed:" + result);
		finish();		
	}

	public void onTransactionError(String error, int code) {
		logger.log(Level.FINE, "Error occured during Financial Transaction: " + error + (0 == code ? "" : "; code:"+code));
		mButton.setTag(BUTTON_TAG_DONE);
        mButton.setText(R.string.done);
        mButton.setEnabled(true);
		mMessageView.setText(com.handpoint.headstart.R.string.error_common);										
	}

	@Override
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    switch(id) {
	    case DIALOG_TIMEOUT_ID:
	    	dialog = createTimeoutDialog();
	        break;
	    default:
	        dialog = null;
	    }
	    return dialog;
	}
	
	private Dialog createTimeoutDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.th_message)
		       .setCancelable(false)
		       .setPositiveButton(R.string.th_positive_button_label, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   mConnection.getService().timeoutHandleResult(true);
		           }
		       })
		       .setNegativeButton(R.string.th_negative_button_label, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   mConnection.getService().timeoutHandleResult(false);
		           }
		       });
		return builder.create();
	}
	
	public void handleTimeout() {
		logger.log(Level.FINE, "Timeout is occured");
		showDialog(DIALOG_TIMEOUT_ID);				
	}

	public void verifySignature(String merchantReceiptText) {
		Intent intent = new Intent(HeadstartService.ACTION_VERIFY_SIGNATURE);
		intent.putExtra(HeadstartService.EXTRA_MERCHANT_RECEIPT_TEXT, merchantReceiptText);
		startActivity(intent);
	}
	
	private void registerBroadcastReceivers() {
		IntentFilter btServiceFilter = new IntentFilter();
        btServiceFilter.addAction(BROADCAST_TRANSACTION_STATE_CHANGE);
        btServiceFilter.addAction(BROADCAST_FINANCIAL_TRANSACTION_FINISHED);
        btServiceFilter.addAction(BROADCAST_ERROR);
        btServiceFilter.addAction(BROADCAST_OPERATION_CANCEL);
        btServiceFilter.addAction(ACTION_VERIFY_SIGNATURE);
        btServiceFilter.addAction(ACTION_HANDLE_TIMEOUT);
        
        registerReceiver(mBtServiceReceiver, btServiceFilter);
	}
	
	/**
	 * Broadcast receiver for handling events from service 
	 */
	private BroadcastReceiver mBtServiceReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BROADCAST_TRANSACTION_STATE_CHANGE.equals(intent.getAction())) {
				ParcelDeviceState parcel = intent.getParcelableExtra(EXTRA_TRANSACTION_STATE);
				if (null != parcel) {
					onDeviceState(parcel.getResult());
				}
			} else if (BROADCAST_FINANCIAL_TRANSACTION_FINISHED.equals(intent.getAction())) {
				ParcelFinancialTransactionResult parcel = intent.getParcelableExtra(EXTRA_FINANCIAL_RESULT);
				if (null != parcel) {
					onTransactionCompleted(parcel.getResult());
				}
			} else if (ACTION_VERIFY_SIGNATURE.equals(intent.getAction())) {
				String merchantReceiptText = intent.getStringExtra(EXTRA_MERCHANT_RECEIPT_TEXT);
				verifySignature(merchantReceiptText);
			} else if (ACTION_HANDLE_TIMEOUT.equals(intent.getAction())) {
				handleTimeout();
			} else if (BROADCAST_ERROR.equals(intent.getAction())) {
				String errorMessage = intent.getStringExtra(EXTRA_ERROR_DESCRIPTION);
				int code = intent.getIntExtra(EXTRA_ERROR_CODE, 0);
				onTransactionError(errorMessage, code);
				mConnection.getService().setLastTransactionHandled();
			} else if (BROADCAST_OPERATION_CANCEL.equals(intent.getAction())) {
				doneCurrentOperation();
			}
		}
		
	};	

}
