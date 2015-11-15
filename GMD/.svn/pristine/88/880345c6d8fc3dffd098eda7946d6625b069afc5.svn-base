package com.uk.pocketapp.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.uk.pocketapp.gmd.R;

import com.uk.pocketapp.common.Common;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FragmentTwo extends ListFragment {

	int mCurCheckPosition = 0;
	private List<String> mItem = null;
	private List<String> mPath = null;
	private File mFile;
	private String mRoot = "";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		mRoot = Common.sdPath;

		if (mRoot.equals("")) {
			Common.showToastMsg(getActivity(), "SD card is missing!");
		} else {

			getDir(mRoot);

			FragmentOne details = (FragmentOne) getFragmentManager()
					.findFragmentById(R.id.details);

			details = FragmentOne.newInstance(mRoot);

			// Execute a transaction, replacing any
			// existing fragment
			// with this one inside the frame.
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.details, details);
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();

		}

	}

	/** iterate external directory path */
	private void getDir(String dirPath) {

		try {

			mItem = new ArrayList<String>();
			mPath = new ArrayList<String>();

			File f = new File(dirPath);
			File[] files = f.listFiles();

			// Common.showLogCat(f.getAbsolutePath());

			if (!dirPath.equals(mRoot)) {

				mItem.add(mRoot);
				mPath.add(mRoot);

				mItem.add("../");
				mPath.add(f.getParent());

			}

			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				mPath.add(file.getPath());
				if (file.isDirectory())
					mItem.add(file.getName() + "/");
				else
					mItem.add(file.getName());
			}

			// Use a custom adapter so we can inflate files name that is
			// fetching from SD card
			setListAdapter(new CustomAdapter(getActivity(),
					R.layout.complex_list_item, R.id.imgicon, mItem));

		} catch (Exception e) {
			// Common.showLogCat(e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("curChoice", mCurCheckPosition);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		mFile = new File(mPath.get(position));

		if (mFile.isDirectory()) {
			if (mFile.canRead())
				getDir(mPath.get(position));
			else {
				new AlertDialog.Builder(getActivity())
						// .setIcon(R.drawable.icon)
						.setTitle(
								"[" + mFile.getName()
										+ "] folder can't be read!")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		} else {

			String abtpath = mFile.getAbsolutePath();
			File file = new File(abtpath);

			Common.dialogForOpenFile(getActivity(), file.getName(), file);

		}

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ListView listView = getListView();
		listView.setDivider(new ColorDrawable(Color.DKGRAY));
		listView.setBackgroundColor(Color.WHITE);
		listView.setDividerHeight(2);
	}

	/**
	 * CustomAdapter
	 */

	private class CustomAdapter extends ArrayAdapter<String> {

		private Context mContext;
		private int mLayoutId;

		/**
		 * Constructor
		 */

		public CustomAdapter(Context context, int layoutId,
				int textViewResourceId, List<String> items) {
			super(context, textViewResourceId, items);
			mContext = context;
			mLayoutId = layoutId;
		}

		/**
		 * getView Return a view that displays an item in the array.
		 */

		@SuppressLint("UseValueOf")
		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(mLayoutId, null);
			}

			View itemView = v;

			ImageView img = (ImageView) itemView.findViewById(R.id.imgicon);

			String text2 = getItem(position);
			TextView tv2 = (TextView) itemView.findViewById(R.id.text2);
			tv2.setTextColor(Color.BLACK);
			if (tv2 != null)
				tv2.setText(text2);

			if (text2.toString().contains(".doc")
					|| text2.toString().contains(".docx")) {
				// Word document
				img.setBackgroundResource(R.drawable.ticon);
			} else if (text2.toString().contains(".pdf")) {
				// PDF file
				img.setBackgroundResource(R.drawable.picon);
			} else if (text2.toString().contains(".ppt")
					|| text2.toString().contains(".pptx")) {
				// Powerpoint file
				img.setBackgroundResource(R.drawable.iicon);
			} else if (text2.toString().contains(".xls")
					|| text2.toString().contains(".xlsx")) {
				// Excel file
				img.setBackgroundResource(R.drawable.iicon);
			} else if (text2.toString().contains(".zip")
					|| text2.toString().contains(".rar")) {
				// WAV audio file
				img.setBackgroundResource(R.drawable.ticon);
			} else if (text2.toString().contains(".rtf")) {
				// RTF file
				img.setBackgroundResource(R.drawable.ticon);
			} else if (text2.toString().contains(".wav")
					|| text2.toString().contains(".mp3")) {
				// WAV audio file
				img.setBackgroundResource(R.drawable.vicon);
			} else if (text2.toString().contains(".gif")) {
				// GIF file
				img.setBackgroundResource(R.drawable.vicon);
			} else if (text2.toString().contains(".jpg")
					|| text2.toString().contains(".jpeg")
					|| text2.toString().contains(".png")) {
				// JPG file
				img.setBackgroundResource(R.drawable.iicon);
			} else if (text2.toString().contains(".txt")) {
				// Text file
				img.setBackgroundResource(R.drawable.ticon);
			} else if (text2.toString().contains(".3gp")
					|| text2.toString().contains(".mpg")
					|| text2.toString().contains(".mpeg")
					|| text2.toString().contains(".mpe")
					|| text2.toString().contains(".mp4")
					|| text2.toString().contains(".avi")) {
				// Video files
				img.setBackgroundResource(R.drawable.vicon);
			} else if (text2.toString().contains(".flv")) {
				img.setBackgroundResource(R.drawable.vicon);
			}

			return itemView;

		}

	} // end class CustomAdapter

} // end class TitlesFragment
