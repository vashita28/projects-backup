package co.uk.peeki.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import co.uk.peeki.Photo_Application;
import co.uk.peeki.R;
import co.uk.peeki.adapter.ViewTutorialAdapter;
import co.uk.peeki.interfaces.FragmentLoad;
import co.uk.peeki.ui.MainActivity;

public class ViewTutorialFragment extends Fragment {
	private static final String TAG = "VIEW TUTORIAL FRAGMENT";

	static Context mContext;

	static FragmentLoad mFragmentLoad;
	TextView tvSkip, tvNext, tvPosition1, tvPosition2, tvPosition3;
	Gallery gallery;
	int position;

	public static Fragment newInstance(Context context,
			FragmentLoad mfragmentLoad) {
		mContext = context;
		ViewTutorialFragment f = new ViewTutorialFragment();
		mFragmentLoad = mfragmentLoad;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.view_tutorial_fragment, null, true);
		tvSkip = (TextView) root.findViewById(R.id.tvSkip);
		tvNext = (TextView) root.findViewById(R.id.tvNext);
		gallery = (Gallery) root.findViewById(R.id.gallery);
		tvPosition1 = (TextView) root.findViewById(R.id.tvPosition1);
		tvPosition2 = (TextView) root.findViewById(R.id.tvPosition2);
		tvPosition3 = (TextView) root.findViewById(R.id.tvPosition3);

		tvSkip.setTypeface(Photo_Application.font_SemiBold);
		tvNext.setTypeface(Photo_Application.font_SemiBold);

		gallery.setAdapter(new ViewTutorialAdapter(MainActivity.mContext));

		gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				position = pos;
				resetTextBackground();
				if (pos == 0) {
					tvPosition1.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.textshape_selected));
				} else if (pos == 1) {
					tvPosition2.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.textshape_selected));
				} else if (pos == 2) {
					tvPosition3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.textshape_selected));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		tvSkip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		tvNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (position < 2)
					gallery.setSelection(position + 1);
			}
		});

		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mFragmentLoad.onViewTutorialFragmentLoad(this);
	}

	void resetTextBackground() {
		tvPosition1.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.textshape_unselected));
		tvPosition2.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.textshape_unselected));
		tvPosition3.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.textshape_unselected));
	}
}
