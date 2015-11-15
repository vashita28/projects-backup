package com.uk.pocketapp.fragments;

import co.uk.pocketapp.gmd.R;
import com.uk.pocketapp.common.Common;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

public class ChangesActivity extends FragmentActivity {
	
	private String mPath = "";
	String mSDpath = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(R.layout.fragment_layout);
		
		Intent intent = getIntent();
		mPath = intent.getStringExtra(Common.EXTRA_MESSAGE);

		if (mPath.equals("")) { 
			finish();
			Common.showLogCat("No path: "+mPath);
		} else {
			Common.sdPath = mPath;
			Common.showLogCat("Get path: "+mPath);
		}

	}

}
