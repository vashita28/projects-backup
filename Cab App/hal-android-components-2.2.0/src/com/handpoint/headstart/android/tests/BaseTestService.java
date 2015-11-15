package com.handpoint.headstart.android.tests;

import static com.handpoint.headstart.android.HeadstartService.ACTION_HANDLE_TIMEOUT;
import static com.handpoint.headstart.android.HeadstartService.ACTION_VERIFY_SIGNATURE;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_CONNECTION_STATE_CHANGE;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_DEVICE_FOUND;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_DISCOVERY_FINISHED;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_ERROR;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_FINANCIAL_TRANSACTION_FINISHED;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_OPERATION_CANCEL;
import static com.handpoint.headstart.android.HeadstartService.BROADCAST_TRANSACTION_STATE_CHANGE;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_ERROR_CODE;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_ERROR_DESCRIPTION;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_FINANCIAL_RESULT;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_MERCHANT_RECEIPT_TEXT;
import static com.handpoint.headstart.android.HeadstartService.EXTRA_TRANSACTION_STATE;
import static com.handpoint.headstart.android.HeadstartService.PREFERENCE_SIMULATION_MODE;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.test.ServiceTestCase;

import com.handpoint.headstart.android.Application;
import com.handpoint.headstart.android.HeadstartService;
import com.handpoint.headstart.android.HeadstartService.LocalBinder;
import com.handpoint.headstart.android.ParcelDeviceDescriptor;
import com.handpoint.headstart.android.ParcelDeviceState;
import com.handpoint.headstart.android.ParcelFinancialTransactionResult;
import com.handpoint.headstart.api.DeviceConnectionState;
import com.handpoint.headstart.api.DeviceDescriptor;
import com.handpoint.headstart.api.FinancialTransactionResult;
import com.handpoint.headstart.api.HeadstartOperationException;
import com.handpoint.headstart.eft.DeviceState;
import com.handpoint.headstart.spi.logging.LoggerManagerAndroid;
import com.handpoint.util.logging.ApplicationLogger;
import com.handpoint.util.logging.Level;
/**
 * This class is parent for testing <b>Headstart Android Lib</b>. 
 * <p>
 * It performs several main functions:
 * <ol>
 * <li>initialization (mock application, mock context, mock listeners, logging, binding to service).
 * <li>Represents the methods to perform basic operations: connect to card reader, disconnect,device discovery, financial transaction like sale, refund, void.
 * <li>Provides interface and default implementation listeners for basic operations.
 * <li>Provides access to internal properties like service, mock listeners, etc.
 * </ol>
 * All class that will do test of Android Lib should extends it.
 * <p>
 * Sample of usage with <b>doConnect</b> method:
 * <p><blockquote><pre>
 * import android.util.Log;
 *
 * import com.handpoint.headstart.android.tests.BaseTestService;
 * import com.handpoint.headstart.api.DeviceConnectionState;
 * import com.handpoint.headstart.api.DeviceDescriptor;
 * import com.handpoint.headstart.api.HeadstartOperationException;
 * import com.handpoint.headstart.spi.android.bluetooth.BluetoothDeviceManager;
 * import com.handpoint.headstart.spi.android.protocol.Mped400;
 * 
 * public class TestSample extends BaseTestService {
 * 
 *	private static final long CONNECTION_TIMEOUT = 30l; // 30 sec
 *	private static final String SIMULATION_DEVICE_NAME = "btspp://device1"; //device name for simulator
 * 			
 *	{@literal @}Override
 *	protected void setUp() throws Exception {
 *		//use simulator instead real device
 *		setSimulationMode(true);
 *		//always call parent setUp method
 *		super.setUp();
 *	}
 * 	
 * 	public void testConnection() {
 * 		try {
 * 			//create device descriptor for connect
 * 			DeviceDescriptor dd = new DeviceDescriptor(BluetoothDeviceManager.DEVICE_TYPE, SIMULATION_DEVICE_NAME);
 * 			// connect to device
 * 			assertTrue(doConnect(dd, Mped400.NAME, new byte[32], CONNECTION_TIMEOUT));
 * 			// check current status
 * 			assertEquals(DeviceConnectionState.CONNECTED, service.getCurrentConnectionState());
 * 			// disconnect from device
 * 			assertTrue(doDisconnect(CONNECTION_TIMEOUT));
 * 			// check current status
 * 			assertEquals(DeviceConnectionState.DISCONNECTED, service.getCurrentConnectionState());
 * 
 * 		} catch (HeadstartOperationException e) {
 * 			fail(e.getMessage());
 * 		}
 * 	}
 * }
 * </pre></blockquote>
 * Sample of creation DeviceDescriptor for real card reader:
 * <p><blockquote><pre>
 * DeviceDescriptor dd = new DeviceDescriptor(BluetoothDeviceManager.DEVICE_TYPE, "Friendly Name of Device");
 * dd.setAttribute("Address", "MAC address of device"); //where MAC address is string in format: "00:01:90:EC:36:99" 
 * </pre></blockquote>
 * <p>
 * More complex is usage of financial transaction methods: because financial methods have asynchronous nature we need some listener 
 * for handling events that occurs during transaction. Base Test class have default (mock) implementation of listener. 
 * It only sets some variables on events (see implementation in code).
 * You can write own listener for handling events (for example action on signature request).
 * <p>
 * Short sample (error will be occur because there is no connection was made before performing sale operation):
 * <blockquote><pre>
 *	public void testSale() {
 *		assertTrue(doSale(100, "0826", true, 40));
 *		assertTrue(((BaseTestService.DefaultTransactionStateListener)transactionListener).isFinished);		
 *		assertTrue(((BaseTestService.DefaultTransactionStateListener)transactionListener).isError);		
 *	}
 * </pre></blockquote>
 * <p>
 * Same for discovery operation.
 * <blockquote><pre>
 * public void testDiscovery() {
 * 		try {
 * 			assertTrue(doDiscovery(false, 60));
 * 		} catch (HeadstartOperationException e) {
 * 			fail(e.getMessage());
 * 		}
 * 		int deviceCounter = ((BaseTestService.DefaultDiscoveryListener)discoveryListener).deviceCounter;
 * 		assertTrue(deviceCounter > 0);
 * 		assertEquals(deviceCounter, ((BaseTestService.DefaultDiscoveryListener)discoveryListener).devices.length);
 * 	}
 * </pre></blockquote>
 */
