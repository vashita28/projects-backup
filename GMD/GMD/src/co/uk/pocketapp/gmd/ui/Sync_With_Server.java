package co.uk.pocketapp.gmd.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.receiver.NetworkBroadcastReceiver;
import co.uk.pocketapp.gmd.tasks.Download_XML_Task;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.PhotoUpload;
import co.uk.pocketapp.gmd.util.UploadXML;
import co.uk.pocketapp.gmd.util.Util;

public class Sync_With_Server extends Activity {
	private final String TAG = "SYNC WITH SERVER";
	ProgressBar progressBar;
	TextView textview_loadingstatus, progressText;
	Button btn_sync_with_server, btn_logout;

	public static Handler mHandler;
	TextView textview_download_status;

	public static int ERROR_SYNCING_REPORT = 100;
	// public static int ERROR_NO_TASKS = 999;
	public static int SUCCESS = 200;
	public static int ALL_TASKS_COMPLETED = 777;
	public static int REPORT_UPLOADED = 1234;
	public static int DOWNLOAD_NOT_GOING_ON = 56789;

	public boolean bIsAllTasksCompleted = false;

	boolean bIsDownloadOrUploadOnGoing = false;

	int nPhotoTransferAttemptCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sync_with_server);

		Log.d("Sync With Server", "Sync with Server - onCreate()");

		progressBar = (ProgressBar) findViewById(R.id.progress_sync);
		progressText = (TextView) findViewById(R.id.tv_progress);
		textview_loadingstatus = (TextView) findViewById(R.id.textview_loadingstatus);

		btn_sync_with_server = (Button) findViewById(R.id.btn_sync_with_server);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		textview_download_status = (TextView) findViewById(R.id.textview_download_status);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				super.handleMessage(message);
				if (message.what == ERROR_SYNCING_REPORT) {
					if (btn_sync_with_server != null) {
						btn_sync_with_server.setVisibility(View.VISIBLE);
						btn_logout.setVisibility(View.VISIBLE);
					}
					if (textview_download_status != null)
						textview_download_status.setVisibility(View.GONE);
				} else if (message.what == SUCCESS) {
					if (textview_download_status != null) {
						textview_download_status.setText(getResources()
								.getString(R.string.SYNC_SUCCESSFULLY));
						textview_download_status.setVisibility(View.VISIBLE);
						if (Service_CheckList_Without_Swipe.mHandler != null)
							Service_CheckList_Without_Swipe.mHandler
									.sendEmptyMessage(0);
					}
					if (btn_sync_with_server != null) {
						btn_sync_with_server.setVisibility(View.GONE);
						btn_logout.setVisibility(View.GONE);
					}
				} else if (message.what == DOWNLOAD_NOT_GOING_ON) {
					bIsDownloadOrUploadOnGoing = false;
				} else if (message.what == ALL_TASKS_COMPLETED) {
					bIsAllTasksCompleted = true;
					if (textview_download_status != null)
						textview_download_status.setVisibility(View.GONE);
					if (btn_sync_with_server != null) {
						btn_sync_with_server.setVisibility(View.GONE);
						btn_logout.setVisibility(View.GONE);
					}
				} else if (message.what == REPORT_UPLOADED) {
					bIsAllTasksCompleted = false;
					if (textview_download_status != null) {
						textview_download_status.setVisibility(View.VISIBLE);
						textview_download_status
								.setText("Report was successfully uploaded. Please sync with server for new reports");
						Photographs.bIsCameraOrGalleryOn = true;
					}
					if (btn_sync_with_server != null)
						btn_sync_with_server.setVisibility(View.GONE);
					btn_logout.setVisibility(View.GONE);
				}

				else {
					Bundle values = (Bundle) message.obj;
					if (btn_sync_with_server != null) {
						btn_sync_with_server.setVisibility(View.VISIBLE);
						btn_logout.setVisibility(View.VISIBLE);
					}
					if (textview_download_status != null) {
						textview_download_status.setText(values
								.getString("error_message"));
						textview_download_status.setVisibility(View.VISIBLE);
					}
				}

				// int status = Util.getReportStatus(Sync_With_Server.this);
				// Log.v(TAG, "STATUS------------>   " + status);
				// if (status != 0) {
				// textview_download_status.setVisibility(View.VISIBLE);
				// super.handleMessage(message);
				// }else{
				// textview_download_status.setText("No tasks available!!");
				// textview_download_status.setVisibility(View.VISIBLE);
				// }

			}
		};

		// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable
		// && !Util.getIsReportXMLDownloaded(AppValues.AppContext,
		// Util.getReportID(AppValues.AppContext))) {
		// btn_sync_with_server.setVisibility(View.GONE);
		// download();
		// } else {
		// Toast.makeText(Sync_With_Server.this, "No network connection",
		// Toast.LENGTH_LONG).show();
		// progressBar.setVisibility(View.GONE);
		// btn_sync_with_server.setVisibility(View.VISIBLE);
		// }

		btn_sync_with_server.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!bIsAllTasksCompleted
						&& !Util.getIsReportXMLDownloaded(Sync_With_Server.this))
					download();

				if (bIsAllTasksCompleted
						&& !Util.getIsReportUploaded(Sync_With_Server.this)) {
					upload();
				}
			}
		});

		btn_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getBaseContext(),
						Activity_login.class);
				startActivity(myIntent);
				MainActivity.logout();
				finish();

			}
		});

		// DOWNLOAD PDF FROM SERVER:
		// Log.v(TAG, "Downloading pdf file");
		// Download_PDF_Task downloadPDFTask = new Download_PDF_Task();
		// downloadPDFTask.execute();
		// Log.v(TAG, "Downloaded task from server done");

		// //Encryption function:
		// Log.v(TAG, "GOINT TO  ENCYPTION");
		// EncodeDecodeAES.encrypt();
		// Log.v(TAG, "Coming BACK FROM ENCYPTION");

		// Image_Uploader image_Uploader = new Image_Uploader();
		// image_Uploader.ImageUpload_Function();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Sync With Server", "Sync with Server - onResume()");

		if (!Util.getReportID(Sync_With_Server.this).equals("")) {
			// Condition to check for all completed tasks
			Cursor cursor = getContentResolver().query(
					DataProvider.Tasks.CONTENT_URI, null, null, null, null);
			cursor.moveToFirst();

			Cursor taskCursor = getContentResolver().query(
					DataProvider.Tasks.CONTENT_URI,
					null,
					DataProvider.Tasks.REPORT_ID + " = ?" + " AND "
							+ DataProvider.Tasks.TASK_COMPLETED + " = ?",
					new String[] { Util.getReportID(Sync_With_Server.this),
							"false" }, null);
			taskCursor.moveToFirst();
			if (cursor.getCount() > 0 && taskCursor.getCount() == 0) {
				bIsAllTasksCompleted = true;
				Sync_With_Server.mHandler.sendEmptyMessage(ALL_TASKS_COMPLETED);
			}
			taskCursor.close();
			cursor.close();
		}

		if (!bIsAllTasksCompleted
				&& !Util.getIsReportXMLDownloaded(Sync_With_Server.this)
				&& !bIsDownloadOrUploadOnGoing)
			download();

		if (bIsAllTasksCompleted
				&& !Util.getIsReportUploaded(Sync_With_Server.this)
				&& !bIsDownloadOrUploadOnGoing) {
			upload();
		}

	}

	private List<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<File>();
		File[] files = parentDir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				inFiles.addAll(getListFiles(file));
			} else {
				inFiles.add(file);
			}
		}
		return inFiles;
	}

	private void parseUploadReportResponse(String response) {
		// TODO Auto-generated method stub
		try {

			JSONObject jsonObj = new JSONObject(response);

			JSONObject jsonHeadObj = jsonObj.getJSONObject("head");
			String szStatus = jsonHeadObj.getString("status");

			if (szStatus != null && szStatus.equalsIgnoreCase("1")) {

				JSONObject bodyObj = jsonObj.getJSONObject("body");
				String szReportStatus = bodyObj.getString("report_status");
				Log.d("parseUploadReportResponse : ", "Report status :: "
						+ szReportStatus);
				Util.setIsReportUploaded(Sync_With_Server.this, true);
				mHandler.sendEmptyMessage(REPORT_UPLOADED);
				setReportUploaded();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Util.setIsReportUploaded(Sync_With_Server.this, false);
		}
	}

	private boolean parseUploadPhotoResponse(String response) {
		// TODO Auto-generated method stub
		try {

			JSONObject jsonObj = new JSONObject(response);

			JSONObject jsonHeadObj = jsonObj.getJSONObject("head");
			String szStatus = jsonHeadObj.getString("status");

			if (szStatus != null && szStatus.equalsIgnoreCase("1")) {

				JSONObject bodyObj = jsonObj.getJSONObject("body");
				String szImagePath = bodyObj.getString("image_path");
				Log.d("parseUploadPhotoResponse : ", "Image Path is :: "
						+ szImagePath);
				nPhotoTransferAttemptCount = 0;
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		nPhotoTransferAttemptCount++;
		Log.d("parseUploadPhotoResponse : ", "Photo Transfer attempt count :: "
				+ nPhotoTransferAttemptCount);
		return false;
	}

	void download() {
		// to download the report
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			btn_sync_with_server.setVisibility(View.GONE);
			btn_logout.setVisibility(View.GONE);
			textview_download_status.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			progressText.setVisibility(View.GONE);
			textview_loadingstatus.setVisibility(View.VISIBLE);
			textview_loadingstatus.setText("Downloading report...");

			bIsDownloadOrUploadOnGoing = true;
			Download_XML_Task downloadXMLTask = new Download_XML_Task();
			downloadXMLTask.mContext = Sync_With_Server.this;
			downloadXMLTask.progressBar = progressBar;
			downloadXMLTask.txtview_loadingmessage = textview_loadingstatus;
			downloadXMLTask.execute();
		} else {
			Toast.makeText(Sync_With_Server.this, "No network connection",
					Toast.LENGTH_LONG).show();
			bIsDownloadOrUploadOnGoing = false;
			progressBar.setVisibility(View.GONE);
			progressText.setVisibility(View.GONE);
			textview_loadingstatus.setVisibility(View.GONE);
			btn_sync_with_server.setVisibility(View.VISIBLE);
			btn_logout.setVisibility(View.VISIBLE);
		}
	}

	void upload() {
		// to upload the completed report
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			textview_download_status.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
			progressText.setVisibility(View.VISIBLE);
			textview_loadingstatus.setVisibility(View.VISIBLE);
			textview_loadingstatus.setText("Uploading all photos...");
			btn_sync_with_server.setVisibility(View.GONE);
			btn_logout.setVisibility(View.GONE);
			bIsDownloadOrUploadOnGoing = true;
			new UploadAllPhotosTask().execute();
		} else {
			Toast.makeText(Sync_With_Server.this, "No network connection",
					Toast.LENGTH_LONG).show();
			bIsDownloadOrUploadOnGoing = false;
			progressBar.setVisibility(View.GONE);
			progressText.setVisibility(View.GONE);
			textview_loadingstatus.setVisibility(View.GONE);
			btn_sync_with_server.setVisibility(View.VISIBLE);
			btn_logout.setVisibility(View.VISIBLE);
		}

	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

		AlertDialog quitAlertDialog = new AlertDialog.Builder(
				Sync_With_Server.this)
				.setTitle("Exit")
				.setMessage("Confirm exit?")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface argDialog, int argWhich) {
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

	// Report ID parameter required
	// ===============================
	// status = 0
	// error_code = PARAM_REQUIRED
	// error_msg = report_id - parameter required
	//
	// XML parameter required
	// ===============================
	// status = 0
	// error_code = PARAM_REQUIRED
	// error_msg = xml - parameter required
	//
	// XML Upload Failed
	// ===============================
	//
	// status = 0
	// error_code = XML_FAILED
	// error_msg = Please try again. XML cannot be uploaded.
	// [4:05:46 PM] Raju Vishwas: ==================================>
	// [4:05:49 PM] Raju Vishwas: ===================>
	// [4:05:52 PM] Raju Vishwas: No Access Token
	// =============================================
	// {"head":{"status":"0","error_code":"NO_ACCESS_TOKEN","error_msg":"Access token not provided."},"body":[]}
	//
	// No Action Specified, action=page
	// =============================================
	// {"head":{"status":"0","error_code":"INVALID_URL","error_msg":"Invalid URL"},"body":[]}
	//
	// Report Id not provided
	// =============================================
	// {"head":{"status":"0","error_code":"PARAM_REQUIRED","error_msg":"report_id - parameter required"},"body":[]}
	//
	// XML not provided
	// =============================================
	// {"head":{"status":"0","error_code":"PARAM_REQUIRED","error_msg":"xml - parameter required."},"body":[]}
	//
	// File Upload Issue
	// =============================================
	// {"head":{"status":"0","error_code":"XML_FAILED","error_msg":"Please try again. XML cannot be uploaded."},"body":[]}

	// {"head":{"status":"1"},"body":{"report_status":"Waiting"}}

	void setReportUploaded() {

		Util.setReportKilled(Sync_With_Server.this);

		if (MainActivity.mHandler != null)
			MainActivity.mHandler.sendEmptyMessage(0);
		if (Report_Details.mHandler != null)
			Report_Details.mHandler.sendEmptyMessage(REPORT_UPLOADED);

		if (AppValues.getReportXMLFile().exists())
			AppValues.getReportXMLFile().delete();
		if (AppValues.getServiceCheckListtXMLFile().exists())
			AppValues.getServiceCheckListtXMLFile().delete();

		getContentResolver().delete(DataProvider.Mill.CONTENT_URI, null, null);
		getContentResolver().delete(DataProvider.Services.CONTENT_URI, null,
				null);
		getContentResolver().delete(DataProvider.Child_Leaf.CONTENT_URI, null,
				null);
		getContentResolver().delete(DataProvider.Tasks.CONTENT_URI, null, null);
		getContentResolver().delete(DataProvider.Material_Log.CONTENT_URI,
				null, null);
	}

	class UploadAllPhotosTask extends AsyncTask<String, String, String> {

		String totalSize = "";
		String currentFile = "";

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.d("***************", "UPLOAD ALL PHOTOS");
			File file = AppValues.getPhotoGraphDirectory();
			List<File> files = getListFiles(file);

			for (int i = 0; i < files.size(); i++) {
				String szFilePath = files.get(i).getPath();
				String szFileName = files.get(i).getName();

				totalSize = String.valueOf(files.size());
				currentFile = String.valueOf(i + 1);
				Log.d("SyncWithServer-upload() - uploadAllPhotosTask()",
						"File  is :: " + szFileName);

				// publishing the progress....
				// After this onProgressUpdate will be called
				publishProgress("" + (int) (((i + 1) * 100) / files.size()));

				PhotoUpload photoupload = new PhotoUpload(Sync_With_Server.this);
				String response;
				try {
					do {
						response = photoupload.httpPostSendFile("",
								CompressFile(files.get(i), szFileName));
						Log.d("SyncWithServer-upload() - uploadAllPhotosTask()",
								"Photo Response is : " + response);
					} while (parseUploadPhotoResponse(response) == false);
					System.gc();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return null;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			progressBar.setProgress(Integer.parseInt(progress[0]));
			progressText.setText(currentFile + " / " + totalSize);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			textview_loadingstatus.setVisibility(View.VISIBLE);
			textview_loadingstatus.setText("Uploading report...");
			new UploadAllReportsTask().execute();

		}

	}

	class UploadAllReportsTask extends AsyncTask<String, Integer, String> {
		String szReportResponse = "";
		long totalSize;
		String currentFile = "";

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			Log.d("***************", "UPLOAD ALL XML");
			totalSize = 2;

			UploadXML uploadXML = new UploadXML(Sync_With_Server.this);
			try {

				if (AppValues.bIsEncryptionRequired)
					Util.DecryptReportXML();

				szReportResponse = uploadXML.httpPostSendFile("",
						AppValues.getReportXMLFile(), "report_details");

				Log.d("Response is ", "Report Details response :"
						+ szReportResponse);

				currentFile = "1";
				publishProgress((int) ((1 / (float) totalSize) * 100));

				if (AppValues.bIsEncryptionRequired)
					Util.DecryptServiceCheckListXML();

				szReportResponse = uploadXML.httpPostSendFile("",
						AppValues.getServiceCheckListtXMLFile(),
						"service_checklists");

				currentFile = "2";
				publishProgress((int) ((2 / (float) totalSize) * 100));

				Log.d("Response is ", "Service Checklists response :"
						+ szReportResponse);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (AppValues.bIsEncryptionRequired) {
					Util.EncryptReportXML();
					Util.EncryptServiceCheckListXML();
				}
			}

			return null;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			progressBar.setProgress(Integer.parseInt(progress[0]));
			progressText.setText(currentFile + " / " + totalSize);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (progressBar != null) {
				progressBar.setVisibility(View.GONE);
				progressText.setVisibility(View.GONE);
			}
			if (textview_loadingstatus != null)
				textview_loadingstatus.setVisibility(View.GONE);

			Log.d("******************",
					"response post execute Report :: ***** " + szReportResponse);
			parseUploadReportResponse(szReportResponse);
			bIsDownloadOrUploadOnGoing = false;
		}
	}

	File CompressFile(File inputFile, String szInputFileName) {

		File outPutFile = null;
		Bitmap bm = null;
		try {

			if (szInputFileName != null && szInputFileName.endsWith(".png"))
				szInputFileName = szInputFileName.replace("png", "jpg");

			outPutFile = new File(AppValues.getPhotosCompressedTempDirectory(),
					szInputFileName);
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
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
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

}
