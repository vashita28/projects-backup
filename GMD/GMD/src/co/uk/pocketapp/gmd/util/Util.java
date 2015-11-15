package co.uk.pocketapp.gmd.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class Util {
	static SharedPreferences prefs;
	static Editor editor;

	public static void setLoggedIn(Context context, boolean bIsLoggedIn) {

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putBoolean("gmdloggedin", bIsLoggedIn);
		editor.commit();
	}

	public static boolean getLoggedIn(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("gmdloggedin", false);
	}

	public static void setPhotoEntryDetails(Context context, String szFileName,
			String szLocation, String szPosition, String szComments,
			String szMotorPart, String szDateTime) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putString("photodetails_" + getReportID(context) + "_"
				+ szFileName, szLocation + "," + szPosition + "," + szComments
				+ "," + szMotorPart + "," + szDateTime);
		editor.commit();
	}

	public static String getPhotoEntryDetails(Context context, String szFileName) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("photodetails_" + getReportID(context) + "_"
				+ szFileName, "");
	}

	public static void setEngineerID(Context context, String szID) {

		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putString("engineerid", szID);
		editor.commit();
	}

	public static String getEngineerID(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("engineerid", "");
	}

	public static void setLogoutValues(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		// setEngineerID(context, "");
		setLoggedIn(context, false);
		// setSiteName(context, "");
		// setMillName(context, "");
		setIsServiceCheclistValuesInserted(context, false);
		// setIsReportXMLDownloaded(context, getReportID(context), false);
		// setReportID(context, "");
		setIsLeafNodeValuesInserted(context, false);
		setServiceCheckListCreated(context, false);
		setIsReportUploaded(context, false);
		editor.commit();
	}

	// // set Report Status
	// public static void setReportStatus(Context context, int szReportStatus) {
	// prefs = PreferenceManager.getDefaultSharedPreferences(context);
	// editor = prefs.edit();
	// editor.putInt("gmd_report_status", szReportStatus);
	// editor.commit();
	// }
	//
	// //get Report Status
	// public static int getReportStatus(Context context) {
	// prefs = PreferenceManager.getDefaultSharedPreferences(context);
	// //return prefs.getInt("gmd_report_status", "");
	// return prefs.getInt("gmd_report_status", 0);
	// }

	public static void setSiteName(Context context, String szSiteName) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putString("gmd_site_name", szSiteName);
		editor.commit();
	}

	public static String getSiteName(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("gmd_site_name", "");
	}

	public static void setMillName(Context context, String szMillName) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putString("gmd_mill_name", szMillName);
		editor.commit();
	}

	public static String getMillName(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("gmd_mill_name", "");
	}

	public static void setMillID(Context context, String szMillName,
			String szMillID) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putString("gmd_mill_id_" + szMillName, szMillID);
		editor.commit();
	}

	public static String getMillID(Context context, String szMillName) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("gmd_mill_id_" + szMillName, "");
	}

	public static void setIsReportReassigned(Context context,
			boolean bIsReassigned) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putBoolean("gmd_report_reassigned", bIsReassigned);
		editor.commit();
	}

	public static boolean getIsReportReassigned(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("gmd_report_reassigned", false);
	}

	public static void setIsServiceCheclistValuesInserted(Context context,
			boolean bValue) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putBoolean("gmd_service_checklist_values_inserted", bValue);
		editor.commit();
	}

	public static Boolean getIsServiceCheclistValuesInserted(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("gmd_service_checklist_values_inserted", false);
	}

	public static void setReportID(Context context, String szReportID) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putString("gmd_report_id", szReportID);
		editor.commit();
	}

	public static String getReportID(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("gmd_report_id", "");
	}

	public static void setIsReportXMLDownloaded(Context context, boolean bValue) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putBoolean("gmd_report_xml", bValue);
		editor.commit();
	}

	public static Boolean getIsReportXMLDownloaded(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("gmd_report_xml", false);
	}

	public static void setIsLeafNodeValuesInserted(Context context,
			boolean bValue) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putBoolean("gmd_report_leafnodevalues_inserted", bValue);
		editor.commit();
	}

	public static Boolean getIsLeafNodeValuesInserted(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("gmd_report_leafnodevalues_inserted", false);
	}

	public static void setReportKilled(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		setPhotoFileNumber(context, "001");
		setSiteName(context, "");
		setMillName(context, "");
		setIsServiceCheclistValuesInserted(context, false);
		setIsReportXMLDownloaded(context, false);
		setReportID(context, "");
		setIsLeafNodeValuesInserted(context, false);
		setServiceCheckListCreated(context, false);
		setIsReportUploaded(context, false);
		setPDFListDownloaded(context, false);
		deleteAllPDFInArchiveFolder(context);
		editor.commit();
	}

	public static void setServiceCheckListCreated(Context context,
			boolean bValue) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putBoolean("gmd_servicechecklist_created", bValue);
		editor.commit();
	}

	public static boolean getServiceCheckListCreated(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("gmd_servicechecklist_created", false);
	}

	public static void setIsReportUploaded(Context context, boolean bValue) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putBoolean("gmd_report_uploaded_" + getReportID(context), bValue);
		editor.commit();
	}

	public static boolean getIsReportUploaded(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("gmd_report_uploaded_" + getReportID(context),
				false);
	}

	public static void setPhotoFileNumber(Context context, String number) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putString("gmd_photo_name_" + getReportID(context), number);
		editor.commit();
	}

	public static String getPhotoFileNumber(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString("gmd_photo_name_" + getReportID(context), "001");
	}

	public static void setPDFListDownloaded(Context context,
			boolean bIsDownloaded) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		editor = prefs.edit();
		editor.putBoolean("gmd_pdf_list_downloaded_" + getReportID(context),
				bIsDownloaded);
		editor.commit();
	}

	public static void deleteAllPDFInArchiveFolder(Context context) {
		String extStorageDirectory = AppValues.getDirectory() + File.separator
				+ AppValues.APP_NAME;
		File file_Archive_Reports_Folder = new File(extStorageDirectory
				+ File.separator + "Archive Reports");
		if (file_Archive_Reports_Folder.isDirectory()) {
			File[] files = file_Archive_Reports_Folder.listFiles();
			if (files != null)
				for (File file : files) {
					if (file.exists()) {
						file.delete();
					}
				}
		}
		Util.setPDFListDownloaded(context, false);
	}

	public static boolean getPDFListDownloaded(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean("gmd_pdf_list_downloaded_"
				+ getReportID(context), false);
	}

	// public static void setPassword(Context context, String szPassword) {
	// prefs = PreferenceManager.getDefaultSharedPreferences(context);
	// editor = prefs.edit();
	// editor.putString("gmd_password", szPassword);
	// editor.commit();
	// }
	// public static String getPassword(Context context) {
	// prefs = PreferenceManager.getDefaultSharedPreferences(context);
	// return prefs.getString("gmd_password", "");
	// }

	public static void EncryptReportXML() {
		if (AppValues.bIsEncryptionRequired) {
			StringBuilder text = null;
			if (AppValues.getReportXMLFile().exists()) {
				text = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(new FileReader(
							AppValues.getReportXMLFile()));
					String line;

					while ((line = br.readLine()) != null) {
						text.append(line);
						text.append('\n');
					}
					br.close();

					try {
						FileOutputStream fos = new FileOutputStream(
								AppValues.getReportXMLFile());
						fos.write(AES.aesEncrypt(text.toString(),
								AES.SHA256("")).getBytes());
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	public static void DecryptReportXML() {
		if (AppValues.bIsEncryptionRequired) {
			StringBuilder text = null;
			if (AppValues.getReportXMLFile().exists()) {
				text = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(new FileReader(
							AppValues.getReportXMLFile()));
					String line;

					while ((line = br.readLine()) != null) {
						text.append(line);
						text.append('\n');
					}
					br.close();

					try {
						FileOutputStream fos = new FileOutputStream(
								AppValues.getReportXMLFile());
						fos.write(AES.aesDecrypt(text.toString(),
								AES.SHA256("")).getBytes());
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	public static void EncryptServiceCheckListXML() {
		if (AppValues.bIsEncryptionRequired) {
			StringBuilder text = null;
			if (AppValues.getServiceCheckListtXMLFile().exists()) {
				text = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(new FileReader(
							AppValues.getServiceCheckListtXMLFile()));
					String line;

					while ((line = br.readLine()) != null) {
						text.append(line);
						text.append('\n');
					}
					br.close();

					try {
						FileOutputStream fos = new FileOutputStream(
								AppValues.getServiceCheckListtXMLFile());
						fos.write(AES.aesEncrypt(text.toString(),
								AES.SHA256("")).getBytes());
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	public static void DecryptServiceCheckListXML() {
		if (AppValues.bIsEncryptionRequired) {
			StringBuilder text = null;
			if (AppValues.getServiceCheckListtXMLFile().exists()) {
				text = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(new FileReader(
							AppValues.getServiceCheckListtXMLFile()));
					String line;

					while ((line = br.readLine()) != null) {
						text.append(line);
						text.append('\n');
					}
					br.close();

					try {
						FileOutputStream fos = new FileOutputStream(
								AppValues.getServiceCheckListtXMLFile());
						fos.write(AES.aesDecrypt(text.toString(),
								AES.SHA256("")).getBytes());
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressLint("NewApi")
	public static boolean isAirplaneModeOn(Context context) {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return Settings.System.getInt(context.getContentResolver(),
					Settings.System.AIRPLANE_MODE_ON, 0) != 0;
		} else {
			return Settings.Global.getInt(context.getContentResolver(),
					Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
		}
	}

}
