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
import co.uk.android.lldc.fragments.WelcomeFragment;
import co.uk.android.lldc.tablet.HomeActivityTablet;

public class WelcomeParentFragmentTablet extends Fragment {

	private ViewGroup mWelcomeFragment;
	private View mView;
	private FragmentManager mFragmentManager;

	public static WelcomeParentFragmentTablet sWelcomeParentFragmentTablet;

	public WelcomeParentFragmentTablet() {
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
		mWelcomeFragment = (ViewGroup) mView
				.findViewById(R.id.frame_container_parent);
		showSubfragments(0, WelcomeFragment.class.getName());

		((HomeActivityTablet) getActivity()).hideBackButton();
		return mView;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((HomeActivityTablet) getActivity()).tvWelcome
				.setTextColor(getResources().getColor(R.color.textcolor_pink));
		((HomeActivityTablet) getActivity()).ivWelcomeHome
				.setImageResource(R.drawable.welcome_tab_selc);
	}

	public void showSubfragments(int position, String tagName) {
		Fragment fragment = null;
		mFragmentManager = this.getChildFragmentManager();
		FragmentTransaction fragtrans = mFragmentManager.beginTransaction();
		switch (position) {
		case 0:
			fragment = new co.uk.android.lldc.fragments.WelcomeFragment();
			break;

		}

		if (fragment != null) {
			fragtrans.replace(R.id.frame_container_parent, fragment,
					WelcomeFragment.class.getName());
			fragtrans.commit();
		}

	}

	@Override
	public void onAttach(Activity activity) {
		sWelcomeParentFragmentTablet = this;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		sWelcomeParentFragmentTablet = null;
		super.onDetach();
	}
}
