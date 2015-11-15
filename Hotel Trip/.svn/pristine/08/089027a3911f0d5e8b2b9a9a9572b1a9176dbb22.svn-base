package com.hoteltrip.android;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewTwitterActivity extends Activity {

	// Twitter oauth urls
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	WebView webViewTwitter;
	ProgressBar progressWebView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_webviewtwitter);

		webViewTwitter = (WebView) findViewById(R.id.wbTwitter);

		progressWebView = (ProgressBar) findViewById(R.id.progress_webview);
		progressWebView.setVisibility(View.VISIBLE);

		// Shared Preferences
		ConfirmationActivity.mSharedPreferences = getApplicationContext()
				.getSharedPreferences("MyPref", 0);

		if (getIntent().getExtras() != null) {

			webViewTwitter.getSettings().setJavaScriptEnabled(true);
			webViewTwitter.loadUrl(getIntent().getExtras().getString("URI"));
			webViewTwitter.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					progressWebView.setVisibility(View.GONE);
				}

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					boolean shouldOverride = false;
					if (url != null
							&& url.toString().startsWith("oauth://t4jsample")) { // NON-NLS
						// DO SOMETHING
						shouldOverride = true;

						try {
							ConfirmationActivity.requestToken = ConfirmationActivity.twitter
									.getOAuthRequestToken(ConfirmationActivity.TWITTER_CALLBACK_URL);
						} catch (TwitterException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						Uri uri = Uri.parse(url);
						// oAuth verifier
						String verifier = uri
								.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

						try {
							// Get the access token
							AccessToken accessToken = ConfirmationActivity.twitter
									.getOAuthAccessToken(
											ConfirmationActivity.requestToken,
											verifier);

							// Shared Preferences
							Editor e = ConfirmationActivity.mSharedPreferences
									.edit();

							// After getting access token, access token
							// secret
							// store them in application preferences
							e.putString(
									ConfirmationActivity.PREF_KEY_OAUTH_TOKEN,
									accessToken.getToken());
							e.putString(
									ConfirmationActivity.PREF_KEY_OAUTH_SECRET,
									accessToken.getTokenSecret());
							// Store login status - true
							e.putBoolean(
									ConfirmationActivity.PREF_KEY_TWITTER_LOGIN,
									true);
							e.commit(); // save changes

							Log.e("Twitter OAuth Token",
									"> " + accessToken.getToken());

							// Getting user details from twitter
							// For now i am getting his name only
							long userID = accessToken.getUserId();
							User user = ConfirmationActivity.twitter
									.showUser(userID);
							String username = user.getName();

							// Intent intent = new Intent(
							// WebViewTwitterActivity.this,
							// ConfirmationActivity.class);
							// intent.putExtra("twittersuccess", true);
							// intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							// startActivity(intent);
							ConfirmationActivity.bIsTwitterSuccess = true;
							finish();

						} catch (Exception e) {
							// Check log for login errors
							Log.e("Twitter Login Error", "> " + e.getMessage());
						}
					}

					return shouldOverride;
				}
			});

		}

	}
}
