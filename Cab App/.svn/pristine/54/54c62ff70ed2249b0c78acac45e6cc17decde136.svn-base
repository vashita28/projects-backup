package com.android.cabapp.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Pramod.W
 * 
 */

public class DES {

	public String encrypt(String str, String szKey) {
		try {
			byte[] keyBytes = szKey.getBytes("UTF-8");
			final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
			final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
			final Cipher ecipher = Cipher
					.getInstance("DESede/CBC/PKCS5Padding");
			ecipher.init(Cipher.ENCRYPT_MODE, key, iv);
			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF-8");
			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);
			// Encode bytes to base64 to get a string
			return new String(Base64.encode(enc));
		} catch (javax.crypto.BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String decrypt(String str, String szKey) {
		try {
			byte[] keyBytes = szKey.getBytes("UTF-8");
			final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
			final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
			final Cipher dcipher = Cipher
					.getInstance("DESede/CBC/PKCS5Padding");
			dcipher.init(Cipher.DECRYPT_MODE, key, iv);
			// Decode base64 to get bytes
			byte[] dec = Base64.decode(str.getBytes("UTF-8"));
			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);
			// Decode using utf-8
			return new String(utf8, "UTF-8");
		} catch (javax.crypto.BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
