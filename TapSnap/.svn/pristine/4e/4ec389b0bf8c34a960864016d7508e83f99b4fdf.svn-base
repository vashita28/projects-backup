package com.coudriet.snapnshare.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.mobileapptracker.MobileAppTracker;
import com.parse.ParseAnalytics;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.snapshare.utility.AppStatus;
import com.snapshare.utility.Common;
import com.coudriet.snapnshare.adapters.SectionsPagerAdapter;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	public static final String TAG = MainActivity.class.getSimpleName();

	public static final int TAKE_PHOTO_REQUEST = 0;
	public static final int TAKE_VIDEO_REQUEST = 1;
	public static final int PICK_PHOTO_REQUEST = 2;
	public static final int PICK_VIDEO_REQUEST = 3;

	public static final int MEDIA_TYPE_IMAGE = 4;
	public static final int MEDIA_TYPE_VIDEO = 5;

	public static final int FILE_SIZE_LIMIT = 1024 * 1024 * 10; // 10 MB

	protected Uri mMediaUri;

	private String name;
	private ActionBar actionBar;
	private ParseUser currentUser;

	public MobileAppTracker mobileAppTracker = null;
	public static final String ADVERTISER_ID = "1511";
	public static final String CONVERSION_KEY = "35f8b4730fdaf4ddda89f76ab5903b10";

	protected DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {

			case 0: // Take picture
				Intent takePhotoIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				// mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
				// mediaRecorder.setVideoEncodingBitRate(690000 );

				mMediaUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				if (mMediaUri == null) {
					// display an error
					Toast.makeText(MainActivity.this,
							R.string.error_external_storage, Toast.LENGTH_LONG)
							.show();
				} else {
					takePhotoIntent
							.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
					startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
				}

				break;

			case 1: // Take video

				Intent intent = new Intent(MainActivity.this,
						VideoViewActivity.class);
				startActivity(intent);

				/*
				 * Intent videoIntent = new
				 * Intent(MediaStore.ACTION_VIDEO_CAPTURE); mMediaUri =
				 * getOutputMediaFileUri(MEDIA_TYPE_VIDEO); if (mMediaUri ==
				 * null) { // display an error Toast.makeText(MainActivity.this,
				 * R.string.error_external_storage, Toast.LENGTH_LONG).show(); }
				 * else { videoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				 * mMediaUri);
				 * videoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
				 * videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // 0
				 * = lowest res startActivityForResult(videoIntent,
				 * TAKE_VIDEO_REQUEST); }
				 */

				break;

			case 2: // Choose picture
				Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
				choosePhotoIntent.setType("image/*");
				startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);

				break;

			case 3: // Choose video
				Intent chooseVideoIntent = new Intent(Intent.ACTION_GET_CONTENT);
				chooseVideoIntent.setType("video/*");
				Toast.makeText(MainActivity.this,
						R.string.video_file_size_warning, Toast.LENGTH_LONG)
						.show();
				startActivityForResult(chooseVideoIntent, PICK_VIDEO_REQUEST);

				break;
			}
		}

		private Uri getOutputMediaFileUri(int mediaType) {
			// To be safe, you should check that the SDCard is mounted
			// using Environment.getExternalStorageState() before doing this.
			if (isExternalStorageAvailable()) {
				// get the URI

				// 1. Get the external storage directory
				String appName = MainActivity.this
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

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	static CustomViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);

//		// Initialize MAT
//		MobileAppTracker.init(getApplicationContext(), ADVERTISER_ID,
//				CONVERSION_KEY);
//		mobileAppTracker = MobileAppTracker.getInstance();
//		mobileAppTracker.setReferralSources(this);
//
//		boolean isExistingUser = true;
//		if (isExistingUser) {
//			mobileAppTracker.setExistingUser(true);
//		}
//
//		// Collect Google Play Advertising ID
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				// See sample code at
//				// http://developer.android.com/google/play-services/id.html
//				try {
//					Info adInfo = AdvertisingIdClient
//							.getAdvertisingIdInfo(getApplicationContext());
//					mobileAppTracker.setGoogleAdvertisingId(adInfo.getId(),
//							adInfo.isLimitAdTrackingEnabled());
//				} catch (IOException e) {
//					// Unrecoverable error connecting to Google Play services
//					// (e.g.,
//					// the old version of the service doesn't support getting
//					// AdvertisingId).
//				} catch (GooglePlayServicesNotAvailableException e) {
//					// Google Play services is not available entirely.
//				} catch (GooglePlayServicesRepairableException e) {
//					// Encountered a recoverable error connecting to Google Play
//					// services.
//				}
//			}
//		}).start();

		if (AppStatus.getInstance(MainActivity.this).isOnline()) {

			// Fetch Facebook user info if the session is active
			// Session session = ParseFacebookUtils.getSession();
			// if (session != null && session.isOpened()) {
			// makeMeRequest();
			// }

			currentUser = ParseUser.getCurrentUser();

		} else {

			Toast.makeText(MainActivity.this, R.string.connectivity_error,
					Toast.LENGTH_SHORT).show();
		}

		// Set up the action bar.
		actionBar = getActionBar();
		actionBar.setIcon(getResources().getDrawable(R.drawable.statusbarlogo));
		actionBar.setDisplayShowTitleEnabled(false);

		ColorDrawable colorDrawable = new ColorDrawable(
				Color.parseColor("#507fb6"));
		actionBar.setBackgroundDrawable(colorDrawable);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (currentUser == null) {
					navigateToLogin();
				} else {
					Log.i(TAG, currentUser.getUsername());

					String appName = MainActivity.this
							.getString(R.string.app_name);
					// actionBar.setTitle(appName);
					// actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.statusbarlogo));

					// actionBar.setSubtitle("Logged in as: " +
					// currentUser.getUsername());
					// actionBar.setSubtitle("Logged in as: " + name);

					ParseAnalytics.trackAppOpened(getIntent());
					ParseInstallation.getCurrentInstallation()
							.saveInBackground();
					// PushService.subscribe(getApplicationContext(),
					// currentUser.getUsername(), MainActivity.class);

					// Create the adapter that will return a fragment for each
					// of the three
					// primary sections of the app.
					mSectionsPagerAdapter = new SectionsPagerAdapter(
							MainActivity.this, getSupportFragmentManager());

					// Set up the ViewPager with the sections adapter.
					mViewPager = (CustomViewPager) findViewById(R.id.pager);
					mViewPager.setAdapter(mSectionsPagerAdapter);

					// When swiping between different sections, select the
					// corresponding
					// tab. We can also use ActionBar.Tab#select() to do this if
					// we have
					// a reference to the Tab.
					mViewPager
							.setOnPageChangeListener(new CustomViewPager.SimpleOnPageChangeListener() {
								@Override
								public void onPageSelected(int position) {
									actionBar
											.setSelectedNavigationItem(position);

									mViewPager.getAdapter()
											.notifyDataSetChanged();

									mViewPager.setPagingEnabled(true);
								}
							});

					// For each of the sections in the app, add a tab to the
					// action bar.
					for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
						// Create a tab with text corresponding to the page
						// title defined by
						// the adapter. Also specify this Activity object, which
						// implements
						// the TabListener interface, as the callback (listener)
						// for when
						// this tab is selected.
						actionBar.addTab(actionBar.newTab()
								.setText(mSectionsPagerAdapter.getPageTitle(i))
								.setTabListener(MainActivity.this));
					}
				}
			}
		});

	}

	//@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//
