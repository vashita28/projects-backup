package com.android.cabapp.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.activity.MainActivity;
import com.android.cabapp.datastruct.json.Card;
import com.urbanairship.UAirship;

public class Util {

	private static final String TAG = Util.class.getSimpleName();

	static SharedPreferences prefs;
	static SharedPreferences.Editor editor;
	public static Context mContext;
	static AlertDialog jobsAlertDialog;
	public static boolean bIsNowOrPreebookFragment = false;
	public static boolean bIsJobAcceptedFragment = false;

	public static boolean bIsNowFragment = false;
	public static boolean bIsPrebookFragment = false;

	public static void showToastMessage(Context context, String text,
			int duration) {
		Toast.makeText(context, text, duration).show();
	}

	static List<Card> cardsList = new ArrayList<Card>();

	// public static Location getLocationByProvider(Context context,
	// String provider) {
	// Location location = null;
	// if (!isProviderSupported(context, provider)) {
	// return null;
	// }
	// LocationManager locationManager = (LocationManager) context
	// .getSystemService(Context.LOCATION_SERVICE);
	// try {
	// if (locationManager.isProviderEnabled(provider)) {
	// location = locationManager.getLastKnownLocation(provider);
	// }
	// } catch (Exception e) {
	// Log.d("", "Cannot acces Provider " + provider);
	// }
	// return location;
	// }
	//
	// public static boolean isProviderSupported(Context context, String
	// provider) {
	// LocationManager locationManager = (LocationManager) context
	// .getSystemService(Context.LOCATION_SERVICE);
	// boolean isProviderEnabled = false;
	// try {
	// isProviderEnabled = locationManager.isProviderEnabled(provider);
	// } catch (Exception ex) {
	// }
	// return isProviderEnabled;
	//
	// }

	public static List<Card> getCardsList() {
		return cardsList;
	}

	public static void setCardsList(List<Card> listCards) {
		cardsList.clear();
		cardsList.addAll(listCards);
	}

