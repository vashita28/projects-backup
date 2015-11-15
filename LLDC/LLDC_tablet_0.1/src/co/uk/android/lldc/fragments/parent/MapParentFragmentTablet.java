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

public class MapParentFragmentTablet extends Fragment {

	private ViewGroup mMapFragment;
	private View mView;
	private FragmentManager mFragmentManager;

	public static MapParentFragmentTablet sMapParentFragmentTablet;

	public MapParentFragmentTablet() {
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
		mMapFragment = (ViewGroup) mView
				.findViewById(R.id.frame_container_parent);
		showSubfragments(0, "MapFragment");

		((HomeActivityTablet) getActivity()).hideBackButton();
		return mView;

	}

	public void showSubfragments(int position, String tagName) {
		Fragment fragment = null;
		mFragmentManager = this.getChildFragmentManager();
		FragmentTransaction fragtrans = mFragmentManager.beginTransaction();
		switch (position) {
		case 0:
			fragment = new co.uk.android.lldc.fragments.MapFragment(tagName,
					tagName);
			break;

		}

		if (fragment != null) {
			fragtrans.replace(R.id.frame_container_parent, fragment,
					MapParentFragmentTablet.class.getName());
			fragtrans.commit();
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		((HomeActivityTablet) getActivity()).tvMap.setTextColor(getResources()
				.getColor(R.color.textcolor_pink));
		((HomeActivityTablet) getActivity()).ivMap
				.setImageResource(R.drawable.map_tab_selc);
	}

	@Override
	public void onAttach(Activity activity) {
		sMapParentFragmentTablet = this;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		sMapParentFragmentTablet = null;
		super.onDetach();
	}
}
