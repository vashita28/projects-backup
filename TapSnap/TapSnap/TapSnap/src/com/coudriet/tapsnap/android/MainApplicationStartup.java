package com.coudriet.tapsnap.android;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.provider.Settings.Secure;
import android.util.Log;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

public class MainApplicationStartup extends Application {

	public static Typeface font_HelveticaLTStdBold;
	public static Typeface font_ProximaNovaBold;
	public static Typeface font_ProximaNovaRegular;

	public static String FLURRY_API_KEY = "673W3XBNYNXWK2MVG7J3";

	@Override
	public void onCreate() {
		super.onCreate();

		font_HelveticaLTStdBold = Typeface.createFromAsset(getAssets(),
				"HelveticaLTStd-Bold.otf");

		font_ProximaNovaBold = Typeface.createFromAsset(getAssets(),
				"Mark Simonson - Proxima Nova Bold.otf");

		font_ProximaNovaRegular = Typeface.createFromAsset(getAssets(),
				"Mark Simonson - Proxima Nova Regular.otf");

		// initialize parse account by using appid and clientkey
		Parse.initialize(this, "fhUsT9w7Ho3OrMeALLWKNGY2gsRywZbbipSSZ5JN",
				"boDdGwPWfJunraOhbYMR9ao8kgovXTrobReenT3r");
		/* Parse.initialize(this, "NX3OL6mQPEYHHmTX03NZvhnhnvr0kNQf1YqJ5u5c",
				   "AsLbn7qBEaN8uTLyFdoF5R37fPepknYu9Lo53QBh");*/
		ParseFacebookUtils.initialize(getString(R.string.app_id));

		// Parse.initialize(this, "NX3OL6mQPEYHHmTX03NZvhnhnvr0kNQf1YqJ5u5c",
		// "AsLbn7qBEaN8uTLyFdoF5R37fPepknYu9Lo53QBh");//by sagar credentials

		//PushService.setDefaultPushCallback(this, MainActivity.class);
		
	
		//ParseInstallation.getCurrentInstallation().getInstallationId();
		ParseInstallation installation = ParseInstallation
				 .getCurrentInstallation();
		String android_id = Secure.getString(getApplicationContext()
				.getContentResolver(), Secure.ANDROID_ID);
		Log.e("LOG", "android id >>" + android_id);
		 installation.put("UniqueId", android_id);
		 installation.saveInBackground();

		//ParseInstallation.getCurrentInstallation().saveInBackground();

		
		
		
		// PushService.setDefaultPushCallback(this, MainActivity.class);
		// ParseInstallation installation = ParseInstallation
		// .getCurrentInstallation();
		
		initImageLoader(MainApplicationStartup.this);

	}

	void initImageLoader(Context context) {

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.showStubImage(R.drawable.defaultprofilepic)
				.showImageForEmptyUri(R.drawable.defaultprofilepic)
				.considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
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
