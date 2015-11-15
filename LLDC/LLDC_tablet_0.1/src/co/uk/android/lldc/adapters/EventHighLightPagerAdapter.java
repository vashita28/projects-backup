package co.uk.android.lldc.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.EventsFragment;
import co.uk.android.lldc.models.ServerModel;

import com.nostra13.universalimageloader.core.ImageLoader;

public class EventHighLightPagerAdapter extends PagerAdapter {
	private static final String TAG = EventHighLightPagerAdapter.class
			.getSimpleName();

	Context context;
	ArrayList<ServerModel> highlightEvent;
	// PageIndicator mIndicator;
	ViewPager viewPager;
	Calendar startcal, endcal;
	int screenWidth = 0, screenHeight = 0;
	EventsFragment mFragment;

	// int nPosition = 0;
	public EventHighLightPagerAdapter(EventsFragment fragment, Context context,
			ArrayList<ServerModel> highlightEvent, ViewPager viewPager) {
		this.mFragment = fragment;
		this.context = context;
		this.highlightEvent = highlightEvent;
		this.viewPager = viewPager;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;
	}

	public EventHighLightPagerAdapter(Context context,
			ArrayList<ServerModel> highlightEvent, ViewPager viewPager) {
		this.context = context;
		this.highlightEvent = highlightEvent;
		this.viewPager = viewPager;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y / 7;
	}

