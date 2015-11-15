package com.android.cabapp.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.fragments.JobsFragment;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;
import com.androidquery.AQuery;

public class NowJobsAdapter extends BaseAdapter implements Filterable {

	private Context mContext;
	private List<Job> jobsList;
	private List<Job> jobsListOriginal;
	AQuery query;

	Location currentLocation;
	// String szDistancePickUp, szDistanceDrop;

	ProgressDialog acceptDialog;
	boolean bIsNowFragment;

	SimpleDateFormat dfDate_day = new SimpleDateFormat("HH:mm:ss");
	Calendar c = Calendar.getInstance();
	String currentDateandTime;
	String sPassengerdateTime;

	ViewHolder holder = null;

	HashMap<String, Distance> hmMapDistance = new HashMap<String, Distance>();

	public NowJobsAdapter(Context context, List<Job> jobsList,

	boolean isNowFragment) {
		this.mContext = context;
		this.jobsListOriginal = jobsList;
		this.jobsList = jobsList;
		this.bIsNowFragment = isNowFragment;
		query = new AQuery(context);

		if (Util.getLocation(mContext) != null)
			currentLocation = Util.getLocation(mContext);
		if (Constants.isDebug)
			Log.e("NowJobsAdapter", "Current Location:: " + currentLocation);
	}

	public void updateList(List<Job> jobsList) {
		this.jobsListOriginal = jobsList;
		this.jobsList = jobsList;
	}

	public int getListCount() {
		if (jobsList != null)
			return jobsList.size();
		else
			return 0;
	}

	class ViewHolder {
		TextView txtPickupAddress, txtDistancePickup, txtTime, txtDistanceUnit, txtPickUpPincode, txtPickUpTimeCounter;
	}

	public List<Job> getCurrentList() {
		return jobsList;
	}

