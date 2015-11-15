package co.uk.peeki;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Typeface;

import co.uk.peeki.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by Pramod on 8/22/13.
 */
public class Photo_Application extends Application {

	public static Typeface font_Regular;
	public static Typeface font_SemiBold;
	public static Typeface font_Light;
	public static String FLURRY_API_KEY = "277XQWW2786TB9VHWDK8";// "J5XW44XPD9Q5NG594DVJ";

	@Override
	public void onCreate() {
		super.onCreate();

		font_Regular = Typeface.createFromAsset(getAssets(),
				"OpenSans-Regular.ttf");
		font_SemiBold = Typeface.createFromAsset(getAssets(),
				"OpenSans-Semibold.ttf");
		font_Light = Typeface
				.createFromAsset(getAssets(), "OpenSans-Light.ttf");

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
