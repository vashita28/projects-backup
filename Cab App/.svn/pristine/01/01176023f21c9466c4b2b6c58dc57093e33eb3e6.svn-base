package com.android.cabapp.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.datastruct.json.Job;
import com.android.cabapp.fragments.NowFragment;
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
import com.flurry.android.FlurryAgent;

public class NowJobsActivity extends RootActivity {
	// HashMap<String, Distance> mapDistance = new HashMap<String, Distance>();

	private static final String TAG = NowJobsActivity.class.getSimpleName();
	public static Job mJob;

	int nJobPositionInList;

	TextView txtPickupAddress, txtPickupPincode, txtDistancePickup, txtDropAddress, txtDropPinCode, txtDistanceDrop,
			txtPassengerCount, txtFare, txtLuggage, tvPassengerName, txtTime, txtDistanceUnit, tvAvailable,
			textPickUpTimeCounter;
	ImageView ivWheelChair, ivHearingImpaired, ivPaymentType;

	RelativeLayout rlAcceptButton, rlRejectButton;
	Location currentLocation;
	ProgressDialog nowJobsDialog;

	Timer mMinutesPassengerWaiting;
	SimpleDateFormat dfDate_day = new SimpleDateFormat("HH:mm:ss");
	Calendar c = Calendar.getInstance();
	String currentDateandTime;
	String sPassengerdateTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nowjobs);
		mContext = this;
		Util.bIsNowOrPreebookFragment = false;

		if (getIntent().getExtras() != null) {
			nJobPositionInList = getIntent().getExtras().getInt("position");
			Log.e(TAG, "Position:: NowJobsActivity " + nJobPositionInList);
		}

		txtPickupAddress = (TextView) findViewById(R.id.txtPickupAddress);
		txtPickupPincode = (TextView) findViewById(R.id.txtPickupPincode);
		txtDistancePickup = (TextView) findViewById(R.id.txtDistancePickup);
		txtDropAddress = (TextView) findViewById(R.id.txtDropAddress);
		txtDropPinCode = (TextView) findViewById(R.id.txtDropPinCode);
		txtDistanceDrop = (TextView) findViewById(R.id.txtDistanceDrop);
		txtPassengerCount = (TextView) findViewById(R.id.txtPassengerCount);
		txtFare = (TextView) findViewById(R.id.txtFare);
		txtLuggage = (TextView) findViewById(R.id.txtLuggage);
		txtTime = (TextView) findViewById(R.id.txtTime);
		ivWheelChair = (ImageView) findViewById(R.id.ivWheelchair);
		ivHearingImpaired = (ImageView) findViewById(R.id.ivHearingImpaired);
		rlAcceptButton = (RelativeLayout) findViewById(R.id.rl_btnAccept);
		rlRejectButton = (RelativeLayout) findViewById(R.id.rl_btnReject);
		txtDistanceUnit = (TextView) findViewById(R.id.txtDistanceUnit);
		ivPaymentType = (ImageView) findViewById(R.id.ivPayment);
		tvAvailable = (TextView) findViewById(R.id.tvAvailable);
		textPickUpTimeCounter = (TextView) findViewById(R.id.txtTimeStatus);

		if (AppValues.driverSettings != null && AppValues.driverSettings.getAvailability() != null) {
			String sAvailability = AppValues.driverSettings.getAvailability();
			if (sAvailability.equals("true")) {
				tvAvailable.setText("Free");
				tvAvailable.setCompoundDrawablesWithIntrinsicBounds(R.drawable.availablegray, 0, 0, 0);
			} else {
				tvAvailable.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unavailable, 0, 0, 0);
				tvAvailable.setText("Busy");
			}
		}

		if (Util.mContext != null) {
			txtDistanceUnit.setText(Util.getDistanceFormat(mContext));
		}

		if (Util.getLocation(mContext) != null)
			currentLocation = Util.getLocation(mContext);

		if (mJob.getDropLocation() != null && mJob.getDropLocation().getAddressLine1() != null
				&& mJob.getDropLocation().getAddressLine1().equalsIgnoreCase("As Directed")) {

			Log.d(TAG, "onCreate :: inside if");

			// If AD only calculate and show PickUp location
			txtDistanceDrop.setText("-");

			CalculateDistanceTask calculateDistance = new CalculateDistanceTask();
			calculateDistance.jobID = mJob.getId();
			calculateDistance.pickupLat = mJob.getPickupLocation().getLatitude();
			calculateDistance.pickupLng = mJob.getPickupLocation().getLongitude();
			if (currentLocation != null) {
				calculateDistance.userLat = String.valueOf(currentLocation.getLatitude());
				calculateDistance.userLng = String.valueOf(currentLocation.getLongitude());
				calculateDistance.txtviewDistancePickup = txtDistancePickup;
				calculateDistance.txtviewDistanceDropoff = txtDistanceDrop;
				calculateDistance.execute();
			}
		} else {

			Log.d(TAG, "onCreate :: inside else");

			CalculateDistanceTask calculateDistance = new CalculateDistanceTask();
			calculateDistance.jobID = mJob.getId();
			calculateDistance.pickupLat = mJob.getPickupLocation().getLatitude();
			calculateDistance.pickupLng = mJob.getPickupLocation().getLongitude();
			if (currentLocation != null) {
				calculateDistance.userLat = String.valueOf(currentLocation.getLatitude());
				calculateDistance.userLng = String.valueOf(currentLocation.getLongitude());
			}
			calculateDistance.dropoffLat = mJob.getDropLocation().getLatitude();
			calculateDistance.dropoffLng = mJob.getDropLocation().getLongitude();
			calculateDistance.txtviewDistanceDropoff = txtDistanceDrop;
			calculateDistance.txtviewDistancePickup = txtDistancePickup;
			// calculateDistance.calculateAndDisplayDistancePickupAndDropOff();
			// szDistanceDrop = holder.txtDistanceDrop.getText().toString();
			// szDistancePickUp =
			// holder.txtDistancePickup.getText().toString();
			calculateDistance.execute();

		}

		if (mJob != null) {
			// PickUp Address
			String pickupAddress = "", pickUpWithoutNumber = "", firstAdd = "";
			pickupAddress = mJob.getPickupLocation().getAddressLine1();
			Log.e(TAG, "pickupAddress " + pickupAddress);
			firstAdd = pickupAddress.substring(0, pickupAddress.indexOf(" "));
			if (firstAdd.matches(".*[0-9].*")) {
				pickUpWithoutNumber = pickupAddress.replace(firstAdd, "");
				pickUpWithoutNumber = pickUpWithoutNumber.trim();
			} else {
				pickUpWithoutNumber = pickupAddress;
			}

			if (pickUpWithoutNumber.contains(","))
				pickUpWithoutNumber = pickUpWithoutNumber.substring(0, pickUpWithoutNumber.lastIndexOf(","));
			txtPickupAddress.setText(pickUpWithoutNumber);

			// PickUp Pincode
			String szPickUpPostCode = mJob.getPickupLocation().getPostCode().trim();
			if (!szPickUpPostCode.isEmpty() && szPickUpPostCode.contains(" ")) {
				String sTruncatedPostCode = szPickUpPostCode.substring(0, szPickUpPostCode.indexOf(" "));
				txtPickupPincode.setText(sTruncatedPostCode);
			} else if (!szPickUpPostCode.isEmpty()) {
				txtPickupPincode.setText(szPickUpPostCode);
			} else {
				txtPickupPincode.setText("");
			}
			// if (!szPickUpPostCode.isEmpty() &&
			// szPickUpPostCode.contains(" ")) {
			// String sTruncatedPostCode = szPickUpPostCode.substring(0,
			// szPickUpPostCode.indexOf(" "));
			// txtPickupPincode.setText(sTruncatedPostCode);
			// } else
			// txtPickupPincode
			// .setText(mJob.getPickupLocation().getPostCode());

			// DropOff Address
			String dropOffAddress = "", dropOffWithoutNumber = "", firstAddDropOff = "";
			if (mJob.getDropLocation() != null && mJob.getDropLocation().getAddressLine1() != null)
				dropOffAddress = mJob.getDropLocation().getAddressLine1();
			Log.e(TAG, "drop off address   " + dropOffAddress);

			firstAddDropOff = dropOffAddress.substring(0, dropOffAddress.indexOf(" "));
			if (firstAddDropOff.matches(".*[0-9].*")) {
				dropOffWithoutNumber = dropOffAddress.replace(firstAddDropOff, "");
				dropOffWithoutNumber = dropOffWithoutNumber.trim();
			} else {
				dropOffWithoutNumber = dropOffAddress;
			}

			if (dropOffWithoutNumber.contains(","))
				dropOffWithoutNumber = dropOffWithoutNumber.substring(0, dropOffWithoutNumber.lastIndexOf(","));
			txtDropAddress.setText(dropOffWithoutNumber);

			// DropOff Pincode
			String szDropOffPostCode = "";
			if (mJob.getDropLocation() != null && mJob.getDropLocation().getPostCode() != null)
				szDropOffPostCode = mJob.getDropLocation().getPostCode().trim();
			if (!szDropOffPostCode.isEmpty() && szDropOffPostCode.contains(" ")) {
				String sTruncatedDropOffPostCode = szDropOffPostCode.substring(0, szDropOffPostCode.indexOf(" "));
				txtDropPinCode.setText(sTruncatedDropOffPostCode);
			} else if (!szDropOffPostCode.isEmpty()) {
				txtDropPinCode.setText(szDropOffPostCode);
			} else {
				txtDropPinCode.setText("");
			}
			// if (!szDropOffPostCode.isEmpty() &&
			// szDropOffPostCode.contains(" ")) {
			// String sTruncatedDropOffPostCode = szDropOffPostCode.substring(
			// 0, szDropOffPostCode.indexOf(" "));
			// txtDropPinCode.setText(sTruncatedDropOffPostCode);
			// } else
			// txtDropPinCode.setText(mJob.getDropLocation().getPostCode());

			// No of Passengers
			if (mJob.getNumberOfPassengers() <= 5 && Util.getCitySelectedLondon(mContext)) {
				txtPassengerCount.setText("1-5");
			} else if (mJob.getNumberOfPassengers() <= 4) {
				txtPassengerCount.setText("1-4");
			} else {
				txtPassengerCount.setText(String.valueOf(mJob.getNumberOfPassengers()));
			}

			if (mJob.getNote() != null && !mJob.getNote().equals("")) {
				txtLuggage.setText("\"" + mJob.getNote() + "\"");
			} else {
				txtLuggage.setText("");
			}
			String date = Util.getTimeFormat(mJob.getPickupDateTime());
			txtTime.setText(date);

			// Wheelchair
			String isWheelChairAcessRequired = mJob.getWheelchairAccessRequired();
			if (isWheelChairAcessRequired.equals("true"))
				ivWheelChair.setVisibility(View.VISIBLE);
			else
				ivWheelChair.setVisibility(View.GONE);

			// Hearing impairment
			String isHearingImpaired = mJob.getIsHearingImpaired();
			if (isHearingImpaired.equals("true"))
				ivHearingImpaired.setVisibility(View.VISIBLE);
			else
				ivHearingImpaired.setVisibility(View.GONE);

			// Fixed Price/ On Meter
			if (mJob.getFixedPriceAmount() == null || mJob.getFixedPriceAmount().equals("")) {
				txtFare.setText("On Meter");
			} else {
				if (AppValues.driverSettings != null && AppValues.driverSettings.getCurrencySymbol() != null)
					txtFare.setText(AppValues.driverSettings.getCurrencySymbol() + mJob.getFixedPriceAmount());
				else
					txtFare.setText(mJob.getFixedPriceAmount());
			}
			// Payment type
			if (mJob.getPaymentType().equals("cash"))
				ivPaymentType.setImageResource(R.drawable.cashblack);
			else
				ivPaymentType.setImageResource(R.drawable.paymentblack);

			// PickupDateTime counter
			sPassengerdateTime = mJob.getPickupDateTime();
			mMinutesPassengerWaiting = new Timer();
			// instantiated
			mMinutesPassengerWaiting.schedule(new TimerTask() {
				@Override
				public void run() {
					textPickUpTimeCounter.setVisibility(View.VISIBLE);
					currentDateandTime = dfDate_day.format(new Date());
					SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

					Date dPassengerJobtime, dCurrentTime;
					final int dif;
					try {
						dPassengerJobtime = format.parse(Util.getTimeOnly((sPassengerdateTime)).toString());
						dCurrentTime = format.parse(currentDateandTime);
						dif = (int) (dPassengerJobtime.getTime() / 60000 - dCurrentTime.getTime() / 60000);
						runOnUiThread(new Runnable() {
							public void run() {

								if (dif < 0) {
									textPickUpTimeCounter.setTextColor(getResources().getColor(R.color.button_red));
								} else {
									textPickUpTimeCounter.setTextColor(getResources().getColor(R.color.button_green));
								}

								textPickUpTimeCounter.setText("(" + dif + " mins)");

								// if ((dif < -60) || (dif > 60)) {
								// // More than 1 hour
								// if (Constants.isDebug)
								// Log.e(TAG, "Diff >60 " + dif);
								//
								// textPickUpTimeCounter.setText("(" + dif
								// / 60 + "h " + -(dif % 60)
								// + " mins)");
								// } else {
								// if (Constants.isDebug)
								// Log.e(TAG, "Diff <60 " + dif);
								// textPickUpTimeCounter.setText("(" + dif
								// + " mins)");
								// }
								//
								// if (Constants.isDebug)
								// Log.e(TAG,
								// "Time Passenger"
								// + Util.getTimeOnly((sPassengerdateTime))
								// + " Time Device  "
								// + currentDateandTime
								// + " Time Differnce " + dif);
							}
						});

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, 0, 60000);

		}

		rlAcceptButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AppValues.driverSettings != null && AppValues.driverSettings.getAvailability().equals("true")) {
					if (NetworkUtil.isNetworkOn(mContext)) {
						nowJobsDialog = new ProgressDialog(mContext);
						nowJobsDialog.setMessage("Loading...");
						nowJobsDialog.setIndeterminate(true);
						nowJobsDialog.setCancelable(false);
						nowJobsDialog.show();
						AcceptJobTask acceptTask = new AcceptJobTask();
						// acceptTask.childPosition = childPosition;
						// acceptTask.groupPosition = groupPosition;
						acceptTask.job = mJob;
						if (!txtDistanceDrop.getText().toString().equals("-"))
							acceptTask.szDistanceDropOff = txtDistanceDrop.getText().toString();
						if (!txtDistancePickup.getText().toString().equals("-"))
							acceptTask.szDistancePickup = txtDistancePickup.getText().toString();
						// acceptTask.distanceDrop = szDistanceDrop;
						// acceptTask.distancePickUp = szDistancePickUp;

						acceptTask.execute();
					} else {
						Util.showToastMessage(mContext, mContext.getString(R.string.no_network_error), Toast.LENGTH_LONG);
					}
				} else {
					Util.showToastMessage(Util.mContext, "You are currently busy!", Toast.LENGTH_LONG);
					finish();
				}

			}
		});

		rlRejectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (AppValues.driverSettings != null && AppValues.driverSettings.getAvailability().equals("true")) {
					if (NetworkUtil.isNetworkOn(mContext)) {
						nowJobsDialog = new ProgressDialog(mContext);
						nowJobsDialog.setMessage("Loading...");
						nowJobsDialog.setIndeterminate(true);
						nowJobsDialog.setCancelable(false);
						nowJobsDialog.show();
						RejectJobTask rejectTask = new RejectJobTask();
						// rejectTask.childPosition = childPosition;
						// rejectTask.groupPosition = groupPosition;
						rejectTask.job = mJob;
						rejectTask.execute();
					} else {
						Util.showToastMessage(mContext, mContext.getString(R.string.no_network_error), Toast.LENGTH_LONG);
					}
				} else {
					Util.showToastMessage(Util.mContext, "You are currently busy!", Toast.LENGTH_LONG);
					finish();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mJob = null;
		if (mMinutesPassengerWaiting != null)
			try {
				mMinutesPassengerWaiting.cancel();
			} catch (Exception e) {

			}
	}

	// class Distance {
	// String szDistancePickup;
	// String szDistanceDropoff;
	// }

	class ViewHolder {
		TextView txtDistancePickup, txtDistanceDrop;
	}

	class CalculateDistanceTask extends AsyncTask<String, Void, String> {
		String pickupLat, pickupLng, dropoffLat, dropoffLng, userLat, userLng;
		TextView txtviewDistanceDropoff, txtviewDistancePickup;
		String szPickupToDropOffDistance = "", szUserToPickupDistance = "";
		String jobID;

		public void calculateAndDisplayDistancePickupAndDropOff() {

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url;
			// http://maps.googleapis.com/maps/api/distancematrix/json?origins=54.406505,18.67708&destinations=54.446251,18.570993&mode=driving&language=en-EN&sensor=false
			// url =
			// "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&language=en-EN&sensor=false";
			url = "http://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&mode=driving&units=imperial&alternatives=true&sensor=false";
			url = String.format(url, pickupLat, pickupLng, dropoffLat, dropoffLng);
			Log.e("NowJobsExpandableAdapter", "mapsURLPickupToDropOff:: " + url);

			Connection connection = new Connection(mContext);
			connection.mapURL = url;
			connection.connect(Constants.MAP_DISTANCE);
			try {
				szPickupToDropOffDistance = Util.parseMapDistanceData(connection.inputStreamToString(connection.mInputStream)
						.toString(), mContext);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// url =
			// "http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s,%s&destinations=%s,%s&mode=driving&language=en-EN&sensor=false";
			url = "http://maps.googleapis.com/maps/api/directions/json?origin=%s,%s&destination=%s,%s&mode=driving&units=imperial&alternatives=true&sensor=false";
			url = String.format(url, userLat, userLng, pickupLat, pickupLng);
			Log.e("NowJobsExpandableAdapter", "mapsURLUserToPickup:: " + url);

			connection = new Connection(mContext);
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

			try {
				if (Util.mContext != null) {
					if (!szPickupToDropOffDistance.equals("-") && !szPickupToDropOffDistance.equals(""))
						txtviewDistanceDropoff.setText(Util.getDistance(mContext, Double.parseDouble(szPickupToDropOffDistance),
								" " + Util.getDistanceFormat(mContext)));
					else
						txtviewDistanceDropoff.setText("0");

					if (!szUserToPickupDistance.equals("-") && !szUserToPickupDistance.equals(""))
						txtviewDistancePickup.setText(Util.getDistance(mContext, Double.parseDouble(szUserToPickupDistance), " "
								+ Util.getDistanceFormat(mContext)));
					else
						txtviewDistancePickup.setText("0");

				}

				// else {
				// if (!szPickupToDropOffDistance.equals("-")
				// && !szPickupToDropOffDistance.equals(""))
				// txtviewDistanceDropoff.setText(Util.getDistance(
				// mContext,
				// Double.parseDouble(szPickupToDropOffDistance),
				// "km"));
				// else
				// txtviewDistanceDropoff.setText("0");
				//
				// if (!szUserToPickupDistance.equals("-")
				// && !szUserToPickupDistance.equals(""))
				// txtviewDistancePickup.setText(Util.getDistance(
				// mContext,
				// Double.parseDouble(szUserToPickupDistance),
				// "km"));
				// else
				// txtviewDistancePickup.setText("0");
				//
				// }

				if (mJob.getDropLocation() != null && mJob.getDropLocation().getAddressLine1() != null) {
					if (mJob.getDropLocation().getAddressLine1().equalsIgnoreCase("As Directed"))
						txtviewDistanceDropoff.setText("-");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Distance distance = new Distance();
			// distance.szDistanceDropoff = txtviewDistanceDropoff.getText()
			// .toString();
			// distance.szDistancePickup = txtviewDistancePickup.getText()
			// .toString();
			// mapDistance.put(jobID, distance);
		}
	}

	class AcceptJobTask extends AsyncTask<String, Void, String> {
		Job job;
		String szDistanceDropOff = "", szDistancePickup = "";

		// int childPosition, groupPosition;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub

			AcceptJob acceptJob = new AcceptJob(job.getId(), mContext);
			if (acceptJob.isJobAccepted()) {
				FlurryAgent.logEvent("Job Accepted");
				DriverSettingDetails driverSettings = new DriverSettingDetails(mContext);
				driverSettings.retriveDriverSettings(mContext);

				DriverAccountDetails driverAccount = new DriverAccountDetails(mContext);
				driverAccount.retriveAccountDetails(mContext);
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

			if (nowJobsDialog != null)
				nowJobsDialog.dismiss();
			if (result != null) {
				if (Constants.isDebug)
					Log.e("NowJobsActivity", "onPostExecute:: " + result);

				/*
				 * if the result is true that means the driver has successfully
				 * accepted the job
				 */
				if (result.equals("true")) {
					finish();
					if (NowFragment.refreshListOnAcceptRejectHandler != null) {
						Util.mContext = MainActivity.mMainActivityContext;
						Bundle bundle = new Bundle();
						bundle.putInt("position", nJobPositionInList);
						bundle.putString(Constants.DISTANCE_DROP, szDistanceDropOff);
						bundle.putString(Constants.DISTANCE_PICKUP, szDistancePickup);

						Message message = new Message();
						message.obj = bundle;
						message.what = Constants.JOB_ACCEPTED_SUCCESS;
						NowFragment.acceptedJob = job;
						NowFragment.refreshListOnAcceptRejectHandler.sendMessage(message);
					}
				} else {
					/*
					 * if the result is anything other than true show it to the
					 * user and go back to now job screen
					 */
					AppValues.clearAllJobsList();
					finish();
					Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
				}

			}
		}
	}

	public class RejectJobTask extends AsyncTask<String, Void, String> {
		Job job;

		// int childPosition, groupPosition;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			RejectJobs rejectJobs = new RejectJobs(mContext, job.getId());
			String response = rejectJobs.rejectSelectedJob();
			try {
				JSONObject jObject = new JSONObject(response);

				if (jObject.has("success") && jObject.getString("success").equals("true")) {
					DriverSettingDetails driverSettings = new DriverSettingDetails(mContext);
					if (driverSettings.retriveDriverSettings(mContext) != null)
						AppValues.driverSettings = driverSettings.retriveDriverSettings(mContext);
					return "success";
				} else {
					if (jObject.has("errors")) {
						JSONArray jErrorsArray = jObject.getJSONArray("errors");

						String errorMessage = jErrorsArray.getJSONObject(0).getString("message");
						return errorMessage;
					}
				}
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
			if (nowJobsDialog != null)
				nowJobsDialog.dismiss();

			if (result.equals("success")) {
				sendMessageToHandler();
				Util.showToastMessage(mContext, "Job rejected successfully", Toast.LENGTH_LONG);
			} else if (result.equalsIgnoreCase("Job already accepted by other driver")) {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
				sendMessageToHandler();
			} else if (result.equalsIgnoreCase("Job has been cancelled by passenger")) {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
				sendMessageToHandler();
			} else {
				Util.showToastMessage(mContext, result, Toast.LENGTH_LONG);
			}

		}
	}

	void sendMessageToHandler() {
		finish();
		AppValues.getNowJobs().clear();
		if (NowFragment.refreshListOnAcceptRejectHandler != null) {
			Util.mContext = MainActivity.mMainActivityContext;
			Bundle bundle = new Bundle();
			bundle.putInt("position", nJobPositionInList);
			Message message = new Message();
			message.obj = bundle;
			message.what = Constants.JOB_REJECED_SUCCESS;
			NowFragment.refreshListOnAcceptRejectHandler.sendMessage(message);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (mMinutesPassengerWaiting != null)
			try {
				mMinutesPassengerWaiting.cancel();
			} catch (Exception e) {

			}
		super.onPause();
	}
}