	@Override
	public int getCount() {
		if (highlightEvent.size() % 2 == 0)
			return this.highlightEvent.size() / 2;
		else
			return this.highlightEvent.size() / 2 + 1;

	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		// return super.instantiateItem(container, position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = null;
		Log.e(TAG, "Device is Tablet...");
		((Activity) context)
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		itemView = inflater.inflate(R.layout.highlighteventsitem_tablet,
				container, false);
		// ImageView highlight_image = (ImageView) itemView
		// .findViewById(R.id.highlight_image);
		TextView eventlist_eventname_header = (TextView) itemView
				.findViewById(R.id.eventlist_eventname_header);
		TextView eventlist_venuename_header = (TextView) itemView
				.findViewById(R.id.eventlist_venuename_header);
		TextView eventlist_date_header = (TextView) itemView
				.findViewById(R.id.eventlist_date_header);
		ImageView eventlist_highlight_image = (ImageView) itemView
				.findViewById(R.id.eventlist_highlight_image);

		// tablet
		TextView eventlist_eventname_header2 = (TextView) itemView
				.findViewById(R.id.eventlist_eventname_header2);
		TextView eventlist_venuename_header2 = (TextView) itemView
				.findViewById(R.id.eventlist_venuename_header2);
		TextView eventlist_date_header2 = (TextView) itemView
				.findViewById(R.id.eventlist_date_header2);
		ImageView eventlist_highlight_image2 = (ImageView) itemView
				.findViewById(R.id.eventlist_highlight_image2);

		// nPosition = position + 1;
		// (nPosition * 2) - 2
		// (nPosition * 2) - 1
		// nPosition = position + 1;

		eventlist_eventname_header.setText(highlightEvent.get(position * 2)
				.getName().toUpperCase());
		eventlist_venuename_header.setText(highlightEvent.get(position * 2)
				.getVenueTitle().toUpperCase());
		eventlist_date_header.setText(highlightEvent.get(position * 2)
				.getActiveDays());
		try {
			String szImageUrl = highlightEvent.get(position).getLargeImage()
					+ "&width=" + (screenWidth / 2) + "&height="
					+ (screenHeight * 0.4) + "&crop-to-fit";
			Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);

			eventlist_highlight_image.getLayoutParams().height = (int) (screenHeight * 0.4);
			eventlist_highlight_image.getLayoutParams().width = (screenWidth / 2);

			ImageLoader.getInstance().displayImage(szImageUrl,
					eventlist_highlight_image);

			// eventlist_highlight_image.setImageUrl(szImageUrl,
			// ImageCacheManager
			// .getInstance().getImageLoader());
			// eventlist_highlight_image
			// .setDefaultImageResId(R.drawable.qeop_placeholder);
			// eventlist_highlight_image
			// .setErrorImageResId(R.drawable.imgnt_placeholder);
			// eventlist_highlight_image.setAdjustViewBounds(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String startdate_millisec = highlightEvent.get(position * 2)
				.getStartDateTime() + "000";
		String enddate_millisec = highlightEvent.get(position * 2)
				.getEndDateTime() + "000";

		startcal = Calendar.getInstance();
		startcal.setTimeInMillis(Long.parseLong(startdate_millisec));
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
		String startdate = sdf.format(startcal.getTime());
		System.out.println(startdate);

		sdf = new SimpleDateFormat("dd MMM yyyy");
		endcal = Calendar.getInstance();
		endcal.setTimeInMillis(Long.parseLong(enddate_millisec));
		String endate = sdf.format(endcal.getTime());
		System.out.println(endate);
		String active_days = startdate + " - " + endate;
		if (startdate_millisec.equals(enddate_millisec)) {
			eventlist_date_header.setText(endate.toString().toUpperCase());
		} else {
			eventlist_date_header.setText(active_days.toUpperCase());
		}

		eventlist_eventname_header.setTextColor(Color.parseColor(highlightEvent
				.get(position * 2).getColor()));

		// if (getCount() % 2 != 0 || ((position * 2) + 1 == getCount())) {
		if (position + 1 < getCount()) {
			eventlist_eventname_header2.setText(highlightEvent
					.get((position * 2) + 1).getName().toUpperCase());
			eventlist_venuename_header2.setText(highlightEvent
					.get((position * 2) + 1).getVenueTitle().toUpperCase());
			eventlist_date_header2.setText(highlightEvent.get(
					(position * 2) + 1).getActiveDays());

			try {
				String szImageUrl = highlightEvent.get((position * 2) + 1)
						.getLargeImage()
						+ "&width="
						+ (screenWidth / 2)
						+ "&height=" + (screenHeight * 0.4) + "&crop-to-fit";
				Log.e(TAG, "szImageUrl::> " + szImageUrl);

				ImageLoader.getInstance().displayImage(szImageUrl,
						eventlist_highlight_image2);

				// eventlist_highlight_image2.setImageUrl(szImageUrl,
				// ImageCacheManager.getInstance().getImageLoader());
				// eventlist_highlight_image2
				// .setDefaultImageResId(R.drawable.qeop_placeholder);
				// eventlist_highlight_image2
				// .setErrorImageResId(R.drawable.imgnt_placeholder);
				// eventlist_highlight_image2.setAdjustViewBounds(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String startdate_millisec1 = highlightEvent.get((position * 2) + 1)
					.getStartDateTime() + "000";
			String enddate_millisec1 = highlightEvent.get((position * 2) + 1)
					.getEndDateTime() + "000";

			startcal = Calendar.getInstance();
			startcal.setTimeInMillis(Long.parseLong(startdate_millisec1));
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM");
			String startdate1 = sdf1.format(startcal.getTime());
			System.out.println(startdate1);

			sdf1 = new SimpleDateFormat("dd MMM yyyy");
			endcal = Calendar.getInstance();
			endcal.setTimeInMillis(Long.parseLong(enddate_millisec1));
			String endate1 = sdf.format(endcal.getTime());
			System.out.println(endate);
			String active_days1 = startdate1 + " - " + endate1;
			eventlist_date_header2.setText(active_days1.toUpperCase());

			eventlist_eventname_header2.setTextColor(Color
					.parseColor(highlightEvent.get((position * 2) + 1)
							.getColor()));
		}

		itemView.setTag("" + position);

		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mFragment.callEventDetailsFragment();

			}
		});

		// itemView.setBackgroundResource(res[0]);
		((ViewPager) container).addView(itemView);
		return itemView;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		// super.destroyItem(container, position, object);
		container.removeView((LinearLayout) object);
	}
}
