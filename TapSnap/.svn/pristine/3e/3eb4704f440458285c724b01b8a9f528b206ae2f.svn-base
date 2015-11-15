package com.coudriet.tapsnap.android;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coudriet.tapsnap.utility.AppStatus;
import com.coudriet.tapsnap.utility.Common;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity implements OnClickListener {

	public static final String TAG = SignUpActivity.class.getSimpleName();

	protected EditText mUsername;
	protected EditText mPassword;
	protected EditText mEmail;
	protected EditText mBirthdayField;
	// protected TextView mCreateAccountTextView;
	protected RelativeLayout rlRegister;
	protected TextView mRegisterTextView;
	protected ImageView ivBackBtn;

	private ProgressDialog progressDialog;

	private Button btnfb;
	DisplayImageOptions options;
	private ImageView ibCalender;
	private Calendar cal;
	ImageLoader imageloader;
	private int day;
	private int month;
	private int year;
	private Editor editor;
	private ActionBar actionBar;

	private ImageView ivProfilePicture;
	protected Uri mMediaUri;
	public static final int TAKE_PHOTO_REQUEST = 0;
	public static final int PICK_PHOTO_REQUEST = 1;
	public static final int MEDIA_TYPE_IMAGE = 4;
	public static final int MEDIA_TYPE_VIDEO = 5;
	File destination;
	String imagePath;
	ParseFile fileImgGallery;
	String currImageURI;
	boolean isSaved = false;

	protected ParseUser mCurrentUser;
	public final JSONObject userProfile = new JSONObject();

	private ProfilePictureView profilePictureView;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	String BirthdateFormatted = "";

	public static SharedPreferences sharedpreferences;
	public final static String MyPREFERENCES = "MyPrefs";
	public static final String IS_PASSWORD_SET = "IsPasswordSet";
	public static final String PASSWORD = "password";
	public static boolean isbPasswordSet = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_sign_up);

		imageloader = ImageLoader.getInstance();

		setProgressBarIndeterminateVisibility(false);
		actionBar = getActionBar();
		actionBar.hide();

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();

		mUsername = (EditText) findViewById(R.id.usernameField);
		mPassword = (EditText) findViewById(R.id.passwordField);
		mEmail = (EditText) findViewById(R.id.emailField);
		mBirthdayField = (EditText) findViewById(R.id.birthdayField);
		ivBackBtn = (ImageView) findViewById(R.id.ivBackBtn);
		ibCalender = (ImageView) findViewById(R.id.imageButton1);
		ivProfilePicture = (ImageView) findViewById(R.id.ivSelectionProfilePic);
		cal = Calendar.getInstance();
		day = cal.get(Calendar.DAY_OF_MONTH);
		month = cal.get(Calendar.MONTH);
		year = cal.get(Calendar.YEAR);
		ibCalender.setOnClickListener(this);

		mBirthdayField.setEnabled(false);

		mUsername.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		mPassword.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		mEmail.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		mBirthdayField
				.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);

		profilePictureView = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
		// profilePictureView.setCropped(true);
		profilePictureView.setDrawingCacheEnabled(true);

		// mUsername.setHintTextColor(getResources().getColor(
		// R.color.edittext_hintcolor));
		// mPassword.setHintTextColor(getResources().getColor(
		// R.color.edittext_hintcolor));
		// mEmail.setHintTextColor(getResources().getColor(
		// R.color.edittext_hintcolor));
		// mBirthdayField.setHintTextColor(getResources().getColor(
		// R.color.edittext_hintcolor));

		ivBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ivProfilePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SignUpActivity.this);
				builder.setItems(R.array.camera_choices_signup, mDialogListener);
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		mRegisterTextView = (TextView) findViewById(R.id.RegisterTextView);
		mRegisterTextView
				.setTypeface(MainApplicationStartup.font_HelveticaLTStdBold);
		rlRegister = (RelativeLayout) findViewById(R.id.rlRegister);
		// mCreateAccountTextView = (TextView)
		// findViewById(R.id.signUpTextView);
		rlRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Editor editor = sharedpreferences.edit();
				editor.putBoolean(ParseConstants.KEY_LOGIN_VIA_FACEBOOK, false);
				editor.commit();

				SignUpTask signUpTask = new SignUpTask();
				signUpTask.execute();

			}
		});
	}

	protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {

			case 0: // Take picture
				Intent takePhotoIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				if (mMediaUri == null) {
					// display an error
					Toast.makeText(SignUpActivity.this,
							R.string.error_external_storage, Toast.LENGTH_LONG)
							.show();
				} else {

					takePhotoIntent
					// .putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri)
							.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(getImageFileName()));
					startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
				}

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

		private Uri getOutputMediaFileUri(int mediaType) {
			// To be safe, you should check that the SDCard is mounted
			// using Environment.getExternalStorageState() before doing this.
			if (isExternalStorageAvailable()) {
				// get the URI

				// 1. Get the external storage directory
				String appName = SignUpActivity.this
						.getString(R.string.app_name_alt);
				File mediaStorageDir = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						appName);

				// 2. Create our subdirectory
				if (!mediaStorageDir.exists()) {
					if (!mediaStorageDir.mkdirs()) {
						Log.e(TAG, "Failed to create directory.");
						return null;
					}
				}

				// 3. Create a file name
				// 4. Create the file
				File mediaFile;
				Date now = new Date();

				String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
						Locale.US).format(now);

				String path = mediaStorageDir.getPath() + File.separator;

				if (mediaType == MEDIA_TYPE_IMAGE) {
					mediaFile = new File(path + "IMG_" + timestamp + ".jpg");
				} else if (mediaType == MEDIA_TYPE_VIDEO) {
					mediaFile = new File(path + "VID_" + timestamp + ".3gp");
				} else {
					return null;
				}

				Log.d(TAG, "File: " + Uri.fromFile(mediaFile));

				// 5. Return the file's URI
				return Uri.fromFile(mediaFile);
			} else {
				return null;
			}
		}

		private boolean isExternalStorageAvailable() {
			String state = Environment.getExternalStorageState();

			if (state.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			} else {
				return false;
			}
		}
	};

	@Override
	public void onClick(View v) {
		showDialog(0);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this, datePickerListener, year, month, day) {
			@Override
			public void onDateChanged(DatePicker view, int yearSelected,
					int monthOfYear, int dayOfMonth) {
				if (yearSelected > year)
					view.updateDate(year, month, day);

				if (monthOfYear > month && yearSelected == year)
					view.updateDate(year, month, day);

				if (dayOfMonth > day && yearSelected == year
						&& monthOfYear == month)
					view.updateDate(year, month, day);

			}
		};

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			mBirthdayField.setText(selectedDay + " / " + (selectedMonth + 1)
					+ " / " + selectedYear);
			int month = selectedMonth + 1;
			// mSelectedYear = selectedYear;
			String monthName = "";
			if (month == 1) {
				monthName = "Jan";
			} else if (month == 2) {
				monthName = "Feb";
			} else if (month == 3) {
				monthName = "Mar";
			} else if (month == 4) {
				monthName = "Apr";
			} else if (month == 5) {
				monthName = "May";
			} else if (month == 6) {
				monthName = "Jun";
			} else if (month == 7) {
				monthName = "Jul";
			} else if (month == 8) {
				monthName = "Aug";
			} else if (month == 9) {
				monthName = "Sep";
			} else if (month == 10) {
				monthName = "Oct";
			} else if (month == 11) {
				monthName = "Nov";
			} else if (month == 12) {
				monthName = "Dec";
			}

			BirthdateFormatted = monthName + " " + selectedDay + " , "
					+ selectedYear;
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		Log.e(TAG, "onDestroy ");
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		if (progressDialog != null)
			progressDialog.dismiss();

	}

	// callin parseFacebookUtils

	private class SignUpTask extends AsyncTask<Void, Void, Void> {

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(SignUpActivity.this);
			progressDialog.setMessage("Registering...");
			// progressDialog.setCancelable(true);
			progressDialog.show();
		}

		protected void onPostExecute(Void Result) {
			// if (dialog != null)
			// dialog.dismiss();

			String username = mUsername.getText().toString();
			String password = mPassword.getText().toString();
			String email = mEmail.getText().toString();
			String birthday = mBirthdayField.getText().toString().trim();

			username = username.trim();
			password = password.trim();
			email = email.trim();

			Pattern pattern = Pattern.compile("\\s");
			Matcher matcher = pattern.matcher(username);
			boolean found = matcher.find();

			if (username.isEmpty() && password.isEmpty() && email.isEmpty()
					&& birthday.isEmpty()) {
				if (progressDialog != null)
					progressDialog.dismiss();

				String msg = getString(R.string.signup_error_message);
				String tit = getString(R.string.signup_error_title);

				Common.callDialog(SignUpActivity.this, msg, tit);

			} else if (username.isEmpty()) {
				if (progressDialog != null)
					progressDialog.dismiss();

				String msg = getString(R.string.login_error_message_for_user);
				String tit = getString(R.string.signup_error_title);

				Common.callDialog(SignUpActivity.this, msg, tit);

			} else if (username.length() < 1 || username.length() > 17) {
				if (progressDialog != null)
					progressDialog.dismiss();

				String msg = getString(R.string.signup_error_message_for_user_length);
				String tit = getString(R.string.signup_error_title);

				Common.callDialog(SignUpActivity.this, msg, tit);

			} else if (password.isEmpty()) {
				if (progressDialog != null)
					progressDialog.dismiss();
				String msg = getString(R.string.login_error_message_for_password);
				String tit = getString(R.string.signup_error_title);

				Common.callDialog(SignUpActivity.this, msg, tit);

			} else if (password.length() < 6 || password.length() > 17) {
				if (progressDialog != null)
					progressDialog.dismiss();

				String msg = getString(R.string.signup_error_message_for_password_length);
				String tit = getString(R.string.signup_error_title);

				Common.callDialog(SignUpActivity.this, msg, tit);
			}

			else if (email.isEmpty()) {
				if (progressDialog != null)
					progressDialog.dismiss();
				String msg = getString(R.string.signup_error_message_email);
				String tit = getString(R.string.signup_error_title);

				Common.callDialog(SignUpActivity.this, msg, tit);

			} else if (username.startsWith("1") || username.startsWith("2")
					|| username.startsWith("3") || username.startsWith("4")
					|| username.startsWith("5") || username.startsWith("6")
					|| username.startsWith("7") || username.startsWith("8")
					|| username.startsWith("9") || username.startsWith("0")) {

				if (progressDialog != null)
					progressDialog.dismiss();

				AlertDialog.Builder builder = new AlertDialog.Builder(
						SignUpActivity.this);
				builder.setMessage(
						"The username must not start with a number! Please try again by entering a username starting with a letter.")
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
				AlertDialog dialog = builder.create();
				dialog.show();
			} else if (found) {
				if (progressDialog != null)
					progressDialog.dismiss();

				AlertDialog.Builder builder = new AlertDialog.Builder(
						SignUpActivity.this);
				builder.setMessage(
						"The username must not contain any spaces! Please try again by entering a username without spaces.")
						.setTitle(R.string.signup_error_title)
						.setPositiveButton(android.R.string.ok, null);
				AlertDialog dialog = builder.create();
				dialog.show();
			} else if (!email.matches(EMAIL_PATTERN)) {
				if (progressDialog != null)
					progressDialog.dismiss();

				AlertDialog quitAlertDialog = new AlertDialog.Builder(
						SignUpActivity.this)
						.setTitle(R.string.signup_error_title)
						.setMessage(R.string.signup_error_message_valid_email)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface argDialog,
											int argWhich) {
										mEmail.setText("");
									}
								}).create();
				quitAlertDialog.setCanceledOnTouchOutside(false);
				quitAlertDialog.show();
				// String msg =
				// getString(R.string.signup_error_message_valid_email);
				// String tit = getString(R.string.signup_error_title);
				//
				// Common.callDialog(SignUpActivity.this, msg, tit);
			} else if (birthday.isEmpty()) {
				if (progressDialog != null)
					progressDialog.dismiss();
				String msg = getString(R.string.signup_error_message_birthday);
				String tit = getString(R.string.signup_error_title);

				Common.callDialog(SignUpActivity.this, msg, tit);

			} else if (ivProfilePicture.getDrawable() == null) {
				if (progressDialog != null)
					progressDialog.dismiss();
				String msg = getString(R.string.signup_error_message_profile_picture);
				String tit = getString(R.string.signup_error_title);

				Common.callDialog(SignUpActivity.this, msg, tit);

			} else {

				if (AppStatus.getInstance(SignUpActivity.this).isOnline()) {

					// create the new user!
					setProgressBarIndeterminateVisibility(true);

					ParseUser newUser = new ParseUser();
					newUser.setUsername(username);
					newUser.setPassword(password);
					newUser.setEmail(email);
					newUser.put(ParseConstants.KEY_BIRTHDAY, BirthdateFormatted);
					if (fileImgGallery != null) {
						newUser.put(ParseConstants.KEY_PROFILE_PICTURE,
								fileImgGallery);
					}

					newUser.signUpInBackground(new SignUpCallback() {
						@Override
						public void done(ParseException e) {
							setProgressBarIndeterminateVisibility(false);

							if (e == null) {
								// Success!
								Intent intent = new Intent(SignUpActivity.this,
										MainActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								startActivity(intent);
							} else {
								Log.v(TAG, "Error: " + e.toString());
								/*
								 * AlertDialog.Builder builder = new
								 * AlertDialog.Builder(SignUpActivity.this);
								 * builder.setMessage(R.string.user_already)
								 * .setTitle(R.string.signup_error_title)
								 * .setPositiveButton(android.R.string.ok,
								 * null); AlertDialog dialog = builder.create();
								 * dialog.show();
								 */
								if (progressDialog != null)
									progressDialog.dismiss();
								String msg, tit;
								if (e.toString().toLowerCase()
										.contains("username")) {
									msg = "Username already exist!";
									tit = getString(R.string.signup_error_title);

								} else if (e.toString().toLowerCase()
										.contains("email")) {
									msg = "Email already exist!";
									tit = getString(R.string.signup_error_title);
								} else {
									msg = "Error in connection!";
									tit = getString(R.string.signup_error_title);
								}
								Common.callDialog(SignUpActivity.this, msg, tit);

							}
						}
					});

				} else {
					if (progressDialog != null)
						progressDialog.dismiss();

					String msg = getString(R.string.connectivity_error);
					String tit = getString(R.string.signup_error_title);

					Common.callDialog(SignUpActivity.this, msg, tit);

				}
			}

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			return null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		if (resultCode == RESULT_OK) {
			if (requestCode == TAKE_PHOTO_REQUEST) {
				// camera picture
				String imagePath = destination.getPath();
				Log.i("****Camera path******", "" + imagePath);
				setimage(imagePath);

			} else if (requestCode == PICK_PHOTO_REQUEST) {
				// gallery
				Uri selectedImageUri = intent.getData();
				String galleryimagepath = getPath(selectedImageUri);
				Log.i("**********ImagePath*******", "" + galleryimagepath);
				setimage(galleryimagepath);

			}
		} else if (resultCode != RESULT_CANCELED) {
			Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG)
					.show();
		}
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
			imageloader.displayImage("file://" + imagepath, ivProfilePicture,
					options);// new ImageLoadListener()
			// sendbitmap_parse(imagepath);

			Uri uri = Uri.parse("file://" + imagepath);
			sendbitmap_Parse(uri, imagepath);

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
				currImageURI = imagepath;

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
								isSaved = true;
							} else {
								// Failed
								Log.e(TAG, "saved   false");
								isSaved = false;
							}
						}
					});
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

	public String dateToString(Date date, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	private File getImageFileName() {
		String name = dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
		return destination = new File(
				Environment.getExternalStorageDirectory(), name + ".jpg");
	}

	private class ImageLoadListener extends SimpleImageLoadingListener {

		ImageLoadListener() {

		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			// if (loadedImage != null) {
			// ImageView imageView = (ImageView) view;
			// imageView.setImageBitmap(loadedImage);
			// mapImages.put(imageUri, loadedImage);
			// }
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// mapImages.put(imageUri, null);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// mapImages.put(imageUri, null);
		}
	}

	private void makeMeRequest() {

		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(final GraphUser user,
							Response response) {
						if (user != null) {
							AsyncTask<Void, Void, Bitmap> t = new AsyncTask<Void, Void, Bitmap>() {
								protected Bitmap doInBackground(Void... p) {
									Bitmap bm = null;
									try {
										URL aURL = new URL(
												"http://graph.facebook.com/"
														+ user.getId()
														+ "/picture?type=large");
										Log.d("Profile Picture",
												"http://graph.facebook.com/"
														+ user.getId()
														+ "/picture?type=large");
										URLConnection conn = aURL
												.openConnection();
										conn.setUseCaches(true);
										conn.connect();
										InputStream is = conn.getInputStream();
										BufferedInputStream bis = new BufferedInputStream(
												is);
										bm = BitmapFactory.decodeStream(bis);
										bis.close();
										is.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
									return bm;
								}

								protected void onPostExecute(Bitmap bm) {

									Log.d("onPostExecute", "bm " + bm);
									// Drawable drawable = new
									// BitmapDrawable(getResources(), bm);

									ivProfilePicture.setImageBitmap(bm);

								}
							};
							t.execute();

						} else if (response.getError() != null) {
							// handle error
						}
					}
				});
		request.executeAsync();

	}

}
