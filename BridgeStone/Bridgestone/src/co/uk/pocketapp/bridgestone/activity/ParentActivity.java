package co.uk.pocketapp.bridgestone.activity;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import co.uk.pocketapp.bridgestone.R;
import co.uk.pocketapp.bridgestone.util.Util;

public class ParentActivity extends Activity {
	private Activity mActivity;

	Spinner spinnerLanguage, spinnerPressureMeasurement;
	public MyAdapter spinnerLanguageAdapter, spinnerPressureMeaurementAdapter;

	ImageView imgSettings;
	private Locale myLocale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mActivity = this;

	}

	protected void initWidgets(boolean bIsSettingsVisible, View viewTopbar) {
		toggleSettings(bIsSettingsVisible, viewTopbar);
	}

	void setLanguageAndPressureAdapter() {
		spinnerLanguage = (Spinner) findViewById(R.id.spinnerLanguage);
		spinnerPressureMeasurement = (Spinner) findViewById(R.id.spinnerPressureMeasurement);

		spinnerLanguageAdapter = new MyAdapter(this, R.layout.spinner_item,
				getResources().getStringArray(R.array.languagesArray));
		spinnerLanguage.setAdapter(spinnerLanguageAdapter);

		spinnerPressureMeaurementAdapter = new MyAdapter(this,
				R.layout.spinner_item, getResources().getStringArray(
						R.array.pressuremeasurementArray));
		spinnerPressureMeasurement.setAdapter(spinnerPressureMeaurementAdapter);

		spinnerLanguage.setSelection(Util.getLanguage(ParentActivity.this));
		onLanguageToggled(Util.getLanguage(ParentActivity.this), false);
		spinnerPressureMeasurement.setSelection(Util
				.getPressureMeasurement(ParentActivity.this));

		spinnerLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				setSpinnerFirstItemBackground(parent);
				Util.setLanguage(ParentActivity.this, position);
				onLanguageToggled(position, true);

				TextView main_text = (TextView) view.findViewById(R.id.txtRow);
				main_text.setText(getResources().getStringArray(
						R.array.languagesArray)[position]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				setSpinnerFirstItemBackground(parent);
			}

		});

		spinnerPressureMeasurement
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						setSpinnerFirstItemBackground(parent);

						int previousPosition = Util
								.getPressureMeasurement(ParentActivity.this);
						float fPressure = 0;
						if (previousPosition != position) {
							Util.setPressureMeasurement(ParentActivity.this,
									position);
							if (Util.getPressureMeasurement(ParentActivity.this) == 0) // Pressure
							// in
							// BAR
							{
								Log.e("MainActivity", "pressure in BAR");

								try {
									// float fPressureInBar = Float
									// .parseFloat(String.format(
									// Locale.ENGLISH,
									// "%.2f",
									// (Float.parseFloat(Util
									// .getPressureFilter(MainActivity.this)) *
									// 0.026) - 4.99));
									fPressure = (float) (Float.parseFloat(Util
											.getPressureFilter(ParentActivity.this)) / 14.5037738007);
								} catch (NumberFormatException e) {

								}

							} else { // Pressure in PSI
								Log.e("MainActivity", "pressure in PSI");
								try {
									// int nPressureInPSI = Integer
									// .parseInt(String.format(
									// Locale.ENGLISH,
									// "%.2f",
									// (Float.parseFloat(Util
									// .getPressureFilter(MainActivity.this)) +
									// 4.99) / 0.026));
									fPressure = (float) (Float.parseFloat(Util
											.getPressureFilter(ParentActivity.this)) * 14.5037738007);
								} catch (NumberFormatException e) {

								}
							}

							Util.setPressureFilter(ParentActivity.this,
									String.valueOf(fPressure));
							onPressureMeasurementValueToggled();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub
						setSpinnerFirstItemBackground(parent);
					}

				});

	}

	void setSpinnerFirstItemBackground(AdapterView<?> parent) {
		parent.getChildAt(0).setBackgroundColor(
				getResources().getColor(android.R.color.transparent));
		((TextView) ((LinearLayout) parent.getChildAt(0))
				.findViewById(R.id.txtRow)).setTextColor(getResources()
				.getColor(R.color.white));
	}

	void onPressureMeasurementValueToggled() {

	}

	void updateLocale() {

	}

	void onLanguageToggled(int languagePosition, boolean bFlag) {
		String language = "en";

		if (languagePosition == 0) { // English
			language = "en";
		} else if (languagePosition == 1) { // French
			language = "fr";
		} else if (languagePosition == 2) { // German
			language = "de";
		} else if (languagePosition == 3) { // Spanish
			language = "es";
		} else if (languagePosition == 4) { // Italian
			language = "it";
		} else if (languagePosition == 5) { // Portigeus
			language = "pt";
		} else if (languagePosition == 6) { // Polish
			language = "pl";
		} else if (languagePosition == 7) { // Czech
			language = "cz";
		} else if (languagePosition == 8) { // Hungarian
			language = "hu";
		}
		// else if (languagePosition == 9) { // Slovak
		// language = "sk";
		// }
		else if (languagePosition == 9) { // Dutch
			language = "nl";
		}

		Log.i("ParentActivity", "onLanguageToggled::  " + language);
		myLocale = new Locale(language);
		Locale.setDefault(myLocale);
		android.content.res.Configuration config = new android.content.res.Configuration();
		config.locale = myLocale;
		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());

		if (bFlag)
			spinnerLanguageAdapter.updateValues(getResources().getStringArray(
					R.array.languagesArray));

		updateLocale();

		// spinnerLanguage.setBackgroundColor(getResources().getColor(
		// android.R.color.holo_purple));
	}

	public class MyAdapter extends ArrayAdapter<String> {
		String[] values;

		public MyAdapter(Context ctx, int resource, String[] objects) {
			super(ctx, resource, objects);
			values = objects;
		}

		public void updateValues(String[] objects) {
			values = objects;
			// notifyDataSetChanged();
		}

		@Override
		public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
			return getCustomView(position, cnvtView, prnt);
		}

		@Override
		public View getView(int pos, View cnvtView, ViewGroup prnt) {
			return getCustomView(pos, cnvtView, prnt);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View mySpinner = inflater.inflate(R.layout.spinner_item, parent,
					false);
			TextView main_text = (TextView) mySpinner.findViewById(R.id.txtRow);

			mySpinner
					.setBackgroundColor(getResources().getColor(R.color.white));
			main_text.setText(values[position]);
			main_text.setTextColor(getResources().getColor(R.color.black));
			return mySpinner;
		}
	}

	public void toggleSettings(boolean bIsSettingsVisible, View view) {
		imgSettings = (ImageView) view.findViewById(R.id.imgSettings);
		// Log.e("ParentActivity", "toggleSettings");
		if (bIsSettingsVisible)
			imgSettings.setVisibility(View.VISIBLE);
		else
			imgSettings.setVisibility(View.INVISIBLE);
	}

	public void onSettingsClick(View v) {

	}

}
