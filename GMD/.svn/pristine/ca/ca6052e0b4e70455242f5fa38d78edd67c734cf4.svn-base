package co.uk.pocketapp.gmd.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.adapter.ImageAdapter;
import co.uk.pocketapp.gmd.db.DataProvider;
import co.uk.pocketapp.gmd.ui.LeafPhotograph.saveOrProcessPhotoTask;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.PhotoUpload;
import co.uk.pocketapp.gmd.util.UploadXML;
import co.uk.pocketapp.gmd.util.Util;

public class Photographs extends ParentActivity implements OnItemClickListener {
	Button camera_Button;
	Button gallery_Button;

	private final int REQ_CODE_PICK_IMAGE = 1337;

	String m_SelectedImagePath = "", m_szFileName = "";

	GridView grid_photographs;
	// File file;
	File[] getAllFiles;
	int noOfImages, i = 0, x = 0;
	float div;
	boolean dirCreated = true;

	ListAdapter photoAdapter;
	View view_photographs_top_bar;
	TextView textview_title;
	TextView textview_existingphotos;

	Date date;
	String m_szDateTime = "";
	TextView textview_dateAndTime, textview_GMD_Heading;
	ProgressBar progressPhotographs;

	private int index = -1;
	private int top = 0;

	public static boolean bIsCameraOrGalleryOn = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photographs);

		view_photographs_top_bar = (View) findViewById(R.id.view_photograhs_header);
		textview_title = (TextView) view_photographs_top_bar
				.findViewById(R.id.pageTitle);
		textview_title.setText("Photographs");

		textview_existingphotos = (TextView) findViewById(R.id.textview_existingphotos);
		textview_existingphotos.setTypeface(GMDApplication.fontHeading);

		camera_Button = (Button) findViewById(R.id.cameraBtn);
		gallery_Button = (Button) findViewById(R.id.galleryBtn);

		// camera button:
		camera_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent photographs_cameraIntent = new Intent(Photographs.this,
						Photographs_Camera_Activity.class);
				startActivity(photographs_cameraIntent);
				bIsCameraOrGalleryOn = true;
			}
		});

		// gallery button:
		gallery_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent, REQ_CODE_PICK_IMAGE);
				bIsCameraOrGalleryOn = true;
			}
		});

		// file = new File(Environment.getExternalStorageDirectory()
		// + File.separator + AppValues.APP_NAME + "/Camera/");
		// file = new File(AppValues.getDirectory() + File.separator
		// + AppValues.APP_NAME + "/Camera/");

		grid_photographs = (GridView) findViewById(R.id.grid_photographs);
		grid_photographs.setOnItemClickListener(this);

		grid_photographs.setOnScrollListener(new PauseOnScrollListener(
				ImageLoader.getInstance(), true, true));

		progressPhotographs = (ProgressBar) findViewById(R.id.progress_photographs);
		progressPhotographs.setVisibility(View.VISIBLE);

		new LoadPhotosTask().execute();

	}

	// getting gallery selected picture details:
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_PICK_IMAGE:
			switch (resultCode) {
			case Activity.RESULT_OK:
				date = new Date();
				m_szDateTime = dateFormat.format(date);
				bIsCameraOrGalleryOn = true;
				System.gc();

				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					ProgressDialog dialog = new ProgressDialog(Photographs.this);
					dialog.setCanceledOnTouchOutside(false);
					dialog.setCancelable(false);
					dialog.setMessage("Loading...");
					dialog.show();
					new saveOrProcessPhotoTask(cursor, dialog).execute();

				} else {
					Toast.makeText(this, "Unable to retrieve selected image",
							Toast.LENGTH_LONG).show();
					return;
				}
				break;
			default:
				Log.d("Photographs-onActivityResult()", "Cancelled");
				break;
			}
			break;
		}
	}

	class saveOrProcessPhotoTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;
		Cursor cursor;
		String filename = "";
		String filePath = "";

		public saveOrProcessPhotoTask(Cursor cursor, ProgressDialog dialog) {
			// TODO Auto-generated constructor stub
			this.cursor = cursor;
			this.dialog = dialog;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			int columnIndex = cursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			filePath = cursor.getString(columnIndex);
			int pos = filePath.lastIndexOf("/");
			filename = filePath.substring(pos + 1).trim();
			Log.d("Photographs-onActivityResult()", " Filename is:: "
					+ filename);
			cursor.deactivate();
			cursor.close();
			copyfile(filePath);
			Intent intent = new Intent(Photographs.this,
					Photo_Details_Entry.class);
			intent.putExtra("capturedimageuri", m_SelectedImagePath);
			intent.putExtra("filename", m_szFileName);
			intent.putExtra("datetime", m_szDateTime.toLowerCase());
			startActivity(intent);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialog != null)
				dialog.dismiss();
		}

	}

	private void copyfile(String srFile) {
		try {

			// File directory = new
			// File(Environment.getExternalStorageDirectory()
			// + File.separator + AppValues.APP_NAME + "/Gallery/");
			// File directory = new File(AppValues.getDirectory() +
			// File.separator
			// + AppValues.APP_NAME + "/Gallery/");
			// directory.mkdirs();
			// m_szFileName = System.currentTimeMillis() + "_"
			// + AppValues.APP_NAME + "_"
			// + Util.getReportID(Photographs.this) + ".png";

			Bitmap bm = null;
			OutputStream fOut = null;
			// File f2 = new File(directory, m_szFileName);
			fOut = new FileOutputStream(AppValues.getPhotoGraphFile());
			try {
				ExifInterface ei = new ExifInterface(srFile);
				int orientation = ei.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);

				if (orientation == ExifInterface.ORIENTATION_ROTATE_90
						|| orientation == ExifInterface.ORIENTATION_ROTATE_180) {

					bm = BitmapFactory.decodeFile(srFile);

					Matrix matrix = new Matrix();
					switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						matrix.postRotate(90);
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						matrix.postRotate(180);
						break;
					}
					bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
							bm.getHeight(), matrix, true);
					bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
				} else {
					File f1 = new File(srFile);
					InputStream in = new FileInputStream(f1);
					// For Append the file.
					// OutputStream out = new FileOutputStream(f2,true);
					// For Overwrite the file.
					// OutputStream out = new FileOutputStream(f2);

					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						fOut.write(buf, 0, len);
					}
					in.close();
					Log.d("Photographs", "Photo File copied...");
				}

				m_SelectedImagePath = AppValues.getPhotoGraphFile()
						.getAbsolutePath();
				m_szFileName = AppValues.getPhotoGraphFile().getName();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				fOut.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		// TODO Auto-generated method stub
		TextView txtFileName = (TextView) view
				.findViewById(R.id.textview_filename);
		String szFilePath = txtFileName.getText().toString();
		String szFileName = szFilePath
				.substring(szFilePath.lastIndexOf("/") + 1);

		Intent photoDetailsIntent = new Intent(Photographs.this,
				Photo_Page.class);
		photoDetailsIntent.putExtra("filename", szFileName);
		photoDetailsIntent.putExtra("capturedimageuri", szFilePath);
		startActivity(photoDetailsIntent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		super.initWidgets();

		if (index != -1 && bIsCameraOrGalleryOn == false) {
			grid_photographs.setSelection(index);
		}

		// font implementation of main heading "GMD": textview
		textview_GMD_Heading = (TextView) findViewById(R.id.Heading);
		textview_GMD_Heading.setTypeface(GMDApplication.fontHeading);

		if (bIsCameraOrGalleryOn) {
			progressPhotographs.setVisibility(View.VISIBLE);
			index = 0;
			new LoadPhotosTask().execute();
		}

		// new createFolderStructureTask().execute();

		// new testXMLParsingTask().execute();

		// new uploadAllPhotosTask().execute();
	}

	class testXMLParsingTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			// Util.setServiceCheckListCreated(Photographs.this, false);
			//
			// // new XML_Creation().add_Service_Checklist();
			// CreateServiceCheckListXMLTask task = new
			// CreateServiceCheckListXMLTask();
			// task.mContext = Photographs.this;
			// task.execute();

			UploadXML uploadXML = new UploadXML(Photographs.this);

			try {
				String response = uploadXML.httpPostSendFile("",
						AppValues.getReportXMLFile(), "report_details");

				Log.d("Response is ", "UPLOAD Report Details response :"
						+ response);

				response = uploadXML.httpPostSendFile("",
						AppValues.getServiceCheckListtXMLFile(),
						"service_checklists");

				Log.d("Response is ", "UPLOAD Service Checklists response :"
						+ response);

				// JSONObject jsonObj = new JSONObject(response);
				//
				// JSONObject jsonHeadObj = jsonObj.getJSONObject("head");
				// String szStatus = jsonHeadObj.getString("status");
				//
				// if (szStatus != null && szStatus.equalsIgnoreCase("1")) {
				//
				// JSONObject bodyObj = jsonObj.getJSONObject("body");
				// String szReportStatus = bodyObj.getString("report_status");
				// Log.d("parseUploadReportResponse : ", "Report status :: "
				// + szReportStatus);
				// }

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// try {
			// InputStream mInputStream = new ByteArrayInputStream(
			// AppValues.XMLResponse.getBytes());
			// new ReportXMLHandler(Photographs.this, mInputStream);
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			// XMLParser parser = new XMLParser();
			// parser.parseXML(
			// new HttpConnect()
			// .connect("http://dev.pocketapp.co.uk/dev/gmd/gmd_summary.xml"),
			// Photographs.this);

			// PhotoUploadTask photoupload = new
			// PhotoUploadTask(Photographs.this);
			// try {
			// String response = photoupload.httpPostSendFile("", null);
			// Log.d("SendPHoto", "Response is : " + response);
			// } catch (Exception e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			return null;
		}
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub

		AlertDialog quitAlertDialog = new AlertDialog.Builder(Photographs.this)
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

	class LoadPhotosTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			photoAdapter = new ImageAdapter(Photographs.this);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				progressPhotographs.setVisibility(View.GONE);
				grid_photographs.setAdapter(photoAdapter);
				bIsCameraOrGalleryOn = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class uploadAllPhotosTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			File file = AppValues.getPhotoGraphDirectory();
			List<File> files = getListFiles(file);

			for (int i = 0; i < files.size(); i++) {
				String szFilePath = files.get(i).getPath();
				String szFileName = files.get(i).getName();

				Log.d("SyncWithServer-upload() - uploadAllPhotosTask()",
						"UPLOAD File  is :: " + szFileName);

				PhotoUpload photoupload = new PhotoUpload(Photographs.this);
				try {
					String response = photoupload.httpPostSendFile("",
							CompressFile(files.get(i), szFileName));
					Log.d("SyncWithServer-upload() - uploadAllPhotosTask()",
							"UPLOAD Photo Response is : " + response);
					System.gc();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			// textview_loadingstatus.setText("Uploading report...");
			// new uploadAllReortsTask().execute();

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

	File CompressFile(File inputFile, String szInputFileName) {

		File outPutFile = null;
		Bitmap bm = null;
		try {
			outPutFile = new File(AppValues.getPhotosCompressedTempDirectory(),
					szInputFileName);
			bm = decodeFile(inputFile);
			OutputStream fOut = null;
			fOut = new FileOutputStream(outPutFile);
			bm.compress(Bitmap.CompressFormat.PNG, 50, fOut);
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

	class createFolderStructureTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub

			try {
				Log.d("*****************************",
						"STARTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
				Cursor cursorMill = getContentResolver().query(
						DataProvider.Mill.CONTENT_URI, null, null, null, null);

				File rootFile = new File(AppValues.getDirectory(),
						AppValues.APP_NAME);

				File mMill_File, mFirstGenFile, mSecondGenFile, mThirdGenFile, mLeafChildFile;

				if (cursorMill.moveToFirst()) {

					do {

						String szMillName = cursorMill.getString(cursorMill
								.getColumnIndex(DataProvider.Mill.MILL_NAME));

						mMill_File = new File(rootFile.getAbsolutePath(),
								szMillName);
						if (!mMill_File.exists()) {
							mMill_File.mkdir();
							Log.d("*****************************",
									"1111111111111111111111111111   "
											+ mMill_File.getAbsolutePath());
						}

						String szMillID = cursorMill.getString(cursorMill
								.getColumnIndex(DataProvider.Mill.MILL_ID));

						Cursor cursorServices = getContentResolver().query(
								DataProvider.Services.CONTENT_URI,
								null,
								DataProvider.Services.MILL_ID + " ='"
										+ szMillID + "'", null, null);
						if (cursorServices.moveToFirst()) {
							do {

								String szParentID = cursorServices
										.getString(cursorServices
												.getColumnIndex(DataProvider.Services.PARENT_ID));
								String szServicesFirstGenItemID = cursorServices
										.getString(cursorServices
												.getColumnIndex(DataProvider.Services.ITEM_ID));
								String szServicesFirstGenItemName = cursorServices
										.getString(cursorServices
												.getColumnIndex(DataProvider.Services.ITEM_NAME));

								// if (szServicesFirstGenItemName
								// .contains("Rotor poles"))
								// szServicesFirstGenItemName = "Rotor Poles";

								mFirstGenFile = new File(
										mMill_File.getAbsolutePath(),
										szServicesFirstGenItemName);
								if (!mFirstGenFile.exists()) {
									mFirstGenFile.mkdir();

									Log.d("*****************************",
											"22222222222222222222222222   "
													+ mFirstGenFile
															.getAbsolutePath());
								}

								Cursor cursorServicesSecondGen = getContentResolver()
										.query(DataProvider.Services.CONTENT_URI,
												null,
												DataProvider.Services.PARENT_ID
														+ " ='"
														+ szServicesFirstGenItemID
														+ "'", null, null);
								if (cursorServicesSecondGen.moveToFirst()) {
									do {
										String szServicesSecondGenItemID = cursorServicesSecondGen
												.getString(cursorServicesSecondGen
														.getColumnIndex(DataProvider.Services.ITEM_ID));

										String szServicesSecondGenItemName = cursorServicesSecondGen
												.getString(cursorServicesSecondGen
														.getColumnIndex(DataProvider.Services.ITEM_NAME));

										// if (szServicesSecondGenItemName
										// .contains("Rotor poles"))
										// szServicesSecondGenItemName =
										// "Rotor Poles";

										mSecondGenFile = new File(
												mFirstGenFile.getAbsolutePath(),
												szServicesSecondGenItemName);
										if (!mSecondGenFile.exists()) {
											mSecondGenFile.mkdir();

											Log.d("*****************************",
													"3333333333333333333333   "
															+ mSecondGenFile
																	.getAbsolutePath());
										}

										Cursor cursorServicesThirdGen = getContentResolver()
												.query(DataProvider.Services.CONTENT_URI,
														null,
														DataProvider.Services.PARENT_ID
																+ " ='"
																+ szServicesSecondGenItemID
																+ "'", null,
														null);

										if (cursorServicesThirdGen
												.moveToFirst()) {
											do {

												String szServicesThirdGenItemID = cursorServicesThirdGen
														.getString(cursorServicesThirdGen
																.getColumnIndex(DataProvider.Services.ITEM_ID));

												String szServicesThirdGenItemName = cursorServicesThirdGen
														.getString(cursorServicesThirdGen
																.getColumnIndex(DataProvider.Services.ITEM_NAME));

												mThirdGenFile = new File(
														mSecondGenFile
																.getAbsolutePath(),
														szServicesThirdGenItemName);
												if (!mThirdGenFile.exists()) {
													mThirdGenFile.mkdir();

													Log.d("*****************************",
															"444444444444444444   "
																	+ mThirdGenFile
																			.getAbsolutePath());
												}

												Cursor taskCursor = getContentResolver()
														.query(DataProvider.Tasks.CONTENT_URI,
																null,
																DataProvider.Tasks.SERVICES_ITEM_ID
																		+ " ='"
																		+ szServicesThirdGenItemID
																		+ "' AND "
																		+ DataProvider.Tasks.TASK_CONTENT
																		+ " ='Special Type' AND "
																		+ DataProvider.Tasks.TASK_NAME
																		+ " !='Report a problem'",
																null, null);

												boolean bIsContainSpecialType = false;
												String szSpecialTypeName = "";
												if (taskCursor.moveToFirst()) {
													bIsContainSpecialType = true;
													szSpecialTypeName = taskCursor
															.getString(taskCursor
																	.getColumnIndex(DataProvider.Tasks.TASK_NAME));
												} else {
													bIsContainSpecialType = false;
													szSpecialTypeName = "";
												}
												taskCursor.close();

												String[] selectionargs;
												String szSelection = null;

												if (!bIsContainSpecialType) {
													szSelection = DataProvider.Child_Leaf.CHILD_LEAF
															+ " != ?";
													selectionargs = new String[] { "Special Type" };
												} else {
													szSelection = null;
													selectionargs = null;
												}

												Cursor cursorLeaf = getContentResolver()
														.query(DataProvider.Child_Leaf.CONTENT_URI,
																null,
																szSelection,
																selectionargs,
																null);

												if (cursorLeaf.moveToFirst()) {
													do {
														String szLeafChild = cursorLeaf
																.getString(cursorLeaf
																		.getColumnIndex(DataProvider.Child_Leaf.CHILD_LEAF));

														// if (szLeafChild
														// .contains("Help"))
														// szLeafChild = "Help";

														if (!szLeafChild
																.equals("Report a problem")) {

															if (szLeafChild
																	.equals("Special Type"))
																szLeafChild = szSpecialTypeName;

															mLeafChildFile = new File(
																	mThirdGenFile
																			.getAbsolutePath(),
																	szLeafChild);
															if (!mLeafChildFile
																	.exists()) {
																mLeafChildFile
																		.mkdir();

																Log.d("*****************************",
																		"555555555555555555   "
																				+ mLeafChildFile
																						.getAbsolutePath());
															}
														}
													} while (cursorLeaf
															.moveToNext());
													cursorLeaf.close();
												}

											} while (cursorServicesThirdGen
													.moveToNext());
											cursorServicesThirdGen.close();
										}

									} while (cursorServicesSecondGen
											.moveToNext());
									cursorServicesSecondGen.close();
								}

							} while (cursorServices.moveToNext());
							cursorServices.close();
						}

					} while (cursorMill.moveToNext());

					cursorMill.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.d("*******************************",
					"STOPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			index = grid_photographs.getFirstVisiblePosition();
			View v = grid_photographs.getChildAt(0);
			top = (v == null) ? 0 : v.getTop();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	// save value on onSaveInstanceState
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("SCROLL_POSITION", index);
		outState.putBoolean("isCameraOrGalleryClicked", bIsCameraOrGalleryOn);
	}

	// Restore them on onRestoreInstanceState
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		index = savedInstanceState.getInt("SCROLL_POSITION");
		bIsCameraOrGalleryOn = savedInstanceState
				.getBoolean("isCameraOrGalleryClicked");
		// if (index != -1)
		// if (bIsCameraOrGalleryOn == false)
		// grid_photographs.post(new Runnable() {
		// public void run() {
		// grid_photographs.setSelection(index);
		// }
		// });
	}

}
