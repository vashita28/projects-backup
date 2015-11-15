package com.android.cabapp.activity;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cabapp.R;
import com.android.cabapp.adapter.ViewTutorialAdapter;
import com.android.cabapp.model.UserAuthorisation;
import com.android.cabapp.util.Constants;
import com.android.cabapp.util.Util;

public class TutorialActivity extends RootActivity {
	private static final String TAG = TutorialActivity.class.getSimpleName();

	ImageView ivBack;
	Context _context;
	TextView txtEdit;
	int position;
	Gallery gallery;

	private ProgressDialog dialogTutorial;

	String eMail = "";
	Bundle mBundle;

	boolean isFromMyAccount = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		_context = this;
		mBundle = getIntent().getExtras();

		eMail = Util.getEmailOrUserName(TutorialActivity.this);
		if (Constants.isDebug)
			Log.e(TAG, "eMail::>  " + eMail);

		if (mBundle != null && mBundle.containsKey("isFromMyAccount"))
			isFromMyAccount = mBundle.getBoolean("isFromMyAccount");

		// if (mBundle != null
		// && mBundle
		// .containsKey(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS)) {
		// isFromMyAccount = mBundle
		// .getBoolean(Constants.FROM_EDIT_MY_ACC_VECHILE_DETAILS);
		// } else if (mBundle != null
		// && mBundle
		// .containsKey(Constants.FROM_EDIT_MY_ACC_PERSONAL_DETAILS)) {
		// isFromMyAccount = mBundle
		// .getBoolean(Constants.FROM_EDIT_MY_ACC_PERSONAL_DETAILS);
		// }
		txtEdit = (TextView) findViewById(R.id.tvEdit);
		txtEdit.setVisibility(View.GONE);

		gallery = (Gallery) findViewById(R.id.gallery);
		gallery.setAdapter(new ViewTutorialAdapter(_context));

		ivBack = (ImageView) findViewById(R.id.ivBack);
		if (isFromMyAccount) {
			mContext = this;
			ivBack.setVisibility(View.VISIBLE);
		} else
			ivBack.setVisibility(View.GONE);

		ivBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!isFromMyAccount) {
					MainActivity.finishActivity();
					SignUp_DriverDetails_Activity.finishActivity();
					SignUp_VehicleDetails_Activity.finishActivity();
					SignUp_AboutYou_Activity.finishActivity();
					SignUp_YourAccount_Activity.finishActivity();
				}
				finish();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!isFromMyAccount) {
			dialogTutorial = new ProgressDialog(_context);
			dialogTutorial
					.setMessage("Checking account status! Please wait...");
			dialogTutorial.setCancelable(false);
			dialogTutorial.show();
			AuthorisedTask authoriseTask = new AuthorisedTask();
			authoriseTask.email = eMail;
			authoriseTask.execute();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (!isFromMyAccount) {
			MainActivity.finishActivity();
			SignUp_DriverDetails_Activity.finishActivity();
			SignUp_VehicleDetails_Activity.finishActivity();
			SignUp_YourAccount_Activity.finishActivity();
			SignUp_AboutYou_Activity.finishActivity();

			finish();
		} else {
			finish();
		}
	}

	public class AuthorisedTask extends AsyncTask<String, Void, String> {
		public String email;

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			UserAuthorisation userAuthorisation = new UserAuthorisation(
					_context, email);
			String response = userAuthorisation.IsUserAuthorised();
			try {
				JSONObject jObject = new JSONObject(response);

				if (jObject.has("driverId")) {
					Util.setDriverID(TutorialActivity.this,
							jObject.getString("driverId"));
				}

				if (jObject.has("document")) {
					Util.setIsDocumentsUploaded(TutorialActivity.this,
							jObject.getBoolean("document"));
				}

				if (jObject.has("status")
						&& jObject.getString("status").equals("ap")) {
					return "approved";
				} else {
					Util.setAccessToken(_context, "");
					if (jObject.has("status")) {
						if (jObject.getString("status").equals("pn")) {
							return "pending";
						} else if (jObject.getString("status").equals("dc")
								|| jObject.getString("status").equals("sp")
								|| jObject.getString("status").equals("dl")) {
							return "failed";
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (dialogTutorial != null)
				dialogTutorial.dismiss();

			if (result.equals("approved")) {
				Util.showToastMessage(_context, "Authorisation successful",
						Toast.LENGTH_LONG);
				Util.setIsAuthorised(_context, true);
				Util.finishActivity(_context);
				Intent intent1 = new Intent(_context, LogInActivity.class);
				intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent1);

			} else if (result.equals("pending")) {
				Util.setIsAuthorised(_context, false);
				Util.showToastMessage(_context,
						"Your account is awaiting approval", Toast.LENGTH_LONG);// Your
																				// request
																				// is
																				// still
																				// pending
				if (!Util.getIsDocumentsUploaded(_context)) {
					Util.finishActivity(_context);
					Intent documentUploadIntent = new Intent(_context,
							DocumentUploadActivity.class);
					documentUploadIntent
							.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(documentUploadIntent);
				}

			} else if (result.equals("failed")) {
				Util.showToastMessage(_context,
						"Your request has been declined", Toast.LENGTH_LONG);
				Util.finishActivity(_context);
				Intent intent1 = new Intent(_context, LandingActivity.class);
				intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent1);
			}

		}
	}

}
