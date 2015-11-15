package co.uk.peeki.adapter;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase.DisplayType;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import co.uk.peeki.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;

public class PageViewerImageAdapter extends PagerAdapter {

	private Activity _activity;
	private ArrayList<String> _imagePaths;
	private LayoutInflater inflater;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	PageViewerImageAdapter() {
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheOnDisc(true)
				// .imageScaleType(ImageScaleType.EXACTLY)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	// constructor
	public PageViewerImageAdapter(Activity activity,
			ArrayList<String> imagePaths) {
		this._activity = activity;
		this._imagePaths = imagePaths;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this._imagePaths.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		// ImageView imgDisplay;

		LayoutInflater inflater = (LayoutInflater) _activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewHolder holder;
		// View viewLayout = null;

		// if (viewLayout == null) {
		View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image,
				container, false);

		holder = new ViewHolder();
		holder.ivDisplay = (ImageViewTouch) viewLayout
				.findViewById(R.id.imgDisplay);
		holder.ivDisplay.resetDisplay();
		holder.ivDisplay.setScaleType(ScaleType.FIT_CENTER);
		holder.ivDisplay.setDisplayType(DisplayType.FIT_IF_BIGGER);

		// viewLayout.setTag(holder);
		// } else {
		// holder = (ViewHolder) viewLayout.getTag();
		// }

		//Log.e("PagerAdapter", "ImagePath is ::"
			//	+ _imagePaths.get(position).toString());

		// MemoryCacheUtil.findCachedBitmapsForImageUri("file://"
		// + _imagePaths.get(position).toString(), ImageLoader
		// .getInstance().getMemoryCache());
		ImageLoader.getInstance().displayImage(
				"file://" + _imagePaths.get(position).toString(),
				holder.ivDisplay);

		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inJustDecodeBounds = false;
		// options.inPurgeable = true;
		// options.inSampleSize = 2;

		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		// Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position),
		// options);
		// holder.ivDisplay.setImageBitmap(bitmap);

		// Bitmap bitmap = null;
		// try {
		// bitmap = BitmapFactory.decodeFile(_imagePaths.get(position),
		// options);
		// holder.ivDisplay.setImageBitmap(bitmap);
		// } catch (OutOfMemoryError e) {
		// System.gc();
		// e.printStackTrace();
		// if (bitmap != null && bitmap.isRecycled()) {
		// bitmap.recycle();
		// bitmap = null;
		// }
		// }

		((ViewPager) container).addView(viewLayout);

		// inflater = (LayoutInflater) _activity
		// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image,
		// container, false);
		//
		// imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
		//
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		//
		// Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position),
		// options);
		// imgDisplay.setImageBitmap(bitmap);
		//
		// ((ViewPager) container).addView(viewLayout);

		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);

	}

	class ViewHolder {
		ImageViewTouch ivDisplay;
	}

}
