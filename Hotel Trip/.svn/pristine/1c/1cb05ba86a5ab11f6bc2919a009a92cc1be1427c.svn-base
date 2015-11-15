package com.hoteltrip.android;

import java.util.Locale;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.hoteltrip.android.util.Const;

public class OptionsActivity extends SherlockPreferenceActivity {

	private String[] currencies;
	private String[] languages;
	private String[] unitConversion;
	private ListPreference currencyListPref;
	private ListPreference languageListPref;
	private ListPreference unitListPref;
	private Locale locale;
	private Locale[] availableLocales;
	TextView currency,language,conversion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currencies = getResources().getStringArray(R.array.array_currencies);
		languages = getResources().getStringArray(R.array.array_languages);
		unitConversion = getResources().getStringArray(
				R.array.array_distance_unit);
		setPreferenceScreen(createPreferenceHierarchy());

		locale = Locale.getDefault();

		availableLocales = Locale.getAvailableLocales();

		for (Locale locale : availableLocales) {
			Log.d(getClass().getName(), locale.toString());
		}
	}

	private PreferenceScreen createPreferenceHierarchy() {
		// Root
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(
				this);

		// List preference
		currency = new TextView(this);
		currencyListPref = new ListPreference(this);
		currencyListPref.setKey(Const.currencyPrefKey);
		currencyListPref.setTitle("Currency");
		currencyListPref.setEntries(currencies);
		currencyListPref.setEntryValues(currencies);
		currencyListPref.setDefaultValue(currencies[0]);
		currencyListPref.setSummary(currencies[0]);
		currencyListPref.setOnPreferenceChangeListener(currencyChangedListener);

		// add listpref to root
		root.addPreference(currencyListPref);

		languageListPref = new ListPreference(this);
		languageListPref.setKey(Const.languagePrefKey);
		languageListPref.setTitle(R.string.title_pref_language);
		languageListPref.setEntries(languages);
		languageListPref.setEntryValues(languages);
		languageListPref.setDefaultValue(languages[0]);
		languageListPref.setSummary(languages[0]);
		languageListPref.setOnPreferenceChangeListener(languageChangedListener);

		root.addPreference(languageListPref);

		unitListPref = new ListPreference(this);
		unitListPref.setKey(Const.unitPrefKey);
		unitListPref.setTitle(R.string.title_pref_distance_unit);
		unitListPref.setEntries(unitConversion);
		unitListPref.setEntryValues(unitConversion);
		unitListPref.setDefaultValue(unitConversion[0]);
		unitListPref.setSummary(unitConversion[0]);
		unitListPref.setOnPreferenceChangeListener(unitChangedListener);
		root.addPreference(unitListPref);

		return root;
	}

	OnPreferenceChangeListener currencyChangedListener = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			currencyListPref.setSummary(newValue.toString());
			return true;
		}
	};

	OnPreferenceChangeListener languageChangedListener = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			languageListPref.setSummary(newValue.toString());
			return true;
		}
	};

	OnPreferenceChangeListener unitChangedListener = new OnPreferenceChangeListener() {

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			unitListPref.setSummary(newValue.toString());
			return true;
		}
	};

}
