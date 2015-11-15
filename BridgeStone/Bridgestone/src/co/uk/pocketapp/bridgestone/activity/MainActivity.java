/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.uk.pocketapp.bridgestone.activity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.pocketapp.bridgestone.R;
import co.uk.pocketapp.bridgestone.receivers.NetworkBroadcastReceiver;
import co.uk.pocketapp.bridgestone.tasks.UploadSensorValuesTask;
import co.uk.pocketapp.bridgestone.util.AppValues;
import co.uk.pocketapp.bridgestone.util.BluetoothService;
import co.uk.pocketapp.bridgestone.util.Util;

/**
 * This is the main Activity that displays the current bluetooth session.
 */
public class MainActivity extends ParentActivity {
	// Debugging
	private static final String TAG = "MainActivity";
	private static final boolean D = true;

	// Message types sent from the BluetoothService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// Key names received from the BluetoothService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	// Layout Views
	// private TextView mTitle;
	// private ListView mConversationView;
	// private EditText mOutEditText;
	// private Button mSendButton;
	Button mConnectChangeDeviceButton;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	// private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the Bluetooth services
	private BluetoothService mBluetoothService = null;

	// S 0 1 001CB1 0BF B8 3 0B5 0F3 182A

	TextView txtConnectedStatus;
	EditText etConnectedStatus;
	EditText etMinimumPressure;

	boolean bIsSettingsOpened = false;
	ProgressDialog mConnectingDialog; // mUploadDialog;

	Button btnUploadNow;
	String szMinimumPressureText = "";
	private TextView txtSensorsCaptured, txtSensorsBelowMinPressureCaptured;
	private RelativeLayout relSensorsCaptured,
			relSensorsBelowMinPressureCaptured;
	private Button btnSave;
	View viewTopbar;

	Handler mUploadHandler;

