package co.uk.pocketapp.gmd;

import android.app.Application;
import android.graphics.Typeface;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class GMDApplication extends Application {

	public static Typeface fontHeading;
	public static Typeface fontText;

	@Override
	public void onCreate() {
		super.onCreate();

		// Initialize ImageLoader with configuration.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(2 * 1024 * 1024)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .imageDownloader(new URLConnectionImageDownloader())
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.build();
		ImageLoader.getInstance().init(config);

		fontHeading = Typeface.createFromAsset(getAssets(),
				"HelveticaLTStd-Bold.otf");
		fontText = Typeface.createFromAsset(getAssets(),
				"HelveticaLTStd-Roman.otf");

	}

}
