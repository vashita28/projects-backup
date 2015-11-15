package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import co.uk.android.lldc.HomeActivityTablet;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.StaticTrailsActivity;
import co.uk.android.lldc.models.DashboardModel;
import co.uk.android.lldc.models.TheParkModel;
import co.uk.android.util.Constants;
import co.uk.android.util.Util;

public class TheParkFragment extends Fragment {
	private static final String TAG = TheParkFragment.class.getSimpleName();

	LinearLayout rlOverview, rlGettingToPark, rlAccessibility, rlReportAnIssue;

	ImageView ivClock, ivGettingToPark, ivAccessibility;

	ArrayList<TheParkModel> theParkList = null;

	TextView tvOverview, tvVenueOpeningTimes, tvGettingToPark,
			tvNearestTubeAndTrain, tvAccessibility, tvOurAccess;

	boolean bIsTablet = false;
	Bundle nBundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;
		if (Util.getIsTabletFlag(getActivity())) {
			Log.e(TAG, "Device is Tablet...");
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			rootView = inflater.inflate(R.layout.fragment_the_park_tablet,
					container, false);
			bIsTablet = true;
		} else {
			Log.e(TAG, "Device is Phone...");
			rootView = inflater.inflate(R.layout.fragment_the_park, container,
					false);
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			bIsTablet = false;
		}

		nBundle = new Bundle();

		rlOverview = (LinearLayout) rootView.findViewById(R.id.rlOverview);
		rlGettingToPark = (LinearLayout) rootView
				.findViewById(R.id.rlGettingToPark);
		rlAccessibility = (LinearLayout) rootView
				.findViewById(R.id.rlAccessibility);
		rlReportAnIssue = (LinearLayout) rootView
				.findViewById(R.id.rlReportAnIssue);

		tvOverview = (TextView) rootView.findViewById(R.id.tvOverview);
		tvVenueOpeningTimes = (TextView) rootView
				.findViewById(R.id.tvVenueOpeningTimes);
		tvGettingToPark = (TextView) rootView
				.findViewById(R.id.tvGettingToPark);
		tvNearestTubeAndTrain = (TextView) rootView
				.findViewById(R.id.tvNearestTubeAndTrain);
		tvAccessibility = (TextView) rootView
				.findViewById(R.id.tvAccessibility);
		tvOurAccess = (TextView) rootView.findViewById(R.id.tvOurAccess);

		ivClock = (ImageView) rootView.findViewById(R.id.ivClock);
		ivGettingToPark = (ImageView) rootView
				.findViewById(R.id.ivGettingToPark);
		ivAccessibility = (ImageView) rootView
				.findViewById(R.id.ivAccessibility);

		((HomeActivityTablet) getActivity()).ivBack
				.setVisibility(View.INVISIBLE);
		((HomeActivityTablet) getActivity()).rlBackBtn.setEnabled(false);
		((HomeActivityTablet) getActivity()).tvHeaderTitle.setText("The Park");

