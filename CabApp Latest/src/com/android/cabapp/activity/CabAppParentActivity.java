package com.android.cabapp.activity;

import android.app.Activity;
import android.os.Bundle;

import com.android.cabapp.R;

public class CabAppParentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Override how this activity is animated into view
        // The new activity is pulled in from the left and the current activity is kept still
        // This has to be called before onCreate
        overridePendingTransition(R.anim.pull_in_from_left, R.anim.hold);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Whenever this activity is paused (i.e. looses focus because another
		// activity is started etc)
		// Override how this activity is animated out of view
		// The new activity is kept still and this activity is pushed out to the
		// left
		overridePendingTransition(R.anim.hold, R.anim.pull_out_to_left);
	}
}
