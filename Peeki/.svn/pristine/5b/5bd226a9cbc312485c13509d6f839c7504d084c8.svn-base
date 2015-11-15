package co.uk.peeki.adapter;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import co.uk.peeki.R;

public class ViewTutorialAdapter extends BaseAdapter {
	private Context mContext;

	// array of integers for images IDs
	private Integer[] mImageIds = { R.drawable.tut1, R.drawable.tut2,
			R.drawable.tut3 };

	// constructor
	public ViewTutorialAdapter(Context c) {
		mContext = c;
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

	@Override
	public View getView(int pos, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		ImageView imageView = new ImageView(mContext);

		// imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		// imageView.setAdjustViewBounds(true);

		// ImageLoader.getInstance().displayImage("drawable://" +
		// mImageIds[pos],
		// imageView);

		imageView.setImageResource(mImageIds[pos]);
		imageView.setLayoutParams(new Gallery.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));

		return imageView;
	}

}
