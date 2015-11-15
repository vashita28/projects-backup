package co.uk.android.lldc.fragments.tablet;

import android.app.Activity;
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
import co.uk.android.lldc.fragments.StaticTrailsFragment;
import co.uk.android.lldc.tablet.HomeActivityTablet;

public class StaticTrailsDummyFragmentTablet extends Fragment {
	FragmentManager mFragmentManager;
	String mPageTitle = "";
	Bundle bundle = new Bundle();

	public Fragment fragment = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = null;
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		rootView = inflater.inflate(
				R.layout.static_trials_dumy_fragment_tablet, container, false);

		((HomeActivityTablet) getActivity()).ivBack.setVisibility(View.VISIBLE);
		((HomeActivityTablet) getActivity()).rlBackBtn.setEnabled(true);
		((HomeActivityTablet) getActivity()).rlBackBtn
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						getParentFragment().getChildFragmentManager()
								.popBackStack();
					}
				});
		bundle = getArguments();
		mPageTitle = bundle.getString("PAGETITLE");

		displayView(1);
		return rootView;
	}

	private void displayView(int position) {

		Fragment fragment = null;
		switch (position) {
		case 1:
			fragment = (Fragment) new StaticTrailsFragment();
			bundle.putBoolean("bIsFromStaticTrailsDummy", true);
			fragment.setArguments(bundle);
			break;

		}

		if (fragment != null) {
			mFragmentManager = getParentFragment().getChildFragmentManager();
			mFragmentManager
					.beginTransaction()
					// .addToBackStack(
					// StaticTrailsDummyFragmentTablet.class.getName())
					.replace(R.id.frame_container_parent, fragment,
							StaticTrailsFragment.class.getName())
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
	}
}
