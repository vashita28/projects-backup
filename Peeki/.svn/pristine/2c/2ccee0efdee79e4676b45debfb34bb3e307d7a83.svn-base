package co.uk.peeki.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.peeki.Photo_Application;
import co.uk.peeki.R;
import co.uk.peeki.adapter.PageViewerImageAdapter;

public class ShowSelectionActivity extends Activity {
	private static final String TAG = "SHOW SELECTION ACTIVITY";

	// ImageView imageViewBack;
	ViewPager viewPagerShowSelection;
	PageViewerImageAdapter adapter;
	private Context mContext;
	Button btnConfirm;
	TextView tvDone, tvNoImagesToDisplay, tvsetPasscodeAlert;
	EditText etPassword;

	public static SharedPreferences sharedpreferences;
	Editor editor;
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String IS_PASSWORD_SET = "IsPasswordSet";
	public static final String PASSWORD = "password";
	public static boolean isbPasswordSet = false;

	public static boolean isbFirstTime = true;
	InputMethodManager inputMethodManager;
	// Dialog dialog;
	// Window window;
	// WindowManager.LayoutParams wLayoutParams;

	boolean bIsDialogShown = false;
	int nImagePosition = 0;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_selection);
		mContext = this;
		// imageViewBack = (ImageView) findViewById(R.id.iv_back);
		viewPagerShowSelection = (ViewPager) findViewById(R.id.pagerShowSelection);
		tvDone = (TextView) findViewById(R.id.tvDoneShowSelection);
		tvDone.setTypeface(Photo_Application.font_SemiBold);
		tvNoImagesToDisplay = (TextView) findViewById(R.id.tvNoImagesToScroll);
		tvNoImagesToDisplay.setTypeface(Photo_Application.font_SemiBold);

		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		sharedpreferences = getSharedPreferences(MyPREFERENCES,
				Context.MODE_PRIVATE);

		if (isbFirstTime) {
			isbPasswordSet = sharedpreferences.getBoolean(IS_PASSWORD_SET,
					false);
			isbFirstTime = false;
		}

		final ArrayList<String> myList = (ArrayList<String>) getIntent()
				.getSerializableExtra("mylist");
		if (myList.size() > 0) {
			tvNoImagesToDisplay.setVisibility(View.GONE);
		} else {
			tvNoImagesToDisplay.setVisibility(View.VISIBLE);
		}

		adapter = new PageViewerImageAdapter(ShowSelectionActivity.this, myList);
		viewPagerShowSelection.setAdapter(adapter);
		viewPagerShowSelection.setCurrentItem(nImagePosition);

		viewPagerShowSelection
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int state) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
						// TODO Auto-generated method stub
						nImagePosition = position;
						if (myList != null && myList.size() > 0
								&& position <= myList.size()) {
							// Log.e("ShowSelection()",
							// "onPageChange Image is :: "
							// + myList.get(position));
						}

					}

					@Override
					public void onPageScrollStateChanged(int position) {
						// TODO Auto-generated method stub

					}
				});

		// imageViewBack.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// // Intent intent = new Intent(ShowSelectionActivity.this,
		// // MainActivity.class);
		// // startActivity(intent);
		// finish();
		//
		// }
		// });

		tvDone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
	}

	void showDialog() {

		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.dialog_password);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);

		etPassword = (EditText) dialog.findViewById(R.id.etPassword);
		btnConfirm = (Button) dialog.findViewById(R.id.btnSavePassword);
		tvsetPasscodeAlert = (TextView) dialog.findViewById(R.id.tvsetPasscode);

		etPassword.setFilters(new InputFilter[] { filter });

		tvsetPasscodeAlert.setTypeface(Photo_Application.font_Regular);
		etPassword.setHintTextColor(getResources().getColor(
				R.color.edittext_hintpasscode));

		if (!isbPasswordSet) {
			tvsetPasscodeAlert.setVisibility(View.VISIBLE);
			dialog.setTitle("Please set your Passcode");
			btnConfirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					isbPasswordSet = true;
					if (etPassword.getText().length() > 0) {// &&
															// !etPassword.getText().toString().trim()
						// .equals("")
						if (etPassword.getText().length() > 5
								&& etPassword.getText().length() < 17) {
							editor = sharedpreferences.edit();
							editor.putBoolean(IS_PASSWORD_SET, true);
							editor.putString(PASSWORD, etPassword.getText()
									.toString());
							editor.commit();
							dialog.dismiss();
							bIsDialogShown = false;
						} else {
							Toast.makeText(
									ShowSelectionActivity.this,
									"Passcode should be minimum 6 and maximum 16 characters long.",
									Toast.LENGTH_LONG).show();
						}

					} else {
						Toast.makeText(getBaseContext(),
								"Field cannot be empty", Toast.LENGTH_LONG)
								.show();
					}
				}
			});
			dialog.show();

		} else {
			tvsetPasscodeAlert.setVisibility(View.GONE);
			etPassword.setText("");
			dialog.setTitle("Please enter Passcode");
			btnConfirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (etPassword.getText().length() > 0) {
						if (sharedpreferences.contains(PASSWORD)) {
							String getPassword = sharedpreferences.getString(
									PASSWORD, "");
							if (etPassword.getText().toString()
									.equals(getPassword)) {
								dialog.dismiss();
								bIsDialogShown = false;
								// finish activity
								finish();

							} else {
								Toast.makeText(getBaseContext(),
										"Please enter correct passcode.",
										Toast.LENGTH_LONG).show();
							}
						}
					} else {
						Toast.makeText(getBaseContext(),
								"Field cannot be empty", Toast.LENGTH_LONG)
								.show();
					}
				}
			});
			dialog.show();
		}
		bIsDialogShown = true;
		inputMethodManager.hideSoftInputFromWindow(tvDone.getWindowToken(), 0);
	}

	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// // TODO Auto-generated method stub
	// int currentOrientation = getResources().getConfiguration().orientation;
	// if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
	// wLayoutParams.screenOrientation = Configuration.ORIENTATION_PORTRAIT;
	// } else {
	// wLayoutParams.screenOrientation = Configuration.ORIENTATION_LANDSCAPE;
	// }
	// super.onConfigurationChanged(newConfig);
	// }

	public InputFilter filter = new InputFilter() {
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			for (int i = start; i < end; i++) {
				if (!Character.isLetterOrDigit(source.charAt(i))) {
					return "";
				}
			}
			return null;
		}
	};

	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isDialogShown", bIsDialogShown);
		outState.putInt("image_position", nImagePosition);
		super.onSaveInstanceState(outState);
	};

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState != null) {
			// Restore last state for checked position.
			bIsDialogShown = savedInstanceState.getBoolean("isDialogShown",
					false);
			nImagePosition = savedInstanceState.getInt("image_position", 0);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (bIsDialogShown) {
			showDialog();
		}
	}

}