public abstract class BaseTestService extends ServiceTestCase<HeadstartService> {
	
	protected HeadstartService.LocalBinder binder;
	/**
	 * Running service
	 */
	protected HeadstartService service;
	/**
	 * Logger manager
	 */
	protected LoggerManagerAndroid loggerManager;
	/**
	 * Listener for financial operations
	 */
	protected FinancialTransactionStateListener transactionListener;

	/**
	 * Listener for discovery operation
	 */
	protected DiscoveryListener discoveryListener;
	
	private boolean simulationMode;

	public BaseTestService() {
		this(HeadstartService.class);
	}

	private BaseTestService(Class<HeadstartService> serviceClass) {
		super(serviceClass);
	}

	/**
	 * Initializes Logger Manager, running mode (simulation or real), listeners, binds to service.
	 * This method should be always called in child class (if it overrides setUp method). 
	 */
	@Override
	protected void setUp() throws Exception {
		Application app = new MockTestApplication();
		setApplication(app);
		
		loggerManager = new LoggerManagerAndroid();
		loggerManager.setLevel(Level.ALL);
		ApplicationLogger.setLoggerManager(loggerManager);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
		Editor editor = preferences.edit();
		editor.putBoolean(PREFERENCE_SIMULATION_MODE, simulationMode);
		editor.commit();
				
		binder = (LocalBinder) bindService(new Intent(this.getContext(), HeadstartService.class));
		service = binder.getService();
		
		if (null == transactionListener) {
			transactionListener = new DefaultTransactionStateListener(service);
		}
		if (null == discoveryListener) {
			discoveryListener = new DefaultDiscoveryListener();
		}
	}
		
	private CountDownLatch connectionLatch;
	
