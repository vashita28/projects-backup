package co.uk.peeki.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import co.uk.peeki.Photo_Application;
import co.uk.peeki.R;
import co.uk.peeki.interfaces.FragmentLoad;
import co.uk.peeki.ui.ShowSelectionActivity;

public class ChangePasscodeFragment extends Fragment {
	private static final String TAG = "CHANGE PASSCODE FRAGMENT";

	static Context mContext;
	static FragmentLoad mFragmentLoad;
	TextView tvOldPasscode, tvNewPasscode, tvConfirmPasscode;
	EditText etOldPascode, etNewPasscode, etConfirmNewPasscode;
	RelativeLayout rlOldPasscode;
	Button btnConfirm;
	public static SharedPreferences sharedpreferences;
	Editor editor;
	boolean isPasswordSet;

	public static Fragment newInstance(Context context,
			FragmentLoad mfragmentLoad) {
		mContext = context;
		ChangePasscodeFragment f = new ChangePasscodeFragment();
		mFragmentLoad = mfragmentLoad;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.change_passcode_fragment, null, true);
		rlOldPasscode = (RelativeLayout) root
				.findViewById(R.id.rlEnterPasscodeOld);
		tvOldPasscode = (TextView) root.findViewById(R.id.tvEnterOldPasscode);
		tvNewPasscode = (TextView) root.findViewById(R.id.tvEnterNewPasscode);
		tvConfirmPasscode = (TextView) root
				.findViewById(R.id.tvConfirmNewPasscode);
		etOldPascode = (EditText) root.findViewById(R.id.etEnterPasscodeOld);
		etNewPasscode = (EditText) root.findViewById(R.id.etEnterPasscodeNew);
		etConfirmNewPasscode = (EditText) root
				.findViewById(R.id.etEnterPasscodeConfirm);
		btnConfirm = (Button) root.findViewById(R.id.btnConfirm);

		etOldPascode.setFilters(new InputFilter[] { filter });
		etNewPasscode.setFilters(new InputFilter[] { filter });
		etConfirmNewPasscode.setFilters(new InputFilter[] { filter });

		tvOldPasscode.setTypeface(Photo_Application.font_Light);
		tvNewPasscode.setTypeface(Photo_Application.font_Light);
		tvConfirmPasscode.setTypeface(Photo_Application.font_Light);

		etOldPascode.setHintTextColor(getResources().getColor(
				R.color.edittext_hintpasscode));
		etNewPasscode.setHintTextColor(getResources().getColor(
				R.color.edittext_hintpasscode));
		etConfirmNewPasscode.setHintTextColor(getResources().getColor(
				R.color.edittext_hintpasscode));

		sharedpreferences = mContext.getSharedPreferences(
				ShowSelectionActivity.MyPREFERENCES, Context.MODE_PRIVATE);

		if (ShowSelectionActivity.isbFirstTime) {
			ShowSelectionActivity.isbPasswordSet = sharedpreferences
					.getBoolean(ShowSelectionActivity.IS_PASSWORD_SET, false);
			ShowSelectionActivity.isbFirstTime = false;
		}

