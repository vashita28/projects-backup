package com.example.cabapppassenger.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.datastruct.json.Bookings;
import com.example.cabapppassenger.fragments.BookingDetailsFragment;
import com.example.cabapppassenger.fragments.Bookings_Driver_DetailsFragment;
import com.example.cabapppassenger.fragments.Pre_BookFragment;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumLocations;
import com.example.cabapppassenger.fragments.Pre_BookFragment.EnumPaymentType;
import com.example.cabapppassenger.model.LocationParcelable;
import com.example.cabapppassenger.model.PreBookParcelable;
import com.example.cabapppassenger.task.CancelBookingTask;
import com.example.cabapppassenger.util.Constant;
import com.google.gson.JsonObject;

public class MyBookingsAdapter extends BaseAdapter implements Filterable {

	Context mContext;
	List<Bookings> bookingsListOriginal, bookingsList;

	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	Editor editor;
	SharedPreferences shared_pref;
	String PREF_NAME = "CabApp_Passenger";
	Fragment fragment;

	Handler emptyviewHandler, cancelbooking_handler;
	ArrayList<Bookings> filteredBookingsList;
	public boolean isHistory_global;

	PreBookParcelable pbParcelable;
	LocationParcelable defaultPickUpLocation, defaultDropOffLocation;
	Calendar mCal;

