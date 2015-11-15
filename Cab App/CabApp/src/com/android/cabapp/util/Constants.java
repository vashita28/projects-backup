package com.android.cabapp.util;

import android.content.Context;

public class Constants {

	public static Context context;
	public static boolean isDebug = true;

	public static final String szDESSecretKey = "ef4c346814edc0cdb67424a5";
	public static final String CAB_APP_DRIVE_PREFERENCES = "cabappdrivepref";

	public static final String UADevlopmentAppKey = "yv65eWSETC6lTF2U5YY2iA";
	public static final String UADevelopmentAppSecret = "viuN0YtOQQ6srJjpJu7GxQ";

	public static final String LocationKey = "AIzaSyAqYFxz9LpiWokwyD2tRbEtFEzgUb2nGcI";

	// public static final String driverURL =
	// "http://192.168.0.10:8081/ws/v2/driver/";
	// public static final String accessToken =
	// "877B1BC6DFE1D0003B56BEDF98FFF852F1A7FC58";

	// 54.76.145.113
	// public static final String driverURL =
	// "http://cabapp.pocketapp.co.uk/ws/v2/driver/";
	// public static final String driverURL =
	// "http://cabapp.pocketapp.co.uk/ws/v2/driver/";

	public static final String driverURL = "http://dev.api.cabapp.pocketapp.co.uk/ws/v2/driver/";
	public static final String passengerURL = "http://dev.api.cabapp.pocketapp.co.uk/ws/v2/passenger/";
	public static final String cityURL = "http://dev.api.cabapp.pocketapp.co.uk/ws/v2/city/list";
	public static final String promotionURL = "http://dev.api.cabapp.pocketapp.co.uk/promotion?accessToken="
			+ Util.getAccessToken(context);

	public static final String sectorURL = "http://dev.api.cabapp.pocketapp.co.uk/ws/v2/city/sectorslist?cityId=1";

	// http://54.154.107.136/
	// public static final String driverURL =
	// "http://54.154.107.136//ws/v2/driver/";
	// public static final String passengerURL =
	// "http://54.154.107.136//ws/v2/passenger/";
	// public static final String promotionURL =
	// "http://54.154.107.136//promotion";
	// public static final String sectorURL =
	// "http://54.154.107.136//ws/v2/city/sectorslist?cityId=1";

	// http://cabapp.pocketapp.co.uk

	public static String accessToken = "F7F75C10711EF9D068BD4003D495574F5DD1A7BB"; // testAndroid1:India

	// public static final String accessToken =
	// "BFA12692551097D9501C55719B9DD80717EBE66C"; // devAndroid1:London

	public static final String paymentURL = "https://api.test.netbanx.com/hosted/v1/orders";
	// public static final String paymentURL =
	// "https://api.netbanx.com/hosted/v1/orders";

	public static final int socket_timeout = 5000; // in millisecs
	public static final int connection_timeout = 3000; // in millisecs

	/* Sliding menu items */
	public static final int JOBS_FRAGMENT = 0;
	public static final int INBOX_FRAGMENT = 1;
	// public static final int MY_FILTERS_FRAGMENT = 2;
	public static final int MY_JOBS_FRAGMENT = 2;
	public static final int CAB_PAY_FRAGMENT = 3;
	public static final int FARECALCULATOR_FRAGMENT = 4;
	public static final int PROMOTIONS_FRAGMENT = 5;
	public static final int CONTACT_SUPPORT_FRAGMENT = 6;
	public static final int MY_ACCOUNT_FRAGMENT = 7;
	public static final int BUY_ADD_CREDITS_FRAGMENT = 8;

