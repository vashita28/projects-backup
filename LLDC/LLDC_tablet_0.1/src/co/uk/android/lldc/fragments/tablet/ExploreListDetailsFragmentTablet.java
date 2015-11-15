package co.uk.android.lldc.fragments.tablet;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.uk.android.lldc.R;
import co.uk.android.lldc.tablet.LLDCApplication;

public class ExploreListDetailsFragmentTablet extends Fragment {
	FragmentManager mFragmentManager;
	String pageTitle = "";
	Bundle bundle = new Bundle();

	public Fragment fragment;

	public Handler mExploreListDetailsHandler = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		fragment = this;

		View rootView = inflater.inflate(
				R.layout.fragment_explorelistdetails_tablet, container, false);
		bundle = getArguments();
		pageTitle = bundle.getString("PAGETITLE");

		bundle = new Bundle();
		bundle.putString("PAGETITLE", pageTitle);

		mExploreListDetailsHandler = new Handler() {
			public void handleMessage(Message message) {

				if (message.what == 1101) {
					try {
						if (LLDCApplication.isInsideThePark
								|| LLDCApplication.isDebug) {
							// relSearch.setVisibility(View.VISIBLE);
						} else {
							// relSearch.setVisibility(View.GONE);
						}
						mExploreListDetailsHandler.sendEmptyMessageDelayed(
								1101, 300000);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		displayView(1);
		mExploreListDetailsHandler.sendEmptyMessage(1101);

		// ((HomeActivityTablet) getActivity()).showBackButton();
		// ((HomeActivityTablet) getActivity()).rlBackBtn
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// ((HomeActivityTablet) getActivity()).hideBackButton();
		//
		// getParentFragment().getChildFragmentManager()
		// .popBackStack();
		// }
		// });
		return rootView;
	}

	private void displayView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:
			fragment = new co.uk.android.lldc.fragments.ExploreDetailsFragment();
			fragment.setArguments(bundle);
			break;
		}

		if (fragment != null) {
			mFragmentManager = getParentFragment().getChildFragmentManager();
			mFragmentManager.beginTransaction()
					.replace(R.id.frame_container_parent, fragment)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		fragment = this;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		fragment = null;

		try {
			mExploreListDetailsHandler.removeMessages(1101);
			mExploreListDetailsHandler = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