	boolean bIsValuesUploaded = false, bIsuploading = false;

	Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
			Locale.ENGLISH);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");

		// Set up the window layout
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.custom_title);
		viewTopbar = (View) findViewById(R.id.mainTopBar);
		super.initWidgets(false, viewTopbar);

		// Set up the custom title
		// mTitle = (TextView) findViewById(R.id.title_left_text);
		// mTitle.setText(R.string.app_name);
		// mTitle = (TextView) findViewById(R.id.title_right_text);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this,
					getResources().getString(R.string.bluetooth_not_available),
					Toast.LENGTH_LONG).show();
			// finish();
			return;
		}

		mUploadHandler = new Handler() {

			@Override
			public void handleMessage(android.os.Message msg) {

				// if (mUploadDialog != null) {
				// mUploadDialog.dismiss();
				// mUploadDialog = null;
				// }
				if (msg.what == 0) { // success
					bIsValuesUploaded = true;

					if (btnUploadNow != null) {
						btnUploadNow.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.greenbutton));
						btnUploadNow.setText(getResources().getString(
								R.string.uploaded));
						btnUploadNow.setEnabled(false);
					}
					AppValues.mapSensorValues.clear();
					AppValues.mapSensorValuesBelowMinimumPressure.clear();
					try {
						txtSensorsCaptured.setText(String
								.valueOf(AppValues.mapSensorValues.size()));
						txtSensorsBelowMinPressureCaptured
								.setText(String
										.valueOf(AppValues.mapSensorValuesBelowMinimumPressure
												.size()));
					} catch (Exception e) {
						e.printStackTrace();

					}
					bIsValuesUploaded = false;
				} else if (msg.what == 1) {
					Toast.makeText(MainActivity.this,
							getResources().getString(R.string.upload_error),
							Toast.LENGTH_LONG).show();

					if (btnUploadNow != null) {
						btnUploadNow.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.btn_background));
						btnUploadNow.setText(getResources().getString(
								R.string.btnUploadNow));
						btnUploadNow.setEnabled(true);
					}
				}

				bIsuploading = false;

				// if (mDialog != null) {
				// mDialog.dismiss();
				// }
				//
				// if (msg.what == Const.LOGIN_SUCCESS) { // login success
				// rel_login.setVisibility(View.GONE);
				// loggedIn.setVisibility(View.VISIBLE);
				// Bundle bundle = (Bundle) msg.obj;
				// AppValues.szLoggedInEmail = bundle.getString("email");
				// AppValues.szName = bundle.getString("name");
				// loggedIn.setText("Welcome " + bundle.getString("name")
				// + "!");
				// } else if (msg.what == Const.LOGIN_FAILED) { // login failed
				// Bundle bundle = (Bundle) msg.obj;
				// String szError = bundle.getString("error");
				// showAlertDialog(szError);
				// rel_login.setVisibility(View.VISIBLE);
				// loggedIn.setVisibility(View.GONE);
				// }
			};
		};

		TextView txtVersion = (TextView) findViewById(R.id.txtVersion);
		txtVersion.setText("Version: "
				+ getResources().getString(R.string.app_version));

	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupBluetooth() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the Bluetooth session
		} else {
			if (mBluetoothService == null)
				setupBluetooth(true);
			else {
				if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED
						&& !Util.getDeviceAddress(MainActivity.this).equals("")) {
					Intent data = new Intent();
					data.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS,
							Util.getDeviceAddress(MainActivity.this));
					showConnectingDialog();
					connectDevice(data, false);
				}
			}
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		try {
			txtSensorsCaptured.setText(String.valueOf(AppValues.mapSensorValues
					.size()));
			txtSensorsBelowMinPressureCaptured.setText(String
					.valueOf(AppValues.mapSensorValuesBelowMinimumPressure
							.size()));
			// if (szMinimumPressureText != null
			// && !szMinimumPressureText.equals(""))
			// etMinimumPressure.setText(Util
			// .getPressureFilter(MainActivity.this));
		} catch (Exception e) {
			e.printStackTrace();

		}
		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mBluetoothService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mBluetoothService.getState() == BluetoothService.STATE_NONE) {
				// Start the Bluetooth services
				mBluetoothService.start();
			}
		}
	}

	private void setupBluetooth(boolean bIsBluetoothSetup) {
		Log.d(TAG, "setupBluetooth()");

		// // Initialize the array adapter for the conversation thread
		// mConversationArrayAdapter = new ArrayAdapter<String>(this,
		// R.layout.message);
		// mConversationView = (ListView) findViewById(R.id.in);
		// mConversationView.setAdapter(mConversationArrayAdapter);
		//
		// // Initialize the compose field with a listener for the return key
		// mOutEditText = (EditText) findViewById(R.id.edit_text_out);
		// mOutEditText.setOnEditorActionListener(mWriteListener);
		//
		// // Initialize the send button with a listener that for click events
		// mSendButton = (Button) findViewById(R.id.button_send);
		// mSendButton.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// // Send a message using content of the edit text widget
		// TextView view = (TextView) findViewById(R.id.edit_text_out);
		// String message = view.getText().toString();
		// sendMessage(message);
		// }
		// });

		if (bIsBluetoothSetup) {
			// Initialize the BluetoothService to perform bluetooth connections
			mBluetoothService = new BluetoothService(this, mHandler);
		}

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");

		try {
			toggleConnectChangeDeviceButton(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (mBluetoothService != null && bIsBluetoothSetup)
			if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED
					&& !Util.getDeviceAddress(MainActivity.this).equals("")) {
				Intent data = new Intent();
				data.putExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS,
						Util.getDeviceAddress(MainActivity.this));
				showConnectingDialog();
				connectDevice(data, false);
			}

	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
		dismissConnectingDialog();

	}

	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth services
		if (mBluetoothService != null)
			mBluetoothService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
	}

	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			txtConnectedStatus.setText(getResources().getString(
					R.string.not_connected));
			txtConnectedStatus.setTextColor(getResources().getColor(
					R.color.not_connected));
			mConnectChangeDeviceButton.setText(getResources().getString(
					R.string.connect_device));
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			// Get the message bytes and tell the BluetoothService to write
			byte[] send = message.getBytes();
			mBluetoothService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			// mOutEditText.setText(mOutStringBuffer);
		}
	}

	// The action listener for the EditText widget, to listen for the return key
	private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView view, int actionId,
				KeyEvent event) {
			// If the action is a key-up event on the return key, send the
			// message
			if (actionId == EditorInfo.IME_NULL
					&& event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			if (D)
				Log.i(TAG, "END onEditorAction");
			return true;
		}
	};

	// The Handler that gets information back from the BluetoothService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			toggleConnectStatus();
			Date currentDate = calendar.getTime();
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothService.STATE_CONNECTED:
					// mTitle.setText(R.string.title_connected_to);
					// mTitle.append(mConnectedDeviceName);
					// txtConnectedStatus.setText(getResources().getString(
					// R.string.connected));
					// txtConnectedStatus.setTextColor(getResources().getColor(
					// R.color.connected));
					// mConnectChangeDeviceButton.setText(getResources()
					// .getString(R.string.change_device));
					// mConversationArrayAdapter.clear();
					if (AppValues.bIsDebug)
						appendLog("Bluetooth Connected:: " + currentDate);
					dismissConnectingDialog();
					setContentView(R.layout.sensors);
					viewTopbar = (View) findViewById(R.id.sensorsTopbar);
					toggleSettings(true, viewTopbar);
					initSensorValues();
					break;
				case BluetoothService.STATE_CONNECTING:
					// mTitle.setText(R.string.title_connecting);
					if (AppValues.bIsDebug)
						appendLog("Bluetooth Connecting:: " + currentDate);
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					// mTitle.setText(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				// mConversationArrayAdapter.add("Me:  " + writeMessage);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1).trim();
				// mConversationArrayAdapter.add(mConnectedDeviceName + ":  "
				// + readMessage);

				// S 0 1 001CB1 0BF B8 3 0B5 0F3 182A
				// Log.e("Received:: ", " Message is :: " + readMessage
				// + " Length is :: " + readMessage.length());

				String header,
				fleetID,
				transmitterID,
				pressure,
				temperature,
				batteryVoltage,
				RSSI,
				notused;

				try {
					if (readMessage != null && readMessage.length() == 25
							&& !bIsValuesUploaded) {
						transmitterID = readMessage.substring(3, 9);
						pressure = readMessage.substring(9, 12);
						int nPressureInPSI = Integer.parseInt(pressure, 16);

						// SimpleDateFormat dateFormat = new SimpleDateFormat(
						// "EEE MMM dd HH:mm:ss");
						// String formatted = dateFormat
						// .format(calendar.getTime());
						// readMessage = readMessage + formatted;

						// readMessage = readMessage
						// + calendar.getTime().toString();

						readMessage = readMessage + System.currentTimeMillis();

						AppValues.mapSensorValues.put(transmitterID,
								readMessage);
						if (btnUploadNow != null) {
							btnUploadNow.setBackgroundDrawable(getResources()
									.getDrawable(R.drawable.btn_background));
							if (!bIsuploading)
								btnUploadNow.setText(getResources().getString(
										R.string.btnUploadNow));
							btnUploadNow.setEnabled(true);
						}

						txtSensorsCaptured.setText(String
								.valueOf(AppValues.mapSensorValues.size()));

						updateMinimumPressureText(readMessage, transmitterID,
								nPressureInPSI);

						// SensorDetailsActivity.refreshList();

						// mConversationArrayAdapter.add(mConnectedDeviceName
						// + ":  "
						// + "header: "
						// + header
						// + " fleetID: "
						// + fleetID
						// + " transmitterID : "
						// + transmitterID
						// + " pressure in PSI : "
						// + nPressureInPSI
						// + " pressure in Bar : "
						// // + String.format("%.2f",
						// // ((nPressureInPSI * 0.026) - 4.99))
						// + fPressureInBar
						// + " temperature in F : "
						// + nTemperature
						// + " temperature in Celcius : "
						// + String.format("%.2f",
						// ((nTemperature * 1.657) - 273))
						// + " batteryVoltage : " + nBatteryVoltage
						// + " RSSI : " + (nNNN - nSSS) + " RSSI in mV : "
						// + String.format("%.2f", (nNNN - nSSS) * 3.22));

						if (AppValues.bIsDebug) {

							header = readMessage.substring(1, 2);
							fleetID = readMessage.substring(2, 3);
							temperature = readMessage.substring(12, 14);
							batteryVoltage = readMessage.substring(14, 15);
							RSSI = readMessage.substring(15, 21);
							notused = readMessage.substring(21, 25);
							int nTemperature = Integer
									.parseInt(temperature, 16);
							int nBatteryVoltage = Integer.parseInt(
									batteryVoltage, 16);

							int nNNN = Integer.parseInt(RSSI.substring(3, 6),
									16);
							int nSSS = Integer.parseInt(RSSI.substring(0, 3),
									16);

							/*
							 * Conversion formula : PRESSURE - (relative
							 * pressure) : “PPP” in Decimal form = “VAL” P ( Bar
							 * ) =(VAL*0.026)-4.99
							 * 
							 * TEMPERATURE : “TT” in decimal form = “VAL” T ( °C
							 * ) = (VAL * 1.657) – 273
							 * 
							 * RSSI : “NNN” – “SSS” in decimal form = “VAL” RSSI
							 * ( mV ) = VAL * 3.22
							 */

							nNNN = (int) (nNNN * 3.22);

							String outPutValue = (":  "
									+ "header: "
									+ header
									+ " fleetID: "
									+ fleetID
									+ " transmitterID : "
									+ transmitterID
									+ " pressure in PSI : "
									+ nPressureInPSI
									+ " pressure in Bar : "
									+ String.format(Locale.ENGLISH, "%.2f",
											((nPressureInPSI * 0.026) - 4.99))
									+ " temperature in F : "
									+ nTemperature
									+ " temperature in Celcius : "
									+ String.format(Locale.ENGLISH, "%.2f",
											((nTemperature * 1.657) - 273))
									+ " batteryVoltage : "
									+ nBatteryVoltage
									+ " RSSI : "
									+ (nNNN - nSSS)
									+ " RSSI in mV : "
									+ String.format(Locale.ENGLISH, "%.2f",
											(nNNN - nSSS) * 3.22) + " nNNN : " + (nNNN));

							appendLog(readMessage + " " + outPutValue);
						}

					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(
						getApplicationContext(),
						getResources().getString(R.string.title_connected_to)
								+ mConnectedDeviceName, Toast.LENGTH_SHORT)
						.show();
				break;
			case MESSAGE_TOAST:
				dismissConnectingDialog();
				hidekeyboard();
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				if (msg.getData() != null
						&& msg.getData().containsKey(TOAST)
						&& msg.getData()
								.getString(TOAST)
								.equals(getResources().getString(
										R.string.device_connection_lost))) {
					if (AppValues.bIsDebug)
						appendLog("Bluetooth Connection LOST:: " + currentDate);
					SensorDetailsActivity.refreshList();
				}

				break;
			}
		}
	};

	void showConnectingDialog() {
		mConnectingDialog = new ProgressDialog(MainActivity.this);
		mConnectingDialog.setCanceledOnTouchOutside(false);
		mConnectingDialog.setCancelable(false);
		mConnectingDialog.setMessage(getResources().getString(
				R.string.connecting));
		mConnectingDialog.show();
	}

	void dismissConnectingDialog() {
		if (mConnectingDialog != null) {
			mConnectingDialog.dismiss();
			mConnectingDialog = null;
		}
	}

	void updateMinimumPressureText(String sensorValue, String transmitterID,
			int nPressureInPSI) {

		float fPressureInBar = 0;
		fPressureInBar = Float.parseFloat(String.format(Locale.ENGLISH, "%.2f",
				(float) ((nPressureInPSI * 0.026) - 4.99)));

		if (Util.getPressureMeasurement(MainActivity.this) == 0) // Pressure
		// in
		// BAR
		{
			try {
				// float fPressureInBar = (float) (Float.valueOf(nPressureInPSI)
				// / 14.5037738007);

				if (fPressureInBar < Float.parseFloat(szMinimumPressureText)) {
					AppValues.mapSensorValuesBelowMinimumPressure.put(
							transmitterID, sensorValue);
				}
			} catch (NumberFormatException e) {

			}
		} else { // Pressure in PSI

			nPressureInPSI = (int) (fPressureInBar * 14.5037738007);

			if (nPressureInPSI < Float.parseFloat(szMinimumPressureText)) {
				AppValues.mapSensorValuesBelowMinimumPressure.put(
						transmitterID, sensorValue);
			}
		}
		txtSensorsBelowMinPressureCaptured.setText(String
				.valueOf(AppValues.mapSensorValuesBelowMinimumPressure.size()));
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				if (data != null
						&& !(data.getExtras().getString(
								DeviceListActivity.EXTRA_DEVICE_ADDRESS)
								.equals(""))) {
					showConnectingDialog();
					connectDevice(data, true);
				} else {
					Toast.makeText(MainActivity.this,
							getString(R.string.no_device_selected),
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				if (data != null
						&& !(data.getExtras().getString(
								DeviceListActivity.EXTRA_DEVICE_ADDRESS)
								.equals(""))) {
					showConnectingDialog();
					connectDevice(data, false);
				} else {
					Toast.makeText(MainActivity.this,
							getString(R.string.no_device_selected),
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a bluetooth session
				try {
					setupBluetooth(true);
				} catch (Exception e) {

				}
			} else {
				// User did not enable Bluetooth or an error occured
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled,
						Toast.LENGTH_SHORT).show();
				// finish();
				setupBluetooth(false);
			}
		}
	}

	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BLuetoothDevice object
		try {
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
			// Attempt to connect to the device
			mBluetoothService.connect(device, secure);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent serverIntent = null;
		switch (item.getItemId()) {
		case R.id.secure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;
		case R.id.insecure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent,
					REQUEST_CONNECT_DEVICE_INSECURE);
			return true;
		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		}
		return false;
	}

	public void onUploadClick(View view) {
		// To upload the values back to Bridgestone server

		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

			// mUploadDialog = new ProgressDialog(MainActivity.this);
			// mUploadDialog.setCanceledOnTouchOutside(false);
			// mUploadDialog.setCancelable(false);
			// mUploadDialog.setMessage(getResources().getString(
			// R.string.uploading));
			// mUploadDialog.show();

			bIsuploading = true;
			btnUploadNow.setEnabled(false);
			btnUploadNow.setText(getResources().getText(R.string.uploading));
			UploadSensorValuesTask uploadTask = new UploadSensorValuesTask(
					MainActivity.this, mUploadHandler);
			uploadTask.mapSensorValues = AppValues.mapSensorValues;
			uploadTask.execute();
		} else {
			bIsuploading = false;
			Toast.makeText(MainActivity.this,
					getResources().getString(R.string.no_network_error),
					Toast.LENGTH_LONG).show();
		}
	}

	void settingsInit() {
		imgSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hidekeyboard();
				bIsValuesUploaded = false;
				setContentView(R.layout.activity_main);
				final View viewDeviceConnect = (View) findViewById(R.id.initial_device_connect);
				RelativeLayout relLanguageAndPressure = (RelativeLayout) viewDeviceConnect
						.findViewById(R.id.rel_langpressure);
				relLanguageAndPressure.setVisibility(View.VISIBLE);

				setLanguageAndPressureAdapter();
				try {
					toggleConnectChangeDeviceButton(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	void toggleConnectStatus() {
		if (mBluetoothService == null
				|| mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
			txtConnectedStatus.setText(getResources().getString(
					R.string.not_connected));
			txtConnectedStatus.setTextColor(getResources().getColor(
					R.color.not_connected));
			mConnectChangeDeviceButton.setText(getResources().getString(
					R.string.connect_device));
		} else if (mBluetoothService != null
				&& mBluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
			txtConnectedStatus.setText(getResources().getString(
					R.string.connected));
			txtConnectedStatus.setTextColor(getResources().getColor(
					R.color.connected));
			mConnectChangeDeviceButton.setText(getResources().getString(
					R.string.change_device));
		}
	}

	void toggleConnectChangeDeviceButton(boolean bIsConnectDevice)
			throws Exception {
		final View viewDeviceConnect = (View) findViewById(R.id.initial_device_connect);
		txtConnectedStatus = (TextView) viewDeviceConnect
				.findViewById(R.id.txtConnectedStatus);
		etConnectedStatus = (EditText) viewDeviceConnect
				.findViewById(R.id.etConnectedStatus);

		TextView txtVersion = (TextView) viewDeviceConnect
				.findViewById(R.id.txtVersion);
		txtVersion.setText("Version: "
				+ getResources().getString(R.string.app_version));

		mConnectChangeDeviceButton = (Button) viewDeviceConnect
				.findViewById(R.id.btnConnectChangeDevice);
		toggleConnectStatus();

		if (bIsConnectDevice) {
			mConnectChangeDeviceButton.setText(getResources().getString(
					R.string.connect_device));
			if (!bIsSettingsOpened)
				bIsSettingsOpened = false;
		} else {
			if (mBluetoothService != null
					&& mBluetoothService.getState() == BluetoothService.STATE_CONNECTED)
				mConnectChangeDeviceButton.setText(getResources().getString(
						R.string.change_device));
			else
				mConnectChangeDeviceButton.setText(getResources().getString(
						R.string.connect_device));

			bIsSettingsOpened = true;
		}
		mConnectChangeDeviceButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Initiate Device Connect
				Log.e(TAG, "Connect click");
				if (!mBluetoothAdapter.isEnabled()) {
					Intent enableIntent = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
					// Otherwise, setup the Bluetooth session
				} else {
					Intent serverIntent = new Intent(MainActivity.this,
							DeviceListActivity.class);
					startActivityForResult(serverIntent,
							REQUEST_CONNECT_DEVICE_INSECURE);
				}
			}
		});
	}

	void initSensorValues() {
		settingsInit();
		btnUploadNow = (Button) findViewById(R.id.btnUpload);
		btnUploadNow.setText(getResources().getString(R.string.btnUploadNow));
		btnUploadNow.setEnabled(true);
		etMinimumPressure = (EditText) findViewById(R.id.etMinPressure);

		// etMinimumPressure.setText(Util.getPressureFilter(MainActivity.this));
		etMinimumPressure.setText(String.format(Locale.ENGLISH, "%.2f",
				Float.parseFloat(Util.getPressureFilter(MainActivity.this))));

		etMinimumPressure.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					etMinimumPressure.setCursorVisible(false);
					return true;
				}
				return false;
			}
		});

		// InputFilter[] FilterArray = new InputFilter[1];
		// FilterArray[0] = new InputFilter.LengthFilter(4);
		// etMinimumPressure.setFilters(FilterArray);
		szMinimumPressureText = etMinimumPressure.getText().toString();
		btnSave = (Button) findViewById(R.id.btnSave);
		txtSensorsCaptured = (TextView) findViewById(R.id.txtSensorsCapturedNumber);
		txtSensorsBelowMinPressureCaptured = (TextView) findViewById(R.id.txtSensorsBelowMinPressure);
		txtSensorsCaptured.setText(String.valueOf(AppValues.mapSensorValues
				.size()));
		txtSensorsBelowMinPressureCaptured.setText(String
				.valueOf(AppValues.mapSensorValuesBelowMinimumPressure.size()));
		relSensorsCaptured = (RelativeLayout) findViewById(R.id.relSensorsCaptured);
		relSensorsBelowMinPressureCaptured = (RelativeLayout) findViewById(R.id.relSensorsBelowMinPressure);

		hidekeyboard();

		final Intent sensorDetailsIntent = new Intent(this,
				SensorDetailsActivity.class);
		relSensorsCaptured.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sensorDetailsIntent.putExtra("isTotal", true);
				startActivity(sensorDetailsIntent);
			}
		});

		relSensorsBelowMinPressureCaptured
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						sensorDetailsIntent.putExtra("isTotal", false);
						startActivity(sensorDetailsIntent);
					}
				});

		etMinimumPressure.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				btnSave.setVisibility(View.VISIBLE);
				etMinimumPressure.setCursorVisible(true);
				return false;
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (etMinimumPressure.getText().toString().length() > 0) {
					hidekeyboard();

					if (etMinimumPressure.getText().toString().contains(".")) {
						// if (!(etMinimumPressure.getText().toString()
						// .indexOf(".") > 0)) {
						// Toast.makeText(
						// MainActivity.this,
						// getResources()
						// .getString(
						// R.string.input_number_invalid_error),
						// Toast.LENGTH_LONG).show();
						// return;
						// }
						if (!validate(etMinimumPressure.getText().toString())) {
							Toast.makeText(
									MainActivity.this,
									getResources()
											.getString(
													R.string.input_number_invalid_error),
									Toast.LENGTH_LONG).show();
							return;
						}

					}

					etMinimumPressure.setText(String.format(Locale.ENGLISH,
							"%.2f", Float.parseFloat(etMinimumPressure
									.getText().toString())));

					szMinimumPressureText = etMinimumPressure.getText()
							.toString();
					etMinimumPressure.setCursorVisible(false);
					Util.setPressureFilter(MainActivity.this,
							szMinimumPressureText);
					btnSave.setVisibility(View.GONE);

					filterMinPressureValues();
				}

				else {
					Toast.makeText(
							MainActivity.this,
							getResources().getString(
									R.string.input_length_error),
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public boolean validate(String input) {

		String regexp = "[-+]?(\\d*[.])?\\d+";
		Pattern pattern = Pattern.compile(regexp);
		Matcher matcher = pattern.matcher(input);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	void filterMinPressureValues() {
		// to filter the values from previous list
		AppValues.mapSensorValuesBelowMinimumPressure.clear();

		Iterator it = AppValues.mapSensorValues.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
			String sensorValue = pairs.getValue().toString();
			String transmitterID = sensorValue.substring(3, 9);
			int nPressureInPSI = 0;
			if (sensorValue != null) {
				nPressureInPSI = Integer.parseInt(sensorValue.substring(9, 12),
						16);
			}

			updateMinimumPressureText(sensorValue, transmitterID,
					nPressureInPSI);
		}
		txtSensorsBelowMinPressureCaptured.setText(String
				.valueOf(AppValues.mapSensorValuesBelowMinimumPressure.size()));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (bIsSettingsOpened) {
			bIsSettingsOpened = false;
			setContentView(R.layout.sensors);
			viewTopbar = (View) findViewById(R.id.sensorsTopbar);
			toggleSettings(true, viewTopbar);
			initSensorValues();
		} else {
			AlertDialog quitAlertDialog = new AlertDialog.Builder(
					MainActivity.this)
					.setTitle(getResources().getString(R.string.exit))
					.setMessage(getResources().getString(R.string.confirm_exit))
					.setPositiveButton(getResources().getString(R.string.ok),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
									finish();

								}
							})
					.setNegativeButton(
							getResources().getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
								}
							}).create();
			quitAlertDialog.setCanceledOnTouchOutside(false);
			quitAlertDialog.show();
		}
		return;
	}

	void hidekeyboard() {
		try {
			// View view = this.getCurrentFocus();
			// if (view == null) {
			// view = new View(this);
			// }
			// InputMethodManager im = (InputMethodManager) this
			// .getApplicationContext().getSystemService(
			// Activity.INPUT_METHOD_SERVICE);
			// im.hideSoftInputFromWindow(getWindow().getDecorView()
			// .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

			InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etConnectedStatus.getWindowToken(), 0);

		} catch (Exception e) {

		}
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etMinimumPressure.getWindowToken(), 0);
		} catch (Exception e1) {

		}
	}

	@Override
	void onPressureMeasurementValueToggled() {
		filterMinPressureValues();
	}

	@Override
	void updateLocale() {
		final View viewDeviceConnect = (View) findViewById(R.id.initial_device_connect);
		TextView txtHeader = (TextView) viewDeviceConnect
				.findViewById(R.id.txtHeader);
		txtHeader.setText(getResources().getString(R.string.set_up));

		TextView txtVersion = (TextView) viewDeviceConnect
				.findViewById(R.id.txtVersion);
		txtVersion.setText("Version: "
				+ getResources().getString(R.string.app_version));

		TextView txtCurrentStatus = (TextView) viewDeviceConnect
				.findViewById(R.id.txtCurrentStatus);
		txtCurrentStatus.setText(getResources().getString(
				R.string.current_status));

		TextView txtConnectedStatus = (TextView) viewDeviceConnect
				.findViewById(R.id.txtConnectedStatus);
		if (mBluetoothService == null
				|| mBluetoothService.getState() != BluetoothService.STATE_CONNECTED) {
			txtConnectedStatus.setText(getResources().getString(
					R.string.not_connected));
			txtConnectedStatus.setTextColor(getResources().getColor(
					R.color.not_connected));
			mConnectChangeDeviceButton.setText(getResources().getString(
					R.string.connect_device));
		} else if (mBluetoothService != null
				&& mBluetoothService.getState() == BluetoothService.STATE_CONNECTED) {
			txtConnectedStatus.setText(getResources().getString(
					R.string.connected));
			txtConnectedStatus.setTextColor(getResources().getColor(
					R.color.connected));
			mConnectChangeDeviceButton.setText(getResources().getString(
					R.string.change_device));
		}

		TextView txtConnectedToDevice = (TextView) viewDeviceConnect
				.findViewById(R.id.txtConnectToDevice);
		txtConnectedToDevice.setText(getResources().getText(
				R.string.to_a_device));

		// Button btnConnectChangeDevice = (Button) viewDeviceConnect
		// .findViewById(R.id.btnConnectChangeDevice);
		// btnConnectChangeDevice.setText(getResources().getString(
		// R.string.change_device));

		TextView txtLanguage, txtPressure;
		txtLanguage = (TextView) findViewById(R.id.txtLanguage);
		txtPressure = (TextView) findViewById(R.id.txtPressure);
		txtLanguage.setText(getResources().getString(R.string.language));
		txtPressure.setText(getResources().getString(
				R.string.pressuremeasurement));
	}

	public static void appendLog(String text) {
		File bridgeStone = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "Bridgestone");
		if (!bridgeStone.isDirectory()) {
			bridgeStone.mkdir();
		}

		File logFile = new File(bridgeStone, "log.txt");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.append(text);
			buf.newLine();
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
