package co.uk.android.lldc.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.WrapperListAdapter;
import co.uk.android.lldc.LLDCApplication;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.GalleryAdapter;
import co.uk.android.lldc.adapters.MediaAsymmetricListAdapter;
import co.uk.android.lldc.custom.MyGallery;
import co.uk.android.lldc.models.EventMediaModel;
import co.uk.android.lldc.models.MediaModel;
import co.uk.android.lldc.models.MediaModelAsymm;
import co.uk.android.util.Util;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

public class VenueMediaFragment extends Fragment {
	private static final String TAG = VenueMediaFragment.class.getSimpleName();

	Context mContext;
	private AsymmetricGridView listView;
	private ListAdapter adapter;
	private int currentOffset = 0;
	private AsymmetricGridViewAdapter<MediaModelAsymm> asymmetricAdapter;
	ArrayList<MediaModel> mediaList;

	public static boolean bIsTablet = false;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = null;
		if (Util.getIsTabletFlag(getActivity())) {
			Log.e(TAG, "Device is Tablet...");
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			rootView = inflater.inflate(R.layout.fragment_venue_media_tablet,
					container, false);
			bIsTablet = true;
		} else {
			Log.e(TAG, "Device is Phone...");
			rootView = inflater.inflate(R.layout.fragment_venue_media,
					container, false);
			getActivity().setRequestedOrientation(
					ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			bIsTablet = false;
		}
		mContext = getActivity();

		listView = (AsymmetricGridView) rootView.findViewById(R.id.listView);

		mediaList = LLDCApplication.selectedModel.getMediaList();
		adapter = new MediaAsymmetricListAdapter(getActivity(), listView,
				new ArrayList<MediaModelAsymm>());

		if (adapter instanceof WrapperListAdapter)
			asymmetricAdapter = (AsymmetricGridViewAdapter) ((WrapperListAdapter) adapter)
					.getWrappedAdapter();
		else
			asymmetricAdapter = (AsymmetricGridViewAdapter<MediaModelAsymm>) adapter;
		asymmetricAdapter
				.appendItems(getMoreItems(LLDCApplication.selectedModel
						.getMediaList().size()));

		listView.setRequestedColumnCount(3);
		listView.setRequestedHorizontalSpacing(Utils.dpToPx(getActivity(), 5));
		listView.setAdapter(adapter);
		listView.setDebugging(true);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unused")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(mContext);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_media);
				RelativeLayout rlClose = (RelativeLayout) dialog
						.findViewById(R.id.rlClose);
				ImageView ivBanner = (ImageView) dialog
						.findViewById(R.id.ivBanner);

				final ImageView ivLeft = (ImageView) dialog
						.findViewById(R.id.ivLeft);
				final ImageView ivRight = (ImageView) dialog
						.findViewById(R.id.ivRight);

				ArrayList<EventMediaModel> socialList = new ArrayList<EventMediaModel>();

				final GalleryAdapter adapter = new GalleryAdapter(
						getActivity(), mediaList, socialList, "Media");

				final MyGallery gallery = (MyGallery) dialog
						.findViewById(R.id.mediaGallery);

				gallery.setAdapter(adapter);

				gallery.setSelection(position);

				gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						ivLeft.setVisibility(View.VISIBLE);
						ivRight.setVisibility(View.VISIBLE);
						int count = adapter.getCount();
						if (arg2 == count - 1) {
							ivRight.setVisibility(View.GONE);
							ivLeft.setVisibility(View.VISIBLE);
						} else if (arg2 == 0) {
							ivLeft.setVisibility(View.GONE);
							ivRight.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

				ivLeft.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						gallery.setSelection(gallery.getSelectedItemPosition() - 1);
						if (gallery.getSelectedItemPosition() == 0) {
							ivLeft.setVisibility(View.GONE);
							ivRight.setVisibility(View.VISIBLE);
						}
					}
				});

				ivRight.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						gallery.setSelection(gallery.getSelectedItemPosition() + 1);
						if (gallery.getSelectedItemPosition() == adapter
								.getCount()) {
							ivRight.setVisibility(View.GONE);
							ivLeft.setVisibility(View.VISIBLE);
						}
					}
				});

				rlClose.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				// dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
		});

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
