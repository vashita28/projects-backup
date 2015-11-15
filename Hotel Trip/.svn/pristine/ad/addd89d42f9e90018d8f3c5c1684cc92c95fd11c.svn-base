package com.hoteltrip.android.adapters;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hoteltrip.android.R;
import com.hoteltrip.android.util.BookingGetterSetter;

public class BookingDataAdapter extends BaseAdapter {

	List<BookingGetterSetter> bookinglist;
	private Context context;
	Handler handler;

	public BookingDataAdapter(Context context,
			List<BookingGetterSetter> bookingList) {
		this.context = context;
		bookinglist = bookingList;

	}

	public void setData(List<BookingGetterSetter> bookingList) {
		this.bookinglist = bookingList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bookinglist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bookinglist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		// pos = position;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.mybooking_row, parent,
					false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.bookingid.setText(bookinglist.get(position).getBooking_id());
		viewHolder.hotelname.setText(bookinglist.get(position).getHotel_name());
		viewHolder.hotelpincode.setText(bookinglist.get(position).getPincode());
		viewHolder.paymentstatus.setText(bookinglist.get(position)
				.getPayment_status());
		viewHolder.bookingperiod.setText(bookinglist.get(position)
				.getCheckindate()
				+ " to "
				+ bookinglist.get(position).getCheckoutdate());

		return convertView;
	}

	class ViewHolder {

		TextView bookingid;
		TextView hotelname;
		TextView hotelpincode;
		TextView bookingperiod;
		TextView paymentstatus;

		public ViewHolder(final View v) {
			bookingid = (TextView) v.findViewById(R.id.tv_bookingidtext);
			hotelname = (TextView) v.findViewById(R.id.tv_hotelname);
			hotelpincode = (TextView) v.findViewById(R.id.tv_pincodetext);
			bookingperiod = (TextView) v
					.findViewById(R.id.tv_bookingperiodtext);
			paymentstatus = (TextView) v
					.findViewById(R.id.tv_paymentstatustext);

		}
	}
}
