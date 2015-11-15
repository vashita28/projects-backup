package com.handpoint.headstart.android.ui;

import static com.handpoint.headstart.android.HeadstartService.BROADCAST_DEVICE_FOUND;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_DISCOVERY_FINISHED;

import java.util.Vector;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handpoint.headstart.R;
import com.handpoint.headstart.android.HeadstartService;
import com.handpoint.headstart.android.HeadstartServiceConnection;
import com.handpoint.headstart.android.ParcelDeviceDescriptor;
import com.handpoint.headstart.api.DeviceDescriptor;
import com.handpoint.headstart.api.HeadstartOperationException;
import com.handpoint.util.logging.ApplicationLogger;
import com.handpoint.util.logging.Level;
import com.handpoint.util.logging.Logger;

/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the DeviceDescriptor (with MAC address) of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class DeviceListActivity extends Activity {
    // Debugging
    private static final String TAG = DeviceListActivity.class.getSimpleName();
	private static Logger logger = ApplicationLogger.getLogger(TAG);
    
    // Return Intent extra
    public static String EXTRA_DEVICE = "extra_device";

    // Member fields
	HeadstartServiceConnection mConnection;
	BluetoothAdapter mBtAdapter;
	ListView mPairedListView;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    boolean mRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.device_list);

        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);
        
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Initialize the button to perform device discovery
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

    	mConnection = new HeadstartServiceConnection(this, false,
    			new HeadstartServiceConnection.BindListener() {
			
			public void onBindCompleted() {
				try {
					mConnection.getService().discoverDevices(false);
				} catch (HeadstartOperationException e) {
					logger.log(Level.WARNING, "Error on device discovering", e);
				}				
			}
		});
        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        mPairedListView = (ListView) findViewById(R.id.paired_devices);
        mPairedListView.setAdapter(mPairedDevicesArrayAdapter);
        mPairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
    }

    @Override
    protected void onResume() {
    	super.onResume();
        registerBroadcastReceivers();
        mRegistered = true;
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	if (mRegistered) {
    		mRegistered = false;
    		unregisterReceiver(mReceiver);
    	}
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();

    	if (null != mConnection) {
			mConnection.getService().stopDiscoverDevices();    		
    		mConnection.doUnbindService(false);
    	}
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
    	logger.log(Level.FINE, "doDiscovery()");


		if (null != mBtAdapter && mBtAdapter.isEnabled() && mConnection.isBinded()) {
	        // Indicate scanning in the title
	        setProgressBarIndeterminateVisibility(true);
	        setTitle(R.string.scanning);

	        // Turn on sub-title for new devices
	        findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
			mNewDevicesArrayAdapter.clear();
			try {
				mConnection.getService().discoverDevices(true);
			} catch (HeadstartOperationException e) {
				logger.log(Level.WARNING, "Error on device discovering", e);
			}			
		}
    }
    
	public void onDeviceFound(final DeviceDescriptor deviceDescriptor) {
        if (!Integer.toString(BluetoothDevice.BOND_BONDED).equals(deviceDescriptor.getAttribute("State"))) {
            mNewDevicesArrayAdapter.add(deviceDescriptor.getName() + "\n" + deviceDescriptor.getAttribute("Address"));
        } else {        
        	mPairedDevicesArrayAdapter.add(deviceDescriptor.getName() + "\n" + deviceDescriptor.getAttribute("Address"));
        }
	}
    
	public void onDiscoveryCompleted(final Vector deviceDescriptors) {
        setProgressBarIndeterminateVisibility(false);
        setTitle(R.string.select_device);
        if (mPairedDevicesArrayAdapter.isEmpty()) {
	        String noDevices = getResources().getText(R.string.none_paired).toString();
	        mPairedDevicesArrayAdapter.add(noDevices);
	        mPairedListView.setClickable(false);
	        mPairedListView.setDuplicateParentStateEnabled(true);
        } else {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
        }				
	}

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
			mConnection.getService().stopDiscoverDevices();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            int index = info.indexOf('\n');
            String name = "";
            if (index > 0) {
            	name = info.substring(0, index);
            }
            String address = info.substring(info.length() - 17);

            DeviceDescriptor descriptor = new DeviceDescriptor(DeviceDescriptor.DEVICE_TYPE_BT, name);
            descriptor.setAttribute("Address", address);
            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE, new ParcelDeviceDescriptor(descriptor));

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
    
	private void registerBroadcastReceivers() {
		IntentFilter btServiceFilter = new IntentFilter();
        btServiceFilter.addAction(BROADCAST_DEVICE_FOUND);
        btServiceFilter.addAction(BROADCAST_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, btServiceFilter);
	}

	/**
	 * Broadcast receiver for handling events from service 
	 */
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BROADCAST_DEVICE_FOUND.equals(intent.getAction())) {
				ParcelDeviceDescriptor dd = intent.getParcelableExtra(HeadstartService.EXTRA_REMOTE_DEVICE);
				onDeviceFound(dd.getResult());
			} else if (BROADCAST_DISCOVERY_FINISHED.equals(intent.getAction())) {
				//skip returned set of remote devices, because we already have them
				onDiscoveryCompleted(new Vector());
			}
		}
		
	};	

}
