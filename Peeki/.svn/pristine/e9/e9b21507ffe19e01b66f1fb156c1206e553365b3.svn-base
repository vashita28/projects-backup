package co.uk.peeki.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.uk.peeki.Photo_Application;
import co.uk.peeki.R;
import co.uk.peeki.interfaces.FragmentLoad;

public class TermsAndConditionsFragment extends Fragment {
	private static final String TAG = "TERMS AND CONDITIONS FRAGMENT";

	static FragmentLoad mFragmentLoad;
	TextView tvHeading, tvTermsData;

	public static Fragment newInstance(Context context,
			FragmentLoad mfragmentLoad) {
		TermsAndConditionsFragment f = new TermsAndConditionsFragment();
		mFragmentLoad = mfragmentLoad;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.terms_and_conditions_fragment, null, true);
		tvHeading = (TextView) root.findViewById(R.id.textviewHeading);
		tvTermsData = (TextView) root.findViewById(R.id.textviewTerms);

		tvHeading.setTypeface(Photo_Application.font_Light);
		tvTermsData.setTypeface(Photo_Application.font_Regular);
		tvHeading.setText(Html
				.fromHtml(getString(R.string.terms_and_conditions)));
		tvTermsData.setText(Html.fromHtml(getString(R.string.terms_data)));

		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mFragmentLoad.onTermsAndConditionsFragmentLoad(this);
	}
}
