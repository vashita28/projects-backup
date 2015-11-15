package com.android.cabapp.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.fragments.JobsFragment;
import com.android.cabapp.fragments.RootFragment;
import com.android.cabapp.model.AcceptJob;
import com.android.cabapp.model.DriverAccountDetails;
import com.android.cabapp.model.DriverSettingDetails;
import com.android.cabapp.model.RejectJobs;
import com.android.cabapp.util.AppValues;
import com.android.cabapp.util.Connection;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.NetworkUtil;
import com.android.cabapp.util.Util;
import com.androidquery.AQuery;
import com.flurry.android.FlurryAgent;

public class NowJobsExpandableListAdapter extends BaseExpandableListAdapter
		implements Filterable {

	// private Context mContext;
	private List<Job> jobsList, jobsListOriginal;
	private ExpandableListView mExpListView;
	AQuery query;

	Location currentLocation;
	// String szDistancePickUp, szDistanceDrop;

	ProgressDialog acceptDialog;
	boolean bIsNowFragment;

	HashMap<String, Distance> mapDistance = new HashMap<String, Distance>();

	public NowJobsExpandableListAdapter(Context context, List<Job> jobsList,

	ExpandableListView expListView, boolean isNowFragment) {
		// this.mContext = context;
		this.jobsListOriginal = jobsList;
		this.jobsList = jobsList;
		this.mExpListView = expListView;
		this.bIsNowFragment = isNowFragment;
		query = new AQuery(context);

		if (Util.getLocation(Util.mContext) != null)
			currentLocation = Util.getLocation(Util.mContext);
		// else if (currentLocation == null)
		// currentLocation = Util.getLocationByProvider(mContext,
		// LocationManager.GPS_PROVIDER);
		// else if (currentLocation == null)
		// currentLocation = Util.getLocationByProvider(mContext,
		// LocationManager.NETWORK_PROVIDER);

		// currentLocation = AppValues.currentLocation;

		Log.e("NowJobsExpandableListAdapter", "Current Location:: "
				+ currentLocation);
	}

	public void updateList(List<Job> jobsList) {
		this.jobsListOriginal = jobsList;
		this.jobsList = jobsList;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.jobsList.get(groupPosition);

	}

	public List<Job> getCurrentList() {
		return jobsList;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getListCount() {
		if (jobsList != null)
			return jobsList.size();
		else
			return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		// final String childText = (String) getChild(groupPosition,
		// childPosition);

		RelativeLayout rlAcceptButton = null, rlRejectButton = null;

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) Util.mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.row_jobs_footer, null);
		}

		final Job job = this.jobsList.get(groupPosition);

		final TextView txtPassengerCount = (TextView) convertView
				.findViewById(R.id.txtPassengerCount);
		ImageView ivWheelChair = (ImageView) convertView
				.findViewById(R.id.ivWheelchair);
		final TextView txtLuggage = (TextView) convertView
				.findViewById(R.id.txtLuggage);
		ivWheelChair.setVisibility(View.GONE);
		ImageView ivHearingImpaired = (ImageView) convertView
				.findViewById(R.id.ivHearingImpaired);

		if (this.jobsList.get(groupPosition).getNumberOfPassengers() <= 5
				&& Util.getCitySelectedLondon(Util.mContext)) {
			txtPassengerCount.setText("1-5");
		} else if (this.jobsList.get(groupPosition).getNumberOfPassengers() <= 4) {
			txtPassengerCount.setText("1-4");
		} else {
			txtPassengerCount.setText(String.valueOf(this.jobsList.get(
					groupPosition).getNumberOfPassengers()));
		}

		// Wheelchair:
		if (job.getWheelchairAccessRequired().equals("true")) {
			ivWheelChair.setVisibility(View.VISIBLE);
		} else
			ivWheelChair.setVisibility(View.GONE);

		// Hearing impairment:
		String isHearingImpaired = job.getIsHearingImpaired();
		if (isHearingImpaired.equals("true"))
			ivHearingImpaired.setVisibility(View.VISIBLE);
		else
			ivHearingImpaired.setVisibility(View.GONE);

		// Note:
		if (job.getNote() != null && !job.getNote().equals("")) {
			txtLuggage.setText("\"" + job.getNote() + "\"");
		} else {
			txtLuggage.setText("");
		}

		rlAcceptButton = (RelativeLayout) convertView
				.findViewById(R.id.rl_btnAccept);
		rlRejectButton = (RelativeLayout) convertView
				.findViewById(R.id.rl_btnReject);

		if (groupPosition % 2 == 0) {
			convertView.setBackgroundColor(Util.mContext.getResources()
					.getColor(R.color.list_item_even_bg));
		} else {
			convertView.setBackgroundColor(Util.mContext.getResources()
					.getColor(R.color.list_item_odd_bg));
		}

		rlAcceptButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkUtil.isNetworkOn(Util.mContext)) {
					acceptDialog = new ProgressDialog(Util.mContext);
					acceptDialog.setMessage("Loading...");
					acceptDialog.setIndeterminate(true);
					acceptDialog.setCancelable(false);
					acceptDialog.show();
					AcceptJobTask acceptTask = new AcceptJobTask();
					acceptTask.childPosition = childPosition;
					acceptTask.groupPosition = groupPosition;
					acceptTask.job = job;
					// acceptTask.distanceDrop = szDistanceDrop;
					// acceptTask.distancePickUp = szDistancePickUp;

					acceptTask.execute();
				} else {
					Util.showToastMessage(Util.mContext,
							Util.mContext.getString(R.string.no_network_error),
							Toast.LENGTH_LONG);
				}

			}
		});

		rlRejectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkUtil.isNetworkOn(Util.mContext)) {
					acceptDialog = new ProgressDialog(Util.mContext);
					acceptDialog.setMessage("Loading...");
					acceptDialog.setIndeterminate(true);
					acceptDialog.setCancelable(false);
					acceptDialog.show();
					RejectJobTask rejectTask = new RejectJobTask();
					rejectTask.childPosition = childPosition;
					rejectTask.groupPosition = groupPosition;
					rejectTask.job = job;
					rejectTask.execute();
				} else {
					Util.showToastMessage(Util.mContext,
							Util.mContext.getString(R.string.no_network_error),
							Toast.LENGTH_LONG);
				}
			}
		});

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.jobsList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		if (jobsList != null)
			return jobsList.size();
		else
			return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// String headerTitle = (String) getGroup(groupPosition);
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) Util.mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.row_jobs, null);
			holder = new ViewHolder();
			holder.txtPickupAddress = (TextView) convertView
					.findViewById(R.id.txtPickupAddress);
			holder.txtPickUpPinCode = (TextView) convertView
					.findViewById(R.id.txtPickupPinCode);
			holder.txtDistancePickup = (TextView) convertView
					.findViewById(R.id.txtDistancePickup);
			holder.txtDropAddress = (TextView) convertView
					.findViewById(R.id.txtDropAddress);
			holder.txtDropPinCode = (TextView) convertView
					.findViewById(R.id.txtDropPinCode);
			holder.txtDistanceDrop = (TextView) convertView
					.findViewById(R.id.txtDistanceDrop);
			holder.txtFare = (TextView) convertView.findViewById(R.id.txtFare);
			holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
			holder.ivPayment = (ImageView) convertView
					.findViewById(R.id.ivPayment);
			holder.txtDistanceUnit = (TextView) convertView
					.findViewById(R.id.txtDistanceUnit);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (Util.mContext != null) {
			holder.txtDistanceUnit.setText(Util
					.getDistanceFormat(Util.mContext));
		}

		holder.txtDistancePickup.setText("0");
		holder.txtDistanceDrop.setText("0");
		holder.txtFare.setText("-");

		Job job = this.jobsList.get(groupPosition);

		// Calendar calendarPickup = Calendar.getInstance();
		// calendarPickup.setTime(new Date(job.getPickupDateTime()));
		// holder.txtTime.setText(String.format("%02d:%02d",
		// calendarPickup.get(Calendar.HOUR_OF_DAY),
		// calendarPickup.get(Calendar.MINUTE)));

		String date = Util.getTimeFormat(job.getPickupDateTime());
		holder.txtTime.setText(date);

		// Set pick up address
		String pickupAddress = "", pickUpWithoutNumber = "", firstAdd = "";
		pickupAddress = job.getPickupLocation().getAddressLine1();
		try {
			firstAdd = pickupAddress.substring(0, pickupAddress.indexOf(" "));
		} catch (IndexOutOfBoundsException e) {

		}
		if (firstAdd.matches(".*[0-9].*")) {
			pickUpWithoutNumber = pickupAddress.replace(firstAdd, "");
			pickUpWithoutNumber = pickUpWithoutNumber.trim();
		} else {
			pickUpWithoutNumber = pickupAddress;
		}

		if (pickUpWithoutNumber.contains(","))
			pickUpWithoutNumber = pickUpWithoutNumber.substring(0,
					pickUpWithoutNumber.lastIndexOf(","));
		holder.txtPickupAddress.setText(pickUpWithoutNumber);

		// DropOff Address
		String dropOffAddress = "", dropOffWithoutNumber = "", dropAdd = "";
		dropOffAddress = job.getDropLocation().getAddressLine1();
		dropAdd = dropOffAddress.substring(0, dropOffAddress.indexOf(" "));
		if (dropAdd.matches(".*[0-9].*")) {
			dropOffWithoutNumber = dropOffAddress.replace(dropAdd, "");
			dropOffWithoutNumber = dropOffWithoutNumber.trim();
		} else {
			dropOffWithoutNumber = dropOffAddress;
		}

		if (dropOffWithoutNumber.contains(","))
			dropOffWithoutNumber = dropOffWithoutNumber.substring(0,
					dropOffWithoutNumber.lastIndexOf(","));
		holder.txtDropAddress.setText(dropOffWithoutNumber);

		// Set PickUp Pincode
		String szPickUpPostCode = job.getPickupLocation().getPostCode().trim();
		if (!szPickUpPostCode.isEmpty() && szPickUpPostCode.contains(" ")) {
			String sTruncatedPostCode = szPickUpPostCode.substring(0,
					szPickUpPostCode.indexOf(" "));
			holder.txtPickUpPinCode.setText(sTruncatedPostCode);
		} else if (!szPickUpPostCode.isEmpty())
			holder.txtPickUpPinCode.setText(job.getPickupLocation()
					.getPostCode());

		// Set DropOff Pincode
		String szDropOffPostCode = job.getDropLocation().getPostCode().trim();
		if (!szDropOffPostCode.isEmpty() && szDropOffPostCode.contains(" ")) {
			String sTruncatedDropOffPostCode = szDropOffPostCode.substring(0,
					szDropOffPostCode.indexOf(" "));
			holder.txtDropPinCode.setText(sTruncatedDropOffPostCode);
		} else if (!szDropOffPostCode.isEmpty())
			holder.txtDropPinCode.setText(job.getDropLocation().getPostCode());

		if (job.getDropLocation().getAddressLine1()
				.equalsIgnoreCase("As Directed")) {
			holder.txtDistanceDrop.setText("-");
		} else {
			if (!mapDistance.containsKey(job.getId()
					+ Util.getDistanceFormat(Util.mContext))) {
				CalculateDistanceTask calculateDistance = new CalculateDistanceTask();
				calculateDistance.jobID = job.getId();
				calculateDistance.pickupLat = job.getPickupLocation()
						.getLatitude();
				calculateDistance.pickupLng = job.getPickupLocation()
						.getLongitude();
				if (currentLocation != null) {
					calculateDistance.userLat = String.valueOf(currentLocation
							.getLatitude());
					calculateDistance.userLng = String.valueOf(currentLocation
							.getLongitude());
				}
				calculateDistance.dropoffLat = job.getDropLocation()
						.getLatitude();
				calculateDistance.dropoffLng = job.getDropLocation()
						.getLongitude();
				calculateDistance.holder = holder;
				calculateDistance.txtviewDistanceDropoff = holder.txtDistanceDrop;
				calculateDistance.txtviewDistancePickup = holder.txtDistancePickup;
				if (job.getDropLocation().getAddressLine1()
						.equalsIgnoreCase("As Directed"))
					calculateDistance.bIsAsDirected = true;

				// calculateDistance.calculateAndDisplayDistancePickupAndDropOff();
				// szDistanceDrop = holder.txtDistanceDrop.getText().toString();
				// szDistancePickUp =
				// holder.txtDistancePickup.getText().toString();
				calculateDistance.execute();
			} else {
				Distance distance = mapDistance.get(job.getId()
						+ Util.getDistanceFormat(Util.mContext));
				holder.txtDistanceDrop.setText(distance.szDistanceDropoff);
				holder.txtDistancePickup.setText(distance.szDistancePickup);
			}
		}

		// Fixed Price/ On Meter
		if (job.getFixedPriceAmount() == null
				|| job.getFixedPriceAmount().equals("")) {
			holder.txtFare.setText("On Meter");
		} else {
			if (AppValues.driverSettings != null
					&& AppValues.driverSettings.getCurrencySymbol() != null)
				holder.txtFare.setText(AppValues.driverSettings
						.getCurrencySymbol() + job.getFixedPriceAmount());
			else
				holder.txtFare.setText(job.getFixedPriceAmount());
		}

		// Payment type
		if (job.getPaymentType().equals("cash"))
			holder.ivPayment.setImageResource(R.drawable.cashblack);
		else
			holder.ivPayment.setImageResource(R.drawable.paymentblack);

		if (groupPosition % 2 == 0) {
			convertView.setBackgroundColor(Util.mContext.getResources()
					.getColor(R.color.list_item_even_bg));
		} else {
			convertView.setBackgroundColor(Util.mContext.getResources()
					.getColor(R.color.list_item_odd_bg));
		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public void removeGroup(int group) {
		// TODO: Remove the according group.
		Log.v("NowJobsExpandableListAdapter", "Removing group: " + group);
		try {
			this.jobsList.remove(group);
		} catch (Exception e) {

		}
		notifyDataSetChanged();
	}

	public void removeChild(int group, int child) {
		// TODO: Remove the according child
		// Log.v("NowJobsExpandableListAdapter", "Removing child: " + child
		// + " in group " + group);
		// this._listDataChild.remove(this._listDataHeader.get(group)).get(child);
		// notifyDataSetChanged();
	}

	class CalculateDistanceTask extends AsyncTask<String, Void, String> {
		String pickupLat, pickupLng, dropoffLat, dropoffLng, userLat, userLng;
		ViewHolder holder;
		TextView txtviewDistanceDropoff, txtviewDistancePickup;
		String szPickupToDropOffDistance = "", szUserToPickupDistance = "";
		String jobID;
		boolean bIsAsDirected = false;

		public void calculateAndDisplayDistancePickupAndDropOff() {

			// url =
			// "http://maps.googleapis.com/maps/api/distancematrix/json?origins=53.545658,-2.732965&destinations=53.545650,-2.732960&mode=driving&language=en-EN&sensor=false";

			// query.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>()
			// {
			//
			// @Override
			// public void callback(String url, JSONObject json,
			// AjaxStatus status) {
			// if (json != null) {
			//
			// // successful ajax call, show status code and json
			// // content
			// // Toast.makeText(query.getContext(),
			// // status.getCode() + ":" + json.toString(),
			// // Toast.LENGTH_LONG).show();
			// try {
			// JSONObject jObject = new JSONObject(json.toString());
			//
			// String distance = jObject.getJSONArray("rows")
			// .getJSONObject(0).getJSONArray("elements")
			// .getJSONObject(0).getJSONObject("distance")
			// .getString("text");
			// Log.e("Distance", "Distance " + distance);
			// if (distance != null) {
			// distance = distance.replace("km", "");
			// txtviewDistanceDropoff.setText(Util
			// .getDistanceInMiles(Double
			// .parseDouble(distance)));
			// }
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// } else {
			//
			// // ajax error, show error code
			// // Toast.makeText(query.getContext(),
			// // "Error:" + status.getCode(),
			// // Toast.LENGTH_LONG).show();
			// }
			// }
			// });
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url;
			// http://maps.googleapis.com/maps/api/distancematrix/json?origins=54.406505,18.67708&destinations=54.446251,18.570993&mode=driving&language=en-EN&sensor=false
			// url =
			// "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&language=en-EN&sensor=false";
			url = "http://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&mode=driving&units=imperial&alternatives=true&sensor=false";
			url = String.format(url, pickupLat, pickupLng, dropoffLat,
					dropoffLng);
			Log.e("NowJobsExpandableAdapter", "mapsURLPickupToDropOff:: " + url);

			Connection connection = new Connection(Util.mContext);
			connection.mapURL = url;
			connection.connect(Constants.MAP_DISTANCE);
			try {
				szPickupToDropOffDistance = Util.parseMapDistanceData(
						connection.inputStreamToString(connection.mInputStream)
								.toString(), Util.mContext);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// url =
			// "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&language=en-EN&sensor=false";
			url = "http://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&mode=driving&units=imperial&alternatives=true&sensor=false";
			url = String.format(url, userLat, userLng, pickupLat, pickupLng);
			Log.e("NowJobsExpandableAdapter", "mapsURLUserToPickup:: " + url);

			connection = new Connection(Util.mContext);
			connection.mapURL = url;
			connection.connect(Constants.MAP_DISTANCE);

			try {
				szUserToPickupDistance = Util.parseMapDistanceData(connection
						.inputStreamToString(connection.mInputStream)
						.toString(), Util.mContext);
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

			if (Util.mContext != null) {
				if (!szPickupToDropOffDistance.equals("-")
						&& !szPickupToDropOffDistance.equals(""))
					txtviewDistanceDropoff.setText(Util.getDistance(
							Util.mContext,
							Double.parseDouble(szPickupToDropOffDistance),
							Util.getDistanceFormat(Util.mContext)));
				else {
					if (bIsAsDirected)
						txtviewDistanceDropoff.setText("-");
					else
						txtviewDistanceDropoff.setText("0");
				}

				if (!szUserToPickupDistance.equals("-")
						&& !szUserToPickupDistance.equals(""))
					txtviewDistancePickup.setText(Util.getDistance(
							Util.mContext,
							Double.parseDouble(szUserToPickupDistance),
							Util.getDistanceFormat(Util.mContext)));
				else
					txtviewDistancePickup.setText("0");
			}

			// else {
			// if (!szPickupToDropOffDistance.equals("-")
			// && !szPickupToDropOffDistance.equals(""))
			// txtviewDistanceDropoff
			// .setText(Util.getDistance(Util.mContext, Double
			// .parseDouble(szPickupToDropOffDistance),
			// "km"));
			// else {
			// if (bIsAsDirected)
			// txtviewDistanceDropoff.setText("-");
			// else
			// txtviewDistanceDropoff.setText("0");
			//
			// }
			//
			// if (!szUserToPickupDistance.equals("-")
			// && !szUserToPickupDistance.equals(""))
			// txtviewDistancePickup.setText(Util.getDistance(
			// Util.mContext,
			// Double.parseDouble(szUserToPickupDistance), "km"));
			// else
			// txtviewDistancePickup.setText("0");
			// }

			Distance distance = new Distance();
			distance.szDistanceDropoff = txtviewDistanceDropoff.getText()
					.toString();
			distance.szDistancePickup = txtviewDistancePickup.getText()
					.toString();
			mapDistance.put(jobID + Util.getDistanceFormat(Util.mContext),
					distance);
		}
	}

	class ViewHolder {
		TextView txtPickupAddress, txtPickUpPinCode, txtDistancePickup,
				txtDropPinCode, txtDropAddress, txtDistanceDrop, txtFare,
				txtTime, txtDistanceUnit;

		ImageView ivPayment;
	}

	class AcceptJobTask extends AsyncTask<String, Void, String> {
		Job job;
		int childPosition, groupPosition;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			AcceptJob acceptJob = new AcceptJob(job.getId(), Util.mContext);
			if (acceptJob.isJobAccepted()) {
				FlurryAgent.logEvent("Prebook Job Accepted");

				DriverAccountDetails driverAccount = new DriverAccountDetails(
						Util.mContext);
				driverAccount.retriveAccountDetails(Util.mContext);

				DriverSettingDetails driverSettings = new DriverSettingDetails(
						Util.mContext);
				driverSettings.retriveDriverSettings(Util.mContext);

				RootFragment.szJobID = job.getId();
				RootFragment.szPaymentType = job.getPaymentType();
				return "true";
			} else if (acceptJob.errorMessage != null)
				return acceptJob.errorMessage;

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (acceptDialog != null)
				acceptDialog.dismiss();
			if (result != null) {
				if (result.equals("true")) {
					Fragment fragment = null;
					fragment = new com.android.cabapp.fragments.JobAcceptedFragment();
					if (fragment != null) {
						// FragmentManager fragmentManager =
						// ((FragmentActivity)
						// _context)
						// .getSupportFragmentManager();
						// fragmentManager.beginTransaction()
						// .replace(R.id.frame_container, fragment)
						// .addToBackStack("JobAcceptedFragment").commit();

						Bundle bundle = new Bundle();
						bundle.putBoolean(Constants.IS_PREBOOK, true);
						String szPickUpAddress = job.getPickupLocation()
								.getAddressLine1();
						if (szPickUpAddress.contains(","))
							szPickUpAddress = szPickUpAddress.substring(0,
									szPickUpAddress.lastIndexOf(","));
						bundle.putString(Constants.PICK_UP_ADDRESS,
								szPickUpAddress);
						bundle.putString(Constants.PICK_UP_PINCODE, job
								.getPickupLocation().getPostCode());

						String szDropOffAddress = job.getDropLocation()
								.getAddressLine1();
						if (szDropOffAddress.contains(","))
							szDropOffAddress = szDropOffAddress.substring(0,
									szDropOffAddress.lastIndexOf(","));
						bundle.putString(Constants.DROP_ADDRESS,
								szDropOffAddress);
						bundle.putString(Constants.DROP_OFF_PINCODE, job
								.getDropLocation().getPostCode());
						bundle.putString(Constants.NO_OF_PASSENGERS,
								String.valueOf(job.getNumberOfPassengers()));
						bundle.putString(Constants.WHEEL_CHAIR_ACCESS_REQUIRED,
								job.getWheelchairAccessRequired());
						bundle.putString(Constants.HEARING_IMPAIRED,
								job.getIsHearingImpaired());
						bundle.putString(Constants.PICK_UP_LOCATION_LAT, job
								.getPickupLocation().getLatitude());
						bundle.putString(Constants.PICK_UP_LOCATION_LNG, job
								.getPickupLocation().getLongitude());
						bundle.putString(Constants.DROP_LOCATION_LAT, job
								.getDropLocation().getLatitude());
						bundle.putString(Constants.DROP_LOCATION_LNG, job
								.getDropLocation().getLongitude());
						bundle.putString(Constants.PASSENGER_NAME,
								job.getName());
						bundle.putString(Constants.FARE,
								job.getFixedPriceAmount());
						bundle.putString(Constants.JOB_PAYMENT_TYPE,
								job.getPaymentType());
						bundle.putString(Constants.TIME,
								job.getPickupDateTime());
						// Put is cabShare
						if (job.getCabShare() != null
								&& !job.getCabShare().isEmpty()) {
							bundle.putString(Constants.CAB_SHARE,
									job.getCabShare());
						}
						// Put cab miles
						if (job.getCabMiles() != null
								&& !job.getCabMiles().isEmpty()) {
							bundle.putString(Constants.CAB_MILES,
									job.getCabMiles());
						}
						if (mapDistance.containsKey(job.getId()
								+ Util.getDistanceFormat(Util.mContext))) {
							bundle.putString(
									Constants.DISTANCE_DROP,
									mapDistance.get(job.getId()
											+ Util.getDistanceFormat(Util.mContext)).szDistanceDropoff);
							bundle.putString(
									Constants.DISTANCE_PICKUP,
									mapDistance.get(job.getId()
											+ Util.getDistanceFormat(Util.mContext)).szDistancePickup);
						}
						bundle.putString(Constants.LUGGAGE_NOTE, job.getNote());
						bundle.putBoolean("isfrommyjobs", false);
						bundle.putBoolean("ishistory", false);
						bundle.putString(Constants.JOB_ID, job.getId());
						bundle.putString(
								Constants.PASSENGER_INTERNATIONAL_CODE,
								job.getInternationalCode());
						bundle.putString(Constants.PASSENGER_NUMBER,
								job.getMobileNumber());
						bundle.putString(Constants.PASSENGER_EMAIL,
								job.getEmailAddress());
						fragment.setArguments(bundle);
						removeChild(groupPosition, childPosition);
						removeGroup(groupPosition);
						mExpListView.collapseGroup(groupPosition);
						updateTabsTitle();
						((MainActivity) Util.mContext).replaceFragment(
								fragment, false);
					}

				} else {
					Util.showToastMessage(Util.mContext, result,
							Toast.LENGTH_LONG);
				}

			}
		}
	}

	void updateTabsTitle() {
		if (jobsList != null) {
			if (bIsNowFragment) {
				AppValues.nNowJobsFilteredCount = jobsList.size();
				JobsFragment.setNowTabTitle("Now ("
						+ AppValues.nNowJobsFilteredCount + ")");
			} else {
				AppValues.nPreBookJbsFilteredCount = jobsList.size();
				JobsFragment.setPreBookTabTitle("Pre-book ("
						+ AppValues.nPreBookJbsFilteredCount + ")");
			}
		}
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub

		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

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

							String paymentMethod = jobsListOriginal.get(i)
									.getPaymentType().toString();
							if (paymentMethod.toLowerCase().contains("card")) {
								filteredJobsList.add(jobsListOriginal.get(i));
							}

							results.count = filteredJobsList.size();
							results.values = filteredJobsList;

						} else if (constraint.equals("filterbycash")) {
							String paymentMethod = jobsListOriginal.get(i)
									.getPaymentType().toString();
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

	class Distance {
		String szDistancePickup;
		String szDistanceDropoff;
	}

	public class RejectJobTask extends AsyncTask<String, Void, String> {
		Job job;
		int childPosition, groupPosition;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			RejectJobs rejectJobs = new RejectJobs(Util.mContext, job.getId());
			String response = rejectJobs.rejectSelectedJob();
			try {
				JSONObject jObject = new JSONObject(response);

				if (jObject.has("success")
						&& jObject.getString("success").equals("true")) {
					FlurryAgent.logEvent("Prebook Job Rejected");
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						String errorMessage = jErrorsArray.getJSONObject(0)
								.getString("message");
						return errorMessage;
					}
				}
				return "success";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (acceptDialog != null)
				acceptDialog.dismiss();

			if (result.equals("success")) {

				removeChild(groupPosition, childPosition);
				removeGroup(groupPosition);
				mExpListView.collapseGroup(groupPosition);
				updateTabsTitle();
				Util.showToastMessage(Util.mContext,
						"Job rejected successfully", Toast.LENGTH_LONG);
			} else {
				Util.showToastMessage(Util.mContext, result, Toast.LENGTH_LONG);
			}

		}

	}

}