	public static void showJobsAlertDialog(final Context context) {
		try {
			if (jobsAlertDialog != null) {
				jobsAlertDialog.dismiss();
			}

			AlertDialog.Builder jobsAlertDialogBuilder = new AlertDialog.Builder(
					context);
			jobsAlertDialogBuilder
					.setTitle("Jobs")
					.setMessage("New Jobs received. Navigate to Jobs page?")
					.setPositiveButton("Navigate",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
									AppValues.clearAllJobsList();
									Intent launch = new Intent(
											Intent.ACTION_MAIN);
									launch.setClass(context, MainActivity.class);
									launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									launch.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
									UAirship.shared().getApplicationContext()
											.startActivity(launch);
								}
							})
					.setNegativeButton("Dismiss",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
								}
							});
			jobsAlertDialog = jobsAlertDialogBuilder.create();
			jobsAlertDialog.setCancelable(false);
			jobsAlertDialog.show();
		} catch (Exception e) {

		}
	}

	public static void showCancelJobAlertDialog(final Context context) {
		try {
			if (jobsAlertDialog != null) {
				jobsAlertDialog.dismiss();
			}

			AlertDialog.Builder jobsAlertDialogBuilder = new AlertDialog.Builder(
					context);
			jobsAlertDialogBuilder
					.setTitle("Job cancelled")
					.setMessage("Your job has been cancelled")
					.setPositiveButton("Navigate",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
									// redirect to history cancelled
									Bundle bundleMyAcc = new Bundle();
									bundleMyAcc.putBoolean("cancelPush", true);

									Fragment fragmentMyJobs = new com.android.cabapp.fragments.MyJobsFragment();
									if (fragmentMyJobs != null) {
										fragmentMyJobs
												.setArguments(bundleMyAcc);
										((MainActivity) Util.mContext)
												.setSlidingMenuPosition(Constants.MY_JOBS_FRAGMENT);
										((MainActivity) Util.mContext)
												.replaceFragment(
														fragmentMyJobs, false);
									}
								}
							})
					.setNegativeButton("Dismiss",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
								}
							});
			jobsAlertDialog = jobsAlertDialogBuilder.create();
			jobsAlertDialog.setCancelable(false);
			jobsAlertDialog.show();
		} catch (Exception e) {

		}
	}

	public static void showCancelJobAlertDialogOk(final Context context) {

		try {
			if (jobsAlertDialog != null) {
				jobsAlertDialog.dismiss();
			}
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			alertDialogBuilder
					.setTitle("Job cancelled")
					.setMessage("Booking has been cancelled")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
									// redirect to history cancelled
									Bundle bundleMyAcc = new Bundle();
									bundleMyAcc.putBoolean("cancelPush", true);

									Fragment fragmentMyJobs = new com.android.cabapp.fragments.MyJobsFragment();
									if (fragmentMyJobs != null) {
										fragmentMyJobs
												.setArguments(bundleMyAcc);
										((MainActivity) Util.mContext)
												.setSlidingMenuPosition(Constants.MY_JOBS_FRAGMENT);
										((MainActivity) Util.mContext)
												.replaceFragment(
														fragmentMyJobs, false);
									}
								}
							});
			jobsAlertDialog.setCancelable(false);
			jobsAlertDialog.show();
		} catch (Exception e) {

		}
	}

	public static void showAlertDialog(final Context context, String text) {

		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					context);
			alertDialogBuilder
					.setTitle("Alert")
					.setMessage(text)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface argDialog,
										int argWhich) {
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.setCancelable(false);
			alertDialog.show();
		} catch (Exception e) {

		}
	}

	public static Location getLocation(Context context) {
		Location location = null;

		try {
			LocationManager locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
			if (locationManager != null) {
				location = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				if (location == null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

	// public static String getDistanceInMiles(double kilometers) {
	// final double MILES_PER_KILOMETER = 0.621;
	// double miles = kilometers * MILES_PER_KILOMETER;
	// Log.e("Util", "miles:: " + miles);
	// return String.format(Locale.ENGLISH, "%.1f", miles);
	// }
	//
	// public static String getDistanceInKilometers(double miles) {
	// final double KILOMETER_PER_MILES = 1.609344;
	// double kilometers = miles * KILOMETER_PER_MILES;
	// Log.e("Util", "Kilometers:: " + kilometers);
	// return String.format(Locale.ENGLISH, "%.1f", kilometers);
	// }

	public static String getDistance(Context context, double input,
			String szDistanceUnitType) {
		if (szDistanceUnitType.equals("km")) {
			final double KILOMETER_PER_MILES = 1.609344;
			double kilometers = input * KILOMETER_PER_MILES;
			if (Constants.isDebug)
				Log.e("Util", "Kilometers:: " + kilometers);
			return String.format(Locale.ENGLISH, "%.1f", kilometers);
		} else {
			final double MILES_PER_KILOMETER = 0.621;
			double miles = input * MILES_PER_KILOMETER;
			if (Constants.isDebug)
				Log.e("Util", "miles:: " + miles);
			return String.format(Locale.ENGLISH, "%.1f", miles);
		}
	}

	public static void hideSoftKeyBoard(Context mContext, View v) {
		// InputMethodManager imm = (InputMethodManager) mContext
		// .getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	public static final String md5(final String password) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(password.getBytes());
			byte messageDigest[] = digest.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void finishActivity(Context mContext) {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	static void initSharedPref(Context context) {
		// prefs = context.getSharedPreferences(
		// Constants.CAB_APP_DRIVE_PREFERENCES, Context.MODE_PRIVATE);
		if (prefs == null)
			prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if (editor == null)
			editor = prefs.edit();
	}

	// public static void clearSharePRef(Context context) {
	// initSharedPref(context);
	// editor.clear();
	// editor.commit();
	// }

	public static void setEmailOrUserName(Context context, String szEmail) {
		initSharedPref(context);
		editor.putString(Constants.EMAIL_ADDRESS_SHAREPREFERENCES, szEmail);
		editor.commit();
	}

	public static String getEmailOrUserName(Context context) {
		initSharedPref(context);
		return prefs.getString(Constants.EMAIL_ADDRESS_SHAREPREFERENCES, "");
	}

	public static void setPassword(Context context, String szPassword) {
		initSharedPref(context);
		editor.putString("cabappuserpassword", szPassword);
		editor.commit();
	}

	public static String getPassword(Context context) {
		initSharedPref(context);
		return prefs.getString("cabappuserpassword", "");
	}

	public static void setIsAuthorised(Context context, boolean bIsAuthorised) {
		initSharedPref(context);
		editor.putBoolean(Constants.IS_AUTHORISED, bIsAuthorised);
		editor.commit();
	}

	public static Boolean getIsAuthorised(Context context) {
		initSharedPref(context);
		return prefs.getBoolean(Constants.IS_AUTHORISED, true);
	}

	public static void setAccessToken(Context context, String szAccesstoken) {
		Log.e(TAG, "szAccesstoken: " + szAccesstoken);
		initSharedPref(context);
		editor.putString(Constants.ACCESS_TOKEN_VALUE, szAccesstoken);
		editor.commit();
	}

	public static String getAccessToken(Context context) {
		initSharedPref(context);
		return prefs.getString(Constants.ACCESS_TOKEN_VALUE, "");
	}

	public static void setPaymentAccessToken(Context context,
			String szPaymentAccesstoken) {
		initSharedPref(context);
		editor.putString(Constants.PAYMENT_ACCESS_TOKEN_VALUE,
				szPaymentAccesstoken);
		editor.commit();
	}

	public static String getPaymentAccessToken(Context context) {
		initSharedPref(context);
		return prefs.getString(Constants.PAYMENT_ACCESS_TOKEN_VALUE, "");
	}

	// public static void setDriverDetails(Context context, String
	// szDriverDetails) {
	// initSharedPref(context);
	// editor.putString(Constants.DRIVER_DETAILS, szDriverDetails);
	// editor.commit();
	// }

	// public static String getDriverDetails(Context context) {
	// initSharedPref(context);
	// return prefs.getString(Constants.DRIVER_DETAILS, "");
	// }

	public static void setNumberOfCredits(Context context, int noOfCredits) {
		initSharedPref(context);
		editor.putInt(Constants.NO_OF_CREDITS, noOfCredits);
		editor.commit();
	}

	public static int getNumberOfCredits(Context context) {
		initSharedPref(context);
		return prefs.getInt(Constants.NO_OF_CREDITS, 0);
	}

	public static void setForgot(Context context, String szisForgot) {
		initSharedPref(context);
		editor.putString(Constants.IS_FORGOT, szisForgot);
		editor.commit();
	}

	public static String getForgot(Context context) {
		initSharedPref(context);
		return prefs.getString(Constants.IS_FORGOT, "Y");
	}

	public static void setDistanceFormat(Context context, String sDisFormat) {
		initSharedPref(context);
		editor.putString(Constants.DISTANCE_FORMAT, sDisFormat);
		editor.commit();
	}

	public static String getDistanceFormat(Context context) {
		initSharedPref(context);
		String format = prefs.getString(Constants.DISTANCE_FORMAT, "miles");
		if (format.equals(""))
			format = "miles";
		return format;
	}

	public static void setAutoTopUp(Context context, boolean bAutoTopUp) {
		initSharedPref(context);
		editor.putBoolean(Constants.IS_AUTO_TOP_UP, bAutoTopUp);
		editor.commit();
	}

	public static Boolean getAutoTopUp(Context context) {
		initSharedPref(context);
		return prefs.getBoolean(Constants.IS_AUTO_TOP_UP, false);
	}

	public static void setPOSDeviceName(Context context, String szPOSDeviceName) {
		initSharedPref(context);
		editor.putString(Constants.POS_DEVICE_NAME, szPOSDeviceName);
		editor.commit();
	}

	public static String getPOSDeviceName(Context context) {
		initSharedPref(context);
		String szDeviceName = "";
		szDeviceName = prefs.getString(Constants.POS_DEVICE_NAME, "");// PP0513901425
		// Log.e("Util", "Device Name: " + szDeviceName);
		// if (szDeviceName.isEmpty())
		// szDeviceName = "PP0513901425";
		Log.e("Util", "Device Name: " + szDeviceName);

		return szDeviceName;
	}

	public static void setDriverID(Context context, String szDriverID) {
		initSharedPref(context);
		editor.putString(Constants.DRIVER_ID, szDriverID);
		editor.commit();
	}

	public static String getDriverID(Context context) {
		initSharedPref(context);
		return prefs.getString(Constants.DRIVER_ID, ""); // 104 for testing
															// purpose
	}

	public static void setIsDocumentsUploaded(Context context,
			boolean bIsDocumentUploaded) {
		initSharedPref(context);
		editor.putBoolean(Constants.IS_DOCUMENT_UPLOADED, bIsDocumentUploaded);
		editor.commit();
	}

	public static Boolean getIsDocumentsUploaded(Context context) {
		initSharedPref(context);
		return prefs.getBoolean(Constants.IS_DOCUMENT_UPLOADED, false);
	}

	public static void setCitySelectedLondon(Context context,
			boolean bCistySelectedLondon) {
		initSharedPref(context);
		editor.putBoolean(Constants.IS_CITY_SELECTED_LONDON,
				bCistySelectedLondon);
		editor.commit();
	}

	public static Boolean getCitySelectedLondon(Context context) {
		initSharedPref(context);
		return prefs.getBoolean(Constants.IS_CITY_SELECTED_LONDON, false);
	}

	public static void setIsRememberMeChecked(Context context,
			boolean bIsRememberMeChecked) {
		initSharedPref(context);
		editor.putBoolean(Constants.REMEMBER_ME, bIsRememberMeChecked);
		editor.commit();
	}

	public static Boolean getIsRememberMeChecked(Context context) {
		initSharedPref(context);
		return prefs.getBoolean(Constants.REMEMBER_ME, false);
	}

	public static void setLogInEmailOrUserName(Context context, String sEmail) {
		initSharedPref(context);
		editor.putString(Constants.LOGIN_EMAIL_USERNAME, sEmail);
		editor.commit();
	}

	public static String getLogInEmailOrUserName(Context context) {
		initSharedPref(context);
		return prefs.getString(Constants.LOGIN_EMAIL_USERNAME, "");
	}

	public static void setLogInPassword(Context context, String sPassword) {
		initSharedPref(context);
		editor.putString(Constants.LOGIN_PASSSWORD, sPassword);
		editor.commit();
	}

	public static String getLogInPassword(Context context) {
		initSharedPref(context);
		return prefs.getString(Constants.LOGIN_PASSSWORD, "");
	}

	public static void setCashBack(Context context, String szCashBack) {
		initSharedPref(context);
		editor.putString(Constants.CASHBACK_VALUE, szCashBack);
		editor.commit();
	}

	public static String getCashBack(Context context) {
		initSharedPref(context);
		return prefs.getString(Constants.CASHBACK_VALUE, "");
	}

	// public static void setIsFromRegistrationcomplete(Context context,
	// boolean bIsFromRegistrationComplete) {
	// initSharedPref(context);
	// editor.putBoolean(Constants.KEY_FROM_REGISTRATION_COMPLETE,
	// bIsFromRegistrationComplete);
	// editor.commit();
	// }
	//
	// public static Boolean getIsFromRegistrationcomplete(Context context) {
	// initSharedPref(context);
	// return prefs
	// .getBoolean(Constants.KEY_FROM_REGISTRATION_COMPLETE, false);
	// }
	//
	// public static void setIsFromEditMyAccount(Context context,
	// boolean bIsFromRegistrationComplete) {
	// initSharedPref(context);
	// editor.putBoolean(Constants.KEY_FROM_EDIT_MY_ACCOUNT,
	// bIsFromRegistrationComplete);
	// editor.commit();
	// }
	//
	// public static Boolean getIsFromEditMyAccount(Context context) {
	// initSharedPref(context);
	// return prefs.getBoolean(Constants.KEY_FROM_EDIT_MY_ACCOUNT, false);
	// }

	public static String parseMapDistanceData(String json, Context mContext) {
		// if (Constants.isDebug)
		// Log.e(TAG + "::parseMapDistanceData::", "json::>  " + json);
		if (json != null || mContext != null) {
			try {
				String distance = "0";
				JSONObject jObject = new JSONObject(json);

				jObject = jObject.getJSONArray("rows").getJSONObject(0)
						.getJSONArray("elements").getJSONObject(0);
				if (jObject.has("distance")) {
					distance = jObject.getJSONObject("distance").getString(
							"text");

					if (Constants.isDebug)
						Log.e("Util", "Distance " + distance);
					if (distance != null) {
						distance = distance.replace("km", "");
						if (distance.contains("m")) {
							distance = distance.replace("m", "");
							distance = String.format(Locale.ENGLISH, "%.2f",
									Float.parseFloat(distance) / 1000);
						}
					}

				}
				return distance;

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

	public static String getTimeFormat(String date) {
		// if (Constants.isDebug)
		// Log.d("Util", "getTimeFormat:: " + date);
		SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("EEE dd MMM h:mm a");// HH:mm
		String eDate = date;
		Date eDDte = null;
		try {
			eDDte = df1.parse(eDate);
			// if (Constants.isDebug)
			// Log.d("Util", "getTimeFormat::Formatted " + df2.format(eDDte));
			return df2.format(eDDte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-";
	}

	public static Date getDateFormatted(String date) {
		SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("EEE dd MMM HH:mm");
		String eDate = date;
		Date eDDte = null;
		try {
			eDDte = df1.parse(eDate);
			return eDDte;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Time getTimeOnly(String date) {
		// if (Constants.isDebug)
		// Log.d("Util", "getDateFormatted:: " + date);
		SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");
		String eDate = date;
		Date eDDte = null;
		try {
			eDDte = df1.parse(eDate);
			// if (Constants.isDebug)
			// Log.d("Util", "getTimeFormat::Formatted " + df2.format(eDDte));
			return Time.valueOf(df2.format(eDDte));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void setLogOutValues(Context context) {
		AppValues.mapRegistrationData.clear();
		AppValues.clearAllJobsList();
		setAccessToken(context, "");
		setDriverID(context, "");
		setIsAuthorised(context, false);
		setIsDocumentsUploaded(context, false);
		setPOSDeviceName(context, "");
		setNumberOfCredits(context, 0);
		setEmailOrUserName(context, "");
		setPassword(context, "");
		setDistanceFormat(context, "");
		// setDriverDetails(context, "");
		setForgot(context, "N");
		setCitySelectedLondon(context, false);
	}

	public static void setIsOverlaySeen(Context context,
			boolean bIsOverlaySeen, String activityName) {
		initSharedPref(context);
		Log.e(TAG, "Key:SET " + activityName + " bIsOverlaySeen: "
				+ bIsOverlaySeen);
		editor.putBoolean(activityName, bIsOverlaySeen);
		editor.commit();
	}

	public static Boolean getIsOverlaySeen(Context context, String activityName) {
		initSharedPref(context);
		Log.e(TAG, "Key:GET " + activityName);
		return prefs.getBoolean(activityName, false);
	}

	public static HashMap<String, Integer> cardsCodeHashMap = new HashMap<String, Integer>() {
		{
			put("visa", R.drawable.visa);
			put("amex", R.drawable.amex);
			put("discover", R.drawable.dicover);
			put("maestro", R.drawable.maestro);
			put("mastercard", R.drawable.mastercard);
		}
	};

	public static String getCurrentAddress(Context _mContext, Location location) {
		String address = "";

		Geocoder geocoder;
		List<Address> addresses = null;
		try {
			geocoder = new Geocoder(_mContext, Locale.getDefault());
			addresses = geocoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
			address = addresses.get(0).getAddressLine(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// String city = addresses.get(0).getAddressLine(1);
		// String country = addresses.get(0).getAddressLine(2);
		return address;

	}

}