		if (ShowSelectionActivity.isbPasswordSet) {
			tvOldPasscode.setVisibility(View.VISIBLE);
			rlOldPasscode.setVisibility(View.VISIBLE);
		} else {
			tvOldPasscode.setVisibility(View.GONE);
			rlOldPasscode.setVisibility(View.GONE);
		}

		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!ShowSelectionActivity.isbPasswordSet) {
					// if passcode not already set
					if (etNewPasscode.getText().length() > 0
							&& etConfirmNewPasscode.getText().length() > 0) {// &&
																				// !etNewPasscode.getText().toString().trim()
						// .equals("")
						// && !etConfirmNewPasscode.getText().toString()
						// .trim().equals("")

						if (etNewPasscode.getText().length() > 5
								&& etConfirmNewPasscode.getText().length() > 5
								&& etNewPasscode.getText().length() < 17
								&& etConfirmNewPasscode.getText().length() < 17) {
							if (etNewPasscode
									.getText()
									.toString()
									.equals(etConfirmNewPasscode.getText()
											.toString())) {
								// set passcode in share preferences
								saveInSharePreferences();

								ShowSelectionActivity.isbPasswordSet = true;
								Toast.makeText(getActivity(),
										"Passcode set.Thank You!",
										Toast.LENGTH_LONG).show();
								// clearing all fields
								etNewPasscode.setText("");
								etConfirmNewPasscode.setText("");
								// Going back to selection fragment
								SelectionFragment selectionFragment = new SelectionFragment();
								FragmentTransaction fragTransaction = getFragmentManager()
										.beginTransaction();
								fragTransaction.replace(R.id.main_fragment,
										selectionFragment, "SelectionFragment");
								fragTransaction.addToBackStack(null);
								fragTransaction.commit();

							} else {
								Toast.makeText(
										getActivity(),
										"Passcode and confirm passcode are not same.",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(
									getActivity(),
									"Passcode should be minimum 6 and maximum 16 characters long.",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getActivity(), "Field cannot be empty.",
								Toast.LENGTH_LONG).show();
					}
				} else {// passcode already set
					if (etOldPascode.getText().length() > 0
							&& etNewPasscode.getText().length() > 0
							&& etConfirmNewPasscode.getText().length() > 0) {// &&
																				// !etOldPascode.getText().toString().trim()
						// .equals("")
						// && !etNewPasscode.getText().toString().trim()
						// .equals("")
						// && !etConfirmNewPasscode.getText().toString()
						// .trim().equals("")

						if (etNewPasscode.getText().length() > 5
								&& etConfirmNewPasscode.getText().length() > 5
								&& etNewPasscode.getText().length() < 17
								&& etConfirmNewPasscode.getText().length() < 17) {

							editor = sharedpreferences.edit();
							String oldPasscode = sharedpreferences.getString(
									ShowSelectionActivity.PASSWORD, null);
							if (etOldPascode.getText().toString()
									.equals(oldPasscode)) {
								if (etNewPasscode
										.getText()
										.toString()
										.equals(etConfirmNewPasscode.getText()
												.toString())) {
									ShowSelectionActivity.isbPasswordSet = true;
									editor.putString(
											ShowSelectionActivity.PASSWORD,
											etNewPasscode.getText().toString());
									editor.commit();
									Toast.makeText(getActivity(),
											"Passcode set.Thank You!",
											Toast.LENGTH_LONG).show();
									// clearing all fields
									etOldPascode.setText("");
									etNewPasscode.setText("");
									etConfirmNewPasscode.setText("");
									// Going back to selection fragment
									SelectionFragment selectionFragment = new SelectionFragment();
									FragmentTransaction fragTransaction = getFragmentManager()
											.beginTransaction();
									fragTransaction.replace(R.id.main_fragment,
											selectionFragment,
											"SelectionFragment");
									fragTransaction.addToBackStack(null);
									fragTransaction.commit();

								} else {
									Toast.makeText(
											getActivity(),
											"Passcode and confirm passcode are not same.",
											Toast.LENGTH_LONG).show();
								}
							} else {
								Toast.makeText(getActivity(),
										"Kindly enter correct old passcode.",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(
									getActivity(),
									"Passcode should be minimum 6 and maximum 16 characters long.",
									Toast.LENGTH_LONG).show();
						}
					} else {
						Toast.makeText(getActivity(), "Field cannot be empty.",
								Toast.LENGTH_LONG).show();
					}
				}

			}
		});

		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mFragmentLoad.onChangePasscodeFragmentLoad(this);
	}

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

	void saveInSharePreferences() {
		editor = sharedpreferences.edit();
		editor.putBoolean(ShowSelectionActivity.IS_PASSWORD_SET, true);
		editor.putString(ShowSelectionActivity.PASSWORD, etNewPasscode
				.getText().toString());
		editor.commit();
	}

}
