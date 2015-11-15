package co.uk.pocketapp.gmd.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.pocketapp.gmd.GMDApplication;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.util.AppValues;
import co.uk.pocketapp.gmd.util.Util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageAdapter extends BaseAdapter {

	ArrayList<String> fileArrayList;
	File file;
	public Context mContext;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;

	public ImageAdapter(Context context) {

		// ImageLoader Options
		options = new DisplayImageOptions.Builder()
		// .displayer(new FadeInBitmapDisplayer(800))
				.bitmapConfig(Bitmap.Config.RGB_565).build(); // ARGB_8888
		mContext = context;
		// file = new File(AppValues.getDirectory()
		// + File.separator + AppValues.APP_NAME); // + "/Camera/"
		// file = new File(AppValues.getDirectory() + File.separator
		// + AppValues.APP_NAME); // + "/Camera/"

		file = AppValues.getPhotoGraphDirectory();

		List<File> files = getListFiles(file);
		fileArrayList = new ArrayList<String>();

		for (int i = files.size() - 1; i >= 0; i--) {
			String szFilePath = files.get(i).getPath();
			// String szFileName = szFilePath.substring(szFilePath
			// .lastIndexOf("/") + 1);
			String szFileName = files.get(i).getName();
			String szPhotoEntryDetails = Util.getPhotoEntryDetails(mContext,
					szFileName).trim();

			if (!szPhotoEntryDetails.equals("")) {
				String[] photoDetails = szPhotoEntryDetails.split(",");
				String szLocation = "";
				try {
					szLocation = photoDetails[0];
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!szLocation.equals("")) {
					fileArrayList.add(files.get(i).getPath());
				}
			}
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fileArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// ImageView imageview;
		ViewHolder holder;
		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.photographs_grid_row, null);

			holder = new ViewHolder();
			holder.img = (ImageView) convertView
					.findViewById(R.id.imageview_photo);
			holder.txtPosition = (TextView) convertView
					.findViewById(R.id.textview_position);
			holder.txtLocation = (TextView) convertView
					.findViewById(R.id.textview_location);
			holder.txtExtra = (TextView) convertView
					.findViewById(R.id.textview_extra);
			holder.txtDateTime = (TextView) convertView
					.findViewById(R.id.textview_dateTime);
			holder.txtFileName = (TextView) convertView
					.findViewById(R.id.textview_filename);
			holder.txtLabel = (TextView) convertView
					.findViewById(R.id.textview_label);

			holder.txtPosition.setTypeface(GMDApplication.fontText);
			holder.txtLocation.setTypeface(GMDApplication.fontText);
			holder.txtExtra.setTypeface(GMDApplication.fontText);
			holder.txtDateTime.setTypeface(GMDApplication.fontText);
			holder.txtLabel.setTypeface(GMDApplication.fontText);

			convertView.setTag(holder); // set the View holder

			// imageview = new ImageView(mContext);
			// imageview.setLayoutParams(new GridView.LayoutParams(300, 300));
			// imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
			// imageview.setPadding(1, 1, 1, 1);

		} else {
			// imageview = (ImageView) convertView;
			holder = (ViewHolder) convertView.getTag();
		}

		String szFilePath = fileArrayList.get(position);

		holder.img.setImageDrawable(mContext.getResources().getDrawable(
				R.drawable.placeholderportrait));
		holder.txtFileName.setText(szFilePath);

		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 4;
		// Bitmap mBitmap = BitmapFactory.decodeFile(szFilePath, options);
		// holder.img.setImageBitmap(mBitmap);
		// holder.img.setAdjustViewBounds(true);
		// holder.img.setMaxHeight(200);
		// holder.img.setMaxWidth(200);

		// // Load image into imageView
		imageLoader.displayImage("file://" + szFilePath, holder.img, options);

		String szFileName = szFilePath
				.substring(szFilePath.lastIndexOf("/") + 1);
		String szPhotoEntryDetails = Util.getPhotoEntryDetails(mContext,
				szFileName).trim();

		// Log.d("ImageAdapter", "PhotoDetails:: " + szFilePath + "   "
		// + szFileName + "  " + szPhotoEntryDetails);

		if (!szPhotoEntryDetails.equals("")) {
			String[] photoDetails = szPhotoEntryDetails.split(",");
			String szLocation = "";
			String szPosition = "";
			String szComments = "";
			String szMotorPart = "";
			String szDateTime = "";
			try {
				szLocation = photoDetails[0];
				szPosition = photoDetails[1];
				szComments = photoDetails[2];
				szMotorPart = photoDetails[3];
				szDateTime = photoDetails[4];
			} catch (Exception e) {
				e.printStackTrace();
			}
			holder.txtLocation.setText(szLocation);// Location:
			holder.txtPosition.setText(szPosition);// Position:
			holder.txtExtra.setText(szComments);
			holder.txtDateTime.setText(szDateTime.toLowerCase());
			holder.txtLabel.setText(szFileName.substring(0,
					szFileName.lastIndexOf(".")));
		} else {
			holder.txtLocation.setText("");
			holder.txtPosition.setText("");
			holder.txtExtra.setText("");
			holder.txtDateTime.setText("");
			holder.txtLabel.setText("");
			holder.img.setImageBitmap(null);
		}

		return convertView;

	}

	class ViewHolder {
		ImageView img;
		TextView txtPosition;
		TextView txtDateTime;
		TextView txtLocation;
		TextView txtExtra;
		TextView txtFileName;
		TextView txtLabel;
	}

	class ImageInfo {
		String szFilePath;
	}

	private List<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<File>();
		File[] files = parentDir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				inFiles.addAll(getListFiles(file));
			} else {
				// if (file.getName().endsWith(".csv")) {
				// inFiles.add(file);
				// }
				inFiles.add(file);
			}
		}
		return inFiles;
	}
}