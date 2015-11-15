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
import co.uk.android.lldc.tablet.HomeActivityTablet;
import co.uk.android.lldc.utils.Constants;

public class EventsParentFragmentTablet extends Fragment {

	private ViewGroup mEventsFragment;
	private View mView;
	private FragmentManager mFragmentManager;
	Bundle bundle = new Bundle();

	public static EventsParentFragmentTablet sEventsParentFragmentTablet;

	public EventsParentFragmentTablet() {
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
		mEventsFragment = (ViewGroup) mView
				.findViewById(R.id.frame_container_parent);
		showSubfragments(0, EventsParentFragmentTablet.class.getName());
		
		bundle=getArguments();

		((HomeActivityTablet) getActivity()).hideBackButton();
		return mView;

	}

	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((HomeActivityTablet) getActivity()).tvEvents
				.setTextColor(getResources().getColor(R.color.textcolor_pink));
		((HomeActivityTablet) getActivity()).ivEvents
				.setImageResource(R.drawable.events_tab_selc);
	}

	public void showSubfragments(int position, String tagName) {
		Fragment fragment = null;
		mFragmentManager = this.getChildFragmentManager();
		FragmentTransaction fragtrans = mFragmentManager.beginTransaction();
		switch (position) {
		case 0:
			fragment = new co.uk.android.lldc.fragments.EventsFragment();
			//bundle=new Bundle();
			bundle.putBoolean(Constants.IS_COMING_FROM_EXPLORE, false);
			bundle.putString(Constants.IS_FOR_TODAY_EVENT, "Events");
			fragment.setArguments(bundle);
			break;

		}

		if (fragment != null) {
			fragtrans.replace(R.id.frame_container_parent, fragment, tagName);
			fragtrans.commit();
		}

	}

	@Override
	public void onAttach(Activity activity) {
		sEventsParentFragmentTablet = this;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		sEventsParentFragmentTablet = null;
		super.onDetach();
	}

}