//		// MAT will not function unless the measureSession call is included
//		mobileAppTracker.measureSession();
//
//		// If you choose to measure revenue for your paid installs, do something
//		// like the following instead.
//		// boolean isFirstAppSession = ...
//		// if (isFirstAppSession) {
//		// mobileAppTracker.measureAction("session", 2.99, "USD");
//		// } else {
//		// mobileAppTracker.measureSession();
//		// }
//	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(this, MainApplicationStartup.FLURRY_API_KEY);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	// parseFacebookUtils for fetching user related information
	private void makeMeRequest() {

		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {

						if (user != null) {

							try {

								name = user.getName();

								actionBar.setSubtitle("Logged in as: " + name);

							} catch (Exception e) {
								Log.d("E", "Error parsing returned user data."
										+ e.toString());
							}

						} else if (response.getError() != null) {
							// handle error
							Log.e("errorrrr", response.toString());
						}

					}
				});
		request.executeAsync();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			if (requestCode == PICK_PHOTO_REQUEST
					|| requestCode == PICK_VIDEO_REQUEST) {

				if (data == null) {
					Toast.makeText(this, getString(R.string.general_error),
							Toast.LENGTH_LONG).show();
				} else {
					mMediaUri = data.getData();
				}

				Log.i(TAG, "Media URI: " + mMediaUri);
				if (requestCode == PICK_VIDEO_REQUEST) {
					// make sure the file is less than 10 MB
					int fileSize = 0;
					InputStream inputStream = null;

					try {
						inputStream = getContentResolver().openInputStream(
								mMediaUri);
						fileSize = inputStream.available();
					} catch (FileNotFoundException e) {
						Toast.makeText(this, R.string.error_opening_file,
								Toast.LENGTH_LONG).show();
						return;
					} catch (IOException e) {
						Toast.makeText(this, R.string.error_opening_file,
								Toast.LENGTH_LONG).show();
						return;
					} finally {
						try {
							inputStream.close();
						} catch (IOException e) { /* Intentionally blank */
						}
					}

					if (fileSize >= FILE_SIZE_LIMIT) {
						Toast.makeText(this,
								R.string.error_file_size_too_large,
								Toast.LENGTH_LONG).show();
						return;
					}
				}
			} else {
				Intent mediaScanIntent = new Intent(
						Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				mediaScanIntent.setData(mMediaUri);
				sendBroadcast(mediaScanIntent);
			}

			Intent recipientsIntent = new Intent(this, RecipientsActivity.class);
			recipientsIntent.setData(mMediaUri);

			String fileType;
			if (requestCode == PICK_PHOTO_REQUEST
					|| requestCode == TAKE_PHOTO_REQUEST) {
				fileType = ParseConstants.TYPE_IMAGE;
			} else {
				fileType = ParseConstants.TYPE_VIDEO;
			}

			Common.flag = false;

			recipientsIntent.putExtra(ParseConstants.KEY_FILE_TYPE, fileType);
			startActivity(recipientsIntent);
		} else if (resultCode != RESULT_CANCELED) {
			Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG)
					.show();
		}
	}

	private void navigateToLogin() {
		Intent intent = new Intent(this, ActivityLogIn.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();

		switch (itemId) {

		case R.id.action_logout:

			// final ParseUser currentUser = ParseUser.getCurrentUser();
			// String requestMessage = currentUser.getUsername() + ", " +
			String requestMessage = "Are you sure you want to log out? Please note you will not be notified of any new snaps or friend requests until after you log in again.";

			AlertDialog.Builder builder1 = new AlertDialog.Builder(
					MainActivity.this);
			builder1.setMessage(requestMessage)
					.setTitle("Log Out?")
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog1,
										int id) {

									dialog1.cancel();
								}
							})
					.setPositiveButton("Log Out",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog1,
										int id) {

									boolean isFblogin;
									SharedPreferences sharedpreferences = getSharedPreferences(
											SignUpActivity.MyPREFERENCES,
											Context.MODE_PRIVATE);
									isFblogin = sharedpreferences
											.getBoolean(
													ParseConstants.KEY_LOGIN_VIA_FACEBOOK,
													false);
									if (isFblogin) {
										Editor editor = sharedpreferences
												.edit();
										isFblogin = false;
										editor.clear();
										editor.commit();
									}

									if (AppStatus
											.getInstance(MainActivity.this)
											.isOnline()) {

										PushService.unsubscribe(
												getApplicationContext(),
												currentUser.getUsername());

										// logout facebook
										Common.callFacebookLogout(MainActivity.this);
										// logout parse
										ParseUser.logOut();
										// finish();

										final Handler handler = new Handler();
										handler.postDelayed(new Runnable() {
											@Override
											public void run() {
												navigateToLogin();
											}

										}, 1000);

									} else {

										Toast.makeText(MainActivity.this,
												R.string.connectivity_error,
												Toast.LENGTH_SHORT).show();
									}
								}
							});

			AlertDialog dialog1 = builder1.create();
			dialog1.show();

			break;

		case R.id.action_edit_friends:
			Intent intent = new Intent(this, EditFriendsActivity.class);
			startActivity(intent);
			break;
		case R.id.action_camera:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setItems(R.array.camera_choices, mDialogListener);
			AlertDialog dialog = builder.create();
			dialog.show();
			break;
		case R.id.action_settings:
			Intent intentSettings = new Intent(this, SettingsActivity.class);
			startActivity(intentSettings);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onBackPressed() {

		// if (Common.flagBackPress) {
		// super.onBackPressed();
		// } else {
		// Toast.makeText(MainActivity.this, "Please wait for a moment!",
		// Toast.LENGTH_SHORT).show();
		// }

		// TODO Auto-generated method stub
		AlertDialog quitAlertDialog = new AlertDialog.Builder(MainActivity.this)
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
}
