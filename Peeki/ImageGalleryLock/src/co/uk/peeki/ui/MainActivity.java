package co.uk.peeki.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.peeki.Photo_Application;
import co.uk.peeki.R;
import co.uk.peeki.adapter.ImageAdapterForBlocked;
import co.uk.peeki.adapter.ImageAdapterForSelection;
import co.uk.peeki.adapter.ListAdapter;
import co.uk.peeki.db.MySqLiteHelper;
import co.uk.peeki.fragments.BlockImagesFragment;
import co.uk.peeki.fragments.ChangePasscodeFragment;
import co.uk.peeki.fragments.SelectionFragment;
import co.uk.peeki.fragments.ShareFragment;
import co.uk.peeki.fragments.TermsAndConditionsFragment;
import co.uk.peeki.fragments.ViewTutorialFragment;
import co.uk.peeki.interfaces.FragmentLoad;

import com.flurry.android.FlurryAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

public class MainActivity extends FragmentActivity implements
		View.OnClickListener, FragmentLoad {

	private static final String TAG = "MAIN ACTIVITY";

	DrawerLayout drawer;
	ListView navList;
	// Button btnMenu;// btnBack
	ImageView imageViewBlock, imageViewPlay;
	RelativeLayout rlBodySliding, rlFragmentBody, rlMenuIcon, rlBackIcon;
	TextView tvNoSelectedImages, tvDoneButton;
	MySqLiteHelper db;

	RelativeLayout rel_header;

	private String[] navMenuTitles;
	// final String[] fragments = {
	// "com.example.imagegallerylock.fragments.ShowSelectionFragment",
	// "com.example.imagegallerylock.fragments.BlockImagesFragment",
	// "com.example.imagegallerylock.fragments.ViewTutorialFragment",
	// "com.example.imagegallerylock.fragments.TermsAndConditionsFragment",
	// "com.example.imagegallerylock.fragments.ShareFragment",
	// "com.example.imagegallerylock.fragments.ChangePasscodeFragment" };

	public static FragmentLoad mfragmentLoad;

	Fragment mFragment = null;
	Fragment mPreviousFragment = null;

	public static ProgressDialog pDialog;

	public static Activity mActivity;
	public static Context mContext;
	InputMethodManager inputMethodManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mfragmentLoad = this;
		mActivity = this;

		rel_header = (RelativeLayout) findViewById(R.id.rl_header);
		rlMenuIcon = (RelativeLayout) rel_header.findViewById(R.id.rlMenuIcon);
		rlBackIcon = (RelativeLayout) rel_header.findViewById(R.id.rlBackIcon);

		imageViewBlock = (ImageView) rel_header.findViewById(R.id.ivBlocked);
		rlBodySliding = (RelativeLayout) findViewById(R.id.rl_bodySliding);
		rlFragmentBody = (RelativeLayout) findViewById(R.id.rl_fragmentBody);
		tvNoSelectedImages = (TextView) rel_header
				.findViewById(R.id.tvNoSelectedImages);
		imageViewPlay = (ImageView) rel_header
				.findViewById(R.id.ivPlaySelection);
		tvNoSelectedImages.setVisibility(View.GONE);
		tvDoneButton = (TextView) rel_header.findViewById(R.id.tvDone);

		tvNoSelectedImages.setTypeface(Photo_Application.font_Regular);
		tvDoneButton.setTypeface(Photo_Application.font_SemiBold);

		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		mContext = this;

		db = new MySqLiteHelper(MainActivity.mContext);
		LoadFragment();

		imageViewBlock.setOnClickListener(this);
		imageViewPlay.setOnClickListener(this);
		tvDoneButton.setOnClickListener(this);
		rlBackIcon.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.e("MainActivity*********", "onStop**********");
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("MainActivity*********", "onDestroy**********");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ivBlocked:

			if (SelectionFragment.listOfSelectedImages != null) {

				Bundle bundle = new Bundle();
				ImageAdapterForBlocked.clearAllList = false;
				Fragment blockImagesFragment = BlockImagesFragment.newInstance(
						this, mfragmentLoad);
				bundle.putStringArrayList("selectedList",
						SelectionFragment.listOfSelectedImages);
				blockImagesFragment.setArguments(bundle);

				mPreviousFragment = blockImagesFragment;
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.main_fragment, blockImagesFragment,
								"BlockFragment")
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.addToBackStack(null).commit();

			}
			break;
		case R.id.ivPlaySelection:
			ArrayList<String> myList = new ArrayList<String>();
			ArrayList<String> unblockedList = new ArrayList<String>();
			unblockedList = db.getAllUnblockedPhotos();
			if (SelectionFragment.listOfSelectedImages.size() > 0) {
				myList.addAll(SelectionFragment.listOfSelectedImages);
			} else if (unblockedList.size() > 0) {
				Log.v(TAG, "unblockedList " + unblockedList.size());
				myList.addAll(unblockedList);
			}
			Intent intent = new Intent(MainActivity.this,
					ShowSelectionActivity.class);
			intent.putExtra("mylist", myList);
			startActivity(intent);

			break;
		case R.id.tvDone:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Blocking/Unblocking photos...");
			pDialog.setCancelable(false);
			pDialog.show();

			BlockImagesTask blockImagesTask = new BlockImagesTask(pDialog);
			blockImagesTask.fragmentLoad = mfragmentLoad;
			blockImagesTask.execute();

			break;
		case R.id.rlBackIcon:
			Handler handler = new Handler();
			Runnable r = new Runnable() {
				public void run() {
					hideAllButtons();
					try {
						FragmentManager fm = getSupportFragmentManager();
						fm.popBackStack("BlockFragment",
								FragmentManager.POP_BACK_STACK_INCLUSIVE);

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					Fragment selectionFragmentBack = SelectionFragment
							.newInstance(MainActivity.this, mfragmentLoad);
					mPreviousFragment = selectionFragmentBack;
					getSupportFragmentManager()
							.beginTransaction()
							.replace(R.id.main_fragment, selectionFragmentBack,
									"SelectionFragment")
							.setTransition(
									FragmentTransaction.TRANSIT_FRAGMENT_FADE)
							.addToBackStack(null).commit();

				}
			};
			handler.postDelayed(r, 400);

			break;

		default:
			Log.v(TAG, "count default ");
			break;
		}

	}

	void LoadFragment() {

		ListAdapter adapter = new ListAdapter(this, navMenuTitles);
		// drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		navList = (ListView) findViewById(R.id.listview_drawer);
		navList.setAdapter(adapter);

		boolean pauseOnScroll = true; // or true
		boolean pauseOnFling = true; // or false
		PauseOnScrollListener listener = new PauseOnScrollListener(
				ImageLoader.getInstance(), pauseOnScroll, pauseOnFling);
		navList.setOnScrollListener(listener);

		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				inputMethodManager.hideSoftInputFromWindow(
						rlMenuIcon.getWindowToken(), 0);
				tvDoneButton.setEnabled(false);
				imageViewBlock.setEnabled(false);
				imageViewPlay.setEnabled(false);
				mFragment = null;
			}

			@Override
			public void onDrawerClosed(View arg0) {
				// TODO Auto-generated method stub
				tvDoneButton.setEnabled(true);
				imageViewBlock.setEnabled(true);
				imageViewPlay.setEnabled(true);

				FragmentTransaction tx = getSupportFragmentManager()
						.beginTransaction();

				if (mFragment != null) {

					if (mFragment.getClass().equals(SelectionFragment.class)) {
						tx.replace(R.id.main_fragment, mFragment,
								"SelectionFragment");
						mPreviousFragment = mFragment;
					}

					else if (mFragment.getClass().equals(
							BlockImagesFragment.class)) {
						tx.replace(R.id.main_fragment, mFragment,
								"BlockFragment");
						mPreviousFragment = mFragment;
					} else
						tx.replace(R.id.main_fragment, mFragment);

					tx.addToBackStack(null).commit();
				}
			}
		});

		navList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int pos, long id) {
				switch (pos) {
				case 0:
					// imageViewBlock.setEnabled(true);
					// imageViewPlay.setEnabled(true);
					ArrayList<String> myList = new ArrayList<String>();
					ArrayList<String> unblockedList = new ArrayList<String>();
					unblockedList = db.getAllUnblockedPhotos();
					if (SelectionFragment.listOfSelectedImages.size() > 0) {
						myList.addAll(SelectionFragment.listOfSelectedImages);
					} else if (unblockedList.size() > 0) {
						myList.addAll(unblockedList);
					}
					Intent intent = new Intent(MainActivity.this,
							ShowSelectionActivity.class);
					intent.putExtra("mylist", myList);
					startActivity(intent);
					mFragment = SelectionFragment.newInstance(
							MainActivity.this, mfragmentLoad);
					break;
				case 1:
					tvDoneButton.setEnabled(true);
					mFragment = BlockImagesFragment.newInstance(
							MainActivity.this, mfragmentLoad);
					ImageAdapterForBlocked.clearAllList = false;
					break;
				case 2:
					mFragment = SelectionFragment.newInstance(
							MainActivity.this, mfragmentLoad);
					Intent tutorialIntent = new Intent(MainActivity.this,
							ViewTutorialActivity.class);
					startActivity(tutorialIntent);
					// mFragment = ViewTutorialFragment.newInstance(
					// MainActivity.this, mfragmentLoad);
					break;

				case 3:
					mFragment = TermsAndConditionsFragment.newInstance(
							MainActivity.this, mfragmentLoad);
					break;

				case 4:
					// mFragment = SelectionFragment.newInstance(
					// MainActivity.this, mfragmentLoad);
					mFragment = ShareFragment.newInstance(MainActivity.this,
							mfragmentLoad);
					break;

				case 5:
					mFragment = ChangePasscodeFragment.newInstance(
							MainActivity.this, mfragmentLoad);
					break;
				case 6:
					// Toast.makeText(mActivity, "Refreshing images..",
					// Toast.LENGTH_LONG).show();
					pDialog = new ProgressDialog(MainActivity.this);
					pDialog.setMessage("Refreshing photos...");
					pDialog.setCancelable(false);
					pDialog.show();
					RefreshImagesTask refreshImagesTask = new RefreshImagesTask(
							pDialog);
					refreshImagesTask.execute();

					break;
				default:
					break;
				}
				drawer.closeDrawer(navList);

			}
		});

		Fragment fragment = SelectionFragment.newInstance(MainActivity.this,
				mfragmentLoad);

		mPreviousFragment = fragment;
		FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
		tx.replace(R.id.main_fragment, fragment, "SelectionFragment");
		tx.addToBackStack(null);
		tx.commit();

		rlMenuIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// drawableLayout.setVisibility(View.VISIBLE);

				if (drawer.isDrawerOpen(navList)) {
					drawer.closeDrawer(navList);
				} else {
					inputMethodManager.hideSoftInputFromWindow(
							rlMenuIcon.getWindowToken(), 0);
					drawer.openDrawer(navList);
				}

			}
		});

	}

	@SuppressLint("NewApi")
	@Override
	public void onShowSelectionFragmentLoad(
			SelectionFragment showSelectionFragment,
			final int countSelectedImages, boolean bFlag) {
		// TODO Auto-generated method stub
		drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		// nPreviousPosition = 0;
		// nCurrentPosition = 0;
		navList.setItemChecked(0, true);

		if (bFlag) {

		} else {

			Handler handler = new Handler();
			Runnable r = new Runnable() {
				public void run() {
					// Log.e("MainActivity", "onShowSelectionFragment");
					hideAllButtons();
					rlMenuIcon.setVisibility(View.VISIBLE);
					imageViewBlock.setVisibility(View.VISIBLE);
					imageViewPlay.setVisibility(View.VISIBLE);
					// imageViewBlock.setEnabled(true);
					// imageViewPlay.setEnabled(true);
				}
			};
			handler.postDelayed(r, 800);
		}

		if (countSelectedImages == 0) {
			tvNoSelectedImages.setVisibility(View.GONE);
		} else {
			tvNoSelectedImages.setVisibility(View.VISIBLE);
		}
		if (countSelectedImages == 1)
			tvNoSelectedImages.setText(String.valueOf(countSelectedImages)
					+ " image selected");
		else
			tvNoSelectedImages.setText(String.valueOf(countSelectedImages)
					+ " images selected");

	}

	@Override
	public void onBlockImagesFragmentLoad(
			BlockImagesFragment blockImagesFragment,
			final int countblockedImages) {
		// TODO Auto-generated method stub
		drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		navList.setItemChecked(1, true);
		Handler handler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				hideAllButtons();
				rlBackIcon.setVisibility(View.VISIBLE);
				tvDoneButton.setVisibility(View.VISIBLE);
				tvDoneButton.setEnabled(true);
				tvNoSelectedImages.setVisibility(View.VISIBLE);

				if (countblockedImages == 0) {
					tvNoSelectedImages.setVisibility(View.GONE);
				} else {
					tvNoSelectedImages.setVisibility(View.VISIBLE);
				}
				if (countblockedImages == 1)
					tvNoSelectedImages.setText(String
							.valueOf(countblockedImages) + " image selected");
				else
					tvNoSelectedImages.setText(String
							.valueOf(countblockedImages) + " images selected");

			}
		};
		handler.postDelayed(r, 400);
	}

	@Override
	public void onViewTutorialFragmentLoad(
			ViewTutorialFragment viewTutorialFragment) {
		// TODO Auto-generated method stub
		navList.setItemChecked(2, true);
		drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		hideAllButtons();
		rlMenuIcon.setVisibility(View.VISIBLE);
	}

	@Override
	public void onTermsAndConditionsFragmentLoad(
			TermsAndConditionsFragment termsAndConditionsFragment) {
		// TODO Auto-generated method stub
		Handler handler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				navList.setItemChecked(3, true);
				drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				hideAllButtons();
				rlMenuIcon.setVisibility(View.VISIBLE);
			}
		};
		handler.postDelayed(r, 400);

		// navList.setItemChecked(3, true);
		// drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		// rlBackIcon.setVisibility(View.GONE);
		// rlMenuIcon.setVisibility(View.VISIBLE);
		// tvDoneButton.setVisibility(View.GONE);
		// tvNoSelectedImages.setVisibility(View.GONE);
		// imageViewBlock.setVisibility(View.GONE);
		// imageViewPlay.setVisibility(View.GONE);
	}

	@Override
	public void onShareFragmentLoad(ShareFragment shareFragment) {
		Handler handler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				navList.setItemChecked(4, true);
				drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				hideAllButtons();
				rlMenuIcon.setVisibility(View.VISIBLE);
			}
		};
		handler.postDelayed(r, 400);

		// // TODO Auto-generated method stub
		// navList.setItemChecked(4, true);
		// drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		// rlBackIcon.setVisibility(View.GONE);
		// rlMenuIcon.setVisibility(View.VISIBLE);
		// tvNoSelectedImages.setVisibility(View.GONE);
		// tvDoneButton.setVisibility(View.GONE);
		// imageViewBlock.setVisibility(View.GONE);
		// imageViewPlay.setVisibility(View.GONE);
	}

	@Override
	public void onChangePasscodeFragmentLoad(
			ChangePasscodeFragment changePasscodeFragment) {
		// TODO Auto-generated method stub
		Handler handler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				navList.setItemChecked(5, true);
				drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				hideAllButtons();
				rlMenuIcon.setVisibility(View.VISIBLE);
			}
		};
		handler.postDelayed(r, 400);
		// navList.setItemChecked(5, true);
		// drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		// rlBackIcon.setVisibility(View.GONE);
		// rlMenuIcon.setVisibility(View.VISIBLE);
		// tvNoSelectedImages.setVisibility(View.GONE);
		// tvDoneButton.setVisibility(View.GONE);
		// imageViewBlock.setVisibility(View.GONE);
		// imageViewPlay.setVisibility(View.GONE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, Photo_Application.FLURRY_API_KEY);
	}

	@Override
	public void onBackPressed() {
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

	private class BlockImagesTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;
		FragmentLoad fragmentLoad;

		public BlockImagesTask(ProgressDialog dialog) {
			// TODO Auto-generated constructor stub
			this.dialog = dialog;
		}

		protected void onPreExecute() {
		}

		protected void onPostExecute(Void Result) {
			if (dialog != null)
				dialog.dismiss();

			ImageAdapterForSelection.countSelectedImages = 0;
			// tvDoneButton.setVisibility(View.GONE);
			// imageViewBlock.setVisibility(View.VISIBLE);
			// imageViewPlay.setVisibility(View.VISIBLE);

			// Goes to selection screen
			SelectionFragment.listOfSelectedImages.clear();
			try {
				FragmentManager fm = getSupportFragmentManager();
				for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
					fm.popBackStack();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Fragment selectionFragment = SelectionFragment.newInstance(
						MainActivity.this, fragmentLoad);
				mPreviousFragment = selectionFragment;
				getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.main_fragment, selectionFragment,
								"SelectionFragment")
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.addToBackStack(null).commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			// Block images
			if (BlockImagesFragment.listOfBlockedImages.size() > 0) {
				for (int i = 0; i < BlockImagesFragment.listOfBlockedImages
						.size(); i++) {
					db.updatePhotos(BlockImagesFragment.listOfBlockedImages
							.get(i).toString(), true);
				}
			}
			// Unblock images
			if (BlockImagesFragment.listOfUnblockedImages.size() > 0) {
				for (int i = 0; i < BlockImagesFragment.listOfUnblockedImages
						.size(); i++) {
					db.updatePhotos(BlockImagesFragment.listOfUnblockedImages
							.get(i).toString(), false);
				}
			}
			return null;
		}
	}

	private class RefreshImagesTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;

		public RefreshImagesTask(ProgressDialog dialog) {
			// TODO Auto-generated constructor stub
			this.dialog = dialog;
		}

		protected void onPreExecute() {
			Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			String[] projection = { MediaStore.Images.ImageColumns.DATA };
			Cursor cursor = null;
			SortedSet<String> dirList = new TreeSet<String>();
			ArrayList<String> resultIAV = new ArrayList<String>();

			String[] directories = null;
			if (uri != null) {
				cursor = mActivity.managedQuery(uri, projection, null, null,
						null);
			}

			if ((cursor != null) && (cursor.moveToFirst())) {
				do {
					String tempDir = cursor.getString(0);
					tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
					try {
						dirList.add(tempDir);
					} catch (Exception e) {

					}
				} while (cursor.moveToNext());
				directories = new String[dirList.size()];
				dirList.toArray(directories);
			}

			for (int i = 0; i < dirList.size(); i++) {
				File imageDir = new File(directories[i]);
				File[] imageList = imageDir.listFiles();
				if (imageList == null)
					continue;
				for (File imagePath : imageList) {
					try {

						if (imagePath.isDirectory()) {
							imageList = imagePath.listFiles();

						}
						if (imagePath.getName().contains(".jpg")
								|| imagePath.getName().contains(".JPG")
								|| imagePath.getName().contains(".jpeg")
								|| imagePath.getName().contains(".JPEG")
								|| imagePath.getName().contains(".png")
								|| imagePath.getName().contains(".PNG")
								|| imagePath.getName().contains(".gif")
								|| imagePath.getName().contains(".GIF")
								|| imagePath.getName().contains(".bmp")
								|| imagePath.getName().contains(".BMP")) {

							String path = imagePath.getAbsolutePath();
							resultIAV.add(path);

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			SelectionFragment.listOfTotalImages.clear();
			SelectionFragment.listOfTotalImages.addAll(resultIAV);
			Log.v("RefreshImagesTask", "listOfTotalimages:: "
					+ SelectionFragment.listOfTotalImages.size());
		}

		protected void onPostExecute(Void Result) {

			// ArrayList<String> unblockedList = db.getAllUnblockedPhotos();
			//
			// Log.e(TAG, "SelectionFragment.listOfTotalImagesafter "
			// + SelectionFragment.listOfTotalImages.size()
			// + " unblockedList   " + unblockedList.size());
			if (dialog != null)
				dialog.dismiss();
			// ImageAdapterForSelection adapter = new ImageAdapterForSelection(
			// getApplicationContext(), null,
			// SelectionFragment.listOfTotalImages);
			// adapter.notifyDataSetChanged();

			if (mPreviousFragment != null
					&& mPreviousFragment.getClass().equals(
							SelectionFragment.class)) {

				SelectionFragment fragment = ((SelectionFragment) getSupportFragmentManager()
						.findFragmentByTag("SelectionFragment"));
				if (fragment != null)
					fragment.setDataInAdapter();
			}

			else if (mPreviousFragment != null
					&& mPreviousFragment.getClass().equals(
							BlockImagesFragment.class)) {

				BlockImagesFragment fragment = ((BlockImagesFragment) getSupportFragmentManager()
						.findFragmentByTag("BlockFragment"));
				if (fragment != null)
					fragment.setDataInAdapter();

			} else
				SelectionFragment.bIsPhotosLoaded = false;

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Log.e(TAG, "SelectionFragment.listOfTotalImagesbefore "
					+ SelectionFragment.listOfTotalImages.size());
			if (SelectionFragment.listOfTotalImages.size() > 0) {
				// Addition:
				for (int i = 0; i < SelectionFragment.listOfTotalImages.size(); i++) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					File file = new File(
							SelectionFragment.listOfTotalImages.get(i));
					String currentTime = sdf.format(file.lastModified());
					db.addPhoto(new DataModelPhoto(
							SelectionFragment.listOfTotalImages.get(i)
									.toString(), false, currentTime.toString()));
				}
				// Deletion:
				Cursor cursor = db.query();
				if (cursor != null && cursor.moveToFirst()) {
					do {
						if (!SelectionFragment.listOfTotalImages
								.contains(cursor.getString(cursor
										.getColumnIndex("photopath")))) {
							int deleted = db.delete(cursor.getString(cursor
									.getColumnIndex("photopath")));
							Log.v(TAG, "Image deleted from database:" + deleted);
						}
					} while (cursor.moveToNext());
					// cursor.close();
				}

			}
			return null;
		}
	}

	void hideAllButtons() {
		rlMenuIcon.setVisibility(View.GONE);
		rlBackIcon.setVisibility(View.GONE);
		tvNoSelectedImages.setVisibility(View.GONE);
		tvDoneButton.setVisibility(View.GONE);
		imageViewBlock.setVisibility(View.GONE);
		imageViewPlay.setVisibility(View.GONE);
	}

	@Override
	public void onBlockImagesFragmentDestroyed() {
		// TODO Auto-generated method stub
		hideAllButtons();
	}

}
