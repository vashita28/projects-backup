package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.android.lldc.HomeActivityTablet;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.util.Util;

public class StaticTrailsFragment extends Fragment {
	private static final String TAG = StaticTrailsFragment.class
			.getSimpleName();

	TextView place_name, txt_eventdetails;
	ImageView img_imagedetls;
	String pageTitle;
	ImageLoader imageLoader;
	boolean bIsTablet = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = null;
		if (Util.getIsTabletFlag(getActivity())) {
			Log.e(TAG, "Device is Tablet...");
			// getParentFragment().getChildFragmentManager().popBackStack();
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			rootView = inflater.inflate(R.layout.fragment_static_trails_tablet,
					container, false);
			bIsTablet = true;
			Bundle bundle = getArguments();
			pageTitle = bundle.getString("PAGETITLE");
		} else {
			Log.e(TAG, "Device is Phone...");
			rootView = inflater.inflate(R.layout.fragment_static_trails,
					container, false);
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			bIsTablet = false;
			pageTitle = getActivity().getIntent().getExtras()
					.getString("PAGETITLE");

		}
		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText(pageTitle);
		((HomeActivityTablet) getActivity()).ivBack.setVisibility(View.VISIBLE);
		((HomeActivityTablet) getActivity()).rlBackBtn.setEnabled(true);
		((HomeActivityTablet) getActivity()).rlBackBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getParentFragment().getChildFragmentManager()
								.popBackStack();
					}
				});

		place_name = (TextView) rootView.findViewById(R.id.place_title);

		txt_eventdetails = (TextView) rootView
				.findViewById(R.id.txt_eventdetails);

		img_imagedetls = (ImageView) rootView.findViewById(R.id.img_imagedetls);

		imageLoader = ImageLoader.getInstance();

		onLoad();
		return rootView;
	}

	@SuppressLint("DefaultLocale")
	public void onLoad() {
		ArrayList<String> boatTrail = new ArrayList<String>();
		String ImageUrl = "";
		String text = "<p>Come along to take part in a series of hands-on making workshops led by local East London makers and try your hand at throwing a pot, carving a spoon or working with stone.</p."
				+ "<p>This sculpture will echo the form of the Lockkeepers Cottage that once sat at Carpenters Road Lock - a historically significant lock over the River Lea located in the heart of the Park.</p>";
		if (pageTitle.equals("Guided Tours")) {
			boatTrail = LLDCApplication.DBHelper.getStaticTrails("2");
			ImageUrl = boatTrail.get(2);

			imageLoader.displayImage(ImageUrl, img_imagedetls);

			place_name.setText(boatTrail.get(0));
			txt_eventdetails.setText(boatTrail.get(1));
		} else if (pageTitle.equals("Boat Trails")) {
			boatTrail = LLDCApplication.DBHelper.getStaticTrails("1");
			ImageUrl = boatTrail.get(2);

			imageLoader.displayImage(ImageUrl, img_imagedetls);

			place_name.setText(boatTrail.get(0));
			txt_eventdetails.setText(boatTrail.get(1));

		} else {
			place_name.setText(LLDCApplication.selectedParkModel.getTitle());
			txt_eventdetails.setText(Html
					.fromHtml(LLDCApplication.selectedParkModel.getDesc()));
			img_imagedetls.setVisibility(View.GONE);
			// ImageUrl = LLDCApplication.selectedParkModel.getImageUrl();
			// imageLoader.displayImage(ImageUrl, img_imagedetls);
		}

		// if (pageTitle.equals("Overview")) {
		// place_name.setText("Overview");
		// txt_eventdetails.setText(Html.fromHtml(text));
		// img_imagedetls.setVisibility(View.GONE);
		// } else if (pageTitle.equals("Getting to the park")) {
		// place_name.setText("Getting to the park");
		// img_imagedetls.setVisibility(View.GONE);
		// txt_eventdetails.setText(Html.fromHtml(text));
		// } else if (pageTitle.equals("Accessibility")) {
		// place_name.setText("Overview");
		// img_imagedetls.setVisibility(View.GONE);
		// txt_eventdetails.setText(Html.fromHtml(text));
		// }

	}
}
