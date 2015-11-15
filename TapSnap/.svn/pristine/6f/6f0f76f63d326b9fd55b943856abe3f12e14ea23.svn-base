package com.coudriet.snapnshare.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class EditProfilePictureActivity extends Activity {
	public static String TAG = "EditProfilePicture";

	public static final int TAKE_PHOTO_REQUEST = 0;
	public static final int PICK_PHOTO_REQUEST = 1;

	private ActionBar actionBar;
	private ImageView ivProfilePicture;
	private RelativeLayout rlProfilePicture;
	private Button btnSavebutton;
	File destination;
	ImageLoader imageloader;
	DisplayImageOptions options;
	ParseFile fileImgGallery;

	Context mContext;

	private ProgressDialog progressDialog;
	private ParseUser currentUser;

	public static String sImagePath = null;
	String parseFileLocation = null;
	File photoAfterRotation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile_picture);

		mContext = this;

		// Show the Up button in the action bar.
		setupActionBar();
		actionBar = getActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.logo));

		imageloader = ImageLoader.getInstance();

		if (ParseUser.getCurrentUser() != null)
			currentUser = ParseUser.getCurrentUser();

		ivProfilePicture = (ImageView) findViewById(R.id.ivProfilePic);
		btnSavebutton = (Button) findViewById(R.id.saveButton);
		rlProfilePicture = (RelativeLayout) findViewById(R.id.rlProfilePicture);
		btnSavebutton
				.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);

		final ParseFile file = (ParseFile) currentUser
				.get(ParseConstants.KEY_PROFILE_PICTURE);

		if (file != null) {
			try {
				imageloader.displayImage(file.getUrl(), ivProfilePicture,
						options);

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			String pictureURL = (String) currentUser
					.get(ParseConstants.KEY_FACEBOOK_PROFILE_PICTURE);
			imageloader.displayImage(pictureURL, ivProfilePicture, options);
		}

		rlProfilePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						EditProfilePictureActivity.this);
				builder.setItems(R.array.camera_choices_signup, mDialogListener);
				AlertDialog dialog = builder.create();
				dialog.show();

			}
		});

		btnSavebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SaveImageTask saveImageTask = new SaveImageTask();
				saveImageTask.execute();

			}
		});
	}

	public File getTempFile(Context context, String url) {
		File file = null;
		try {
			String fileName = Uri.parse(url).getLastPathSegment();
			file = File.createTempFile(fileName, null, context.getCacheDir());
			// setImageInPotraitMode(file.getPath().toString());
		} catch (IOException e) {
			// Error while creating file
		}
		return file;
	}

	private String setImageInPotraitMode(String imagePath) {
		String returnPath = null;
		FileOutputStream out = null;
		Bitmap bm = null;
		try {
			Random generator = new Random();
			int n = 10000;
			n = generator.nextInt(n);
			String fname = "ParseImage-" + n + ".jpg";
			photoAfterRotation = new File(
					Environment.getExternalStorageDirectory(), fname);

			if (photoAfterRotation.exists()) {
				photoAfterRotation.delete();
			}
			out = new FileOutputStream(photoAfterRotation.getPath());
		} catch (Exception e) {
			Log.e(TAG, "Exception:: " + e.getMessage());
		}
		try {

			ExifInterface ei = new ExifInterface(imagePath);
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90
					|| orientation == ExifInterface.ORIENTATION_ROTATE_180) {

				bm = BitmapFactory.decodeFile(imagePath);

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

				// ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				// bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				// out.write(bytes.toByteArray());

				bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			}

			if (bm != null) {
				Log.e(TAG, "bitmap not null  ");

				returnPath = photoAfterRotation.getPath();
			} else {
				returnPath = imagePath;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			try {
				out.close();

			} catch (Throwable ignore) {
			}
		}
		return returnPath;
	}

	protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {

			case 0: // Take picture
				Intent takePhotoIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(getImageFileName()));
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

			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == TAKE_PHOTO_REQUEST) {
				// camera picture
				String imagePath = destination.getPath();
				Log.i(TAG, "Camera Path::>  " + imagePath);
				setimage(imagePath);

			} else if (requestCode == PICK_PHOTO_REQUEST) {
				// gallery
				Uri selectedImageUri = intent.getData();
				String galleryimagepath = getPath(selectedImageUri);
				Log.i(TAG, "Gallery Path::>  " + galleryimagepath);
				setimage(galleryimagepath);

			}
		} else if (resultCode != RESULT_CANCELED) {
			Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG)
					.show();
		}
	}

	public String dateToString(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	private File getImageFileName() {
		String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
		return destination = new File(
				Environment.getExternalStorageDirectory(), name + ".jpg");
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

	void setimage(String imagepath) {
		if (imagepath != null) {
			ivProfilePicture.setImageBitmap(null);
			ivProfilePicture.setTag(null);

			RotateImageTask rotateTask = new RotateImageTask(imagepath);
			rotateTask.execute();

		}
	}

	public void sendbitmap_Parse(Uri uri, String imagepath) {

		ParcelFileDescriptor parcelFD = null;
		try {
			parcelFD = getContentResolver().openFileDescriptor(uri, "r");
			FileDescriptor imageSource = parcelFD.getFileDescriptor();

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFileDescriptor(imageSource, null, o);

			// the new size we want to scale to
			final int REQUIRED_SIZE = 200;

			// Find the correct scale value. It should be the power of 2.
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
					break;
				}
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeFileDescriptor(imageSource,
					null, o2);

			if (bitmap != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				String currImageURI = imagepath;

				if (currImageURI != null) {
					String fullPath = currImageURI;
					int index = fullPath.lastIndexOf("/");
					String fileName = fullPath.substring(index + 1);

					// byte[] fileData = currImageURI.getBytes();
					fileImgGallery = new ParseFile(fileName, byteArray);

					// fileImgGallery.saveInBackground();
					fileImgGallery.saveInBackground(new SaveCallback() {
						@Override
						public void done(ParseException ex) {
							if (ex == null) {
								Log.e(TAG, "saved   true");
								if (progressDialog != null)
									progressDialog.dismiss();

								Intent intent = new Intent(
										EditProfilePictureActivity.this,
										SettingsActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								Toast.makeText(
										EditProfilePictureActivity.this,
										"Profile picture successfully updated.",
										Toast.LENGTH_SHORT).show();
							} else {
								// Failed
								Log.e(TAG, "saved   false");

								Intent intent = new Intent(
										EditProfilePictureActivity.this,
										SettingsActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								Toast.makeText(
										EditProfilePictureActivity.this,
										"Network error.Please try after some time.",
										Toast.LENGTH_SHORT).show();
							}
						}
					});
					currentUser.put(ParseConstants.KEY_PROFILE_PICTURE,
							fileImgGallery);
					currentUser.put(
							ParseConstants.KEY_FACEBOOK_PROFILE_PICTURE, "");
					currentUser.saveInBackground();
				}
			}

		} catch (FileNotFoundException e) {
			// handle errors
		} catch (IOException e) {
			// handle errors
		} finally {
			if (parcelFD != null)
				try {
					parcelFD.close();
				} catch (IOException e) {
					// ignored
				}
		}
	}

	private class SaveImageTask extends AsyncTask<Void, Void, Void> {

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(EditProfilePictureActivity.this);
			progressDialog.setMessage("Saving image...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		protected void onPostExecute(Void Result) {
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (sImagePath != null) {
				Log.e(TAG, "sImagePath  " + sImagePath);
				Uri uri = Uri.parse("file://" + sImagePath);
				if (uri != null && sImagePath != null) {
					sendbitmap_Parse(uri, sImagePath);
				}
			}
			return null;
		}
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	private class RotateImageTask extends AsyncTask<Void, Void, Void> {

		String imagePath;
		String path = null;

		public RotateImageTask(String imagePath) {
			// TODO Auto-generated constructor stub
			this.imagePath = imagePath;
		}

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(EditProfilePictureActivity.this);
			progressDialog.setMessage("Loading image...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		protected void onPostExecute(Void Result) {

			imageloader.displayImage("file://" + path, ivProfilePicture,
					options);

			if (progressDialog != null)
				progressDialog.dismiss();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			path = setImageInPotraitMode(imagePath);
			sImagePath = path;

			return null;
		}
	}

}
