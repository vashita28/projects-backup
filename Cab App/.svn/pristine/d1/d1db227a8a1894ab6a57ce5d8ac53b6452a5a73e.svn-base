/*
 *
 *  * This file is subject to the terms and conditions defined in
 *  * file 'license.txt', which is part of this source code package.
 *
 */

package com.handpoint.headstart.android;

import java.util.Properties;
import java.util.Vector;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.handpoint.headstart.R;
import com.handpoint.headstart.api.*;
import com.handpoint.headstart.api.android.AndroidHeadstart;
import com.handpoint.headstart.eft.DeviceState;
import com.handpoint.headstart.pc.simulator.MockHeadstart;
import com.handpoint.headstart.pc.simulator.Mped400SimulatorProvider;
import com.handpoint.headstart.spi.android.serial.SerialDeviceManager;
import com.handpoint.util.NotificationMgr;
import com.handpoint.util.logging.ApplicationLogger;
import com.handpoint.util.logging.Level;
import com.handpoint.util.logging.Logger;
import jp.co.casio.caios.framework.device.SerialCom;

/**
 * <p>Service for executing operation with remote device (PED) via Bluetooth network stack.
 * Service starts separate threads in which all operations with remote device will be performed.
 * <p> Bluetooth service requires BLUETOOTH_ADMIN, BLUETOOTH and INTERNET permission</p>
 */