	public static final int PRE_BOOK_FRAGMENT = 11;
	public static final int JOB_ACCEPTED = 12;
	public static final int MAP_DISTANCE = 13;
	public static final int SEND_MESSAGE = 14;
	public static final int PAYMENT = 15;
	public static final int LOGIN = 16;
	public static final int FORGOT_PASSWORD = 17;
	public static final int CHANGE_PASSWORD = 18;
	public static final int COUNTRY = 19;
	public static final int CONTACT_SUPPORT = 20;
	public static final int CITY = 21;
	public static final int REGISTRATION = 22;
	public static final int AVAILABLE = 23;
	public static final int RETRIVE_DRIVER_ACCOUNT_DETAILS = 24;
	public static final int ACCOUNT_MODIFICATION = 25;
	public static final int USER_AUTHORISATION = 26;
	public static final int REJECT_JOBS = 27;
	public static final int DRIVER_SETTINGS = 28;
	public static final int SEND_RECEIPT = 29;
	public static final int CAB_PIN_VALIDATION = 30;
	public static final int UPDATE_POSITION_AND_GET_JOB = 31;
	public static final int PAYCYCLE = 32;
	public static final int DELETE_DOCUMENT = 33;
	public static final int CHIP_AND_PIN = 34;
	public static final int AUTO_TOP_UP_STATUS = 35;
	public static final int FIXED_PRICE = 36;
	public static final int MY_JOBS_COUNT = 37;
	public static final int ADD_CARD = 38;
	public static final int NO_SHOW = 39;
	public static final int LOGOUT = 40;
	public static final int BUY_CREDITS = 41;
	public static final int JOB_PAYMENT = 42;
	public static final int SECTOR_CITY = 43;
	public static final int MPOS_CHIP_AND_PIN_PAYMENT = 44;
	public static final int MPOS_SEND_RECEIPT = 45;
	public static final int PAYMENT_URL_TO_PASSENGER = 46;
	public static final int CHECK_STATUS_OF_PAYMENT_URL_TO_PASSENGER = 47;
	public static final int PAYMENT_STATUS = 48;

	public static final int JOB_ACCEPTED_SUCCESS = 100;
	public static final int JOB_REJECED_SUCCESS = 101;
	// public static final String IS_IM_OUTSIDE_VISIBLE =
	// "IS_IM_OUTSIDE_BTN_VISIBLE";

	public static final String JOB_ID = "jobId";
	public static final String PICK_UP_ADDRESS = "pickupAddress";
	public static final String DROP_ADDRESS = "dropAddress";
	public static final String NO_OF_PASSENGERS = "noOfPassengers";
	public static final String WHEEL_CHAIR_ACCESS_REQUIRED = "wheelChairAccessRequired";
	public static final String HEARING_IMPAIRED = "isHearingImpaired";
	public static final String PICK_UP_LOCATION_LAT = "pickupLocationLat";
	public static final String PICK_UP_LOCATION_LNG = "pickupLocationLng";
	public static final String PICK_UP_DATE_TIME = "getPickupDateTime";
	public static final String DROP_LOCATION_LAT = "dropLocationlat";
	public static final String DROP_LOCATION_LNG = "dropLocationlng";
	public static final String PASSENGER_NAME = "passengerName";
	public static final String PASSENGER_NUMBER = "passengerNumber";
	public static final String PASSENGER_INTERNATIONAL_CODE = "passengerInternationalCode";
	public static final String PASSENGER_EMAIL = "passengerEmail";
	public static final String FARE = "fare";
	public static final String DISTANCE_DROP = "distanceDrop";
	public static final String DISTANCE_PICKUP = "distancePickUp";
	public static final String LUGGAGE_NOTE = "luggageNote";
	public static final String TIME = "time";
	public static final String JSON_GET_MESSAGE = "jobresponsejson";
	public static final String IS_FROM_MY_JOBS = "isfrommyjobs";
	public static final String IS_FROM_HISTORY = "isFromHistory";
	public static final String FROM_JOB_ACCEPTED = "fromjobaccepted";
	public static final String FROM_SHOW_MAP_ICON = "fromShowMapIcon";
	public static final String SOURCE = "source";
	public static final String MESSAGE = "message";
	public static final String MESSAGETIME = "messageTime";
	public static final String PICK_UP_PINCODE = "pickupPincode";
	public static final String DROP_OFF_PINCODE = "dropOffPincode";
	public static final String JOB_PAYMENT_TYPE = "job_payment_type";
	public static final String AMOUNT = "amount";
	public static final String CLIENT_PAID = "clientpaid";
	public static final String CANCELLED = "cancelled";
	public static final String CAB_SHARE = "cabShare";
	public static final String CAB_MILES = "cabMiles";
	public static final String CARD_LIST = "cardList";
	public static final String NO_SHOW_STATUS = "NoShowStatus";
	public static final String IS_PREBOOK = "isPreBook";

	// Registration
	public static final String FIRSTNAME = "firstname";
	public static final String SURNAME = "surname";
	public static final String MOBILE_NUMBER = "mobileNumber";
	public static final String COUNRTY_CODE = "internationalCode";
	public static final String WORKING_CITY = "workingCity";
	public static final String EMAIL_ADDRESS = "email";
	public static final String WORKING_CITY_LONDON = "1";
	public static final String IS_CITY_SELECTED_LONDON = "is_city_selected_london";
	public static final String DRIVER_DOCUMENT = "driverDocument";
	public static final String DRIVER_DOCUMENT_LIST = "driverDocumentList";

	public static final String USERNAME = "username";
	public static final String REGISTRATION_PASSWORD = "password";

