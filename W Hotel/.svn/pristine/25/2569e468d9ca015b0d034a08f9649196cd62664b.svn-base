package uk.co.pocketapp.whotel;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Typeface;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by Pramod on 8/22/13.
 */
public class W_Hotel_Application extends Application {

	public Typeface font_Bold;
	public Typeface font_BoldItalic;
	public Typeface font_Book;
	public Typeface font_bold_crop_left;

	@Override
	public void onCreate() {
		super.onCreate();

		font_Bold = Typeface.createFromAsset(getAssets(), "WSansNew-Bold.otf");
		// font_Bold = Typeface.createFromAsset(getAssets(),
		// "wsansnewbold.ttf");
		font_BoldItalic = Typeface.createFromAsset(getAssets(),
				"WSansNew-BoldItalic.otf");
		font_Book = Typeface.createFromAsset(getAssets(), "WSansNew-Book.otf");
		font_bold_crop_left = Typeface.createFromAsset(getAssets(),
				"WSansNew-BoldCropLeft.otf");

		// Initialize ImageLoader with configuration.
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(
		// getApplicationContext())
		// .threadPriority(Thread.NORM_PRIORITY - 1)
		// // .memoryCacheSize(2 * 1024 * 1024)
		// .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
		// .discCacheExtraOptions(400, 480, CompressFormat.JPEG, 75, null)
		// // extra
		// .denyCacheImageMultipleSizesInMemory()
		// .discCacheFileNameGenerator(new Md5FileNameGenerator())
		// .tasksProcessingOrder(QueueProcessingType.LIFO)
		// .discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
		// .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
		// .build();

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.showStubImage(R.drawable.placeholderportrait)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions).threadPoolSize(1)
				// .writeDebugLogs()
				.build();

		ImageLoader.getInstance().init(config);
	}
}
