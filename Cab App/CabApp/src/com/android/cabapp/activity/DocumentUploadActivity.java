package com.android.cabapp.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.datastruct.json.DriverDetails.Document;
import com.android.cabapp.model.DeleteDocument;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.DocumentUpload;
import com.android.cabapp.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DocumentUploadActivity extends RootActivity {

	private static final String TAG = DocumentUploadActivity.class
			.getSimpleName();

	LinearLayout llPhotoRow;
	TextView txtEdit, textAddImage, tvUpload, tvSkip, textYes, textNo,
			tvLicensingDocument;
	ArrayList<String> documentList;

	File cameraFile;
	View viewAddPhoto;

	public static final int TAKE_PHOTO_REQUEST = 0;
	public static final int PICK_PHOTO_REQUEST = 1;

	Bundle documentBundle;
	boolean isComingFromEditMyAccount;

	private ProgressDialog progressUploadDocument;
	ProgressBar progressBar;
	TextView progressText, tvLicensingDocumentText;
	RelativeLayout relProgress, rlTopPoint;
	boolean isDeleteSuccess = false, bIsFromRegistration = false;
	String szDriverDocument = "", szDriverDocumentsList = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_document_upload);
		super.initWidgets();
		mContext = this;
		documentBundle = getIntent().getExtras();

		documentList = new ArrayList<String>();
		ivBack.setVisibility(View.GONE);
		llPhotoRow = (LinearLayout) findViewById(R.id.llLicencingDocumentRow);
		textAddImage = (TextView) findViewById(R.id.tvAddImage);
		tvUpload = (TextView) findViewById(R.id.tvUpload);
		tvSkip = (TextView) findViewById(R.id.tvSkip);
		tvSkip.setVisibility(View.GONE);
		tvLicensingDocument = (TextView) findViewById(R.id.tvLicensingDocument);
		rlTopPoint = (RelativeLayout) findViewById(R.id.rlTopPoint);
		tvLicensingDocumentText = (TextView) findViewById(R.id.tvLicensingDocumentText);

		relProgress = (RelativeLayout) findViewById(R.id.rel_progress_sync);
		progressBar = (ProgressBar) findViewById(R.id.progress_sync);
		progressText = (TextView) findViewById(R.id.tv_progress);

		tvUpload.setText("Complete registration");

		progressText = (TextView) findViewById(R.id.tv_progress);

		txtEdit = (TextView) findViewById(R.id.tvEdit);
		txtEdit.setVisibility(View.GONE);
		if (((LinearLayout) llPhotoRow).getChildCount() > 0)
			((LinearLayout) llPhotoRow).removeAllViews();

		if (AppValues.driverSettings != null
				&& AppValues.driverSettings.getDriverDocuments() != null)
			szDriverDocument = AppValues.driverSettings.getDriverDocuments();

		if (AppValues.driverSettings != null
				&& AppValues.driverSettings.getDriverDocumentsList() != null)
			szDriverDocumentsList = AppValues.driverSettings
					.getDriverDocumentsList();
		tvLicensingDocumentText.setText(" " + szDriverDocumentsList + " ");

		if (mContext != null) {
			szDriverDocument = Util.getDriverDocument(mContext);
			szDriverDocumentsList = Util.getDriverDocumentsList(mContext);
			if (!szDriverDocumentsList.toString().isEmpty())
				tvLicensingDocumentText.setText(" " + szDriverDocumentsList
						+ " ");
		}

		if (documentBundle != null) {
			if (documentBundle
					.containsKey(Constants.FROM_REGISTRATION_COMPLETE)
					&& documentBundle
							.getBoolean(Constants.FROM_REGISTRATION_COMPLETE)) {
				bIsFromRegistration = documentBundle
						.getBoolean(Constants.FROM_REGISTRATION_COMPLETE);
			}

			if (documentBundle
					.containsKey(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS)) {
				isComingFromEditMyAccount = documentBundle
						.getBoolean(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS);

				if (isComingFromEditMyAccount) {
					tvSkip.setVisibility(View.GONE);
					ivBack.setVisibility(View.VISIBLE);
					rlTopPoint.setVisibility(View.GONE);
					tvUpload.setText("Save");
					tvLicensingDocument.setText("* LICENSING DOCUMENT");

					AlertDialog PendingAlertDialog = new AlertDialog.Builder(
							mContext)
							.setMessage(
									"Adding or deleting documents will put your account in pending state.")
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface argDialog,
												int argWhich) {
										}
									}).create();
					PendingAlertDialog.setCanceledOnTouchOutside(false);
					PendingAlertDialog.show();

					List<Document> documentDetails = AppValues.driverDetails
							.getDocument();

					if (AppValues.driverDetails != null
							&& documentDetails != null
							&& documentDetails.size() > 0) {
						for (int i = 0; i < documentDetails.size(); i++) {

							LayoutInflater inflater = LayoutInflater
									.from(getBaseContext());
							viewAddPhoto = inflater.inflate(
									R.layout.row_attached_files, null);

							TextView tvAttachedFileName = (TextView) viewAddPhoto
									.findViewById(R.id.tvAttachedFileName);
							// TextView tvFileSize = (TextView) viewAddPhoto
							// .findViewById(R.id.tvFileSize);

							ImageView ivCancel = (ImageView) viewAddPhoto
									.findViewById(R.id.ivCancel);
							ImageView ivThumbnail = (ImageView) viewAddPhoto
									.findViewById(R.id.ivThumbnail);

							ImageLoader.getInstance().displayImage(
									documentDetails.get(i).getThumbnail(),
									ivThumbnail);

							if (Constants.isDebug)
								Log.e("DocumentUploadActivity", "Filename:: "
										+ documentDetails.get(i).getFilename());
							tvAttachedFileName.setText(documentDetails.get(i)
									.getFilename());
							// tvFileSize.setText("");// + " kb"

							DocumentDetails details = new DocumentDetails();
							details.szFilePath = "";
							details.view = viewAddPhoto;
							details.szDocumentID = documentDetails.get(i)
									.getId();
							documentList.add(details.szDocumentID);
							ivCancel.setTag(details);

							ivCancel.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(final View v) {
									// TODO Auto-generated method stub
									AlertDialog.Builder builder = new AlertDialog.Builder(
											DocumentUploadActivity.this);
									builder.setMessage(
											"Are you sure you want to delete document?")
											.setCancelable(false)
											.setPositiveButton(
													getString(android.R.string.ok),
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {
															DeleteDocumentTask deleteTask = new DeleteDocumentTask();
															deleteTask.documentView = v;
															deleteTask
																	.execute();
														}
													})
											.setNegativeButton(
													getString(android.R.string.cancel),
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {
															dialog.cancel();
														}
													});
									builder.setTitle("Alert");
									AlertDialog alert = builder.create();
									alert.show();
								}
							});

							llPhotoRow.addView(viewAddPhoto);
						}
					}

				}
			}
		}
		tvSkip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				// Intent tutorialIntent = new Intent(mContext,
				// TutorialActivity.class);
				// startActivity(tutorialIntent);
			}
		});

		textAddImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int maxUpload = 0;
				maxUpload = Integer.valueOf(szDriverDocument);
				if (llPhotoRow.getChildCount() < maxUpload) {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							DocumentUploadActivity.this);
					builder.setItems(
							R.array.add_image_options_upload_documents,
							mDialogListener);
					AlertDialog dialog = builder.create();
					dialog.show();
				} else {
					Util.showToastMessage(DocumentUploadActivity.this,
							"Maximum document limit is " + maxUpload,
							Toast.LENGTH_LONG);
				}
			}
		});

		tvUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// code for upload
				if (isComingFromEditMyAccount) {// tvUpload.getText().equals("Save")

					if (llPhotoRow.getChildCount() <= 0
							&& documentList.size() <= 0) {

						Util.showToastMessage(mContext,
								"Please upload a document first",
								Toast.LENGTH_LONG);

					} else {
						UploadDocument();

						// final Dialog dialog = new Dialog(mContext,
						// R.style.mydialogstyle);
						// dialog.setContentView(R.layout.confirmation_dialog);
						// dialog.setCanceledOnTouchOutside(false);
						// // dialog.setTitle("Alert!!");
						// dialog.setCancelable(false);
						// dialog.show();
						//
						// TextView tvAlertText = (TextView) dialog
						// .findViewById(R.id.tvAlertText);
						// tvAlertText.setText("");
						// textYes = (TextView) dialog.findViewById(R.id.tvYes);
						// textNo = (TextView) dialog.findViewById(R.id.tvNo);
						// textNo.setOnClickListener(new OnClickListener() {
						//
						// @Override
						// public void onClick(View v) {
						// // TODO Auto-generated method stub
						// dialog.dismiss();
						// }
						// });
						// textYes.setOnClickListener(new OnClickListener() {
						//
						// @Override
						// public void onClick(View v) {
						// // TODO Auto-generated method stub
						// dialog.dismiss();
						// UploadDocument();
						// }
						// });
					}
				} else {
					UploadDocument();
				}
			}
		});

	}

	void UploadDocument() {

		if (llPhotoRow.getChildCount() <= 0 && documentList.size() <= 0) {

			Util.showToastMessage(mContext, "Please upload a document first",
					Toast.LENGTH_LONG);

		} else if (documentList != null && documentList.size() > 0
				&& (documentList.get(0).contains("/"))) {

			relProgress.setVisibility(View.VISIBLE);
			UploadAllFilesTask uploadFiles = new UploadAllFilesTask();
			uploadFiles.context = mContext;
			// uploadFiles.szFileName = "iqra";
			// uploadFiles.szFilePath =
			// Environment.getExternalStorageDirectory()
			// + "/apk/iqra.jpg";// "/sdcard/apk/pic.jpg";
			uploadFiles.execute();

		} else {
			if (isDeleteSuccess && llPhotoRow.getChildCount() >= 1) {
				Util.setIsDocumentsUploaded(mContext, true);
				finish();
				Intent tutorialIntent = new Intent(mContext,
						TutorialActivity.class);
				if (bIsFromRegistration)
					tutorialIntent.putExtra(
							Constants.FROM_REGISTRATION_COMPLETE, true);
				startActivity(tutorialIntent);
			} else {
				Util.showToastMessage(mContext,
						"Please upload a document first", Toast.LENGTH_LONG);
			}

			// Util.showToastMessage(mContext, "No new document to upload",
			// Toast.LENGTH_LONG);
		}
	}

	protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// isImagechanged = true;
			switch (which) {

			case 0: // Take picture
				Intent takePhotoIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(getImageFileName()));
				// Log.e(TAG, "image path :: " +
				// Uri.fromFile(getImageFileName()));
				startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);

				break;

			case 1: // Choose picture
				if (Integer.decode(Build.VERSION.SDK) >= 19) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, PICK_PHOTO_REQUEST);
				} else {
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Select Picture"),
							PICK_PHOTO_REQUEST);
				}

				break;

			case 2: // Cancel
				dialog.cancel();
				break;
			}

		}
	};

	public String dateToString(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	private File getImageFileName() {
		String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
		return cameraFile = new File(Environment.getExternalStorageDirectory()
				+ File.separator + getResources().getString(R.string.app_name),
				name + ".jpg");
	}

	public static String extractFileNameFromString(String fullFileName) {
		return fullFileName.substring(fullFileName.lastIndexOf("/") + 1,
				fullFileName.length());
	}

	public String getPath(Uri uri) {
		if (uri == null) {
			return null;
		}
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// returns the image path
		return uri.getPath();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		String imagePath = "";
		String fileName = "";
		long file_size = 0;
		if (resultCode == RESULT_OK) {
			if (requestCode == TAKE_PHOTO_REQUEST) {
				// camera picture
				imagePath = cameraFile.getPath();
				file_size = Integer
						.parseInt(String.valueOf(cameraFile.length() / 1024));
				if (Constants.isDebug)
					Log.i(TAG, "Camera Path::>  " + imagePath);

			} else if (requestCode == PICK_PHOTO_REQUEST) {
				// gallery
				Uri selectedImageUri = intent.getData();
				imagePath = getPath(selectedImageUri);
				if (Constants.isDebug)
					Log.i(TAG, "Gallery Path::>  " + imagePath);

				// File galleryFile = new File(imagePath);
				// file_size = Integer.parseInt(String.valueOf(galleryFile
				// .length() / 1024));

				if (imagePath != null
						&& (imagePath.endsWith("mp4")
								|| imagePath.endsWith("wmv") || imagePath
									.endsWith("avi"))) {
					Util.showToastMessage(DocumentUploadActivity.this,
							"Video file not supported!", Toast.LENGTH_LONG);
					return;
				}

			}

			if (documentList == null)
				documentList = new ArrayList<String>();

			if (documentList != null && documentList.size() > 0
					&& documentList.get(0) != null
					&& !documentList.get(0).contains("/"))
				documentList.clear();

			fileName = extractFileNameFromString(imagePath);

			// Adding new row:
			LayoutInflater inflater = LayoutInflater.from(getBaseContext());
			viewAddPhoto = inflater.inflate(R.layout.row_attached_files, null);

			TextView tvAttachedFileName = (TextView) viewAddPhoto
					.findViewById(R.id.tvAttachedFileName);
			// TextView tvFileSize = (TextView) viewAddPhoto
			// .findViewById(R.id.tvFileSize);

			ImageView ivCancel = (ImageView) viewAddPhoto
					.findViewById(R.id.ivCancel);
			ImageView ivThumbnail = (ImageView) viewAddPhoto
					.findViewById(R.id.ivThumbnail);

			ImageLoader.getInstance().displayImage("file://" + imagePath,
					ivThumbnail);

			DocumentDetails details = new DocumentDetails();
			details.szFilePath = imagePath;
			details.view = viewAddPhoto;

			ivCancel.setTag(details);

			ivCancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					DocumentDetails details = (DocumentDetails) v.getTag();
					llPhotoRow.removeView(details.view);
					documentList.remove(details.szFilePath);
				}
			});

			tvAttachedFileName.setText(fileName);
			// if (file_size > 0) {
			// tvFileSize.setText(String.valueOf(file_size) + " kb");
			// } else {
			// tvFileSize.setText("0 kb");
			// }

			llPhotoRow.addView(viewAddPhoto);
			documentList.add(imagePath);

		} else if (resultCode != RESULT_CANCELED) {
			Util.showToastMessage(mContext, "Sorry, there was an error",
					Toast.LENGTH_LONG);
		}

	}

	public class DeleteDocumentTask extends AsyncTask<String, Void, String> {

		String szDocID;
		View documentView;
		DocumentDetails docDetails;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressUploadDocument = new ProgressDialog(mContext);
			progressUploadDocument.setMessage("Deleting document...");
			progressUploadDocument.setCancelable(false);
			progressUploadDocument.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			docDetails = (DocumentDetails) documentView.getTag();
			DeleteDocument deleteDoc = new DeleteDocument(
					docDetails.szDocumentID, mContext);
			String response = deleteDoc.getResponse();
			try {
				JSONObject jObject = new JSONObject(response);

				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {
					DriverAccountDetails driverAccount = new DriverAccountDetails(
							mContext);
					driverAccount.retriveAccountDetails(mContext);
					return "success";
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						String errorMessage = jErrorsArray.getJSONObject(0)
								.getString("message");
						return errorMessage;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progressUploadDocument != null)
				progressUploadDocument.dismiss();

			// On delete of any document
			ivBack.setVisibility(View.GONE);
			isDeleteSuccess = true;

			if (result != null && result.equals("success")) {
				llPhotoRow.removeView(docDetails.view);
				documentList.remove(docDetails.szDocumentID);
			} else
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);

		}
	}

	class UploadAllFilesTask extends AsyncTask<String, String, String> {
		Context context;
		int nCurrentFile = 0;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			textAddImage.setEnabled(false);
			tvUpload.setEnabled(false);
			tvSkip.setEnabled(false);
			// progressUploadDocument = new ProgressDialog(context);
			// progressUploadDocument.setMessage("Uploading documents...");
			// progressUploadDocument.setCancelable(false);
			// progressUploadDocument.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			// publishing the progress....
			// After this onProgressUpdate will be called
			// publishProgress("" + (int) (((i + 1) * 100) / files.size()));

			String szFilePath = "", szFileName = "";

			for (int i = 0; i < documentList.size(); i++) {

				nCurrentFile = i + 1;
				szFilePath = documentList.get(i);
				Log.d("Document upload() - UploadAllFilesTask", "File  is :: "
						+ szFilePath);

				// publishing the progress....
				// After this onProgressUpdate will be called
				publishProgress(""
						+ (int) (((i + 1) * 100) / documentList.size()));

				DocumentUpload docUpload = new DocumentUpload(
						DocumentUploadActivity.this);
				String response;
				try {
					do {
						// response = docUpload.httpPostSendFile("", new File(
						// szFilePath));
						File targetFileForUpload = new File(szFilePath);
						response = docUpload.httpPostSendFile(
								"",
								compressFile(targetFileForUpload,
										targetFileForUpload.getName()));

						Log.d(TAG + "upload() - uploadAllDocsTask()",
								"Docs Response is : " + response);
					} while (parseUploadDocumentResponse(response) == false);
					System.gc();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return "success";
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			progressBar.setProgress(Integer.parseInt(progress[0]));
			progressText.setText(nCurrentFile + " / " + documentList.size());
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// if (progressUploadDocument != null) {
			// progressUploadDocument.dismiss();
			// }

			if (relProgress != null) {
				relProgress.setVisibility(View.GONE);
				textAddImage.setEnabled(true);
				tvUpload.setEnabled(true);
				tvSkip.setEnabled(true);
			}

			if (result != null && result.equals("success")) {
				Util.setIsDocumentsUploaded(mContext, true);
				finish();
				Intent tutorialIntent = new Intent(mContext,
						TutorialActivity.class);
				if (bIsFromRegistration)
					tutorialIntent.putExtra(
							Constants.FROM_REGISTRATION_COMPLETE, true);
				/*
				 * As everytime any change in document will redirect to tutorial
				 * screen
				 */
				startActivity(tutorialIntent);
			} else {
				Util.setIsDocumentsUploaded(mContext, false);
			}

		}

	}

	boolean parseUploadDocumentResponse(String szResponse) {

		if (szResponse != null) {
			JSONObject jObject;
			try {
				jObject = new JSONObject(szResponse);
				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {
					return true;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return false;
	}

	class DocumentDetails {
		View view;
		String szFilePath;
		String szDocumentID;
	}

	static void finishActivity() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	File compressFile(File inputFile, String szInputFileName) {

		File outPutFile = null;
		Bitmap bm = null;
		try {

			// if (szInputFileName != null && szInputFileName.endsWith(".png"))
			// szInputFileName = szInputFileName.replace("png", "jpg");

			File cabApp = new File(Environment.getExternalStorageDirectory()
					+ File.separator
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
