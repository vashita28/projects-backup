package co.uk.peeki.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import co.uk.peeki.R;
import co.uk.peeki.db.MySqLiteHelper;
import co.uk.peeki.fragments.BlockImagesFragment;
import co.uk.peeki.ui.MainActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageAdapterForBlocked extends BaseAdapter {

	private static final String TAG = "IMAGE ADAPTER FOR BLOCKED";
	private Context mContext;
	private List<String> imgPathList;
	public static boolean blockedImage = false;
	public static boolean clearAllList = false;
	BlockImagesFragment blockedFragment;
	static int countBlockedImages = 0;// ImageAdapterForSelection.countSelectedImages;
	MySqLiteHelper db = new MySqLiteHelper(MainActivity.mContext);

	ImageLoader imageLoader;

	// static HashMap<String, Bitmap> mapImages = new HashMap<String, Bitmap>();

	public ImageAdapterForBlocked(Context c,
			BlockImagesFragment blockedFragment, List<String> listImagePaths,
			int count) {
		imageLoader = ImageLoader.getInstance();
		mContext = c;
		imgPathList = listImagePaths;
		this.blockedFragment = blockedFragment;
		this.countBlockedImages = count;
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

	public static void blockCount(int count) {
		countBlockedImages = count;
		if (countBlockedImages == 0) {
			clearAllList = true;
		} else
			clearAllList = false;
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
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.blocked_row, parent, false);

			holder = new ViewHolder();
			holder.ivBlockedFragment = (ImageView) convertView
					.findViewById(R.id.ivBlockedFragment);
			holder.imageViewBlock = (ImageView) convertView
					.findViewById(R.id.imageViewBlock);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.ivBlockedFragment.setImageDrawable(mContext.getResources()
				.getDrawable(R.drawable.placeholderportrait));

		// selected images from selection screen+db
		if (BlockImagesFragment.listOfBlockedImages.contains(imgPathList.get(
				position).toString())) {
			holder.imageViewBlock.setVisibility(View.VISIBLE);
		} else
			holder.imageViewBlock.setVisibility(View.GONE);

		if (clearAllList) {
			holder.imageViewBlock.setVisibility(View.GONE);
		}

		// if (mapImages.get("file://" + imgPathList.get(position).toString())
		// == null)
		imageLoader.displayImage("file://"
				+ imgPathList.get(position).toString(),
				holder.ivBlockedFragment, new ImageLoadListener());
		// else
		// holder.ivBlockedFragment.setImageBitmap(mapImages.get("file://"
		// + imgPathList.get(position).toString()));

		holder.ivBlockedFragment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (v != null) {
					BlockImagesFragment.bIsSelectAllInBlocked = false;
					if (BlockImagesFragment.listOfBlockedImages
							.contains(imgPathList.get(position).toString())) {

						BlockImagesFragment.listOfBlockedImages
								.remove(imgPathList.get(position).toString());
						BlockImagesFragment.listOfUnblockedImages
								.add(imgPathList.get(position).toString());
						blockedImage = false;

						if (countBlockedImages > 0) {
							countBlockedImages = countBlockedImages - 1;
						}
						holder.imageViewBlock.setVisibility(View.GONE);
					} else {

						BlockImagesFragment.listOfBlockedImages.add(imgPathList
								.get(position).toString());
						blockedImage = true;
						countBlockedImages++;
						holder.imageViewBlock.setVisibility(View.VISIBLE);
						if (BlockImagesFragment.listOfUnblockedImages
								.contains(imgPathList.get(position).toString())) {
							BlockImagesFragment.listOfUnblockedImages
									.remove(imgPathList.get(position)
											.toString());
						}

					}
				}
			}
		});
		blockedFragment.onBlockImagesFragmentLoad(countBlockedImages);
		return convertView;
	}

	class ViewHolder {
		ImageView ivBlockedFragment;
		ImageView imageViewBlock;
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
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
		}
	}

}
