package co.uk.android.lldc.fragments;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.tablet.LLDCApplication;
import co.uk.android.lldc.tablet.MapNavigationActivity;
import co.uk.android.lldc.utils.Constants;

import com.nostra13.universalimageloader.core.ImageLoader;

public class ExploreDetailsFragment extends Fragment {
	private static final String TAG = ExploreDetailsFragment.class
			.getSimpleName();

	LinearLayout screen;
	TextView place_title, text_time, text_walk, text_duration, text_accessible,
			tvStartTheTrail;
	HtmlTextView txt_eventdetails;
	LinearLayout dynamic_innerlayout;

	RelativeLayout rlBannerImage;
	ImageView img_imagedetls;
	private String pageTitle = "";
	int screenWidth = 0, screenHeight = 0;
	public Handler mExploreDetailsFragHandler = null;
	Bundle bundle = new Bundle();
	boolean bIsfromTrailsBack = false;

	@SuppressLint("HandlerLeak")
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
		View rootView = null;
		Log.e(TAG, "Device is Tablet..." + TAG);
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(R.layout.fragment_exploredetails_tablet,
				container, false);

		screen = (LinearLayout) rootView.findViewById(R.id.screen);
		place_title = (TextView) rootView.findViewById(R.id.place_title);
		text_walk = (TextView) rootView.findViewById(R.id.text_walk);
		text_duration = (TextView) rootView.findViewById(R.id.text_duration);
		text_accessible = (TextView) rootView.findViewById(R.id.text_);
		txt_eventdetails = (HtmlTextView) rootView
				.findViewById(R.id.txt_eventdetails);
		img_imagedetls = (ImageView) rootView.findViewById(R.id.img_imagedetls);
		text_time = (TextView) rootView.findViewById(R.id.text_time);
		dynamic_innerlayout = (LinearLayout) rootView
				.findViewById(R.id.dynamic_innerlayout);
		tvStartTheTrail = (TextView) rootView
				.findViewById(R.id.tvStartTheTrail);
		rlBannerImage = (RelativeLayout) rootView
				.findViewById(R.id.rlBannerImage);

		bundle = getArguments();
		pageTitle = bundle.getString("PAGETITLE");

		if (pageTitle.equals("Trails"))
			Constants.IS_TRAILS = true;
		else
			Constants.IS_TRAILS = false;

		if (bundle.containsKey(Constants.IS_TRAILS_BACK))
			bIsfromTrailsBack = bundle.getBoolean(Constants.IS_TRAILS_BACK);

		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText(pageTitle);
		txt_eventdetails.setTypeface(Typeface.createFromAsset(getActivity()
				.getAssets(), "ROBOTO-LIGHT.TTF"));
		if (pageTitle.equals("Facilities")) {
			text_time.setVisibility(View.VISIBLE);
			dynamic_innerlayout.setVisibility(View.GONE);
			((HomeActivityTablet) getActivity()).tvHeaderTitle
					.setText("Food & Drink");
		} else {
			text_time.setVisibility(View.GONE);
			dynamic_innerlayout.setVisibility(View.VISIBLE);
		}

		if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.TRAILS) {
			tvStartTheTrail.setVisibility(View.VISIBLE);
			mExploreDetailsFragHandler.sendEmptyMessageDelayed(1101, 300000);
		} else {
			tvStartTheTrail.setVisibility(View.GONE);
		}

		/* Click for imageview */
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

						LLDCApplication.showSimpleProgressDialog(getActivity());

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
		LLDCApplication.removeSimpleProgressDialog();

		((HomeActivityTablet) getActivity()).showBackButton();
		((HomeActivityTablet) getActivity()).showNavigationIcon();
		((HomeActivityTablet) getActivity()).rlBackBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						ExploreFragment exploreFragment = (ExploreFragment) getParentFragment()
								.getChildFragmentManager().findFragmentByTag(
										ExploreFragment.class.getName());
						if (bIsfromTrailsBack) {
							getParentFragment().getChildFragmentManager()
									.popBackStack();
						} else if (exploreFragment != null) {
							getParentFragment()
									.getChildFragmentManager()
									.beginTransaction()
									.replace(R.id.frame_container_parent,
											exploreFragment).commit();
						} else {
							Log.e(TAG, "else back");
							getParentFragment().getFragmentManager()
									.popBackStack();
						}

					}
				});
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

		if (LLDCApplication.selectedModel.getModelType() == LLDCApplication.TRAILS) {
			text_walk.setText(LLDCApplication.selectedModel.getVenueTitle());
			text_duration
					.setText(LLDCApplication.selectedModel.getActiveDays());
		}

		try {
			String szImageUrl = "";
			szImageUrl = LLDCApplication.selectedModel.getLargeImage()
					+ "&width=" + LLDCApplication.screenWidth + "&height="
					+ LLDCApplication.screenHeight / 2 + "&crop-to-fit";
			Log.e(TAG, "szImageUrl::> " + szImageUrl);

			img_imagedetls.getLayoutParams().height = LLDCApplication.screenHeight / 2;
			img_imagedetls.getLayoutParams().width = LLDCApplication.screenWidth;
			ImageLoader.getInstance().displayImage(szImageUrl, img_imagedetls);

			// img_imagedetls.setImageUrl(szImageUrl, ImageCacheManager
			// .getInstance().getImageLoader());
			// img_imagedetls.setDefaultImageResId(R.drawable.qeop_placeholder);
			// img_imagedetls.setErrorImageResId(R.drawable.placeholder);

			// if (szImageUrl.length() > 0)
			// img_imagedetls.setScaleType(ScaleType.FIT_XY);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		txt_eventdetails.setHtmlFromString(
				LLDCApplication.selectedModel.getLongDescription(), false);

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
