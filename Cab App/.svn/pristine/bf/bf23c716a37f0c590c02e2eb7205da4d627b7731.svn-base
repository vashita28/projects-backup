package com.android.cabapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.adapter.SuburbanAreasAdapter;
import com.android.cabapp.util.AppValues;

public class SuburbanSectorsActivity extends RootActivity {

	ListView lvSubsectors;
	ImageView ivBack;
	TextView tvEdit;
	SuburbanAreasAdapter suburbanAreaAdapter;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suburban_sectors);

		mContext = this;
		lvSubsectors = (ListView) findViewById(R.id.lvSubsectors);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tvEdit = (TextView) findViewById(R.id.tvEdit);
		tvEdit.setVisibility(View.GONE);
		ivBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		if (AppValues.sectorList != null) {
			suburbanAreaAdapter = new SuburbanAreasAdapter(mContext,
					AppValues.sectorList);
			lvSubsectors.setAdapter(suburbanAreaAdapter);
		}

	}

}
