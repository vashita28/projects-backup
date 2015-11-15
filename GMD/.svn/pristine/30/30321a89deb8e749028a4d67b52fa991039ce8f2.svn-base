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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

public class Review_Photo extends ParentActivity implements OnClickListener {
	TextView GMD_heading_TextView;
	TextView page_Title_TextView;

	View view_review_photo_top_bar;
	TextView textview_title;

	Button btn_next, btn_take_again;
	ImageView imageview_preview;
	File file;
	Uri uriImage = null;
	String m_CapturedImagePath = "", m_szFileName = "";

	private final int CLICK_AND_SAVE = 1336;

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mmaa");
	Date date;
	String m_szDateTime = "";
	TextView textview_dateAndTime;

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

		// font implementation of main heading "GMD": textview
		GMD_heading_TextView = (TextView) findViewById(R.id.Heading);
		GMD_heading_TextView.setTypeface(GMDApplication.fontHeading);

		// font implementation of page heading: textview
		page_Title_TextView = (TextView) findViewById(R.id.pageTitle);
		page_Title_TextView.setTypeface(GMDApplication.fontText);

		view_review_photo_top_bar = (View) findViewById(R.id.view_review_photo_top_bar);
		textview_title = (TextView) view_review_photo_top_bar
				.findViewById(R.id.pageTitle);

		textview_title.setText("Log a Problem");

		textview_dateAndTime = (TextView) view_review_photo_top_bar
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

		btn_next = (Button) findViewById(R.id.btn_next);
		btn_take_again = (Button) findViewById(R.id.btn_takePhotoAgain);
		btn_next.setOnClickListener(this);
		btn_take_again.setOnClickListener(this);
		imageview_preview = (ImageView) findViewById(R.id.imageview_preview);

		if (getIntent().getExtras() != null) {
			m_CapturedImagePath = getIntent()
					.getStringExtra("capturedimageuri");
			m_szDateTime = getIntent().getStringExtra("datetime");
		}
		Log.d("Review_Photo", m_CapturedImagePath);
		file = new File(m_CapturedImagePath);
		m_szFileName = file.getName();
		uriImage = Uri.fromFile(file);
		// imageview_preview.setImageURI(uriImage);
		imageLoader.displayImage("file://" + file.getPath(), imageview_preview,
				options);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_takePhotoAgain:
			File file = new File(m_CapturedImagePath);
			file.delete();
			m_CapturedImagePath = "";
			m_szFileName = "";
			startCameraCapture();
			break;

		case R.id.btn_next:
			Intent intent = new Intent(this, Photo_Details_Entry.class);
			intent.putExtra("capturedimageuri", m_CapturedImagePath);
			intent.putExtra("filename", m_szFileName);
			intent.putExtra("datetime", m_szDateTime);
			Report_A_Problem.hm_Problem_Data.put("capturedimagename",
					m_szFileName);
			Report_A_Problem.hm_Problem_Data.put("capturedimagepath",
					m_CapturedImagePath);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		file.delete();
		Util.setPhotoEntryDetails(Review_Photo.this, m_szFileName, "", "", "",
				"", "");
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case CLICK_AND_SAVE:
			switch (resultCode) {
			case Activity.RESULT_OK:
				date = new Date();
				m_szDateTime = dateFormat.format(date);

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
						int columnIndex = cursor
								.getColumnIndex(filePathColumn[0]);
						filePath = cursor.getString(columnIndex);
						int pos = filePath.lastIndexOf("/");
						filename = filePath.substring(pos + 1).trim();
						Log.d("*********************************************",
								" SELECTED FILENAME IS ::: " + filename);
						copyfile(filePath);

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
						imageLoader.displayImage("file://"
								+ m_CapturedImagePath, imageview_preview,
								options);
						break;
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

				// imageview_preview.setImageURI(Uri.parse(m_CapturedImagePath));
				imageLoader.displayImage("file://" + m_CapturedImagePath,
						imageview_preview, options);
				// saveAndProcessSavedImage(m_CapturedImagePath);
				break;
			default:
				Log.d("Review_Photo-onActivityResult()", "Cancelled");
				break;
			}
			break;
		default:
			Log.d("Review_Photo-onActivityResult()", "Cancelled");
			break;
		}
	}

	private void saveAndProcessSavedImage(String szFilePath) {
		// TODO Auto-generated method stub

		Bitmap bm = null;
		OutputStream fOut = null;
		// File newFile = null;
		try {
			ExifInterface ei = new ExifInterface(szFilePath);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90
					|| orientation == ExifInterface.ORIENTATION_ROTATE_180) {

				bm = BitmapFactory.decodeFile(szFilePath);
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

		// if (newFile != null && newFile.length() > 0) {
		// new File(m_CapturedImagePath).delete();
		// m_CapturedImagePath = newFile.getAbsolutePath();
		// m_szFileName = "_" + m_szFileName;
		// imageview_preview.setImageBitmap(bm);
		// } else {
		// imageview_preview.setImageURI(Uri.parse(szFilePath));
		// }

		// imageview_preview.setImageURI(Uri.parse(m_CapturedImagePath));
		imageLoader.displayImage("file://" + m_CapturedImagePath,
				imageview_preview, options);

	}

	private void startCameraCapture() {
		// TODO Auto-generated method stub
		// File directory = new File(AppValues.getDirectory()
		// + File.separator + AppValues.APP_NAME + "/Camera/");
		// File directory = new File(AppValues.getDirectory() + File.separator
		// + AppValues.APP_NAME + "/Camera/");
		// directory.mkdirs();
		// m_szFileName = System.currentTimeMillis() + "_" + AppValues.APP_NAME
		// + "_" + Util.getReportID(Review_Photo.this) + ".png";
		// File imageFile = new File(directory, m_szFileName);

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

}
