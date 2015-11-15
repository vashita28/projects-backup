package co.uk.android.lldc.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import co.uk.android.lldc.R;
import co.uk.android.lldc.images.ImageCacheManager;
import co.uk.android.lldc.models.EventMediaModel;
import co.uk.android.lldc.models.MediaModel;

import com.android.volley.toolbox.NetworkImageView;

@SuppressWarnings("deprecation")
public class GalleryAdapter extends BaseAdapter {
	private Context mContext;

	// array of integers for images IDs
	ArrayList<MediaModel> mediaList;

	ArrayList<EventMediaModel> eventMediaList;

	String pageTitle = "";

	// constructor
	public GalleryAdapter(Context c, ArrayList<MediaModel> mediaList,
			ArrayList<EventMediaModel> socialList, String pagetitle) {
		mContext = c;
		this.mediaList = mediaList;
		eventMediaList = socialList;
		pageTitle = pagetitle;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (pageTitle.equals("Media")) {
			return mediaList.size();
		} else {
			return eventMediaList.size();
		}
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
		NetworkImageView imageView = new NetworkImageView(mContext);
		imageView.setBackgroundColor(Color.BLACK);

		String imageUrl = "";

		if (pageTitle.equals("Media")) {
			imageUrl = mediaList.get(pos).getImageUrl();
		} else {
			imageUrl = eventMediaList.get(pos).getUrlBanner();
		}

		imageView.setLayoutParams(new Gallery.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
//		ImageLoader.getInstance().displayImage(imageUrl, imageView);
		imageView.setImageUrl(imageUrl, ImageCacheManager.getInstance().getImageLoader());
		imageView.setDefaultImageResId(R.drawable.qeop_placeholder);
		imageView.setErrorImageResId(R.drawable.imgnt_placeholder);
		imageView.setAdjustViewBounds(true);

		return imageView;
	}

}
