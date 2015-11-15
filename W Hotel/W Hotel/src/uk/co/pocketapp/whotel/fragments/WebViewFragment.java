package uk.co.pocketapp.whotel.fragments;

import uk.co.pocketapp.whotel.FragmentLoad;
import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.util.Util;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.maps.model.LatLng;
import com.mblox.engage.MbloxManager;
import com.mblox.engage.exceptions.NoSuchMessageException;

@SuppressLint("ValidFragment")
public class WebViewFragment extends SherlockFragment {

	WebView webview_readmore;
	private ProgressDialog progressBar;

	String szURL, szMessageID, szLat, szLng, szIsWhotel;

	final String TAG = "WebViewFragment";

	public static FragmentLoad fragmentLoad;

	Button btn_getDirections;
	LatLng destinationLatLng;

	EasyTracker easyTracker;

	public WebViewFragment() {

	}

	public static WebViewFragment newInstance() {
		WebViewFragment webviewFragment = new WebViewFragment();
		return webviewFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundleArgs = getArguments();
		if (bundleArgs != null) {
			szURL = (String) bundleArgs.get("webURL");
			szMessageID = (String) bundleArgs.getString("messageid");
			szLat = (String) bundleArgs.getString("lat");
			szLng = (String) bundleArgs.getString("lng");
			szIsWhotel = (String) bundleArgs.getString("is_whotel");
		}

		if (szMessageID != null) {
			new registerClickEvent().execute();
		}

		if (szLat != null && szLng != null && szLat.length() > 0
				&& szLng.length() > 0) {
			destinationLatLng = new LatLng(Double.parseDouble(szLat),
					Double.parseDouble(szLng));
			Log.d("WebViewFragment", "Webview-Location:: " + destinationLatLng);
		}

		easyTracker = EasyTracker.getInstance(getActivity());

		if (easyTracker != null) {
			// This screen name value will remain set on the tracker and sent
			// with
			// hits until it is set to a new value or to null.
			easyTracker.set(Fields.SCREEN_NAME, "Read More");

			easyTracker.send(MapBuilder.createAppView().build());
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		fragmentLoad.onWebviewFragmentLoad(this);

		btn_getDirections.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (destinationLatLng != null) {
					// ProgressDialog mapDialog = new
					// ProgressDialog(getActivity());
					// mapDialog.setMessage("Loading map. Please wait...");
					// mapDialog.setIndeterminate(false);
					// mapDialog.setCancelable(false);
					// mapDialog.show();

					// Fragment mNewFragmentContent = new
					// MapGetDirectionFragment(
					// fragmentLoad, destinationLatLng, szIsWhotel);
					// getActivity()
					// .getSupportFragmentManager()
					// .beginTransaction()
					// .replace(R.id.main_frame, mNewFragmentContent,
					// "MapGetDirectionFragment")
					// .setTransition(
					// FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					// .addToBackStack(null).commit();

					final Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://maps.google.com/maps?" + "saddr="
									+ Util.getLocation().getLatitude() + ","
									+ Util.getLocation().getLongitude()
									+ "&daddr=" + szLat + "," + szLng));
					intent.setClassName("com.google.android.apps.maps",
							"com.google.android.maps.MapsActivity");
					startActivity(intent);

				} else {
					Toast.makeText(getActivity(), "Destination not found",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void onBackPressed() {
		Log.d("********************", "WebViewFragment on Key Back");
		fragmentLoad.onWebViewFragmentClosed();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_webview, container,
				false);
		webview_readmore = (WebView) view.findViewById(R.id.webview_readmore);

		btn_getDirections = (Button) view.findViewById(R.id.btn_get_directions);
		Typeface font_BoldItalic = Typeface.createFromAsset(getActivity()
				.getAssets(), "WSansNew-BoldItalic.otf");
		btn_getDirections.setTypeface(font_BoldItalic);
		btn_getDirections.setIncludeFontPadding(false);

		WebSettings settings = webview_readmore.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setBuiltInZoomControls(true);
		webview_readmore.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.create();

		progressBar = ProgressDialog.show(getActivity(), "", "Loading...");

		webview_readmore.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i(TAG, "Processing webview url click...");
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				Log.i(TAG, "Finished loading URL: " + url);
				if (progressBar.isShowing()) {
					progressBar.dismiss();
				}
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if (progressBar.isShowing()) {
					progressBar.dismiss();
				}
				Log.e(TAG, "Error: " + description);
				alertDialog.setTitle("Error");
				alertDialog.setMessage(description);
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								return;
							}
						});
				alertDialog.show();
			}
		});

		webview_readmore.loadUrl(szURL);

		return view;
	}

	class registerClickEvent extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
				Log.d("********************", "registerClickEvent"
						+ szMessageID);
				MbloxManager.getInstance(getActivity()).registerClick(
						szMessageID, szURL, Util.getLocation());
			} catch (NoSuchMessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (progressBar != null && progressBar.isShowing()) {
			progressBar.dismiss();
		}
	}

}
