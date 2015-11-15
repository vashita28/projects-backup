package co.uk.peeki.adapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import co.uk.peeki.R;
import co.uk.peeki.fragments.SelectionFragment;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageAdapterForSelection extends BaseAdapter {

	private static final String TAG = "IMAGE ADAPTER FOR SELECTION";
	private Context context;
	private List<String> imgPathList;
	public static boolean selectedImage = false;
	public static int countSelectedImages = 0;
	SelectionFragment showselectionFragment;

	ImageLoader imageLoader;

	// static HashMap<String, Bitmap> mapImages = new HashMap<String, Bitmap>();

	public ImageAdapterForSelection(Context c,
			SelectionFragment showselectionFragment, List<String> listImagePaths) {
		imageLoader = ImageLoader.getInstance();
		context = c;
		imgPathList = listImagePaths;
		this.showselectionFragment = showselectionFragment;
	}

	public void updateData(List<String> listImagePaths) {
		imgPathList = listImagePaths;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (imgPathList != null)
			return imgPathList.size();
		else
			return 0;
	}

	public static void selectedCount(int count) {
		countSelectedImages = count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	};

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		final ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.selection_row, parent,
					false);

			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageview);
			holder.imageViewSelect = (ImageView) convertView
					.findViewById(R.id.imageViewSelect);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView.setImageDrawable(context.getResources().getDrawable(
				R.drawable.placeholderportrait));

		// Save the state of selected images:
		if (SelectionFragment.listOfSelectedImages.contains(imgPathList.get(
				position).toString())
				|| countSelectedImages == imgPathList.size()) {
			holder.imageViewSelect.setVisibility(View.VISIBLE);

		} else
			holder.imageViewSelect.setVisibility(View.GONE);

		// if (mapImages.get("file://" + imgPathList.get(position).toString())
		// == null)
		imageLoader.displayImage("file://"
				+ imgPathList.get(position).toString(), holder.imageView,
				new ImageLoadListener());
		// else
		// holder.imageView.setImageBitmap(mapImages.get("file://"
		// + imgPathList.get(position).toString()));

		holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (v != null) {
					SelectionFragment.bIsSelectAll = false;
					if (SelectionFragment.listOfSelectedImages
							.contains(imgPathList.get(position).toString())) {
						SelectionFragment.listOfSelectedImages
								.remove(imgPathList.get(position).toString());
						selectedImage = false;

						if (countSelectedImages > 0) {
							countSelectedImages = countSelectedImages - 1;
						}
						holder.imageViewSelect.setVisibility(View.GONE);

					} else {
						SelectionFragment.listOfSelectedImages.add(imgPathList
								.get(position).toString());
						selectedImage = true;
						countSelectedImages++;
						holder.imageViewSelect.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		showselectionFragment.onShowSelectionFragmentLoad(countSelectedImages);
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		ImageView imageViewSelect;
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

}
