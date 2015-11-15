package co.uk.android.lldc.tablet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import co.uk.android.lldc.R;

public class ParkWifiActivity extends Activity {
	private static final String TAG = ParkWifiActivity.class.getSimpleName();

	private WebView webView;
	RelativeLayout rlBackBtn;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_parkwifi);

		String url = LLDCApplication.PARK_WIFI_UEP_URL;

		rlBackBtn = (RelativeLayout) findViewById(R.id.rlBackBtn);
		webView = (WebView) findViewById(R.id.webViewWifi);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);

		setResult(2);

		rlBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showQuitDialog();
			}
		});

	}

	void showQuitDialog() {
		AlertDialog quitAlertDialog = new AlertDialog.Builder(
				ParkWifiActivity.this)
				.setTitle("Exit")
				.setMessage("Confirm exit?")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
						SplashScreen.finishActivity();
						finish();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {
							}
						}).create();
		quitAlertDialog.setCanceledOnTouchOutside(false);
		quitAlertDialog.show();
		return;
	}

}
