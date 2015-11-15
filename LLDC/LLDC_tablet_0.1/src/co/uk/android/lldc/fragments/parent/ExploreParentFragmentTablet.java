package co.uk.android.lldc.fragments.parent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.uk.android.lldc.R;
import co.uk.android.lldc.fragments.ExploreFragment;
import co.uk.android.lldc.tablet.HomeActivityTablet;

public class ExploreParentFragmentTablet extends Fragment {

	private ViewGroup mExploreFragment;
	private View mView;
	private FragmentManager mFragmentManager;

	public static ExploreParentFragmentTablet sExploreParentFragmentTablet;

	public ExploreParentFragmentTablet() {
		// android framework need that
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.parent_fragment_tablet, container,
				false);
		mExploreFragment = (ViewGroup) mView
				.findViewById(R.id.frame_container_parent);
		showSubfragments(0, "ExploreFragment");

		((HomeActivityTablet) getActivity()).hideBackButton();
		return mView;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivityTablet) getActivity()).tvExplore
				.setTextColor(getResources().getColor(R.color.textcolor_pink));
		((HomeActivityTablet) getActivity()).ivExplore
				.setImageResource(R.drawable.explore_tab_selc);
	}

	public void showSubfragments(int position, String tagName) {
		Fragment fragment = null;
		mFragmentManager = this.getChildFragmentManager();
		FragmentTransaction fragtrans = mFragmentManager.beginTransaction();
		switch (position) {
		case 0:
			fragment = new co.uk.android.lldc.fragments.ExploreFragment();
			break;

		}

		if (fragment != null) {
			fragtrans.replace(R.id.frame_container_parent, fragment,
					ExploreFragment.class.getName());
			fragtrans.commit();
		}

	}

	@Override
	public void onAttach(Activity activity) {
		sExploreParentFragmentTablet = this;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		sExploreParentFragmentTablet = null;
		super.onDetach();
	}

}
