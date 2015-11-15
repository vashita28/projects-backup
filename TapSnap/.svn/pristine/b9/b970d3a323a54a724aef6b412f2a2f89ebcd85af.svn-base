package com.coudriet.tapsnap.android;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.coudriet.tapsnap.utility.Common;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VideoViewActivity extends Activity implements Callback {

	@Override
	protected void onDestroy() {
		stopRecording();
		super.onDestroy();
	}

	private SurfaceHolder mSurfaceHolder;
	private SurfaceView mSurfaceView;
	public MediaRecorder mrec = new MediaRecorder();
	private Camera mCamera;
	private Button mbtnStart;
	private CountDownTimer mt;
	private boolean mflag = false;
	private boolean mflagbackpress = false;

	private int mResult;

	private ActionBar actionBar;

	@Override
	protected void onStart() {
		mCamera = Camera.open();
		mResult = Common.setCameraDisplayOrientation(VideoViewActivity.this, 1,
				mCamera);
		super.onStart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_view);

		// Show the Up button in the action bar.
		actionBar = getActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.logo));

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);

		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		// surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mbtnStart = (Button) findViewById(R.id.btnStart);
		mbtnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Common.flag = true;

				if (mbtnStart.getText().toString().equals("start")) {

					try {
                         mbtnStart.setEnabled(false);
                     	mflag = true;
						mflagbackpress = true;
						mbtnStart.setText("stop");
						startRecording();
						mt.start();

						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								

								 mbtnStart.setEnabled(true);
								
							}
						}, 1000);

						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {

								if (mflag) {
									if(mrec!=null)
									{
									mrec.stop();
									mrec.reset();
									mrec.release();
									//mrec = null;
									}
									finish();
									mt.cancel();

									callReci();
								}
							}
						}, 60000);

					} catch (Exception e) {

						String message = e.getMessage();
						Log.i(null, "Problem " + message);
						if(mrec!=null)
						{
						mrec.reset();
						mrec.release();
						//mrec = null;
						}
					}

				} else {

					mflag = false;
					mflagbackpress = false;
					if(mrec!=null)
					{
					mrec.stop();
					mrec.reset();
					mrec.release();
					//mrec = null;
					}
				//	mbtnStart.setText("start");
					finish();

					callReci();
				}

			}
		});

		final TextView _tv = (TextView) findViewById(R.id.txtTimer);
		mt = new CountDownTimer(60000, 1000) {

			@SuppressLint("SimpleDateFormat")
			public void onTick(long millisUntilFinished) {
				_tv.setText("Remaining time: "
						+ new SimpleDateFormat("ss").format(new Date(
								millisUntilFinished)));
			}

			public void onFinish() {
				_tv.setText("done!");
			}
		};
	}

	private void callReci() {

		Intent intent = new Intent(VideoViewActivity.this,
				RecipientsActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onStop() {
		stopRecording();
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

	protected void startRecording() throws IOException {

		String filename;
		String path;

		filename = "VID_.3gp";

		path = Environment.getExternalStorageDirectory() + File.separator
				+ filename;

		mrec = new MediaRecorder();

		mCamera.lock();
		mCamera.unlock();

		// Please maintain sequence of following code.
		mrec.setCamera(mCamera);
		mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
		mrec.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mrec.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
		mrec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mrec.setPreviewDisplay(mSurfaceHolder.getSurface());
		mrec.setOrientationHint(mResult);
		mrec.setOutputFile(path);
		mrec.prepare();
		mrec.start();

	}

	protected void stopRecording() {

		if (mflag) {
			if (mrec != null) {
				mrec.stop();
				mrec.reset();
				mrec.release();
				//mrec=null;
				mCamera.release();
			//	mCamera.lock();
			}

			releaseMediaRecorder();
			releaseCamera();
		}
	}

	private void releaseMediaRecorder() {

		if (mrec != null) {
			mrec.reset(); // clear recorder configuration
			mrec.release(); // release the recorder object
			//mrec=null;
		}
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		if (mCamera != null) {

			try {
				mCamera.setPreviewDisplay(mSurfaceHolder);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Fail to connect camera service!", Toast.LENGTH_SHORT)
						.show();
			}

			Log.i("Surface", "Created");
		} else {
			Toast.makeText(getApplicationContext(), "Camera not available!",
					Toast.LENGTH_LONG).show();

			finish();
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		mSurfaceView.getHolder().removeCallback(this);
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if (mflagbackpress) {

			mflagbackpress = false;
			mflag = false;
         if(mrec!=null)
         {
			mrec.stop();
			mrec.reset();
			mrec.release();
		//	mrec = null;
         }	finish();
			mt.cancel();

			callReci();
		}
	}

}
