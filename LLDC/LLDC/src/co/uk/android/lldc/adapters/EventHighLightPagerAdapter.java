package co.uk.android.lldc.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.EventDetailsActivity;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.EventsFragment;
import co.uk.android.lldc.fragments.tablet.EventDetailsFragmentTablet;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.util.Util;

import com.google.android.gms.internal.mc;
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
	boolean isTablet = false;
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
		screenHeight = size.y / 7;
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
		if (Util.getIsTabletFlag(context)) {
			Log.e(TAG, "Device is Tablet...");
			((Activity) context)
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			itemView = inflater.inflate(R.layout.highlighteventsitem_tablet,
					container, false);
			isTablet = true;
		} else {
			Log.e(TAG, "Device is Phone...");
			itemView = inflater.inflate(R.layout.highlighteventsitem,
					container, false);
			((Activity) context)
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			isTablet = false;
		}
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

		if (!isTablet) {
			eventlist_eventname_header.setText(highlightEvent.get(position)
					.getName().toUpperCase());
			eventlist_venuename_header.setText(highlightEvent.get(position)
					.getVenueTitle().toUpperCase());
			eventlist_date_header.setText(highlightEvent.get(position)
					.getActiveDays());

			try {
				String szImageUrl = highlightEvent.get(position)
						.getLargeImage();
				Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);
				ImageLoader.getInstance().displayImage(szImageUrl,
						eventlist_highlight_image);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String startdate_millisec = highlightEvent.get(position)
					.getStartDateTime() + "000";
			String enddate_millisec = highlightEvent.get(position)
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
			eventlist_date_header.setText(active_days.toUpperCase());

			eventlist_eventname_header.setTextColor(Color
					.parseColor(highlightEvent.get(position).getColor()));

		} else if (isTablet) {

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
				String szImageUrl = highlightEvent.get(position * 2)
						.getLargeImage();
				Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);
				ImageLoader.getInstance().displayImage(szImageUrl,
						eventlist_highlight_image);
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
			eventlist_date_header.setText(active_days.toUpperCase());

			eventlist_eventname_header.setTextColor(Color
					.parseColor(highlightEvent.get(position * 2).getColor()));

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
							.getLargeImage();
					Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);
					ImageLoader.getInstance().displayImage(szImageUrl,
							eventlist_highlight_image2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String startdate_millisec1 = highlightEvent.get(
						(position * 2) + 1).getStartDateTime()
						+ "000";
				String enddate_millisec1 = highlightEvent.get(
						(position * 2) + 1).getEndDateTime()
						+ "000";

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
		}

		itemView.setTag("" + position);

		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!EventsFragment.bIsTablet) {
					int position = Integer.parseInt(arg0.getTag().toString());
					LLDCApplication.selectedModel = highlightEvent
							.get(position);
					Intent intent = new Intent(context,
							EventDetailsActivity.class);
					// intent.putExtra("EVENTID",
					// highlightEvent.get(position).get_id());
					intent.putExtra("PAGETITLE", "Events");
					context.startActivity(intent);
				} else {
					mFragment.callEventDetailsFragment();
				}

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
		if (!isTablet)
			container.removeView((RelativeLayout) object);
		else
			container.removeView((LinearLayout) object);
		// container.removeView((LinearLayout) object);
	}
}
