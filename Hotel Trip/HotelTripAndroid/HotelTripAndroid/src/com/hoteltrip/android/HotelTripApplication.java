package com.hoteltrip.android;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;

import com.hoteltrip.android.gallery.Constants.Config;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class HotelTripApplication extends Application {

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		if (Config.DEVELOPER_MODE
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyDeath().build());
		}

		super.onCreate();

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

		// DisplayImageOptions defaultOptions = new
		// DisplayImageOptions.Builder()
		// .cacheInMemory(true).cacheOnDisc(true)
		// .showStubImage(R.drawable.placeholder)
		// .bitmapConfig(Bitmap.Config.RGB_565)
		// // .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		// .build();
		//
		// ImageLoaderConfiguration config = new
		// ImageLoaderConfiguration.Builder(
		// getApplicationContext())
		// .defaultDisplayImageOptions(defaultOptions).threadPoolSize(1)
		// // .writeDebugLogs()
		// .build();
		//
		// ImageLoader.getInstance().init(config);

		initImageLoader(getApplicationContext());

	}

	public static void initImageLoader(Context context) {

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.showStubImage(R.drawable.placeholder)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(defaultOptions)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
