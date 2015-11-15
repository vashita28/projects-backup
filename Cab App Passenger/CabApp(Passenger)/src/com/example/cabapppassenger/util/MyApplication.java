package com.example.cabapppassenger.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.receivers.IntentReceiver;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.CustomPushNotificationBuilder;
import com.urbanairship.push.PushManager;

public class MyApplication extends Application {

	Context mContext;
	Editor editor;
	SharedPreferences shred_pref;
	String PREF_NAME = "HandT";

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		shred_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shred_pref.edit();
		AirshipConfigOptions options = AirshipConfigOptions
				.loadDefaultOptions(this);
		options.inProduction = false;
		options.analyticsEnabled = false;
		options.gcmSender = "246589659776";
		options.developmentAppKey = "tAX1WVCxQByMWY5muCs1MQ";
		options.developmentAppSecret = "LoRYmuw3S3edaQnOJK8J6Q";
		UAirship.takeOff(this, options);
		mContext = this;

		PushManager.shared().setIntentReceiver(IntentReceiver.class);
		PushManager.enablePush();
		String apid = PushManager.shared().getAPID();
		Log.i("****APPID*****", "" + apid);
		if (apid != null && !apid.matches("null")) {
			editor.putString("APPID", apid);
			editor.commit();
		}

		CustomPushNotificationBuilder nb = new CustomPushNotificationBuilder();
		nb.layout = R.layout.customnotification_layout;
		nb.layoutIconId = R.id.iv_appicon;
		nb.layoutSubjectId = R.id.tv_appname;
		nb.layoutMessageId = R.id.tv_message;

		PushManager.shared().setNotificationBuilder(nb);
		initImageLoader(getApplicationContext());

	}

	void initImageLoader(Context context) {

		@SuppressWarnings("deprecation")
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.showStubImage(R.drawable.ic_stub).considerExifParams(true)
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
	}

}
