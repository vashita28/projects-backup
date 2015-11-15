package com.android.cabapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.android.cabapp.R;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class MyJobsOverlayActivity extends CabAppParentActivity {

	RelativeLayout rlMyjobsOkyGotIt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_myjobs_overlay);

		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		rlMyjobsOkyGotIt = (RelativeLayout) findViewById(R.id.rlMyjobsOkyGotIt);
		rlMyjobsOkyGotIt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Util.setIsOverlaySeen(MyJobsOverlayActivity.this, true,
						MainActivity.class.getSimpleName());
				finish();

				Fragment fragment = new com.android.cabapp.fragments.MyJobsFragment();
				if (fragment != null) {
					((MainActivity) Util.mContext).replaceFragment(fragment,
							true);
					((MainActivity) Util.mContext)
							.setSlidingMenuPosition(Constants.MY_JOBS_FRAGMENT);

					((MainActivity) Util.mContext).slidingMenu.toggle();
				}

			}
		});

	}
}