		rlOverview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (theParkList.size() > 0) {
					LLDCApplication.selectedParkModel = theParkList.get(0);

					if (bIsTablet) {
						((HomeActivityTablet) getActivity()).tvHeaderTitle
								.setText("Overview");
						nBundle.putString("PAGETITLE", "Overview");
						displayView(Constants.OVERVIEW);
					} else {
						Intent intent = new Intent(getActivity(),
								StaticTrailsActivity.class);
						intent.putExtra("PAGETITLE", "Overview");
						getActivity().startActivity(intent);
					}
				} else {
					LLDCApplication.onShowToastMesssage(getActivity(),
							"Unable to retrive the data...");
				}
			}
		});
		rlGettingToPark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (theParkList.size() > 0) {
					LLDCApplication.selectedParkModel = theParkList.get(1);
					if (bIsTablet) {
						((HomeActivityTablet) getActivity()).tvHeaderTitle
								.setText("Getting to the park");
						nBundle.putString("PAGETITLE", "Getting to the park");
						displayView(Constants.OVERVIEW);
					} else {
						Intent intent = new Intent(getActivity(),
								StaticTrailsActivity.class);
						intent.putExtra("PAGETITLE", "Getting to the park");
						getActivity().startActivity(intent);
					}
				} else {
					LLDCApplication.onShowToastMesssage(getActivity(),
							"Unable to retrive the data...");
				}
			}
		});
		rlAccessibility.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (theParkList.size() > 0) {
					LLDCApplication.selectedParkModel = theParkList.get(2);
					if (bIsTablet) {
						((HomeActivityTablet) getActivity()).tvHeaderTitle
								.setText("Accessibility");
						nBundle.putString("PAGETITLE", "Accessibility");
						displayView(Constants.OVERVIEW);
					} else {
						Intent intent = new Intent(getActivity(),
								StaticTrailsActivity.class);
						intent.putExtra("PAGETITLE", "Accessibility");
						getActivity().startActivity(intent);
					}
				} else {
					LLDCApplication.onShowToastMesssage(getActivity(),
							"Unable to retrive the data...");
				}
			}
		});
		rlReportAnIssue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				DashboardModel dashboardModel = LLDCApplication.DBHelper
						.onGetDashBoardData();

				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/html");

				intent.putExtra(Intent.EXTRA_EMAIL,
						new String[] { dashboardModel.getReportEmailTo() });
				intent.putExtra(Intent.EXTRA_SUBJECT,
						dashboardModel.getReportEmailSubject());
				intent.putExtra(Intent.EXTRA_TEXT, "");
				// intent.setType("text/plain");
				// need this to prompts email client only
				intent.setType("message/rfc822");
				startActivityForResult(
						Intent.createChooser(intent, "Send Email"), 0);

			}
		});
		onGetDB();

		return rootView;
	}

	public void onGetDB() {

		theParkList = LLDCApplication.DBHelper.onGetTheParkData();
		if (theParkList.size() > 0) {
			setServerData();
		}
	}

	public void setServerData() {

		String szImageUrl = theParkList.get(0).getImageUrl();
		ImageLoader.getInstance().displayImage(szImageUrl, ivClock);

		szImageUrl = theParkList.get(1).getImageUrl();
		ImageLoader.getInstance().displayImage(szImageUrl, ivGettingToPark);

		szImageUrl = theParkList.get(2).getImageUrl();

		@SuppressWarnings("deprecation")
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.showStubImage(R.drawable.imgnt_placeholder)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.NONE).build();

		ImageLoader.getInstance().displayImage(szImageUrl, ivAccessibility,
				defaultOptions);

		tvOverview.setText(theParkList.get(0).getTitle().toUpperCase());
		tvVenueOpeningTimes.setText(theParkList.get(0).getSubTitle()
				.toUpperCase());
		tvGettingToPark.setText(theParkList.get(1).getTitle().toUpperCase());
		tvNearestTubeAndTrain.setText(theParkList.get(1).getSubTitle()
				.toUpperCase());
		tvAccessibility.setText(theParkList.get(2).getTitle().toUpperCase());
		tvOurAccess.setText(theParkList.get(2).getSubTitle().toUpperCase());
	}

	private void displayView(int position) {

		Fragment fragment = null;
		FragmentManager mFragmentManager;
		String szFrameName = "";
		switch (position) {
		case Constants.OVERVIEW:
			fragment = new StaticTrailsFragment();
			szFrameName = StaticTrailsFragment.class.getName();
			fragment.setArguments(nBundle);
			break;

		}

		if (fragment != null) {
			mFragmentManager = getParentFragment().getChildFragmentManager();
			mFragmentManager
					.beginTransaction()
					.addToBackStack(TAG)
					.replace(R.id.frame_container_parent, fragment, szFrameName)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}

	}

}
