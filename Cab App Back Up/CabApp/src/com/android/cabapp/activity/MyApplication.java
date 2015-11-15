package com.android.cabapp.activity;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.android.cabapp.receiver.IntentReceiver;
import com.android.cabapp.util.Constants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		// Configure your application
		//
		// This can be done in code as illustrated here,
		// or you can add these settings to a properties file
		// called airshipconfig.properties
		// and place it in your "assets" folder
		// AirshipConfigOptions options = AirshipConfigOptions
		// .loadDefaultOptions(this);
		// options.developmentAppKey = Constants.UADevlopmentAppKey;
		// options.productionAppKey = Constants.UADevlopmentAppKey;
		// options.inProduction = false; // determines which app key to use

		AirshipConfigOptions options = AirshipConfigOptions
				.loadDefaultOptions(this);
		options.developmentAppKey = Constants.UADevlopmentAppKey;
	//	options.productionAppKey = Constants.UADevelopmentAppSecret;
		options.developmentAppSecret=Constants.UADevelopmentAppSecret;
		options.gcmSender="134763849757";
		
		options.inProduction = false; // determines which app key to use

		// Take off initializes the services
		UAirship.takeOff(this, options);
		PushManager.startService();
		PushManager.enablePush();
		String apid = PushManager.shared().getAPID();
		Logger.info("My Application onCreate - App APID: " + apid);

		PushManager.shared().setIntentReceiver(IntentReceiver.class);

		initImageLoader(MyApplication.this);
	}

	void initImageLoader(Context context) {

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCache(
						new UnlimitedDiscCache(StorageUtils
								.getCacheDirectory(this)))
				.defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(config);

		// .showStubImage(R.drawable.defaultprofilepic)
		// .showImageForEmptyUri(R.drawable.defaultprofilepic)
	}
}