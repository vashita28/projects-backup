package uk.co.pocketapp.whotel.ui;

import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.util.Util;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

/**
 * Created by Pramod on 8/21/13.
 */
public class AgreeAndContinueActivity extends RootActivity {
	// Button btn_Agree_Continue;
	TextView tv_Agree_Continue;
	RelativeLayout rel_agree;

	EasyTracker easyTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_agree_continue);

		easyTracker = EasyTracker.getInstance(this);

		tv_Agree_Continue = (TextView) findViewById(R.id.textview_agree);
		tv_Agree_Continue.setIncludeFontPadding(false);

		rel_agree = (RelativeLayout) findViewById(R.id.rel_agree);
		rel_agree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Util.setAgreeContinueAccepted(AgreeAndContinueActivity.this,
						true);
				Intent intent = new Intent();
				intent.setClass(AgreeAndContinueActivity.this,
						MainActivity.class);
				startActivity(intent);

				if (easyTracker != null) {
					easyTracker.set(Fields.SCREEN_NAME, "Terms & Conditions");
					easyTracker.send(MapBuilder.createEvent("ui_action", // Event
																			// category
																			// (required)
							"button_press", // Event action (required)
							"t&c agree", // Event label
							null) // Event value
							.build());
				}
				finish();
			}
		});

		// btn_Agree_Continue = (Button)
		// findViewById(R.id.button_agree_continue);

		// btn_Agree_Continue.setTypeface(W_Hotel_Application.font_Bold);

		// btn_Agree_Continue.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		//
		// Util.setAgreeContinueAccepted(Agree_Continue_Activity.this, true);
		// Intent intent = new Intent();
		// intent.setClass(Agree_Continue_Activity.this,
		// MainActivity.class);
		// startActivity(intent);
		// finish();
		// }
		// });

	}
}
