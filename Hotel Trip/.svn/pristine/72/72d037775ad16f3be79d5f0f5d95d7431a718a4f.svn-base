package com.hoteltrip.android.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.graphics.Typeface;

public class Utils {
	private Context _context;

	// private static Handler mHandler = new Handler();
	// PR4M0DANK1TKAN4D
	// 5052344d3044414e4b31544b414e3444
	public static String szAESKey = "25d6c7fe35b9979a161f2136cd13b0ff";

	// constructor
	public Utils(Context context) {
		this._context = context;
	}

	public static Typeface getHelveticaNeue(Context context) {
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaNeue.ttf");
	}

	public static Typeface getHelveticaNeueBold(Context context) {
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaNeueBold.ttf");
	}

	public static Typeface getHelveticaNeueCondensedBlack(Context context) {
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaNeueCondensedBlack.ttf");
	}

	public static Typeface getHelveticaNeueCondensedBold(Context context) {
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaNeueCondensedBold.ttf");
	}

	public static Typeface getHelveticaNeueLight(Context context) {
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaNeueLight.ttf");
	}

	public static Typeface getHelveticaNeueMedium(Context context) {
		return Typeface.createFromAsset(context.getAssets(),
				"fonts/HelveticaNeueMedium.ttf");
	}

	public static String aesEncrypt(String szData, String szSecret) {
		try {
			byte[] data = szData.getBytes("UTF-8");

			Key key = new SecretKeySpec(hex2Byte(szSecret), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cipherData = cipher.doFinal(data);
			String szCipherData = new String(byte2hex(cipherData));
			return szCipherData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "ERROR";
	}

	public static String aesDecrypt(String szEncryptedData, String szSecret) {
		try {
			byte[] data = hex2Byte(szEncryptedData);

			Key key = new SecretKeySpec(hex2Byte(szSecret), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] cipherData = cipher.doFinal(data);
			String szDecryptedData = new String(cipherData);
			return szDecryptedData;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "ERROR";
	}

	// Convert Hex String to Byte Arrary
	public static byte[] hex2Byte(String str) {
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2),
					16);
		}
		return bytes;
	}

	// Convert Byte Arrary to Hex String
	public static String byte2hex(byte[] b) {
		// String Buffer can be used instead
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}

			if (n < b.length - 1) {
				hs = hs + "";
			}
		}

		return hs;
	}

	public static String DecodeXML(String szString) {
		szString = szString.replaceAll("&amp;", "&");
		szString = szString.replaceAll("&lt;", "<");
		szString = szString.replaceAll("&gt;", ">");
		szString = szString.replaceAll("&apos;", "'");
		szString = szString.replaceAll("&quot;", "\"");
		return szString;
	}

	public static String EncodeXML(String szString) {
		szString = szString.replaceAll("&", "&amp;");
		szString = szString.replaceAll("<", "&lt;");
		szString = szString.replaceAll(">", "&gt;");
		szString = szString.replaceAll("'", "&apos;");
		szString = szString.replaceAll("\"", "&quot;");
		return szString;
	}
}
