package co.uk.peeki.fragments;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

import co.uk.peeki.R;
import co.uk.peeki.adapter.ImageAdapterForBlocked;
import co.uk.peeki.adapter.ImageAdapterForSelection;
import co.uk.peeki.db.MySqLiteHelper;
import co.uk.peeki.interfaces.FragmentLoad;
import co.uk.peeki.ui.DataModelPhoto;
import co.uk.peeki.ui.MainActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

public class BlockImagesFragment extends Fragment {
	private static final String TAG = "BLOCK IMAGES FRAGMENT";

	GridView gridViewBlockedFragment;
	ImageView imageViewBlock;
	ImageAdapterForBlocked adapter;
	static FragmentLoad mFragmentLoad;
	int countBlockedImagesFragment = 0;
	MySqLiteHelper db = new MySqLiteHelper(MainActivity.mContext);

	public static ArrayList<String> listOfBlockedImages = new ArrayList<String>();
	public static ArrayList<String> listOfUnblockedImages = new ArrayList<String>();

	public static boolean bIsSelectAllInBlocked = false;

	public BlockImagesFragment() {
		// TODO Auto-generated constructor stub
	}

	public static Fragment newInstance(Context context,
			FragmentLoad mfragmentLoadBlocked) {
		BlockImagesFragment f = new BlockImagesFragment();
		mFragmentLoad = mfragmentLoadBlocked;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.block_images_fragment, null, true);
		bIsSelectAllInBlocked = false;

		gridViewBlockedFragment = (GridView) root
				.findViewById(R.id.gridViewPhotoGalleryBlocked);
		imageViewBlock = (ImageView) root.findViewById(R.id.ivBlock);

		gridViewBlockedFragment.setOnScrollListener(new PauseOnScrollListener(
				ImageLoader.getInstance(), true, true));

		listOfBlockedImages.clear();
		listOfUnblockedImages.clear();
		// Adding already blocked images from db::
		MySqLiteHelper db = new MySqLiteHelper(MainActivity.mContext);
		if (db.getAllBlockedPhotos().size() > 0) {
			listOfBlockedImages.addAll(db.getAllBlockedPhotos());
		}

		// Adding selected images from selection screen
		for (int i = 0; i < SelectionFragment.listOfSelectedImages.size(); i++) {
			if (!listOfBlockedImages
					.contains(SelectionFragment.listOfSelectedImages.get(i)))
				listOfBlockedImages.add(SelectionFragment.listOfSelectedImages
						.get(i));
		}

		if (listOfBlockedImages.size() == SelectionFragment.listOfTotalImages
				.size()) {
			bIsSelectAllInBlocked = true;
		}

		imageViewBlock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!bIsSelectAllInBlocked) {
					bIsSelectAllInBlocked = true;
					listOfBlockedImages.clear();
					listOfUnblockedImages.clear();
					listOfBlockedImages
							.addAll(SelectionFragment.listOfTotalImages);
					ImageAdapterForBlocked
							.blockCount(SelectionFragment.listOfTotalImages
									.size());
					if (adapter != null)
						adapter.notifyDataSetChanged();
				} else {
					bIsSelectAllInBlocked = false;
					if (listOfBlockedImages.size() == SelectionFragment.listOfTotalImages
							.size()) {
						listOfUnblockedImages.addAll(listOfBlockedImages);
					}
					listOfBlockedImages.clear();
					ImageAdapterForBlocked.blockCount(0);
					if (adapter != null)
						adapter.notifyDataSetChanged();
				}
			}

		});

		setDataInAdapter();

		if (mFragmentLoad == null) {
			mFragmentLoad = MainActivity.mfragmentLoad;
		}

		return root;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mFragmentLoad.onBlockImagesFragmentLoad(this,
				countBlockedImagesFragment);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.e("***********", "BlockImages-onDestroyView()");
		mFragmentLoad.onBlockImagesFragmentDestroyed();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("***********", "BlockImages-onDestroy()");
	}

	public void setDataInAdapter() {
		Log.e("BlockFragment", "setDataInAdapter");
		int count = listOfBlockedImages.size();

		ArrayList<String> totalList = db.getTotalPhotos();
		if (totalList != null && totalList.size() > 0) {
			if (adapter != null) {
				adapter.updateData(totalList);
				adapter.notifyDataSetChanged();
			} else {
				adapter = new ImageAdapterForBlocked(MainActivity.mContext,
						this, totalList, count);
				gridViewBlockedFragment.setAdapter(adapter);
			}
		}
		// if (SelectionFragment.listOfTotalImages.size() > 0
		// && SelectionFragment.listOfTotalImages != null) {
		// if (adapter != null) {
		// adapter.updateData(SelectionFragment.listOfTotalImages);
		// adapter.notifyDataSetChanged();
		// } else {
		// adapter = new ImageAdapterForBlocked(MainActivity.mContext,
		// this, SelectionFragment.listOfTotalImages, count);
		// gridViewBlockedFragment.setAdapter(adapter);
		// }
		// }
	}

	public void onBlockImagesFragmentLoad(int countBlockedImages) {
		countBlockedImagesFragment = countBlockedImages;
		mFragmentLoad.onBlockImagesFragmentLoad(this, countBlockedImages);
	}

}
