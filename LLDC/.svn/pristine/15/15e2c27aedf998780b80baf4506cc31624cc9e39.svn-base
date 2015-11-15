package co.uk.android.lldc.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import co.uk.android.lldc.models.MediaModel;

import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressWarnings("deprecation")
public class StaggardGridAdapter extends BaseAdapter {
	private Context mContext;

	// array of integers for images IDs
	ArrayList<MediaModel> mediaList;


	// constructor
	public StaggardGridAdapter(Context c, ArrayList<MediaModel> mediaList) {
		mContext = c;
		this.mediaList = mediaList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
			return mediaList.size();
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
		imageView.setBackgroundColor(Color.BLACK);

		String imageUrl = "";

		imageUrl = mediaList.get(pos).getImageUrl();

		ImageLoader.getInstance().displayImage(imageUrl, imageView);

		imageView.setLayoutParams(new Gallery.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT));

		return imageView;
	}

}
