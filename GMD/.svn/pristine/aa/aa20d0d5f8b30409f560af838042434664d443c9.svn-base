package co.uk.pocketapp.gmd.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.VideoView;
import co.uk.pocketapp.gmd.R;

public class VideoPlayer extends Activity implements OnCompletionListener,
		OnPreparedListener {

	String m_szVideoPath = "";
	String mTextPath = "";
	Integer currentPos;

	private VideoView mVideoView;
	private ProgressBar progressVideo;
	private Button nextBtn, prevBtn, rePlayBtn;
	private Integer currentIndex = 1;
	private Boolean isLogEnabled = false;

	SeekBar mSeekBar;
	ArrayList<String> stoppingCoordinates;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.videoplayer);
		progressVideo = (ProgressBar) findViewById(R.id.progress_video);

		mSeekBar = (SeekBar) findViewById(R.id.seekbar_video);
		mSeekBar.setVisibility(View.GONE);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		nextBtn = (Button) findViewById(R.id.nextButton);
		prevBtn = (Button) findViewById(R.id.prevButton);
		rePlayBtn = (Button) findViewById(R.id.rePlayButton);
		nextBtn.setBackgroundColor(Color.TRANSPARENT);
		prevBtn.setBackgroundColor(Color.TRANSPARENT);
		rePlayBtn.setBackgroundColor(Color.TRANSPARENT);
		// rePlayBtn.setEnabled(false);
		// nextBtn.setEnabled(false);
		// prevBtn.setEnabled(false);

		if (isLogEnabled)
			Log.d("VideoPlayer-onCreate", " m_szVideoPath :: " + m_szVideoPath);
		if (isLogEnabled)
			Log.d("VideoPlayer-onCreate", "mTextPath :: " + mTextPath);
		if (getIntent().getExtras() != null) {
			m_szVideoPath = getIntent().getStringExtra("video_path");
			mTextPath = getIntent().getStringExtra("text_path");
		}
		// else
		// m_szVideoPath = "android.resource://" + getPackageName() + "/"
		// + R.raw.gmd;

		stoppingCoordinates = new ArrayList<String>();
		loadData(mTextPath);
		videoPlayer();

		rePlayBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentIndex >= 2) {
					currentIndex = currentIndex - 2;

					nextBtn.setEnabled(false);
					prevBtn.setEnabled(false);
					rePlayBtn.setEnabled(false);
					String startingIndex = stoppingCoordinates
							.get(currentIndex);
					String[] separated = startingIndex.split(":");
					Integer startingValue = Integer.parseInt(separated[1])
							* 60000 + Integer.parseInt(separated[2]) * 1000
							+ Integer.parseInt(separated[3]);

					if (isLogEnabled)
						Log.d("VideoPlayer-rePlayBtn",
								" ############## PLAY ############## STARTING INDEX :: "
										+ startingIndex);
					mVideoView.seekTo(startingValue);
					mSeekBar.setProgress(startingValue);
					mVideoView.start();
					currentIndex++;
					if (isLogEnabled)
						Log.d("VideoPlayer-rePlayBtn",
								" ############## PLAY ############## stoppingIndex AFTER :: "
										+ stoppingCoordinates.get(currentIndex));
					mSeekBar.postDelayed(onEverySecond, 1);
				}
			}
		});

		prevBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentIndex >= 4) {
					currentIndex = currentIndex - 4;

					nextBtn.setEnabled(false);
					prevBtn.setEnabled(false);
					rePlayBtn.setEnabled(false);
					String stoppingIndex = stoppingCoordinates
							.get(currentIndex);
					String[] separated = stoppingIndex.split(":");
					Integer stoppingValue = Integer.parseInt(separated[1])
							* 60000 + Integer.parseInt(separated[2]) * 1000
							+ Integer.parseInt(separated[3]);

					if (isLogEnabled)
						Log.d("VideoPlayer-prevBtn",
								" ############## PLAY ##############  BACK STOP stoppingIndex :: "
										+ stoppingIndex);
					mVideoView.seekTo(stoppingValue);
					mVideoView.start();
					mSeekBar.setProgress(stoppingValue);
					currentIndex++;
					if (isLogEnabled)
						Log.d("VideoPlayer-prevBtn",
								" ############## PLAY ##############  BACK STOP stoppingIndex AFTER :: "
										+ stoppingCoordinates.get(currentIndex));
					mSeekBar.postDelayed(onEverySecond, 1);
				}
			}
		});

		nextBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentIndex <= stoppingCoordinates.size()) {
					currentIndex++;
				}
				nextBtn.setEnabled(false);
				prevBtn.setEnabled(false);
				rePlayBtn.setEnabled(false);
				if (isLogEnabled)
					Log.d("VideoPlayer-nextBtn",
							" ############## PLAY ############## ");
				mVideoView.start();
				mSeekBar.setProgress(mVideoView.getCurrentPosition());
				mSeekBar.postDelayed(onEverySecond, 1);
				if (isLogEnabled)
					Log.d("VideoPlayer", "NEXT stoppingIndex :: "
							+ stoppingCoordinates.get(currentIndex));
			}
		});

	}

	public void videoPlayer() {
		// MediaController mediaController = new MediaController(this);
		// mediaController.setAnchorView(mVideoView);
		// mVideoView.setMediaController(mediaController);
		mVideoView.setOnPreparedListener(this);
		mVideoView.setOnCompletionListener(this);
		mVideoView.setKeepScreenOn(true);
		mVideoView.setVideoURI(Uri.parse(m_szVideoPath));

		mSeekBar.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return true;
			}
		});
	}

	private Runnable onEverySecond = new Runnable() {

		@Override
		public void run() {

			if (mSeekBar != null) {
				int val = mVideoView.getCurrentPosition();

				mSeekBar.setProgress(val);
				String stoppingIndex = null;
				try {
					stoppingIndex = stoppingCoordinates.get(currentIndex);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (stoppingIndex != null) {
					String[] separated = stoppingIndex.split(":");
					Integer stoppingValue = Integer.parseInt(separated[1])
							* 60000 + Integer.parseInt(separated[2]) * 1000
							+ Integer.parseInt(separated[3]);
					if (isLogEnabled)
						Log.d("VideoPlayer", "stoppingValue :: "
								+ stoppingIndex + " ::Value:: " + stoppingValue);
					if (isLogEnabled)
						Log.d("VideoPlayer", "video current position :: " + val);
					// nextBtn.setEnabled(false);
					// prevBtn.setEnabled(false);
					if (val >= stoppingValue) {
						mVideoView.pause();
						Log.d("VideoPlayer",
								" ############## PAUSED ############## ");
						currentIndex++;
						nextBtn.setEnabled(true);
						prevBtn.setEnabled(true);
						rePlayBtn.setEnabled(true);
					}
					// if (stoppingCoordinates.contains(new Integer(4000))) {
					// mVideoView.pause();
					// }
				}
			}

			if (mVideoView.isPlaying()) {
				mSeekBar.postDelayed(onEverySecond, 1);
			}

		}
	};

	/** This callback will be invoked when the file is ready to play */
	@Override
	public void onPrepared(MediaPlayer vp) {

		mVideoView.start();
		if (isLogEnabled)
			Log.d("Sangam",
					"video current position :: "
							+ mVideoView.getCurrentPosition());
		progressVideo.setVisibility(View.GONE);
		mSeekBar.setMax(mVideoView.getDuration());
		mSeekBar.postDelayed(onEverySecond, 1);
	}

	/** This callback will be invoked when the file is finished playing */
	@Override
	public void onCompletion(MediaPlayer mp) {
		// Statements to be executed when the video finishes.
		this.finish();
	}

	/** Use screen touches to toggle the video between playing and paused. */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			// if (mVideoView.isPlaying()) {
			// mVideoView.pause();
			// } else {
			// if(isLogEnabled)Log.d("VideoPlayer-onTouch",
			// " ############## PLAY ############## ");
			// mVideoView.start();
			// mSeekBar.setProgress(mVideoView.getCurrentPosition());
			// mSeekBar.postDelayed(onEverySecond, 1);
			// }
			return true;
		} else {
			return false;
		}
	}

	private void loadData(String path) {

		File file = new File(path);

		// For each entry the following lines are repeated

		HashMap<String, String> hmap = new HashMap<String, String>();
		String line = "";

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				stoppingCoordinates.add(line);
				if (isLogEnabled)
					Log.d("VideoPlayer-loadData", "line ::  " + line);
			}
			br.close();
		} catch (IOException e) {
			if (isLogEnabled)
				Log.d("File Read test: Error= ", e.getMessage());
		}

	}
}
