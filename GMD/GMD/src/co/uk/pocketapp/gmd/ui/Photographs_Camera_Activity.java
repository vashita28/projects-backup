package co.uk.pocketapp.gmd.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class Photographs_Camera_Activity extends Activity implements
		OnClickListener {

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

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.review_photo);

		// ImageLoader Options
		options = new DisplayImageOptions.Builder()
				.displayer(new FadeInBitmapDisplayer(800)).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.ARGB_8888).build();

		site_Name = (TextView) findViewById(R.id.siteName);
		site_Name.setTypeface(GMDApplication.fontText);
		mill_Name = (TextView) findViewById(R.id.millName);
		mill_Name.setTypeface(GMDApplication.fontText);

		// Getting data from share preferences and show on site name and mill
		// name textview:
		site_Name.setText(Util.getSiteName(Photographs_Camera_Activity.this));
		mill_Name.setText(Util.getMillName(Photographs_Camera_Activity.this));

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
		btn_next.setOnClickListener(this);
		btn_take_again.setOnClickListener(this);
		imageview_preview = (ImageView) findViewById(R.id.imageview_preview);

		startCameraCapture();
	}

	private void startCameraCapture() {
		// TODO Auto-generated method stub
		// File directory = new File(Environment.getExternalStorageDirectory()
		// + File.separator + AppValues.APP_NAME + "/Camera/");
		// File directory = new File(AppValues.getDirectory() + File.separator
		// + AppValues.APP_NAME + "/Camera/");
		// directory.mkdirs();
		// m_szFileName = System.currentTimeMillis() + "_" + AppValues.APP_NAME
		// + "_" + Util.getReportID(Photographs_Camera_Activity.this)
		// + ".png";
		// File imageFile = new File(directory, m_szFileName);

		File imageFile = AppValues.getPhotoGraphFile();

		m_CapturedImagePath = imageFile.getAbsolutePath();
		m_szFileName = imageFile.getName();

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(imageFile));
		startActivityForResult(intent, CLICK_AND_SAVE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case CLICK_AND_SAVE:
			switch (resultCode) {
			case Activity.RESULT_OK:
				date = new Date();
				m_szDateTime = dateFormat.format(date);
				ProgressDialog dialog = new ProgressDialog(
						Photographs_Camera_Activity.this);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
				dialog.setMessage("Loading...");
				dialog.show();
				new saveOrProcessPhotoTask(dialog).execute();
				break;
			default:
				Log.d("Photographs_Camera-onActivityResult()", "Cancelled");
				finish();
				break;
			}
			break;
		default:
			Log.d("Photographs_Camera-onActivityResult()", "Cancelled");
			break;
		}
	}

	class saveOrProcessPhotoTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;

		public saveOrProcessPhotoTask(ProgressDialog dialog) {
			// TODO Auto-generated constructor stub
			this.dialog = dialog;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			saveAndProcessSavedImage();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (dialog != null)
				dialog.dismiss();

			imageLoader.displayImage("file://" + m_CapturedImagePath,
					imageview_preview, options);
		}

	}

	private void saveAndProcessSavedImage() {
		// TODO Auto-generated method stub

		Bitmap bm = null;
		OutputStream fOut = null;
		// File newFile = null;
		try {

			ExifInterface ei = new ExifInterface(m_CapturedImagePath);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90
					|| orientation == ExifInterface.ORIENTATION_ROTATE_180) {

				bm = BitmapFactory.decodeFile(m_CapturedImagePath);

				// newFile = new File(AppValues.getDirectory() + File.separator
				// + AppValues.APP_NAME + File.separator + "/Camera/", "_"
				// + m_szFileName);
				// newFile.createNewFile();
				// fOut = new FileOutputStream(newFile);

				fOut = new FileOutputStream(m_CapturedImagePath);

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
				fOut.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// if (newFile != null && newFile.length() > 0) {
		// new File(m_CapturedImagePath).delete();
		// m_CapturedImagePath = newFile.getAbsolutePath();
		// m_szFileName = "_" + m_szFileName;
		// imageview_preview.setImageBitmap(bm);
		// } else {
		// imageview_preview.setImageURI(Uri.parse(szFilePath));
		// }

		// imageview_preview.setImageURI(Uri.parse(m_CapturedImagePath));

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
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		imageview_preview.setImageBitmap(null);
		imageview_preview = null;
		System.gc();
		System.gc();
	}
}
