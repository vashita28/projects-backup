package com.example.cabapppassenger.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.example.cabapppassenger.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ViewTutorialAdapter extends BaseAdapter {
	private Context mContext;
	ImageLoader imgloader;
	DisplayImageOptions options;
	// array of integers for images IDs
	private Integer[] mImageIds = { R.drawable.thumbnail, R.drawable.thumbnail,
			R.drawable.thumbnail, R.drawable.thumbnail };

	// constructor
	public ViewTutorialAdapter(Context context) {
		mContext = context;
		imgloader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mImageIds.length;
	}

	@Override
	public Object getItem(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		ImageView imageView = new ImageView(mContext);
		imageView.setLayoutParams(new Gallery.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));
		imageView.setImageResource(mImageIds[pos]);

		return imageView;
	}
}
