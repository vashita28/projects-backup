package co.uk.peeki.tasks;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import co.uk.peeki.db.MySqLiteHelper;
import co.uk.peeki.fragments.SelectionFragment;
import co.uk.peeki.ui.DataModelPhoto;

public class GetAllPhotosTask extends AsyncTask<Void, Void, ArrayList<String>> {

	Activity mActivity;
	public ProgressDialog dialog;
	public Handler handler;
	public MySqLiteHelper db;

	public GetAllPhotosTask(Activity activity) {
		// TODO Auto-generated constructor stub
		mActivity = activity;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog.setMessage("Loading photos...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		dialog.show();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected ArrayList<String> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { MediaStore.Images.ImageColumns.DATA };
		Cursor cursor = null;
		SortedSet<String> dirList = new TreeSet<String>();
		ArrayList<String> resultIAV = new ArrayList<String>();

		String[] directories = null;
		if (uri != null) {
			cursor = mActivity.managedQuery(uri, projection, null, null, null);
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
		Log.v("GetAllPhotosTask", "listOfTotalimages:: "
				+ SelectionFragment.listOfTotalImages.size());

		if (!SelectionFragment.bIsPhotosLoaded) {
			// Addition:
			for (int i = 0; i < SelectionFragment.listOfTotalImages.size(); i++) {
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				File file = new File(SelectionFragment.listOfTotalImages.get(i));
				String currentTime = sdf.format(file.lastModified());
				db.addPhoto(new DataModelPhoto(
						SelectionFragment.listOfTotalImages.get(i).toString(),
						false, currentTime.toString()));
			}
			// Deletion:
			Cursor cursor2 = db.query();
			if (cursor2 != null && cursor2.moveToFirst()) {
				do {
					if (!SelectionFragment.listOfTotalImages.contains(cursor2
							.getString(cursor2.getColumnIndex("photopath")))) {
						int deleted = db.delete(cursor2.getString(cursor2
								.getColumnIndex("photopath")));
						Log.v("GetAllPhotos", "Image deleted from database:"
								+ deleted);
					}
				} while (cursor2.moveToNext());
				// cursor.close();
			}
			if (cursor2 != null) {
				cursor2.close();
			}

			SelectionFragment.bIsPhotosLoaded = true;
		}

		return resultIAV;
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		// TODO Auto-generated method stub
		// if (dialog != null) {
		// dialog.dismiss();
		// }

		if (handler != null) {
			handler.sendEmptyMessage(0);
		}
	}

}