	public void updateTabsTitle() {
		if (this.jobsList != null) {
			AppValues.nNowJobsFilteredCount = jobsList.size();
			JobsFragment.setNowTabTitle("Now (" + AppValues.nNowJobsFilteredCount + ")");
		}
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub

		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {

				jobsList = (List<Job>) results.values;
				notifyDataSetChanged();
				updateTabsTitle();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				ArrayList<Job> filteredJobsList = new ArrayList<Job>();

				if (constraint == null || constraint.length() == 0) {
					results.count = jobsListOriginal.size();
					results.values = jobsListOriginal;
				} else {
					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < jobsListOriginal.size(); i++) {
						if (constraint.equals("filterbycard")) {

							String paymentMethod = jobsListOriginal.get(i).getPaymentType().toString();
							if (paymentMethod.toLowerCase().contains("card")) {
								filteredJobsList.add(jobsListOriginal.get(i));
							}

							results.count = filteredJobsList.size();
							results.values = filteredJobsList;

						} else if (constraint.equals("filterbycash")) {
							String paymentMethod = jobsListOriginal.get(i).getPaymentType().toString();
							if (paymentMethod.toLowerCase().contains("cash")) {
								filteredJobsList.add(jobsListOriginal.get(i));
							}

							results.count = filteredJobsList.size();
							results.values = filteredJobsList;

						} else if (constraint.equals("filterbyall")) {

							results.count = jobsListOriginal.size();
							results.values = jobsListOriginal;
						}
					}
				}

				return results;
			}
		};

		return filter;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (jobsList != null)
			return jobsList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return jobsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Job job = this.jobsList.get(position);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.row_now_jobs, null);
			holder = new ViewHolder();
			holder.txtPickupAddress = (TextView) convertView.findViewById(R.id.txtPickupAddress);
			holder.txtPickUpPincode = (TextView) convertView.findViewById(R.id.txtPickupPincode);
			holder.txtDistancePickup = (TextView) convertView.findViewById(R.id.txtDistancePickup);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
			holder.txtDistanceUnit = (TextView) convertView.findViewById(R.id.txtDistanceUnit);
			holder.txtPickUpTimeCounter = (TextView) convertView.findViewById(R.id.txtTimeStatus);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (Util.mContext != null) {
			holder.txtDistanceUnit.setText(Util.getDistanceFormat(mContext));
		}

		if (!hmMapDistance.containsKey(job.getId() + Util.getDistanceFormat(Util.mContext))) {
			CalculateDistanceTask calculateDistance = new CalculateDistanceTask();
			calculateDistance.jobID = job.getId();
			calculateDistance.pickupLat = job.getPickupLocation().getLatitude();
			calculateDistance.pickupLng = job.getPickupLocation().getLongitude();
			if (currentLocation != null) {
				calculateDistance.userLat = String.valueOf(currentLocation.getLatitude());
				calculateDistance.userLng = String.valueOf(currentLocation.getLongitude());
			}
			calculateDistance.holder = holder;
			calculateDistance.txtviewDistancePickup = holder.txtDistancePickup;
			calculateDistance.execute();
		} else {
			holder.txtDistancePickup
					.setText(hmMapDistance.get(job.getId() + Util.getDistanceFormat(Util.mContext)).szDistancePickup);
		}

		// Calendar calendarPickup = Calendar.getInstance();
		// calendarPickup.setTime(new Date(job.getPickupDateTime()));
		// holder.txtTime.setText(String.format("%02d:%02d",
		// calendarPickup.get(Calendar.HOUR_OF_DAY),
		// calendarPickup.get(Calendar.MINUTE)));

		// Set time
		String date = Util.getTimeFormat(job.getPickupDateTime());
		holder.txtTime.setText(date);

		// Set pick up address
		String pickupAddress = "", pickUpWithoutNumber = "", firstAdd = "";
		pickupAddress = job.getPickupLocation().getAddressLine1();
		if (pickupAddress.contains(" "))
			firstAdd = pickupAddress.substring(0, pickupAddress.indexOf(" "));
		if (firstAdd.matches(".*[0-9].*")) {
			// pickUpWithoutNumber = pickupAddress.replaceAll("[0-9]|-|/",
			// "");// \\d
			// pickUpWithoutNumber = pickUpWithoutNumber.substring(
			// pickUpWithoutNumber.indexOf(" ")).trim();
			pickUpWithoutNumber = pickupAddress.replace(firstAdd, "");
			pickUpWithoutNumber = pickUpWithoutNumber.trim();
		} else {
			pickUpWithoutNumber = pickupAddress;
		}

		if (pickUpWithoutNumber.contains(","))
			pickUpWithoutNumber = pickUpWithoutNumber.substring(0, pickUpWithoutNumber.lastIndexOf(","));
		holder.txtPickupAddress.setText(pickUpWithoutNumber);

		// Set PostCode
		String szPostCode = job.getPickupLocation().getPostCode().trim();
		if (szPostCode.contains(" ")) {
			String sTruncatedPostCode = szPostCode.substring(0, szPostCode.indexOf(" "));
			holder.txtPickUpPincode.setText(sTruncatedPostCode);
		} else
			holder.txtPickUpPincode.setText(szPostCode);

		// set PickupDateTime counter
		sPassengerdateTime = job.getPickupDateTime();

		holder.txtPickUpTimeCounter.setVisibility(View.VISIBLE);
		currentDateandTime = dfDate_day.format(new Date());
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

		Date dPassengerJobtime, dCurrentTime;
		final int dif;
		try {
			dPassengerJobtime = format.parse(Util.getTimeOnly((sPassengerdateTime)).toString());
			dCurrentTime = format.parse(currentDateandTime);
			dif = (int) (dPassengerJobtime.getTime() / 60000 - dCurrentTime.getTime() / 60000);

			if (dif < 0) {
				holder.txtPickUpTimeCounter.setTextColor(mContext.getResources().getColor(R.color.button_red));
			} else {
				holder.txtPickUpTimeCounter.setTextColor(mContext.getResources().getColor(R.color.button_green));
			}

			holder.txtPickUpTimeCounter.setText("(" + dif + " mins)");

			// if ((dif < -60) || (dif > 60)) {
			// More than 1 hour
			// if (Constants.isDebug)
			// Log.e("NowJobsAdapter", "Diff >60 " + dif);

			// holder.txtPickUpTimeCounter.setText("(" + dif / 60 + "h "
			// + -(dif % 60) + " mins)");
			// } else {
			// if (Constants.isDebug)
			// Log.e("NowJobsAdapter", "Diff <60 " + dif);
			// holder.txtPickUpTimeCounter.setText("(" + dif + " mins)");
			// }

			// if (Constants.isDebug)
			// Log.e("NowJobsAdapter",
			// "Time Passenger"
			// + Util.getTimeOnly((sPassengerdateTime))
			// + " Time Device  " + currentDateandTime
			// + " Time Differnce " + dif);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (position % 2 == 0) {
			convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.list_item_even_bg));
		} else {
			convertView.setBackgroundColor(this.mContext.getResources().getColor(R.color.list_item_odd_bg));
		}
		return convertView;

	}

	// public void removeItem(int position) {
	// // TODO: Remove the according item.
	// try {
	// jobsList.remove(position);
	// notifyDataSetChanged();
	// updateTabsTitle();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	class CalculateDistanceTask extends AsyncTask<String, Void, String> {
		String pickupLat, pickupLng, userLat, userLng;

		ViewHolder holder;
		TextView txtviewDistancePickup;
		String szPickupToDropOffDistance = "", szUserToPickupDistance = "";
		String jobID;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url;

			// distance matrix
			// url =
			// "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&language=en-EN&sensor=false";
			// url = String.format(url, userLat, userLng, pickupLat, pickupLng);
			// Log.e("NowJobsAdapter", "mapsURLUserToPickup:: " + url);

			// directions
			url = "http://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&mode=driving&units=imperial&alternatives=true&sensor=false";
			url = String.format(url, userLat, userLng, pickupLat, pickupLng);
			if (Constants.isDebug)
				Log.d("NowJobsAdapter", "mapsURLUserToPickup:: " + url);

			Connection connection = new Connection(mContext);
			connection.mapURL = url;
			connection.connect(Constants.MAP_DISTANCE);

			try {
				szUserToPickupDistance = Util.parseMapDistanceData(connection.inputStreamToString(connection.mInputStream)
						.toString(), mContext);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (Util.getDistanceFormat(mContext).equals("km")) {
				if (!szUserToPickupDistance.equals("-") && !szUserToPickupDistance.equals(""))
					txtviewDistancePickup.setText(Util.getDistance(mContext, (Double.parseDouble(szUserToPickupDistance)), "km"));
				else
					txtviewDistancePickup.setText("0");

			} else {
				if (!szUserToPickupDistance.equals("-") && !szUserToPickupDistance.equals(""))
					txtviewDistancePickup.setText(Util.getDistance(mContext, (Double.parseDouble(szUserToPickupDistance)),
							"miles"));
				else
					txtviewDistancePickup.setText("0");
			}

			Distance distance = new Distance();
			distance.szDistancePickup = txtviewDistancePickup.getText().toString();
			hmMapDistance.put(jobID + Util.getDistanceFormat(Util.mContext), distance);

			if (Constants.isDebug)
				Log.e("NowJobsAdapter", "hmMapDistance:: " + distance);
		}
	}

	class Distance {
		String szDistancePickup;
	}

}
