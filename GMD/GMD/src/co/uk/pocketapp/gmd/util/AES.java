package co.uk.pocketapp.gmd.util;

import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	static String szSecretShared = "aff5762d83b70c077bf9189648b2d0747e9c25c7011c268f55639b8a70f8c16d45b56b65c6ba81b8a08ee74fa18f0fcd0ccb45286bbd96a744c1d453fbd6dd45";

	public static String SHA256(String szData) {
		szData = szSecretShared;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return byte2hex(md.digest(hex2Byte(szData)));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "ERROR";
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

}
