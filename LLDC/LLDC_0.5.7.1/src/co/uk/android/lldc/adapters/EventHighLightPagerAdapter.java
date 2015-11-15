package co.uk.android.lldc.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.EventDetailsActivity;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.images.ImageCacheManager;
import co.uk.android.lldc.models.ServerModel;

import com.android.volley.toolbox.NetworkImageView;

public class EventHighLightPagerAdapter extends PagerAdapter {
	Context context;
	ArrayList<ServerModel> highlightEvent;
	// PageIndicator mIndicator;
	ViewPager viewPager;
	Calendar startcal, endcal;
	int screenWidth = 0, screenHeight = 0;

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
		return this.highlightEvent.size();
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
		View itemView = inflater.inflate(R.layout.highlighteventsitem,
				container, false);
		// ImageView highlight_image = (ImageView) itemView
		// .findViewById(R.id.highlight_image);
		TextView eventlist_eventname_header = (TextView) itemView
				.findViewById(R.id.eventlist_eventname_header);
		TextView eventlist_venuename_header = (TextView) itemView
				.findViewById(R.id.eventlist_venuename_header);
		TextView eventlist_date_header = (TextView) itemView
				.findViewById(R.id.eventlist_date_header);
		NetworkImageView eventlist_highlight_image = (NetworkImageView) itemView
				.findViewById(R.id.eventlist_highlight_image);

		eventlist_eventname_header.setText(highlightEvent.get(position)
				.getName().toUpperCase());
		eventlist_venuename_header.setText(highlightEvent.get(position)
				.getVenueTitle().toUpperCase());
		eventlist_date_header.setText(highlightEvent.get(position)
				.getActiveDays());

		String startdate_millisec = highlightEvent.get(position)
				.getStartDateTime() + "000";
		String enddate_millisec = highlightEvent.get(position).getEndDateTime()
				+ "000";

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
				.get(position).getColor()));

		try {
			int screenHeight = (int)(LLDCApplication.screenHeight * 0.35f) + 100;
			String szImageUrl = highlightEvent.get(position).getLargeImage()
					+ "&width=" + (LLDCApplication.screenWidth + 200) + "&height=" + screenHeight
					+ "&crop-to-fit";
			Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);
//			ImageLoader.getInstance().displayImage(szImageUrl,
//					eventlist_highlight_image);
			
			eventlist_highlight_image.setImageUrl(szImageUrl, ImageCacheManager
					.getInstance().getImageLoader());
			eventlist_highlight_image.setDefaultImageResId(R.drawable.qeop_placeholder);
			eventlist_highlight_image.setErrorImageResId(R.drawable.imgnt_placeholder);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		itemView.setTag("" + position);

		itemView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int position = Integer.parseInt(arg0.getTag().toString());
				LLDCApplication.selectedModel = highlightEvent.get(position);
				Intent intent = new Intent(context, EventDetailsActivity.class);
				// intent.putExtra("EVENTID",
				// highlightEvent.get(position).get_id());
				intent.putExtra("PAGETITLE", "Events");
				context.startActivity(intent);
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
		container.removeView((RelativeLayout) object);
		// container.removeView((LinearLayout) object);
	}
}
