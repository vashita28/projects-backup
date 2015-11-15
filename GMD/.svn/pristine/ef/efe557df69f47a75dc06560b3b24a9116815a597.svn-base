package co.uk.pocketapp.gmd;

import android.app.Application;
import android.graphics.Typeface;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class GMDApplication extends Application {

	public static Typeface fontHeading;
	public static Typeface fontText;

	@Override
	public void onCreate() {
		super.onCreate();

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.showImageOnFail(R.drawable.placeholderportrait)
				.showImageForEmptyUri(R.drawable.placeholderportrait)
				.showImageOnLoading(R.drawable.placeholderportrait)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();

		// Initialize ImageLoader with configuration.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions).threadPoolSize(1)
				.build();
		ImageLoader.getInstance().init(config);

		fontHeading = Typeface.createFromAsset(getAssets(),
				"HelveticaLTStd-Bold.otf");
		fontText = Typeface.createFromAsset(getAssets(),
				"HelveticaLTStd-Roman.otf");

	}
}
