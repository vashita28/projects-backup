package co.uk.android.lldc.fragments.tablet;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.EventsFragment;
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.utils.Constants;

public class TodaysEventFragmentTablet extends Fragment {

	FragmentManager mFragmentManager;
	Fragment fragmentTodayEvent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = null;
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(R.layout.fragment_todays_event, container,
				false);
		fragmentTodayEvent = this;

		((HomeActivityTablet) getActivity()).rlHeader
				.setVisibility(View.VISIBLE);
		((HomeActivityTablet) getActivity()).tvHeaderTitle
				.setText("Today's Event");
		//((HomeActivityTablet) getActivity()).rlSearchWelcome
		//		.setVisibility(View.GONE);
		((HomeActivityTablet) getActivity()).rlSearchHeaderDefault
				.setVisibility(View.VISIBLE);

		((HomeActivityTablet) getActivity()).showBackButton();
		((HomeActivityTablet) getActivity()).rlBackBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						// if (Constants.TODAYEVENT_FLOW == true) {
						getParentFragment().getChildFragmentManager()
								.popBackStack();

						getParentFragment()
								.getChildFragmentManager()
								.popBackStackImmediate(
										null,
										FragmentManager.POP_BACK_STACK_INCLUSIVE);

						// }

						// returnBackStackImmediate(getParentFragment().getChildFragmentManager());

						// FragmentManager manager = getParentFragment()
						// .getChildFragmentManager();
						// FragmentTransaction trans =
						// manager.beginTransaction();
						// trans.remove(fragmentTodayEvent);
						// trans.commit();
						// manager.popBackStack();

						// getParentFragment().getChildFragmentManager()
						// .popBackStack();

						// manager.popBackStackImmediate(null,
						// FragmentManager.POP_BACK_STACK_INCLUSIVE);
						// popBackStack(null,
						// FragmentManager.POP_BACK_STACK_INCLUSIVE)

					}
				});
		displayView(1);

		return rootView;
	}

	private void displayView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:
			fragment = (Fragment) new EventsFragment();
			Bundle bundle = new Bundle();
			bundle.putString(Constants.IS_FOR_TODAY_EVENT,
					"TodaysEventFragment");
			fragment.setArguments(bundle);
			break;

		}

		if (fragment != null) {
			mFragmentManager = getParentFragment().getChildFragmentManager();
			mFragmentManager.beginTransaction()
					.replace(R.id.frame_container_parent, fragment)
					// frame_container
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
					.commit();
		}

	}

}
