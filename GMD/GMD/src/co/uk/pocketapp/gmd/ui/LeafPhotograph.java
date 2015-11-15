package co.uk.pocketapp.gmd.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

public class LeafPhotograph extends Activity implements View.OnClickListener {

	private final int CLICK_AND_SAVE = 1336;
	String m_CapturedImagePath = "", m_szFileName = "";

	Button btn_next, btn_take_again;
	ImageView imageview_preview;

	View view_photographs_camera_top_bar;
	TextView textview_title;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mmaa");
	Date date;
	String m_szDateTime = "";

	TextView site_Name;
	TextView mill_Name;

	TextView date_N_Time;
	String reportDate;

	boolean bIsFirsTime = true;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	String szThirdGenItemID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		options = new DisplayImageOptions.Builder()
				.displayer(new FadeInBitmapDisplayer(800))
				.bitmapConfig(Bitmap.Config.ARGB_8888).build();

		if (getIntent().getExtras() != null)
			szThirdGenItemID = getIntent().getExtras().getString(
					"thirdgenitemid");

		// photo-details entry
		// log a problem
		startCameraCapture();

	}

	private void startCameraCapture() {
		// TODO Auto-generated method stub

		File imageFile = AppValues.getPhotoGraphFile();

		m_CapturedImagePath = imageFile.getAbsolutePath();
		m_szFileName = imageFile.getName();

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
				displayAllLayoutContents();
				date = new Date();
				m_szDateTime = dateFormat.format(date);
				System.gc();
				ProgressDialog dialog = new ProgressDialog(LeafPhotograph.this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
				dialog.setMessage("Loading...");
				dialog.show();
				new saveOrProcessPhotoTask(data, dialog).execute();
				break;
			default:
				finish();
				Log.d("LeafPhotograph-onActivityResult()", "Cancelled");
				break;
			}
			break;
		default:
			Log.d("LeafPhotograph-onActivityResult()", "Cancelled");
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
					filePath = cursor.getString(columnIndex);
					if (filePath != null) {
						int pos = filePath.lastIndexOf("/");
						filename = filePath.substring(pos + 1).trim();
						Log.d("*********************************************",
								" SELECTED FILENAME IS ::: " + filename);
						copyfile(filePath);
					}
					// Intent intent = new Intent(this, Review_Photo.class);
					// intent.putExtra("capturedimageuri",
					// m_CapturedImagePath);
					// intent.putExtra("filename", m_szFileName);
					// intent.putExtra("datetime", m_szDateTime);
					// startActivity(intent);
					// m_CapturedImagePath = filePath;
					cursor.deactivate();
					cursor.close();

					// imageview_preview.setImageURI(Uri
					// .parse(m_CapturedImagePath));

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

			// imageview_preview.setImageURI(Uri.parse(m_CapturedImagePath));
			imageLoader.displayImage("file://" + m_CapturedImagePath,
					imageview_preview, options);

			// Intent intent = new Intent(this, Review_Photo.class);
			// intent.putExtra("capturedimageuri", m_CapturedImagePath);
			// intent.putExtra("filename", m_szFileName);
			// intent.putExtra("datetime", m_szDateTime);
			// startActivity(intent);
		}

	}

	private void copyfile(String srFile) {
		try {

			Bitmap bm = null;
			OutputStream fOut = null;
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

					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						fOut.write(buf, 0, len);
					}
					in.close();
					Log.d("LeafPhotograph", "Photo File copied...");
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_takePhotoAgain:
			File file = new File(m_CapturedImagePath);
			file.delete();
			imageview_preview.setImageBitmap(null);
			m_CapturedImagePath = "";
			m_szFileName = "";
			startCameraCapture();
			break;

		case R.id.btn_next:
			Intent intent = new Intent(this, Photo_Details.class);
			intent.putExtra("capturedimageuri", m_CapturedImagePath);
			intent.putExtra("filename", m_szFileName);
			intent.putExtra("datetime", m_szDateTime);
			intent.putExtra("thirdgenitemid", szThirdGenItemID);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	void displayAllLayoutContents() {

		if (bIsFirsTime) {
			setContentView(R.layout.review_photo);
			bIsFirsTime = false;
		}

		site_Name = (TextView) findViewById(R.id.siteName);
		site_Name.setTypeface(GMDApplication.fontText);
		mill_Name = (TextView) findViewById(R.id.millName);
		mill_Name.setTypeface(GMDApplication.fontText);

		// Getting data from share preferences and show on site name and mill
		// name textview:
		site_Name.setText(Util.getSiteName(LeafPhotograph.this));
		mill_Name.setText(Util.getMillName(LeafPhotograph.this));

		view_photographs_camera_top_bar = (View) findViewById(R.id.view_review_photo_top_bar);
		textview_title = (TextView) view_photographs_camera_top_bar
				.findViewById(R.id.pageTitle);

		textview_title.setText("Log a Problem");

		date_N_Time = (TextView) findViewById(R.id.DateNtime);
		// setting data and time:
		try {
			Date today = Calendar.getInstance().getTime();
			reportDate = dateFormat.format(today);
			date_N_Time.setText(reportDate.toLowerCase());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		btn_next = (Button) findViewById(R.id.btn_next);
		btn_take_again = (Button) findViewById(R.id.btn_takePhotoAgain);
		btn_next.setOnClickListener(LeafPhotograph.this);
		btn_take_again.setOnClickListener(this);
		imageview_preview = (ImageView) findViewById(R.id.imageview_preview);
	}

}
