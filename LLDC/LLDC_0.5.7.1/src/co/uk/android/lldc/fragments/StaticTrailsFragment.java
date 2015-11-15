package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.images.ImageCacheManager;

import com.android.volley.toolbox.NetworkImageView;

public class StaticTrailsFragment extends Fragment {

	TextView place_name;
	NetworkImageView img_imagedetls;
	String pageTitle;
	ScrollView sv_description;
	RelativeLayout rlBanner;
	WebView txt_eventdetails;

	@SuppressLint("SetJavaScriptEnabled") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_static_trails,
				container, false);
		place_name = (TextView) rootView.findViewById(R.id.place_title);
		sv_description = (ScrollView) rootView.findViewById(R.id.scrollview);

		rlBanner = (RelativeLayout) rootView.findViewById(R.id.rlBanner);

		pageTitle = getActivity().getIntent().getExtras()
				.getString("PAGETITLE");

		txt_eventdetails = (WebView) rootView
				.findViewById(R.id.txt_eventdetails);
		txt_eventdetails.setVerticalScrollBarEnabled(false);
		txt_eventdetails.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);
		
		txt_eventdetails.getSettings().setLoadWithOverviewMode(true);
		txt_eventdetails.getSettings().setUseWideViewPort(false);
		txt_eventdetails.getSettings().setDefaultFixedFontSize(40);
		txt_eventdetails.getSettings().setLoadsImagesAutomatically(true);
		txt_eventdetails.getSettings().setTextZoom(90);
		txt_eventdetails.getSettings().setJavaScriptEnabled(true);
		// txt_eventdetails.setTypeface(Typeface.createFromAsset(getActivity()
		// .getAssets(), "ROBOTO-LIGHT.TTF"));
		img_imagedetls = (NetworkImageView) rootView.findViewById(R.id.img_imagedetls);
		onLoad();
		return rootView;
	}

	@SuppressLint("DefaultLocale")
	public void onLoad() {
		ArrayList<String> boatTrail = new ArrayList<String>();
		String ImageUrl = "";
		String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/ROBOTO-LIGHT.TTF\"); }body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body><font font-family: MyFont;color=\"#444444\">";
		String pas = "</font></body></html>";

		if (pageTitle.equals("Guided Tours")) {
			boatTrail = LLDCApplication.DBHelper.getStaticTrails("2");
			ImageUrl = boatTrail.get(2);
//			imageLoader.displayImage(ImageUrl, img_imagedetls);
			place_name.setText(boatTrail.get(0).toString().toUpperCase());
			// txt_eventdetails.setHtmlFromString(boatTrail.get(1), false);
			
			img_imagedetls.setImageUrl(ImageUrl, ImageCacheManager
					.getInstance().getImageLoader());
			img_imagedetls.setDefaultImageResId(R.drawable.qeop_placeholder);
			img_imagedetls.setErrorImageResId(R.drawable.imgnt_placeholder);
			
			txt_eventdetails.loadData(pish + boatTrail.get(1) + pas,
					"text/html", "UTF-8");
			if (ImageUrl.equals(""))
				rlBanner.setVisibility(View.GONE);
			else
				rlBanner.setVisibility(View.VISIBLE);
		} else if (pageTitle.equals("Boat Tours")) {
			boatTrail = LLDCApplication.DBHelper.getStaticTrails("1");
			ImageUrl = boatTrail.get(2);
//			imageLoader.displayImage(ImageUrl, img_imagedetls);
			place_name.setText(boatTrail.get(0).toString().toUpperCase());
			// txt_eventdetails.setHtmlFromString(boatTrail.get(1), false);
			img_imagedetls.setImageUrl(ImageUrl, ImageCacheManager
					.getInstance().getImageLoader());
			img_imagedetls.setDefaultImageResId(R.drawable.qeop_placeholder);
			img_imagedetls.setErrorImageResId(R.drawable.imgnt_placeholder);
			txt_eventdetails.loadData(pish + boatTrail.get(1) + pas,
					"text/html", "UTF-8");
			if (ImageUrl.equals(""))
				rlBanner.setVisibility(View.GONE);
			else
				rlBanner.setVisibility(View.VISIBLE);
		} else {
			place_name.setText(LLDCApplication.selectedParkModel.getTitle()
					.toString().toUpperCase());
			// txt_eventdetails.setLinksClickable(true);
			// txt_eventdetails.setHtmlFromString(
			// LLDCApplication.selectedParkModel.getDesc(), false);
			txt_eventdetails.loadData(
					pish + LLDCApplication.selectedParkModel.getDesc() + pas,
					"text/html", "UTF-8");
			rlBanner.setVisibility(View.GONE);
		}

	}
}
