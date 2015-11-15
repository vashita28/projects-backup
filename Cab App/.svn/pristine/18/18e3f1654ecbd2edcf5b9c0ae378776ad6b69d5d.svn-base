package com.handpoint.headstart.android.ui;

import static com.handpoint.headstart.android.HeadstartService.EXTRA_SIGNATURE_TIMEOUT_SECONDS;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_MERCHANT_RECEIPT_TEXT;

import android.os.Handler;
import com.handpoint.headstart.R;
import com.handpoint.headstart.android.HeadstartServiceConnection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class SignatureDialogActivity extends Activity {


    private HeadstartServiceConnection mConnection;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signature_dialog);
		
		final Intent intent  = getIntent();
		TextView title = (TextView) findViewById(R.id.message);

        //Check if there is a timeout on the signature and kill activity within that time
        int timeout = 0;
        try {
            Integer.parseInt(intent.getStringExtra(EXTRA_SIGNATURE_TIMEOUT_SECONDS));
        } catch (NumberFormatException e) {}
        if(timeout>=5 && timeout<=65535){  //EFT Protocol legal values
            int delayMillis = timeout * 1000;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    mConnection.getService().signatureDecline();
                    finish();
                }
            }, delayMillis-1000); //make sure we timeout before the EFT client does
        }

        //Fetch the signature slip and display in GUI
        String merchantReceipt = intent.getStringExtra(EXTRA_MERCHANT_RECEIPT_TEXT);
		if (null != merchantReceipt) {
			title.setText(Html.fromHtml(merchantReceipt));
		}

		Button positiveButton = (Button) findViewById(R.id.button1);
		positiveButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mConnection.getService().signatureAccept();
				finish();
			}
		});
		Button negativeButton = (Button) findViewById(R.id.button2);
		negativeButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				mConnection.getService().signatureDecline();
				finish();
			}
		});				
    	mConnection = new HeadstartServiceConnection(this, false);
	}
	
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if (null != mConnection) {
    		mConnection.doUnbindService(false);
    	}
    }
	
}
