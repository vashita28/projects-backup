package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.tablet.LLDCApplication;

import com.nostra13.universalimageloader.core.ImageLoader;

public class StaticTrailsFragment extends Fragment {
	private static final String TAG = StaticTrailsFragment.class
			.getSimpleName();

	TextView place_name;
	ImageView img_imagedetls;
	String pageTitle;
	RelativeLayout rlBanner;
	WebView txt_eventdetails;
	boolean bIsFromStatisTrailsDummy = false;

	public static StaticTrailsFragment sStaticTrailsTablet;

	@Override
	public void onAttach(Activity activity) {
		sStaticTrailsTablet = this;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		sStaticTrailsTablet = null;
		super.onDetach();
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = null;
		Log.e(TAG, "Device is Tablet..." + TAG);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(R.layout.fragment_static_trails_tablet,
				container, false);
		Bundle bundle = getArguments();
		pageTitle = bundle.getString("PAGETITLE");

		if (bundle != null && bundle.containsKey("bIsFromStaticTrailsDummy")
				&& bundle.getBoolean("bIsFromStaticTrailsDummy"))
			bIsFromStatisTrailsDummy = true;

		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText(pageTitle);
		((HomeActivityTablet) getActivity()).ivBack.setVisibility(View.VISIBLE);
		((HomeActivityTablet) getActivity()).rlBackBtn.setEnabled(true);
		((HomeActivityTablet) getActivity()).rlBackBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.e(TAG, "StaticTrailsFragment back");

						if (bIsFromStatisTrailsDummy) {
							ExploreListingFragment exploreListingFragment = (ExploreListingFragment) getParentFragment()
									.getChildFragmentManager()
									.findFragmentByTag(
											ExploreListingFragment.class
													.getName());
							getParentFragment()
									.getChildFragmentManager()
									.beginTransaction()
									.replace(R.id.frame_container_parent,
											exploreListingFragment).commit();
						} else {
							boolean temp = getParentFragment()
									.getChildFragmentManager()
									.popBackStackImmediate();
							// Log.v("", temp +"");
						}
					}
				});

		place_name = (TextView) rootView.findViewById(R.id.place_title);

		rlBanner = (RelativeLayout) rootView.findViewById(R.id.rlBanner);
		txt_eventdetails = (WebView) rootView
				.findViewById(R.id.txt_eventdetails);
		txt_eventdetails.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.SINGLE_COLUMN);

		txt_eventdetails.getSettings().setLoadWithOverviewMode(true);
		txt_eventdetails.getSettings().setUseWideViewPort(false);
		txt_eventdetails.getSettings().setDefaultFixedFontSize(40);
		txt_eventdetails.getSettings().setLoadsImagesAutomatically(true);
		txt_eventdetails.getSettings().setTextZoom(90);
		txt_eventdetails.getSettings().setJavaScriptEnabled(true);
		txt_eventdetails.getSettings().setDefaultTextEncodingName("UTF-8");
		txt_eventdetails.getSettings().setAppCacheEnabled(false);
		// txt_eventdetails.setTypeface(Typeface.createFromAsset(getActivity()
		// .getAssets(), "ROBOTO-LIGHT.TTF"));
		img_imagedetls = (ImageView) rootView.findViewById(R.id.img_imagedetls);

		onLoad();
		LLDCApplication.removeSimpleProgressDialog();
		return rootView;
	}

	@SuppressLint("DefaultLocale")
	public void onLoad() {
		ArrayList<String> boatTrail = new ArrayList<String>();
		String ImageUrl = "";
		String pish = "<html><head>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\">"
				+ "<meta charset=\"UTF-8\"><style type=\"text/css\">"
				+ "@font-face {font-family: MyFont;src: url(\"file:///android_asset/ROBOTO-LIGHT.TTF\");}"
				+ "body {max-width: 100%; width:auto; height: auto; font-family: MyFont;font-size: medium;text-align: justify;} "
				+ "img{max-width: 100%; width:auto; height: auto;}</style></head>"
				+ "<body><font font-family: MyFont;color=\"#444444\" align=\"left\">";
		String pas = "</font></body></html>";

		if (pageTitle.equals("Guided Tours")) {
			boatTrail = LLDCApplication.DBHelper.getStaticTrails("2");

			ImageUrl = boatTrail.get(2);
			// + "&width="
			// + LLDCApplication.screenWidth + "&height="
			// + LLDCApplication.screenHeight / 2 + "&crop-to-fit";

			ImageLoader.getInstance().displayImage(ImageUrl, img_imagedetls);
			// img_imagedetls.setImageUrl(ImageUrl, ImageCacheManager
			// .getInstance().getImageLoader());
			// img_imagedetls.setDefaultImageResId(R.drawable.qeop_placeholder);
			// img_imagedetls.setErrorImageResId(R.drawable.placeholder);

			place_name.setText(boatTrail.get(0).toString().toUpperCase());
			txt_eventdetails.loadData(pish + boatTrail.get(1) + pas,
					"text/html; charset=utf-8", "UTF-8");
			if (ImageUrl.equals(""))
				rlBanner.setVisibility(View.GONE);
			else
				rlBanner.setVisibility(View.VISIBLE);
		} else if (pageTitle.equals("Boat Tours")) {
			boatTrail = LLDCApplication.DBHelper.getStaticTrails("1");
			ImageUrl = boatTrail.get(2);
			// + "&width="
			// + LLDCApplication.screenWidth + "&height="
			// + LLDCApplication.screenHeight / 2 + "&crop-to-fit";

			ImageLoader.getInstance().displayImage(ImageUrl, img_imagedetls);
			// img_imagedetls.setImageUrl(ImageUrl, ImageCacheManager
			// .getInstance().getImageLoader());
			// img_imagedetls.setDefaultImageResId(R.drawable.qeop_placeholder);
			// img_imagedetls.setErrorImageResId(R.drawable.placeholder);

			place_name.setText(boatTrail.get(0).toString().toUpperCase());
			txt_eventdetails.loadData(pish + boatTrail.get(1) + pas,
					"text/html; charset=utf-8", "UTF-8");
			if (ImageUrl.equals(""))
				rlBanner.setVisibility(View.GONE);
			else
				rlBanner.setVisibility(View.VISIBLE);

		} else {
			place_name.setPadding(0, 20, 0, 0);
			place_name.setText(LLDCApplication.selectedParkModel.getTitle()
					.toString().toUpperCase());
			if (LLDCApplication.isDebug) {
				LLDCApplication.onShowToastMesssage(getActivity(),
						LLDCApplication.selectedParkModel.getDesc());
			}
			txt_eventdetails.loadData(
					pish + LLDCApplication.selectedParkModel.getDesc() + pas,
					"text/html; charset=utf-8", "UTF-8");
			rlBanner.setVisibility(View.GONE);
		}

	}
}
