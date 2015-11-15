package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;

import com.nostra13.universalimageloader.core.ImageLoader;

public class StaticTrailsFragment extends Fragment {

	TextView place_name, txt_eventdetails;
	ImageView img_imagedetls;
	String pageTitle;
	ScrollView sv_description;
	ImageLoader imageLoader;
	RelativeLayout rlBanner;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_static_trails,
				container, false);
		place_name = (TextView) rootView.findViewById(R.id.place_title);
		sv_description = (ScrollView) rootView.findViewById(R.id.scrollview);
		
		rlBanner = (RelativeLayout) rootView.findViewById(R.id.rlBanner);
		
//		boolean strtext = getArguments().getBoolean("tvFullText");
//		if (strtext) {
//			RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//			scrollParams.addRule(RelativeLayout.BELOW, place_name.getId());
//			sv_description.setLayoutParams(scrollParams);
//		}

		pageTitle = getActivity().getIntent().getExtras()
				.getString("PAGETITLE");

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
		if (pageTitle.equals("Guided Tours")) {
			boatTrail = LLDCApplication.DBHelper.getStaticTrails("2");
			ImageUrl = boatTrail.get(2);
			imageLoader.displayImage(ImageUrl, img_imagedetls);
			place_name.setText(boatTrail.get(0).toString().toUpperCase());
			txt_eventdetails.setText(boatTrail.get(1));
			if(ImageUrl.equals(""))
				rlBanner.setVisibility(View.GONE);
			else
				rlBanner.setVisibility(View.VISIBLE);
		} else if (pageTitle.equals("Boat Tours")) {
			boatTrail = LLDCApplication.DBHelper.getStaticTrails("1");
			ImageUrl = boatTrail.get(2);
			imageLoader.displayImage(ImageUrl, img_imagedetls);
			place_name.setText(boatTrail.get(0).toString().toUpperCase());
			txt_eventdetails.setText(boatTrail.get(1));
			if(ImageUrl.equals(""))
				rlBanner.setVisibility(View.GONE);
			else
				rlBanner.setVisibility(View.VISIBLE);
		}else {
			place_name.setText(LLDCApplication.selectedParkModel.getTitle()
					.toString().toUpperCase());
			txt_eventdetails.setText(Html
					.fromHtml(LLDCApplication.selectedParkModel.getDesc()));
			rlBanner.setVisibility(View.GONE);
			// ImageUrl = LLDCApplication.selectedParkModel.getImageUrl();
			// imageLoader.displayImage(ImageUrl, img_imagedetls);
		}

	}
}