	private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (BROADCAST_CONNECTION_STATE_CHANGE.equals(intent.getAction())) {
				int status = intent.getIntExtra(HeadstartService.EXTRA_PED_STATE, 0);
				if (status == DeviceConnectionState.CONNECTED) {
					connectionLatch.countDown();
				}
				if (status == DeviceConnectionState.DISCONNECTED) {
					connectionLatch.countDown();
				}
			} else if (BROADCAST_ERROR.equals(intent.getAction())) {
				connectionLatch.countDown();
			}
		}
	};

	private CountDownLatch financialTransactionLatch;

	private BroadcastReceiver financialTransactionReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (BROADCAST_TRANSACTION_STATE_CHANGE.equals(intent.getAction())) {
				//TODO: transaction state changed
				ParcelDeviceState parcel = intent.getParcelableExtra(EXTRA_TRANSACTION_STATE);
				if (null != parcel && null != transactionListener) {
					transactionListener.onTransactionStateChanged(parcel.getResult());
				}
			} else if (BROADCAST_FINANCIAL_TRANSACTION_FINISHED.equals(intent.getAction())) {
				//transaction finished
				ParcelFinancialTransactionResult parcel = intent.getParcelableExtra(EXTRA_FINANCIAL_RESULT);
				if (null != parcel && null != transactionListener) {
					transactionListener.onTransactionFinished(parcel.getResult());
				}				
				financialTransactionLatch.countDown();
			} else if (ACTION_VERIFY_SIGNATURE.equals(intent.getAction())) {
				//verify signature event
				if (null != transactionListener) {
					transactionListener.onVerifySignature(intent.getStringExtra(EXTRA_MERCHANT_RECEIPT_TEXT));
				}
			} else if (ACTION_HANDLE_TIMEOUT.equals(intent.getAction())) {
				//handle timeout event
				if (null != transactionListener) {
					transactionListener.onTimeout();
				}
			} else if (BROADCAST_ERROR.equals(intent.getAction())) {
				//error is happen
				String errorMessage = intent.getStringExtra(EXTRA_ERROR_DESCRIPTION);
				int code = intent.getIntExtra(EXTRA_ERROR_CODE, 0);
				if (null != transactionListener) {
					transactionListener.onTransactionError(code, errorMessage);
				}
				financialTransactionLatch.countDown();
			} else if (BROADCAST_OPERATION_CANCEL.equals(intent.getAction())) {
				//transaction is cancelled
				if (null != transactionListener) {
					transactionListener.onTransactionCancelled();
				}
				financialTransactionLatch.countDown();
			}
		}
		
	};
	
	private CountDownLatch discoveryLatch;
	
	private BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
			if (BROADCAST_DEVICE_FOUND.equals(intent.getAction())) {
				ParcelDeviceDescriptor dd = intent.getParcelableExtra(HeadstartService.EXTRA_REMOTE_DEVICE);
				if (null != discoveryListener && null != dd) {
					discoveryListener.onDeviceFound(dd.getResult());
				}

			} else if (BROADCAST_DISCOVERY_FINISHED.equals(intent.getAction())) {
				Parcelable[] tmp = intent.getParcelableArrayExtra(HeadstartService.EXTRA_REMOTE_DEVICE);
				if (null != discoveryListener && null != tmp) {
					DeviceDescriptor[] devices = new DeviceDescriptor[tmp.length];
					for (int i = 0; i < tmp.length; i++) {
						devices[i] = ((ParcelDeviceDescriptor)tmp[i]).getResult();
					}
					discoveryListener.onDiscoveryFinished(devices);
				}
				
				discoveryLatch.countDown();
			} else if (BROADCAST_ERROR.equals(intent.getAction())) {
				discoveryLatch.countDown();
			}
		}
	};
	
	/**
	 * Starts device discovery operation.
	 * @param force - This parameter forces resetting cache and re-initiation of fresh discovery
	 * @param delay - time to waiting in seconds (is used to prevent infinite wait in case of an error)
	 * @return boolean, false if timeout is occurred, otherwise true
	 * @throws HeadstartOperationException - if error is occurred during discovery operation
	 */
	public boolean  doDiscovery(boolean force, long delay) throws HeadstartOperationException {
		registerDiscoveryReceiver();

		discoveryLatch = new CountDownLatch(1);
		service.discoverDevices(force);
		try {
			return discoveryLatch.await(delay, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}

	private void registerDiscoveryReceiver() {
		IntentFilter btServiceFilter = new IntentFilter();
        btServiceFilter.addAction(BROADCAST_DEVICE_FOUND);
        btServiceFilter.addAction(BROADCAST_DISCOVERY_FINISHED);
        btServiceFilter.addAction(BROADCAST_ERROR);
        getContext().registerReceiver(discoveryReceiver, btServiceFilter);		
	}
	/**
	 * Starts connect operation.
	 * @param dd -  remote device(card reader) descriptor
	 * @param protocolName - protocol name
	 * @param ss - byte array containing shared secret
	 * @param delay- time to waiting in seconds (is used to prevent infinite wait in case of an error)
	 * @return boolean, false if timeout is occurred, otherwise true
	 * @throws HeadstartOperationException
	 */
	public boolean  doConnect(DeviceDescriptor dd, String protocolName, byte[] ss, long delay) throws HeadstartOperationException {
		registerConnectionReceiver();

		connectionLatch = new CountDownLatch(1);
		service.connectToDevice(dd, protocolName, ss);
		try {
			return connectionLatch.await(delay, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
	/**
	 * Starts disconnect operation.
	 * @param delay- time to waiting in seconds (is used to prevent infinite wait in case of an error)
	 * @return boolean, false if timeout is occurred, otherwise true
	 * @throws HeadstartOperationException
	 */
	public boolean doDisconnect(long delay) {
		registerConnectionReceiver();
		connectionLatch = new CountDownLatch(1);
		service.disconnectFromDevice();
		try {
			return connectionLatch.await(delay, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	private void registerConnectionReceiver() {
		IntentFilter btServiceFilter = new IntentFilter();
        btServiceFilter.addAction(BROADCAST_CONNECTION_STATE_CHANGE);
        btServiceFilter.addAction(BROADCAST_ERROR);
		getContext().registerReceiver(connectionReceiver, btServiceFilter);		
	}
	/**
	 * Starts sale financial transaction.
	 * @param amount - transaction amount in cents
	 * @param currencyCode - transaction currency code
	 * @param cardHolderPresent - boolean value
	 * @param delay- time to waiting in seconds (is used to prevent infinite wait in case of an error)
	 * @return boolean, false if timeout is occurred, otherwise true
	 */
	public boolean doSale(int amount, String currencyCode, boolean cardHolderPresent, long delay) {
		registerTransactionStateReceiver();
		
		financialTransactionLatch = new CountDownLatch(1);
		service.startSaleTransaction(amount, currencyCode, cardHolderPresent);
		try {
			return financialTransactionLatch.await(delay, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	/**
	 * Starts sale void financial transaction.
	 * @param originTransactionId - original transaction ID
	 * @param amount - transaction amount in cents
	 * @param currencyCode - transaction currency code
	 * @param cardHolderPresent - boolean value
	 * @param delay- time to waiting in seconds (is used to prevent infinite wait in case of an error)
	 * @return boolean, false if timeout is occurred, otherwise true
	 */	
	public boolean doSaleVoid(String originTransactionId, int amount, String currencyCode, boolean cardHolderPresent, long delay) {
		registerTransactionStateReceiver();
		
		financialTransactionLatch = new CountDownLatch(1);
		service.startSaleVoidTransaction(originTransactionId, amount, currencyCode, cardHolderPresent);
		try {
			return financialTransactionLatch.await(delay, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	/**
	 * Starts refund financial transaction.
	 * @param amount - transaction amount in cents
	 * @param currencyCode - transaction currency code
	 * @param cardHolderPresent - boolean value
	 * @param delay- time to waiting in seconds (is used to prevent infinite wait in case of an error)
	 * @return boolean, false if timeout is occurred, otherwise true
	 */	
	public boolean doRefund(int amount, String currencyCode, boolean cardHolderPresent, long delay) {
		registerTransactionStateReceiver();
		
		financialTransactionLatch = new CountDownLatch(1);
		service.startRefundTransaction(amount, currencyCode, cardHolderPresent);
		try {
			return financialTransactionLatch.await(delay, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	/**
	 * Starts refund void financial transaction.
	 * @param originTransactionId - original transaction ID
	 * @param amount - transaction amount in cents
	 * @param currencyCode - transaction currency code
	 * @param cardHolderPresent - boolean value
	 * @param delay- time to waiting in seconds (is used to prevent infinite wait in case of an error)
	 * @return boolean, false if timeout is occurred, otherwise true
	 */	
	public boolean doRefundVoid(String originTransactionId, int amount, String currencyCode, boolean cardHolderPresent, long delay) {
		registerTransactionStateReceiver();
		
		financialTransactionLatch = new CountDownLatch(1);
		service.startRefundVoidTransaction(originTransactionId, amount, currencyCode, cardHolderPresent);
		try {
			return financialTransactionLatch.await(delay, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
	
	/**
	 * Sets custom implementation of FinancialTransactionStateListener instead using default 
	 * @param mockDiscoveryListener - custom implementation of FinancialTransactionStateListener
	 */
	public void setTransactionListener(FinancialTransactionStateListener transactionListener) {
		this.transactionListener = transactionListener;
	}

	/**
	 * Sets custom implementation of DiscoveryListener instead using default 
	 * @param mockDiscoveryListener - custom implementation of DiscoveryListener
	 */
	public void setDiscoveryListener(DiscoveryListener discoveryListener) {
		this.discoveryListener = discoveryListener;
	}

	private void registerTransactionStateReceiver() {
		IntentFilter btServiceFilter = new IntentFilter();
        btServiceFilter.addAction(BROADCAST_TRANSACTION_STATE_CHANGE);
        btServiceFilter.addAction(BROADCAST_FINANCIAL_TRANSACTION_FINISHED);
        btServiceFilter.addAction(BROADCAST_ERROR);
        btServiceFilter.addAction(BROADCAST_OPERATION_CANCEL);
        btServiceFilter.addAction(ACTION_VERIFY_SIGNATURE);
        btServiceFilter.addAction(ACTION_HANDLE_TIMEOUT);
        
        getContext().registerReceiver(financialTransactionReceiver, btServiceFilter);
	}

	/**
	 * Returns is service was started in simulation mode.
	 * @return true - service was started in simulation mode, otherwise - false
	 */
	public boolean isSimulationMode() {
		return simulationMode;
	}

	/**
	 * Sets mode in which service will be started
	 * @param simulationMode - true - start service in simulation mode.
	 */
	public void setSimulationMode(boolean simulationMode) {
		this.simulationMode = simulationMode;
	}


	/**
	 * Interface of listener for handling events during device discovery operation 
	 */
	protected interface DiscoveryListener {

		/**
		 * Called when new device is found
		 * @param dd - descriptor of found device
		 */
		public void onDeviceFound(DeviceDescriptor dd);
		
		/**
		 * Called when discovery process is finished
		 * @param devices - array of founded devices
		 */
		public void onDiscoveryFinished(DeviceDescriptor[] devices);
	}
	
	/**
	 * Default implementation of {@link DiscoveryListener} interface
	 *
	 */
	public static class DefaultDiscoveryListener implements DiscoveryListener {
		
		public int deviceCounter = 0;
		public DeviceDescriptor[] devices = new DeviceDescriptor[0];
		
		public void reset() {
			deviceCounter = 0;
			devices = new DeviceDescriptor[0];
		}
		
		@Override
		public void onDeviceFound(DeviceDescriptor dd) {
			deviceCounter++;
		}

		@Override
		public void onDiscoveryFinished(DeviceDescriptor[] devices) {
			this.devices = devices;
		}
		
	}
	
	/**
	 * Interface of listener for handling events during financial transaction 
	 */
	protected interface FinancialTransactionStateListener {
		
		/**
		 * Called when transaction state is changed
		 * @param state - new state of transaction
		 */
		public void onTransactionStateChanged(DeviceState state);
		
		/**
		 * Called when transaction is finished
		 * @param result - result of financial transaction
		 */
		public void onTransactionFinished(FinancialTransactionResult result);
		
		/**
		 * Called if error is occurred during financial transaction
		 * @param code
		 * @param message
		 */
		public void onTransactionError(int code, String message);
		
		/**
		 * Called if transaction was cancelled
		 */
		public void onTransactionCancelled();
		
		/**
		 * Called when signature verification is required during financial transaction
		 * @param text
		 */
		public void onVerifySignature(String text);
		
		/**
		 * Called if timeout is occurred during financial transaction
		 */
		public void onTimeout();
		
	}

	/**
	 * Default implementation of {@link FinancialTransactionStateListener} interface
	 *
	 */
	public static class DefaultTransactionStateListener implements FinancialTransactionStateListener {

		private HeadstartService service;
		
		public int stateChangedCounter = 0;
		public boolean isFinished = false;
		public boolean isError = false;
		public boolean isCancelled = false;
		public boolean wasVerification = false;
		public boolean wasTimeout = false;

		public DefaultTransactionStateListener(HeadstartService service) {
			this.service = service;
		}
		
		public void reset() {
			stateChangedCounter = 0;
			isFinished = false;
			isError = false;
			isCancelled = false;
			wasVerification = false;
			wasTimeout = false;
		}
		
		@Override
		public void onTransactionStateChanged(DeviceState state) {
			stateChangedCounter++;
		}

		@Override
		public void onTransactionFinished(FinancialTransactionResult result) {
			isFinished = true;
		}

		@Override
		public void onTransactionError(int code, String message) {
			isFinished = true;
			isError = true;
			service.setLastTransactionHandled();
		}

		@Override
		public void onTransactionCancelled() {
			isFinished = true;
			isCancelled = true;
		}

		@Override
		public void onVerifySignature(String text) {
			wasVerification = true;
			service.signatureAccept();
		}

		@Override
		public void onTimeout() {
			wasTimeout = true;
			service.timeoutHandleResult(false);
		}		
	}
}
