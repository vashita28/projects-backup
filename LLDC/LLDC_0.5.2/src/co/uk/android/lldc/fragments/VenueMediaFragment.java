package co.uk.android.lldc.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.WrapperListAdapter;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.MediaAsymmetricListAdapter;
import co.uk.android.lldc.models.EventMediaModel;
import co.uk.android.lldc.models.MediaModel;
import co.uk.android.lldc.models.MediaModelAsymm;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

public class VenueMediaFragment extends Fragment {

	Context mContext;
	private AsymmetricGridView listView;
//	private StaggeredGridView listView;
	private MediaAsymmetricListAdapter adapter;
	private int currentOffset = 0;
	private AsymmetricGridViewAdapter<MediaModelAsymm> asymmetricAdapter;
	ArrayList<MediaModel> mediaList;

	TextView tvnoMedia;

	@SuppressWarnings({"unchecked" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_venue_media,
				container, false);
		mContext = getActivity();

		listView = (AsymmetricGridView) rootView.findViewById(R.id.listView);

//		listView = (StaggeredGridView) rootView.findViewById(R.id.listView);
		
		tvnoMedia = (TextView) rootView.findViewById(R.id.tvnoMedia);

		mediaList = LLDCApplication.selectedModel.getMediaList();

		if (mediaList.size() == 0) {
			tvnoMedia.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		} else {
			tvnoMedia.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);

			adapter = new MediaAsymmetricListAdapter(getActivity(), listView,
					new ArrayList<MediaModelAsymm>());

//			adapter = new StaggardGridAdapter(getActivity(), LLDCApplication.selectedModel.getMediaList());
			
//			listView.setAdapter(adapter);
			
			if (adapter instanceof WrapperListAdapter)
				asymmetricAdapter = (AsymmetricGridViewAdapter<MediaModelAsymm>) ((WrapperListAdapter) adapter)
						.getWrappedAdapter();
			else
				asymmetricAdapter = (AsymmetricGridViewAdapter<MediaModelAsymm>) adapter;

			asymmetricAdapter
					.appendItems(getMoreItems(LLDCApplication.selectedModel
							.getMediaList().size()));

			listView.setRequestedColumnCount(3);
			listView.setRequestedHorizontalSpacing(LLDCApplication.dpToPx(5));
			listView.setAdapter(adapter);
			listView.setDebugging(true);
			listView.setAllowReordering(true);
			
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					try {
						ArrayList<EventMediaModel> socialList = new ArrayList<EventMediaModel>();

						MediaDialogFragment dialog = new MediaDialogFragment(
								socialList, mediaList, "Media", position);

						dialog.show(getFragmentManager(), "Dlg");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});
			
		}
		return rootView;
	}

	private List<MediaModelAsymm> getMoreItems(int qty) {
		final List<MediaModelAsymm> items = new ArrayList<MediaModelAsymm>();

		for (int i = 0; i < qty; i++) {
			int colSpan = Math.random() < 0.2f ? 2 : 1;
			// Swap the next 2 lines to have items with variable
			// column/row span.
			// int rowSpan = Math.random() < 0.2f ? 2 : 1;
			int rowSpan = colSpan;
			final MediaModelAsymm item = new MediaModelAsymm(colSpan, rowSpan,
					currentOffset + i);
			items.add(item);
		}

		currentOffset += qty;

		return items;
	}

}
