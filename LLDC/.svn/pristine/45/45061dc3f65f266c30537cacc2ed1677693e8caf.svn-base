package co.uk.android.lldc.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.MapNavigationActivity;
import co.uk.android.lldc.R;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ExploreDetailsFragment extends Fragment {

	TextView place_title, text_time, text_walk, text_duration, text_accessible,
			txt_eventdetails, tvStartTheTrail;

	RelativeLayout dynamic_innerlayout;

	RelativeLayout rlBannerImage;
	ImageView img_imagedetls;
	String pageTitle = "";
	int screenWidth = 0, screenHeight = 0;
	public Handler mExploreDetailsFragHandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mExploreDetailsFragHandler = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1101) {
					try {
						if (LLDCApplication.isInsideThePark
								|| LLDCApplication.isDebug) {
							tvStartTheTrail.setVisibility(View.VISIBLE);
						} else {
							tvStartTheTrail.setVisibility(View.GONE);
						}

						mExploreDetailsFragHandler.sendEmptyMessageDelayed(
								1101, 300000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.fragment_exploredetails,
				container, false);

		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y / 2;

		place_title = (TextView) rootView.findViewById(R.id.place_title);
		text_walk = (TextView) rootView.findViewById(R.id.text_walk);
		text_duration = (TextView) rootView.findViewById(R.id.text_duration);
		text_accessible = (TextView) rootView.findViewById(R.id.text_);
		txt_eventdetails = (TextView) rootView
				.findViewById(R.id.txt_eventdetails);
		img_imagedetls = (ImageView) rootView.findViewById(R.id.img_imagedetls);
		text_time = (TextView) rootView.findViewById(R.id.text_time);
		dynamic_innerlayout = (RelativeLayout) rootView
				.findViewById(R.id.dynamic_innerlayout);
		tvStartTheTrail = (TextView) rootView
				.findViewById(R.id.tvStartTheTrail);
		rlBannerImage = (RelativeLayout) rootView
				.findViewById(R.id.rlBannerImage);
		pageTitle = (String) getActivity().getIntent().getExtras()
				.get("PAGETITLE");

		if (pageTitle.equals("Facilities")) {
			text_time.setVisibility(View.VISIBLE);
			dynamic_innerlayout.setVisibility(View.GONE);
		} else {
			text_time.setVisibility(View.GONE);
			dynamic_innerlayout.setVisibility(View.VISIBLE);
		}

		if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.TRAILS) {
			/*
			 * if (LLDCApplication.isInsideThePark || LLDCApplication.isDebug) {
			 * tvStartTheTrail.setVisibility(View.VISIBLE); } else {
			 * tvStartTheTrail.setVisibility(View.GONE); }
			 */
			tvStartTheTrail.setVisibility(View.VISIBLE);
			mExploreDetailsFragHandler.sendEmptyMessageDelayed(1101, 300000);
		} else {
			tvStartTheTrail.setVisibility(View.GONE);
		}

		img_imagedetls.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (pageTitle.equals("Trails")) {
					if (!LLDCApplication.isInsideThePark
							&& !LLDCApplication.isDebug) {
						LLDCApplication
								.onShowToastMesssage(
										getActivity(),
										"Navigation will not function as it appears you are outside of the park or your GPS is not active.");
					} else if (LLDCApplication.selectedModel.getWyapoitnList()
							.size() > 0) {
						Intent intent = new Intent(getActivity(),
								MapNavigationActivity.class);
						intent.putExtra("PAGETITLE", "Trails");
						getActivity().startActivity(intent);
					} else {
						LLDCApplication.onShowToastMesssage(getActivity(),
								"Trail data is not present.");
					}
				}
			}
		});

		onLoadData();
		return rootView;
	}

	@SuppressLint("DefaultLocale")
	public void onLoadData() {

		place_title.setText(LLDCApplication.selectedModel.getName().toString()
				.toUpperCase());

		text_time.setText(LLDCApplication.selectedModel.getActiveDays()
				.toString().toUpperCase());

		place_title.setTextColor(Color.parseColor(LLDCApplication.selectedModel
				.getColor()));

		text_time.setTextColor(Color.parseColor(LLDCApplication.selectedModel
				.getColor()));

		if (pageTitle.equals("Trails")) {
			text_walk.setText(LLDCApplication.selectedModel.getVenueTitle());
			text_duration
					.setText(LLDCApplication.selectedModel.getActiveDays());
		}

		try {
			String szImageUrl = LLDCApplication.selectedModel.getLargeImage()
					+ "&w=" + screenWidth + "&h=" + screenHeight
					+ "&crop-to-fit";
			Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);
			ImageLoader.getInstance().displayImage(szImageUrl, img_imagedetls);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		txt_eventdetails.setText(Html.fromHtml(LLDCApplication.selectedModel
				.getLongDescription()));

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			mExploreDetailsFragHandler.removeMessages(1101);
			mExploreDetailsFragHandler = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
