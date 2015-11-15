package com.android.cabapp.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.fragments.ContactSupportFragment;
import com.android.cabapp.service.AndroidMultiPartEntity;
import com.android.cabapp.service.AndroidMultiPartEntity.ProgressListener;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.ReportIncident;
import com.android.cabapp.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ReportAnIncidentActivity extends RootActivity {

	private static final String TAG = ReportAnIncidentActivity.class.getSimpleName();

	public static final int TAKE_PHOTO_REQUEST = 0;
	public static final int PICK_PHOTO_REQUEST = 1;
	public static final int TAKE_VIDEO_REQUEST = 2;
	public static final int PICK_VIDEO_REQUEST = 3;

	ImageView ivAttachImage, ivLogo;// /ivIncident
	RelativeLayout relSend;
	EditText etMessage;
	LinearLayout llImages;
	HashMap<Integer, String> hmListOfImages;
	ArrayList<String> listVideo;

	View viewAddPhoto;
	File cameraFile;
	private Uri fileUri;
	String urlServer;
	long totalSize = 0;

	String imagePath = "", szMessage = "", videoPath = "";
	private ProgressDialog progressUploadDocument;

	@SuppressLint("UseSparseArrays")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_an_incident);

		mContext = this;
		hmListOfImages = new HashMap<Integer, String>();
		listVideo = new ArrayList<String>();
		urlServer = Constants.driverURL + "reportincident/?accessToken=" + Util.getAccessToken(mContext);

		etMessage = (EditText) findViewById(R.id.etMessage);
		ivAttachImage = (ImageView) findViewById(R.id.ivAttachImage);
		// ivIncident = (ImageView) findViewById(R.id.ivIncident);
		relSend = (RelativeLayout) findViewById(R.id.relReportAnIncidentSend);
		ivLogo = (ImageView) findViewById(R.id.ivLogo);
		llImages = (LinearLayout) findViewById(R.id.llImages);
		if (((LinearLayout) llImages).getChildCount() > 0)
			((LinearLayout) llImages).removeAllViews();

		ivAttachImage.setOnClickListener(new TextViewOnClickListener());
		relSend.setOnClickListener(new TextViewOnClickListener());

		ivLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				if (ContactSupportFragment.mHandler != null) {
					ContactSupportFragment.mHandler.sendEmptyMessage(1);
				}
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

	}

	class TextViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.relReportAnIncidentSend:

				Util.hideSoftKeyBoard(mContext, ivAttachImage);
				szMessage = etMessage.getText().toString().trim();

				if (szMessage.isEmpty()) {
					Util.showToastMessage(mContext, "Please add a message", Toast.LENGTH_LONG);
				} else if (llImages.getChildCount() == 0) {

					AlertDialog alertDialog = new AlertDialog.Builder(mContext)
							.setMessage("It would be more helpful if you provided us with an image/video for investigation")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog, int argWhich) {
									Util.hideSoftKeyBoard(mContext, ivAttachImage);
									AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
									builder.setItems(R.array.add_image_options, mDialogListener);
									AlertDialog dialog = builder.create();
									dialog.show();
								}
							}).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0, int arg1) {
									// TODO Auto-generated method stub
									ReportIncidentCall();
								}
							}).create();
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.show();
				} else {
					ReportIncidentCall();
				}

				break;

			case R.id.ivAttachImage:
				Util.hideSoftKeyBoard(mContext, ivAttachImage);
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setItems(R.array.add_image_options, mDialogListener);
				AlertDialog dialog = builder.create();
				dialog.show();
				break;
			}
		}
	}

	void ReportIncidentCall() {
		UploadFileToServer reportTask = new UploadFileToServer();
		reportTask.context = mContext;
		reportTask.szMessage = szMessage;
		reportTask.execute();
	}

	protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// isImagechanged = true;
			switch (which) {

			case 0: // Take picture
				if (hmListOfImages != null && hmListOfImages.size() <= 4) {
					Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getImageFileName()));
					startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
				} else {
					Util.showToastMessage(mContext, "Maximum images limit is 5", Toast.LENGTH_LONG);
				}

				break;

			case 1: // Choose picture
				if (hmListOfImages != null && hmListOfImages.size() <= 4) {
					if (Integer.decode(Build.VERSION.SDK) >= 19) {
						Intent intent = new Intent(Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(intent, PICK_PHOTO_REQUEST);
					} else {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PHOTO_REQUEST);
					}
				} else {
					Util.showToastMessage(mContext, "Maximum images limit is 5", Toast.LENGTH_LONG);
				}

				break;

			case 2:// Take video
				if (listVideo.size() == 0) {
					Intent takeVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
					if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {

						takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getVideoFileName()));
						/*
						 * android.intent.extra.videoQuality = 0 to capture low
						 * quality video android.intent.extra.durationLimit = 30
						 * to capture 30 seconds video only
						 * android.intent.extra.sizeLimit = 10000000 to record
						 * only 10mb of data
						 * 
						 * MORE-INFO
						 * http://developer.android.com/guide/topics/media
						 * /camera.html#intent-video
						 */
						takeVideoIntent.putExtra("android.intent.extra.videoQuality", 0);
						takeVideoIntent.putExtra("android.intent.extra.durationLimit", 30);
						takeVideoIntent.putExtra("android.intent.extra.sizeLimit", 10000000);

						startActivityForResult(takeVideoIntent, TAKE_VIDEO_REQUEST);
					}
				} else {
					Util.showToastMessage(mContext, "Maximum video limit is 1", Toast.LENGTH_LONG);
				}

				break;

			case 3:// Select video

				if (listVideo.size() == 0) {
					if (Integer.decode(Build.VERSION.SDK) >= 19) {
						Intent selectVideoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(selectVideoIntent, PICK_VIDEO_REQUEST);
					} else {
						Intent intent = new Intent();
						intent.setType("video/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
					}
				} else {
					Util.showToastMessage(mContext, "Maximum video limit is 1", Toast.LENGTH_LONG);
				}
				break;

			}

		}
	};

	@Override
	protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {

		boolean isCorrectFileSize = false, isCorrectFileDuration = false;
		if (resultCode == RESULT_OK) {
			imagePath = "";
			videoPath = "";
			if (requestCode == TAKE_PHOTO_REQUEST) {
				// camera picture
				imagePath = cameraFile.getPath();
				if (Constants.isDebug)
					Log.i(TAG, "Camera PhotoPath::>  " + imagePath);

			} else if (requestCode == PICK_PHOTO_REQUEST) {
				// gallery picture
				Uri selectedImageUri = intent.getData();
				imagePath = getPath(selectedImageUri);
				if (Constants.isDebug)
					Log.i(TAG, "Gallery PhotoPath::>  " + imagePath);

			} else if (requestCode == TAKE_VIDEO_REQUEST) {
				// camera video
				videoPath = cameraFile.getPath();
				if (Constants.isDebug)
					Log.i(TAG, "Camera VideoPath::>  " + videoPath);

				// Duration
				String szRecordedVideoTime = IsRecordedVideoCorrect(videoPath);
				if (szRecordedVideoTime != null && !szRecordedVideoTime.isEmpty()) {
					String duration = szRecordedVideoTime.replaceAll("\\D+", "");

					int recordedVideoTime = Integer.valueOf(duration);
					if (recordedVideoTime <= 30) {
						isCorrectFileDuration = true;
					} else {
						Util.showToastMessage(mContext, "Video duration should be 30 seconds", Toast.LENGTH_LONG);
						isCorrectFileDuration = false;
					}
					if (!isCorrectFileDuration)
						return;
				}

				// Size
				double length = cameraFile.length();
				isCorrectFileSize = IsVideoSizeCorrect(length);
				if (!isCorrectFileSize)
					return;

			} else if (requestCode == PICK_VIDEO_REQUEST) {
				// gallery video
				Uri selectedImageUri = intent.getData();
				File videoFile = new File(selectedImageUri.getPath());
				videoPath = getPath(selectedImageUri);

				// Duration
				isCorrectFileDuration = IsVideoDurationCorrect(intent.getData());
				if (!isCorrectFileDuration)
					return;

				double length = videoFile.length();
				isCorrectFileSize = IsVideoSizeCorrect(length);
				if (!isCorrectFileSize)
					return;

			}

			// Adding new row:
			LayoutInflater inflater = LayoutInflater.from(getBaseContext());
			viewAddPhoto = inflater.inflate(R.layout.row_reportanincident, null);

			final RelativeLayout rlVideoIcon = (RelativeLayout) viewAddPhoto.findViewById(R.id.rlVideoIcon);

			final ImageView ivThumbnail = (ImageView) viewAddPhoto.findViewById(R.id.ivThumbnailImage);

			if (requestCode == TAKE_PHOTO_REQUEST || requestCode == PICK_PHOTO_REQUEST) {

				ImageLoader.getInstance().displayImage("file://" + imagePath, ivThumbnail);
				rlVideoIcon.setVisibility(View.GONE);

			} else if (requestCode == TAKE_VIDEO_REQUEST || requestCode == PICK_VIDEO_REQUEST) {
				Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
				ivThumbnail.setImageBitmap(thumbnail);
				rlVideoIcon.setVisibility(View.VISIBLE);
				listVideo.add(videoPath);

			}
			llImages.addView(viewAddPhoto);

			ImageDetails imageDetails = new ImageDetails();
			imageDetails.view = viewAddPhoto;
			imageDetails.mapKey = llImages.getChildCount() - 1;
			imageDetails.szFilePath = imagePath;

			ivThumbnail.setTag(imageDetails);
			if (requestCode == TAKE_PHOTO_REQUEST || requestCode == PICK_PHOTO_REQUEST)
				hmListOfImages.put(llImages.getChildCount() - 1, imagePath);

			ivThumbnail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub

					ImageDetails imageDetails = (ImageDetails) ivThumbnail.getTag();
					final int imageNo = imageDetails.mapKey;
					final View imageView = imageDetails.view;
					final String szFilePath = imageDetails.szFilePath;

					final AlertDialog ImageOptionDialog = new AlertDialog.Builder(mContext).setMessage("Please select an option")
							.setPositiveButton("VIEW", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog, int argWhich) {
									Intent intentShowFullImage = new Intent(Util.mContext, ShowFullImageActivity.class);
									if (requestCode == TAKE_PHOTO_REQUEST || requestCode == PICK_PHOTO_REQUEST) {
										intentShowFullImage.putExtra("filePath", szFilePath);
									} else if (videoPath != null && !videoPath.isEmpty()) {
										intentShowFullImage.putExtra("videoPath", videoPath);
									}
									startActivity(intentShowFullImage);
								}
							}).setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog, int argWhich) {
									llImages.removeView(imageView);
									hmListOfImages.remove(imageNo);

									if (rlVideoIcon.getVisibility() == View.VISIBLE) {
										listVideo.clear();
									}
								}
							}).create();
					ImageOptionDialog.setCanceledOnTouchOutside(false);
					ImageOptionDialog.show();
				}
			});

		} else if (resultCode != RESULT_CANCELED) {
			Util.showToastMessage(mContext, "Sorry, there was an error!", Toast.LENGTH_LONG);
		}

	}

	boolean IsVideoSizeCorrect(double fileSize) {
		double kilobytes = (fileSize / 1024);
		double megabytes = (kilobytes / 1024);

		if (megabytes > 10) {
			Util.showToastMessage(mContext, "Maximum file size is 10mb", Toast.LENGTH_LONG);
			return false;
		} else {
			return true;
		}
	}

	String IsRecordedVideoCorrect(String filePath) {
		MediaPlayer mp = MediaPlayer.create(this, Uri.parse(filePath));
		int duration = mp.getDuration();
		mp.release();
		/* convert millis to appropriate time */
		// return String.format(
		// "%d min, %d sec",
		// TimeUnit.MILLISECONDS.toMinutes(duration),
		// TimeUnit.MILLISECONDS.toSeconds(duration)
		// - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
		// .toMinutes(duration)));

		return String
				.format("%d sec",
						TimeUnit.MILLISECONDS.toSeconds(duration)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

	}

	boolean IsVideoDurationCorrect(Uri data) {

		// Get duration
		Cursor cursor = MediaStore.Video.query(getContentResolver(), data,
				new String[] { MediaStore.Video.VideoColumns.DURATION });
		cursor.moveToFirst();

		String szDuration = cursor.getString(cursor.getColumnIndex("duration"));

		double duration = Double.valueOf(szDuration);
		double dDuration = duration / 1000;

		if (Constants.isDebug)
			Log.i(TAG, "Gallery VideoPath:>  " + videoPath + "  Duration:sec " + dDuration);

		if (dDuration > 30) {
			Util.showToastMessage(mContext, "Video duration should be 30 seconds", Toast.LENGTH_LONG);
			return false;
		} else {
			return true;
		}

	}

	public String dateToString(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	private File getImageFileName() {
		String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
		return cameraFile = new File(Environment.getExternalStorageDirectory() + File.separator
				+ getResources().getString(R.string.app_name), name + ".jpg");
	}

	private File getVideoFileName() {
		String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
		return cameraFile = new File(Environment.getExternalStorageDirectory() + File.separator
				+ getResources().getString(R.string.app_name), name + ".mp4");
	}

	public String getPath(Uri uri) {
		if (uri == null) {
			return null;
		}
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// returns the image path
		return uri.getPath();
	}

//	class ReportIncidentTask extends AsyncTask<String, String, String> {
//		Context context;
//		String szMessage = "";
//
//		// int nCurrentFile = 0;
//
//		@Override
//		protected void onPreExecute() {
//			// TODO Auto-generated method stub
//			super.onPreExecute();
//
//			progressUploadDocument = new ProgressDialog(context);
//			progressUploadDocument.setMessage("Uploading...");
//			progressUploadDocument.setCancelable(false);
//			progressUploadDocument.show();
//		}
//
//		@Override
//		protected String doInBackground(String... params) {
//			// TODO Auto-generated method stub
//
//			// publishing the progress....
//			// After this onProgressUpdate will be called
//			// publishProgress("" + (int) (((i + 1) * 100) / files.size()));
//
//			String szFilePath = "", szFileName = "";
//
//			// for (int i = 0; i < documentList.size(); i++) {
//
//			// nCurrentFile = i + 1;
//			File image1 = null, image2 = null, image3 = null, image4 = null, image5 = null;
//
//			File video = null;
//
//			Iterator it = hmListOfImages.entrySet().iterator();
//			while (it.hasNext()) {
//				Map.Entry pairs = (Map.Entry) it.next();
//				// System.out.println(pairs.getKey() + " = " +
//				// pairs.getValue());
//				if ((Integer) pairs.getKey() == 0)
//					image1 = new File(pairs.getValue().toString());
//
//				if ((Integer) pairs.getKey() == 1)
//					image2 = new File(pairs.getValue().toString());
//
//				if ((Integer) pairs.getKey() == 2)
//					image3 = new File(pairs.getValue().toString());
//
//				if ((Integer) pairs.getKey() == 3)
//					image4 = new File(pairs.getValue().toString());
//
//				if ((Integer) pairs.getKey() == 4)
//					image5 = new File(pairs.getValue().toString());
//
//			}
//
//			if (listVideo.size() > 0)
//				video = new File(listVideo.get(0));
//
//			Log.d("ReportAnIncident upload() - ReportIncidentTask", "File  is :: " + szFilePath);
//
//			// publishing the progress....
//			// After this onProgressUpdate will be called
//			// publishProgress("" + (int) (((i + 1) * 100) /
//			// documentList.size()));
//
//			ReportIncident reportIncident = new ReportIncident(ReportAnIncidentActivity.this);
//			String response;
//			try {
//				do {
//					// response = docUpload.httpPostSendFile("", new File(
//					// szFilePath));
//					// File targetFileForUpload = new File(szFilePath);
//
//					if (image1 != null)
//						image1 = compressFile(image1, image1.getName());
//
//					if (image2 != null)
//						image2 = compressFile(image2, image2.getName());
//
//					if (image3 != null)
//						image3 = compressFile(image3, image3.getName());
//
//					if (image4 != null)
//						image4 = compressFile(image4, image4.getName());
//
//					if (image5 != null)
//						image5 = compressFile(image5, image5.getName());
//
//					// if (video != null)
//					// video = compressFile(video, video.getName());
//
//					response = reportIncident
//							.httpPostReportIncident("", image1, image2, image3, image4, image5, video, szMessage);
//
//					Log.d(TAG + "upload() - ReportIncidentTask()", "Response is : " + response);
//				} while (parseUploadDocumentResponse(response) == false);
//				System.gc();
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return "failed";
//			}
//			// }
//
//			return "success";
//		}
//
//		// /**
//		// * Updating progress bar
//		// * */
//		// protected void onProgressUpdate(String... progress) {
//		// // setting progress percentage
//		// progressBar.setProgress(Integer.parseInt(progress[0]));
//		// progressText.setText(nCurrentFile + " / " + documentList.size());
//		// }
//
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			if (progressUploadDocument != null) {
//				progressUploadDocument.dismiss();
//				// relProgress.setVisibility(View.GONE);
//			}
//
//			if (result != null && result.equals("success")) {
//				Util.showToastMessage(mContext, "Incident has been reported successfully", Toast.LENGTH_LONG);
//				etMessage.setText("");
//				// ivIncident.setImageDrawable(null);
//				hmListOfImages.clear();
//				if (((LinearLayout) llImages).getChildCount() > 0)
//					((LinearLayout) llImages).removeAllViews();
//			} else {
//				Util.showToastMessage(mContext, "Incident reporting failed! Please try again", Toast.LENGTH_LONG);
//			}
//
//		}
//
//	}

	/**
	 * Uploading the file to server
	 * */
	private class UploadFileToServer extends AsyncTask<Void, Integer, String> {

		Context context;
		String szMessage = "";

		@Override
		protected void onPreExecute() {
			// setting progress bar to zero
			super.onPreExecute();
			progressUploadDocument = new ProgressDialog(context);
			progressUploadDocument.setMessage("Uploading...");
			progressUploadDocument.setCancelable(false);
			progressUploadDocument.show();
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {

			// updating progress bar value
			progressUploadDocument.setMessage("Uploading " + progress[0] + "% ...");
		}

		@Override
		protected String doInBackground(Void... params) {
			return uploadFile();
		}

		@SuppressWarnings("deprecation")
		private String uploadFile() {
			String responseString = null;

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(urlServer);

			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new ProgressListener() {

					@Override
					public void transferred(long num) {
						// TODO Auto-generated method stub
						publishProgress((int) ((num / (float) totalSize) * 100));
					}
				});

				File sourceFile;
				Iterator it = hmListOfImages.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry) it.next();
					// System.out.println(pairs.getKey() + " = " +
					// pairs.getValue());
					if ((Integer) pairs.getKey() == 0) {
						// Adding file data to http body
						sourceFile = new File(pairs.getValue().toString());
						Log.d(TAG, "uploadFile :: images1 = "+pairs.getValue().toString());
						entity.addPart("image1", new FileBody(sourceFile));
					}

					if ((Integer) pairs.getKey() == 1) {
						sourceFile = new File(pairs.getValue().toString());
						Log.d(TAG, "uploadFile :: images2 = "+pairs.getValue().toString());
						entity.addPart("image2", new FileBody(sourceFile));
					}

					if ((Integer) pairs.getKey() == 2) {
						sourceFile = new File(pairs.getValue().toString());
						Log.d(TAG, "uploadFile :: images3 = "+pairs.getValue().toString());
						entity.addPart("image3", new FileBody(sourceFile));
					}

					if ((Integer) pairs.getKey() == 3) {
						sourceFile = new File(pairs.getValue().toString());
						Log.d(TAG, "uploadFile :: images4 = "+pairs.getValue().toString());
						entity.addPart("image4", new FileBody(sourceFile));
					}

					if ((Integer) pairs.getKey() == 4) {
						sourceFile = new File(pairs.getValue().toString());
						Log.d(TAG, "uploadFile :: images5 = "+pairs.getValue().toString());
						entity.addPart("image5", new FileBody(sourceFile));
					}

				}

				if (listVideo.size() > 0) {
					sourceFile = new File(listVideo.get(0));
					Log.d(TAG, "uploadFile :: video = "+listVideo.get(0));
					entity.addPart("video", new FileBody(sourceFile));
				}

				entity.addPart("data[text]", new StringBody(szMessage));

				totalSize = entity.getContentLength();
				httppost.setEntity(entity);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: " + statusCode;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String result) {
			Log.e(TAG, "Response from server: " + result);

			// showing the server response in an alert dialog
			if (progressUploadDocument != null) {
				progressUploadDocument.dismiss();
				// relProgress.setVisibility(View.GONE);
			}

			if (result != null && result.contains("success")) {
				Util.showToastMessage(mContext, "Incident has been reported successfully", Toast.LENGTH_LONG);
				etMessage.setText("");
				// ivIncident.setImageDrawable(null);
				hmListOfImages.clear();
				if (((LinearLayout) llImages).getChildCount() > 0)
					((LinearLayout) llImages).removeAllViews();
			} else {
				Util.showToastMessage(mContext, "Incident reporting failed! Please try again", Toast.LENGTH_LONG);
			}

			super.onPostExecute(result);
		}

	}

	File compressFile(File inputFile, String szInputFileName) {

		File outPutFile = null;
		Bitmap bm = null;
		try {

			// if (szInputFileName != null && szInputFileName.endsWith(".png"))
			// szInputFileName = szInputFileName.replace("png", "jpg");

			File cabApp = new File(Environment.getExternalStorageDirectory() + File.separator
					+ getResources().getString(R.string.app_name));
			if (!cabApp.isDirectory()) {
				cabApp.mkdir();
			}

			outPutFile = new File(cabApp, szInputFileName);
			bm = decodeFile(inputFile);
			OutputStream fOut = null;
			fOut = new FileOutputStream(outPutFile);
			bm.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
			fOut.flush();
			fOut.close();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return outPutFile;
	}

	private Bitmap decodeFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 480;

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	boolean parseUploadDocumentResponse(String szResponse) {

		if (szResponse != null) {
			JSONObject jObject;
			try {
				jObject = new JSONObject(szResponse);
				if (jObject.has("success") && jObject.getString("success").equals("true")) {
					return true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return false;
	}

	class ImageDetails {
		View view;
		int mapKey;
		String szFilePath;
	}

}