public class HeadstartService
        extends Service 
        implements DeviceLogDownloadListener,
        DeviceStateListener,
        XMLCommandListener,
        TimeoutHandler, 
        SignatureVerifier,
        DeviceDiscoveryListener {

    private static final String TAG = HeadstartService.class.getSimpleName();
	private static Logger logger = ApplicationLogger.getLogger(TAG);

    public static final String BROADCAST_DEVICE_FOUND = "com.handpoint.headstart.hal.CONNECTION_DEVICE_FOUND";
    public static final String BROADCAST_DISCOVERY_FINISHED = "com.handpoint.headstart.hal.CONNECTION_DISCOVERY_FINISHED";
    public static final String BROADCAST_CONNECTION_STATE_CHANGE = "com.handpoint.headstart.hal.CONNECTION_STATE_CHANGED";
    public static final String BROADCAST_TRANSACTION_STATE_CHANGE = "com.handpoint.headstart.hal.TRANSACTION_STATE_CHANGED";
    public static final String BROADCAST_FINANCIAL_TRANSACTION_FINISHED = "com.handpoint.headstart.hal.FINANCIAL_TRANSACTION_FINISHED";
    public static final String BROADCAST_XML_COMMAND_REQUEST_FINISHED = "com.handpoint.headstart.hal.XML_COMMAND_REQUEST_FINISHED";

    public static final String BROADCAST_ERROR = "com.handpoint.headstart.hal.ERROR";
    public static final String BROADCAST_OPERATION_CANCEL = "com.handpoint.headstart.hal.CANCELL";

    // additional transaction state constants
    public static final int ADDITIONAL_DEVICE_STATE_STARTING = 100;
    public static final int ADDITIONAL_DEVICE_STATE_DOWNLOADING_LOGS = 101;
    public static final int ADDITIONAL_DEVICE_STATE_HANDLING_TIMEOUT = 102;
    public static final int ADDITIONAL_DEVICE_STATE_SIGNATURE_VERIFIED = 103;
    public static final int ADDITIONAL_DEVICE_STATE_DOWNLOADING_LOGS_FINISHED = 104;
    public static final int ADDITIONAL_DEVICE_STATE_FINISHED = 200;

    public static final String ACTION_MAIN = "com.handpoint.action.MAIN";
    public static final String ACTION_DEVICE_PICK = "com.handpoint.settings.connection.PICK";
    public static final String ACTION_DISCOVERY = "com.handpoint.headstart.hal.ACTION_DISCOVERY";
    public static final String ACTION_CONNECT_PED = "com.handpoint.headstart.hal.ACTION_CONNECT_PED";
    public static final String ACTION_DISCONNECT_PED = "com.handpoint.headstart.hal.ACTION_DISCONNET_PED";
    public static final String ACTION_SALE = "com.handpoint.headstart.hal.ACTION_SALE";
    public static final String ACTION_SALE_VOID = "com.handpoint.headstart.hal.ACTION_SALE_VOID";
    public static final String ACTION_REFUND = "com.handpoint.headstart.hal.ACTION_REFUND";
    public static final String ACTION_REFUND_VOID = "com.handpoint.headstart.hal.ACTION_REFUND_VOID";
    public static final String ACTION_CANCEL = "com.handpoint.headstart.hal.ACTION_CANCEL";

    public static final String ACTION_BLUETOOTH = "com.handpoint.headstart.hal.ACTION_BLUETOOTH";
    public static final String ACTION_CONNECTION = "com.handpoint.headstart.hal.ACTION_CONNECTION";
    public static final String ACTION_CONNECTION_PROGRESS = "com.handpoint.headstart.hal.ACTION_CONNECTION_PROGRESS";
    public static final String ACTION_FT_PROGRESS = "com.handpoint.headstart.hal.ACTION_FT_PROGRESS";
	public static final String ACTION_VERIFY_SIGNATURE = "com.handpoint.headstart.hal.ACTION_VERIFY_SIGNATURE";
	public static final String ACTION_HANDLE_TIMEOUT = "com.handpoint.headstart.hal.ACTION_HANDLE_TIMEOUT";

    public static final String EXTRA_REMOTE_DEVICE = "com.handpoint.headstart.hal.extra_remote_device";
    public static final String EXTRA_REMOTE_DEVICES = "com.handpoint.headstart.hal.extra_remote_devices";
    public static final String EXTRA_PAYMENT_VALUE = "com.handpoint.headstart.hal.extra_payment_value";
    public static final String EXTRA_PAYMENT_CURRENCY = "com.handpoint.headstart.hal.extra_payment_currency";
    public static final String EXTRA_PAYMENT_TRANSACTION_ID = "com.handpoint.headstart.hal.extra_payment_transaction_id";
    public static final String EXTRA_PAYMENT_CUSTOMER_REFERENCE = "com.handpoint.headstart.hal.extra_customer_reference";
    public static final String EXTRA_PAYMENT_DIVIDE_BY_MONTHS = "com.handpoint.headstart.hal.extra_divide_by_months";

    public static final String EXTRA_PED_STATE = "com.handpoint.headstart.hal.extra_ped_state";
    public static final String EXTRA_OPERATION_TYPE = "com.handpoint.headstart.hal.extra_operation_type";
    public static final String EXTRA_FINANCIAL_RESULT = "com.handpoint.headstart.hal.extra_financial_result";
    public static final String EXTRA_BACK_RESULT = "com.handpoint.headstart.hal.extra_back_status";
    public static final String EXTRA_ERROR_DESCRIPTION = "com.handpoint.headstart.hal.extra_error_description";
    public static final String EXTRA_ERROR_CODE = "com.handpoint.headstart.hal.extra_error_code";
	public static final String EXTRA_MERCHANT_RECEIPT_TEXT = "com.handpoint.headstart.hal.EXTRA_MERCHANT_RECEIPT_TEXT";
    public static final String EXTRA_SIGNATURE_TIMEOUT_SECONDS = "com.handpoint.headstart.hal.EXTRA_SIGNATURE_TIMEOUT_SECONDS";
    public static final String EXTRA_TRANSACTION_STATE = "com.handpoint.headstart.hal.extra_transaction_state";

    public static final String EXTRA_XML_COMMAND_RESULT = "com.handpoint.headstart.hal.extra_xml_command_result";

    //Common preferences
    public static final String PREFERENCE_SIMULATION_MODE = "mode_simulator";
    public static final String PREFERENCE_SS_KEY = "shared_secret_key";
    public static final String PREFERENCE_SIMULATOR_DELAY = "simulator_delay";
    public static final String PREFERENCE_LAST_USED_ADDRESS = "last_used_device_address";
    public static final String PREFERENCE_LAST_USED_NAME = "last_used_device_name";
    public static final String PREFERENCE_LAST_USED_TYPE = "last_used_device_type";
    public static final String PREFERENCE_DEFAULT_CURRENCY = "default_currency";
    public static final String PREFERENCE_MERCHANT_NAME = "merchant_name";
    public static final String PREFERENCE_MERCHANT_EMAIL_ADDRESS = "merchant_email_address";

    //Connection type preferences
    public static final String PREFERENCE_CONNECTION_TYPE = AndroidHeadstart.PREFERENCE_CONNECTION_TYPE;

    // Serial connection preferences
	public static final String PREFERENCE_COM_PORT = SerialDeviceManager.PREFERENCE_COM_PORT;
	public static final String PREFERENCE_BAUD_RATE = SerialDeviceManager.PREFERENCE_BAUD_RATE;
	public static final String PREFERENCE_NUMBER_OF_BITS = SerialDeviceManager.PREFERENCE_NUMBER_OF_BITS;
	public static final String PREFERENCE_PARITY = SerialDeviceManager.PREFERENCE_PARITY;
	public static final String PREFERENCE_STOP_BITS = SerialDeviceManager.PREFERENCE_STOP_BITS;
	public static final String PREFERENCE_HANDSHAKE = SerialDeviceManager.PREFERENCE_HANDSHAKE;
	public static final String PREFERENCE_FLOW_CONTROL = SerialDeviceManager.PREFERENCE_FLOW_CONTROL;

    // Fields for saving Financial Transaction
    static final String PREFERENCE_LAST_FT_TYPE = "last_ft_type";
    static final String PREFERENCE_LAST_FT_OPERATION_STATUS = "last_ft_operation_status";
    static final String PREFERENCE_LAST_FT_TRANSACTION_STATUS = "last_ft_transaction_status";
    static final String PREFERENCE_LAST_FT_AMOUNT = "last_ft_amount";
    static final String PREFERENCE_LAST_FT_TRANSACTION_ID = "last_ft_transaction_id";
    static final String PREFERENCE_LAST_FT_MERCHANT_RECEIPT = "last_ft_merchant_receipt";
    static final String PREFERENCE_LAST_FT_CUSTOMER_RECEIPT = "last_ft_customer_receipt";
    static final String PREFERENCE_LAST_FT_CURRENCY = "last_ft_currency";
    static final String PREFERENCE_LAST_FT_STATUS_MESSAGE = "last_ft_status_message";
    static final String PREFERENCE_LAST_FT_TRANSACTION_TYPE = "last_ft_transaction_type";
    static final String PREFERENCE_LAST_FT_FINANCIAL_STATUS = "last_ft_financial_status";
    static final String PREFERENCE_LAST_FT_REQUESTED_AMOUNT = "last_ft_requested_amount";
    static final String PREFERENCE_LAST_FT_GRATUITY_AMOUNT = "last_ft_gratuity_amount";
    static final String PREFERENCE_LAST_FT_GRATUITY_PERCENTAGE = "last_ft_gratuity_percentage";
    static final String PREFERENCE_LAST_FT_TOTAL_AMOUNT = "last_ft_total_amount";
    static final String PREFERENCE_LAST_FT_EFT_TRANSACTION_ID = "last_ft_eft_transaction_id";
    static final String PREFERENCE_LAST_FT_EFT_TIMESTAMP = "last_ft_eft_timestamp";
    static final String PREFERENCE_LAST_FT_AUTHORISATION_CODE = "last_ft_authorisation_code";
    static final String PREFERENCE_LAST_FT_CVM = "last_ft_cvm";
    static final String PREFERENCE_LAST_FT_CARD_ENTRY_TYPE = "last_ft_card_entry_type";
    static final String PREFERENCE_LAST_FT_CARD_SCHEME_NAME = "last_ft_card_scheme_name";
    static final String PREFERENCE_LAST_FT_ERROR_MESSAGE = "last_ft_error_message";
    static final String PREFERENCE_LAST_FT_CUSTOMER_REFERENCE = "last_ft_customer_reference";
    static final String PREFERENCE_LAST_FT_DIVIDE_BY_MONTHS = "last_ft_divide_by_months";
    
    //Field for saving signature request text
    static final String PREFERENCE_MERCHANT_SIGNATURE_TEXT = "last_ft_merchant_signature_text";
    static final String PREFERENCE_SIGNATURE_TIMEOUT_SECONDS = "last_ft_signature_timeout_seconds";

    // Fields for saving XML Command Type Results
    static final String PREFERENCE_LAST_XML_CMD_TYPE = "last_xml_cmd_type";
    static final String PREFERENCE_LAST_XML_CMD_SERIAL_NUMBER = "last_xml_cmd_serial_number";
    static final String PREFERENCE_LAST_XML_CMD_STATUS_MESSAGE = "last_xml_cmd_status_message";
    static final String PREFERENCE_LAST_XML_CMD_BATTERY_STATUS = "last_xml_cmd_battery_status";
    static final String PREFERENCE_LAST_XML_CMD_BATTERY_MV = "last_xml_cmd_battery_mv";
    static final String PREFERENCE_LAST_XML_CMD_BATTERY_CHARGING = "last_xml_cmd_battery_charging";
    static final String PREFERENCE_LAST_XML_CMD_EXTERNAL_POWER = "last_xml_cmd_external_power";
    static final String PREFERENCE_LAST_XML_CMD_OLD_BT_NAME = "last_xml_cmd_old_bt_name";
    static final String PREFERENCE_LAST_XML_CMD_BT_NAME = "last_xml_cmd_";
    static final String PREFERENCE_LAST_XML_CMD_RECONNECTION_REQUIRED = "last_xml_cmd_reconnection_required";

    public static final String BACK_ACTION_BLUETOOTH = "com.handpoint.headstart.hal.bt.back";
    public static final String BACK_ACTION_CONNECT = "com.handpoint.headstart.hal.connection.back";
    
    public static final int BACK_RESULT_OK = 0;
    public static final int BACK_RESULT_CANCEL = 1;

//    private static HeadstartService mService;
  
    private static Properties mProperties;
    Headstart mHeadstart;
    SharedPreferences mPreferences;
    String mProtocolName;
    HeadstartDeviceDiscovery mHdd;
    
    //current remote device descriptor
    DeviceDescriptor mDevice;
    //current connection to remote device
    HeadstartDeviceConnection mConnection;
    //current remote device state during transaction
    DeviceState mDeviceState;
    // is transaction running flag
    boolean mInTransaction;
    // is downloading logs flag
    boolean mDownloadingLogs;
    // path to signature image for current transaction
    String mPath;
    // state of error occurred during last transaction
    Throwable mError;
    // was connection attempt performed
    boolean mWasConnectionAttempt;
    // holder for signature request text
	private String mMerchantText;

    //internal state of signature verification step
    private boolean mSignatureOk;
    private final IBinder mBinder = new LocalBinder();

    /**
     * Class used for the client Binder.
     */
    public class LocalBinder extends Binder {
    	public HeadstartService getService() {
            // Return this instance of LocalService so clients can call public methods
            return HeadstartService.this;
        }
    }

    @Override
    public void onCreate() {
    	logger.log(Level.FINEST, "Core service created");
        super.onCreate();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mProperties = new Properties();
        NotificationMgr.init(this);
        notifyDisconneted();
        initDefaultValues();
        initHeadstart();

    }
    
	private void initDefaultValues() {

        if ("".equals(mPreferences.getString(HeadstartService.PREFERENCE_DEFAULT_CURRENCY, ""))) {
            mPreferences.edit().putString(HeadstartService.PREFERENCE_DEFAULT_CURRENCY,"0826").commit();
        }
        if ("".equals(mPreferences.getString(HeadstartService.PREFERENCE_SS_KEY, ""))) {
			mPreferences.edit().putString(
					HeadstartService.PREFERENCE_SS_KEY,
					"FF00000000000000000000000000000000000000000000000000000000000000").commit();
		}
		if ("".equals(mPreferences.getString(HeadstartService.PREFERENCE_CONNECTION_TYPE, ""))) {
			mPreferences.edit().putString(
					HeadstartService.PREFERENCE_CONNECTION_TYPE, DeviceDescriptor.DEVICE_TYPE_BT).commit();
		}
	}
	
    private void initHeadstart() {
    	if (mPreferences.getBoolean(PREFERENCE_SIMULATION_MODE, false)) {
    		int delay = 0;
    		try {
    			delay = Integer.parseInt(mPreferences.getString(HeadstartService.PREFERENCE_SIMULATOR_DELAY, "0"));
			} catch (NumberFormatException ignore) {
			}
    		mHeadstart = new MockHeadstart(delay, new Mped400SimulatorProvider());
    	} else {
    		mHeadstart = new AndroidHeadstart(this);
    	}
    }

    /**
     * Refreshes headstart library with disconnection from current remote device
     */
    public void refresh() {
    	disconnectFromDevice();
    	initHeadstart();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	logger.log(Level.FINEST, "Core service destroyed");
    	disconnectFromDevice();
    	NotificationMgr.getInstance().cancelStatusChanged();
    	mProperties = null;
    }
    /**
     * Discovers remote devices
     * @param force - This parameter forces resetting cache and re-initiation of fresh discovery
     * @throws HeadstartOperationException
     */
    public void discoverDevices(boolean force) 
    throws HeadstartOperationException {
		 mHdd = mHeadstart.getDeviceDiscovery(mPreferences.getString(HeadstartService.PREFERENCE_CONNECTION_TYPE, ""));
		 DiscoveryDevicesAsyncTask taskDiscovery = new DiscoveryDevicesAsyncTask();
		 taskDiscovery.execute(new DiscoveryDevicesHolder(mHdd, this, force));
    }

    /**
     * Stops remote devices discovery.
     */
    public void stopDiscoverDevices() {
    	if (null != mHdd) {
    		mHdd.stop();    		
    	}
    }
    
    /**
     * Returns current remote device connection state.
     * @return int - current state
     */
    public int getCurrentConnectionState() {
    	if (null == mConnection) {
    		return DeviceConnectionState.DISCONNECTED;
    	}
    	return mConnection.getState();
    }
    
    /**
     * Returns: is financial transaction in progress
     * @return boolean
     */
    public boolean isInTransaction() {
    	return mInTransaction; 
    }

    /**
     * Returns: are logs being downloaded
     * @return boolean
     */
    public boolean isDownloadingLogs() {
        return mDownloadingLogs;
    }
    
    /**
     * Returns is new financial transaction can be started 
     * @return boolean
     */
    public boolean isNewTransactionAvailable() {
    	return !mInTransaction && null == mDeviceState;
    }
    /**
     * Returns status of handling of last financial transaction
     * @return true - if last transaction result was handled
     */
    public boolean isLastTransactionHandled() {
    	return null == getLastTransaction() && null == mError;
    }
    /**
     * Returns current transaction state or null if there is not transaction in progress 
     * @return null if there is no transaction in progress or transaction state
     */
    public DeviceState getDeviceState() {
    	return mDeviceState;
    }
    /**
     * Returns flag of connection attempt to remote device
     * @return false - no connection attempts were done from service start, true-otherwise
     */
    public boolean wasConnectionAttempt() {
    	return mWasConnectionAttempt;
    }
    /**
     * Connects to remote device 
     * @param descriptor - remote device descriptor
     * @param protocolName - protocol name
     * @param ss - byte array containing shared secret
     * @throws HeadstartOperationException
     */
    public void connectToDevice(DeviceDescriptor descriptor, String protocolName, byte[] ss) throws HeadstartOperationException {
    	disconnectFromDevice();
    	mDevice = descriptor;
    	mProtocolName = protocolName;
    	try {
    		mWasConnectionAttempt = true;
    		mConnection = mHeadstart.openDeviceConnection(descriptor, protocolName, ss);
    	} catch(Exception e) {
    		throw new HeadstartOperationException(e);
    	}
	    AsyncTask<DeviceDescriptor, Void, Void> taskConnection = new ConnectToDeviceAsyncTask();
    	taskConnection.execute(descriptor);
    }

    /**
     * Disconnects from current remote device
     */
    public void disconnectFromDevice() {
    	if (null != mConnection) {
    		mConnection.disconnect();
    		notifyDisconneted();
	        sendConnectionStateChangedBroadcast(DeviceConnectionState.DISCONNECTED, mDevice);
    		mConnection = null;
	    	mDevice = null;
    	}
    }

    /**
     * Cancel connection process.
     */
    public void cancelConnectionProccess() {
    	if (null != mConnection) {
        	mConnection.disconnect();
	        sendConnectionStateChangedBroadcast(DeviceConnectionState.DISCONNECTED, mDevice);
	        notifyDisconneted();
    	}
    }


    /**
     * Starts XMLCommand SET_SYSTEM_TIMEOUT request
     * @param value The new system timeout value for the Card Reader.
     *              Max value: 2147483647/1000
     * */
    public void setCardReaderSystemTimeout(int value){
        doXMLCommandRequest(XMLCommandResult.SET_SYSTEM_TIMEOUT, Integer.toString(value), this);
    }

    /**
     * Starts XMLCommand SET_SCREEN_TIMEOUT request
     * @param value The new screen timeout value in seconds for the Card Reader.
     *              Max value: 2147483647/1000
     * */
    public void setCardReaderScreenTimeout(int value){
        doXMLCommandRequest(XMLCommandResult.SET_SCREEN_TIMEOUT, Integer.toString(value), this);
    }

    /**
     * Starts XMLCommand SET_SIGNATURE_TIMEOUT request
     * @param value The new signature timeout value in seconds for the Card Reader.
     *              Min value: 5
     *              Max value: 65535
     * */
    public void setSignatureTimeout(int value){
        doXMLCommandRequest(XMLCommandResult.SET_SIGNATURE_TIMEOUT, Integer.toString(value), this);
    }

    /**
     * Starts XMLCommand SET_OPERATING_MODE request
     * @param mode The new operation mode for the Card Reader.
     *             One of the following text strings:
     *             serial
     *             bt
     *             sbHost
     *             usbClient
     * */
    public void setCardReaderOperatingMode(String mode){
        doXMLCommandRequest(XMLCommandResult.SET_OPERATING_MODE, mode, this);
    }

    /**
     * Starts XMLCommand SET_LANGUAGE request
      * @param language The new display language for the Card Reader.
                 One of the following text strings:
                 en
                 is
                 default
     * */
    public void setCardReaderLanguage(String language){
        doXMLCommandRequest(XMLCommandResult.SET_LANGUAGE, language, this);
    }

    /**
     * Starts XMLCommand SET_BLUETOOTH_NAME request
     * @param name the new name to use as a Bluetooth device name
     * */
    public void setCardReaderBTName(String name){
        doXMLCommandRequest(XMLCommandResult.SET_BLUETOOTH_NAME, name, this);
    }

    /**
     * Starts XMLCommand GET_BLUETOOTH_NAME request
     * */
    public void getCardReaderBTName(){
        doXMLCommandRequest(XMLCommandResult.GET_BLUETOOTH_NAME, null, this);
    }

    /**
     * Starts XMLCommand GET_STATUS request
     * */
    public void getCardReaderStatus(){
        doXMLCommandRequest(XMLCommandResult.GET_STATUS, null, this);
    }

    /**
     * Starts INITIALISATION transaction
     */
    public void startFinancialInitialization() {
    	doOtherTransaction(FinancialTransactionResult.FT_TYPE_FINANCIAL_INITIALIZATION);
    }
    
    /**
     * Starts START OF DAY transaction
     * @deprecated As of release 2.1.0
     *      This method is obsolete and is ignored by the MPED (EFT client)
     */
    public void startStartOfDayOperation() {
    	doOtherTransaction(FinancialTransactionResult.FT_TYPE_START_OF_DAY);
    }
    
    /**
     * Starts END OF DAY transaction
     * @deprecated As of release 2.1.0
     *      This method is obsolete and is ignored by the MPED (EFT client)
     */
    public void startEndOfDayOperation(DeviceStateListener stateListener) {
    	doOtherTransaction(FinancialTransactionResult.FT_TYPE_END_OF_DAY);
    }

    /**
     * Starts SALE financial transaction.
     * @param amount - transaction amount in cents
     * @param currencyCode - currency code
     * @param cardHolderPresent - is card will be used in transaction
     */
    public void startSaleTransaction(
    		int amount, 
    		String currencyCode, 
    		boolean cardHolderPresent) 
    {
    	doFinancialTransaction(
    			FinancialTransactionResult.FT_TYPE_SALE, 
    			amount, 
    			currencyCode, 
    			cardHolderPresent,
                null,
                null,
    			null, 
    			this, 
    			this, 
    			this);
    }

    /**
     * Starts SALE financial transaction.
     * @param amount - transaction amount in cents
     * @param currencyCode - currency code
     * @param cardHolderPresent - is card will be used in transaction
     * @param customerReference - an optional reference id (max 25 characters) that can be associated
     *                            with the authorization. Example: "R45678135".
     */
    public void startSaleTransaction(
            int amount,
            String currencyCode,
            boolean cardHolderPresent,
            String customerReference)
    {
        doFinancialTransaction(
                FinancialTransactionResult.FT_TYPE_SALE,
                amount,
                currencyCode,
                cardHolderPresent,
                null,
                customerReference,
                null,
                this,
                this,
                this);
    }

    /**
     * Starts SALE financial transaction.
     * @param amount - transaction amount in cents
     * @param currencyCode - currency code
     * @param cardHolderPresent - is card will be used in transaction
     * @param customerReference - an optional reference id (max 25 characters) that can be associated
     *                            with the authorization. Example: "R45678135".
     * @param  divideByMonths  -  budget facility indicator. Decides how many months a payment can be divided into.
     *                            Is required for budget transactions.
     *                            Accepted values are:  03, 06, 12, 18, 24, 30, 36, 42, 48, 54, 60
     */
    public void startSaleTransaction(
            int amount,
            String currencyCode,
            boolean cardHolderPresent,
            String customerReference,
            String divideByMonths)
    {
        doFinancialTransaction(
                FinancialTransactionResult.FT_TYPE_SALE,
                amount,
                currencyCode,
                cardHolderPresent,
                null,
                customerReference,
                divideByMonths,
                this,
                this,
                this);
    }

    /**
     * Starts SALE_VOID financial transaction.
     * @param amount - transaction amount in cents
     * @param currencyCode - currency code
     * @param cardHolderPresent - is card will be used in transaction
     * @param originTransactionId - original transaction ID
     */
    public void startSaleVoidTransaction(
    		String originTransactionId,
    		int amount, 
    		String currencyCode, 
    		boolean cardHolderPresent) 
    {
    	doFinancialTransaction(
    			FinancialTransactionResult.FT_TYPE_SALE_VOID, 
    			amount, 
    			currencyCode, 
    			cardHolderPresent, 
    			originTransactionId,
                null,
                null,
    			this, 
    			this, 
    			this);
    }
    /**
     * Starts REFUND financial transaction.
     * @param amount - transaction amount in cents
     * @param currencyCode - currency code
     * @param cardHolderPresent - is card will be used in transaction
     */
    public void startRefundTransaction(
    		int amount, 
    		String currencyCode, 
    		boolean cardHolderPresent) 
    {
    	doFinancialTransaction(
    			FinancialTransactionResult.FT_TYPE_REFUND, 
    			amount, 
    			currencyCode, 
    			cardHolderPresent, 
    			null,
                null,
                null,
    			this, 
    			this, 
    			this);
    }

    /**
     * Starts REFUND financial transaction.
     * @param amount - transaction amount in cents
     * @param currencyCode - currency code
     * @param cardHolderPresent - is card will be used in transaction
     * @param customerReference - an optional reference id (max 25 characters) that can be associated
     *                            with the authorization. Example: "R45678135".
     */
    public void startRefundTransaction(
            int amount,
            String currencyCode,
            boolean cardHolderPresent,
            String customerReference)
    {
        doFinancialTransaction(
                FinancialTransactionResult.FT_TYPE_REFUND,
                amount,
                currencyCode,
                cardHolderPresent,
                null,
                customerReference,
                null,
                this,
                this,
                this);
    }

    /**
     * Starts REFUND_VOID financial transaction.
     * @param amount - transaction amount in cents
     * @param currencyCode - currency code
     * @param cardHolderPresent - is card will be used in transaction
     * @param originTransactionId - original transaction ID
     */
    public void startRefundVoidTransaction(
    		String originTransactionId,
    		int amount, 
    		String currencyCode, 
    		boolean cardHolderPresent) 
    {
    	doFinancialTransaction(
    			FinancialTransactionResult.FT_TYPE_REFUND_VOID, 
    			amount, 
    			currencyCode, 
    			cardHolderPresent, 
    			originTransactionId,
                null,
                null,
    			this, 
    			this, 
    			this);
    }
    
    /**
     * Cancel current financial transaction.
     * @throws HeadstartOperationException
     */
    public void cancelFinancialTransaction() throws HeadstartOperationException {
    	if (null != mConnection) {
    		logger.log(Level.FINE, "Canceling current Financial Transaction ...");
    		mConnection.cancelCurrentFinancialTransaction();
    	}    	
    }
    
    private void doOtherTransaction(int type) {
    	doFinancialTransaction(type, 0, null, false, null,null,null, this, TimeoutHandler.DEFAULT_TIMEOUT_HANDLER, SignatureVerifier.NULL_VERIFIER);
    }

    private void doFinancialTransaction(
    		int type, 
    		int amount, 
    		String currencyCode,
    		boolean cardHolderPresent,
    		String originTransactionId,
            String customerReference,
            String divideByMonths,
    		DeviceStateListener stateListener, 
    		TimeoutHandler timeoutHandler,
    		SignatureVerifier signatureVerifier) 
    {
    	if (isInTransaction()) {
    		return;
    	}
    	if (null != mConnection) {
    		FtDataHolder data = new FtDataHolder();
    		data.type = type;
    		data.amount = amount;
    		data.currencyCode = currencyCode;
    		data.cardHolderPresent = cardHolderPresent;
    		data.originTransactionId = originTransactionId;
            data.customerReference = customerReference;
            data.divideByMonths = divideByMonths;
    		data.stateListener = stateListener;
    		data.signatureVerifier = signatureVerifier;
    		data.timeoutHandler = timeoutHandler;
    		logger.log(Level.FINE, "Starting Financial Transaction: " + type);
    		FinancialTransactionAsyncTask ftAsyncTask = new FinancialTransactionAsyncTask();
    		ftAsyncTask.execute(data);
    	} else {
    		notifyDisconneted();
	        sendConnectionStateChangedBroadcast(DeviceConnectionState.DISCONNECTED, mDevice);
    		sendErrorBroadcast(getResources().getString(R.string.not_connected_to_ped));
    	}
    }
    
    /**
     * Sends broadcast with description
     *
     * @param message
     */
	private void sendErrorBroadcast(final String message) {
		sendErrorBroadcast(message, null);
	}
    /**
     * Sends broadcast with error code and description
     *
     * @param code
     * @param message
     */
	private void sendErrorBroadcast(final String message, Integer code) {
		logger.log(Level.FINE, "Sending error broadcast:" + message);
		Intent intent = new Intent(BROADCAST_ERROR);
		intent.putExtra(EXTRA_ERROR_DESCRIPTION, message);
		if (null != code) {
			intent.putExtra(EXTRA_ERROR_CODE, code.intValue());
		}
		sendBroadcast(intent);
	}
	
	private void sendCancelOperationBroadcast() {
		logger.log(Level.FINE, "Sending cancel operation broadcast:");
		Intent intent = new Intent(BROADCAST_OPERATION_CANCEL);
		sendBroadcast(intent);		
	}
    /**
     * Sends broadcast about state changing
     *
     * @param state
     */
    void sendConnectionStateChangedBroadcast(int state, DeviceDescriptor descriptor) {
    	logger.log(Level.FINE, "Sending connection state changed broadcast: " + descriptor + " going to " + state);
    	if (null != descriptor) {
	        Intent intent = new Intent(BROADCAST_CONNECTION_STATE_CHANGE);
	        intent.putExtra(EXTRA_PED_STATE, state);
	        intent.putExtra(EXTRA_REMOTE_DEVICE, new ParcelDeviceDescriptor(descriptor));
	        sendBroadcast(intent);
    	}
    }
    /**
     * Sends broadcast about transaction state changing
     * @param transactionState
     */
    void sendTransactionStateChangedBroadcast(DeviceState transactionState) {
    	if (null != transactionState) {
    		Intent intent = new Intent(BROADCAST_TRANSACTION_STATE_CHANGE);
    		intent.putExtra(EXTRA_TRANSACTION_STATE, new ParcelDeviceState(transactionState));
    		sendBroadcast(intent);
    	}
    }
        
    /**
     * Sends broadcast about finish of financial transaction
     * @param transactionResult
     */
	void sendTransactionFinishedBroadcast(FinancialTransactionResult transactionResult) {
		Intent intent = new Intent(BROADCAST_FINANCIAL_TRANSACTION_FINISHED);
		if (null != transactionResult) {
			intent.putExtra(EXTRA_FINANCIAL_RESULT, new ParcelFinancialTransactionResult(transactionResult));
		}
		sendBroadcast(intent);
	}

	void sendVerifySignatureBroadcast(String text) {
		Intent intent = new Intent(ACTION_VERIFY_SIGNATURE);
		if (null != text) {
			intent.putExtra(EXTRA_MERCHANT_RECEIPT_TEXT, text);
		}
		sendBroadcast(intent);
	}

    void sendVerifySignatureBroadcast(String receipt, String timeout) {
        Intent intent = new Intent(ACTION_VERIFY_SIGNATURE);
        if (null != receipt && null != timeout) {
            intent.putExtra(EXTRA_MERCHANT_RECEIPT_TEXT, receipt);
            intent.putExtra(EXTRA_SIGNATURE_TIMEOUT_SECONDS, timeout);
        }
        sendBroadcast(intent);
    }

	void sendHandleTimeoutBroadcast() {
		Intent intent = new Intent(ACTION_HANDLE_TIMEOUT);
		sendBroadcast(intent);
	}
	
    void sendDeviceFoundBroadcast(DeviceDescriptor descriptor) {
    	if (null != descriptor) {
	        Intent intent = new Intent(BROADCAST_DEVICE_FOUND);
	        intent.putExtra(EXTRA_REMOTE_DEVICE, new ParcelDeviceDescriptor(descriptor));
	        sendBroadcast(intent);
    	}
    }
    
    void sendDeviceDiscoveryFinishedBroadcast(Vector<DeviceDescriptor> descriptors) {
    	if (null != descriptors) {
	        Intent intent = new Intent(BROADCAST_DISCOVERY_FINISHED);
	        ParcelDeviceDescriptor[] tmp = new ParcelDeviceDescriptor[descriptors.size()];
	        int i = 0;
	        for (DeviceDescriptor descriptor : descriptors) {
				tmp[i++] = new ParcelDeviceDescriptor(descriptor);
			}
	        intent.putExtra(EXTRA_REMOTE_DEVICE, tmp);
	        sendBroadcast(intent);
    	}
    }

    /**
     * Returns current connected remote device.
     * @return DeviceDescriptor - descriptor of remote device. 
     */
    public DeviceDescriptor getConnectedDevice() {
    	return mDevice;
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}


    private void doXMLCommandRequest(int type, String parameter, XMLCommandListener listener){
        if (isInTransaction()) {
            return;
        }
        if (null != mConnection) {
            XMLDataHolder data = new XMLDataHolder();
            data.type = type;
            data.parameter = parameter;
            data.listener = listener;
            logger.log(Level.FINE, "Starting XML Command Request: " + type);
            XMLCommandAsyncTask xmlCmdAsyncTask = new XMLCommandAsyncTask();
            xmlCmdAsyncTask.execute(data);
        } else {
            notifyDisconneted();
            sendConnectionStateChangedBroadcast(DeviceConnectionState.DISCONNECTED, mDevice);
            sendErrorBroadcast(getResources().getString(R.string.not_connected_to_ped));
        }
    }

    private class XMLDataHolder {
        public int type;
        public String parameter;
        public XMLCommandListener listener;
    }

    private class XMLCommandAsyncTask extends AsyncTask<XMLDataHolder, Void, Void> {
        @Override
        protected Void doInBackground(XMLDataHolder... data) {
            try {
                mDeviceState = new DeviceState(ADDITIONAL_DEVICE_STATE_STARTING, getResources().getString(R.string.xml_cmd_starting), true);
                setLastXMLCommandRequestHandled();
                switch (data[0].type) {
                    case XMLCommandResult.GET_STATUS:
                        mConnection.getStatus(data[0].listener);
                        break;
                    case XMLCommandResult.GET_BLUETOOTH_NAME:
                        mConnection.getBluetoothName(data[0].listener);
                        break;
                    case XMLCommandResult.SET_BLUETOOTH_NAME:
                        mConnection.setBluetoothName(data[0].parameter, data[0].listener);
                        break;
                    case XMLCommandResult.SET_LANGUAGE:
                        mConnection.setLanguage(data[0].parameter, data[0].listener);
                        break;
                    case XMLCommandResult.SET_OPERATING_MODE:
                        mConnection.setOperatingMode(data[0].parameter, data[0].listener);
                        break;
                    case XMLCommandResult.SET_SCREEN_TIMEOUT:
                        mConnection.setScreenTimeout(data[0].parameter, data[0].listener);
                        break;
                    case XMLCommandResult.SET_SIGNATURE_TIMEOUT:
                        mConnection.setSignatureTimeout(data[0].parameter, data[0].listener);
                        break;
                    case XMLCommandResult.SET_SYSTEM_TIMEOUT:
                        mConnection.setSystemTimeout(data[0].parameter, data[0].listener);
                        break;
                }
            } catch (HeadstartOperationException e) {
                logger.log(Level.SEVERE, "XML Command request failed", e);
                if (e instanceof DeviceConnectionException) {
                    notifyDisconneted();
                    sendConnectionStateChangedBroadcast(DeviceConnectionState.DISCONNECTED, mDevice);
                    mConnection = null;
                    mDevice = null;
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error XML Command request (other)", e);
            } finally {
                mDeviceState = new DeviceState(ADDITIONAL_DEVICE_STATE_FINISHED, getResources().getString(R.string.xml_cmd_completed), true);
                sendTransactionStateChangedBroadcast(mDeviceState);
                mDeviceState = null;
            }
            return null;
        }

    }

    /**
     * Save last XML Command request values to preferences
     * @param result
     */
    void saveXMLCommandResult(XMLCommandResult result) {
        Editor editor = mPreferences.edit();
        editor.putInt(PREFERENCE_LAST_XML_CMD_TYPE , result.type);
        editor.putInt(PREFERENCE_LAST_XML_CMD_BATTERY_MV , result.batteryMv);
        editor.putInt(PREFERENCE_LAST_XML_CMD_BATTERY_STATUS , result.batteryStatus);
        editor.putBoolean(PREFERENCE_LAST_XML_CMD_BATTERY_CHARGING, result.batteryCharging);
        editor.putBoolean(PREFERENCE_LAST_XML_CMD_EXTERNAL_POWER, result.externalPower);
        editor.putString(PREFERENCE_LAST_XML_CMD_TYPE, result.serialNumber);
        editor.putString(PREFERENCE_LAST_XML_CMD_STATUS_MESSAGE, result.statusMessage);

        if(null != result.btInfo){
            if (null != result.btInfo.bTReconnectRequired) {
                editor.putBoolean(PREFERENCE_LAST_XML_CMD_RECONNECTION_REQUIRED , result.btInfo.bTReconnectRequired);
            }
            if (null != result.btInfo.bluetoothName) {
                editor.putString(PREFERENCE_LAST_XML_CMD_BT_NAME, result.btInfo.bluetoothName);
            }
            if (null != result.btInfo.oldBluetoothName) {
                editor.putString(PREFERENCE_LAST_XML_CMD_OLD_BT_NAME, result.btInfo.oldBluetoothName);
            }
        }

        editor.commit();
    }

    public XMLCommandResult getLastXMLResults() {
        int type = Integer.parseInt(mPreferences.getString(HeadstartService.PREFERENCE_LAST_XML_CMD_TYPE, "-1"));
        if (type == -1) {
            return null;
        }

        BluetoothInformation btInfo = new BluetoothInformation();
        btInfo.bTReconnectRequired =  mPreferences.getBoolean(PREFERENCE_LAST_XML_CMD_RECONNECTION_REQUIRED, false);
        btInfo.bluetoothName = mPreferences.getString(PREFERENCE_LAST_XML_CMD_BT_NAME, null);
        btInfo.oldBluetoothName = mPreferences.getString(PREFERENCE_LAST_XML_CMD_OLD_BT_NAME  , null);

        XMLCommandResult result =
                new XMLCommandResult(
                        type,
                        btInfo,
                        mPreferences.getString(PREFERENCE_LAST_XML_CMD_STATUS_MESSAGE , ""),
                        mPreferences.getString(PREFERENCE_LAST_XML_CMD_SERIAL_NUMBER , ""),
                        mPreferences.getInt(PREFERENCE_LAST_XML_CMD_BATTERY_STATUS, 0),
                        mPreferences.getInt(PREFERENCE_LAST_XML_CMD_BATTERY_MV , 0),
                        mPreferences.getBoolean(PREFERENCE_LAST_XML_CMD_BATTERY_CHARGING , false),
                        mPreferences.getBoolean(PREFERENCE_LAST_XML_CMD_EXTERNAL_POWER ,false)
                );
        return result;
    }

    public void setLastXMLCommandRequestHandled() {
        mPath = null;
        mError = null;
        int type = Integer.parseInt(mPreferences.getString(HeadstartService.PREFERENCE_LAST_XML_CMD_TYPE, "-1"));
        if (type == -1) {
            return;
        }
        Editor editor = mPreferences.edit();
        editor.remove(PREFERENCE_LAST_XML_CMD_TYPE);
        editor.remove(PREFERENCE_LAST_XML_CMD_SERIAL_NUMBER);
        editor.remove(PREFERENCE_LAST_XML_CMD_STATUS_MESSAGE);
        editor.remove(PREFERENCE_LAST_XML_CMD_BATTERY_STATUS);
        editor.remove(PREFERENCE_LAST_XML_CMD_BATTERY_MV);
        editor.remove(PREFERENCE_LAST_XML_CMD_BATTERY_CHARGING);
        editor.remove(PREFERENCE_LAST_XML_CMD_EXTERNAL_POWER);
        editor.remove(PREFERENCE_LAST_XML_CMD_OLD_BT_NAME);
        editor.remove(PREFERENCE_LAST_XML_CMD_BT_NAME);
        editor.remove(PREFERENCE_LAST_XML_CMD_RECONNECTION_REQUIRED );
        editor.commit();
    }

    @Override
    public void onXMLCommandCompleted(HeadstartDeviceConnection activeConnection, XMLCommandResult result) {
        logger.log(Level.FINE, "XML Command request completed");
        saveXMLCommandResult(result);
        sendXMLCommandRequestFinishedBroadcast(result);
    }

    @Override
    public void onXMLCommandError(HeadstartDeviceConnection activeConnection, Throwable error) {
        logger.log(Level.FINE, "XML Command request error:" + error);
        mError = error;
        if (error instanceof DeviceException) {
            sendErrorBroadcast(error.getMessage(), ((DeviceException)error).code);
        } else {
            sendErrorBroadcast(error.getMessage());
        }
    }


    /**
     * Returns status of handling of last xml command request
     * @return true - if last xml command request was handled
     */
    public boolean isLastXMLRequestHandled() {
        return null == getLastXMLResults() && null == mError;
    }

    /**
     * Sends broadcast about finish of XML Command Request
     * @param result
     */
    void sendXMLCommandRequestFinishedBroadcast(XMLCommandResult result) {
        Intent intent = new Intent(BROADCAST_XML_COMMAND_REQUEST_FINISHED);
        if (null != result) {
            intent.putExtra(EXTRA_XML_COMMAND_RESULT, new ParcelXmlCommandResult(result));
        }
        sendBroadcast(intent);
    }


	private class FtDataHolder {
		public int type;
		public int amount;
		public String currencyCode;
		public boolean cardHolderPresent;
        public String customerReference;
        public String divideByMonths;
		public DeviceStateListener stateListener;
		public SignatureVerifier signatureVerifier;
		public TimeoutHandler timeoutHandler;
		public String originTransactionId;
	}
	
	private class FinancialTransactionAsyncTask extends AsyncTask<FtDataHolder, Void, Void> {

		@Override
		protected Void doInBackground(FtDataHolder... data) {
    		try {
    			mInTransaction = true;
    			mDeviceState = new DeviceState(ADDITIONAL_DEVICE_STATE_STARTING, getResources().getString(R.string.ft_starting), true);
    			setLastTransactionHandled();
    			switch (data[0].type) {
    			case FinancialTransactionResult.FT_TYPE_FINANCIAL_INITIALIZATION:
    				mConnection.financialInitialization(data[0].stateListener);
        			setLastTransactionHandled();
    				break;
    			case FinancialTransactionResult.FT_TYPE_START_OF_DAY:
    				mConnection.startDay(data[0].stateListener);
    				break;
    			case FinancialTransactionResult.FT_TYPE_END_OF_DAY:
    				mConnection.endDay(data[0].stateListener);
    				break;
				case FinancialTransactionResult.FT_TYPE_SALE:
					mConnection.sale(
							data[0].amount, 
							data[0].currencyCode, 
							data[0].cardHolderPresent,
                            data[0].customerReference,
                            data[0].divideByMonths,
							data[0].stateListener, 
							data[0].signatureVerifier,
							data[0].timeoutHandler);					
					break;
				case FinancialTransactionResult.FT_TYPE_REFUND:
					mConnection.refund(
							data[0].amount, 
							data[0].currencyCode, 
							data[0].cardHolderPresent,
                            data[0].customerReference,
							data[0].stateListener, 
							data[0].signatureVerifier,
							data[0].timeoutHandler);					
					break;
				case FinancialTransactionResult.FT_TYPE_SALE_VOID:
					mConnection.saleVoid(
							data[0].originTransactionId,
							data[0].amount, 
							data[0].currencyCode, 
							data[0].cardHolderPresent, 
							data[0].stateListener, 
							data[0].signatureVerifier,
							data[0].timeoutHandler);
					break;
				case FinancialTransactionResult.FT_TYPE_REFUND_VOID:
					mConnection.refundVoid(
							data[0].originTransactionId,
							data[0].amount, 
							data[0].currencyCode, 
							data[0].cardHolderPresent, 
							data[0].stateListener, 
							data[0].signatureVerifier,
							data[0].timeoutHandler);
					break;
				}
			} catch (HeadstartOperationException e) {
				logger.log(Level.SEVERE, "Financial transaction failed", e);
				if (e instanceof DeviceConnectionException) {
					notifyDisconneted();
					sendConnectionStateChangedBroadcast(DeviceConnectionState.DISCONNECTED, mDevice);
					mConnection = null;
					mDevice = null;
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error financial transaction (other)", e);
			} finally {
				mInTransaction = false;
				if (null != mConnection) {
					mConnection.downloadDeviceLog(HeadstartService.this);
				}
                mDeviceState = null;
			}
			return null;
		}
		
	}

    /**
	 * Save last financial transaction to preferences
	 * @param result
	 */
	void saveFinancialTransaction(FinancialTransactionResult result) {
		Editor editor = mPreferences.edit();
		editor.putInt(PREFERENCE_LAST_FT_TYPE, result.type);
		editor.putInt(PREFERENCE_LAST_FT_OPERATION_STATUS, result.operationStatus);
		editor.putInt(PREFERENCE_LAST_FT_TRANSACTION_STATUS, result.transactionStatus);
		editor.putInt(PREFERENCE_LAST_FT_AMOUNT, result.authorizedAmount);
		editor.putString(PREFERENCE_LAST_FT_TRANSACTION_ID, result.transactionId);
		editor.putString(PREFERENCE_LAST_FT_MERCHANT_RECEIPT, result.merchantReceipt);
		editor.putString(PREFERENCE_LAST_FT_CUSTOMER_RECEIPT, result.customerReceipt);
		editor.putString(PREFERENCE_LAST_FT_CURRENCY, result.currency);
		editor.putString(PREFERENCE_LAST_FT_STATUS_MESSAGE, result.statusMessage);
		editor.putString(PREFERENCE_LAST_FT_TRANSACTION_TYPE, result.transactionType);
		editor.putString(PREFERENCE_LAST_FT_FINANCIAL_STATUS, result.financialStatus);

        if (null != result.requestAmount) {
			editor.putInt(PREFERENCE_LAST_FT_REQUESTED_AMOUNT, result.requestAmount);
		}
		if (null != result.gratuityAmount) {
			editor.putInt(PREFERENCE_LAST_FT_GRATUITY_AMOUNT, result.gratuityAmount);
		}
		if (null != result.gratuityPercentage) {
			editor.putInt(PREFERENCE_LAST_FT_GRATUITY_PERCENTAGE, result.gratuityPercentage);
		}
		if (null != result.totalAmount) {
			editor.putInt(PREFERENCE_LAST_FT_TOTAL_AMOUNT, result.totalAmount);
		}
		editor.putString(PREFERENCE_LAST_FT_EFT_TRANSACTION_ID, result.eftTransactionId);
		editor.putString(PREFERENCE_LAST_FT_EFT_TIMESTAMP, result.eftTimestamp);
		editor.putString(PREFERENCE_LAST_FT_AUTHORISATION_CODE, result.authorisationCode);
		editor.putString(PREFERENCE_LAST_FT_CVM, result.cvm);
		editor.putString(PREFERENCE_LAST_FT_CARD_ENTRY_TYPE, result.cardEntryType);
		editor.putString(PREFERENCE_LAST_FT_CARD_SCHEME_NAME, result.cardSchemeName);
		editor.putString(PREFERENCE_LAST_FT_ERROR_MESSAGE, result.errorMessage);
        if(null != result.customerReference){
            editor.putString(PREFERENCE_LAST_FT_CUSTOMER_REFERENCE, result.customerReference);
        }
        if(null != result.budgetNumber){
            editor.putString(PREFERENCE_LAST_FT_DIVIDE_BY_MONTHS, result.budgetNumber);
        }
		editor.commit();
	}
	
	public FinancialTransactionResult getLastTransaction() {
		int type = mPreferences.getInt(HeadstartService.PREFERENCE_LAST_FT_TYPE, -1);
		if (type == -1) {
			return null;
		}

		FinancialTransactionResult result =
                    new FinancialTransactionResult(
                            mPreferences.getInt(PREFERENCE_LAST_FT_OPERATION_STATUS, 0),
                            mPreferences.getInt(PREFERENCE_LAST_FT_TRANSACTION_STATUS, 0),
                            mPreferences.getInt(PREFERENCE_LAST_FT_AMOUNT, 0),
                            mPreferences.getString(PREFERENCE_LAST_FT_TRANSACTION_ID, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_MERCHANT_RECEIPT, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_CUSTOMER_RECEIPT, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_STATUS_MESSAGE, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_TRANSACTION_TYPE, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_FINANCIAL_STATUS, ""),
                            mPreferences.getInt(PREFERENCE_LAST_FT_REQUESTED_AMOUNT, 0),
                            mPreferences.getInt(PREFERENCE_LAST_FT_GRATUITY_AMOUNT, 0),
                            mPreferences.getInt(PREFERENCE_LAST_FT_GRATUITY_PERCENTAGE, 0),
                            mPreferences.getInt(PREFERENCE_LAST_FT_TOTAL_AMOUNT, 0),
                            mPreferences.getString(PREFERENCE_LAST_FT_CURRENCY, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_EFT_TRANSACTION_ID, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_EFT_TIMESTAMP, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_AUTHORISATION_CODE, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_CVM, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_CARD_ENTRY_TYPE, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_CARD_SCHEME_NAME, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_ERROR_MESSAGE, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_CUSTOMER_REFERENCE, ""),
                            mPreferences.getString(PREFERENCE_LAST_FT_DIVIDE_BY_MONTHS, "")
                            );
            result.type = type;

        return result;
	}

	public void setLastTransactionHandled() {
		mPath = null;
		mError = null;
		int type = mPreferences.getInt(HeadstartService.PREFERENCE_LAST_FT_TYPE, -1);
		if (type == -1) {
			return;
		}
		Editor editor = mPreferences.edit();
		editor.remove(PREFERENCE_LAST_FT_TYPE);
		editor.remove(PREFERENCE_LAST_FT_OPERATION_STATUS);
		editor.remove(PREFERENCE_LAST_FT_TRANSACTION_STATUS); 
		editor.remove(PREFERENCE_LAST_FT_AMOUNT);
		editor.remove(PREFERENCE_LAST_FT_TRANSACTION_ID);
		editor.remove(PREFERENCE_LAST_FT_MERCHANT_RECEIPT);
		editor.remove(PREFERENCE_LAST_FT_CUSTOMER_RECEIPT);
		editor.remove(PREFERENCE_LAST_FT_STATUS_MESSAGE);
		editor.remove(PREFERENCE_LAST_FT_TRANSACTION_TYPE);
		editor.remove(PREFERENCE_LAST_FT_FINANCIAL_STATUS);
		editor.remove(PREFERENCE_LAST_FT_REQUESTED_AMOUNT);
		editor.remove(PREFERENCE_LAST_FT_GRATUITY_AMOUNT);
		editor.remove(PREFERENCE_LAST_FT_GRATUITY_PERCENTAGE);
		editor.remove(PREFERENCE_LAST_FT_TOTAL_AMOUNT);
		editor.remove(PREFERENCE_LAST_FT_CURRENCY);
		editor.remove(PREFERENCE_LAST_FT_EFT_TRANSACTION_ID);
		editor.remove(PREFERENCE_LAST_FT_EFT_TIMESTAMP);
		editor.remove(PREFERENCE_LAST_FT_AUTHORISATION_CODE);
		editor.remove(PREFERENCE_LAST_FT_CVM);
		editor.remove(PREFERENCE_LAST_FT_CARD_ENTRY_TYPE);
		editor.remove(PREFERENCE_LAST_FT_CARD_SCHEME_NAME);
		editor.remove(PREFERENCE_LAST_FT_ERROR_MESSAGE);
        editor.remove(PREFERENCE_LAST_FT_CUSTOMER_REFERENCE);
        editor.remove(PREFERENCE_LAST_FT_DIVIDE_BY_MONTHS);
		editor.remove(PREFERENCE_MERCHANT_SIGNATURE_TEXT);
        editor.remove(PREFERENCE_SIGNATURE_TIMEOUT_SECONDS);
		editor.commit();
	}

	/**
	 * Returns error occurred at last financial transaction operation
	 * @return error if it occurred during last financial transaction or NULL otherwise
	 */
	public Throwable getLastTransactionError() {
		return mError;
	}


    /**
     * Persists signature timeout received on signature verification step
     * @param timeout
     */
    void saveSignatureTimeoutSeconds(String timeout) {
        mPreferences.edit().putString(PREFERENCE_SIGNATURE_TIMEOUT_SECONDS, timeout).commit();
    }
    /**
     * Returns text received from remote device for signature verification
     * @return
     */
    public String getSignatureTimeoutSeconds() {
        return mPreferences.getString(PREFERENCE_SIGNATURE_TIMEOUT_SECONDS, null);
    }

	/**
	 * Persists merchant text received on signature verification step
	 * @param merchantText
	 */
	void saveSignatureVerificationText(String merchantText) {
		mPreferences.edit().putString(PREFERENCE_MERCHANT_SIGNATURE_TEXT, merchantText).commit();
	}
	/**
	 * Returns text received from remote device for signature verification
	 * @return
	 */
	public String getSignatureVerificationText() {
		return mPreferences.getString(PREFERENCE_MERCHANT_SIGNATURE_TEXT, null);
	}

	public String getPath() {
		return mPath;
	}

	public void setPath(String mPath) {
		this.mPath = mPath;
	}

	private class ConnectToDeviceAsyncTask extends AsyncTask<DeviceDescriptor, Void, Void> {

		@Override
		protected Void doInBackground(DeviceDescriptor... params) {
			try {
				notifyConnecting();
				mConnection.connect();
				notifyConnected();
				sendConnectionStateChangedBroadcast(mConnection.getState(), params[0]);
			    mPreferences                                                                                                                                                                                         
				    .edit()                                                                                                                                                                                      
				    .putString(PREFERENCE_LAST_USED_ADDRESS, params[0].getAttribute("Address"))
				    .putString(PREFERENCE_LAST_USED_NAME, params[0].getName())
				    .putString(PREFERENCE_LAST_USED_TYPE, params[0].getType())
			    .commit();                                                                                                                                                                                   
			} catch (HeadstartOperationException e) {
				logger.log(Level.SEVERE, "Error connection to device", e);
				notifyDisconneted();
				sendConnectionStateChangedBroadcast(DeviceConnectionState.DISCONNECTED, params[0]);
				sendErrorBroadcast(e.getMessage());
				mConnection = null;
				mDevice = null;
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error connection to device (other)", e);
				notifyDisconneted();
			}
			return null;
		}		
	}
	
	private class DiscoveryDevicesHolder {
		
		public DiscoveryDevicesHolder(HeadstartDeviceDiscovery hdd,
				DeviceDiscoveryListener discoveryListener, boolean force) {
			super();
			this.hdd = hdd;
			this.discoveryListener = discoveryListener;
			this.force = force;
		}
		public HeadstartDeviceDiscovery hdd;
		public DeviceDiscoveryListener discoveryListener;
		public boolean force;
	}

    /**
     * Returns Serial DeviceDescriptor with default connection preferences for MPED-400
     * @retuns DeviceDescriptor
     */
    public DeviceDescriptor getSerialDevice(){
        DeviceDescriptor device = new DeviceDescriptor(DeviceDescriptor.DEVICE_TYPE_COM, Integer.toString(SerialCom.SERIAL_TYPE_COM1));
        device.setAttribute(PREFERENCE_COM_PORT, Integer.toString(SerialCom.SERIAL_TYPE_COM1));
        device.setAttribute(PREFERENCE_BAUD_RATE, Integer.toString(SerialCom.SERIAL_BOUDRATE_115000));
        device.setAttribute(PREFERENCE_NUMBER_OF_BITS, Integer.toString(SerialCom.SERIAL_BITLEN_8));
        device.setAttribute(PREFERENCE_PARITY, Integer.toString(SerialCom.SERIAL_PARITY_NON));
        device.setAttribute(PREFERENCE_STOP_BITS, Integer.toString(SerialCom.SERIAL_STOP_2));
        device.setAttribute(PREFERENCE_FLOW_CONTROL, Integer.toString(SerialCom.SERIAL_FLOW_NON));
        device.setAttribute(PREFERENCE_HANDSHAKE, Integer.toString(SerialCom.SERIAL_IO_NON));
        setSerialDevicePreferences();
        return device;
    }

    private void setSerialDevicePreferences(){
        Editor editor = mPreferences.edit();
        editor.putString(PREFERENCE_COM_PORT, Integer.toString(SerialCom.SERIAL_TYPE_COM1));
        editor.putString(PREFERENCE_BAUD_RATE, Integer.toString(SerialCom.SERIAL_BOUDRATE_115000));
        editor.putString(PREFERENCE_NUMBER_OF_BITS, Integer.toString(SerialCom.SERIAL_BITLEN_8));
        editor.putString(PREFERENCE_PARITY, Integer.toString(SerialCom.SERIAL_PARITY_NON));
        editor.putString(PREFERENCE_STOP_BITS, Integer.toString(SerialCom.SERIAL_STOP_2));
        editor.putString(PREFERENCE_HANDSHAKE, Integer.toString(SerialCom.SERIAL_IO_NON));
        editor.putString(PREFERENCE_FLOW_CONTROL, Integer.toString(SerialCom.SERIAL_FLOW_NON));
        editor.commit();
    }

	private class DiscoveryDevicesAsyncTask extends AsyncTask<DiscoveryDevicesHolder, Void, Void> {

		@Override
		protected Void doInBackground(DiscoveryDevicesHolder... holder) {
			try {
				holder[0].hdd.discoverDevices(holder[0].discoveryListener, holder[0].force);
			} catch (HeadstartOperationException e) {
				logger.log(Level.SEVERE, "Error on device discovery", e);
				sendErrorBroadcast(e.getMessage());
			}
			return null;
		}
		
	}

	private void notifyDisconneted() {
        NotificationMgr.getInstance().notifyStatusChanged(
        		null, 
        		ACTION_MAIN, 
        		getResources().getString(R.string.disconnected), 
        		R.drawable.status_disconnected, 
        		null);
	}
	
	private void notifyConnecting() {
        NotificationMgr.getInstance().notifyStatusChanged(
        		null, 
        		ACTION_MAIN, 
        		getResources().getString(R.string.connecting), 
        		R.drawable.status_disconnected, 
        		null);
	}
	
	private void notifyConnected() {
        NotificationMgr.getInstance().notifyStatusChanged(
        		null, 
        		ACTION_MAIN, 
        		getResources().getString(R.string.connected), 
        		R.drawable.status_connected, 
        		null);
	}
	
	// Listeners section
	@Override
	public void onDownloadProgress(int status) {
		switch (status) {
		case DeviceLogDownloadListener.STATE_START:
            mDownloadingLogs = true;
			String tmp = getResources().getString(R.string.logs_download);
	        NotificationMgr.getInstance().notifyStatusChanged(null, ACTION_MAIN, tmp, R.drawable.status_connected, null);
			mDeviceState = new DeviceState(ADDITIONAL_DEVICE_STATE_DOWNLOADING_LOGS, tmp, false);
            sendTransactionStateChangedBroadcast(mDeviceState);
			break;
        case DeviceLogDownloadListener.STATE_FINISH:
            String fin = getResources().getString(R.string.logs_download_finish);
            mDeviceState = new DeviceState(ADDITIONAL_DEVICE_STATE_DOWNLOADING_LOGS_FINISHED, fin, false);
            mDownloadingLogs = false;
            sendTransactionStateChangedBroadcast(mDeviceState);
            break;

		default:
			if (null != mConnection) {
				notifyConnected();
			} else {
				notifyDisconneted();
			}
			break;
		}
	}

	@Override
	public void onDeviceFound(HeadstartDeviceDiscovery discoveryEngine, DeviceDescriptor deviceDescriptor) {
		sendDeviceFoundBroadcast(deviceDescriptor);
	}

	@Override
	public void onDiscoveryCompleted(HeadstartDeviceDiscovery discoveryEngine, Vector deviceDescriptors) {
		sendDeviceDiscoveryFinishedBroadcast(deviceDescriptors);
	}

	/**
	 * Accepts signature verification
	 */
	public void signatureAccept() {
		signatureAccept(null);
	}

	public void signatureAccept(String signaturePath) {
		mPath = signaturePath;
		mSignatureOk = true;
		if (null != mConnection) {
			synchronized (mConnection) {
				mConnection.notify();
			}
		}
	}
	
	/**
	 * Declines signature verification
	 */
	public void signatureDecline() {
		mSignatureOk = false;
		if (null != mConnection) {
			synchronized (mConnection) {
				mConnection.notify();
			}
		}
	}

	/**
	 * Callback method for handling signature verification.
	 * Calls external handler (if set) and blocks until
     * accept ( {@link #signatureAccept()} ) or
     * decline ( {@link #signatureDecline()} ) methods will be called.
	 * Also set verification signature state to notification bar with {@link #ACTION_VERIFY_SIGNATURE} action for notification intent.
	 */
	@Override
	public void verifySignature(HeadstartDeviceConnection currentConnection,
			String merchantReceiptData) throws SignatureVerificationException {
		mSignatureOk = false;
		mMerchantText = merchantReceiptData;
		saveSignatureVerificationText(mMerchantText);
		Bundle b = new Bundle();
		b.putString(EXTRA_MERCHANT_RECEIPT_TEXT, merchantReceiptData);
		NotificationMgr.getInstance().notifyStatusChanged(
				null, 
				ACTION_MAIN, 
				getResources().getString(R.string.sv_notification_text), 
				R.drawable.status_connected,
				b);
		sendVerifySignatureBroadcast(merchantReceiptData);
		synchronized (mConnection) {
			try {
				mConnection.wait();
			} catch (InterruptedException e) {
				throw new SignatureVerificationException();
			}
		}
		mDeviceState = new DeviceState(ADDITIONAL_DEVICE_STATE_SIGNATURE_VERIFIED, getResources().getString(R.string.sv_signature_verified), false);
		mMerchantText = null;
		if (!mSignatureOk) {
			throw new SignatureVerificationException();
		}
	}

    /**
     * Callback method for handling signature verification.
     * Calls external handler (if set) with a timeout value and blocks until
     * accept ( {@link #signatureAccept()} ) or
     * decline ( {@link #signatureDecline()} ) methods will be called.
     * Also set verification signature state to notification bar with {@link #ACTION_VERIFY_SIGNATURE} action for notification intent.
     */
    @Override
    public void verifySignature(HeadstartDeviceConnection currentConnection,
                                String merchantReceiptData, int timeout) throws SignatureVerificationException {
        mSignatureOk = false;
        mMerchantText = merchantReceiptData;
        String seconds = Integer.toString(timeout);

        saveSignatureTimeoutSeconds(seconds);
        saveSignatureVerificationText(mMerchantText);

        Bundle b = new Bundle();
        b.putString(EXTRA_MERCHANT_RECEIPT_TEXT, merchantReceiptData);
        b.putString(EXTRA_SIGNATURE_TIMEOUT_SECONDS, seconds);

        NotificationMgr.getInstance().notifyStatusChanged(
                null,
                ACTION_MAIN,
                getResources().getString(R.string.sv_notification_text),
                R.drawable.status_connected,
                b);
        sendVerifySignatureBroadcast(merchantReceiptData,seconds);

        synchronized (mConnection) {
            try {
                mConnection.wait();
            } catch (InterruptedException e) {
                throw new SignatureVerificationException();
            }
        }

        mDeviceState = new DeviceState(ADDITIONAL_DEVICE_STATE_SIGNATURE_VERIFIED, getResources().getString(R.string.sv_signature_verified), false);
        mMerchantText = null;
        if (!mSignatureOk) {
            throw new SignatureVerificationException();
        }
    }

    /**
	 * Unblock method for {@link #handleTimeout(TimeoutException)} 
	 * @param waitMore - if true action caused timeout will be repeated, otherwise action will be interrupted with exception
	 */
	public void timeoutHandleResult(boolean waitMore) {
		continueWait = waitMore;
		if (null != mConnection) {
			synchronized (mConnection) {
				mConnection.notify();
			}
		}
	}
	
	private boolean continueWait;
	/**
	 * Callback method for handling timeout with remote device.
	 * If external handler was set delegates control to it. If external handler was not set,
	 * puts to notification bar notification intent with {@link #ACTION_HANDLE_TIMEOUT} action and blocks 
	 * until ( {@link #timeoutHandleResult(boolean)} ) methods will be called.
	 */
	@Override
	public boolean handleTimeout(TimeoutException e) {
		sendHandleTimeoutBroadcast();
		String statusMessage = getResources().getString(R.string.th_waiting_timeout_handling);
		DeviceState backup = mDeviceState;
		mDeviceState = new DeviceState(ADDITIONAL_DEVICE_STATE_HANDLING_TIMEOUT, statusMessage, false);
		NotificationMgr.getInstance().notifyStatusChanged(
				null, 
				ACTION_MAIN, 
				statusMessage, 
				R.drawable.status_connected,
				null);
		synchronized (mConnection) {
			try {
				mConnection.wait();
			} catch (InterruptedException ignore) {
			}
		}
		mDeviceState = backup;
		NotificationMgr.getInstance().notifyStatusChanged(
				null, 
				ACTION_MAIN, 
				mDeviceState.getStatusMessage(), 
				R.drawable.status_connected,
				null);
		logger.log(Level.FINE, "Timeout handled: " + (continueWait ? "waiting more time" : "cancelling connection"));
		return continueWait;
	}

	@Override
	public void onDeviceState(HeadstartDeviceConnection activeConnection, DeviceState deviceState) {
		mDeviceState = deviceState;
		logger.log(Level.FINE, "Financial Transaction state changed: " + deviceState.getState() + "=" + deviceState.getStatusMessage());
		NotificationMgr.getInstance().notifyStatusChanged(
				null, 
				ACTION_MAIN, 
				deviceState.getStatusMessage(), 
				R.drawable.status_connected,
				null);
		sendTransactionStateChangedBroadcast(deviceState);		
	}

	@Override
	public void onTransactionCompleted(HeadstartDeviceConnection activeConnection, FinancialTransactionResult result) {
		logger.log(Level.FINE, "Financial Transaction completed");
		saveFinancialTransaction(result);
		sendTransactionFinishedBroadcast(result);
	}

	@Override
	public void onTransactionError(HeadstartDeviceConnection activeConnection, Throwable error) {
		logger.log(Level.FINE, "Financial Transaction error:" + error);
		if (error instanceof FinancialTransactionCancelledException) {
			sendCancelOperationBroadcast();
			return;
		}
		mError = error;
		if (error instanceof DeviceException) {
			sendErrorBroadcast(error.getMessage(), ((DeviceException)error).code);
		} else {
			sendErrorBroadcast(error.getMessage());
		}
	}
	
	public static synchronized String getProperty(String name) {
		if (null == mProperties) {
			return null;
		}
		return mProperties.getProperty(name);
	}
	
	public static synchronized void setProperty(String name, String value) {
		if (null != mProperties) {
			mProperties.setProperty(name, value);
		}		
	}
	
	public static synchronized void removeProperty(String name) {
		if (null != mProperties) {
			mProperties.remove(name);
		}
	}
}