	public static final String DRIVER_BADGE_EXPIRY = "badgeExpiration";
	public static final String BADGE_NUMBER = "badgeNumber";
	public static final String BADGE_COLOUR = "badgeColour";
	public static final String BADGE_COLOUR_GREEN = "green";
	public static final String BADGE_COLOUR_YELLOW = "yellow";
	public static final String SECTOR = "sector";

	public static final String VECHILE_REGISTRATION_NO = "registration";
	public static final String VECHILE_MAKE = "make";
	public static final String VECHILE_MODEL = "model";
	public static final String VECHILE_COLOUR = "colour";
	public static final String VECHILE_WHEELCHAIR_ACCESS = "wheelchairAccess";
	public static final String VECHILE_MAXIMUM_PASSENGERS = "maximumPassengers";

	public static final String PAYMENT_TYPE = "paymentType";
	public static final String IS_CARD_CHECKED = "isCardChecked";
	public static final String BANK_NAME = "bankName";
	public static final String BANK_ACCOUNT_NAME = "bankAccountName";
	public static final String BANK_ACCOUNT_NUMBER = "bankAccountNumber";
	public static final String BANK_SORT_CODE = "bankSortCode";
	public static final String BANK_IBAN = "bankIban";

	public static final String IS_AVAILABLE = "is_available";
	public static final String ACCESS_TOKEN_VALUE = "access_token";
	public static final String PAYMENT_ACCESS_TOKEN_VALUE = "payment_access_token";
	public static final String PAYMENT_CARD_ISSELECTED = "is_cardS_Selected";
	public static final String DRIVER_DETAILS = "driver_details";
	public static final String NO_OF_CREDITS = "no_of_credits";

	public static final String FROM_EDIT_MY_ACC_PERSONAL_DETAILS = "from_edit_my_account_personal_Details";
	public static final String FROM_EDIT_MY_ACC_VECHILE_DETAILS = "from_edit_my_account_vehicle_details";
	public static final String FROM_EDIT_ACCOUNT = "from_edit_my_account";

	public static final String IS_AUTHORISED = "is_authorised";
	public static final String EMAIL_ADDRESS_SHAREPREFERENCES = "email_address";
	public static final String IS_FORGOT = "isForgot";
	public static final String DISTANCE_FORMAT = "distance_format";

	public static final String BILLING_ADDRESS1 = "billingAddress1";
	public static final String BILLING_ADDRESS2 = "billingAddress2";
	public static final String BILLING_CITY = "billingCity";
	public static final String BILLING_POSTCODE = "billingPostcode";
	public static final String BILLING_COUNTRY = "billingCountry";

	// Fare Calculator
	public static final String IS_FROM_FARE_CALCULATOR = "is_from_fare_calculator";
	public static final String FC_PICKUP_ADDRESS = "fc_pickupAddress";
	public static final String FC_PICKUP_PINCODE = "fc_pickupPincode";
	public static final String FC_DROPOFF_ADDRESS = "fc_droppffAddress";
	public static final String FC_DROPOFF_PINCODE = "fc_droppffPincode";
	public static final String FC_DISTANCE = "fc_distance";
	public static final String FC_METER_VALUE = "fc_meter_value";
	public static final String PICKUP_LATITUDE = "pickupData_latitude";
	public static final String PICKUP_LONGITUDE = "pickupData_longitude";
	public static final String DESTINATION_LATITUDE = "destinationData_latitude";
	public static final String DESTINATION_LONGITUDE = "destinationData_longitude";

	public static final String TOTAL_VALUE = "totalvalue";
	public static final String METER_VALUE = "metervalue";
	public static final String TIP_VALUE = "tipvalue";
	public static final String CARD_FEE_VALUE = "cardfees";
	public static final String CABPAY_PAYMENT_TYPE = "CabPay_Payment_type";
	public static final String CABPAY_IS_CARD_REGISTERED = "isCardRegistered";
	public static final String CABPAY_FEES_PAID_BY = "feesPaidBy";
	public static final String PAYMENT_METHOD_DETAILS = "paymentMethod";
	public static final String MPOS_WALKUP = "mPosWalkUp";
	public static final String MPOS_TRANSACTION_ID = "mPosTransactionId";
	public static final String MPOS_PAYMENT_STATUS = "mPosPaymentStatus";
	public static final String MPOS_TRUNCATEDPAN = "mPosTruncatedPan";
	public static final String MPOS_BRAND = "mPosBrand";
	public static final String MPOS_MERCHANT_ID = "mPosMerchantId";
	public static final String MPOS_TERMINAL_ID = "mPosTerminalId";
	public static final String MPOS_ENTRY_MODE = "mPosEntryMode";
	public static final String MPOS_AID = "mPosAID";
	public static final String MPOS_PWID = "mPosPWID";
	public static final String MPOS_AUTHORIZATION = "mPosAuthorization";

