package co.uk.peeki.fragments;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import co.uk.peeki.R;
import co.uk.peeki.interfaces.FragmentLoad;
import co.uk.peeki.receivers.NetworkBroadcastReceiver;
import co.uk.peeki.ui.MainActivity;
import co.uk.peeki.ui.ShowSelectionActivity;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class ShareFragment extends Fragment {
	private static final String TAG = "SHARE FRAGMENT";
	static FragmentLoad mFragmentLoad;
	static Context mContext;

	Button btnFacebook;

	// Your Facebook APP ID
	private static String APP_ID = "515738058461322";

	// Instance of Facebook Class
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	public static SharedPreferences mSharedPreferences;

	public static ProgressDialog pDialog;

	public static Fragment newInstance(Context context,
			FragmentLoad mfragmentLoad) {
		mContext = context;
		ShareFragment f = new ShareFragment();
		mFragmentLoad = mfragmentLoad;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.share_fragment,
				null, true);

		btnFacebook = (Button) root.findViewById(R.id.BtnFacebook);

		facebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(facebook);
		// Shared Preferences
		mSharedPreferences = getActivity().getApplicationContext()
				.getSharedPreferences(ShowSelectionActivity.MyPREFERENCES,
						Context.MODE_PRIVATE);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		btnFacebook.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {

					loginToFacebook();
					// Fragment mFragment = SelectionFragment.newInstance(
					// mContext, mFragmentLoad);
					// FragmentTransaction tx = getActivity()
					// .getSupportFragmentManager().beginTransaction();
					// tx.replace(R.id.main_fragment, mFragment);
					// tx.addToBackStack(null);
					// tx.commit();
				} else {
					Toast.makeText(mContext, "No network connection",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mFragmentLoad.onShareFragmentLoad(this);
	}

	public void loginToFacebook() {
		mSharedPreferences = mContext.getSharedPreferences(null,
				Context.MODE_PRIVATE);
		String access_token = mSharedPreferences.getString(
				"access_token_facebook1", null);
		long expires = mSharedPreferences
				.getLong("access_expires_facebook1", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			Log.e("", "Facebook:IF");

			facebook.authorize(MainActivity.mActivity, new String[] { "email",
					"publish_stream" }, Facebook.FORCE_DIALOG_AUTH,
					new DialogListener() {

						@Override
						public void onCancel() {
							// Function to handle cancel event
						}

						@Override
						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = mSharedPreferences
									.edit();
							editor.putString("access_token_facebook1",
									facebook.getAccessToken());
							editor.putLong("access_expires_facebook1",
									facebook.getAccessExpires());
							editor.commit();

							pDialog = new ProgressDialog(mContext);
							pDialog.setMessage("Posting on Facebook...");
							pDialog.setCancelable(false);
							pDialog.show();

							// postToWall();
							PostingOnFacebookTask postingOnFacebookTask = new PostingOnFacebookTask(
									pDialog);
							postingOnFacebookTask.execute();
						}

						@Override
						public void onError(DialogError error) {
							// Function to handle error
						}

						@Override
						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors
						}
					});
		} else {
			Log.e("", "Facebook:ELSE");
			// postToWall();

			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Posting on Facebook...");
			pDialog.setCancelable(false);
			pDialog.show();
			PostingOnFacebookTask postingOnFacebookTask = new PostingOnFacebookTask(
					pDialog);
			postingOnFacebookTask.execute();
		}
	}

	public void postToWall() {
		// post on user's wall.

		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		Bundle parameters = new Bundle();
		parameters
				.putString("link",
						"https://play.google.com/store/apps/details?id=co.uk.peeki");
		parameters.putString("message",
				"I’m using Peeki. You can download it here...");
		parameters.putString("description",
				" Peeki – Private Eye Photo Lock for Android!");

		// parameters.putString("message",
		// "Peeki" + " " + formatter.format(currentDate.getTime()));
		// parameters.putString("message",
		// category_item_name_desc.getText().toString());
		// parameters.putString("picture",
		// getResources().getDrawable(R.drawable.logo));
		// parameters.putString("caption",
		// txtDescription_desc.getText().toString());
		// facebook.request("/me/feed", parameters, "POST");

		// Toast.makeText(mContext, "Message posted successfully!!",
		// Toast.LENGTH_LONG).show();
		try {
			facebook.request("me/feed", parameters, "POST");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class PostingOnFacebookTask extends AsyncTask<Void, Void, Void> {

		ProgressDialog dialog;

		public PostingOnFacebookTask(ProgressDialog dialog) {
			// TODO Auto-generated constructor stub
			this.dialog = dialog;
		}

		protected void onPreExecute() {
		}

		protected void onPostExecute(Void Result) {
			if (dialog != null)
				dialog.dismiss();

			Toast.makeText(mContext,
					"Message posted on Facebook successfully.",
					Toast.LENGTH_LONG).show();
			// Goes to selection screen
			try {
				Fragment selectionFragment = SelectionFragment.newInstance(
						mContext, mFragmentLoad);
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.main_fragment, selectionFragment,
								"SelectionFragment")
						.setTransition(
								FragmentTransaction.TRANSIT_FRAGMENT_FADE)
						.addToBackStack(null).commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			postToWall();
			return null;
		}
	}
}
