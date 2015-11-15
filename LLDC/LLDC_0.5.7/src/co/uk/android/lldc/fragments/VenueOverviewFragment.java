package co.uk.android.lldc.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class VenueOverviewFragment extends Fragment {

	ImageView img_imagedetls;
	TextView eventname, eventdate, txt_eventdetails, venue_feature;
	ImageLoader imageLoader;
	DisplayImageOptions options;
	int screenWidth = 0, screenHeight = 0;
	LinearLayout venue_feature_wrapper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_venue_overview,
				container, false);

		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y / 2;

		imageLoader = ImageLoader.getInstance();
		img_imagedetls = (ImageView) rootView.findViewById(R.id.img_imagedetls);
		eventname = (TextView) rootView.findViewById(R.id.eventname);
		eventdate = (TextView) rootView.findViewById(R.id.eventdate);
		venue_feature = (TextView) rootView.findViewById(R.id.venue_feature);
		txt_eventdetails = (TextView) rootView
				.findViewById(R.id.txt_eventdetails);
		venue_feature_wrapper = (LinearLayout) rootView
				.findViewById(R.id.venue_feature_wrapper);

		onLoadVenueData();

		return rootView;

	}

	@SuppressLint("DefaultLocale")
	public void onLoadVenueData() {

		try {
			String szImageUrl = LLDCApplication.selectedModel.getLargeImage()
					+ "&w=" + screenWidth + "&h=" + screenHeight
					+ "&crop-to-fit";
			;
			Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);
			ImageLoader.getInstance().displayImage(szImageUrl, img_imagedetls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// img_feature4, img_feature5, img_feature6;
		eventname.setText(LLDCApplication.selectedModel.getName().toString()
				.toUpperCase());
		eventdate.setText(LLDCApplication.selectedModel.getActiveDays()
				.toString().toUpperCase());

		eventname.setTextColor(Color.parseColor(LLDCApplication.selectedModel
				.getColor()));
		eventdate.setTextColor(Color.parseColor(LLDCApplication.selectedModel
				.getColor()));
		txt_eventdetails.setText(Html.fromHtml(LLDCApplication.selectedModel
				.getLongDescription()));

		String featureList = "";

		for (int i = 0; i < LLDCApplication.selectedModel
				.getVenueFacilityList().size(); i++) {
			int id = Integer.parseInt(LLDCApplication.selectedModel
					.getVenueFacilityList().get(i).getFacility_id());
			switch (id) {
			case 1:
				featureList = featureList
						+ LLDCApplication.VENUE_FAC_ID_UNICODE_1 + "  ";
				break;
			case 2:
				featureList = featureList
						+ LLDCApplication.VENUE_FAC_ID_UNICODE_2 + "  ";
				break;
			case 3:
				featureList = featureList
						+ LLDCApplication.VENUE_FAC_ID_UNICODE_3 + "  ";
				break;
			case 4:
				featureList = featureList
						+ LLDCApplication.VENUE_FAC_ID_UNICODE_4 + "  ";
				break;
			case 5:
				featureList = featureList
						+ LLDCApplication.VENUE_FAC_ID_UNICODE_5 + "  ";
				break;
			case 6:
				featureList = featureList
						+ LLDCApplication.VENUE_FAC_ID_UNICODE_6 + "  ";
				break;
			}
		}
		if (featureList.toString().equals("")) {
			venue_feature_wrapper.setVisibility(View.GONE);
		} else {
			venue_feature_wrapper.setVisibility(View.VISIBLE);
			venue_feature.setText(featureList.toString().trim());
			venue_feature.setTextColor(Color
					.parseColor(LLDCApplication.selectedModel.getColor()));
		}
	}

}
