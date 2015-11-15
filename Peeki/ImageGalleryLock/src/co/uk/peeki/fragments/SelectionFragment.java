package co.uk.peeki.fragments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.peeki.Photo_Application;
import co.uk.peeki.R;
import co.uk.peeki.adapter.ImageAdapterForSelection;
import co.uk.peeki.db.MySqLiteHelper;
import co.uk.peeki.interfaces.FragmentLoad;
import co.uk.peeki.tasks.GetAllPhotosTask;
import co.uk.peeki.ui.DataModelPhoto;
import co.uk.peeki.ui.MainActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

public class SelectionFragment extends Fragment {
	private static final String TAG = "SELECTION FRAGMENT";

	public static ArrayList<String> listOfTotalImages = new ArrayList<String>();
	public static ArrayList<String> listOfSelectedImages = new ArrayList<String>();

	MySqLiteHelper db;

	GridView gridViewPhotoGallery;
	ImageView imageViewSelect;
	TextView tvNoImagesToDisplay;
	ProgressDialog pDialog;

	ImageAdapterForSelection adapter;
	static FragmentLoad mFragmentLoad;
	int countSelectedImagesShowFragment = 0;
	public static boolean bIsSelectAll = false;
	boolean bIsListLoading = false;
	public static boolean bIsPhotosLoaded = false;

	Handler mHandler;

	public SelectionFragment() {
	}

	public static Fragment newInstance(Context context,
			FragmentLoad fragmentLoad) {
		SelectionFragment f = new SelectionFragment();
		mFragmentLoad = fragmentLoad;
		return f;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		if (savedInstanceState != null) {
			// Restore last state.
			bIsPhotosLoaded = savedInstanceState.getBoolean("isPhotosLoaded",
					false);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.selection_fragment, null, true);
		bIsSelectAll = false;

		db = new MySqLiteHelper(MainActivity.mContext);
		gridViewPhotoGallery = (GridView) root
				.findViewById(R.id.gridViewPhotoGallerySelection);
		imageViewSelect = (ImageView) root.findViewById(R.id.ivSelect);
		tvNoImagesToDisplay = (TextView) root
				.findViewById(R.id.tvNoImagesToDisplay);
		tvNoImagesToDisplay.setTypeface(Photo_Application.font_SemiBold);

		gridViewPhotoGallery.setOnScrollListener(new PauseOnScrollListener(
				ImageLoader.getInstance(), true, true));
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				Log.e("handleMessage", "handleMessage");
				bIsListLoading = false;
				AddListToDisplayAndSetAdapter();
				super.handleMessage(message);
			}
		};

		// listOfTotalImages = getFilePaths();
		if (!bIsPhotosLoaded) {
			bIsListLoading = true;

			pDialog = new ProgressDialog(MainActivity.mContext);

			GetAllPhotosTask task = new GetAllPhotosTask(getActivity());
			task.dialog = pDialog;
			task.handler = mHandler;
			task.db = db;
			task.execute();
		}

		imageViewSelect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> unblockedPhotos = db.getAllUnblockedPhotos();
				if (!bIsSelectAll) {
					bIsSelectAll = true;
					listOfSelectedImages.clear();
					listOfSelectedImages.addAll(unblockedPhotos);
					if (listOfSelectedImages.size() > 0) {
						ImageAdapterForSelection
								.selectedCount(listOfSelectedImages.size());
						adapter.notifyDataSetChanged();
					}
				} else {
					bIsSelectAll = false;
					listOfSelectedImages.clear();
					ImageAdapterForSelection.selectedCount(0);
					if (adapter != null) {
						adapter.notifyDataSetChanged();
					}
				}
			}
		});

		AddListToDisplayAndSetAdapter();
		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mFragmentLoad.onShowSelectionFragmentLoad(this,
				countSelectedImagesShowFragment, false);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isPhotosLoaded", bIsPhotosLoaded);
		super.onSaveInstanceState(outState);
	}

	void AddListToDisplayAndSetAdapter() {
		// Adding in database
		if (listOfTotalImages != null && !bIsListLoading) {

			// List<DataModelPhoto> photos = db.getAllPhotos();
			// // updating adapter with new list:
			// ArrayList<String> unblockedList = new ArrayList<String>();
			// for (int i = 0; i < photos.size(); i++) {
			// if (!photos.get(i).isBlocked()) {
			// unblockedList.add(photos.get(i).getImagePath());
			// }
			// }
			// if(db.getAllUnblockedPhotos()!=null){

			gridViewPhotoGallery.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub

					setDataInAdapter();

					if (bIsPhotosLoaded && pDialog != null
							&& pDialog.isShowing()) {
						pDialog.dismiss();
					}
				}
			});

		}
	}

	public void setDataInAdapter() {
		Log.e("SelectionFragment", "setDataInAdapter");
		ArrayList<String> unblockedList = db.getAllUnblockedPhotos();

		if (unblockedList != null && unblockedList.size() > 0) {

			Log.e("SelectionFragment",
					"unblockedListSize:: " + unblockedList.size());
			tvNoImagesToDisplay.setVisibility(View.GONE);
			if (gridViewPhotoGallery.getChildCount() > 0) {
				adapter.updateData(unblockedList);
				adapter.notifyDataSetChanged();
			} else {
				adapter = new ImageAdapterForSelection(MainActivity.mContext,
						this, unblockedList);
				gridViewPhotoGallery.setAdapter(adapter);
			}

		} else {
			gridViewPhotoGallery.setAdapter(null);
			tvNoImagesToDisplay.setVisibility(View.VISIBLE);
		}
	}

	// Get all pictures from all folders:
	@SuppressWarnings("deprecation")
	public ArrayList<String> getFilePaths() {

		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { MediaStore.Images.ImageColumns.DATA };
		Cursor cursor = null;
		SortedSet<String> dirList = new TreeSet<String>();
		ArrayList<String> resultIAV = new ArrayList<String>();

		String[] directories = null;
		if (uri != null) {
			cursor = getActivity().managedQuery(uri, projection, null, null,
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
			// cursor.close();
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
		return resultIAV;
	}

	public void onShowSelectionFragmentLoad(int countSelectedImages) {
		if (countSelectedImages != 0) {
			countSelectedImagesShowFragment = countSelectedImages;
		}
		mFragmentLoad.onShowSelectionFragmentLoad(this, countSelectedImages,
				true);
	}

}
