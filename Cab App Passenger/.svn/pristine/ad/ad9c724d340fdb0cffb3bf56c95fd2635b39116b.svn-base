package com.example.cabapppassenger.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.receivers.NetworkBroadcastReceiver;
import com.example.cabapppassenger.task.GetAddressTask;
import com.example.cabapppassenger.task.GetAutoPlacesTask;
import com.example.cabapppassenger.task.GetPincodeTask;
import com.example.cabapppassenger.task.SignupTask;
import com.example.cabapppassenger.util.Constant;
import com.example.cabapppassenger.util.Util;

public class SignupActivity extends Activity {

	static Context mContext;
	ImageView iv_menu;
	EditText et_firstname, et_lastname, et_email, et_password, et_confirmpass;
	List<Address> list_postcode;
	Handler mHandler, mhandler_mobileno;
	Geocoder geocoder;
	ProgressDialog mDialog;
	Location currentLocation = null;
	double latitude, longitude;
	View activityRootView;
	GetPincodeTask pincodeTask;
	public AutoCompleteTextView atvCity_Address;
	String country = null, placeId = null;
	TextView tv_next, tv_hidden_next, tv_vicinity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signup);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		tv_next = (TextView) findViewById(R.id.tvNext);
		tv_vicinity = (TextView) findViewById(R.id.tv_vicinity);
		tv_hidden_next = (TextView) findViewById(R.id.tv_hidden_next);
		activityRootView = findViewById(R.id.rel_main);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						// r will be populated with the coordinates of your view
						// that area still visible.
						activityRootView.getWindowVisibleDisplayFrame(r);

						int heightDiff = activityRootView.getRootView()
								.getHeight() - (r.bottom - r.top);
						if (heightDiff > 100) { // if more than 100 pixels, its
							// probably a keyboard...
							// Log.e("Visible", "Visible");
							tv_hidden_next.setVisibility(View.VISIBLE);
							tv_next.setVisibility(View.GONE);
						} else {
							// Log.e("Not visible", "not visible");
							tv_hidden_next.setVisibility(View.GONE);
							tv_next.setVisibility(View.VISIBLE);
						}
					}
				});

		View.OnClickListener onclick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {

				case R.id.tv_hidden_next:
					check_fields(v);
					break;

				case R.id.tvNext:
					check_fields(v);
					break;
				}
			}
		};
		tv_next.setOnClickListener(onclick);
		tv_hidden_next.setOnClickListener(onclick);
		mContext = this;
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			if (Util.getLocation(mContext) != null)
				currentLocation = Util.getLocation(mContext);
		} else
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();
		mContext = this;
		Intent intent_location = getIntent();
		if (intent_location != null) {
			latitude = intent_location.getDoubleExtra("Latitude", 0.0);
			longitude = intent_location.getDoubleExtra("Longitude", 0.0);
		}
		geocoder = new Geocoder(mContext, Locale.getDefault());
		final LocationManager manager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);

		et_firstname = (EditText) findViewById(R.id.et_firstname);
		et_lastname = (EditText) findViewById(R.id.et_lastname);
		et_email = (EditText) findViewById(R.id.et_email);
		atvCity_Address = (AutoCompleteTextView) findViewById(R.id.et_city);
		et_password = (EditText) findViewById(R.id.et_password);
		et_confirmpass = (EditText) findViewById(R.id.et_confirmpassword);

		iv_menu = (ImageView) findViewById(R.id.ivBack);
		iv_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				hideKeybord(v);
			}
		});
		atvCity_Address.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				atvCity_Address.setSelection(s.length(), s.length());
				GetAutoPlacesTask placesTask = new GetAutoPlacesTask(mContext);
				placesTask.execute(s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		atvCity_Address.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HashMap<String, String> selected = (HashMap<String, String>) parent
						.getItemAtPosition(position);
				atvCity_Address.setText(selected.get("description"));
				placeId = selected.get("placeId");

				pincodeTask = new GetPincodeTask(SignupActivity.this, placeId,
						"atvPickUpPlaces", mHandler);
				pincodeTask.execute();

			}

		});
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg != null && msg.peekData() != null) {
					Bundle bundle = (Bundle) msg.obj;
					if (bundle.containsKey("Country")) {
						country = bundle.getString("Country");
						if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
							signup();
						} else {
							Toast.makeText(SignupActivity.this,
									"No network connection", Toast.LENGTH_SHORT)
									.show();
						}
					}

					if (bundle.containsKey("City")) {
						tv_vicinity.setVisibility(View.VISIBLE);
						tv_vicinity.setText(bundle.getString("City"));
						atvCity_Address.setVisibility(View.INVISIBLE);
					}

				}

			}

		};

		tv_vicinity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tv_vicinity.setVisibility(View.GONE);
				atvCity_Address.setVisibility(View.VISIBLE);
				atvCity_Address.setText(tv_vicinity.getText().toString());
				atvCity_Address.requestFocus();
			}
		});
		mhandler_mobileno = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {

					Intent mobile = new Intent(mContext, MobileNoActivity.class);
					mobile.putExtra("FirstName", et_firstname.getText()
							.toString());
					mobile.putExtra("LastName", et_lastname.getText()
							.toString());
					mobile.putExtra("City", atvCity_Address.getText()
							.toString());
					mobile.putExtra("Password",
							Constant.md5(et_password.getText().toString()));
					mobile.putExtra("Email", et_email.getText().toString());
					mobile.putExtra("Country", country);
					mobile.putExtra("Latitude", latitude);
					mobile.putExtra("Longitude", longitude);
					startActivity(mobile);
				}
			}

		};

		// tv_next.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// if (et_firstname.getText().toString().length() > 0
		// && et_lastname.getText().toString().length() > 0
		// && et_email.getText().toString().length() > 0
		// && atvCity_Address.getText().toString().length() > 0
		// && et_password.getText().toString().length() > 0
		// && et_confirmpass.getText().toString().length() > 0) {
		//
		// if (Constant.isEmailAddressValid(et_email.getText()
		// .toString())) {
		// if (et_password.length() >= 6) {
		//
		// if (et_password
		// .getText()
		// .toString()
		// .equals(et_confirmpass.getText().toString())) {
		//
		// if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
		// if (country != null
		// && country.length() <= 0) {
		// GetAddressTask task = new GetAddressTask(
		// mContext);
		// Log.e("Latitude and Longitude", ""
		// + latitude + longitude);
		// task.latitude = latitude;
		// task.longitude = longitude;
		// task.flag_hail = false;
		// task.mHandler = mHandler;
		// task.execute("");
		// hideKeybord(v);
		// } else {
		// signup();
		// }
		// } else {
		// Toast.makeText(mContext,
		// "No network connection",
		// Toast.LENGTH_SHORT).show();
		// }
		//
		// } else {
		// Constant.alertdialog("passwordnotmatches",
		// mContext);
		// }
		//
		// } else {
		// Constant.alertdialog("passwordlessthan", mContext);
		// }
		// } else {
		// Constant.alertdialog("invalidemail", mContext);
		// }
		//
		// } else {
		// Constant.alertdialog("emptyfields", mContext);
		// }
		// }
		// });
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
			if (Util.getLocation(mContext) != null)
				currentLocation = Util.getLocation(mContext);
		} else
			Toast.makeText(mContext, "No network connection",
					Toast.LENGTH_SHORT).show();

	}

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(iv_menu.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	public void check_fields(View v) {
		hideKeybord(v);
		if (et_firstname.getText().toString().length() > 0
				&& et_lastname.getText().toString().length() > 0
				&& et_email.getText().toString().length() > 0
				&& atvCity_Address.getText().toString().length() > 0
				&& et_password.getText().toString().length() > 0
				&& et_confirmpass.getText().toString().length() > 0) {

			if (Constant.isEmailAddressValid(et_email.getText().toString())) {
				if (et_password.length() >= 6) {

					if (et_password.getText().toString()
							.equals(et_confirmpass.getText().toString())) {

						if (NetworkBroadcastReceiver.ms_bIsNetworkAvailable) {
							if (country != null && country.length() <= 0) {
								GetAddressTask task = new GetAddressTask(
										mContext);
								Log.e("Latitude and Longitude", "" + latitude
										+ longitude);
								task.latitude = latitude;
								task.longitude = longitude;
								task.flag_hail = false;
								task.mHandler = mHandler;
								task.execute("");
								hideKeybord(v);
							} else {
								signup();
							}
						} else {
							Toast.makeText(mContext, "No network connection",
									Toast.LENGTH_SHORT).show();
						}

					} else {
						Constant.alertdialog("passwordnotmatches", mContext);
					}

				} else {
					Constant.alertdialog("passwordlessthan", mContext);
				}
			} else {
				Constant.alertdialog("invalidemail", mContext);
			}

		} else {
			Constant.alertdialog("emptyfields", mContext);
		}
	}

	public void signup() {
		SignupTask task = new SignupTask(mContext, mDialog, true);
		task.firstname = et_firstname.getText().toString();
		task.lastname = et_lastname.getText().toString();
		task.city = atvCity_Address.getText().toString();
		task.email = et_email.getText().toString();
		task.password = et_password.getText().toString();
		task.country = country;
		task.flag_keepmeloged = true;
		task.mHandler = mhandler_mobileno;
		task.mobile_no = "";
		task.international_code = "";
		task.execute(Constant.passengerURL + "ws/v2/passenger/account/register");
	}
}
