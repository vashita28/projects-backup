package co.uk.pocketapp.bridgestone.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.pocketapp.bridgestone.R;
import co.uk.pocketapp.bridgestone.util.AppValues;
import co.uk.pocketapp.bridgestone.util.Util;

public class LoginActivity extends ParentActivity {

	Button btnLogin;
	EditText userIDText;
	View viewTopbar;
	TextView txtLogin, txtUser, txtUserIdTitle, txtPreferences, txtLanguage,
			txtPressure, txtVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		viewTopbar = (View) findViewById(R.id.loginTopbar);
		super.initWidgets(false, viewTopbar);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		txtLogin = (TextView) findViewById(R.id.txtLogin);
		txtUser = (TextView) findViewById(R.id.txtUser);
		txtUserIdTitle = (TextView) findViewById(R.id.txtUserIdTitle);
		txtPreferences = (TextView) findViewById(R.id.txtPreferences);
		txtLanguage = (TextView) findViewById(R.id.txtLanguage);
		txtPressure = (TextView) findViewById(R.id.txtPressure);
		txtVersion = (TextView) findViewById(R.id.txtVersion);

		if (AppValues.bIsDebug)
			txtVersion.setText("v "
					+ getResources().getString(R.string.app_version));

		userIDText = (EditText) findViewById(R.id.etEnterUserID);
		userIDText.append(Util.getUserID(LoginActivity.this));

		userIDText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					userIDText.setCursorVisible(false);
					return true;
				}
				return false;
			}
		});

		userIDText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable et) {
				// TODO Auto-generated method stub
				String value = et.toString();
				if (!value.equals(value.toUpperCase())) {
					value = value.toUpperCase();
					userIDText.setText(value);
					userIDText.setSelection(et.length());
				}
			}
		});

		InputFilter[] FilterArray = new InputFilter[2];
		FilterArray[0] = new InputFilter.LengthFilter(6);
		userIDText.setFilters(FilterArray);

		InputFilter alphaNumericFilter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub

				for (int k = start; k < end; k++) {
					if (!Character.isLetterOrDigit(source.charAt(k))) {
						return "";
					}
				}
				return null;
			}

		};
		FilterArray[1] = alphaNumericFilter;
		userIDText.setFilters(FilterArray);

		setLanguageAndPressureAdapter();

		// ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
		// R.layout.spinner_item, getResources().getStringArray(
		// R.array.languagesArray));
		// spinnerLanguage.setAdapter(spinnerAdapter);

		// spinnerAdapter = new ArrayAdapter<String>(this,
		// R.layout.spinner_item,
		// getResources().getStringArray(R.array.pressuremeasurement));
		// spinnerPressureMeasurement.setAdapter(spinnerAdapter);

		// ((TextView)
		// spinnerLanguage.getChildAt(0)).setTextColor(getResources()
		// .getColor(R.color.white));
		// ((TextView) spinnerPressureMeasurement.getChildAt(0))
		// .setTextColor(getResources().getColor(R.color.white));

	}

	public void onLoginClick(View view) {

		if (userIDText.getText().toString().length() > 0) {
			if (userIDText.getText().toString().length() < 6) {
				Toast.makeText(
						LoginActivity.this,
						getResources().getString(
								R.string.user_id_invalid_length),
						Toast.LENGTH_LONG).show();
				return;
			}

			Pattern numberPat = Pattern.compile("\\d+");
			Matcher matcher1 = numberPat.matcher(userIDText.getText()
					.toString());

			Pattern stringPat = Pattern.compile("[a-zA-Z]");
			Matcher matcher2 = stringPat.matcher(userIDText.getText()
					.toString());

			if (matcher2.find() && matcher1.find()) {

				// if (userIDText.getText().toString().contains("[0-9]")
				// && userIDText.getText().toString().contains("[a-zA-Z]")) {
				Util.setUserID(LoginActivity.this, userIDText.getText()
						.toString().toUpperCase());
				finish();
				Intent mainIntent = new Intent(this, MainActivity.class);
				startActivity(mainIntent);
				// final Intent sensorDetailsIntent = new Intent(this,
				// SensorDetailsActivity.class);
				// sensorDetailsIntent.putExtra("isTotal", true);
				// startActivity(sensorDetailsIntent);
			} else {
				Toast.makeText(
						LoginActivity.this,
						getResources().getString(
								R.string.user_id_must_contain_alphanumberic),
						Toast.LENGTH_LONG).show();
			}

		} else {
			Toast.makeText(LoginActivity.this,
					getResources().getString(R.string.user_id_error),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	void updateLocale() {
		// TODO Auto-generated method stub
		updateWidgets();
	}

	void updateWidgets() {
		Log.i("LoginActivity", "updateWidgets");
		btnLogin.setText(getResources().getString(R.string.btnLogin));
		txtLogin.setText(getResources().getString(R.string.login));
		txtUser.setText(getResources().getString(R.string.user));
		txtUserIdTitle
				.setText(getResources().getString(R.string.enter_user_id));
		txtPreferences.setText(getResources().getString(R.string.preferences));
		txtLanguage.setText(getResources().getString(R.string.language));
		txtPressure.setText(getResources().getString(
				R.string.pressuremeasurement));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AlertDialog quitAlertDialog = new AlertDialog.Builder(
				LoginActivity.this)
				.setTitle(getResources().getString(R.string.exit))
				.setMessage(getResources().getString(R.string.confirm_exit))
				.setPositiveButton(getResources().getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {
								finish();

							}
						})
				.setNegativeButton(getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface argDialog,
									int argWhich) {
							}
						}).create();
		quitAlertDialog.setCanceledOnTouchOutside(false);
		quitAlertDialog.show();
		return;
	}
}
