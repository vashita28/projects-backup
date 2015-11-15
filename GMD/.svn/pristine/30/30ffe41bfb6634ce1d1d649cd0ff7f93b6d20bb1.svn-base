package co.uk.pocketapp.gmd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class EncodeDecodeAES {
	private final String TAG = "ENCODE DECODE AES";

	// String szKey = "4D92199549E0F2EF009B4160F3582E5528A11A45017F3EF8";

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

	public void encrypt(File file) {// InputStream in,OutputStream out
		try {
			FileInputStream fis = new FileInputStream(file);
			Log.v(TAG, "FILE INPUT STREam" + fis.toString());

			File outfile = AppValues.getServiceCheckListtXMLFileTemp();
			Log.v(TAG, "FILE OUTPUT STREAM "
					+ AppValues.getServiceCheckListtXMLFileTemp().getPath()
							.toString());
			int read;
			FileOutputStream fos = new FileOutputStream(outfile);

			Key key = new SecretKeySpec(hex2Byte(SHA256("")), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			CipherInputStream cis = new CipherInputStream(fis, cipher);
			while ((read = cis.read()) != -1) {
				fos.write((char) read);
				fos.flush();
				Log.v(TAG, "ENCRYTPITON DATA " + cis.toString());
			}
			fos.close();

		} catch (Exception e) {
			e.getMessage().toString();
		}

	}

	public void decrypt(File file) {

		try {
			int read;

			FileInputStream fis = new FileInputStream(file);
			File decfile = AppValues.getServiceCheckListtXMLFileTempNew();

			FileOutputStream decfos = new FileOutputStream(decfile);

			Key key = new SecretKeySpec(hex2Byte(SHA256("")), "AES");
			Cipher decipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
			decipher.init(Cipher.DECRYPT_MODE, key);

			CipherOutputStream cos = new CipherOutputStream(decfos, decipher);
			while ((read = fis.read()) != -1) {
				cos.write(read);
				cos.flush();
				Log.v(TAG, "DECRYPTION DATA " + cos.toString());

			}
			cos.close();

		} catch (Exception e) {
			Log.v(TAG, e.getMessage().toString());
		}

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
