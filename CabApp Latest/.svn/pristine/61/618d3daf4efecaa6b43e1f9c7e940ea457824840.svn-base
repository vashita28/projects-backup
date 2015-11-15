package com.android.cabapp.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.cabapp.R;
import com.android.cabapp.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShowFullImageActivity extends RootActivity {

	private static final String TAG = ShowFullImageActivity.class
			.getSimpleName();

	Context _Context;

	ImageView ivFullImage;
	VideoView videoView;
	String szFilePath = "", szVideoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showfullimage);

		_Context = this;
		if (getIntent().hasExtra("filePath"))
			szFilePath = getIntent().getStringExtra("filePath");
		if (getIntent().hasExtra("videoPath"))
			szVideoPath = getIntent().getStringExtra("videoPath");

		ivFullImage = (ImageView) findViewById(R.id.ivFullImage);
		videoView = (VideoView) findViewById(R.id.videoView);

		if (szFilePath != null && !szFilePath.isEmpty()) {

			ImageLoader.getInstance().displayImage("file://" + szFilePath,
					ivFullImage);
			ivFullImage.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.GONE);

		} else if (szVideoPath != null && !szVideoPath.isEmpty()) {

			videoView.setVisibility(View.VISIBLE);
			ivFullImage.setVisibility(View.GONE);
			videoView.setVideoURI(Uri.parse(szVideoPath));
			videoView.start();

		} else {

			Util.showToastMessage(_Context,
					"Some error occured. Please try again", Toast.LENGTH_LONG);
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

	}

}
