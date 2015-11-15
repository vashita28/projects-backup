package co.uk.pocketapp.gmd.util;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.util.Log;

public class AppValues {

	// public static final String SERVER_URL =
	// "http://www.armdev3.co.uk/api.php?";
	public static final String SERVER_URL = "http://dev.pocketapp.co.uk/dev/gmd/api.php?";

	public static final String APP_NAME = "GMD";
	public static final String ACCESS_TOKEN = "83DF627CB93B8437";

	public static final String szMasterPassword = "88888888";

	public static Context AppContext;

	public static String XMLSummary = "";

	// public static int nleaf_child_in_each_thirdgen = 7;

	public static boolean bIsEncryptionRequired = false;

	// public static final String EXTERNAL_STORAGE_DIRECTORY =
	// getDirectory("Removable", "MicroSD");

	public static String getDirectory() { // String variableName, String
											// defaultPath

		String path = "";
		File dir = new File("Removable", "MicroSD"); // asus fonepad
		// File dir = new File("storage", "sdcard0"); // nexus 7
		// File dir = new File("mnt", "extSdCard"); // samsung tab sangam
		// File dir = Environment.getExternalStorageDirectory(); // internal
		// memory for other devices
		if (dir.exists())
			path = dir.getAbsolutePath();
		else
			path = null;
		return path;

	}

	public static File getReportXMLFile() {

		File reportFile = new File(getDirectory() + File.separator
				+ AppValues.APP_NAME);
		if (!reportFile.isDirectory()) {
			reportFile.mkdir();
		}
		String XMLName = "gmd_report.xml";
		reportFile = new File(reportFile, XMLName);
		return reportFile;
	}

	public static File getServiceCheckListtXMLFile() {

		File checklistFile = new File(getDirectory() + File.separator
				+ AppValues.APP_NAME);
		if (!checklistFile.isDirectory()) {
			checklistFile.mkdir();
		}
		String XMLName = "gmd_service_checklist.xml";
		checklistFile = new File(checklistFile, XMLName);
		return checklistFile;

	}

	public static File getPhotoGraphFile() {

		File photoFile = new File(getDirectory() + File.separator
				+ AppValues.APP_NAME + "/Data");
		if (!photoFile.isDirectory()) {
			photoFile.mkdir();
		}
		photoFile = new File(photoFile, "Photos");
		if (!photoFile.isDirectory()) {
			photoFile.mkdir();
		}
		photoFile = new File(photoFile, Util.getReportID(AppContext));
		if (!photoFile.isDirectory()) {
			photoFile.mkdir();
		}

		String szFileNumber = Util.getPhotoFileNumber(AppContext);
		if (szFileNumber.length() == 1)
			szFileNumber = "00" + szFileNumber;
		if (szFileNumber.length() == 2)
			szFileNumber = "0" + szFileNumber;

		String photoFileName = "Photo_" + szFileNumber + ".png";
		photoFile = new File(photoFile, photoFileName);
		return photoFile;

	}

	public static File getPhotoGraphDirectory() {

		File photoFile = new File(getDirectory() + File.separator
				+ AppValues.APP_NAME + "/Data");
		if (!photoFile.isDirectory()) {
			photoFile.mkdir();
		}
		photoFile = new File(photoFile, "Photos");
		if (!photoFile.isDirectory()) {
			photoFile.mkdir();
		}
		photoFile = new File(photoFile, Util.getReportID(AppContext));
		if (!photoFile.isDirectory()) {
			photoFile.mkdir();
		}
		return photoFile;
	}

	public static File getPhotosCompressedTempDirectory() {

		File photoFile = new File(getDirectory() + File.separator
				+ AppValues.APP_NAME + "/Data");
		if (!photoFile.isDirectory()) {
			photoFile.mkdir();
		}
		photoFile = new File(photoFile, "Temp");
		if (!photoFile.isDirectory()) {
			photoFile.mkdir();
		}
		return photoFile;
	}

	public static String getMd5Hash(String szSource) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bmessageDigest = md.digest(szSource.getBytes());
			// We convert the result into a big integer, so that we can convert
			// it into a Hexadecimal;
			BigInteger convertedToNumber = new BigInteger(1, bmessageDigest);
			String szmd5 = convertedToNumber.toString(16); // convert to hex
															// string

			// stuff with leading zeroes which are removed when we convert the
			// byte array to a bigInteger;
			if (szmd5.length() < 32) {
				while (szmd5.length() < 32)
					szmd5 = "0" + szmd5;
			}

			return szmd5;
		} catch (NoSuchAlgorithmException e) {
			Log.e("MD5", e.getMessage());
			return null;
		}
	}

	public static File getServiceCheckListtXMLFileTemp() {

		File checklistFile = new File(getDirectory() + File.separator
				+ AppValues.APP_NAME);
		if (!checklistFile.isDirectory()) {
			checklistFile.mkdir();
		}
		String XMLName = "gmd_service_checklist_temp.xml";
		checklistFile = new File(checklistFile, XMLName);
		return checklistFile;

	}

	public static File getServiceCheckListtXMLFileTempNew() {

		File checklistFile = new File(getDirectory() + File.separator
				+ AppValues.APP_NAME);
		if (!checklistFile.isDirectory()) {
			checklistFile.mkdir();
		}
		String XMLName = "gmd_service_checklist_temp_new.xml";
		checklistFile = new File(checklistFile, XMLName);
		return checklistFile;

	}

	public static File getServiceCheckListDirectory() {

		File checklistFile = new File(getDirectory() + File.separator
				+ AppValues.APP_NAME);
		if (!checklistFile.isDirectory()) {
			checklistFile.mkdir();
		}

		checklistFile = new File(checklistFile, "Service Checklist");
		if (!checklistFile.isDirectory()) {
			checklistFile.mkdir();
		}
		return checklistFile;
	}
}
