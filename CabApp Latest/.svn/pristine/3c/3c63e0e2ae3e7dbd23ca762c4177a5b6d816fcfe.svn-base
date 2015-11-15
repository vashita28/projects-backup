package com.android.cabapp.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Log;

public class UrlSigner {

	// Note: Generally, you should store your private key someplace safe
	// and read them into your code

	private static String keyString = "w22Jtfg5c6BrAnfEgqUbpbQsj84=";

	// The URL shown in these examples must be already
	// URL-encoded. In practice, you will likely have code
	// which assembles your URL from user or web service input
	// and plugs those values into its parameters.
	private static String urlString = "http://maps.googleapis.com/maps/api/directions/json?client=gme-cabapp&origin=51.425273,0.111563&destination=51.153662,-0.182063&waypoints=optimize:true&mode=driving&units=imperial&alternatives=true&sensor=false";

	// This variable stores the binary key, which is computed from the string
	// (Base64) key
	private static byte[] key;

	public static String getMapURL(String clientUrl) throws IOException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {

		String SignedURL = "";

		// Convert the string to a URL so we can parse it
		URL url = new URL(clientUrl);

		UrlSigner signer = new UrlSigner(keyString);
		String request = signer.signRequest(url.getPath(), url.getQuery());

		Log.d("UrlSigner", "getMapURL :: " + "Signed URL :" + url.getProtocol() + "://" + url.getHost() + request);

		SignedURL = url.getProtocol() + "://" + url.getHost() + request;

		return SignedURL;
	}

	public UrlSigner(String keyString) throws IOException {
		// Convert the key from 'web safe' base 64 to binary
		keyString = keyString.replace('-', '+');
		keyString = keyString.replace('_', '/');
		Log.d("UrlSigner", "Key: " + keyString);
		this.key = Base64.decode(keyString);
	}

	public String signRequest(String path, String query) throws NoSuchAlgorithmException, InvalidKeyException,
			UnsupportedEncodingException, URISyntaxException {

		// Retrieve the proper URL components to sign
		String resource = path + '?' + query;

		// Get an HMAC-SHA1 signing key from the raw key bytes
		SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");

		// Get an HMAC-SHA1 Mac instance and initialize it with the HMAC-SHA1
		// key
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(sha1Key);

		// compute the binary signature for the request
		byte[] sigBytes = mac.doFinal(resource.getBytes());

		// base 64 encode the binary signature
		String signature = android.util.Base64.encodeToString(sigBytes, android.util.Base64.DEFAULT);

		// convert the signature to 'web safe' base 64
		signature = signature.replace('+', '-');
		signature = signature.replace('/', '_');
		
		String newURL = resource + "&signature="+ URLEncoder.encode(signature,"UTF-8");
		
		return newURL;
	}
}
