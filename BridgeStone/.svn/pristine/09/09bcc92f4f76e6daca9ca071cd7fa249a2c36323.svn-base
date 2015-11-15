package co.uk.pocketapp.bridgestone.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import co.uk.pocketapp.bridgestone.R;
import co.uk.pocketapp.bridgestone.util.AppValues;
import co.uk.pocketapp.bridgestone.util.Util;

import com.nostra13.universalimageloader.core.ImageLoader;

public class SensorDetailsActivity extends ParentActivity {

	ListView mSensorDetailsList;
	View viewTopbar;
	boolean bIsTotalSensorValues = false;
	SensorvaluesAdapter sensorvaluesAdpater;
	ArrayList<String> sensorValues = new ArrayList<String>();
	TextView emptyText, sensorsCapturedText;

	int nPressureMeasurementCurrentSelected;

	View view_header_sensor;
	TextView txtPressureHeader;

	static Handler mHandler;

	static boolean bIsListRefreshing = false;
	Timer myTimer;
	Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
			Locale.ENGLISH);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sensor_details);
		viewTopbar = (View) findViewById(R.id.sensordetailsTopbar);
		super.initWidgets(false, viewTopbar);

		nPressureMeasurementCurrentSelected = Util
				.getPressureMeasurement(SensorDetailsActivity.this);
		// sensorValues.add("S01001CB10BFB830B50F3182A");
		// sensorValues.add("S01001CB10BFB830B50F3182A");
		// sensorValues.add("S01001CB10BFB830B50F3182A");
		// sensorValues.add("S01001CB10BFB830B50F3182A");
		// sensorValues.add("S01001CB10BFB830B50F3182A");

		// Log.e("SensorDetailsActivity-onCreate()", "Sensor Values Size:: "
		// + AppValues.mapSensorValues.size());
		emptyText = (TextView) findViewById(R.id.emptyText);
		sensorsCapturedText = (TextView) findViewById(R.id.txtSenorBelowPressureTitle);
		mSensorDetailsList = (ListView) findViewById(R.id.listviewSensorDetails);
		mSensorDetailsList.setDivider(null);

		view_header_sensor = (View) findViewById(R.id.list_header);
		txtPressureHeader = (TextView) view_header_sensor
				.findViewById(R.id.txtPressure);

		if (Util.getPressureMeasurement(SensorDetailsActivity.this) == 0)// Pressure
		// in
		// BAR
		{
			txtPressureHeader.setText(getString(R.string.bar));

		} else { // Pressure in PSI
			txtPressureHeader.setText(getString(R.string.psi));
		}

		if (getIntent().getExtras() != null) {
			bIsTotalSensorValues = (getIntent().getExtras()
					.getBoolean("isTotal"));
		}

		mHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {

				sensorValues.clear();

				if (bIsTotalSensorValues) {

					sensorsCapturedText.setText(getResources().getString(
							R.string.sensors_captured));
					Iterator it = AppValues.mapSensorValues.entrySet()
							.iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						String sensorValue = pairs.getValue().toString();
						sensorValues.add(sensorValue);
					}
				} else {

					sensorsCapturedText.setText(getResources().getString(
							R.string.sensors_below_min_pressure));
					Iterator it = AppValues.mapSensorValuesBelowMinimumPressure
							.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						String sensorValue = pairs.getValue().toString();
						sensorValues.add(sensorValue);
					}
				}

				// sorting list before binding
				if (sensorValues != null && sensorValues.size() > 1)
					Collections.sort(sensorValues, new SignalComparator());

				if (sensorvaluesAdpater == null) {
					sensorvaluesAdpater = new SensorvaluesAdapter(
							SensorDetailsActivity.this);
					mSensorDetailsList.setAdapter(sensorvaluesAdpater);
				} else {
					sensorvaluesAdpater.updateValues(sensorValues);
				}

				if (sensorvaluesAdpater != null
						&& sensorvaluesAdpater.getCount() == 0) {
					emptyText.setVisibility(View.VISIBLE);
					emptyText.setText(getResources().getText(
							R.string.no_results));
				} else {
					emptyText.setVisibility(View.GONE);
				}

				bIsListRefreshing = false;
			};
		};
		mHandler.sendEmptyMessage(0);

	}

	class SensorvaluesAdapter extends BaseAdapter {
		private Context context;
		LayoutInflater inflater;
		ViewHolder viewHolder;

		public SensorvaluesAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		void updateValues(ArrayList<String> updatedSensorValues) {
			sensorValues = updatedSensorValues;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sensorValues.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return sensorValues.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// TODO Auto-generated method stub
			// S 0 1 001CB1 0BF B8 3 0B5 0F3 182A

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.row_sensor_reading,
						parent, false);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (position % 2 == 1) {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.list_item_bg_dark));
			} else {
				convertView.setBackgroundColor(getResources().getColor(
						R.color.white));
			}

			String header, fleetID, transmitterID, pressure, temperature, batteryVoltage, RSSI, notused;

			String sensorValue = sensorValues.get(position);
			String szSensorDate = "";

			try {
				if (sensorValue != null) {
					header = sensorValue.substring(1, 2);
					fleetID = sensorValue.substring(2, 3);
					transmitterID = sensorValue.substring(3, 9);
					pressure = sensorValue.substring(9, 12);
					temperature = sensorValue.substring(12, 14);
					batteryVoltage = sensorValue.substring(14, 15);
					RSSI = sensorValue.substring(15, 21);
					notused = sensorValue.substring(21, 25);
					szSensorDate = sensorValue.substring(25);

					// Log.e("SensorDetailsActivity:: ", " Message is :: "
					// + sensorValue);

					int nPressureInPSI = Integer.parseInt(pressure, 16);

					int nTemperature = Integer.parseInt(temperature, 16);
					int nBatteryVoltage = Integer.parseInt(batteryVoltage, 16);

					int nNNN = Integer.parseInt(RSSI.substring(3, 6), 16);
					int nSSS = Integer.parseInt(RSSI.substring(0, 3), 16);

					nNNN = (int) (nNNN * 3.22);
					/*
					 * Conversion formula : PRESSURE - (relative pressure) :
					 * “PPP” in Decimal form = “VAL” P ( Bar ) =(VAL*0.026)-4.99
					 * 
					 * TEMPERATURE : “TT” in decimal form = “VAL” T ( °C ) =
					 * (VAL * 1.657) – 273
					 * 
					 * RSSI : “NNN” – “SSS” in decimal form = “VAL” RSSI ( mV )
					 * = VAL * 3.22
					 */

					// RSSI range: 0 to 1500 == 4 * 375

					// String outPutValue = (":  "
					// + "header: "
					// + header
					// + " fleetID: "
					// + fleetID
					// + " transmitterID : "
					// + transmitterID
					// + " pressure in PSI : "
					// + nPressureInPSI
					// + " pressure in Bar : "
					// + String.format(Locale.ENGLISH, "%.2f",
					// ((nPressureInPSI * 0.026) - 4.99))
					// + " temperature in F : "
					// + nTemperature
					// + " temperature in Celcius : "
					// + String.format(Locale.ENGLISH, "%.2f",
					// ((nTemperature * 1.657) - 273))
					// + " batteryVoltage : "
					// + nBatteryVoltage
					// + " RSSI : "
					// + (nNNN - nSSS)
					// + " RSSI in mV : "
					// + String.format(Locale.ENGLISH, "%.2f",
					// (nNNN - nSSS) * 3.22) + " nNNN : " + (nNNN));

					// Log.i("OutPutValue is", " ****Sensor Reading***"
					// + outPutValue);
					float fPressureInBar = 0;
					fPressureInBar = Float.parseFloat(String.format(
							Locale.ENGLISH, "%.2f",
							(float) ((nPressureInPSI * 0.026) - 4.99)));
					if (Util.getPressureMeasurement(SensorDetailsActivity.this) == 0)// Pressure
					// in
					// BAR
					{
						// fPressure = (float) (nPressureInPSI / 14.5037738007);
						viewHolder.txtPressure.setText(String
								.valueOf(fPressureInBar));
					} else { // Pressure in PSI

						nPressureInPSI = (int) (fPressureInBar * 14.5037738007);

						viewHolder.txtPressure.setText(String
								.valueOf(nPressureInPSI));
					}
					viewHolder.txtTransmitterID.setText(transmitterID);

					long diffInSec = 0;
					try {
						// long currentSeconds = calendar.getTimeInMillis();
						//
						// // SimpleDateFormat simpleDateFormat = new
						// // SimpleDateFormat(
						// // "EEE MMM dd HH:mm:ss", Locale.ENGLISH);
						// //
						// calendar.setTime(simpleDateFormat.parse(szSensorDate));
						// // Mon Sep 08 17:00:08 GMT+05:30 2014
						//
						// calendar.setTime(new Date(szSensorDate));
						// long sensorTimeSeconds = calendar.getTimeInMillis();

						long currentSeconds = System.currentTimeMillis();
						long sensorTimeSeconds = Long.valueOf(szSensorDate);

						diffInSec = TimeUnit.MILLISECONDS
								.toSeconds(currentSeconds - sensorTimeSeconds);
						// Log.e("SensorDetailsActivity", "getView() " +
						// szSensorDate
						// + "  " + diffInSec);

						// Log.e("SensorDetailsActivity",
						// "getView()+ timedifference:: " + diffInSec);

						if (AppValues.bIsDebug) {
							MainActivity.appendLog("TransmitterID:: "
									+ transmitterID + "  Time:: "
									+ szSensorDate + "  Current time in ms:: "
									+ currentSeconds + "  Sensor time in ms:: "
									+ sensorTimeSeconds
									+ "  Difference in seconds:: " + diffInSec);
						}

					} catch (Exception e) {
						if (AppValues.bIsDebug) {
							MainActivity
									.appendLog("Exception. Setting time diff as 0.");
						}
					}
					if (diffInSec > AppValues.nTimeDifferenceBetweenLastReceivedSensor)
						ImageLoader.getInstance().displayImage(
								"drawable://" + R.drawable.signal,
								viewHolder.imgSignalStrength);
					else if (nNNN <= 375)
						ImageLoader.getInstance().displayImage(
								"drawable://" + R.drawable.signal1,
								viewHolder.imgSignalStrength);
					else if (nNNN > 375 && nNNN <= 750)
						ImageLoader.getInstance().displayImage(
								"drawable://" + R.drawable.signal2,
								viewHolder.imgSignalStrength);
					else if (nNNN > 750 && nNNN <= 1125)
						ImageLoader.getInstance().displayImage(
								"drawable://" + R.drawable.signal3,
								viewHolder.imgSignalStrength);
					else if (nNNN > 1125)
						ImageLoader.getInstance().displayImage(
								"drawable://" + R.drawable.signal4,
								viewHolder.imgSignalStrength);

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				if (AppValues.bIsDebug) {
					MainActivity.appendLog("Exception. Setting default image.");
				}
			}
			return convertView;
		}
	}

	class ViewHolder {

		TextView txtPressure;
		TextView txtTransmitterID;
		ImageView imgSignalStrength;

		public ViewHolder(final View v) {
			txtPressure = (TextView) v.findViewById(R.id.txtPressure);
			txtTransmitterID = (TextView) v.findViewById(R.id.txtTransmitterID);
			imgSignalStrength = (ImageView) v
					.findViewById(R.id.imgSignalStrength);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (sensorvaluesAdpater != null
				&& nPressureMeasurementCurrentSelected != Util
						.getPressureMeasurement(SensorDetailsActivity.this)) {
			sensorvaluesAdpater.notifyDataSetChanged();
		}

		RefreshListTask myTask = new RefreshListTask();
		myTimer = new Timer();
		// public void schedule (TimerTask task, long delay, long period)
		// Schedule a task for repeated fixed-delay execution after a specific
		// delay.
		//
		// Parameters
		// task the task to schedule.
		// delay amount of time in milliseconds before first execution.
		// period amount of time in milliseconds between subsequent executions.

		myTimer.schedule(myTask, AppValues.nTimeDelayForTimerToRefresh,
				AppValues.nTimeDelayForTimerToRefresh);

	}

	class RefreshListTask extends TimerTask {
		public void run() {
			refreshList();
		}
	}

	public static void refreshList() {
		if (mHandler != null && !bIsListRefreshing) {
			mHandler.sendEmptyMessage(0);
			bIsListRefreshing = true;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mHandler = null;

		if (myTimer != null)
			myTimer.cancel();
	}

	class SignalComparator implements Comparator<String> {

		@Override
		public int compare(String lhs, String rhs) {

			try {
				String lhsRSSI = lhs.substring(15, 21);
				int nlhsNNN = Integer.parseInt(lhsRSSI.substring(3, 6), 16);
				nlhsNNN = (int) (nlhsNNN * 3.22);

				String rhsRSSI = rhs.substring(15, 21);
				int nrhsNNN = Integer.parseInt(rhsRSSI.substring(3, 6), 16);
				nrhsNNN = (int) (nrhsNNN * 3.22);

				if (nlhsNNN < nrhsNNN) {
					return 1;
				} else if (nlhsNNN > nrhsNNN) {
					return -1;
				} else {
					return 0;
				}

			} catch (Exception e) {
				return -1;
			}

		}

	}

}