	public static final String CASHBACK_VALUE = "cashbackValue";

	public static final String CABPAY_WALKUP = "bIsWalkUpJob";

	public static final String POS_DEVICE_NAME = "posdevicename";
	public static final String DRIVER_ID = "driverid";
	public static final String IS_DOCUMENT_UPLOADED = "documentuploaded";
	public static final String IS_AUTO_TOP_UP = "is_auto_top_up";

	public static final String LOGIN_EMAIL_USERNAME = "login_email_password";
	public static final String LOGIN_PASSSWORD = "login_password";
	public static final String REMEMBER_ME = "remember_me";
	public static final String ARRIVED_AT_PICK_UP_TIME = "arried_at_pick_up_time";

	// Buy Credits
	public static final String BUYCREDITS_NOOFCREDITS = "noOfCredits";
	public static final String BUYCREDITS_COSTPERCREDIT = "costPerCredit";
	public static final String BUYCREDITS_TOTLCOST = "totalCost";

	public static final String BUYCREDITS_CARD_BRAND = "card_brand";
	public static final String BUYCREDITS_DRIVERCARDID = "buyCredit_driverCardId";
	public static final String BUYCREDITS_CARD_ISSELECTED = "buy_credits_is_Selected";
	public static final String BUYCREDITS_CARD_TRUNCATEDPAN = "truncatedPan";
	public static final String BUYCREDITS_CARD_PAYWORKTOKEN = "buyCredits_payworkToken";

	// My Account
	public static final String AUTOTOPUP_LASTTRAN_AMT = "autoTopUp_LastTranAmt";
	public static final String AUTOTOPUP_LASTTRAN_CREDITCOUNT = "autoTopUp_LastTranCreditCount";
	public static final String AUTOTOPUP_LASTTRAN_BRAND = "autoTopUp_LastTranBrand";
	public static final String AUTOTOPUP_LASTTRAN_TRUNCATEDPAN = "autoTopUp_LastTranTruncatedPan";
	public static final String AUTOTOPUP_TOTAL_AMOUNT = "autoTopUp_Total_amount";
	public static final String AUTOTOPUP_SELECTED_CARDBRAND = "autoTopUp_Selected_CardBrand";
	public static final String AUTOTOPUP_SELECTED_CARD_TRUNCATEDPAN = "autoTopUp_Selected_CardTruncatedPan";
	public static final String AUTOTOPUP_CARD_PAYWORKTOKEN = "autoTopUp_payworkToken";
	public static final String AUTOTOPUP_CARD_ISSELECTED = "auto_top_up_is_Selected";
	public static final String AUTOTOPUP_DRIVERCARDID = "autoTopUp_driverCardId";

	// public static final String FROM_MY_ACCOUNT_EDIT = "isFromMyAccountEdit";
	// public static final String FROM_MY_ACCOUNT_TUTORIAL =
	// "isFromMyAccountTutorial";
	public static final String FROM_REGISTRATION_COMPLETE = "bIsFromRegistrationcomplete";
	// public static final String KEY_FROM_REGISTRATION_COMPLETE =
	// "KeyFromRegistrationComplete";
	// public static final String KEY_FROM_EDIT_MY_ACCOUNT =
	// "KeyFromMyAccountEdit";

	/*
	 * Constants for location update parameters
	 */
	// Milliseconds per second
	public static final int MILLISECONDS_PER_SECOND = 1000;

	// The update interval
	public static final int UPDATE_INTERVAL_IN_SECONDS = 10;

	// A fast interval ceiling
	public static final int FAST_CEILING_IN_SECONDS = 1;

	// Update interval in milliseconds
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

	// A fast ceiling of update intervals, used when the app is visible
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND * FAST_CEILING_IN_SECONDS;

	// Email validation String
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public static boolean flag_added_newcard = false;

	public static final int POLLING_TIME = 90000;
	public static final float MAX_DISTANCE = (float) 111111111111111111.00;
	public static final String BOOKING_ALREADY_ACCEPETED = "booking has already been accepted";
	public static final String BOOKING_CANCELLED = "booking has been cancelled";
	public static final String INSUFFICIENT_CREDIT = "You do not have sufficient credit to accept this booking";
	public static final String EDIT_BOOKING = "booking has been edited by passenger, please try again after some time.";

}