	public MyBookingsAdapter(Fragment fragment, Context context,
			List<Bookings> bookingsList, Handler emptyviewHandler,
			Handler mhandler) {
		// TODO Auto-generated constructor stub
		mContext = context;
		this.fragment = fragment;
		this.bookingsListOriginal = bookingsList; // not using this one
		this.bookingsList = bookingsList;
		this.emptyviewHandler = emptyviewHandler;
		this.cancelbooking_handler = mhandler;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (bookingsList != null)
			return bookingsList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bookingsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		shared_pref = mContext.getSharedPreferences(PREF_NAME,
				Context.MODE_PRIVATE);
		editor = shared_pref.edit();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_mybookings_malik, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvPaymentMethod = (TextView) convertView
				.findViewById(R.id.tvPaymentMethod);
		holder.rel_pending = (RelativeLayout) convertView
				.findViewById(R.id.rel_pending);
		holder.rel_cancelled = (RelativeLayout) convertView
				.findViewById(R.id.rel_cancelled);
		holder.rl_list = (RelativeLayout) convertView
				.findViewById(R.id.rl_list);
		holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
		holder.tvPickupAddress = (TextView) convertView
				.findViewById(R.id.tvPickupAddress);
		holder.tvDropOffAddress = (TextView) convertView
				.findViewById(R.id.tvDropOffAddress);
		holder.vBackground = convertView.findViewById(R.id.v_background);

		final Bookings booking = this.bookingsList.get(position);
		holder.tvDropOffAddress.setText(booking.getDropLocation()
				.getAddressLine1()
				+ ", "
				+ booking.getDropLocation().getPostCode());
		holder.tvPickupAddress.setText(booking.getPickupLocation()
				.getAddressLine1()
				+ ", "
				+ booking.getPickupLocation().getPostCode());

		if (booking.getFixedPriceAmount() != null
				&& !booking.getFixedPriceAmount().equals("")
				&& !booking.getFixedPriceAmount().equals("0"))

		{
			if (booking.getPaymentType().equals("both"))
				holder.tvPaymentMethod.setText("Card - "
						+ booking.getFixedPriceAmount());
			else
				holder.tvPaymentMethod.setText("Cash - "
						+ booking.getFixedPriceAmount());
		} else {
			if (booking.getPaymentType().equals("both"))
				holder.tvPaymentMethod.setText("Card");
			else
				holder.tvPaymentMethod.setText("Cash");
		}

		mCal = Calendar.getInstance(TimeZone.getDefault());
		String szDateTime = booking.getPickupDateTime();
		// Date startDate;
		try {
			// startDate = (Date) dateFormat.parse(szDateTime);
			// mCal.setTime(startDate);
			//
			// Date currentTime = mCal.getTime();
			// int currentDay = mCal.get(Calendar.DATE);
			// int currentMonth = mCal.get(Calendar.MONTH) + 1;
			// int currentYear = mCal.get(Calendar.YEAR);
			// int currentDayOfWeek = mCal.get(Calendar.DAY_OF_WEEK);
			// int currentDayOfMonth = mCal.get(Calendar.DAY_OF_MONTH);
			// int CurrentDayOfYear = mCal.get(Calendar.DAY_OF_YEAR);
			//
			// String dateFormatted = currentDay
			// /* + getDayOfMonthSuffix(currentDay) */
			// + " "
			// + new SimpleDateFormat("MMM").format(mCal.getTime())
			// + " "
			// + String.format("%02d:%02d",
			// mCal.get(Calendar.HOUR_OF_DAY),
			// mCal.get(Calendar.MINUTE));

			// holder.tvTime.setText(dateFormatted);

			SimpleDateFormat sdf = new SimpleDateFormat("E dd LLL");
			Calendar c = Calendar.getInstance();

			// Date date=new Date(szDateTime);
			// int selectedDay=date.getHours();
			// c.setTime(date);
			int selectedDay = Integer.parseInt(szDateTime.substring(0,
					szDateTime.indexOf("/")));
			int selectedMonth = Integer.parseInt(szDateTime.substring(
					szDateTime.indexOf("/") + 1, szDateTime.lastIndexOf("/"))) - 1;
			if (selectedMonth == -1) {
				selectedMonth = 11;
			}
			Log.e("Date", szDateTime);
			int hour = Integer.parseInt(szDateTime.split(" ")[1].split(":")[0]
					.toString());

			// int hour = Integer.parseInt(szDateTime.substring(10,
			// szDateTime.indexOf(":")));

			int minute = Integer
					.parseInt(szDateTime.split(" ")[1].split(":")[1].toString()); // szDateTime.substring(14,
																					// 16).trim());
			//
			Log.i(getClass().getSimpleName(), "selectedDay " + selectedDay
					+ "hour and minute " + hour + minute + " selectedMonth "
					+ selectedMonth);

			c.set(0, selectedMonth, selectedDay, hour, minute, 0);
			String pickUpDateToShow = sdf.format(c.getTime());

			holder.tvTime.setText(pickUpDateToShow + " "
					+ szDateTime.split(" ")[1].split(":")[0].toString() + ":"
					+ szDateTime.split(" ")[1].split(":")[1].toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// if (getCount() - 1 == position) {
		// Log.i(getClass().getSimpleName(), "getView " + (getCount() - 1));
		// holder.vBackground.setVisibility(View.GONE);

		// } else {
		// Log.i(getClass().getSimpleName(), "getView " + (getCount() - 1));
		holder.vBackground.setVisibility(View.VISIBLE);
		// }

		holder.rl_list.setOnClickListener(new togglebuttonClickListener(
				position));

		JsonObject jobj = bookingsList.get(position).getStatus();
		String status = jobj.get("currentStatus").getAsString();

		if (status.toLowerCase().equals("cancelled")) {
			holder.rel_cancelled.setVisibility(View.VISIBLE);
			holder.rel_pending.setVisibility(View.GONE);

		} else if (status.toLowerCase().equals("pending")) {
			holder.rel_cancelled.setVisibility(View.GONE);
			holder.rel_pending.setVisibility(View.VISIBLE);
		} else {
			holder.rel_cancelled.setVisibility(View.GONE);
			holder.rel_pending.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvPaymentMethod, tvTime, tvPickupAddress, tvDropOffAddress;
		RelativeLayout rl_list;

		View vBackground;
		RelativeLayout rel_pending, rel_cancelled;
	}

	String getDayOfMonthSuffix(final int n) {
		if (n < 1 || n > 31) {
			throw new IllegalArgumentException("Illegal day of month");
		}

		if (n >= 11 && n <= 13) {
			return "th";
		}

		switch (n % 10) {
		case 1:
			return "st";
		case 2:
			return "nd";
		case 3:
			return "rd";
		default:
			return "th";
		}
	}

	public Filter getFilter(final boolean isHistory) {
		// TODO Auto-generated method stub
		isHistory_global = isHistory;
		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				bookingsList = (List<Bookings>) results.values;
				notifyDataSetChanged();
				emptyviewHandler.sendEmptyMessage(0);
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				filteredBookingsList = new ArrayList<Bookings>();

				if (constraint == null || constraint.length() == 0) {
					results.count = bookingsList.size();
					results.values = bookingsList;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < bookingsList.size(); i++) {

						if (isHistory) {
							if (bookingsList.get(i).getCompletedAt().equals("")
									|| bookingsList.get(i).getStatus()
											.get("currentStatus").toString()
											.equals("")) {
								continue;
							}

						}

						String pickupAddress, dropoffAddress;
						pickupAddress = bookingsList.get(i).getPickupLocation()
								.getAddressLine1().toString();

						dropoffAddress = bookingsList.get(i).getDropLocation()
								.getAddressLine1().toString();

						if (pickupAddress.toLowerCase().contains(
								constraint.toString())
								|| dropoffAddress.toLowerCase().contains(
										constraint.toString())) {
							filteredBookingsList.add(bookingsList.get(i));
						}

					}
					results.count = filteredBookingsList.size();
					results.values = filteredBookingsList;

				}

				return results;
			}
		};

		return filter;
	}

