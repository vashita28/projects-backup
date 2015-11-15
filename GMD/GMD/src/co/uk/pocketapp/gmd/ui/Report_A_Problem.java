package co.uk.pocketapp.gmd.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;
import co.uk.pocketapp.gmd.util.XML_Values;

public class Report_A_Problem extends ParentActivity implements OnClickListener {

	public static HashMap<String, String> hm_Problem_Data = new HashMap<String, String>();

	TextView GMD_heading_TextView;
	TextView page_Title_TextView;

	View view_report_a_problem_top_bar;
	TextView textview_title;
	ImageView imageview_camera;
	private final int CLICK_AND_SAVE = 1336;
	String m_CapturedImagePath = "", m_szFileName = "";

	Button btn_next, btn_cancel;
	EditText et_what_is_the_problem, et_where_is_the_problem,
			et_why_is_it_a_problem, et_please_add_image;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mmaa");
	Date date;
	String m_szDateTime = "";
	TextView textview_dateAndTime;
	public static Context mContext;

	String mill_ID = "";
	String parent_ID = "";
	String Id = "", m_szThirdGenItemID = "";
	String coordinates = "empty";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_a_problem);

		mContext = Report_A_Problem.this;

		if (getIntent().getExtras() != null) {
			mill_ID = getIntent().getStringExtra("mill_id");
			parent_ID = getIntent().getStringExtra("parent_id");
			Id = getIntent().getStringExtra("id");
			coordinates = getIntent().getStringExtra("coordinates");
			m_szThirdGenItemID = getIntent().getStringExtra("thirdgenitemid");

			Log.v("***********************************-----------*********************  ",
					"MILL ID:: " + mill_ID + " PARENT_ID::   " + parent_ID
							+ " ID:: " + Id + " coordinates :: " + coordinates);
		}

		// font implementation of main heading "GMD": textview
		GMD_heading_TextView = (TextView) findViewById(R.id.Heading);
		GMD_heading_TextView.setTypeface(GMDApplication.fontHeading);

		// font implementation of page heading: textview
		page_Title_TextView = (TextView) findViewById(R.id.pageTitle);
		page_Title_TextView.setTypeface(GMDApplication.fontText);

		btn_next = (Button) findViewById(R.id.btn_next);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_next.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);

		et_what_is_the_problem = (EditText) findViewById(R.id.et_what_is_the_problem);
		et_where_is_the_problem = (EditText) findViewById(R.id.et_where_is_the_problem);
		et_why_is_it_a_problem = (EditText) findViewById(R.id.et_why_is_it_a_problem);
		et_please_add_image = (EditText) findViewById(R.id.et_please_add_image);

		et_what_is_the_problem.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_where_is_the_problem.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_why_is_it_a_problem.setImeOptions(EditorInfo.IME_ACTION_DONE);
		et_please_add_image.setImeOptions(EditorInfo.IME_ACTION_DONE);

		et_what_is_the_problem.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_where_is_the_problem.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));
		et_why_is_it_a_problem.setHintTextColor(getResources().getColor(
				R.color.hint_text_color));

		view_report_a_problem_top_bar = (View) findViewById(R.id.view_report_a_problem_top_bar);
		textview_title = (TextView) view_report_a_problem_top_bar
				.findViewById(R.id.pageTitle);
		textview_title.setText("Report a Problem");

		textview_dateAndTime = (TextView) view_report_a_problem_top_bar
				.findViewById(R.id.DateNtime);
		// setting data and time:
		try {
			Date today = Calendar.getInstance().getTime();
			String reportDate = dateFormat.format(today);
			textview_dateAndTime.setText(reportDate.toLowerCase());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		imageview_camera = (ImageView) findViewById(R.id.imageview_camera);
		imageview_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startCameraCapture();
			}
		});

	}

	private void startCameraCapture() {
		// TODO Auto-generated method stub
		// File directory = new File(Environment.getExternalStorageDirectory()
		// + File.separator + AppValues.APP_NAME + "/Camera/");
		// File directory = new File(AppValues.getDirectory() + File.separator
		// + AppValues.APP_NAME + "/Camera/");
		// directory.mkdirs();
		// m_szFileName = System.currentTimeMillis() + "_" + AppValues.APP_NAME
		// + "_" + Util.getReportID(Report_A_Problem.this) + ".png";
		// File imageFile = new File(directory, m_szFileName);
		// Uri imageFileUri = Uri.fromFile(imageFile);

		File imageFile = AppValues.getPhotoGraphFile();

		m_CapturedImagePath = imageFile.getAbsolutePath();
		m_szFileName = imageFile.getName();

		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
		// Uri.fromFile(imageFile));
		// startActivityForResult(intent, CLICK_AND_SAVE);

		Intent galleryintent = new Intent(Intent.ACTION_PICK);
		galleryintent.setType("image/*");

		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(imageFile));
		cameraIntent.putExtra("return-data", true);

		Intent chooser = new Intent(Intent.ACTION_CHOOSER);
		chooser.putExtra(Intent.EXTRA_INTENT, galleryintent);
		chooser.putExtra(Intent.EXTRA_TITLE, "Select");

		Intent[] intentArray = { cameraIntent };
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
		startActivityForResult(chooser, CLICK_AND_SAVE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CLICK_AND_SAVE:
			switch (resultCode) {
			case Activity.RESULT_OK:
				date = new Date();
				m_szDateTime = dateFormat.format(date);
				System.gc();
				ProgressDialog dialog = new ProgressDialog(
						Report_A_Problem.this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
				dialog.setMessage("Loading...");
				dialog.show();
				new saveOrProcessPhotoTask(data, dialog).execute();
				break;
			default:
				Log.d("Report_A_Problem-onActivityResult()", "Cancelled");
				break;
			}
			break;
		default:
			Log.d("Report_A_Problem-onActivityResult()", "Cancelled");
			break;
		}

	}

	class saveOrProcessPhotoTask extends AsyncTask<Void, Void, Void> {

		Intent data;
		ProgressDialog dialog;

		public saveOrProcessPhotoTask(Intent data, ProgressDialog dialog) {
			// TODO Auto-generated constructor stub
			this.data = data;
			this.dialog = dialog;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (data != null) {
				Uri selectedImage = data.getData();

				Log.d("*********************************************",
						" SELECTED IMAGE ::: " + selectedImage);

				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				String filename = "";
				String filePath = "";

				if (cursor != null && cursor.moveToFirst()) {
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					if (filePath != null) {
						filePath = cursor.getString(columnIndex);
						int pos = filePath.lastIndexOf("/");
						filename = filePath.substring(pos + 1).trim();
						Log.d("*********************************************",
								" SELECTED FILENAME IS ::: " + filename);
						copyfile(filePath);
					}
					// m_CapturedImagePath = filePath;
					cursor.deactivate();
					cursor.close();
					return null;
				}
			}

			Bitmap bm = null;
			OutputStream fOut = null;
			// File newFile;
			try {

				ExifInterface ei = new ExifInterface(m_CapturedImagePath);
				int orientation = ei.getAttributeInt(
						ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);

				if (orientation == ExifInterface.ORIENTATION_ROTATE_90
						|| orientation == ExifInterface.ORIENTATION_ROTATE_180) {

					bm = BitmapFactory.decodeFile(m_CapturedImagePath);

					// newFile = new File(AppValues.getDirectory()
					// + File.separator + AppValues.APP_NAME
					// + File.separator + "/Camera/", "_"
					// + m_szFileName);
					// newFile.createNewFile();
					fOut = new FileOutputStream(m_CapturedImagePath);

					Matrix matrix = new Matrix();
					switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						matrix.postRotate(90);
						bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
								bm.getHeight(), matrix, true);
						bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						matrix.postRotate(180);
						bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
								bm.getHeight(), matrix, true);
						bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
						break;
					}

					// if (newFile.length() > 0) {
					// new File(m_CapturedImagePath).delete();
					// m_CapturedImagePath = newFile.getAbsolutePath();
					// m_szFileName = "_" + m_szFileName;
					// }

					fOut.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialog != null)
				dialog.dismiss();

			Intent intent = new Intent(Report_A_Problem.this,
					Review_Photo.class);
			intent.putExtra("capturedimageuri", m_CapturedImagePath);
			intent.putExtra("filename", m_szFileName);
			intent.putExtra("datetime", m_szDateTime);
			startActivity(intent);
			// finish();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btn_next:
			if (et_what_is_the_problem.getText().length() > 0
					&& et_where_is_the_problem.getText().length() > 0
					&& et_why_is_it_a_problem.getText().length() > 0
					&& !m_CapturedImagePath.equals("")) {

				Intent defineASolutionIntent = new Intent(
						Report_A_Problem.this, Define_The_Solution.class);
				defineASolutionIntent.putExtra("capturedimageuri",
						m_CapturedImagePath);
				defineASolutionIntent.putExtra("filename", m_szFileName);
				defineASolutionIntent.putExtra("datetime",
						m_szDateTime.toLowerCase());

				Save_Values_In_XML();

				startActivity(defineASolutionIntent);

				/*
				 * Intent intent = new Intent(Report_A_Problem.this,
				 * Report_A_Problem_ReviewImage.class);
				 * intent.putExtra("capturedimageuri", m_CapturedImagePath);
				 * intent.putExtra("filename", m_szFileName);
				 * intent.putExtra("datetime", m_szDateTime);
				 * startActivity(intent); // finish();
				 */
			} else {
				// Toast.makeText(Report_A_Problem.this,
				// "All fields are mandatory!", Toast.LENGTH_LONG).show();
				AlertDialog m_AlertDialog = new AlertDialog.Builder(
						Report_A_Problem.this)
						.setTitle("Alert!")
						.setMessage("All fields are mandatory")
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface argDialog,
											int argWhich) {

									}
								}).create();
				m_AlertDialog.setCanceledOnTouchOutside(false);
				m_AlertDialog.show();
			}
			break;

		case R.id.btn_cancel:
			finish();
			File file = new File(m_CapturedImagePath);
			file.delete();
			m_CapturedImagePath = "";
			Util.setPhotoEntryDetails(Report_A_Problem.this, m_szFileName, "",
					"", "", "", "");
			Photographs.bIsCameraOrGalleryOn = true;
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		File file = new File(m_CapturedImagePath);
		file.delete();
		m_CapturedImagePath = "";
		Util.setPhotoEntryDetails(Report_A_Problem.this, m_szFileName, "", "",
				"", "", "");
		Photographs.bIsCameraOrGalleryOn = true;
	}

	private void Save_Values_In_XML() {
		String LN = System.getProperty("line.separator");
		hm_Problem_Data.put(XML_Values.MILL_ID, mill_ID);
		hm_Problem_Data.put(XML_Values.PARENT_ID, parent_ID);
		hm_Problem_Data.put(XML_Values.THIRDGENITEMID, m_szThirdGenItemID);
		hm_Problem_Data.put(XML_Values.ID, Id);
		hm_Problem_Data.put(XML_Values.COORDINATES, coordinates);

		hm_Problem_Data.put(XML_Values.WHAT_IS_THE_PROBLEM,
				et_what_is_the_problem.getText().toString());
		hm_Problem_Data.put(XML_Values.WHERE_IS_THE_PROBLEM,
				et_where_is_the_problem.getText().toString());
		hm_Problem_Data.put(XML_Values.WHY_IS_IT_A_PROBLEM,
				et_why_is_it_a_problem.getText().toString());

	}

	public void saveInPrefrences(String key, String value) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private void copyfile(String srFile) {
		try {

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

				m_CapturedImagePath = AppValues.getPhotoGraphFile()
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!m_CapturedImagePath.equals(""))
			et_please_add_image
					.setText("Photo added, you may retake by clicking camera icon");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		m_CapturedImagePath = "";
	}
}
