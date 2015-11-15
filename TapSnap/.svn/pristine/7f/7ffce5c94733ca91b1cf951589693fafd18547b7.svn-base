package com.coudriet.snapnshare.android;

import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends Activity {

	int secondsLeft = 0;
	private String actionBarTitle;
	private String actionBarSubtitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_view_image);

		// Show the Up button in the action bar.
		setupActionBar();

		setProgressBarIndeterminateVisibility(true);

		actionBarTitle = getIntent().getExtras().getString("messageSender");
		actionBarSubtitle = getIntent().getExtras().getString("messageSentAt");
		Uri imageUri = getIntent().getData();

		ActionBar ab = getActionBar();
		ab.setIcon(getResources().getDrawable(R.drawable.logo));
		ab.setTitle("Sent from " + actionBarTitle);
		ab.setSubtitle(actionBarSubtitle);

		final ImageView imageView = (ImageView) findViewById(R.id.imageView);
		final TextView countDownText = (TextView) findViewById(R.id.counterTextView);

		Picasso.with(getApplicationContext()).load(imageUri).centerCrop()
				.resize(1180, 1700)

				.into(imageView, new Callback() {

					@Override
					public void onSuccess() {

						setProgressBarIndeterminateVisibility(false);

						imageView.setVisibility(View.VISIBLE);

						new CountDownTimer(15000, 100) {
							public void onTick(long ms) {
								if (Math.round((float) ms / 1000.0f) != secondsLeft) {
									secondsLeft = Math
											.round((float) ms / 1000.0f);
									countDownText.setText("" + secondsLeft);
								}

								// Log.i("test","ms="+ms+" till finished="+secondsLeft
								// );
							}

							public void onFinish() {
								countDownText.setText("0");

								finish();
							}
						}.start();
					}

					@Override
					public void onError() {

						imageView.setVisibility(View.INVISIBLE);
					}
				});
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
