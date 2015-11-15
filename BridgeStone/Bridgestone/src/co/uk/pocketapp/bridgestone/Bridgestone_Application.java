package co.uk.pocketapp.bridgestone;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by Pramod on 4/24/14.
 */
public class Bridgestone_Application extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				// .showStubImage(R.drawable.placeholderportrait)
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
