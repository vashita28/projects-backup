package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import co.uk.android.lldc.R;
import co.uk.android.lldc.adapters.GalleryAdapter;
import co.uk.android.lldc.custom.MyGallery;
import co.uk.android.lldc.models.EventMediaModel;
import co.uk.android.lldc.models.MediaModel;

public class MediaDialogFragment extends DialogFragment {

	ArrayList<MediaModel> mediaList = new ArrayList<MediaModel>();
	ArrayList<EventMediaModel> evetnmediaList = new ArrayList<EventMediaModel>();

	String pageTitle = "";
	RelativeLayout rlClose;
	ImageView ivBanner, ivLeft, ivRight;
	GalleryAdapter adapter;
	MyGallery gallery;
	int position = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public MediaDialogFragment(ArrayList<EventMediaModel> eventMediaList,
			ArrayList<MediaModel> mediaList, String pagetTitle, int position) {
		// TODO Auto-generated constructor stub
		setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
		this.mediaList = mediaList;
		this.evetnmediaList = eventMediaList;
		this.pageTitle = pagetTitle;
		this.position = position;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.dialog_media, container,
				false);

		rlClose = (RelativeLayout) rootView.findViewById(R.id.rlClose);
		ivBanner = (ImageView) rootView.findViewById(R.id.ivBanner);

		ivLeft = (ImageView) rootView.findViewById(R.id.ivLeft);
		ivRight = (ImageView) rootView.findViewById(R.id.ivRight);

		gallery = (MyGallery) rootView.findViewById(R.id.mediaGallery);

		adapter = new GalleryAdapter(getActivity(), mediaList, evetnmediaList,
				pageTitle);

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
				if (gallery.getSelectedItemPosition() == adapter.getCount()) {
					ivRight.setVisibility(View.GONE);
					ivLeft.setVisibility(View.VISIBLE);
				}
			}
		});

		rlClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});

		// getDialog().setTitle("Media");
		// getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		return rootView;

	}

}
