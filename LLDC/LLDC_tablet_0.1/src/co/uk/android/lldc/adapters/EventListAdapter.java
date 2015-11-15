package co.uk.android.lldc.adapters;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.models.ServerModel;
import co.uk.android.lldc.tablet.LLDCApplication;

import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("InflateParams")
public class EventListAdapter extends BaseAdapter {
	private static final String TAG = EventListAdapter.class.getSimpleName();

	Context mContext;
	ArrayList<ServerModel> eventList;
	ViewHolder vholder;
	LayoutInflater inflater;
	Calendar startcal, endcal;

	public EventListAdapter(Context context, ArrayList<ServerModel> eventList) {
		// TODO Auto-generated constructor stub
		mContext = context;
		this.eventList = eventList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return eventList.size();
	}

	@Override
	public ServerModel getItem(int arg0) {
		// TODO Auto-generated method stub
		return eventList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			// convertView = inflater.inflate(R.layout.row_explorelist, parent,
			// true);
			if (inflater == null)
				inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.eventlist_row_tablet,
						null);
			}

			vholder = new ViewHolder();

			vholder.eventImage = (ImageView) convertView
					.findViewById(R.id.eventlist_image);
			vholder.eventName = (TextView) convertView
					.findViewById(R.id.eventlist_eventname);
			vholder.venueName = (TextView) convertView
					.findViewById(R.id.eventlist_venuename);
			vholder.evnetDate = (TextView) convertView
					.findViewById(R.id.eventlist_date);

			convertView.setTag(vholder);
		} else
			vholder = (ViewHolder) convertView.getTag();

		vholder.eventName.setText(eventList.get(arg0).getName().toUpperCase());
		vholder.eventName.setSelected(true);
		vholder.venueName.setText(eventList.get(arg0).getVenueTitle()
				.toUpperCase());
		// vholder.evnetDate.setText(eventList.get(arg0).getActiveDays());

		try {
			String szImageUrl = "";
			int screenWidth = (int) (LLDCApplication.screenWidth / 5);
			szImageUrl = eventList.get(arg0).getLargeImage() + "&width="
					+ screenWidth + "&height=" + screenWidth + "&crop-to-fit";
			// Log.e(TAG, "szImageUrl::> " + szImageUrl);

			vholder.eventImage.getLayoutParams().height = screenWidth;
			vholder.eventImage.getLayoutParams().width = screenWidth;

			ImageLoader.getInstance().displayImage(szImageUrl,
					vholder.eventImage);

			// vholder.eventImage.setImageUrl(szImageUrl, ImageCacheManager
			// .getInstance().getImageLoader());
			// vholder.eventImage
			// .setDefaultImageResId(R.drawable.qeop_placeholder);
			// vholder.eventImage.setErrorImageResId(R.drawable.imgnt_placeholder);
			// vholder.eventImage.setAdjustViewBounds(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String startdate_millisec = eventList.get(arg0).getStartDateTime()
				+ "000";
		String enddate_millisec = eventList.get(arg0).getEndDateTime() + "000";

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
			vholder.evnetDate.setText(endate.toString().toUpperCase());
		} else {
			vholder.evnetDate.setText(active_days.toUpperCase());
		}
		vholder.eventName.setTextColor(Color.parseColor(eventList.get(arg0)
				.getColor()));

		return convertView;
	}

	class ViewHolder {
		public TextView eventName, venueName, evnetDate;
		public ImageView eventImage;

	}

	String getMonthForInt(int num) {
		String month = "";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
	}
}
