package co.uk.peeki.ui;

import co.uk.peeki.Photo_Application;
import co.uk.peeki.R;
import co.uk.peeki.adapter.ViewTutorialAdapter;
import co.uk.peeki.fragments.SelectionFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.TextView;

public class ViewTutorialActivity extends Activity {

	TextView tvSkip, tvNext, tvPosition1, tvPosition2, tvPosition3;
	int position;
	Gallery gallery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_tutorial_fragment);

		tvSkip = (TextView) findViewById(R.id.tvSkip);
		tvNext = (TextView) findViewById(R.id.tvNext);
		tvPosition1 = (TextView) findViewById(R.id.tvPosition1);
		tvPosition2 = (TextView) findViewById(R.id.tvPosition2);
		tvPosition3 = (TextView) findViewById(R.id.tvPosition3);
		gallery = (Gallery) findViewById(R.id.gallery);

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
					tvNext.setText("NEXT");
					tvPosition1.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.textshape_selected));
				} else if (pos == 1) {
					tvNext.setText("NEXT");
					tvPosition2.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.textshape_selected));
				} else if (pos == 2) {
					tvNext.setText("FINISH");
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
				finish();

			}
		});

		tvNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (position < 2)
					gallery.setSelection(position + 1);
				if (position == 2)
					finish();
			}
		});

	}

	void resetTextBackground() {
		tvPosition1.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.textshape_unselected));
		tvPosition2.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.textshape_unselected));
		tvPosition3.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.textshape_unselected));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		return;
	}

}