	public List<Bookings> getCurrentList() {
		return bookingsList;
	}

	public void setUpdatedList(List<Bookings> updatedList) {
		bookingsList = updatedList;
		this.notifyDataSetChanged();
		Log.i(getClass().getSimpleName(), "setUpdatedList " + (getCount() - 1));
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}

	int post;

	public class togglebuttonClickListener implements OnClickListener {
		int position;

		public togglebuttonClickListener(int position) {

			this.position = position;
		}

		public void onClick(View v) {
			{
				// String status
				hideKeybord(v);
				Log.e("Booking id", bookingsList.get(position).getId());
				JsonObject jobj = bookingsList.get(position).getStatus();
				String status = jobj.get("currentStatus").getAsString();

				/*
				 * editor.putString("Booking Id", bookingsList.get(position)
				 * .getId()); editor.commit();
				 */
				// .get("currentStatus").toString();
				if (isHistory_global) {
					if (status.equals("Booked")) {
						// if (bookingsList.get(position).getCompletedAt()
						// .equals("")) {

						// } else {
						// edit = new Bookings_Driver_DetailsFragment(true,
						// true);
						// }
						Bookings_Driver_DetailsFragment edit = new Bookings_Driver_DetailsFragment(
								true, bookingsList.get(position).getId());
						FragmentTransaction ft = fragment.getParentFragment()
								.getChildFragmentManager().beginTransaction();
						ft.addToBackStack("myBookingsFragment");
						ft.replace(R.id.frame_container, edit,
								"bookingsDriverDetailsFragment");
						ft.commit();
					} else if (status.equals("Cancelled")) {
						Constant.alertdialog("Cancelled", mContext);
					}
				} else {

					Log.e("My bookings status", status);
					if (status.equals("Pending")) {
						// Constant.alertdialog("Pending", mContext);
						final Dialog pending_dialog = new Dialog(mContext,
								android.R.style.Theme_Translucent);

						pending_dialog
								.requestWindowFeature(Window.FEATURE_NO_TITLE);
						pending_dialog.setContentView(R.layout.bookings_dialog);
						pending_dialog.setCancelable(true);
						pending_dialog.setCanceledOnTouchOutside(true);

						final View view_amend = (View) pending_dialog
								.findViewById(R.id.amend_view);
						final View view_cancel = (View) pending_dialog
								.findViewById(R.id.cancel_view);
						final View view_close = (View) pending_dialog
								.findViewById(R.id.close_view);

						view_amend.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								pbParcelable = new PreBookParcelable();
								defaultPickUpLocation = new LocationParcelable();
								defaultDropOffLocation = new LocationParcelable();
								pbParcelable.setWheelChairSelected(bookingsList
										.get(position)
										.getWheelchairAccessRequired()
										.equalsIgnoreCase("true") ? true
										: false);
								pbParcelable
										.setHearingImpairedSelected(bookingsList
												.get(position)
												.getIsHearingImpaired()
												.equalsIgnoreCase("true") ? true
												: false);

								pbParcelable.setBookingId(bookingsList.get(
										position).getId());

								// pbParcelable.setPassengerCount(bookingsList.get(
								// position).get);
								pbParcelable.setComment(bookingsList.get(
										position).getNote());
								pbParcelable.setePaymentType(EnumPaymentType
										.valueOf(bookingsList.get(position)
												.getPaymentType().toUpperCase()));

								if (pbParcelable != null
										&& !pbParcelable.getTruncatedPan()
												.equals(""))
									pbParcelable.setTruncatedPan(bookingsList
											.get(position).getTruncatedPan()
											.trim());
								pbParcelable.setPaymentTypeUpdated(true);

								for (int i = 0; i < Constant.arr_passengercount
										.size(); i++) {
									if (Constant.arr_passengercount
											.get(i)
											.equals("1-"
													+ bookingsList
															.get(position)
															.getNumberOfPassengers())) {
										pbParcelable.setPassengerCount(i);
										break;
									}
								}

								pbParcelable.setValuesUpdated(true);
								if (bookingsList.get(position)
										.getFixedPriceAmount() != null) {
									if (bookingsList.get(position)
											.getFixedPriceAmount().length() > 1) {

										pbParcelable.setFixedPrice(Double
												.valueOf(bookingsList
														.get(position)
														.getFixedPriceAmount()
														.substring(1)));

										pbParcelable.setIsFixedPrice(true);
										pbParcelable
												.setFixedPriceCurrency(bookingsList
														.get(position)
														.getFixedPriceAmount()
														.substring(0, 1));
									}
								}

								defaultPickUpLocation.setAddress1(bookingsList
										.get(position).getPickupLocation()
										.getAddressLine1());
								defaultPickUpLocation.setPinCode(bookingsList
										.get(position).getPickupLocation()
										.getPostCode());
								defaultPickUpLocation.setLatitude(Double
										.valueOf(bookingsList.get(position)
												.getPickupLocation()
												.getLatitude()));
								defaultPickUpLocation.setLongitude(Double
										.valueOf(bookingsList.get(position)
												.getPickupLocation()
												.getLongitude()));
								defaultDropOffLocation.setAddress1(bookingsList
										.get(position).getDropLocation()
										.getAddressLine1());
								defaultDropOffLocation.setPinCode(bookingsList
										.get(position).getDropLocation()
										.getPostCode());
								defaultDropOffLocation.setLatitude(Double
										.valueOf(bookingsList.get(position)
												.getDropLocation()
												.getLatitude()));
								defaultDropOffLocation.setLongitude(Double
										.valueOf(bookingsList.get(position)
												.getDropLocation()
												.getLongitude()));
								SimpleDateFormat sdf = new SimpleDateFormat(
										"E dd LLL");

								Date startDate = null;
								startDate = mCal.getTime();
								try {
									startDate = (Date) dateFormat
											.parse(bookingsList.get(position)
													.getPickupDateTime());
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								mCal.setTime(startDate);
								pbParcelable.setPickUpDateToShow((sdf
										.format(startDate).toString()));

								sdf = new SimpleDateFormat("dd/MM/yyyy");
								pbParcelable.setPickUpDate((sdf
										.format(startDate).toString()));
								pbParcelable.setPickUpTime(String.format(
										"%02d:%02d",
										mCal.get(Calendar.HOUR_OF_DAY),
										mCal.get(Calendar.MINUTE)));

								pbParcelable
										.setMapPickUpLocation(defaultPickUpLocation);
								pbParcelable
										.setMapDropOffLocation(defaultDropOffLocation);
								pbParcelable
										.setePickUpSelected(EnumLocations.MAPLOCATION);
								pbParcelable
										.seteDropOffSelected(EnumLocations.MAPLOCATION);

								BookingDetailsFragment edit = new BookingDetailsFragment();
								pending_dialog.dismiss();
								FragmentTransaction ft = fragment
										.getParentFragment()
										.getChildFragmentManager()
										.beginTransaction();
								Bundle parcelable = new Bundle();
								parcelable.putParcelable("pbParcelable",
										pbParcelable);
								edit.setArguments(parcelable);
								ft.addToBackStack("myBookingsFragment");
								ft.replace(R.id.frame_container, edit,
										"bookingsDetailsFragment");
								ft.commit();

							}
						});
						view_cancel.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								pending_dialog.dismiss();
								CancelBookingTask task = new CancelBookingTask(
										mContext);
								task.booking_id = bookingsList.get(position)
										.getId();
								;
								task.handler = cancelbooking_handler;
								task.execute();
							}
						});
						// if button is clicked, open full image
						view_close.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								pending_dialog.dismiss();
							}
						});
						pending_dialog.show();

					} else if (status.equals("Booked")) {

						Bookings_Driver_DetailsFragment edit = new Bookings_Driver_DetailsFragment(
								false, bookingsList.get(position).getId());

						FragmentTransaction ft = fragment.getParentFragment()
								.getChildFragmentManager().beginTransaction();
						ft.addToBackStack("myBookingsFragment");
						ft.replace(R.id.frame_container, edit,
								"bookingsDriverDetailsFragment");
						ft.commit();
					} else if (status.equals("Cancelled")) {
						Constant.alertdialog("Cancelled", mContext);
					}
				}
			}

		}
	}

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}
}
