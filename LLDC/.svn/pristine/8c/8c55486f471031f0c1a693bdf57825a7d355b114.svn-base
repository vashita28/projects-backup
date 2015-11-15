package co.uk.android.lldc.fragments;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.images.ImageCacheManager;

import com.android.volley.toolbox.NetworkImageView;

public class VenueOverviewFragment extends Fragment {

	NetworkImageView img_imagedetls;
	TextView eventname, eventdate, venue_feature;
	LinearLayout venue_feature_wrapper;

	HtmlTextView txt_eventdetails;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_venue_overview,
				container, false);
		img_imagedetls = (NetworkImageView) rootView.findViewById(R.id.img_imagedetls);
		eventname = (TextView) rootView.findViewById(R.id.eventname);
		eventdate = (TextView) rootView.findViewById(R.id.eventdate);
		venue_feature = (TextView) rootView.findViewById(R.id.venue_feature);
		txt_eventdetails = (HtmlTextView) rootView
				.findViewById(R.id.txt_eventdetails);
		venue_feature_wrapper = (LinearLayout) rootView
				.findViewById(R.id.venue_feature_wrapper);

		txt_eventdetails.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "ROBOTO-LIGHT.TTF"));
		
		onLoadVenueData();

		return rootView;

	}

	@SuppressLint("DefaultLocale")
	public void onLoadVenueData() {

		try {
			String szImageUrl = LLDCApplication.selectedModel.getLargeImage();
//					+ "&w=" + screenWidth + "&h=" + screenHeight
//					+ "&crop-to-fit";
			Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);
//			ImageLoader.getInstance().displayImage(szImageUrl, img_imagedetls);
			
			img_imagedetls.setImageUrl(szImageUrl, ImageCacheManager
					.getInstance().getImageLoader());
			img_imagedetls.setDefaultImageResId(R.drawable.qeop_placeholder);
			img_imagedetls.setErrorImageResId(R.drawable.imgnt_placeholder);
			
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
		txt_eventdetails.setHtmlFromString(LLDCApplication.selectedModel
				.getLongDescription(),false);

		String featureList = "";

		for (int i = 0; i < LLDCApplication.selectedModel
				.getVenueFacilityList().size(); i++) {
			try {
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
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
