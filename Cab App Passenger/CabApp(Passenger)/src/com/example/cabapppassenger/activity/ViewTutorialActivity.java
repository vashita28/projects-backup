package com.example.cabapppassenger.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.adapter.ViewTutorialAdapter;

public class ViewTutorialActivity extends Activity {

	Context mContext;
	TextView tvPosition1, tvPosition2, tvPosition3, tvPosition4, btn_skip;
	Gallery gallery;
	int position;
	ImageView ivBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_viewtutorial);
		mContext = this;
		ivBack = (ImageView) findViewById(R.id.ivBack);
		ivBack.setVisibility(View.GONE);
		btn_skip = (TextView) findViewById(R.id.tvskip);
		gallery = (Gallery) findViewById(R.id.gallery);
		tvPosition1 = (TextView) findViewById(R.id.tvPosition1);
		tvPosition2 = (TextView) findViewById(R.id.tvPosition2);
		tvPosition3 = (TextView) findViewById(R.id.tvPosition3);
		tvPosition4 = (TextView) findViewById(R.id.tvPosition4);

		gallery.setAdapter(new ViewTutorialAdapter(mContext));

		gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				position = pos;
				resetTextBackground();

				if (pos == 0) {
					tvPosition1.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.textshape_selected));
					btn_skip.setText("Skip");
				} else if (pos == 1) {
					tvPosition2.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.textshape_selected));
					btn_skip.setText("Skip");
				} else if (pos == 2) {
					tvPosition3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.textshape_selected));
					btn_skip.setText("Skip");
				} else if (pos == 3) {
					tvPosition4.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.textshape_selected));
					btn_skip.setText("Finish");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		btn_skip.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent into = new Intent(mContext, MainFragmentActivity.class);
				startActivity(into);
				finish();
				hideKeybord(v);

			}
		});
	}

	@SuppressWarnings("deprecation")
	void resetTextBackground() {
		tvPosition1.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.textshape_unselected));
		tvPosition2.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.textshape_unselected));
		tvPosition3.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.textshape_unselected));
		tvPosition4.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.textshape_unselected));
	}

	public void hideKeybord(View view) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}
}
